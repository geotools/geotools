/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geojson.feature;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geojson.DelegatingHandler;
import org.geotools.geojson.IContentHandler;
import org.json.simple.parser.ParseException;
import org.locationtech.jts.geom.Geometry;

/**
 * Obtains a complete feature type from GeoJSON by parsing beyond first feature and finding
 * attributes that did not appear in the first feature or had null values.
 *
 * <p>If null values are encoded, parsing will stop when all data types are found. In the worst
 * case, all features will be parsed. If null values are not encoded, all features will be parsed
 * anyway.
 */
public class FeatureTypeHandler extends DelegatingHandler<SimpleFeatureType>
        implements IContentHandler<SimpleFeatureType> {

    SimpleFeatureType featureType;

    private boolean inFeatures = false;

    private Map<String, Class<?>> propertyTypes = new LinkedHashMap<>();

    private boolean inProperties;

    private String currentProp;

    private CoordinateReferenceSystem crs;

    private boolean nullValuesEncoded;

    private boolean checkAllGeometryTypes;

    private String geomLocalName;

    private Class<?> geomBinding;

    public FeatureTypeHandler(boolean nullValuesEncoded, boolean checkAllGeometryTypes) {
        this.nullValuesEncoded = nullValuesEncoded;
        this.checkAllGeometryTypes = checkAllGeometryTypes;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if ("crs".equals(key)) {
            delegate = new CRSHandler();
            return true;
        }
        if ("features".equals(key)) {
            delegate = UNINITIALIZED;
            inFeatures = true;
            return true;
        }
        if (inFeatures && delegate == NULL) {
            if ("properties".equals(key)) {
                inProperties = true;
                return true;
            }
            if (inProperties) {
                if (!propertyTypes.containsKey(key)) {
                    // found previously unknown property
                    propertyTypes.put(key, Object.class);
                }
                currentProp = key;
                return true;
            }
        }
        return super.startObjectEntry(key);
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        /*
         * If we aren't checking all geometry types then use FeatureHandler for the first feature only.
         * FeatureHandler is used to initialize the property list and infer the geometry attribute descriptor.
         */
        if (inFeatures
                && (delegate == UNINITIALIZED || checkAllGeometryTypes)
                && !(delegate instanceof FeatureHandler)) {
            delegate = new FeatureHandler(null, new DefaultAttributeIO());
        }

        return super.startObject();
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        super.endObject();

        if (delegate instanceof FeatureHandler) {
            // obtain a type from the first feature
            SimpleFeature feature = ((FeatureHandler) delegate).getValue();
            if (feature != null) {
                /*
                 * Set the geometry binding if it is unset.  If it is set and the new
                 * type doesn't match the previous type, use Geometry as the type and
                 * stop checking geometry types for subsequent features.
                 */
                GeometryDescriptor geometryDescriptor =
                        feature.getFeatureType().getGeometryDescriptor();
                if (geometryDescriptor != null) {
                    geomLocalName = geometryDescriptor.getLocalName();
                    if (geomBinding == null) {
                        geomBinding = geometryDescriptor.getType().getBinding();
                    } else if (geomBinding != geometryDescriptor.getType().getBinding()) {
                        geomBinding = Geometry.class;
                        checkAllGeometryTypes = false;
                    }
                }
                List<AttributeDescriptor> attributeDescriptors =
                        feature.getFeatureType().getAttributeDescriptors();
                for (AttributeDescriptor ad : attributeDescriptors) {
                    if (!ad.equals(geometryDescriptor)) {
                        propertyTypes.put(ad.getLocalName(), ad.getType().getBinding());
                    }
                }
                delegate = NULL;

                if (foundAllValues()) {
                    buildType();
                    return false;
                }
            }
        }

        return true;
    }

    @Override
    public boolean primitive(Object value) throws ParseException, IOException {

        if (value != null) {
            Class<?> newType = value.getClass();
            if (currentProp != null) {
                Class<?> knownType = propertyTypes.get(currentProp);
                if (knownType == Object.class) {
                    propertyTypes.put(currentProp, newType);

                    if (foundAllValues()) {
                        // found the last unknown type, stop parsing
                        buildType();
                        return false;
                    }
                } else if (knownType != newType) {
                    if (Number.class.isAssignableFrom(knownType) && newType == Double.class
                            || (Number.class.isAssignableFrom(newType)
                                    && knownType == Double.class)) {
                        propertyTypes.put(currentProp, Double.class);
                    } else {
                        throw new IllegalStateException(
                                "Found conflicting types "
                                        + knownType.getSimpleName()
                                        + " and "
                                        + newType.getSimpleName()
                                        + " for property "
                                        + currentProp);
                    }
                }
            }
        }

        return super.primitive(value);
    }

    /*
     * When null values are encoded there's the possibility of stopping the
     * parsing earlier, i.e.: as soon as all data types and the crs are found.
     */
    private boolean foundAllValues() {
        return !checkAllGeometryTypes
                && nullValuesEncoded
                && geomLocalName != null
                && geomBinding != null
                && crs != null
                && !thereAreUnknownDataTypes();
    }

    private boolean thereAreUnknownDataTypes() {

        for (Class<?> clazz : propertyTypes.values()) {
            if (clazz == Object.class) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean endObjectEntry() throws ParseException, IOException {

        super.endObjectEntry();

        if (delegate != null && delegate instanceof CRSHandler) {
            crs = ((CRSHandler) delegate).getValue();
            if (crs != null) {
                delegate = NULL;
            }
        } else if (currentProp != null) {
            currentProp = null;
        } else if (inProperties) {
            inProperties = false;
        }
        return true;
    }

    @Override
    public void endJSON() throws ParseException, IOException {
        buildType();
    }

    private void buildType() {

        SimpleFeatureTypeBuilder typeBuilder = new SimpleFeatureTypeBuilder();
        typeBuilder.setName("feature");
        typeBuilder.setNamespaceURI("http://geotools.org");

        if (geomLocalName != null && geomBinding != null) {
            typeBuilder.add(geomLocalName, geomBinding, crs);
        }

        if (propertyTypes != null) {
            Set<Entry<String, Class<?>>> entrySet = propertyTypes.entrySet();
            for (Entry<String, Class<?>> entry : entrySet) {
                Class<?> binding = entry.getValue();
                if (binding.equals(Object.class)) {
                    binding = String.class;
                }
                typeBuilder.add(entry.getKey(), binding);
            }
        }

        if (crs != null) {
            typeBuilder.setCRS(crs);
        }

        featureType = typeBuilder.buildFeatureType();
    }

    @Override
    public SimpleFeatureType getValue() {
        return featureType;
    }
}
