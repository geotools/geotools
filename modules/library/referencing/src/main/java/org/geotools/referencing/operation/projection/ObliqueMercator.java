/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
 *    This package contains formulas from the PROJ package of USGS.
 *    USGS's work is fully acknowledged here. This derived work has
 *    been relicensed under LGPL with Frank Warmerdam's permission.
 */
package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;
import javax.measure.unit.NonSI;
import org.opengis.util.InternationalString;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.InvalidParameterValueException;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.measure.Angle;
import org.geotools.measure.Latitude;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.ErrorKeys;

import static java.lang.Math.*;


/**
 * Oblique Mercator Projection. A conformal, oblique, cylindrical projection with the cylinder
 * touching the ellipsoid (or sphere) along a great circle path (the central line). The
 * {@linkplain Mercator} and {@linkplain TransverseMercator Transverse Mercator} projections can
 * be thought of as special cases of the oblique mercator, where the central line is along the
 * equator or a meridian, respectively. The Oblique Mercator projection has been used in
 * Switzerland, Hungary, Madagascar, Malaysia, Borneo and the panhandle of Alaska.
 * <p>
 * The Oblique Mercator projection uses a (<var>U</var>,<var>V</var>) coordinate system, with the
 * <var>U</var> axis along the central line. During the forward projection, coordinates from the
 * ellipsoid are projected conformally to a sphere of constant total curvature, called the
 * "aposphere", before being projected onto the plane. The projection coordinates are further
 * convented to a (<var>X</var>,<var>Y</var>) coordinate system by rotating the calculated
 * (<var>u</var>,<var>v</var>) coordinates to give output (<var>x</var>,<var>y</var>) coordinates.
 * The rotation value is usually the same as the projection azimuth (the angle, east of north, of
 * the central line), but some cases allow a separate rotation parameter.
 * <p>
 * There are two forms of the oblique mercator, differing in the origin of their grid coordinates.
 * The {@linkplain HotineObliqueMercator Hotine Oblique Mercator} (EPSG code 9812) has grid
 * coordinates start at the intersection of the central line and the equator of the aposphere.
 * The {@linkplain ObliqueMercator Oblique Mercator} (EPSG code 9815) is the same, except the
 * grid coordinates begin at the central point (where the latitude of center and central line
 * intersect). ESRI separates these two case by appending {@code "Natural_Origin"} (for the
 * {@code "Hotine_Oblique_Mercator"}) and {@code "Center"} (for the {@code "Oblique_Mercator"})
 * to the projection names.
 * <p>
 * Two different methods are used to specify the central line for the oblique mercator:
 * 1) a central point and an azimuth, east of north, describing the central line and
 * 2) two points on the central line. The EPSG does not use the two point method,
 * while ESRI separates the two cases by putting {@code "Azimuth"} and {@code "Two_Point"}
 * in their projection names. Both cases use the point where the {@code "latitude_of_center"}
 * parameter crosses the central line as the projection's central point.
 * The {@linkplain #centralMeridian central meridian} is not a projection parameter,
 * and is instead calculated as the intersection between the central line and the
 * equator of the aposphere.
 * <p>
 * For the azimuth method, the central latitude cannot be &plusmn;90.0 degrees
 * and the central line cannot be at a maximum or minimum latitude at the central point.
 * In the two point method, the latitude of the first and second points cannot be
 * equal. Also, the latitude of the first point and central point cannot be
 * &plusmn;90.0 degrees. Furthermore, the latitude of the first point cannot be 0.0 and
 * the latitude of the second point cannot be -90.0 degrees. A change of
 * 10<sup>-7</sup> radians can allow calculation at these special cases. Snyder's restriction
 * of the central latitude being 0.0 has been removed, since the equations appear
 * to work correctly in this case.
 * <p>
 * Azimuth values of 0.0 and &plusmn;90.0 degrees are allowed (and used in Hungary
 * and Switzerland), though these cases would usually use a Mercator or
 * Transverse Mercator projection instead. Azimuth values > 90 degrees cause
 * errors in the equations.
 * <p>
 * The oblique mercator is also called the "Rectified Skew Orthomorphic" (RSO). It appears
 * is that the only difference from the oblique mercator is that the RSO allows the rotation
 * from the (<var>U</var>,<var>V</var>) to (<var>X</var>,<var>Y</var>) coordinate system to
 * be different from the azimuth. This separate parameter is called
 * {@code "rectified_grid_angle"} (or {@code "XY_Plane_Rotation"} by ESRI) and is also
 * included in the EPSG's parameters for the Oblique Mercator and Hotine Oblique Mercator.
 * The rotation parameter is optional in all the non-two point projections and will be
 * set to the azimuth if not specified.
 * <p>
 * Projection cases and aliases implemented by the {@link ObliqueMercator} are:
 * <ul>
 *   <li>{@code Oblique_Mercator} (EPSG code 9815)<br>
 *       grid coordinates begin at the central point,
 *       has {@code "rectified_grid_angle"} parameter.</li>
 *   <li>{@code Hotine_Oblique_Mercator_Azimuth_Center} (ESRI)<br>
 *       grid coordinates begin at the central point.</li>
 *   <li>{@code Rectified_Skew_Orthomorphic_Center} (ESRI)<br>
 *       grid coordinates begin at the central point,
 *       has {@code "rectified_grid_angle"} parameter.</li>
 *
 *   <li>{@code Hotine_Oblique_Mercator} (EPSG code 9812)<br>
 *       grid coordinates begin at the interseciton of the central line and aposphere equator,
 *       has {@code "rectified_grid_angle"} parameter.</li>
 *   <li>{@code Hotine_Oblique_Mercator_Azimuth_Natural_Origin} (ESRI)<br>
 *       grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 *   <li>{@code Rectified_Skew_Orthomorphic_Natural_Origin} (ESRI)<br>
 *       grid coordinates begin at the interseciton of the central line and aposphere equator,
 *       has {@code "rectified_grid_angle"} parameter.</li>
 *
 *   <li>{@code Hotine_Oblique_Mercator_Two_Point_Center} (ESRI)<br>
 *       grid coordinates begin at the central point.</li>
 *   <li>{@code Hotine_Oblique_Mercator_Two_Point_Natural_Origin} (ESRI)<br>
 *       grid coordinates begin at the interseciton of the central line and aposphere equator.</li>
 * </ul>
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>{@code libproj4} is available at
 *       <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/">libproj4 Miscellanea</A><br>
 *       Relevent files are: {@code PJ_omerc.c}, {@code pj_tsfn.c},
 *       {@code pj_fwd.c}, {@code pj_inv.c} and {@code lib_proj.h}</li>
 *   <li>John P. Snyder (Map Projections - A Working Manual,
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",
 *       EPSG Guidence Note Number 7 part 2, Version 24.</li>
 *   <li>Gerald Evenden, 2004, <a href="http://members.verizon.net/~vze2hc4d/proj4/omerc.pdf">
 *       Documentation of revised Oblique Mercator</a></li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Oblique Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/hotine_oblique_mercator.html">"hotine_oblique_mercator" on RemoteSensing.org</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/oblique_mercator.html">"oblique_mercator" on RemoteSensing.org</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Gerald I. Evenden (for original code in Proj4)
 * @author  Rueben Schulz
 */
