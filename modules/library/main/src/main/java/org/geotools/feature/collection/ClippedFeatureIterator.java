/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008-2011 TOPP - www.openplans.org.
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

package org.geotools.feature.collection;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.logging.Logger;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.AttributeDescriptor;
import org.geotools.api.feature.type.GeometryDescriptor;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.crs.GeographicCRS;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.geometry.jts.GeometryClipper;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.renderer.crs.GeometryDimensionCollector;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.algorithm.LineIntersector;
import org.locationtech.jts.algorithm.RobustLineIntersector;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.CoordinateArrays;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.CoordinateSequenceFilter;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.LineSegment;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.impl.CoordinateArraySequence;
import org.locationtech.jts.geom.util.GeometryEditor;

/**
 * SimpleFeatureIterator wrapper that clip (crops) features according to the clip geometry passed. Can preserve the Z
 * dimension.
 *
 * @author Andrea Aime - GeoSolutions
 */
public class ClippedFeatureIterator implements SimpleFeatureIterator {

    static final Logger LOGGER = Logging.getLogger(ClippedFeatureIterator.class);

    protected SimpleFeatureIterator delegate;

    GeometryClipper clipper;

    boolean preserveTopology;

    protected SimpleFeatureBuilder fb;

    protected SimpleFeature next;

    protected Geometry clip;

    boolean preserveZ;

    public ClippedFeatureIterator(
            SimpleFeatureIterator delegate, Geometry clip, SimpleFeatureType schema, boolean preserveZ) {
        this.delegate = delegate;

        // can we use the fast clipper?
        if (clip.getEnvelope().equals(clip)) {
            this.clipper = new GeometryClipper(clip.getEnvelopeInternal());
        } else {
            this.clip = clip;
        }

        fb = new SimpleFeatureBuilder(schema);
        this.preserveZ = preserveZ;
    }

    @Override
    public void close() {
        delegate.close();
    }

    @Override
    public boolean hasNext() {
        while (next == null && delegate.hasNext()) {
            // try building the clipped feature out of the original feature, if the
            // default geometry is clipped out, skip it
            SimpleFeature f = delegate.next();
            boolean clippedOut = prepareBuilderForNextFeature(f);

            if (!clippedOut) {
                // build the next feature
                next = fb.buildFeature(f.getID());
            }
            fb.reset();
        }

        return next != null;
    }

    protected boolean prepareBuilderForNextFeature(SimpleFeature f) {
        boolean clippedOut = false;
        for (AttributeDescriptor ad : f.getFeatureType().getAttributeDescriptors()) {
            Object attribute = f.getAttribute(ad.getName());
            if (ad instanceof GeometryDescriptor) {
                attribute =
                        clipGeometry((Geometry) attribute, ((GeometryDescriptor) ad).getCoordinateReferenceSystem());
                if (attribute == null && f.getFeatureType().getGeometryDescriptor() == ad) {
                    // the feature has been clipped out
                    fb.reset();
                    clippedOut = true;
                    break;
                }
            }
            fb.add(attribute);
        }
        return clippedOut;
    }

    @Override
    public SimpleFeature next() throws NoSuchElementException {
        if (!hasNext()) {
            throw new NoSuchElementException("hasNext() returned false!");
        }

        SimpleFeature result = next;
        next = null;
        return result;
    }

    private Object clipGeometry(Geometry geom, CoordinateReferenceSystem crs) {
        // first off, clip
        Geometry clipped = null;
        if (clipper != null) {
            clipped = clipper.clip(geom, true);
        } else {
            if (geom.getEnvelopeInternal().intersects(clip.getEnvelopeInternal())) {
                clipped = clip.intersection(geom);
            }
        }

        if (clipped == null) {
            return null;
        }

        // empty intersection?
        GeometryDimensionCollector collector = new GeometryDimensionCollector(geom.getDimension());
        clipped.apply(collector);
        Geometry result = collector.collect();
        if (result == null) {
            return null;
        }

        // manage Z preservation
        if (preserveZ && !geom.equalsExact(clipped)) {
            // Check if the clipped geometry has 3 ordinates across entire area.
            GeometryEditor editor = new GeometryEditor();
            result = editor.edit(result, new GeometryEditor.CoordinateOperation() {
                @Override
                public Coordinate[] edit(Coordinate[] coordinates, Geometry geometry) {
                    return CoordinateArrays.enforceConsistency(coordinates, 3, 0);
                }
            });
            // for polygons we need to go idw, for points and multipoints idw will do and will
            // not
            // add much overhead (it has optimizations for points that were already in the
            // input)
            if (result.getDimension() == 2 || result.getDimension() == 0) {
                result.apply(new IDWElevationInterpolator(geom, crs));
            } else if (result.getDimension() == 1) {
                result.apply(new LinearElevationInterpolator(geom, crs));
            }
        }

        return result;
    }

