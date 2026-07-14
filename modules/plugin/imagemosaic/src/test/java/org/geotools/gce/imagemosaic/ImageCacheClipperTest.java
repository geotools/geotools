/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.imagemosaic;

import static org.junit.Assert.assertEquals;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import org.geotools.image.test.ImageAssert;
import org.junit.BeforeClass;
import org.junit.Test;

/** Confirms {@link ImageCacheClipper} reproduces exactly what a real {@link ImageReader} would decode. */
public class ImageCacheClipperTest {

    private static final int WIDTH = 64;
    private static final int HEIGHT = 48;

    private static File file;
    private static BufferedImage full;

    @BeforeClass
    public static void writeSourceImage() throws Exception {
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_3BYTE_BGR);
        for (int y = 0; y < HEIGHT; y++) {
            for (int x = 0; x < WIDTH; x++) {
                image.setRGB(x, y, ((x * 3) & 0xFF) << 16 | ((y * 5) & 0xFF) << 8 | ((x + y) & 0xFF));
            }
        }
        file = File.createTempFile("clipper", ".png");
        file.deleteOnExit();
        ImageIO.write(image, "png", file);
        full = ImageIO.read(file);
    }

    @Test
    public void testFullRegionReturnsSameImage() {
        assertEquals(full, ImageCacheClipper.clip(full, new Rectangle(0, 0, WIDTH, HEIGHT), 1, 1));
    }

    @Test
    public void testCropOnly() throws Exception {
        assertMatchesReader(new Rectangle(10, 8, 20, 16), 1, 1);
    }

    @Test
    public void testSubsamplingOnly() throws Exception {
        assertMatchesReader(new Rectangle(0, 0, WIDTH, HEIGHT), 3, 2);
    }

    @Test
    public void testCropAndSubsampling() throws Exception {
        assertMatchesReader(new Rectangle(7, 5, 40, 30), 4, 3);
    }

    private void assertMatchesReader(Rectangle region, int ssx, int ssy) throws Exception {
        RenderedImage expected = readWithReader(region, ssx, ssy);
        RenderedImage actual = ImageCacheClipper.clip(full, region, ssx, ssy);
        assertEquals(expected.getWidth(), actual.getWidth());
        assertEquals(expected.getHeight(), actual.getHeight());
        ImageAssert.assertEquals(expected, actual, 0);
    }

    private RenderedImage readWithReader(Rectangle region, int ssx, int ssy) throws Exception {
        try (ImageInputStream iis = ImageIO.createImageInputStream(file)) {
            ImageReader reader = ImageIO.getImageReaders(iis).next();
            reader.setInput(iis);
            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceRegion(region);
            param.setSourceSubsampling(ssx, ssy, 0, 0);
            try {
                return reader.read(0, param);
            } finally {
                reader.dispose();
            }
        }
    }
}
