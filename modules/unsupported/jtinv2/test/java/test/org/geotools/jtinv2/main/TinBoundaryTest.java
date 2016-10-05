/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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

@author Julian Padilla

package test.org.geotools.jtinv2.main;

import static org.junit.Assert.*;

import org.geotools.jtinv2.main.TinBoundary;
import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;

public class TinBoundaryTest 
{

	private TinBoundary testSubject;
	
	@Before
	public void setUp() throws Exception
	{
		Coordinate coord1 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord2 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord3 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord4 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords = new Coordinate[4];
		
		coords[0] = coord1;
		coords[1] = coord2;
		coords[2] = coord3;
		coords[3] = coord4;
	
		GeometryFactory factory = new GeometryFactory();
		LineString line = factory.createLineString(coords);
		
		this.testSubject = new TinBoundary(1000, line, "Ridges", "Pike's Peak", true);
	}

	@Test
	public void testClone() 
	{
		TinBoundary clone = this.testSubject.clone(1000);
		
		int cloneID = clone.getIdentifier();
		String cloneCategory = clone.getCategory();
		String cloneDescription = clone.getDescription();
		LineString cloneLineString = clone.getLineString();
		
		int subjectID = this.testSubject.getIdentifier();
		String subjectCategory = this.testSubject.getCategory();
		String subjectDescription = this.testSubject.getDescription();
		LineString subjectLineString = this.testSubject.getLineString();
		
		if(cloneID != subjectID)
		{
			fail("The clone didn't have the correct ID.");
		}
		
		if(cloneCategory.equals(subjectCategory) == false)
		{	
			fail("The clone didn't have the correct Category.");
		}
		
		if(cloneDescription.equals(subjectDescription) == false)
		{
			fail("The clone didn't have the correct Description.");
		}
		
		boolean areEqual = this.areLineStringsEqual(subjectLineString, cloneLineString, 10.0);
		
		if(areEqual == false)
		{		
			fail("The clone didn't have the correct LineString.");
		}
	}

	@Test
	public void testEqualsTinBoundary() 
	{
		Coordinate coord1 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord2 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord3 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord4 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords = new Coordinate[4];
		
		coords[0] = coord1;
		coords[1] = coord2;
		coords[2] = coord3;
		coords[3] = coord4;
	
		GeometryFactory factory = new GeometryFactory();
		LineString line = factory.createLineString(coords);
		
		TinBoundary compareTo = new TinBoundary(1000, line, "Ridges", "Pike's Peak", true);
		
		if((compareTo.equals(this.testSubject) != true))
		{
			fail("The method didn't detect two TinBoundaries that were equal.");
		}
	}

