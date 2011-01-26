/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io;


/**
 * Overview policies.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Andrea Aime
 * @author Simone Giannecchini
 */
public enum OverviewPolicy {
    /**
     * Choose the overview with the lower resolution among the ones
     * with higher resolution than one used for rendering.
     */
    QUALITY,

    /**
     * Ignore the overviews.
     */
    IGNORE,

    /**
     * Choose the overview with with the resolution closest to the one used for rendering.
     */
    NEAREST,

    /**
     * Choose the overview with the higher resolution among the ones
     * with lower resolution than one used for rendering.
     */
    SPEED;
    
    public static OverviewPolicy getDefaultPolicy(){
    	return NEAREST;
    }
}
