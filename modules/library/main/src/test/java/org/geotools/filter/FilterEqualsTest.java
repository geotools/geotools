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
package org.geotools.filter;

import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.Or;
import org.opengis.filter.PropertyIsEqualTo;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.spatial.Disjoint;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Unit test for testing filters equals method.
 *
 * @author Chris Holmes, TOPP
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 *
 *
 * @source $URL$
 */            
public class FilterEqualsTest extends TestCase {
    
    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");
    
    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    private Expression testExp1;
    private Expression testExp2;
    private Expression testExp3;
    private Expression testExp4;
    private Filter tFilter1;
    private Filter tFilter2;

  /** Feature on which to preform tests */
    private static SimpleFeature testFeature = null;

    /** Schema on which to preform tests */
    private static SimpleFeatureType testSchema = null;
    boolean set = false;
    
    /** 
     * Constructor with test name.
     */
    public FilterEqualsTest(String testName) {
        super(testName);
    }        
    
    /** 
     * Main for test runner.
     */
    public static void main(String[] args) {
        org.geotools.util.logging.Logging.GEOTOOLS.forceMonolineConsoleOutput();
        junit.textui.TestRunner.run(suite());
    }
    
    /** 
     * Required suite builder.
     * @return A test suite for this unit test.
     */
    public static Test suite() {
        
        TestSuite suite = new TestSuite(FilterEqualsTest.class);
        return suite;
    }
    
  
    /**
     * Sets up a schema and a test feature.
     *
     * @throws SchemaException If there is a problem setting up the schema.
     * @throws IllegalAttributeException If problem setting up the feature.
     */
    protected void setUp() throws SchemaException, IllegalAttributeException {
        if (set) {
            return;
        }

        set = true;
        
        SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
        ftb.setCRS(null);
    	ftb.add("testGeometry", LineString.class);
    	ftb.add("testBoolean", Boolean.class);
    	ftb.add("testCharacter", Character.class);
    	ftb.add("testByte", Byte.class);
    	ftb.add("testShort", Short.class);
    	ftb.add("testInteger", Integer.class);
    	ftb.add("testLong", Long.class);
    	ftb.add("testFloat", Float.class);
    	ftb.add("testDouble", Double.class);
    	ftb.add("testString", String.class);
    	ftb.add("testZeroDouble", Double.class);
    	ftb.setName("testSchema");
        testSchema = ftb.buildFeatureType();

        //LOGGER.finer("added string to feature type");
        // Creates coordinates for the linestring
        Coordinate[] coords = new Coordinate[3];
        coords[0] = new Coordinate(1, 2);
        coords[1] = new Coordinate(3, 4);
        coords[2] = new Coordinate(5, 6);

        // Builds the test feature
        Object[] attributes = new Object[11];
        GeometryFactory gf = new GeometryFactory(new PrecisionModel());
        attributes[0] = gf.createLineString(coords);
        attributes[1] = new Boolean(true);
        attributes[2] = new Character('t');
        attributes[3] = new Byte("10");
        attributes[4] = new Short("101");
        attributes[5] = new Integer(1002);
        attributes[6] = new Long(10003);
        attributes[7] = new Float(10000.4);
        attributes[8] = new Double(100000.5);
        attributes[9] = "test string data";
        attributes[10] = "0.0";

        // Creates the feature itself
        //FlatFeatureFactory factory = new FlatFeatureFactory(testSchema);
        testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
        //LOGGER.finer("...flat feature created");
    }

    public void testLiteralExpressionImplEquals(){
    	try {
    	    Literal testString1 = new LiteralExpressionImpl("test literal");
            Literal testString2 = new LiteralExpressionImpl("test literal");
    	    assertTrue(testString1.equals(testString2));
            
    	    Literal testOtherString = new LiteralExpressionImpl("not test literal");
    	    assertFalse( testString1.equals(testOtherString) );
            
    	    Literal testNumber34 = new LiteralExpressionImpl(new Integer(34));
    	    assertFalse( testString1.equals(testNumber34));
            
            Literal testOtherNumber34 = new LiteralExpressionImpl(new Integer(34));	    
    	    assertTrue(testNumber34.equals(testOtherNumber34));                  
    	}  catch (IllegalFilterException e) {
    	    LOGGER.warning("bad filter " + e.getMessage());
    	}
    }
    
    

    public void testFidFilter(){
        FidFilterImpl ff = new FidFilterImpl();
        ff.addFid("1");
        
        FidFilterImpl ff2 = new FidFilterImpl("1");
        assertNotNull(ff2);
        assertEquals(ff, ff2);
        assertTrue(!ff.equals(null));
        assertTrue(!ff.equals("a string not even a filter"));
        ff2.addFid("2");
        assertTrue(!ff.equals(ff2));
        
        
        
        ff.addFid("2");
        assertEquals(ff, ff2);
    }
    
