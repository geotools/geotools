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
package org.geotools.geometry;

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
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Quick implementation for testing purposes.
 * 
 * @author Jody
 *
 *
 * @source $URL$
 */
public class MockGeometryFactory implements GeometryFactory, PrimitiveFactory {
    CoordinateReferenceSystem crs;
    public Precision precision;
    public MockGeometryFactory() {
        this(DefaultGeographicCRS.WGS84);
    }
    public MockGeometryFactory( CoordinateReferenceSystem crs ) {
        this.crs = crs;
    }
    public Arc createArc( Position startPoint, Position midPoint, Position endPoint )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public Arc createArc( Position startPoint, Position endPoint, double bulge, double[] normal )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public ArcByBulge createArcByBulge( Position startPoint, Position endPoint, double bulge,
            double[] normal ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return null;
    }

    public ArcString createArcString( List points ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return null;
    }

    public ArcStringByBulge createArcStringByBulge( List points, double[] bulges, List normals )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public BSplineCurve createBSplineCurve( int degree, PointArray points, List knots,
            KnotType knotSpec ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return null;
    }

    public BSplineSurface createBSplineSurface( List points, int[] degree, List[] knots,
            KnotType knotSpec ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return null;
    }

    public DirectPosition createDirectPosition() {
        return new MockDirectPosition();
    }

    public DirectPosition createDirectPosition( double[] coordinates ) {
        return new MockDirectPosition(coordinates);
    }
    class MockDirectPosition implements DirectPosition {
        double[] coordinates;
        MockDirectPosition() {
            this(new double[crs.getCoordinateSystem().getDimension()]);
        }

        public MockDirectPosition( double[] coordinates ) {
            this.coordinates = coordinates;
        }

        public MockDirectPosition( DirectPosition position ) {
            assert position.getCoordinateReferenceSystem() == crs;
            coordinates = position.getCoordinates();
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }
        public double[] getCoordinate() {
            double copy[] = new double[crs.getCoordinateSystem().getDimension()];
            System.arraycopy(coordinates, 0, copy, 0, getDimension());
            return copy;
        }
        @Deprecated
        public double[] getCoordinates() {
            return getCoordinate();
        }
        public int getDimension() {
            return crs.getCoordinateSystem().getDimension();
        }

        public double getOrdinate( int dimension ) throws IndexOutOfBoundsException {
            return coordinates[dimension];
        }

        public void setOrdinate( int dimension, double value ) throws IndexOutOfBoundsException {
            coordinates[dimension] = value;

        }
        public DirectPosition getDirectPosition() {
            return this;
        }
        @Deprecated
        public DirectPosition getPosition() {
            return this;
        }
        public MockDirectPosition clone() {
            return new MockDirectPosition(this);
        }
    }

    public Envelope createEnvelope( final DirectPosition lowerCorner,
            final DirectPosition upperCorner ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return new Envelope(){
            public double getCenter( int dimension ) {
                return getMedian( dimension );
            }
            public double getMedian( int dimension ) {
                double lower = lowerCorner.getOrdinate(dimension);
                double upper = upperCorner.getOrdinate(dimension);
                return (upper + lower) / 2.0;
            }
            public CoordinateReferenceSystem getCoordinateReferenceSystem() {
                return crs;
            }
            public int getDimension() {
                return crs.getCoordinateSystem().getDimension();
            }
            public double getLength( int dimension ) {
                return getSpan( dimension );
            }
            public double getSpan( int dimension ) {
                double lower = lowerCorner.getOrdinate(dimension);
                double upper = upperCorner.getOrdinate(dimension);
                return Math.abs(upper - lower);
            }
            public DirectPosition getLowerCorner() {
                return lowerCorner;
            }

            public double getMaximum( int dimension ) {
                return upperCorner.getOrdinate(dimension);
            }

            public double getMinimum( int dimension ) {
                return lowerCorner.getOrdinate(dimension);
            }

            public DirectPosition getUpperCorner() {
                return upperCorner;
            }
        };
    }

