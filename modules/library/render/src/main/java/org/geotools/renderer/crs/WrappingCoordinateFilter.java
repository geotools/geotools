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

import com.vividsolutions.jts.geom.CoordinateSequence;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryComponentFilter;
import com.vividsolutions.jts.geom.LineString;

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

    double wrapLimit;

    double offset;

    /**
     * Builds a new wrapper
     * 
     * @param wrapLimit
     *            Subsequent coordinates whose X differ from more than {@code wrapLimit} are
     *            supposed to be wrapping the dateline and need to be offsetted
     * @param offset
     *            The offset to be applied to coordinates to unwrap them
     */
    public WrappingCoordinateFilter(double wrapLimit, double offset) {
        this.wrapLimit = wrapLimit;
        this.offset = offset;
    }

    public void filter(Geometry geom) {
        // this filter will receive either points, which we don't consider,
        // or lines, that we need to wrap
        if (geom instanceof LineString) {
            LineString ls = (LineString) geom;
            CoordinateSequence cs = ls.getCoordinateSequence();
            int direction = getDisconinuityDirection(cs);
            if (direction == NOWRAP)
                return;

            applyOffset(cs, direction == EAST_TO_WEST ? 0 : wrapLimit * 2);
        }
    }

    private int getDisconinuityDirection(CoordinateSequence cs) {
        double lastX = cs.getX(0);
        for (int i = 0; i < cs.size(); i++) {
            double x = cs.getX(i);
            if (Math.abs(x - lastX) > wrapLimit) {
                if (x > lastX)
                    return WEST_TO_EAST;
                else if (x < lastX)
                    return EAST_TO_WEST;
            }
            lastX = x;
        }
        return NOWRAP;
    }

    private void applyOffset(CoordinateSequence cs, double offset) {
        double lastX = cs.getX(0);
        for (int i = 0; i < cs.size(); i++) {
            double x = cs.getX(i);
            final double distance = Math.abs(x - lastX);
            // heuristic: an object crossing the dateline is not as big as the world, if it
            // is, it's probably something like Antarctica that does not need coordinate rewrapping
            if (distance > wrapLimit && distance < wrapLimit * 1.9) {
                if (offset != 0)
                    offset = 0;
                else
                    offset = wrapLimit * 2;
            }
            if (offset != 0)
                cs.setOrdinate(i, 0, x + offset);
            lastX = x;
        }
    }

}