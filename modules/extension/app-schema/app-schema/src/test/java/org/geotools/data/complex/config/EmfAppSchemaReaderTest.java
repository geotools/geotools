/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.eclipse.xsd.XSDComplexTypeDefinition;
import org.eclipse.xsd.XSDElementDeclaration;
import org.eclipse.xsd.XSDTypeDefinition;
import org.geotools.feature.Types;
import org.geotools.feature.type.ComplexFeatureTypeImpl;
import org.geotools.gml3.GML;
import org.geotools.gml3.GMLConfiguration;
import org.geotools.test.AppSchemaTestSupport;
import org.geotools.xml.AppSchemaCatalog;
import org.geotools.xml.AppSchemaConfiguration;
import org.geotools.xml.AppSchemaResolver;
import org.geotools.xml.Configuration;
import org.geotools.xml.SchemaIndex;
import org.geotools.xs.XS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;
import org.opengis.feature.type.ComplexType;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 * 
 * @author Gabriel Roldan (Axios Engineering)
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 * @version $Id$
 *
 *
 *
 * @source $URL$
 * @since 2.4
 */
public class EmfAppSchemaReaderTest extends AppSchemaTestSupport {

    /**
     * Namespace URI of parsed types
     */
    private static final String NS_URI = "http://online.socialchange.net.au";

    @Test
    public void testParseSimpleFeatureType() throws Exception {
        String res = "/test-data/simpleFeature.xsd";
        URL resource = getClass().getResource(res);

        SchemaIndex schemaIndex = EmfAppSchemaReader.newInstance().parse(resource);

        FeatureTypeRegistry parsedTypes = new FeatureTypeRegistry();
        
        try {
            parsedTypes.addSchemas(schemaIndex);
    
            Name typeName = Types.typeName(NS_URI, "simpleFeatureType");
            AttributeType type = parsedTypes.getAttributeType(typeName);
            Assert.assertNotNull(type);
            Assert.assertTrue(type.getClass().getName(), type instanceof ComplexType);
            Assert.assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);
    
            ComplexType ft = (ComplexType) type;
            String local = ft.getName().getLocalPart();
            String uri = ft.getName().getNamespaceURI();
            Assert.assertEquals("simpleFeatureType", local);
            Assert.assertEquals(NS_URI, uri);
    
            List<PropertyDescriptor> attributes = Arrays.asList(((ComplexFeatureTypeImpl) ft)
                    .getTypeDescriptors().toArray(new PropertyDescriptor[0]));
            Assert.assertEquals(8, attributes.size());
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
        finally {
            parsedTypes.disposeSchemaIndexes();
        }
    }

    private void assertSimpleAttribute(AttributeDescriptor descriptor, Name name, Name typeName,
            Class<?> binding, int minOccurs, int maxOccurs) {
        AttributeType type;
        Assert.assertEquals(name, descriptor.getName());
        Assert.assertEquals(minOccurs, descriptor.getMinOccurs());
        Assert.assertEquals(maxOccurs, descriptor.getMaxOccurs());
        Assert.assertTrue(descriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);

        type = (AttributeType) descriptor.getType();
        Assert.assertNotNull(type);
        Assert.assertFalse(type instanceof ComplexType);
        Assert.assertEquals(typeName, type.getName());
        Assert.assertEquals(binding, type.getBinding());
        // they're prebuilt types, does not contains the emf information
        // assertTrue(type.getUserData(EmfAppSchemaReader.EMF_USERDATA_KEY)
        // instanceof XSDTypeDefinition);
    }

