/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Accepts geometries and collapses all the vertices that will be rendered to the same pixel. This
 * class works only if the Geometries are based on {@link LiteCoordinateSequence} instances.
 *
 * @author jeichar
 * @since 2.1.x
 */
public final class Decimator {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(Decimator.class);

    static final double DP_THRESHOLD;

    static {
        int threshold = -1;
        String sthreshold = System.getProperty("org.geotools.decimate.dpThreshold");
        if (sthreshold != null) {
            try {
                threshold = Integer.parseInt(sthreshold);
            } catch (Throwable t) {
                LOGGER.log(
                        Level.WARNING,
                        "Invalid value for org.geotools.decimate.dpThreshold, "
                                + "should be a positive integer but is: "
                                + sthreshold);
            }
        }
        DP_THRESHOLD = threshold;
    }

    private static final double EPS = 1e-9;

    private double spanx = -1;

    private double spany = -1;

    /**
     * Builds a decimator that will generalize geometries so that two subsequent points will be at
     * least pixelDistance away from each other when painted on the screen. Set pixelDistance to 0
     * if you don't want any generalization (but just a transformation)
     */
    public Decimator(MathTransform screenToWorld, Rectangle paintArea, double pixelDistance) {
        if (screenToWorld != null && pixelDistance > 0) {
            try {
                double[] spans =
                        computeGeneralizationDistances(screenToWorld, paintArea, pixelDistance);
                this.spanx = spans[0];
                this.spany = spans[1];
            } catch (TransformException e) {
                throw new RuntimeException(
                        "Could not perform the generalization spans computation", e);
            }
        } else {
            this.spanx = 1;
            this.spany = 1;
        }
    }

    /** The generalization step in the x direction */
    public double getSpanX() {
        return spanx;
    }

    /** The generalization step in the y direction */
    public double getSpanY() {
        return spany;
    }

    /**
     * djb - noticed that the old way of finding out the decimation is based on the (0,0) location
     * of the image. This is often wildly unrepresentitive of the scale of the entire map.
     *
     * <p>A better thing to do is to decimate this on a per-shape basis (and use the shape's
     * center). Another option would be to sample the image at different locations (say 9) and
     * choose the smallest spanx/spany you find.
     *
     * <p>Also, if the xform is an affine Xform, you can be a bit more aggressive in the decimation.
     * If its not an affine xform (ie. its actually doing a CRS xform), you may find this is a bit
     * too aggressive due to any number of mathematical issues.
     *
     * <p>This is just a simple method that uses the centre of the given rectangle instead of (0,0).
     *
     * <p>NOTE: this could need more work based on CRS, but the rectangle is in pixels so it should
     * be fairly immune to all but crazy projections.
     */
    public Decimator(MathTransform screenToWorld, Rectangle paintArea) {
        // 0.8 is just so you don't decimate "too much". magic number.
        this(screenToWorld, paintArea, 0.8);
    }

    /**
     * Given a full transformation from screen to world and the paint area computes a best guess of
     * the maxium generalization distance that won't make the transformations induced by the
     * generalization visible on screen.
     *
     * <p>In other words, it computes how long a pixel is in the native spatial reference system of
     * the data
     */
    public static double[] computeGeneralizationDistances(
            MathTransform screenToWorld, Rectangle paintArea, double pixelDistance)
            throws TransformException {
        try {
            // init the spans with the upper left corner
            double[] spans =
                    getGeneralizationSpans(
                            paintArea.x + paintArea.width / 2,
                            paintArea.y + paintArea.height / 2,
                            screenToWorld);
            // search over a simple 3x3 grid for higher spans so that we perform a basic sampling of
            // the whole area and we pick the shortest generalization distances
            for (int i = 0; i < 2; i++) {
                for (int j = 0; j < 2; j++) {
                    double[] ns =
                            getGeneralizationSpans(
                                    paintArea.x + paintArea.width * i / 2.0,
                                    paintArea.y + paintArea.height * j / 2.0,
                                    screenToWorld);
                    if (isFinite(ns[0]) && (ns[0] < spans[0] || !isFinite(spans[0]))) {
                        spans[0] = ns[0];
                    }
                    if (isFinite(ns[1]) && (ns[1] < spans[1] || !isFinite(spans[1]))) {
                        spans[1] = ns[1];
                    }
                }
            }
            if (!isFinite(spans[0])) {
                spans[0] = 0;
            }
            if (!isFinite(spans[1])) {
                spans[1] = 0;
            }
            spans[0] *= pixelDistance;
            spans[1] *= pixelDistance;
            return spans;
        } catch (TransformException e) {
            // if we can't transform we went way out of the area of definition for the transform ->
            // don't generalize
            return new double[] {0, 0};
        }
    }

