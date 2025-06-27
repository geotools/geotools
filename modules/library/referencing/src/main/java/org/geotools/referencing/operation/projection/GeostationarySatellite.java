/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
 *    USGS's work is fully acknowledged here.
 */
package org.geotools.referencing.operation.projection;

import java.awt.geom.Point2D;
import java.util.Collection;
import org.geotools.api.geometry.Bounds;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.geometry.GeneralBounds;
import org.geotools.geometry.Position2D;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import si.uom.SI;

/**
 * The Geostationary Satellite Projection
 *
 * <p>Adapted from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
 *
 * <p>NOTE: Not all valid coordinates in this projection will transform to valid terrestrial coordinates, this is
 * especially true of "Full Disk" earth coverages. If one must deal with coverages in this projection with generalized
 * code which requires the coverage bounding-box coordinates to transform to valid terrestrial values consider clipping
 * to a rectangle inscribing the ellipsoid.
 *
 * @author Tom Kunicki
 */
public abstract class GeostationarySatellite extends MapProjection {

    private static final long serialVersionUID = 1L;

    final double h;
    final double radius_g;
    final double radius_g_1;
    final double C;
    final boolean flip_axis;

    public GeostationarySatellite(ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);

        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();

        // from https://github.com/OSGeo/proj.4/blob/4.9/src/projects.h
        //  a,  /* major axis or radius if es==0 */

        final double a = semiMajor;
        h = doubleValue(expected, Provider.SATELLITE_HEIGHT, parameters);

        // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
        //  P->radius_g_1 = P->h / P->a;
        //  P->radius_g = 1. + P->radius_g_1;
        //  P->C  = P->radius_g * P->radius_g - 1.0;
        radius_g_1 = h / a;
        radius_g = 1d + radius_g_1;
        C = radius_g * radius_g - 1d;

