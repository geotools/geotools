/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2006  Vivid Solutions
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
package org.geotools.geometry.iso.util.algorithm2D;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;
import org.geotools.geometry.iso.aggregate.MultiCurveImpl;
import org.geotools.geometry.iso.aggregate.MultiPointImpl;
import org.geotools.geometry.iso.aggregate.MultiPrimitiveImpl;
import org.geotools.geometry.iso.aggregate.MultiSurfaceImpl;
import org.geotools.geometry.iso.complex.CompositeCurveImpl;
import org.geotools.geometry.iso.complex.CompositePointImpl;
import org.geotools.geometry.iso.complex.CompositeSurfaceImpl;
import org.geotools.geometry.iso.coordinate.DirectPositionImpl;
import org.geotools.geometry.iso.coordinate.LineStringImpl;
import org.geotools.geometry.iso.coordinate.PointArrayImpl;
import org.geotools.geometry.iso.primitive.CurveBoundaryImpl;
import org.geotools.geometry.iso.primitive.CurveImpl;
import org.geotools.geometry.iso.primitive.PointImpl;
import org.geotools.geometry.iso.primitive.PrimitiveImpl;
import org.geotools.geometry.iso.primitive.RingImpl;
import org.geotools.geometry.iso.primitive.RingImplUnsafe;
import org.geotools.geometry.iso.primitive.SurfaceBoundaryImpl;
import org.geotools.geometry.iso.primitive.SurfaceImpl;
import org.geotools.geometry.iso.root.GeometryImpl;
import org.geotools.geometry.iso.topograph2D.Coordinate;
import org.geotools.geometry.iso.topograph2D.CoordinateList;
import org.geotools.geometry.iso.topograph2D.util.CoordinateArrays;
import org.geotools.geometry.iso.topograph2D.util.UniqueCoordinateArrayFilter;
import org.geotools.geometry.iso.util.Assert;
import org.geotools.util.SuppressFBWarnings;
import org.opengis.geometry.Geometry;
import org.opengis.geometry.coordinate.Position;
import org.opengis.geometry.primitive.CurveSegment;
import org.opengis.geometry.primitive.OrientableCurve;
import org.opengis.geometry.primitive.OrientableSurface;
import org.opengis.geometry.primitive.Primitive;
import org.opengis.geometry.primitive.Ring;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Computes the convex hull of a {@link Geometry}. The convex hull is the smallest convex Geometry
 * that contains all the points in the input Geometry.
 *
 * <p>Uses the Graham Scan algorithm. Asymptotic running time: O(n*log(n))
 */
public class ConvexHull {
    // private FeatGeomFactoryImpl geomFactory;
    private CoordinateReferenceSystem crs;

    private Coordinate[] inputPts;

    /** Create a new convex hull construction for the input {@link Geometry}. */
    public ConvexHull(GeometryImpl geometry) {
        this(extractCoordinates(geometry), geometry.getCoordinateReferenceSystem());
    }

    /** Create a new convex hull construction for the input {@link Coordinate} array. */
    public ConvexHull(Coordinate[] pts, CoordinateReferenceSystem crs) {
        inputPts = pts;
        this.crs = crs;
    }

