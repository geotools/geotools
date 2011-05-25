/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.geom.Point2D;
import java.util.Collection;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.ConicProjection;
import org.opengis.referencing.operation.MathTransform;
import org.geotools.measure.Latitude;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.VocabularyKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

import static java.lang.Math.*;


/**
 * Albers Equal Area Projection (EPSG code 9822). This is a conic projection with parallels being
 * unequally spaced arcs of concentric circles, more closely spaced at north and south edges of the
 * map. Merideans are equally spaced radii of the same circles and intersect parallels at right
 * angles. As the name implies, this projection minimizes distortion in areas.
 * <p>
 * The {@code "standard_parallel_2"} parameter is optional and will be given the same value as
 * {@code "standard_parallel_1"} if not set (creating a 1 standard parallel projection).
 * <p>
 * <b>NOTE:</b>
 * formulae used below are from a port, to Java, of the {@code proj4}
 * package of the USGS survey. USGS work is acknowledged here.
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li> Proj-4.4.7 available at <A HREF="http://www.remotesensing.org/proj">www.remotesensing.org/proj</A><br>
 *        Relevent files are: PJ_aea.c, pj_fwd.c and pj_inv.c </li>
 *   <li> John P. Snyder (Map Projections - A Working Manual,
 *        U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li> "Coordinate Conversions and Transformations including Formulas",
 *        EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/AlbersEqual-AreaConicProjection.html">Albers Equal-Area Conic Projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/albers_equal_area_conic.html">"Albers_Conic_Equal_Area" on RemoteSensing.org</A>
 * @see <A HREF="http://srmwww.gov.bc.ca/gis/bceprojection.html">British Columbia Albers Standard Projection</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author Gerald I. Evenden (for original code in Proj4)
 * @author Rueben Schulz
 */
