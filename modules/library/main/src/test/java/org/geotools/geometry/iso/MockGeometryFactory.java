/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.iso;

import java.util.List;
import java.util.Set;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.Precision;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.UnmodifiableGeometryException;
import org.opengis.geometry.aggregate.MultiPrimitive;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.complex.Composite;
import org.opengis.geometry.complex.CompositeCurve;
import org.opengis.geometry.coordinate.Arc;
import org.opengis.geometry.coordinate.ArcByBulge;
import org.opengis.geometry.coordinate.ArcString;
import org.opengis.geometry.coordinate.ArcStringByBulge;
import org.opengis.geometry.coordinate.BSplineCurve;
import org.opengis.geometry.coordinate.BSplineSurface;
import org.opengis.geometry.coordinate.Geodesic;
import org.opengis.geometry.coordinate.GeodesicString;
import org.opengis.geometry.coordinate.GeometryFactory;
import org.opengis.geometry.coordinate.Knot;
import org.opengis.geometry.coordinate.KnotType;
import org.opengis.geometry.coordinate.LineSegment;
import org.opengis.geometry.coordinate.LineString;
import org.opengis.geometry.coordinate.ParamForPoint;
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Tin;
import org.opengis.geometry.primitive.Bearing;
import org.opengis.geometry.primitive.Curve;
import org.opengis.geometry.primitive.CurveBoundary;
import org.opengis.geometry.primitive.CurveInterpolation;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientablePrimitive;
import org.opengis.geometry.primitive.Point;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.PrimitiveBoundary;
import org.opengis.geometry.primitive.PrimitiveFactory;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Solid;
import org.opengis.geometry.primitive.SolidBoundary;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.geometry.primitive.SurfacePatch;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Quick implementation for testing purposes.
 *
 * @author Jody
 */
public class MockGeometryFactory implements GeometryFactory, PrimitiveFactory {
    CoordinateReferenceSystem crs;
    public Precision precision;
    MockPositionFactory pf;

    public MockGeometryFactory() {
        this(DefaultGeographicCRS.WGS84);
    }

    public MockGeometryFactory(CoordinateReferenceSystem crs) {
        this.crs = crs;
        this.pf = new MockPositionFactory(crs);
    }

