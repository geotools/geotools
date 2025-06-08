/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.Point2D;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

@SuppressWarnings("FloatingPointLiteralPrecision")
public class CylindricalEqualArea extends MapProjection {

    private static final double DTR = Math.PI / 180.0;
    private static final double HALFPI = Math.PI / 2.;
    private double qp;
    private double[] apa;
    private double es;
    private double e;
    private double one_es;
    private double trueScaleLatitude;
    private double trueScaleLatitudeDeg;

    protected CylindricalEqualArea(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);

        // Set trueScaleLatitude ("lat_ts" in Proj4)
        trueScaleLatitudeDeg = parameters.parameter("standard_parallel_1").doubleValue();
        trueScaleLatitude = DTR * trueScaleLatitude;

        es = excentricitySquared;
        e = excentricity;
        one_es = 1 - excentricitySquared;

        double t = trueScaleLatitude;
        scaleFactor = Math.cos(t);
        if (es != 0) {
            t = Math.sin(t);
            scaleFactor /= Math.sqrt(1. - es * t * t);
            e = Math.sqrt(es); // P->e = sqrt(P->es);
            apa = ProjectionMath.authset(es);
            qp = ProjectionMath.qsfn(1., e, one_es);
        }
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        values.parameter("standard_parallel_1").setValue(trueScaleLatitudeDeg);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code xy} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(double lam, double phi, final Point2D xy) throws ProjectionException {
        double x, y;
        if (isSpherical) {
            x = scaleFactor * lam;
            y = Math.sin(phi) / scaleFactor;
        } else {
            x = scaleFactor * lam;
            y = .5 * ProjectionMath.qsfn(Math.sin(phi), e, one_es) / scaleFactor;
        }
        if (xy != null) {
            xy.setLocation(x, y);
            return xy;
        } else {
            return new Point2D.Double(x, y);
        }
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinates and stores the result in {@code lp}. */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D lp) throws ProjectionException {
        double lam, phi;
        if (isSpherical) {

            double t = Math.abs(y *= scaleFactor);
            // if (t - EPS <= 1.) {//if (true) { //if ((t = fabs(xy.y *= P->k0)) - EPS <= 1.) {
            if (t >= 1.) {
                phi = y < 0. ? -HALFPI : HALFPI;
            } else {
                phi = Math.asin(y);
            }
            lam = x / scaleFactor;
        } else {
            phi = ProjectionMath.authlat(Math.asin(2. * y * scaleFactor / qp), apa);
            lam = x / scaleFactor;
        }

        if (lp != null) {
            lp.setLocation(lam, phi);
            return lp;
        } else {
            return new Point2D.Double(lam, phi);
        }
    }

    public static class Provider extends AbstractProvider {

        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Cylindrical_Equal_Area"),
                    new NamedIdentifier(Citations.OGC, "Cylindrical_Equal_Area"),
                    // new NamedIdentifier(Citations.EPSG,     "Cylindrical_Equal_Area"),
                    new NamedIdentifier(Citations.ESRI, "Cylindrical_Equal_Area"),
                    new NamedIdentifier(Citations.GEOTIFF, "CT_CylindricalEqualArea"),
                    new NamedIdentifier(Citations.PROJ, "cea")
                },
                getParameterDescriptors());

        public Provider() {
            super(PARAMETERS);
        }

        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException {
            return new CylindricalEqualArea(parameters);
        }

        /* PROJ4 Definition for Cylindrical Equal Area (CAE):
            +proj=cea
            +lon_0=Central Meridian
            +lat_ts=Standard Parallel
            +x_0=False Easting
            +y_0=False Northing

        */
        protected static ParameterDescriptor[] getParameterDescriptors() {
            return new ParameterDescriptor[] {
                SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, STANDARD_PARALLEL_1, FALSE_EASTING, FALSE_NORTHING
            };
        }
    }

    public static class BehrmannProvider extends AbstractProvider {

        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Behrmann"), new NamedIdentifier(Citations.ESRI, "54017")
                },
                Provider.getParameterDescriptors());

        public BehrmannProvider() {
            super(PARAMETERS);
        }

        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException {
            parameters.parameter("standard_parallel_1").setValue(30);
            return new CylindricalEqualArea(parameters);
        }
    }

    public static class LambertCylindricalEqualAreaProvider extends AbstractProvider {

        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.GEOTOOLS, "Lambert Cylindrical Equal Area (Spherical)"),
                    new NamedIdentifier(Citations.PROJ, "cea")
                },
                Provider.getParameterDescriptors());

        public LambertCylindricalEqualAreaProvider() {
            super(PARAMETERS);
        }

        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException {
            return new CylindricalEqualArea(parameters);
        }
    }

    // **************************************************************************
    // ** ProjectionMath
    // **************************************************************************
    /**
     * Adopted from Proj4j. Original source code can be found here:
     * http://trac.osgeo.org/proj4j/browser/trunk/src/main/java/org/osgeo/proj4j/util/ProjectionMath.java
     */
    private static class ProjectionMath {

        private static final double P00 = .33333333333333333333;
        private static final double P01 = .17222222222222222222;
        private static final double P02 = .10257936507936507936;
        private static final double P10 = .06388888888888888888;
        private static final double P11 = .06640211640211640211;
        private static final double P20 = .01641501294219154443;

        public static double[] authset(double es) {
            double[] APA = new double[3];
            APA[0] = es * P00;
            double t = es * es;
            APA[0] += t * P01;
            APA[1] = t * P10;
            t *= es;
            APA[0] += t * P02;
            APA[1] += t * P11;
            APA[2] = t * P20;
            return APA;
        }

        public static double qsfn(double sinphi, double e, double one_es) {
            double con;

            if (e >= 1.0e-7) {
                con = e * sinphi;
                return one_es * (sinphi / (1. - con * con) - .5 / e * Math.log((1. - con) / (1. + con)));
            } else return sinphi + sinphi;
        }

        public static double authlat(double beta, double[] APA) {
            double t = beta + beta;
            return beta + APA[0] * Math.sin(t) + APA[1] * Math.sin(t + t) + APA[2] * Math.sin(t + t + t);
        }
    }
}
