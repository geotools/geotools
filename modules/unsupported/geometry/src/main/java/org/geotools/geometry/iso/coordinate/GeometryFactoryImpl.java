/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.geotools.geometry.GeometryFactoryFinder;
import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.util.factory.Factory;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.MismatchedReferenceSystemException;
import org.opengis.geometry.PositionFactory;
import org.opengis.geometry.aggregate.MultiPrimitive;
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
import org.opengis.geometry.coordinate.PointArray;
import org.opengis.geometry.coordinate.Polygon;
import org.opengis.geometry.coordinate.PolyhedralSurface;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.coordinate.Tin;
import org.opengis.geometry.coordinate.Triangle;
import org.opengis.geometry.coordinate.TriangulatedSurface;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.Ring;
import org.opengis.geometry.primitive.Surface;
import org.opengis.geometry.primitive.SurfaceBoundary;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * convenience methods to create objects of the coordinate geometry package using only java native
 * types as parameters
 *
 * @author Jackson Roehrig & Sanjay Jena
 */
public class GeometryFactoryImpl implements Factory, GeometryFactory {

    // **************************************************************************
    // **************************************************************************
    // ***** FACTORY MANAGING METHODS *****
    // **************************************************************************
    // **************************************************************************

    // private FeatGeomFactoryImpl geometryFactory;
    private CoordinateReferenceSystem crs;
    private PositionFactory positionFactory;
    private Map hintsWeCareAbout = new HashMap();

    /** FactorySPI entry point */
    public GeometryFactoryImpl() {
        this((Hints) null);
    }

    /** Just the defaults, use GeometryFactoryFinder for the rest */
    public GeometryFactoryImpl(Hints hints) {
        if (hints == null) {
            this.crs = DefaultGeographicCRS.WGS84;
            hints = GeoTools.getDefaultHints();
            hints.put(Hints.CRS, crs);
        } else {
            this.crs = (CoordinateReferenceSystem) hints.get(Hints.CRS);
            if (crs == null) {
                throw new NullPointerException(
                        "A CRS Hint is required in order to use GeometryFactoryImpl");
            }
        }

        this.positionFactory = GeometryFactoryFinder.getPositionFactory(hints);
        hintsWeCareAbout.put(Hints.CRS, crs);
        hintsWeCareAbout.put(Hints.POSITION_FACTORY, positionFactory);
    }

    /** @param crs */
    public GeometryFactoryImpl(CoordinateReferenceSystem crs, PositionFactory pf) {
        this.crs = crs;
        this.positionFactory = pf;
        hintsWeCareAbout.put(Hints.CRS, crs);
        hintsWeCareAbout.put(Hints.POSITION_FACTORY, positionFactory);
    }

    /**
     * Report back to FactoryRegistry about our configuration.
     *
     * <p>FactoryRegistry will check to make sure that there are no duplicates created (so there
     * will be only a "single" PositionFactory created with this configuration).
     */
    public Map getImplementationHints() {
        return Collections.unmodifiableMap(hintsWeCareAbout);
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#getCoordinateReferenceSystem()
     */
    public CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return this.crs;
    }

    /**
     * Returns the Coordinate Dimension of the used Coordinate System (Sanjay)
     *
     * @return dimension Coordinate Dimension used in this Factory
     */
    public int getDimension() {
        //  Test OK
        return this.crs.getCoordinateSystem().getDimension();
    }

