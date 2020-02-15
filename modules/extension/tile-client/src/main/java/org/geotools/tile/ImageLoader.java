/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015-2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.tile;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * The ImageLoader is a delegate used by a tile to load the image. Its purpose is to provide a tile
 * image cache opportunity to speed up image loading and prevent tile to be repeatedly loaded off
 * the network.
 *
 * @author Ugo Taddei
 * @since 12
 */
public interface ImageLoader {

    /**
     * Loads an image for the given tile.
     *
     * @return an image
     */
    BufferedImage loadImageTileImage(Tile tile) throws IOException;
}
