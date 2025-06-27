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

/**
 * Obtains a complete feature type from GeoJSON by parsing beyond first feature and finding attributes that did not
 * appear in the first feature or had null values.
 *
 * <p>If null values are encoded, parsing will stop when all data types are found. In the worst case, all features will
 * be parsed. If null values are not encoded, all features will be parsed anyway.
 */
public class FeatureTypeHandler extends DelegatingHandler<SimpleFeatureType>
        implements IContentHandler<SimpleFeatureType> {

    SimpleFeatureType featureType;

    private boolean inFeatures = false;

    private Map<String, Class<?>> propertyTypes = new LinkedHashMap<>();

    private boolean inProperties;

    private int complexNestingLevel; // level inside a complex property

    private String currentProp;

    private CoordinateReferenceSystem crs;

    private boolean nullValuesEncoded;

    private GeometryDescriptor geom;

    public FeatureTypeHandler(boolean nullValuesEncoded) {
        this.nullValuesEncoded = nullValuesEncoded;
    }

    @Override
    public boolean startObjectEntry(String key) throws ParseException, IOException {
        if (complexNestingLevel > 0) {
            return true;
        }
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
    public boolean startArray() throws ParseException, IOException {

        /*
         * Use FeatureHandler for the first feature only, to initialize the property
         * list and obtain the geometry attribute descriptor
         */
        if (delegate == UNINITIALIZED) {
            delegate = new FeatureHandler(null, new DefaultAttributeIO());
            return true;
        }

        if (inProperties && currentProp != null) {
            if (complexNestingLevel++ == 0) {
                // Record property type
                if (!propertyTypes.containsKey(currentProp)) {
                    // found previously unknown property
                    propertyTypes.put(currentProp, List.class);
                } else {
                    checkValueCompatibility(List.class);
                }
            }
            return true;
        }

        return super.startArray();
    }

    @Override
    public boolean endArray() throws ParseException, IOException {
        super.endArray();

        if (inProperties) {
            --complexNestingLevel;
        }
        return true;
    }

    @Override
    public boolean startObject() throws ParseException, IOException {
        super.startObject();
        if (inProperties && currentProp != null) {
            if (complexNestingLevel++ == 0) {
                // Record property type
                if (!propertyTypes.containsKey(currentProp)) {
                    // found previously unknown property
                    propertyTypes.put(currentProp, Map.class);
                } else {
                    checkValueCompatibility(Map.class);
                }
            }
        }
        return true;
    }

    @Override
    public boolean endObject() throws ParseException, IOException {
        super.endObject();

        if (inProperties && currentProp != null) {
            --complexNestingLevel;
        }
        if (complexNestingLevel > 0) {
            return true;
        }

        if (delegate instanceof FeatureHandler) {
            // obtain a type from the first feature
            SimpleFeature feature = ((FeatureHandler) delegate).getValue();
            if (feature != null) {
                geom = feature.getFeatureType().getGeometryDescriptor();
                List<AttributeDescriptor> attributeDescriptors =
                        feature.getFeatureType().getAttributeDescriptors();
                for (AttributeDescriptor ad : attributeDescriptors) {
                    if (!ad.equals(geom)) {
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

        if (inProperties && complexNestingLevel == 0 && currentProp != null) {
            if (!checkValueCompatibility(value)) {
                return false;
            }
        }

        return super.primitive(value);
    }

    private boolean checkValueCompatibility(Object value) {
        if (value != null) {
            return checkValueCompatibility(value.getClass());
        }
        return true;
    }

    private boolean checkValueCompatibility(Class<?> newType) {
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
                    || Number.class.isAssignableFrom(newType) && knownType == Double.class) {
                propertyTypes.put(currentProp, Double.class);
            } else {
                throw new IllegalStateException("Found conflicting types "
                        + knownType.getSimpleName()
                        + " and "
                        + newType.getSimpleName()
                        + " for property "
                        + currentProp);
            }
        }
        return true;
    }

    /*
     * When null values are encoded there's the possibility of stopping the
     * parsing earlier, i.e.: as soon as all data types and the crs are found.
     */
    private boolean foundAllValues() {
        return nullValuesEncoded && geom != null && crs != null && !thereAreUnknownDataTypes();
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
        if (complexNestingLevel > 0) {
            // Still inside complex property
            return true;
        }

        if (delegate != null && delegate instanceof CRSHandler) {
            crs = ((CRSHandler) delegate).getValue();
            if (crs != null) {
                delegate = NULL;
            }
        } else if (currentProp != null) {
            currentProp = null;
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

        if (geom != null) {
            typeBuilder.add(geom.getLocalName(), geom.getType().getBinding(), crs);
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