    @Override
    public Arc createArc(Position startPoint, Position midPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public Arc createArc(Position startPoint, Position endPoint, double bulge, double[] normal)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public ArcByBulge createArcByBulge(
            Position startPoint, Position endPoint, double bulge, double[] normal)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public ArcString createArcString(List<Position> points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public ArcStringByBulge createArcStringByBulge(
            List<Position> points, double[] bulges, List<double[]> normals)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public BSplineCurve createBSplineCurve(
            int degree, PointArray points, List<Knot> knots, KnotType knotSpec)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public BSplineSurface createBSplineSurface(
            List<PointArray> points, int[] degree, List<Knot>[] knots, KnotType knotSpec)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public Envelope createEnvelope(
            final DirectPosition lowerCorner, final DirectPosition upperCorner)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return new Envelope() {
            @Override
            public double getMedian(int dimension) {
                double lower = lowerCorner.getOrdinate(dimension);
                double upper = upperCorner.getOrdinate(dimension);
                return (upper + lower) / 2.0;
            }

            @Override
            public CoordinateReferenceSystem getCoordinateReferenceSystem() {
                return crs;
            }

            @Override
            public int getDimension() {
                return crs.getCoordinateSystem().getDimension();
            }

            public double getLength(int dimension) {
                return getSpan(dimension);
            }

            @Override
            public double getSpan(int dimension) {
                double lower = lowerCorner.getOrdinate(dimension);
                double upper = upperCorner.getOrdinate(dimension);
                return Math.abs(upper - lower);
            }

            @Override
            public DirectPosition getLowerCorner() {
                return lowerCorner;
            }

            @Override
            public double getMaximum(int dimension) {
                return upperCorner.getOrdinate(dimension);
            }

            @Override
            public double getMinimum(int dimension) {
                return lowerCorner.getOrdinate(dimension);
            }

            @Override
            public DirectPosition getUpperCorner() {
                return upperCorner;
            }
        };
    }

    @Override
    public Geodesic createGeodesic(Position startPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public GeodesicString createGeodesicString(List<Position> points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public LineSegment createLineSegment(Position startPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    /** Takes a List<Position> ... */
    @Override
    public LineString createLineString(List<Position> points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return new LineString() {
            PointArray points;

            @Override
            public List<LineSegment> asLineSegments() {
                return null;
            }

            @Override
            public PointArray getControlPoints() {
                return null;
            }

            @Override
            public CurveBoundary getBoundary() {
                return null;
            }

            @Override
            public Curve getCurve() {
                return null;
            }

            @Override
            public CurveInterpolation getInterpolation() {
                return null;
            }

            @Override
            public int getNumDerivativesAtEnd() {
                return 0;
            }

            @Override
            public int getNumDerivativesAtStart() {
                return 0;
            }

            @Override
            public int getNumDerivativesInterior() {
                return 0;
            }

            @Override
            public PointArray getSamplePoints() {
                return null;
            }

            @Override
            public CurveSegment reverse() {
                return null;
            }

            @Override
            public LineString asLineString(double maxSpacing, double maxOffset) {
                return this;
            }

            @Override
            public DirectPosition forConstructiveParam(double cp) {
                return null;
            }

            @Override
            public DirectPosition forParam(double s) {
                return null;
            }

            @Override
            public double getEndConstructiveParam() {
                return 0;
            }

            @Override
            public double getEndParam() {
                return 0;
            }

            @Override
            public DirectPosition getEndPoint() {
                return points.getDirectPosition(points.size() - 1, null);
            }

            @Override
            public ParamForPoint getParamForPoint(DirectPosition p) {
                return null;
            }

            @Override
            public double getStartConstructiveParam() {
                return 0;
            }

            @Override
            public double getStartParam() {
                return 0;
            }

            @Override
            public DirectPosition getStartPoint() {
                return points.getDirectPosition(0, null);
            }

            @Override
            public double[] getTangent(double s) {
                return null;
            }

            @Override
            public double length(Position point1, Position point2) {
                return 0;
            }

            @Override
            public double length(double cparam1, double cparam2) {
                return 0;
            }
        };
    }

    @Override
    public MultiPrimitive createMultiPrimitive() {
        return null;
    }

    @Override
    public Polygon createPolygon(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public Polygon createPolygon(SurfaceBoundary boundary, Surface spanSurface)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public PolyhedralSurface createPolyhedralSurface(List<Polygon> tiles)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public Tin createTin(
            Set<Position> post,
            Set<LineString> stopLines,
            Set<LineString> breakLines,
            double maxLength)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    @Override
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return null;
    }

    @Override
    public Curve createCurve(final List<CurveSegment> segments)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return new MockCurve(segments);
    }

    class MockCurve implements Curve {
        List<? extends CurveSegment> segments;

        MockCurve(List<? extends CurveSegment> segments) {
            this.segments = segments;
        }

        @Override
        public List<? extends CurveSegment> getSegments() {
            return segments;
        }

        @Override
        public MockCurve clone() {
            return new MockCurve(getSegments());
        }

        @Override
        public CompositeCurve getComposite() {
            return null;
        }

        @Override
        public int getOrientation() {
            return 0;
        }

        @Override
        public MockCurve getPrimitive() {
            return this;
        }

        @Override
        public Set<Complex> getComplexes() {
            return null;
        }

        @Override
        public Set<Primitive> getContainedPrimitives() {
            // TODO Auto-generated method stub
            return null;
        }

        @Override
        public Set<Primitive> getContainingPrimitives() {
            return null;
        }

        @Override
        public OrientableCurve[] getProxy() {
            return null;
        }

        @Override
        public CurveBoundary getBoundary() {
            return null;
        }

        @Override
        public Geometry getBuffer(double distance) {
            return null;
        }

        @Override
        public DirectPosition getCentroid() {
            return null;
        }

        @Override
        public Complex getClosure() {
            return null;
        }

        @Override
        public Geometry getConvexHull() {
            return null;
        }

        @Override
        public int getCoordinateDimension() {
            return crs.getCoordinateSystem().getDimension();
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        @Override
        public int getDimension(DirectPosition point) {
            return 0;
        }

        @Override
        public double distance(Geometry geometry) {
            return 0;
        }

        public double getDistance(Geometry geometry) {
            return distance(geometry);
        }

        @Override
        public Envelope getEnvelope() {
            return null;
        }

        @Override
        public Set<? extends Complex> getMaximalComplex() {
            return null;
        }

        @Override
        public Geometry getMbRegion() {
            return null;
        }

        @Override
        public Precision getPrecision() {
            return precision;
        }

        @Override
        public DirectPosition getRepresentativePoint() {
            return null;
        }

        @Override
        public boolean isCycle() {
            return false;
        }

        @Override
        public boolean isMutable() {
            return false;
        }

        @Override
        public boolean isSimple() {
            return false;
        }

        @Override
        public Geometry toImmutable() {
            return this;
        }

        @Override
        public Geometry transform(CoordinateReferenceSystem newCRS) throws TransformException {
            return null;
        }

        @Override
        public Geometry transform(CoordinateReferenceSystem newCRS, MathTransform transform)
                throws TransformException {
            return null;
        }

        @Override
        public boolean contains(TransfiniteSet pointSet) {
            return false;
        }

        @Override
        public boolean contains(DirectPosition point) {
            return false;
        }

        @Override
        public TransfiniteSet difference(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public boolean equals(TransfiniteSet pointSet) {
            return false;
        }

        @Override
        public TransfiniteSet intersection(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public boolean intersects(TransfiniteSet pointSet) {
            return false;
        }

        @Override
        public TransfiniteSet symmetricDifference(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public TransfiniteSet union(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public LineString asLineString(double maxSpacing, double maxOffset) {
            return null;
        }

        @Override
        public DirectPosition forConstructiveParam(double cp) {
            return null;
        }

        @Override
        public DirectPosition forParam(double s) {
            return null;
        }

        @Override
        public double getEndConstructiveParam() {
            return 0;
        }

        @Override
        public double getEndParam() {
            return 0;
        }

        @Override
        public DirectPosition getEndPoint() {
            return null;
        }

        @Override
        public ParamForPoint getParamForPoint(DirectPosition p) {
            return null;
        }

        @Override
        public double getStartConstructiveParam() {
            return 0;
        }

        @Override
        public double getStartParam() {
            return 0;
        }

        @Override
        public DirectPosition getStartPoint() {
            return null;
        }

        @Override
        public double[] getTangent(double s) {
            return null;
        }

        @Override
        public double length(Position point1, Position point2) {
            return 0;
        }

        @Override
        public double length(double cparam1, double cparam2) {
            return 0;
        }
    }

    @Override
    public Point createPoint(double[] coordinates) throws MismatchedDimensionException {
        return createPoint(createPoint(coordinates));
    }

    @Override
    public Point createPoint(Position position)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return new MockPoint(position.getDirectPosition());
    }

    class MockPoint implements Point {
        private DirectPosition position;

        MockPoint(DirectPosition position) {
            this.position = position;
        }

        @Override
        public MockPoint clone() {
            return new MockPoint(pf.createDirectPosition(position.getCoordinate()));
        }

        @Override
        public Bearing getBearing(Position toPoint) {
            return null;
        }

        @Override
        public DirectPosition getDirectPosition() {
            return position;
        }

        @Override
        public void setDirectPosition(DirectPosition position)
                throws UnmodifiableGeometryException {
            this.position = position;
        }

        @Override
        public Set<Complex> getComplexes() {
            return null;
        }

        @Override
        public Composite getComposite() {
            return null;
        }

        @Override
        public Set<Primitive> getContainedPrimitives() {
            return null;
        }

        @Override
        public Set<Primitive> getContainingPrimitives() {
            return null;
        }

        @Override
        public OrientablePrimitive[] getProxy() {
            return null;
        }

        @Override
        public PrimitiveBoundary getBoundary() {
            return null;
        }

        @Override
        public Geometry getBuffer(double distance) {
            return null;
        }

        @Override
        public DirectPosition getCentroid() {
            return position;
        }

        @Override
        public Complex getClosure() {
            return null;
        }

        @Override
        public Geometry getConvexHull() {
            return null;
        }

        @Override
        public int getCoordinateDimension() {
            return getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
        }

        @Override
        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        @Override
        public int getDimension(DirectPosition point) {
            return 0;
        }

        @Override
        public double distance(Geometry geometry) {
            return 0;
        }

        public double getDistance(Geometry geometry) {
            return distance(geometry);
        }

        @Override
        public Envelope getEnvelope() {
            return null;
        }

        @Override
        public Set<Complex> getMaximalComplex() {
            return null;
        }

        @Override
        public Geometry getMbRegion() {
            return null;
        }

        @Override
        public Precision getPrecision() {
            return precision;
        }

        @Override
        public DirectPosition getRepresentativePoint() {
            return position;
        }

        @Override
        public boolean isCycle() {
            return false;
        }

        @Override
        public boolean isMutable() {
            return true;
        }

        @Override
        public boolean isSimple() {
            return true;
        }

        @Override
        public Geometry toImmutable() {
            return null;
        }

        @Override
        public Geometry transform(CoordinateReferenceSystem newCRS) throws TransformException {
            return null;
        }

        @Override
        public Geometry transform(CoordinateReferenceSystem newCRS, MathTransform transform)
                throws TransformException {
            return null;
        }

        @Override
        public boolean contains(TransfiniteSet pointSet) {
            return pointSet.contains(position);
        }

        @Override
        public boolean contains(DirectPosition point) {
            return point.equals(position);
        }

        @Override
        public TransfiniteSet difference(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public boolean equals(TransfiniteSet pointSet) {
            return false;
        }

        @Override
        public TransfiniteSet intersection(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public boolean intersects(TransfiniteSet pointSet) {
            return false;
        }

        @Override
        public TransfiniteSet symmetricDifference(TransfiniteSet pointSet) {
            return null;
        }

        @Override
        public TransfiniteSet union(TransfiniteSet pointSet) {
            return null;
        }
    }

    @Override
    public Primitive createPrimitive(Envelope envelope)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Ring createRing(List<OrientableCurve> curves)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Solid createSolid(SolidBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Surface createSurface(List<SurfacePatch> surfaces)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Surface createSurface(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public SurfaceBoundary createSurfaceBoundary(Ring exterior, List<Ring> interiors)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
}
