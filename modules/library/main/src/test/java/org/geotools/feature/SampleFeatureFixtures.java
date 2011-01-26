/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature;

import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;


/**
 * This is a support class which creates test features for use in testing.
 *
 * @author jamesm
 * @source $URL$
 */
public class SampleFeatureFixtures {
    /**
     * Feature on which to preform tests
     */

    // private Feature testFeature = null;

    /**
     * Creates a new instance of SampleFeatureFixtures
     */
    public SampleFeatureFixtures() {
    }

    public static SimpleFeature createFeature() {
        try {
            SimpleFeatureType testType = createTestType();
            Object[] attributes = createAttributes();

            return SimpleFeatureBuilder.build( testType,attributes,null);
        } catch (Exception e) {
            Error ae = new AssertionError(
                    "Sample Feature for tests has been misscoded");
            ae.initCause(e);
            throw ae;
        }
    }

    public static SimpleFeature createAddressFeature() {
        try {
            return createFeature();

            //FeatureType addressType = createAddressType();
            //Object[] attributes = createComplexAttributes();
            //return addressType.create(attributes);
        } catch (Exception e) {
            Error ae = new AssertionError(
                    "Sample Feature for tests has been misscoded");
            ae.initCause(e);
            throw ae;
        }
    }

    /**
     * creates and returns an array of sample attributes.
     *
     */
    public static Object[] createAttributes() {
        Object[] attributes = new Object[10];
        GeometryFactory gf = new GeometryFactory();
        attributes[0] = gf.createPoint(new Coordinate(1, 2));
        attributes[1] = new Boolean(true);
        attributes[2] = new Character('t');
        attributes[3] = new Byte("10");
        attributes[4] = new Short("101");
        attributes[5] = new Integer(1002);
        attributes[6] = new Long(10003);
        attributes[7] = new Float(10000.4);
        attributes[8] = new Double(100000.5);
        attributes[9] = "test string data";

        return attributes;
    }

    //If we go to factories/protected constructors this won't work, will need
    //to move to a types directory, or use the factory
    public static AttributeDescriptor getChoiceAttrType1() {
        return createChoiceAttrType("choiceTest1", createType1Choices());
    }

    public static AttributeDescriptor[] createType1Choices() {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        AttributeDescriptor[] choices = new AttributeDescriptor[3];
        
        ab.setBinding(Byte.class);
        choices[0] = ab.buildDescriptor( "testByte" );
        
        ab.setBinding(Double.class);
        choices[1] = ab.buildDescriptor( "testDouble");
        
        ab.setBinding(String.class);
        choices[2] = ab.buildDescriptor( "testString");
        
        return choices;
    }

    public static AttributeDescriptor getChoiceAttrType2() {
        AttributeTypeBuilder ab = new AttributeTypeBuilder();
        AttributeDescriptor[] choices = new AttributeDescriptor[2];
        
        ab.setBinding(String.class);
        choices[0] = ab.buildDescriptor( "testString" );
        
        ab.setBinding(Integer.class);
        choices[1] = ab.buildDescriptor( "testInt" );
        
        return createChoiceAttrType("choiceTest2", choices);
    }

    public static AttributeDescriptor createChoiceAttrType(String name,
        AttributeDescriptor[] choices) {
        
        throw new RuntimeException("Figure out how to handle choice");
        //return new ChoiceAttributeType(name, choices);
    }

    public static AttributeDescriptor createGeomChoiceAttrType(String name,
        GeometryDescriptor[] choices) {
        throw new RuntimeException("Figure out how to handle choice");
        //return new ChoiceAttributeType.Geometric(name, choices);
    }

    public static AttributeDescriptor getChoiceGeomType() {
        throw new RuntimeException("Figure out how to handle choice");
        
//        GeometryAttributeType[] choices = new GeometryAttributeType[2];
//        choices[0] = (GeometryAttributeType) AttributeTypeFactory
//            .newAttributeType("testLine", LineString.class);
//        choices[1] = (GeometryAttributeType) AttributeTypeFactory
//            .newAttributeType("testMultiLine", MultiLineString.class);
//
//        return createGeomChoiceAttrType("choiceGeom", choices);
    }

    public static SimpleFeatureType createChoiceFeatureType() {
        throw new RuntimeException("Figure out how to handle choice");
        
//        DefaultFeatureTypeBuilder tb = new DefaultFeatureTypeBuilder();
//        tb.setName( "test" );
//        
//        tb.add(getChoiceAttrType1());
//        tb.add(getChoiceAttrType2());
//        tb.add(getChoiceGeomType());
//        tb.setDefaultGeometry(getChoiceGeomType());
//        
//        return tb.buildFeatureType();
    }

    /**
     * DOCUMENT ME!
     *
     *
     * @throws SchemaException
     */
    public static SimpleFeatureType createTestType() throws SchemaException {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.setName("test");
        
        tb.add("testGeometry", Point.class);
        tb.add("testBoolean", Boolean.class);
        tb.add("testCharacter", Character.class);
        tb.add("testByte", Byte.class);
        tb.add("testShort", Short.class);
        tb.add("testInteger", Integer.class);
        tb.add("testLong", Long.class);
        tb.add("testFloat", Float.class);
        tb.add("testDouble", Double.class);
        tb.add("testString", String.class);
        
        tb.setDefaultGeometry("testGeometry");
        return tb.buildFeatureType();
        
    }
}
