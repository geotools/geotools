/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geometry.jts;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFactory;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.locationtech.jts.geom.PrecisionModel;
import org.locationtech.jts.geom.TopologyException;
import org.locationtech.jts.precision.GeometryPrecisionReducer;

/**
 * A stateful geometry clipper, can clip linestring on a specified rectangle. Trivial benchmarks
 * report a speedup factor between 20 and 60 compared to JTS generic intersection algorithm. The
 * class is not thread safe.
 *
 * @author Andrea Aime - OpenGeo
 */
public class GeometryClipper {

    private static int RIGHT = 2;

    private static int TOP = 8;

    private static int BOTTOM = 4;

    private static int LEFT = 1;

    final double xmin;

    final double ymin;

    final double xmax;

    final double ymax;

    final Envelope bounds;

    public Envelope getBounds() {
        return bounds;
    }

    public GeometryClipper(Envelope bounds) {
        this.xmin = bounds.getMinX();
        this.ymin = bounds.getMinY();
        this.xmax = bounds.getMaxX();
        this.ymax = bounds.getMaxY();
        this.bounds = bounds;
    }

    /**
     * This will try to handle failures when clipping - i.e. because of invalid input geometries
     * (often caused by simplification).
     *
     * <p>This attempts to do a normal clip(). If it fails, it will try to do more to ensure the
     * clip works properly.
     *
     * <ol>
     *   <li>if its a polygon/multipolygon it will try to make the polygon valid and re-try the clip
     *   <li>it will attempt to put the geometry on a precision grid (this will move the points
     *       around) and re-try the clip
     *   <li>will attempt to clip with ensureValid = false (ie geotools simple clipping)
     * </ol>
     *
     * See {@link #clip(Geometry, boolean) clip}
     *
     * @param scale Scale used to snap geometry to precision model, 0 to disable
     * @return a clipped geometry, which may be empty, or null
     */
    public Geometry clipSafe(Geometry g, boolean ensureValid, double scale) {
        try {
            return clip(g, ensureValid);
        } catch (TopologyException e) {
            try {
                if (((g instanceof Polygon) || (g instanceof MultiPolygon)) && (!g.isValid())) {
                    // its an invalid Polygon or MultiPolygon. Use buffer(0) to attempt to fix it
                    // do not use buffer(0) on points or lines - it returns an empty polygon
                    return clip(g.buffer(0), ensureValid);
                }
            } catch (TopologyException e2) {
            }

            if (scale != 0) {
                // Step 2: Snap to provided scale
                try {
                    GeometryPrecisionReducer reducer =
                            new GeometryPrecisionReducer(new PrecisionModel(scale));

                    // reduce method already tries to fix problems with geometry (ie buffer(0) if
                    // invalid)
                    Geometry reduced = reducer.reduce(g);
                    if (reduced.isEmpty()) {
                        throw new TopologyException("Could not snap geometry to precision model");
                    }
                    return clip(reduced, ensureValid);
                } catch (TopologyException e3) {
                    // if this fails, continue with other methods
                }
            }
            if (ensureValid) {
                try {
                    // Step 3: try again with ensureValid false
                    return clip(g, false);
                } catch (TopologyException e3) {
                }
            }
            return g; // unable to clip geometry
        }
    }

    /**
     * Clips the geometry on the specified bounds.
     *
     * @param g The geometry to be clipped
     * @param ensureValid If false there is no guarantee the polygons returned will be valid
     *     according to JTS rules (but should still be good enough to be used for pure rendering)
     */
    public Geometry clip(Geometry g, boolean ensureValid) {
        // basic pre-flight checks
        if (g == null) {
            return null;
        }
        Envelope geomEnvelope = g.getEnvelopeInternal();
        if (geomEnvelope.isNull()) {
            return null;
        }
        if (bounds.contains(geomEnvelope)) {
            return g;
        } else if (!bounds.intersects(geomEnvelope)) {
            return null;
        }

        // clip for good
        if (g instanceof LineString) {
            return clipLineString((LineString) g);
        } else if (g instanceof Polygon) {
            if (ensureValid) {
                GeometryFactory gf = g.getFactory();
                CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();
                Polygon fence = gf.createPolygon(buildBoundsString(gf, csf), null);
                return g.intersection(fence);
            } else {
                return clipPolygon((Polygon) g);
            }
        } else if (g instanceof GeometryCollection) {
            return clipCollection((GeometryCollection) g, ensureValid);
        } else {
            // still don't know how to clip this
            return g;
        }
    }

