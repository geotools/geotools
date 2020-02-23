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
import junit.framework.TestCase;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.renderer.lite.StreamingRenderer;
import org.opengis.filter.FilterFactory;

public class ImageGraphicFactoryTest extends TestCase {

    private ImageGraphicFactory image;
    private FilterFactory ff;

    @Override
    protected void setUp() throws Exception {
        image = new ImageGraphicFactory();
        ff = CommonFactoryFinder.getFilterFactory(null);
    }

    /** Check that at least the well known png and jpeg formats are supported */
    public void testFormats() throws Exception {
        assertTrue(image.getSupportedMimeTypes().contains("image/png"));
        assertTrue(image.getSupportedMimeTypes().contains("image/jpeg"));
    }

    public void testInvalidPaths() throws Exception {
        assertNull(image.getIcon(null, ff.literal("http://www.nowhere.com"), "image/not!", 20));
        try {
            image.getIcon(null, ff.literal("ThisIsNotAUrl"), "image/png", 20);
            fail("Should have throw an exception, invalid url");
        } catch (IllegalArgumentException e) {
        }
    }

    public void testLocalURL() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/draw.png");
        assertNotNull(url);
        // first call, non cached path
        Icon icon = image.getIcon(null, ff.literal(url), "image/png", 80);
        assertNotNull(icon);
        assertEquals(80, icon.getIconHeight());
    }

    public void testNaturalSize() throws Exception {
        URL url = StreamingRenderer.class.getResource("test-data/draw.png");
        assertNotNull(url);
        Icon icon = image.getIcon(null, ff.literal(url), "image/png", -1);
        assertNotNull(icon);
        assertEquals(22, icon.getIconHeight());
    }

    /**
     * Tests that a fetched icon is added to the cache, and that the {@link
     * GraphicCache#clearCache()} method correctly clears the cache.
     */
    public void testClearCache() {
        URL u = this.getClass().getResource("test-data/test.png");
        Icon icon = image.getIcon(null, ff.literal(u), "image/png", -1);

        assertTrue(image.imageCache.containsKey(u));
        assertNotNull(image.imageCache.get(u));

        ((GraphicCache) image).clearCache();

        assertTrue(image.imageCache.isEmpty());
    }
}
