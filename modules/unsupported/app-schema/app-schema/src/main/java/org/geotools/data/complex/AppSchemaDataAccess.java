/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.DataStore;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.ServiceInfo;
import org.geotools.data.complex.config.NonFeatureTypeProxy;
import org.geotools.data.complex.filter.UnmappingFilterVisitor;
import org.geotools.data.complex.filter.UnmappingFilterVistorFactory;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.Types;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A {@link DataAccess} that maps a "simple" source {@link DataStore} into a source of full Feature
 * features conforming to an application schema.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @author Rini Angreani, Curtin University of Technology
 * @version $Id$
 * @source $URL$
 * @since 2.4
 */
public class AppSchemaDataAccess implements DataAccess<FeatureType, Feature> {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AppSchemaDataAccess.class.getPackage().getName());

    private Map<Name, FeatureTypeMapping> mappings = new LinkedHashMap<Name, FeatureTypeMapping>();

    private FilterFactory filterFac = CommonFactoryFinder.getFilterFactory(null);

    /**
     * Constructor.
     * 
     * @param mappings
     *            a Set containing a {@linkplain FeatureTypeMapping} for each FeatureType this
     *            DataAccess is going to produce.
     * @throws IOException
     */
    public AppSchemaDataAccess(Set<FeatureTypeMapping> mappings) throws IOException {
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
                    new NonFeatureTypeProxy((ComplexType) type, mapping);
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

    /**
     * Registers this data access to the registry so the mappings can be retrieved globally
     */
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
        return (FeatureType) getMappingByElement(typeName).getTargetFeature().getType();
    }

    /**
     * Returns the mapping suite for the given targetElement name or mappingName.
     * 
     * <p>
     * Note this method is public just for unit testing purposes
     * </p>
     * 
     * @param typeName
     * @return
     * @throws IOException
     */
    public FeatureTypeMapping getMappingByName(Name typeName) throws IOException {
        FeatureTypeMapping mapping = (FeatureTypeMapping) this.mappings.get(typeName);
        if (mapping == null) {
            throw new DataSourceException(typeName + " not found. Available: "
                    + mappings.keySet().toString());
        }
        return mapping;
    }

    /**
     * Returns the mapping suite for the given target type name.
     * 
     * <p>
     * Note this method is public just for unit testing purposes
     * </p>
     * 
     * @param typeName
     * @return
     * @throws IOException
     */
    public FeatureTypeMapping getMappingByElement(Name typeName) throws IOException {
        for (FeatureTypeMapping mapping : mappings.values()) {
            if (mapping.getTargetFeature().getName().equals(typeName)) {
                return mapping;
            }
        }
        ArrayList<String> availables = new ArrayList<String>();
        for (FeatureTypeMapping mapping : mappings.values()) {
            availables.add(mapping.getTargetFeature().getName().toString());
        }
        throw new DataSourceException(typeName + " not found. Available: " + availables.toString());
    }

    /**
     * @param name
     *            mappingName or targetElement
     * @return true if this data access contains mapping with for provided name
     */
    public boolean hasName(Name name) {
        return this.mappings.containsKey(name);
    }

    /**
     * @param typeName
     *            targetElement name
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
     * <p>
     * Will return null if there is not fast way to compute the bounds. Since it's based on some
     * kind of header/cached information, it's not guaranteed to be real bound of the features
     * </p>
     * 
     * @param query
     * @return the bounds, or null if too expensive
     * @throws SchemaNotFoundException
     * @throws IOException
     */
    protected ReferencedEnvelope getBounds(Query query) throws IOException {
        FeatureTypeMapping mapping = getMappingByElement(getName(query));
        Query unmappedQuery = unrollQuery(query, mapping);
        return mapping.getSource().getBounds(unmappedQuery);
    }

    /**
     * Gets the number of the features that would be returned by this query for the specified
     * feature type.
     * <p>
     * If getBounds(Query) returns <code>-1</code> due to expense consider using
     * <code>getFeatures(Query).getCount()</code> as a an alternative.
     * </p>
     * 
     * @param targetQuery
     *            Contains the Filter and MaxFeatures to find the bounds for.
     * @return The number of Features provided by the Query or <code>-1</code> if count is too
     *         expensive to calculate or any errors or occur.
     * @throws IOException
     * 
     * @throws IOException
     *             if there are errors getting the count
     */
    protected int getCount(final Query targetQuery) throws IOException {
        final FeatureTypeMapping mapping = getMappingByElement(getName(targetQuery));
        final FeatureSource<FeatureType, Feature> mappedSource = mapping.getSource();
        Query unmappedQuery = unrollQuery(targetQuery, mapping);
        ((DefaultQuery) unmappedQuery).setMaxFeatures(targetQuery.getMaxFeatures());
        return mappedSource.getCount(unmappedQuery);
    }

    /**
     * Return the name of the type that is queried.
     * 
     * @param query
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
     * unrolling the <code>org.geotools.filter.Filter</code> contained in the passed
     * <code>query</code>, and replacing the list of required attributes by the ones of the mapped
     * FeatureType.
     * 
     * @param query
     * @param mapping
     * @return
     */
    @SuppressWarnings("unchecked")
    public Query unrollQuery(Query query, FeatureTypeMapping mapping) {
        Query unrolledQuery = Query.ALL;
        FeatureSource<FeatureType, Feature> source = mapping.getSource();

        if (!Query.ALL.equals(query)) {
            Filter complexFilter = query.getFilter();
            Filter unrolledFilter = AppSchemaDataAccess.unrollFilter(complexFilter, mapping);

            List propNames = getSurrogatePropertyNames(query.getPropertyNames(), mapping);

            DefaultQuery newQuery = new DefaultQuery();

            String name = source.getName().getLocalPart();
            newQuery.setTypeName(name);
            newQuery.setFilter(unrolledFilter);
            newQuery.setPropertyNames(propNames);
            newQuery.setCoordinateSystem(query.getCoordinateSystem());
            newQuery.setCoordinateSystemReproject(query.getCoordinateSystemReproject());
            newQuery.setHandle(query.getHandle());
            newQuery.setMaxFeatures(query.getMaxFeatures());

            unrolledQuery = newQuery;
        }
        return unrolledQuery;
    }

    /**
     * 
     * @param mappingProperties
     * @param mapping
     * @return <code>null</code> if all surrogate attributes shall be queried, else the list of
     *         needed surrogate attributes to satisfy the mapping of prorperties in
     *         <code>mappingProperties</code>
     */
    private List<String> getSurrogatePropertyNames(String[] mappingProperties,
            FeatureTypeMapping mapping) {
        List<String> propNames = null;
        //NC - get source type
        final AttributeType mappedType = mapping.getSource().getSchema();
        if (mappingProperties != null && mappingProperties.length > 0) {
            Set<String> requestedSurrogateProperties = new HashSet<String>();
            // add all surrogate attributes involved in mapping of the requested
            // target schema attributes
            List<AttributeMapping> attMappings = mapping.getAttributeMappings();
            List<String> requestedProperties = new ArrayList<String>( Arrays.asList(mappingProperties));
            //NC - add feature to list, to include its ID expression
            requestedProperties.add(mapping.getTargetFeature().getName().getLocalPart());
            for (String requestedProperty : requestedProperties) {
                for (final AttributeMapping entry : attMappings) {
                    final StepList targetSteps = entry.getTargetXPath();
                    // i.e.: requested "measurement", found mapping of
                    // "measurement/result".
                    // "result" must be included to create "measurement"
                    //NC - requested Properties are only top level nodes, get all mappings inside node
                    if (targetSteps.get(0).getName().getLocalPart().equals(requestedProperty) ) {
                        
                        final Expression sourceExpression = entry.getSourceExpression();
                        final Expression idExpression = entry.getIdentifierExpression();
                        //NC - include client properties
                        final Collection<Expression> clientProperties = entry.getClientProperties().values();
                        
                        FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                        sourceExpression.accept(extractor, null);
                        idExpression.accept(extractor, null);
                        Iterator<Expression> it = clientProperties.iterator(); 
                        while (it.hasNext()) {
                            it.next().accept(extractor, null);
                        }
                        Set<String> exprAtts = extractor.getAttributeNameSet();
                        for (String mappedAtt : exprAtts) {
                            if (!mappedAtt.equals("Expression.NIL")) { //NC - ignore Nil Expression
                               PropertyName propExpr = filterFac.property(mappedAtt);
                               Object object = propExpr.evaluate(mappedType);
                               AttributeDescriptor mappedAttribute = (AttributeDescriptor) object;
                               if (mappedAttribute != null) {
                                    requestedSurrogateProperties.add(mappedAtt);
                               } else {
                                    LOGGER.info("mapped type does not contains property " + mappedAtt);
                                }
                            }
                        }
                        LOGGER.fine("adding atts needed for : " + exprAtts);
                    }
                }
            }
            propNames = new ArrayList<String>(requestedSurrogateProperties);
        }
        return propNames;
    }

    /**
     * Takes a filter that operates against a {@linkplain FeatureTypeMapping}'s target FeatureType,
     * and unrolls it creating a new Filter that operates against the mapping's source FeatureType.
     * 
     * @param complexFilter
     * @return TODO: implement filter unrolling
     */
    public static Filter unrollFilter(Filter complexFilter, FeatureTypeMapping mapping) {
        UnmappingFilterVisitor visitor = UnmappingFilterVistorFactory.getInstance(mapping);
        Filter unrolledFilter = (Filter) complexFilter.accept(visitor, null);
        return unrolledFilter;
    }

    public void dispose() {
        DataAccessRegistry.unregister(this);
        // dispose all the source data stores
        for (FeatureTypeMapping mapping : mappings.values()) {
            mapping.getSource().getDataStore().dispose();
        }
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
        return new MappingFeatureSource(this, getMappingByElement(typeName));
    }

    /**
     * Not a supported operation.
     * 
     * @see org.geotools.data.DataAccess#updateSchema(org.opengis.feature.type.Name,
     *      org.opengis.feature.type.FeatureType)
     */
    public void updateSchema(Name typeName, FeatureType featureType) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Return a feature source that can be used to obtain features of a particular name. This name
     * would be the mappingName in the TypeMapping if it exists, otherwise it's the target element
     * name.
     * 
     * @param typeName
     *            mappingName or targetElement
     * @return Mapping feature source
     * @throws IOException
     */
    public FeatureSource<FeatureType, Feature> getFeatureSourceByName(Name typeName)
            throws IOException {
        return new MappingFeatureSource(this, getMappingByName(typeName));
    }
}