    /** Cohen-Sutherland outcode, see http://en.wikipedia.org/wiki/Cohen%E2%80%93Sutherland */
    private int computeOutCode(
            double x, double y, double xmin, double ymin, double xmax, double ymax) {
        int code = 0;
        if (y > ymax) code |= TOP;
        else if (y < ymin) code |= BOTTOM;
        if (x > xmax) code |= RIGHT;
        else if (x < xmin) code |= LEFT;
        return code;
    }

    /** Cohen sutherland based segment clipping */
    private double[] clipSegment(double[] segment) {
        // dump to local variables to avoid the array access check overhead
        double x0 = segment[0];
        double y0 = segment[1];
        double x1 = segment[2];
        double y1 = segment[3];

        // compute outcodes
        int outcode0 = computeOutCode(x0, y0, xmin, ymin, xmax, ymax);
        int outcode1 = computeOutCode(x1, y1, xmin, ymin, xmax, ymax);

        int step = 0;
        do {
            if ((outcode0 | outcode1) == 0) {
                // check if we got a degenerate segment
                if (x0 == x1 && y0 == y1) {
                    return null;
                }

                // both points are inside the clip area
                segment[0] = x0;
                segment[1] = y0;
                segment[2] = x1;
                segment[3] = y1;
                return segment;
            } else if ((outcode0 & outcode1) > 0) {
                // both points are outside of the clip area,
                // and on a same side (both top, both bottom, etc)
                return null;
            } else {
                // failed both tests, so calculate the line segment to clip
                // from an outside point to an intersection with clip edge

                // At least one endpoint is outside the clip rectangle; pick it.
                int outcodeOut = outcode0 != 0 ? outcode0 : outcode1;
                // Now find the intersection point;
                // use formulas y = y0 + slope * (x - x0),
                // x = x0 + (1/slope) * (y - y0)
                // depending on which side we're clipping
                // Note we might end up getting a point that is still outside (touches one side
                // but out on the other)
                double x, y;
                if ((outcodeOut & TOP) > 0) {
                    x = x0 + (x1 - x0) * (ymax - y0) / (y1 - y0);
                    y = ymax;
                } else if ((outcodeOut & BOTTOM) > 0) {
                    x = x0 + (x1 - x0) * (ymin - y0) / (y1 - y0);
                    y = ymin;
                } else if ((outcodeOut & RIGHT) > 0) {
                    y = y0 + (y1 - y0) * (xmax - x0) / (x1 - x0);
                    x = xmax;
                } else { // LEFT
                    y = y0 + (y1 - y0) * (xmin - x0) / (x1 - x0);
                    x = xmin;
                }
                // We sliced at least one ordinate, recompute the outcode for the end we
                // modified
                if (outcodeOut == outcode0) {
                    x0 = x;
                    y0 = y;
                    outcode0 = computeOutCode(x0, y0, xmin, ymin, xmax, ymax);
                } else {
                    x1 = x;
                    y1 = y;
                    outcode1 = computeOutCode(x1, y1, xmin, ymin, xmax, ymax);
                }
            }

            step++;
        } while (step < 5);

        // we should really never get here, the algorithm must at most clip two ends,
        // at worst one ordinate at a time, so at most 4 steps
        throw new RuntimeException("Algorithm did not converge");
    }

    /** Checks if the specified segment it outside the clipping bounds */
    private boolean outside(double x0, double y0, double x1, double y1) {
        int outcode0 = computeOutCode(x0, y0, xmin, ymin, xmax, ymax);
        int outcode1 = computeOutCode(x1, y1, xmin, ymin, xmax, ymax);

        return ((outcode0 & outcode1) > 0);
    }