public class ObliqueMercator extends MapProjection {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = 5382294977124711214L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Maximum difference allowed when comparing latitudes.
     */
    private static final double EPSILON_LATITUDE = 1E-10;

    //////
    //////    Map projection parameters. The following are NOT used by the transformation
    //////    methods, but are stored in order to restitute them in WKT formatting.  They
    //////    are made visible ('protected' access) for documentation purpose and because
    //////    they are user-supplied parameters, not derived coefficients.
    //////

    /**
     * Latitude of the projection centre. This is similar to the
     * {@link #latitudeOfOrigin}, but the latitude of origin is the
     * Earth equator on aposphere for the oblique mercator.
     */
    protected final double latitudeOfCentre;

    /**
     * Longitude of the projection centre. This is <strong>NOT</strong> equal
     * to the {@link #centralMeridian}, which is the meridian where the
     * central line intersects the Earth equator on aposphere.
     * <p>
     * This parameter applies to the "azimuth" case only.
     * It is set to {@link Double#NaN NaN} for the "two points" case.
     */
    protected final double longitudeOfCentre;

    /**
     * The azimuth of the central line passing throught the centre of the projection, in radians.
     */
    protected final double azimuth;

    /**
     * The rectified bearing of the central line, in radians. This is equals to the
     * {@linkplain #azimuth} if the {@link Provider#RECTIFIED_GRID_ANGLE "rectified_grid_angle"}
     * parameter value is not set.
     */
    protected final double rectifiedGridAngle;

