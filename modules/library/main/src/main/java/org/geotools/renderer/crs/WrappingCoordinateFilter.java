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
package org.geotools.renderer.crs;

import java.util.logging.Level;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;

/**
 * Wraps the coordinates at the discontinuity so that they are always moved towards East (fundamental in order to have
 * all pieces of a complex geometry moved into the same direction).
 *
 * @author Andrea Aime - OpenGeo
 */
class WrappingCoordinateFilter implements GeometryComponentFilter {
    static final int EAST_TO_WEST = 0;

    static final int WEST_TO_EAST = 1;

    static final int NOWRAP = 2;

    final double wrapLimit;

    final double offset;

    final MathTransform mt;

    final int ordinateIdx;

    final boolean isPreFlipped;

    /**
     * Builds a new wrapper
     *
     * @param wrapLimit Subsequent coordinates whose X differ from more than {@code wrapLimit} are supposed to be
     *     wrapping the dateline and need to be offsetted
     * @param offset The offset to be applied to coordinates to unwrap them
     * @param mt The math transform to use to detect the wrapping
     * @param wrapOnY If true, the wrapping is supposed to happen on the Y axis, otherwise on the X
     * @param isPreFlipped If true, the coordinates are already flipped and wrapping is needed
     */
    public WrappingCoordinateFilter(
            double wrapLimit, double offset, MathTransform mt, boolean wrapOnY, boolean isPreFlipped) {
        this.wrapLimit = wrapLimit;
        this.offset = offset;
        this.mt = mt;
        this.ordinateIdx = wrapOnY ? 1 : 0;
        this.isPreFlipped = isPreFlipped;
    }

    @Override
    public void filter(Geometry geom) {
        // this filter will receive either points, which we don't consider,
        // or lines, that we need to wrap
        if (geom instanceof LineString) {
            LineString ls = (LineString) geom;
            CoordinateSequence cs = ls.getCoordinateSequence();
            int direction = getDisconinuityDirection(cs);
            if (direction == NOWRAP) return;

            boolean ring = geom instanceof LinearRing || cs.getCoordinate(0).equals(cs.getCoordinate(cs.size() - 1));
            applyOffset(cs, direction == EAST_TO_WEST ? 0 : wrapLimit * 2, ring, isPreFlipped);
        }
    }

    private int getDisconinuityDirection(CoordinateSequence cs) {
        double lastOrdinate = cs.getOrdinate(0, ordinateIdx);
        for (int i = 0; i < cs.size(); i++) {
            double ordinate = cs.getOrdinate(i, ordinateIdx);
            if (Math.abs(ordinate - lastOrdinate) > wrapLimit) {
                if (ordinate > lastOrdinate) return WEST_TO_EAST;
                else if (ordinate < lastOrdinate) return EAST_TO_WEST;
            }
            lastOrdinate = ordinate;
        }
        return NOWRAP;
    }

    /**
     * Applies the offset to the coordinates
     *
     * @param cs The coordinate sequence to modify
     * @param offset The offset to apply
     * @param ring If true, the sequence is supposed to be a ring
     * @param preFlipped If true, the coordinates are already flipped
     */
    private void applyOffset(CoordinateSequence cs, double offset, boolean ring, boolean preFlipped) {
        final double maxWrap = wrapLimit * 1.9;
        double lastOrdinate = cs.getOrdinate(0, ordinateIdx);
        int last = ring ? cs.size() - 1 : cs.size();
        for (int i = 0; i < last; i++) {
            final double ordinate = cs.getOrdinate(i, ordinateIdx);
            final double distance = Math.abs(ordinate - lastOrdinate);
            // heuristic: an object crossing the dateline is not as big as the world, if it
            // is, it's probably something like Antarctica that does not need coordinate rewrapping,
            // the exception is when the object is already flipped
            if (distance > wrapLimit) {
                boolean wraps = distance < maxWrap || preFlipped;
                // if we fail here, revert to more expensive calculation if
                // we have a reverse transform
                // this is analagous to the technique mentioned here:
                // http://trac.osgeo.org/mapserver/ticket/15
                if (!wraps && mt != null) {
                    // convert back to projected coordinates
                    double[] src;
                    if (ordinateIdx == 0) {
                        src = new double[] {lastOrdinate, cs.getY(i - 1), ordinate, cs.getY(i)};
                    } else {
                        src = new double[] {cs.getX(i - 1), lastOrdinate, cs.getX(i), ordinate};
                    }
                    double[] dest = new double[4];
                    try {
                        mt.transform(src, 0, dest, 0, 2);
                        // find the midpoint coordinate
                        src[0] = Math.min(dest[0], dest[2]) + Math.abs(dest[2] - dest[0]) / 2;
                        src[1] = Math.min(dest[1], dest[3]) + Math.abs(dest[3] - dest[1]) / 2;
                        // and convert back again
                        mt.inverse().transform(src, 0, dest, 0, 1);
                        // if the midpoint isn't between the two end points, it's a wrap
                        wraps = !(dest[ordinateIdx] > Math.min(lastOrdinate, ordinate)
                                && dest[ordinateIdx] < Math.max(lastOrdinate, ordinate));
                    } catch (TransformException ex) {
                        Logging.getLogger(WrappingCoordinateFilter.class)
                                .log(Level.WARNING, "Unable to perform transform to detect dateline wrapping", ex);
                    }
                }
                // toggle between offset
                if (wraps) {
                    if (offset != 0) {
                        offset = 0;
                    } else {
                        offset = wrapLimit * 2;
                    }
                }
            }

            if (offset != 0) cs.setOrdinate(i, ordinateIdx, ordinate + offset);

            lastOrdinate = ordinate;
        }
        if (ring) {
            cs.setOrdinate(last, ordinateIdx, cs.getOrdinate(0, ordinateIdx));
        }
    }
}