    /** Checks if the point is inside the clipping bounds */
    private boolean contained(final double x, final double y) {
        return x > xmin && x < xmax && y > ymin && y < ymax;
    }

    /**
     * Clips a polygon using the Liang-Barsky helper routine. Does not generate, in general, valid
     * polygons (but still does generate polygons good enough for rendering)
     */
    private Geometry clipPolygon(Polygon polygon) {
        final GeometryFactory gf = polygon.getFactory();

        LinearRing exterior = (LinearRing) polygon.getExteriorRing();
        LinearRing shell = polygonClip(exterior);
        shell = cleanupRings(shell);
        if (shell == null) {
            return null;
        }

        List<LinearRing> holes = new ArrayList<LinearRing>();
        for (int i = 0; i < polygon.getNumInteriorRing(); i++) {
            LinearRing hole = (LinearRing) polygon.getInteriorRingN(i);
            hole = polygonClip(hole);
            hole = cleanupRings(hole);
            if (hole != null) {
                holes.add(hole);
            }
        }

        return gf.createPolygon(shell, (LinearRing[]) holes.toArray(new LinearRing[holes.size()]));
    }

    /**
     * The {@link #polygonClip(LinearRing)} routine can generate invalid rings fully on top of the
     * clipping area borders (with no inside). Do a quick check that does not involve an expensive
     * isValid() call
     *
     * @return The ring, or null if the ring was not valid
     */
    private LinearRing cleanupRings(LinearRing ring) {
        if (ring == null || ring.isEmpty()) {
            return null;
        }

        final CoordinateSequence cs = ring.getCoordinateSequence();
        double px = cs.getX(0);
        double py = cs.getY(0);
        boolean fullyOnBorders = true;
        for (int i = 1; i < cs.size() && fullyOnBorders; i++) {
            double x = cs.getX(i);
            double y = cs.getY(i);
            // check if the current segment lies on the bbox side fully
            if ((x == px && (x == xmin || x == xmax)) || (y == py && (y == ymin || y == ymax))) {
                px = x;
                py = y;
            } else {
                fullyOnBorders = false;
            }
        }
        // all sides are sitting on the bbox borders, this is the degenerate case
        // we are trying to filter out
        if (fullyOnBorders) {
            // could still be a case of a polygon equal to the clipping border itself
            // This area test could actually replace the whole method,
            // but it's more expensive to run, so we use it as a last resort for a specific case
            if (ring.getFactory().createPolygon(ring).getArea() > 0) {
                return ring;
            } else {
                return null;
            }
        } else {
            return ring;
        }
    }

