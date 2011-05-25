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
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.metadata.iso.citation.Citations;

import static java.lang.Math.*;


/**
 * Provides the transform equations for the Oblique Stereographic (EPSG code 9809).
 * The formulas used below are not from the EPSG, but rather those of the
 * "Oblique Stereographic Alternative" in the {@code libproj4} package
 * written by Gerald Evenden. His work is acknowledged here and greatly appreciated.
 * <p>
 * The forward equations used in {@code libproj4} are the same as those given in the
 * UNB reports for the Double Stereographic. The inverse equations are similar,
 * but use different methods to iterate for the latitude.
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>{@code libproj4} is available at
 *       <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/">libproj4 Miscellanea</A><br>
 *        Relevent files are: {@code PJ_sterea.c}, {@code pj_gauss.c},
 *        {@code pj_fwd.c}, {@code pj_inv.c} and {@code lib_proj.h}</li>
 *   <li>Gerald Evenden. <A HREF="http://members.bellatlantic.net/~vze2hc4d/proj4/sterea.pdf">
 *       "Supplementary PROJ.4 Notes - Oblique Stereographic Alternative"</A></li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",
 *       EPSG Guidence Note Number 7, Version 19.</li>
 *   <li>Krakiwsky, E.J., D.B. Thomson, and R.R. Steeves. 1977. A Manual
 *       For Geodetic Coordinate Transformations in the Maritimes.
 *       Geodesy and Geomatics Engineering, UNB. Technical Report No. 48.</li>
 *   <li>Thomson, D.B., M.P. Mepham and R.R. Steeves. 1977.
 *       The Stereographic Double Projection.
 *       Surveying Engineering, University of New Brunswick. Technical Report No. 46.</li>
 * </ul>
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Gerald I. Evenden (for original code in Proj4)
 * @author Rueben Schulz
 */
public class ObliqueStereographic extends StereographicUSGS {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = -1454098847621943639L;

    /*
     * The tolerance used for the inverse iteration. This is smaller
     * than the tolerance in the {@code StereographicOblique} superclass.
     */
    private static final double ITERATION_TOLERANCE = 1E-14;

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /*
     * Contstants used in the forward and inverse gauss methods.
     */
    private final double C, K, ratexp;

    /*
     * Constants for the EPSG stereographic transform.
     */
    private final double phic0, cosc0, sinc0, R2;

    /**
     * Constructs an oblique stereographic projection (EPSG equations).
     *
     * @param  parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected ObliqueStereographic(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        this(parameters, Provider.PARAMETERS);
    }

    /**
     * Constructs an oblique stereographic projection (EPSG equations).
     *
     * @param  parameters The group of parameter values.
     * @param  descriptor The expected parameter descriptor.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    ObliqueStereographic(final ParameterValueGroup parameters,
                         final ParameterDescriptorGroup descriptor)
            throws ParameterNotFoundException
    {
        super(parameters, descriptor);

        // Compute constants
        final double sphi = sin(latitudeOfOrigin);
        double       cphi = cos(latitudeOfOrigin);
        cphi  *= cphi;
        R2     = 2.0 * sqrt(1-excentricitySquared) / (1-excentricitySquared * sphi * sphi);
        C      = sqrt(1. + excentricitySquared * cphi * cphi / (1. - excentricitySquared));
        phic0  = asin(sphi / C);
        sinc0  = sin(phic0);
        cosc0  = cos(phic0);
        ratexp = 0.5 * C * excentricity;
        K      = tan(0.5 * phic0 + PI/4) /
                (pow(tan(0.5 * latitudeOfOrigin + PI/4), C) * srat(excentricity * sphi, ratexp));
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
        // Compute using USGS formulas, for comparaison later.
        assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;

        y = 2.0 * atan(K * pow(tan(0.5 * y + PI/4), C) * srat(excentricity * sin(y), ratexp)) - PI/2;
        x *= C;
        final double sinc = sin(y);
        final double cosc = cos(y);
        final double cosl = cos(x);
        final double k = R2 / (1.0 + sinc0 * sinc + cosc0 * cosc * cosl);
        x = k * cosc * sin(x);
        y = k * (cosc0 * sinc - sinc0 * cosc * cosl);

        assert checkTransform(x, y, ptDst, 0.1);
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
        // Compute using USGS formulas, for comparaison later.
        assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;
        final double rho = hypot(x, y);
        if (abs(rho) < EPSILON) {
            x = 0.0;
            y = phic0;
        } else {
            final double ce   = 2.0 * atan2(rho, R2);
            final double sinc = sin(ce);
            final double cosc = cos(ce);
            x = atan2(x * sinc, rho * cosc0 * cosc - y * sinc0 * sinc);
            y = (cosc * sinc0) + (y * sinc * cosc0 / rho);

            if (abs(y) >= 1.0) {
                y = (y < 0.0) ? -PI/2.0 : PI/2.0;
            } else {
                y = asin(y);
            }
        }
        // Begin pj_inv_gauss(...) method inlined
        x /= C;
        double num = pow(tan(0.5 * y + PI/4)/K, 1.0/C);
        for (int i=MAXIMUM_ITERATIONS;;) {
            double phi = 2.0 * atan(num * srat(excentricity * sin(y), -0.5 * excentricity)) - PI/2;
            if (abs(phi - y) < ITERATION_TOLERANCE) {
                break;
            }
            y = phi;
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
        }
        // End pj_inv_gauss(...) method inlined

        // TODO: the tolerance in the following assertion is quite large for
        //       an angle in radians. We should check if this is normal.
        assert checkInverseTransform(x, y, ptDst, 0.01);
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * A simple function used by the transforms.
     */
    private static double srat(double esinp, double exp) {
        return pow((1.0 - esinp) / (1.0 + esinp), exp);
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
     * provider} for a stereographic projection of any kind. The equations used are the one from
     * EPSG.
     *
     * @since 2.4
     * @source $URL$
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static final class Provider extends Stereographic.Provider {
        /**
         * For compatibility with different versions during deserialization.
         */
        private static final long serialVersionUID = 6505988910141381354L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Oblique_Stereographic"),
                new NamedIdentifier(Citations.EPSG,     "Oblique Stereographic"),
                new NamedIdentifier(Citations.EPSG,     "Roussilhe"),
                new NamedIdentifier(Citations.EPSG,     "9809"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_ObliqueStereographic"),
                new NamedIdentifier(Citations.ESRI,     "Double_Stereographic"),
                new NamedIdentifier(Citations.GEOTOOLS, NAME)
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
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
         * Creates the general case.
         */
        @Override
        MathTransform createMathTransform(final ParameterValueGroup parameters,
                                          final ParameterDescriptorGroup descriptor)
                throws ParameterNotFoundException
        {
            return new ObliqueStereographic(parameters, descriptor);
        }
    }
}
