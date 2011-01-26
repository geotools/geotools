/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.hexagon;

import org.geotools.grid.Orientation;
import org.geotools.grid.GridElement;

/**
 * Defines methods and enum constants to work with hexagons.
 *
 * @author mbedward
 * @since 2.7
 * @source $URL$
 * @version $Id$
 */
public interface Hexagon extends GridElement {

    /**
     * Gets the side length of this hexagon.
     *
     * @return side length
     */
    public double getSideLength();

    /**
     * Gets the orientation of this hexagon.
     *
     * @return either {@linkplain Orientation#ANGLED} or {@linkplain Orientation#FLAT}
     */
    public Orientation getOrientation();

}