	@Test
	public void testHowEqual() 
	{
		// Create subjects for comparison.
		Coordinate coord1 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord2 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord3 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord4 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords1 = new Coordinate[4];
		
		coords1[0] = coord1;
		coords1[1] = coord2;
		coords1[2] = coord3;
		coords1[3] = coord4;
	
		GeometryFactory factory = new GeometryFactory();
		LineString line1 = factory.createLineString(coords1);
		
		TinBoundary first = new TinBoundary(1000, line1, "Ridges", "Pike's Peak", true);
		
		// Create subjects for comparison.
		Coordinate coord5 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord6 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord7 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord8 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords2 = new Coordinate[4];
			
		coords2[0] = coord1;
		coords2[1] = coord2;
		coords2[2] = coord3;
		coords2[3] = coord4;
			
		LineString line2 = factory.createLineString(coords2);
				
		TinBoundary second = new TinBoundary(1000, line2, "Ridges", "Pike's Peak", true);

		int howEqual1 = first.howEqual(second);
		
		if(howEqual1 != 3)
		{
			System.err.println(howEqual1);
			fail("The howEqual method didn't perform a proper comparison.");
		}
		
		// Create subjects for comparison.
		Coordinate coord9 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord10 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord11 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord12 = new Coordinate(500.0, 1000.0, 100.0);
				
		Coordinate[] coords3 = new Coordinate[4];
		
		coords3[0] = coord1;
		coords3[1] = coord2;
		coords3[2] = coord3;
		coords3[3] = coord4;
			
		LineString line3 = factory.createLineString(coords1);
		
		TinBoundary third = new TinBoundary(1000, line3, "Bank", "Pike's Peak", true);
				
		// Create subjects for comparison.
		Coordinate coord13 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord14 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord15 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord16 = new Coordinate(500.0, 1000.0, 100.0);
				
		Coordinate[] coords4 = new Coordinate[4];
					
		coords4[0] = coord1;
		coords4[1] = coord2;
		coords4[2] = coord3;
		coords4[3] = coord4;
					
		LineString line4 = factory.createLineString(coords2);
					
		TinBoundary fourth = new TinBoundary(1000, line4, "Ridges", "Pike'sPeak", true);
		
		int howEqual2= third.howEqual(fourth);
		
		if(howEqual2 != 2)
		{
			fail("The howEqual method didn't perform a proper comparison.");
		}		
	
		// Create subjects for comparison.
		Coordinate coord17 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord18 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord19 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord20 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords5 = new Coordinate[4];
		
		coords5[0] = coord1;
		coords5[1] = coord2;
		coords5[2] = coord3;
		coords5[3] = coord4;
	
		LineString line5 = factory.createLineString(coords1);
		
		TinBoundary fifth = new TinBoundary(1000, line5, "Ridges", "Pike's Peak", true);
		
		// Create subjects for comparison.
		Coordinate coord21 = new Coordinate(500.0, 1000.0, 100.0);
		Coordinate coord22 = new Coordinate(600.0, 2000.0, 200.0);
		Coordinate coord23 = new Coordinate(700.00, 3000.0, 300.0);
		Coordinate coord24 = new Coordinate(500.0, 1000.0, 100.0);
		
		Coordinate[] coords6 = new Coordinate[4];
			
		coords6[0] = coord21;
		coords6[1] = coord22;
		coords6[2] = coord23;
		coords6[3] = coord24;
			
		LineString line6 = factory.createLineString(coords2);
				
		TinBoundary sixth = new TinBoundary(1000, line6, "Gullies", "Julian's Peak", true);

		int howEqual3 = fifth.howEqual(sixth);
		
		if(howEqual3 != 1)
		{
			System.err.println("Hello!");
			fail("The howEqual method didn't perform a proper comparison");
		}
	
	}
	
	@Test
	public void testToString() 
	{
		String expectedResult = "[TIN Boundary = LINESTRING (500 1000, 600 2000, 700 3000, 500 1000) | Category: Ridges | Description: Pike's Peak]";

		String actualResult = this.testSubject.toString();
		
		if(expectedResult.equals(actualResult) != true)
		{
			fail("The method didn't return the correct string.");
		}
	}
	
	private boolean areLineStringsEqual(LineString first, LineString second, double tolerance)
	{
		// Get the coordinates from each LineString.
		Coordinate[] firstCoords = first.getCoordinates();
		Coordinate[] secondCoords = second.getCoordinates();
		
		if(firstCoords.length != secondCoords.length)
		{
			return false;
		}
		
		int numberOfCoords = firstCoords.length;
		
		int counter = 0;
		
		while(counter < numberOfCoords)
		{
			double firstX = firstCoords[counter].x;
			double firstY = firstCoords[counter].y;
			double firstZ = firstCoords[counter].z;
			
			double secondX = secondCoords[counter].x;
			double secondY = secondCoords[counter].y;
			double secondZ = secondCoords[counter].z;
			
			double xDiff = firstX - secondX;
			double xDiffAbs = Math.abs(xDiff);
			
			double yDiff = firstY - secondY;
			double yDiffAbs = Math.abs(yDiff);
			
			double zDiff = firstZ - secondZ;
			double zDiffAbs = Math.abs(zDiff);
			
			if(zDiffAbs > tolerance)
			{
				return false;
			}
			
			if(yDiffAbs > tolerance)
			{
				return false;
			}
			
			if(zDiffAbs > tolerance)
			{
				return false;
			}
			
			counter++;
		}
		
		return true;
	}

}
