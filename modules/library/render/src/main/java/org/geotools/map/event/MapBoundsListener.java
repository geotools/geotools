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


// J2SE dependencies
import java.util.EventListener;


/**
 * The listener that's notified when a bounding box changes its area of interest.
 *
 * @author Andrea Aime
 * @source $URL$
 * @version $Id$
 *
 * @see AreaOfInterestEvent
 */
public interface MapBoundsListener extends EventListener {
    /**
     * Invoked when the area of interest or the coordinate system changes
     *
     * @param event The change event.
     */
    void mapBoundsChanged(MapBoundsEvent event);
}
