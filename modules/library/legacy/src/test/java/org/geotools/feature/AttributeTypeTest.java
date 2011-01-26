/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Point;

/**
 *
 * @author jamesm
 * @source $URL$
 */
public class AttributeTypeTest extends TestCase {
    
    public AttributeTypeTest(java.lang.String testName) {
        super(testName);
    }
    
    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }
    
    public static Test suite() {
        TestSuite suite = new TestSuite(AttributeTypeTest.class);
        return suite;
    }
    
    public void testAttributeTypeFactory(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        assertNotNull(type);
        type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, true);
        assertNotNull(type);
        type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, false);
        assertNotNull(type);
    }
    
    public void testGetName(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        assertEquals("testAttribute", type.getLocalName());
    }
    
    public void testGetType(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        assertEquals(Double.class, type.getBinding());
    }
    
    
    public void testEquals(){
        AttributeType typeA = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        AttributeType typeB = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        AttributeType typeC = AttributeTypeFactory.newAttributeType("differnetName", Double.class);
        AttributeType typeD = AttributeTypeFactory.newAttributeType("testAttribute", Integer.class);
        AttributeType typeE = AttributeTypeFactory.newAttributeType(null, Integer.class);
        AttributeType typeF = AttributeTypeFactory.newAttributeType(null, Integer.class);
        assertTrue(typeA.equals(typeA));
        assertTrue(typeA.equals(typeB));
        assertTrue(typeE.equals(typeF));
        assertTrue(!typeA.equals(typeC));
        assertTrue(!typeA.equals(typeD));
        assertTrue(!typeA.equals(null));
        assertTrue(!typeA.equals(typeE));
    }
    
    public void testIsNillable(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        assertEquals(true, type.isNillable());
        type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, true);
        assertEquals(true, type.isNillable());
        type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, false);
        assertEquals(false, type.isNillable());
    }
    
    public void testIsGeometry(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class);
        assertEquals(false, type instanceof GeometryAttributeType);
        type = AttributeTypeFactory.newAttributeType("testAttribute", Point.class);
        assertEquals(true, type instanceof GeometryAttributeType);
        type = AttributeTypeFactory.newAttributeType("testAttribute", Geometry.class);
        assertEquals(true, type instanceof GeometryAttributeType);
    }
    
    public void testValidate(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, true);
        try{
            type.validate(new Double(3));
        }
        catch(IllegalArgumentException iae){
            fail();
        }
        try{
            type.validate(new Integer(3));
            fail("Integer should not be validated by a Double type");
        }
        catch(IllegalArgumentException iae){
            
        }
        try{
            type.validate(null);
        }
        catch(IllegalArgumentException iae){
            fail("null should have been allowed as type is Nillable");
        }
        type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, false);
        try{
            type.validate(null);
            type.validate((Double)null);
            fail("null should not have been allowed as type is not Nillable");
        }
        catch(IllegalArgumentException iae){
            
        }
        
        
        type = AttributeTypeFactory.newAttributeType("testAttribute", List.class, true);
        try{
            type.validate(new ArrayList());
        }
        catch(IllegalArgumentException iae){
            fail("decended types should be allowed");
        }
        
        
    }
    
    public void testFeatureConstruction() throws Exception {
        FeatureType a = FeatureTypeFactory.newFeatureType(new AttributeType[]{},"noAttribs");
        FeatureType b = FeatureTypeFactory.newFeatureType(new AttributeType[]{AttributeTypeFactory.newAttributeType("testAttribute", Double.class)},"oneAttribs");
        //Direct construction should never be used like this, however it is the only way to test
        //the code fully
        AttributeType feat = AttributeTypeFactory.newAttributeType( "good",a, false);        
    }
    
    public void testFeatureValidate() throws SchemaException {
//        try{
//            //FeatureType b = FeatureTypeFactory.newFeatureType(new AttributeType[]{AttributeTypeFactory.newAttributeType("testAttribute", Double.class)},"oneAttribs");
//            
//            FeatureType type = FeatureTypeFactory.newFeatureType(new AttributeType[]{},"noAttribs");
//            AttributeType feat = AttributeTypeFactory.newAttributeType("foo",  type);
//            Feature good = type.create(new Object[]{});
//            feat.validate(good);
//        }
//        catch(IllegalAttributeException iae){
//            fail();
//        }
//        Feature bad = null;
//        FeatureType b = FeatureTypeFactory.newFeatureType(new AttributeType[]{AttributeTypeFactory.newAttributeType("testAttribute", Double.class)},"oneAttribs");
//        
//        try{
//            bad = b.create(new Object[]{new Double(4)});
//        }
//        catch(IllegalAttributeException iae){
//            fail();
//        }
//        
    	
//       try{
//            FeatureType type = FeatureTypeFactory.newFeatureType(new AttributeType[]{},"noAttribs");
//            AttributeType feat = AttributeTypeFactory.newAttributeType("foo",  type);
//            feat.validate(bad);
//            fail();
//       }
//       catch(IllegalArgumentException iae){
//           
//       }
           
        
        
    }
    
    
    public void testNumericConstruction(){
        //Direct construction should never be used like this, however it is the only way to test
        //the code fully
        AttributeType num = AttributeTypeFactory.newAttributeType("good",  Double.class, false, 0,new Double(0));
        
        try{
            num = AttributeTypeFactory.newAttributeType("bad",  String.class, false,0,new Double(0));
            fail("Numeric type should not be constructable with type String");
        }
        catch(IllegalArgumentException iae){
        }
    }
    
    
    public void testIsNested(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, true);