    // **************************************************************************
    // **************************************************************************
    // ***** METHODS SPECIFIED BY THE GeoAPI *****
    // **************************************************************************
    // **************************************************************************

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createDirectPosition()
     */
    public DirectPosition createDirectPosition() {
        // Test ok
        return (DirectPosition) positionFactory.createDirectPosition(null);
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createDirectPosition(double[])
     */
    public DirectPosition createDirectPosition(double[] coord) {
        // Test ok
        if (coord == null)
            throw new IllegalArgumentException("Parameter coord is null"); // $NON-NLS-1$
        if (coord.length != this.getDimension()) throw new MismatchedDimensionException();
        // Create a DirectPosition which references to a COPY of the given double array
        return (DirectPosition) positionFactory.createDirectPosition(coord);
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createEnvelope(org.opengis.geometry.coordinate.DirectPosition, org.opengis.geometry.coordinate.DirectPosition)
     */
    public Envelope createEnvelope(DirectPosition lowerCorner, DirectPosition upperCorner)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // Test ok
        return new EnvelopeImpl(
                (DirectPosition) positionFactory.createPosition(lowerCorner),
                (DirectPosition) positionFactory.createPosition(upperCorner));
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createLineSegment(org.opengis.geometry.coordinate.Position, org.opengis.geometry.coordinate.Position)
     */
    public LineSegment createLineSegment(Position startPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // Test ok
        // Return a LineSegment based on the start and end point.
        // - The startParam will be initialized with 0.0.
        // - The parent curve will not be set
        return createLineSegment(startPoint, endPoint, 0.0);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.Factory#createPolygon(org.opengis.geometry.primitive.SurfaceBoundary)
     */
    public Polygon createPolygon(SurfaceBoundary boundary)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // ok
        // Creates a SurfaceBoundary on basis of the given surface boundary
        return new PolygonImpl((SurfaceBoundaryImpl) boundary);
    }

    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.Factory#createPolygon(org.opengis.geometry.primitive.SurfaceBoundary,
     *      org.opengis.geometry.primitive.Surface)
     */
    public Polygon createPolygon(SurfaceBoundary boundary, Surface spanSurface)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // ok - but implementation in PolygonImpl is not ready yet
        return new PolygonImpl((SurfaceBoundaryImpl) boundary, (SurfaceImpl) spanSurface);
    }

    // COMMENTED OUT BECAUSE THIS METHOD IS DEPRECATED (Sanjay, 24/08/2006)
    /*
     * (non-Javadoc)
     *
     * @see org.opengis.geometry.coordinate.Factory#createSurfaceBoundary(org.opengis.geometry.primitive.Ring,
     *      java.util.List)
     */
    // public SurfaceBoundary createSurfaceBoundary(Ring exterior, List
    // interiors) throws MismatchedReferenceSystemException {
    // return null;
    // }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createMultiPrimitive()
     */
    public MultiPrimitive createMultiPrimitive() {
        // ok - this method will disappear from GeoAPI soon
        return new MultiPrimitiveImpl(crs, new HashSet());
    }

    // **************************************************************************
    // **************************************************************************
    // ***** INDIVIDUAL METHODS WHICH ARE NOT SPECIFIED BY GeoAPI           *****
    // **************************************************************************
    // **************************************************************************

    /** @return DirectPositionImpl */
    public DirectPosition createDirectPosition(DirectPosition dp) {
        return (DirectPosition) positionFactory.createDirectPosition(dp.getCoordinate());
    }

    /** @return Collection<PositionImpl> */
    public Collection<DirectPosition> createDirectPositions(
            Collection<double[]> coordList, Collection<DirectPosition> directPositions) {
        if (coordList == null || coordList.isEmpty())
            throw new IllegalArgumentException(""); // $NON-NLS-1$
        if (directPositions == null)
            directPositions = new ArrayList<DirectPosition>(coordList.size());
        for (double[] coords : coordList) {
            directPositions.add(positionFactory.createDirectPosition(coords));
        }
        return directPositions;
    }

