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
import org.geotools.filter.FilterTest.MockDataObject;
import org.geotools.filter.expression.AddImpl;
import org.geotools.filter.expression.DivideImpl;
import org.geotools.filter.expression.MultiplyImpl;
import org.geotools.filter.expression.SubtractImpl;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.Function;
import org.opengis.filter.expression.Literal;
import org.opengis.filter.expression.PropertyName;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.PrecisionModel;

/**
 * Unit test for expressions. This is a complimentary test suite with the filter
 * test suite.
 * 
 * @author James MacGill, CCG
 * @author Rob Hranac, TOPP
 *
 * @source $URL$
 */
public class ExpressionTest extends TestCase {
	/** Standard logging instance */
	private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");

	/** Feature on which to preform tests */
	private static SimpleFeature testFeature = null;

	/** Schema on which to preform tests */
	private static SimpleFeatureType testSchema = null;

	static FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2( null );

	boolean set = false;

	/** Test suite for this test case */
	TestSuite suite = null;

	/**
	 * Constructor with test name.
	 * 
	 * @param testName
	 *            DOCUMENT ME!
	 */
	public ExpressionTest(String testName) {
		super(testName);
	}

	/**
	 * Main for test runner.
	 * 
	 * @param args
	 *            arguments to run main
	 */
	public static void main(String[] args) {
		junit.textui.TestRunner.run(suite());
	}

	/**
	 * Required suite builder.
	 * 
	 * @return A test suite for this unit test.
	 */
	public static Test suite() {
		TestSuite suite = new TestSuite(ExpressionTest.class);

		return suite;
	}

	/**
	 * Sets up a schema and a test feature.
	 * 
	 * @throws SchemaException
	 *             If there is a problem setting up the schema.
	 * @throws IllegalFeatureException
	 *             If problem setting up the feature.
	 */
	protected void setUp() throws SchemaException, IllegalAttributeException {
		if (set) {
			return;
		}

		set = true;

		// Create the schema attributes
		LOGGER.finer("creating flat feature...");
		
		SimpleFeatureTypeBuilder ftb = new SimpleFeatureTypeBuilder();
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

		// Creates coordinates for the linestring
		Coordinate[] coords = new Coordinate[3];
		coords[0] = new Coordinate(1, 2);
		coords[1] = new Coordinate(3, 4);
		coords[2] = new Coordinate(5, 6);

		// Builds the test feature
		Object[] attributes = new Object[10];
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

		// Creates the feature itself
		// FlatFeatureFactory factory = new FlatFeatureFactory(testSchema);
		testFeature = SimpleFeatureBuilder.build(testSchema, attributes, null);
		LOGGER.finer("...feature created");
	}

	/**
	 * Tests the attribute expression.
	 * 
	 * @throws IllegalFilterException
	 *             if filters mess up.
	 */
	public void testAttribute() throws IllegalFilterException {
		// Test integer attribute
		Expression testAttribute = new AttributeExpressionImpl(testSchema,
				"testInteger");
		LOGGER.fine("integer attribute expression equals: "
				+ testAttribute.getValue(testFeature));
		assertEquals(new Integer(1002), testAttribute.getValue(testFeature));

		// Test string attribute
		testAttribute = new AttributeExpressionImpl(testSchema, "testString");
		LOGGER.fine("string attribute expression equals: "
				+ testAttribute.getValue(testFeature));
		assertEquals("test string data", testAttribute.getValue(testFeature));
	}

	/**
	 * Tests the attribute expression over an object other than Feature.
	 * 
	 * @throws IllegalFilterException
	 *             if filters mess up.
	 */
	public void testAttributeObject() throws IllegalFilterException {
		MockDataObject testFeature = new MockDataObject(10, "diez");

		// Test integer attribute
		org.opengis.filter.expression.Expression testAttribute = new AttributeExpressionImpl(
				"intVal");

		assertEquals(new Integer(10), testAttribute.evaluate(testFeature));

		// Test string attribute
		testAttribute = new AttributeExpressionImpl("stringVal");

		assertEquals("diez", testAttribute.evaluate(testFeature));
	}

