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
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.complex.filter.XPath.StepList;
import org.geotools.factory.Hints;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.Types;
import org.geotools.filter.AttributeExpressionImpl;
import org.geotools.filter.FilterFactoryImplNamespaceAware;
import org.geotools.util.Converters;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.expression.Expression;
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
 * @author Rini Angreani, Curtin University of Technology
 * 
 * @source $URL:
 *         http://svn.osgeo.org/geotools/trunk/modules/unsupported/app-schema/app-schema/src/main
 *         /java/org/geotools/data/complex/NestedAttributeMapping.java $
 */
public class NestedAttributeMapping extends AttributeMapping {
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.data.complex");
	
    /**
     * Input feature source of the nested features
     */
    private FeatureSource<FeatureType, Feature> source;

    /**
     * Mapped feature source of the nested features
     */
    private FeatureSource<FeatureType, Feature> mappingSource;

    /**
     * Name of the nested features element
     */
    private final Expression nestedFeatureType;

    /**
     * Target xpath that links to nested features
     */
    private final StepList nestedTargetXPath;

    /**
     * Source expression of the nested features
     */
    private Expression nestedSourceExpression;

    /**
     * Filter factory
     */
    private FilterFactory filterFac;

    private NamespaceSupport namespaces;

    /**
     * Id expression for the nested type.
     */
    private Expression nestedIdExpression;
    
    /**
     * Sole constructor
     * 
     * @param idExpression
     * @param parentExpression
     * @param targetXPath
     * @param targetNodeInstance
     * @param isMultiValued
     * @param clientProperties
     * @param sourceElement
     *            parent feature element type
     * @param sourcePath
     *            XPath link to nested feature
     * @param parentSource
     *            parent feature source
     * @throws IOException
     */
    public NestedAttributeMapping(Expression idExpression, Expression parentExpression,
            StepList targetXPath, boolean isMultiValued, Map<Name, Expression> clientProperties,
            Expression sourceElement, StepList sourcePath, NamespaceSupport namespaces)
            throws IOException {
        super(idExpression, parentExpression, targetXPath, null, isMultiValued, clientProperties);
        this.nestedTargetXPath = sourcePath;
        this.nestedFeatureType = sourceElement;
        this.filterFac = new FilterFactoryImplNamespaceAware(namespaces);
        this.namespaces = namespaces;
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
     * @param foreignKeyValue
     * @return The matching input feature
     * @throws IOException
     * @throws IOException
     */
    public List<Feature> getInputFeatures(Object foreignKeyValue, Feature feature)
            throws IOException {
        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }                        
        if (source == null || !(nestedFeatureType instanceof AttributeExpressionImpl)) {
            // We can't initiate this in the constructor because the feature type mapping
            // might not be built yet.
            Object featureTypeName = getNestedFeatureType(feature);
            if (featureTypeName == null || !(featureTypeName instanceof Name)) {
                // this could be legitimate, for some null values polymorphism use case
                // or that it's set to be xlink:href
                return Collections.EMPTY_LIST;
            }
            FeatureTypeMapping featureTypeMapping = AppSchemaDataAccessRegistry
                    .getMappingByName((Name) featureTypeName);
            if (featureTypeMapping == null) {
            	LOGGER.info("FeatureTypeMapping for '" + featureTypeName + "' not found when evaluating filter!");
            	return Collections.EMPTY_LIST;
            }
            
            nestedIdExpression = featureTypeMapping.getFeatureIdExpression();

            source = featureTypeMapping.getSource();
            
            if (source == null) {
            	LOGGER.info("Feature source for '" + featureTypeName + "' not found when evaluating filter");
            	return Collections.EMPTY_LIST;
            }

            // find source expression on nested features side
            List<AttributeMapping> mappings = featureTypeMapping
                    .getAttributeMappingsIgnoreIndex(this.nestedTargetXPath);
            if (mappings.size() < 1) {
                throw new IllegalArgumentException("Mapping is missing for: '"
                        + this.nestedTargetXPath + "'!");
            }
            nestedSourceExpression = mappings.get(0).getSourceExpression();
        }    
                
        return getFilteredFeatures(foreignKeyValue);        
    }
    
