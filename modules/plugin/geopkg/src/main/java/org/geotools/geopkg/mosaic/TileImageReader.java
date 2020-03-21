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
        if (lastReader != null) {
            lastReader.reset();
            lastReader.setInput(getImageInputStream(data));
        }
        if (lastReader == null
                || !lastReader.getOriginatingProvider().canDecodeInput(getImageInputStream(data))) {
            boolean found = false;
            for (ImageReader ir : readersCache) {
                if (ir != lastReader) {
                    ir.reset();
                    ir.setInput(getImageInputStream(data));
                    if (ir.canReadRaster()) {
                        lastReader = ir;
                        found = true;
                    }
                }
            }
            if (!found) {
                Iterator<ImageReader> readers = ImageIO.getImageReaders(getImageInputStream(data));
                if (!readers.hasNext()) {
                    throw new IOException(
                            "Unexpected, cannot find a reader for the current tile image format");
                }
                lastReader = readers.next();
                lastReader.setInput(getImageInputStream(data));
                readersCache.add(lastReader);
            }
        }

        ImageReadParam param = lastReader.getDefaultReadParam();
        return lastReader.read(0, param);
    }

    /**
     * Methods to create a {@link ImageInputStream} out of a byte array. Gets called over and over
     * because just marking the stream and resetting it was not working against some sample
     * GeoPackage files, and the result was not finding the readers to use
     */
    ImageInputStream getImageInputStream(byte[] data) {
        ByteArrayInputStream bis = new ByteArrayInputStream(data);
        return new MemoryCacheImageInputStream(bis);
    }
}
