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
import static java.lang.Math.acos;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;
import static java.lang.Math.toDegrees;
import static java.lang.Math.toRadians;

import java.awt.geom.Point2D;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import org.geotools.api.parameter.GeneralParameterDescriptor;
import org.geotools.api.parameter.InvalidParameterValueException;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.operation.MathTransform2D;
import org.geotools.api.referencing.operation.NoninvertibleTransformException;
import org.geotools.api.referencing.operation.Projection;
import org.geotools.api.referencing.operation.TransformException;
import org.geotools.measure.Latitude;
import org.geotools.measure.Longitude;
import org.geotools.metadata.i18n.ErrorKeys;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.metadata.math.XMath;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.MathTransformProvider;
import org.geotools.referencing.operation.transform.AbstractMathTransform;
import org.geotools.util.Utilities;
import org.geotools.util.logging.Logging;
import si.uom.NonSI;
import si.uom.SI;
import tech.units.indriya.AbstractUnit;

/**
 * Base class for transformation services between ellipsoidal and cartographic projections. This base class provides the
 * basic feature needed for all methods (no need to overrides methods). Subclasses must "only" implements the following
 * methods:
 *
 * <ul>
 *   <li>{@link #getParameterValues}
 *   <li>{@link #transformNormalized}
 *   <li>{@link #inverseTransformNormalized}
 * </ul>
 *
 * <p><strong>NOTE:</strong>Serialization of this class is appropriate for short-term storage or RMI use, but will
 * probably not be compatible with future version. For long term storage, WKT (Well Know Text) or XML (not yet
 * implemented) are more appropriate.
 *
 * @since 2.0
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 * @see <A HREF="http://mathworld.wolfram.com/MapProjection.html">Map projections on MathWorld</A>
 * @see <A HREF="http://atlas.gc.ca/site/english/learningresources/carto_corner/map_projections.html">Map projections on
 *     the atlas of Canada</A>
 */
@SuppressWarnings("FloatingPointLiteralPrecision")
public abstract class MapProjection extends AbstractMathTransform implements MathTransform2D, Serializable {

    /**
     * A global variable use to disable the reciprocal distance checks made when assertions are enabled. This allows
     * tests to work in "real world" conditions, where users often ask for points that are way to far from the correct
     * area of usage of projections
     */
    public static boolean SKIP_SANITY_CHECKS = false;

    /** For cross-version compatibility. */
    private static final long serialVersionUID = -406751619777246914L;

    /** The projection package logger */
    protected static final Logger LOGGER = Logging.getLogger(MapProjection.class);

    /**
     * Maximum difference allowed when comparing real numbers. This field is private because subclasses may use
     * different threshold value.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Maximum difference allowed when comparing longitudes or latitudes in degrees. This tolerance do not apply to
     * angle in radians. A tolerance of 1E-4 is about 10 kilometers.
     */
    private static final double ANGLE_TOLERANCE = 1E-4;

    /** Difference allowed in iterative computations. */
    private static final double ITERATION_TOLERANCE = 1E-10;

    /**
     * Relative iteration precision used in the <code>mlfn<code> method
     */
    private static final double MLFN_TOL = 1E-11;

    /** Maximum number of iterations for iterative computations. */
    private static final int MAXIMUM_ITERATIONS = 15;

    /** Constants used to calculate {@link #en0}, {@link #en1}, {@link #en2}, {@link #en3}, {@link #en4}. */
    private static final double C00 = 1.0,
            C02 = 0.25,
            C04 = 0.046875,
            C06 = 0.01953125,
            C08 = 0.01068115234375,
            C22 = 0.75,
            C44 = 0.46875,
            C46 = 0.01302083333333333333,
            C48 = 0.00712076822916666666,
            C66 = 0.36458333333333333333,
            C68 = 0.00569661458333333333,
            C88 = 0.3076171875;

    /**
     * Ellipsoid excentricity, equals to <code>sqrt({@link #excentricitySquared})</code>. Value 0 means that the
     * ellipsoid is spherical.
     *
     * @see #excentricitySquared
     * @see #isSpherical
     */
    protected final double excentricity;

    /**
     * The square of excentricity: e² = (a²-b²)/a² where <var>e</var> is the {@linkplain #excentricity excentricity},
     * <var>a</var> is the {@linkplain #semiMajor semi major} axis length and <var>b</var> is the {@linkplain #semiMinor
     * semi minor} axis length.
     *
     * @see #excentricity
     * @see #semiMajor
     * @see #semiMinor
     * @see #isSpherical
     */
    protected final double excentricitySquared;

    /**
     * {@code true} if this projection is spherical. Spherical model has identical {@linkplain #semiMajor semi major}
     * and {@linkplain #semiMinor semi minor} axis length, and an {@linkplain #excentricity excentricity} zero.
     *
     * @see #excentricity
     * @see #semiMajor
     * @see #semiMinor
     */
    protected final boolean isSpherical;

    /**
     * Length of semi-major axis, in metres. This is named '<var>a</var>' or '<var>R</var>' (Radius in spherical cases)
     * in Snyder.
     *
     * @see #excentricity
     * @see #semiMinor
     */
    protected final double semiMajor;

    /**
     * Length of semi-minor axis, in metres. This is named '<var>b</var>' in Snyder.
     *
     * @see #excentricity
     * @see #semiMajor
     */
    protected final double semiMinor;

    /**
     * Central longitude in <u>radians</u>. Default value is 0, the Greenwich meridian. This is called
     * '<var>lambda0</var>' in Snyder.
     *
     * <p><strong>Consider this field as final</strong>. It is not final only because some classes need to modify it at
     * construction time.
     */
    protected double centralMeridian;

    /**
     * Latitude of origin in <u>radians</u>. Default value is 0, the equator. This is called '<var>phi0</var>' in
     * Snyder.
     *
     * <p><strong>Consider this field as final</strong>. It is not final only because some classes need to modify it at
     * construction time.
     */
    protected double latitudeOfOrigin;

    /**
     * The scale factor. Default value is 1. Named '<var>k</var>' in Snyder.
     *
     * <p><strong>Consider this field as final</strong>. It is not final only because some classes need to modify it at
     * construction time.
     */
    protected double scaleFactor;

    /** False easting, in metres. Default value is 0. */
    protected final double falseEasting;

    /** False northing, in metres. Default value is 0. */
    protected final double falseNorthing;

    /**
     * Global scale factor. Default value {@code globalScale} is equal to {@link #semiMajor}&times;{@link #scaleFactor}.
     *
     * <p><strong>Consider this field as final</strong>. It is not final only because some classes need to modify it at
     * construction time.
     */
    protected double globalScale;

    /** The inverse of this map projection. Will be created only when needed. */
    private transient MathTransform2D inverse;

    /**
     * Constant needed for the <code>mlfn<code> method.
     * Setup at construction time.
     */
    protected double en0, en1, en2, en3, en4;

    /**
     * When different than {@link #globalRangeCheckSemaphore}, coordinate ranges will be checked and a {@code WARNING}
     * log will be issued if they are out of their natural ranges (-180/180&deg; for longitude, -90/90&deg; for
     * latitude).
     *
     * @see #verifyCoordinateRanges()
     * @see #warningLogged()
     */
    private transient int rangeCheckSemaphore;

    /**
     * The value to be checked against {@link #rangeCheckSemaphore} in order to determine if coordinates ranges should
     * be checked.
     */
    private static int globalRangeCheckSemaphore = 1;

    /** Marks if the projection is invertible. The vast majority is, subclasses can override. */
    protected boolean invertible = true;

