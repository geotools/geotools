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

import org.osgeo.geometry.primitive.Point;

/**
 * An {@link ArcStringByBulge} that consists of a single arc only.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public interface ArcByBulge extends ArcStringByBulge {

    /**
     * Returns the first control point.
     *
     * @return the first control point
     */
    public Point getPoint1();

    /**
     * Returns the second control point.
     *
     * @return the second control point
     */
    public Point getPoint2();

    /**
     * Returns the single bulge value.
     *
     * @return the bulge value
     */
    public double getBulge();

    /**
     * Returns the single normal vector for the bulge.
     *
     * @return the bulge's normal vector
     */
    public Point getNormal();
}