    /** Get coordinates from a geometry and eliminate positions with equal coordinates */
    // positions might actually go NPE, just ignoring it as the module is generally broken
    @SuppressFBWarnings("NP_NULL_ON_SOME_PATH")
    private static Coordinate[] extractCoordinates(GeometryImpl geom) {
        // Get relevant coordinates from the geometry instance

        Collection positions = null;

        if (geom instanceof PointImpl) {
            // Add point
            positions = new ArrayList<DirectPositionImpl>();
            positions.add(((PointImpl) geom).getDirectPosition());
        } else if (geom instanceof CurveImpl) {
            // Add control points
            positions = ((CurveImpl) geom).asDirectPositions();
        } else if (geom instanceof RingImpl) {
            // Add control points
            positions = ((RingImplUnsafe) geom).asDirectPositions();
        } else if (geom instanceof SurfaceImpl) {
            // Add control points of exterior ring of boundary
            positions =
                    ((RingImplUnsafe) ((SurfaceImpl) geom).getBoundary().getExterior())
                            .asDirectPositions();
        } else if (geom instanceof MultiPointImpl) {
            // Add all points of the set
            positions = ((MultiPointImpl) geom).getElements();
        } else if (geom instanceof MultiCurveImpl) {
            // Add all curves of the set
            positions = new HashSet<DirectPositionImpl>();
            Iterator<OrientableCurve> curveIter = ((MultiCurveImpl) geom).getElements().iterator();
            while (curveIter.hasNext()) {
                positions.addAll(((CurveImpl) curveIter.next()).asDirectPositions());
            }
        } else if (geom instanceof MultiSurfaceImpl) {
            // Add all exterior rings of the surfaceboundaries of the surfaces in the set
            positions = new HashSet<DirectPositionImpl>();
            Iterator<OrientableSurface> surfaceIter =
                    ((MultiSurfaceImpl) geom).getElements().iterator();
            while (surfaceIter.hasNext()) {
                positions.addAll(
                        ((RingImplUnsafe)
                                        ((SurfaceImpl) surfaceIter.next())
                                                .getBoundary()
                                                .getExterior())
                                .asDirectPositions());
            }
        } else if (geom instanceof MultiPrimitiveImpl) {
            positions = new HashSet<PointImpl>();
            Iterator<? extends Primitive> iterator =
                    ((MultiPrimitiveImpl) geom).getElements().iterator();
            while (iterator.hasNext()) {
                PrimitiveImpl prim = ((PrimitiveImpl) iterator.next());

                // if this is a point, it has no boundary so just add the point
                if (prim instanceof PointImpl) {
                    positions.add(prim);
                } else {
                    // this is not a point, so get its convexhull and add those
                    // points to this hull
                    Geometry hull = prim.getConvexHull();

                    if (hull instanceof CurveImpl) {
                        CurveImpl curve = (CurveImpl) prim.getConvexHull();
                        positions.addAll(((CurveImpl) curve).asDirectPositions());
                    } else if (hull instanceof SurfaceImpl) {
                        SurfaceImpl surface = (SurfaceImpl) prim.getConvexHull();
                        positions.addAll(
                                ((RingImplUnsafe)
                                                ((SurfaceImpl) surface).getBoundary().getExterior())
                                        .asDirectPositions());
                    }

                    /*
                    // this is not a point, so get its boundary and add its
                    // positions to the list.
                    PrimitiveBoundaryImpl pb = (PrimitiveBoundaryImpl) (prim.getBoundary());
                     if (pb instanceof CurveBoundaryImpl) {
                    	 CurveBoundaryImpl boundary = (CurveBoundaryImpl) pb;
                    	 positions.add(boundary.getStartPoint());
                    	 positions.add(boundary.getEndPoint());
                     }
                     else if (pb instanceof SolidBoundaryImpl) {
                    	 SolidBoundaryImpl boundary = (SolidBoundaryImpl) pb;
                    	 positions.addAll(boundary.getExterior().getElements());
                     }
                     else if (pb instanceof SurfaceBoundaryImpl) {
                    	 SurfaceBoundaryImpl boundary = (SurfaceBoundaryImpl) pb;
                    	 positions.addAll(boundary.getExterior().asDirectPositions());
                     }
                     */
                }
            }

            // Assert.isTrue(false, "not implemented yet");
        } else if (geom instanceof CompositePointImpl) {
            positions = new ArrayList<DirectPositionImpl>();
            positions.add(((CompositePointImpl) geom).getElements().iterator().next());
        } else if (geom instanceof CompositeCurveImpl) {
            Assert.isTrue(false, "not implemented yet");
        } else if (geom instanceof CompositeSurfaceImpl) {
            Assert.isTrue(false, "not implemented yet");
        } else if (geom instanceof CurveBoundaryImpl) {
            positions = new ArrayList<DirectPositionImpl>();
            positions.add(((CurveBoundaryImpl) geom).getStartPoint());
            positions.add(((CurveBoundaryImpl) geom).getEndPoint());
        } else if (geom instanceof SurfaceBoundaryImpl) {
            // Add control points of exterior ring
            positions =
                    ((RingImplUnsafe) ((SurfaceBoundaryImpl) geom).getExterior())
                            .asDirectPositions();
        }

        UniqueCoordinateArrayFilter filter = new UniqueCoordinateArrayFilter();

        // Filter all coordinates to eleminate redudant coordinates
        Iterator posIter = positions.iterator();
        while (posIter.hasNext()) {
            Object pos = posIter.next();
            if (pos instanceof DirectPositionImpl) {
                filter.filter(new Coordinate(((DirectPositionImpl) pos).getCoordinate()));
            } else if (pos instanceof PointImpl) {
                filter.filter(
                        new Coordinate(((PointImpl) pos).getDirectPosition().getCoordinate()));
            } else Assert.isTrue(false, "Invalid coordinate type");
        }

        return filter.getCoordinates();
    }