public class AlbersEqualArea extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = -3024658742514888646L;

    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 15;

    /**
     * Difference allowed in iterative computations.
     */
    private static final double ITERATION_TOLERANCE = 1E-10;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Constants used by the spherical and elliptical Albers projection.
     */
    private final double n, c, rho0;

    /**
     * An error condition indicating iteration will not converge for the
     * inverse ellipse. See Snyder (14-20)
     */
    private final double ec;

    /**
     * Standards parallel 1 in radians, for {@link #getParameterValues} implementation.
     */
    private final double phi1;

    /**
     * Standards parallel 2 in radians, for {@link #getParameterValues} implementation.
     */
    private double phi2;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected AlbersEqualArea(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        // Fetch parameters
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        phi1 = doubleValue(expected, Provider.STANDARD_PARALLEL_1, parameters);
        ensureLatitudeInRange(       Provider.STANDARD_PARALLEL_1, phi1, true);
        phi2 = doubleValue(expected, Provider.STANDARD_PARALLEL_2, parameters);
        if (Double.isNaN(phi2)) {
            phi2 = phi1;
        }
        ensureLatitudeInRange(Provider.STANDARD_PARALLEL_2, phi2, true);

        // Compute Constants
        if (abs(phi1 + phi2) < EPSILON) {
            throw new IllegalArgumentException(Errors.format(ErrorKeys.ANTIPODE_LATITUDES_$2,
                    new Latitude(toDegrees(phi1)), new Latitude(toDegrees(phi2))));
        }
        double  sinphi = sin(phi1);
        double  cosphi = cos(phi1);
        double  n      = sinphi;
        boolean secant = (abs(phi1 - phi2) >= EPSILON);
        if (isSpherical) {
            if (secant) {
                n = 0.5 * (n + sin(phi2));
            }
            c    = cosphi * cosphi + n*2 * sinphi;
            rho0 = sqrt(c - n*2 * sin(latitudeOfOrigin)) /n;
            ec   = Double.NaN;
        } else {
            double m1 = msfn(sinphi, cosphi);
            double q1 = qsfn(sinphi);
            if (secant) { // secant cone
                sinphi    = sin(phi2);
                cosphi    = cos(phi2);
                double m2 = msfn(sinphi, cosphi);
                double q2 = qsfn(sinphi);
                n = (m1 * m1 - m2 * m2) / (q2 - q1);
            }
            c = m1 * m1 + n * q1;
            rho0 = sqrt(c - n * qsfn(sin(latitudeOfOrigin))) /n;
            ec = 1.0 - .5 * (1.0-excentricitySquared) *
                 log((1.0 - excentricity) / (1.0 + excentricity)) / excentricity;
        }
        this.n = n;
    }

    /**
     * {@inheritDoc}
     */
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        set(expected, Provider.STANDARD_PARALLEL_1, values, phi1);
        set(expected, Provider.STANDARD_PARALLEL_2, values, phi2);
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, Point2D ptDst)
            throws ProjectionException
    {
        x *= n;
        double rho;
        if (isSpherical) {
            rho = c - n*2 * sin(y);
        } else {
            rho = c - n * qsfn(sin(y));
        }
        if (rho < 0.0) {
            if (rho > -EPSILON) {
                rho = 0.0;
            } else {
                throw new ProjectionException(ErrorKeys.TOLERANCE_ERROR);
            }
        }
        rho = sqrt(rho) / n;
        y   = rho0 - rho * cos(x);
        x   =        rho * sin(x);

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
        y = rho0 - y;
        double rho = hypot(x, y);
        if (rho > EPSILON) {
            if (n < 0.0) {
                rho = -rho;
                x   = -x;
                y   = -y;
            }
            x = atan2(x, y) / n;
            y = rho * n;
            if (isSpherical) {
                y = (c - y * y) / (n*2);
                if (abs(y) <= 1.0){
                    y = asin(y);
                }
                else {
                    y = (y < 0.0) ? -PI/2.0 : PI/2.0;
                }
            } else {
                y = (c - y*y) / n;
                if (abs(ec - abs(y)) > EPSILON) {
                    y = phi1(y);
                } else {
                    y = (y < 0.0) ? -PI/2.0 : PI/2.0;
                }
            }
        } else {
            x = 0.0;
            y = n > 0.0 ? PI/2.0 : - PI/2.0;
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }

    /**
     * Iteratively solves equation (3-16) from Snyder.
     *
     * @param qs arcsin(q/2), used in the first step of iteration
     * @return the latitude
     */
    private double phi1(final double qs) throws ProjectionException {
        final double tone_es = 1 - excentricitySquared;
        double phi = asin(0.5 * qs);
        if (excentricity < EPSILON) {
            return phi;
        }
        for (int i=0; i<MAXIMUM_ITERATIONS; i++) {
            final double sinpi = sin(phi);
            final double cospi = cos(phi);
            final double con   = excentricity * sinpi;
            final double com   = 1.0 - con*con;
            final double dphi  = 0.5 * com*com / cospi *
                    (qs/tone_es - sinpi / com + 0.5/excentricity * log((1. - con) / (1. + con)));
            phi += dphi;
            if (abs(dphi) <= ITERATION_TOLERANCE) {
                return phi;
            }
        }
        throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
    }

    /**
     * Calculates q, Snyder equation (3-12)
     *
     * @param sinphi sin of the latitude q is calculated for
     * @return q from Snyder equation (3-12)
     */
    private double qsfn(final double sinphi) {
        final double one_es = 1 - excentricitySquared;
        if (excentricity >= EPSILON) {
            final double con = excentricity * sinphi;
            return (one_es * (sinphi / (1. - con*con) -
                    (0.5/excentricity) * log((1.-con) / (1.+con))));
        } else {
            return sinphi + sinphi;
        }
    }

    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(c);
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
        if (super.equals(object)) {
            final AlbersEqualArea that = (AlbersEqualArea) object;
            return equals(this.n    , that.n   ) &&
                   equals(this.c    , that.c   ) &&
                   equals(this.rho0 , that.rho0) &&
                   equals(this.phi1 , that.phi1) &&
                   equals(this.phi2 , that.phi2);
        }
        return false;
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
     * provider} for an {@linkplain AlbersEqualArea Albers Equal Area} projection (EPSG code 9822).
     *
     * @version $Id$
     * @author Rueben Schulz
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = -7489679528438418778L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Albers_Conic_Equal_Area"),
                new NamedIdentifier(Citations.EPSG,     "Albers Equal Area"),
                new NamedIdentifier(Citations.EPSG,     "9822"),
                new NamedIdentifier(Citations.GEOTIFF,  "CT_AlbersEqualArea"),
                new NamedIdentifier(Citations.ESRI,     "Albers"),
                new NamedIdentifier(Citations.ESRI,     "Albers Equal Area Conic"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.ALBERS_EQUAL_AREA_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR,          SEMI_MINOR,
                CENTRAL_MERIDIAN,    LATITUDE_OF_ORIGIN,
                STANDARD_PARALLEL_1, STANDARD_PARALLEL_2,
                FALSE_EASTING,       FALSE_NORTHING
            });

        /**
         * Constructs a new provider.
         */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Returns the operation type for this map projection.
         */
        @Override
        public Class<ConicProjection> getOperationType() {
            return ConicProjection.class;
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
            return new AlbersEqualArea(parameters);
        }
    }
}
