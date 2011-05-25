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
import com.vividsolutions.jts.geom.Polygon;

import org.geotools.geometry.jts.ReferencedEnvelope;

/**
 * Defines basic methods for grid elements.
 *
 * @author michael
 *
 * @source $URL$
 */
public interface GridElement {

    /**
     * Gets the area of this grid element.
     *
     * @return the area
     */
    double getArea();

    /**
     * Gets the bounds of this grid element.
     *
     * @return the bounding rectangle
     */
    ReferencedEnvelope getBounds();

    /**
     * Gets the center coordinates of this grid element.
     *
     * @return the center coordinates
     */
    Coordinate getCenter();

    /**
     * Gets the vertices of this grid element.
     *
     * @return the vertices
     */
    Coordinate[] getVertices();

    /**
     * Creates a new {@code Polygon} from this grid element.
     * 
     * @return a new {@code Polygon}
     */
    Polygon toPolygon();

    /**
     * Creates a new, densified {@code Polygon} from this grid element.
     *
     * @param maxSpacing the maximum distance between adjacent vertices
     *
     * @return a new {@code Polygon} with additional vertices on each edge
     *
     * @throws IllegalArgumentException if maxSpacing is {@code <=} 0
     */
    Polygon toDensePolygon(double maxSpacing);
}