    /**
     * Constructs a new map projection from the suplied parameters.
     *
     * @param values The parameter values in standard units. The following parameter are recognized:
     *     <ul>
     *       <li>"semi_major" (mandatory: no default)
     *       <li>"semi_minor" (mandatory: no default)
     *       <li>"central_meridian" (default to 0°)
     *       <li>"latitude_of_origin" (default to 0°)
     *       <li>"scale_factor" (default to 1 )
     *       <li>"false_easting" (default to 0 )
     *       <li>"false_northing" (default to 0 )
     *     </ul>
     *
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected MapProjection(final ParameterValueGroup values) throws ParameterNotFoundException {
        this(values, null);
    }

    /**
     * Constructor invoked by sub-classes when we can't rely on {@link #getParameterDescriptors} before the construction
     * is completed. This is the case when the later method depends on the value of some class's attribute, which has
     * not yet been set. An example is {@link ObliqueMercator#getParameterDescriptors}.
     *
     * <p>This method is not public because it is not a very elegant hack, and a work around exists. For example
     * {@code ObliqueMercator} two-points case could be implemented by as a separated classes, in which case
     * {@link #getParameterDescriptors} returns a constant and can be safely invoked in a constructor.
     */
    MapProjection(final ParameterValueGroup values, Collection<GeneralParameterDescriptor> expected)
            throws ParameterNotFoundException {
        if (expected == null) {
            expected = getParameterDescriptors().descriptors();
        }
        semiMajor = doubleValue(expected, AbstractProvider.SEMI_MAJOR, values);
        semiMinor = doubleValue(expected, AbstractProvider.SEMI_MINOR, values);
        centralMeridian = doubleValue(expected, AbstractProvider.CENTRAL_MERIDIAN, values);
        latitudeOfOrigin = doubleValue(expected, AbstractProvider.LATITUDE_OF_ORIGIN, values);
        scaleFactor = doubleValue(expected, AbstractProvider.SCALE_FACTOR, values);
        falseEasting = doubleValue(expected, AbstractProvider.FALSE_EASTING, values);
        falseNorthing = doubleValue(expected, AbstractProvider.FALSE_NORTHING, values);
        isSpherical = (semiMajor == semiMinor);
        excentricitySquared = 1.0 - (semiMinor * semiMinor) / (semiMajor * semiMajor);
        excentricity = sqrt(excentricitySquared);
        globalScale = scaleFactor * semiMajor;
        ensureLongitudeInRange(AbstractProvider.CENTRAL_MERIDIAN, centralMeridian, true);
        ensureLatitudeInRange(AbstractProvider.LATITUDE_OF_ORIGIN, latitudeOfOrigin, true);

        //  Compute constants for the mlfn
        double t;
        final double es = excentricitySquared;
        en0 = C00 - es * (C02 + es * (C04 + es * (C06 + es * C08)));
        en1 = es * (C22 - es * (C04 + es * (C06 + es * C08)));
        en2 = (t = es * es) * (C44 - es * (C46 + es * C48));
        en3 = (t *= es) * (C66 - es * C68);
        en4 = t * es * C88;
    }

    /**
     * Returns {@code true} if the specified parameter can apply to this map projection. The set of expected parameters
     * must be supplied. The default implementation just invokes {@code expected.contains(param)}. Some subclasses will
     * override this method in order to handle {@link ModifiedParameterDescriptor} in a special way.
     *
     * @see #doubleValue
     * @see #set
     */
    boolean isExpectedParameter(
            final Collection<GeneralParameterDescriptor> expected, final ParameterDescriptor param) {
        return expected.contains(param);
    }

    /**
     * Returns the parameter value for the specified operation parameter. Values are automatically converted into the
     * standard units specified by the supplied {@code param} argument, except {@link NonSI#DEGREE_ANGLE degrees} which
     * are converted to {@link SI#RADIAN radians}.
     *
     * @param expected The value returned by {@code getParameterDescriptors().descriptors()}.
     * @param param The parameter to look for.
     * @param group The parameter value group to search into.
     * @return The requested parameter value, or {@code NaN} if {@code param} is
     *     {@linkplain MathTransformProvider#createOptionalDescriptor optional} and the user didn't provided any value.
     * @throws ParameterNotFoundException if the parameter is not found.
     * @see MathTransformProvider#doubleValue
     */
    final double doubleValue(
            final Collection<GeneralParameterDescriptor> expected,
            final ParameterDescriptor param,
            final ParameterValueGroup group)
            throws ParameterNotFoundException {
        if (isExpectedParameter(expected, param)) {
            /*
             * Gets the value supplied by the user. The conversion from decimal
             * degrees to radians (if needed) is performed by AbstractProvider.
             */
            return AbstractProvider.doubleValue(param, group);
        }
        /*
         * The constructor asked for a parameter value that do not apply to the type of the
         * projection to be created. Returns a default value common to all projection types,
         * but this value should not be used in projection computations.
         */
        double v;
        final Object value = param.getDefaultValue();
        if (value instanceof Number) {
            v = ((Number) value).doubleValue();
            if (NonSI.DEGREE_ANGLE.equals(param.getUnit())) {
                v = toRadians(v);
            }
        } else {
            v = Double.NaN;
        }
        return v;
    }

    /**
     * Ensures that this projection has equals semi-major and semi-minor axis. This method is invoked by constructors of
     * classes implementing only spherical formulas.
     */
    final void ensureSpherical() throws IllegalArgumentException {
        if (!isSpherical) {
            throw new IllegalArgumentException(ErrorKeys.ELLIPTICAL_NOT_SUPPORTED);
        }
    }

    /**
     * Ensures that the absolute value of a latitude is equals to the specified value, up to the tolerance value. The
     * expected value is usually either {@code 0} or {@code PI/2} (the equator or a pole).
     *
     * @param y Latitude to check, in radians.
     * @param expected The expected value, in radians.
     * @throws IllegalArgumentException if the latitude is not the expected one.
     */
    static void ensureLatitudeEquals(final ParameterDescriptor name, double y, double expected)
            throws IllegalArgumentException {
        if (!(abs(abs(y) - expected) < EPSILON)) {
            y = toDegrees(y);
            final String n = name.getName().getCode();
            final Object arg1 = new Latitude(y);
            throw new InvalidParameterValueException(
                    MessageFormat.format(ErrorKeys.ILLEGAL_ARGUMENT_$2, n, arg1), n, y);
        }
    }

    /**
     * Ensures that the latitude is within allowed limits (&plusmn;&pi;/2). This method is useful to check the validity
     * of projection parameters, like {@link #latitudeOfOrigin}.
     *
     * @param y Latitude to check, in radians.
     * @param edge {@code true} to accept latitudes of &plusmn;&pi;/2.
     * @throws IllegalArgumentException if the latitude is out of range.
     */
    static void ensureLatitudeInRange(final ParameterDescriptor name, double y, final boolean edge)
            throws IllegalArgumentException {
        if (edge
                ? (y >= Latitude.MIN_VALUE * PI / 180 && y <= Latitude.MAX_VALUE * PI / 180)
                : (y > Latitude.MIN_VALUE * PI / 180 && y < Latitude.MAX_VALUE * PI / 180)) {
            return;
        }
        y = toDegrees(y);
        final Object arg0 = new Latitude(y);
        throw new InvalidParameterValueException(
                MessageFormat.format(ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, arg0),
                name.getName().getCode(),
                y);
    }

    /**
     * Ensures that the longitue is within allowed limits (&plusmn;&pi;). This method is used to check the validity of
     * projection parameters, like {@link #centralMeridian}.
     *
     * @param x Longitude to verify, in radians.
     * @param edge {@code true} for accepting longitudes of &plusmn;&pi;.
     * @throws IllegalArgumentException if the longitude is out of range.
     */
    static void ensureLongitudeInRange(final ParameterDescriptor name, double x, final boolean edge)
            throws IllegalArgumentException {
        if (edge
                ? (x >= Longitude.MIN_VALUE * PI / 180 && x <= Longitude.MAX_VALUE * PI / 180)
                : (x > Longitude.MIN_VALUE * PI / 180 && x < Longitude.MAX_VALUE * PI / 180)) {
            return;
        }
        x = toDegrees(x);
        final Object arg0 = new Longitude(x);
        throw new InvalidParameterValueException(
                MessageFormat.format(ErrorKeys.LONGITUDE_OUT_OF_RANGE_$1, arg0),
                name.getName().getCode(),
                x);
    }

