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

package org.geotools.data.complex.config;

import java.net.URL;
import java.util.Arrays;
import java.util.List;

import junit.framework.TestCase;

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Types;
import org.geotools.feature.type.ComplexFeatureTypeImpl;
import org.geotools.gml3.GML;
import org.geotools.xml.SchemaIndex;
import org.geotools.xs.XS;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Gabriel Roldan, Axios Engineering
 * @version $Id$
 *
 * @source $URL$
 * @since 2.4
 */
public class EmfAppSchemaReaderTest extends TestCase {

    /**
     * Namespace URI of parsed types
     */
    private static final String NS_URI = "http://online.socialchange.net.au";

    private EmfAppSchemaReader schemaLoader;

    protected void setUp() throws Exception {
        super.setUp();
        schemaLoader = EmfAppSchemaReader.newInstance();
    }

    protected void tearDown() throws Exception {
        super.tearDown();
        schemaLoader = null;
    }

    public void testParseSimpleFeatureType() throws Exception {
        String res = "/test-data/simpleFeature.xsd";
        URL resource = getClass().getResource(res);

        SchemaIndex schemaIndex = schemaLoader.parse(resource);

        FeatureTypeRegistry parsedTypes = new FeatureTypeRegistry();
        parsedTypes.addSchemas(schemaIndex);

        Name typeName = Types.typeName(NS_URI, "simpleFeatureType");
        AttributeType type = parsedTypes.getAttributeType(typeName);
        assertNotNull(type);
        assertTrue(type.getClass().getName(), type instanceof ComplexType);
        assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);

        ComplexType ft = (ComplexType) type;
        String local = ft.getName().getLocalPart();
        String uri = ft.getName().getNamespaceURI();
        assertEquals("simpleFeatureType", local);
        assertEquals(NS_URI, uri);

        List attributes = Arrays.asList(((ComplexFeatureTypeImpl) ft).getTypeDescriptors().toArray());
        assertEquals(8, attributes.size());
        AttributeDescriptor descriptor;

        descriptor = (AttributeDescriptor) attributes.get(5);
        Name name = Types.typeName(NS_URI, "the_geom");
        typeName = Types.typeName(GML.NAMESPACE, "GeometryPropertyType");
        
        // I do not think this is true anymore, as abstract geometry type
        // is an AttributeTypeImpl
        // assertTrue(descriptor.getType() instanceof GeometryType);

        assertSimpleAttribute(descriptor, name, typeName, Geometry.class, 1, 1);

        descriptor = (AttributeDescriptor) attributes.get(6);
        name = Types.typeName(NS_URI, "stringAtt");
        typeName = Types.typeName(XS.NAMESPACE, XS.STRING.getLocalPart());

        assertSimpleAttribute(descriptor, name, typeName, String.class, 1, 1);

