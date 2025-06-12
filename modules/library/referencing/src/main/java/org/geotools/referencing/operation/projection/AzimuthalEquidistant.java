/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.awt.geom.Point2D;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.List;
import net.sf.geographiclib.Geodesic;
import net.sf.geographiclib.GeodesicData;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.InvalidParameterNameException;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * Azimuthal Equidistant projection.
 *
 * <p>This implementation does not include the Guam or Micronesia variants.
 *
 * @author Gerald Evenden (original PROJ.4 implementation in C)
 * @author Ben Caradoc-Davies (Transient Software Limited)
 * @see <a href="https://pubs.er.usgs.gov/publication/pp1395"><em>Map Projections: A Working Manual</em>, Snyder
 *     (1987)</a>, pages 191-202
 * @see <a href="http://geotiff.maptools.org/proj_list/azimuthal_equidistant.html">PROJ.4 notes on parameters</a>
 * @see <a href="https://github.com/OSGeo/proj.4/blob/master/src/PJ_aeqd.c">PROJ.4 implemention in C</a>
 * @see <a href="https://en.wikipedia.org/wiki/Azimuthal_equidistant_projection">Wikipedia</a>
 * @see <a href="http://mathworld.wolfram.com/AzimuthalEquidistantProjection.html">Wolfram Alpha</a>
 */
public class AzimuthalEquidistant {

    /** Less strict tolerance. */
    public static double EPS10 = 1.e-10;

    /** Stricter tolerance. */
    public static double TOL = 1.e-14;

    /** Half of π. */
    public static double HALF_PI = PI / 2;

    /** The four possible modes or aspects of the projection. */
    public enum Mode {
        NORTH_POLAR,
        SOUTH_POLAR,
        EQUATORIAL,
        OBLIQUE
    }
    /** Abstract base class for Azimuthal Equidistant projections. */
    @SuppressWarnings("serial")
    public abstract static class Abstract extends MapProjection {

        /** The mode or aspect of the projection. */
        protected final Mode mode;

        /** The sine of the central latitude of the projection. */
        protected final double sinph0;

        /** The cosine of the central latitude of the projection. */
        protected final double cosph0;

        /**
         * Constructor.
         *
         * @param parameters the parameters that define this projection
         */
        protected Abstract(ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);
            List<GeneralParameterDescriptor> parameterDescriptors =
                    getParameterDescriptors().descriptors();
            centralMeridian = doubleValue(parameterDescriptors, Provider.LONGITUDE_OF_CENTRE, parameters);
            latitudeOfOrigin = doubleValue(parameterDescriptors, Provider.LATITUDE_OF_CENTRE, parameters);
            ensureLongitudeInRange(Provider.LONGITUDE_OF_CENTRE, centralMeridian, true);
            ensureLatitudeInRange(Provider.LATITUDE_OF_CENTRE, latitudeOfOrigin, true);
            if (abs(latitudeOfOrigin - HALF_PI) < EPS10) {
                mode = Mode.NORTH_POLAR;
                sinph0 = 1;
                cosph0 = 0;
            } else if (abs(latitudeOfOrigin + HALF_PI) < EPS10) {
                mode = Mode.SOUTH_POLAR;
                sinph0 = -1;
                cosph0 = 0;
            } else if (abs(latitudeOfOrigin) < EPS10) {
                mode = Mode.EQUATORIAL;
                sinph0 = 0;
                cosph0 = 1;
            } else {
                mode = Mode.OBLIQUE;
                sinph0 = sin(latitudeOfOrigin);
                cosph0 = cos(latitudeOfOrigin);
            }
        }

        /**
         * The descriptors for the parameters that define the projection.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#getParameterDescriptors()
         */
        @Override
        public ParameterDescriptorGroup getParameterDescriptors() {
            return Provider.PARAMETERS;
        }

