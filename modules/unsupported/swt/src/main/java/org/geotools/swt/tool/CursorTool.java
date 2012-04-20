/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2003-2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.swt.tool;

import org.eclipse.swt.graphics.Cursor;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.event.MapMouseAdapter;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.utils.CursorManager;
import org.geotools.swt.utils.Messages;

/**
 * The base class for map pane cursor tools. Simply adds a getCursor
 * method to the MapToolAdapter
 * 
 * @author Michael Bedward
 * @author Andrea Antonello (www.hydrologis.com)
 *
 *
 *
 * @source $URL$
 */
public abstract class CursorTool extends MapMouseAdapter {

    /**
     * Flag indicating that the tool should be triggered whenever any mouse button
     * is used.
     */
    public static final int ANY_BUTTON = -1;

    private SwtMapPane mapPane;
    private int triggeringMouseButton;


    /**
     * Constructs a new cursor tool.
     * @param triggeringMouseButton Mouse button which triggers the tool's activation
     * or {@value #ANY_BUTTON} if the tool is to be triggered by any button
     */
    public CursorTool(int triggeringMouseButton) {
        this.triggeringMouseButton = triggeringMouseButton;
    }

    /**
     * Constructs a new cursor tool which is triggered by any mouse button.
     */
    public CursorTool() {
        this(ANY_BUTTON);
    }

    /**
     * Set the map pane that this cursor tool is associated with
     * @param pane the map pane
     * @throws IllegalArgumentException if mapPane is null
     */
    public void setMapPane( SwtMapPane pane ) {
        if (pane == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error"));
        }

        this.mapPane = pane;
    }

    /**
     * Get the map pane that this tool is servicing
     *
     * @return the map pane
     */
    public SwtMapPane getMapPane() {
        return mapPane;
    }

    /**
     * Get the cursor for this tool. Sub-classes should override this
     * method to provide a custom cursor.
     *
     * @return the default cursor
     */
    public Cursor getCursor() {
        return CursorManager.getInstance().getArrowCursor();
    }

    /**
     * Checks if the tool can draw when dragging.
     * 
     * @return <code>true</code> if the tool can draw.
     */
    public abstract boolean canDraw();

    /**
     * Checks if the tool can move the map when dragging.
     * 
     * @return <code>true</code> if the tool can move the map while dragging.
     */
    public abstract boolean canMove();

    /**
     * Checks if the tool should be triggered by the event.
     * @param event event to be checked
     * @return <code>true</code> if the tool is triggered by the event
     */
    protected boolean isTriggerMouseButton(MapMouseEvent event)
    {
        return triggeringMouseButton == ANY_BUTTON || triggeringMouseButton == event.getMouseButton();
    }
}