        flip_axis = doubleValue(expected, Provider.SWEEP, parameters) == 0;
    }

    /**
     * Transform a satellite view angle to coordinates in the Geostationary projection. Based on
     * https://github.com/OSGeo/proj.4/blob/5.2/src/PJ_geos.c
     *
     * @param Vx X component of the satellite view vector.
     * @param Vy Y component of the satellite view vector.
     * @param Vz Z component of the satellite view vector.
     * @param xy A point whose location is set to the coordinates in this projection.
     * @return xy for convenience.
     */
    final Point2D transformViewVectorToCoordinates(double Vx, double Vy, double Vz, Point2D xy) {
        double tmp = radius_g - Vx;
        if (flip_axis) {
            xy.setLocation(radius_g_1 * Math.atan(Vy / Math.hypot(Vz, tmp)), radius_g_1 * Math.atan(Vz / tmp));
        } else {
            xy.setLocation(radius_g_1 * Math.atan(Vy / tmp), radius_g_1 * Math.atan(Vz / Math.hypot(Vy, tmp)));
        }
        return xy;
    }

    /**
     * Transforms these coordinates to the initialization vector for calculating the satellite view vector.
     *
     * @param x The X coordinate on Earth.
     * @param y The Y coordinate on Earth.
     * @param yz A point whose location is set to the Y and Z initializers of the view vector.
     * @return yz for convenience.
     */
    final Point2D transformCoordinatesToViewVectorInitializer(double x, double y, Point2D yz) {
        double Vy;
        double Vz;
        if (flip_axis) {
            Vz = Math.tan(y / radius_g_1);
            Vy = Math.tan(x / radius_g_1) * Math.hypot(1., Vz);
        } else {
            Vy = Math.tan(x / radius_g_1);
            Vz = Math.tan(y / radius_g_1) * Math.hypot(1., Vy);
        }
        yz.setLocation(Vy, Vz);
        return yz;
    }

    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final ParameterDescriptorGroup descriptor = getParameterDescriptors();
        final Collection<GeneralParameterDescriptor> expected = descriptor.descriptors();
        set(expected, Provider.SATELLITE_HEIGHT, values, h);
        set(expected, Provider.SWEEP, values, flip_axis ? 0 : 1);
        return values;
    }

    @Override
    public boolean equals(final Object object) {
        if (super.equals(object)) {
            GeostationarySatellite that = (GeostationarySatellite) object;
            // Other parameters are derived from these two, plus others already checked in
            // super.equals().
            return h == that.h && flip_axis == that.flip_axis;
        }
        return false;
    }

    @Override
    public int hashCode() {
        long code = super.hashCode();
        code = code * 37 + Double.doubleToLongBits(h);
        code = code * 37 + Boolean.hashCode(flip_axis);
        return (int) code ^ (int) (code >>> 32);
    }

    public static class Spherical extends GeostationarySatellite {

        private static final long serialVersionUID = 1L;
        final double radius_p;
        final double radius_p2;
        final double radius_p_inv2;

        public Spherical(final ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);

            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            //  P->radius_p = P->radius_p2 = P->radius_p_inv2 = 1.0;
            radius_p = radius_p2 = radius_p_inv2 = 1.;
        }

        @Override
        protected Point2D transformNormalized(double lambda, double phi, Point2D p2d) throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Calculation of the three components of the vector from satellite to
             ** position on earth surface (lon,lat).*/
            double tmp = Math.cos(phi);
            double Vx = Math.cos(lambda) * tmp;
            double Vy = Math.sin(lambda) * tmp;
            double Vz = Math.sin(phi);
            /* Check visibility.*/
            if ((radius_g - Vx) * Vx - Vy * Vy - Vz * Vz < 0.) {
                throw new ProjectionException();
            }
            return transformViewVectorToCoordinates(Vx, Vy, Vz, p2d);
        }

        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D p2d) throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Setting three components of vector from satellite to position.*/
            double Vx = -1.;
            transformCoordinatesToViewVectorInitializer(x, y, p2d);
            double Vy = p2d.getX();
            double Vz = p2d.getY();
            /* Calculation of terms in cubic equation and determinant.*/
            double a = Vy * Vy + Vz * Vz + Vx * Vx;
            double b = 2. * radius_g * Vx;
            double det = b * b - 4. * a * C;
            if (det < 0.) {
                throw new ProjectionException("Det less than 0: " + det);
            }
            /* Calculation of three components of vector from satellite to position.*/
            double k = (-b - Math.sqrt(det)) / (2. * a);
            Vx = radius_g + k * Vx;
            Vy *= k;
            Vz *= k;
            /* Calculation of longitude and latitude.*/
            double lambda = Math.atan2(Vy, Vx);
            double phi = Math.atan(Vz * Math.cos(lambda) / Vx);

            p2d.setLocation(lambda, phi);

            return p2d;
        }
    }

    public static class Ellipsoidal extends GeostationarySatellite {

        private static final long serialVersionUID = 1L;
        final double radius_p;
        final double radius_p2;
        final double radius_p_inv2;

        public Ellipsoidal(final ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);

            // from https://github.com/OSGeo/proj.4/blob/4.9/src/projects.h
            //  es, /* e ^ 2 */
            //  one_es, /* 1 - e^2 */
            //  rone_es, /* 1/one_es */
            final double es = excentricitySquared;
            final double one_es = 1. - es;
            final double rone_es = 1. / one_es;

            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            //   P->radius_p      = sqrt (P->one_es);
            //   P->radius_p2     = P->one_es;
            //   P->radius_p_inv2 = P->rone_es;
            radius_p = Math.sqrt(one_es);
            radius_p2 = one_es;
            radius_p_inv2 = rone_es;
        }

        @Override
        protected Point2D transformNormalized(double lambda, double phi, Point2D p2d) throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Calculation of geocentric latitude. */
            phi = Math.atan(radius_p2 * Math.tan(phi));
            /* Calculation of the three components of the vector from satellite to
             ** position on earth surface (lon,lat).*/
            double r = radius_p / Math.hypot(radius_p * Math.cos(phi), Math.sin(phi));
            double Vx = r * Math.cos(lambda) * Math.cos(phi);
            double Vy = r * Math.sin(lambda) * Math.cos(phi);
            double Vz = r * Math.sin(phi);
            /* Check visibility. */
            if ((radius_g - Vx) * Vx - Vy * Vy - Vz * Vz * radius_p_inv2 < 0.) {
                throw new ProjectionException();
            }
            return transformViewVectorToCoordinates(Vx, Vy, Vz, p2d);
        }

        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D p2d) throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Setting three components of vector from satellite to position.*/
            double Vx = -1.;
            transformCoordinatesToViewVectorInitializer(x, y, p2d);
            double Vy = p2d.getX();
            double Vz = p2d.getY();
            /* Calculation of terms in cubic equation and determinant.*/
            double a = Vz / radius_p;
            a = Vy * Vy + a * a + Vx * Vx;
            double b = 2. * radius_g * Vx;
            double det = b * b - 4. * a * C;
            if (det < 0.) {
                throw new ProjectionException();
            }
            /* Calculation of three components of vector from satellite to position.*/
            double k = (-b - Math.sqrt(det)) / (2. * a);
            Vx = radius_g + k * Vx;
            Vy *= k;
            Vz *= k;
            /* Calculation of longitude and latitude.*/
            double lambda = Math.atan2(Vy, Vx);
            double phi = Math.atan(Vz * Math.cos(lambda) / Vx);
            phi = Math.atan(radius_p_inv2 * Math.tan(phi));
            p2d.setLocation(lambda, phi);

            return p2d;
        }
    }

    /** Circumscribed rectangle (smallest) for full disk earth image */
    public static Bounds circumscribeFullDisk(CoordinateReferenceSystem geosCRS)
            throws TransformException, FactoryException {

        if (!isGeostationaryCRS(geosCRS)) {
            return null;
        }

        MathTransform mt =
                CRS.findMathTransform(geosCRS, CRS.getProjectedCRS(geosCRS).getBaseCRS(), true);
        MathTransform imt = mt.inverse();

        ParameterValueGroup parameters = CRS.getMapProjection(geosCRS).getParameterValues();
        double semiMajorAxis = parameters.parameter("semi_major").doubleValue();
        double satelliteHeight = parameters.parameter("satellite_height").doubleValue();
        double centralMeridian = parameters.parameter("central_meridian").doubleValue();

        Position2D dp2d = new Position2D();

        double halfFoVRadians = Math.acos(semiMajorAxis / (satelliteHeight + semiMajorAxis));
        double halfFoVDegrees = Math.toDegrees(halfFoVRadians);

        dp2d.setLocation(centralMeridian - halfFoVDegrees, 0.);
        imt.transform(dp2d, dp2d);
        double xMin = dp2d.getX();

        dp2d.setLocation(centralMeridian + halfFoVDegrees, 0.);
        imt.transform(dp2d, dp2d);
        double xMax = dp2d.getX();

        dp2d.setLocation(centralMeridian, -halfFoVDegrees);
        imt.transform(dp2d, dp2d);
        double yMin = dp2d.getY();

        dp2d.setLocation(centralMeridian, halfFoVDegrees);
        imt.transform(dp2d, dp2d);
        double yMax = dp2d.getY();

        GeneralBounds bounds = new GeneralBounds(geosCRS);
        bounds.setEnvelope(xMin, yMin, xMax, yMax);

        return bounds;
    }

    /**
     * Inscribed rectangle for for full disk earth image (not largest inscribing rectangle but close, hence "Estimate")
     */
    public static Bounds inscribeFullDiskEstimate(CoordinateReferenceSystem geosCRS)
            throws TransformException, FactoryException {
        Bounds circumscribed = circumscribeFullDisk(geosCRS);
        return circumscribed == null ? null : doInscribeFullDisk(circumscribed);
    }

    private static final double SQRT2 = Math.sqrt(2.);

    static Bounds doInscribeFullDisk(Bounds circumscribed) {
        double dx = circumscribed.getSpan(0) / SQRT2;
        double dy = circumscribed.getSpan(1) / SQRT2;

        GeneralBounds bounds = new GeneralBounds(circumscribed.getCoordinateReferenceSystem());
        bounds.setEnvelope(
                circumscribed.getMedian(0) - dx / 2.0,
                circumscribed.getMedian(1) - dy / 2.0,
                circumscribed.getMedian(0) + dx / 2.0,
                circumscribed.getMedian(1) + dy / 2.0);

        return bounds;
    }

    static boolean isGeostationaryCRS(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return false;
        }
        String code = crs.getName().getCode();
        return "GEOS".equals(code) || "Geostationary_Satellite".equals(code);
    }

    public static class Provider extends MapProjection.AbstractProvider {

        public static final ParameterDescriptor SATELLITE_HEIGHT = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "satellite_height"), new NamedIdentifier(Citations.PROJ, "h")
                },
                35785831, // default
                0.0, // minimum
                Double.POSITIVE_INFINITY, // maximum
                SI.METRE);

        static final ParameterDescriptor<Double> SWEEP = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "sweep"),
                },
                1, // default
                0, // minimum
                1, // maximum
                null);

        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "GEOS"),
                    new NamedIdentifier(Citations.OGC, "Geostationary_Satellite"),
                    new NamedIdentifier(Citations.PROJ, "geos")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR, SEMI_MINOR, CENTRAL_MERIDIAN, SATELLITE_HEIGHT, FALSE_EASTING, FALSE_NORTHING, SWEEP
                });

        public Provider() {
            super(PARAMETERS);
        }

        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            if (isSpherical(parameters)) {
                return new Spherical(parameters);
            } else {
                return new Ellipsoidal(parameters);
            }
        }
    }
}
