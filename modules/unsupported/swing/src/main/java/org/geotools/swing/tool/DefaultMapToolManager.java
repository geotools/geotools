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

import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.util.HashSet;
import java.util.Set;

import org.geotools.swing.MapPane;
import org.geotools.swing.event.MapMouseEvent;
import org.geotools.swing.event.MapMouseListener;

/**
 * Receives mouse events from a MapPane instance, converts them to
 * MapPaneMouseEvents, and sends these to the active map pane 
 * tools.
 * 
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public class DefaultMapToolManager implements MapToolManager {
    
    private MapPane mapPane;
    private Set<MapMouseListener> listeners = new HashSet<MapMouseListener>();
    private CursorTool cursorTool;

    /**
     * Creates a new manager instance to work with the specified map pane.
     * 
     * @param mapPane the map pane
     */
    public DefaultMapToolManager(MapPane mapPane) {
        this.mapPane = mapPane;
    }

    /**
     * Disables the cursor tool. This is equivalent to 
     * {@code setCursorTool( null )}.
     */
    public void setNoCursorTool() {
        setCursorTool(null);
    }

    /**
     * Sets the cursor tool.
     * 
     * @param tool the tool to set or {@code null} for no tool
     * 
     * @return true if successful; false otherwise
     */
    @Override
    public boolean setCursorTool(CursorTool tool) {
        if (cursorTool != null) {
            listeners.remove(cursorTool);
        }

        cursorTool = tool;
        if (cursorTool != null) {
            cursorTool.setMapPane(mapPane);
            return listeners.add(tool);
        } else {
            return true;
        }
    }

    /**
     * Get the active cursor tool
     *
     * @return live reference to the active cursor tool or {@code null} if no
     *         tool is active
     */
    @Override
    public CursorTool getCursorTool() {
        return cursorTool;
    }

    /**
     * Adds a listener for map pane mouse events.
     *
     * @param listener the new listener
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    @Override
    public boolean addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        return listeners.add(listener);
    }

    /**
     * Removes the given listener.
     *
     * @param listener the listener to remove
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the {@code listener} is {@code null}
     */
    @Override
    public boolean removeMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        return listeners.remove(listener);
    }

    /**
     * Receives a mouse clicked event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseClicked(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseClicked(ev);
            }
        }
    }

    /**
     * Receives a mouse pressed event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mousePressed(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMousePressed(ev);
            }
        }
    }

    /**
     * Receives a mouse released event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseReleased(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseReleased(ev);
            }
        }
    }

    /**
     * Receives a mouse entered event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseEntered(ev);
            }
        }
    }

    /**
     * Receives a mouse exited event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseExited(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseExited(ev);
            }
        }
    }

    /**
     * Receives a mouse dragged event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseDragged(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseDragged(ev);
            }
        }
    }

    /**
     * Receives a mouse moved event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseMoved(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseMoved(ev);
            }
        }
    }

    /**
     * Receives a mouse wheel event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param e the input event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseWheelMoved(ev);
            }
        }
    }

    /**
     * Converts an input mouse event to a corresponding map pane mouse event.
     * 
     * @param e the input event
     * @return a new map pane mouse event
     */
    private MapMouseEvent convertEvent(MouseEvent e) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e);
        }

        return ev;
    }

    /**
     * Converts an input mouse wheel event to a corresponding map pane mouse event.
     * 
     * @param e the input event
     * @return a new map pane mouse event
     */
    private MapMouseEvent convertEvent(MouseWheelEvent e) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e);
        }

        return ev;
    }

}