    public void testExpressionMath(){
        try {
	    MathExpressionImpl testMath1;
	    MathExpressionImpl testMath2;
	    testExp1 = new LiteralExpressionImpl(new Double(5));
	    testExp2 = new LiteralExpressionImpl(new Double(5));
	    testMath1 = new AddImpl(null,null);
	    testMath1.setExpression1(testExp1);
	    testMath1.setExpression2(testExp2);
	    testMath2 =  new AddImpl(null,null);
	    testMath2.setExpression1(testExp2);
	    testMath2.setExpression2(testExp1);
	    assertTrue(testMath1.equals(testMath2));
	    testExp3 = new LiteralExpressionImpl(new Integer(4));
	    testExp4 = new LiteralExpressionImpl(new Integer(4));
	    testMath2.setExpression1(testExp3);
	    assertTrue(!testMath1.equals(testMath2));
	    testMath1.setExpression1(testExp4);
	    assertTrue(testMath1.equals(testMath2));
	    testMath1 = new SubtractImpl(null,null);
	    testMath1.setExpression1(testExp4);
	    testMath1.setExpression1(testExp2);
	    assertTrue(!testMath1.equals(testMath2));
            assertTrue(!testMath1.equals("Random Object that happens to be a string"));
	} catch (IllegalFilterException e){
	    LOGGER.warning("bad filter: " + e.getMessage());
	}
            
    }

    public void testExpressionAttribute()
	throws IllegalFilterException, SchemaException {
    	SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
    	ftb.add("testBoolean", Boolean.class);
    	ftb.add("testString", String.class);
    	ftb.setName("test2");
	
	SimpleFeatureType testSchema2 = ftb.buildFeatureType();
	//FeatureType testSchema2 = feaTypeFactory.getFeatureType(); 
	testExp1 = new AttributeExpressionImpl(testSchema, "testBoolean");
	testExp2 = new AttributeExpressionImpl(testSchema, "testBoolean");
	assertTrue(testExp1.equals(testExp2));
	testExp3 = new AttributeExpressionImpl(testSchema, "testString");
	assertTrue(!testExp1.equals(testExp3));

	
	testExp4 = new AttributeExpressionImpl(testSchema2, "testBoolean");
	assertTrue(!testExp1.equals(testExp4));
 
	testExp1 = new AttributeExpressionImpl(testSchema2, "testBoolean");
	assertTrue(testExp1.equals(testExp4));
		   
    }

    public void testCompareFilter()
	throws IllegalFilterException {
	testExp1 = new LiteralExpressionImpl(new Integer(45));
	testExp2 = new LiteralExpressionImpl(new Integer(45));
	testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
	testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
	PropertyIsEqualTo cFilter1 = ff.equals(testExp1, testExp3);
	PropertyIsEqualTo cFilter2 = ff.equals(testExp1, testExp3);
	assertTrue(cFilter1.equals(cFilter2));
        cFilter2 = ff.equals(testExp2, testExp4);
	assertTrue(cFilter1.equals(cFilter2));
	// see if converters make this work
	cFilter2 = ff.equals(ff.literal(new Double(45.0)), testExp3);
	assertTrue(cFilter1.equals(cFilter2));
	tFilter1 = ff.between(testExp1, testExp2, testExp3);
	assertTrue(!cFilter1.equals(tFilter1));
    }	
    
    public void testBetweenFilter()
	throws IllegalFilterException {
	BetweenFilterImpl bFilter1 = new BetweenFilterImpl();
	BetweenFilterImpl bFilter2 = new BetweenFilterImpl();
	LiteralExpressionImpl testLit1 = new LiteralExpressionImpl(new Integer(55));
	LiteralExpressionImpl testLit2 = new LiteralExpressionImpl(new Integer(55));
	testExp1 = new LiteralExpressionImpl(new Integer(45));
	testExp2 = new LiteralExpressionImpl(new Integer(45));
	testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
	testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
	bFilter1.setExpression1(testExp1);
	bFilter2.setExpression1(testExp2);
	bFilter1.setExpression(testExp3);
	bFilter2.setExpression(testExp4);
	bFilter1.setExpression2(testLit1);
	bFilter2.setExpression2(testLit2);
	assertTrue(bFilter2.equals(bFilter1));
	tFilter1 = ff.equals(org.opengis.filter.expression.Expression.NIL, 
	        org.opengis.filter.expression.Expression.NIL);
	assertTrue(!bFilter2.equals(tFilter1));
	bFilter2.setExpression2(new LiteralExpressionImpl(new Integer(65)));
	assertTrue(!bFilter2.equals(bFilter1));
    }
    