    /** Checks the specified number is not infinite nor Nan */
    private static boolean isFinite(double d) {
        return !Double.isNaN(d) && !Double.isInfinite(d);
    }

    /** Computes the real world distance of a one pixel segment centered in the specified point */
    static double[] getGeneralizationSpans(double x, double y, MathTransform transform)
            throws TransformException {
        double[] original = new double[] {x - 0.5, y - 0.5, x + 0.5, y + 0.5};
        double[] transformed = new double[4];
        transform.transform(original, 0, transformed, 0, 2);
        double[] spans = new double[2];
        spans[0] = Math.abs(transformed[0] - transformed[2]);
        spans[1] = Math.abs(transformed[1] - transformed[3]);
        return spans;
    }

    public Decimator(double spanx, double spany) {
        this.spanx = spanx;
        this.spany = spany;
    }

    public final Geometry decimateTransformGeneralize(Geometry geometry, MathTransform transform)
            throws TransformException {
        if (geometry instanceof GeometryCollection) {
            GeometryCollection collection = (GeometryCollection) geometry;
            final int length = collection.getNumGeometries();
            boolean cloned = false;
            Class elementType = null;
            Geometry[] elements = null;
            for (int i = 0; i < length; i++) {
                Geometry source = collection.getGeometryN(i);
                Geometry generalized = decimateTransformGeneralize(source, transform);

                // lazily handle the case where we need to deep clone
                if (generalized != source) {
                    cloned = true;
                    if (elements == null) {
                        elements = new Geometry[collection.getNumGeometries()];
                        for (int j = 0; j < i; j++) {
                            Geometry element = collection.getGeometryN(j);
                            elements[j] = element;
                            accumulateGeometryType(elementType, element);
                        }
                    }
                }
                if (cloned) {
                    elements[i] = generalized;
                    elementType = accumulateGeometryType(elementType, generalized);
                }
            }
            if (cloned) {
                if (elementType == Point.class) {
                    Point[] points = new Point[elements.length];
                    System.arraycopy(elements, 0, points, 0, elements.length);
                    return collection.getFactory().createMultiPoint(points);
                } else if (elementType == LineString.class) {
                    LineString[] lines = new LineString[elements.length];
                    System.arraycopy(elements, 0, lines, 0, elements.length);
                    return collection.getFactory().createMultiLineString(lines);
                } else if (elementType == Polygon.class) {
                    Polygon[] polys = new Polygon[elements.length];
                    System.arraycopy(elements, 0, polys, 0, elements.length);
                    return collection.getFactory().createMultiPolygon(polys);
                } else {
                    return collection.getFactory().createGeometryCollection(elements);
                }
            } else {
                return collection;
            }
        } else if (geometry instanceof Point) {
            LiteCoordinateSequence seq =
                    (LiteCoordinateSequence) ((Point) geometry).getCoordinateSequence();
            decimateTransformGeneralize(seq, transform, false, spanx, spany);
            return geometry;
        } else if (geometry instanceof Polygon) {
            Polygon polygon = (Polygon) geometry;
            LinearRing shell =
                    (LinearRing) decimateTransformGeneralize(polygon.getExteriorRing(), transform);
            boolean cloned = shell != polygon.getExteriorRing();
            final int length = polygon.getNumInteriorRing();
            LinearRing[] holes = cloned ? new LinearRing[length] : null;
            for (int i = 0; i < length; i++) {
                LineString hole = polygon.getInteriorRingN(i);
                LinearRing generalized = (LinearRing) decimateTransformGeneralize(hole, transform);
                cloned |= generalized != hole;
                if (cloned) {
                    if (holes == null) {
                        holes = new LinearRing[length];
                        for (int j = 0; j < i; j++) {
                            holes[j] = (LinearRing) polygon.getInteriorRingN(j);
                        }
                    }
                    holes[i] = generalized;
                }
            }

            if (cloned) {
                return polygon.getFactory().createPolygon(shell, holes);
            } else {
                return polygon;
            }
        } else if (geometry instanceof LineString) {
            double spanx = this.spanx;
            double spany = this.spany;
            LineString ls = (LineString) geometry;
            if (ls instanceof CurvedGeometry<?>) {
                CurvedGeometry<LineString> curved = (CurvedGeometry<LineString>) ls;
                ls = curved.linearize(Math.min(Math.abs(spanx), Math.abs(spany)));
                // do not generalize further, we already got a good representation
                spanx = -1;
                spany = -1;
            }
            CoordinateSequence originalSequence = ls.getCoordinateSequence();
            LiteCoordinateSequence seq = LiteCoordinateSequenceFactory.lite(originalSequence);
            boolean loop = ls instanceof LinearRing;
            if (!loop && seq.size() > 1) {
                double x0 = seq.getOrdinate(0, 0);
                double y0 = seq.getOrdinate(0, 1);
                double x1 = seq.getOrdinate(seq.size() - 1, 0);
                double y1 = seq.getOrdinate(seq.size() - 1, 1);
                loop = Math.abs(x0 - x1) < EPS && Math.abs(y0 - y1) < EPS;
            }
            decimateTransformGeneralize(seq, transform, loop, spanx, spany);
            if (seq != originalSequence) {
                if (loop) {
                    ls = ls.getFactory().createLinearRing(seq);
                } else {
                    ls = ls.getFactory().createLineString(seq);
                }
            }
            return ls;
        } else {
            return geometry;
        }
    }

