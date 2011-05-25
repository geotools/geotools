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

import java.awt.Cursor;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.Point2D;
import java.util.ResourceBundle;

import javax.swing.ImageIcon;

import org.geotools.geometry.DirectPosition2D;
import org.geotools.geometry.Envelope2D;
import org.geotools.swing.event.MapMouseEvent;

/**
 * A zoom-in tool for JMapPane.
 * <p>
 * For mouse clicks, the display will be zoomed-in such that the 
 * map centre is the position of the mouse click and the map
 * width and height are calculated as:
 * <pre>   {@code len = len.old / z} </pre>
 * where {@code z} is the linear zoom increment (>= 1.0)
 * <p>
 * The tool also responds to the user drawing a box on the map mapPane with
 * mouse click-and-drag to define the zoomed-in area.
 * 
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public class ZoomInTool extends AbstractZoomTool {
    
    private static final ResourceBundle stringRes = ResourceBundle.getBundle("org/geotools/swing/Text");

    /** Tool name */
    public static final String TOOL_NAME = stringRes.getString("tool_name_zoom_in");
    /** Tool tip text */
    public static final String TOOL_TIP = stringRes.getString("tool_tip_zoom_in");
    /** Cursor */
    public static final String CURSOR_IMAGE = "/org/geotools/swing/icons/mActionZoomIn.png";
    /** Cursor hotspot coordinates */
    public static final Point CURSOR_HOTSPOT = new Point(14, 9);
    /** Icon for the control */
    public static final String ICON_IMAGE = "/org/geotools/swing/icons/mActionZoomIn.png";
    
    private Cursor cursor;
    
    private Point2D startDragPos;
    private boolean dragged;
    
    /**
     * Constructor
     */
    public ZoomInTool() {
        Toolkit tk = Toolkit.getDefaultToolkit();
        ImageIcon imgIcon = new ImageIcon(getClass().getResource(CURSOR_IMAGE));
        cursor = tk.createCustomCursor(imgIcon.getImage(), CURSOR_HOTSPOT, TOOL_NAME);
        
        startDragPos = new DirectPosition2D();
        dragged = false;
    }
    
    /**
     * Zoom in by the currently set increment, with the map
     * centred at the location (in world coords) of the mouse
     * click
     * 
     * @param e map mapPane mouse event
     */
    @Override
    public void onMouseClicked(MapMouseEvent e) {
        Rectangle paneArea = getMapPane().getVisibleRect();
        DirectPosition2D mapPos = e.getMapPosition();

        double scale = getMapPane().getWorldToScreenTransform().getScaleX();
        double newScale = scale * zoom;

        DirectPosition2D corner = new DirectPosition2D(
                mapPos.getX() - 0.5d * paneArea.getWidth() / newScale,
                mapPos.getY() + 0.5d * paneArea.getHeight() / newScale);
        
        Envelope2D newMapArea = new Envelope2D();
        newMapArea.setFrameFromCenter(mapPos, corner);
        getMapPane().setDisplayArea(newMapArea);
    }
    
    /**
     * Records the map position of the mouse event in case this
     * button press is the beginning of a mouse drag
     *
     * @param ev the mouse event
     */
    @Override
    public void onMousePressed(MapMouseEvent ev) {
        startDragPos = new DirectPosition2D();
        startDragPos.setLocation(ev.getMapPosition());
    }

    /**
     * Records that the mouse is being dragged
     *
     * @param ev the mouse event
     */
    @Override
    public void onMouseDragged(MapMouseEvent ev) {
        dragged = true;
    }

    /**
     * If the mouse was dragged, determines the bounds of the
     * box that the user defined and passes this to the mapPane's
     * {@link org.geotools.swing.JMapPane#setDisplayArea(org.opengis.geometry.Envelope) }
     * method
     *
     * @param ev the mouse event
     */
    @Override
    public void onMouseReleased(MapMouseEvent ev) {
        if (dragged && !ev.getPoint().equals(startDragPos)) {
            Envelope2D env = new Envelope2D();
            env.setFrameFromDiagonal(startDragPos, ev.getMapPosition());
            dragged = false;
            getMapPane().setDisplayArea(env);
        }
    }

    /**
     * Get the mouse cursor for this tool
     */
    @Override
    public Cursor getCursor() {
        return cursor;
    }
    
    /**
     * Returns true to indicate that this tool draws a box
     * on the map display when the mouse is being dragged to
     * show the zoom-in area
     */
    @Override
    public boolean drawDragBox() {
        return true;
    }
}
