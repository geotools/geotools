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

import static java.lang.Math.*;

import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.referencing.operation.transform.ConcatenatedTransform;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.ReferenceIdentifier;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


/**
 * Transverse Mercator Projection (EPSG code 9807). This
 * is a cylindrical projection, in which the cylinder has been rotated 90°.
 * Instead of being tangent to the equator (or to an other standard latitude),
 * it is tangent to a central meridian. Deformation are more important as we
 * are going futher from the central meridian. The Transverse Mercator
 * projection is appropriate for region wich have a greater extent north-south
 * than east-west.
 * <p>
 *
 * The elliptical equations used here are series approximations, and their accuracy
 * decreases as points move farther from the central meridian of the projection.
 * The forward equations here are accurate to a less than a mm &plusmn;10 degrees from
 * the central meridian, a few mm &plusmn;15 degrees from the
 * central meridian and a few cm &plusmn;20 degrees from the central meridian.
 * The spherical equations are not approximations and should always give the
 * correct values.
 * <p>
 *
 * There are a number of versions of the transverse mercator projection
 * including the Universal (UTM) and Modified (MTM) Transverses Mercator
 * projections. In these cases the earth is divided into zones. For the UTM
 * the zones are 6 degrees wide, numbered from 1 to 60 proceeding east from
 * 180 degrees longitude, and between lats 84 degrees North and 80
 * degrees South. The central meridian is taken as the center of the zone
 * and the latitude of origin is the equator. A scale factor of 0.9996 and
 * false easting of 500000m is used for all zones and a false northing of 10000000m
 * is used for zones in the southern hemisphere.
 * <p>
 *
 * NOTE: formulas used below are not those of Snyder, but rather those
 *       from the {@code proj4} package of the USGS survey, which
 *       have been reproduced verbatim. USGS work is acknowledged here.
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li> Proj-4.4.6 available at <A HREF="http://www.remotesensing.org/proj">www.remotesensing.org/proj</A><br>
 *        Relevent files are: {@code PJ_tmerc.c}, {@code pj_mlfn.c}, {@code pj_fwd.c} and {@code pj_inv.c}.</li>
 *   <li> John P. Snyder (Map Projections - A Working Manual,
 *        U.S. Geological Survey Professional Paper 1395, 1987).</li>
 *   <li> "Coordinate Conversions and Transformations including Formulas",
 *        EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Transverse Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/transverse_mercator.html">"Transverse_Mercator" on RemoteSensing.org</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 */
public class TransverseMercator extends MapProjection {
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;

    /**
     * Relative iteration precision used in the {@code mlfn} method.
     * This overrides the value in the {@link MapProjection} class.
     */
    private static final double ITERATION_TOLERANCE = 1E-11;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Maximum difference allowed when comparing latitudes.
     */
    private static final double EPSILON_LATITUDE = 1E-10;

    /**
     * A derived quantity of excentricity, computed by <code>e'² = (a²-b²)/b² = es/(1-es)</code>
     * where <var>a</var> is the semi-major axis length and <var>b</bar> is the semi-minor axis
     * length.
     */
    private final double esp;

    /**
     * Meridian distance at the {@code latitudeOfOrigin}.
     * Used for calculations for the ellipsoid.
     */
    private final double ml0;

    /**
     * Contants used for the forward and inverse transform for the eliptical
     * case of the Transverse Mercator.
     */
    private static final double FC1= 1.00000000000000000000000,  // 1/1
                                FC2= 0.50000000000000000000000,  // 1/2
                                FC3= 0.16666666666666666666666,  // 1/6
                                FC4= 0.08333333333333333333333,  // 1/12
                                FC5= 0.05000000000000000000000,  // 1/20
                                FC6= 0.03333333333333333333333,  // 1/30
                                FC7= 0.02380952380952380952380,  // 1/42
                                FC8= 0.01785714285714285714285;  // 1/56

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected TransverseMercator(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters);

        //  Compute constants
        esp = excentricitySquared / (1.0 - excentricitySquared);
        ml0 = mlfn(latitudeOfOrigin, sin(latitudeOfOrigin), cos(latitudeOfOrigin));
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        double sinphi = sin(y);
        double cosphi = cos(y);

        double t = (abs(cosphi) > EPSILON) ? sinphi/cosphi : 0;
        t *= t;
        double al = cosphi*x;
        double als = al*al;
        al /= sqrt(1.0 - excentricitySquared * sinphi*sinphi);
        double n = esp * cosphi*cosphi;

