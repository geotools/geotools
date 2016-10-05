/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.jtinv2.pointutils;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.vividsolutions.jts.geom.Coordinate;

public class SortableCoordinateCollectionTest 
{

	@Before
	public void setUp() throws Exception 
	{
		Coordinate coord1 = new Coordinate(100.00, 2000.00, 50.00);
		Coordinate coord2 = new Coordinate(200.00, 1900.00, 60.00);
		Coordinate coord3 = new Coordinate(300.00, 1800.00, 40.00);
		Coordinate coord4 = new Coordinate(400.00, 1700.00, 70.00);
	}

	@Test
	public void testGetNumberOfCoordinates() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByNorthingBiggerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByNorthingSmallerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByEastingBiggerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByEastingSmallerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSoretedByElevationBiggerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByElevationSmallerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByDistanceBiggerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByDistanceSmallerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByChangeInElevationBiggerFirst() 
	{
		fail("Not yet implemented");
	}

	@Test
	public void testGetCoordinatesSortedByChangeInElevationSmallerFirst() 
	{
		fail("Not yet implemented");
	}
}
