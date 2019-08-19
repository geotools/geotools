/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import java.awt.geom.Point2D;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.operation.MathTransform;

/**
 * Equal Earth is a projection inspired by the Robinson projection, but unlike the Robinson
 * projection retains the relative size of areas.
 *
 * <p>The projection was designed in 2018 by Bojan Savric, Tom Patterson and Bernhard Jenny.
 *
 * <p>Publication: Bojan Savric, Tom Patterson & Bernhard Jenny (2018). The Equal Earth map
 * projection, International Journal of Geographical Information Science, DOI:
 * 10.1080/13658816.2018.1504949
 */
public class EqualArea extends MapProjection {

    static final double A1 = 1.340264;
    static final double A2 = -0.081106;
    static final double A3 = 0.000893;
    static final double A4 = 0.003796;
    static final double M = Math.sqrt(3) / 2;
    static final double EPSILON = 1e-11;
    static final int MAX_ITER = 12;
    static final double MAX_Y = 1.3173627591574;

    public EqualArea(ParameterValueGroup parameters) {
        super(parameters);
    }

    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException {
        double yc, tol, y2, y6, f, fder;

        /* make sure y is inside valid range */
        if (y > MAX_Y) {
            y = MAX_Y;
        } else if (y < -MAX_Y) {
            y = -MAX_Y;
        }

        yc = y;

        for (int i = MAX_ITER; ; ) {
            /* Newton-Raphson */
            y2 = yc * yc;
            y6 = y2 * y2 * y2;
            f = yc * (A1 + A2 * y2 + y6 * (A3 + A4 * y2)) - y;
            fder = A1 + 3 * A2 * y2 + y6 * (7 * A3 + 9 * A4 * y2);
            tol = f / fder;
            yc -= tol;
            if (Math.abs(tol) < EPSILON) {
                break;
            }
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
        }

        y2 = yc * yc;
        y6 = y2 * y2 * y2;

        double lam = M * x * (A1 + 3 * A2 * y2 + y6 * (7 * A3 + 9 * A4 * y2)) / cos(yc);
        double phi = asin(sin(yc) / M);

        if (ptDst != null) {
            ptDst.setLocation(lam, phi);
            return ptDst;
        }
        return new Point2D.Double(lam, phi);
    }

    @Override
    protected Point2D transformNormalized(double lpLambda, double lpPhi, Point2D ptDst)
            throws ProjectionException {
        double phi = asin(M * sin(lpPhi));
        double phi2 = phi * phi;
        double phi6 = phi2 * phi2 * phi2;

        double x =
                lpLambda * cos(phi) / (M * (A1 + 3 * A2 * phi2 + phi6 * (7 * A3 + 9 * A4 * phi2)));
        double y = phi * (A1 + A2 * phi2 + phi6 * (A3 + A4 * phi2));
        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for an {@linkplain org.geotools.referencing.operation.projection.PlateCarree Plate
     * Carree} projection.
     *
     * @since 2.2
     * @version $Id$
     * @author John Grange
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        private static final long serialVersionUID = -339526664946772642L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS =
                createDescriptorGroup(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.GEOTOOLS, "Equal Earth"),
                        },
                        new ParameterDescriptor[] {
                            SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, FALSE_EASTING, FALSE_NORTHING
                        });

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
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException {
            return new EqualArea(parameters);
        }
    }
}