        /* NOTE: meridinal distance at latitudeOfOrigin is always 0 */
        y = (mlfn(y, sinphi, cosphi) - ml0 +
            sinphi * al * x *
            FC2 * ( 1.0 +
            FC4 * als * (5.0 - t + n*(9.0 + 4.0*n) +
            FC6 * als * (61.0 + t * (t - 58.0) + n*(270.0 - 330.0*t) +
            FC8 * als * (1385.0 + t * ( t*(543.0 - t) - 3111.0))))));

        x = al*(FC1 + FC3 * als*(1.0 - t + n +
            FC5 * als * (5.0 + t*(t - 18.0) + n*(14.0 - 58.0*t) +
            FC7 * als * (61.0+ t*(t*(179.0 - t) - 479.0 )))));

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        double phi = inv_mlfn(ml0 + y);

        if (abs(phi) >= PI/2) {
            y = y<0.0 ? -(PI/2) : (PI/2);
            x = 0.0;
        } else {
            double sinphi = sin(phi);
            double cosphi = cos(phi);
            double t = (abs(cosphi) > EPSILON) ? sinphi/cosphi : 0.0;
            double n = esp * cosphi*cosphi;
            double con = 1.0 - excentricitySquared * sinphi*sinphi;
            double d = x * sqrt(con);
            con *= t;
            t *= t;
            double ds = d*d;

            y = phi - (con*ds / (1.0 - excentricitySquared)) *
                FC2 * (1.0 - ds *
                FC4 * (5.0 + t*(3.0 - 9.0*n) + n*(1.0 - 4*n) - ds *
                FC6 * (61.0 + t*(90.0 - 252.0*n + 45.0*t) + 46.0*n - ds *
                FC8 * (1385.0 + t*(3633.0 + t*(4095.0 + 1574.0*t))))));

            x = d*(FC1 - ds * FC3 * (1.0 + 2.0*t + n -
                ds*FC5*(5.0 + t*(28.0 + 24* t + 8.0*n) + 6.0*n -
                ds*FC7*(61.0 + t*(662.0 + t*(1320.0 + 720.0*t))))))/cosphi;
        }

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getToleranceForAssertions(final double longitude, final double latitude) {
        if (abs(longitude - centralMeridian) > 0.26) {   // 15 degrees
            // When far from the valid area, use a larger tolerance.
            return 2.5;
        } else if (abs(longitude - centralMeridian) > 0.22) {  // 12.5 degrees
            return 1.0;
        } else if (abs(longitude - centralMeridian) > 0.17) {  // 10 degrees
            return 0.5;
        }
        // a normal tolerance
        return 1E-6;
    }


