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
 * Implemented by classes that wish to receive MapPaneEvents
 *
 * @author Michael Bedward
 * @since 2.6
 *
 * @source $URL$
 * @version $Id$
 */
public interface MapPaneListener {

    /**
     * Called by the map pane when a new map context has been set
     *
     * @param ev the event
     */
    public void onNewContext(MapPaneEvent ev);

    /**
     * Called by the map pane when a new renderer has been set
     *
     * @param ev the event
     */
    public void onNewRenderer(MapPaneEvent ev);

    /**
     * Called by the map pane when it has been resized
     *
     * @param ev the event
     */
    public void onResized(MapPaneEvent ev);

    /**
     * Called by the map pane when its display area has been
     * changed e.g. by zooming or panning
     *
     * @param ev the event
     */
    public void onDisplayAreaChanged(MapPaneEvent ev);

    /**
     * Called by the map pane when it has started rendering features
     * 
     * @param ev the event
     */
    public void onRenderingStarted(MapPaneEvent ev);

    /**
     * Called by the map pane when it has stopped rendering features
     *
     * @param ev the event
     */
    public void onRenderingStopped(MapPaneEvent ev);

    /**
     * Called by the map pane when it is rendering features. The
     * event will be carrying data: a floating point value between
     * 0 and 1 indicating rendering progress.
     * 
     * @param ev the event
     */
    public void onRenderingProgress(MapPaneEvent ev);

}