    /**
     * This routine uses the Liang-Barsky algorithm for polygon clipping as described in Foley & van
     * Dam. It's more efficient Sutherland-Hodgman version, but produces redundent turning vertices
     * at the corners of the clip region. This can make rendering as a series of triangles very
     * awkward, but it's fine of your underlying graphics mechanism has a forgiving drawPolygon
     * routine. This algorithm comes from http://www.longsteve.com/fixmybugs/?p=359, under a "DO
     * WHAT THE FUCK YOU WANT TO PUBLIC LICENSE" (no kidding!)
     */
    private LinearRing polygonClip(LinearRing ring) {
        final double INFINITY = Double.MAX_VALUE;

        CoordinateSequence cs = ring.getCoordinateSequence();
        Ordinates out = new Ordinates();

        // Coordinates of intersection between the infinite line hosting the segment and the clip
        // area
        double xIn, xOut, yIn, yOut;
        // Parameter values of same, they are in [0,1] if the intersections are inside the segment,
        // < 0 or > 1 otherwise
        double tInX, tOutX, tInY, tOutY;
        // tOut2: max between tOutX and tOutY, tIn2: max between tInX and tinY
        double tOut1, tOut2, tIn2;

        // Direction of edge
        double deltaX, deltaY;
        int i;

        // for each edge
        for (i = 0; i < cs.size() - 1; i++) {
            // extract the edge
            double x0 = cs.getOrdinate(i, 0);
            double x1 = cs.getOrdinate(i + 1, 0);
            double y0 = cs.getOrdinate(i, 1);
            double y1 = cs.getOrdinate(i + 1, 1);

            // determine direction of edge
            deltaX = x1 - x0;
            deltaY = y1 - y0;

            // use this to determine which bounding lines for the clip region the
            // containing line hits first (from which side, to which other side)
            if ((deltaX > 0) || (deltaX == 0 && x0 > xmax)) {
                xIn = xmin;
                xOut = xmax;
            } else {
                xIn = xmax;
                xOut = xmin;
            }
            if ((deltaY > 0) || (deltaY == 0 && y0 > ymax)) {
                yIn = ymin;
                yOut = ymax;
            } else {
                yIn = ymax;
                yOut = ymin;
            }

            // find the t values for the x and y exit points
            if (deltaX != 0) {
                tOutX = (xOut - x0) / deltaX;
            } else if (x0 <= xmax && xmin <= x0) {
                // vertical line crossing the clip box
                tOutX = INFINITY;
            } else {
                // vertical line outside the clip box
                tOutX = -INFINITY;
            }

            if (deltaY != 0) {
                tOutY = (yOut - y0) / deltaY;
            } else if (y0 <= ymax && ymin <= y0) {
                // horizontal line crossing the clip box
                tOutY = INFINITY;
            } else {
                // horizontal line outside the clip box
                tOutY = -INFINITY;
            }

            // Order the two exit points
            if (tOutX < tOutY) {
                tOut1 = tOutX;
                tOut2 = tOutY;
            } else {
                tOut1 = tOutY;
                tOut2 = tOutX;
            }

            // skip tests if exit intersection points are before the
            // beginning of the segment
            if (tOut2 > 0) {

                // now compute the params of the first intersection point
                if (deltaX != 0) {
                    tInX = (xIn - x0) / deltaX;
                } else {
                    tInX = -INFINITY;
                }

                if (deltaY != 0) {
                    tInY = (yIn - y0) / deltaY;
                } else {
                    tInY = -INFINITY;
                }

                // sort them
                if (tInX < tInY) {
                    tIn2 = tInY;
                } else {
                    tIn2 = tInX;
                }

                if (tOut1 < tIn2) {
                    // no visible segment
                    if (0 < tOut1 && tOut1 <= 1.0) {
                        // line crosses over intermediate corner region
                        if (tInX < tInY) {
                            out.add(xOut, yIn);
                        } else {
                            out.add(xIn, yOut);
                        }
                    }
                } else {
                    // line crosses though window
                    if (0 < tOut1 && tIn2 <= 1.0) {
                        if (0 <= tIn2) { // visible segment
                            if (tInX > tInY) {
                                out.add(xIn, y0 + (tInX * deltaY));
                            } else {
                                out.add(x0 + (tInY * deltaX), yIn);
                            }
                        }

                        if (1.0 >= tOut1) {
                            if (tOutX < tOutY) {
                                out.add(xOut, y0 + (tOutX * deltaY));
                            } else {
                                out.add(x0 + (tOutY * deltaX), yOut);
                            }
                        } else {
                            out.add(x1, y1);
                        }
                    }
                }

                if ((0 < tOut2 && tOut2 <= 1.0)) {
                    out.add(xOut, yOut);
                }
            }
        }

        if (out.size() < 3) {
            return null;
        }

        if (out.getOrdinate(0, 0) != out.getOrdinate(out.size() - 1, 0)
                || out.getOrdinate(0, 1) != out.getOrdinate(out.size() - 1, 1)) {
            out.add(out.getOrdinate(0, 0), out.getOrdinate(0, 1));
        } else if (out.size() == 3) {
            return null;
        }

        return ring.getFactory()
                .createLinearRing(
                        out.toCoordinateSequence(ring.getFactory().getCoordinateSequenceFactory()));
    }

