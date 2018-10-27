/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *
 *
 */
package org.geotools.image.util;

import static org.junit.Assert.assertTrue;

import com.sun.media.jai.operator.ImageReadDescriptor;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.media.jai.RenderedOp;
import org.junit.Test;

public class ImageUtilitiesTest {

    @Test
    public void testDisposeReader() {
        // build a stream and a reader on top of a one pixel GIF image,
        // with a check on whether they get disposed of
        AtomicBoolean readerDisposed = new AtomicBoolean(false);
        AtomicBoolean streamDisposed = new AtomicBoolean(false);
        byte[] bytes =
                Base64.getDecoder().decode("R0lGODlhAQABAAAAACH5BAEKAAEALAAAAAABAAEAAAICTAEAOw==");
        MemoryCacheImageInputStream is =
                new MemoryCacheImageInputStream(new ByteArrayInputStream(bytes)) {
                    @Override
                    public void close() {
                        streamDisposed.set(true);
                    }
                };
        final ImageReader nativeReader = ImageIO.getImageReadersByFormatName("GIF").next();
        nativeReader.setInput(is);
        ImageReader reader =
                new ImageReader(null) {

                    @Override
                    public int getNumImages(boolean allowSearch) throws IOException {
                        return nativeReader.getNumImages(allowSearch);
                    }

                    @Override
                    public int getWidth(int imageIndex) throws IOException {
                        return nativeReader.getWidth(imageIndex);
                    }

                    @Override
                    public int getHeight(int imageIndex) throws IOException {
                        return nativeReader.getHeight(imageIndex);
                    }

                    @Override
                    public Iterator<ImageTypeSpecifier> getImageTypes(int imageIndex)
                            throws IOException {
                        return nativeReader.getImageTypes(imageIndex);
                    }

                    @Override
                    public IIOMetadata getStreamMetadata() throws IOException {
                        return nativeReader.getStreamMetadata();
                    }

                    @Override
                    public IIOMetadata getImageMetadata(int imageIndex) throws IOException {
                        return nativeReader.getImageMetadata(imageIndex);
                    }

                    @Override
                    public BufferedImage read(int imageIndex, ImageReadParam param)
                            throws IOException {
                        return nativeReader.read(imageIndex, param);
                    }

                    @Override
                    public void dispose() {
                        nativeReader.dispose();
                        readerDisposed.set(true);
                    }
                };
        // wrap it in a image read
        RenderedOp image =
                ImageReadDescriptor.create(
                        is, 0, false, false, false, null, null, null, reader, null);

        // dispose the chain, check the stream and reader have been disposed of
        ImageUtilities.disposePlanarImageChain(image);
        assertTrue(streamDisposed.get());
        assertTrue(readerDisposed.get());
    }
}
