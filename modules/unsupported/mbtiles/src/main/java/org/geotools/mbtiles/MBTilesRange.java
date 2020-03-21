/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

/** Identifies a flat set of tiles in a MBTiles file, a rectangle in a given zoom level */
class MBTilesRange extends RectangleLong {

    long z;

    public MBTilesRange(long z, long minX, long maxX, long minY, long maxY) {
        super(minX, maxX, minY, maxY);
        this.z = z;
    }

    public MBTilesRange(long z) {
        this.z = z;
    }
}
