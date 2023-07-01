/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.AttributeType;
import org.geotools.api.feature.type.GeometryType;
import org.geotools.api.feature.type.Name;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.transform.Definition;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.text.cql2.CQLException;
import org.geotools.filter.text.ecql.ECQL;

/**
 * An abstract class which groups common attributes and methods to remap original FeatureType onto
 * the new one
 */
public abstract class AbstractFeatureTypeMapper implements FeatureTypeMapper {

    /** The original typeName */
    protected Name originalName;

    /** The mapped typeName (UPPERCASE and less than 30 chars) */
    protected String mappedName;

    /** the coordinateReferenceSystem for the geometry */
    protected CoordinateReferenceSystem coordinateReferenceSystem;

    /** The list of {@link Definition} object defining the mapping */
    protected List<Definition> definitions;

    /** The original feature type */
    protected SimpleFeatureType wrappedFeatureType;

    /** The oracle specific featureType */
    protected SimpleFeatureType mappedFeatureType;

    /** The mapping between an attributeName and its definition */
    protected Map<Name, Definition> definitionsMapping;

    /** The {@link SimpleFeatureSource} available for that type */
    protected SimpleFeatureSource simpleFeatureSource;

    protected int maxLength;

    protected AbstractFeatureTypeMapper(SimpleFeatureType featureType, int maxLength)
            throws CQLException {
        wrappedFeatureType = featureType;
        this.maxLength = maxLength;
        originalName = featureType.getName();
        mappedName = originalName.getLocalPart();
        mappedName = remap(mappedName);
        List<AttributeDescriptor> attributes = featureType.getAttributeDescriptors();
        definitions = new LinkedList<>();
        definitionsMapping = new HashMap<>();
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
    }

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
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    @Override
    public SimpleFeatureType getMappedFeatureType() {
        return mappedFeatureType;
    }

    @Override
    public SimpleFeatureType getWrappedFeatureType() {
        return wrappedFeatureType;
    }

    @Override
    public SimpleFeatureSource getSimpleFeatureSource() {
        return simpleFeatureSource;
    }

    @Override
    public String remap(String name) {
        return remap(name, maxLength);
    }

    protected String remap(String name, int maxLength) {
        String mappedName = name;
        mappedName =
                mappedName.length() >= maxLength ? mappedName.substring(0, maxLength) : mappedName;
        return mappedName;
    }

    public void setSimpleFeatureSource(SimpleFeatureSource simpleFeatureSource) {
        this.simpleFeatureSource = simpleFeatureSource;
    }

    /**
     * Remap the original featureType on top of the available definitions to create the database
     * specific featureType
     */
    protected void remapFeatureType() {
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
                remapGeometryAttribute(tb, definition, descriptor, type);
            } else {
                tb.add(definition.getExpression().toString(), definition.getBinding());
            }
        }
        mappedFeatureType = tb.buildFeatureType();
    }

    /**
     * Remap the original GeomtryType on top of the available definitions to create the database
     * specific featureType
     */
    protected void remapGeometryAttribute(
            SimpleFeatureTypeBuilder tb,
            Definition definition,
            AttributeDescriptor descriptor,
            AttributeType type) {
        tb.add(
                definition.getExpression().toString(),
                definition.getBinding(),
                coordinateReferenceSystem);
    }
}
