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
 * Defines a transformation from a constructive parameter space to the coordinate space of the coordinate reference
 * system being used.
 *
 * @author <a href="mailto:schneider@lat-lon.de">Markus Schneider </a>
 * @author last edited by: $Author$
 *
 * @version $Revision$, $Date$
 */
public class AffinePlacement {

    private Point location;

    private Points refDirections;

    private int inDimension;

    private int outDimension;

    /**
     * Creates a new <code>AffinePlacement</code> from the given parameters.
     *
     * @param location
     *            the target of the parameter space origin
     * @param refDirections
     *            the target directions for the coordinate basis vectors of the parameter space
     * @param inDimension
     *            the dimension of the constructive parameter space
     * @param outDimension
     *            the dimension of the coordinate space
     */
    public AffinePlacement( Point location, Points refDirections, int inDimension, int outDimension ) {
        this.location = location;
        this.refDirections = refDirections;
        this.inDimension = inDimension;
        this.outDimension = outDimension;
    }

    /**
     * Returns the target of the parameter space origin.
     *
     * @return the target of the parameter space origin
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Returns the target directions for the coordinate basis vectors of the parameter space.
     * <p>
     * The number of directions is equal to the <code>inDimension</code>. The dimension of the directions is equal to
     * the <code>outDimension</code>.
     * </p>
     *
     * @return the target directions
     */
    public Points getRefDirections() {
        return refDirections;
    }

    /**
     * Returns the <code>inDimension</code>, i.e. the dimension of the constructive parameter space.
     *
     * @return the <code>inDimension</code>
     */
    public int getInDimension() {
        return inDimension;
    }

    /**
     * Returns the <code>outDimension</code>, i.e. the dimension of the coordinate space.
     *
     * @return the <code>outDimension</code>
     */
    public int getOutDimension() {
        return outDimension;
    }
}
