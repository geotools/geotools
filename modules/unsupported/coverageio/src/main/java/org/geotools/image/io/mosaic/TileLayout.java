/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;


/**
 * The layout of tiles in a {@link TileManager}. Used by {@link TileBuilder} for creating tiles
 * in some commonly used layout.
 *
 * @since 2.5
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 *
 * @see TileBuilder
 */
public enum TileLayout {
    /**
     * A generic layout with heteregenous tile size or geographic area.
     * This enum is returned when no other enum fit.
     */
    GENERIC,

    /**
     * All tiles have the same width and height in pixels. Consequently the levels at the finest
     * resolution have more tiles than levels at lower resolution. In other words, the tiles at
     * the finest resolution cover smaller geographic area. This is the most efficient tile layout.
     */
    CONSTANT_TILE_SIZE,

    /**
     * All tiles cover the same geographic area. Consequently, tiles at the finest resolution may
     * be very big while tiles at lower resolutions are smaller. This is the simpliest tile layout,
     * easy to manage but inefficient. It is provided for testing purpose and compatibility with
     * some external softwares using such layout.
     */
    CONSTANT_GEOGRAPHIC_AREA
}
