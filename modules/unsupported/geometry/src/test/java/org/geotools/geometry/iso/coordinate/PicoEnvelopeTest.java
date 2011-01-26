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

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

public class PicoEnvelopeTest extends TestCase {

	public void testMain() {
		
		PicoContainer c = container( DefaultGeographicCRS.WGS84 );
		
		this._testEnvelope1(c);
		
	}	
	
	/**
	 * Creates a pico container that knows about all the geom factories
	 * @param crs
	 * @return container
	 */
	protected PicoContainer container( CoordinateReferenceSystem crs ){
		
		DefaultPicoContainer container = new DefaultPicoContainer(); // parent
		
		// Teach Container about Factory Implementations we want to use
		container.registerComponentImplementation(PositionFactoryImpl.class);
		container.registerComponentImplementation(AggregateFactoryImpl.class);
		container.registerComponentImplementation(ComplexFactoryImpl.class);
		container.registerComponentImplementation(GeometryFactoryImpl.class);
		container.registerComponentImplementation(CollectionFactoryMemoryImpl.class);
		container.registerComponentImplementation(PrimitiveFactoryImpl.class);
		container.registerComponentImplementation(Geo2DFactory.class);
		
		// Teach Container about other dependacies needed
		container.registerComponentInstance( crs );
		Precision pr = new PrecisionModel();
		container.registerComponentInstance( pr );
		
		return container;		
	}

	private void _testEnvelope1(PicoContainer c) {
		
		GeometryFactoryImpl gf = (GeometryFactoryImpl) c.getComponentInstanceOfType(GeometryFactory.class);

		
		// CoordinateFactory.createDirectPosition(double[])
		DirectPosition dp1 = gf.createDirectPosition(new double[] {0, 0});
		DirectPosition dp2 = gf.createDirectPosition(new double[] {100, 100});

		DirectPosition dp0 = gf.createDirectPosition(new double[] {100, 100});
		
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
		
		
		DirectPosition dp3 = gf.createDirectPosition(new double[] {0,0});
		DirectPosition dp4 = gf.createDirectPosition(new double[] {100,50});
		DirectPosition dp5 = gf.createDirectPosition(new double[] {100.01,50});
		DirectPosition dp6 = gf.createDirectPosition(new double[] {50,100});
		DirectPosition dp7 = gf.createDirectPosition(new double[] {50,100.01});
		
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
		env1 = gf.createEnvelope(dp1.getCoordinates());
		//System.outprintln(env1);
		env1.expand(dp2.getCoordinates());
		//System.outprintln(env1);
		env1.expand(dp5.getCoordinates());
		//System.outprintln(env1);
		
		// TODO Test Intersects		
	}
}