    /** Builds a linear ring representing the clipping area */
    LinearRing buildBoundsString(final GeometryFactory gf, final CoordinateSequenceFactory csf) {
        CoordinateSequence cs = JTS.createCS(csf, 5, 2);
        cs.setOrdinate(0, 0, xmin);
        cs.setOrdinate(0, 1, ymin);
        cs.setOrdinate(1, 0, xmin);
        cs.setOrdinate(1, 1, ymax);
        cs.setOrdinate(2, 0, xmax);
        cs.setOrdinate(2, 1, ymax);
        cs.setOrdinate(3, 0, xmax);
        cs.setOrdinate(3, 1, ymin);
        cs.setOrdinate(4, 0, xmin);
        cs.setOrdinate(4, 1, ymin);
        return gf.createLinearRing(cs);
    }

    /** Recursively clips a collection */
    private Geometry clipCollection(GeometryCollection gc, boolean ensureValid) {
        if (gc.getNumGeometries() == 1) {
            return clip(gc.getGeometryN(0), ensureValid);
        } else {
            List<Geometry> result = new ArrayList<Geometry>(gc.getNumGeometries());
            for (int i = 0; i < gc.getNumGeometries(); i++) {
                Geometry clipped = clip(gc.getGeometryN(i), ensureValid);
                if (clipped != null) {
                    result.add(clipped);
                }
            }

            if (gc instanceof MultiPoint) {
                result = filterCollection(Point.class, result);
            } else if (gc instanceof MultiLineString) {
                result = filterCollection(LineString.class, result);
            } else if (gc instanceof MultiPolygon) {
                result = filterCollection(Polygon.class, result);
            }

            if (result.size() == 0) {
                return null;
            } else if (result.size() == 1) {
                return result.get(0);
            }

            flattenCollection(result);

            if (gc instanceof MultiPoint) {
                return gc.getFactory()
                        .createMultiPoint((Point[]) result.toArray(new Point[result.size()]));
            } else if (gc instanceof MultiLineString) {
                return gc.getFactory()
                        .createMultiLineString(
                                (LineString[]) result.toArray(new LineString[result.size()]));
            } else if (gc instanceof MultiPolygon) {
                return gc.getFactory()
                        .createMultiPolygon((Polygon[]) result.toArray(new Polygon[result.size()]));
            } else {
                return gc.getFactory()
                        .createGeometryCollection(
                                (Geometry[]) result.toArray(new Geometry[result.size()]));
            }
        }
    }

    /** Returns a collection with just the elements matching the desired class */
    private List<Geometry> filterCollection(Class clazz, List<Geometry> collection) {
        return collection.stream().filter(g -> clazz.isInstance(g)).collect(Collectors.toList());
    }

    private void flattenCollection(List<Geometry> result) {
        for (int i = 0; i < result.size(); ) {
            Geometry g = result.get(i);
            if (g instanceof GeometryCollection) {
                GeometryCollection gc = (GeometryCollection) g;
                for (int j = 0; j < gc.getNumGeometries(); j++) {
                    result.add(gc.getGeometryN(j));
                }
                result.remove(i);
            } else {
                i++;
            }
        }
    }