    private Class accumulateGeometryType(Class elementType, Geometry generalized) {
        Class<? extends Geometry> geometryType = generalized.getClass();
        if (elementType == null) {
            elementType = geometryType;
        } else if (elementType != geometryType && elementType != Geometry.class) {
            if (!elementType.isAssignableFrom(geometryType)) {
                if (geometryType.isAssignableFrom(elementType)) {
                    elementType = geometryType;
                } else {
                    elementType = Geometry.class;
                }
            }
        }
        return elementType;
    }

    /** decimates JTS geometries. */
    public final void decimate(Geometry geom) {
        if (spanx == -1) return;
        if (geom instanceof MultiPoint) {
            // TODO check geometry and if its bbox is too small turn it into a 1
            // point geom
            return;
        }
        if (geom instanceof GeometryCollection) {
            // TODO check geometry and if its bbox is too small turn it into a
            // 1-2 point geom
            // takes a bit of work because the geometry will need to be
            // recreated.
            GeometryCollection collection = (GeometryCollection) geom;
            final int numGeometries = collection.getNumGeometries();
            for (int i = 0; i < numGeometries; i++) {
                decimate(collection.getGeometryN(i));
            }
        } else if (geom instanceof LineString) {
            LineString line = (LineString) geom;
            LiteCoordinateSequence seq = (LiteCoordinateSequence) line.getCoordinateSequence();
            if (decimateOnEnvelope(line, seq)) {
                return;
            }
            decimate(line, seq);
        } else if (geom instanceof Polygon) {
            Polygon line = (Polygon) geom;
            decimate(line.getExteriorRing());
            final int numRings = line.getNumInteriorRing();
            for (int i = 0; i < numRings; i++) {
                decimate(line.getInteriorRingN(i));
            }
        }
    }

    /** */
    private boolean decimateOnEnvelope(Geometry geom, LiteCoordinateSequence seq) {
        Envelope env = geom.getEnvelopeInternal();
        if (env.getWidth() <= spanx && env.getHeight() <= spany) {
            if (geom instanceof LinearRing) {
                decimateRingFully(seq);
                return true;
            } else {
                double[] coords = seq.getArray();
                int dim = seq.getDimension();
                double[] newcoords = new double[dim * 2];
                for (int i = 0; i < dim; i++) {
                    newcoords[i] = coords[i];
                    newcoords[dim + i] = coords[coords.length - dim + i];
                }
                seq.setArray(newcoords);
                return true;
            }
        }
        return false;
    }

