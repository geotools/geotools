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

import java.util.EventObject;

import org.geotools.map.MapContext;
import org.geotools.map.MapLayer;


/**
 * Event object used to report changes in the list of layers managed by a MapContext
 *
 * @author wolf
 * @source $URL$
 */
public class MapLayerListEvent extends EventObject {
    /** Holds value of property layer. */
    private MapLayer layer;

    /** Holds value of property fromIndex. */
    private int fromIndex;

    /** Holds value of property toIndex. */
    private int toIndex;

    /** Holds value of property mapLayerEvent. */
    private MapLayerEvent mapLayerEvent;

    /**
     * Creates a new instance of MapLayerListEvent
     *
     * @param source DOCUMENT ME!
     * @param layer DOCUMENT ME!
     * @param fromIndex DOCUMENT ME!
     * @param toIndex DOCUMENT ME!
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int fromIndex, int toIndex) {
        super(source);
        this.layer = layer;
        this.fromIndex = fromIndex;
        this.toIndex = toIndex;
    }

    /**
     * Creates a new instance of MapLayerListEvent
     *
     * @param source DOCUMENT ME!
     * @param layer DOCUMENT ME!
     * @param position DOCUMENT ME!
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int position) {
        super(source);
        this.layer = layer;
        this.fromIndex = position;
        this.toIndex = position;
    }

    /**
     * Creates a new instance of MapLayerListEvent
     *
     * @param source DOCUMENT ME!
     * @param layer DOCUMENT ME!
     * @param position DOCUMENT ME!
     * @param mapLayerEvent DOCUMENT ME!
     */
    public MapLayerListEvent(MapContext source, MapLayer layer, int position,
        MapLayerEvent mapLayerEvent) {
        this(source, layer, position);
        this.mapLayerEvent = mapLayerEvent;
    }

    /**
     * Returns the layer involved in the change
     *
     * @return Value of property layer.
     */
    public MapLayer getLayer() {
        return this.layer;
    }

    /**
     * Returns the index of the first layer involved in the change
     *
     * @return The old index of the layer. -1 will be returned if the layer was not in the
     *         MapContext
     */
    public int getFromIndex() {
        return this.fromIndex;
    }

    /**
     * Returns the index of the last layer involved in the change
     *
     * @return The old index of the layer. -1 will be returned if the layer is no more in the
     *         MapContext
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
