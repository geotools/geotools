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

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.geom.Point2D;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.util.SuppressFBWarnings;

/**
 * Mollweide projection
 *
 * @see <A HREF="http://mathworld.wolfram.com/MollweideProjection.html">Mollweide</A>
 * @see <A HREF="http://en.wikipedia.org/wiki/Mollweide_projection">"Mollweide" on Wikipedia</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mollweide.html">"Mollweide" on RemoteSensing.org</A>
 * @since 2.7.0
 * @author Andrea Aime
 */
public class Mollweide extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -737778661392950540L;

    private static final int MAX_ITER = 10;
    private static final double LOOP_TOL = 1e-7;

    double C_x, C_y, C_p;

    static enum ProjectionMode {
        Mollweide,
        WagnerIV,
        WagnerV
    }

    ParameterDescriptorGroup descriptors;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Mollweide(
            ProjectionMode mode, final ParameterDescriptorGroup descriptors, final ParameterValueGroup parameters)
            throws ParameterNotFoundException {
        super(parameters, descriptors.descriptors());
        this.descriptors = descriptors;

        if (mode == ProjectionMode.WagnerV) {
            C_x = 0.90977;
            C_y = 1.65014;
            C_p = 3.00896;
        } else {
            double p;
            if (mode == ProjectionMode.Mollweide) {
                p = PI / 2;
            } else {
                p = PI / 3;
            }
            double p2 = p + p;
            double sp = sin(p);
            double r = sqrt(PI * 2 * sp / (p2 + sin(p2)));
            C_x = 2 * r / PI;
            C_y = r / sp;
            C_p = p2 + sin(p2);
        }
    }

    /** {@inheritDoc} */
    @Override
    @SuppressFBWarnings("UR_UNINIT_READ_CALLED_FROM_SUPER_CONSTRUCTOR")
    public ParameterDescriptorGroup getParameterDescriptors() {
        return descriptors;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double lam, double phi, Point2D ptDst) throws ProjectionException {

        double k = C_p * sin(phi);
        int i = MAX_ITER;
        for (; i > 0; --i) {
            double V = (phi + sin(phi) - k) / (1. + cos(phi));
            phi -= V;
            if (abs(V) < LOOP_TOL) break;
        }
        if (i == 0) {
            phi = phi < 0 ? -PI / 2 : PI / 2;
        } else {
            phi *= 0.5;
        }

        if (ptDst == null) {
            ptDst = new Point2D.Double();
        }
        ptDst.setLocation(C_x * lam * cos(phi), C_y * sin(phi));
        return ptDst;
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {

        double phi = aasin(y / C_y);
        double lam = x / (C_x * cos(phi));
        phi += phi;
        phi = aasin((phi + sin(phi)) / C_p);

        // the above can occasionaly result in lon out of range, normalize it
        lam = rollLongitude(lam);

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
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for the
     * Mollweide projection (not part of the EPSG database).
     *
     * @since 2.7.0
     * @author Andrea Aime
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class MollweideProvider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -2616680275771881688L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Mollweide"),
                    new NamedIdentifier(Citations.ESRI, "Mollweide"),
                    new NamedIdentifier(Citations.PROJ, "moll")
                },
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, FALSE_EASTING, FALSE_NORTHING, CENTRAL_MERIDIAN});

        /** Constructs a new provider. */
        public MollweideProvider() {
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
            return new Mollweide(ProjectionMode.Mollweide, PARAMETERS, parameters);
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for the Wagner
     * IV projection (not part of the EPSG database).
     *
     * @since 2.7.0
     * @author Andrea Aime
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class WagnerIVProvider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = 1079407274370647753L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {new NamedIdentifier(Citations.GEOTOOLS, "Wagner_IV")},
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN});

        /** Constructs a new provider. */
        public WagnerIVProvider() {
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
            // make sure we assume a spherical reference
            parameters
                    .parameter("semi_minor")
                    .setValue(parameters.parameter("semi_major").getValue());
            return new Mollweide(ProjectionMode.WagnerIV, PARAMETERS, parameters);
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for the Wagner
     * V projection (not part of the EPSG database).
     *
     * @since 2.7.0
     * @author Andrea Aime
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class WagnerVProvider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -3583284443974045930L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {new NamedIdentifier(Citations.GEOTOOLS, "Wagner_V")},
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN});

        /** Constructs a new provider. */
        public WagnerVProvider() {

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
            // make sure we assume a spherical reference
            parameters
                    .parameter("semi_minor")
                    .setValue(parameters.parameter("semi_major").getValue());
            return new Mollweide(ProjectionMode.WagnerV, PARAMETERS, parameters);
        }
    }
}
