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
import java.awt.event.MouseWheelListener;
import java.util.HashSet;
import java.util.Set;

import javax.swing.event.MouseInputListener;

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
 *
 *
 * @source $URL$
 * @version $Id$
 */
public class MapToolManager implements MouseInputListener, MouseWheelListener {

    private MapPane mapPane;
    private Set<MapMouseListener> listeners = new HashSet<MapMouseListener>();
    private CursorTool cursorTool;

    /**
     * Constructor
     * 
     * @param pane the map pane that owns this listener
     */
    public MapToolManager(MapPane pane) {
        this.mapPane = pane;
    }

    /**
     * Unset the current cursor tool
     */
    public void setNoCursorTool() {
        listeners.remove(cursorTool);
        cursorTool = null;
    }

    /**
     * Set the active cursor tool
     * 
     * @param tool the tool to set
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the tool argument is null
     */
    public boolean setCursorTool(CursorTool tool) {
        if (tool == null) {
            throw new IllegalArgumentException("tool must not be null");
        }

        if (cursorTool != null) {
            listeners.remove(cursorTool);
        }

        cursorTool = tool;
        cursorTool.setMapPane(mapPane);
        return listeners.add(tool);
    }

    /**
     * Get the active cursor tool
     *
     * @return live reference to the active cursor tool or {@code null} if no
     *         tool is active
     */
    public CursorTool getCursorTool() {
        return cursorTool;
    }

    /**
     * Add a listener for MapPaneMouseEvents
     *
     * @param listener the listener to add
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the tool argument is null
     */
    public boolean addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        return listeners.add(listener);
    }

    /**
     * Remove a MapMouseListener from the active listeners
     *
     * @param listener the listener to remove
     * @return true if successful; false otherwise
     */
    public boolean removeMouseListener(MapMouseListener listener) {
        if (listener != null) {
            return listeners.remove(listener);
        } else {
            return false;
        }
    }

    public void mouseClicked(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseClicked(ev);
            }
        }
    }

    public void mousePressed(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMousePressed(ev);
            }
        }
    }

    public void mouseReleased(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseReleased(ev);
            }
        }
    }

    public void mouseEntered(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseEntered(ev);
            }
        }
    }

    public void mouseExited(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseExited(ev);
            }
        }
    }

    public void mouseDragged(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseDragged(ev);
            }
        }
    }

    public void mouseMoved(MouseEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseMoved(ev);
            }
        }
    }

    public void mouseWheelMoved(MouseWheelEvent e) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseWheelMoved(ev);
            }
        }
    }

    private MapMouseEvent convertEvent(MouseEvent e) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e);
        }

        return ev;
    }

    private MapMouseEvent convertEvent(MouseWheelEvent e) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e);
        }

        return ev;
    }

}
