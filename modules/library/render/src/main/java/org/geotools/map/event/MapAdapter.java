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

package org.geotools.map.event;

/**
 * An abstract adapter class to receive events about map bounds and layer changes. 
 * All of the methods are empty. This class exists as convenience for creating listener
 * objects.
 *
 * @author Michael Bedward
 * @since 8.0
 *
 * @source $URL$
 * @version $Id$
 */
public abstract class MapAdapter implements MapBoundsListener, MapLayerListListener {
    
    public void mapBoundsChanged(MapBoundsEvent event) { }

    public void layerAdded(MapLayerListEvent event) { }

    public void layerRemoved(MapLayerListEvent event) { }

    public void layerChanged(MapLayerListEvent event) { }

    public void layerMoved(MapLayerListEvent event) { }

    public void layerPreDispose(MapLayerListEvent event) { }
    
}
