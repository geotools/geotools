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

package org.geotools.grid;

import com.vividsolutions.jts.geom.Coordinate;

/**
 * Represents a {@code GridElement} that is a polygon.
 *
 * @author michael
 */
public interface PolygonElement extends GridElement {

    /**
     * Gets the area of this grid element.
     *
     * @return the area
     */
    double getArea();

    /**
     * Gets the center coordinates of this grid element.
     *
     * @return the center coordinates
     */
    Coordinate getCenter();

}
