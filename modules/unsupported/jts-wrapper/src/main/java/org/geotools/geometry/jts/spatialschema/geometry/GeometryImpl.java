/*******************************************************************************
 * $Id$
 * $Source: /cvs/ctree/LiteGO1/src/jar/com/polexis/lite/spatialschema/geometry/GeometryImpl.java,v $
 * Copyright (C) 2003 Open GIS Consortium, Inc. All Rights Reserved.
 * http://www.opengis.org/Legal/
 ******************************************************************************/
package org.geotools.geometry.jts.spatialschema.geometry;

import java.io.Serializable;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;
import org.opengis.geometry.Boundary;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.MismatchedDimensionException;
import org.opengis.geometry.Precision;
import org.opengis.geometry.TransfiniteSet;
import org.opengis.geometry.complex.Complex;
import org.opengis.geometry.primitive.Ring;
import org.opengis.util.Cloneable;

import org.geotools.geometry.jts.spatialschema.geometry.primitive.CurveBoundaryImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.PointImpl;
import org.geotools.geometry.jts.spatialschema.geometry.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.jts.JTSGeometry;
import org.geotools.geometry.jts.JTSUtils;

//geotools dependencies
import org.geotools.factory.BasicFactories;

/**
 * Base class for our JTS-based implementation of the various ISO 19107 geometry
 * classes.
 *
 * @source $URL$
 */