    /**
     * Verifies if the given coordinates are in the range of geographic coordinates. If they are not, then this method
     * logs a warning and returns {@code true}. Otherwise this method does nothing and returns {@code false}.
     *
     * @param tr The caller.
     * @param x The longitude in decimal degrees.
     * @param y The latitude in decimal degrees.
     * @return {@code true} if the coordinates are not in the geographic range, in which case a warning has been logged.
     */
    private static boolean verifyGeographicRanges(final AbstractMathTransform tr, final double x, final double y) {
        // Note: the following tests should not fails for NaN values.
        final boolean xOut =
                (x < (Longitude.MIN_VALUE - ANGLE_TOLERANCE) || x > (Longitude.MAX_VALUE + ANGLE_TOLERANCE));
        final boolean yOut = (y < (Latitude.MIN_VALUE - ANGLE_TOLERANCE) || y > (Latitude.MAX_VALUE + ANGLE_TOLERANCE));
        if (!xOut && !yOut) {
            return false;
        }
        final String lineSeparator = System.getProperty("line.separator", "\n");
        final StringBuilder buffer = new StringBuilder();
        final Object arg01 = tr.getName();
        buffer.append(MessageFormat.format(ErrorKeys.OUT_OF_PROJECTION_VALID_AREA_$1, arg01));
        if (xOut) {
            buffer.append(lineSeparator);
            final Object arg0 = new Longitude(x);
            buffer.append(MessageFormat.format(ErrorKeys.LONGITUDE_OUT_OF_RANGE_$1, arg0));
        }
        if (yOut) {
            buffer.append(lineSeparator);
            final Object arg0 = new Latitude(y);
            buffer.append(MessageFormat.format(ErrorKeys.LATITUDE_OUT_OF_RANGE_$1, arg0));
        }
        final LogRecord record = new LogRecord(Level.WARNING, buffer.toString());
        final String classe;
        if (tr instanceof Inverse) {
            classe = ((Inverse) tr).inverse().getClass().getName() + ".Inverse";
        } else {
            classe = tr.getClass().getName();
        }
        record.setSourceClassName(classe);
        record.setSourceMethodName("transform");
        record.setLoggerName(LOGGER.getName());
        LOGGER.log(record);
        return true;
    }

    /**
     * Sets the value in a parameter group. This convenience method is used by subclasses for
     * {@link #getParameterValues} implementation. Values are automatically converted from radians to decimal degrees if
     * needed.
     *
     * @param expected The value returned by {@code getParameterDescriptors().descriptors()}.
     * @param param One of the {@link AbstractProvider} constants.
     * @param group The group in which to set the value.
     * @param value The value to set.
     */
    final void set(
            final Collection<GeneralParameterDescriptor> expected,
            final ParameterDescriptor<?> param,
            final ParameterValueGroup group,
            double value) {
        if (isExpectedParameter(expected, param)) {
            if (NonSI.DEGREE_ANGLE.equals(param.getUnit())) {
                /*
                 * Converts radians to degrees and try to fix rounding error
                 * (e.g. -61.500000000000014  -->  -61.5). This is necessary
                 * in order to avoid a bias when formatting a transform and
                 * parsing it again.
                 */
                value = toDegrees(value);
                double old = value;
                value = XMath.trimDecimalFractionDigits(value, 4, 12);
                if (value == old) {
                    /*
                     * The attempt to fix rounding error failed. Try again with the
                     * assumption that the true value is a multiple of 1/3 of angle
                     * (e.g. 51.166666666666664  -->  51.166666666666666), which is
                     * common in the EPSG database.
                     */
                    old *= 3;
                    final double test = XMath.trimDecimalFractionDigits(old, 4, 12);
                    if (test != old) {
                        value = test / 3;
                    }
                }
            }
            group.parameter(param.getName().getCode()).setValue(value);
        }
    }

    /**
     * Returns the parameter descriptors for this map projection. This is used for a providing a default implementation
     * of {@link #getParameterValues}, as well as arguments checking.
     */
    @Override
    public abstract ParameterDescriptorGroup getParameterDescriptors();

    /**
     * Returns the parameter values for this map projection.
     *
     * @return A copy of the parameter values for this map projection.
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterDescriptorGroup descriptor = getParameterDescriptors();
        final Collection<GeneralParameterDescriptor> expected = descriptor.descriptors();
        final ParameterValueGroup values = descriptor.createValue();
        set(expected, AbstractProvider.SEMI_MAJOR, values, semiMajor);
        set(expected, AbstractProvider.SEMI_MINOR, values, semiMinor);
        set(expected, AbstractProvider.CENTRAL_MERIDIAN, values, centralMeridian);
        set(expected, AbstractProvider.LATITUDE_OF_ORIGIN, values, latitudeOfOrigin);
        set(expected, AbstractProvider.SCALE_FACTOR, values, scaleFactor);
        set(expected, AbstractProvider.FALSE_EASTING, values, falseEasting);
        set(expected, AbstractProvider.FALSE_NORTHING, values, falseNorthing);
        return values;
    }

    /** Returns the dimension of input points. */
    @Override
    public final int getSourceDimensions() {
        return 2;
    }

