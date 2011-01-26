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
 *    This class contains formulas from the public FTP area of NOAA.
 *    NOAAS's work is fully acknowledged here.
 */
package org.geotools.referencing.datum;

import java.awt.geom.Point2D;
import java.util.Collections;
import java.util.Map;
import javax.measure.unit.SI;
import javax.measure.unit.Unit;
import javax.measure.quantity.Length;

import org.opengis.referencing.datum.Ellipsoid;

import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.measure.CoordinateFormat;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;


/**
 * Geometric figure that can be used to describe the approximate shape of the earth.
 * In mathematical terms, it is a surface formed by the rotation of an ellipse about
 * its minor axis. An ellipsoid requires two defining parameters:
 * <ul>
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and
 *       {@linkplain #getInverseFlattening inverse flattening}, or</li>
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and
 *       {@linkplain #getSemiMinorAxis semi-minor axis}.</li>
 * </ul>
 *
 * @since 2.1
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultEllipsoid extends AbstractIdentifiedObject implements Ellipsoid {
    /**
     * Serial number for interoperability with different versions.
     */
    private static final long serialVersionUID = -1149451543954764081L;

    /**
     * WGS 1984 ellipsoid with axis in {@linkplain SI#METER metres}. This ellipsoid is used
     * in GPS systems and is the default for most {@code org.geotools} packages.
     */
    public static final DefaultEllipsoid WGS84 =
            createFlattenedSphere("WGS84", 6378137.0, 298.257223563, SI.METER);

    /**
     * GRS 80 ellipsoid with axis in {@linkplain SI#METER metres}.
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid GRS80 =
            createFlattenedSphere("GRS80", 6378137.0, 298.257222101, SI.METER);

    /**
     * International 1924 ellipsoid with axis in {@linkplain SI#METER metres}.
     */
    public static final DefaultEllipsoid INTERNATIONAL_1924 =
            createFlattenedSphere("International 1924", 6378388.0, 297.0, SI.METER);

    /**
     * Clarke 1866 ellipsoid with axis in {@linkplain SI#METER metres}.
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid CLARKE_1866 =
            createFlattenedSphere("Clarke 1866", 6378206.4, 294.9786982, SI.METER);

    /**
     * A sphere with a radius of 6371000 {@linkplain SI#METER metres}. Spheres use a simplier
     * algorithm for {@linkplain #orthodromicDistance orthodromic distance computation}, which
     * may be faster and more robust.
     */
    public static final DefaultEllipsoid SPHERE =
            createEllipsoid("SPHERE", 6371000, 6371000, SI.METER);

    /**
     * The equatorial radius.
     * @see #getSemiMajorAxis
     */
    private final double semiMajorAxis;

    /**
     * The polar radius.
     * @see #getSemiMinorAxis
     */
    private final double semiMinorAxis;

    /**
     * The inverse of the flattening value, or {@link Double#POSITIVE_INFINITY}
     * if the ellipsoid is a sphere.
     *
     * @see #getInverseFlattening
     */
    private final double inverseFlattening;

    /**
     * Tells if the Inverse Flattening definitive for this ellipsoid.
     *
     * @see #isIvfDefinitive
     */
    private final boolean ivfDefinitive;

    /**
     * The units of the semi-major and semi-minor axis values.
     */
    private final Unit<Length> unit;

    /**
     * Constructs a new ellipsoid with the same values than the specified one.
     * This copy constructor provides a way to wrap an arbitrary implementation into a
     * Geotools one or a user-defined one (as a subclass), usually in order to leverage
     * some implementation-specific API. This constructor performs a shallow copy,
     * i.e. the properties are not cloned.
     *
     * @param ellipsoid The ellipsoid to copy.
     *
     * @since 2.2
     *
     * @see #wrap
     */
    protected DefaultEllipsoid(final Ellipsoid ellipsoid) {
        super(ellipsoid);
        semiMajorAxis     = ellipsoid.getSemiMajorAxis();
        semiMinorAxis     = ellipsoid.getSemiMinorAxis();
        inverseFlattening = ellipsoid.getInverseFlattening();
        ivfDefinitive     = ellipsoid.isIvfDefinitive();
        unit              = ellipsoid.getAxisUnit();
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is
     * given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties        Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis     The equatorial radius.
     * @param semiMinorAxis     The polar radius.
     * @param inverseFlattening The inverse of the flattening value.
     * @param ivfDefinitive     {@code true} if the inverse flattening is definitive.
     * @param unit              The units of the semi-major and semi-minor axis values.
     *
     * @see #createEllipsoid
     * @see #createFlattenedSphere
     */
    protected DefaultEllipsoid(final Map<String,?> properties,
                               final double  semiMajorAxis,
                               final double  semiMinorAxis,
                               final double  inverseFlattening,
                               final boolean ivfDefinitive,
                               final Unit<Length> unit)
    {
        super(properties);
        this.unit = unit;
        this.semiMajorAxis     = check("semiMajorAxis",     semiMajorAxis);
        this.semiMinorAxis     = check("semiMinorAxis",     semiMinorAxis);
        this.inverseFlattening = check("inverseFlattening", inverseFlattening);
        this.ivfDefinitive     = ivfDefinitive;
        ensureNonNull("unit", unit);
        ensureLinearUnit(unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length.
     *
     * @param name          The ellipsoid name.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit          The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(final String name,
                                                   final double semiMajorAxis,
                                                   final double semiMinorAxis,
                                                   final Unit<Length> unit)
    {
        return createEllipsoid(Collections.singletonMap(NAME_KEY, name),
                               semiMajorAxis, semiMinorAxis, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is
     * given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map)
     * super-class constructor}.
     *
     * @param properties    Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit          The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(final Map<String,?> properties,
                                                   final double semiMajorAxis,
                                                   final double semiMinorAxis,
                                                   final Unit<Length> unit)
    {
        if (semiMajorAxis == semiMinorAxis) {
            return new Spheroid(properties, semiMajorAxis, false, unit);
        } else {
            return new DefaultEllipsoid(properties, semiMajorAxis, semiMinorAxis,
                       semiMajorAxis/(semiMajorAxis-semiMinorAxis), false, unit);
        }
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and inverse flattening value.
     *
     * @param name              The ellipsoid name.
     * @param semiMajorAxis     The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit              The units of the semi-major and semi-minor axis
     *                          values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(final String name,
                                                         final double semiMajorAxis,
                                                         final double inverseFlattening,
                                                         final Unit<Length> unit)
    {
        return createFlattenedSphere(Collections.singletonMap(NAME_KEY, name),
                                     semiMajorAxis, inverseFlattening, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and
     * inverse flattening value. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties        Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis     The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit              The units of the semi-major and semi-minor axis
     *                          values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(final Map<String,?> properties,
                                                         final double semiMajorAxis,
                                                         final double inverseFlattening,
                                                         final Unit<Length> unit)
    {
        if (Double.isInfinite(inverseFlattening)) {
            return new Spheroid(properties, semiMajorAxis, true, unit);
        } else {
            return new DefaultEllipsoid(properties, semiMajorAxis,
                                        semiMajorAxis*(1-1/inverseFlattening),
                                        inverseFlattening, true, unit);
        }
    }

    /**
     * Wraps an arbitrary ellipsoid into a Geotools implementation. This method is usefull if
     * {@link #orthodromicDistance orthodromic distance computation} (for example) are desired.
     * If the supplied ellipsoid is already an instance of {@code DefaultEllipsoid} or is
     * {@code null}, then it is returned unchanged.
     *
     * @param ellipsoid The ellipsoid to wrap.
     * @return The given ellipsoid as a {@code DefaultEllipsoid} instance.
     */
    public static DefaultEllipsoid wrap(final Ellipsoid ellipsoid) {
        if (ellipsoid==null || ellipsoid instanceof DefaultEllipsoid) {
            return (DefaultEllipsoid) ellipsoid;
        }
        if (ellipsoid.isIvfDefinitive()) {
            return createFlattenedSphere(getProperties(ellipsoid),
                                         ellipsoid.getSemiMajorAxis(),
                                         ellipsoid.getInverseFlattening(),
                                         ellipsoid.getAxisUnit());
        } else {
            return createEllipsoid(getProperties(ellipsoid),
                                   ellipsoid.getSemiMajorAxis(),
                                   ellipsoid.getSemiMinorAxis(),
                                   ellipsoid.getAxisUnit());
        }
    }

    /**
     * Checks the argument validity. Argument {@code value} should be greater than zero.
     *
     * @param  name  Argument name.
     * @param  value Argument value.
     * @return {@code value}.
     * @throws IllegalArgumentException if {@code value} is not greater than  0.
     */
    static double check(final String name, final double value) throws IllegalArgumentException {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value));
    }

    /**
     * Returns the linear unit of the {@linkplain #getSemiMajorAxis semi-major}
     * and {@linkplain #getSemiMinorAxis semi-minor} axis values.
     *
     * @return The axis linear unit.
     */
    public Unit<Length> getAxisUnit() {
        return unit;
    }

    /**
     * Length of the semi-major axis of the ellipsoid. This is the
     * equatorial radius in {@linkplain #getAxisUnit axis linear unit}.
     *
     * @return Length of semi-major axis.
     */
    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Length of the semi-minor axis of the ellipsoid. This is the
     * polar radius in {@linkplain #getAxisUnit axis linear unit}.
     *
     * @return Length of semi-minor axis.
     */
    public double getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * The ratio of the distance between the center and a focus of the ellipse
     * to the length of its semimajor axis. The eccentricity can alternately be
     * computed from the equation: <code>e=sqrt(2f-f²)</code>.
     *
     * @return The eccentricity of this ellipsoid.
     */
    public double getEccentricity() {
        final double f = 1-getSemiMinorAxis()/getSemiMajorAxis();
        return Math.sqrt(2*f - f*f);
    }

    /**
     * Returns the value of the inverse of the flattening constant. Flattening is a value
     * used to indicate how closely an ellipsoid approaches a spherical shape. The inverse
     * flattening is related to the equatorial/polar radius by the formula
     *
     * <var>ivf</var>&nbsp;=&nbsp;<var>r</var><sub>e</sub>/(<var>r</var><sub>e</sub>-<var>r</var><sub>p</sub>).
     *
     * For perfect spheres (i.e. if {@link #isSphere} returns {@code true}),
     * the {@link Double#POSITIVE_INFINITY} value is used.
     *
     * @return The inverse flattening value.
     */
    public double getInverseFlattening() {
        return inverseFlattening;
    }

    /**
     * Indicates if the {@linkplain #getInverseFlattening inverse flattening} is definitive for
     * this ellipsoid. Some ellipsoids use the IVF as the defining value, and calculate the polar
     * radius whenever asked. Other ellipsoids use the polar radius to calculate the IVF whenever
     * asked. This distinction can be important to avoid floating-point rounding errors.
     *
     * @return {@code true} if the {@linkplain #getInverseFlattening inverse flattening} is
     *         definitive, or {@code false} if the {@linkplain #getSemiMinorAxis polar radius}
     *         is definitive.
     */
    public boolean isIvfDefinitive() {
        return ivfDefinitive;
    }

    /**
     * {@code true} if the ellipsoid is degenerate and is actually a sphere. The sphere is
     * completely defined by the {@linkplain #getSemiMajorAxis semi-major axis}, which is the
     * radius of the sphere.
     *
     * @return {@code true} if the ellipsoid is degenerate and is actually a sphere.
     */
    public boolean isSphere() {
        return semiMajorAxis == semiMinorAxis;
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates.
     * The orthodromic distance is the shortest distance between two points
     * on a sphere's surface. The default implementation delegates the work
     * to {@link #orthodromicDistance(double,double,double,double)}.
     *
     * @param  P1 Longitude and latitude of first point (in decimal degrees).
     * @param  P2 Longitude and latitude of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid).
     */
    public double orthodromicDistance(final Point2D P1, final Point2D P2) {
        return orthodromicDistance(P1.getX(), P1.getY(), P2.getX(), P2.getY());
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates.
     * The orthodromic distance is the shortest distance between two points
     * on a sphere's surface. The orthodromic path is always on a great circle.
     * This is different from the <cite>loxodromic distance</cite>, which is a
     * longer distance on a path with a constant direction on the compass.
     *
     * @param  x1 Longitude of first  point (in decimal degrees).
     * @param  y1 Latitude  of first  point (in decimal degrees).
     * @param  x2 Longitude of second point (in decimal degrees).
     * @param  y2 Latitude  of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid's axis).
     */
    public double orthodromicDistance(double x1, double y1, double x2, double y2) {
        x1 = Math.toRadians(x1);
        y1 = Math.toRadians(y1);
        x2 = Math.toRadians(x2);
        y2 = Math.toRadians(y2);
        /*
         * Solution of the geodetic inverse problem after T.Vincenty.
         * Modified Rainsford's method with Helmert's elliptical terms.
         * Effective in any azimuth and at any distance short of antipodal.
         *
         * Latitudes and longitudes in radians positive North and East.
         * Forward azimuths at both points returned in radians from North.
         *
         * Programmed for CDC-6600 by LCDR L.Pfeifer NGS ROCKVILLE MD 18FEB75
         * Modified for IBM SYSTEM 360 by John G.Gergen NGS ROCKVILLE MD 7507
         * Ported from Fortran to Java by Martin Desruisseaux.
         *
         * Source: ftp://ftp.ngs.noaa.gov/pub/pcsoft/for_inv.3d/source/inverse.for
         *         subroutine INVER1
         */
        final int    MAX_ITERATIONS = 100;
        final double EPS = 0.5E-13;
        final double F   = 1/getInverseFlattening();
        final double R   = 1-F;

        double tu1 = R * Math.sin(y1) / Math.cos(y1);
        double tu2 = R * Math.sin(y2) / Math.cos(y2);
        double cu1 = 1 / Math.sqrt(tu1*tu1 + 1);
        double cu2 = 1 / Math.sqrt(tu2*tu2 + 1);
        double su1 = cu1*tu1;
        double s   = cu1*cu2;
        double baz = s*tu2;
        double faz = baz*tu1;
        double x   = x2-x1;
        for (int i=0; i<MAX_ITERATIONS; i++) {
            final double sx = Math.sin(x);
            final double cx = Math.cos(x);
            tu1 = cu2*sx;
            tu2 = baz - su1*cu2*cx;
            final double sy = Math.hypot(tu1, tu2);
            final double cy = s*cx + faz;
            final double y = Math.atan2(sy, cy);
            final double SA = s*sx/sy;
            final double c2a = 1 - SA*SA;
            double cz = faz+faz;
            if (c2a > 0) {
                cz = -cz/c2a + cy;
            }
            double e = cz*cz*2 - 1;
            double c = ((-3*c2a+4)*F+4)*c2a*F/16;
            double d = x;
            x = ((e*cy*c+cz)*sy*c+y)*SA;
            x = (1-c)*x*F + x2-x1;

            if (Math.abs(d-x) <= EPS) {
                if (false) {
                    // 'faz' and 'baz' are forward azimuths at both points.
                    // Since the current API can't returns this result, it
                    // doesn't worth to compute it at this time.
                    faz = Math.atan2(tu1, tu2);
                    baz = Math.atan2(cu1*sx, baz*cx - su1*cu2)+Math.PI;
                }
                x = Math.sqrt((1/(R*R)-1) * c2a + 1)+1;
                x = (x-2)/x;
                c = 1-x;
                c = (x*x/4 + 1)/c;
                d = (0.375*x*x - 1)*x;
                x = e*cy;
                s = 1-2*e;
                s = ((((sy*sy*4 - 3)*s*cz*d/6-x)*d/4+cz)*sy*d+y)*c*R*getSemiMajorAxis();
                return s;
            }
        }
        // No convergence. It may be because coordinate points
        // are equals or because they are at antipodes.
        final double LEPS = 1E-10;
        if (Math.abs(x1-x2)<=LEPS && Math.abs(y1-y2)<=LEPS) {
            return 0; // Coordinate points are equals
        }
        if (Math.abs(y1)<=LEPS && Math.abs(y2)<=LEPS) {
            return Math.abs(x1-x2) * getSemiMajorAxis(); // Points are on the equator.
        }
        // Other cases: no solution for this algorithm.
        final CoordinateFormat format = new CoordinateFormat();
        throw new ArithmeticException(Errors.format(ErrorKeys.NO_CONVERGENCE_$2,
                  format.format(new GeneralDirectPosition(Math.toDegrees(x1),Math.toDegrees(y1))),
                  format.format(new GeneralDirectPosition(Math.toDegrees(x2),Math.toDegrees(y2)))));
    }

    /**
     * Compare this ellipsoid with the specified object for equality.
     *
     * @param  object The object to compare to {@code this}.
     * @param  compareMetadata {@code true} for performing a strict comparaison, or
     *         {@code false} for comparing only properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultEllipsoid that = (DefaultEllipsoid) object;
            return (!compareMetadata || this.ivfDefinitive == that.ivfDefinitive)   &&
                   Utilities.equals(this.semiMajorAxis,     that.semiMajorAxis)     &&
                   Utilities.equals(this.semiMinorAxis,     that.semiMinorAxis)     &&
                   Utilities.equals(this.inverseFlattening, that.inverseFlattening) &&
                   Utilities.equals(this.unit,              that.unit);
        }
        return false;
    }

    /**
     * Returns a hash value for this ellipsoid. {@linkplain #getName Name},
     * {@linkplain #getRemarks remarks} and the like are not taken in account.
     * In other words, two ellipsoids will return the same hash value if they
     * are equal in the sense of
     * <code>{@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same
     *         in past or future versions of this class.
     */
    @Override
    public int hashCode() {
        long longCode = 37*Double.doubleToLongBits(semiMajorAxis);
        if (ivfDefinitive) {
            longCode += inverseFlattening;
        } else {
            longCode += semiMinorAxis;
        }
        return (((int)(longCode >>> 32)) ^ (int)longCode);
    }

    /**
     * Format the inner part of a
     * <A HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well
     * Known Text</cite> (WKT)</A> element.
     *
     * @param  formatter The formatter to use.
     * @return The WKT element name, which is "SPHEROID"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final double ivf = getInverseFlattening();
        formatter.append(getAxisUnit().getConverterTo(SI.METER).convert(getSemiMajorAxis()));
        formatter.append(Double.isInfinite(ivf) ? 0 : ivf);
        return "SPHEROID";
    }
}
