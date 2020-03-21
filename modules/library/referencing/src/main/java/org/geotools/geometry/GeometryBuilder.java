/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import org.geotools.referencing.CRS;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.Precision;
import org.opengis.geometry.aggregate.AggregateFactory;
import org.opengis.geometry.aggregate.MultiCurve;
import org.opengis.geometry.aggregate.MultiPoint;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.aggregate.MultiSurface;
import org.opengis.geometry.complex.ComplexFactory;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.complex.CompositePoint;
import org.opengis.geometry.complex.CompositeSurface;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Tin;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Solid;
import org.opengis.geometry.primitive.SolidBoundary;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.cs.AxisDirection;
import org.opengis.referencing.cs.CoordinateSystemAxis;

/**
 * A Builder to help with Geometry creation.
 *
 * <p>The factory interfaces provided by GeoAPI are hard to use in isolation (they are even hard to
 * collect a matched set in order to work on the same problem). The main advantage a "builder" has
 * over a factory is that it does not have to be thread safe and can hold state in order to make
 * your job easier.
 *
 * <p>
 *
 * @author Jody Garnett
 */
public class GeometryBuilder {
    /** Hints used for the duration of this GeometryBuilder. */
    private Hints hints;

    /**
     * CoordinateReferenceSystem used to construct the next geometry artifact.
     *
     * <p>This forms the core state of our builds, all other factories are created with this
     * CoordinateReferenceSystem in mind.
     */
    private CoordinateReferenceSystem crs;

    private PositionFactory positionFactory;
    private PrimitiveFactory primitiveFactory;
    private AggregateFactory aggregateFactory;
    private ComplexFactory complexFactory;
    private GeometryFactory geometryFactory;

    public GeometryBuilder(CoordinateReferenceSystem crs) {
        this.crs = crs;
        this.hints = GeoTools.getDefaultHints();
        hints.put(Hints.CRS, crs);
        hints.put(Hints.GEOMETRY_VALIDATE, true);
    }

    public GeometryBuilder(String code) throws NoSuchAuthorityCodeException, FactoryException {
        this(CRS.decode(code));
    }

    public GeometryBuilder(Hints hints) {
        this.crs = (CoordinateReferenceSystem) hints.get(Hints.CRS);
        this.hints = hints;
        getPositionFactory();
        getPrecision();
        getPrimitiveFactory();
        getAggregateFactory();
        getGeometryFactory();
        getComplexFactory();
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return crs;
    }

    /**
     * Set the CoordinateReferenceSystem for the geometries that will be produced.
     *
     * @param crs the CoordinateReferenceSystem to set
     */
    public void setCoordinateReferenceSystem(CoordinateReferenceSystem crs) {
        if (this.crs != crs) {
            hints.remove(Hints.CRS);
            hints.put(Hints.CRS, crs);
            this.crs = crs;
            positionFactory = null;
            primitiveFactory = null;
            aggregateFactory = null;
            complexFactory = null;
            geometryFactory = null;
            getPositionFactory();
            getPrecision();
            getPrimitiveFactory();
            getAggregateFactory();
            getGeometryFactory();
            getComplexFactory();
        }
    }

    public Precision getPrecision() {
        return getPositionFactory().getPrecision();
    }

    public PositionFactory getPositionFactory() {
        if (positionFactory == null) {
            if (hints.containsKey(Hints.POSITION_FACTORY)) {
                // check the hints for something explicit
                Object factory = hints.get(Hints.POSITION_FACTORY);
                if (factory instanceof PositionFactory) {
                    positionFactory = (PositionFactory) factory;
                    return positionFactory;
                }
            }
            positionFactory = GeometryFactoryFinder.getPositionFactory(hints);
        }
        return positionFactory;
    }

    public PrimitiveFactory getPrimitiveFactory() {
        if (primitiveFactory == null) {
            if (hints.containsKey(Hints.PRIMITIVE_FACTORY)) {
                // check the hints for something explicit
                Object factory = hints.get(Hints.PRIMITIVE_FACTORY);
                if (factory instanceof PrimitiveFactory) {
                    primitiveFactory = (PrimitiveFactory) factory;
                    return primitiveFactory;
                }
            }
            primitiveFactory = GeometryFactoryFinder.getPrimitiveFactory(hints);
        }
        return primitiveFactory;
    }

