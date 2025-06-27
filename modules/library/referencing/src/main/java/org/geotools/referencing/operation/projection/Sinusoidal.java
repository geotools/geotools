/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

import java.awt.geom.Point2D;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Sinusoidal (Sanson–Flamsteed) projection
 *
 * @see <A HREF="http://en.wikipedia.org/wiki/Sinusoidal_projection">Sinusoidal projection on Wikipedia</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/sinusoidal.html">"Sinusoidal" on RemoteSensing.org</A>
 * @since 14.0
 * @author Mihail Andreev
 */
public class Sinusoidal extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = 7528023862968814860L;

    private static final double EPS10 = 1e-10;
    private static final double HALFPI = Math.PI / 2.;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Sinusoidal(final ParameterValueGroup parameters) throws ParameterNotFoundException {
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
    protected Point2D transformNormalized(double lam, double phi, final Point2D ptDst) throws ProjectionException {
        double x, y;

        if (isSpherical) {
            x = lam * cos(phi);
            y = phi;
        } else {
            double s = sin(phi);
            double c = cos(phi);

            y = mlfn(phi, sin(phi), cos(phi));
            x = lam * c / sqrt(1. - excentricitySquared * s * s);
        }

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        } else {
            return new Point2D.Double(x, y);
        }
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst) throws ProjectionException {
        double phi;
        double lam;

        if (isSpherical) {
            phi = y;
            lam = x / cos(y);
        } else {
            phi = inv_mlfn(y);
            double s = Math.abs(phi);
            double diff = Math.abs(s - HALFPI);

            if (diff < EPS10) {
                lam = 0.;
            } else if (s < HALFPI) {
                s = sin(phi);
                lam = x * sqrt(1. - excentricitySquared * s * s) / cos(phi) % Math.PI;
            } else {
                throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                // throw new ProjectionException("Tolerance error occurred appling inverse
                // Sinusoidal projection");
            }
        }

        if (ptDst != null) {
            ptDst.setLocation(lam, phi);
            return ptDst;
        } else {
            return new Point2D.Double(lam, phi);
        }
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for the
     * Sinusoidal projection (not part of the EPSG database).
     *
     * @since 14.0
     * @author Mihail Andreev
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = 8374488793001927036L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Sinusoidal"),
                    new NamedIdentifier(Citations.ESRI, "Sinusoidal"),
                    new NamedIdentifier(Citations.PROJ, "sinu")
                },
                new ParameterDescriptor[] {SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, FALSE_EASTING, FALSE_NORTHING});

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
            return new Sinusoidal(parameters);
        }
    }
}
