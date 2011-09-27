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

package org.geotools.swing.event;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;

/**
 * Takes Java AWT mouse events received by a map pane and converts them to 
 * {@code MapMouseEvents} which add world location data. The resulting events are then
 * dispatched to {@code MapMouseListeners} by the methods overriden from the 
 * AWT listener interfaces.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public interface MapMouseEventDispatcher 
        extends MouseListener, MouseMotionListener, MouseWheelListener {

    /**
     * Adds a listener for map pane mouse events.
     *
     * @param listener the new listener
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    boolean addMouseListener(MapMouseListener listener);

    /**
     * Removes the given listener.
     *
     * @param listener the listener to remove
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    boolean removeMouseListener(MapMouseListener listener);

    /**
     * Removes all listeners.
     */
    void removeAllListeners();

    /**
     * Converts an incoming Java AWT mouse event to a {@linkplain MapMouseEvent}.
     * 
     * @param ev the input event
     */
    MapMouseEvent convertEvent(MouseEvent ev);
    
    /**
     * Converts an incoming Java AWT mouse wheel event to a {@linkplain MapMouseEvent}.
     * 
     * @param ev the input event
     */
    MapMouseEvent convertEvent(MouseWheelEvent ev);

}
