/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2000-2008, Open Source Geospatial Foundation (OSGeo)
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
import org.opengis.parameter.ParameterNotFoundException;
import org.opengis.parameter.ParameterValueGroup;
import org.geotools.resources.i18n.ErrorKeys;

import static java.lang.Math.*;


/**
 * The polar case of the {@link Orthographic} projection. Only the spherical
 * form is given here.
 *
 * @since 2.4
 *
 * @source $URL$
 * @version $Id$
 * @author Rueben Schulz
 */
public class PolarOrthographic extends Orthographic {
    /**
     * For compatibility with different versions during deserialization.
     */
    private static final long serialVersionUID = 3281503361127178484L;

    /**
     * Maximum difference allowed when comparing real numbers.
     */
    private static final double EPSILON = 1E-6;

    /**
     * {@code true} if this projection is for the north pole, or {@code false}
     * if it is for the south pole.
     */
    private final boolean northPole;

    /**
     * Constructs a polar orthographic projection.
     *
     * @param  parameters The parameter values in standard units.
     * @throws ParameterNotFoundException if a mandatory parameter is missing.
     */
    protected PolarOrthographic(final ParameterValueGroup parameters)
            throws ParameterNotFoundException
    {
        super(parameters);
        ensureLatitudeEquals(Provider.LATITUDE_OF_ORIGIN, latitudeOfOrigin, PI/2);
        northPole = (latitudeOfOrigin > 0);
        latitudeOfOrigin = (northPole) ? PI/2 : -PI/2;
    }

    /**
     * Transforms the specified (<var>&lambda;</var>,<var>&phi;</var>) coordinates
     * (units in radians) and stores the result in {@code ptDst} (linear distance
     * on a unit sphere).
     */
    protected Point2D transformNormalized(double x, double y, final Point2D ptDst)
            throws ProjectionException
    {
        if (abs(y - latitudeOfOrigin) - EPSILON > PI/2) {
            throw new ProjectionException(ErrorKeys.POINT_OUTSIDE_HEMISPHERE);
        }
        double cosphi = cos(y);
        double coslam = cos(x);
        if (northPole) {
            coslam = -coslam;
        }
        y = cosphi * coslam;
        x = cosphi * sin(x);

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
        final double rho = hypot(x, y);
        double sinc = rho;
        if (sinc > 1.0) {
            if ((sinc - 1.0) > EPSILON) {
                throw new ProjectionException(ErrorKeys.POINT_OUTSIDE_HEMISPHERE);
            }
            sinc = 1.0;
        }
        if (rho <= EPSILON) {
            y = latitudeOfOrigin;
            x = 0.0;
        } else {
            double phi;
            if (northPole) {
                y = -y;
                phi = acos(sinc);   // equivalent to asin(cos(c)) over the range [0:1]
            } else {
                phi = -acos(sinc);
            }
            x = atan2(x, y);
            y = phi;
        }
        if (ptDst != null) {
            ptDst.setLocation(x,y);
            return ptDst;
        }
        return new Point2D.Double(x,y);
    }
}
