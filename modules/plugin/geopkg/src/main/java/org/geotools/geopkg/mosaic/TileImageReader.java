/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg.mosaic;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.imageio.stream.MemoryCacheImageInputStream;

/**
 * Support class helping to read tile images from a geopackage. This class keeps state to avoid
 * performing repeated image reader lookups, it is not thread safe, create one for each thread
 * reading data.
 *
 * @author Andrea Aime - GeoSolutions
 */
class TileImageReader {

    List<ImageReader> readersCache = new ArrayList<>();

    ImageReader lastReader;

    public BufferedImage read(byte[] data) throws IOException {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        ImageInputStream iis = new MemoryCacheImageInputStream(bis);
        if (lastReader != null) {
            lastReader.reset();
            lastReader.setInput(iis);
        }
        if (lastReader == null || !lastReader.getOriginatingProvider().canDecodeInput(iis)) {
            boolean found = false;
            for (ImageReader ir : readersCache) {
                if (ir != lastReader) {
                    ir.reset();
                    ir.setInput(iis);
                    if (ir.canReadRaster()) {
                        lastReader = ir;
                        found = true;
                    }
                }
            }
            if (!found) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(iis);
                if (!readers.hasNext()) {
                    throw new IOException(
                            "Unexpected, cannot find a reader for the current tile image format");
                }
                lastReader = readers.next();
                lastReader.setInput(iis);
                readersCache.add(lastReader);
            }
        }

        ImageReadParam param = lastReader.getDefaultReadParam();
        return lastReader.read(0, param);
    }
}
