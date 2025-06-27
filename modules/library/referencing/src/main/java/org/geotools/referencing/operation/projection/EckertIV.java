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

import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.geom.Point2D;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Eckert IV projection
 *
 * @see <A HREF="http://mathworld.wolfram.com/EckertIVProjection.html">Robinson projection on MathWorld</A>
 * @see <A HREF="http://www.equal-area-maps.com/info_eckert.php">"Eckert IV" on the Equal Area Maps web site</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/eckert_iv.html">"Robinson" on RemoteSensing.org</A>
 * @since 2.7.0
 * @author Andrea Aime
 */
@SuppressWarnings("FloatingPointLiteralPrecision")
public class EckertIV extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = 1635471013603509976L;

    private static final double C_x = 0.42223820031577120149;

    private static final double C_y = 1.32650042817700232218;

    private static final double C_p = 3.57079632679489661922;

    private static final double EPS = 1e-7;

    private static final int NITER = 6;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected EckertIV(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double lam, double phi, Point2D ptDst) throws ProjectionException {

        double p = C_p * sin(phi);
        double V = phi * phi;
        phi *= 0.895168 + V * (0.0218849 + V * 0.00826809);
        int i = NITER;
        for (; i > 0; --i) {
            double c = cos(phi);
            double s = sin(phi);
            phi -= V = (phi + s * (c + 2d) - p) / (1d + c * (c + 2d) - s * s);
            if (abs(V) < EPS) {
                break;
            }
        }
        if (ptDst == null) {
            ptDst = new Point2D.Double();
        }
        if (i == 0) {
            ptDst.setLocation(C_x * lam, phi < 0. ? -C_y : C_y);
        } else {
            ptDst.setLocation(C_x * lam * (1. + cos(phi)), C_y * sin(phi));
        }
        return ptDst;
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {

        double phi = aasin(y / C_y);
        double c = cos(phi);
        double lam = x / (C_x * (1. + c));
        phi = aasin((phi + sin(phi) * (c + 2.)) / C_p);

        if (ptDst == null) {
            ptDst = new Point2D.Double();
        }
        ptDst.setLocation(lam, phi);

        return ptDst;
    }

    @Override
    protected double getToleranceForAssertions(double longitude, double latitude) {
        // the Robinson projection is meant for world-wide displays, don't be picky
        return 2;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// PROVIDERS ////////
    // ////// ////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for the Eckert
     * IV projection (not part of the EPSG database).
     *
     * @since 2.7.0
     * @author Andrea Aime
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = 1136453952351519284L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Eckert_IV"),
                    new NamedIdentifier(Citations.ESRI, "Eckert_IV"),
                    new NamedIdentifier(Citations.PROJ, "eck4")
                },
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN});

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            parameters
                    .parameter("semi_minor")
                    .setValue(parameters.parameter("semi_major").getValue());
            return new EckertIV(parameters);
        }
    }
}
