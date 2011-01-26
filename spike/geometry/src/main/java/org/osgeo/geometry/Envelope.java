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
package org.osgeo.geometry;

import org.osgeo.geometry.primitive.Point;

/**
 * Axis-parallel bounding box.
 * 
 * @author <a href="mailto:poth@lat-lon.de">Andreas Poth</a>
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version. $Revision$, $Date$
 */
public interface Envelope extends Geometry {

    /**
     * Must always return {@link Geometry.GeometryType#ENVELOPE}.
     * 
     * @return {@link Geometry.GeometryType#ENVELOPE}.
     */
    public GeometryType getGeometryType();

    /**
     * Returns the envelope's minimum coordinate.
     * 
     * @return minimum coordinate
     */
    public Point getMin();

    /**
     * Returns the envelope's maximum coordinate
     * 
     * @return maximum coordinate
     */
    public Point getMax();

    /**
     * Returns the envelope's span of the first dimension (in units of the associated coordinate system).
     * 
     * @return span of the first dimension
     */
    public double getSpan0();

    /**
     * Returns the envelope's span of the second dimension (in units of the associated coordinate system).
     * 
     * @return span of the second dimension
     */
    public double getSpan1();

    /**
     * Returns the envelope's span of the second dimension (in units of the associated coordinate system).
     * 
     * @param dim
     *            index of the span to be returned
     * @return span of the specified dimension
     */
    public double getSpan( int dim );
}