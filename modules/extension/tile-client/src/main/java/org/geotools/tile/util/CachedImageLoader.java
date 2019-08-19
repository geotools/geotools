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
package org.geotools.tile.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import org.geotools.image.io.ImageIOExt;
import org.geotools.tile.ImageLoader;
import org.geotools.tile.Tile;
import org.geotools.util.logging.Logging;

/**
 * The CachedImageLoader is a simple ImageLoader that uses your disk as a cache for tiles. You can
 * plug this implementation into a Tile object. Note that the TileService also has a cache of its
 * own, but for caching tiles, not necessarily their images.
 *
 * <p>
 *
 * <p>Image loading is an important performance factor to tile clients. Tests have shown that image
 * loading is more important than image rendering. The risk is, however, to fill your disk with tile
 * images, so make sure to empty the directory from time to time. Also note that some tile service
 * may not allow you to save tile locally. If you do so, you might be breaching licenses. So, be
 * nice.
 *
 * @author Ugo Taddei
 * @since 12
 */
public class CachedImageLoader implements ImageLoader {

    private static final Logger LOGGER = Logging.getLogger(CachedImageLoader.class);

    private final File cacheDirectory;

    public CachedImageLoader(File cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }

    @Override
    public BufferedImage loadImageTileImage(Tile tile) throws IOException {

        BufferedImage img = null;

        File imgFile = new File(this.cacheDirectory, tile.getId() + ".png");
        if (imgFile.exists()) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Found image in cache for '"
                                + tile.getId()
                                + "' at "
                                + imgFile.getAbsolutePath());
            }
            img = ImageIOExt.readBufferedImage(imgFile);

        } else {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine(
                        "Not found in cache '" + tile.getId() + "'. Loading from " + tile.getUrl());
            }
            img = ImageIOExt.readBufferedImage(tile.getUrl());
            ImageIO.write(img, "png", imgFile);
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Wrote to cache " + imgFile.getAbsolutePath());
            }
        }
        return img;
    }
}
