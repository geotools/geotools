/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2003-2011, Open Source Geospatial Foundation (OSGeo)
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


// J2SE dependencies
import java.util.EventListener;

/**
 * The listener that's notified when some {@linkplain MapLayer layer} property changes.
 *
 * @author Andrea Aime
 *
 * @see org.geotools.map.MapLayer
 * @see MapLayerEvent
 *
 *
 * @source $URL$
 */
public interface MapLayerListener extends EventListener {
    /**
     * Invoked when some property of this layer has changed. May be data,  style, title,
     * visibility.
     *
     * @param event encapsulating the event information
     */
    void layerChanged(MapLayerEvent event);

    /**
     * Invoked when the component has been made visible.
     *
     * @param event encapsulating the event information
     */
    void layerShown(MapLayerEvent event);

    /**
     * nvoked when the component has been made invisible.
     *
     * @param event encapsulating the event information
     */
    void layerHidden(MapLayerEvent event);

    /**
     * Invoked when the component has been set as selected.
     *
     * @param event encapsulating the event information
     */
    void layerSelected(MapLayerEvent event);

    /**
     * Invoked when the component has been set as not selected.
     *
     * @param event encapsulating the event information
     */
    void layerDeselected(MapLayerEvent event);
    
    /**
     * Invoked when the layer is scheduled for disposal to give listeners
     * the chance to finish or cancel any tasks involving the layer.
     * 
     * @param event encapsulating the event information
     */
    void layerPreDispose(MapLayerEvent event);
}
