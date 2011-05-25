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
package org.geotools.map.event;

import java.util.EventListener;


/**
 * DOCUMENT ME!
 *
 * @author wolf
 *
 * @source $URL$
 */
public interface MapLayerListListener extends EventListener {
    /**
     * Triggered when a new layer is added to the MapContext
     *
     * @param event encapsulating the event information
     */
    public void layerAdded(MapLayerListEvent event);

    /**
     * Triggered when a layer is removed from the MapContext
     *
     * @param event encapsulating the event information
     */
    public void layerRemoved(MapLayerListEvent event);

    /**
     * Triggered when something in a layer changed (data, style, title)
     *
     * @param event encapsulating the event information
     */
    public void layerChanged(MapLayerListEvent event);

    /**
     * Triggered when a group of layers chenges position in the layer list
     *
     * @param event encapsulating the event information
     */
    public void layerMoved(MapLayerListEvent event);
}