    /**
     * Creates a Position with the given ordinates coord. The array coord must have at least two
     * elements
     *
     * @param coord is the array of ordinates of the position
     * @return PositionImpl
     */
    public PositionImpl createPosition(double[] coord) {
        // TODO Test
        if (coord.length != this.getDimension()) throw new MismatchedDimensionException();

        return (PositionImpl) createPosition(positionFactory.createDirectPosition(coord));
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createPosition(org.opengis.geometry.coordinate.DirectPosition)
     */
    public Position createPosition(DirectPosition dp) {
        // Test ok
        return new PositionImpl((DirectPosition) positionFactory.createPosition(dp));
    }

    /**
     * Creates a new PointArray
     *
     * @return PointArrayImpl
     */
    public PointArrayImpl createPointArray(List<Position> positions) {
        PointArrayImpl pa = null;
        try {
            pa = new PointArrayImpl(positions);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(
                    "List contains Position instances which can not be casted to the local geometry Position classes.",
                    e);
        }
        return pa;
    }

    /**
     * Creates an Envelope with the given coordinates.
     *
     * @param c Coordinate c of a point p. The created envelope will have this coordinate as lower
     *     and upper corner
     * @return EnvelopeImpl The created envelope will have the coordinate as lower and upper corner.
     */
    public EnvelopeImpl createEnvelope(double[] c) {
        if (c.length != this.getDimension()) throw new MismatchedDimensionException();

        return new EnvelopeImpl(positionFactory.createDirectPosition(c));
    }

    /**
     * Creates a new Envelope equal to the given envelope
     *
     * @return Envelope
     */
    public EnvelopeImpl createEnvelope(Envelope env) {
        return new EnvelopeImpl(env);
    }

    /** @return Collection<PositionImpl> */
    public List<Position> createPositions(
            Collection<double[]> coordList, List<Position> positions) {
        if (coordList == null || coordList.isEmpty())
            throw new IllegalArgumentException(""); // $NON-NLS-1$
        if (positions == null) positions = new ArrayList<Position>(coordList.size());
        for (double[] coords : coordList) {
            positions.add(createPosition(coords));
        }
        return positions;
    }

    /**
     * Creates a line segment between fromPosition and toPosition. The coordinate dimension of the
     * segment is fromPosition.length, fromPosition.length and toPoint.length must be equal and
     * greater then 1
     *
     * <p>This method creates a LineSegment without references to the parent Curve. this will cause
     * NullPointerExceptions in use of some methods
     *
     * @return LineSegmentImpl
     */
    public LineSegmentImpl createLineSegment(
            double[] fromPosition, double[] toPosition, double startPar) {

        if (fromPosition == null || toPosition == null)
            throw new IllegalArgumentException("Start or End parameter is null"); // $NON-NLS-1$;
        if (fromPosition.length != toPosition.length) throw new MismatchedDimensionException();

        return createLineSegment(
                this.createPosition(fromPosition), this.createPosition(toPosition), startPar);
    }

    /**
     * Creates a LineSegment with a given value as startParam
     *
     * @return a new LineSegmentImpl
     */
    public LineSegmentImpl createLineSegment(Position p0, Position p1, double startPar) {
        List<Position> positions = new ArrayList<Position>();
        positions.add(p0);
        positions.add(p1);
        return new LineSegmentImpl(createPointArray(positions), startPar);
    }

    /** @return LineStringImpl */
    public LineStringImpl createLineString(PointArrayImpl pointArray, double startPar) {
        if (pointArray == null || pointArray.isEmpty())
            throw new IllegalArgumentException(""); // $NON-NLS-1$
        return new LineStringImpl(pointArray, startPar);
    }

    /**
     * Creates LineString from Array of DirectPosition2D
     *
     * @return LineString
     */
    public LineStringImpl createLineString(List<Position> positions, double startPar) {
        return new LineStringImpl(createPointArray(positions), startPar);
    }

    /** @return Collection<LineStringImpl> */
    public Collection<LineStringImpl> createLineStrings(
            Collection<List<double[]>> coordLists, List<LineStringImpl> lineStrings) {
        if (lineStrings == null) lineStrings = new ArrayList<LineStringImpl>(coordLists.size());
        // GeometryFactoryImpl cf = this.geometryFactory.getGeometryFactoryImpl();
        double startPar = 0.0;
        for (List<double[]> coordList : coordLists) {
            // List<Position> positions = cf.createPositions(coordList, null);
            PointArray positions = positionFactory.createPointArray();
            for (double[] coords : coordList) {
                positions.add(createPosition(coords));
            }
            PointArrayImpl pai = createPointArray(positions);
            lineStrings.add(createLineString(pai, startPar));
            startPar += pai.getDistanceSum();
        }
        return lineStrings;
    }

    /* (non-Javadoc)
     * @see org.opengis.geometry.coordinate.Factory#createLineString(java.util.List)
     */
    public LineStringImpl createLineString(List<Position> positions)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        return this.createLineString(positions, 0.0);
    }

