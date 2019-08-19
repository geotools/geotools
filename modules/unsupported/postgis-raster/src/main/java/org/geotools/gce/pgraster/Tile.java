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

import java.awt.image.BufferedImage;
import org.locationtech.jts.geom.Envelope;

/** A tile from a row in a raster table. */
class Tile {

    static final Tile NULL = new Tile(null, null);

    /** The decoded tile image. */
    final BufferedImage image;

    /** The tiles world bounds. */
    final Envelope bounds;

    Tile(BufferedImage image, Envelope bounds) {
        this.image = image;
        this.bounds = bounds;
    }
}
