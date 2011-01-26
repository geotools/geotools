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
import org.geotools.geometry.iso.UnsupportedDimensionException;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;

/**
 * @author sanjay
 *
 *
 * @source $URL$
 */
public class EnvelopeTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84); 
		this._testEnvelope1(builder);
		
	}	

	private void _testEnvelope1(GeometryBuilder builder) {
		
		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();

		
		// CoordinateFactory.createDirectPosition(double[])
		DirectPosition dp1 = tCoordFactory.createDirectPosition(new double[] {0, 0});
		DirectPosition dp2 = tCoordFactory.createDirectPosition(new double[] {100, 100});

		DirectPosition dp0 = tCoordFactory.createDirectPosition(new double[] {100, 100});
		
		// DirectPosition.equals(DirectPosition)
		assertTrue(dp2.equals(dp0));

		// Envelope.getDimension()
		assertTrue(dp2.getDimension() == 2);
		//System.outprintln("Dimension of dp1: " + dp2.getDimension());
		
		EnvelopeImpl env1 = new EnvelopeImpl(dp1, dp2);
		
		// Envelope.getLowerCorner() + Envelope.equals(DP, tol)
		assertTrue(env1.getLowerCorner().equals(dp1));
		//System.outprintln(env1.getLowerCorner());
		
		// Envelope.getUpperCorner() + Envelope.equals(DP, tol)
		assertTrue(env1.getUpperCorner().equals(dp2));
		//System.outprintln(env1.getUpperCorner());
		//System.outprintln(env1);
		
		EnvelopeImpl env2 = new EnvelopeImpl(env1);
		//System.outprintln(env2);
		
		// Envelope.equals(Envelope)
		assertTrue(env1.equals(env2));
		
		
		DirectPosition dp3 = tCoordFactory.createDirectPosition(new double[] {0,0});
		DirectPosition dp4 = tCoordFactory.createDirectPosition(new double[] {100,50});
		DirectPosition dp5 = tCoordFactory.createDirectPosition(new double[] {100.01,50});
		DirectPosition dp6 = tCoordFactory.createDirectPosition(new double[] {50,100});
		DirectPosition dp7 = tCoordFactory.createDirectPosition(new double[] {50,100.01});
		
		// Envelope.contains(DirectPosition)
		//System.outprintln("Contains Method for " + env1);
		assertTrue(env1.contains(dp3) == true);
		//System.outprintln(dp3 + " liegt im Envelope: " + env1.contains(dp3));
		assertTrue(env1.contains(dp4) == true);
		//System.outprintln(dp4 + " liegt im Envelope: " + env1.contains(dp4));
		assertTrue(env1.contains(dp5) == false);
		//System.outprintln(dp5 + " liegt im Envelope: " + env1.contains(dp5));
		assertTrue(env1.contains(dp6) == true);
		//System.outprintln(dp6 + " liegt im Envelope: " + env1.contains(dp6));
		assertTrue(env1.contains(dp7) == false);
		//System.outprintln(dp7 + " liegt im Envelope: " + env1.contains(dp7));

//		DirectPositionImpl dp8 = tCoordFactory.createDirectPosition(new double[] {200,200});
//		
//		EnvelopeImpl env2 = new EnvelopeImpl(dp6, dp8);
//		EnvelopeImpl env3 = new EnvelopeImpl(dp7, dp8);
//		
//		//System.outprintln(env1 + " intersects with " + env2 + " : " + env1.intersects(env2));
//		//System.outprintln(env1 + " intersects with " + env3 + " : " + env1.intersects(env3));
		
		//System.outprintln("TEST EXPAND");
		env1 = tCoordFactory.createEnvelope(dp1.getCoordinates());
		//System.outprintln(env1);
		env1.expand(dp2.getCoordinates());
		//System.outprintln(env1);
		env1.expand(dp5.getCoordinates());
		//System.outprintln(env1);
		
		// Test other envelope methods
		env1.setValues(env2);
		DirectPosition[] dpArray = new DirectPositionImpl[2];
		dpArray[0] = dp0;
		dpArray[1] = dp4;
		EnvelopeImpl impl = env1.createEnvelope(dpArray);
		
		// test toString
		String toS = impl.toString();
		assertTrue(toS != null);
		assertTrue(toS.length() > 0);
		
		// test intersects
		assertTrue(impl.intersects(dp0));
		assertFalse(impl.intersects(dp1));
		
		// test get corners
		assertTrue(impl.getNECorner().equals(dp0));
		assertTrue(impl.getSWCorner().equals(dp4));
		try {
			assertTrue(impl.getSECorner().equals(dp4));
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			fail();
		}
		try {
			assertTrue(impl.getNWCornerOld().equals(dp0));
		} catch (UnsupportedDimensionException e) {
			e.printStackTrace();
			fail();
		}
	}

	
}