    /**
     * Creates a Triangle from three DirectPositions Builds the Surface Boundary for the Triangle
     *
     * @return Triangle
     */
    public TriangleImpl createTriangle(
            TriangulatedSurface ts, DirectPosition p1, DirectPosition p2, DirectPosition p3) {

        // PrimitiveFactoryImpl primFactory = this.geometryFactory.getPrimitiveFactory();

        // Create a SurfaceBoundary for the Triangle

        List<Position> positionList = new ArrayList<Position>();
        positionList.add(p1);
        positionList.add(p2);
        positionList.add(p3);
        positionList.add(p1);

        // Ring exterior = primFactory.createRingByDirectPositions(positionList);

        // Create List of CurveSegment´s (LineString´s)
        LineStringImpl lineString = new LineStringImpl(new PointArrayImpl(positionList), 0.0);
        List<CurveSegment> segments = new ArrayList<CurveSegment>();
        segments.add(lineString);
        OrientableCurve curve = new CurveImpl(crs, segments);
        List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
        orientableCurves.add(curve);

        Ring exterior = new RingImpl(orientableCurves);
        List<Ring> interiorList = new ArrayList<Ring>();

        SurfaceBoundaryImpl triangleBoundary = new SurfaceBoundaryImpl(crs, exterior, interiorList);

        return new TriangleImpl(
                triangleBoundary,
                ts,
                new PositionImpl(p1),
                new PositionImpl(p2),
                new PositionImpl(p3));
    }

    // TODO Jackson: geändert durch Sanjay; bitte korrektheit prüfen
    // TODO SJ: folgende aussage pruefen:
    // Parameter TriangulatedSurface wurde hinzufgefuegt: Ein Triangle sollte
    // nicht erzeugt werden, ohne eine Referenz auf das zugehoerige Surface zu
    // uebernehmen
    // Die moeglichkeit, triangles zu erzeugen und spaeter die referenz auf den
    // surface ueber einen setter zu setzen, sollten wir moeglichst vermeiden,
    // da es unuebersichtlich wird, wann alle notwendigen daten erzeugt sind
    // generell sollte es ziel sein, dass die Factories nur objekte erzeugen,
    // welche in sich
    /** @return ArrayList<Triangle> */
    public ArrayList<Triangle> createTriangles(ArrayList<double[][]> triangles) {

        ArrayList<Triangle> result = new ArrayList<Triangle>();
        for (double[][] triangle : triangles) {
            double[] coord0 = triangle[0];
            double[] coord1 = triangle[1];
            double[] coord2 = triangle[2];
            result.add(
                    this.createTriangle(
                            null,
                            positionFactory.createDirectPosition(coord0),
                            positionFactory.createDirectPosition(coord1),
                            positionFactory.createDirectPosition(coord2)));
        }
        return result;
    }