    /**
     * Provides the transform equations for the spherical case of the
     * TransverseMercator projection.
     *
     * @version $Id$
     * @author André Gosselin
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     */
    private static final class Spherical extends TransverseMercator {
        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param  parameters The parameter values in standard units.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        protected Spherical(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            super(parameters);
            ensureSpherical();
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            final double normalizedLongitude = x;
            assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;

            double cosphi = cos(y);
            double b = cosphi * sin(x);
            if (abs(abs(b) - 1.0) <= EPSILON) {
                throw new ProjectionException(ErrorKeys.VALUE_TEND_TOWARD_INFINITY);
            }

            //Using Snyder's equation for calculating y, instead of the one used in Proj4
            //poential problems when y and x = 90 degrees, but behaves ok in tests
            y = atan2(tan(y), cos(x)) - latitudeOfOrigin;   /* Snyder 8-3 */
            x = 0.5 * log((1.0+b) / (1.0-b));               /* Snyder 8-1 */

            assert checkTransform(x, y, ptDst, getToleranceForSphereAssertions(normalizedLongitude));
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * {@inheritDoc}
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            double sinhX = sinh(x);
            double cosD = cos(latitudeOfOrigin + y);
            double phi = asin(sqrt((1.0 - cosD*cosD) / (1.0 + sinhX*sinhX)));
            // correct for the fact that we made everything positive using sqrt(x*x)
            y = ((y + latitudeOfOrigin)<0.0) ? -phi : phi;
            x = (abs(sinhX) <= EPSILON  &&  abs(cosD) <= EPSILON) ? 0.0 : atan2(sinhX,cosD);

            assert checkInverseTransform(x, y, ptDst, getToleranceForSphereAssertions(x));
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Maximal error tolerated for assertions in the spherical case. When assertions
         * are enabled, every projection using spherical formulas is followed by a projection
         * using the ellipsical formulas, and the results are compared. If a distance greater
         * than the tolerance level is found, then an {@link AssertionError} will be thrown.
         * Subclasses may override this method if they need to relax the tolerance level.
         * <p>
         * The default implementation allows a larger tolerance when comparing spherical to
         * elliptical calculations when we are far from the central meridian (elliptical
         * calculations are not valid here).
         *
         * @param  longitude The longitude standardized (in radians) with
         *         {@linkplain #centralMeridian} already removed from it.
         * @return The tolerance level for assertions, in meters.
         */
        protected double getToleranceForSphereAssertions(final double longitude) {
            if (abs(abs(longitude)- PI/2) < EPSILON_LATITUDE) {  // 90 degrees
                // elliptical equations are at their worst here
                return 1E+18;
            }
            if (abs(longitude) > 0.26) {   // 15 degrees
                // When far from the valid area, use a very larger tolerance.
                return 1E+6;
            }
            // a normal tolerance
            return 1E-6;
        }
    }

     /**
     * Convenience method computing the zone code from the central meridian.
     * Information about zones convention must be specified in argument. Two
     * widely set of arguments are of Universal Transverse Mercator (UTM) and
     * Modified Transverse Mercator (MTM) projections:<br>
     * <p>
     *
     * UTM projection (zones numbered from 1 to 60):<br>
     * <p>
     *        {@code getZone(-177, 6);}<br>
     * <p>
     * MTM projection (zones numbered from 1 to 120):<br>
     * <p>
     *        {@code getZone(-52.5, -3);}<br>
     *
     * @param  centralLongitudeZone1 Longitude in the middle of zone 1, in decimal degrees
     *         relative to Greenwich. Positive longitudes are toward east, and negative
     *         longitudes toward west.
     * @param  zoneWidth Number of degrees of longitudes in one zone. A positive value
     *         means that zones are numbered from west to east (i.e. in the direction of
     *         positive longitudes). A negative value means that zones are numbered from
     *         east to west.
     * @return The zone number. First zone is numbered 1.
     */
    private int getZone(final double centralLongitudeZone1, final double zoneWidth) {
        final double zoneCount = abs(360 / zoneWidth);
        double t;
        t  = centralLongitudeZone1 - 0.5*zoneWidth; // Longitude at the beginning of the first zone.
        t  = toDegrees(centralMeridian) - t;        // Degrees of longitude between the central longitude and longitude 1.
        t  = floor(t/zoneWidth + EPSILON);          // Number of zones between the central longitude and longitude 1.
        t -= zoneCount*floor(t/zoneCount);          // If negative, bring back to the interval 0 to (zoneCount-1).
        return ((int) t)+1;
    }

    /**
     * Convenience method returning the meridian in the middle of current zone. This meridian is
     * typically the central meridian. This method may be invoked to make sure that the central
     * meridian is correctly set.
     *
     * @param  centralLongitudeZone1 Longitude in the middle of zone 1, in decimal degrees
     *         relative to Greenwich. Positive longitudes are toward east, and negative
     *         longitudes toward west.
     * @param  zoneWidth Number of degrees of longitudes in one zone. A positive value
     *         means that zones are numbered from west to east (i.e. in the direction of
     *         positive longitudes). A negative value means that zones are numbered from
     *         east to west.
     * @return The central meridian.
     */
    private double getCentralMedirian(final double centralLongitudeZone1, final double zoneWidth) {
        double t;
        t  = centralLongitudeZone1 + (getZone(centralLongitudeZone1, zoneWidth)-1)*zoneWidth;
        t -= 360 * floor((t+180) / 360); // Bring back into [-180..+180] range.
        return t;
    }

    /**
     * Convenience method computing the zone code from the central meridian.
     *
     * @return The zone number, using the scalefactor and false easting
     *         to decide if this is a UTM or MTM case. Returns 0 if the
     *         case of the projection cannot be determined.
     */
    public int getZone() {
        // UTM
        if (scaleFactor == 0.9996 && falseEasting == 500000.0) {
            return getZone(-177, 6);
        }
        // MTM
        if (scaleFactor == 0.9999 && falseEasting == 304800.0){
            return getZone(-52.5, -3);
        }
        // unknown
        throw new IllegalStateException(Errors.format(ErrorKeys.UNKNOW_PROJECTION_TYPE));
    }

    /**
     * Convenience method returning the meridian in the middle of current zone. This meridian is
     * typically the central meridian. This method may be invoked to make sure that the central
     * meridian is correctly set.
     *
     * @return The central meridian, using the scalefactor and false easting
     *         to decide if this is a UTM or MTM case. Returns {@link Double#NaN}
     *         if the case of the projection cannot be determined.
     */
    public double getCentralMeridian() {
        // UTM
        if (scaleFactor == 0.9996 && falseEasting == 500000.0) {
            return getCentralMedirian(-177, 6);
        }
        // MTM
        if (scaleFactor == 0.9999 && falseEasting == 304800.0){
            return getCentralMedirian(-52.5, -3);
        }
        // unknown
        throw new IllegalStateException(Errors.format(ErrorKeys.UNKNOW_PROJECTION_TYPE));
    }

    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(ml0);
        return ((int)code ^ (int)(code >>> 32)) + 37*super.hashCode();
    }

