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
package org.geotools.geometry.iso.coordinate;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.util.Cloneable;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.GeometryFactory;

/**
 * @author Sanjay Jena
 *
 *
 *
 * @source $URL$
 */
public class DirectPositionTest extends TestCase {
	GeometryFactory gf2D;
	GeometryFactory gf3D;
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        
        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84); 
        gf2D = builder.getGeometryFactory();
        builder.setCoordianteReferenceSystem(DefaultGeographicCRS.WGS84_3D);
        gf3D = builder.getGeometryFactory();
        
    }

	public void testDirectPosition() {
		double x1 = 10000.00;
		double y1 = 10015.50;
		double z1 = 10031.00;
		
		double coords1[] = new double[]{x1, y1, z1};
		double coords2[] = new double[]{x1, y1};
		double resultCoords[];
		
		// Creating a DP
		DirectPosition dp1 = gf3D.createDirectPosition(coords1);
		
		// getCoordinates()
		resultCoords = dp1.getCoordinates();
		assertTrue(coords1[0] == resultCoords[0]);
		assertTrue(coords1[1] == resultCoords[1]);
		assertTrue(coords1[2] == resultCoords[2]);
		
		// getOrdinate(dim)
		assertTrue(coords1[0] == dp1.getOrdinate(0));
		assertTrue(coords1[1] == dp1.getOrdinate(1));
		assertTrue(coords1[2] == dp1.getOrdinate(2));
		
		// Cloning a DP
		DirectPosition dp2 = (DirectPosition) ((Cloneable) dp1).clone();
		
		// setOrdinate(dim, value)
		dp1.setOrdinate(0, 10.5);
		dp1.setOrdinate(1, 20.7);
		dp1.setOrdinate(2, -30.666);
		resultCoords = dp1.getCoordinates();
		assertTrue(resultCoords[0] == 10.5);
		assertTrue(resultCoords[1] == 20.7);
		assertTrue(resultCoords[2] == -30.666);
		
		// Test if clone() returned a copy, and not a reference
		// The values of dp2 should not be modified by the previous setOrdinate call in dp1
		resultCoords = dp2.getCoordinates();
		assertTrue(x1 == resultCoords[0]);
		assertTrue(y1 == resultCoords[1]);
		assertTrue(z1 == resultCoords[2]);

		//DirectPosition dp3 = aCoordFactory.createDirectPosition(coords2);
		
		assertTrue(dp1.getDimension() == 3);
		
		
	}

}
