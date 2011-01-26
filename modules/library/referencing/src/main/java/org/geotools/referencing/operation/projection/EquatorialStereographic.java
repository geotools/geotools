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
 * The USGS equatorial case of the {@linkplain Stereographic stereographic} projection.
 * This is a special case of oblique stereographic projection for
 * {@linkplain #latitudeOfOrigin latitude of origin} == 0.0.
 *
 * @since 2.4
 * @source $URL$
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
public class EquatorialStereographic extends StereographicUSGS {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -5098015759558831875L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * A constant used in the transformations.
     * This is <strong>not</strong> equal to the {@link #scaleFactor}.
     */
    static final double k0 = 2;

    /**
     * Constructs an equatorial stereographic projection (EPSG equations).
     *
     * @param  parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected EquatorialStereographic(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, Stereographic.Provider.PARAMETERS);
    }

    /**
     * Constructs an equatorial stereographic projection (USGS equations).
     *
     * @param  parameters The group of parameter values.
     * @param  descriptor The expected parameter descriptor.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    EquatorialStereographic(final ParameterValueGroup parameters,
                            final ParameterDescriptorGroup descriptor)
            throws ParameterNotFoundException
    {
        super(parameters, descriptor);
        assert super.k0 == k0 : super.k0;
        latitudeOfOrigin = 0.0;
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
        // Compute using oblique formulas, for comparaison later.
        assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;

        final double chi = 2.0 * atan(ssfn(y, sin(y))) - PI/2;
        final double cosChi = cos(chi);
        final double A = k0 / (1.0 + cosChi * cos(x));    // typo in (12-29)
        x = A * cosChi * sin(x);
        y = A * sin(chi);

        assert checkTransform(x, y, ptDst);
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }


    /**
     * Provides the transform equations for the spherical case of the
     * equatorial stereographic projection.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     */
    static final class Spherical extends EquatorialStereographic {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -4790138052004333003L;

        /**
         * Constructs a spherical equatorial stereographic projection (USGS equations).
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
            double f = 1.0 + coslat * cos(x);
            if (f < EPSILON) {
                throw new ProjectionException(ErrorKeys.VALUE_TEND_TOWARD_INFINITY);
            }
            f = k0 / f;              // (21-14)
            x = f * coslat * sin(x); // (21-2)
            y = f * sin(y);          // (21-13)

            assert checkTransform(x, y, ptDst);
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
        protected Point2D inverseTransformNormalized(double x, double y,Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            final double rho = hypot(x, y);
            if (abs(rho) < EPSILON) {
                y = 0.0; // latitudeOfOrigin
                x = 0.0;
            } else {
                final double c = 2.0 * atan(rho / k0);
                final double cosc = cos(c);
                final double sinc = sin(c);
                y = asin(y * sinc/rho); // (20-14)  with phi1=0
                final double t  = x*sinc;
                final double ct = rho*cosc;
                x = (abs(t) < EPSILON && abs(ct) < EPSILON) ? 0.0 : atan2(t, ct);
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