	/**
	 * Tests the literal expression.
	 * 
	 * @throws IllegalFilterException
	 *             if there are problems
	 */
	public void testLiteral() throws IllegalFilterException {
		// Test integer attribute
		Expression testLiteral = new LiteralExpressionImpl(new Integer(1002));
		LOGGER.fine("integer literal expression equals: "
				+ testLiteral.getValue(testFeature));
		assertEquals(new Integer(1002), testLiteral.getValue(testFeature));

		// Test string attribute
		testLiteral = new LiteralExpressionImpl("test string data");
		LOGGER.fine("string literal expression equals: "
				+ testLiteral.getValue(testFeature));
		assertEquals("test string data", testLiteral.getValue(testFeature));
	}

	/**
	 * Tests the literal expression over an object other than Feature.
	 * 
	 * @throws IllegalFilterException
	 *             if there are problems
	 */
	public void testLiteralObject() throws IllegalFilterException {
		MockDataObject testObj = new MockDataObject(1000, "mil");

		// Test integer attribute
		org.opengis.filter.expression.Expression testLiteral = new LiteralExpressionImpl(
				new Integer(1002));

		assertEquals(new Integer(1002), testLiteral.evaluate(testObj));

		// Test string attribute
		testLiteral = new LiteralExpressionImpl("test string data");

		assertEquals("test string data", testLiteral.evaluate(testObj));
	}

	public void testMinFunction() {
	    PropertyName a = ff.property("testInteger");
	    Literal b = ff.literal( 1004.0 );
        Function min = ff.function("min", a, b );
        
	}
	
	/**
	 * Tests the min function expression.
	 * 
	 * @throws IllegalFilterException
	 *             if filter problems
	 */
	public void testMinFunctionOld() throws IllegalFilterException {
	    org.opengis.filter.expression.Expression a, b;
		a = new AttributeExpressionImpl(testSchema, "testInteger");
		b = new LiteralExpressionImpl(new Double(1004));

		Function min = ff.function("min", a, b);

		Object value = min.evaluate(testFeature);
		assertEquals(1002d, ((Double) value).doubleValue(),
				0);

		b = ff.literal(new Double(-100.001));
		min = ff.function("min", a, b);
		
		value = min.evaluate(testFeature);
		assertEquals(-100.001, ((Double) value).doubleValue(), 0);
	}

	public void testNonExistentFunction() {
		try {
			Function nochance = ff.function("%$#%$%#%#$@#%@", (org.opengis.filter.expression.Expression) null);
			assertNull(nochance);
		} catch (RuntimeException re) {
		}

	}

	public void testFunctionNameTrim() throws IllegalFilterException {
		Function min = ff.function("minFunction", ff.literal(2), ff.literal(3));
		assertTrue(min != null);
	}

	/**
	 * Tests the max function expression.
	 * 
	 * @throws IllegalFilterException
	 *             if filter problems
	 */
	public void testMaxFunction() throws IllegalFilterException {
	    org.opengis.filter.expression.Expression a, b;
		a = new AttributeExpressionImpl(testSchema, "testInteger");
		b = new LiteralExpressionImpl(new Double(1004));

		Function max = ff.function("max", a, b);
		assertEquals(1004d, ((Double) max.evaluate(testFeature)).doubleValue(),
				0);

		b = new LiteralExpressionImpl(new Double(-100.001));
		max = ff.function("max", a, b);
		assertEquals(1002d, ((Double) max.evaluate(testFeature)).doubleValue(),
				0);
	}

	/**
	 * Tests the max function expression over other kind of object than Feature.
	 * 
	 * @throws IllegalFilterException
	 *             if filter problems
	 */
	public void testMaxFunctionObject() throws IllegalFilterException {
		MockDataObject testObj = new MockDataObject(10, "diez");
		org.opengis.filter.expression.Expression a = new AttributeExpressionImpl(
				"intVal");
		org.opengis.filter.expression.Expression b = new LiteralExpressionImpl(
				new Double(1004));

		Function max = ff.function("max", a, b);
		assertEquals("max", max.getName());

		Object maxValue = max.evaluate(testObj);
		assertEquals(1004d, ((Double) maxValue).doubleValue(), 0);

		b = new LiteralExpressionImpl(new Double(-100.001));

		max = ff.function("max", a, b);
		maxValue = max.evaluate(testObj);

		assertEquals(10, ((Double) maxValue).doubleValue(), 0);

	}

