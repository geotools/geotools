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
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.geotools.data.DataAccess;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.filter.XPath;
import org.geotools.data.complex.filter.XPath.Step;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.Hints;
import org.geotools.feature.AttributeBuilder;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureImpl;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterFactoryImpl;
import org.opengis.feature.Attribute;
import org.opengis.feature.ComplexAttribute;
import org.opengis.feature.Feature;
import org.opengis.feature.GeometryAttribute;
import org.opengis.feature.Property;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.Attributes;

import com.vividsolutions.jts.geom.EmptyGeometry;
import com.vividsolutions.jts.geom.Geometry;

/**
 * A Feature iterator that operates over the FeatureSource of a
 * {@linkplain org.geotools.data.complex.FeatureTypeMapping} and produces Features of the output
 * schema by applying the mapping rules to the Features of the source schema.
 * <p>
 * This iterator acts like a one-to-one mapping, producing a Feature of the target type for each
 * feature of the source type.
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @author Ben Caradoc-Davies, CSIRO Exploration and Mining
 * @author Rini Angreani, Curtin University of Technology
 * @author Russell Petty, GSV
 * @version $Id$
 *
 * @source $URL$
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/DataAccessMappingFeatureIterator.java $
 * @since 2.4
 */
public class DataAccessMappingFeatureIterator extends AbstractMappingFeatureIterator {
    /**
     * Hold on to iterator to allow features to be streamed.
     */
    private Iterator<Feature> sourceFeatureIterator;

    /**
     * Reprojected CRS from the source simple features, or null
     */
    protected CoordinateReferenceSystem reprojection;

    /**
     * This is the feature that will be processed in next()
     */
    protected Feature curSrcFeature;

    protected FeatureSource<FeatureType, Feature> mappedSource;

    protected FeatureCollection<FeatureType, Feature> sourceFeatures;

    private boolean isNextFeatureSet;

    private boolean isFiltered;

    private ArrayList<String> filteredFeatures;

