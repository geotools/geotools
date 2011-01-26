/*
 *    GeoTools - OpenSource mapping toolkit
 *    http://geotools.org
 *    (C) 2005-2006, GeoTools Project Managment Committee (PMC)
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
package org.geotools.data.edigeo;

import java.io.IOException;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import junit.framework.TestCase;

public class EdigeoVECTest extends TestCase {
	private EdigeoVEC eVecS1 ;
	private EdigeoVEC eVecT1 ;
	private EdigeoVEC eVecT3 ;
	private String coord = null;
	
	@Before protected void setUp() throws Exception {
		super.setUp();
		// Spaghetti vector object handled 
		eVecS1 = new EdigeoVEC(EdigeoTestUtils.fileName("EDAB01S1.VEC"));
		
		// Topologic vector object handled
		eVecT1 = new EdigeoVEC(EdigeoTestUtils.fileName("EDAB01T1.VEC"));
		
		// FIXME : SECTION_id objects should be topologic according to the EDigeo specification
		// However they are never described as well, so handled as spaghetti Objects
		eVecT3 = new EdigeoVEC(EdigeoTestUtils.fileName("EDAB01T3.VEC"));
	}
	
	@After protected void tearDown() throws Exception {
		eVecS1 = null;
		eVecT1 = null;
		super.tearDown();
	}
	
	@Test public void testGetValue() { 
		assertEquals("CHAMBERY should be the value of this line", "CHAMBERY", 
				eVecS1.getValue("ATVST08:CHAMBERY", "ATVST"));
		// test with a bad number of characters specified
		assertEquals("CHAMBERY should be the value of this line, even if the " +
				"specified length is out of range", "CHAMBERY",
				eVecS1.getValue("ATVST30:CHAMBERY", "ATVST"));
	}

	@Test public void testGetType() {
		List<Coordinate[]> geoms = null;
		try {
			geoms = eVecS1.getType("Noeud_434505");
			assertEquals("Node should have only one coordinate", 1, geoms.get(0).length);
			
			geoms = null;
			geoms = eVecS1.getType("Arc_647020");
			assertTrue("Arc should have more than one coordinate", geoms.get(0).length > 1);
			
			geoms = null;
			geoms = eVecS1.getType("Face_647020");
			assertTrue("Face should be componed from at least one Arc", geoms.size() > 0 && geoms.get(0).length > 1);
			
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
	}
	
	@Test public void testGetXY() {
		String line = "CORCC21:+880582.18;+67265.43;";
		coord = eVecS1.getXY(line);
		assertEquals("880582.18 67265.43 ", coord);
	}
	
	@Test public void testParseCoordinate() {
		if (coord == null)
			testGetXY();
		Coordinate coordinate = eVecS1.parseCoordinate(coord);
		assertTrue("coordinate should be an instance of Coordinate",coordinate instanceof Coordinate);
		assertEquals("X value of the coordinate should be 880582.18", 880582.18, coordinate.x);
		assertEquals("Y value of the coordinate should be 67265.43", 67265.43, coordinate.y);
	}
	
	@Test public void testCreateGeometry() {
		List<Coordinate[]> coordList = null;
		Geometry geom = null;
		String idObj = null;
		
		/**
		 * Test for spaghetti edigeo objects. No needs to construct geometry from edges.
		 */
		// Create geometry for a BORNE_id edigeo object with id Objet_434514 (Point)
		try {
			idObj = "Objet_434514";
			eVecS1.geoType = "POINT";
			coordList = eVecS1.getRelation(idObj);
			geom = eVecS1.createGeometry(coordList, idObj);
			assertTrue("geom should be an instance of Point", geom instanceof Point);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		// Create geometry for a TLINE_id edigeo object with id Objet_3351611 (LineString)
		try {
			idObj = "Objet_3351611";
			eVecS1.geoType = "LINESTRING";
			coordList = eVecS1.getRelation(idObj);
			geom = eVecS1.createGeometry(coordList, idObj);
			assertTrue("geom should be an instance of LineString", geom instanceof LineString);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		// Create geometry for a COMMUNE_id edigeo object with id Objet_647020 (Polygon)
		try {
			idObj = "Objet_647020";
			eVecS1.geoType = "POLYGON";
			coordList = eVecS1.getRelation(idObj);
			geom = eVecS1.createGeometry(coordList, idObj);
			assertTrue("geom should be an instance of Polygon", geom instanceof Polygon);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		// Create geometry for a SECTION_id edigeo object with id Objet_434718 Objet_966450 (MultiPolygon)
		try {
			idObj = "Objet_966450";
			eVecT3.geoType = "MULTIPOLYGON";
			coordList = eVecT3.getRelation(idObj);
			geom = eVecT3.createGeometry(coordList, idObj);
			assertTrue("geom should be an instance of MultiPolygon", geom instanceof MultiPolygon);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		/**
		 * Test for topologic edigeo objects. Geometry needs to be constructs from edges.
		 */
		// Create geometry for a PARCELLE_id edigeo object with id Objet_434764 (Polygon)
		try {
			idObj = "Objet_434764";
			eVecT1.geoType = "POLYGON";
			eVecT1.topo = true;
			coordList = eVecT1.getRelation(idObj);
			assertTrue("Coordinate list should contains at least two edges in order to construct a polygon",
					coordList.size() > 1);
			geom = eVecT1.createGeometry(coordList, idObj);
			assertTrue("geom should be an instance of Polygon", geom instanceof Polygon);
		} catch (IOException e) {
			assertFalse(e.getMessage(), true);
		}
		
		
	}
	
}
