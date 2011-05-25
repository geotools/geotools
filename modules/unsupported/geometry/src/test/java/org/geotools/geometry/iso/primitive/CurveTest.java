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
package org.geotools.geometry.iso.primitive;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.EnvelopeImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineSegmentImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

/**
 * @author sanjay
 *
 *
 *
 * @source $URL$
 */
public class CurveTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		
		this._testCurve(builder);
		
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
	
	public void testCurveEquals(){
		CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
		PicoContainer container = container( crs ); // normal 2D
		PrimitiveFactoryImpl factory = (PrimitiveFactoryImpl) container.getComponentInstanceOfType( PrimitiveFactoryImpl.class );
		PositionFactory positionFactory = (PositionFactory ) container.getComponentInstanceOfType( PositionFactory.class );
		
		DirectPosition positionA = positionFactory.createDirectPosition(new double[]{10, 10});
		DirectPosition positionB = positionFactory.createDirectPosition(new double[]{70, 30});
		
		CurveImpl expected = createCurve(positionA, positionB);		
		CurveImpl actual = createCurve(positionA, positionB);
		
		assertEquals( expected, actual );
	}

	private CurveImpl createCurve(DirectPosition positionA, DirectPosition positionB) {
		List segments = new ArrayList(1);
		segments.add( new LineSegmentImpl( positionA.getCoordinateReferenceSystem(), positionA.getCoordinates(), positionB.getCoordinates(), 0) );
		
		return new CurveImpl( positionA.getCoordinateReferenceSystem(), segments );	
	}
	
	private void _testCurve(GeometryBuilder builder) {
		
		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		PositionImpl p1 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-50,  0}));
		PositionImpl p2 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-30,  30}));
		PositionImpl p3 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{0,  50}));
		PositionImpl p4 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30,  30}));
		PositionImpl p5 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50,  0}));

		LineStringImpl line1 = null;
		
		ArrayList<Position> positionList = new ArrayList<Position>();
		positionList .add(p1);
		positionList.add(p2);
		positionList.add(p3);
		positionList.add(p4);
		positionList.add(p5);
		line1 = tCoordFactory.createLineString(positionList); 

		/* Set parent curve for LineString */
		ArrayList<CurveSegment> tLineList = new ArrayList<CurveSegment>();
		tLineList.add(line1);

		// PrimitiveFactory.createCurve(List<CurveSegment>)
		CurveImpl curve1 = tPrimFactory.createCurve(tLineList);
		//System.out.println("\nCurve1: " + curve1);
		
		assertTrue(curve1.isCycle() == false);
		
		// Set curve for further LineString tests
		line1.setCurve(curve1);

		//System.out.println("\n*** TEST: Curve\n" + curve1);
		
		// ***** getStartPoint()
		//System.out.println("\n*** TEST: .getStartPoint()\n" + curve1.getStartPoint());
		assertTrue(curve1.getStartPoint().getOrdinate(0) == -50);
		assertTrue(curve1.getStartPoint().getOrdinate(1) == 0);
		
		// ***** getEndPoint()
		//System.out.println("\n*** TEST: .getEndPoint()\n" + curve1.getEndPoint());
		assertTrue(curve1.getEndPoint().getOrdinate(0) == 50);
		assertTrue(curve1.getEndPoint().getOrdinate(1) == 0);

		// ***** getStartParam()
		//System.out.println("\n*** TEST: .getStartParam()\n" + curve1.getStartParam());
		assertTrue(curve1.getStartParam() == 0.0);

		// ***** getEndParam()
		//System.out.println("\n*** TEST: .getEndParam()\n" + curve1.getEndParam());
		assertTrue(Math.round(line1.getEndParam()) == 144.0);

		// ***** getStartConstructiveParam()
		//System.out.println("\n*** TEST: .getStartConstructiveParam()\n" + curve1.getStartConstructiveParam());
		assertTrue(curve1.getStartConstructiveParam() == 0.0);

		// ***** getEndConstructiveParam()
		//System.out.println("\n*** TEST: .getEndConstructiveParam()\n" + curve1.getEndConstructiveParam());
		assertTrue(curve1.getEndConstructiveParam() == 1.0);

		// ***** getBoundary()
		//System.out.println("\n*** TEST: .getBoundary()\n" + curve1.getBoundary());
		CurveBoundary cb = curve1.getBoundary();
		assertTrue(cb != null);
		double[] dp = cb.getStartPoint().getPosition().getCoordinates();
		assertTrue(dp[0] == -50);
		assertTrue(dp[1] == 0);
		dp = cb.getEndPoint().getPosition().getCoordinates();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 0);
		
		// ***** getEnvelope()
		//System.out.println("\n*** TEST: .getEnvelope()\n" + curve1.getEnvelope());
		assertTrue(curve1.getEnvelope() != null);
		dp = curve1.getEnvelope().getLowerCorner().getCoordinates();
		assertTrue(dp[0] == -50);
		assertTrue(dp[1] == 0);
		dp = curve1.getEnvelope().getUpperCorner().getCoordinates();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 50);
		
		// ***** forParam(double distance) : DirectPosition
		dp = curve1.forParam(0).getCoordinates();
		assertTrue(dp[0] == -50);
		assertTrue(dp[1] == 0.0);
		
		dp = curve1.forParam(curve1.length()).getCoordinates();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 0.0);
		
		dp = curve1.forParam(50).getCoordinates();
		////System.out.println("forParam: " + dp[0] + "," + dp[1]);
		assertTrue(Math.round(dp[0]*1000) == -18397);
		assertTrue(Math.round(dp[1]*1000) == 37735);

		// ***** forConstructiveParam(double distance)
		dp = curve1.forConstructiveParam(0.0).getCoordinates();
		assertTrue(dp[0] == -50);
		assertTrue(dp[1] == 0.0);

		dp = curve1.forConstructiveParam(1.0).getCoordinates();
		assertTrue(dp[0] == 50);
		assertTrue(dp[1] == 0.0);
		
		dp = curve1.forConstructiveParam(50 / curve1.length()).getCoordinates();
		assertTrue(Math.round(dp[0]*1000) == -18397);
		assertTrue(Math.round(dp[1]*1000) == 37735);
		
		// ***** getTangent(double distance)
		dp = curve1.getTangent(0);
		////System.out.println("tangent: " + dp[0] + "," + dp[1]);
		assertTrue(Math.round(dp[0]*1000) == -49445);
		assertTrue(Math.round(dp[1]*1000) == 832);

		dp = curve1.getTangent(40);
		assertTrue(Math.round(dp[0]*100) == -2589);
		assertTrue(Math.round(dp[1]*100) == 3274);

		dp = curve1.getTangent(curve1.getEndParam());
		//System.out.println("tangent: " + dp[0] + "," + dp[1]);
		assertTrue(Math.round(dp[0]*100) == 5055);
		assertTrue(Math.round(dp[1]*100) == -83);
		
		// ***** getRepresentativePoint()
		dp = curve1.getRepresentativePoint().getCoordinates();
		////System.out.print("REPRER" + dp);
		assertTrue(dp[0] == 0);
		assertTrue(dp[1] == 50);
		
		
		// ***** Curve.Merge(Curve)
		
		DirectPosition p6 = tCoordFactory.createDirectPosition(new double[]{80,  20});
		DirectPosition p7 = tCoordFactory.createDirectPosition(new double[]{130,  60});
		
		List<DirectPosition> directPositions = new ArrayList<DirectPosition>();
		
		directPositions.add(p5.getPosition());
		directPositions.add(p6);
		directPositions.add(p7);
		CurveImpl curve2 = (CurveImpl) tPrimFactory.createCurveByDirectPositions(directPositions);
		
		CurveImpl curve3 = curve1.merge(curve2);
		//System.out.println("Curve1: " + curve1);
		//System.out.println("Curve2: " + curve2);
		//System.out.println("Merge: " + curve3);
		// Lists of line1 and line2 are not modified
		assertTrue(curve1.asDirectPositions().size() == 5);
		assertTrue(curve2.asDirectPositions().size() == 3);
		// New LineString has combined positions
		assertTrue(curve3.asDirectPositions().size() == 7);

		curve3 = curve2.merge(curve1);
		//System.out.println("Curve1: " + curve1);
		//System.out.println("Curve2: " + curve2);
		//System.out.println("Merge: " + curve3);
		// Lists of line1 and line2 are not modified
		assertTrue(curve1.asDirectPositions().size() == 5);
		assertTrue(curve2.asDirectPositions().size() == 3);
		// New LineString has combined positions
		assertTrue(curve3.asDirectPositions().size() == 7);
		
		directPositions.remove(0);
		curve2 = (CurveImpl) tPrimFactory.createCurveByDirectPositions(directPositions);
		curve3 =  null;
		try {
			curve3 = curve2.merge(curve1);
		} catch (IllegalArgumentException e){
			//
		}
		// Merge of two not touching linestrings does not work
		assertTrue(curve3==null);
		
		Complex cc1 = curve1.getClosure();
		//System.out.println(cc1);
		assertTrue(cc1 instanceof CompositeCurve);

		
	}

}
