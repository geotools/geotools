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

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Cursor;
import org.eclipse.swt.graphics.Rectangle;
import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.utils.CursorManager;
import org.geotools.swt.utils.Messages;

/**
 * A zoom-out tool for JMapPane.
 * <p>
 * For mouse clicks, the display will be zoomed-out such that the 
 * map centre is the position of the mouse click and the map
 * width and height are calculated as:
 * <pre>   {@code len = len.old * z} </pre>
 * where {@code z} is the linear zoom increment (>= 1.0)
 * 
 * @author Michael Bedward
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 2.6
 *
 *
 *
 * @source $URL$
 */
public class ZoomOutTool extends AbstractZoomTool {

    /** Tool name */
    public static final String TOOL_NAME = Messages.getString("tool_name_zoom_out");
    /** Tool tip text */
    public static final String TOOL_TIP = Messages.getString("tool_tip_zoom_out");

    private Cursor cursor;

    /**
     * Constructs a new zoom out tool. To activate the tool only on certain
     * mouse events provide a single mask, e.g. {@link SWT#BUTTON1}, or
     * a combination of multiple SWT-masks.
     *
     * @param triggerButtonMask Mouse button which triggers the tool's activation
     * or {@value #ANY_BUTTON} if the tool is to be triggered by any button
     */
    public ZoomOutTool(int triggerButtonMask) {
        super(triggerButtonMask);
        cursor = CursorManager.getInstance().getZoomoutCursor();
    }

    /**
     * Constructs a new zoom out tool which is triggered by any mouse button.
     */
    public ZoomOutTool() {
        this(CursorTool.ANY_BUTTON);
    }

    /**
     * Zoom out by the currently set increment, with the map
     * centred at the location (in world coords) of the mouse
     * click
     *
     * @param ev the mouse event
     */
    @Override
    public void onMouseClicked( MapMouseEvent ev ) {

        if ( ! isTriggerMouseButton(ev)) {
            return;
        }

        Rectangle paneArea = getMapPane().getBounds();
        DirectPosition2D mapPos = ev.getMapPosition();

        double scale = getMapPane().getWorldToScreenTransform().getScaleX();
        double newScale = scale / zoom;

        DirectPosition2D corner = new DirectPosition2D(mapPos.getX() - 0.5d * paneArea.width / newScale, mapPos.getY() + 0.5d
                * paneArea.height / newScale);

        Envelope2D newMapArea = new Envelope2D();
        newMapArea.setFrameFromCenter(mapPos, corner);
        getMapPane().setDisplayArea(newMapArea);
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
        return false;
    }
}
