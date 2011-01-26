/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2008, Open Source Geospatial Foundation (OSGeo)
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
 *
 *    This package contains documentation from OpenGIS specifications.
 *    OpenGIS consortium's work is fully acknowledged here.
 */
package org.geotools.referencing.datum;

import java.util.Map;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;


/**
 * A ellipsoid which is spherical. This ellipsoid implements a faster
 * {@link #orthodromicDistance} method.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 *
 * @since 2.0
 */
final class Spheroid extends DefaultEllipsoid {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = 7867565381280669821L;

    /**
     * Constructs a new sphere using the specified radius.
     *
     * @param properties    Set of properties. Should contains at least {@code "name"}.
     * @param radius        The equatorial and polar radius.
     * @param ivfDefinitive {@code true} if the inverse flattening is definitive.
     * @param unit          The units of the radius value.
     */
    protected Spheroid(Map<String,?> properties, double radius, boolean ivfDefinitive, Unit<Length> unit) {
        super(properties, check("radius", radius), radius, Double.POSITIVE_INFINITY, ivfDefinitive, unit);
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates.
     * The orthodromic distance is the shortest distance between two points
     * on a sphere's surface. The orthodromic path is always on a great circle.
     *
     * @param  x1 Longitude of first point (in decimal degrees).
     * @param  y1 Latitude of first point (in decimal degrees).
     * @param  x2 Longitude of second point (in decimal degrees).
     * @param  y2 Latitude of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid's axis).
     */
    @Override
    public double orthodromicDistance(double x1, double y1, double x2, double y2) {
        /*
         * The calculation of orthodromic distance on an ellipsoidal surface is complex,
         * subject to rounding errors and has no solution near the poles. In some situation
         * we use a calculation based on a spherical shape of the earth.  A Fortran program
         * which calculates orthodromic distances on an ellipsoidal surface can be downloaded
         * from the NOAA site:
         *
         *            ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/
         */
        y1 = Math.toRadians(y1);
        y2 = Math.toRadians(y2);
        final double dx = Math.toRadians(Math.abs(x2-x1) % 360);
        double rho = Math.sin(y1)*Math.sin(y2) + Math.cos(y1)*Math.cos(y2)*Math.cos(dx);
        assert Math.abs(rho) < 1.0000001 : rho;
        if (rho>+1) rho=+1; // Catch rounding error.
        if (rho<-1) rho=-1; // Catch rounding error.
        final double distance = Math.acos(rho)*getSemiMajorAxis();
        /*
         * Compare the distance with the orthodromic distance using ellipsoidal
         * computation. This should be close to the same.
         */
        try {
            double delta;
            assert (delta = Math.abs(super.orthodromicDistance(x1, Math.toDegrees(y1),
                                                               x2, Math.toDegrees(y2))-distance))
                                                               < getSemiMajorAxis()/1E+9 : delta;
        } catch (ArithmeticException exception) {
            // The ellipsoidal model do not converge. Give up the assertion test.
            // Note: the assertion fails for illegal latitudes (i.e. abs(y1)>90°
            //       or abs(y2)>90°).
        }
        return distance;
    }
}
