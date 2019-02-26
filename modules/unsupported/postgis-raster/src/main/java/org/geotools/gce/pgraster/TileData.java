/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.pgraster;

import org.locationtech.jts.geom.Envelope;

/** Raw data for a {@link Tile}. */
class TileData {

    /** Raw bytes for the tile. */
    final byte[] bytes;

    /** World bounds of the tile. */
    final Envelope bounds;

    TileData(byte[] bytes, Envelope bounds) {
        this.bytes = bytes;
        this.bounds = bounds;
    }
}