    protected boolean hasElevations(CoordinateSequence seq) {
        return seq instanceof CoordinateArraySequence
                        && !Double.isNaN(seq.getCoordinate(0).getZ())
                || !(seq instanceof CoordinateArraySequence) && seq.getDimension() > 2;
    }

    /**
     * An interpolator that will copy over the Z from the original points where possible, and will use the IDW
     * Interpolation algorithm for the missing ones
     *
     * @author Andrea Aime - GeoSolutions
     */
    private class IDWElevationInterpolator implements CoordinateSequenceFilter {

        private static final int MAX_POINTS = 12;
        private final int scale;
        private final List<PointDistance> elevations;
        private final CoordinateReferenceSystem crs;

        public IDWElevationInterpolator(Geometry geom, CoordinateReferenceSystem crs) {
            this.elevations = gatherElevationPointCloud(geom);
            this.scale = crs instanceof GeographicCRS ? 9 : 6;
            this.crs = crs;
        }

        List<PointDistance> gatherElevationPointCloud(Geometry geom) {
            final List<PointDistance> results = new ArrayList<>();
            geom.apply(new CoordinateSequenceFilter() {

                @Override
                public boolean isGeometryChanged() {
                    return false;
                }

                @Override
                public boolean isDone() {
                    return false;
                }

                @Override
                public void filter(CoordinateSequence seq, int i) {
                    // we do all the collecting when called for the first ordinate
                    if (i > 0) {
                        return;
                    }
                    // collects only points with a Z
                    if (hasElevations(seq)) {
                        Coordinate[] coords = seq.toCoordinateArray();
                        for (int j = 0; j < coords.length; j++) {
                            Coordinate c = coords[j];
                            // avoid adding the last element of a ring to avoid
                            // un-balancing the
                            // weights (the fist/last coordinate would be counted twice)
                            if ((j < coords.length - 1 || !c.equals(coords[0])) && !Double.isNaN(c.getZ())) {
                                results.add(new PointDistance(c));
                            }
                        }
                    }
                }
            });

            if (results.isEmpty()) {
                return null;
            } else {
                return results;
            }
        }

        @Override
        public boolean isGeometryChanged() {
            return true;
        }

        @Override
        public boolean isDone() {
            return elevations == null;
        }

        @Override
        public void filter(CoordinateSequence seq, int i) {
            if (elevations == null) {
                return;
            }

            if (seq.getDimension() < 3) {
                throw new IllegalArgumentException(
                        "Expecting a 3 dimensional coordinate sequence to re-apply the Z values");
            }
            double x = seq.getX(i);
            double y = seq.getY(i);

            for (PointDistance pd : elevations) {
                double distance = pd.updateDistance(x, y, crs);
                // did we match an existing point?
                if (distance < PointDistance.EPS_METERS) {
                    // copy over the z and bail out
                    seq.setOrdinate(i, 2, pd.c.getZ());
                    return;
                }
            }

            // ok, we need to apply the IDW interpolation for this point
            Collections.sort(elevations);
            double sum = 0;
            double weights = 0;
            final int usedPoints = Math.min(MAX_POINTS, elevations.size());
            for (int j = 0; j < usedPoints; j++) {
                PointDistance pd = elevations.get(j);
                sum += pd.c.getZ() / pd.squareDistance;
                weights += 1 / pd.squareDistance;
            }
            double z = sum / weights;
            // apply safe rounding to avoid numerical issues with the above calculation due
            // to the weights being, often, very small numbers
            BigDecimal bd = BigDecimal.valueOf(z);
            double rz = bd.setScale(scale, RoundingMode.HALF_EVEN).doubleValue();
            seq.setOrdinate(i, 2, rz);
        }
    }

