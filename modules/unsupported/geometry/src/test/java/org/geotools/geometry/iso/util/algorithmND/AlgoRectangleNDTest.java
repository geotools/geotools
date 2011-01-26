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
package org.geotools.geometry.iso.util.algorithmND;

import junit.framework.TestCase;

import org.geotools.geometry.iso.util.algorithmND.AlgoPointND;
import org.geotools.geometry.iso.util.algorithmND.AlgoRectangleND;

public class AlgoRectangleNDTest extends TestCase {

	public void testWorkingAlgos() {
		
		this._testContainsND();
		this._testIntersects2D();
		this._testIntersects3D();
	}
	
	public void testNotWorkingYetAlgos() {
		
		//this._testNotWorkingAlgorithms();
		
	}

	private void _testContainsND() {
		
		double[] c1 = new double[]{10, 30};
		double[] c2 = new double[]{150, 150};
		double[] c3 = new double[]{70, 10};
		double[] c4 = new double[]{200, 140};
		double[] c5 = new double[]{10, 150};
		double[] c6 = new double[]{10, 150.00000000001};
		double[] c7 = new double[]{100, 70};
		
		boolean b = false;

		// Within envelope
		b = AlgoRectangleND.contains(c1, c2, c7);
		assertTrue (b);

		// Out of envelope
		b = AlgoRectangleND.contains(c1, c2, c3);
		assertTrue (!b);

		// Out of envelope
		b = AlgoRectangleND.contains(c1, c2, c4);
		assertTrue (!b);

		// Within envelope
		b = AlgoRectangleND.contains(c3, c4, c7);
		assertTrue (b);

		// Out of envelope
		b = AlgoRectangleND.contains(c3, c4, c1);
		assertTrue (!b);

		// Out of envelope
		b = AlgoRectangleND.contains(c3, c4, c2);
		assertTrue (!b);
		
		// Border test - On envelope corner
		b = AlgoRectangleND.contains(c1, c2, c1);
		assertTrue (b);

		// Border test - On envelope corner
		b = AlgoRectangleND.contains(c1, c2, c2);
		assertTrue (b);

		// Border test - on envelope border
		b = AlgoRectangleND.contains(c1, c2, c5);
		assertTrue (b);

		// Border test - out of envelope
		b = AlgoRectangleND.contains(c1, c2, c6);
		assertTrue (!b);

	}
	
	private void _testIntersects2D() {
	
		double[] c1 = new double[]{10, 30};
		double[] c2 = new double[]{150, 150};
		double[] c3 = new double[]{70, 10};
		double[] c4 = new double[]{200, 140};
		double[] c5 = new double[]{10, 150};
		double[] c6 = new double[]{10, 150.00000000001};
		double[] c7 = new double[]{250, 250};
		double[] c8 = new double[]{150, 150.00000000001};
		
		boolean b = false;

		// Intersect
		b = AlgoRectangleND.intersects(c1, c2, c3, c4);
		assertTrue (b);
		
		// Intersect
		b = AlgoRectangleND.intersects(c1, c2, c3, c4);
		assertTrue (b);

		// Intersect
		b = AlgoRectangleND.intersects(c3, c4, c1, c2);
		assertTrue (b);

		// Intersect
		b = AlgoRectangleND.intersects(c1, c2, c2, c7);
		assertTrue (b);

		// Do not Intersect
		b = AlgoRectangleND.intersects(c1, c2, c8, c7);
		assertTrue (!b);
	}

	private void _testIntersects3D() {
		
		double[] c1 = new double[]{0, 0, 0};
		double[] c2 = new double[]{50, 50, 50};
		double[] c3 = new double[]{100, 100, 100};
		double[] c4 = new double[]{150, 150, 150};
		double[] c5 = new double[]{200, 200, 200};
		double[] c6 = new double[]{150, 150.000000001, 150};
		
		boolean b = false;

		// Intersect
		b = AlgoRectangleND.intersects(c1, c3, c2, c4);
		assertTrue (b);

		// Intersect
		b = AlgoRectangleND.intersects(c2, c4, c1, c3);
		assertTrue (b);

		// Intersect
		b = AlgoRectangleND.intersects(c1, c4, c2, c3);
		assertTrue (b);

		// Intersect
		b = AlgoRectangleND.intersects(c2, c3, c1, c4);
		assertTrue (b);
		
		// Intersect
		b = AlgoRectangleND.intersects(c1, c4, c4, c5);
		assertTrue (b);
		
		// Do not Intersect - almost Border
		b = AlgoRectangleND.intersects(c1, c4, c6, c5);
		assertTrue (!b);
		
		// Do not Intersect - almost Border
		b = AlgoRectangleND.intersects(c1, c2, c3, c4);
		assertTrue (!b);
		
		
	}

	
	
	private void _testNotWorkingAlgorithms() {
		
		double[] coords = new double[2];
		coords[0] = 0.3;
		coords[1] = 0.4;
		double[] newCoords = AlgoPointND.scale(coords, 3.0);
		
		System.out.println(newCoords[0] + "  " + newCoords[1]);
		
		assertTrue(newCoords[0] == 0.9);
		assertTrue(newCoords[1] == 1.2);
		
	}


}
