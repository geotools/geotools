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

package org.geotools.swing;

/**
 *
 * @author Michael Bedward
 * @since 2.7
 *
 * @source $URL$
 * @version $Id$
 */
public interface RenderingExecutorListener {
    
    /**
     * Called by the executor when rendering has started.
     */
    void onRenderingStarted(RenderingExecutorEvent ev);
    
    /**
     * Called by the executor when rendering has been completed.
     */
    void onRenderingCompleted(RenderingExecutorEvent ev);
    
    /**
     * Called by the executor when rendering failed for some reason.
     */
    void onRenderingFailed(RenderingExecutorEvent ev);
    
}