    public AggregateFactory getAggregateFactory() {
        if (aggregateFactory == null) {
            if (hints.containsKey(Hints.AGGREGATE_FACTORY)) {
                // check the hints for something explicit
                Object factory = hints.get(Hints.AGGREGATE_FACTORY);
                if (factory instanceof AggregateFactory) {
                    aggregateFactory = (AggregateFactory) factory;
                    return aggregateFactory;
                }
            }
            aggregateFactory = GeometryFactoryFinder.getAggregateFactory(hints);
        }
        return aggregateFactory;
    }

    public GeometryFactory getGeometryFactory() {
        if (geometryFactory == null) {
            if (hints.containsKey(Hints.GEOMETRY_FACTORY)) {
                // check the hints for something explicit
                Object factory = hints.get(Hints.GEOMETRY_FACTORY);
                if (factory instanceof GeometryFactory) {
                    geometryFactory = (GeometryFactory) factory;
                    return geometryFactory;
                }
            }
            geometryFactory = GeometryFactoryFinder.getGeometryFactory(hints);
        }
        return geometryFactory;
    }

    public ComplexFactory getComplexFactory() {
        if (complexFactory == null) {
            if (hints.containsKey(Hints.COMPLEX_FACTORY)) {
                // check the hints for something explicit
                Object factory = hints.get(Hints.COMPLEX_FACTORY);
                if (factory instanceof ComplexFactory) {
                    complexFactory = (ComplexFactory) factory;
                    return complexFactory;
                }
            }
            complexFactory = GeometryFactoryFinder.getComplexFactory(hints);
        }
        return complexFactory;
    }

    public DirectPosition createDirectPosition(double[] ordinates) {
        return getPositionFactory().createDirectPosition(ordinates);
    }

    public Position createPosition(Position position) {
        return getPositionFactory().createPosition(position);
    }

    public PointArray createPointArray() {
        return getPositionFactory().createPointArray();
    }

    public PointArray createPointArray(double[] array) {
        return getPositionFactory()
                .createPointArray(
                        array, 0, array.length / crs.getCoordinateSystem().getDimension());
    }

    public PointArray createPointArray(double[] array, int start, int end) {
        return getPositionFactory().createPointArray(array, start, end);
    }

    public PointArray createPositionList(float[] array, int start, int end) {
        return getPositionFactory().createPointArray(array, start, end);
    }

    public Curve createCurve(List segments)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        if (segments == null)
            throw new NullPointerException("Segments are required to create a curve");

