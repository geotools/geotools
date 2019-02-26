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
import java.io.ByteArrayInputStream;
import java.util.concurrent.Callable;
import javax.imageio.ImageIO;
import org.locationtech.jts.geom.Envelope;

/** A task that decodes {@link TileData}'s as they are streamed from a query. */
public class TileDecoder implements Callable<Tile> {

    final TileData data;
    final ReadRequest read;

    TileDecoder(TileData data, ReadRequest read) {
        this.data = data;
        this.read = read;
    }

    @Override
    public Tile call() throws Exception {
        Envelope bounds = data.bounds;

        // throw out anything not in bounds, shouldn't happen but just in case
        if (!read.nativeBounds.intersects(bounds)) return Tile.NULL;

        // decode the image
        BufferedImage image = ImageIO.read(new ByteArrayInputStream(data.bytes));

        Tile tile;

        // clip it if need be
        if (!read.nativeBounds.contains(bounds)) {
            Envelope clippedBounds = bounds.intersection(read.nativeBounds);

            double scaleX = image.getWidth() / bounds.getWidth();
            double scaleY = image.getHeight() / bounds.getHeight();

            int x = (int) Math.round((clippedBounds.getMinX() - bounds.getMinX()) * scaleX);
            int y = (int) Math.round((clippedBounds.getMaxY() - bounds.getMinY()) * scaleY);
            y = image.getHeight() - y;

            int w =
                    (int)
                            Math.round(
                                    image.getWidth()
                                            / bounds.getWidth()
                                            * clippedBounds.getWidth());
            int h =
                    (int)
                            Math.round(
                                    image.getHeight()
                                            / bounds.getHeight()
                                            * clippedBounds.getHeight());

            if (w > 0 && h > 0) {
                image = image.getSubimage(x, y, w, h);
                tile = new Tile(image, clippedBounds);
            } else {
                tile = Tile.NULL;
            }
        } else {
            // good to go
            tile = new Tile(image, bounds);
        }

        return tile;
    }
}
