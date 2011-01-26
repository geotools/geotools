/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer3d;

import com.jme.scene.Spatial;
import org.geotools.map.MapContext;
import org.geotools.renderer3d.navigationgestures.NavigationGesture;
import org.geotools.renderer3d.utils.canvas3d.FrameListener;

import java.awt.Component;

/**
 * A 3D map renderer.
 *
 * @author Hans Häggström
 */
public interface Renderer3D
{

    /**
     * @return the map data to render in the 3D view.
     */
    MapContext getMapContext();

    /**
     * @param mapContext the map data to render in the 3D view.
     */
    void setMapContext( MapContext mapContext );

    /**
     * @return the 3D view UI component.
     */
    Component get3DView();

    /**
     * @return the 3D scenegraph node containing the terrain.
     */
    Spatial get3DNode();

    /**
     * Adds the specified NavigationGesture.
     *
     * @param addedNavigationGesture should not be null or already added.
     */
    void addNavigationGesture( NavigationGesture addedNavigationGesture );

    /**
     * Removes the specified NavigationGesture.
     *
     * @param removedNavigationGesture should not be null, and should be present.
     */
    void removeNavigationGesture( NavigationGesture removedNavigationGesture );

    /**
     * Removes all current navigation gestures.
     * Useful if you want to remove the default gestures, in order to add your own custom ones.
     */
    void removeAllNavigationGestures();

    /**
     * Adds the specified FrameListener.  The listener is called after each frame is rendered in the swing thread.
     *
     * @param addedFrameListener should not be null or already added.
     */
    void addFrameListener( FrameListener addedFrameListener );

    /**
     * Removes the specified FrameListener.
     *
     * @param removedFrameListener should not be null.
     *
     * @return true if the listener was found and removed, false if it was not found.
     */
    boolean removeFrameListener( FrameListener removedFrameListener );
}