    /**
     * Helper that keeps in the same container the point and its distance to a certain reference point. Allows for
     * sorting on said distance
     */
    static final class PointDistance implements Comparable<PointDistance> {
        static final double EPS_METERS = 1e-6;
        static final double EPS_DEGREES = 1e-9;

        Coordinate c;
        double squareDistance;

        public PointDistance(Coordinate c) {
            this.c = c;
        }

        public boolean isSame(double x, double y, CoordinateReferenceSystem crs) {
            double tolerance = crs instanceof GeographicCRS ? EPS_DEGREES : EPS_METERS;
            return Math.abs(c.x - x) < tolerance && Math.abs(c.x - y) < tolerance;
        }

        public double updateDistance(double x, double y, CoordinateReferenceSystem crs) {
            if (crs instanceof DefaultGeographicCRS) {
                double d = ((DefaultGeographicCRS) crs)
                        .distance(new double[] {c.x, c.y}, new double[] {x, y})
                        .doubleValue();
                this.squareDistance = d * d;
            } else {
                double dx = c.x - x;
                double dy = c.y - y;
                this.squareDistance = dx * dx + dy * dy;
            }
            return this.squareDistance;
        }

        @Override
        public int compareTo(PointDistance o) {
            return (int) Math.signum(this.squareDistance - o.squareDistance);
        }

        @Override
        public String toString() {
            return "[" + c.x + " " + c.y + " " + c.getZ() + "] - " + squareDistance;
        }
    }

    private class LinearElevationInterpolator implements GeometryComponentFilter {

        private final ArrayList<LineString> originalLines;

        @SuppressWarnings("UnusedVariable") // crs
        public LinearElevationInterpolator(Geometry original, CoordinateReferenceSystem crs) {
            originalLines = new ArrayList<>();
            original.apply((GeometryComponentFilter) geom -> {
                if (geom instanceof LineString) {
                    originalLines.add((LineString) geom);
                }
            });
        }

        @Override
        public void filter(Geometry geom) {
            if (geom instanceof LineString) {
                LineString ls = (LineString) geom;

                // look for the original line containing this one
                LineString original = getOriginator(ls);
                if (original == null) {
                    LOGGER.log(
                            java.util.logging.Level.WARNING,
                            "Could not find the original line from which the output line " + geom + " originated");
                    return;
                }

                try {
                    applyElevations(ls, original, false);
                } catch (ClippingException e) {
                    // fine, let's try with the more expensive way then
                    applyElevations(ls, original, true);
                }
            }
        }