    public DataAccessMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, boolean isFiltered) throws IOException {
        this(store, mapping, query, isFiltered, false);
    }

    /**
     * 
     * @param store
     * @param mapping
     *            place holder for the target type, the surrogate FeatureSource and the mappings
     *            between them.
     * @param query
     *            the query over the target feature type, that is to be unpacked to its equivalent
     *            over the surrogate feature type.
     * @throws IOException
     */
    public DataAccessMappingFeatureIterator(AppSchemaDataAccess store, FeatureTypeMapping mapping,
            Query query, boolean isFiltered, boolean isQueryUnrolled) throws IOException {
        super(store, mapping, query, isQueryUnrolled);
        this.isFiltered = isFiltered;
        if (isFiltered) {
            filteredFeatures = new ArrayList<String>();
        }
    }

    @Override
    public boolean hasNext() {
        setHasNextCalled(true);

        boolean exists = false;

        if (featureCounter < maxFeatures) {
            if (isNextFeatureSet()) {
                flagNextFeature(false);
                return curSrcFeature != null;
            }

            if (getSourceFeatureIterator() != null && getSourceFeatureIterator().hasNext()) {
                this.curSrcFeature = getSourceFeatureIterator().next();
                exists = true;
            }
            if (exists && filteredFeatures != null) {
                // get the next one if this row has already been added to the target
                // feature from setNextFilteredFeature
                while (exists && filteredFeatures.contains(extractIdForFeature(this.curSrcFeature))) {
                    if (getSourceFeatureIterator() != null && getSourceFeatureIterator().hasNext()) {
                        this.curSrcFeature = getSourceFeatureIterator().next();
                        exists = true;
                    } else {
                        exists = false;
                    }
                }
                if (!exists) {
                    curSrcFeature = null;
                }
            }
        }

        if (!exists) {
            LOGGER.finest("no more features, produced " + featureCounter);
            close();
            curSrcFeature = null;
        }

        flagNextFeature(false);

        return exists;
    }

    protected Iterator<Feature> getSourceFeatureIterator() {
        return sourceFeatureIterator;
    }

    protected boolean isSourceFeatureIteratorNull() {
        return getSourceFeatureIterator() == null;
    }

    protected void initialiseSourceFeatures(FeatureTypeMapping mapping, Query query)
            throws IOException {
        mappedSource = mapping.getSource();
        this.reprojection = query.getCoordinateSystemReproject();
        // we need to disable the max number of features retrieved so we can
        // sort them manually just in case the data is denormalised
        query.setMaxFeatures(Query.DEFAULT_MAX);
        sourceFeatures = mappedSource.getFeatures(query);
        if (reprojection != null) {
            xpathAttributeBuilder.setCRS(reprojection);
            if (sourceFeatures.getSchema().getGeometryDescriptor() == null) {
                // VT: No point trying to re-project without any geometry.
                query.setCoordinateSystemReproject(null);
            }
        }
        try {
            this.sourceFeatureIterator = sourceFeatures.iterator();
        } catch (Exception e) {
            throw new IOException(e.getMessage());
        }

    }

    protected boolean unprocessedFeatureExists() {

        boolean exists = getSourceFeatureIterator().hasNext();
        if (exists && this.curSrcFeature == null) {
            this.curSrcFeature = getSourceFeatureIterator().next();
        }

        return exists;
    }

    protected String extractIdForFeature() {
        return extractIdForFeature(curSrcFeature);
    }

    protected String extractIdForFeature(Feature feature) {
		if (mapping.getFeatureIdExpression().equals(Expression.NIL)) {
			if (feature.getIdentifier() == null) {
				return null;
			} else {
				return feature.getIdentifier().getID();
			}
		} 
		return mapping.getFeatureIdExpression().evaluate(feature, String.class);
	}

    protected String extractIdForAttribute(final Expression idExpression, Object sourceInstance) {
        String value = (String) idExpression.evaluate(sourceInstance, String.class);
        return value;
    }

    protected boolean isNextSourceFeatureNull() {
        return curSrcFeature == null;
    }

    protected boolean sourceFeatureIteratorHasNext() {
        return getSourceFeatureIterator().hasNext();
    }

    protected Object getValues(boolean isMultiValued, Expression expression,
            Object sourceFeatureInput) {
        if (isMultiValued && sourceFeatureInput instanceof FeatureImpl
                && expression instanceof AttributeExpressionImpl) {
            // RA: Feature Chaining
            // complex features can have multiple nodes of the same attribute.. and if they are used
            // as input to an app-schema data access to be nested inside another feature type of a
            // different XML type, it has to be mapped like this:
            // <AttributeMapping>
            // <targetAttribute>
            // gsml:composition
            // </targetAttribute>
            // <sourceExpression>
            // <inputAttribute>mo:composition</inputAttribute>
            // <linkElement>gsml:CompositionPart</linkElement>
            // <linkField>gml:name</linkField>
            // </sourceExpression>
            // <isMultiple>true</isMultiple>
            // </AttributeMapping>
            // As there can be multiple nodes of mo:composition in this case, we need to retrieve
            // all of them
            AttributeExpressionImpl attribExpression = ((AttributeExpressionImpl) expression);
            String xpath = attribExpression.getPropertyName();
            ComplexAttribute sourceFeature = (ComplexAttribute) sourceFeatureInput;
            StepList xpathSteps = XPath.steps(sourceFeature.getDescriptor(), xpath, namespaces);
            return getProperties(sourceFeature, xpathSteps);
        }
        return expression.evaluate(sourceFeatureInput);
    }

    /**
     * Sets the values of grouping attributes.
     * 
     * @param sourceFeature
     * @param groupingMappings
     * @param targetFeature
     * 
     * @return Feature. Target feature sets with simple attributes
     */
    protected void setAttributeValue(Attribute target, final Feature source,
            final AttributeMapping attMapping) throws IOException {

        final Expression sourceExpression = attMapping.getSourceExpression();
        final AttributeType targetNodeType = attMapping.getTargetNodeInstance();
        final StepList xpath = attMapping.getTargetXPath();
        Map<Name, Expression> clientPropsMappings = attMapping.getClientProperties();
        boolean isNestedFeature = attMapping.isNestedAttribute();
        String id = null;
        if (Expression.NIL != attMapping.getIdentifierExpression()) {
            id = extractIdForAttribute(attMapping.getIdentifierExpression(), source);
        }
        if (attMapping.isNestedAttribute()) {
            NestedAttributeMapping nestedMapping = ((NestedAttributeMapping) attMapping);
            Object mappingName = nestedMapping.getNestedFeatureType(source);
            if (mappingName != null) {
                if (nestedMapping.isSameSource() && mappingName instanceof Name) {
                    // data type polymorphism mapping
                    setPolymorphicValues((Name) mappingName, target, id, nestedMapping, source,
                            xpath, clientPropsMappings);
                    return;
                } else if (mappingName instanceof Hints) {
                    // referential polymorphism mapping
                    setPolymorphicReference((Hints) mappingName, clientPropsMappings, target,
                            xpath, targetNodeType);
                    return;
                }
            } else {
                // polymorphism could result in null, to skip the attribute
                return;
            }
        }
        Object value = getValues(attMapping.isMultiValued(), sourceExpression, source);
        boolean isHRefLink = isByReference(clientPropsMappings, isNestedFeature);
        if (isNestedFeature) {
            // get built feature based on link value
            if (value instanceof Collection) {
                ArrayList<Feature> nestedFeatures = new ArrayList<Feature>(((Collection) value)
                        .size());
                for (Object val : (Collection) value) {
                    if (val instanceof Attribute) {
                        val = ((Attribute) val).getValue();
                        if (val instanceof Collection) {
                            val = ((Collection) val).iterator().next();
                        }
                        while (val instanceof Attribute) {
                            val = ((Attribute) val).getValue();
                        }
                    }
                    if (isHRefLink) {
                        // get the input features to avoid infinite loop in case the nested
                        // feature type also have a reference back to this type
                        // eg. gsml:GeologicUnit/gsml:occurence/gsml:MappedFeature
                        // and gsml:MappedFeature/gsml:specification/gsml:GeologicUnit
                        nestedFeatures.addAll(((NestedAttributeMapping) attMapping)
                                .getInputFeatures(val, source));
                    } else {
                        nestedFeatures.addAll(((NestedAttributeMapping) attMapping).getFeatures(
                                val, reprojection, source));
                    }
                }
                value = nestedFeatures;
            } else if (isHRefLink) {
                // get the input features to avoid infinite loop in case the nested
                // feature type also have a reference back to this type
                // eg. gsml:GeologicUnit/gsml:occurence/gsml:MappedFeature
                // and gsml:MappedFeature/gsml:specification/gsml:GeologicUnit
                value = ((NestedAttributeMapping) attMapping).getInputFeatures(value, source);
            } else {
                value = ((NestedAttributeMapping) attMapping).getFeatures(value, reprojection,
                        source);
            }
            if (isHRefLink) {
                // only need to set the href link value, not the nested feature properties
                setXlinkReference(target, clientPropsMappings, value, xpath, targetNodeType);
                return;
            }
        }
        if (isNestedFeature) {
            if (value == null) {
                // polymorphism use case, if the value doesn't match anything, don't encode
                return;
            }
        }
        if (value instanceof Collection) {
            // nested feature type could have multiple instances as the whole purpose
            // of feature chaining is to cater for multi-valued properties
            Map<Object, Object> userData;
            Map<Name, Expression> valueProperties = new HashMap<Name, Expression>();
            for (Object singleVal : (Collection) value) {
                ArrayList valueList = new ArrayList();
                // copy client properties from input features if they're complex features
                // wrapped in app-schema data access
                if (singleVal instanceof Attribute) {
                    // copy client properties from input features if they're complex features
                    // wrapped in app-schema data access
                    valueProperties = getClientProperties((Attribute) singleVal);
                    if (!valueProperties.isEmpty()) {
                        valueProperties.putAll(clientPropsMappings);
                    }
                }
                if (!isNestedFeature) {
                    if (singleVal instanceof Attribute) {
                        singleVal = ((Attribute) singleVal).getValue();
                        if (singleVal instanceof Collection) {
                            valueList.addAll((Collection) singleVal);
                        } else {
                            valueList.add(singleVal);
                        }
                    }
                } else {
                    valueList.add(singleVal);
                }
                Attribute instance = xpathAttributeBuilder.set(target, xpath, valueList, id,
                        targetNodeType, false, sourceExpression);
                setClientProperties(instance, source, valueProperties);
            }
        } else {
            if (value instanceof Attribute) {
                // copy client properties from input features if they're complex features
                // wrapped in app-schema data access
                Map<Name, Expression> newClientProps = getClientProperties((Attribute) value);
                if (!newClientProps.isEmpty()) {
                    newClientProps.putAll(clientPropsMappings);
                    clientPropsMappings = newClientProps;
                }
                value = ((Attribute) value).getValue();
            }
            Attribute instance = xpathAttributeBuilder.set(target, xpath, value, id,
                    targetNodeType, false, sourceExpression);
            setClientProperties(instance, source, clientPropsMappings);

        }
    }

    /**
     * Special handling for polymorphic mapping where the value of the attribute determines that
     * this attribute should be a placeholder for an xlink:href.
     * 
     * @param xlinkHrefHints
     *            the xlink:href hints holding the URI
     * @param clientPropsMappings
     *            client properties
     * @param target
     *            the complex feature being built
     * @param xpath
     *            the xpath of attribute
     * @param targetNodeType
     *            the type of the attribute to be cast to, if any
     */
    private void setPolymorphicReference(Hints xlinkHrefHints,
            Map<Name, Expression> clientPropsMappings, Attribute target, StepList xpath,
            AttributeType targetNodeType) {

        Object uri = xlinkHrefHints.get(ComplexFeatureConstants.STRING_KEY);
        if (uri != null) {
            Attribute instance = xpathAttributeBuilder.set(target, xpath, null, "", targetNodeType,
                    true, null);
            FilterFactoryImpl ff = new FilterFactoryImpl();
            Map<Name, Expression> newClientProps = new HashMap<Name, Expression>();
            newClientProps.putAll(clientPropsMappings);
            newClientProps.put(XLINK_HREF_NAME, ff.literal(uri));
            setClientProperties(instance, null, newClientProps);
        }
    }

    /**
     * Special handling for polymorphic mapping. Works out the polymorphic type name by evaluating
     * the function on the feature, then set the relevant sub-type values.
     * 
     * @param target
     *            The target feature to be encoded
     * @param id
     *            The target feature id
     * @param nestedMapping
     *            The mapping that is polymorphic
     * @param source
     *            The source simple feature
     * @param xpath
     *            The xpath of polymorphic type
     * @param clientPropsMappings
     *            Client properties
     * @throws IOException
     */
    private void setPolymorphicValues(Name mappingName, Attribute target, String id,
            NestedAttributeMapping nestedMapping, Feature source, StepList xpath,
            Map<Name, Expression> clientPropsMappings) throws IOException {
        // process sub-type mapping
        DataAccess<FeatureType, Feature> da = DataAccessRegistry.getDataAccess((Name) mappingName);
        if (da instanceof AppSchemaDataAccess) {
            // why wouldn't it be? check just to be safe
            FeatureTypeMapping fTypeMapping = ((AppSchemaDataAccess) da)
                    .getMappingByName((Name) mappingName);
            List<AttributeMapping> polymorphicMappings = fTypeMapping.getAttributeMappings();
            AttributeDescriptor attDescriptor = fTypeMapping.getTargetFeature();
            Name polymorphicTypeName = attDescriptor.getName();
            StepList prefixedXpath = xpath.clone();
            prefixedXpath.add(new Step(new QName(polymorphicTypeName.getNamespaceURI(),
                    polymorphicTypeName.getLocalPart(), this.namespaces
                            .getPrefix(polymorphicTypeName.getNamespaceURI())), 1));
            Attribute instance = xpathAttributeBuilder.set(target, prefixedXpath, null, id,
                    attDescriptor.getType(), false, attDescriptor, null);
            setClientProperties(instance, source, clientPropsMappings);
            for (AttributeMapping mapping : polymorphicMappings) {
                if (isTopLevelmapping(polymorphicTypeName, mapping.getTargetXPath())) {
                    // if the top level mapping for the Feature itself, the attribute instance
                    // has already been created.. just need to set the client properties
                    setClientProperties(instance, source, mapping.getClientProperties());
                    continue;
                }
                setAttributeValue(instance, source, mapping);
            }
        }
    }

    /**
     * Set xlink:href client property for multi-valued chained features. This has to be specially
     * handled because we don't want to encode the nested features attributes, since it's already an
     * xLink. Also we need to eliminate duplicates.
     * 
     * @param target
     *            The target attribute
     * @param clientPropsMappings
     *            Client properties mappings
     * @param value
     *            Nested features
     * @param xpath
     *            Attribute xPath where the client properties are to be set
     * @param targetNodeType
     *            Target node type
     */
    protected void setXlinkReference(Attribute target, Map<Name, Expression> clientPropsMappings,
            Object value, StepList xpath, AttributeType targetNodeType) {
        // Make sure the same value isn't already set
        // in case it comes from a denormalized view for many-to-many relationship.
        // (1) Get the first existing value
        Property existingAttribute = getProperty(target, xpath);

        if (existingAttribute != null) {
            Object existingValue = existingAttribute.getUserData().get(Attributes.class);
            if (existingValue != null) {
                assert existingValue instanceof HashMap;
                existingValue = ((Map) existingValue).get(XLINK_HREF_NAME);
            }
            if (existingValue != null) {
                Expression linkExpression = clientPropsMappings.get(XLINK_HREF_NAME);
                for (Object singleVal : (Collection) value) {
                    assert singleVal instanceof Feature;
                    assert linkExpression != null;
                    Object hrefValue = linkExpression.evaluate(singleVal);
                    if (hrefValue != null && hrefValue.equals(existingValue)) {
                        // (2) if one of the new values matches the first existing value,
                        // that means this comes from a denormalized view,
                        // and this set has already been set
                        return;
                    }
                }
            }
        }

        for (Object singleVal : (Collection) value) {
            assert singleVal instanceof Feature;
            Attribute instance = xpathAttributeBuilder.set(target, xpath, null, null,
                    targetNodeType, true, null);
            setClientProperties(instance, singleVal, clientPropsMappings);
        }
    }

    protected void setClientProperties(final Attribute target, final Object source,
            final Map<Name, Expression> clientProperties) {
        if (target == null) {
            return;
        }

        // NC - first calculate target attributes
        final Map<Name, Object> targetAttributes = new HashMap<Name, Object>();
        if (target.getUserData().containsValue(Attributes.class)) {
            targetAttributes.putAll((Map<? extends Name, ? extends Object>) target.getUserData()
                    .get(Attributes.class));
        }        
        for (Map.Entry<Name, Expression> entry : clientProperties.entrySet()) {
            Name propName = entry.getKey();
            Object propExpr = entry.getValue();
            Object propValue;
            if (propExpr instanceof Expression) {
                propValue = getValue((Expression) propExpr, source);
            } else {
                propValue = propExpr;
            }
            targetAttributes.put(propName, propValue);
        }
        // FIXME should set a child Property.. but be careful for things that
        // are smuggled in there internally and don't exist in the schema, like
        // XSDTypeDefinition, CRS etc.
        if (targetAttributes.size() > 0) {
            target.getUserData().put(Attributes.class, targetAttributes);
        }
        
        // with geometry objects, set ID and attributes in geometry object
        if (target instanceof GeometryAttribute
                && (targetAttributes.size() > 0 || target.getIdentifier() != null)) {
            Geometry geom;
            if (target.getValue() == null) {
                // create empty geometry if null but attributes
                geom = new EmptyGeometry();
            } else {
                // need to clone because it seems the same geometry object from the
                // db is reused instead of regenerated if different attributes refer
                // to the same database row... so if we change the userData, we have
                // to clone it
                geom = (Geometry) ((Geometry) target.getValue()).clone();
            }

            if (geom != null) {

                Object userData = geom.getUserData();
                Map newUserData = new HashMap<Object, Object>();
                if (userData != null) {
                    if (userData instanceof Map) {
                        newUserData.putAll((Map) userData);
                    } else if (userData instanceof CoordinateReferenceSystem) {
                        newUserData.put(CoordinateReferenceSystem.class, userData);
                    }
                }
                // set gml:id and attributes in Geometry userData
                if (target.getIdentifier() != null) {
                    newUserData.put("gml:id", target.getIdentifier().toString());
                }
                if (targetAttributes.size() > 0) {
                    newUserData.put(Attributes.class, targetAttributes);
                }

                geom.setUserData(newUserData);
                target.setValue(geom);
            }
        }
    }

    private Map getClientProperties(Property attribute) throws DataSourceException {

        Map<Object, Object> userData = attribute.getUserData();
        Map clientProperties = new HashMap<Name, Expression>();
        if (userData != null && userData.containsKey(Attributes.class)) {
            Map props = (Map) userData.get(Attributes.class);
            if (!props.isEmpty()) {
                clientProperties.putAll(props);
            }
        }
        return clientProperties;
    }

    protected void setNextFeature(String fId, ArrayList<Feature> features) throws IOException {
        if (features.isEmpty()) {
            features.add(curSrcFeature);
        }
        curSrcFeature = null;

        while (getSourceFeatureIterator().hasNext()) {
            Feature next = getSourceFeatureIterator().next();
            if (extractIdForFeature(next).equals(fId)) {
                features.add(next);
            } else {
                curSrcFeature = next;
                flagNextFeature(true);
                break;
            }
        }
    }

    private void setNextFilteredFeature(String fId, ArrayList<Feature> features) throws IOException {
        FeatureCollection<FeatureType, Feature> matchingFeatures;
        FeatureId featureId = namespaceAwareFilterFactory.featureId(fId);
        Query query = new Query();
        if (reprojection != null) {
            if (sourceFeatures.getSchema().getGeometryDescriptor() != null) {
                query.setCoordinateSystemReproject(reprojection);
            }
        }

        if (mapping.getFeatureIdExpression().equals(Expression.NIL)) {
            // no real feature id mapping,
            // so let's find by database row id
            Set<FeatureId> ids = new HashSet<FeatureId>();
            ids.add(featureId);
            query.setFilter(namespaceAwareFilterFactory.id(ids));
            matchingFeatures = this.mappedSource.getFeatures(query);
        } else {
            // in case the expression is wrapped in a function, eg. strConcat
            // that's why we don't always filter by id, but do a PropertyIsEqualTo
            query.setFilter(namespaceAwareFilterFactory.equals(mapping.getFeatureIdExpression(),
                    namespaceAwareFilterFactory.literal(fId)));
            matchingFeatures = this.mappedSource.getFeatures(query);
        }

        Iterator<Feature> iterator = matchingFeatures.iterator();

        while (iterator.hasNext()) {
            features.add(iterator.next());
        }
        // Probably cause there is no primary key nor idExpression
        if (features.isEmpty()) {
            features.add(curSrcFeature);
        }

        filteredFeatures.add(fId);

        matchingFeatures.close(iterator);

        curSrcFeature = null;
    }

    protected Feature computeNext() throws IOException {
        if (curSrcFeature == null) {
            throw new UnsupportedOperationException("No features found in next()."
                    + "This wouldn't have happenned if hasNext() was called beforehand.");
        }

        setHasNextCalled(false);

        ArrayList<Feature> sources = new ArrayList<Feature>();

        String id = extractIdForFeature(curSrcFeature);

        if (isFiltered) {
            setNextFilteredFeature(id, sources);
        } else {
            setNextFeature(id, sources);
        }
        final AttributeDescriptor targetNode = mapping.getTargetFeature();
        final Name targetNodeName = targetNode.getName();
        final List<AttributeMapping> mappings = mapping.getAttributeMappings();

        AttributeBuilder builder = new AttributeBuilder(attf);
        builder.setDescriptor(targetNode);
        Feature target = (Feature) builder.build(id);

        for (AttributeMapping attMapping : mappings) {
            if (propertyNames == null || propertyNames.contains(attMapping.getTargetXPath().get(0).getName().getLocalPart())) {
                try {
                    if (isTopLevelmapping(targetNodeName, attMapping.getTargetXPath())) {
                        // ignore the top level mapping for the Feature itself
                        // as it was already set
                        continue;
                    }
                    // extract the values from multiple source features of the same id
                    // and set them to one built feature
                    if (attMapping.isMultiValued()) {
                        for (Feature source : sources) {
                            setAttributeValue(target, source, attMapping);
                        }
                    } else {
                        setAttributeValue(target, sources.get(0), attMapping);
                    }
                } catch (Exception e) {
                    throw new RuntimeException("Error applying mapping with targetAttribute "
                            + attMapping.getTargetXPath(), e);
                }
            }
        }
        this.cleanEmptyElements(target);
        return target;
    }

    private void cleanEmptyElements(Feature target) throws DataSourceException {
        try {
            ArrayList values = new ArrayList<Property>();
            for (Iterator i = target.getValue().iterator(); i.hasNext();) {
                Property p = (Property) i.next();

                if (hasChild(p) || p.getDescriptor().getMinOccurs() > 0) {
                    values.add(p);
                }
            }
            target.setValue(values);
        } catch (DataSourceException e) {
            throw new DataSourceException("Unable to clean empty element", e);
        }
    }

    private boolean hasChild(Property p) throws DataSourceException {
        if (p instanceof Feature) {
            return true;
        }
        boolean result = false;
        if (p.getValue() instanceof Collection) {

            Collection c = (Collection) p.getValue();
            // Will need to add && this.getClientProperties(p).get(XLINK_HREF_NAME)!=null if we
            // intend to skip empty xlink href eg <gsml:samplingFrame/>
            if (c.size() == 0 && this.getClientProperties(p).containsKey(XLINK_HREF_NAME)) {
                return true;
            }

            ArrayList values = new ArrayList();
            for (Object o : c) {
                if (o instanceof Property) {
                    if (hasChild((Property) o)) {
                        values.add(o);
                        result = true;
                    } else if (((Property) o).getDescriptor().getMinOccurs() > 0) {
                        if (((Property) o).getDescriptor().isNillable()) {
                            // add nil mandatory property
                            values.add(o);
                        }
                    }
                }
            }
            p.setValue(values);
        } else {
            result = true;
        }
        return result;

    }

    private boolean isTopLevelmapping(Name targetNodeName, StepList targetXPath) {
        if (targetXPath.size() == 1) {
            Step rootStep = targetXPath.get(0);
            QName stepName = rootStep.getName();
            if (Types.equals(targetNodeName, stepName)) {
                return true;
            }
        }
        return false;
    }

    protected Feature populateFeatureData(String id) throws IOException {
        throw new UnsupportedOperationException("populateFeatureData should not be called!");
    }

    protected void closeSourceFeatures() {
        if (sourceFeatures != null && getSourceFeatureIterator() != null) {
            sourceFeatures.close(sourceFeatureIterator);
            sourceFeatureIterator = null;
            sourceFeatures = null;
            filteredFeatures = null;
        }
    }

    protected Object getValue(final Expression expression, Object sourceFeature) {
        Object value;
        value = expression.evaluate(sourceFeature);
        if (value instanceof Attribute) {
            value = ((Attribute) value).getValue();
        }
        return value;
    }

    /**
     * Returns first matching attribute from provided root and xPath.
     * 
     * @param root
     *            The root attribute to start searching from
     * @param xpath
     *            The xPath matching the attribute
     * @return The first matching attribute
     */
    private Property getProperty(Attribute root, StepList xpath) {
        Property property = root;

        final StepList steps = new StepList(xpath);

        Iterator<Step> stepsIterator = steps.iterator();

        while (stepsIterator.hasNext()) {
            assert property instanceof ComplexAttribute;
            Step step = stepsIterator.next();
            property = ((ComplexAttribute) property).getProperty(Types.toTypeName(step.getName()));
            if (property == null) {
                return null;
            }
        }
        return property;
    }

    /**
     * Return all matching properties from provided root attribute and xPath.
     * 
     * @param root
     *            The root attribute to start searching from
     * @param xpath
     *            The xPath matching the attribute
     * @return The matching attributes collection
     */
    private Collection<Property> getProperties(ComplexAttribute root, StepList xpath) {

        final StepList steps = new StepList(xpath);

        Iterator<Step> stepsIterator = steps.iterator();
        Collection<Property> properties = null;
        Step step = null;
        if (stepsIterator.hasNext()) {
            step = stepsIterator.next();
            properties = ((ComplexAttribute) root).getProperties(Types.toTypeName(step.getName()));
        }

        while (stepsIterator.hasNext()) {
            step = stepsIterator.next();
            Collection<Property> nestedProperties = new ArrayList<Property>();
            for (Property property : properties) {
                assert property instanceof ComplexAttribute;
                Collection<Property> tempProperties = ((ComplexAttribute) property)
                        .getProperties(Types.toTypeName(step.getName()));
                if (!tempProperties.isEmpty()) {
                    nestedProperties.addAll(tempProperties);
                }
            }
            properties.clear();
            if (nestedProperties.isEmpty()) {
                return properties;
            }
            properties.addAll(nestedProperties);
        }
        return properties;
    }

    /**
     * Checks if client property has xlink:ref in it, if the attribute is for chained features.
     * 
     * @param clientPropsMappings
     *            the client properties mappings
     * @param isNested
     *            true if we're dealing with chained/nested features
     * @return
     */
    protected boolean isByReference(Map<Name, Expression> clientPropsMappings, boolean isNested) {
        // only care for chained features
        return isNested ? (clientPropsMappings.isEmpty() ? false : (clientPropsMappings
                .get(XLINK_HREF_NAME) == null) ? false : true) : false;
    }

    public void flagNextFeature(boolean isSet) {
        this.isNextFeatureSet = isSet;
    }

    public boolean isNextFeatureSet() {
        return isNextFeatureSet;
    }

}