    private List<Feature> getFilteredFeatures(Object foreignKeyValue) throws IOException {   
    	if (nestedSourceExpression == null) {
        	return Collections.EMPTY_LIST;
        }
    	
    	ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();
    	
        Filter filter = filterFac.equals(this.nestedSourceExpression, filterFac
                .literal(foreignKeyValue));
        // get all the nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = source.getFeatures(filter);
        FeatureIterator<Feature> it = fCollection.features();
        Filter matchingIdFilter = null;
        if (nestedIdExpression.equals(Expression.NIL)) {
			HashSet<FeatureId> featureIds = new HashSet<FeatureId>();
			while (it.hasNext()) {
				Feature f = it.next();
				matchingFeatures.add(f);
				if (f.getIdentifier() != null) {
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
				featureIds.add(Converters.convert(nestedIdExpression
						.evaluate(f), String.class));

			}

			// Find features of the same id from denormalised view
			if (!featureIds.isEmpty()) {
				List<Filter> idFilters = new ArrayList<Filter>(featureIds
						.size());
				for (String id : featureIds) {
					idFilters.add(filterFac.equals(nestedIdExpression,
							filterFac.literal(id)));
				}
				matchingIdFilter = filterFac.or(idFilters);
			}
		}

		it.close();

		if (matchingIdFilter != null) {
			fCollection = source.getFeatures(matchingIdFilter);

			if (fCollection.size() > matchingFeatures.size()) {
				// there are rows of same id from denormalised view
				it = fCollection.features();
				matchingFeatures.clear();
				while (it.hasNext()) {
					matchingFeatures.add(it.next());
				}
				it.close();
			}			
		}

		return matchingFeatures;
    	
    }

    /**
     * Get matching input features that are stored in this mapping using a supplied link value.
     * 
     * @param foreignKeyValue
     * @return The matching input feature
     * @throws IOException
     * @throws IOException
     */
    public List<Feature> getInputFeatures(Object foreignKeyValue, FeatureTypeMapping fMapping)
            throws IOException {
        if (isSameSource()) {
            // if linkField is null, this method shouldn't be called because the mapping
            // should use the same table, and handles it differently
            throw new UnsupportedOperationException(
                    "Link field is missing from feature chaining mapping!");
        }
        if (source == null || !(nestedFeatureType instanceof AttributeExpressionImpl)) {
            assert fMapping != null;

			source = fMapping.getSource();
			if (source == null) {
				LOGGER.info("Feature source for '"
						+ fMapping.getTargetFeature().getName()
						+ "' not found when evaluating filter");
				return Collections.EMPTY_LIST;
			}
			
			nestedIdExpression = fMapping.getFeatureIdExpression();

            // find source expression on nested features side
            List<AttributeMapping> mappings = fMapping
                    .getAttributeMappingsIgnoreIndex(this.nestedTargetXPath);
            if (mappings.size() < 1) {
                throw new IllegalArgumentException("Mapping is missing for: '"
                        + this.nestedTargetXPath + "'!");
            }
            nestedSourceExpression = mappings.get(0).getSourceExpression();
        }

        return getFilteredFeatures(foreignKeyValue);     
    }

    /**
     * Get the maching built features that are stored in this mapping using a supplied link value
     * 
     * @param foreignKeyValue
     * @param reprojection
     *            Reprojected CRS or null
     * @return The matching simple features
     * @throws IOException
     */
    public List<Feature> getFeatures(Object foreignKeyValue,
            CoordinateReferenceSystem reprojection, Feature feature) throws IOException {
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
        Filter filter;

        ArrayList<Feature> matchingFeatures = new ArrayList<Feature>();
        PropertyName propertyName = filterFac.property(this.nestedTargetXPath.toString());

        filter = filterFac.equals(propertyName, filterFac.literal(foreignKeyValue));

        Query query = new Query();
        query.setCoordinateSystemReproject(reprojection);
        query.setFilter(filter);

        // get all the mapped nested features based on the link values
        FeatureCollection<FeatureType, Feature> fCollection = fSource.getFeatures(query);
        FeatureIterator<Feature> iterator = fCollection.features();
        while (iterator.hasNext()) {
            matchingFeatures.add(iterator.next());
        }
        iterator.close();

        return matchingFeatures;
    }

    private FeatureSource<FeatureType, Feature> getMappingSource(Feature feature)
            throws IOException {

        if (mappingSource == null || !(nestedFeatureType instanceof AttributeExpressionImpl)) {
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

    /**
     * @return the nested feature type name
     */
    public Object getNestedFeatureType(Feature feature) {
        Object fTypeValue;
        if (nestedFeatureType instanceof AttributeExpressionImpl) {
            fTypeValue = ((AttributeExpressionImpl) nestedFeatureType).getPropertyName();
        } else {
            fTypeValue = nestedFeatureType.evaluate(feature);
        }
        if (fTypeValue == null) {
            // this could be legitimate, i.e. in polymorphism
            // to evaluate a function with a certain column value
            // if null, don't encode this element
            return null;
        }
        if (!(fTypeValue instanceof Hints)) {
            return Types.degloseName(fTypeValue.toString(), namespaces);
        }
        return fTypeValue;
    }

    public boolean isSameSource() {
        // if the linkField is null, we're meant to work out the nestedFeatureType from
        // the linkElement, which should contain a function. So the value could vary
        // feature per feature. But the linkElement would point to the same data source table
        // if the linkField is null.
        return this.nestedTargetXPath == null;
    }

    public boolean isConditional() {
        // true if the type is depending on a function value, i.e. could be a Function or filter
        return !(nestedFeatureType instanceof AttributeExpressionImpl);
    }

    public FeatureTypeMapping getFeatureTypeMapping(Feature feature) throws IOException {
        FeatureSource<FeatureType, Feature> fSource = getMappingSource(feature);
        if (fSource == null) {
            return null;
        }
        return (fSource instanceof MappingFeatureSource) ? ((MappingFeatureSource) fSource)
                .getMapping() : null;
    }

    public NamespaceSupport getNamespaces() {
        return namespaces;
    }
}
