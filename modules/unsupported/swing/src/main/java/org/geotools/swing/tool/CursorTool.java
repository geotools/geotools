/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2011, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Cursor;
import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapMouseAdapter;

/**
 * The base class for map pane cursor tools. Simply adds a getCursor method to the MapToolAdapter
 *
 * @author Michael Bedward
 * @since 2.6
 * @version $Id$
 */
public abstract class CursorTool extends MapMouseAdapter {

    private MapPane mapPane;

    /**
     * Set the map pane that this cursor tool is associated with
     *
     * @param pane the map pane
     * @throws IllegalArgumentException if mapPane is null
     */
    public void setMapPane(MapPane pane) {
        if (pane == null) {
            throw new IllegalArgumentException("pane arg must not be null");
        }

        this.mapPane = pane;
    }

    /**
     * Get the map pane that this tool is servicing
     *
     * @return the map pane
     */
    public MapPane getMapPane() {
        return mapPane;
    }

    /**
     * Get the cursor for this tool. Sub-classes should override this method to provide a custom
     * cursor.
     *
     * @return the default cursor
     */
    public Cursor getCursor() {
        return Cursor.getDefaultCursor();
    }

    /**
     * Query if the tool is one that draws a box on the map display when the mouse is being dragged
     * (eg. to indicate a zoom area).
     *
     * @return true if this tool supports drawing a drag-box on the map; false otherwise
     */
    public boolean drawDragBox() {
        return false;
    }
}
