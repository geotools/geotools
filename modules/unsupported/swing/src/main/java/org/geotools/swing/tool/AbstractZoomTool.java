/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swing.tool;


/**
 * Abstract base class for the zoom-in and zoom-out tools. Provides getter / setter
 * methods for the zoom increment.
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */

public abstract class AbstractZoomTool extends CursorTool {
    /** The default zoom increment */
    public static final double DEFAULT_ZOOM_FACTOR = 1.5;

    /** The working zoom increment */
    protected double zoom;

    /**
     * Constructor
     */
    public AbstractZoomTool() {
        setZoom(DEFAULT_ZOOM_FACTOR);
    }
    
    /**
     * Get the current areal zoom increment. 
     * 
     * @return the current zoom increment as a double
     */
    public double getZoom() {
        return zoom;
    }
    
    /**
     * Set the zoom increment
     * 
     * @param newZoom the new zoom increment; values &lt;= 1.0
     * will be ignored
     * 
     * @return the previous zoom increment
     */
    public double setZoom(double newZoom) {
        double old = zoom;
        if (newZoom > 1.0d) {
            zoom = newZoom;
        }
        return old;
    }

}