        /**
         * Return the values of the parameters that define the projection.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#getParameterValues()
         */
        @Override
        public ParameterValueGroup getParameterValues() {
            ParameterValueGroup values = super.getParameterValues();
            List<GeneralParameterDescriptor> descriptors =
                    getParameterDescriptors().descriptors();
            set(descriptors, Provider.LONGITUDE_OF_CENTRE, values, centralMeridian);
            set(descriptors, Provider.LATITUDE_OF_CENTRE, values, latitudeOfOrigin);
            return values;
        }
    }
    /** Spherical Azimuthal Equidistant projection. */
    @SuppressWarnings("serial")
    public static class Spherical extends Abstract {

        /**
         * Constructor.
         *
         * @param parameters the parameters that define this projection
         */
        protected Spherical(ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);
            ensureSpherical();
        }

        /**
         * Forward transform from longitude/latitude in radians to projected coordinates.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#transformNormalized(double, double,
         *     java.awt.geom.Point2D)
         */
        @Override
        @SuppressWarnings("FallThrough") // `case NORTH_POLAR` falls through `case SOUTH_POLAR`
        protected Point2D transformNormalized(double lambda, double phi, Point2D ptDst) throws ProjectionException {
            double x = 0;
            double y = 0;
            double sinphi = sin(phi);
            double cosphi = cos(phi);
            double coslam = cos(lambda);
            switch (mode) {
                case EQUATORIAL:
                case OBLIQUE:
                    if (mode == Mode.EQUATORIAL) {
                        y = cosphi * coslam;
                    } else { // Oblique
                        y = sinph0 * sinphi + cosph0 * cosphi * coslam;
                    }
                    if (abs(abs(y) - 1) < TOL) {
                        if (y < 0) {
                            throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                        } else {
                            x = 0;
                            y = 0;
                        }
                    } else {
                        y = acos(y);
                        y /= sin(y);
                        x = y * cosphi * sin(lambda);
                        y *= (mode == Mode.EQUATORIAL) ? sinphi : (cosph0 * sinphi - sinph0 * cosphi * coslam);
                    }
                    break;
                case NORTH_POLAR:
                    phi = -phi;
                    coslam = -coslam;
                case SOUTH_POLAR:
                    if (Math.abs(phi - HALF_PI) < EPS10) {
                        throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                    }
                    y = HALF_PI + phi;
                    x = y * sin(lambda);
                    y *= coslam;
                    break;
            }
            if (ptDst == null) {
                return new Point2D.Double(x, y);
            } else {
                ptDst.setLocation(x, y);
                return ptDst;
            }
        }

        /**
         * Inverse transform from projected coordinates to latitude/longitude in radians.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#inverseTransformNormalized(double, double,
         *     java.awt.geom.Point2D)
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
            double lambda = 0;
            double phi = 0;
            double c_rh = hypot(x, y);
            if (c_rh > PI) {
                if (c_rh - EPS10 > PI) {
                    throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
                }
                c_rh = PI;
            } else if (c_rh < EPS10) {
                phi = latitudeOfOrigin;
                lambda = 0.;
            } else {
                if (mode == Mode.OBLIQUE || mode == Mode.EQUATORIAL) {
                    double sinc = sin(c_rh);
                    double cosc = cos(c_rh);
                    if (mode == Mode.EQUATORIAL) {
                        phi = aasin(y * sinc / c_rh);
                        x *= sinc;
                        y = cosc * c_rh;
                    } else { // Oblique
                        phi = aasin(cosc * sinph0 + y * sinc * cosph0 / c_rh);
                        y = (cosc - sinph0 * sin(phi)) * c_rh;
                        x *= sinc * cosph0;
                    }
                    lambda = (y == 0) ? 0 : atan2(x, y);
                } else if (mode == Mode.NORTH_POLAR) {
                    phi = HALF_PI - c_rh;
                    lambda = atan2(x, -y);
                } else { // South Polar
                    phi = c_rh - HALF_PI;
                    lambda = atan2(x, y);
                }
            }
            if (ptDst == null) {
                return new Point2D.Double(lambda, phi);
            } else {
                ptDst.setLocation(lambda, phi);
                return ptDst;
            }
        }
    }

    /** Ellipsoidal Azimuthal Equidistant projection. */
    @SuppressWarnings("serial")
    public static class Ellipsoidal extends Abstract {

        /** Geodesic calculator used for this projection. Not used and set to null for polar projections. */
        protected transient Geodesic geodesic;

        /** Meridian distance from the equator to the pole. Not used and set to NaN for non-polar projections. */
        protected final double Mp;

        /** Manual override of object deserialization in order to assign transient "geodesic" field. */
        private void readObject(ObjectInputStream in) throws IOException, ClassNotFoundException {
            in.defaultReadObject();
            if (mode == Mode.OBLIQUE) {
                this.geodesic = buildGeodesic();
            }
        }

        private Geodesic buildGeodesic() {
            return new Geodesic(semiMajor, (semiMajor - semiMinor) / semiMajor);
        }

        /**
         * Constructor.
         *
         * @param parameters the parameters that define this projection
         */
        protected Ellipsoidal(ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);
            switch (mode) {
                case NORTH_POLAR:
                    Mp = mlfn(HALF_PI, 1, 0);
                    geodesic = null;
                    break;
                case SOUTH_POLAR:
                    Mp = mlfn(-HALF_PI, -1, 0);
                    geodesic = null;
                    break;
                case EQUATORIAL:
                case OBLIQUE:
                    Mp = Double.NaN;
                    geodesic = buildGeodesic();
                    break;
                default:
                    throw new RuntimeException(
                            "Unexpected mode " + mode + " for ellipsoidal AzimuthalEquidistant projection");
            }
        }

        /**
         * Forward transform from longitude/latitude in radians to projected coordinates.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#transformNormalized(double, double,
         *     java.awt.geom.Point2D)
         */
        @Override
        @SuppressWarnings("FallThrough") // `case NORTH_POLAR` falls through `case SOUTH_POLAR`
        protected Point2D transformNormalized(double lambda, double phi, Point2D ptDst) throws ProjectionException {
            double x = 0;
            double y = 0;
            double coslam = cos(lambda);
            double cosphi = cos(phi);
            double sinphi = sin(phi);
            switch (mode) {
                case NORTH_POLAR:
                    coslam = -coslam;
                case SOUTH_POLAR:
                    double rho = abs(Mp - mlfn(phi, sinphi, cosphi));
                    x = rho * sin(lambda);
                    y = rho * coslam;
                    break;
                case EQUATORIAL:
                case OBLIQUE:
                    if (abs(lambda) < EPS10 && abs(phi - latitudeOfOrigin) < EPS10) {
                        x = 0;
                        y = 0;
                        break;
                    }
                    GeodesicData g = geodesic.Inverse(
                            toDegrees(latitudeOfOrigin),
                            toDegrees(centralMeridian),
                            toDegrees(phi),
                            toDegrees(lambda + centralMeridian));
                    double azi1 = toRadians(g.azi1);
                    x = g.s12 * sin(azi1) / semiMajor;
                    y = g.s12 * cos(azi1) / semiMajor;
                    break;
            }
            if (ptDst == null) {
                return new Point2D.Double(x, y);
            } else {
                ptDst.setLocation(x, y);
                return ptDst;
            }
        }

        /**
         * Inverse transform from projected coordinates to latitude/longitude in radians.
         *
         * @see org.geotools.referencing.operation.projection.MapProjection#inverseTransformNormalized(double, double,
         *     java.awt.geom.Point2D)
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
            double lambda = 0;
            double phi = 0;
            double c = hypot(x, y);
            if (c < EPS10) {
                phi = latitudeOfOrigin;
                lambda = 0;
            } else {
                if (mode == Mode.OBLIQUE || mode == Mode.EQUATORIAL) {
                    double x2 = x * semiMajor;
                    double y2 = y * semiMajor;
                    double azi1 = atan2(x2, y2);
                    double s12 = sqrt(x2 * x2 + y2 * y2);
                    GeodesicData g = geodesic.Direct(
                            toDegrees(latitudeOfOrigin), toDegrees(centralMeridian), toDegrees(azi1), s12);
                    phi = toRadians(g.lat2);
                    lambda = toRadians(g.lon2);
                    lambda -= centralMeridian;
                } else { // Polar
                    phi = inv_mlfn((mode == Mode.NORTH_POLAR) ? (Mp - c) : (Mp + c));
                    lambda = atan2(x, (mode == Mode.NORTH_POLAR) ? -y : y);
                }
            }
            if (ptDst == null) {
                return new Point2D.Double(lambda, phi);
            } else {
                ptDst.setLocation(lambda, phi);
                return ptDst;
            }
        }
    }

    /** Factory for creating Azimuthal Equidistant projections. */
    @SuppressWarnings("serial")
    public static class Provider extends MapProjection.AbstractProvider {

        /** The descriptors for the parameters that define the projection. */
        public static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    // @formatter:off
                    // see: http://geotiff.maptools.org/proj_list/azimuthal_equidistant.html
                    new NamedIdentifier(Citations.OGC, "Azimuthal_Equidistant"),
                    new NamedIdentifier(Citations.GEOTIFF, "CT_AzimuthalEquidistant"),
                    new NamedIdentifier(Citations.GEOTOOLS, "Azimuthal Equidistant"),
                    new NamedIdentifier(Citations.PROJ, "aeqd")
                    // there is no EPSG code for this projection
                    // @formatter:on
                },
                new ParameterDescriptor[] {
                    // @formatter:off
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    LONGITUDE_OF_CENTRE,
                    LATITUDE_OF_CENTRE,
                    FALSE_EASTING,
                    FALSE_NORTHING,
                    SCALE_FACTOR
                    // @formatter:on
                });

        /** Constructor. */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Create an Azimuthal Equidistant projection.
         *
         * @return {@link Spherical} or {@link Ellipsoidal} depending on the parameters.
         * @see
         *     org.geotools.referencing.operation.MathTransformProvider#createMathTransform(org.geotools.api.parameter.ParameterValueGroup)
         */
        @Override
        protected MathTransform createMathTransform(ParameterValueGroup parameters)
                throws InvalidParameterNameException, ParameterNotFoundException, InvalidParameterValueException,
                        FactoryException {
            if (isSpherical(parameters)) {
                return new Spherical(parameters);
            } else {
                return new Ellipsoidal(parameters);
            }
        }
    }
}
