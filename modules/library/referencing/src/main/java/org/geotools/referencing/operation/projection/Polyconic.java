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

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.tan;

import java.awt.geom.Point2D;
import java.util.logging.Level;

import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Vocabulary;
import org.geotools.resources.i18n.VocabularyKeys;
import org.opengis.parameter.ParameterDescriptor;
import org.opengis.parameter.ParameterDescriptorGroup;
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.referencing.operation.CylindricalProjection;
import org.opengis.referencing.operation.MathTransform;


/**
 * Polyconic (American). 
 * 
 * <b>References:</b>
 * <ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/PolyconicProjection.html">Polyconic projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/polyconic.html">"Polyconic" on RemoteSensing.org</A>
 *
 * @since 2.6.1
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.6.x/modules/library/referencing/src/main/java/org/geotools/referencing/operation/projection/Mercator.java $
 * @version $Id: Mercator.java 30641 2008-06-12 17:42:27Z acuster $
 * @author Andrea Aime
 */
public class Polyconic extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 6516419168461705584L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-10;
    
    /**
     * Maximum number of iterations for iterative computations.
     */
    private static final int MAXIMUM_ITERATIONS = 20;

    /**
     * Difference allowed in iterative computations.
     */
    private static final double ITERATION_TOLERANCE = 1E-12;
    
    /**
     * Meridian distance at the {@code latitudeOfOrigin}.
     * Used for calculations for the ellipsoid.
     */
    private final double ml0;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Polyconic(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        
        //  Compute constants
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
    protected Point2D transformNormalized(double lam, double phi, final Point2D ptDst)
            throws ProjectionException {
        double ms, sp, cp, x, y;

        if (abs(phi) <= EPSILON) {
            x = lam;
            y = -ml0;
        } else {
            sp = sin(phi);
            ms = abs(cp = cos(phi)) > EPSILON ? msfn(sp, cp) / sp : 0.;
            lam *= sp;
            x = ms * sin(lam);
            y = (mlfn(phi, sp, cp) - ml0) + ms * (1. - cos(lam));
        }

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
     * and stores the result in {@code ptDst}.
     */
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        double lam, phi;
        
        y += ml0;
        if (abs(y) <= EPSILON) { 
            lam = x; 
            phi = 0.; 
        } else {
            final double r = y * y + x * x;
            phi = y;
            int i = 0;
            for (; i <= MAXIMUM_ITERATIONS; i++) {
                final double sp = sin(phi);
                final double cp = cos(phi);
                if (abs(cp) < ITERATION_TOLERANCE)
                    throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);

                final double s2ph = sp * cp;
                double mlp = sqrt(1. - excentricitySquared * sp * sp);
                final double c = sp * mlp / cp;
                final double ml = mlfn(phi, sp, cp);
                final double mlb = ml * ml + r;
                mlp = (1. - excentricitySquared) / (mlp * mlp * mlp);
                final double dPhi = ( ml + ml + c * mlb - 2. * y * (c * ml + 1.) ) / (
                            excentricitySquared * s2ph * (mlb - 2. * y * ml) / c +
                            2.* (y - ml) * (c * mlp - 1. / s2ph) - mlp - mlp );
                if (abs(dPhi) <= ITERATION_TOLERANCE)
                    break;
                            
                phi += dPhi;
            }
            if (i > MAXIMUM_ITERATIONS)
                throw new ProjectionException(ErrorKeys.NO_CONVERGENCE);
            final double c = sin(phi);
            lam = asin(x * tan(phi) * sqrt(1. - excentricitySquared * c * c)) / sin(phi);
        }

        if (ptDst != null) {
            ptDst.setLocation(lam, phi);
            return ptDst;
        }
        return new Point2D.Double(lam, phi);
    }
    
    @Override
    protected double getToleranceForAssertions(double longitude, double latitude) {
        if (abs(longitude - centralMeridian)/2 + abs(latitude  - latitudeOfOrigin) > 10) {
            // When far from the valid area, use a larger tolerance.
            return 0.1;
        }
        return super.getToleranceForAssertions(longitude, latitude);
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
        if (super.equals(object)) {
            final Polyconic that = (Polyconic) object;
            return equals(this.ml0,  that.ml0);
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
     * provider} for a {@linkplain Mercator1SP Mercator 1SP} projection (EPSG code 9804).
     *
     * @since 2.6.2
     * @version $Id: Mercator1SP.java 34715 2009-12-21 17:11:00Z aaime $
     * @author Andrea Aime
     *
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 3082828148070128422L;

        /**
         * The parameters group.
         */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(new NamedIdentifier[] {
                new NamedIdentifier(Citations.OGC,      "Polyconic"),
                new NamedIdentifier(Citations.EPSG,     "American Polyconic"),
                new NamedIdentifier(Citations.EPSG,     "9818"),
                new NamedIdentifier(Citations.GEOTIFF,  "Polyconic"),
                new NamedIdentifier(Citations.GEOTOOLS, Vocabulary.formatInternational(
                                    VocabularyKeys.POLYCONIC_PROJECTION))
            }, new ParameterDescriptor[] {
                SEMI_MAJOR, SEMI_MINOR,
                LATITUDE_OF_ORIGIN, CENTRAL_MERIDIAN, SCALE_FACTOR,
                FALSE_EASTING, FALSE_NORTHING
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
            if (isSpherical(parameters))
                LOGGER.log(Level.WARNING, "Polyconic spherical case not implemented, falling back on the elliptical equations");
            return new Polyconic(parameters);
        }
    }
}
