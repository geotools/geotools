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

import java.awt.geom.AffineTransform;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.map.MapContent;
import org.geotools.swing.event.MapMouseEventDispatcher;
import org.geotools.swing.event.MapMouseListener;
import org.geotools.swing.event.MapPaneListener;
import org.geotools.swing.tool.CursorTool;
import org.opengis.geometry.Envelope;

/**
 * Defines the core map pane methods.
 *
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public interface MapPane {

    /**
     * Gets the {@code MapConent} instance containing the layers
     * being displayed by this map pane.
     *
     * @return the map content
     */
    MapContent getMapContent();

    /**
     * Sets the {@code MapContent} instance containing the layers
     * to display.
     *
     * @param content the map content
     */
    void setMapContent(MapContent content);
    
    /**
     * Gets the current mouse event dispatcher which is responsible for converting
     * each input Java AWT mouse event into a {@linkplain org.geotools.swing.event.MapMouseEvent}
     * and forwarding it to each {@linkplain MapMouseListener}.
     * 
     * @return the current mouse event dispatcher (may be {@code null})
     */
    MapMouseEventDispatcher getMouseEventDispatcher();
    
    /**
     * Replaces the current mouse event dispatcher. All current listeners will
     * be removed. It is the responsibility of the client to add them to the new 
     * dispatcher if this is desired.
     * 
     * @param dispatcher the new dispatcher (may be {@code null})
     */
    void setMouseEventDispatcher(MapMouseEventDispatcher dispatcher);

    /**
     * Gets the current display area in world coordinates. This is a
     * short-cut for {@code mapPane.getMapContent().getViewport().getBounds()}.
     * If a MapContent object has not yet been associated with the map pane, an
     * empty {@code ReferencedEnvelope} is returned.
     *
     * @return the display area in world coordinates
     */
    ReferencedEnvelope getDisplayArea();

    /**
     * Sets the area to display in world units.
     * 
     * @param the new display area
     * @throws IllegalArgumentException if {@code envelope} is {@code null]
     */
    void setDisplayArea(Envelope envelope);
    
    /**
     * Reset the map area to include the full extent of all
     * layers and redraw the display
     */
    void reset();
    
    /**
     * Gets the screen to world coordinate transform. This is a short-cut for
     * {@code mapPane.getMapContent().getViewport().getScreenToWorld()}.
     *
     * @return the screen to world coordinate transform
     */
    AffineTransform getScreenToWorldTransform();

    /**
     * Gets the world to screen coordinate transform. This is a short-cut for
     * {@code mapPane.getMapContent().getViewport().getWorldToScreen()}.
     * <p>
     * The returned {@code AffineTransform} can be used to determine the 
     * current drawing scale...
     * <pre><code>
     * double scale = mapPane.getWorldToScreenTransform().getScaleX();
     * </code></pre>
     *
     * @return the world to screen coordinate transform
     */
    AffineTransform getWorldToScreenTransform();

    /**
     * Adds a listener to receive {@link org.geotools.swing.event.MapPaneEvent}s.
     *
     * @param listener the listener to add
     * 
     * @throws IllegalArgumentException if {@code listener} is {@code null}
     */
    void addMapPaneListener(MapPaneListener listener);
    
    /**
     * Removes the specified listener.
     *
     * @param listener the listener to remove
     */
    void removeMapPaneListener(MapPaneListener listener);

    /**
     * Registers an object that wishes to receive {@code MapMouseEvent}s
     * such as a {@linkplain StatusBar}.
     *
     * @param listener the listener to add
     * @throws IllegalArgumentException if listener is null
     * @see MapMouseListener
     */
    void addMouseListener(MapMouseListener listener);

    /**
     * Removes the specified listener.
     *
     * @param listener the listener to remove
     */
    void removeMouseListener(MapMouseListener listener);
    
    /**
     * Gets the current cursor tool.
     * 
     * @return the current cursor tool (may be {@code null})
     */
    CursorTool getCursorTool();

    /**
     * Sets the current cursor tool.
     *
     * @param tool the tool; or {@code null} for no cursor tool
     */
    void setCursorTool(CursorTool tool);
    
    /**
     * Moves the image(s) displayed by the map pane from the current
     * origin (x,y) (device pixels) to (x+dx, y+dy).
     * 
     * @param dx the x offset in pixels
     * @param dy the y offset in pixels.
     */
    void moveImage(int dx, int dy);
}
