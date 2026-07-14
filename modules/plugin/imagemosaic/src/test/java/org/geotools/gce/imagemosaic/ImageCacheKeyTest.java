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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.net.MalformedURLException;
import java.net.URL;
import org.junit.Test;

public class ImageCacheKeyTest {

    private static final String MOSAIC_A = "file:/mosaic/a";
    private static final String MOSAIC_B = "file:/mosaic/b";
    private static final URL URL_A = url("file:/data/a.tif");
    private static final URL URL_B = url("file:/data/b.tif");

    @Test
    public void testEqualKeys() {
        ImageCacheKey k1 = new ImageCacheKey(MOSAIC_A, URL_A, 0, new int[] {0, 1, 2});
        ImageCacheKey k2 = new ImageCacheKey(MOSAIC_A, URL_A, 0, new int[] {0, 1, 2});
        assertEquals(k1, k2);
        assertEquals(k1.hashCode(), k2.hashCode());
    }

    @Test
    public void testDifferentMosaicId() {
        assertNotEquals(new ImageCacheKey(MOSAIC_A, URL_A, 0, null), new ImageCacheKey(MOSAIC_B, URL_A, 0, null));
    }

    @Test
    public void testDifferentUrl() {
        assertNotEquals(new ImageCacheKey(MOSAIC_A, URL_A, 0, null), new ImageCacheKey(MOSAIC_A, URL_B, 0, null));
    }

    @Test
    public void testDifferentImageIndex() {
        assertNotEquals(new ImageCacheKey(MOSAIC_A, URL_A, 0, null), new ImageCacheKey(MOSAIC_A, URL_A, 1, null));
    }

    @Test
    public void testDifferentBands() {
        assertNotEquals(
                new ImageCacheKey(MOSAIC_A, URL_A, 0, new int[] {0}),
                new ImageCacheKey(MOSAIC_A, URL_A, 0, new int[] {1}));
    }

    @Test
    public void testSameGranule() {
        ImageCacheKey key = new ImageCacheKey(MOSAIC_A, URL_A, 0, null);
        assertTrue(key.sameGranule(URL_A));
        assertFalse(key.sameGranule(URL_B));
        assertFalse(key.sameGranule(null));
    }

    @Test
    public void testSameMosaic() {
        ImageCacheKey key = new ImageCacheKey(MOSAIC_A, URL_A, 0, null);
        assertTrue(key.sameMosaic(MOSAIC_A));
        assertFalse(key.sameMosaic(MOSAIC_B));
    }

    @Test
    public void testEligibleWithinThreshold() {
        // 10x10 RGB = 300 bytes, pool default 1 KB -> eligible
        GranuleImageCache cache = new GuavaGranuleImageCache(1024 * 1024, 1024);
        BufferedImage sm = new BufferedImage(10, 10, BufferedImage.TYPE_3BYTE_BGR);
        assertTrue(ImageCacheKey.isCacheable(
                cache, -1, new Rectangle(0, 0, 10, 10), new int[] {0, 1, 2}, sm.getSampleModel()));
    }

    @Test
    public void testTooLargeForPoolDefault() {
        // 100x100 RGB = 30000 bytes > 1 KB default -> not eligible
        GranuleImageCache cache = new GuavaGranuleImageCache(1024 * 1024, 1024);
        BufferedImage sm = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        assertFalse(ImageCacheKey.isCacheable(cache, -1, new Rectangle(0, 0, 100, 100), null, sm.getSampleModel()));
    }

    @Test
    public void testThresholdOverrideWidensEligibility() {
        GranuleImageCache cache = new GuavaGranuleImageCache(1024 * 1024, 1024);
        BufferedImage sm = new BufferedImage(100, 100, BufferedImage.TYPE_3BYTE_BGR);
        // 30000 bytes fits a 64 KB override
        assertTrue(ImageCacheKey.isCacheable(cache, 64, new Rectangle(0, 0, 100, 100), null, sm.getSampleModel()));
    }

    private static URL url(String spec) {
        try {
            return new URL(spec);
        } catch (MalformedURLException e) {
            throw new IllegalStateException(e);
        }
    }
}
