/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2009-2011, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import net.opengis.wfs20.ResolveValueType;
import org.geotools.appschema.filter.FilterFactoryImplNamespaceAware;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.feature.type.Types;
import org.geotools.data.complex.util.ComplexFeatureConstants;
import org.geotools.data.complex.util.XPathUtil.StepList;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.util.Converters;
import org.geotools.util.factory.Hints;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * This class represents AttributeMapping for attributes that are nested inside another complex
 * attribute. The nested attributes would be features, or fake features, ie. complex attributes
 * which types are wrapped with NonFeatureTypeProxy instances. The purpose of this class is to store
 * nested built features so they can be retrieved when the parent feature is being built. Simple
 * features are also stored for caching if a filter involving these nested features is run.
 *
 * @author Rini Angreani (CSIRO Earth Science and Resource Engineering)
 */
public class NestedAttributeMapping extends AttributeMapping {
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(NestedAttributeMapping.class);

    /** Input feature source of the nested features */
    private FeatureSource<FeatureType, Feature> source;

    /** Mapped feature source of the nested features */
    private FeatureSource<FeatureType, Feature> mappingSource;

    /** Name of the nested features element */
    protected final Expression nestedFeatureType;

    /** Target xpath that links to nested features */
    protected final StepList nestedTargetXPath;

    /** Source expression of the nested features */
    private Expression nestedSourceExpression;

    /** Filter factory */
    protected FilterFactory filterFac;

    private NamespaceSupport namespaces;

    /** Id expression for the nested type. */
    private Expression nestedIdExpression;
    /** true if the type is depending on a function value, i.e. could be a Function */
    private boolean isConditional;

    /**
     * Sole constructor
     *
     * @param sourceElement parent feature element type
     * @param sourcePath XPath link to nested feature
     */
    public NestedAttributeMapping(
            Expression idExpression,
            Expression parentExpression,
            StepList targetXPath,
            boolean isMultiValued,
            Map<Name, Expression> clientProperties,
            Expression sourceElement,
            StepList sourcePath,
            NamespaceSupport namespaces)
            throws IOException {
        super(
                idExpression,
                parentExpression,
                null,
                targetXPath,
                null,
                isMultiValued,
                clientProperties);
        this.nestedTargetXPath = sourcePath;
        this.nestedFeatureType = sourceElement;
        this.filterFac = new FilterFactoryImplNamespaceAware(namespaces);
        this.namespaces = namespaces;
        this.isConditional = nestedFeatureType instanceof Function;
    }

    @Override
    /*
     * @see org.geotools.data.complex.AttributeMapping#isNestedAttribute()
     */
    public boolean isNestedAttribute() {
        return true;
    }

    /**
     * Get matching input features that are stored in this mapping using a supplied link value.
     *
     * @return The matching input feature
     */
    public List<Feature> getInputFeatures(
            Object caller,
            Object foreignKeyValue,
            List<Object> idValues,
            Object feature,
            CoordinateReferenceSystem reprojection,
            List<PropertyName> selectedProperties,
            boolean includeMandatory)
            throws IOException {
        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }
        boolean isMultiple = false;
        if (source == null || isConditional) {
            // We can't initiate this in the constructor because the feature type mapping
            // might not be built yet.
            Object featureTypeName = getNestedFeatureType(feature);
            if (featureTypeName == null || !(featureTypeName instanceof Name)) {
                // this could be legitimate, for some null values polymorphism use case
                // or that it's set to be xlink:href
                return Collections.EMPTY_LIST;
            }
            FeatureTypeMapping featureTypeMapping =
                    AppSchemaDataAccessRegistry.getMappingByName((Name) featureTypeName);
            if (featureTypeMapping == null) {
                LOGGER.info(
                        "FeatureTypeMapping for '"
                                + featureTypeName
                                + "' not found when evaluating filter!");
                return Collections.EMPTY_LIST;
            }

            nestedIdExpression = featureTypeMapping.getFeatureIdExpression();

            source = featureTypeMapping.getSource();

            if (source == null) {
                LOGGER.info(
                        "Feature source for '"
                                + featureTypeName
                                + "' not found when evaluating filter");
                return Collections.EMPTY_LIST;
            }

            AttributeMapping mapping = getMapping(featureTypeMapping);
            nestedSourceExpression = mapping.getSourceExpression();
            isMultiple = mapping.isMultiValued();
        }

