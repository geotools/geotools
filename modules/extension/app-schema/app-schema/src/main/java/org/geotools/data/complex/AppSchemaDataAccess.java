/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */

package org.geotools.data.complex;

import static org.geotools.data.complex.util.ComplexFeatureConstants.DEFAULT_GEOMETRY_LOCAL_NAME;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import org.geotools.appschema.jdbc.JoiningJDBCFeatureSource;
import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.ServiceInfo;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.filter.UnmappingFilterVisitor;
import org.geotools.data.complex.filter.UnmappingFilterVisitorFactory;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.spi.CustomSourceDataStore;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.data.joining.JoiningQuery;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.filter.SortByImpl;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jdbc.JDBCFeatureSource;
import org.geotools.jdbc.JDBCFeatureStore;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.filter.sort.SortOrder;

/**
 * A {@link DataAccess} that maps a "simple" source {@link DataStore} into a source of full Feature
 * features conforming to an application schema.
 *
 * @author Gabriel Roldan (Axios Engineering)
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 * @since 2.4
 */
public class AppSchemaDataAccess implements DataAccess<FeatureType, Feature> {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AppSchemaDataAccess.class);

    private Map<Name, FeatureTypeMapping> mappings = new LinkedHashMap<Name, FeatureTypeMapping>();

    private FilterFactory2 filterFac = CommonFactoryFinder.getFilterFactory2(null);

    /**
     * Flag to mark non-accessible data accesses, which should be automatically disposed of when no
     * longer needed by any accessible data access.
     */
    boolean hidden = false;

    /**
     * Constructor.
     *
     * @param mappings a Set containing a {@linkplain FeatureTypeMapping} for each FeatureType this
     *     DataAccess is going to produce.
     */
    public AppSchemaDataAccess(Set<FeatureTypeMapping> mappings) throws IOException {
        this(mappings, false);
    }

    /**
     * Two args constructor.
     *
     * @param mappings a Set containing a {@linkplain FeatureTypeMapping} for each FeatureType this
     *     DataAccess is going to produce.
     * @param hidden marks this data access as non-accessible, which makes it a candidate for
     *     automatic disposal
     */
    public AppSchemaDataAccess(Set<FeatureTypeMapping> mappings, boolean hidden)
            throws IOException {
        this.hidden = hidden;
        try {
            for (FeatureTypeMapping mapping : mappings) {
                Name name = mapping.getMappingName();
                if (name == null) {
                    name = mapping.getTargetFeature().getName();
                }
                if (this.mappings.containsKey(name) || DataAccessRegistry.hasName(name)) {
                    // check both mappings and the registry, because the data access is
                    // only registered at the bottom of this constructor, so it might not
                    // be in the registry yet
                    throw new DataSourceException(
                            "Duplicate mappingName or targetElement across FeatureTypeMapping instances detected.\n"
                                    + "They have to be unique, or app-schema doesn't know which one to get.\n"
                                    + "Please check your mapping file(s) with mappingName or targetElement of: "
                                    + name);
                }
                this.mappings.put(name, mapping);
                // if the type is not a feature, it should be wrapped with
                // a fake feature type, so attributes can be chained/nested
                AttributeType type = mapping.getTargetFeature().getType();
                if (!(type instanceof FeatureType)) {
                    // nasty side-effect: constructor edits mapping to use this type proxy
                    new NonFeatureTypeProxy(type, mapping);
                }
            }
        } catch (RuntimeException e) {
            // dispose all source data stores in the input mappings
            for (FeatureTypeMapping mapping : mappings) {
                mapping.getSource().getDataStore().dispose();
            }
            throw e;
        }
        register();
    }

    /** Registers this data access to the registry so the mappings can be retrieved globally */
    protected void register() {
        DataAccessRegistry.register(this);
    }

    /**
     * Returns the set of target type names this DataAccess holds, where the term 'target type name'
     * refers to the name of one of the types this DataAccess produces by mapping another ones
     * through the definitions stored in its {@linkplain FeatureTypeMapping}s
     */
    public Name[] getTypeNames() throws IOException {
        Name[] typeNames = new Name[mappings.size()];
        int i = 0;
        for (FeatureTypeMapping mapping : mappings.values()) {
            typeNames[i] = mapping.getTargetFeature().getName();
            i++;
        }
        return typeNames;
    }

    /**
     * Finds the target FeatureType named <code>typeName</code> in this ComplexDatastore's internal
     * list of FeatureType mappings and returns it.
     */
    public FeatureType getSchema(Name typeName) throws IOException {
        return (FeatureType) getMappingByNameOrElement(typeName).getTargetFeature().getType();
    }

    /**
     * Returns the mapping suite for the given targetElement name or mappingName.
     *
     * <p>Note this method is public just for unit testing purposes
     */
    public FeatureTypeMapping getMappingByName(Name typeName) throws IOException {
        FeatureTypeMapping mapping = (FeatureTypeMapping) this.mappings.get(typeName);
        if (mapping == null) {
            throw new DataSourceException(
                    typeName + " not found. Available: " + mappings.keySet().toString());
        }
        return mapping;
    }

    /**
     * Returns the mapping suite for the given target type name. This name would be the mappingName
     * in the TypeMapping if it exists, otherwise it's the target element name.
     *
     * <p>Note this method is public just for unit testing purposes
     */
    public FeatureTypeMapping getMappingByNameOrElement(Name typeName) throws IOException {
        FeatureTypeMapping mapping = (FeatureTypeMapping) this.mappings.get(typeName);
        if (mapping != null) {
            return mapping;
        }

        // lookup by mapping name failed, try to lookup by target element
        // NOTE: in this case, there is a risk of ambiguity. E.g. consider a (questionable) mapping
        // configuration where multiple mappings of the
        // same type have been specified, each with its own mappingName: they would have the same
        // targetElement, and we wouldn't know which one to
        // pick. The result would be unpredictable in such a situation.
        for (FeatureTypeMapping typeMapping : mappings.values()) {
            if (typeMapping.getTargetFeature().getName().equals(typeName)) {
                return typeMapping;
            }
        }
        throw new DataSourceException(
                typeName + " not found. Available: " + mappings.keySet().toString());
    }

    /**
     * @param name mappingName or targetElement
     * @return true if this data access contains mapping with for provided name
     */
    public boolean hasName(Name name) {
        return this.mappings.containsKey(name);
    }

    /**
     * @param typeName targetElement name
     * @return true if this data access contains mapping for provided targetElement name
     */
    public boolean hasElement(Name typeName) {
        for (FeatureTypeMapping mapping : mappings.values()) {
            if (mapping.getTargetFeature().getName().equals(typeName)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Computes the bounds of the features for the specified feature type that satisfy the query
     * provided that there is a fast way to get that result.
     *
     * <p>Will return null if there is not fast way to compute the bounds. Since it's based on some
     * kind of header/cached information, it's not guaranteed to be real bound of the features
     *
     * @return the bounds, or null if too expensive
     */
    protected ReferencedEnvelope getBounds(Query query) throws IOException {
        FeatureTypeMapping mapping = getMappingByNameOrElement(getName(query));
        Query unmappedQuery = unrollQuery(query, mapping);
        return mapping.getSource().getBounds(unmappedQuery);
    }

    /**
     * Gets the number of the features that would be returned by this query for the specified
     * feature type.
     *
     * <p>If getBounds(Query) returns <code>-1</code> due to expense consider using <code>
     * getFeatures(Query).getCount()</code> as a an alternative.
     *
     * @param targetQuery Contains the Filter and MaxFeatures to find the bounds for.
     * @return The number of Features provided by the Query or <code>-1</code> if count is too
     *     expensive to calculate or any errors or occur.
     * @throws IOException if there are errors getting the count
     */
    protected int getCount(final Query targetQuery) throws IOException {
        final FeatureTypeMapping mapping = getMappingByNameOrElement(getName(targetQuery));
        FeatureSource mappedSource = mapping.getSource();
        // Wrap with JoiningJDBCFeatureSource like in DataAccessMappingFeatureIterator
        // this is so it'd use the splitFilter in JoiningJDBCFeatureSource
        // otherwise you'll get an error when it can't find complex attributes in the
        // simple feature source
        if (mappedSource instanceof JDBCFeatureSource) {
            mappedSource = new JoiningJDBCFeatureSource((JDBCFeatureSource) mappedSource);
        } else if (mappedSource instanceof JDBCFeatureStore) {
            mappedSource = new JoiningJDBCFeatureSource((JDBCFeatureStore) mappedSource);
        }
        Query unmappedQuery = unrollQuery(targetQuery, mapping);
        return mappedSource.getCount(unmappedQuery);
    }

    /**
     * Return the name of the type that is queried.
     *
     * @return Name constructed from the query.
     */
    private Name getName(Query query) {
        if (query.getNamespace() == null) {
            return Types.typeName(query.getTypeName());
        } else {
            return Types.typeName(query.getNamespace().toString(), query.getTypeName());
        }
    }

    /**
     * Returns <code>Filter.INCLUDE</code>, as the whole filter is unrolled and passed back to the
     * underlying DataStore to be treated.
     *
     * @return <code>Filter.INLCUDE</code>
     */
    protected Filter getUnsupportedFilter(String typeName, Filter filter) {
        return Filter.INCLUDE;
    }

    /**
     * Creates a <code>org.geotools.data.Query</code> that operates over the surrogate DataStore, by
     * unrolling the <code>org.geotools.filter.Filter</code> contained in the passed <code>query
     * </code>, and replacing the list of required attributes by the ones of the mapped FeatureType.
     */
    @SuppressWarnings("unchecked")
    public Query unrollQuery(Query query, FeatureTypeMapping mapping) {
        Query unrolledQuery = Query.ALL;
        FeatureSource source = mapping.getSource();

        if (!Query.ALL.equals(query)) {
            Filter complexFilter = query.getFilter();
            Filter unrolledFilter = AppSchemaDataAccess.unrollFilter(complexFilter, mapping);

            Object includeProps = query.getHints().get(Query.INCLUDE_MANDATORY_PROPS);
            List<PropertyName> propNames =
                    getSurrogatePropertyNames(
                            query.getProperties(),
                            mapping,
                            includeProps instanceof Boolean
                                    && ((Boolean) includeProps).booleanValue());

            Query newQuery = new Query();
            String name = source.getName().getLocalPart();
            newQuery.setTypeName(name);
            newQuery.setFilter(unrolledFilter);
            newQuery.setProperties(propNames);
            newQuery.setCoordinateSystem(query.getCoordinateSystem());
            newQuery.setCoordinateSystemReproject(query.getCoordinateSystemReproject());
            newQuery.setHandle(query.getHandle());
            newQuery.setMaxFeatures(query.getMaxFeatures());
            newQuery.setStartIndex(query.getStartIndex());

            List<SortBy> sort = new ArrayList<SortBy>();
            if (query.getSortBy() != null) {
                for (SortBy sortBy : query.getSortBy()) {
                    List<Expression> expressions =
                            unrollProperty(sortBy.getPropertyName(), mapping);
                    for (Expression expr : expressions) {
                        if (expr != null) {
                            FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                            expr.accept(extractor, null);

                            for (String att : extractor.getAttributeNameSet()) {
                                sort.add(
                                        new SortByImpl(
                                                filterFac.property(att), sortBy.getSortOrder()));
                            }
                        }
                    }
                }
            }

            if (query instanceof JoiningQuery) {
                FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                mapping.getFeatureIdExpression().accept(extractor, null);

                if (!Expression.NIL.equals(mapping.getFeatureIdExpression())
                        && !(mapping.getFeatureIdExpression() instanceof Literal)
                        && extractor.getAttributeNameSet().isEmpty()) {
                    // GEOS-5618: getID() and functions in idExpression aren't supported with
                    // joining
                    String ns =
                            mapping.namespaces.getPrefix(
                                    mapping.getTargetFeature().getName().getNamespaceURI());
                    String separator = mapping.getTargetFeature().getName().getSeparator();
                    String typeName = mapping.getTargetFeature().getLocalName();
                    throw new UnsupportedOperationException(
                            String.format(
                                    "idExpression '%s' for targetElement '%s%s%s' cannot be translated into SQL, "
                                            + "therefore is not supported with joining!"
                                            + "\nPlease make sure idExpression is mapped into existing database fields, "
                                            + "and only use functions that are supported by your database."
                                            + "\nIf this cannot be helped, you can turn off joining in app-schema.properties file.",
                                    mapping.getFeatureIdExpression(), ns, separator, typeName));
                }

                JoiningQuery jQuery = new JoiningQuery(newQuery);
                jQuery.setDenormalised(((JoiningQuery) query).isDenormalised());
                jQuery.setQueryJoins(((JoiningQuery) query).getQueryJoins());
                jQuery.setSubset(((JoiningQuery) query).isSubset());

                for (String att : extractor.getAttributeNameSet()) {
                    sort.add(new SortByImpl(filterFac.property(att), SortOrder.ASCENDING));
                    jQuery.addId(att);
                }

                unrolledQuery = jQuery;
            } else {
                unrolledQuery = newQuery;
            }

            unrolledQuery.setSortBy(sort.toArray(new SortBy[sort.size()]));
        }

        return unrolledQuery;
    }

    /**
     * Helper method for getSurrogatePropertyNames to match a requested x-path property with a
     * target x-path
     *
     * @param requestedProperty requested property x-path
     * @param target target x-path steps
     * @return whether they match, i.e. when one of them is completely contained in the other
     */
    public static boolean matchProperty(StepList requestedProperty, StepList target) {
        // NC - include all parent and children paths of the requested property
        // i.e.: requested "measurement", found mapping of "measurement/result".
        // "result" must be included to create "measurement"
        // in other cases, requested property is a nested x-path,
        // so get all mappings that could be needed
        // i.e.: requested "measurement/result", found mapping of "measurement".
        // "measurement" must be included to create "result"

        int minSize = Math.min(requestedProperty.size(), target.size());

        for (int i = 0; i < minSize; i++) {
            if (!target.get(i).getName().equals(requestedProperty.get(i).getName())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Helper method for getSurrogatePropertyNames to match a requested single step property with a
     * target x-path, ignoring namespaces
     *
     * @param requestedProperty requested property x-path
     * @param target target x-path steps
     * @return whether they match, i.e. when one of them is completely contained in the other
     */
    public static boolean matchProperty(String requestedProperty, StepList target) {
        // requested Properties are top level nodes, so get all mappings inside node
        return target.get(0).getName().getLocalPart().equals(requestedProperty);
    }

    /**
     * @return <code>null</code> if all surrogate attributes shall be queried, else the list of
     *     needed surrogate attributes to satisfy the mapping of prorperties in <code>
     *     mappingProperties</code>
     */
    private List<PropertyName> getSurrogatePropertyNames(
            List<PropertyName> requestedProperties,
            FeatureTypeMapping mapping,
            boolean includeMandatory) {
        List<PropertyName> propNames = new ArrayList<>();
        final AttributeDescriptor targetDescriptor = mapping.getTargetFeature();
        if (requestedProperties != null && requestedProperties.size() > 0) {
            requestedProperties = new ArrayList<PropertyName>(requestedProperties);
            Set<PropertyName> requestedSurrogateProperties = new HashSet<PropertyName>();
            // extension point allowing stores to contribute properties
            for (CustomSourceDataStore extension : CustomSourceDataStore.loadExtensions()) {
                // ask the extension for surrogate properties
                List<PropertyName> contributedProperties =
                        extension.getSurrogatePropertyNames(requestedProperties, mapping);
                if (contributedProperties != null) {
                    // we got some surrogate properties, let's store them
                    propNames.addAll(contributedProperties);
                }
            }
            // add all surrogate attributes involved in mapping of the requested
            // target schema attributes
            List<AttributeMapping> attMappings = mapping.getAttributeMappings();
            // NC - add feature to list, to include its ID expression
            requestedProperties.add(filterFac.property(mapping.getTargetFeature().getName()));

            // get source type
            AttributeType mappedType;
            try {
                mappedType = mapping.getSource().getSchema();
            } catch (UnsupportedOperationException e) {
                // web service backend doesn't support getSchema()
                mappedType = null;
            }

            for (final AttributeMapping entry : attMappings) {
                final StepList targetSteps = entry.getTargetXPath();

                boolean addThis = false;

                if (includeMandatory) {
                    PropertyName targetProp =
                            filterFac.property(targetSteps.toString(), mapping.getNamespaces());
                    Object descr = targetProp.evaluate(targetDescriptor.getType());
                    if (descr instanceof PropertyDescriptor) {
                        if (((PropertyDescriptor) descr).getMinOccurs() >= 1) {
                            addThis = true;
                        }
                    }
                }

                if (!addThis) {
                    for (PropertyName requestedProperty : requestedProperties) {
                        // replace the artificial DEFAULT_GEOMETRY property with the actual one
                        if (DEFAULT_GEOMETRY_LOCAL_NAME.equals(
                                requestedProperty.getPropertyName())) {
                            String defGeomPath = mapping.getDefaultGeometryXPath();
                            requestedProperty =
                                    filterFac.property(defGeomPath, mapping.getNamespaces());
                        }

                        StepList requestedPropertySteps;
                        if (requestedProperty.getNamespaceContext() == null) {
                            requestedPropertySteps =
                                    XPath.steps(
                                            targetDescriptor,
                                            requestedProperty.getPropertyName(),
                                            mapping.getNamespaces());
                        } else {
                            requestedPropertySteps =
                                    XPath.steps(
                                            targetDescriptor,
                                            requestedProperty.getPropertyName(),
                                            requestedProperty.getNamespaceContext());
                        }

                        if (requestedPropertySteps == null
                                ? matchProperty(requestedProperty.getPropertyName(), targetSteps)
                                : matchProperty(requestedPropertySteps, targetSteps)) {
                            addThis = true;
                            break;
                        }
                    }
                }

                if (addThis) {
                    final Expression sourceExpression = entry.getSourceExpression();
                    final Expression idExpression = entry.getIdentifierExpression();
                    // NC - include client properties
                    final Collection<Expression> clientProperties =
                            entry.getClientProperties().values();

                    FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                    sourceExpression.accept(extractor, null);
                    idExpression.accept(extractor, null);

                    // RA - include function parameters in linkField
                    if (entry instanceof NestedAttributeMapping) {
                        final Expression linkFieldExpression =
                                ((NestedAttributeMapping) entry).nestedFeatureType;
                        linkFieldExpression.accept(extractor, null);
                    }

                    Iterator<Expression> it = clientProperties.iterator();
                    while (it.hasNext()) {
                        it.next().accept(extractor, null);
                    }
                    Set<String> exprAtts = extractor.getAttributeNameSet();

                    for (String mappedAtt : exprAtts) {
                        if (!mappedAtt.equals("Expression.NIL")) { // NC - ignore Nil Expression
                            if (mappedType == null) {
                                // web service backend.. no underlying simple feature
                                // so just assume that it exists..
                                requestedSurrogateProperties.add(filterFac.property(mappedAtt));
                            } else {
                                PropertyName propExpr = filterFac.property(mappedAtt);
                                Object object = propExpr.evaluate(mappedType);
                                AttributeDescriptor mappedAttribute = (AttributeDescriptor) object;
                                if (mappedAttribute != null) {
                                    requestedSurrogateProperties.add(filterFac.property(mappedAtt));
                                } else {
                                    LOGGER.info(
                                            "mapped type does not contains property " + mappedAtt);
                                }
                            }
                        }
                    }
                    LOGGER.fine("adding atts needed for : " + exprAtts);
                }
            }

            propNames.addAll(requestedSurrogateProperties);
        }
        // App-Schema business code expects a NULL if no properties
        return propNames.isEmpty() ? null : propNames;
    }

    private List<Expression> unrollProperty(
            PropertyName property, final FeatureTypeMapping mapping) {

        final AttributeDescriptor targetDescriptor = mapping.getTargetFeature();
        StepList propertySteps =
                XPath.steps(targetDescriptor, property.getPropertyName(), mapping.getNamespaces());

        return mapping.findMappingsFor(propertySteps, true);
    }

    /**
     * Takes a filter that operates against a {@linkplain FeatureTypeMapping}'s target FeatureType,
     * and unrolls it creating a new Filter that operates against the mapping's source FeatureType.
     *
     * @return TODO: implement filter unrolling
     */
    public static Filter unrollFilter(Filter complexFilter, FeatureTypeMapping mapping) {
        UnmappingFilterVisitor visitor = UnmappingFilterVisitorFactory.getInstance(mapping);
        Filter unrolledFilter = (Filter) complexFilter.accept(visitor, null);
        return unrolledFilter;
    }

    public void dispose() {
        DataAccessRegistry.unregister(this);
        // dispose all the source data stores
        for (FeatureTypeMapping mapping : mappings.values()) {
            mapping.getSource().getDataStore().dispose();
        }
        mappings.clear();
    }

    /**
     * Not a supported operation.
     *
     * @see org.geotools.data.DataAccess#getInfo()
     */
    public ServiceInfo getInfo() {
        throw new UnsupportedOperationException();
    }

    /**
     * Return the names of the target features.
     *
     * @see org.geotools.data.DataAccess#getNames()
     */
    public List<Name> getNames() {
        List<Name> names = new LinkedList<Name>();
        names.addAll(mappings.keySet());
        return names;
    }

    /**
     * Not a supported operation.
     *
     * @see org.geotools.data.DataAccess#createSchema(org.opengis.feature.type.FeatureType)
     */
    public void createSchema(FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a feature source that can be used to obtain features of a particular type.
     *
     * @see org.geotools.data.DataAccess#getFeatureSource(org.opengis.feature.type.Name)
     */
    public FeatureSource<FeatureType, Feature> getFeatureSource(Name typeName) throws IOException {
        return new MappingFeatureSource(this, getMappingByNameOrElement(typeName));
    }

    /**
     * Not a supported operation.
     *
     * @see org.geotools.data.DataAccess#updateSchema(org.opengis.feature.type.Name,
     *     org.opengis.feature.type.FeatureType)
     */
    public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Not a supported operation.
     *
     * @see org.geotools.data.DataAccess#removeSchema(org.opengis.feature.type.Name)
     */
    public void removeSchema(Name typeName) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a feature source that can be used to obtain features of a particular name. This name
     * would be the mappingName in the TypeMapping if it exists, otherwise it's the target element
     * name.
     *
     * @param typeName mappingName or targetElement
     * @return Mapping feature source
     */
    public FeatureSource<FeatureType, Feature> getFeatureSourceByName(Name typeName)
            throws IOException {
        return new MappingFeatureSource(this, getMappingByName(typeName));
    }

    public Feature findFeature(FeatureId id, Hints hints) throws IOException {
        for (Entry<Name, FeatureTypeMapping> mapping : mappings.entrySet()) {
            if (Thread.currentThread().isInterrupted()) {
                return null;
            }
            Filter filter = filterFac.id(id);
            FeatureCollection<FeatureType, Feature> fCollection =
                    new MappingFeatureSource(this, mapping.getValue()).getFeatures(filter, hints);
            FeatureIterator<Feature> iterator = fCollection.features();
            try {
                if (iterator.hasNext()) {
                    return iterator.next();
                }
            } finally {
                iterator.close();
            }
        }
        return null;
    }
}
