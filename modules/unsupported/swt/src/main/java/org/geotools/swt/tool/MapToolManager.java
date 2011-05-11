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

import java.util.HashSet;
import java.util.Set;

import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseListener;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.MouseTrackListener;
import org.eclipse.swt.events.MouseWheelListener;
import org.geotools.swt.SwtMapPane;
import org.geotools.swt.event.MapMouseEvent;
import org.geotools.swt.event.MapMouseListener;
import org.geotools.swt.utils.Messages;

/**
 * Receives mouse events from a {@link SwtMapPane} instance, converts them to
 * {@link MapMouseEvent}s, and sends these to the registered listeners.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @author Michael Bedward
 */
public class MapToolManager implements MouseListener, MouseMoveListener, MouseWheelListener, MouseTrackListener {

    private SwtMapPane mapPane;
    private Set<MapMouseListener> listeners = new HashSet<MapMouseListener>();
    private CursorTool cursorTool;

    /**
     * Constructor
     * 
     * @param pane the map pane that owns this listener
     */
    public MapToolManager( SwtMapPane pane ) {
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
    public boolean setCursorTool( CursorTool tool ) {
        if (tool == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error"));
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
     * Add a listener for JMapPaneMouseEvents
     *
     * @param listener the listener to add
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the tool argument is null
     */
    public boolean addMouseListener( MapMouseListener listener ) {
        if (listener == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error"));
        }

        return listeners.add(listener);
    }

    /**
     * Remove a MapMouseListener from the active listeners
     *
     * @param listener the listener to remove
     * @return true if successful; false otherwise
     * @throws IllegalArgumentException if the tool argument is null
     */
    public boolean removeMouseListener( MapMouseListener listener ) {
        if (listener == null) {
            throw new IllegalArgumentException(Messages.getString("arg_null_error"));
        }
        return listeners.remove(listener);
    }

    public void mouseScrolled( MouseEvent e ) {
        MapMouseEvent ev = convertWheelEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                listener.onMouseWheelMoved(ev);
            }
        }
    }

    public void mouseMove( MouseEvent e ) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                if (isMouseDown) {
                    listener.onMouseDragged(ev);
                } else {
                    listener.onMouseMoved(ev);
                }
            }
        }
    }

    public void mouseDoubleClick( MouseEvent arg0 ) {
        // TODO Auto-generated method stub

    }

    boolean isMouseDown = false;
    public void mouseDown( MouseEvent e ) {
        isMouseDown = true;
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                listener.onMouseClicked(ev);
            }
            for( MapMouseListener listener : listeners ) {
                listener.onMousePressed(ev);
            }
        }
    }

    public void mouseUp( MouseEvent e ) {
        isMouseDown = false;
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                listener.onMouseReleased(ev);
            }
        }
    }

    public void mouseEnter( MouseEvent e ) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                listener.onMouseEntered(ev);
            }
        }
    }

    public void mouseExit( MouseEvent e ) {
        MapMouseEvent ev = convertEvent(e);
        if (ev != null) {
            for( MapMouseListener listener : listeners ) {
                listener.onMouseExited(ev);
            }
        }
    }

    public void mouseHover( MouseEvent arg0 ) {
        // TODO Auto-generated method stub

    }

    private MapMouseEvent convertEvent( MouseEvent e ) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e, false);
        }

        return ev;
    }

    private MapMouseEvent convertWheelEvent( MouseEvent e ) {
        MapMouseEvent ev = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            ev = new MapMouseEvent(mapPane, e, true);
        }

        return ev;
    }

}
