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
package org.osgeo.geometry.points;

import org.osgeo.geometry.primitive.LineString;
import org.osgeo.geometry.primitive.Point;

/**
 * Abstraction of a sequence of {@link Point} instances.
 * <p>
 * The motivation for this interface is to allow more compact and efficient representations than using lists or arrays
 * of {@link Point} objects. This is essential, as geometries are usually build from many points, e.g. a detailed
 * {@link LineString} may consist of tens or hundreds thousands of points.
 * </p>
 * 
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider</a>
 * @author last edited by: $Author$
 * 
 * @version $Revision$, $Date$
 */
public interface Points extends Iterable<Point> {

    /**
     * Returns the coordinate dimension, i.e. the dimension of the space that the points are embedded in.
     * 
     * @return the coordinate dimension
     */
    public int getDimension();

    /**
     * Returns the number of represented {@link Point}s.
     * 
     * @return the number of points
     */
    public int size();

    /**
     * Returns the {@link Point} at the specified position.
     * <p>
     * NOTE: It is generally more expensive to use this method than to access a {@link Point} by iterating over this
     * object, because a new {@link Point} object may have to be created (depending on the implementation).
     * 
     * @param i
     * @return the point at the specified position
     */
    public Point get( int i );

    /**
     * Returns the first point of the sequence.
     * 
     * @return the first point
     */
    public Point getStartPoint();

    /**
     * Returns the last point of the sequence.
     * 
     * @return the last point
     */
    public Point getEndPoint();

    /**
     * Returns all coordinates of the contained {@link Point}s as an array.
     * <p>
     * NOTE: This method should be avoided, as it may involve expensive operations.
     * </p>
     * 
     * @return coordinates as a one-dimensional array
     */
    public double[] getAsArray();
}
