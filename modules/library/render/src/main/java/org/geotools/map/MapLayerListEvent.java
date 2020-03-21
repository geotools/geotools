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
package org.geotools.map;

import java.util.EventObject;

/**
 * Event object used to report changes in the list of layers managed by a MapContext
 *
 * @author wolf
 */
public class MapLayerListEvent extends EventObject {
    private Layer layer;

    /** Holds value of property fromIndex. */
    private int fromIndex;

    /** Holds value of property toIndex. */
    private int toIndex;

    /** Holds value of property mapLayerEvent. */
    private MapLayerEvent mapLayerEvent;

    /** Creates a new instance of MapLayerListEvent */
    public MapLayerListEvent(MapContent source, Layer layer, int fromIndex, int toIndex) {
        super(source);
        this.layer = layer;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /**
     * Creates a new instance of MapLayerListEvent
     *
     * @param source Map issuing the event
     * @param layer Layer being reported against; may be null
     * @param position index modified in layer list
     */
    public MapLayerListEvent(MapContent source, Layer layer, int position) {
        super(source);
        this.layer = layer;
        this.fromIndex = position;
        this.toIndex = position;
    }

    public MapLayerListEvent(
            MapContent map, Layer element, int index, MapLayerEvent mapLayerEvent) {
        super(map);
        this.layer = element;
        this.fromIndex = index;
        this.toIndex = index;
        this.mapLayerEvent = mapLayerEvent;
    }

    /** Return the layer involved in the change. */
    public Layer getLayer() {
        return layer;
    }

    /**
     * Returns the index of the first layer involved in the change
     *
     * @return The old index of the layer. -1 will be returned if the layer was not in the
     *     MapContext
     */
    public int getFromIndex() {
        return this.fromIndex;
    }

    /**
     * Returns the index of the last layer involved in the change
     *
     * @return The old index of the layer. -1 will be returned if the layer is no more in the
     *     MapContext
     */
    public int getToIndex() {
        return this.toIndex;
    }

    /**
     * Returns the map layer event that originated this layer list event
     *
     * @return Value of property mapLayerEvent.
     */
    public MapLayerEvent getMapLayerEvent() {
        return this.mapLayerEvent;
    }
}
