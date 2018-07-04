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
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.CoordinateSequence;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.geom.GeometryComponentFilter;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;

/**
 * Wraps the coordinates at the discontinuity so that they are always moved towards East
 * (fundamental in order to have all pieces of a complex geometry moved into the same direction).
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

    /**
     * Builds a new wrapper
     *
     * @param wrapLimit Subsequent coordinates whose X differ from more than {@code wrapLimit} are
     *     supposed to be wrapping the dateline and need to be offsetted
     * @param offset The offset to be applied to coordinates to unwrap them
     */
    public WrappingCoordinateFilter(
            double wrapLimit, double offset, MathTransform mt, boolean wrapOnY) {
        this.wrapLimit = wrapLimit;
        this.offset = offset;
        this.mt = mt;
        this.ordinateIdx = wrapOnY ? 1 : 0;
    }

    public void filter(Geometry geom) {
        // this filter will receive either points, which we don't consider,
        // or lines, that we need to wrap
        if (geom instanceof LineString) {
            LineString ls = (LineString) geom;
            CoordinateSequence cs = ls.getCoordinateSequence();
            int direction = getDisconinuityDirection(cs);
            if (direction == NOWRAP) return;

            boolean ring =
                    geom instanceof LinearRing
                            || cs.getCoordinate(0).equals(cs.getCoordinate(cs.size() - 1));
            applyOffset(cs, direction == EAST_TO_WEST ? 0 : wrapLimit * 2, ring);
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

    private void applyOffset(CoordinateSequence cs, double offset, boolean ring) {
        final double maxWrap = wrapLimit * 1.9;
        double lastOrdinate = cs.getOrdinate(0, ordinateIdx);
        int last = ring ? cs.size() - 1 : cs.size();
        for (int i = 0; i < last; i++) {
            final double ordinate = cs.getOrdinate(i, ordinateIdx);
            final double distance = Math.abs(ordinate - lastOrdinate);
            // heuristic: an object crossing the dateline is not as big as the world, if it
            // is, it's probably something like Antarctica that does not need coordinate rewrapping
            if (distance > wrapLimit) {
                boolean wraps = distance < maxWrap;
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
                        src = new double[] {cs.getX(i - i), lastOrdinate, cs.getX(i), ordinate};
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
                        wraps =
                                !(dest[ordinateIdx] > Math.min(lastOrdinate, ordinate)
                                        && dest[ordinateIdx] < Math.max(lastOrdinate, ordinate));
                    } catch (TransformException ex) {
                        Logging.getLogger("org.geotools.rendering")
                                .log(
                                        Level.WARNING,
                                        "Unable to perform transform to detect dateline wrapping",
                                        ex);
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
