/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.postgis;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.Definition;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.gce.imagemosaic.catalog.oracle.FeatureTypeMapper;
import org.geotools.jdbc.JDBCDataStore;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * A Postgis specific {@link FeatureTypeMapper} instance
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class PostgisFeatureTypeMapper implements FeatureTypeMapper {

    private int srID = 0;

    private static final int MAX_LENGTH = 63;

    /** The original typeName */
    private Name originalName;

    /** The mapped typeName (less than {@link #MAX_LENGTH} chars) */
    private String mappedName;

    /** the coordinateReferenceSystem for the geometry */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /** The list of {@link Definition} object defining the mapping */
    private List<Definition> definitions;

    /** The original feature type */
    private SimpleFeatureType wrappedFeatureType;

    /** The postgis specific featureType */
    private SimpleFeatureType postgisFeatureType;

    /** The mapping between an attributeName and its definition */
    private Map<Name, Definition> definitionsMapping;

    /** The {@link SimpleFeatureSource} available for that type */
    private SimpleFeatureSource simpleFeatureSource;

    @Override
    public Name getName() {
        return originalName;
    }

    @Override
    public String getMappedName() {
        return mappedName;
    }

    @Override
    public List<Definition> getDefinitions() {
        return definitions;
    }

    @Override
    public SimpleFeatureType getMappedFeatureType() {
        return postgisFeatureType;
    }

    @Override
    public SimpleFeatureType getWrappedFeatureType() {
        return wrappedFeatureType;
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    @Override
    public SimpleFeatureSource getSimpleFeatureSource() {
        return simpleFeatureSource;
    }

    int getSrID() {
        return srID;
    }

    void setSrID(int srID) {
        this.srID = srID;
    }

    /** Create a new {@link PostgisFeatureTypeMapper} on top of the original featureType provided */
    public PostgisFeatureTypeMapper(SimpleFeatureType featureType) throws CQLException {
        wrappedFeatureType = featureType;
        originalName = featureType.getName();
        mappedName = originalName.getLocalPart();
        mappedName = remap(mappedName);
        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        definitions = new LinkedList<Definition>();
        definitionsMapping = new HashMap<Name, Definition>();

        // Loop over attributes and prepare the definitions
        for (AttributeDescriptor attribute : attributes) {
            final String originalAttribute = attribute.getLocalName();
            final AttributeType type = attribute.getType();
            final Class<?> binding = type.getBinding();
            String attributeName = remap(originalAttribute);

            // Create the definition to map the original attribute to the Postgis specific one
            final Definition definition =
                    new Definition(originalAttribute, ECQL.toExpression(attributeName), binding);
            definitions.add(definition);
            definitionsMapping.put(attribute.getName(), definition);
        }
        remapFeatureType();
    }

    /**
     * Remap the original featureType on top of the available definitions to create the Postgis
     * specific featureType
     */
    private void remapFeatureType() {
        final SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName(mappedName);
        final List<AttributeDescriptor> descriptors = wrappedFeatureType.getAttributeDescriptors();

        // Loop over the attribute descriptors
        for (AttributeDescriptor descriptor : descriptors) {

            // Get main properties (name and type)
            Name name = descriptor.getName();
            Definition definition = definitionsMapping.get(name);
            AttributeType type = descriptor.getType();
            if (type instanceof GeometryType) {
                coordinateReferenceSystem = ((GeometryType) type).getCoordinateReferenceSystem();
                Map<Object, Object> userData = descriptor.getUserData();
                if (userData != null && !userData.isEmpty()) {
                    Set<Object> keys = userData.keySet();
                    for (Object key : keys) {
                        Object value = userData.get(key);
                        tb.userData(key, value);
                        if (key instanceof String) {
                            String id = (String) key;
                            if (id.equalsIgnoreCase(JDBCDataStore.JDBC_NATIVE_SRID)
                                    && value != null) {
                                srID = (Integer) value;
                            }
                        }
                    }
                }
                tb.add(
                        definition.getExpression().toString(),
                        definition.getBinding(),
                        coordinateReferenceSystem);
            } else {
                tb.add(definition.getExpression().toString(), definition.getBinding());
            }
        }
        postgisFeatureType = tb.buildFeatureType();
    }

    void setSimpleFeatureSource(SimpleFeatureSource simpleFeatureSource) {
        this.simpleFeatureSource = simpleFeatureSource;
    }

    @Override
    public String remap(String name) {
        String mappedName = name;
        mappedName =
                mappedName.length() >= MAX_LENGTH
                        ? mappedName.substring(0, MAX_LENGTH)
                        : mappedName;
        return mappedName;
    }
}
