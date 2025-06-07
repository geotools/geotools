/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.operation.projection;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.awt.geom.Point2D;
import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * World Van der Grinten I projection. The Van der Grinten I is the most popular Van der Grinten projection and often
 * referenced as the Van der Grinten projection. It is a circular projection that is neither conformal nor equal-area.
 *
 * @see <A HREF="http://en.wikipedia.org/wiki/Van_der_Grinten_projection">Van der Grinten projection on Wikipedia</A>
 * @author Simon Nyvlt (ars navigandi)
 */
@SuppressWarnings("FloatingPointLiteralPrecision")
public class WorldVanDerGrintenI extends MapProjection {

    /** */
    private static final long serialVersionUID = -4432651736803211463L;

    private static final double TOL = 1.e-10;
    private static final double THIRD = .33333333333333333333;
    private static final double C2_27 = .07407407407407407407;
    private static final double PI4_3 = 4.18879020478639098458;
    private static final double PISQ = 9.86960440108935861869;
    private static final double TPISQ = 19.73920880217871723738;
    private static final double HPISQ = 4.93480220054467930934;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param values The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    public WorldVanDerGrintenI(ParameterValueGroup values) throws ParameterNotFoundException {
        super(values);
    }

    public WorldVanDerGrintenI(ParameterValueGroup values, Collection<GeneralParameterDescriptor> expected)
            throws ParameterNotFoundException {
        super(values, expected);
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
    protected Point2D transformNormalized(double lambda, double phi, Point2D ptDst) throws ProjectionException {

        double al, al2, g, g2, x, y;

        double p2 = abs(phi / (PI / 2));
        if (p2 - TOL > 1.) throw new ProjectionException();
        if (p2 > 1.) p2 = 1.;
        if (abs(phi) <= TOL) {
            x = lambda;
            y = 0.;
        } else if (abs(lambda) <= TOL || abs(p2 - 1.) < TOL) {
            x = 0.;
            y = PI * tan(.5 * asin(p2));
            if (phi < 0) {
                y = -y;
            }
        } else {
            al = .5 * abs(PI / lambda - lambda / PI);
            al2 = al * al;
            g = sqrt(1. - p2 * p2);
            g = g / (p2 + g - 1.);
            g2 = g * g;
            p2 = g * (2. / p2 - 1.);
            p2 = p2 * p2;
            x = g - p2;
            g = p2 + al2;
            x = PI * (al * x + sqrt(al2 * x * x - g * (g2 - p2))) / g;
            if (lambda < 0.) x = -x;
            y = abs(x / PI);
            y = 1. - y * (y + 2. * al);
            if (y < -TOL) throw new ProjectionException();
            if (y < 0.) y = 0.;
            else y = sqrt(y) * (phi < 0. ? -PI : PI);
        }

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {

        double t, c0, c1, c2, c3, r2, r, ay, lambda, phi;

        double x2 = x * x;
        if ((ay = abs(y)) < TOL) {
            phi = 0.;
            t = x2 * x2 + TPISQ * (x2 + HPISQ);
            lambda = abs(x) <= TOL ? 0. : .5 * (x2 - PISQ + sqrt(t)) / x;
            if (ptDst != null) {
                ptDst.setLocation(lambda, phi);
                return ptDst;
            }
            return new Point2D.Double(lambda, phi);
        }

        double y2 = y * y;
        r = x2 + y2;
        r2 = r * r;
        c1 = -PI * ay * (r + PISQ);
        c3 = r2 + (PI * 2) * (ay * r + PI * (y2 + PI * (ay + (PI / 2))));
        c2 = c1 + PISQ * (r - 3. * y2);
        c0 = PI * ay;
        c2 /= c3;
        double al = c1 / c3 - THIRD * c2 * c2;
        double m = 2. * sqrt(-THIRD * al);
        double d = C2_27 * c2 * c2 * c2 + (c0 * c0 - THIRD * c2 * c1) / c3;

        if (((t = abs(d = 3. * d / (al * m))) - TOL) <= 1.) {
            d = t > 1. ? (d > 0. ? 0. : PI) : acos(d);
            phi = PI * (m * cos(d * THIRD + PI4_3) - THIRD * c2);
            if (y < 0.) phi = -phi;
            t = r2 + TPISQ * (x2 - y2 + HPISQ);
            lambda = abs(x) <= TOL ? 0. : .5 * (r - PISQ + (t <= 0. ? 0. : sqrt(t))) / x;
        } else throw new ProjectionException();

        if (ptDst != null) {
            ptDst.setLocation(lambda, phi);
            return ptDst;
        }
        return new Point2D.Double(lambda, phi);
    }

    public static class Provider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -4432651736803211463L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "World_Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.GEOTOOLS, "World_Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.ESRI, "World_Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.GEOTIFF, "World_Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.OGC, "Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.GEOTOOLS, "Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.GEOTIFF, "Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.ESRI, "Van_der_Grinten_I"),
                    new NamedIdentifier(Citations.OGC, "CT_VanDerGrinten"),
                    new NamedIdentifier(Citations.GEOTOOLS, "CT_VanDerGrinten"),
                    new NamedIdentifier(Citations.GEOTIFF, "CT_VanDerGrinten"),
                    new NamedIdentifier(Citations.ESRI, "CT_VanDerGrinten"),
                    new NamedIdentifier(Citations.EPSG, "54029"),
                    new NamedIdentifier(Citations.ESRI, "54029"),
                    new NamedIdentifier(Citations.OGC, "Van der Grinten WGS84"),
                    new NamedIdentifier(Citations.GEOTOOLS, "Van der Grinten WGS84"),
                    new NamedIdentifier(Citations.GEOTIFF, "Van der Grinten WGS84"),
                    new NamedIdentifier(Citations.ESRI, "Van der Grinten WGS84"),
                    new NamedIdentifier(Citations.EPSG, "Van der Grinten WGS84"),
                    new NamedIdentifier(Citations.OGC, "Van der Grinten"),
                    new NamedIdentifier(Citations.GEOTOOLS, "Van der Grinten"),
                    new NamedIdentifier(Citations.GEOTIFF, "Van der Grinten"),
                    new NamedIdentifier(Citations.ESRI, "Van der Grinten"),
                    new NamedIdentifier(Citations.EPSG, "Van der Grinten"),
                    new NamedIdentifier(Citations.PROJ, "vandg")
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
            return new WorldVanDerGrintenI(parameters);
        }
    }
}