    /** Returns the dimension of output points. */
    @Override
    public final int getTargetDimensions() {
        return 2;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                          TRANSFORMATION METHODS                          ////////
    ////////             Includes an inner class for inverse projections.             ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * Returns the orthodromic distance between the two specified points using a spherical approximation. This is used
     * for assertions only.
     */
    protected double orthodromicDistance(final Point2D source, final Point2D target) {
        // The orthodromic distance calculation here does not work well over short
        // distances, so we only use it if we believe the distance is significant.
        if (source.distanceSq(target) > 1.0) {
            final double y1 = toRadians(source.getY());
            final double y2 = toRadians(target.getY());
            final double dx = toRadians(abs(target.getX() - source.getX()) % 360);
            double rho = sin(y1) * sin(y2) + cos(y1) * cos(y2) * cos(dx);
            if (rho > +1) {
                assert rho <= +(1 + EPSILON) : rho;
                rho = +1;
            }
            if (rho < -1) {
                assert rho >= -(1 + EPSILON) : rho;
                rho = -1;
            }
            return acos(rho) * semiMajor;
        } else {
            // Otherwise we approximate using alternate means. This is based
            // on the Haversine formula to compute the arc angle between the
            // point (derived from S2LatLng.getDistance()) which is stable for
            // small distances.
            double lat1 = toRadians(source.getY());
            double lat2 = toRadians(target.getY());
            double lng1 = toRadians(source.getX());
            double lng2 = toRadians(target.getX());
            double dlat = Math.sin(0.5 * (lat2 - lat1));
            double dlng = Math.sin(0.5 * (lng2 - lng1));
            double x = dlat * dlat + dlng * dlng * Math.cos(lat1) * Math.cos(lat2);
            double arcRadians = 2 * Math.atan2(Math.sqrt(x), Math.sqrt(Math.max(0.0, 1.0 - x)));
            return arcRadians * semiMajor;
        }
    }

    /**
     * Check point for private use by {@link #checkReciprocal}. This class is necessary in order to avoid never-ending
     * loop in {@code assert} statements (when an {@code assert} calls {@code transform(...)}, which calls
     * {@code inverse.transform(...)}, which calls {@code transform(...)}, etc.).
     */
    @SuppressWarnings("serial")
    private static final class CheckPoint extends Point2D.Double {
        public CheckPoint(final Point2D point) {
            super(point.getX(), point.getY());
        }
    }

    /**
     * Check if the transform of {@code point} is close enough to {@code target}. "Close enough" means that the two
     * points are separated by a distance shorter than {@link #getToleranceForAssertions}. This method is used for
     * assertions with J2SE 1.4.
     *
     * @param point Point to transform, in decimal degrees if {@code inverse} is {@code false}.
     * @param target Point to compare to, in metres if {@code inverse} is {@code false}.
     * @param inverse {@code true} for an inverse transform instead of a direct one.
     * @return {@code true} if the two points are close enough.
     */
    protected boolean checkReciprocal(Point2D point, final Point2D target, final boolean inverse)
            throws ProjectionException {
        if (SKIP_SANITY_CHECKS) {
            return true;
        }

        if (!(point instanceof CheckPoint))
            try {
                point = new CheckPoint(point);
                final double longitude;
                final double latitude;
                final double distance;
                if (inverse) {
                    // Computes orthodromic distance (spherical model) in metres.
                    point = inverse().transform(point, point);
                    distance = orthodromicDistance(point, target);
                    longitude = point.getX();
                    latitude = point.getY();
                } else {
                    // Computes cartesian distance in metres.
                    longitude = point.getX();
                    latitude = point.getY();
                    point = transform(point, point);
                    distance = point.distance(target);
                }
                if (distance > getToleranceForAssertions(longitude, latitude)) {
                    /*
                     * Do not fail for NaN values. For other cases we must throw a ProjectionException,
                     * not an AssertionError, because some code like CRS.transform(CoordinateOperation,
                     * ...) will project points that are know to be suspicious by surrounding them in
                     * "try ... catch" statements. Failure are normal in their case and we want to let
                     * them handle the exception the way they are used to.
                     */
                    final Object arg1 = new Longitude(longitude - toDegrees(centralMeridian));
                    final Object arg2 = new Latitude(latitude - toDegrees(latitudeOfOrigin));
                    final Object arg3 = getName();
                    throw new ProjectionException(
                            MessageFormat.format(ErrorKeys.PROJECTION_CHECK_FAILED_$4, distance, arg1, arg2, arg3));
                }
            } catch (ProjectionException exception) {
                throw exception;
            } catch (TransformException exception) {
                throw new ProjectionException(exception);
            }
        return true;
    }

    /**
     * Checks if transform using spherical formulas produces the same result than ellipsoidal formulas. This method is
     * invoked during assertions only.
     *
     * @param x The easting computed by spherical formulas, in metres.
     * @param y The northing computed by spherical formulas, in metres.
     * @param expected The (easting,northing) computed by ellipsoidal formulas.
     * @param tolerance The tolerance (optional).
     */
    static boolean checkTransform(final double x, final double y, final Point2D expected, final double tolerance) {
        if (SKIP_SANITY_CHECKS) {
            return true;
        }
        compare("x", expected.getX(), x, tolerance);
        compare("y", expected.getY(), y, tolerance);
        return tolerance < Double.POSITIVE_INFINITY;
    }

    /** Default version of {@link #checkTransform(double,double,Point2D,double)}. */
    static boolean checkTransform(final double x, final double y, final Point2D expected) {
        return checkTransform(x, y, expected, EPSILON);
    }

    /**
     * Checks if inverse transform using spherical formulas produces the same result than ellipsoidal formulas. This
     * method is invoked during assertions only.
     *
     * <p><strong>Note:</strong> this method ignores the longitude if the latitude is at a pole, because in such case
     * the longitude is meanless.
     *
     * @param longitude The longitude computed by spherical formulas, in radians.
     * @param latitude The latitude computed by spherical formulas, in radians.
     * @param expected The (longitude,latitude) computed by ellipsoidal formulas.
     * @param tolerance The tolerance (optional).
     */
    static boolean checkInverseTransform(
            final double longitude, final double latitude, final Point2D expected, final double tolerance) {
        compare("latitude", expected.getY(), latitude, tolerance);
        if (abs(PI / 2 - abs(latitude)) > EPSILON) {
            compare("longitude", expected.getX(), longitude, tolerance);
        }
        return tolerance < Double.POSITIVE_INFINITY;
    }

    /** Default version of {@link #checkInverseTransform(double,double,Point2D,double)}. */
    static boolean checkInverseTransform(double longitude, double latitude, Point2D expected) {
        return checkInverseTransform(longitude, latitude, expected, EPSILON);
    }

    /**
     * Compares two value for equality up to some tolerance threshold. This is used during assertions only. The
     * comparaison do not fails if at least one value to compare is {@link Double#NaN}.
     *
     * <p><strong>Hack:</strong> if the {@code variable} name starts by lower-case {@code L} (as in "longitude" and
     * "latitude"), then the value is assumed to be an angle in radians. This is used for formatting an error message,
     * if needed.
     */
    private static void compare(String variable, double expected, double actual, double tolerance) {
        if (abs(expected - actual) > tolerance) {
            if (variable.charAt(0) == 'l') {
                actual = toDegrees(actual);
                expected = toDegrees(expected);
            }
            throw new AssertionError(MessageFormat.format(ErrorKeys.TEST_FAILURE_$3, variable, expected, actual));
        }
    }

    /**
     * Transforms the specified coordinate and stores the result in {@code ptDst}. This method returns longitude as
     * <var>x</var> values in the range {@code [-PI..PI]} and latitude as <var>y</var> values in the range
     * {@code [-PI/2..PI/2]}. It will be checked by the caller, so this method doesn't need to performs this check.
     *
     * <p>Input coordinates have the {@link #falseEasting} and {@link #falseNorthing} removed and are divided by
     * {@link #globalScale} before this method is invoked. After this method is invoked, the {@link #centralMeridian} is
     * added to the {@code x} results in {@code ptDst}. This means that projections that implement this method are
     * performed on an ellipse (or sphere) with a semi-major axis of 1.
     *
     * <p>In <A HREF="http://www.remotesensing.org/proj/">PROJ.4</A>, the same standardization, described above, is
     * handled by {@code pj_inv.c}. Therefore when porting projections from PROJ.4, the inverse transform equations can
     * be used directly here with minimal change. In the equations of Snyder, {@link #falseEasting},
     * {@link #falseNorthing} and {@link #scaleFactor} are usually not given. When implementing these equations here,
     * you will not need to add the {@link #centralMeridian} to the output longitude or remove the {@link #semiMajor}
     * (<var>a</var> or <var>R</var>).
     *
     * @param x The easting of the coordinate, linear distance on a unit sphere or ellipse.
     * @param y The northing of the coordinate, linear distance on a unit sphere or ellipse.
     * @param ptDst the specified coordinate point that stores the result of transforming {@code ptSrc}, or
     *     {@code null}. Ordinates will be in <strong>radians</strong>.
     * @return the coordinate point after transforming {@code x}, {@code y} and storing the result in {@code ptDst}.
     * @throws ProjectionException if the point can't be transformed.
     */
    protected abstract Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException;

    /**
     * Transforms the specified coordinate and stores the result in {@code ptDst}. This method is usually (but
     * <strong>not</strong> guaranteed) to be invoked with values of <var>x</var> in the range {@code [-PI..PI]} and
     * values of <var>y</var> in the range {@code [-PI/2..PI/2]}. Values outside those ranges are accepted (sometime
     * with a warning logged) on the assumption that most implementations use those values only in trigonometric
     * functions like {@linkplain Math#sin sin} and {@linkplain Math#cos cos}.
     *
     * <p>Coordinates have the {@link #centralMeridian} removed from <var>lambda</var> before this method is invoked.
     * After this method is invoked, the results in {@code ptDst} are multiplied by {@link #globalScale}, and the
     * {@link #falseEasting} and {@link #falseNorthing} are added. This means that projections that implement this
     * method are performed on an ellipse (or sphere) with a semi-major axis of 1.
     *
     * <p>In <A HREF="http://www.remotesensing.org/proj/">PROJ.4</A>, the same standardization, described above, is
     * handled by {@code pj_fwd.c}. Therefore when porting projections from PROJ.4, the forward transform equations can
     * be used directly here with minimal change. In the equations of Snyder, {@link #falseEasting},
     * {@link #falseNorthing} and {@link #scaleFactor} are usually not given. When implementing these equations here,
     * you will not need to remove the {@link #centralMeridian} from <var>lambda</var> or apply the {@link #semiMajor}
     * (<var>a</var> or <var>R</var>).
     *
     * @param lambda The longitude of the coordinate, in <strong>radians</strong>.
     * @param phi The latitude of the coordinate, in <strong>radians</strong>.
     * @param ptDst the specified coordinate point that stores the result of transforming {@code ptSrc}, or
     *     {@code null}. Ordinates will be in a dimensionless unit, as a linear distance on a unit sphere or ellipse.
     * @return the coordinate point after transforming ({@code lambda}, {@code phi}) and storing the result in
     *     {@code ptDst}.
     * @throws ProjectionException if the point can't be transformed.
     */
    protected abstract Point2D transformNormalized(double lambda, double phi, final Point2D ptDst)
            throws ProjectionException;

    /**
     * Transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
     *
     * <p>This method standardizes the source {@code x} coordinate by removing the {@link #centralMeridian}, before
     * invoking <code>
     * {@link #transformNormalized transformNormalized}(x, y, ptDst)</code>. It also multiplies by {@link #globalScale}
     * and adds the {@link #falseEasting} and {@link #falseNorthing} to the point returned by the
     * {@code transformNormalized(...)} call.
     *
     * @param ptSrc the specified coordinate point to be transformed. Ordinates must be in decimal degrees.
     * @param ptDst the specified coordinate point that stores the result of transforming {@code ptSrc}, or
     *     {@code null}. Ordinates will be in metres.
     * @return the coordinate point after transforming {@code ptSrc} and storing the result in {@code ptDst}.
     * @throws ProjectionException if the point can't be transformed.
     */
    @Override
    public final Point2D transform(final Point2D ptSrc, Point2D ptDst) throws ProjectionException {
        final double x = ptSrc.getX();
        final double y = ptSrc.getY();
        if (verifyCoordinateRanges()) {
            if (verifyGeographicRanges(this, x, y)) {
                warningLogged();
            }
        }
        /*
         * Makes sure that the longitude before conversion stay within +/- PI radians. As a
         * special case, we do not check the range if no rotation were applied on the longitude.
         * This is because the user may have a big area ranging from -180° to +180°. With the
         * slight rounding errors related to map projections, the 180° longitude may be slightly
         * over the limit. Rolling the longitude would changes its sign. For example a bounding
         * box from 30° to +180° would become 30° to -180°, which is probably not what the user
         * wanted.
         */
        ptDst = transformNormalized(
                centralMeridian != 0 ? rollLongitude(toRadians(x) - centralMeridian) : toRadians(x),
                toRadians(y),
                ptDst);
        ptDst.setLocation(globalScale * ptDst.getX() + falseEasting, globalScale * ptDst.getY() + falseNorthing);

        if (invertible) {
            assert checkReciprocal(ptDst, (ptSrc != ptDst) ? ptSrc : new Point2D.Double(x, y), true);
        }
        return ptDst;
    }

    /**
     * Transforms a list of coordinate point ordinal values. Ordinates must be
     * (<var>longitude</var>,<var>latitude</var>) pairs in decimal degrees.
     *
     * @throws ProjectionException if a point can't be transformed. This method tries to transform every points even if
     *     some of them can't be transformed. Non-transformable points will have value {@link Double#NaN}. If more than
     *     one point can't be transformed, then this exception may be about an arbitrary point.
     */
    @Override
    public final void transform(final double[] srcPts, int srcOff, final double[] dstPts, int dstOff, int numPts)
            throws ProjectionException {
        /*
         * Vérifie s'il faudra parcourir le tableau en sens inverse.
         * Ce sera le cas si les tableaux source et destination se
         * chevauchent et que la destination est après la source.
         */
        final boolean reverse = (srcPts == dstPts && srcOff < dstOff && srcOff + (2 * numPts) > dstOff);
        if (reverse) {
            srcOff += 2 * numPts;
            dstOff += 2 * numPts;
        }
        final Point2D.Double point = new Point2D.Double();
        ProjectionException firstException = null;
        while (--numPts >= 0) {
            try {
                point.x = srcPts[srcOff++];
                point.y = srcPts[srcOff++];
                transform(point, point);
                dstPts[dstOff++] = point.x;
                dstPts[dstOff++] = point.y;
            } catch (ProjectionException exception) {
                dstPts[dstOff++] = Double.NaN;
                dstPts[dstOff++] = Double.NaN;
                if (firstException == null) {
                    firstException = exception;
                }
            }
            if (reverse) {
                srcOff -= 4;
                dstOff -= 4;
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }

    /**
     * Transforms a list of coordinate point ordinal values. Ordinates must be
     * (<var>longitude</var>,<var>latitude</var>) pairs in decimal degrees.
     *
     * @throws ProjectionException if a point can't be transformed. This method tries to transform every points even if
     *     some of them can't be transformed. Non-transformable points will have value {@link Float#NaN}. If more than
     *     one point can't be transformed, then this exception may be about an arbitrary point.
     */
    @Override
    public final void transform(final float[] srcPts, int srcOff, final float[] dstPts, int dstOff, int numPts)
            throws ProjectionException {
        final boolean reverse = (srcPts == dstPts && srcOff < dstOff && srcOff + (2 * numPts) > dstOff);
        if (reverse) {
            srcOff += 2 * numPts;
            dstOff += 2 * numPts;
        }
        final Point2D.Double point = new Point2D.Double();
        ProjectionException firstException = null;
        while (--numPts >= 0) {
            try {
                point.x = srcPts[srcOff++];
                point.y = srcPts[srcOff++];
                transform(point, point);
                dstPts[dstOff++] = (float) point.x;
                dstPts[dstOff++] = (float) point.y;
            } catch (ProjectionException exception) {
                dstPts[dstOff++] = Float.NaN;
                dstPts[dstOff++] = Float.NaN;
                if (firstException == null) {
                    firstException = exception;
                }
            }
            if (reverse) {
                srcOff -= 4;
                dstOff -= 4;
            }
        }
        if (firstException != null) {
            throw firstException;
        }
    }

    /**
     * Inverse of a map projection. Will be created by {@link MapProjection#inverse()} only when first required.
     * Implementation of {@code transform(...)} methods are mostly identical to {@code MapProjection.transform(...)},
     * except that they will invokes {@link MapProjection#inverseTransformNormalized} instead of
     * {@link MapProjection#transformNormalized}.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     */
    private final class Inverse extends AbstractMathTransform.Inverse implements MathTransform2D {
        /** For cross-version compatibility. */
        private static final long serialVersionUID = -9138242780765956870L;

        /** Default constructor. */
        public Inverse() {
            MapProjection.this.super();
        }

        /**
         * Inverse transforms the specified {@code ptSrc} and stores the result in {@code ptDst}.
         *
         * <p>This method standardizes the {@code ptSrc} by removing the {@link #falseEasting} and
         * {@link #falseNorthing} and dividing by {@link #globalScale} before invoking <code>
         * {@link #inverseTransformNormalized inverseTransformNormalized}(x, y, ptDst)</code>. It then adds the
         * {@link #centralMeridian} to the {@code x} of the point returned by the {@code inverseTransformNormalized}
         * call.
         *
         * @param ptSrc the specified coordinate point to be transformed. Ordinates must be in metres.
         * @param ptDst the specified coordinate point that stores the result of transforming {@code ptSrc}, or
         *     {@code null}. Ordinates will be in decimal degrees.
         * @return the coordinate point after transforming {@code ptSrc} and stroring the result in {@code ptDst}.
         * @throws ProjectionException if the point can't be transformed.
         */
        @Override
        public final Point2D transform(final Point2D ptSrc, Point2D ptDst) throws ProjectionException {
            final double x0 = ptSrc.getX();
            final double y0 = ptSrc.getY();
            ptDst = inverseTransformNormalized(
                    (x0 - falseEasting) / globalScale, (y0 - falseNorthing) / globalScale, ptDst);
            /*
             * Makes sure that the longitude after conversion stay within +/- PI radians. As a
             * special case, we do not check the range if no rotation were applied on the longitude.
             * This is because the user may have a big area ranging from -180° to +180°. With the
             * slight rounding errors related to map projections, the 180° longitude may be slightly
             * over the limit. Rolling the longitude would changes its sign. For example a bounding
             * box from 30° to +180° would become 30° to -180°, which is probably not what the user
             * wanted.
             */
            final double x =
                    toDegrees(centralMeridian != 0 ? rollLongitude(ptDst.getX() + centralMeridian) : ptDst.getX());
            final double y = toDegrees(ptDst.getY());
            ptDst.setLocation(x, y);
            if (verifyCoordinateRanges()) {
                if (verifyGeographicRanges(this, x, y)) {
                    warningLogged();
                }
            }
            assert checkReciprocal(ptDst, (ptSrc != ptDst) ? ptSrc : new Point2D.Double(x0, y0), false);
            return ptDst;
        }

        /**
         * Inverse transforms a list of coordinate point ordinal values. Ordinates must be (<var>x</var>,<var>y</var>)
         * pairs in metres.
         *
         * @throws ProjectionException if a point can't be transformed. This method tries to transform every points even
         *     if some of them can't be transformed. Non-transformable points will have value {@link Double#NaN}. If
         *     more than one point can't be transformed, then this exception may be about an arbitrary point.
         */
        @Override
        public final void transform(final double[] src, int srcOffset, final double[] dest, int dstOffset, int numPts)
                throws TransformException {
            /*
             * Vérifie s'il faudra parcourir le tableau en sens inverse.
             * Ce sera le cas si les tableaux source et destination se
             * chevauchent et que la destination est après la source.
             */
            final boolean reverse = (src == dest && srcOffset < dstOffset && srcOffset + (2 * numPts) > dstOffset);
            if (reverse) {
                srcOffset += 2 * numPts;
                dstOffset += 2 * numPts;
            }
            final Point2D.Double point = new Point2D.Double();
            ProjectionException firstException = null;
            while (--numPts >= 0) {
                try {
                    point.x = src[srcOffset++];
                    point.y = src[srcOffset++];
                    transform(point, point);
                    dest[dstOffset++] = point.x;
                    dest[dstOffset++] = point.y;
                } catch (ProjectionException exception) {
                    dest[dstOffset++] = Double.NaN;
                    dest[dstOffset++] = Double.NaN;
                    if (firstException == null) {
                        firstException = exception;
                    }
                }
                if (reverse) {
                    srcOffset -= 4;
                    dstOffset -= 4;
                }
            }
            if (firstException != null) {
                throw firstException;
            }
        }

        /**
         * Inverse transforms a list of coordinate point ordinal values. Ordinates must be (<var>x</var>,<var>y</var>)
         * pairs in metres.
         *
         * @throws ProjectionException if a point can't be transformed. This method tries to transform every points even
         *     if some of them can't be transformed. Non-transformable points will have value {@link Float#NaN}. If more
         *     than one point can't be transformed, then this exception may be about an arbitrary point.
         */
        @Override
        public final void transform(final float[] src, int srcOffset, final float[] dest, int dstOffset, int numPts)
                throws ProjectionException {
            final boolean reverse = (src == dest && srcOffset < dstOffset && srcOffset + (2 * numPts) > dstOffset);
            if (reverse) {
                srcOffset += 2 * numPts;
                dstOffset += 2 * numPts;
            }
            final Point2D.Double point = new Point2D.Double();
            ProjectionException firstException = null;
            while (--numPts >= 0) {
                try {
                    point.x = src[srcOffset++];
                    point.y = src[srcOffset++];
                    transform(point, point);
                    dest[dstOffset++] = (float) point.x;
                    dest[dstOffset++] = (float) point.y;
                } catch (ProjectionException exception) {
                    dest[dstOffset++] = Float.NaN;
                    dest[dstOffset++] = Float.NaN;
                    if (firstException == null) {
                        firstException = exception;
                    }
                }
                if (reverse) {
                    srcOffset -= 4;
                    dstOffset -= 4;
                }
            }
            if (firstException != null) {
                throw firstException;
            }
        }

        /** Returns the original map projection. */
        @Override
        public MathTransform2D inverse() {
            return (MathTransform2D) super.inverse();
        }
    }

    /** Returns the inverse of this map projection. */
    @Override
    public final MathTransform2D inverse() throws NoninvertibleTransformException {
        if (!invertible) {
            throw new NoninvertibleTransformException(ErrorKeys.NONINVERTIBLE_TRANSFORM);
        }

        // No synchronization. Not a big deal if this method is invoked in
        // the same time by two threads resulting in two instances created.
        if (inverse == null) {
            inverse = new Inverse();
        }
        return inverse;
    }

    /**
     * Maximal error (in metres) tolerated for assertions, if enabled. When assertions are enabled, every direct
     * projection is followed by an inverse projection, and the result is compared to the original coordinate. If a
     * distance greater than the tolerance level is found, then an {@link ProjectionException} will be thrown.
     * Subclasses should override this method if they need to relax the tolerance level.
     *
     * @param longitude The longitude in decimal degrees.
     * @param latitude The latitude in decimal degrees.
     * @return The tolerance level for assertions, in meters.
     */
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        final double delta = abs(longitude - centralMeridian) / 2 + abs(latitude - latitudeOfOrigin);
        if (delta > 40) {
            // When far from the valid area, use a larger tolerance.
            return 1;
        }
        // Be less strict when the point is near an edge.
        return (abs(longitude) > 179) || (abs(latitude) > 89) ? 1E-1 : 3E-3;
    }

    /**
     * When {@code true}, coordinate ranges will be checked, and a {@link Level#WARNING WARNING} log will be issued if
     * they are out of their natural ranges (-180/180&deg; for longitude, -90/90&deg; for latitude).
     *
     * <p>To avoid excessive logging, this flag will be set to {@code false} after the first coordinate failing the
     * checks is found.
     */
    final boolean verifyCoordinateRanges() {
        /*
         * Do not synchronize - doing so would be a major bottleneck since this method will be
         * invoked thousands of time. The consequence is that a call to {@link #resetWarnings}
         * may not be reflected immediately in other threads, but the later is defined only on
         * a "best effort" basis.
         */
        return rangeCheckSemaphore != globalRangeCheckSemaphore && !SKIP_SANITY_CHECKS;
    }

    /** To be invoked after a warning in order to disable subsequent warnings. */
    final void warningLogged() {
        synchronized (MapProjection.class) {
            rangeCheckSemaphore = globalRangeCheckSemaphore;
        }
    }

    /**
     * Resets the warning status of all projections in the current JVM. Every {@link MapProjection} instance may log a
     * warning the first time they are given coordinates outside their area of validity. Subsequent coordinates outside
     * the area of validity are silently projected in order to avoid flowing the log with warnings. In case of
     * suspicion, this method may be invoked in order to force all projections to log again their first out-of-bounds
     * coordinates.
     *
     * <p><b>Multi-threading</b><br>
     * Calls to this method have immediate effect in the invoker's thread. The effect in other threads may be delayed by
     * some arbitrary amount of time. This method works only on a "best effort" basis.
     *
     * @see org.geotools.referencing.CRS#reset
     * @since 2.5
     */
    public static synchronized void resetWarnings() {
        globalRangeCheckSemaphore++;
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////      IMPLEMENTATION OF Object AND MathTransform2D STANDARD METHODS       ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /** Returns a hash value for this map projection. */
    @Override
    public int hashCode() {
        long code = Double.doubleToLongBits(semiMajor);
        code = code * 37 + Double.doubleToLongBits(semiMinor);
        code = code * 37 + Double.doubleToLongBits(centralMeridian);
        code = code * 37 + Double.doubleToLongBits(latitudeOfOrigin);
        return (int) code ^ (int) (code >>> 32);
    }

    /** Compares the specified object with this map projection for equality. */
    @Override
    public boolean equals(final Object object) {
        // Do not check 'object==this' here, since this
        // optimization is usually done in subclasses.
        if (super.equals(object)) {
            final MapProjection that = (MapProjection) object;
            return equals(this.semiMajor, that.semiMajor)
                    && equals(this.semiMinor, that.semiMinor)
                    && equals(this.centralMeridian, that.centralMeridian)
                    && equals(this.latitudeOfOrigin, that.latitudeOfOrigin)
                    && equals(this.scaleFactor, that.scaleFactor)
                    && equals(this.falseEasting, that.falseEasting)
                    && equals(this.falseNorthing, that.falseNorthing);
        }
        return false;
    }

    /**
     * Returns {@code true} if the two specified value are equals. Two {@link Double#NaN NaN} values are considered
     * equals.
     */
    static boolean equals(final double value1, final double value2) {
        return Utilities.equals(value1, value2);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                           FORMULAS FROM SNYDER                           ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /** Iteratively solve equation (7-9) from Snyder. */
    final double cphi2(final double ts) throws ProjectionException {
        final double eccnth = 0.5 * excentricity;
        double phi = (PI / 2) - 2.0 * atan(ts);
        for (int i = 0; i < MAXIMUM_ITERATIONS; i++) {
            final double con = excentricity * sin(phi);
            final double dphi = (PI / 2) - 2.0 * atan(ts * pow((1 - con) / (1 + con), eccnth)) - phi;
            phi += dphi;
            if (abs(dphi) <= ITERATION_TOLERANCE) {
                return phi;
            }
        }
        throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
    }

    /**
     * Computes function <code>f(s,c,e²) = c/sqrt(1 - s²&times;e²)</code> needed for the true scale latitude (Snyder
     * 14-15), where <var>s</var> and <var>c</var> are the sine and cosine of the true scale latitude, and <var>e²</var>
     * is the {@linkplain #excentricitySquared eccentricity squared}.
     */
    final double msfn(final double s, final double c) {
        return c / sqrt(1.0 - (s * s) * excentricitySquared);
    }

    /** Computes function (15-9) and (9-13) from Snyder. Equivalent to negative of function (7-7). */
    final double tsfn(final double phi, double sinphi) {
        sinphi *= excentricity;
        /*
         * NOTE: change sign to get the equivalent of Snyder (7-7).
         */
        return tan(0.5 * (PI / 2 - phi)) / pow((1 - sinphi) / (1 + sinphi), 0.5 * excentricity);
    }

    /**
     * Calculates the meridian distance. This is the distance along the central meridian from the equator to
     * {@code phi}. Accurate to < 1e-5 meters when used in conjuction with typical major axis values.
     *
     * @param phi latitude to calculate meridian distance for.
     * @param sphi sin(phi).
     * @param cphi cos(phi).
     * @return meridian distance for the given latitude.
     */
    protected final double mlfn(final double phi, double sphi, double cphi) {
        cphi *= sphi;
        sphi *= sphi;
        return en0 * phi - cphi * (en1 + sphi * (en2 + sphi * (en3 + sphi * en4)));
    }

    /**
     * Calculates the latitude ({@code phi}) from a meridian distance. Determines phi to TOL (1e-11) radians, about 1e-6
     * seconds.
     *
     * @param arg meridian distance to calulate latitude for.
     * @return the latitude of the meridian distance.
     * @throws ProjectionException if the itteration does not converge.
     */
    protected final double inv_mlfn(double arg) throws ProjectionException {
        double s, t, k = 1.0 / (1.0 - excentricitySquared);
        int i;
        double phi = arg;
        for (i = MAXIMUM_ITERATIONS; true; ) { // rarely goes over 5 iterations
            if (--i < 0) {
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            }
            s = Math.sin(phi);
            t = 1.0 - excentricitySquared * s * s;
            t = (mlfn(phi, s, Math.cos(phi)) - arg) * (t * Math.sqrt(t)) * k;
            phi -= t;
            if (Math.abs(t) < MLFN_TOL) {
                return phi;
            }
        }
    }

    /** Tolerant asin that will just return the limits of its output range if the input is out of range */
    double aasin(double v) {
        double av = abs(v);
        if (av >= 1.) {
            return (v < 0. ? -PI / 2 : PI / 2);
        }
        return asin(v);
    }

    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDER                                 ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The base provider for {@link MapProjection}s.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     */
    public abstract static class AbstractProvider extends MathTransformProvider {
        /** Serial number for interoperability with different versions. */
        private static final long serialVersionUID = 6280666068007678702L;

        /**
         * The operation parameter descriptor for the {@linkplain #semiMajor semi major} parameter value. Valid values
         * range is from 0 to infinity. This parameter is mandatory.
         *
         * @todo Would like to start range from 0 <u>exclusive</u>.
         */
        public static final ParameterDescriptor<Double> SEMI_MAJOR = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "semi_major"),
                    new NamedIdentifier(Citations.EPSG, "semi-major axis"),
                    new NamedIdentifier(Citations.PROJ, "a")
                    // EPSG does not specifically define the above parameter
                },
                Double.NaN,
                0,
                Double.POSITIVE_INFINITY,
                SI.METRE);

        /**
         * The operation parameter descriptor for the {@linkplain #semiMinor semi minor} parameter value. Valid values
         * range is from 0 to infinity. This parameter is mandatory.
         *
         * @todo Would like to start range from 0 <u>exclusive</u>.
         */
        public static final ParameterDescriptor<Double> SEMI_MINOR = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "semi_minor"),
                    new NamedIdentifier(Citations.EPSG, "semi-minor axis"),
                    new NamedIdentifier(Citations.PROJ, "b")
                    // EPSG does not specifically define the above parameter
                },
                Double.NaN,
                0,
                Double.POSITIVE_INFINITY,
                SI.METRE);

        /**
         * The operation parameter descriptor for the {@linkplain #centralMeridian central meridian} parameter value.
         * Valid values range is from -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> CENTRAL_MERIDIAN = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "central_meridian"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of false origin"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of origin"),
                    new NamedIdentifier(Citations.ESRI, "Longitude_Of_Center"),
                    new NamedIdentifier(Citations.ESRI, "longitude_of_center"),
                    new NamedIdentifier(Citations.ESRI, "Longitude_Of_Origin"),
                    new NamedIdentifier(Citations.ESRI, "longitude_of_origin"),
                    new NamedIdentifier(Citations.GEOTIFF, "NatOriginLong"),
                    new NamedIdentifier(Citations.PROJ, "lon_0")
                    // ESRI uses "Longitude_Of_Origin" in orthographic (not to
                    // be confused with "Longitude_Of_Center" in oblique mercator).
                },
                0,
                -180,
                180,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@linkplain #latitudeOfOrigin latitude of origin} parameter value.
         * Valid values range is from -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> LATITUDE_OF_ORIGIN = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "latitude_of_origin"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of false origin"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of natural origin"),
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_Origin"),
                    new NamedIdentifier(Citations.ESRI, "latitude_of_origin"),
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_Center"),
                    new NamedIdentifier(Citations.ESRI, "latitude_of_center"),
                    new NamedIdentifier(Citations.GEOTIFF, "NatOriginLat"),
                    new NamedIdentifier(Citations.PROJ, "lat_0")
                    // ESRI uses "Latitude_Of_Center" in orthographic.
                },
                0,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the longitude of center parameter value. Valid values range is from
         * -180 to 180°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> LONGITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "longitude_of_center"),
                    new NamedIdentifier(Citations.OGC, "longitude_of_origin"),
                    new NamedIdentifier(Citations.EPSG, "Longitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Spherical longitude of origin"),
                    new NamedIdentifier(Citations.ESRI, "Central_Meridian"),
                    new NamedIdentifier(Citations.GEOTIFF, "ProjCenterLong"),
                    new NamedIdentifier(Citations.PROJ, "lon_0")
                },
                0,
                -180,
                180,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the latitude of center parameter value. Valid values range is from -90
         * to 90°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> LATITUDE_OF_CENTRE = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "latitude_of_center"),
                    new NamedIdentifier(Citations.OGC, "latitude_of_origin"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Spherical latitude of origin"),
                    new NamedIdentifier(Citations.ESRI, "Latitude_Of_Origin"),
                    new NamedIdentifier(Citations.GEOTIFF, "ProjCenterLat"),
                    new NamedIdentifier(Citations.PROJ, "lat_0")
                },
                0,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the standard parallel 1 parameter value. Valid values range is from
         * -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> STANDARD_PARALLEL_1 = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "standard_parallel_1"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of 1st standard parallel"),
                    new NamedIdentifier(Citations.ESRI, "Standard_Parallel_1"),
                    new NamedIdentifier(Citations.ESRI, "standard_parallel_1"),
                    new NamedIdentifier(Citations.GEOTIFF, "StdParallel1"),
                    new NamedIdentifier(Citations.PROJ, "lat_1")
                },
                0,
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the standard parallel 2 parameter value. Valid values range is from
         * -90 to 90°. Default value is 0.
         */
        public static final ParameterDescriptor<Double> STANDARD_PARALLEL_2 = createOptionalDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "standard_parallel_2"),
                    new NamedIdentifier(Citations.EPSG, "Latitude of 2nd standard parallel"),
                    new NamedIdentifier(Citations.ESRI, "Standard_Parallel_2"),
                    new NamedIdentifier(Citations.ESRI, "standard_parallel_2"),
                    new NamedIdentifier(Citations.GEOTIFF, "StdParallel2"),
                    new NamedIdentifier(Citations.PROJ, "lat_2")
                },
                -90,
                90,
                NonSI.DEGREE_ANGLE);

        /**
         * The operation parameter descriptor for the {@link #scaleFactor scaleFactor} parameter value. Valid values
         * range is from 0 to infinity. Default value is 1.
         *
         * @todo Would like to start range from 0 <u>exclusive</u>.
         */
        public static final ParameterDescriptor<Double> SCALE_FACTOR = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "scale_factor"),
                    new NamedIdentifier(Citations.EPSG, "Scale factor at natural origin"),
                    new NamedIdentifier(Citations.EPSG, "Scale factor on initial line"),
                    new NamedIdentifier(Citations.EPSG, "Scale factor at projection centre"),
                    new NamedIdentifier(Citations.GEOTIFF, "ScaleAtNatOrigin"),
                    new NamedIdentifier(Citations.GEOTIFF, "ScaleAtCenter"),
                    new NamedIdentifier(Citations.ESRI, "Scale_Factor"),
                    new NamedIdentifier(Citations.ESRI, "scale_factor"),
                    new NamedIdentifier(Citations.PROJ, "k"),
                    new NamedIdentifier(Citations.PROJ, "k_0")
                },
                1,
                0,
                Double.POSITIVE_INFINITY,
                AbstractUnit.ONE);

        /**
         * The operation parameter descriptor for the {@link #falseEasting falseEasting} parameter value. Valid values
         * range is unrestricted. Default value is 0.
         */
        public static final ParameterDescriptor<Double> FALSE_EASTING = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "false_easting"),
                    new NamedIdentifier(Citations.EPSG, "False easting"),
                    new NamedIdentifier(Citations.EPSG, "Easting at false origin"),
                    new NamedIdentifier(Citations.EPSG, "Easting at projection centre"),
                    new NamedIdentifier(Citations.GEOTIFF, "FalseEasting"),
                    new NamedIdentifier(Citations.ESRI, "False_Easting"),
                    new NamedIdentifier(Citations.PROJ, "x_0")
                },
                0,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                SI.METRE);

        /**
         * The operation parameter descriptor for the {@link #falseNorthing falseNorthing} parameter value. Valid values
         * range is unrestricted. Default value is 0.
         */
        public static final ParameterDescriptor<Double> FALSE_NORTHING = createDescriptor(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.OGC, "false_northing"),
                    new NamedIdentifier(Citations.EPSG, "False northing"),
                    new NamedIdentifier(Citations.EPSG, "Northing at false origin"),
                    new NamedIdentifier(Citations.EPSG, "Northing at projection centre"),
                    new NamedIdentifier(Citations.GEOTIFF, "FalseNorthing"),
                    new NamedIdentifier(Citations.ESRI, "False_Northing"),
                    new NamedIdentifier(Citations.ESRI, "false_northing"),
                    new NamedIdentifier(Citations.PROJ, "y_0")
                },
                0,
                Double.NEGATIVE_INFINITY,
                Double.POSITIVE_INFINITY,
                SI.METRE);

        /**
         * Constructs a math transform provider from a set of parameters. The provider {@linkplain #getIdentifiers
         * identifiers} will be the same than the parameter ones.
         *
         * @param parameters The set of parameters (never {@code null}).
         */
        public AbstractProvider(final ParameterDescriptorGroup parameters) {
            super(2, 2, parameters);
        }

        /** Returns the operation type for this map projection. */
        @Override
        public Class<? extends Projection> getOperationType() {
            return Projection.class;
        }

        /** Returns {@code true} is the parameters use a spherical datum. */
        static boolean isSpherical(final ParameterValueGroup values) {
            try {
                return doubleValue(SEMI_MAJOR, values) == doubleValue(SEMI_MINOR, values);
            } catch (IllegalStateException exception) {
                // Probably could not find the requested values -- gobble error and be forgiving.
                // The error will probably be thrown at MapProjection construction time, which is
                // less surprising to some users.
                return false;
            }
        }

        /**
         * Returns the parameter value for the specified operation parameter in standard units. Values are automatically
         * converted into the standard units specified by the supplied {@code param} argument, except
         * {@link NonSI#DEGREE_ANGLE degrees} which are converted to {@link SI#RADIAN radians}. This conversion is
         * performed because the radians units are standard for all internal computations in the map projection package.
         * For example they are the standard units for {@link MapProjection#latitudeOfOrigin latitudeOfOrigin} and
         * {@link MapProjection#centralMeridian centralMeridian} fields in the {@link MapProjection} class.
         *
         * @param param The parameter to look for.
         * @param group The parameter value group to search into.
         * @return The requested parameter value.
         * @throws ParameterNotFoundException if the parameter is not found.
         */
        protected static double doubleValue(final ParameterDescriptor param, final ParameterValueGroup group)
                throws ParameterNotFoundException {
            double v = MathTransformProvider.doubleValue(param, group);
            if (NonSI.DEGREE_ANGLE.equals(param.getUnit())) {
                v = toRadians(v);
            }
            return v;
        }
    }
}
