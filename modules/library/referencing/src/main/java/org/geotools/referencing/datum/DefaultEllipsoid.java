/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2001-2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.datum;

import java.awt.geom.Point2D;
import java.text.MessageFormat;
import java.util.Collections;
import java.util.Map;
import javax.measure.Unit;
import javax.measure.UnitConverter;
import javax.measure.quantity.Length;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import net.sf.geographiclib.GeodesicMask;
import org.geotools.api.referencing.datum.Ellipsoid;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.proj.PROJFormattable;
import org.geotools.referencing.proj.PROJFormatter;
import org.geotools.referencing.wkt.Formatter;
import org.geotools.util.Utilities;
import si.uom.SI;

/**
 * Geometric figure that can be used to describe the approximate shape of the earth. In mathematical terms, it is a
 * surface formed by the rotation of an ellipse about its minor axis. An ellipsoid requires two defining parameters:
 *
 * <ul>
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and {@linkplain #getInverseFlattening inverse flattening}, or
 *   <li>{@linkplain #getSemiMajorAxis semi-major axis} and {@linkplain #getSemiMinorAxis semi-minor axis}.
 * </ul>
 *
 * @since 2.1
 * @version $Id$
 * @author Martin Desruisseaux (IRD)
 */
public class DefaultEllipsoid extends AbstractIdentifiedObject implements Ellipsoid, PROJFormattable {
    /** Serial number for interoperability with different versions. */
    private static final long serialVersionUID = -1149451543954764081L;

    /**
     * WGS 1984 ellipsoid with axis in {@linkplain SI#METER metres}. This ellipsoid is used in GPS systems and is the
     * default for most {@code org.geotools} packages.
     */
    public static final DefaultEllipsoid WGS84 = createFlattenedSphere("WGS84", 6378137.0, 298.257223563, SI.METRE);

    /**
     * GRS 80 ellipsoid with axis in {@linkplain SI#METER metres}.
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid GRS80 = createFlattenedSphere("GRS80", 6378137.0, 298.257222101, SI.METRE);

    /** International 1924 ellipsoid with axis in {@linkplain SI#METER metres}. */
    public static final DefaultEllipsoid INTERNATIONAL_1924 =
            createFlattenedSphere("International 1924", 6378388.0, 297.0, SI.METRE);

    /**
     * Clarke 1866 ellipsoid with axis in {@linkplain SI#METER metres}.
     *
     * @since 2.2
     */
    public static final DefaultEllipsoid CLARKE_1866 =
            createFlattenedSphere("Clarke 1866", 6378206.4, 294.9786982, SI.METRE);

    /**
     * A sphere with a radius of 6371000 {@linkplain SI#METER metres}. Spheres use a simplier algorithm for
     * {@linkplain #orthodromicDistance orthodromic distance computation}, which may be faster and more robust.
     */
    public static final DefaultEllipsoid SPHERE = createEllipsoid("SPHERE", 6371000, 6371000, SI.METRE);

    /**
     * The equatorial radius.
     *
     * @see #getSemiMajorAxis
     */
    private final double semiMajorAxis;

    /**
     * The polar radius.
     *
     * @see #getSemiMinorAxis
     */
    private final double semiMinorAxis;

    /**
     * The inverse of the flattening value, or {@link Double#POSITIVE_INFINITY} if the ellipsoid is a sphere.
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

    /** The units of the semi-major and semi-minor axis values. */
    private final Unit<Length> unit;

