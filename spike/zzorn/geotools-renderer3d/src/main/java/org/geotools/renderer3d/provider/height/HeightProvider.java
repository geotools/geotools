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
package org.geotools.renderer3d.provider.height;

import org.geotools.renderer3d.utils.BoundingRectangle;

/**
 * Something that provides ground height data for requested areas.
 * The height data may be loaded in the background in a separate thread.
 *
 * @author Hans Häggström
 */
public interface HeightProvider
{
    /**
     * Start creating or loading the height data of the specified size for the specified world area.
     * Provide the height data to the specified listener when ready.
     *
     * @param area            the world area to create the texture for. TODO: What units are the coordinates given in?
     * @param heightGridSizeX size of the grid along x axis.
     * @param heightGridSizeY size of the grid along y axis.
     * @param heightListener  a listener that should be called back when the height data is ready.
     */
    void requestHeightData( BoundingRectangle area,
                            int heightGridSizeX,
                            int heightGridSizeY,
                            HeightListener heightListener );

}
