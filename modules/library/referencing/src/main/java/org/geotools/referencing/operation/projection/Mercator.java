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

import java.util.Collection;
import java.awt.geom.Point2D;
import org.opengis.parameter.GeneralParameterDescriptor;
import org.opengis.parameter.ParameterValueGroup;
import org.opengis.parameter.ParameterNotFoundException;

import static java.lang.Math.*;


/**
 * Mercator Cylindrical Projection. The parallels and the meridians are straight lines and
 * cross at right angles; this projection thus produces rectangular charts. The scale is true
 * along the equator (by default) or along two parallels equidistant of the equator (if a scale
 * factor other than 1 is used). This projection is used to represent areas close to the equator.
 * It is also often used for maritime navigation because all the straight lines on the chart are
 * <em>loxodrome</em> lines, i.e. a ship following this line would keep a constant azimuth on its
 * compass.
 * <p>
 * This implementation handles both the 1 and 2 stardard parallel cases.
 * For {@code Mercator_1SP} (EPSG code 9804), the line of contact is the equator.
 * For {@code Mercator_2SP} (EPSG code 9805) lines of contact are symmetrical
 * about the equator.
 * <p>
 * <b>References:</b>
 * <ul>
 *   <li>John P. Snyder (Map Projections - A Working Manual,<br>
 *       U.S. Geological Survey Professional Paper 1395, 1987)</li>
 *   <li>"Coordinate Conversions and Transformations including Formulas",<br>
 *       EPSG Guidence Note Number 7, Version 19.</li>
 * </ul>
 *
 * @see <A HREF="http://mathworld.wolfram.com/MercatorProjection.html">Mercator projection on MathWorld</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_1sp.html">"mercator_1sp" on RemoteSensing.org</A>
 * @see <A HREF="http://www.remotesensing.org/geotiff/proj_list/mercator_2sp.html">"mercator_2sp" on RemoteSensing.org</A>
 *
 * @since 2.1
 *
 * @source $URL$
 * @version $Id$
 * @author André Gosselin
 * @author Martin Desruisseaux (PMO, IRD)
 * @author Rueben Schulz
 * @author Simone Giannecchini
 */
public abstract class Mercator extends MapProjection {
    /**
     * For cross-version compatibility.
     */
    private static final long serialVersionUID = 6146741819833248649L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * Standard Parallel used for the {@link Mercator2SP} case.
     * Set to {@link Double#NaN} for the {@link Mercator1SP} case.
     */
    protected final double standardParallel;

    /**
     * Constructs a new map projection from the supplied parameters.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected Mercator(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
        final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
        if (expected.contains(AbstractProvider.STANDARD_PARALLEL_1)) {
            /*
             * scaleFactor is not a parameter in the Mercator_2SP case and is computed from
             * the standard parallel.   The super-class constructor should have initialized
             * 'scaleFactor' to 1. We still use the '*=' operator rather than '=' in case a
             * user implementation still provides a scale factor for its custom projections.
             */
            standardParallel = abs(doubleValue(expected, AbstractProvider.STANDARD_PARALLEL_1, parameters));
            ensureLatitudeInRange(AbstractProvider.STANDARD_PARALLEL_1, standardParallel, false);
            if (isSpherical) {
                scaleFactor *= cos(standardParallel);
            }  else {
                scaleFactor *= msfn(sin(standardParallel), cos(standardParallel));
            }
            globalScale = scaleFactor * semiMajor;
        } else {
            // No standard parallel. Instead, uses the scale factor explicitely provided.
            standardParallel = Double.NaN;
        }
        /*
         * A correction that allows us to employs a latitude of origin that is not
         * correspondent to the equator. See Snyder and al. for reference, page 47.
         * The scale correction is multiplied with the global scale, which allows
         * MapProjection superclass to merge this correction with the scale factor
         * in a single multiplication.
         */
        final double sinPhi = sin(latitudeOfOrigin);
        globalScale *= (cos(latitudeOfOrigin) / (sqrt(1 - excentricitySquared * sinPhi * sinPhi)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public ParameterValueGroup getParameterValues() {
        final ParameterValueGroup values = super.getParameterValues();
        if (!Double.isNaN(standardParallel)) {
            final Collection<GeneralParameterDescriptor> expected = getParameterDescriptors().descriptors();
            set(expected, AbstractProvider.STANDARD_PARALLEL_1, values, standardParallel);
        }
        return values;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        if (abs(y) > (PI/2 - EPSILON)) {
            throw new ProjectionException(y);
        }
        y = -log(tsfn(y, sin(y)));

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
    protected Point2D inverseTransformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        y = exp(-y);
        y = cphi2(y);

        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }


    /**
     * Provides the transform equations for the spherical case of the Mercator projection.
     *
     * @version $Id$
     * @author Martin Desruisseaux (PMO, IRD)
     * @author Rueben Schulz
     */
    static abstract class Spherical extends Mercator {
        /**
         * For cross-version compatibility.
         */
        private static final long serialVersionUID = 2383414176395616561L;

        /**
         * Constructs a new map projection from the suplied parameters.
         *
         * @param  parameters The parameter values in standard units.
         * @throws ParameterNotFoundException if a mandatory parameter is missing.
         */
        protected Spherical(final ParameterValueGroup parameters) throws ParameterNotFoundException {
            super(parameters);
            ensureSpherical();
        }

        /**
         * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
         * (units in radians) and stores the result in {@code ptDst} (linear distance
         * on a unit sphere).
         */
        @Override
        protected Point2D transformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            if (abs(y) > (PI/2 - EPSILON)) {
                throw new ProjectionException(y);
            }
            // Compute using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.transformNormalized(x, y, ptDst)) != null;

            y = log(tan(PI/4 + 0.5*y));

            assert checkTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }

        /**
         * Transforms the specified (<var>x</var>,<var>y</var>) coordinates
         * and stores the result in {@code ptDst} using equations for a sphere.
         */
        @Override
        protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst)
                throws ProjectionException
        {
            // Computes using ellipsoidal formulas, for comparaison later.
            assert (ptDst = super.inverseTransformNormalized(x, y, ptDst)) != null;

            y = PI/2 - 2.0*atan(exp(-y));

            assert checkInverseTransform(x, y, ptDst);
            if (ptDst != null) {
                ptDst.setLocation(x,y);
                return ptDst;
            }
            return new Point2D.Double(x,y);
        }
    }


    /**
     * Returns a hash value for this projection.
     */
    @Override
    public int hashCode() {
        final long code = Double.doubleToLongBits(standardParallel);
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
            final Mercator that = (Mercator) object;
            return equals(this.standardParallel,  that.standardParallel);
        }
        return false;
    }
}