    // The next parameters still private for now because I'm not
    // sure if they should appear in some 'TwoPoint' subclass...
    /**
     * The latitude of the 1st point used to specify the central line, in radians.
     * <p>
     * This parameter applies to the "two points" case only.
     * It is set to {@link Double#NaN NaN} for the "azimuth" case.
     */
    private final double latitudeOf1stPoint;

    /**
     * The longitude of the 1st point used to specify the central line, in radians.
     * <p>
     * This parameter applies to the "two points" case only.
     * It is set to {@link Double#NaN NaN} for the "azimuth" case.
     */
    private final double longitudeOf1stPoint;

    /**
     * The latitude of the 2nd point used to specify the central line, in radians.
     * <p>
     * This parameter applies to the "two points" case only.
     * It is set to {@link Double#NaN NaN} for the "azimuth" case.
     */
    private final double latitudeOf2ndPoint;

    /**
     * The longitude of the 2nd point used to specify the central line, in radians.
     * <p>
     * This parameter applies to the "two points" case only.
     * It is set to {@link Double#NaN NaN} for the "azimuth" case.
     */
    private final double longitudeOf2ndPoint;

    //////
    //////    Map projection coefficients computed from the above parameters.
    //////    They are the fields used for coordinate transformations.
    //////

    /**
     * Constants used in the transformation.
     */
    private final double B, A, E;

    /**
     * Convenience values equal to {@link #A} / {@link #B},
     * {@link #A}&times;{@link #B}, and {@link #B} / {@link #A}.
     */
    private final double ArB, AB, BrA;

    /**
     * <var>v</var> values when the input latitude is a pole.
     */
    private final double v_pole_n, v_pole_s;

    /**
     * Sine and Cosine values for gamma0 (the angle between the meridian
     * and central line at the intersection between the central line and
     * the Earth equator on aposphere).
     */
    private final double singamma0, cosgamma0;

    /**
     * Sine and Cosine values for the rotation between (U,V) and
     * (X,Y) coordinate systems
     */
    private final double sinrot, cosrot;

    /**
     * <var>u</var> value (in (U,V) coordinate system) of the central point. Used in
     * the oblique mercator case. The <var>v</var> value of the central point is 0.0.
     */
    private final double u_c;

