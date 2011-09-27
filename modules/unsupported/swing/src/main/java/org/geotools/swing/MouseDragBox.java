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
package org.geotools.swing;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.event.MouseInputAdapter;

/**
 * Draws a box on the parent component (e.g. JMapPane) as the mouse 
 * is dragged.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 * 
 */
public class MouseDragBox extends MouseInputAdapter {
    
    private final JComponent parentComponent;
    private Point startPos;
    private Rectangle rect;
    private boolean dragged;
    private boolean enabled;
    private Graphics2D graphics;

    /**
     * Creates a new instance to work with the given component.
     * 
     * @param component the component on which the box will be drawn
     */
    MouseDragBox(JComponent component) {
        parentComponent = component;
        rect = new Rectangle();
        dragged = false;
        enabled = false;
    }

    /**
     * Enables or disables the drag box. When enabled, the box 
     * is drawn on mouse dragging.
     * 
     * @param state {@code true} to enable; {@code false} to disable
     */
    void setEnabled(boolean state) {
        enabled = state;
    }

    /**
     * If the box is enabled, records the start position for subsequent
     * drawing as the mouse is dragged.
     * 
     * @param ev input mouse event
     */
    @Override
    public void mousePressed(MouseEvent ev) {
        startPos = new Point(ev.getPoint());
    }

    /**
     * If the box is enabled, draws the box with the diagonal running from the
     * start position to the current mouse position. 
     * 
     * @param ev input mouse event
     */
    @Override
    public void mouseDragged(MouseEvent ev) {
        if (enabled) {
            ensureGraphics();
            if (dragged) {
                graphics.drawRect(rect.x, rect.y, rect.width, rect.height);
            }
            rect.setFrameFromDiagonal(startPos, ev.getPoint());
            graphics.drawRect(rect.x, rect.y, rect.width, rect.height);
            dragged = true;
        }
    }

    /**
     * If the box is enabled, removes the final box.
     * 
     * @param ev the input mouse event
     */
    @Override
    public void mouseReleased(MouseEvent ev) {
        if (dragged) {
            ensureGraphics();
            graphics.drawRect(rect.x, rect.y, rect.width, rect.height);
            dragged = false;
            
            graphics.dispose();
            graphics = null;
        }
    }

    /**
     * Creates and initializes the graphics object if required.
     */
    private void ensureGraphics() {
        if (graphics == null) {
            graphics = (Graphics2D) parentComponent.getGraphics().create();
            graphics.setColor(Color.WHITE);
            graphics.setXORMode(Color.RED);
        }
    }
    
}