    /**
     * Returns a {@link Geometry} that represents the convex hull of the input geometry. The
     * returned geometry contains the minimal number of points needed to represent the convex hull.
     * In particular, no more than two consecutive points will be collinear.
     *
     * @return if the convex hull contains 3 or more points, a {@link Polygon}; 2 points, a {@link
     *     LineString}; 1 point, a {@link Point}; 0 points, an empty {@link GeometryCollection}.
     */
    public Geometry getConvexHull() {

        if (inputPts.length == 0) {
            // return geomFactory.createGeometryCollection(null);
            // if no points, return null
            return null;
        }
        if (inputPts.length == 1) {
            // 1 point: return Point
            return new PointImpl(
                    new DirectPositionImpl(
                            crs,
                            inputPts[0]
                                    .getCoordinates())); // this.geomFactory.getPrimitiveFactory().createPoint(inputPts[0].getCoordinate());
        }
        if (inputPts.length == 2) {
            List<Position> positions = CoordinateArrays.toPositionList(this.crs, this.inputPts);
            LineStringImpl lineString = new LineStringImpl(new PointArrayImpl(positions), 0.0);
            List<CurveSegment> segments = new ArrayList<CurveSegment>();
            segments.add(lineString);
            return new CurveImpl(this.crs, segments);
            // return this.geomFactory.getPrimitiveFactory().createCurveByPositions((List<Position>)
            // positions);
        }

        Coordinate[] reducedPts = inputPts;
        // use heuristic to reduce points, if large
        if (inputPts.length > 50) {
            reducedPts = reduce(inputPts);
        }
        // sort points for Graham scan.
        Coordinate[] sortedPts = preSort(reducedPts);

        // Use Graham scan to find convex hull.
        Stack cHS = grahamScan(sortedPts);

        // Convert stack to an array.
        Coordinate[] cH = toCoordinateArray(cHS);

        // Convert array to appropriate output geometry.
        return lineOrPolygon(cH);
    }

    /** An alternative to Stack.toArray, which is not present in earlier versions of Java. */
    protected Coordinate[] toCoordinateArray(Stack stack) {
        Coordinate[] coordinates = new Coordinate[stack.size()];
        for (int i = 0; i < stack.size(); i++) {
            Coordinate coordinate = (Coordinate) stack.get(i);
            coordinates[i] = coordinate;
        }
        return coordinates;
    }

    /**
     * Uses a heuristic to reduce the number of points scanned to compute the hull. The heuristic is
     * to find a polygon guaranteed to be in (or on) the hull, and eliminate all points inside it. A
     * quadrilateral defined by the extremal points in the four orthogonal directions can be used,
     * but even more inclusive is to use an octilateral defined by the points in the 8 cardinal
     * directions.
     *
     * <p>Note that even if the method used to determine the polygon vertices is not 100% robust,
     * this does not affect the robustness of the convex hull.
     */
    private Coordinate[] reduce(Coordinate[] inputPts) {
        // Coordinate[] polyPts = computeQuad(inputPts);
        Coordinate[] polyPts = computeOctRing(inputPts);
        // Coordinate[] polyPts = null;

        // unable to compute interior polygon for some reason
        if (polyPts == null) return inputPts;

        // LinearRing ring = geomFactory.createLinearRing(polyPts);
        // System.out.println(ring);

        // add points defining polygon
        HashSet reducedSet = new HashSet();
        for (int i = 0; i < polyPts.length; i++) {
            reducedSet.add(polyPts[i]);
        }
        /**
         * Add all unique points not in the interior poly. CGAlgorithms.isPointInRing is not defined
         * for points actually on the ring, but this doesn't matter since the points of the interior
         * polygon are forced to be in the reduced set.
         */
        for (int i = 0; i < inputPts.length; i++) {
            if (!CGAlgorithms.isPointInRing(inputPts[i], polyPts)) {
                reducedSet.add(inputPts[i]);
            }
        }
        Coordinate[] reducedPts = CoordinateArrays.toCoordinateArray(reducedSet);
        return reducedPts;
    }