    public ArcString createArcString(List<Position> points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public ArcStringByBulge createArcStringByBulge(
            List<Position> points, double[] bulges, List<double[]> normals)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public BSplineCurve createBSplineCurve(
            int degree, PointArray points, List<Knot> knots, KnotType knotSpec)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public BSplineSurface createBSplineSurface(
            List<PointArray> points, int[] degree, List<Knot>[] knots, KnotType knotSpec)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public GeodesicString createGeodesicString(List<Position> points)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public PolyhedralSurface createPolyhedralSurface(List<Polygon> tiles)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public Tin createTin(
            Set<Position> post,
            Set<LineString> stopLines,
            Set<LineString> breakLines,
            double maxLength)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public Arc createArc(Position startPoint, Position midPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public Arc createArc(Position startPoint, Position endPoint, double bulge, double[] normal)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public ArcByBulge createArcByBulge(
            Position startPoint, Position endPoint, double bulge, double[] normal)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    public Geodesic createGeodesic(Position startPoint, Position endPoint)
            throws MismatchedReferenceSystemException, MismatchedDimensionException {
        // LATER:
        // TO DO semantic SJ, JR
        // TO DO implementation
        // TO DO test
        // TO DO documentation
        Assert.isTrue(false, "not implemented");
        return null;
    }

    //	/**
    //	 *
    //	 * @return ArrayList<LineSegmentImpl>
    //	 */
    //	public Collection<LineSegmentImpl> createLineSegments(
    //			Collection<double[]> p0, Collection<double[]> p1,
    //			Collection<Double> startPoints,
    //			Collection<LineSegmentImpl> lineSegments) {
    //		int s0 = p0.size();
    //		int s1 = p1.size();
    //		if (p0 == null || p1 == null || p0.isEmpty() || p1.isEmpty()
    //				|| s0 != s1)
    //			throw new IllegalArgumentException(""); //$NON-NLS-1$
    //		if (lineSegments == null)
    //			lineSegments = new ArrayList<LineSegmentImpl>();
    //		Iterator<double[]> it0 = p0.iterator();
    //		Iterator<double[]> it1 = p1.iterator();
    //		Iterator<Double> it2 = startPoints.iterator();
    //		while (it0.hasNext()) {
    //			lineSegments.add(createLineSegment(it0.next(), it1.next(), it2
    //					.next()));
    //		}
    //		return lineSegments;
    //	}
    //
    //	/**
    //	 * @return Collection<LineSegmentImpl>
    //	 */
    //	public List<LineSegmentImpl> createLineSegments(
    //			Collection<double[][]> points, List<LineSegmentImpl> lineSegments) {
    //		if (points == null || points.isEmpty())
    //			throw new IllegalArgumentException(""); //$NON-NLS-1$
    //		if (lineSegments == null) {
    //			// TO DO
    //			lineSegments = new ArrayList<LineSegmentImpl>(points.size());
    //		}
    //		Iterator<double[][]> it = points.iterator();
    //		while (it.hasNext()) {
    //			double[][] p = it.next();
    //			double[] p0 = p[0];
    //			double[] p1 = p[1];
    //			double startPar = p[2][0];
    //			lineSegments.add(createLineSegment(p0, p1, startPar));
    //		}
    //		return lineSegments;
    //	}

    // public Tin createTin(GeoAdvancingFront2D mesh) {
    //
    // Hashtable<GeoPoint2D,Position> ht = new
    // Hashtable<GeoPoint2D,Position>(mesh.getPosts().size());
    // ArrayList<Position> controlPoint = new
    // ArrayList<Position>(mesh.getPosts().size());
    // for (GeoPoint2D p : mesh.getPosts()) {
    // Position pos = Factory.createPosition(p.x,p.y);
    // ht.put(p,pos);
    // controlPoint.add(pos);
    // }
    // ArrayList<Triangle> triangles = new
    // ArrayList<Triangle>(mesh.getTriangles().size());
    // for (GeoTriangle2D tri : mesh.getTriangles()) {
    // GeoPoint2D[] pts = tri.getPoints();
    // triangles.add(Factory.createTriangle(ht.get(pts[0]),ht.get(pts[1]),ht.get(pts[2])));
    // }
    //
    // ArrayList<LineString> stopLines = new
    // ArrayList<LineString>(mesh.getStopLines().size());
    // for ( GeoLine2D line : mesh.getStopLines()) {
    // GeoPoint2D p0 = line.getStartPoint();
    // GeoPoint2D p1 = line.getEndPoint();
    // stopLines.add(Factory.createLineSegment(ht.get(p0),ht.get(p1)));
    // }
    //
    // ArrayList<LineString> breakLines = new
    // ArrayList<LineString>(mesh.getBreakLines().size());
    // for ( GeoLine2D line : mesh.getBreakLines()) {
    // GeoPoint2D p0 = line.getStartPoint();
    // GeoPoint2D p1 = line.getEndPoint();
    // breakLines.add(Factory.createLineSegment(ht.get(p0),ht.get(p1)));
    // }
    //
    // SurfaceBoundary surfBdry =
    // Factory.createSurfaceBoundary(mesh.getSurface().getBoundariesPoints());
    //
    // return new
    // Tin(surfBdry,controlPoint,stopLines,breakLines,mesh.getMaxLength(),triangles);
    // }

}
