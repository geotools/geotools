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
package org.geotools.validation.attributes;

import junit.framework.TestCase;

import org.geotools.data.DataUtilities;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.validation.RoadValidationResults;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * RangeFeatureValidationTest purpose.
 * <p>
 * Description of RangeFeatureValidationTest ...
 * <p>
 * Capabilities:
 * <ul>
 * <li></li>
 * </ul>
 * Example Use:
 * <pre><code>
 * RangeFeatureValidationTest x = new RangeFeatureValidationTest(...);
 * </code></pre>
 * 
 * @author bowens, Refractions Research, Inc.
 * @author $Author: sploreg $ (last modification)
 *
 * @source $URL$
 * @version $Id$
 */
public class RangeFeatureValidationTest extends TestCase {
	private GeometryFactory gf;
	private RoadValidationResults results;
	private SimpleFeatureType type;
	private SimpleFeature feature;
	RangeValidation test;
	/**
	 * Constructor for RangeFeatureValidationTest.
	 * @param arg0
	 */
	public RangeFeatureValidationTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();	
		gf = new GeometryFactory();
		
		test = new RangeValidation();		
		test.setAttribute("name");
		test.setTypeRef("road");
		test.setName( "JUnit" );
		test.setName( "test used for junit test "+getName() );
		
		type = DataUtilities.createType(getName()+".road",
		"id:0,*geom:LineString,name:String");
		
		results = new RoadValidationResults();
	}

	private SimpleFeature road( String road, int id, String name ) throws IllegalAttributeException {
		Coordinate[] coords = new Coordinate[]{ new Coordinate(1, 1), new Coordinate( 2, 2), new Coordinate (4, 2), new Coordinate (5, 1)};		
		return SimpleFeatureBuilder.build(type, new Object[] {
				new Integer(id),
				gf.createLineString(coords),
				name,
			},
			type.getTypeName()+"."+road
		);
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		test = null;
		super.tearDown();
	}

	public void testSetName() {
		test.setName("foo");
		assertEquals("foo", test.getName());
	}

	public void testGetName() {
		test.setName("bork");
		assertEquals("bork", test.getName());
	}

	public void testSetDescription() {
		test.setDescription("foo");
		assertEquals("foo", test.getDescription());
	}

	public void testGetDescription() {
		test.setDescription("bork");
		assertEquals("bork", test.getDescription());
	}

	public void testGetPriority() {
		//TODO Implement getPriority().
	}

	public void testGetMax() {
		test.setMax(100);
		assertEquals(100, test.getMax());
	}

	public void testGetMin() {
		test.setMin(10);
		assertEquals(10, test.getMin());
	}

	public void testGetPath() {
		test.setAttribute("path");
		assertEquals("path", test.getAttribute());
	}

	public void testSetMax() {
		test.setMax(500);
		assertEquals(500, test.getMax());

	}

	public void testSetMin() {
		test.setMin(5);
		assertEquals(5, test.getMin());
	}

	public void testSetPath() {
		test.setAttribute("path2");
		assertEquals("path2", test.getAttribute());
	}
	public void testRangeFeatureValidation() throws Exception {
		test.setTypeRef("road");	
		test.setAttribute("id");	
		test.setMin( 0 );
		assertTrue( test.validate(road("rd1", 1,"street"), type, results) );
		assertTrue( test.validate(road("rd2", 0,"avenue"), type, results) );
		assertFalse( test.validate(road("rd3", -1,"alley"), type, results) );			
	}
		
}
