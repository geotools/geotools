/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.grid.ortholine;

/**
 * Constants to identify the orientation of an ortho-line.
 * 
 * @author mbedward
 * @since 8.0
 *
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/unsupported/grid/src/main/java/org/geotools/grid/ortholine/LineOrientation.java $
 * @version $Id: Grids.java 37149 2011-05-10 11:47:02Z mbedward $
 */
public enum LineOrientation {
    /**
     * Line parallel to North-South axis in geographic space or
     * Y-axis in Cartesian space.
     */
    VERTICAL, 
    
    /**
     * Line parallel to EAST-WEST axis in geographic space or
     * X-axis in Cartesian space.
     */ 
    HORIZONTAL
    
}
