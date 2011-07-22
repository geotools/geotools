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

package org.geotools.swing.event;

/**
 * An event class for map pane events.
 *
 * @author Michael Bedward
 * @since 2.6
 * @source $URL$
 * @version $Id$
 */
public interface MapPaneListener {

    /**
     * Called when a new {@code MapContent} instance has been set for 
     * the map pane.
     *
     * @param ev the event
     */
    void onNewContent(MapPaneEvent ev);

    /**
     * Called when a new renderer has been set for the map pane.
     *
     * @param ev the event
     */
    void onNewRenderer(MapPaneEvent ev);

    /**
     * Called the display area (world bounds) has changed.
     *
     * @param ev the event
     */
    void onDisplayAreaChanged(MapPaneEvent ev);

    /**
     * Called when a rendering task has started.
     * 
     * @param ev the event
     */
    void onRenderingStarted(MapPaneEvent ev);

    /**
     * Called when a rendering task has stopped. This includes
     * normal completion, cancellation or failure.
     *
     * @param ev the event
     */
    void onRenderingStopped(MapPaneEvent ev);

}