        descriptor = (AttributeDescriptor) attributes.get(7);
        name = Types.typeName(NS_URI, "intAtt");
        typeName = Types.typeName(XS.NAMESPACE, XS.INT.getLocalPart());
        assertSimpleAttribute(descriptor, name, typeName, Integer.class, 1, 1);
    }

    private void assertSimpleAttribute(AttributeDescriptor descriptor, Name name, Name typeName,
            Class binding, int minOccurs, int maxOccurs) {
        AttributeType type;
        assertEquals(name, descriptor.getName());
        assertEquals(minOccurs, descriptor.getMinOccurs());
        assertEquals(maxOccurs, descriptor.getMaxOccurs());
        assertTrue(descriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);

        type = (AttributeType) descriptor.getType();
        assertNotNull(type);
        assertFalse(type instanceof ComplexType);
        assertEquals(typeName, type.getName());
        assertEquals(binding, type.getBinding());
        // they're prebuilt types, does not contains the emf information
        // assertTrue(type.getUserData(EmfAppSchemaReader.EMF_USERDATA_KEY)
        // instanceof XSDTypeDefinition);
    }

    public void testComplexFeatureType() throws Exception {
        String res = "/test-data/complexFeature.xsd";
        URL resource = getClass().getResource(res);
        SchemaIndex schemaIndex = schemaLoader.parse(resource);
        
        FeatureTypeRegistry typeRegistry = new FeatureTypeRegistry();
        typeRegistry.addSchemas(schemaIndex);
        
        Name typeName = Types.typeName(NS_URI, "wq_plus_Type");
        AttributeType type = (AttributeType) typeRegistry.getAttributeType(typeName);
        assertTrue(type instanceof FeatureType);
        assertFalse(type instanceof SimpleFeatureType);
        assertEquals(typeName, type.getName());
        assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);

        FeatureType wq_plus_Type = (FeatureType) type;

        // I do not think types have default geometries any more.
        // assertNotNull(wq_plus_Type.getDefaultGeometry());
        assertNotNull(wq_plus_Type.getSuper());
        typeName = Types.typeName(GML.NAMESPACE, GML.AbstractFeatureType.getLocalPart());
        assertEquals(typeName, wq_plus_Type.getSuper().getName());
        assertNotNull(wq_plus_Type.getDescriptors());
        assertEquals(8, ((ComplexFeatureTypeImpl) wq_plus_Type).getTypeDescriptors().size());

        Name name = Types.typeName(NS_URI, "wq_plus");
        AttributeDescriptor wqPlusDescriptor = typeRegistry.getDescriptor(name);
        assertNotNull(wqPlusDescriptor);
        assertEquals(name, wqPlusDescriptor.getName());
        assertSame(wq_plus_Type, wqPlusDescriptor.getType());
        assertTrue(wqPlusDescriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);

        typeName = Types.typeName(NS_URI, "measurementType");
        type = typeRegistry.getAttributeType(typeName);
        assertTrue(type instanceof ComplexType);
        assertFalse(type instanceof FeatureType);
        assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);

        ComplexType measurementType = (ComplexType) type;
        assertEquals(typeName, measurementType.getName());
        assertTrue(measurementType.isIdentified());
        assertFalse(measurementType.isAbstract());
        assertEquals(2, measurementType.getDescriptors().size());

        name = Types.typeName(NS_URI, "measurement");
        AttributeDescriptor descriptor;
        descriptor = (AttributeDescriptor) Types.descriptor(wq_plus_Type, name);
        assertNotNull(descriptor);
        assertEquals(name, descriptor.getName());
        assertNotNull(descriptor.getType());
        assertSame(measurementType, descriptor.getType());
        assertEquals(0, descriptor.getMinOccurs());
        assertEquals(Integer.MAX_VALUE, descriptor.getMaxOccurs());
        assertTrue(descriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);

        name = Types.typeName(NS_URI, "result");
        descriptor = (AttributeDescriptor) Types.descriptor(measurementType, name);
        typeName = Types.typeName(XS.NAMESPACE, XS.FLOAT.getLocalPart());
        assertSimpleAttribute(descriptor, name, typeName, Float.class, 1, 1);

        name = Types.typeName(NS_URI, "determinand_description");
        descriptor = (AttributeDescriptor) Types.descriptor(measurementType, name);
        typeName = Types.typeName(XS.NAMESPACE, XS.STRING.getLocalPart());
        assertSimpleAttribute(descriptor, name, typeName, String.class, 1, 1);

        name = Types.typeName(NS_URI, "the_geom");
        descriptor = (AttributeDescriptor) Types.descriptor(wq_plus_Type, name);
        typeName = Types.typeName(GML.NAMESPACE, GML.PointPropertyType.getLocalPart());
        assertSimpleAttribute(descriptor, name, typeName, Point.class, 1, 1);

        name = Types.typeName(NS_URI, "sitename");
        descriptor = (AttributeDescriptor) Types.descriptor(wq_plus_Type, name);
        typeName = Types.typeName(XS.NAMESPACE, XS.STRING.getLocalPart());
        assertSimpleAttribute(descriptor, name, typeName, String.class, 1, Integer.MAX_VALUE);

    }

    public void testSimpleAttributeFromComplexDeclaration() throws Exception {
        String res = "/test-data/complexFeature.xsd";
        URL resource = getClass().getResource(res);
        SchemaIndex schemaIndex = schemaLoader.parse(resource);

        FeatureTypeRegistry registry = new FeatureTypeRegistry();
        registry.addSchemas(schemaIndex);

        Name tcl = Types.typeName(NS_URI, "TypedCategoryListType");
        AttributeType typedCategoryListType = registry.getAttributeType(tcl);
        assertNotNull(typedCategoryListType);
        assertTrue(typedCategoryListType instanceof ComplexType);

        AttributeType superType = typedCategoryListType.getSuper();
        assertNotNull(superType);
        Name superName = superType.getName();
        assertEquals(XS.STRING.getNamespaceURI(), superName.getNamespaceURI());
        assertEquals(XS.STRING.getLocalPart(), superName.getLocalPart());

        assertNotNull(typedCategoryListType.getUserData().get(XSDTypeDefinition.class));
    }
}