    /** Clips a linestring using the Cohen-Sutherlan segment clipping helper method */
    Geometry clipLineString(LineString line) {
        // the result
        List<LineString> clipped = new ArrayList<LineString>();

        // grab all the factories a
        final GeometryFactory gf = line.getFactory();
        final CoordinateSequenceFactory csf = gf.getCoordinateSequenceFactory();
        final CoordinateSequence coords = line.getCoordinateSequence();

        // first step
        final Ordinates ordinates = new Ordinates(coords.size());
        double x0 = coords.getX(0);
        double y0 = coords.getY(0);
        boolean prevInside = contained(x0, y0);
        if (prevInside) {
            ordinates.add(x0, y0);
        }
        double[] segment = new double[4];
        final int size = coords.size();
        // loop over the other coordinates
        for (int i = 1; i < size; i++) {
            final double x1 = coords.getX(i);
            final double y1 = coords.getY(i);

            boolean inside = contained(x1, y1);
            if (inside == prevInside) {
                if (inside) {
                    // both segments were inside, not need for clipping
                    ordinates.add(x1, y1);
                } else {
                    // both were outside, this might still be caused by a line
                    // crossing the envelope but whose endpoints lie outside
                    if (!outside(x0, y0, x1, y1)) {
                        segment[0] = x0;
                        segment[1] = y0;
                        segment[2] = x1;
                        segment[3] = y1;
                        double[] clippedSegment = clipSegment(segment);
                        if (clippedSegment != null) {
                            CoordinateSequence cs =
                                    JTS.createCS(
                                            csf, 2, coords.getDimension(), coords.getMeasures());
                            cs.setOrdinate(0, 0, clippedSegment[0]);
                            cs.setOrdinate(0, 1, clippedSegment[1]);
                            cs.setOrdinate(1, 0, clippedSegment[2]);
                            cs.setOrdinate(1, 1, clippedSegment[3]);
                            clipped.add(gf.createLineString(cs));
                        }
                    }
                }
            } else {
                // one inside, the other outside, a clip must occurr
                segment[0] = x0;
                segment[1] = y0;
                segment[2] = x1;
                segment[3] = y1;
                double[] clippedSegment = clipSegment(segment);
                if (clippedSegment != null) {
                    if (prevInside) {
                        ordinates.add(clippedSegment[2], clippedSegment[3]);
                    } else {
                        ordinates.add(clippedSegment[0], clippedSegment[1]);
                        ordinates.add(clippedSegment[2], clippedSegment[3]);
                    }
                    // if we are going from inside to outside it's time to cut a linestring
                    // into the results
                    if (prevInside) {
                        // if(closed) {
                        // addClosingPoints(ordinates, shell);
                        // clipped.add(gf.createLinearRing(ordinates.toCoordinateSequence(csf)));
                        // } else {
                        clipped.add(gf.createLineString(ordinates.toCoordinateSequence(csf)));
                        // }
                        ordinates.clear();
                    }
                } else {
                    prevInside = false;
                }
            }
            prevInside = inside;
            x0 = x1;
            y0 = y1;
        }
        // don't forget the last linestring
        if (ordinates.size() > 1) {
            clipped.add(gf.createLineString(ordinates.toCoordinateSequence(csf)));
        }

        if (line.isClosed() && clipped.size() > 1) {
            // the first and last strings might be adjacent, in that case fuse them
            CoordinateSequence cs0 = clipped.get(0).getCoordinateSequence();
            CoordinateSequence cs1 = clipped.get(clipped.size() - 1).getCoordinateSequence();
            if (cs0.getOrdinate(0, 0) == cs1.getOrdinate(cs1.size() - 1, 0)
                    && cs0.getOrdinate(0, 1) == cs1.getOrdinate(cs1.size() - 1, 1)) {
                CoordinateSequence cs = JTS.createCS(csf, cs0.size() + cs1.size() - 1, 2);
                for (int i = 0; i < cs1.size(); i++) {
                    cs.setOrdinate(i, 0, cs1.getOrdinate(i, 0));
                    cs.setOrdinate(i, 1, cs1.getOrdinate(i, 1));
                }
                for (int i = 1; i < cs0.size(); i++) {
                    cs.setOrdinate(i + cs1.size() - 1, 0, cs0.getOrdinate(i, 0));
                    cs.setOrdinate(i + cs1.size() - 1, 1, cs0.getOrdinate(i, 1));
                }
                clipped.remove(0);
                clipped.remove(clipped.size() - 1);
                clipped.add(gf.createLineString(cs));
            }
        }

        // return the results
        if (clipped.size() > 1) {
            return gf.createMultiLineString(
                    (LineString[]) clipped.toArray(new LineString[clipped.size()]));
        } else if (clipped.size() == 1) {
            return clipped.get(0);
        } else {
            return null;
        }
    }
}