    private Coordinate[] preSort(Coordinate[] pts) {
        Coordinate t;

        // find the lowest point in the set. If two or more points have
        // the same minimum y coordinate choose the one with the minimu x.
        // This focal point is put in array location pts[0].
        for (int i = 1; i < pts.length; i++) {
            if ((pts[i].y < pts[0].y) || ((pts[i].y == pts[0].y) && (pts[i].x < pts[0].x))) {
                t = pts[0];
                pts[0] = pts[i];
                pts[i] = t;
            }
        }

        // sort the points radially around the focal point.
        Arrays.sort(pts, 1, pts.length, new RadialComparator(pts[0]));

        // radialSort(pts);
        return pts;
    }

    private Stack grahamScan(Coordinate[] c) {
        Coordinate p;
        Stack ps = new Stack();
        p = (Coordinate) ps.push(c[0]);
        p = (Coordinate) ps.push(c[1]);
        p = (Coordinate) ps.push(c[2]);
        for (int i = 3; i < c.length; i++) {
            p = (Coordinate) ps.pop();
            while (CGAlgorithms.computeOrientation((Coordinate) ps.peek(), p, c[i]) > 0) {
                p = (Coordinate) ps.pop();
            }
            p = (Coordinate) ps.push(p);
            p = (Coordinate) ps.push(c[i]);
        }
        p = (Coordinate) ps.push(c[0]);
        return ps;
    }

    /**
     * @return whether the three coordinates are collinear and c2 lies between c1 and c3 inclusive
     */
    private boolean isBetween(Coordinate c1, Coordinate c2, Coordinate c3) {
        if (CGAlgorithms.computeOrientation(c1, c2, c3) != 0) {
            return false;
        }
        if (c1.x != c3.x) {
            if (c1.x <= c2.x && c2.x <= c3.x) {
                return true;
            }
            if (c3.x <= c2.x && c2.x <= c1.x) {
                return true;
            }
        }
        if (c1.y != c3.y) {
            if (c1.y <= c2.y && c2.y <= c3.y) {
                return true;
            }
            if (c3.y <= c2.y && c2.y <= c1.y) {
                return true;
            }
        }
        return false;
    }

    private Coordinate[] computeOctRing(Coordinate[] inputPts) {
        Coordinate[] octPts = computeOctPts(inputPts);
        CoordinateList coordList = new CoordinateList();
        coordList.add(octPts, false);

        // points must all lie in a line
        if (coordList.size() < 3) {
            return null;
        }
        coordList.closeRing();
        return coordList.toCoordinateArray();
    }

    private Coordinate[] computeOctPts(Coordinate[] inputPts) {
        Coordinate[] pts = new Coordinate[8];
        for (int j = 0; j < pts.length; j++) {
            pts[j] = inputPts[0];
        }
        for (int i = 1; i < inputPts.length; i++) {
            if (inputPts[i].x < pts[0].x) {
                pts[0] = inputPts[i];
            }
            if (inputPts[i].x - inputPts[i].y < pts[1].x - pts[1].y) {
                pts[1] = inputPts[i];
            }
            if (inputPts[i].y > pts[2].y) {
                pts[2] = inputPts[i];
            }
            if (inputPts[i].x + inputPts[i].y > pts[3].x + pts[3].y) {
                pts[3] = inputPts[i];
            }
            if (inputPts[i].x > pts[4].x) {
                pts[4] = inputPts[i];
            }
            if (inputPts[i].x - inputPts[i].y > pts[5].x - pts[5].y) {
                pts[5] = inputPts[i];
            }
            if (inputPts[i].y < pts[6].y) {
                pts[6] = inputPts[i];
            }
            if (inputPts[i].x + inputPts[i].y < pts[7].x + pts[7].y) {
                pts[7] = inputPts[i];
            }
        }
        return pts;
    }