        // A curve will be created
        // - The curve will be set as parent curves for the Curve segments
        // - Start and end params for the CurveSegments will be set
        return getPrimitiveFactory().createCurve(segments);
    }

    public Curve createCurve(PointArray points, boolean closed)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        if (points == null) throw new NullPointerException("Points are required to create a curve");
        if (points.size() < 2)
            throw new IllegalArgumentException(
                    "At least two points are required to create a curve");

        // A curve will be created
        // - The curve will be set as parent curves for the Curve segments
        // - Start and end params for the CurveSegments will be set
        List /* <LineSegment> */ segmentList = new ArrayList /* <LineSegment> */();
        for (int i = 0; i < points.size() - 1; i++) {
            int start = i;
            int end = i + 1;
            DirectPosition point1 = points.getDirectPosition(start, null);
            DirectPosition point2 = points.getDirectPosition(end, null);
            LineSegment segment = createLineSegment(point1, point2);
            segmentList.add(segment);
        }

        if (closed) {
            LineSegment first = (LineSegment) segmentList.get(0);
            LineSegment last = (LineSegment) segmentList.get(segmentList.size() - 1);

            if (!first.getStartPoint().equals(last.getEndPoint())) {
                LineSegment segment = createLineSegment(last.getEndPoint(), first.getStartPoint());
                segmentList.add(segment);
            }
        }

        return getPrimitiveFactory().createCurve(segmentList);
    }

    /**
     * Create a point with the provided ordinates.
     *
     * @return createPoint( new double[]{ ord1, ord2})
     */
    public Point createPoint(double ord1, double ord2) {
        return createPoint(new double[] {ord1, ord2});
    }
    /**
     * Create a point with the provided ordinates.
     *
     * @return createPoint( new double[]{ ord1, ord2, ord3 })
     */
    public Point createPoint(double ord1, double ord2, double ord3) {
        return createPoint(new double[] {ord1, ord2, ord3});
    }
    /**
     * Create a point with the provided ordinates
     *
     * @return getPrimitiveFactory().createPoint(coordinates)
     */
    public Point createPoint(double[] ordinates) throws MismatchedDimensionException {
        if (ordinates == null)
            throw new NullPointerException("Ordinates required to create a point");
        int dimension = this.getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
        if (ordinates.length != dimension)
            throw new MismatchedDimensionException(
                    "Create point requires "
                            + dimension
                            + " ordinates ("
                            + ordinates.length
                            + " provided");

        return getPrimitiveFactory().createPoint(ordinates);
    }

    public Point createPoint(Position position)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        if (position == null) {
            throw new NullPointerException();
        }
        setCoordinateReferenceSystem(position.getDirectPosition().getCoordinateReferenceSystem());
        DirectPosition copy =
                (DirectPosition)
                        getPositionFactory()
                                .createDirectPosition(position.getDirectPosition().getCoordinate());
        return getPrimitiveFactory().createPoint(copy);
    }

    public Primitive createPrimitive(Envelope envelope)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        LineSegment segment = processBoundsToSegment(envelope);
        setCoordinateReferenceSystem(envelope.getCoordinateReferenceSystem());
        return processSegmentToPrimitive(envelope, segment, 1);
    }

    private Primitive processSegmentToPrimitive(
            Envelope bounds, LineSegment segment, int dimension) {
        CoordinateSystemAxis axis = crs.getCoordinateSystem().getAxis(dimension);

        if (axis.getDirection() == AxisDirection.OTHER) {
            return processSegmentToPrimitive(bounds, segment, dimension + 1);
        }
        Ring ring = processBoundsToRing(bounds, segment, dimension);
        return processRingToPrimitive(bounds, ring, dimension + 1);
    }

    private Ring processBoundsToRing(Envelope bounds, LineSegment segment, final int D) {
        DirectPosition one =
                getPositionFactory().createDirectPosition(segment.getStartPoint().getCoordinate());
        one.setOrdinate(D, bounds.getMinimum(D));

        DirectPosition two =
                getPositionFactory().createDirectPosition(segment.getEndPoint().getCoordinate());
        two.setOrdinate(D, bounds.getMinimum(D));

        DirectPosition three = getPositionFactory().createDirectPosition(two.getCoordinate());
        three.setOrdinate(D, bounds.getMaximum(D));

        DirectPosition four = getPositionFactory().createDirectPosition(one.getCoordinate());
        four.setOrdinate(D, bounds.getMaximum(D));

        LineSegment edge1 = getGeometryFactory().createLineSegment(one, two);
        LineSegment edge2 = getGeometryFactory().createLineSegment(two, three);
        LineSegment edge3 = getGeometryFactory().createLineSegment(three, four);
        LineSegment edge4 = getGeometryFactory().createLineSegment(four, one);

        List<OrientableCurve> edges = new ArrayList<OrientableCurve>();
        edges.add(createCurve(Arrays.asList(edge1)));
        edges.add(createCurve(Arrays.asList(edge2)));
        edges.add(createCurve(Arrays.asList(edge3)));
        edges.add(createCurve(Arrays.asList(edge4)));
        return createRing(edges);
    }

    private Primitive processRingToPrimitive(Envelope bounds, Ring ring, int dimension) {
        int D = crs.getCoordinateSystem().getDimension();
        if (dimension == D) { // create Surface from ring and return
            SurfaceBoundary boundary = createSurfaceBoundary(ring, Collections.EMPTY_LIST);
            return createSurface(boundary);
        }
        CoordinateSystemAxis axis = crs.getCoordinateSystem().getAxis(dimension);
        if (axis.getDirection() == AxisDirection.OTHER) {
            return processRingToPrimitive(bounds, ring, dimension + 1);
        }
        return processRingToVolumne(bounds, ring, dimension + 1);
    }

    private Primitive processRingToVolumne(Envelope bounds, Ring ring, int i) {
        // go into a volume
        throw new UnsupportedOperationException("Not yet 3D");
    }

    private LineSegment processBoundsToSegment(Envelope bounds) {
        final int D = 0;
        CoordinateReferenceSystem crs = bounds.getCoordinateReferenceSystem();
        CoordinateSystemAxis axis = crs.getCoordinateSystem().getAxis(D);

        DirectPosition positionA = getPositionFactory().createDirectPosition(null);
        DirectPosition positionB = getPositionFactory().createDirectPosition(null);
        if (axis.getDirection() != AxisDirection.OTHER) {
            positionA.setOrdinate(D, bounds.getMinimum(D));
            positionB.setOrdinate(D, bounds.getMaximum(D));
        }
        return getGeometryFactory().createLineSegment(positionA, positionB);
    }

    public Ring createRing(List<OrientableCurve> orientableCurves)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        /**
         * Creates a Ring from triple Array of DirectPositions (Array of arrays, which each
         * represent a future Curve. Each array contain an array of positions, which each represent
         * a future lineString)
         */
        return getPrimitiveFactory().createRing(orientableCurves);
    }

    public Solid createSolid(SolidBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getPrimitiveFactory().createSolid(boundary);
    }

    public SurfaceBoundary createSurfaceBoundary(PointArray points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        Curve curve = createCurve(points, true);
        return createSurfaceBoundary(curve);
    }

    public Surface createSurface(List surfaces)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getPrimitiveFactory().createSurface(surfaces);
    }

    public Surface createSurface(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getPrimitiveFactory().createSurface(boundary);
    }

    public SurfaceBoundary createSurfaceBoundary(Ring exterior, List interiors)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getPrimitiveFactory().createSurfaceBoundary(exterior, interiors);
    }

    public SurfaceBoundary createSurfaceBoundary(Ring exterior)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return createSurfaceBoundary(exterior, new ArrayList<Ring>());
    }

    public SurfaceBoundary createSurfaceBoundary(OrientableCurve curve)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        List<OrientableCurve> exterior = new ArrayList<OrientableCurve>(1);
        exterior.add(curve);
        Ring ring = createRing(exterior);
        return createSurfaceBoundary(ring);
    }

    /* not implemented in GeometryFactory yet
    public Arc createArc(Position startPoint, Position midPoint, Position endPoint) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public Arc createArc(Position startPoint, Position endPoint, double bulge, double[] normal) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public ArcByBulge createArcByBulge(Position startPoint, Position endPoint, double bulge, double[] normal) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public ArcString createArcString(List points) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public ArcStringByBulge createArcStringByBulge(List points, double[] bulges, List normals) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public BSplineCurve createBSplineCurve(int degree, PointArray points, List knots, KnotType knotSpec) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public BSplineSurface createBSplineSurface(List points, int[] degree, List[] knots, KnotType knotSpec) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public Geodesic createGeodesic(Position startPoint, Position endPoint) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }

    public GeodesicString createGeodesicString(List points) throws MismatchedReferenceSystemException, MismatchedDimensionException {
    	// TODO Auto-generated method stub
    	return null;
    }
    */

    public DirectPosition createDirectPosition() {
        return createDirectPosition(null);
    }

    public Envelope createEnvelope(DirectPosition lowerCorner, DirectPosition upperCorner)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createEnvelope(lowerCorner, upperCorner);
    }

    public LineSegment createLineSegment(Position startPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createLineSegment(startPoint, endPoint);
    }

    public LineString createLineString(List points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createLineString(points);
    }

    public LineString createLineString(PointArray points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createLineString(points);
    }

    public LineSegment createLineSegment(DirectPosition from, DirectPosition to) {
        return getGeometryFactory().createLineSegment(from, to);
    }

    @SuppressWarnings("deprecation")
    public MultiPrimitive createMultiPrimitive() {
        return getGeometryFactory().createMultiPrimitive();
    }

    public Polygon createPolygon(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createPolygon(boundary);
    }

    public Polygon createPolygon(SurfaceBoundary boundary, Surface spanSurface)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createPolygon(boundary, spanSurface);
    }

    public PolyhedralSurface createPolyhedralSurface(List tiles)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createPolyhedralSurface(tiles);
    }

    public Tin createTin(Set post, Set stopLines, Set breakLines, double maxLength)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return getGeometryFactory().createTin(post, stopLines, breakLines, maxLength);
    }

    public CompositeCurve createCompositeCurve(List generator) {
        return getComplexFactory().createCompositeCurve(generator);
    }

    public CompositePoint createCompositePoint(Point generator) {
        return getComplexFactory().createCompositePoint(generator);
    }

    public CompositeSurface createCompositeSurface(List generator) {
        return getComplexFactory().createCompositeSurface(generator);
    }

    public MultiCurve createMultiCurve(Set curves) {
        return getAggregateFactory().createMultiCurve(curves);
    }

    public MultiPoint createMultiPoint(Set points) {
        return getAggregateFactory().createMultiPoint(points);
    }

    public MultiPrimitive createMultiPrimitive(Set primitives) {
        return getAggregateFactory().createMultiPrimitive(primitives);
    }

    public MultiSurface createMultiSurface(Set surfaces) {
        return getAggregateFactory().createMultiSurface(surfaces);
    }
}