     public void testLikeFilter()
	throws IllegalFilterException {
	 LikeFilterImpl lFilter1 = new LikeFilterImpl();
	 LikeFilterImpl lFilter2 = new LikeFilterImpl();
	 String pattern = "te_st!";
	 String wcMulti = "!";
	 String wcSingle = "_";
	 String escape = "#";
	 testExp2 = new LiteralExpressionImpl(new Integer(45));
	testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
	testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
	lFilter1.setExpression(testExp3);
	lFilter2.setExpression(testExp4);
	lFilter1.setPattern(pattern, wcMulti, wcSingle, escape);
	lFilter2.setPattern(pattern, wcMulti, wcSingle, escape);
	assertTrue(lFilter1.equals(lFilter2));
	lFilter2.setPattern("te__t!", wcMulti, wcSingle, escape);
	assertTrue(!lFilter1.equals(lFilter2));
	lFilter2.setPattern(pattern, wcMulti, wcSingle, escape);
	lFilter2.setExpression(testExp2);
	assertTrue(!lFilter1.equals(lFilter2));
    }	

    public void testLogicFilter()
	throws IllegalFilterException{
	testExp1 = new LiteralExpressionImpl(new Integer(45));
	testExp2 = new LiteralExpressionImpl(new Integer(45));
	testExp3 = new AttributeExpressionImpl(testSchema, "testInteger");
	testExp4 = new AttributeExpressionImpl(testSchema, "testInteger");
	PropertyIsEqualTo cFilter1 = ff.equals(testExp1, testExp2);
	PropertyIsEqualTo cFilter2 = ff.equals(testExp2, testExp4);
	
	org.opengis.filter.Filter logFilter1 = ff.and(cFilter1,cFilter2);
	org.opengis.filter.Filter logFilter2 = ff.and(cFilter1,cFilter2);
	assertTrue(logFilter1.equals(logFilter2));
	
	logFilter1 = ff.not(cFilter2);
	assertTrue(!logFilter1.equals(logFilter2));
	cFilter1 = ff.equals(testExp1, testExp3);
	logFilter2 = ff.not(cFilter1);
	assertTrue(logFilter1.equals(logFilter2));
        assertTrue(!logFilter1.equals(ff.between(testExp1, testExp2, testExp3)));
	Or logFilter3 = ff.or(logFilter1, logFilter2);
	Or logFilter4 = ff.or(logFilter1, logFilter2);
	assertTrue(logFilter3.equals(logFilter4));

	// Questionable behavior.  Is this what we want?
	Or logFilter5 = ff.or(cFilter1, logFilter3);
	// does not change structure of logFilter3
	Or logFilter6 = ff.or(logFilter4, cFilter1);
	// different structure, but same result
	assertTrue(logFilter5.equals(logFilter6));//do we want these equal? 
	assertTrue(logFilter4.equals(logFilter3));//shouldn't they be equal?
    }

    public void testNullFilter()
	throws IllegalFilterException{
	    testExp1 = new AttributeExpressionImpl(testSchema, "testDouble");
	    testExp2 = new AttributeExpressionImpl(testSchema, "testDouble");
	    testExp3 = new  AttributeExpressionImpl(testSchema, "testBoolean");
	    NullFilterImpl nullFilter1 = new NullFilterImpl();
	    NullFilterImpl nullFilter2 = new NullFilterImpl();
	    nullFilter1.setExpression(testExp1);
	    nullFilter2.setExpression(testExp2);
	    assertTrue(nullFilter1.equals(nullFilter2));
	    nullFilter1.setExpression(testExp3);
	    assertTrue(!nullFilter1.equals(nullFilter2));
	    assertTrue(!nullFilter1.equals(new BetweenFilterImpl()));
	}

     public void testGeometryFilter()
	throws IllegalFilterException {
	Disjoint geomFilter1 = ff.disjoint(testExp1, testExp4);
	Disjoint geomFilter2 = ff.disjoint(testExp2, testExp4);
	assertTrue(geomFilter1.equals(geomFilter2));
	geomFilter2 = ff.disjoint(testExp2, new LiteralExpressionImpl(new Double(45)));
	assertTrue(!geomFilter1.equals(geomFilter2));
	tFilter1 = ff.between(ff.literal(1), ff.literal(-1), ff.literal(3));
	assertTrue(!geomFilter1.equals(tFilter1));
    }	
    
}
