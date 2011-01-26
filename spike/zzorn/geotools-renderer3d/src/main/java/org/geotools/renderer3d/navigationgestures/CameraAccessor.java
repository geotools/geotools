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
package org.geotools.renderer3d.navigationgestures;

import com.jme.renderer.Camera;

/**
 * An interface that allows accessing the camera for a 3D view.
 *
 * @author Hans Häggström
 */
public interface CameraAccessor
{
    /**
     * @return the camera for a 3D view.  Can be modified by calling camera modifying methods.
     */
    Camera getCamera();
}
