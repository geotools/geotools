/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.hypot;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.Collection;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.ConicProjection;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.AbstractIdentifiedObject;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.transform.AffineTransform2D;
import si.uom.NonSI;
import tech.units.indriya.AbstractUnit;

/**
 * Krovak Oblique Conformal Conic projection (EPSG code 9819). This projection is used in the Czech Republic and
 * Slovakia under the name "Krovak" projection. The geographic coordinates on the ellipsoid are first reduced to
 * conformal coordinates on the conformal (Gaussian) sphere. These spherical coordinates are then projected onto the
 * oblique cone and converted to grid coordinates. The pseudo standard parallel is defined on the conformal sphere after
 * its rotation, to obtain the oblique aspect of the projection. It is then the parallel on this sphere at which the map
 * projection is true to scale; on the ellipsoid it maps as a complex curve.
 *
 * <p>The compulsory parameters are just the ellipsoid characteristics. All other parameters are optional and have
 * defaults to match the common usage with Krovak projection.
 *
 * <p>In general the axis of Krovak projection are defined as westing and southing (not easting and northing) and they
 * are also reverted, so if the value of projected coordinates should (and in <var>y</var>, <var>x</var> order in
 * Krovak) be positive the 'Axis' parameter for projection should be defined explicitly like this (in wkt):
 *
 * <pre>PROJCS["S-JTSK (Ferro) / Krovak",
 *         .
 *         .
 *         .
 *     PROJECTION["Krovak"]
 *     PARAMETER["semi_major", 6377397.155],
 *     PARAMETER["semi_minor", 6356078.963],
 *     UNIT["meter",1.0],
 *     AXIS["x", WEST],
 *     AXIS["y", SOUTH]]
 *     </pre>
 *
 * Axis in Krovak:
 *
 * <pre>
 *   y<------------------+
 *                       |
 *    Czech. Rep.        |
 *                       |
 *                       x
 * </pre>
 *
 * By default, the axis are 'easting, northing' so the values of projected coordinates are negative (and in
 * <var>y</var>, <var>x</var> order in Krovak - it is cold Krovak GIS version).
 *
 * <p><b>References:</b>
 *
 * <ul>
 *   <li>Proj-4.4.7 available at <A HREF="http://www.remotesensing.org/proj">www.remotesensing.org/proj</A><br>
 *       Relevant files is: {@code PJ_krovak.c}
 *   <li>"Coordinate Conversions and Transformations including Formulas" available at, <A
 *       HREF="http://www.remotesensing.org/geotiff/proj_list/guid7.html">http://www.remotesensing.org/geotiff/proj_list/guid7.html</A>
 * </ul>
 *
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/krovak.html">Krovak on RemoteSensing.org</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/guid7.html">Krovak on "Coordinate Conversions and
 *     Transformations including Formulas"</A>
 * @see <A HREF="http://www.posc.org/Epicentre.2_2/DataModel/ExamplesofUsage/eu_cs34e2.html">Krovak on POSC</A>
 * @since 2.4
 * @version $Id$
 * @author Jan Jezek
 * @author Martin Desruisseaux
 */
public class Krovak extends MapProjection {
    /** For cross-version compatibility. */
    private static final long serialVersionUID = -8359105634355342212L;

    /** Maximum number of iterations for iterative computations. */
    private static final int MAXIMUM_ITERATIONS = 15;

    /** When to stop the iteration. */
    private static final double ITERATION_TOLERANCE = 1E-11;

    /**
     * Azimuth of the centre line passing through the centre of the projection. This is equals to the co-latitude of the
     * cone axis at point of intersection with the ellipsoid.
     */
    protected final double azimuth;

    /** The descriptor group, stored as a field as there are different providers */
    protected final ParameterDescriptorGroup descriptors;

    /** Parameter used by ESRI - scale for X Axis */
    protected double x_scale;

    /** Parameter used by ESRI - scale for Y Axis */
    protected double y_scale;

    /** Parameter used by ESRI - rotation */
    protected double xy_plane_rotation;

    /** Variable to decide if ESRI parameters were used */
    boolean esriDefinition;

