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
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PrimitiveFactoryImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;

/**
 * Test case for LineString and LineSegment
 * 
 * @author Sanjay Jena
 *
 *
 *
 * @source $URL$
 */
public class LineStringLineSegmentTest extends TestCase {
	
	public void testMain() {
		
		GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
		
		this._testLineString1(builder);
		
	}
	
	
	private void _testLineString1(GeometryBuilder builder) {
		
		GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
		PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();
		
		PositionImpl p1 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-50,  0}));
		PositionImpl p2 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{-30,  30}));
		PositionImpl p3 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{0,  50}));
		PositionImpl p4 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{30,  30}));
		PositionImpl p5 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{50,  0}));

		LineStringImpl line1 = null;
		
		/* Testing constructor of LineString with Array with size of 0 */
		
		//System.out.println("\n***** TEST: Constructors");
		//PositionImpl arrayOfPoints[] = new PositionImpl[0];
		ArrayList<Position> positionList = new ArrayList<Position>();
		try {
			line1 = tCoordFactory.createLineString(positionList); 
		} catch (IllegalArgumentException e) {
			//System.out.println("LineStringImpl - Number of Positions in array: 0 - Not accepted");
		}
		assertEquals(line1,null);

		/* Testing constructor of LineString with Array with size of 1 */

		positionList.add(p1);
		try {
			line1 = tCoordFactory.createLineString(positionList); 
		} catch (IllegalArgumentException e) {
			//System.out.println("LineStringImpl - Number of Positions in array: 1 - Not accepted");
		}
		assertEquals(line1 , null);

		/* Testing constructor of LineString with Array with size of 2 */

		positionList.add(p2);
		try {
			line1 = tCoordFactory.createLineString(positionList); 
			//System.out.println("LineStringImpl - Number of Positions in array: 2 - accepted");
		} catch (IllegalArgumentException e) {
			//System.out.println("LineStringImpl - Number of Positions in array: 2 - Not accepted");
		}
		assertNotNull(line1);
		
		/* Testing constructor of LineString with Array with size of 5 */

		positionList.add(p3);
		positionList.add(p4);
		positionList.add(p5);
		try {
			line1 = tCoordFactory.createLineString(positionList); 
			//System.out.println("LineStringImpl - Number of Positions in array: 5 - accepted");
			//System.out.println("\n" + line1);

		} catch (IllegalArgumentException e) {
			//System.out.println("\nLineStringImpl - Number of Positions in array: 5 - Not accepted");
		}
		assertNotNull(line1);

		// ***** getEnvelope()
		//System.out.println("\n***** TEST: .envelope()");
		//System.out.println("Envelope of the LineString is " +  line1.getEnvelope());

		// ***** getStartPoint();
		//System.out.println("\n***** TEST: .startPoint()");
		//System.out.println("StartPoint: " + line1.getStartPoint());
		assertEquals(line1.getStartPoint().getOrdinate(0) , -50.0);
		assertEquals(line1.getStartPoint().getOrdinate(1) , 0.0);

		// ***** getEndPoint();
		//System.out.println("\n***** TEST: .endPoint()");
		//System.out.println("EndPoint: " + line1.getEndPoint());
		assertEquals(line1.getEndPoint().getOrdinate(0) , 50.0);
		assertEquals(line1.getEndPoint().getOrdinate(1) , 0.0);
		
		// Set curve for further LineString tests
		ArrayList<CurveSegment> tLineList = new ArrayList<CurveSegment>();
		tLineList.add(line1);
		
		CurveImpl curve1 = tPrimFactory.createCurve(tLineList);
		line1.setCurve(curve1);
		
		// ***** length()
		//System.out.println("\n***** TEST: .length()");
		//System.out.println("Length of LineString is " + line1.length());
		assertEquals(14422, Math.round(line1.length() * 100));

		// ***** getStartParam();
		//System.out.println("\n***** TEST: .startParam()");
		//System.out.println("StartParam: " + line1.getStartParam());
		assertEquals(line1.getStartParam() , 0.0);

		// ***** getEndParam();
		//System.out.println("\n***** TEST: .endParam()");
		//System.out.println("EndParam: " + line1.getEndParam());
		assertEquals(14422, Math.round(line1.getEndParam() * 100));

		// ***** getStartConstructiveParam();
		//System.out.println("\n***** TEST: .startConstrParam()");
		//System.out.println("ConstrStartParam: " + line1.getStartConstructiveParam());
		assertEquals(line1.getStartConstructiveParam() , 0.0);
		
		// ***** getEndConstructiveParam();
		//System.out.println("\n***** TEST: .endConstrParam()");
		//System.out.println("ConstrEndParam: " + line1.getEndConstructiveParam());
		assertEquals(line1.getEndConstructiveParam() , 1.0);

		
		
		// Receive LineSegments from LineString
		List<LineSegment> segments = line1.asLineSegments();
		assertEquals(segments.size() , 4);

		LineSegment seg1 = segments.get(0);
		LineSegment seg2 = segments.get(1);
		LineSegment seg3 = segments.get(2);
		LineSegment seg4 = segments.get(3);

		//System.out.println("LineSegment: " + seg1);	
		//System.out.println("LineSegment: " + seg2);	

		// ***** LineSegment.getStartParam()
		//System.out.println(seg1.getStartParam());
		assertEquals(seg1.getStartParam() , 0.0);
		
		// ***** LineSegment.getEndParam()
		//System.out.println(seg1.getEndParam());
		assertEquals( 36, Math.round(seg1.getEndParam()) );

		//System.out.println(seg2.getStartParam());
		assertEquals( 36, Math.round(seg2.getStartParam()) );
		
		//System.out.println(seg2.getEndParam());
		assertEquals( 72, Math.round(seg2.getEndParam()) );
		
		// ***** LineSegment.getStartConstructiveParam()
		// ***** LineSegment.getEndConstructiveParam()
		//System.out.println(seg1.getStartConstructiveParam());
		assertEquals(seg1.getStartConstructiveParam() , 0.0);
		//System.out.println(seg1.getEndConstructiveParam());
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
		//System.out.println(resultPos);
		assertEquals(resultPos.getOrdinate(0) , -50.0);
		assertEquals(resultPos.getOrdinate(1) , 0.0);

		// Parameter for forParam() is endparam
		resultPos = seg1.forParam(seg1.getEndParam());
		//System.out.println(resultPos);
		assertEquals(resultPos.getOrdinate(0) , -30.0);
		assertEquals(resultPos.getOrdinate(1) , 30.0);

		// Parameter for startParam out of param range
		resultPos = null;
		try {
			resultPos = seg1.forParam(180);
		} catch(IllegalArgumentException e) {
			// Shall throw exception
		}
		//System.out.println(resultPos);
		assertEquals(resultPos , null);

		resultPos = seg1.forParam(30);
		//System.out.println(resultPos);
		
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
		////System.out.println("forParam: " + dp[0] + "," + dp[1]);
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
		////System.out.println("tangent: " + dp[0] + "," + dp[1]);
        assertEquals(Math.round(dp[0]*1000) , -49445);
        assertEquals(Math.round(dp[1]*1000) , 832);

		dp = line1.getTangent(40);
        assertEquals(Math.round(dp[0]*100) , -2589);
        assertEquals(Math.round(dp[1]*100) , 3274);

		dp = line1.getTangent(line1.getEndParam());
		//System.out.println("tangent: " + dp[0] + "," + dp[1]);
        assertEquals(Math.round(dp[0]*100) , 5055);
        assertEquals(Math.round(dp[1]*100) , -83);
		
		// ***** merge(LineString)
		PositionImpl p6 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{80,  40}));
		PositionImpl p7 = new PositionImpl(tCoordFactory.createDirectPosition(new double[]{130,  60}));
		ArrayList<Position> positionList2 = new ArrayList<Position>();
		positionList2.add(p5);
		positionList2.add(p6);
		positionList2.add(p7);
		LineStringImpl line2 = tCoordFactory.createLineString(positionList2);
		
		LineStringImpl line3 = line1.merge(line2);
		//System.out.println("Line1: " + line1);
		//System.out.println("Line2: " + line2);
		//System.out.println("MergedLine: " + line3);
		// Lists of line1 and line2 are not modified
		assertEquals(line1.getControlPoints().size(), 5);
        assertEquals(line2.getControlPoints().size() , 3);
		// New LineString has combined positions
        assertEquals(line3.getControlPoints().size() , 7);
		
		line3 = line2.merge(line1);
		//System.out.println("MergedLine: " + line3);
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
		//System.out.println("Reversed. Line1: " + line1);
		
		
		// test more curvesegment methods using tLineList
		CurveSegmentImpl cseg = (CurveSegmentImpl) tLineList.get(0);
		assertNotNull(cseg.getBoundary());
		assertNotNull(cseg.getInterpolation());
		assertTrue(cseg.length(0,1) > 0);
		try {
			cseg.setCurve(null);
			fail();  // should not get here
		}
		catch (IllegalArgumentException e) {
			// good
		}
		
		// test more linestringimpl methods
		//line1.split(1); broken?
		LineStringImpl newline = line1.asLineString(1, 1);
		assertNotNull(newline);
		
		// test toString
		String toS = newline.toString();
		assertTrue(toS != null);
		assertTrue(toS.length() > 0);
		
		// test obj equals
		assertTrue(newline.equals((Object) line1.asLineString(1, 1)));
		assertTrue(line1.equals((Object) line1));
		assertFalse(line1.equals((Object) controlPoints));
		assertFalse(line1.equals((Object) null));
		assertFalse(line1.equals(line1.asLineString(0.5, 0.5)));
	}

}
