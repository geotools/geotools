/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.style;

import java.net.URL;
import javax.swing.Icon;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.StreamingRenderer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.opengis.filter.FilterFactory;

public class ImageGraphicFactoryTest {

    private ImageGraphicFactory image;
    private FilterFactory ff;

    @Before
    public void setUp() throws Exception {
        image = new ImageGraphicFactory();
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    /** Check that at least the well known png and jpeg formats are supported */
    @Test
    public void testFormats() throws Exception {
        Assert.assertTrue(image.getSupportedMimeTypes().contains("image/png"));
        Assert.assertTrue(image.getSupportedMimeTypes().contains("image/jpeg"));
    }

    @Test
    public void testInvalidPaths() throws Exception {
        Assert.assertNull(
                image.getIcon(null, ff.literal("http://www.nowhere.com"), "image/not!", 20));
        try {
            image.getIcon(null, ff.literal("ThisIsNotAUrl"), "image/png", 20);
            Assert.fail("Should have throw an exception, invalid url");
        } catch (IllegalArgumentException e) {
        }
    }

    @Test
    public void testLocalURL() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/draw.png");
        Assert.assertNotNull(url);
        // first call, non cached path
        Icon icon = image.getIcon(null, ff.literal(url), "image/png", 80);
        Assert.assertNotNull(icon);
        Assert.assertEquals(80, icon.getIconHeight());
    }

    @Test
    public void testNaturalSize() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/draw.png");
        Assert.assertNotNull(url);
        Icon icon = image.getIcon(null, ff.literal(url), "image/png", -1);
        Assert.assertNotNull(icon);
        Assert.assertEquals(22, icon.getIconHeight());
    }

    /**
     * Tests that a fetched icon is added to the cache, and that the {@link
     * GraphicCache#clearCache()} method correctly clears the cache.
     */
    @Test
    public void testClearCache() {
        URL u = this.getClass().getResource("test-data/test.png");
        Icon icon = image.getIcon(null, ff.literal(u), "image/png", -1);

        Assert.assertTrue(image.imageCache.containsKey(u));
        Assert.assertNotNull(image.imageCache.get(u));

        ((GraphicCache) image).clearCache();

        Assert.assertTrue(image.imageCache.isEmpty());
    }
}