    /** Makes sure the ring is turned into a minimal 3 non equal points one */
    private void decimateRingFully(LiteCoordinateSequence seq) {
        double[] coords = seq.getArray();
        int dim = seq.getDimension();

        // degenerate one, it's not even a triangle, or just a triangle
        if (seq.size() <= 4) return;

        double[] newcoords = new double[dim * 4];
        // assuming the ring makes sense in the first place (i.e., it's at least a triangle),
        // we copy the first two and the last two points
        for (int i = 0; i < dim; i++) {
            newcoords[i] = coords[i];
            newcoords[dim + i] = coords[dim + i];
            newcoords[dim * 2 + i] = coords[coords.length - dim * 2 + i];
            newcoords[dim * 3 + i] = coords[coords.length - dim + i];
        }
        seq.setArray(newcoords);
    }

    /**
     * 1. remove any points that are within the spanx,spany. We ALWAYS keep 1st and last point 2.
     * transform to screen coordinates 3. remove any points that are close (span <1)
     */
    private final void decimateTransformGeneralize(
            LiteCoordinateSequence seq,
            MathTransform transform,
            boolean ring,
            double spanx,
            double spany)
            throws TransformException {
        // decimates before XFORM
        int ncoords = seq.size();
        double coords[] = null;
        int sourceDimensions = 2;
        if (transform != null) {
            sourceDimensions = transform.getSourceDimensions();
            coords = seq.getOrdinateArray(sourceDimensions);
        } else {
            coords = seq.getXYArray();
        }

        if (ncoords < 2) {
            if (ncoords == 1) // 1 coordinate -- just xform it
            {
                // double[] newCoordsXformed2 = new double[2];
                if (transform != null) {
                    transform.transform(coords, 0, coords, 0, 1);
                    if (sourceDimensions > 2) {
                        double[] flatCoords = new double[seq.size() * 2];
                        for (int i = 0; i < seq.size(); i++) {
                            flatCoords[i * 2] = coords[i * sourceDimensions];
                            flatCoords[i * 2 + 1] = coords[i * sourceDimensions + 1];
                        }
                        seq.setArray(flatCoords, 2);
                    } else {
                        seq.setArray(coords, 2);
                    }
                }
                return;
            } else return; // ncoords =0
        }

        // if spanx/spany is -1, then no generalization should be done and all
        // coordinates can just be transformed directly
        if (spanx == -1 && spany == -1) {
            // do the xform if needed
            if ((transform != null) && (!transform.isIdentity())) {
                transform.transform(coords, 0, coords, 0, ncoords);
                seq.setArray(coords, 2);
            }
            return;
        }

        // generalize, use the heavier algorithm for longer lines
        int actualCoords = spanBasedGeneralize(ncoords, coords, spanx, spany);
        if (DP_THRESHOLD > 0 && actualCoords > DP_THRESHOLD) {
            actualCoords =
                    dpBasedGeneralize(
                            actualCoords, coords, Math.min(spanx, spany) * Math.min(spanx, spany));
        }

        // handle rings
        if (ring && actualCoords <= 3) {
            if (coords.length > 6) {
                // normal rings
                coords[2] = coords[2];
                coords[3] = coords[3];
                coords[4] = coords[4];
                coords[5] = coords[5];
                actualCoords = 3;
            } else if (coords.length > 4) {
                // invalid rings, they do A-B-A, that is, two overlapping lines
                coords[2] = coords[2];
                coords[3] = coords[3];
                actualCoords = 2;
            }
        }

        // always have last one
        coords[actualCoords * 2] = coords[(ncoords - 1) * 2];
        coords[actualCoords * 2 + 1] = coords[(ncoords - 1) * 2 + 1];
        actualCoords++;

        // DO THE XFORM
        if (transform != null && !transform.isIdentity()) {
            transform.transform(coords, 0, coords, 0, actualCoords);
        }

        // stick back into the coordinate sequence
        if (actualCoords * 2 < coords.length) {
            double[] seqDouble = new double[2 * actualCoords];
            System.arraycopy(coords, 0, seqDouble, 0, actualCoords * 2);
            seq.setArray(seqDouble, 2);
        } else {
            seq.setArray(coords, 2);
        }
    }

    private int spanBasedGeneralize(int ncoords, double[] coords, double spanx, double spany) {
        int actualCoords = 1;
        double lastX = coords[0];
        double lastY = coords[1];
        for (int t = 1; t < (ncoords - 1); t++) {
            // see if this one should be added
            double x = coords[t * 2];
            double y = coords[t * 2 + 1];
            if ((Math.abs(x - lastX) > spanx) || (Math.abs(y - lastY)) > spany) {
                coords[actualCoords * 2] = x;
                coords[actualCoords * 2 + 1] = y;
                lastX = x;
                lastY = y;
                actualCoords++;
            }
        }
        return actualCoords;
    }

