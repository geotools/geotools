/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic.catalog.oracle;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.Definition;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.GeometryType;
import org.opengis.feature.type.Name;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * An Oracle specific {@link FeatureTypeMapper} instance
 *
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public class OracleFeatureTypeMapper implements FeatureTypeMapper {

    /** The original typeName */
    private Name originalName;

    /** The mapped typeName (UPPERCASE and less than 30 chars) */
    private String mappedName;

    /** the coordinateReferenceSystem for the geometry */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /** The list of {@link Definition} object defining the mapping */
    private List<Definition> definitions;

    /** The original feature type */
    private SimpleFeatureType wrappedFeatureType;

    /** The oracle specific featureType */
    private SimpleFeatureType oracleFeatureType;

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
        return oracleFeatureType;
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

    /** Create a new {@link OracleFeatureTypeMapper} on top of the original featureType provided */
    public OracleFeatureTypeMapper(SimpleFeatureType featureType) throws CQLException {
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

            // Create the definition to map the original attribute to the Oracle specific one
            final Definition definition =
                    new Definition(originalAttribute, ECQL.toExpression(attributeName), binding);
            definitions.add(definition);
            definitionsMapping.put(attribute.getName(), definition);
        }
        remapFeatureType();
    }

    /**
     * Remap the original featureType on top of the available definitions to create the Oracle
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
                tb.add(
                        definition.getExpression().toString(),
                        definition.getBinding(),
                        coordinateReferenceSystem);
            } else {
                tb.add(definition.getExpression().toString(), definition.getBinding());
            }
        }
        oracleFeatureType = tb.buildFeatureType();
    }

    void setSimpleFeatureSource(SimpleFeatureSource simpleFeatureSource) {
        this.simpleFeatureSource = simpleFeatureSource;
    }

    @Override
    public String remap(String name) {
        String mappedName = name.toUpperCase();
        mappedName = mappedName.length() > 30 ? mappedName.substring(0, 30) : mappedName;
        return mappedName;
    }
}