    @Test
    public void testComplexFeatureType() throws Exception {
        String res = "/test-data/complexFeature.xsd";
        URL resource = getClass().getResource(res);
        SchemaIndex schemaIndex = EmfAppSchemaReader.newInstance().parse(resource);
        
        FeatureTypeRegistry typeRegistry = new FeatureTypeRegistry();
        try {
            typeRegistry.addSchemas(schemaIndex);
            
            Name typeName = Types.typeName(NS_URI, "wq_plus_Type");
            AttributeType type = (AttributeType) typeRegistry.getAttributeType(typeName);
            Assert.assertTrue(type instanceof FeatureType);
            Assert.assertFalse(type instanceof SimpleFeatureType);
            Assert.assertEquals(typeName, type.getName());
            Assert.assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);
    
            FeatureType wq_plus_Type = (FeatureType) type;
    
            // I do not think types have default geometries any more.
            // assertNotNull(wq_plus_Type.getDefaultGeometry());
            Assert.assertNotNull(wq_plus_Type.getSuper());
            typeName = Types.typeName(GML.NAMESPACE, GML.AbstractFeatureType.getLocalPart());
            Assert.assertEquals(typeName, wq_plus_Type.getSuper().getName());
            Assert.assertNotNull(wq_plus_Type.getDescriptors());
            Assert.assertEquals(8, ((ComplexFeatureTypeImpl) wq_plus_Type).getTypeDescriptors().size());
    
            Name name = Types.typeName(NS_URI, "wq_plus");
            AttributeDescriptor wqPlusDescriptor = typeRegistry.getDescriptor(name, null);
            Assert.assertNotNull(wqPlusDescriptor);
            Assert.assertEquals(name, wqPlusDescriptor.getName());
            Assert.assertSame(wq_plus_Type, wqPlusDescriptor.getType());
            Assert.assertTrue(wqPlusDescriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);
    
            typeName = Types.typeName(NS_URI, "measurementType");
            type = typeRegistry.getAttributeType(typeName);
            Assert.assertTrue(type instanceof ComplexType);
            Assert.assertFalse(type instanceof FeatureType);
            Assert.assertTrue(type.getUserData().get(XSDTypeDefinition.class) instanceof XSDComplexTypeDefinition);
    
            ComplexType measurementType = (ComplexType) type;
            Assert.assertEquals(typeName, measurementType.getName());
            Assert.assertTrue(measurementType.isIdentified());
            Assert.assertFalse(measurementType.isAbstract());
            Assert.assertEquals(2, measurementType.getDescriptors().size());
    
            name = Types.typeName(NS_URI, "measurement");
            AttributeDescriptor descriptor;
            descriptor = (AttributeDescriptor) Types.descriptor(wq_plus_Type, name);
            Assert.assertNotNull(descriptor);
            Assert.assertEquals(name, descriptor.getName());
            Assert.assertNotNull(descriptor.getType());
            Assert.assertSame(measurementType, descriptor.getType());
            Assert.assertEquals(0, descriptor.getMinOccurs());
            Assert.assertEquals(Integer.MAX_VALUE, descriptor.getMaxOccurs());
            Assert.assertTrue(descriptor.getUserData().get(XSDElementDeclaration.class) instanceof XSDElementDeclaration);
    
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
        finally {
            typeRegistry.disposeSchemaIndexes();
        }
    }

    @Test
    public void testSimpleAttributeFromComplexDeclaration() throws Exception {
        String res = "/test-data/complexFeature.xsd";
        URL resource = getClass().getResource(res);
        SchemaIndex schemaIndex = EmfAppSchemaReader.newInstance().parse(resource);

        FeatureTypeRegistry registry = new FeatureTypeRegistry();
        try {
            registry.addSchemas(schemaIndex);
    
            Name tcl = Types.typeName(NS_URI, "TypedCategoryListType");
            AttributeType typedCategoryListType = registry.getAttributeType(tcl);
            Assert.assertNotNull(typedCategoryListType);
            Assert.assertTrue(typedCategoryListType instanceof ComplexType);
    
            AttributeType superType = typedCategoryListType.getSuper();
            Assert.assertNotNull(superType);
            Name superName = superType.getName();
            Assert.assertEquals(XS.STRING.getNamespaceURI(), superName.getNamespaceURI());
            Assert.assertEquals(XS.STRING.getLocalPart(), superName.getLocalPart());
    
            Assert.assertNotNull(typedCategoryListType.getUserData().get(XSDTypeDefinition.class));
        }
        finally {
            registry.disposeSchemaIndexes();
        }
    }
    
    /**
     * Test we can find that GeoSciML 2.0 depends on GML 3.1.
     */
    @Test
    public void findGml31Configuration() {
        AppSchemaConfiguration configuration = new AppSchemaConfiguration(
                "urn:cgi:xmlns:CGI:GeoSciML:2.0",
                "http://www.geosciml.org/geosciml/2.0/xsd/geosciml.xsd", new AppSchemaResolver());
        Configuration gmlConfiguration = EmfAppSchemaReader.findGmlConfiguration(configuration);
        Assert.assertNotNull(gmlConfiguration);
        Assert.assertEquals(new GMLConfiguration(), gmlConfiguration);
    }

    /**
     * Test we can find that GeoSciML 3.0rc1 depends on GML 3.2.
     */
    @Test
    public void findGml32Configuration() {
        AppSchemaConfiguration configuration = new AppSchemaConfiguration(
                "urn:cgi:xmlns:CGI:GeoSciML-Core:3.0.0",
                "https://www.seegrid.csiro.au/subversion/GeoSciML/branches/3.0.0_rc1_gml3.2/geosciml-core/3.0.0/xsd/geosciml-core.xsd",
                new AppSchemaResolver());
        Configuration gmlConfiguration = EmfAppSchemaReader.findGmlConfiguration(configuration);
        Assert.assertNotNull(gmlConfiguration);
        Assert.assertEquals(new org.geotools.gml3.v3_2.GMLConfiguration(), gmlConfiguration);
    }
    
    /**
     * Test when secondary schemaUri contains non GML schema used in anyType from primary schema.
     */
    @Test
	public void testNonGMLConfiguration() {
		AppSchemaCatalog catalog = AppSchemaCatalog.build(getClass()
				.getResource("/test-data/mappedPolygons.oasis.xml"));
		AppSchemaConfiguration configuration = new AppSchemaConfiguration(
				"http://www.opengis.net/swe/2.0",
				"http://schemas.opengis.net/sweCommon/2.0/swe.xsd",
				new AppSchemaResolver(catalog));
		Configuration gmlConfiguration = EmfAppSchemaReader
				.findGmlConfiguration(configuration);
		// Null should be returned, not exception
		// Warning message should be in the log
		Assert.assertNull(gmlConfiguration);
	}

}
