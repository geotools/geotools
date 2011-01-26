/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 1999-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.geotools.resources.i18n.ErrorKeys;

import static java.lang.Math.*;


/**
 * The USGS oblique/equatorial case of the Stereographic projection. This is similar but
 * <strong>NOT</strong> equal to EPSG code 9809 ({@code "Oblique_Stereographic"} EPSG name).
 * The later is rather implemented by {@link ObliqueStereographic}.
 * <p>
 * This class is not public in order to keep names that closely match the ones in common usage
 * (i.e. this projection is called just "Stereographic" in ESRI). Furthermore, the "USGS" name
 * is not really accurate for a class to be extended by {@link ObliqueStereographic}.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author Gerald I. Evenden (for original code in Proj4)
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
class StereographicUSGS extends Stereographic {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = 948619442800459871L;

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;

    /**
     * Difference allowed in iterative computations.
     */
    private static final double ITERATION_TOLERANCE = 1E-10;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Constants used for the oblique projections. All those constants are completly determined by
     * {@link #latitudeOfOrigin}. Concequently, there is no need to test them in {@link #hashCode}
     * or {@link #equals} methods.
     */
    final double k0, sinphi0, cosphi0, chi1, sinChi1, cosChi1;

    /**
     * Constructs an oblique stereographic projection (USGS equations).
     *
     * @param  parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected StereographicUSGS(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, Provider.PARAMETERS);
    }

    /**
     * Constructs an oblique stereographic projection (USGS equations).
     *
     * @param  parameters The group of parameter values.
     * @param  descriptor The expected parameter descriptor.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    StereographicUSGS(final ParameterValueGroup parameters,
                      final ParameterDescriptorGroup descriptor)
            throws ParameterNotFoundException
    {
        super(parameters, descriptor);
        if (abs(latitudeOfOrigin) < EPSILON) { // Equatorial
            latitudeOfOrigin = 0;
            cosphi0 = 1.0;
            sinphi0 = 0.0;
            chi1    = 0.0;
            cosChi1 = 1.0;
            sinChi1 = 0.0;
        } else {  // Oblique
            cosphi0 = cos(latitudeOfOrigin);
            sinphi0 = sin(latitudeOfOrigin);
            chi1    = 2.0 * atan(ssfn(latitudeOfOrigin, sinphi0)) - PI/2;
            cosChi1 = cos(chi1);
            sinChi1 = sin(chi1);
        }
        // part of (14 - 15)
        k0 = 2.0 * msfn(sinphi0, cosphi0);
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        final double chi    = 2.0 * atan(ssfn(y, sin(y))) - PI/2;
        final double sinChi = sin(chi);
        final double cosChi = cos(chi);
        final double cosChi_cosLon = cosChi * cos(x);
        final double A = k0 / cosChi1 / (1 + sinChi1*sinChi + cosChi1*cosChi_cosLon);
        x = A * cosChi * sin(x);
        y = A * (cosChi1 * sinChi - sinChi1 * cosChi_cosLon);

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        final double  rho    = hypot(x, y);
        final double  ce     = 2.0 * atan2(rho * cosChi1, k0);
        final double  cosce  = cos(ce);
        final double  since  = sin(ce);
        final boolean rhoIs0 = abs(rho) < EPSILON;
        final double  chi    = rhoIs0 ? chi1 : asin(cosce*sinChi1 + (y*since*cosChi1 / rho));
        final double  tp     = tan(PI/4.0 + chi/2.0);

        // parts of (21-36) used to calculate longitude
        final double t  = x*since;
        final double ct = rho*cosChi1*cosce - y*sinChi1*since;

        // Compute latitude using iterative technique (3-4)
        final double halfe = excentricity / 2.0;
        double phi0 = chi;
        for (int i=MAXIMUM_ITERATIONS;;) {
            final double esinphi = excentricity * sin(phi0);
            final double phi = 2*atan(tp*pow((1+esinphi)/(1-esinphi), halfe)) - PI/2;
            if (abs(phi - phi0) < ITERATION_TOLERANCE) {
                // TODO: checking rho may be redundant
                x = rhoIs0 || (abs(t)<EPSILON && abs(ct)<EPSILON) ? 0.0 : atan2(t, ct);
                y = phi;
                break;
            }
            phi0 = phi;
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Maximal error (in metres) tolerated for assertions, if enabled.
     */
    @Override
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        final double delta = abs(longitude - centralMeridian)/2 +
                             abs(latitude  - latitudeOfOrigin);
        if (delta > 40) {
            return 0.5;
        }
        if (delta > 15) {
            return 0.1;
        }
        return super.getToleranceForAssertions(longitude, latitude);
    }

    /**
     * Computes part of function (3-1) from Snyder.
     */
    final double ssfn(double phi, double sinphi) {
        sinphi *= excentricity;
        return tan(PI/4 + phi/2.0) * pow((1-sinphi) / (1+sinphi), excentricity/2.0);
    }


    /**
     * Provides the transform equations for the spherical case of the
     * Stereographic projection.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     */
    static final class Spherical extends StereographicUSGS {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = -8558594307755820783L;

        /**
         * A constant used in the transformations. This constant hides the {@code k0}
         * constant from the ellipsoidal case. The spherical and ellipsoidal {@code k0}
         * are not computed in the same way, and we preserve the ellipsoidal {@code k0}
         * in {@link Stereographic} in order to allow assertions to work.
         */
        private static final double k0 = 2;

        /**
         * Constructs a spherical oblique stereographic projection.
         *
         * @param  parameters The group of parameter values.
         * @param  descriptor The expected parameter descriptor.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        Spherical(final ParameterValueGroup parameters, final ParameterDescriptorGroup descriptor)
                throws ParameterNotFoundException
        {
            super(parameters, descriptor);
            ensureSpherical();
        }

        /**
         * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
         * (units in radians) and stores the result in {@code ptDst} (linear distance
         * on a unit sphere).
         */
        @Override
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;

            final double coslat = cos(y);
            final double sinlat = sin(y);
            final double coslon = cos(x);
            double f = 1.0 + sinphi0*sinlat + cosphi0*coslat*coslon; // (21-4)
            if (f < EPSILON) {
                throw new ProjectionException(ErrorKeys.VALUE_TEND_TOWARD_INFINITY);
            }
            f = k0 / f;
            x = f * coslat * sin(x);                                // (21-2)
            y = f * (cosphi0 * sinlat - sinphi0 * coslat * coslon); // (21-3)

            assert checkTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
         * and stores the result in {@code ptDst}.
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            final double rho = hypot(x, y);
            if (abs(rho) < EPSILON) {
                y = latitudeOfOrigin;
                x = 0.0;
            } else {
                final double c    = 2.0 * atan(rho/k0);
                final double cosc = cos(c);
                final double sinc = sin(c);
                final double ct   = rho*cosphi0*cosc - y*sinphi0*sinc; // (20-15)
                final double t    = x*sinc;                            // (20-15)
                y = asin(cosc*sinphi0 + y*sinc*cosphi0/rho);           // (20-14)
                x = (abs(ct)<EPSILON && abs(t)<EPSILON) ? 0.0 : atan2(t, ct);
            }
            assert checkInverseTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }
    }
}
