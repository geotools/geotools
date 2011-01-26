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
package org.osgeo.geometry.primitive.patches;

import java.util.List;

import org.osgeo.geometry.primitive.LinearRing;
import org.osgeo.geometry.primitive.Point;

/**
 * A {@link Rectangle} is a {@link PolygonPatch} defined by four planar points.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface Rectangle extends PolygonPatch {

    /**
     * Returns the first of the four control points.
     *
     * @return the first control point
     */
    public Point getPoint1();

    /**
     * Returns the second of the four control points.
     *
     * @return the second control point
     */
    public Point getPoint2();

    /**
     * Returns the third of the four control points.
     *
     * @return the third control point
     */
    public Point getPoint3();

    /**
     * Returns the last of the four control points.
     *
     * @return the last control point
     */
    public Point getPoint4();

    /**
     * Returns the sequence of control points as a {@link LinearRing}.
     *
     * @return the exterior ring
     */
    public LinearRing getExteriorRing();

    public List<LinearRing> getBoundaryRings();
}
