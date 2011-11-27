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
import com.vividsolutions.jts.geom.LinearRing;

import java.util.logging.Level;
import org.geotools.util.logging.Logging;
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

    /**
     * Builds a new wrapper
     * 
     * @param wrapLimit
     *            Subsequent coordinates whose X differ from more than {@code wrapLimit} are
     *            supposed to be wrapping the dateline and need to be offsetted
     * @param offset
     *            The offset to be applied to coordinates to unwrap them
     */
    public WrappingCoordinateFilter(double wrapLimit, double offset, MathTransform mt) {
        this.wrapLimit = wrapLimit;
        this.offset = offset;
        this.mt = mt;
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
            
            boolean ring = geom instanceof LinearRing || cs.getCoordinate(0).equals(cs.getCoordinate(cs.size() - 1));
            applyOffset(cs, direction == EAST_TO_WEST ? 0 : wrapLimit * 2, ring);
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

    private void applyOffset(CoordinateSequence cs, double offset, boolean ring) {
        final double maxWrap = wrapLimit * 1.9;
        double lastX = cs.getX(0);
        int last = ring ? cs.size() - 1 : cs.size(); 
        for (int i = 0; i < last; i++) {
            final double x = cs.getX(i);
            final double distance = Math.abs(x - lastX);
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
                    double[] src = new double[]{lastX, cs.getY(i - 1), x, cs.getY(i)};
                    double[] dest = new double[4];
                    try {
                        mt.transform(src, 0, dest, 0, 2);
                        // find the midpoint coordinate
                        src[0] = Math.min(dest[0], dest[2]) + Math.abs(dest[2] - dest[0]) / 2;
                        src[1] = Math.min(dest[1], dest[3]) + Math.abs(dest[3] - dest[1]) / 2;
                        // and convert back again
                        mt.inverse().transform(src, 0, dest, 0, 1);
                        // if the midpoint isn't between the two end points, it's a wrap
                        wraps = !(dest[0] > Math.min(lastX, x) && dest[0] < Math.max(lastX, x));
                    } catch (TransformException ex) {
                        Logging.getLogger("org.geotools.rendering").log(Level.WARNING,
                                "Unable to perform transform to detect dateline wrapping", ex);
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

            if (offset != 0)
                cs.setOrdinate(i, 0, x + offset);
            
            lastX = x;
        }
        if(ring) {
            cs.setOrdinate(last, 0, cs.getOrdinate(0, 0));
        }
    }
    
}