    /**
     * @param coordinates the vertices of a linear ring, which may or may not be flattened (i.e.
     *     vertices collinear)
     * @return a 2-vertex <code>LineString</code> if the vertices are collinear; otherwise, a <code>
     *     Polygon</code> with unnecessary (collinear) vertices removed
     */
    private Geometry lineOrPolygon(Coordinate[] coordinates) {

        coordinates = cleanRing(coordinates);
        List<Position> positions = CoordinateArrays.toPositionList(this.crs, coordinates);
        if (coordinates.length == 3) {
            positions.remove(2);
            LineStringImpl lineString = new LineStringImpl(new PointArrayImpl(positions), 0.0);
            List<CurveSegment> segments = new ArrayList<CurveSegment>();
            segments.add(lineString);
            return new CurveImpl(this.crs, segments);
            // return
            // this.geomFactory.getPrimitiveFactory().createCurveByDirectPositions((List<Position>)
            // positions);
        }

        LineStringImpl lineString = new LineStringImpl(new PointArrayImpl(positions), 0.0);
        List<CurveSegment> segments = new ArrayList<CurveSegment>();
        segments.add(lineString);
        OrientableCurve curve = new CurveImpl(crs, segments);
        List<OrientableCurve> orientableCurves = new ArrayList<OrientableCurve>();
        orientableCurves.add(curve);

        Ring exterior = new RingImpl(orientableCurves);
        List<Ring> interiorList = new ArrayList<Ring>();

        SurfaceBoundaryImpl sb = new SurfaceBoundaryImpl(crs, exterior, interiorList);
        return new SurfaceImpl(sb);

        // return
        // this.geomFactory.getPrimitiveFactory().createSurfaceByDirectPositions((List<DirectPosition>) positions);
    }

    /**
     * @param original the vertices of a linear ring, which may or may not be flattened (i.e.
     *     vertices collinear)
     * @return the coordinates with unnecessary (collinear) vertices removed
     */
    private Coordinate[] cleanRing(Coordinate[] original) {
        Assert.equals(original[0], original[original.length - 1]);
        ArrayList cleanedRing = new ArrayList();
        Coordinate previousDistinctCoordinate = null;
        for (int i = 0; i <= original.length - 2; i++) {
            Coordinate currentCoordinate = original[i];
            Coordinate nextCoordinate = original[i + 1];
            if (currentCoordinate.equals(nextCoordinate)) {
                continue;
            }
            if (previousDistinctCoordinate != null
                    && isBetween(previousDistinctCoordinate, currentCoordinate, nextCoordinate)) {
                continue;
            }
            cleanedRing.add(currentCoordinate);
            previousDistinctCoordinate = currentCoordinate;
        }
        cleanedRing.add(original[original.length - 1]);
        Coordinate[] cleanedRingCoordinates = new Coordinate[cleanedRing.size()];
        return (Coordinate[]) cleanedRing.toArray(cleanedRingCoordinates);
    }

    /**
     * Compares {@link Coordinate}s for their angle and distance relative to an origin.
     *
     * @author Martin Davis
     * @version 1.7.2
     */
    private static class RadialComparator implements Comparator {
        private Coordinate origin;

        public RadialComparator(Coordinate origin) {
            this.origin = origin;
        }

        public int compare(Object o1, Object o2) {
            Coordinate p1 = (Coordinate) o1;
            Coordinate p2 = (Coordinate) o2;
            return polarCompare(origin, p1, p2);
        }

        /**
         * Given two points p and q compare them with respect to their radial ordering about point
         * o. First checks radial ordering. If points are collinear, the comparison is based on
         * their distance to the origin.
         *
         * <p>p < q iff
         *
         * <ul>
         *   <li>ang(o-p) < ang(o-q) (e.g. o-p-q is CCW)
         *   <li>or ang(o-p) == ang(o-q) && dist(o,p) < dist(o,q)
         * </ul>
         *
         * @param o the origin
         * @param p a point
         * @param q another point
         * @return -1, 0 or 1 depending on whether p is less than, equal to or greater than q
         */
        private static int polarCompare(Coordinate o, Coordinate p, Coordinate q) {
            double dxp = p.x - o.x;
            double dyp = p.y - o.y;
            double dxq = q.x - o.x;
            double dyq = q.y - o.y;

            /*
             * // MD - non-robust int result = 0; double alph = Math.atan2(dxp,
             * dyp); double beta = Math.atan2(dxq, dyq); if (alph < beta) {
             * result = -1; } if (alph > beta) { result = 1; } if (result != 0)
             * return result; //
             */

            int orient = CGAlgorithms.computeOrientation(o, p, q);

            if (orient == CGAlgorithms.COUNTERCLOCKWISE) return 1;
            if (orient == CGAlgorithms.CLOCKWISE) return -1;

            // points are collinear - check distance
            double op = dxp * dxp + dyp * dyp;
            double oq = dxq * dxq + dyq * dyq;
            if (op < oq) {
                return -1;
            }
            if (op > oq) {
                return 1;
            }
            return 0;
        }
    }
}
