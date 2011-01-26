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

import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

import junit.framework.TestCase;

public class PicoLineStringLineSegmentTest extends TestCase {
	
	public void testMain() {
		
		//FeatGeomFactoryImpl tGeomFactory = FeatGeomFactoryImpl.getDefault2D();
		PicoContainer c = container( DefaultGeographicCRS.WGS84 );
		assertNotNull(c);
		this._testLineString1(c);
		
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
	
	private void _testLineString1(PicoContainer c) {
		
		GeometryFactoryImpl tGeomFactory = (GeometryFactoryImpl) c.getComponentInstanceOfType(GeometryFactory.class);
		PositionFactoryImpl tPosFactory = (PositionFactoryImpl) c.getComponentInstanceOfType(PositionFactory.class);
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) c.getComponentInstanceOfType(PrimitiveFactory.class);
		
		PositionImpl p1 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{-50,  0}));
		PositionImpl p2 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{-30,  30}));
		PositionImpl p3 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{0,  50}));
		PositionImpl p4 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{30,  30}));
		PositionImpl p5 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{50,  0}));

		LineStringImpl line1 = null;
		
		/* Testing constructor of LineString with Array with size of 0 */
		
		////System.out.println("\n***** TEST: Constructors");
		//PositionImpl arrayOfPoints[] = new PositionImpl[0];
		ArrayList<Position> positionList = new ArrayList<Position>();
		try {
			line1 = tGeomFactory.createLineString(positionList); 
		} catch (IllegalArgumentException e) {
			//System.out.println("LineStringImpl - Number of Positions in array: 0 - Not accepted");
		}
		assertEquals(line1,null);

		/* Testing constructor of LineString with Array with size of 1 */

		positionList.add(p1);
		try {
			line1 = tGeomFactory.createLineString(positionList); 
		} catch (IllegalArgumentException e) {
			//System.outprintln("LineStringImpl - Number of Positions in array: 1 - Not accepted");
		}
		assertEquals(line1 , null);

		/* Testing constructor of LineString with Array with size of 2 */

		positionList.add(p2);
		try {
			line1 = tGeomFactory.createLineString(positionList); 
			//System.outprintln("LineStringImpl - Number of Positions in array: 2 - accepted");
		} catch (IllegalArgumentException e) {
			//System.outprintln("LineStringImpl - Number of Positions in array: 2 - Not accepted");
		}
		assertNotNull(line1);
		
		/* Testing constructor of LineString with Array with size of 5 */

		positionList.add(p3);
		positionList.add(p4);
		positionList.add(p5);
		try {
			line1 = tGeomFactory.createLineString(positionList); 
			//System.outprintln("LineStringImpl - Number of Positions in array: 5 - accepted");
			//System.outprintln("\n" + line1);

		} catch (IllegalArgumentException e) {
			//System.outprintln("\nLineStringImpl - Number of Positions in array: 5 - Not accepted");
		}
		assertNotNull(line1);

		// ***** getEnvelope()
		//System.outprintln("\n***** TEST: .envelope()");
		//System.outprintln("Envelope of the LineString is " +  line1.getEnvelope());

		// ***** getStartPoint();
		//System.outprintln("\n***** TEST: .startPoint()");
		//System.outprintln("StartPoint: " + line1.getStartPoint());
		assertEquals(line1.getStartPoint().getOrdinate(0) , -50.0);
		assertEquals(line1.getStartPoint().getOrdinate(1) , 0.0);

		// ***** getEndPoint();
		//System.outprintln("\n***** TEST: .endPoint()");
		//System.outprintln("EndPoint: " + line1.getEndPoint());
		assertEquals(line1.getEndPoint().getOrdinate(0) , 50.0);
		assertEquals(line1.getEndPoint().getOrdinate(1) , 0.0);
		
		// Set curve for further LineString tests
		ArrayList<CurveSegment> tLineList = new ArrayList<CurveSegment>();
		tLineList.add(line1);
		
		CurveImpl curve1 = tPrimFactory.createCurve(tLineList);
		line1.setCurve(curve1);
		
		// ***** length()
		//System.outprintln("\n***** TEST: .length()");
		//System.outprintln("Length of LineString is " + line1.length());
		assertEquals(14422, Math.round(line1.length() * 100));

		// ***** getStartParam();
		//System.outprintln("\n***** TEST: .startParam()");
		//System.outprintln("StartParam: " + line1.getStartParam());
		assertEquals(line1.getStartParam() , 0.0);

		// ***** getEndParam();
		//System.outprintln("\n***** TEST: .endParam()");
		//System.outprintln("EndParam: " + line1.getEndParam());
		assertEquals(14422, Math.round(line1.getEndParam() * 100));

		// ***** getStartConstructiveParam();
		//System.outprintln("\n***** TEST: .startConstrParam()");
		//System.outprintln("ConstrStartParam: " + line1.getStartConstructiveParam());
		assertEquals(line1.getStartConstructiveParam() , 0.0);
		
		// ***** getEndConstructiveParam();
		//System.outprintln("\n***** TEST: .endConstrParam()");
		//System.outprintln("ConstrEndParam: " + line1.getEndConstructiveParam());
		assertEquals(line1.getEndConstructiveParam() , 1.0);

		
		
		// Receive LineSegments from LineString
		List<LineSegment> segments = line1.asLineSegments();
		assertEquals(segments.size() , 4);

		LineSegment seg1 = segments.get(0);
		LineSegment seg2 = segments.get(1);
		LineSegment seg3 = segments.get(2);
		LineSegment seg4 = segments.get(3);

		//System.outprintln("LineSegment: " + seg1);	
		//System.outprintln("LineSegment: " + seg2);	

		// ***** LineSegment.getStartParam()
		//System.outprintln(seg1.getStartParam());
		assertEquals(seg1.getStartParam() , 0.0);
		
		// ***** LineSegment.getEndParam()
		//System.outprintln(seg1.getEndParam());
		assertEquals( 36, Math.round(seg1.getEndParam()) );

		//System.outprintln(seg2.getStartParam());
		assertEquals( 36, Math.round(seg2.getStartParam()) );
		
		//System.outprintln(seg2.getEndParam());
		assertEquals( 72, Math.round(seg2.getEndParam()) );
		
		// ***** LineSegment.getStartConstructiveParam()
		// ***** LineSegment.getEndConstructiveParam()
		//System.outprintln(seg1.getStartConstructiveParam());
		assertEquals(seg1.getStartConstructiveParam() , 0.0);
		//System.outprintln(seg1.getEndConstructiveParam());
		assertEquals(seg1.getEndConstructiveParam() , 0.25);
		assertEquals(segments.get(1).getStartConstructiveParam() , 0.25);
		assertEquals(segments.get(1).getEndConstructiveParam() , 0.50);
		assertEquals(segments.get(2).getStartConstructiveParam() , 0.50);
		assertEquals(segments.get(2).getEndConstructiveParam() , 0.75);
		assertEquals(segments.get(3).getStartConstructiveParam() , 0.75);
		assertEquals(segments.get(3).getEndConstructiveParam() , 1.0);


		// ***** LineSegment.forParam(double)
		// Parameter for forParam() is 0.0 (startparam)
		DirectPosition resultPos = seg1.forParam(0.0);
		//System.outprintln(resultPos);
		assertEquals(resultPos.getOrdinate(0) , -50.0);
		assertEquals(resultPos.getOrdinate(1) , 0.0);

		// Parameter for forParam() is endparam
		resultPos = seg1.forParam(seg1.getEndParam());
		//System.outprintln(resultPos);
		assertEquals(resultPos.getOrdinate(0) , -30.0);
		assertEquals(resultPos.getOrdinate(1) , 30.0);

		// Parameter for startParam out of param range
		resultPos = null;
		try {
			resultPos = seg1.forParam(180);
		} catch(IllegalArgumentException e) {
			// Shall throw exception
		}
		//System.outprintln(resultPos);
		assertEquals(resultPos , null);

		resultPos = seg1.forParam(30);
		//System.outprintln(resultPos);
		
		// ***** LineSegment.getControlPoints()
		assertEquals(seg1.getControlPoints().length() , 2);
		
		// ***** LineSegment.asLineSegments()
		assertEquals(seg2.asLineSegments().size() , 1);
		
		// ***** forParam(double distance)
		double[] dp = line1.forParam(0).getCoordinates();
		assertEquals( -50.0, dp[0] );
		assertEquals( 0.0, dp[1]);
		
		dp = line1.forParam(line1.length()).getCoordinates();
		assertEquals(50.0, dp[0]);
		assertEquals(0.0, dp[1]);
		
		dp = line1.forParam(seg1.getEndParam()).getCoordinates();
		assertEquals(-30.0, dp[0]);
		assertEquals(30.0, dp[1]);

		dp = line1.forParam(50).getCoordinates();
		////System.outprintln("forParam: " + dp[0] + "," + dp[1]);
		assertEquals(Math.round(dp[0]*1000) , -18397);
		assertEquals(Math.round(dp[1]*1000) , 37735);

		// ***** forConstructiveParam(double distance)
		dp = line1.forConstructiveParam(0.0).getCoordinates();
        assertEquals(-50.0, dp[0] );
        assertEquals(0.0, dp[1]);

		dp = line1.forConstructiveParam(1.0).getCoordinates();
        assertEquals(50.0, dp[0]);
        assertEquals(0.0, dp[1]);
		
		dp = line1.forConstructiveParam(50 / line1.length()).getCoordinates();
        assertEquals(Math.round(dp[0]*1000) , -18397);
        assertEquals(Math.round(dp[1]*1000) , 37735);

		// ***** getTangent(double distance)
		dp = line1.getTangent(0);
		////System.outprintln("tangent: " + dp[0] + "," + dp[1]);
        assertEquals(Math.round(dp[0]*1000) , -49445);
        assertEquals(Math.round(dp[1]*1000) , 832);

		dp = line1.getTangent(40);
        assertEquals(Math.round(dp[0]*100) , -2589);
        assertEquals(Math.round(dp[1]*100) , 3274);

		dp = line1.getTangent(line1.getEndParam());
		//System.outprintln("tangent: " + dp[0] + "," + dp[1]);
        assertEquals(Math.round(dp[0]*100) , 5055);
        assertEquals(Math.round(dp[1]*100) , -83);
		
		// ***** merge(LineString)
		PositionImpl p6 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{80,  40}));
		PositionImpl p7 = new PositionImpl(tPosFactory.createDirectPosition(new double[]{130,  60}));
		ArrayList<Position> positionList2 = new ArrayList<Position>();
		positionList2.add(p5);
		positionList2.add(p6);
		positionList2.add(p7);
		LineStringImpl line2 = tGeomFactory.createLineString(positionList2);
		
		LineStringImpl line3 = line1.merge(line2);
		//System.outprintln("Line1: " + line1);
		//System.outprintln("Line2: " + line2);
		//System.outprintln("MergedLine: " + line3);
		// Lists of line1 and line2 are not modified
		assertEquals(line1.getControlPoints().size(), 5);
        assertEquals(line2.getControlPoints().size() , 3);
		// New LineString has combined positions
        assertEquals(line3.getControlPoints().size() , 7);
		
		line3 = line2.merge(line1);
		//System.outprintln("MergedLine: " + line3);
		// Lists of line1 and line2 are not modified
        assertEquals(line1.getControlPoints().size() , 5);
        assertEquals(line2.getControlPoints().size() , 3);
		// New LineString has combined positions
        assertEquals(line3.getControlPoints().size() , 7);

		positionList2.remove(0);
		line3 =  null;
		try {
			line3 = line2.merge(line1);
		} catch (IllegalArgumentException e){
			// the exception shall be thrown, hence do nothing
		}
		// Merge of two not touching linestrings does not work
		// assertEquals( null, line3);
		
		// ***** getNumDerivatesAtStart()
        assertEquals(line1.getNumDerivativesAtStart() , 0);
		// ***** getNumDerivativesInterior()
        assertEquals(line1.getNumDerivativesInterior() , 0);
		// ***** getNumDerivativesAtEnd()
        assertEquals(line1.getNumDerivativesAtEnd() , 0);
		
		// ***** reverse()
		line1.reverse();
		// number of control points is unchanged
		PointArray controlPoints = line1.getControlPoints();
		assertEquals(controlPoints.length() , 5);
		// control points are in opposite order
		assertEquals(controlPoints.getDirectPosition(0, null), p5.getPosition());
		assertEquals(controlPoints.getDirectPosition(1, null), p4.getPosition());
		assertEquals(controlPoints.getDirectPosition(2, null), p3.getPosition());
		assertEquals(controlPoints.getDirectPosition(3, null), p2.getPosition());
		assertEquals(controlPoints.getDirectPosition(4, null), p1.getPosition());

	}
}