    /**
     * {@code true} if using two points on the central line to specify the azimuth.
     */
    final boolean twoPoint;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     *
     * @since 2.4
     *
     * @todo Current implementation assumes a "azimuth" case. We may try to detect the
     *       "two points" case in a future version if needed.
     */
    protected ObliqueMercator(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, Provider.PARAMETERS.descriptors(), false, false);
    }

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @param expected The expected parameter descriptors.
     * @param twoPoint {@code true} for the "two points" case, or {@code false} for the
     *         "azimuth" case. The former is used by ESRI but not EPSG.
     * @param hotine {@code true} only if invoked by the {@link HotineObliqueMercator}
     *         constructor.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    ObliqueMercator(final ParameterValueGroup parameters,
                    final Collection<GeneralParameterDescriptor> expected,
                    final boolean twoPoint, final boolean hotine)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters, expected);
        this.twoPoint = twoPoint;
        /*
         * Sets the 'centralMeridian' and 'latitudeOfOrigin' fields to NaN for safety
         * (they are not 'ObliqueMercator' parameters, so the super-class initialized
         * them to 0.0 by default). The 'centralMeridian' value will be computed later.
         */
        centralMeridian  = Double.NaN;
        latitudeOfOrigin = Double.NaN;
        latitudeOfCentre = doubleValue(expected, Provider.LATITUDE_OF_CENTRE, parameters);
        /*
         * Checks that 'latitudeOfCentre' is not +- 90 degrees.
         * Not checking if 'latitudeOfCentere' is 0, since equations behave correctly.
         */
        ensureLatitudeInRange(Provider.LATITUDE_OF_CENTRE, latitudeOfCentre, false);
        /*
         * Computes common constants now. In a previous version of 'ObliqueMercator', those
         * constants were computed a little bit later. We compute them now in order to have
         * a single 'if (twoPoint) ... else' statement, which help us to keep every fields
         * final and catch some potential errors at compile-time (e.g. unitialized fields).
         */
        final double com     = sqrt(1.0 - excentricitySquared);
        final double sinphi0 = sin(latitudeOfCentre);
        final double cosphi0 = cos(latitudeOfCentre);
        double temp = cosphi0 * cosphi0;
        B = sqrt(1.0 + excentricitySquared * (temp * temp) / (1.0 - excentricitySquared));
        final double con = 1.0 - excentricitySquared * sinphi0 * sinphi0;
        A = B * com / con;
        final double D = B * com / (cosphi0 * sqrt(con));
        double F = D * D - 1.0;
        if (F < 0.0) {
            F = 0.0;
        } else {
            F = sqrt(F);
            if (latitudeOfCentre < 0.0) {  // Taking sign of 'latitudeOfCentre'
                F = -F;
            }
        }
        F = F += D;
        E = F * pow(tsfn(latitudeOfCentre, sinphi0), B);
        /*
         * Computes the constants that depend on the "twoPoint" vs "azimuth" case. In the
         * two points case, we compute them from (LAT_OF_1ST_POINT, LONG_OF_1ST_POINT) and
         * (LAT_OF_2ND_POINT, LONG_OF_2ND_POINT).  For the "azimuth" case, we compute them
         * from LONGITUDE_OF_CENTRE and AZIMUTH.
         */
        final double gamma0;
        if (twoPoint) {
            longitudeOfCentre  = Double.NaN; // This is for the "azimuth" case only.
            latitudeOf1stPoint = doubleValue(expected, Provider_TwoPoint.LAT_OF_1ST_POINT, parameters);
            // Checks that latOf1stPoint is not +-90 degrees
            ensureLatitudeInRange(Provider_TwoPoint.LAT_OF_1ST_POINT, latitudeOf1stPoint, false);
            longitudeOf1stPoint = doubleValue(expected, Provider_TwoPoint.LONG_OF_1ST_POINT, parameters);
            ensureLongitudeInRange(Provider_TwoPoint.LONG_OF_1ST_POINT, longitudeOf1stPoint, true);
            latitudeOf2ndPoint = doubleValue(expected, Provider_TwoPoint.LAT_OF_2ND_POINT, parameters);
            ensureLatitudeInRange(Provider_TwoPoint.LAT_OF_2ND_POINT, latitudeOf2ndPoint, true);
            double longitudeOf2ndPoint; // Will be assigned to the field later.
            longitudeOf2ndPoint = doubleValue(expected, Provider_TwoPoint.LONG_OF_2ND_POINT, parameters);
            ensureLongitudeInRange(Provider_TwoPoint.LONG_OF_2ND_POINT, longitudeOf2ndPoint, true);
            /*
             * Ensures that (phi1 != phi2), (phi1 != 0°) and (phi2 != -90°),
             * as specified in class javadoc.
             */
            ParameterDescriptor desc = null;
            Object value = null;
            if (abs(latitudeOf1stPoint - latitudeOf2ndPoint) < EPSILON_LATITUDE) {
                desc  = Provider_TwoPoint.LAT_OF_1ST_POINT;
                value = Provider_TwoPoint.LAT_OF_2ND_POINT.getName().getCode();
                // Exception will be thrown below.
            }
            if (abs(latitudeOf1stPoint) < EPSILON_LATITUDE) {
                desc  = Provider_TwoPoint.LAT_OF_1ST_POINT;
                value = new Latitude(latitudeOf1stPoint);
                // Exception will be thrown below.
            }
            if (abs(latitudeOf2ndPoint + PI/2.0) < EPSILON_LATITUDE) {
                desc  = Provider_TwoPoint.LAT_OF_2ND_POINT;
                value = new Latitude(latitudeOf2ndPoint);
                // Exception will be thrown below.
            }
            if (desc != null) {
                final String name = desc.getName().getCode();
                throw new InvalidParameterValueException(Errors.format(
                        ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value), name, value);
            }
            /*
             * The coefficients for the "two points" case.
             */
            final double H = pow(tsfn(latitudeOf1stPoint, sin(latitudeOf1stPoint)), B);
            final double L = pow(tsfn(latitudeOf2ndPoint, sin(latitudeOf2ndPoint)), B);
            final double Fp = E / H;
            final double P = (L - H) / (L + H);
            double J = E * E;
            J = (J - L * H) / (J + L * H);
            double diff = longitudeOf1stPoint - longitudeOf2ndPoint;
            if (diff < -PI) {
                longitudeOf2ndPoint -= 2.0 * PI;
            } else if (diff > PI) {
                longitudeOf2ndPoint += 2.0 * PI;
            }
            this.longitudeOf2ndPoint = longitudeOf2ndPoint;
            centralMeridian = rollLongitude(0.5 * (longitudeOf1stPoint + longitudeOf2ndPoint) -
                     atan(J * tan(0.5 * B * (longitudeOf1stPoint - longitudeOf2ndPoint)) / P) / B);
            gamma0 = atan(2.0 * sin(B * rollLongitude(longitudeOf1stPoint - centralMeridian)) /
                     (Fp - 1.0 / Fp));
            azimuth = asin(D * sin(gamma0));
            rectifiedGridAngle = azimuth;
        } else {
            /*
             * Computes coefficients for the "azimuth" case. Set the 1st and 2nd points
             * to (NaN,NaN) since they are specific to the "two points" case.  They are
             * involved in WKT formatting only, not in transformation calculation.
             */
            latitudeOf1stPoint  = Double.NaN;
            longitudeOf1stPoint = Double.NaN;
            latitudeOf2ndPoint  = Double.NaN;
            longitudeOf2ndPoint = Double.NaN;
            longitudeOfCentre = doubleValue(expected, Provider.LONGITUDE_OF_CENTRE, parameters);
            ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTRE, longitudeOfCentre, true);
            azimuth = doubleValue(expected, Provider.AZIMUTH, parameters);
            // Already checked for +-360 deg. above.
            if ((azimuth > -1.5*PI && azimuth < -0.5*PI) ||
                (azimuth >  0.5*PI && azimuth <  1.5*PI))
            {
                final String name = Provider.AZIMUTH.getName().getCode();
                final Angle value = new Angle(toDegrees(azimuth));
                throw new InvalidParameterValueException(Errors.format(
                        ErrorKeys.ILLEGAL_ARGUMENT_$2, name, value), name, value);
            }
            temp = doubleValue(expected, Provider.RECTIFIED_GRID_ANGLE, parameters);
            if (Double.isNaN(temp)) {
                temp = azimuth;
            }
            rectifiedGridAngle = temp;
            gamma0 = asin(sin(azimuth) / D);
            // Check for asin(+-1.00000001)
            temp = 0.5 * (F - 1.0 / F) * tan(gamma0);
            if (abs(temp) > 1.0) {
                if (abs(abs(temp) - 1.0) > EPSILON) {
                    throw new IllegalArgumentException(Errors.format(ErrorKeys.TOLERANCE_ERROR));
                }
                temp = (temp > 0) ? 1.0 : -1.0;
            }
            centralMeridian = longitudeOfCentre - asin(temp) / B;
        }
        /*
         * More coefficients common to all kind of oblique mercator.
         */
        singamma0 = sin(gamma0);
        cosgamma0 = cos(gamma0);
        sinrot    = sin(rectifiedGridAngle);
        cosrot    = cos(rectifiedGridAngle);
        ArB       = A / B;
        AB        = A * B;
        BrA       = B / A;
        v_pole_n  = ArB * log(tan(0.5 * (PI/2.0 - gamma0)));
        v_pole_s  = ArB * log(tan(0.5 * (PI/2.0 + gamma0)));
        if (hotine) {
            u_c = 0.0;
        } else {
            if (abs(abs(azimuth) - PI/2.0) < EPSILON_LATITUDE) {
                // LongitudeOfCentre = NaN in twoPoint, but azimuth cannot be 90 here (lat1 != lat2)
                u_c = A * (longitudeOfCentre - centralMeridian);
            } else {
                double u_c = abs(ArB * atan2(sqrt(D * D - 1.0), cos(azimuth)));
                if (latitudeOfCentre < 0.0) {
                    u_c = -u_c;
                }
                this.u_c = u_c;
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return (twoPoint) ? Provider_TwoPoint.PARAMETERS : Provider.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        // Note: we don't need a "if (twoPoint) ... else" statement since
        // the "set" method will actually set the value only if applicable.
        set(expected, Provider.LATITUDE_OF_CENTRE,         values, latitudeOfCentre);
        set(expected, Provider.LONGITUDE_OF_CENTRE,        values, longitudeOfCentre);
        set(expected, Provider.AZIMUTH,                    values, azimuth);
        set(expected, Provider.RECTIFIED_GRID_ANGLE,       values, rectifiedGridAngle);
        set(expected, Provider_TwoPoint.LAT_OF_1ST_POINT,  values, latitudeOf1stPoint);
        set(expected, Provider_TwoPoint.LONG_OF_1ST_POINT, values, longitudeOf1stPoint);
        set(expected, Provider_TwoPoint.LAT_OF_2ND_POINT,  values, latitudeOf2ndPoint);
        set(expected, Provider_TwoPoint.LONG_OF_2ND_POINT, values, longitudeOf2ndPoint);
        return values;
    }

    /**
     * {@inheritDoc}
     *
     * @param x The longitude of the coordinate, in <strong>radians</strong>.
     * @param y The  latitude of the coordinate, in <strong>radians</strong>.
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        double u, v;
        if (abs(abs(y) - PI/2.0) > EPSILON) {
            double Q = E / pow(tsfn(y, sin(y)), B);
            double temp = 1.0 / Q;
            double S = 0.5 * (Q - temp);
            double V = sin(B * x);
            double U = (S * singamma0 - V * cosgamma0) / (0.5 * (Q + temp));
            if (abs(abs(U) - 1.0) < EPSILON) {
                throw new ProjectionException(ErrorKeys.INFINITE_VALUE_$1, "v");
            }
            v = 0.5 * ArB * log((1.0 - U) / (1.0 + U));
            temp = cos(B * x);
            if (abs(temp) < EPSILON_LATITUDE) {
                u = AB * x;
            } else {
                u = ArB * atan2((S * cosgamma0 + V * singamma0), temp);
            }
        } else {
            v = y > 0 ? v_pole_n : v_pole_s;
            u = ArB * y;
        }
        u -= u_c;
        x = v * cosrot + u * sinrot;
        y = u * cosrot - v * sinrot;

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * {@inheritDoc}
     */
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        double v = x * cosrot - y * sinrot;
        double u = y * cosrot + x * sinrot + u_c;
        double Qp = exp(-BrA * v);
        double temp = 1.0 / Qp;
        double Sp = 0.5 * (Qp - temp);
        double Vp = sin(BrA * u);
        double Up = (Vp * cosgamma0 + Sp * singamma0) / (0.5 * (Qp + temp));
        if (abs(abs(Up) - 1.0) < EPSILON) {
            x = 0.0;
            y = Up < 0.0 ? -PI / 2.0 : PI / 2.0;
        } else {
            y = pow(E / sqrt((1. + Up) / (1. - Up)), 1.0 / B);  //calculate t
            y = cphi2(y);
            x = -atan2((Sp * cosgamma0 - Vp * singamma0), cos(BrA * u)) / B;
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Maximal error (in metres) tolerated for assertion, if enabled.
     *
     * @param  longitude The longitude in decimal degrees.
     * @param  latitude The latitude in decimal degrees.
     * @return The tolerance level for assertions, in meters.
     */
    @Override
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        if (abs(longitude - centralMeridian)/2 + abs(latitude  - latitudeOfCentre) > 10) {
            // When far from the valid area, use a larger tolerance.
            return 1;
        }
        return super.getToleranceForAssertions(longitude, latitude);
    }

    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        long code =      Double.doubleToLongBits(latitudeOfCentre);
        code = code*37 + Double.doubleToLongBits(longitudeOfCentre);
        code = code*37 + Double.doubleToLongBits(azimuth);
        code = code*37 + Double.doubleToLongBits(rectifiedGridAngle);
        code = code*37 + Double.doubleToLongBits(latitudeOf1stPoint);
        code = code*37 + Double.doubleToLongBits(latitudeOf2ndPoint);
        return ((int)code ^ (int)(code >>> 32)) + 37*super.hashCode();
    }

    /**
     * Compares the specified object with this map projection for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final ObliqueMercator that = (ObliqueMercator) object;
            return this.twoPoint == that.twoPoint &&
                   equals(this.latitudeOfCentre   , that.latitudeOfCentre   ) &&
                   equals(this.longitudeOfCentre  , that.longitudeOfCentre  ) &&
                   equals(this.azimuth            , that.azimuth            ) &&
                   equals(this.rectifiedGridAngle , that.rectifiedGridAngle ) &&
                   equals(this.latitudeOf1stPoint , that.latitudeOf1stPoint ) &&
                   equals(this.longitudeOf1stPoint, that.longitudeOf1stPoint) &&
                   equals(this.latitudeOf2ndPoint , that.latitudeOf2ndPoint ) &&
                   equals(this.longitudeOf2ndPoint, that.longitudeOf2ndPoint) &&
                   equals(this.u_c,                 that.u_c);
            // Note: "u_c" is a derived parameter, so in theory we don't need to compare it.
            //        However we still compare it as a safety, because it takes a different
            //        value in the "hotine" case.
        }
        return false;
    }




    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for an {@linkplain ObliqueMercator Oblique Mercator} projection (EPSG code 9815).
     *
     * @since 2.1
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 201776686002266891L;

        /**
         * The operation parameter descriptor for the {@link #latitudeOfCentre latitudeOfCentre}
         * parameter value. Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor LATITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "latitude_of_center"),
                    new NamedIdentifier(Citations.EPSG,     "Latitude of projection centre"),
                    new NamedIdentifier(Citations.ESRI,     "Latitude_Of_Center"),
                    new NamedIdentifier(Citations.GEOTIFF,  "CenterLat")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #longitudeOfCentre longitudeOfCentre}
         * parameter value. Valid values range is from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor LONGITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "longitude_of_center"),
                    new NamedIdentifier(Citations.EPSG,     "Longitude of projection centre"),
                    new NamedIdentifier(Citations.ESRI,     "Longitude_Of_Center"),
                    new NamedIdentifier(Citations.GEOTIFF,  "CenterLong")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #azimuth azimuth}
         * parameter value. Valid values range is from -360 to -270, -90 to 90,
         * and 270 to 360 degrees. Default value is 0.
         */
        public static final ParameterDescriptor AZIMUTH = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "azimuth"),
                    new NamedIdentifier(Citations.ESRI,     "Azimuth"),
                    new NamedIdentifier(Citations.EPSG,     "Azimuth of initial line"),
                    new NamedIdentifier(Citations.GEOTIFF,  "AzimuthAngle")
                },
                0, -360, 360, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #rectifiedGridAngle
         * rectifiedGridAngle} parameter value. It is an optional parameter with
         * valid values ranging from -360 to 360°. Default value is {@link #azimuth azimuth}.
         */
        public static final ParameterDescriptor RECTIFIED_GRID_ANGLE = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "rectified_grid_angle"),
                    new NamedIdentifier(Citations.EPSG,     "Angle from Rectified to Skew Grid"),
                    new NamedIdentifier(Citations.ESRI,     "XY_Plane_Rotation"),
                    new NamedIdentifier(Citations.GEOTIFF,  "RectifiedGridAngle")
                },
                -360, 360, NonSI.DEGREE_ANGLE);

        /**
         * The localized name for "Oblique Mercator".
         */
        static final InternationalString NAME =
                Vocabulary.formatInternational(VocabularyKeys.OBLIQUE_MERCATOR_PROJECTION);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Oblique_Mercator"),
                new NamedIdentifier(Citations.EPSG,     "Oblique Mercator"),
                new NamedIdentifier(Citations.EPSG,     "9815"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_ObliqueMercator"),
                new NamedIdentifier(Citations.ESRI,     "Hotine_Oblique_Mercator_Azimuth_Center"),
                new NamedIdentifier(Citations.ESRI,     "Rectified_Skew_Orthomorphic_Center"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LONGITUDE_OF_CENTRE, LATITUDE_OF_CENTRE,
                AZIMUTH,             RECTIFIED_GRID_ANGLE,
                SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Constructs a new provider.
         *
         * @param params A description of parameters.
         */
        protected Provider(final ParameterDescriptorGroup params) {
            super(params);
        }

        /**
         * Returns the operation type for this map projection.
         */
        @Override
        public Class<CylindricalProjection> getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection<GeneralParameterDescriptor> descriptors = PARAMETERS.descriptors();
            return new ObliqueMercator(parameters, descriptors, false, false);
        }
    }


    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain ObliqueMercator Oblique Mercator} projection, specified
     * with two points on the central line (instead of a central point and azimuth).
     *
     * @since 2.1
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider_TwoPoint extends Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 7124258885016543889L;

        /**
         * The operation parameter descriptor for the {@code latitudeOf1stPoint}
         * parameter value. Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_1ST_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_1st_Point")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@code longitudeOf1stPoint}
         * parameter value. Valid values range is from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_1ST_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Longitude_Of_1st_Point")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@code latitudeOf2ndPoint}
         * parameter value. Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor LAT_OF_2ND_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_2nd_Point")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@code longitudeOf2ndPoint}
         * parameter value. Valid values range is from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor LONG_OF_2ND_POINT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Longitude_Of_2nd_Point")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.ESRI,     "Hotine_Oblique_Mercator_Two_Point_Center"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                LAT_OF_1ST_POINT,    LONG_OF_1ST_POINT,
                LAT_OF_2ND_POINT,    LONG_OF_2ND_POINT,
                LATITUDE_OF_CENTRE,  SCALE_FACTOR,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider_TwoPoint() {
            super(PARAMETERS);
        }

        /**
         * Constructs a new provider.
         *
         * @param params A description of parameters.
         */
        protected Provider_TwoPoint(final ParameterDescriptorGroup params) {
            super(params);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final Collection<GeneralParameterDescriptor> descriptors = PARAMETERS.descriptors();
            return new ObliqueMercator(parameters, descriptors, true, false);
        }
    }
}