        return getFilteredFeatures(foreignKeyValue, isMultiple);
    }

    public AttributeMapping getMapping(FeatureTypeMapping featureTypeMapping) {
        // find source expression on nested features side
        AttributeMapping mapping;
        if (!ComplexFeatureConstants.FEATURE_CHAINING_LINK_STRING.equals(
                nestedTargetXPath.get(nestedTargetXPath.size() - 1).getName().getLocalPart())) {
            List<AttributeMapping> mappings =
                    featureTypeMapping.getAttributeMappingsIgnoreIndex(this.nestedTargetXPath);
            if (mappings.size() < 1) {
                throw new IllegalArgumentException(
                        "Mapping is missing for: '" + this.nestedTargetXPath + "'!");
            }
            mapping = mappings.get(0);
        } else {
            mapping = featureTypeMapping.getAttributeMapping(this.nestedTargetXPath);
            if (mapping == null) {
                throw new IllegalArgumentException(
                        "Mapping is missing for: '" + this.nestedTargetXPath + "'!");
            }
        }
        return mapping;
    }

    /**
     * Run the query to get built features from a table based on a foreign key.
     *
     * @param foreignKeyValue foreign key to filter by
     * @param isMultiple true if the table is denormalised and multiple values are possible for the
     *     same id
     * @return list of built features
     */
    private List<Feature> getFilteredFeatures(Object foreignKeyValue, boolean isMultiple)
            throws IOException {
        if (nestedSourceExpression == null) {
            return Collections.EMPTY_LIST;
        }

        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();

        Filter filter =
                filterFac.equals(this.nestedSourceExpression, filterFac.literal(foreignKeyValue));

        // get all the nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = source.getFeatures(filter);
        Filter matchingIdFilter = null;

        try (FeatureIterator<Feature> it = fCollection.features()) {
            if (nestedIdExpression.equals(Expression.NIL)) {
                HashSet<FeatureId> featureIds = new HashSet<FeatureId>();
                while (it.hasNext()) {
                    Feature f = it.next();
                    matchingFeatures.add(f);
                    if (isMultiple && f.getIdentifier() != null) {
                        featureIds.add(f.getIdentifier());
                    }
                }

                // Find features of the same id from denormalised view
                if (!featureIds.isEmpty()) {
                    matchingIdFilter = filterFac.id(featureIds);
                }
            } else {
                HashSet<String> featureIds = new HashSet<String>();
                while (it.hasNext()) {
                    Feature f = it.next();
                    matchingFeatures.add(f);
                    if (isMultiple) {
                        featureIds.add(
                                Converters.convert(nestedIdExpression.evaluate(f), String.class));
                    }
                }

                // Find features of the same id from denormalised view
                if (!featureIds.isEmpty()) {
                    List<Filter> idFilters = new ArrayList<Filter>(featureIds.size());
                    for (String id : featureIds) {
                        idFilters.add(filterFac.equals(nestedIdExpression, filterFac.literal(id)));
                    }
                    matchingIdFilter = filterFac.or(idFilters);
                }
            }
        }

        if (matchingIdFilter != null) {
            fCollection = source.getFeatures(matchingIdFilter);

            if (fCollection.size() > matchingFeatures.size()) {
                // there are rows of same id from denormalised view
                try (FeatureIterator<Feature> it = fCollection.features()) {
                    matchingFeatures.clear();
                    while (it.hasNext()) {
                        matchingFeatures.add(it.next());
                    }
                }
            }
        }

        return matchingFeatures;
    }

    /**
     * Get matching input features that are stored in this mapping using a supplied link value.
     *
     * @return The matching input feature
     */
    public List<Feature> getInputFeatures(Object foreignKeyValue, FeatureTypeMapping fMapping)
            throws IOException {
        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }
        boolean isMultiple = false;
        if (source == null || isConditional) {
            if (fMapping != null) {
                source = fMapping.getSource();

                if (source == null) {
                    LOGGER.info(
                            "Feature source for '"
                                    + fMapping.getTargetFeature().getName()
                                    + "' not found when evaluating filter");
                    return Collections.EMPTY_LIST;
                }

                nestedIdExpression = fMapping.getFeatureIdExpression();

                // find source expression on nested features side
                AttributeMapping mapping = getMapping(fMapping);
                nestedSourceExpression = mapping.getSourceExpression();
                isMultiple = mapping.isMultiValued();
            }
        }

        if (nestedSourceExpression == null) {
            return null;
        }

        return getFilteredFeatures(foreignKeyValue, isMultiple);
    }

    /**
     * Get the maching built features that are stored in this mapping using a supplied link value
     *
     * @param reprojection Reprojected CRS or null
     * @return The matching simple features
     */
    public List<Feature> getFeatures(
            Object foreignKeyValue,
            CoordinateReferenceSystem reprojection,
            Feature feature,
            int resolveDepth,
            Integer resolveTimeOut)
            throws IOException {
        return getFeatures(
                null,
                foreignKeyValue,
                null,
                reprojection,
                feature,
                null,
                true,
                resolveDepth,
                resolveTimeOut);
    }

    /**
     * Get the maching built features that are stored in this mapping using a supplied link value
     *
     * @param reprojection Reprojected CRS or null
     * @param selectedProperties list of properties to get
     * @return The matching simple features
     */
    public List<Feature> getFeatures(
            Object source,
            Object foreignKeyValue,
            List<Object> idValues,
            CoordinateReferenceSystem reprojection,
            Object feature,
            List<PropertyName> selectedProperties,
            boolean includeMandatory,
            int resolveDepth,
            Integer resolveTimeOut)
            throws IOException {

        if (foreignKeyValue == null) {
            return Collections.<Feature>emptyList();
        }

        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }

        FeatureSource<FeatureType, Feature> fSource = getMappingSource(feature);
        if (fSource == null) {
            return null;
        }

        Query query = new Query();
        query.setCoordinateSystemReproject(reprojection);

        Filter filter;
        PropertyName propertyName = filterFac.property(this.nestedTargetXPath.toString());
        filter = filterFac.equals(propertyName, filterFac.literal(foreignKeyValue));
        query.setFilter(filter);

        if (selectedProperties != null && !selectedProperties.isEmpty()) {
            selectedProperties = new ArrayList<PropertyName>(selectedProperties);
            selectedProperties.add(propertyName);
        }

        final Hints hints = new Hints();
        hints.put(Query.INCLUDE_MANDATORY_PROPS, includeMandatory);

        if (resolveDepth > 0) {
            hints.put(Hints.RESOLVE, ResolveValueType.ALL);
            hints.put(Hints.ASSOCIATION_TRAVERSAL_DEPTH, resolveDepth);
            hints.put(Hints.RESOLVE_TIMEOUT, resolveTimeOut);
        } else {
            hints.put(Hints.RESOLVE, ResolveValueType.NONE);
        }

        query.setHints(hints);

        query.setProperties(selectedProperties);

        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();

        // get all the mapped nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = fSource.getFeatures(query);
        if (fCollection instanceof MappingFeatureCollection) {
            try (FeatureIterator<Feature> iterator = fCollection.features()) {
                while (iterator.hasNext()) {
                    matchingFeatures.add(iterator.next());
                }
            }
        }

        return matchingFeatures;
    }

    protected FeatureSource<FeatureType, Feature> getMappingSource(Object feature)
            throws IOException {

        if (mappingSource == null || isConditional) {
            // initiate if null, or evaluate a new one if the targetElement is a function
            // which value depends on the feature
            Object featureTypeName = getNestedFeatureType(feature);
            if (featureTypeName == null || !(featureTypeName instanceof Name)) {
                return null;
            }
            // this cannot be set in the constructor since it might not exist yet
            mappingSource = DataAccessRegistry.getFeatureSource((Name) featureTypeName);
        }
        return mappingSource;
    }

    /** @return the nested feature type name */
    public Object getNestedFeatureType(Object feature) {
        Object fTypeValue;
        if (isConditional) {
            if (feature == null) {
                throw new IllegalArgumentException("Feature parameter is required!");
            }
            fTypeValue = nestedFeatureType.evaluate(feature);
            if (fTypeValue == null) {
                // this could be legitimate, i.e. in polymorphism
                // to evaluate a function with a certain column value
                // if null, don't encode this element
                return null;
            }
            if (fTypeValue instanceof Hints) {
                return ((Hints) fTypeValue).get(ComplexFeatureConstants.STRING_KEY);
            }
        } else {
            fTypeValue = nestedFeatureType.toString();
        }
        return Types.degloseName(String.valueOf(fTypeValue), namespaces);
    }

    public boolean isConditional() {
        return this.isConditional;
    }

    public boolean isSameSource() {
        // if the linkField is null, we're meant to work out the nestedFeatureType from
        // the linkElement, which should contain a function. So the value could vary
        // feature per feature. But the linkElement would point to the same data source table
        // if the linkField is null.
        return this.nestedTargetXPath == null;
    }

    public FeatureTypeMapping getFeatureTypeMapping(Feature feature) throws IOException {
        FeatureSource<FeatureType, Feature> fSource = getMappingSource(feature);
        if (fSource == null) {
            return null;
        }
        return (fSource instanceof MappingFeatureSource)
                ? ((MappingFeatureSource) fSource).getMapping()
                : null;
    }

    public NamespaceSupport getNamespaces() {
        return namespaces;
    }
}