    private int dpBasedGeneralize(int ncoords, double[] coords, double maxDistance) {
        while (coords[0] == coords[(ncoords - 1) * 2]
                && coords[1] == coords[2 * ncoords - 1]
                && ncoords > 0) {
            ncoords--;
        }
        if (ncoords == 0) {
            return 0;
        }

        dpSimplifySection(0, ncoords - 1, coords, maxDistance);
        int actualCoords = 1;
        for (int i = 1; i < ncoords - 1; i++) {
            final double x = coords[i * 2];
            final double y = coords[i * 2 + 1];
            if (!Double.isNaN(x)) {
                coords[actualCoords * 2] = x;
                coords[actualCoords * 2 + 1] = y;
                actualCoords++;
            }
        }

        return actualCoords;
    }

    private void dpSimplifySection(
            int first, int last, double[] coords, double maxDistanceSquared) {
        if (last - 1 <= first) {
            return;
        }

        double x0 = coords[first * 2];
        double y0 = coords[first * 2 + 1];
        double x1 = coords[last * 2];
        double y1 = coords[last * 2 + 1];
        double dx = x1 - x0;
        double dy = y1 - y0;
        double ls = dx * dx + dy * dy;

        int idx = -1;
        double dsmax = -1;
        for (int i = first + 1; i < last; i++) {
            double x = coords[i * 2];
            double y = coords[i * 2 + 1];

            double ds;
            double r = ((x - x0) * dx + (y - y0) * dy) / ls;
            if (r <= 0.0) {
                ds = (x - x0) * (x - x0) + (y - y0) * (y - y0);
            } else if (r >= 1.0) {
                ds = (x - x1) * (x - x1) + (y - y1) * (y - y1);
            } else {
                double s = ((y0 - y) * dx - (x0 - x) * dy) / ls;
                ds = s * s * ls;
            }

            if (idx == -1 || ds > dsmax) {
                idx = i;
                dsmax = ds;
            }
        }

        if (dsmax <= maxDistanceSquared) {
            for (int i = first + 1; i < last; i++) {
                coords[i * 2] = Double.NaN;
                coords[i * 2 + 1] = Double.NaN;
            }
        } else {
            dpSimplifySection(first, idx, coords, maxDistanceSquared);
            dpSimplifySection(idx, last, coords, maxDistanceSquared);
        }
    }

    private void decimate(Geometry g, LiteCoordinateSequence seq) {
        double[] coords = seq.getXYArray();
        int dim = seq.getDimension();
        int numDoubles = coords.length;
        int readDoubles = 0;
        double prevx, currx, prevy, curry, diffx, diffy;
        for (int currentDoubles = 0; currentDoubles < numDoubles; currentDoubles += dim) {
            if (currentDoubles >= dim && currentDoubles < numDoubles - dim) {
                prevx = coords[readDoubles - dim];
                currx = coords[currentDoubles];
                diffx = Math.abs(prevx - currx);
                prevy = coords[readDoubles - dim + 1];
                curry = coords[currentDoubles + 1];
                diffy = Math.abs(prevy - curry);
                if (diffx > spanx || diffy > spany) {
                    readDoubles = copyCoordinate(coords, dim, readDoubles, currentDoubles);
                }
            } else {
                readDoubles = copyCoordinate(coords, dim, readDoubles, currentDoubles);
            }
        }
        if (g instanceof LinearRing && readDoubles < dim * 4) {
            decimateRingFully(seq);
        } else {
            if (readDoubles < numDoubles) {
                double[] newCoords = new double[readDoubles];
                System.arraycopy(coords, 0, newCoords, 0, readDoubles);
                seq.setArray(newCoords);
            }
        }
    }

    /** */
    private int copyCoordinate(
            double[] coords, int dimension, int readDoubles, int currentDoubles) {
        for (int i = 0; i < dimension; i++) {
            coords[readDoubles + i] = coords[currentDoubles + i];
        }
        readDoubles += dimension;
        return readDoubles;
    }
}
