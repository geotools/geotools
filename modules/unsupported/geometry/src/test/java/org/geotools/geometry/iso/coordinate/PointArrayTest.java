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

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;

public class PointArrayTest extends TestCase {

	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		
		this._test1(builder);
		
	}
	
	
	private void _test1(GeometryBuilder builder) {
		
		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		
		PositionImpl p1 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-50,  0}));
		PositionImpl p2 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-30,  30}));
		PositionImpl p3 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{0,  50}));
		PositionImpl p4 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30,  30}));
		PositionImpl p5 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50,  0}));

		List<Position> posList = new ArrayList<Position>();
		
		PointArray pa = null;
		
		// Testing Illegal Constructor call
		try {
			pa = tCoordFactory.createPointArray(posList);
		} catch (IllegalArgumentException e) {
			//
		}
		assertTrue(pa==null);
		
		posList.add(p1);
		posList.add(p2);
		posList.add(p3);
		posList.add(p4);
		posList.add(p5);
		
		// Legal Constructor call
		pa = tCoordFactory.createPointArray(posList);
				
		// PointArray.length()
		assertTrue(pa.size() == 5);
		// PointArray.positions()
		assertTrue(pa.size() == 5);

		// get-method creates new DP instance
		DirectPosition directPosition = pa.getDirectPosition(0, null);
		////System.out.println(dp);
		assertTrue(directPosition.getOrdinate(0) == -50);
		assertTrue(directPosition.getOrdinate(1) == 0);
		
		DirectPosition directPositionAt4 = pa.getDirectPosition(4, directPosition);
		////System.out.println(dp);
		assertTrue(directPosition.getOrdinate(0) == 50);
		assertTrue(directPosition.getOrdinate(1) == 0);
		// get-method uses the same DirectPosition without creating new instance
		assertTrue(directPositionAt4 == directPosition);
		
		DirectPosition directPositionAddition = tCoordFactory.createDirectPosition(new double[]{5, 5});
		pa.setDirectPosition(4, directPositionAddition); // test to see of object or values is stored        
        
        DirectPosition directPositionAt4mk2 = pa.getDirectPosition(4, directPosition); // retrive
        assertEquals( "Same values as we put into 4", directPositionAt4mk2, directPositionAddition );
        assertNotSame( "Not the same object we put into 4", directPositionAt4mk2, directPositionAddition );
        
        assertEquals( directPositionAt4mk2, directPositionAddition );
        
        // BEFORE
		assertEquals( 5.0, directPosition.getOrdinate(0) );
		assertEquals( 5.0, directPosition.getOrdinate(1) );
        
		// Check if the values were copied and not referenced (by modifying the ordinates)
        // Modify values set into position 4
		directPositionAddition.setOrdinate( 0, 2);
		
        // retrive values from position 4 again
        DirectPosition directPositionAt4mk3 = pa.getDirectPosition(4, directPosition);		
		assertEquals( "check if position is independent", 5.0, directPositionAt4mk3.getOrdinate(0) );
		
		double[] coord = ((PointArrayImpl)pa).getCoordinate(0);
		//System.out.print(coord[0] + "|" + coord[1]);
		
		// .isEmpty() and remove(int)
		assertTrue(!((PointArrayImpl)pa).isEmpty());
		((PointArrayImpl)pa).removePosition(((PointArrayImpl)pa).get(3));
		((PointArrayImpl)pa).remove(0);
		((PointArrayImpl)pa).remove(0);
		((PointArrayImpl)pa).remove(0);
		((PointArrayImpl)pa).remove(0);
		assertTrue(((PointArrayImpl)pa).isEmpty());

	}
	
}
