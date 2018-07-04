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
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.CRS;
import org.geotools.referencing.NamedIdentifier;
import org.opengis.parameter.*;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;
import org.opengis.referencing.operation.TransformException;
import si.uom.SI;

/**
 * The Geostationary Satellite Projection
 *
 * <p>Adapted from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
 *
 * <p>NOTE: Not all valid coordinates in this projection will transform to valid terrestrial
 * coordinates, this is especially true of "Full Disk" earth coverages. If one must deal with
 * coverages in this projection with generalized code which requires the coverage bounding-box
 * coordinates to transform to valid terrestrial values consider clipping to a rectangle inscribing
 * the ellipsoid.
 *
 * @author Tom Kunicki
 */
public abstract class GeostationarySatellite extends MapProjection {

    private static final long serialVersionUID = 1L;

    final double h;
    final double radius_g;
    final double radius_g_1;
    final double C;

    public GeostationarySatellite(ParameterValueGroup parameters)
            throws ParameterNotFoundException {
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
        return values;
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
        protected Point2D transformNormalized(double lambda, double phi, Point2D p2d)
                throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Calculation of the three components of the vector from satellite to
             ** position on earth surface (lon,lat).*/
            double tmp = Math.cos(phi);
            double Vx = Math.cos(lambda) * tmp;
            double Vy = Math.sin(lambda) * tmp;
            double Vz = Math.sin(phi);
            /* Check visibility.*/
            if (((radius_g - Vx) * Vx - Vy * Vy - Vz * Vz) < 0.) {
                throw new ProjectionException();
            }
            /* Calculation based on view angles from satellite.*/
            tmp = radius_g - Vx;
            double x = radius_g_1 * Math.atan(Vy / tmp);
            double y = radius_g_1 * Math.atan(Vz / Math.hypot(Vy, tmp));

            p2d.setLocation(x, y);

            return p2d;
        }

        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D p2d)
                throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Setting three components of vector from satellite to position.*/
            double Vx = -1.;
            double Vy = Math.tan(x / (radius_g - 1.));
            double Vz = Math.tan(y / (radius_g - 1.)) * Math.sqrt(1. + Vy * Vy);
            /* Calculation of terms in cubic equation and determinant.*/
            double a = Vy * Vy + Vz * Vz + Vx * Vx;
            double b = 2. * radius_g * Vx;
            double det = (b * b) - 4. * a * C;
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
        protected Point2D transformNormalized(double lambda, double phi, Point2D p2d)
                throws ProjectionException {
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
            if (((radius_g - Vx) * Vx - Vy * Vy - Vz * Vz * radius_p_inv2) < 0.) {
                throw new ProjectionException();
            }
            /* Calculation based on view angles from satellite. */
            double tmp = radius_g - Vx;
            double x = radius_g_1 * Math.atan(Vy / tmp);
            double y = radius_g_1 * Math.atan(Vz / Math.hypot(Vy, tmp));

            p2d.setLocation(x, y);

            return p2d;
        }

        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D p2d)
                throws ProjectionException {
            // from https://github.com/OSGeo/proj.4/blob/4.9/src/PJ_geos.c
            /* Setting three components of vector from satellite to position.*/
            double Vx = -1.;
            double Vy = Math.tan(x / radius_g_1);
            double Vz = Math.tan(y / radius_g_1) * Math.hypot(1., Vy);
            /* Calculation of terms in cubic equation and determinant.*/
            double a = Vz / radius_p;
            a = Vy * Vy + a * a + Vx * Vx;
            double b = 2. * radius_g * Vx;
            double det = (b * b) - 4. * a * C;
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
    public static Envelope2D circumscribeFullDisk(CoordinateReferenceSystem geosCRS)
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

        DirectPosition2D dp2d = new DirectPosition2D();

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

        return new Envelope2D(geosCRS, xMin, yMin, xMax - xMin, yMax - yMin);
    }

    /**
     * Inscribed rectangle for for full disk earth image (not largest inscribing rectangle but
     * close, hence "Estimate")
     */
    public static Envelope2D inscribeFullDiskEstimate(CoordinateReferenceSystem geosCRS)
            throws TransformException, FactoryException {
        Envelope2D circumscribed = circumscribeFullDisk(geosCRS);
        return (circumscribed == null) ? null : doInscribeFullDisk(circumscribed);
    }

    private static final double SQRT2 = Math.sqrt(2.);

    static Envelope2D doInscribeFullDisk(Envelope2D circumscribed) {
        double dx = circumscribed.getWidth() / SQRT2;
        double dy = circumscribed.getHeight() / SQRT2;
        return new Envelope2D(
                circumscribed.getCoordinateReferenceSystem(),
                circumscribed.getCenterX() - dx / 2.,
                circumscribed.getCenterY() - dy / 2.,
                dx,
                dy);
    }

    static boolean isGeostationaryCRS(CoordinateReferenceSystem crs) {
        if (crs == null) {
            return false;
        }
        String code = crs.getName().getCode();
        return ("GEOS".equals(code) || "Geostationary_Satellite".equals(code));
    }

    public static class Provider extends MapProjection.AbstractProvider {

        static final ParameterDescriptor SATELLITE_HEIGHT =
                createDescriptor(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.OGC, "satellite_height"),
                        },
                        35785831, // default
                        0.0, // minimum
                        Double.POSITIVE_INFINITY, // maximum
                        SI.METRE);

        static final ParameterDescriptorGroup PARAMETERS =
                createDescriptorGroup(
                        new NamedIdentifier[] {
                            new NamedIdentifier(Citations.OGC, "GEOS"),
                            new NamedIdentifier(Citations.OGC, "Geostationary_Satellite")
                        },
                        new ParameterDescriptor[] {
                            SEMI_MAJOR,
                            SEMI_MINOR,
                            CENTRAL_MERIDIAN,
                            SATELLITE_HEIGHT,
                            FALSE_EASTING,
                            FALSE_NORTHING
                        });

        public Provider() {
            super(PARAMETERS);
        }

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
