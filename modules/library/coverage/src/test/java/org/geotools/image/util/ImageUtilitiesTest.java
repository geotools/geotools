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

import static org.junit.Assert.*;

import com.sun.media.jai.operator.ImageReadDescriptor;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.ComponentColorModel;
import java.awt.image.DataBuffer;
import java.awt.image.RenderedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Base64;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.ImageTypeSpecifier;
import javax.imageio.metadata.IIOMetadata;
import javax.imageio.stream.MemoryCacheImageInputStream;
import javax.media.jai.ImageLayout;
import javax.media.jai.JAI;
import javax.media.jai.RenderedOp;
import javax.media.jai.WritableRenderedImageAdapter;
import org.geotools.util.factory.Hints;
import org.hamcrest.CoreMatchers;
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

    @Test
    public void testDisposeWritableRenderedImage()
            throws NoSuchFieldException, SecurityException, IllegalArgumentException,
                    IllegalAccessException {
        BufferedImage bi = new BufferedImage(256, 256, BufferedImage.TYPE_BYTE_GRAY);
        WritableRenderedImageAdapter wria = new WritableRenderedImageAdapter(bi);
        ImageUtilities.disposeSinglePlanarImage(wria);
        // Without the fix, both fields would have been NOT null
        Class<? extends Object> theClass = wria.getClass();
        Field f = theClass.getSuperclass().getDeclaredField("theImage");
        f.setAccessible(true);
        Object image = f.get(wria);
        assertNull(image);
        f = theClass.getDeclaredField("theWritableImage");
        f.setAccessible(true);
        image = f.get(wria);
        assertNull(image);
    }

    @Test
    public void testPixelRescaleNoArrays() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        bi.getData().getDataBuffer().setElemDouble(0, 0, 10);
        assertSame(bi, ImageUtilities.applyRescaling(null, null, bi, null));
    }

    @Test
    public void testPixelRescaleFull() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        bi.getRaster().getDataBuffer().setElemDouble(0, 0, 10);
        RenderedImage result =
                ImageUtilities.applyRescaling(new Double[] {10d}, new Double[] {1d}, bi, null);
        assertEquals(DataBuffer.TYPE_DOUBLE, result.getSampleModel().getDataType());
        assertThat(result.getColorModel(), CoreMatchers.instanceOf(ComponentColorModel.class));
        double[] pixel = new double[1];
        result.getData().getPixel(0, 0, pixel);
        assertEquals(101, pixel[0], 0d);
    }

    @Test
    public void testPixelRescaleCustomHints() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        bi.getRaster().getDataBuffer().setElemDouble(0, 0, 10);
        ImageLayout layout = new ImageLayout(bi);
        layout.setTileHeight(50);
        layout.setTileWidth(50);
        RenderedImage result =
                ImageUtilities.applyRescaling(
                        new Double[] {10d},
                        new Double[] {1d},
                        bi,
                        new Hints(JAI.KEY_IMAGE_LAYOUT, layout));
        assertEquals(DataBuffer.TYPE_DOUBLE, result.getSampleModel().getDataType());
        assertEquals(50, result.getTileWidth());
        assertEquals(50, result.getTileHeight());
        assertThat(result.getColorModel(), CoreMatchers.instanceOf(ComponentColorModel.class));
        double[] pixel = new double[1];
        result.getData().getPixel(0, 0, pixel);
        assertEquals(101, pixel[0], 0d);
    }

    @Test
    public void testPixelRescaleOnlyScales() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        bi.getRaster().getDataBuffer().setElemDouble(0, 0, 10);
        RenderedImage result = ImageUtilities.applyRescaling(new Double[] {10d}, null, bi, null);
        assertEquals(DataBuffer.TYPE_DOUBLE, result.getSampleModel().getDataType());
        assertThat(result.getColorModel(), CoreMatchers.instanceOf(ComponentColorModel.class));
        double[] pixel = new double[1];
        result.getData().getPixel(0, 0, pixel);
        assertEquals(100, pixel[0], 0d);
    }

    @Test
    public void testPixelRescaleOnlyOffset() throws Exception {
        BufferedImage bi = new BufferedImage(100, 100, BufferedImage.TYPE_BYTE_GRAY);
        bi.getRaster().getDataBuffer().setElemDouble(0, 0, 10);
        RenderedImage result = ImageUtilities.applyRescaling(null, new Double[] {10d}, bi, null);
        assertEquals(DataBuffer.TYPE_DOUBLE, result.getSampleModel().getDataType());
        assertThat(result.getColorModel(), CoreMatchers.instanceOf(ComponentColorModel.class));
        double[] pixel = new double[1];
        result.getData().getPixel(0, 0, pixel);
        assertEquals(20, pixel[0], 0d);
    }
}