    /**
     * Compares the specified object with this map projection for equality.
     */
    @Override
    public boolean equals(final Object object) {
        if (object == this) {
            // Slight optimization
            return true;
        }
        // Relevant parameters are already compared in MapProjection
        return super.equals(object);
    }




    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////
    ////////                                                                          ////////
    ////////                                 PROVIDERS                                ////////
    ////////                                                                          ////////
    //////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a {@linkplain TransverseMercator Transverse Mercator} projection (EPSG code
     * 9807).
     *
     * @since 2.1
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * Returns a descriptor group for the specified parameters.
         */
        static ParameterDescriptorGroup createDescriptorGroup(final ReferenceIdentifier[] identifiers) {
            return createDescriptorGroup(identifiers, new ParameterDescriptor[] {
                SEMI_MAJOR,       SEMI_MINOR,
                CENTRAL_MERIDIAN, LATITUDE_OF_ORIGIN,
                SCALE_FACTOR,     FALSE_EASTING,
                FALSE_NORTHING
            });
        }

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Transverse_Mercator"),
                new NamedIdentifier(Citations.EPSG,     "Transverse Mercator"),
                new NamedIdentifier(Citations.EPSG,     "Gauss-Kruger"),
                new NamedIdentifier(Citations.EPSG,     "9807"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_TransverseMercator"),
                new NamedIdentifier(Citations.ESRI,     "Transverse_Mercator"),
                new NamedIdentifier(Citations.ESRI,     "Gauss_Kruger"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.TRANSVERSE_MERCATOR_PROJECTION))
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Constructs a new provider with the specified parameters.
         */
        Provider(final ParameterDescriptorGroup descriptor) {
            super(descriptor);
        }

        /**
         * Returns the operation type for this map projection.
         */
        @Override
        public Class<CylindricalProjection> getOperationType() {
            return CylindricalProjection.class;
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            if (isSpherical(parameters)) {
                return new Spherical(parameters);
            } else {
                return new TransverseMercator(parameters);
            }
        }
    }

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform
     * provider} for a South Orientated {@linkplain TransverseMercator Transverse Mercator}
     * projection (EPSG code 9808). Note that at the contrary of what this class name suggest,
     * the projected coordinates are still increasing toward North. This is because all
     * {@linkplain MapProjection map projections} in Geotools must complies with standard axis
     * orientations. The real axis flip is performed by the CRS framework outside this package.
     * See "<cite>Axis units and orientation</cite>" in
     * {@linkplain org.geotools.referencing.operation.projection package description} for details.
     * <p>
     * The usual Transverse Mercator formulas are:
     * <p>
     * <ul>
     *   <li><var>easting</var> = <var>{@linkplain #falseEasting false easting}</var> + <var>px</var></li>
     *   <li><var>northing</var> = <var>{@linkplain #falseNorthing false northing}</var> + <var>py</var></li>
     * </ul>
     * <p>
     * The Transverse Mercator South Orientated Projection formulas are:
     * <p>
     * <ul>
     *   <li><var>westing</var> = <var>{@linkplain #falseEasting false easting}</var> - <var>px</var></li>
     *   <li><var>southing</var> = <var>{@linkplain #falseNorthing false northing}</var> - <var>py</var></li>
     * </ul>
     * <p>
     * Where the <var>px</var> and <var>py</var> terms are the same in both cases.
     * Transforms created by this provider actually computes
     * (<var>easting</var>,<var>northing</var>) = (-<var>westing</var>,-<var>southing</var>).
     * This is equivalent to a {@link TransverseMercator} projection with
     * {@link #falseEasting falseEasting} and {@link #falseNorthing falseNorthing} sign reverted.
     * This operation is implemented as a concatenation of a North-orientated transverse mercator
     * projection with an affine transform for (<var>false easting</var>,<var>false northing</var>)
     * correction.
     *
     * @since 2.2
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider_SouthOrientated extends Provider {
        /**
         * Constructs a new provider.
         */
        public Provider_SouthOrientated() {
            super(createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.EPSG, "Transverse Mercator (South Orientated)"),
                new NamedIdentifier(Citations.EPSG, "9808")
            }));
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param  parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        public MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException
        {
            final MapProjection projection = (MapProjection) super.createMathTransform(parameters);
            if (projection.falseEasting==0 && projection.falseNorthing==0) {
                return projection;
            }
            final AffineTransform step = AffineTransform.getTranslateInstance(
                    -2*projection.falseEasting, -2*projection.falseNorthing);
            return ConcatenatedTransform.create(ProjectiveTransform.create(step), projection);
        }
    }
}