public abstract class GeometryImpl 
	implements Geometry, Serializable, Cloneable, JTSGeometry {

    //*************************************************************************
    //  Fields
    //*************************************************************************

    /**
     * True if we're allowing changes to the geometry.  False if not.
     */
    private boolean mutable;

    /**
     * CRS for this geometry.
     */
    private CoordinateReferenceSystem coordinateReferenceSystem;

    /**
     * The JTS equivalent of this geometry.  This gets set to null whenever we
     * make changes to the geometry so that we can recompute it.
     */
    private com.vividsolutions.jts.geom.Geometry jtsPeer;

    /**
     * If this object is part of a composite, this this member should hold a
     * pointer to that composite so that when our JTS geometry is invalidated,
     * we can also invalidate that of our parent.
     */
    private JTSGeometry parent;

    
    /**
     * Precision model
     */
    private Precision precision;
    
    //*************************************************************************
    //  Constructors
    //*************************************************************************

    /**
     * Creates a new mutable {@code GeometryImpl} with a null CRS.
     */
    public GeometryImpl() {
        this(null);
    }

    /**
     * Creates a new mutable {@code GeometryImpl}.
     * @param coordinateReferenceSystem CRS for this geometry's vertices.
     */
    public GeometryImpl(final CoordinateReferenceSystem coordinateReferenceSystem) {
        this(coordinateReferenceSystem, true);
    }

    /**
     * Creates a new {@code GeometryImpl}.
     *
     * @param coordinateReferenceSystem CRS for this geometry's vertices.
     * @param mutable Whether or not changes will be allowed.
     */
    public GeometryImpl(final CoordinateReferenceSystem coordinateReferenceSystem, boolean mutable) {
        this.coordinateReferenceSystem = coordinateReferenceSystem;
        this.mutable = mutable;
    }

    public void setParent(JTSGeometry parent) {
        this.parent = parent;
    }
    
    public Precision getPrecision() {
        return precision;
    }

    /**
     * Subclasses must override this method to compute the JTS equivalent of
     * this geometry.
     */
    protected abstract com.vividsolutions.jts.geom.Geometry computeJTSPeer();

    /**
     * This method must be called by subclasses whenever the user makes a change
     * to the geometry so that the cached JTS object can be recreated.
     */
    public final void invalidateCachedJTSPeer() {
        jtsPeer = null;
        if (parent != null) parent.invalidateCachedJTSPeer();
    }

    /**
     * This method is meant to be invoked by the JTSUtils utility class when it
     * creates a Geometry from a JTS geometry.  This prevents the Geometry from
     * having to recompute the JTS peer the first time.
     */
    protected final void setJTSPeer(com.vividsolutions.jts.geom.Geometry g) {
        jtsPeer = g;
    }

    /**
     * Returns the JTS version of this geometry.  If the geometry has not
     * changed since the last time this method was called, it will return the
     * exact same object.
     */
    public final com.vividsolutions.jts.geom.Geometry getJTSGeometry() {
        if (jtsPeer == null) {
            jtsPeer = computeJTSPeer();
        }
        return jtsPeer;
    }

    //*************************************************************************
    //  implement the Geometry interface
    //*************************************************************************

    /**
     * Returns the CRS that was given to the constructor.
     */
    public final CoordinateReferenceSystem getCoordinateReferenceSystem() {
        return coordinateReferenceSystem;
    }

    /**
     * Returns a Geometry that represents the minimum bounding region of this
     * geometry.
     */
    public final Geometry getMbRegion() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        return JTSUtils.jtsToGo1(jtsGeom.getEnvelope(), getCoordinateReferenceSystem());
    }

    /**
     * Returns a point interior to the geometry.
     */
    public final DirectPosition getRepresentativePoint() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Point p = jtsGeom.getInteriorPoint();
        return JTSUtils.pointToDirectPosition(p, getCoordinateReferenceSystem());
    }

    /**
     * Returns the boundary of this geometry.  Returns null if the boundary is
     * empty.
     */
    public Boundary getBoundary() {
        // PENDING(CSD):
        // Need to find out if MultiPrimitives are handled correctly.  (I think
        // they are, but 19107's boundary semantics for multi-primitives are
        // not well-specified.)
        // Need to find out if GeometryCollections are handled correctly.  (I
        // don't think they are, but it's not clear what it would mean, nor is
        // it obvious why anyone would call it in the first place.)

        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();

        // PENDING(CSD):
        // As far as I could tell, it's not defined what it would mean to
        // compute the boundary of a collection object in 19107.
        if (jtsGeom instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            throw new UnsupportedOperationException(
                    "Boundary cannot be computed for multi-primitives.");
        }

        com.vividsolutions.jts.geom.Geometry jtsBoundary = jtsGeom.getBoundary();
        int d = jtsGeom.getDimension();
        if (d == 0) {
            // If d is zero, then our geometry is a point.  So the boundary is
            // empty.  ISO 19107 defines the boundary of a point to 
	    // be NULL.
            return null;
        }
        else if (d == 1) {
            // If d is 1, then the boundary is either empty (if it's a ring) or
            // it's two points at either end of the curve.
            // We've ruled out the possibility of multi-primitives (see the
            // instanceof check above), so we know that the boundary can't be
            // more than 2 points.

            com.vividsolutions.jts.geom.Coordinate [] coords = jtsBoundary.getCoordinates();
            // If coords is emtpy, then this geometry is a ring.  So we return
            // an empty CurveBoundary object (i.e. one with both points set to
            // null).
            if ((coords == null) || (coords.length == 0)) {
                CurveBoundaryImpl result = new CurveBoundaryImpl(
                        getCoordinateReferenceSystem(), null, null);
                return result;
            }
            else {
                // If it wasn't empty, then return a CurveBoundary with the two
                // endpoints.
                if (coords.length != 2) {
                    // Should this be an assert instead?
                    throw new RuntimeException("ERROR: One dimensional " +
                            "primitive had wrong number of boundary points (" +
                            coords.length + ")");
                }
                CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
                CurveBoundaryImpl result = new CurveBoundaryImpl(crs,
                        new PointImpl(JTSUtils.coordinateToDirectPosition(
                                coords[0], crs)),
                        new PointImpl(JTSUtils.coordinateToDirectPosition(
                                coords[1], crs)));
                return result;
            }
        }
        else if (d == 2) {
            // If d == 2, then the boundary is a collection of rings.
            // In particular, the JTS tests indicate that it'll be a
            // MultiLineString.
            com.vividsolutions.jts.geom.MultiLineString mls =
                (com.vividsolutions.jts.geom.MultiLineString) jtsBoundary;
            int n = mls.getNumGeometries();
            CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
            Ring exteriorRing = JTSUtils.linearRingToRing(
                (com.vividsolutions.jts.geom.LineString) mls.getGeometryN(0),
                crs);
            Ring [] interiorRings = new Ring[n-1];
            for (int i=1; i<n; i++) {
                interiorRings[n-1] = JTSUtils.linearRingToRing(
                    (com.vividsolutions.jts.geom.LineString)
                    	mls.getGeometryN(i),
                    crs);
            }
            SurfaceBoundaryImpl result = new SurfaceBoundaryImpl(crs,
                exteriorRing, interiorRings);
            return result;
        }
        else {
            throw new UnsupportedOperationException("Computing the boundary " +
                    "for geometries of dimension larger than 2 is not " +
                    "supported.");
        }
    }

    /**
     * This method is not implemented.  Always throws an
     * UnsupportedOperationException.
     */
    public final Complex getClosure() {
        throw new UnsupportedOperationException("Closure not supported");
    }

    /**
     * Returns true if this object does not cross itself.
     */
    public final boolean isSimple() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        return jtsGeom.isSimple();
    }

    public final boolean isCycle() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsBoundary = jtsGeom.getBoundary();
        return jtsBoundary.isEmpty();
    }

    /**
     * Returns the distance between the given geometry and this geometry.  Note
     * that this distance is in units the same as the units of the coordinate
     * reference system, and thus may not have any physical meaning (such as
     * when the coordinate system is a latitude/longitude system).
     */
    public final double getDistance(final Geometry geometry) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) geometry).getJTSGeometry();
        return JTSUtils.distance(jtsGeom1, jtsGeom2);
    }

    /**
     * Returns the manifold dimension of the geometry at the given point.  The
     * point must lie on the geometry.
     *
     * For geometries that consist of multiple parts, this returns the dimension
     * of the part intersecting the given point.  When multiple parts coincide
     * at the given point, this returns the least dimension of those geometries.
     * Returns Integer.MAX_VALUE if the given point is not on this geometry.
     */
    public final int getDimension(final DirectPosition point) {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        if (jtsGeom instanceof com.vividsolutions.jts.geom.GeometryCollection) {
            com.vividsolutions.jts.geom.Point p =
                JTSUtils.directPositionToPoint(point);
            return getDimension(p, (com.vividsolutions.jts.geom.GeometryCollection) jtsGeom);
        }
        else {
            return jtsGeom.getDimension();
        }
    }

    private static final int getDimension(
            final com.vividsolutions.jts.geom.Point p,
            final com.vividsolutions.jts.geom.GeometryCollection gc) {
        int min = Integer.MAX_VALUE;
        int n = gc.getNumGeometries();
        for (int i=0; i<n; i++) {
            int d = Integer.MAX_VALUE;
            com.vividsolutions.jts.geom.Geometry g = gc.getGeometryN(i);
            if (g instanceof com.vividsolutions.jts.geom.GeometryCollection) {
                // If it was a nested GeometryCollection, then just recurse
                // until we get down to non-collections.
                d = getDimension(p, (com.vividsolutions.jts.geom.GeometryCollection) g);
            }
            else {
                if (g.intersects(p))
                    d = g.getDimension();
            }
            if (d < min)
                min = d;
        }
        return min;
    }

    /**
     * Returns the dimension of the coordinates in this geometry.  This
     * delegates to the coordinate reference system, so it may throw a null
     * pointer exception if this geometry has no coordinate reference system.
     */
    public final int getCoordinateDimension() {
        return getCoordinateReferenceSystem().getCoordinateSystem().getDimension();
    }

    /**
     * This impementation of geometry does not support traversing this
     * association in this direction as it would require every geometry to know
     * about all of the larger geometries of which it is a part.  This would
     * add some memory usage and bookkeeping headaches for functionality that
     * will rarely, if ever, be used.  This this method always returns null.
     */
    public final Set getMaximalComplex() {
        return null;
    }

    /**
     * Attempts to find a transform from the current CRS to the new CRS and
     * creates a new geometry by invoking that transform on each control point
     * of this geometry.
     */
    public final Geometry transform(final CoordinateReferenceSystem newCRS) throws TransformException {
        try {
            BasicFactories commonFactory = BasicFactories.getDefault(); 
            CoordinateOperationFactory cof = commonFactory.getCoordinateOperationFactory();
            CoordinateReferenceSystem oldCRS = getCoordinateReferenceSystem();
            CoordinateOperation coordOp = cof.createOperation(oldCRS, newCRS);
            MathTransform mt = coordOp.getMathTransform();
            return transform(newCRS, mt);
        }
        catch (OperationNotFoundException e) {
            throw new TransformException("Unable to find an operation", e);
        }
        catch (FactoryException e) {
            throw new TransformException("Factory exception", e);
        }
    }

    /**
     * Creates a new Geometry out of this one by invoking the given transform
     * on each control point of this geometry.
     */
    public final Geometry transform(final CoordinateReferenceSystem newCRS, 
            final MathTransform transform) throws TransformException {
        // Get the JTS geometry
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        // Make a copy since we're going to modify its values
        jtsGeom = (com.vividsolutions.jts.geom.Geometry) jtsGeom.clone();
        // Get a local variable that has the src CRS
        CoordinateReferenceSystem oldCRS = getCoordinateReferenceSystem();
        // Do the actual work of transforming the vertices
        jtsGeom.apply(new MathTransformFilter(transform, oldCRS, newCRS));
        // Then convert back to a GO1 geometry
        return JTSUtils.jtsToGo1(jtsGeom, getCoordinateReferenceSystem());
    }

    /**
     * @inheritDoc
     * @see org.opengis.geometry.coordinate.#getEnvelope()
     */
    public final Envelope getEnvelope() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Envelope jtsEnv = jtsGeom.getEnvelopeInternal();
        CoordinateReferenceSystem crs = getCoordinateReferenceSystem();
        Envelope result = new EnvelopeImpl(
                new DirectPositionImpl(crs,
                        new double [] { jtsEnv.getMinX(), jtsEnv.getMinY() }),
                new DirectPositionImpl(crs,
                        new double [] { jtsEnv.getMaxX(), jtsEnv.getMaxY() })
        );
        return result;
    }

    /**
     * Returns the centroid of this geometry.
     */
    public final DirectPosition getCentroid() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Point jtsCentroid = jtsGeom.getCentroid();
        return JTSUtils.pointToDirectPosition(jtsCentroid,
                getCoordinateReferenceSystem());
    }

    /**
     * Returns the geometric convex hull of this geometry.
     */
    public final Geometry getConvexHull() {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsHull = jtsGeom.convexHull();
        return JTSUtils.jtsToGo1(jtsHull, getCoordinateReferenceSystem());
    }

    /**
     * Returns an approximate buffer around this object.
     */
    public final Geometry getBuffer(final double distance) {
        com.vividsolutions.jts.geom.Geometry jtsGeom = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsBuffer = jtsGeom.buffer(distance);
        return JTSUtils.jtsToGo1(jtsBuffer, getCoordinateReferenceSystem());
    }

    /**
     * Returns true if this geometry can be changed.
     */
    public final boolean isMutable() {
        return mutable;
    }

    /**
     * Creates an immutable copy of this object or just returns this object if
     * it's already immutable.
     */
    public final Geometry toImmutable() {
        if (isMutable()) {
	        GeometryImpl result = (GeometryImpl) clone();
	        result.mutable = false;
	        return result;
        }
        else {
            return this;
        }
    }

    /**
     * Returns a deep copy of this geometric object.  Subclasses must override
     * to make deep copies of members that are themselves mutable objects.  Note
     * that all of the (private) members of GeometryImpl are already immutable
     * so this method simply delegates to the superclass (Object) clone.
     */
    public GeometryImpl clone() {
        try {
            return (GeometryImpl) super.clone();
        }
        catch (CloneNotSupportedException cnse) {
            throw new AssertionError(cnse);
        }
    }

    /**
     * Returns true if the given position lies in this geometry within the
     * tolerance of the floating point representation.
     */
    public boolean contains(DirectPosition point) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            JTSUtils.directPositionToPoint(point);
        return JTSUtils.contains(jtsGeom1, jtsGeom2);
    }

    /**
     * Returns true if this geometry completely contains the given geometry.
     */
    public boolean contains(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.contains(jtsGeom1, jtsGeom2);
    }

    public double distance( Geometry otherGeometry ) {
        return getDistance( otherGeometry );
    }
    
    public TransfiniteSet difference(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.jtsToGo1(JTSUtils.difference(jtsGeom1, jtsGeom2),
                getCoordinateReferenceSystem());
    }

    public boolean equals(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.equals(jtsGeom1, jtsGeom2);
    }

    public TransfiniteSet intersection(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.jtsToGo1(JTSUtils.intersection(jtsGeom1, jtsGeom2),
                getCoordinateReferenceSystem());
    }

    public boolean intersects(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.intersects(jtsGeom1, jtsGeom2);
    }

    public TransfiniteSet symmetricDifference(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.jtsToGo1(JTSUtils.symmetricDifference(jtsGeom1, jtsGeom2),
                getCoordinateReferenceSystem());
    }

    public TransfiniteSet union(TransfiniteSet pointSet) {
        com.vividsolutions.jts.geom.Geometry jtsGeom1 = getJTSGeometry();
        com.vividsolutions.jts.geom.Geometry jtsGeom2 =
            ((JTSGeometry) pointSet).getJTSGeometry();
        return JTSUtils.jtsToGo1(JTSUtils.union(jtsGeom1, jtsGeom2),
                getCoordinateReferenceSystem());
    }

    public static Set listAsSet(final List list) {
        return new Set() {
            public int size() {
                return list.size();
            }

            public void clear() {
                list.clear();
            }

            public boolean isEmpty() {
                return list.isEmpty();
            }

            public Object [] toArray() {
                return list.toArray();
            }

            public boolean add(Object o) {
                return list.add(o);
            }

            public boolean contains(Object o) {
                return list.contains(o);
            }

            public boolean remove(Object o) {
                return list.remove(o);
            }

            public boolean addAll(Collection c) {
                return list.addAll(c);
            }

            public boolean containsAll(Collection c) {
                return list.containsAll(c);
            }

            public boolean removeAll(Collection c) {
                return list.removeAll(c);
            }

            public boolean retainAll(Collection c) {
                return list.retainAll(c);
            }

            public Iterator iterator() {
                return list.iterator();
            }

            public Object [] toArray(Object [] a) {
                return list.toArray(a);
            }
        };
    }

    /**
     * This class implements JTS's CoordinateFilter interface using a GeoAPI
     * MathTransform object to actually perform the work.
     */
    public static class MathTransformFilter implements com.vividsolutions.jts.geom.CoordinateFilter {
        private MathTransform transform;
        private DirectPosition src;
        private DirectPosition dst;

        public MathTransformFilter(MathTransform transform,
                CoordinateReferenceSystem oldCRS,
                CoordinateReferenceSystem newCRS) {
            this.transform = transform;
            src = new DirectPositionImpl(oldCRS);
            dst = new DirectPositionImpl(newCRS);
        }

        public void filter(com.vividsolutions.jts.geom.Coordinate coord) {
            // Load the input into a DirectPosition
            JTSUtils.coordinateToDirectPosition(coord, src);
            try {
                // Do the transform math.
                transform.transform(src, dst);
            }
            catch (MismatchedDimensionException e) {
                throw new RuntimeException(e);
            }
            catch (TransformException e) {
                throw new RuntimeException(e);
            }
            // Load the result back into the Coordinate.
            JTSUtils.directPositionToCoordinate(dst, coord);
        }
    }
}
