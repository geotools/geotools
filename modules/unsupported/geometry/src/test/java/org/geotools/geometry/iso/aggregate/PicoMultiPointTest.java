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
package org.geotools.geometry.iso.aggregate;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.primitive.Point;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoMultiPointTest extends TestCase {

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
	
	public void testMain() {
		
		PicoContainer c = container( DefaultGeographicCRS.WGS84 );
		
		this._testMultiCurve(c);
	}
	
	private void _testMultiCurve(PicoContainer c) {

		AggregateFactoryImpl af = (AggregateFactoryImpl) c.getComponentInstanceOfType(AggregateFactory.class);
		PositionFactory pf = (PositionFactory ) c.getComponentInstanceOfType( PositionFactory.class );
		
		DirectPosition dp1 = pf.createDirectPosition(new double[]{10, 30});
		DirectPosition dp2 = pf.createDirectPosition(new double[]{70, 15});
		DirectPosition dp3 = pf.createDirectPosition(new double[]{45, 5});
		DirectPosition dp4 = pf.createDirectPosition(new double[]{10, 35});
		Set<Point> points = new HashSet<Point>();
		points.add( new PointImpl(dp1) );
		points.add( new PointImpl(dp2) );
		points.add( new PointImpl(dp3) );
		points.add( new PointImpl(dp4) );
		MultiPoint mp = af.createMultiPoint(points);
		
		assertNotNull(mp);
		assertNotNull(mp.getEnvelope());
		assertEquals(mp.getCoordinateDimension(), 2);
		assertEquals(mp.getElements().size(), 4);
		assertEquals(mp.getElements(), points);
		assertEquals(mp.getEnvelope().getLowerCorner().getOrdinate(0), 10.0);
		assertEquals(mp.getEnvelope().getLowerCorner().getOrdinate(1), 5.0);
		assertEquals(mp.getEnvelope().getUpperCorner().getOrdinate(0), 70.0);
		assertEquals(mp.getEnvelope().getUpperCorner().getOrdinate(1), 35.0);
		
		// test equals
		assertTrue(mp.equals(new MultiPointImpl(mp.getCoordinateReferenceSystem(), points)));
		
	}
}
