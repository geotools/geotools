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

/**
 * Circular {@link CurveSegment}.
 * <p>
 * From the GML 3.1.1 spec: This variant of the arc computes the mid points of the arcs instead of storing the
 * coordinates directly. The control point sequence consists of the start and end points of each arc plus the bulge.
 * </p>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface ArcStringByBulge extends CurveSegment {

    /**
     * Returns the number of arcs of the string.
     * 
     * @return the number of arcs
     */
    public int getNumArcs();

    /**
     * Returns the bulge values.
     * 
     * @return the bulge values
     */
    public double[] getBulges();

    /**
     * Returns the normal vectors that define the arc string.
     * 
     * @return the normal vectors
     */
    public Points getNormals();

    /**
     * Returns the control points of the segment.
     * 
     * @return the control points of the segment
     */
    public Points getControlPoints();
}
