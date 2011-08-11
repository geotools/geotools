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
import org.geotools.renderer.GTRenderer;
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
     * Sets the {@code MapContent} instance containing the layers
     * to display.
     *
     * @param content the map content
     */
    void setMapContent(MapContent content);

    /**
     * Gets the {@code MapConent} instance containing the layers
     * being displayed by this map pane.
     *
     * @return the map content
     */
    MapContent getMapContent();

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
     * Sets the current cursor tool. A {@code null} argument means no
     * current cursor tool.
     *
     * @param tool the tool to set or {@code null}
     */
    void setCursorTool(CursorTool tool);
    
}