	public void testIncompleteMathExpression() throws IllegalFilterException {
		Expression testAttribute1 = new LiteralExpressionImpl(new Integer(4));

		MathExpressionImpl mathTest = new AddImpl(null, null);
		mathTest.addLeftValue(testAttribute1);
		try {
			mathTest.getValue(testFeature);
			fail("math expressions should not work if right hand side is not set");
		} catch (IllegalArgumentException ife) {
		}
		mathTest = new AddImpl(null, null);
		mathTest.addRightValue(testAttribute1);
		try {
			mathTest.getValue(testFeature);
			fail("math expressions should not work if left hand side is not set");
		} catch (IllegalArgumentException ife) {
		}
	}

	/**
	 * Tests the math expression.
	 * 
	 * @throws IllegalFilterException
	 *             if filter problems
	 */
	public void testMath() throws IllegalFilterException {
		// Test integer attribute
		Expression testAttribute1 = new LiteralExpressionImpl(new Integer(4));
		Expression testAttribute2 = new LiteralExpressionImpl(new Integer(2));

		// Test addition
		MathExpressionImpl mathTest = new AddImpl(null, null);
		mathTest.addLeftValue(testAttribute1);
		mathTest.addRightValue(testAttribute2);
		LOGGER.fine("math test: " + testAttribute1.getValue(testFeature)
				+ " + " + testAttribute2.getValue(testFeature) + " = "
				+ mathTest.getValue(testFeature));
		assertEquals(new Integer(6), mathTest.evaluate(testFeature,
				Integer.class));

		// Test subtraction
		mathTest = new SubtractImpl(null, null);
		mathTest.addLeftValue(testAttribute1);
		mathTest.addRightValue(testAttribute2);
		LOGGER.fine("math test: " + testAttribute1.getValue(testFeature)
				+ " - " + testAttribute2.getValue(testFeature) + " = "
				+ mathTest.getValue(testFeature));
		assertEquals(new Integer(2), mathTest.evaluate(testFeature,
				Integer.class));

		// Test multiplication
		mathTest = new MultiplyImpl(null, null);
		mathTest.addLeftValue(testAttribute1);
		mathTest.addRightValue(testAttribute2);
		LOGGER.fine("math test: " + testAttribute1.getValue(testFeature)
				+ " * " + testAttribute2.getValue(testFeature) + " = "
				+ mathTest.getValue(testFeature));
		assertEquals(new Integer(8), mathTest.evaluate(testFeature,
				Integer.class));

		// Test division
		mathTest = new DivideImpl(null, null);
		mathTest.addLeftValue(testAttribute1);
		mathTest.addRightValue(testAttribute2);
		LOGGER.fine("math test: " + testAttribute1.getValue(testFeature)
				+ " / " + testAttribute2.getValue(testFeature) + " = "
				+ mathTest.getValue(testFeature));
		assertEquals(new Double(2), mathTest.getValue(testFeature));
	}

	/**
	 * Tests the math expression over other kind of object than Feature.
	 * 
	 * @throws IllegalFilterException
	 *             if filter problems
	 */
	public void testMathObject() throws IllegalFilterException {
		MockDataObject testObject = new MockDataObject(10, "diez");

		// Test integer attribute
		org.opengis.filter.expression.Expression testAttribute1 = new LiteralExpressionImpl(
				new Integer(4));
		org.opengis.filter.expression.Expression testAttribute2 = new LiteralExpressionImpl(
				new Integer(2));

		// Test addition
		MathExpressionImpl mathTest = new AddImpl(null, null);
		mathTest.setExpression1(testAttribute1);
		mathTest.setExpression2(testAttribute2);

		assertEquals(new Integer(6), mathTest.evaluate(testObject,
				Integer.class));

		// Test subtraction
		mathTest = new SubtractImpl(null, null);
		mathTest.setExpression1(testAttribute1);
		mathTest.setExpression2(testAttribute2);

		assertEquals(new Integer(2), mathTest.evaluate(testObject,
				Integer.class));

		// Test multiplication
		mathTest = new MultiplyImpl(null, null);
		mathTest.setExpression1(testAttribute1);
		mathTest.setExpression2(testAttribute2);

		assertEquals(new Integer(8), mathTest.evaluate(testObject,
				Integer.class));

		// Test division
		mathTest = new DivideImpl(null, null);
		mathTest.setExpression1(testAttribute1);
		mathTest.setExpression2(testAttribute2);

		assertEquals(new Double(2), mathTest.evaluate(testObject));
	}
}
