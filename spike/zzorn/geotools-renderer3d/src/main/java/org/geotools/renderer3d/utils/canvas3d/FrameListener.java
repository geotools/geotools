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
package org.geotools.renderer3d.utils.canvas3d;

/**
 * A listener that is called for each frame that is rendered.
 * The listener is called in the swing thread, so it can update the swing UI.
 *
 * @author Hans Häggström
 */
public interface FrameListener
{
    /**
     * Called each frame, in the swing thread context.
     *
     * @param secondsSinceLastFrame number of seconds since the last frame, or a negative value if this is the first frame.
     */
    void onFrame( double secondsSinceLastFrame );
}
