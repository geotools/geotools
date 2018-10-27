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
package org.geotools.data.complex.config;

import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.data.complex.feature.type.ComplexFeatureTypeFactoryImpl;
import org.geotools.gml3.complex.GmlFeatureTypeRegistryConfiguration;
import org.geotools.xsd.SchemaIndex;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.xml.sax.helpers.NamespaceSupport;

/**
 * A registry of GeoTools {@link AttributeType} and {@link AttributeDescriptor} lazily parsed from
 * the EMF {@link XSDTypeDefinition} and {@link XSDElementDeclaration} added through {@link
 * #addSchemas(SchemaIndex)}.
 *
 * <p>This class is meant to be used in conjunction with {@link EmfComplexFeatureReader}. See {@link
 * AppSchemaDataAccessConfigurator}
 *
 * <p>Usage:
 *
 * <pre>
 * <code>
 * FeatureTypeRegistry registry = new FeatureTypeRegistry();
 * EmfComplexFeatureReader schemaParser = EmfComplexFeatureReader.newInstance();
 * URL schemaLocation1 = ...
 * SchemaIndex schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 * URL schemaLocation2 = ...
 * schemas = schemaParser.parse(schemaLocation1, null);
 * registry.addSchemas(schemas);
 *
 * Name typeName = ...
 * FeatureType ft = (FeatureType)registry.getAttributeType(typeName);
 * </code>
 * </pre>
 *
 * @author Gabriel Roldan
 */
public class AppSchemaFeatureTypeRegistry extends FeatureTypeRegistry {

    public AppSchemaFeatureTypeRegistry() {
        this(null);
    }

    public AppSchemaFeatureTypeRegistry(NamespaceSupport namespaces) {
        super(
                new ComplexFeatureTypeFactoryImpl(),
                new GmlFeatureTypeRegistryConfiguration(
                        namespaces == null ? null : namespaces.getURI("gml")));
    }
}
