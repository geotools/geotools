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

/**
 * Constants to describe the orientation of a grid element.
 * <ul>
 * <li>
 * An {@code ANGLED} element has a "pointy" top with a single vertex
 * touching the upper edge of its bounding rectangle.
 * </li>
 * <li>
 * A {@code FLAT} element has edges that run along the upper and
 * lower edges of its bounding rectangle
 * </li>
 * </ul>
 */
public enum Orientation {

    /**
     * An {@code ANGLED} element has a "pointy" top with a single vertex
     * touching the upper edge of its bounding rectangle.
     */
    ANGLED, 
    
    /**
     * A {@code FLAT} element has edges that run along the upper and
     * lower edges of its bounding rectangle
     */
    FLAT;
}