    /**
     * Constructs a new ellipsoid with the same values than the specified one. This copy constructor provides a way to
     * wrap an arbitrary implementation into a Geotools one or a user-defined one (as a subclass), usually in order to
     * leverage some implementation-specific API. This constructor performs a shallow copy, i.e. the properties are not
     * cloned.
     *
     * @param ellipsoid The ellipsoid to copy.
     * @since 2.2
     * @see #wrap
     */
    protected DefaultEllipsoid(final Ellipsoid ellipsoid) {
        super(ellipsoid);
        semiMajorAxis = ellipsoid.getSemiMajorAxis();
        semiMinorAxis = ellipsoid.getSemiMinorAxis();
        inverseFlattening = ellipsoid.getInverseFlattening();
        ivfDefinitive = ellipsoid.isIvfDefinitive();
        unit = ellipsoid.getAxisUnit();
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param inverseFlattening The inverse of the flattening value.
     * @param ivfDefinitive {@code true} if the inverse flattening is definitive.
     * @param unit The units of the semi-major and semi-minor axis values.
     * @see #createEllipsoid
     * @see #createFlattenedSphere
     */
    protected DefaultEllipsoid(
            final Map<String, ?> properties,
            final double semiMajorAxis,
            final double semiMinorAxis,
            final double inverseFlattening,
            final boolean ivfDefinitive,
            final Unit<Length> unit) {
        super(properties);
        this.unit = unit;
        this.semiMajorAxis = check("semiMajorAxis", semiMajorAxis);
        this.semiMinorAxis = check("semiMinorAxis", semiMinorAxis);
        this.inverseFlattening = check("inverseFlattening", inverseFlattening);
        this.ivfDefinitive = ivfDefinitive;
        ensureNonNull("unit", unit);
        ensureLinearUnit(unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length.
     *
     * @param name The ellipsoid name.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(
            final String name, final double semiMajorAxis, final double semiMinorAxis, final Unit<Length> unit) {
        return createEllipsoid(Collections.singletonMap(NAME_KEY, name), semiMajorAxis, semiMinorAxis, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length. The properties map is given unchanged to the
     * {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis The equatorial radius.
     * @param semiMinorAxis The polar radius.
     * @param unit The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createEllipsoid(
            final Map<String, ?> properties,
            final double semiMajorAxis,
            final double semiMinorAxis,
            final Unit<Length> unit) {
        if (semiMajorAxis == semiMinorAxis) {
            return new Spheroid(properties, semiMajorAxis, false, unit);
        } else {
            return new DefaultEllipsoid(
                    properties,
                    semiMajorAxis,
                    semiMinorAxis,
                    semiMajorAxis / (semiMajorAxis - semiMinorAxis),
                    false,
                    unit);
        }
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and inverse flattening value.
     *
     * @param name The ellipsoid name.
     * @param semiMajorAxis The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(
            final String name, final double semiMajorAxis, final double inverseFlattening, final Unit<Length> unit) {
        return createFlattenedSphere(Collections.singletonMap(NAME_KEY, name), semiMajorAxis, inverseFlattening, unit);
    }

    /**
     * Constructs a new ellipsoid using the specified axis length and inverse flattening value. The properties map is
     * given unchanged to the {@linkplain AbstractIdentifiedObject#AbstractIdentifiedObject(Map) super-class
     * constructor}.
     *
     * @param properties Set of properties. Should contains at least {@code "name"}.
     * @param semiMajorAxis The equatorial radius.
     * @param inverseFlattening The inverse flattening value.
     * @param unit The units of the semi-major and semi-minor axis values.
     * @return An ellipsoid with the given axis length.
     */
    public static DefaultEllipsoid createFlattenedSphere(
            final Map<String, ?> properties,
            final double semiMajorAxis,
            final double inverseFlattening,
            final Unit<Length> unit) {
        if (Double.isInfinite(inverseFlattening)) {
            return new Spheroid(properties, semiMajorAxis, true, unit);
        } else {
            return new DefaultEllipsoid(
                    properties,
                    semiMajorAxis,
                    semiMajorAxis * (1 - 1 / inverseFlattening),
                    inverseFlattening,
                    true,
                    unit);
        }
    }

    /**
     * Wraps an arbitrary ellipsoid into a Geotools implementation. This method is usefull if
     * {@link #orthodromicDistance orthodromic distance computation} (for example) are desired. If the supplied
     * ellipsoid is already an instance of {@code DefaultEllipsoid} or is {@code null}, then it is returned unchanged.
     *
     * @param ellipsoid The ellipsoid to wrap.
     * @return The given ellipsoid as a {@code DefaultEllipsoid} instance.
     */
    public static DefaultEllipsoid wrap(final Ellipsoid ellipsoid) {
        if (ellipsoid == null || ellipsoid instanceof DefaultEllipsoid) {
            return (DefaultEllipsoid) ellipsoid;
        }
        if (ellipsoid.isIvfDefinitive()) {
            return createFlattenedSphere(
                    getProperties(ellipsoid),
                    ellipsoid.getSemiMajorAxis(),
                    ellipsoid.getInverseFlattening(),
                    ellipsoid.getAxisUnit());
        } else {
            return createEllipsoid(
                    getProperties(ellipsoid),
                    ellipsoid.getSemiMajorAxis(),
                    ellipsoid.getSemiMinorAxis(),
                    ellipsoid.getAxisUnit());
        }
    }

    /**
     * Checks the argument validity. Argument {@code value} should be greater than zero.
     *
     * @param name Argument name.
     * @param value Argument value.
     * @return {@code value}.
     * @throws IllegalArgumentException if {@code value} is not greater than 0.
     */
    static double check(final String name, final double value) throws IllegalArgumentException {
        if (value > 0) {
            return value;
        }
        throw new IllegalArgumentException(MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value));
    }

    /**
     * Returns the linear unit of the {@linkplain #getSemiMajorAxis semi-major} and {@linkplain #getSemiMinorAxis
     * semi-minor} axis values.
     *
     * @return The axis linear unit.
     */
    @Override
    public Unit<Length> getAxisUnit() {
        return unit;
    }

    /**
     * Length of the semi-major axis of the ellipsoid. This is the equatorial radius in {@linkplain #getAxisUnit axis
     * linear unit}.
     *
     * @return Length of semi-major axis.
     */
    @Override
    public double getSemiMajorAxis() {
        return semiMajorAxis;
    }

    /**
     * Length of the semi-minor axis of the ellipsoid. This is the polar radius in {@linkplain #getAxisUnit axis linear
     * unit}.
     *
     * @return Length of semi-minor axis.
     */
    @Override
    public double getSemiMinorAxis() {
        return semiMinorAxis;
    }

    /**
     * The ratio of the distance between the center and a focus of the ellipse to the length of its semimajor axis. The
     * eccentricity can alternately be computed from the equation: <code>
     * e=sqrt(2f-f²)</code>.
     *
     * @return The eccentricity of this ellipsoid.
     */
    public double getEccentricity() {
        final double f = 1 - getSemiMinorAxis() / getSemiMajorAxis();
        return Math.sqrt(2 * f - f * f);
    }

    /**
     * Returns the value of the inverse of the flattening constant. Flattening is a value used to indicate how closely
     * an ellipsoid approaches a spherical shape. The inverse flattening is related to the equatorial/polar radius by
     * the formula
     *
     * <p><var>ivf</var>&nbsp;=&nbsp;<var>r</var><sub>e</sub>/(<var>r</var><sub>e</sub>-<var>r</var><sub>p</sub>).
     *
     * <p>For perfect spheres (i.e. if {@link #isSphere} returns {@code true}), the {@link Double#POSITIVE_INFINITY}
     * value is used.
     *
     * @return The inverse flattening value.
     */
    @Override
    public double getInverseFlattening() {
        return inverseFlattening;
    }

    /**
     * Indicates if the {@linkplain #getInverseFlattening inverse flattening} is definitive for this ellipsoid. Some
     * ellipsoids use the IVF as the defining value, and calculate the polar radius whenever asked. Other ellipsoids use
     * the polar radius to calculate the IVF whenever asked. This distinction can be important to avoid floating-point
     * rounding errors.
     *
     * @return {@code true} if the {@linkplain #getInverseFlattening inverse flattening} is definitive, or {@code false}
     *     if the {@linkplain #getSemiMinorAxis polar radius} is definitive.
     */
    @Override
    public boolean isIvfDefinitive() {
        return ivfDefinitive;
    }

    /**
     * {@code true} if the ellipsoid is degenerate and is actually a sphere. The sphere is completely defined by the
     * {@linkplain #getSemiMajorAxis semi-major axis}, which is the radius of the sphere.
     *
     * @return {@code true} if the ellipsoid is degenerate and is actually a sphere.
     */
    @Override
    public boolean isSphere() {
        return semiMajorAxis == semiMinorAxis;
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates. The orthodromic distance is the shortest
     * distance between two points on a sphere's surface. The default implementation delegates the work to
     * {@link #orthodromicDistance(double,double,double,double)}.
     *
     * @param P1 Longitude and latitude of first point (in decimal degrees).
     * @param P2 Longitude and latitude of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid).
     */
    public double orthodromicDistance(final Point2D P1, final Point2D P2) {
        return orthodromicDistance(P1.getX(), P1.getY(), P2.getX(), P2.getY());
    }

    /**
     * Returns the orthodromic distance between two geographic coordinates. The orthodromic distance is the shortest
     * distance between two points on a sphere's surface. The orthodromic path is always on a great circle. This is
     * different from the <cite>loxodromic distance</cite>, which is a longer distance on a path with a constant
     * direction on the compass.
     *
     * @param x1 Longitude of first point (in decimal degrees).
     * @param y1 Latitude of first point (in decimal degrees).
     * @param x2 Longitude of second point (in decimal degrees).
     * @param y2 Latitude of second point (in decimal degrees).
     * @return The orthodromic distance (in the units of this ellipsoid's axis).
     */
    public double orthodromicDistance(double x1, double y1, double x2, double y2) {
        Geodesic geod = new Geodesic(getSemiMajorAxis(), 1 / getInverseFlattening());
        GeodesicData g = geod.Inverse(y1, x1, y2, x2, GeodesicMask.DISTANCE);
        return g.s12;
    }

    /**
     * Compare this ellipsoid with the specified object for equality.
     *
     * @param object The object to compare to {@code this}.
     * @param compareMetadata {@code true} for performing a strict comparaison, or {@code false} for comparing only
     *     properties relevant to transformations.
     * @return {@code true} if both objects are equal.
     */
    @Override
    public boolean equals(final AbstractIdentifiedObject object, final boolean compareMetadata) {
        if (object == this) {
            return true; // Slight optimization.
        }
        if (super.equals(object, compareMetadata)) {
            final DefaultEllipsoid that = (DefaultEllipsoid) object;

            if (!compareMetadata) {
                UnitConverter converter = that.unit.getConverterTo(this.unit);
                return Utilities.equals(this.semiMajorAxis, converter.convert(that.semiMajorAxis))
                        && Utilities.equals(this.semiMinorAxis, converter.convert(that.semiMinorAxis));
            } else {
                return (this.ivfDefinitive == that.ivfDefinitive)
                        && Utilities.equals(this.semiMajorAxis, that.semiMajorAxis)
                        && Utilities.equals(this.semiMinorAxis, that.semiMinorAxis)
                        && Utilities.equals(this.inverseFlattening, that.inverseFlattening)
                        && Utilities.equals(this.unit, that.unit);
            }
        }
        return false;
    }

    /**
     * Returns a hash value for this ellipsoid. {@linkplain #getName Name}, {@linkplain #getRemarks remarks} and the
     * like are not taken in account. In other words, two ellipsoids will return the same hash value if they are equal
     * in the sense of <code>
     * {@link #equals equals}(AbstractIdentifiedObject, <strong>false</strong>)</code>.
     *
     * @return The hash code value. This value doesn't need to be the same in past or future versions of this class.
     */
    @Override
    @SuppressWarnings("PMD.OverrideBothEqualsAndHashcode")
    public int hashCode() {
        long longCode = 37 * Double.doubleToLongBits(semiMajorAxis);
        if (ivfDefinitive) {
            longCode = (long) (longCode + inverseFlattening);
        } else {
            longCode = (long) (longCode + semiMinorAxis);
        }
        return (((int) (longCode >>> 32)) ^ (int) longCode);
    }

    /**
     * Format the inner part of a <A
     * HREF="http://geoapi.sourceforge.net/snapshot/javadoc/org/opengis/referencing/doc-files/WKT.html"><cite>Well Known
     * Text</cite> (WKT)</A> element.
     *
     * @param formatter The formatter to use.
     * @return The WKT element name, which is "SPHEROID"
     */
    @Override
    protected String formatWKT(final Formatter formatter) {
        final double ivf = getInverseFlattening();
        formatter.append(getAxisUnit().getConverterTo(SI.METRE).convert(getSemiMajorAxis()));
        formatter.append(Double.isInfinite(ivf) ? 0 : ivf);
        return "SPHEROID";
    }

    @Override
    public String formatPROJ(final PROJFormatter formatter) {
        final double ivf = getInverseFlattening();
        if (!formatter.isDatumProvided()) {
            if (formatter.isEllipsoidProvided()) {
                return "+ellps=";
            } else {
                double val = getAxisUnit().getConverterTo(SI.METRE).convert(getSemiMajorAxis());
                formatter.append("a", val);
                if (!Double.isInfinite(ivf)) {
                    formatter.append("rf", ivf);
                }
            }
        }
        return PROJFormatter.NO_KEYWORD;
    }
}
