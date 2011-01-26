/*
 *    OSGeom -- Geometry Collab
 *
 *    (C) 2009, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2009 Department of Geography, University of Bonn
 *    (C) 2001-2009 lat/lon GmbH
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
package org.osgeo.geometry.primitive.segments;

import org.osgeo.geometry.points.Points;
import org.osgeo.geometry.primitive.Point;

/**
 * A {@link CurveSegment} that uses the control points and a set of derivative parameters to define a piecewise 3rd
 * degree polynomial interpolation.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface CubicSpline extends CurveSegment {

    /**
     * Returns the control points of the spline.
     *
     * @return the control points of the spline
     */
    public Points getControlPoints();

    /**
     * Returns the unit tangent vector at the start point of the spline.
     *
     * @return the unit tangent vector at the start point of the spline
     */
    public Point getVectorAtStart();

    /**
     * Returns the unit tangent vector at the end point of the spline.
     *
     * @return the unit tangent vector at the end point of the spline
     */
    public Point getVectorAtEnd();
}
