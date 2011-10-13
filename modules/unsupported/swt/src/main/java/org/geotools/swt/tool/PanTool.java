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

package org.geotools.swt.tool;

import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Point;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.utils.CursorManager;
import org.geotools.swt.utils.Messages;

/**
 * A map panning tool for {@link SwtMapPane}.
 * 
 * <p>Allows the user to drag the map
 * with the mouse.
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class PanTool extends CursorTool {

    /** Tool name */
    public static final String TOOL_NAME = Messages.getString("tool_name_pan");
    /** Tool tip text */
    public static final String TOOL_TIP = Messages.getString("tool_tip_pan");

    private Cursor cursor;

    private Point panePos;
    boolean panning;

    /**
     * Constructor
     */
    public PanTool() {
        cursor = CursorManager.getInstance().getPanCursor();

        panning = false;
    }

    /**
     * Respond to a mouse button press event from the map mapPane. This may
     * signal the start of a mouse drag. Records the event's window position.
     * @param ev the mouse event
     */
    @Override
    public void onMousePressed( MapMouseEvent ev ) {
        panePos = ev.getPoint();
        panning = true;
    }

    /**
     * Respond to a mouse dragged event. Calls {@link org.geotools.swing.JMapPane#moveImage()}
     * @param ev the mouse event
     */
    @Override
    public void onMouseDragged( MapMouseEvent ev ) {
        if (panning) {
            Point pos = ev.getPoint();
            if (!pos.equals(panePos)) {
                getMapPane().moveImage(pos.x - panePos.x, pos.y - panePos.y);
                panePos = pos;
            }
        }
    }

    /**
     * If this button release is the end of a mouse dragged event, requests the
     * map mapPane to repaint the display
     * @param ev the mouse event
     */
    @Override
    public void onMouseReleased( MapMouseEvent ev ) {
        panning = false;
        getMapPane().redraw();
    }

    /**
     * Get the mouse cursor for this tool
     */
    @Override
    public Cursor getCursor() {
        return cursor;
    }

    public boolean canDraw() {
        return false;
    }

    public boolean canMove() {
        return true;
    }
}
