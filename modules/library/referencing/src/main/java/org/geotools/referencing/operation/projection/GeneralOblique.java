/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 1999-2015, Open Source Geospatial Foundation (OSGeo)
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
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.toDegrees;

import java.awt.geom.Point2D;
import java.util.logging.Level;
import org.geotools.api.parameter.ParameterDescriptor;
import org.geotools.api.parameter.ParameterDescriptorGroup;
import org.geotools.api.parameter.ParameterNotFoundException;
import org.geotools.api.parameter.ParameterValueGroup;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.operation.MathTransform;
import org.geotools.metadata.iso.citation.Citations;
import org.geotools.referencing.NamedIdentifier;

/**
 * General Oblique Transformation projection useful for rotated spherical coordinates ("Rotated Pole"), commonly used in
 * numerical weather forecasting models.
 *
 * <p>Based on the code provided by Jürgen Seib (Deutscher Wetterdienst), adopted to follow "+proj=ob_tran" behaviour.
 *
 * <p>For examples see "GeneralOblique.txt" file in tests directory
 *
 * @see <a href="http://www.cosmo-model.org/content/model/documentation/core/default.htm#p1">COSMO User Manual, Part
 *     1</a>
 * @see <a href="https://github.com/OSGeo/proj.4/blob/master/src/PJ_ob_tran.c">proj.4</a>
 * @since 13.1
 * @version $Id$
 * @author Maciej Filocha (ICM)
 */
public class GeneralOblique extends MapProjection {

    /** serialVersionUID */
    private static final long serialVersionUID = 9008485425176368580L;

    /**
     * Constructs a rotated latitude/longitude projection.
     *
     * @param parameters The group of parameter values.
     * @throws ParameterNotFoundException if a required parameter was not found.
     */
    protected GeneralOblique(final ParameterValueGroup parameters) throws ParameterNotFoundException {
        super(parameters);
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates (units in radians) and stores the
     * result in {@code ptDst} (linear distance on a unit sphere).
     *
     * @param x The longitude of the coordinate, in <strong>radians</strong>.
     * @param y The latitude of the coordinate, in <strong>radians</strong>.
     */
    @Override
    protected Point2D transformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        final double sinlat = sin(y);
        final double coslat = cos(y);
        final double sinlatP = sin(latitudeOfOrigin);
        final double coslatP = cos(latitudeOfOrigin);
        final double sinlon1 = sin(x);
        final double coslon1 = cos(x);

        x = toDegrees(atan(coslat * sinlon1 / (coslat * sinlatP * coslon1 + sinlat * coslatP))) / globalScale;
        y = toDegrees(asin(sinlat * sinlatP - coslat * coslatP * coslon1)) / globalScale;

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /**
     * Transforms the specified (<var>x</var>,<var>y</var>) coordinates (units in radians) and stores the result in
     * {@code ptDst} (linear distance on a unit sphere).
     */
    @Override
    protected Point2D inverseTransformNormalized(double x, double y, Point2D ptDst) throws ProjectionException {
        final double scalePI = globalScale * PI / 180;
        final double sinlat = sin(y * scalePI);
        final double coslat = cos(y * scalePI);
        final double sinlon = sin(x * scalePI);
        final double coslon = cos(x * scalePI);
        final double sinlatP = sin(latitudeOfOrigin);
        final double coslatP = cos(latitudeOfOrigin);

        x = -atan(coslat * sinlon / (sinlat * coslatP - sinlatP * coslat * coslon));
        y = asin(sinlat * sinlatP + coslat * coslon * coslatP);

        if (ptDst != null) {
            ptDst.setLocation(x, y);
            return ptDst;
        }
        return new Point2D.Double(x, y);
    }

    /** {@inheritDoc} */
    @Override
    public ParameterDescriptorGroup getParameterDescriptors() {
        return Provider.PARAMETERS;
    }

    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////// ////////
    // ////// PROVIDERS ////////
    // ////// ////////
    // ////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////////////////////////////////////////////////////////

    /**
     * The {@linkplain org.geotools.referencing.operation.MathTransformProvider math transform provider} for an
     * {@linkplain org.geotools.referencing.operation.projection.GeneralOblique General Oblique Transformation}
     * projection.
     *
     * @since 2.8
     * @version $Id$
     * @author Maciej Filocha (ICM)
     * @see org.geotools.referencing.operation.DefaultMathTransformFactory
     */
    public static class Provider extends AbstractProvider {

        /** serialVersionUID */
        private static final long serialVersionUID = 8452425384927757022L;

        /** The parameters group. */
        static final ParameterDescriptorGroup PARAMETERS = createDescriptorGroup(
                new NamedIdentifier[] {
                    new NamedIdentifier(Citations.AUTO, "General_Oblique"),
                },
                new ParameterDescriptor[] {
                    SEMI_MAJOR,
                    SEMI_MINOR,
                    CENTRAL_MERIDIAN,
                    LATITUDE_OF_ORIGIN,
                    SCALE_FACTOR,
                    FALSE_EASTING,
                    FALSE_NORTHING
                });

        /** Constructs a new provider. */
        public Provider() {
            super(PARAMETERS);
        }

        /**
         * Creates a transform from the specified group of parameter values.
         *
         * @param parameters The group of parameter values.
         * @return The created math transform.
         * @throws ParameterNotFoundException if a required parameter was not found.
         */
        @Override
        protected MathTransform createMathTransform(final ParameterValueGroup parameters)
                throws ParameterNotFoundException, FactoryException {
            if (isSpherical(parameters)) {
                return new GeneralOblique(parameters);
            } else {
                /*
                 * "Use of the general oblique transformation is limited to projections assuming a spherical earth. Oblique or transverse projections
                 * on a elliptical earth present complex problem that requires specific analysis of each projection and cannot be applied in a general
                 * manner." (see http://download.osgeo.org/proj/proj.4.3.I2.pdf)
                 *
                 * However, enabling this dirty hack below allows to convert to and from WGS84 coordinates with much better accuracy. One possible
                 * reason is that Geotools omits additional transformation between spherical and ellipsoidal coordinates which is not really needed here.
                 */
                LOGGER.log(
                        Level.FINE,
                        "GeoTools GeneralOblique transformation is defined only on the sphere, "
                                + "we're going to use spherical equations even if the projection is using an ellipsoid");
                return new GeneralOblique(parameters);
            }
        }
    }
}
