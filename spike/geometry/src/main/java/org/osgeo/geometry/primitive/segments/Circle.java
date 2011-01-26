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

import org.osgeo.commons.uom.Measure;
import org.osgeo.commons.uom.Unit;
import org.osgeo.geometry.primitive.Point;

/**
 * An {@link Arc} whose ends coincide to form a simple closed loop.
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface Circle extends Arc {

    /**
     * Returns the mid-point of the circle.
     * 
     * @return the mid-point
     */
    public Point getMidPoint();

    /**
     * Returns the radius of the circle.
     * 
     * @param requestedUnits
     *            units that the radius should be expressed as
     * @return the radius of the arc
     */
    public Measure getRadius( Unit requestedUnits );
}