    private MathTransform axisTransform = null;
    /** Latitude of pseudo standard parallel. */
    protected final double pseudoStandardParallel;

    /** Useful variables calculated from parameters defined by user. */
    private final double sinAzim, cosAzim, n, tanS2, alfa, hae, k1, ka, ro0, rop;

    /** Useful constant - 45° in radians. */
    private static final double s45 = 0.785398163397448;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param parameters The parameter values in standard units.
     * @param esriDefinition true if ESRI parameters are specified.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Krovak(
            final ParameterValueGroup parameters, final ParameterDescriptorGroup descriptors, boolean esriDefinition)
            throws ParameterNotFoundException {
        super(parameters, descriptors.descriptors());
        this.descriptors = descriptors;
        this.esriDefinition = esriDefinition;
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        // Fetch parameters from user input.
        latitudeOfOrigin = doubleValue(expected, BaseProvider.LATITUDE_OF_CENTER, parameters);
        centralMeridian = doubleValue(expected, BaseProvider.LONGITUDE_OF_CENTER, parameters);
        azimuth = doubleValue(expected, BaseProvider.AZIMUTH, parameters);
        pseudoStandardParallel = doubleValue(expected, BaseProvider.PSEUDO_STANDARD_PARALLEL, parameters);
        scaleFactor = doubleValue(expected, BaseProvider.SCALE_FACTOR, parameters);
        x_scale = doubleValue(expected, BaseProvider.X_SCALE, parameters);
        y_scale = doubleValue(expected, BaseProvider.Y_SCALE, parameters);
        xy_plane_rotation = doubleValue(expected, BaseProvider.XY_PLANE_ROTATION, parameters);

        /*
         * Check if there are parameters for axis swapping used by ESRI - if so then set variable so the proper
         * ParameterDescriptorGroup will be returned by getParameterDescriptors()
         */
        if (Double.isNaN(doubleValue(expected, BaseProvider.X_SCALE, parameters))
                && Double.isNaN(doubleValue(expected, BaseProvider.Y_SCALE, parameters))
                && Double.isNaN(doubleValue(expected, BaseProvider.XY_PLANE_ROTATION, parameters))) {
            this.esriDefinition = false;

        } else {
            axisTransform = createAffineTransform(x_scale, y_scale, xy_plane_rotation);
        }
        ensureLatitudeInRange(BaseProvider.LATITUDE_OF_CENTER, latitudeOfOrigin, false);
        ensureLongitudeInRange(BaseProvider.LONGITUDE_OF_CENTER, centralMeridian, false);

        // Calculates useful constants.
        sinAzim = sin(azimuth);
        cosAzim = cos(azimuth);
        n = sin(pseudoStandardParallel);
        tanS2 = tan(pseudoStandardParallel / 2 + s45);

        final double sinLat = sin(latitudeOfOrigin);
        final double cosLat = cos(latitudeOfOrigin);
        final double cosL2 = cosLat * cosLat;
        alfa = sqrt(1 + excentricitySquared * (cosL2 * cosL2) / (1 - excentricitySquared));
        hae = alfa * excentricity / 2;
        final double u0 = asin(sinLat / alfa);

        final double g;
        final double esl = excentricity * sinLat;
        g = pow((1 - esl) / (1 + esl), alfa * excentricity / 2);
        k1 = pow(tan(latitudeOfOrigin / 2 + s45), alfa) * g / tan(u0 / 2 + s45);
        ka = pow(1 / k1, -1 / alfa);

        final double radius = sqrt(1 - excentricitySquared) / (1 - excentricitySquared * (sinLat * sinLat));

        ro0 = scaleFactor * radius / tan(pseudoStandardParallel);
        rop = ro0 * pow(tanS2, n);
    }

    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return this.descriptors;
    }

    private MathTransform createAffineTransform(double x_scale, double y_scale, double xy_plane_rotation) {
        /* calculates matrix coefficients form geometric coefficients */
        double a00 = x_scale * Math.cos(xy_plane_rotation);
        double a01 = -y_scale * Math.sin(xy_plane_rotation);
        double a10 = x_scale * Math.sin(xy_plane_rotation);
        double a11 = y_scale * Math.cos(xy_plane_rotation);
        AffineTransform at = new AffineTransform(a00, a10, a01, a11, 0d, 0d);
        MathTransform theAffineTransform = new AffineTransform2D(at);
        return theAffineTransform;
    }

    /** {@inheritDoc} */
    @Override
    public ParameterValueGroup getParameterValues() {
        final Collection<GeneralParameterDescriptor> expected =
                getParameterDescriptors().descriptors();
        final ParameterValueGroup values = super.getParameterValues();
        set(expected, BaseProvider.LATITUDE_OF_CENTER, values, latitudeOfOrigin);
        set(expected, BaseProvider.LONGITUDE_OF_CENTER, values, centralMeridian);
        set(expected, BaseProvider.AZIMUTH, values, azimuth);
        set(expected, BaseProvider.PSEUDO_STANDARD_PARALLEL, values, pseudoStandardParallel);
        set(expected, BaseProvider.SCALE_FACTOR, values, scaleFactor);

        if (esriDefinition) {
            set(expected, BaseProvider.X_SCALE, values, x_scale);
            set(expected, BaseProvider.Y_SCALE, values, y_scale);
            set(expected, BaseProvider.XY_PLANE_ROTATION, values, xy_plane_rotation);
        }
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D transformNormalized(final double lambda, final double phi, Point2D ptDst)
            throws ProjectionException {
        final double esp = excentricity * sin(phi);
        final double gfi = pow((1. - esp) / (1. + esp), hae);
        final double u = 2 * (atan(pow(tan(phi / 2 + s45), alfa) / k1 * gfi) - s45);
        final double deltav = -lambda * alfa;
        final double cosU = cos(u);
        final double s = asin(cosAzim * sin(u) + sinAzim * cosU * cos(deltav));
        final double d = asin(cosU * sin(deltav) / cos(s));
        final double eps = n * d;
        final double ro = rop / pow(tan(s / 2 + s45), n);

        // x and y are reverted.
        final double y = -(ro * cos(eps));
        final double x = -(ro * sin(eps));

        double[] result = {x, y};
        /* swap axis if required */
        if (axisTransform != null) {
            try {
                axisTransform.transform(new double[] {x, y}, 0, result, 0, 1);
            } catch (TransformException e) {
                throw new ProjectionException(e);
            }
        }

        if (ptDst != null) {
            ptDst.setLocation(result[0], result[1]);
            return ptDst;
        }
        return new Point2D.Double(result[0], result[1]);
    }

    /** Transforms the specified (<var>x</var>,<var>y</var>) coordinate and stores the result in {@code ptDst}. */
    @Override
    protected Point2D inverseTransformNormalized(final double x, final double y, Point2D ptDst)
            throws ProjectionException {
        // x -> southing, y -> westing
        double[] result = {x, y};
        /* swap axis if required */
        if (axisTransform != null) {
            try {
                axisTransform.transform(new double[] {x, y}, 0, result, 0, 1);
            } catch (TransformException e) {
                throw new ProjectionException(e);
            }
        }

        final double ro = hypot(result[0], result[1]);
        final double eps = atan2(-result[0], -result[1]);
        final double d = eps / n;
        final double s = 2 * (atan(pow(ro0 / ro, 1 / n) * tanS2) - s45);
        final double cs = cos(s);
        final double u = asin(cosAzim * sin(s) - sinAzim * cs * cos(d));
        final double kau = ka * pow(tan(u / 2. + s45), 1 / alfa);
        final double deltav = asin(cs * sin(d) / cos(u));
        final double lambda = -deltav / alfa;
        double phi = 0;
        double fi1 = u;

        // iteration calculation
        for (int i = MAXIMUM_ITERATIONS; ; ) {
            fi1 = phi;
            final double esf = excentricity * sin(fi1);
            phi = 2. * (atan(kau * pow((1. + esf) / (1. - esf), excentricity / 2.)) - s45);
            if (abs(fi1 - phi) <= ITERATION_TOLERANCE) {
                break;
            }
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
        }
        if (ptDst != null) {
            ptDst.setLocation(lambda, phi);
            return ptDst;
        }
        return new Point2D.Double(lambda, phi);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for an
     * {@linkplain Krovak Krovak} projection (EPSG code 9819).
     *
     * @since 2.4
     * @version $Id$
     * @author Jan Jezek
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    private abstract static class BaseProvider extends AbstractProvider {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -278392856661204734L;

        /**
         * The operation parameter descriptor for the {@linkplain #latitudeOfOrigin latitude of origin} parameter value.
         * Valid values range is from -90 to 90. Default value is 49.5.
         */
        public static final ParameterDescriptor LATITUDE_OF_CENTER = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "latitude_of_center"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of projection centre"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of origin"),
                    new NamedIdentifier(Citations.GEOTIFF, "CenterLat"),
                    new NamedIdentifier(Citations.PROJ, "lat_0")
                },
                49.5,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@linkplain #centralMeridian central meridian} parameter value.
         * Valid values range is from -180 to 180. Default value is 24ï¿½50' (= 42ï¿½50' from Ferro prime meridian).
         */
        public static final ParameterDescriptor LONGITUDE_OF_CENTER = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "longitude_of_center"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of projection centre"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of origin"),
                    new NamedIdentifier(Citations.GEOTIFF, "CenterLong"),
                    new NamedIdentifier(Citations.PROJ, "lon_0")
                },
                42.5 - 17.66666666666667,
                -180,
                180,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@linkplain #azimuth azimuth} parameter value. Valid values range
         * is from -90 to 90. Default value is 30.28813972222.
         */
        public static final ParameterDescriptor AZIMUTH = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.EPSG, "Co-latitude of cone axis"),
                    new NamedIdentifier(Citations.OGC, "azimuth"),
                    new NamedIdentifier(Citations.EPSG, "Azimuth of initial line"),
                    new NamedIdentifier(Citations.GEOTIFF, "AzimuthAngle"),
                    new NamedIdentifier(Citations.ESRI, "Azimuth"),
                    new NamedIdentifier(Citations.PROJ, "alpha"),
                },
                30.28813972222222,
                0,
                360,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the pseudo {@linkplain #pseudoStandardParallel pseudo standard
         * parallel} parameter value. Valid values range is from -90 to 90. Default value is 78.5.
         */
        public static final ParameterDescriptor PSEUDO_STANDARD_PARALLEL = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "pseudo_standard_parallel_1"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of Pseudo Standard Parallel"),
                    new NamedIdentifier(Citations.ESRI, "Pseudo_Standard_Parallel_1")
                },
                78.5,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #scaleFactor scaleFactor} parameter value. Valid values
         * range is from 0 to infinity. Default value is 1.
         */
        public static final ParameterDescriptor SCALE_FACTOR = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "scale_factor"),
                    new NamedIdentifier(Citations.EPSG, "Scale factor on pseudo standard parallel"),
                    new NamedIdentifier(Citations.GEOTIFF, "ScaleAtCenter"),
                    new NamedIdentifier(Citations.OGC, "Scale_Factor"),
                    new NamedIdentifier(Citations.PROJ, "k_0")
                },
                0.9999,
                0,
                Double.POSITIVE_INFINITY,
                AbstractUnit.ONE);

        /** ESRI Parameter for scale of X axis in projected coordinate system. */
        public static final ParameterDescriptor X_SCALE = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "X_Scale"),
                },
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                AbstractUnit.ONE);

        /** ESRI Parameter for scale of Y axis in projected coordinate system. */
        public static final ParameterDescriptor Y_SCALE = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "Y_Scale"),
                },
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                AbstractUnit.ONE);

        /** ESRI Parameter for rotation of projected coordinate system. */
        public static final ParameterDescriptor XY_PLANE_ROTATION = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.ESRI, "XY_Plane_Rotation"),
                },
                -360,
                360,
                NonSI.DEGREE_ANGLE);

        /** Constructs a new provider. */
        public BaseProvider(final ParameterDescriptorGroup params) {
            super(params);
        }

        /** Returns the operation type for this map projection. */
        @Override
        public Class<ConicProjection> getOperationType() {
            return ConicProjection.class;
        }
    }

    public static class Provider extends BaseProvider {

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "Krovak"),
                    new NamedIdentifier(Citations.GEOTIFF, "Krovak"),
                    new NamedIdentifier(Citations.EPSG, "Krovak Oblique Conformal Conic"),
                    new NamedIdentifier(Citations.EPSG, "Krovak Oblique Conic Conformal"),
                    new NamedIdentifier(Citations.EPSG, "9819"),
                    new NamedIdentifier(Citations.ESRI, "Krovak"),
                    new NamedIdentifier(Citations.PROJ, "krovak")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    LATITUDE_OF_CENTER,
                    LONGITUDE_OF_CENTER,
                    AZIMUTH,
                    PSEUDO_STANDARD_PARALLEL,
                    SCALE_FACTOR,
                    FALSE_EASTING,
                    FALSE_NORTHING
                });

        /** The parameters group. */
        static final ParameterDescriptorGroup ESRI_PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "Krovak"),
                    new NamedIdentifier(Citations.GEOTIFF, "Krovak"),
                    new NamedIdentifier(Citations.EPSG, "Krovak Oblique Conformal Conic"),
                    new NamedIdentifier(Citations.EPSG, "Krovak Oblique Conic Conformal"),
                    new NamedIdentifier(Citations.EPSG, "9819"),
                    new NamedIdentifier(Citations.ESRI, "Krovak"),
                    new NamedIdentifier(Citations.PROJ, "krovak")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    LATITUDE_OF_CENTER,
                    LONGITUDE_OF_CENTER,
                    AZIMUTH,
                    PSEUDO_STANDARD_PARALLEL,
                    SCALE_FACTOR,
                    FALSE_EASTING,
                    FALSE_NORTHING,
                    X_SCALE,
                    Y_SCALE,
                    XY_PLANE_ROTATION
                });

        public Provider() {
            super(ESRI_PARAMETERS);
        }

        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            boolean esriDefinition = isESRIDefinition(parameters);

            if (esriDefinition) {
                return new Krovak(parameters, ESRI_PARAMETERS, true);
            } else {
                return new Krovak(parameters, PARAMETERS, false);
            }
        }

        private boolean isESRIDefinition(ParameterValueGroup parameters) {
            for (final GeneralParameterDescriptor descriptor :
                    parameters.getDescriptor().descriptors()) {
                if (descriptor instanceof ParameterDescriptor) {
                    if (AbstractIdentifiedObject.nameMatches(descriptor, X_SCALE)
                            || AbstractIdentifiedObject.nameMatches(descriptor, Y_SCALE)
                            || AbstractIdentifiedObject.nameMatches(descriptor, XY_PLANE_ROTATION)) {
                        return true;
                    }
                }
            }

            return false;
        }
    }

    public static class NorthProvider extends BaseProvider {

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "Krovak"),
                    new NamedIdentifier(Citations.GEOTIFF, "Krovak"),
                    new NamedIdentifier(Citations.OGC, "Krovak (North Orientated)"),
                    new NamedIdentifier(Citations.EPSG, "Krovak (North Orientated)"),
                    new NamedIdentifier(Citations.EPSG, "1041"),
                    new NamedIdentifier(Citations.PROJ, "krovak")
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    LATITUDE_OF_CENTER,
                    LONGITUDE_OF_CENTER,
                    AZIMUTH,
                    PSEUDO_STANDARD_PARALLEL,
                    SCALE_FACTOR,
                    FALSE_EASTING,
                    FALSE_NORTHING
                });

        public NorthProvider() {
            super(PARAMETERS);
        }

        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException {
            // same as the south orientated provider, the axis flip is added around
            // the transformation by the factories, but need to match the citations correctly
            return new Krovak(parameters, PARAMETERS, false);
        }
    }

    /** Returns a hash value for this projection. */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(azimuth) ^ Double.doubleToLongBits(pseudoStandardParallel);
        return ((int) code ^ (int) (code >>> 32)) + 37 * super.hashCode();
    }

    /** Compares the specified object with this map projection for equality. */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        if (super.equals(object)) {
            final Krovak that = (Krovak) object;
            return equals(azimuth, that.azimuth) && equals(pseudoStandardParallel, that.pseudoStandardParallel);
        }
        return false;
    }
}