//        assertEquals(false, type.isNested());
    }
    
    
    public void testParse(){
        AttributeType type = AttributeTypeFactory.newAttributeType("testAttribute", Double.class, true);
        assertEquals(null, type.parse(null));        
        assertEquals(new Double(1.1),(Double)type.parse(new Double(1.1)));
        
        type = AttributeTypeFactory.newAttributeType("testAttribute", Integer.class, true);
        assertEquals(new Integer(10),(Integer)type.parse(new Integer(10)));
        
        type = AttributeTypeFactory.newAttributeType("testAttribute", String.class, true);
        assertEquals("foo",type.parse("foo"));
        
        type = AttributeTypeFactory.newAttributeType("testAttribute", Number.class, true);
        assertEquals(3d,((Number)type.parse(new Long(3))).doubleValue(),0);
        
        Number number = (Number)type.parse("4.4");
        assertEquals( 4.4d, number.doubleValue(),0);
        type = AttributeTypeFactory.newAttributeType("testAttribute", Number.class, true);
        
        
    }

    public void testParseNumberSubclass() throws Exception {
        
        AttributeType type = AttributeTypeFactory.newAttributeType("testbigdecimal", BigDecimal.class,true);

        Object value = type.parse(new BigDecimal(111.111));
        
        // I modified this test to pass using BigDecimal. -IanS
//        assertEquals(new Double(111.111),value);
//        assertEquals(Double.class,value.getClass());
        
        assertEquals(new BigDecimal(111.111),value);
        assertEquals(BigDecimal.class,value.getClass());
    }
    
    public void testBigNumberSupport() throws Exception {
        AttributeType decimal = AttributeTypeFactory.newAttributeType("decimal", BigDecimal.class,true);
        AttributeType integer = AttributeTypeFactory.newAttributeType("integer", BigInteger.class,true);

        BigDecimal decimalValue = new BigDecimal("200");
        BigInteger integerValue = new BigInteger("200");
        Object[] vals = new Object[] {
          "200",
          new Integer(200),
          new Double(200),
          new Long(200),
          decimalValue
        };
        
        
        // BigDecimal tests
        for (int i = 0, ii = vals.length; i < ii; i++) {
            checkNumericAttributeSetting(decimal, vals[i], decimalValue);
        }
        
        
        // BigInteger tests
        for (int i = 0, ii = vals.length; i < ii; i++) {
            checkNumericAttributeSetting(decimal, vals[i], integerValue);
        }
        
        checkNull(decimal);
        checkNull(integer);
    }
    
    private void checkNull(AttributeType type) {
        if (type.isNillable()) {
            assertNull(type.parse(null));
            type.validate(null);
        }
    }
    
    private void checkNumericAttributeSetting(AttributeType type,Object value,Number expected) {
        Number parsed = (Number) type.parse(value);
        type.validate(parsed);
        assertEquals(parsed.intValue(),expected.intValue());
    }
    
    public void testTextualSupport() throws Exception {
        AttributeType textual = AttributeTypeFactory.newAttributeType("textual",String.class,true);
        
        Object[] vals = new Object[] {
            "stringValue",
            new StringBuffer("stringValue"),
            new Date(System.currentTimeMillis()),
            new Long(1000000)
        };
        for (int i = 0, ii = vals.length; i < ii; i++) {
            Object p = textual.parse(vals[i]);
            textual.validate(p);
            assertEquals(p.getClass(),String.class);
        }
        
        checkNull(textual);
    }
    
    public void textTemporalSupport() throws Exception {
        AttributeType temporal = AttributeTypeFactory.newAttributeType("temporal",Date.class,true);
        
        Date d = new Date();
        
        Object[] vals = new Object[] {
          new String(d.toString()),
          new Date(),
          new Long(d.getTime())
        };
        
        for (int i = 0, ii = vals.length; i < ii; i++) {
            checkTemporalAttributeSetting(temporal, vals[i], d);
        }
        
        checkNull(temporal);
    }
    
    private void checkTemporalAttributeSetting(AttributeType type,Object value,Date expected) {
        Object p = type.parse(value);
        type.validate(p);
        assertEquals(expected,p);
    }
    
    
    
}
