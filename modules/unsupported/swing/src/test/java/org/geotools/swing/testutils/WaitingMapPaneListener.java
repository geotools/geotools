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

package org.geotools.swing.testutils;


import org.geotools.swing.event.MapPaneEvent;
import org.geotools.swing.event.MapPaneListener;


/**
 * A MapPaneListener that can be set to expect specified events
 * and test if they are received.
 * 
 * @author Michael Bedward
 * @since 8.0
 * @source $URL$
 * @version $Id$
 */
public class WaitingMapPaneListener extends WaitingListener<MapPaneEvent, MapPaneEvent.Type>
        implements MapPaneListener {

    public WaitingMapPaneListener() {
        super(MapPaneEvent.Type.values().length);
    }
    
    @Override
    public void onNewMapContent(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.NEW_MAPCONTENT.ordinal(), ev);
    }

    @Override
    public void onDisplayAreaChanged(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.DISPLAY_AREA_CHANGED.ordinal(), ev);
    }

    @Override
    public void onRenderingStarted(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STARTED.ordinal(), ev);
    }

    @Override
    public void onRenderingStopped(MapPaneEvent ev) {
        catchEvent(MapPaneEvent.Type.RENDERING_STOPPED.ordinal(), ev);
    }
    
}
