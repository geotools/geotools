/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.ErrorKeys;

import static java.lang.Math.*;


/**
 * Lambert Azimuthal Equal Area (EPSG code 9820).
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li> A. Annoni, C. Luzet, E.Gubler and J. Ihde - Map Projections for Europe</li>
 *   <li> John P. Snyder (Map Projections - A Working Manual,
 *        U.S. Geological Survey Professional Paper 1395)</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/LambertAzimuthalEqual-AreaProjection.html">Lambert Azimuthal Equal-Area Projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/lambert_azimuthal_equal_area.html">"Lambert_Azimuthal_Equal_Area" on RemoteSensing.org</A>
 *
 * @since 2.4
 * @version $Id$
 * @source $URL$
 * @author Gerald Evenden  (for original code in Proj4)
 * @author Beate Stollberg
 * @author Martin Desruisseaux
 */
public class LambertAzimuthalEqualArea extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = 1639914708790574760L;

    /** Maximum difference allowed when comparing real numbers. */
    private static final double EPSILON = 1E-7;

    /** Epsilon for the comparaison of small quantities. */
    private static final double FINE_EPSILON = 1E-10;

    /** Epsilon for the comparaison of latitudes. */
    private static final double EPSILON_LATITUDE = 1E-10;

    /** Constants for authalic latitude. */
    private static final double P00 = 0.33333333333333333333,
                                P01 = 0.17222222222222222222,
                                P02 = 0.10257936507936507936,
                                P10 = 0.06388888888888888888,
                                P11 = 0.06640211640211640211,
                                P20 = 0.01641501294219154443;

    /** The projection mode. */
    static final int OBLIQUE=0, EQUATORIAL=1, NORTH_POLE=2, SOUTH_POLE=3;

    /** The projection mode for this particular instance. */
    final int mode;

    /** Constant parameters. */
    final double sinb1, cosb1, xmf, ymf, mmf, qp, dd, rq;

    /** Coefficients for authalic latitude. */
    private final double APA0, APA1, APA2;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected LambertAzimuthalEqualArea(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        latitudeOfOrigin = doubleValue(expected, Provider.LATITUDE_OF_CENTRE,  parameters);
        centralMeridian  = doubleValue(expected, Provider.LONGITUDE_OF_CENTRE, parameters);
        ensureLatitudeInRange (Provider.LATITUDE_OF_CENTRE,  latitudeOfOrigin, true);
        ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTRE, centralMeridian,  true);
        /*
         * Detects the mode (oblique, etc.).
         */
        final double t = abs(latitudeOfOrigin);
        if (abs(t - PI/2) < EPSILON_LATITUDE) {
            mode = latitudeOfOrigin < 0.0 ? SOUTH_POLE : NORTH_POLE;
        } else if (abs(t) < EPSILON_LATITUDE) {
            mode = EQUATORIAL;
        } else {
            mode = OBLIQUE;
        }
        /*
         * Computes the constants for authalic latitude.
         */
        final double es2 = excentricitySquared * excentricitySquared;
        final double es3 = excentricitySquared * es2;
        APA0 = P02 * es3 + P01 * es2 + P00 * excentricitySquared;
        APA1 = P11 * es3 + P10 * es2;
        APA2 = P20 * es3;

        final double sinphi;
        qp     = qsfn(1);
        rq     = sqrt(0.5 * qp);
        mmf    = 0.5 / (1 - excentricitySquared);
        sinphi = sin(latitudeOfOrigin);
        if (isSpherical) {
            sinb1 = sin(latitudeOfOrigin);
            cosb1 = cos(latitudeOfOrigin);
        } else {
            sinb1 = qsfn(sinphi) / qp;
            cosb1 = sqrt(1.0 - sinb1 * sinb1);
        }
        switch (mode) {
            case NORTH_POLE:  // Fall through
            case SOUTH_POLE: {
                dd  = 1.0;
                xmf = ymf = rq;
                break;
            }
            case EQUATORIAL: {
                dd  = 1.0 / rq;
                xmf = 1.0;
                ymf = 0.5 * qp;
                break;
            }
            case OBLIQUE: {
                dd  = cos(latitudeOfOrigin) /
                        (sqrt(1.0 - excentricitySquared * sinphi * sinphi) * rq * cosb1);
                xmf = rq * dd;
                ymf = rq / dd;
                break;
            }
            default: {
                throw new AssertionError(mode);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        set(expected, Provider.LATITUDE_OF_CENTRE,  values, latitudeOfOrigin);
        set(expected, Provider.LONGITUDE_OF_CENTRE, values, centralMeridian);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(final double lambda, final double phi, Point2D ptDst)
            throws ProjectionException
    {
        final double coslam = cos(lambda);
        final double sinlam = sin(lambda);
        final double sinphi = sin(phi);
        double q = qsfn(sinphi);
        final double sinb, cosb, b, c, x, y;
        switch (mode) {
            case OBLIQUE: {
                sinb = q / qp;
                cosb = sqrt(1.0 - sinb * sinb);
                c    = 1.0 + sinb1 * sinb + cosb1 * cosb * coslam;
                b    = sqrt(2.0 / c);
                y    = ymf * b * (cosb1 * sinb - sinb1 * cosb * coslam);
                x    = xmf * b * cosb * sinlam;
                break;
            }
            case EQUATORIAL: {
                sinb = q / qp;
                cosb = sqrt(1.0 - sinb * sinb);
                c    = 1.0 + cosb * coslam;
                b    = sqrt(2.0 / c);
                y    = ymf * b * sinb;
                x    = xmf * b * cosb * sinlam;
                break;
            }
            case NORTH_POLE: {
                c = (PI / 2) + phi;
                q = qp - q;
                if (q >= 0.0) {
                    b = sqrt(q);
                    x = b * sinlam;
                    y = coslam * -b;
                } else {
                    x = y = 0.;
                }
                break;
            }
            case SOUTH_POLE: {
                c = phi - (PI / 2);
                q = qp + q;
                if (q >= 0.0) {
                    b = sqrt(q);
                    x = b * sinlam;
                    y = coslam * +b;
                } else {
                    x = y = 0.;
                }
                break;
            }
            default: {
                throw new AssertionError(mode);
            }
        }
        if (abs(c) < EPSILON_LATITUDE) {
            throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
     * and stores the result in {@code ptDst}.
     */
    @Override
    @SuppressWarnings("fallthrough")
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        final double lambda, phi;
        switch (mode) {
            case EQUATORIAL: // Fall through
            case OBLIQUE: {
                x /= dd;
                y *= dd;
                final double rho = hypot(x, y);
                if (rho < FINE_EPSILON) {
                    lambda = 0.0;
                    phi = latitudeOfOrigin;
                } else {
                    double sCe, cCe, q, ab;
                    sCe = 2.0 * asin(0.5 * rho / rq);
                    cCe = cos(sCe);
                    sCe = sin(sCe);
                    x *= sCe;
                    if (mode == OBLIQUE) {
                        ab = cCe * sinb1 + y * sCe * cosb1 / rho;
                        q  = qp * ab;
                        y  = rho * cosb1 * cCe - y * sinb1 * sCe;
                    } else {
                        ab = y * sCe / rho;
                        q  = qp * ab;
                        y  = rho * cCe;
                    }
                    lambda = atan2(x, y);
                    phi = authlat(asin(ab));
                }
                break;
            }
            case NORTH_POLE: {
                y = -y;
                // Fall through
            }
            case SOUTH_POLE: {
                final double q = x*x + y*y;
                if (q == 0) {
                    lambda = 0.;
                    phi = latitudeOfOrigin;
                } else {
                    double ab = 1.0 - q / qp;
                    if (mode == SOUTH_POLE) {
                        ab = -ab;
                    }
                    lambda = atan2(x, y);
                    phi = authlat(asin(ab));
                }
                break;
            }
            default: {
                throw new AssertionError(mode);
            }
        }
        if (ptDst != null) {
            ptDst.setLocation(lambda, phi);
            return ptDst;
        }
        return new Point2D.Double(lambda, phi);
    }


    /**
     * Provides the transform equations for the spherical case.
     *
     * @version $Id$
     * @author Martin Desruisseaux
     */
    private static final class Spherical extends LambertAzimuthalEqualArea {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 2091431369806844342L;

        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param  parameters The parameter values in standard units.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        protected Spherical(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            super(parameters);
            ensureSpherical();
        }

        /**
         * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
         * (units in radians) and stores the result in {@code ptDst} (linear distance
         * on a unit sphere).
         */
        @Override
        protected Point2D transformNormalized(final double lambda, final double phi, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.transformNormalized(lambda, phi, ptDst)) != null;

            final double sinphi = sin(phi);
            final double cosphi = cos(phi);
            final double coslam = cos(lambda);
            double x,y;
            switch (mode) {
                case EQUATORIAL: {
                    y = 1.0 + cosphi * coslam;
                    if (y <= FINE_EPSILON) {
                        throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                    }
                    y  = sqrt(2.0 / y);
                    x  = y * cosphi * sin(lambda);
                    y *= sinphi;
                    break;
                }
                case OBLIQUE: {
                    y = 1.0 + sinb1 * sinphi + cosb1 * cosphi * coslam;
                    if (y <= FINE_EPSILON) {
                        throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                    }
                    y  = sqrt(2.0 / y);
                    x  = y * cosphi * sin(lambda);
                    y *= cosb1 * sinphi - sinb1 * cosphi * coslam;
                    break;
                }
                case NORTH_POLE: {
                    if (abs(phi + latitudeOfOrigin) < EPSILON_LATITUDE) {
                        throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                    }
                    y = (PI/4) - phi * 0.5;
                    y = 2.0 * sin(y);
                    x = y * sin(lambda);
                    y *= -coslam;
                    break;
                }
                case SOUTH_POLE: {
                    if (abs(phi + latitudeOfOrigin) < EPSILON_LATITUDE) {
                        throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                    }
                    y = (PI/4) - phi * 0.5;
                    y = 2.0 * cos(y);
                    x = y * sin(lambda);
                    y *= +coslam;
                    break;
                }
                default: {
                    throw new AssertionError(mode);
                }
            }
            assert checkTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinate
         * and stores the result in {@code ptDst} using equations for a sphere.
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            double lambda, phi;
            final double rh = hypot(x, y);
            phi = rh * 0.5;
            if (phi > 1.0) {
                throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
            }
            phi = 2.0 * asin(phi);
            switch (mode) {
                case EQUATORIAL: {
                    final double sinz = sin(phi);
                    final double cosz = cos(phi);
                    phi = abs(rh) <= FINE_EPSILON ? 0.0 : asin(y * sinz / rh);
                    x *= sinz;
                    y = cosz * rh;
                    lambda = (y == 0) ? 0.0 : atan2(x, y);
                    break;
                }
                case OBLIQUE: {
                    final double sinz = sin(phi);
                    final double cosz = cos(phi);
                    phi = abs(rh) <= FINE_EPSILON ? latitudeOfOrigin :
                            asin(cosz * sinb1 + y * sinz * cosb1 / rh);
                    x *= sinz * cosb1;
                    y = (cosz - sin(phi) * sinb1) * rh;
                    lambda = (y == 0) ? 0.0 : atan2(x, y);
                    break;
                }
                case NORTH_POLE: {
                    phi = (PI / 2) - phi;
                    lambda = atan2(x, -y);
                    break;
                }
                case SOUTH_POLE: {
                    phi -= (PI / 2);
                    lambda = atan2(x, y);
                    break;
                }
                default: {
                    throw new AssertionError(mode);
                }
            }
            assert checkInverseTransform(lambda, phi, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(lambda, phi);
                return ptDst;
            }
            return new Point2D.Double(lambda, phi);
        }
    }

    /**
     * Calculates <var>q</var>, Snyder equation (3-12)
     *
     * @param sinphi sin of the latitude <var>q</var> is calculated for.
     * @return <var>q</var> from Snyder equation (3-12).
     */
    private double qsfn(final double sinphi) {
        if (excentricity >= EPSILON) {
            final double con = excentricity * sinphi;
            return ((1.0 - excentricitySquared) * (sinphi / (1.0 - con*con) -
                    (0.5 / excentricity) * log((1.0 - con) / (1.0 + con))));
        } else {
            return sinphi + sinphi;
        }
    }

    /**
     * Determines latitude from authalic latitude.
     */
    private double authlat(final double beta) {
        final double t = beta + beta;
        return beta + APA0 * sin(t) + APA1 * sin(t+t) + APA2 * sin(t+t+t);
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
     * provider} for an {@linkplain LambertAzimuthalEqualArea Lambert Equal Area} projection
     * (EPSG code 9820).
     *
     * @since 2.4
     * @version $Id$
     * @author Beate Stollberg
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 3877793025552244132L;

        /**
         * The operation parameter descriptor for the {@link #latitudeOfOrigin}
         * parameter value. Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor LATITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "latitude_of_center"),
                    new NamedIdentifier(Citations.EPSG,     "Latitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG,     "Spherical latitude of origin"),
                    new NamedIdentifier(Citations.ESRI,     "Latitude_Of_Origin"),
                    new NamedIdentifier(Citations.GEOTIFF,  "ProjCenterLat")
                },
                0, -90, 90, NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #centralMeridian}
         * parameter value. Valid values range is from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor LONGITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC,      "longitude_of_center"),
                    new NamedIdentifier(Citations.EPSG,     "Longitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG,     "Spherical longitude of origin"),
                    new NamedIdentifier(Citations.ESRI,     "Central_Meridian"),
                    new NamedIdentifier(Citations.GEOTIFF,  "ProjCenterLong")
                },
                0, -180, 180, NonSI.DEGREE_ANGLE);

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
            new NamedIdentifier(Citations.OGC,     "Lambert_Azimuthal_Equal_Area"),
            new NamedIdentifier(Citations.EPSG,    "Lambert Azimuthal Equal Area"),
            new NamedIdentifier(Citations.EPSG,    "Lambert Azimuthal Equal Area (Spherical)"),
            new NamedIdentifier(Citations.GEOTIFF, "CT_LambertAzimEqualArea"),
            new NamedIdentifier(Citations.EPSG,    "9820"),
        },  new ParameterDescriptor[] {
                SEMI_MAJOR,         SEMI_MINOR,
                LATITUDE_OF_CENTRE, LONGITUDE_OF_CENTRE,
                FALSE_EASTING,      FALSE_NORTHING
        });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            return isSpherical(parameters) ? new Spherical(parameters) :
                    new LambertAzimuthalEqualArea(parameters);
        }
    }
}