        private void applyElevations(LineString ls, LineString original, boolean tolerant) {
            // do we have any elevation to bring on the new line?
            if (!hasElevations(original.getCoordinateSequence())) {
                return;
            }

            // let's do a synched scan of the two lines
            CoordinateSequence cs = ls.getCoordinateSequence();
            CoordinateSequence csOrig = original.getCoordinateSequence();
            Coordinate c1 = cs.getCoordinate(0);
            Coordinate c2 = cs.getCoordinate(1);
            int localIdx = 0;
            Coordinate o1 = csOrig.getCoordinate(0);
            Coordinate o2 = csOrig.getCoordinate(1);
            int origIdx = 0;
            RobustLineIntersector intersector = new RobustLineIntersector();
            int matched = 0;
            boolean flipped = false;
            // search the fist segment of the origin that contains the first segment of the
            // local
            for (; ; ) {
                intersector.computeIntersection(c1, c2, o1, o2);
                int intersectionNum = intersector.getIntersectionNum();
                // doing these checks for any non collinear point is expensive, do it only if
                // there is at least one intersection
                // or if we're in tolerant mode
                if (intersectionNum == LineIntersector.POINT_INTERSECTION
                        || tolerant && intersectionNum != LineIntersector.COLLINEAR) {
                    // this one might be due to a numerical issue, where the two lines to do
                    // intersect
                    // exactly, but almost. Let's compute the distance and see
                    LineSegment segment = new LineSegment(o1, o2);
                    double d1 = segment.distance(c1);
                    double d2 = segment.distance(c2);
                    if (d1 <= PointDistance.EPS_METERS && d2 <= PointDistance.EPS_METERS) {
                        intersectionNum = LineIntersector.COLLINEAR;
                    }
                }
                if (intersectionNum == LineIntersector.COLLINEAR) {
                    matched++;
                    applyZValues(cs, localIdx, csOrig, origIdx);
                    localIdx++;
                    if (localIdx == cs.size() - 1) {
                        // we reached the end, apply the Z also on the second ordinate
                        // of the segment and exit
                        applyZValues(cs, localIdx, csOrig, origIdx);
                        break;
                    } else {
                        c1 = c2;
                        c2 = cs.getCoordinate(localIdx + 1);
                    }
                } else {
                    origIdx++;
                    if (origIdx >= csOrig.size() - 1) {
                        if (!flipped) {
                            // it may be that the two lines have different orientations, we'll
                            // flip ls and
                            // start back
                            ls = ls.reverse();
                            cs = ls.getCoordinateSequence();
                            flipped = true;
                            c1 = cs.getCoordinate(0);
                            c2 = cs.getCoordinate(1);
                            localIdx = 0;
                            o1 = csOrig.getCoordinate(0);
                            o2 = csOrig.getCoordinate(1);
                            origIdx = 0;
                        } else {
                            throw new ClippingException("Could not find collinear segments between "
                                    + ls.toText()
                                    + "\n and \n"
                                    + original.toText()
                                    + "\n after matching "
                                    + matched
                                    + " points");
                        }
                    } else {
                        o1 = o2;
                        o2 = csOrig.getCoordinate(origIdx + 1);
                    }
                }
            }
        }

        private void applyZValues(CoordinateSequence cs, int idx, CoordinateSequence csOrig, int origIdx) {

            if (!cs.hasZ()) return;

            double lx1 = cs.getOrdinate(idx, 0);
            double ly1 = cs.getOrdinate(idx, 1);
            double lz1;

            double ox1 = csOrig.getX(origIdx);
            double oy1 = csOrig.getY(origIdx);
            double oz1 = csOrig.getZ(origIdx);
            double ox2 = csOrig.getX(origIdx + 1);
            double oy2 = csOrig.getY(origIdx + 1);
            double oz2 = csOrig.getZ(origIdx + 1);

            if (lx1 == ox1 && ly1 == oy1) {
                lz1 = oz1;
            } else {
                double d1 = distance(ox1, oy1, lx1, ly1);
                double d = distance(ox1, oy1, ox2, oy2);
                lz1 = oz1 + (oz2 - oz1) * (d1 / d);
            }

            cs.setOrdinate(idx, 2, lz1);
        }

        private double distance(double x1, double y1, double x2, double y2) {
            double dx = x1 - x2;
            double dy = y1 - y2;
            return Math.sqrt(dx * dx + dy * dy);
        }

        /**
         * TODO: should we use a spatial index? Would be warranted only if the input has a very large amount of
         * sub-lines
         */
        private LineString getOriginator(LineString ls) {
            LineString original = null;
            for (LineString ol : originalLines) {
                if (ls.equals(ol) || ls.overlaps(ol) || ol.contains(ls)) {
                    original = ol;
                    break;
                }
            }
            if (original == null) {
                // sigh, there might be a small difference in the coordinate values,
                // go for a more expensive, but tolerant search
                for (LineString ol : originalLines) {
                    if (ol.buffer(PointDistance.EPS_METERS).contains(ls)) {
                        original = ol;
                        break;
                    }
                }
            }
            return original;
        }
    }

    static class ClippingException extends RuntimeException {

        /** */
        private static final long serialVersionUID = -1373822214375482149L;

        public ClippingException() {
            super();
        }

        public ClippingException(String message, Throwable cause) {
            super(message, cause);
        }

        public ClippingException(String message) {
            super(message);
        }

        public ClippingException(Throwable cause) {
            super(cause);
        }
    }
}