    public Geodesic createGeodesic( Position startPoint, Position endPoint )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }

    public GeodesicString createGeodesicString( List points )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public LineSegment createLineSegment( Position startPoint, Position endPoint )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    /** Takes a List<Position> ... */
    public LineString createLineString( List points ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return new LineString(){
            PointArray points;
            public List asLineSegments() {
                return null;
            }
            public PointArray getControlPoints() {
                return null;
            }
            public CurveBoundary getBoundary() {
                return null;
            }

            public Curve getCurve() {
                return null;
            }

            public CurveInterpolation getInterpolation() {
                return null;
            }

            public int getNumDerivativesAtEnd() {
                return 0;
            }

            public int getNumDerivativesAtStart() {
                return 0;
            }

            public int getNumDerivativesInterior() {
                return 0;
            }

            public PointArray getSamplePoints() {
                return null;
            }

            public CurveSegment reverse() {
                return null;
            }

            public LineString asLineString( double maxSpacing, double maxOffset ) {
                return this;
            }

            public DirectPosition forConstructiveParam( double cp ) {
                return null;
            }

            public DirectPosition forParam( double s ) {
                return null;
            }

            public double getEndConstructiveParam() {
                return 0;
            }

            public double getEndParam() {
                return 0;
            }

            public DirectPosition getEndPoint() {
                return points.getDirectPosition(points.size() - 1, null);
            }

            public ParamForPoint getParamForPoint( DirectPosition p ) {
                return null;
            }

            public double getStartConstructiveParam() {
                return 0;
            }

            public double getStartParam() {
                return 0;
            }

            public DirectPosition getStartPoint() {
                return points.getDirectPosition(0, null);
            }

            public double[] getTangent( double s ) {
                return null;
            }

            public double length( Position point1, Position point2 ) {
                return 0;
            }

            public double length( double cparam1, double cparam2 ) {
                return 0;
            }
        };
    }

    public MultiPrimitive createMultiPrimitive() {
        return null;
    }

    public Polygon createPolygon( SurfaceBoundary boundary )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public Polygon createPolygon( SurfaceBoundary boundary, Surface spanSurface )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public PolyhedralSurface createPolyhedralSurface( List tiles )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public Tin createTin( Set post, Set stopLines, Set breakLines, double maxLength )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return null;
    }

    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return null;
    }
    public Curve createCurve( final List segments ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return new MockCurve(segments);
    }
    class MockCurve implements Curve {
        List segments;
        MockCurve( List segments ) {
            this.segments = segments;
        }
        public List getSegments() {
            return segments;
        }
        public MockCurve clone() {
            return new MockCurve(getSegments());
        }
        public CompositeCurve getComposite() {
            return null;
        }

        public int getOrientation() {
            return 0;
        }
        public MockCurve getPrimitive() {
            return this;
        }

        public Set getComplexes() {
            return null;
        }

        public Set getContainedPrimitives() {
            // TODO Auto-generated method stub
            return null;
        }

        public Set getContainingPrimitives() {
            return null;
        }

        public OrientableCurve[] getProxy() {
            return null;
        }

        public CurveBoundary getBoundary() {
            return null;
        }

        public Geometry getBuffer( double distance ) {
            return null;
        }

        public DirectPosition getCentroid() {
            return null;
        }

        public Complex getClosure() {
            return null;
        }

        public Geometry getConvexHull() {
            return null;
        }

        public int getCoordinateDimension() {
            return crs.getCoordinateSystem().getDimension();
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        public int getDimension( DirectPosition point ) {
            return 0;
        }

        public double distance(Geometry geometry) {
            return 0;
        }

        public double getDistance(Geometry geometry) {
            return distance(geometry);
        }

        public Envelope getEnvelope() {
            return null;
        }

        public Set getMaximalComplex() {
            return null;
        }

        public Geometry getMbRegion() {
            return null;
        }

        public Precision getPrecision() {
            return precision;
        }

        public DirectPosition getRepresentativePoint() {
            return null;
        }

        public boolean isCycle() {
            return false;
        }

        public boolean isMutable() {
            return false;
        }

        public boolean isSimple() {
            return false;
        }

        public Geometry toImmutable() {
            return this;
        }

        public Geometry transform( CoordinateReferenceSystem newCRS ) throws TransformException {
            return null;
        }

        public Geometry transform( CoordinateReferenceSystem newCRS, MathTransform transform )
                throws TransformException {
            return null;
        }

        public boolean contains( TransfiniteSet pointSet ) {
            return false;
        }

        public boolean contains( DirectPosition point ) {
            return false;
        }

        public TransfiniteSet difference( TransfiniteSet pointSet ) {
            return null;
        }

        public boolean equals( TransfiniteSet pointSet ) {
            return false;
        }

        public TransfiniteSet intersection( TransfiniteSet pointSet ) {
            return null;
        }

        public boolean intersects( TransfiniteSet pointSet ) {
            return false;
        }

        public TransfiniteSet symmetricDifference( TransfiniteSet pointSet ) {
            return null;
        }

        public TransfiniteSet union( TransfiniteSet pointSet ) {
            return null;
        }

        public LineString asLineString( double maxSpacing, double maxOffset ) {
            return null;
        }

        public DirectPosition forConstructiveParam( double cp ) {
            return null;
        }

        public DirectPosition forParam( double s ) {
            return null;
        }

        public double getEndConstructiveParam() {
            return 0;
        }

        public double getEndParam() {
            return 0;
        }

        public DirectPosition getEndPoint() {
            return null;
        }

        public ParamForPoint getParamForPoint( DirectPosition p ) {
            return null;
        }

        public double getStartConstructiveParam() {
            return 0;
        }

        public double getStartParam() {
            return 0;
        }

        public DirectPosition getStartPoint() {
            return null;
        }

        public double[] getTangent( double s ) {
            return null;
        }

        public double length( Position point1, Position point2 ) {
            return 0;
        }

        public double length( double cparam1, double cparam2 ) {
            return 0;
        }
    }

    public Point createPoint( double[] coordinates ) throws MismatchedDimensionException {
        return createPoint(createPoint(coordinates));
    }
    public Point createPoint( Position position ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        return new MockPoint(position.getPosition());
    }
    class MockPoint implements Point {
        private DirectPosition position;
        MockPoint( DirectPosition position ) {
            this.position = position;
        }
        public MockPoint clone() {
            return new MockPoint(new MockDirectPosition(position));
        }
        public Bearing getBearing( Position toPoint ) {
            return null;
        }
        public DirectPosition getDirectPosition() {
            return position;
        }
        @Deprecated
        public DirectPosition getPosition() {
            return position;
        }
        public void setDirectPosition( DirectPosition position ) throws UnmodifiableGeometryException {
            this.position = position;
        }
        @Deprecated
        public void setPosition( DirectPosition position ) throws UnmodifiableGeometryException {
            this.position = position;
        }
        public Set getComplexes() {
            return null;
        }

        public Composite getComposite() {
            return null;
        }

        public Set getContainedPrimitives() {
            return null;
        }

        public Set getContainingPrimitives() {
            return null;
        }

        public OrientablePrimitive[] getProxy() {
            return null;
        }

        public PrimitiveBoundary getBoundary() {
            return null;
        }

        public Geometry getBuffer( double distance ) {
            return null;
        }

        public DirectPosition getCentroid() {
            return position;
        }

        public Complex getClosure() {
            return null;
        }

        public Geometry getConvexHull() {
            return null;
        }

        public int getCoordinateDimension() {
            return getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
        }

        public CoordinateReferenceSystem getCoordinateReferenceSystem() {
            return crs;
        }

        public int getDimension( DirectPosition point ) {
            return 0;
        }

        public double distance(Geometry geometry) {
            return 0;
        }

        public double getDistance(Geometry geometry) {
            return distance(geometry);
        }

        public Envelope getEnvelope() {
            return null;
        }

        public Set getMaximalComplex() {
            return null;
        }

        public Geometry getMbRegion() {
            return null;
        }

        public Precision getPrecision() {
            return precision;
        }
        public DirectPosition getRepresentativePoint() {
            return position;
        }
        public boolean isCycle() {
            return false;
        }

        public boolean isMutable() {
            return true;
        }
        public boolean isSimple() {
            return true;
        }
        public Geometry toImmutable() {
            return null;
        }

        public Geometry transform( CoordinateReferenceSystem newCRS ) throws TransformException {
            return null;
        }

        public Geometry transform( CoordinateReferenceSystem newCRS, MathTransform transform )
                throws TransformException {
            return null;
        }

        public boolean contains( TransfiniteSet pointSet ) {
            return pointSet.contains(position);
        }

        public boolean contains( DirectPosition point ) {
            return point.equals(position);
        }

        public TransfiniteSet difference( TransfiniteSet pointSet ) {
            return null;
        }

        public boolean equals( TransfiniteSet pointSet ) {
            return false;
        }

        public TransfiniteSet intersection( TransfiniteSet pointSet ) {
            return null;
        }

        public boolean intersects( TransfiniteSet pointSet ) {
            return false;
        }

        public TransfiniteSet symmetricDifference( TransfiniteSet pointSet ) {
            return null;
        }

        public TransfiniteSet union( TransfiniteSet pointSet ) {
            return null;
        }
    }

    public Primitive createPrimitive( Envelope envelope )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
    public Ring createRing( List curves ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
    public Solid createSolid( SolidBoundary boundary ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
    public Surface createSurface( List surfaces ) throws MismatchedReferenceSystemException,
            MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
    public Surface createSurface( SurfaceBoundary boundary )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
    public SurfaceBoundary createSurfaceBoundary( Ring exterior, List interiors )
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // TODO Auto-generated method stub
        return null;
    }
}
