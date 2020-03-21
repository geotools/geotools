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
import java.util.Iterator;
import java.util.List;
import junit.framework.TestCase;
import org.geotools.geometry.GeometryBuilder;
import org.geotools.geometry.iso.PositionFactoryImpl;
import org.geotools.geometry.iso.PrecisionModel;
import org.geotools.geometry.iso.aggregate.AggregateFactoryImpl;
import org.geotools.geometry.iso.complex.ComplexFactoryImpl;
import org.geotools.geometry.iso.coordinate.CurveSegmentImpl;
import org.geotools.geometry.iso.coordinate.GeometryFactoryImpl;
import org.geotools.geometry.iso.coordinate.LineSegmentImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PositionImpl;
import org.geotools.geometry.iso.io.CollectionFactoryMemoryImpl;
import org.geotools.geometry.iso.util.elem2D.Geo2DFactory;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.picocontainer.PicoContainer;
import org.picocontainer.defaults.DefaultPicoContainer;

public class PicoCurveTest extends TestCase {

    public void testMain() {

        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);

        this._testCurve(builder);
    }

    /**
     * Creates a pico container that knows about all the geom factories
     *
     * @return container
     */
    protected PicoContainer container(CoordinateReferenceSystem crs) {

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
        container.registerComponentInstance(crs);
        Precision pr = new PrecisionModel();
        container.registerComponentInstance(pr);

        return container;
    }

    public void testCurveEquals() {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        // PrimitiveFactoryImpl factory = (PrimitiveFactoryImpl)
        // container.getComponentInstanceOfType( PrimitiveFactoryImpl.class );
        PositionFactory positionFactory =
                (PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);

        DirectPosition positionA = positionFactory.createDirectPosition(new double[] {10, 10});
        DirectPosition positionB = positionFactory.createDirectPosition(new double[] {70, 30});

        CurveImpl expected = createCurve(positionA, positionB);
        CurveImpl actual = createCurve(positionA, positionB);

        assertEquals(expected, actual);
    }

    private CurveImpl createCurve(DirectPosition positionA, DirectPosition positionB) {
        List segments = new ArrayList(1);
        segments.add(
                new LineSegmentImpl(
                        positionA.getCoordinateReferenceSystem(),
                        positionA.getCoordinate(),
                        positionB.getCoordinate(),
                        0));

        return new CurveImpl(positionA.getCoordinateReferenceSystem(), segments);
    }

    private void _testCurve(GeometryBuilder builder) {

        GeometryFactoryImpl tCoordFactory = (GeometryFactoryImpl) builder.getGeometryFactory();
        PositionFactory pf = builder.getPositionFactory();
        PrimitiveFactoryImpl tPrimFactory = (PrimitiveFactoryImpl) builder.getPrimitiveFactory();

        PositionImpl p1 = new PositionImpl(pf.createDirectPosition(new double[] {-50, 0}));
        PositionImpl p2 = new PositionImpl(pf.createDirectPosition(new double[] {-30, 30}));
        PositionImpl p3 = new PositionImpl(pf.createDirectPosition(new double[] {0, 50}));
        PositionImpl p4 = new PositionImpl(pf.createDirectPosition(new double[] {30, 30}));
        PositionImpl p5 = new PositionImpl(pf.createDirectPosition(new double[] {50, 0}));

        LineStringImpl line1 = null;

        ArrayList<Position> positionList = new ArrayList<Position>();
        positionList.add(p1);
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
        // System.out.println("\nCurve1: " + curve1);

        assertTrue(curve1.isCycle() == false);

        // Set curve for further LineString tests
        line1.setCurve(curve1);

        // System.out.println("\n*** TEST: Curve\n" + curve1);

        // ***** getStartPoint()
        // System.out.println("\n*** TEST: .getStartPoint()\n" + curve1.getStartPoint());
        assertTrue(curve1.getStartPoint().getOrdinate(0) == -50);
        assertTrue(curve1.getStartPoint().getOrdinate(1) == 0);

        // ***** getEndPoint()
        // System.out.println("\n*** TEST: .getEndPoint()\n" + curve1.getEndPoint());
        assertTrue(curve1.getEndPoint().getOrdinate(0) == 50);
        assertTrue(curve1.getEndPoint().getOrdinate(1) == 0);

        // ***** getStartParam()
        // System.out.println("\n*** TEST: .getStartParam()\n" + curve1.getStartParam());
        assertTrue(curve1.getStartParam() == 0.0);

        // ***** getEndParam()
        // System.out.println("\n*** TEST: .getEndParam()\n" + curve1.getEndParam());
        assertTrue(Math.round(line1.getEndParam()) == 144.0);

        // ***** getStartConstructiveParam()
        // System.out.println("\n*** TEST: .getStartConstructiveParam()\n" +
        // curve1.getStartConstructiveParam());
        assertTrue(curve1.getStartConstructiveParam() == 0.0);

        // ***** getEndConstructiveParam()
        // System.out.println("\n*** TEST: .getEndConstructiveParam()\n" +
        // curve1.getEndConstructiveParam());
        assertTrue(curve1.getEndConstructiveParam() == 1.0);

        // ***** getBoundary()
        // System.out.println("\n*** TEST: .getBoundary()\n" + curve1.getBoundary());
        CurveBoundary cb = curve1.getBoundary();
        assertTrue(cb != null);
        double[] dp = cb.getStartPoint().getDirectPosition().getCoordinate();
        assertTrue(dp[0] == -50);
        assertTrue(dp[1] == 0);
        dp = cb.getEndPoint().getDirectPosition().getCoordinate();
        assertTrue(dp[0] == 50);
        assertTrue(dp[1] == 0);

        // ***** getEnvelope()
        // System.out.println("\n*** TEST: .getEnvelope()\n" + curve1.getEnvelope());
        assertTrue(curve1.getEnvelope() != null);
        dp = curve1.getEnvelope().getLowerCorner().getCoordinate();
        assertTrue(dp[0] == -50);
        assertTrue(dp[1] == 0);
        dp = curve1.getEnvelope().getUpperCorner().getCoordinate();
        assertTrue(dp[0] == 50);
        assertTrue(dp[1] == 50);

        // ***** forParam(double distance) : DirectPosition
        dp = curve1.forParam(0).getCoordinate();
        assertTrue(dp[0] == -50);
        assertTrue(dp[1] == 0.0);

        dp = curve1.forParam(curve1.length()).getCoordinate();
        assertTrue(dp[0] == 50);
        assertTrue(dp[1] == 0.0);

        dp = curve1.forParam(50).getCoordinate();
        //// System.out.println("forParam: " + dp[0] + "," + dp[1]);
        assertTrue(Math.round(dp[0] * 1000) == -18397);
        assertTrue(Math.round(dp[1] * 1000) == 37735);

        // ***** forConstructiveParam(double distance)
        dp = curve1.forConstructiveParam(0.0).getCoordinate();
        assertTrue(dp[0] == -50);
        assertTrue(dp[1] == 0.0);

        dp = curve1.forConstructiveParam(1.0).getCoordinate();
        assertTrue(dp[0] == 50);
        assertTrue(dp[1] == 0.0);

        dp = curve1.forConstructiveParam(50 / curve1.length()).getCoordinate();
        assertTrue(Math.round(dp[0] * 1000) == -18397);
        assertTrue(Math.round(dp[1] * 1000) == 37735);

        // ***** getTangent(double distance)
        dp = curve1.getTangent(0);
        //// System.out.println("tangent: " + dp[0] + "," + dp[1]);
        assertTrue(Math.round(dp[0] * 1000) == -49445);
        assertTrue(Math.round(dp[1] * 1000) == 832);

        dp = curve1.getTangent(40);
        assertTrue(Math.round(dp[0] * 100) == -2589);
        assertTrue(Math.round(dp[1] * 100) == 3274);

        dp = curve1.getTangent(curve1.getEndParam());
        // System.out.println("tangent: " + dp[0] + "," + dp[1]);
        assertTrue(Math.round(dp[0] * 100) == 5055);
        assertTrue(Math.round(dp[1] * 100) == -83);

        // ***** getRepresentativePoint()
        dp = curve1.getRepresentativePoint().getCoordinate();
        //// System.out.print("REPRER" + dp);
        assertTrue(dp[0] == 0);
        assertTrue(dp[1] == 50);

        // ***** Curve.Merge(Curve)

        DirectPosition p6 = pf.createDirectPosition(new double[] {80, 20});
        DirectPosition p7 = pf.createDirectPosition(new double[] {130, 60});

        List<DirectPosition> directPositions = new ArrayList<DirectPosition>();

        directPositions.add(p5.getDirectPosition());
        directPositions.add(p6);
        directPositions.add(p7);
        CurveImpl curve2 = (CurveImpl) tPrimFactory.createCurveByDirectPositions(directPositions);

        CurveImpl curve3 = curve1.merge(curve2);
        // System.out.println("Curve1: " + curve1);
        // System.out.println("Curve2: " + curve2);
        // System.out.println("Merge: " + curve3);
        // Lists of line1 and line2 are not modified
        assertTrue(curve1.asDirectPositions().size() == 5);
        assertTrue(curve2.asDirectPositions().size() == 3);
        // New LineString has combined positions
        assertTrue(curve3.asDirectPositions().size() == 7);

        curve3 = curve2.merge(curve1);
        // System.out.println("Curve1: " + curve1);
        // System.out.println("Curve2: " + curve2);
        // System.out.println("Merge: " + curve3);
        // Lists of line1 and line2 are not modified
        assertTrue(curve1.asDirectPositions().size() == 5);
        assertTrue(curve2.asDirectPositions().size() == 3);
        // New LineString has combined positions
        assertTrue(curve3.asDirectPositions().size() == 7);

        directPositions.remove(0);
        curve2 = (CurveImpl) tPrimFactory.createCurveByDirectPositions(directPositions);
        curve3 = null;
        try {
            curve3 = curve2.merge(curve1);
        } catch (IllegalArgumentException e) {
            //
        }
        // Merge of two not touching linestrings does not work
        assertTrue(curve3 == null);

        Complex cc1 = curve1.getClosure();
        // System.out.println(cc1);
        assertTrue(cc1 instanceof CompositeCurve);
    }

    public void testOtherCurveMethods() {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        PicoContainer container = container(crs); // normal 2D
        // PrimitiveFactoryImpl factory = (PrimitiveFactoryImpl)
        // container.getComponentInstanceOfType( PrimitiveFactoryImpl.class );
        PositionFactory positionFactory =
                (PositionFactory) container.getComponentInstanceOfType(PositionFactory.class);

        DirectPosition positionA = positionFactory.createDirectPosition(new double[] {10, 10});
        DirectPosition positionB = positionFactory.createDirectPosition(new double[] {20, 20});

        CurveImpl curve = createCurve(positionA, positionB);

        // test split (split seems broken)
        // curve.split(5);

        // test getSegmentAt
        CurveSegmentImpl segmentAt = curve.getSegmentAt(1);
        assertTrue(segmentAt.equals(curve.asLineSegments().get(0)));

        // test getParamForPoint (not fully implemented yet)
        // ParamForPoint paramForPoint = curve.getParamForPoint(positionA);
        // System.out.println(paramForPoint);

        // test length
        double d = curve.length(0, 1);
        assertTrue(d == 1);
        // the following length uses getParamForPoint and it's not fully implemented yet)
        // double e = curve.length(positionA, positionB);
        // System.out.println(e);

        // test asLineString
        LineStringImpl linestring = curve.asLineString();
        List<Position> posList = new ArrayList<Position>();
        posList.add(positionA);
        posList.add(positionB);
        assertTrue(linestring.equals(new LineStringImpl(posList)));

        // test getDimension
        assertEquals(curve.getDimension(positionA), 1);

        // test toString
        String toS = curve.toString();
        assertTrue(toS != null);
        assertTrue(toS.length() > 0);

        // test obj equals
        CurveImpl curve2 = null;
        try {
            curve2 = curve.clone();
        } catch (CloneNotSupportedException e) {
            java.util.logging.Logger.getGlobal().log(java.util.logging.Level.INFO, "", e);
            fail();
        }

        assertTrue(curve.equals((Object) curve2));
        assertTrue(curve.equals((Object) curve));
        assertFalse(curve.equals((Object) positionA));
        assertFalse(curve.equals((Object) null));
        DirectPosition positionC = positionFactory.createDirectPosition(new double[] {30, 30});
        CurveImpl curve3 = createCurve(positionA, positionC);
        assertFalse(curve.equals((Object) curve3));
    }

    public void testCurveAgain() {

        GeometryBuilder builder = new GeometryBuilder(DefaultGeographicCRS.WGS84);
        PositionFactory posF = builder.getPositionFactory();
        PrimitiveFactory primF = builder.getPrimitiveFactory();
        GeometryFactory geomF = builder.getGeometryFactory();

        //		 create directpositions
        DirectPosition start = posF.createDirectPosition(new double[] {48.44, -123.37});
        DirectPosition middle = posF.createDirectPosition(new double[] {47, -122});
        DirectPosition end = posF.createDirectPosition(new double[] {46.5, -121.5});

        //		 add directpositions to a list
        ArrayList<Position> positions = new ArrayList<Position>();
        positions.add(start);
        positions.add(middle);
        positions.add(end);

        //		 create linestring from directpositions
        LineString line = geomF.createLineString(positions);

        //		 create curvesegments from line
        ArrayList<CurveSegment> segs = new ArrayList<CurveSegment>();
        segs.add(line);

        //		 create curve
        Curve curve = primF.createCurve(segs);

        // get points from curve
        List<? extends CurveSegment> segs2 = curve.getSegments();
        Iterator<? extends CurveSegment> iter = segs2.iterator();
        PointArray samplePoints = null;
        while (iter.hasNext()) {
            if (samplePoints == null) {
                samplePoints = iter.next().getSamplePoints();
            } else {
                samplePoints.addAll(iter.next().getSamplePoints());
            }
        }
    }
}
