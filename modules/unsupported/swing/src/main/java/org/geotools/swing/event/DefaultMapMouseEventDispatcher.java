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
import java.awt.event.MouseWheelEvent;
import java.util.ArrayList;
import java.util.List;

import org.geotools.swing.MapPane;

/**
 * Receives mouse events from a MapPane instance, converts them to
 * MapPaneMouseEvents, and sends these to the active map pane 
 * tools.
 * 
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public class DefaultMapMouseEventDispatcher implements MapMouseEventDispatcher {
    
    private MapPane mapPane;
    private List<MapMouseListener> listeners;

    /**
     * Creates a new manager instance to work with the specified map pane.
     * 
     * @param mapPane the map pane
     */
    public DefaultMapMouseEventDispatcher(MapPane mapPane) {
        this.mapPane = mapPane;
        this.listeners = new ArrayList<MapMouseListener>();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean addMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        if (!listeners.contains(listener)) {
            return listeners.add(listener);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean removeMouseListener(MapMouseListener listener) {
        if (listener == null) {
            throw new IllegalArgumentException("listener must not be null");
        }

        return listeners.remove(listener);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeAllListeners() {
        listeners.clear();
    }

    /**
     * Receives a mouse clicked event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseClicked(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseClicked(mapEv);
            }
        }
    }

    /**
     * Receives a mouse pressed event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mousePressed(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMousePressed(mapEv);
            }
        }
    }

    /**
     * Receives a mouse released event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseReleased(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseReleased(mapEv);
            }
        }
    }

    /**
     * Receives a mouse entered event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseEntered(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseEntered(mapEv);
            }
        }
    }

    /**
     * Receives a mouse exited event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseExited(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseExited(mapEv);
            }
        }
    }

    /**
     * Receives a mouse dragged event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseDragged(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseDragged(mapEv);
            }
        }
    }

    /**
     * Receives a mouse moved event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseMoved(MouseEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseMoved(mapEv);
            }
        }
    }

    /**
     * Receives a mouse wheel event and sends a derived {@linkplain MapMouseEvent}
     * to listeners.
     * 
     * @param ev the input event
     */
    @Override
    public void mouseWheelMoved(MouseWheelEvent ev) {
        MapMouseEvent mapEv = convertEvent(ev);
        if (mapEv != null) {
            for (MapMouseListener listener : listeners) {
                listener.onMouseWheelMoved(mapEv);
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapMouseEvent convertEvent(MouseEvent ev) {
        MapMouseEvent mapEv = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            mapEv = new MapMouseEvent(mapPane, ev);
        }

        return mapEv;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MapMouseEvent convertEvent(MouseWheelEvent ev) {
        MapMouseEvent mapEv = null;
        if (mapPane.getScreenToWorldTransform() != null) {
            mapEv = new MapMouseEvent(mapPane, ev);
        }

        return mapEv;
    }

}
