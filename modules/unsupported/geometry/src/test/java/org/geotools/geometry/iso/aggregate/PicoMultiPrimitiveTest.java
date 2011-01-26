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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineSegmentImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoMultiPrimitiveTest extends TestCase {

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
	
	public void testMultiPrimitive() {
		
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		PicoContainer container = container( crs ); // normal 2D
		PositionFactoryImpl pf = (PositionFactoryImpl ) container.getComponentInstanceOfType( PositionFactory.class );
		AggregateFactoryImpl agf = (AggregateFactoryImpl) container.getComponentInstanceOfType( AggregateFactory.class );
		
		// create surfaceboundary object
		
		// build exterior ring
		DirectPosition one = pf.createDirectPosition(new double[]{280,560});
		DirectPosition two = pf.createDirectPosition(new double[]{60,180});
		DirectPosition three = pf.createDirectPosition(new double[]{720,80});
		LineSegment edge1 = new LineSegmentImpl( one, two, 0.0 );
		LineSegment edge2 = new LineSegmentImpl( two, three, 0.0 );
		LineSegment edge3 = new LineSegmentImpl( three, one, 0.0 );
		
		// build interior ring
		DirectPosition one2 = pf.createDirectPosition(new double[]{420,360});
		DirectPosition two2 = pf.createDirectPosition(new double[]{200,360});
		DirectPosition three2 = pf.createDirectPosition(new double[]{320,180});
		LineSegment edge1_1 = new LineSegmentImpl( one2, two2, 0.0 );
		LineSegment edge2_1 = new LineSegmentImpl( two2, three2, 0.0 );
		LineSegment edge3_1 = new LineSegmentImpl( three2, one2, 0.0 );
		
		// create multiprimitive object
		List<CurveSegment> curves = new ArrayList<CurveSegment>();
		curves.add( edge1 );
		curves.add( edge2 );
		curves.add( edge3 );
		Curve s = new CurveImpl(crs, curves);
		
		curves = new ArrayList<CurveSegment>();
		curves.add( edge1_1 );
		curves.add( edge2_1 );
		curves.add( edge3_1 );
		Curve s2 = new CurveImpl(crs, curves);
		
		Set<Primitive> primitives = new HashSet<Primitive>();
		primitives.add(s);
		primitives.add(s2);
		MultiPrimitiveImpl mp = (MultiPrimitiveImpl) agf.createMultiPrimitive(primitives);
		//System.out.println(mp);
		//System.out.println(mp.getBoundary());
		//assertNotNull(mp.getBoundary());
		
		// test equals
		assertTrue(mp.equals(new MultiPrimitiveImpl(mp.getCoordinateReferenceSystem(), primitives)));
	}
}
