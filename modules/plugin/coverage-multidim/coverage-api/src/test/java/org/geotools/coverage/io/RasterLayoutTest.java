/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io;

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import org.junit.Assert;
import org.junit.Test;

public class RasterLayoutTest extends Assert {

    /** The reference Test RasterLayout */
    public static RasterLayout testRasterLayout;

    private static final int H = 100;

    private static final int W = 200;

    private static final int MIN_X = 100;

    private static final int MIN_Y = 50;

    private static final int TILE_H = 20;

    private static final int TILE_W = 40;

    private static final int TILE_X = 0;

    private static final int TILE_Y = 0;

    static {
        testRasterLayout = new RasterLayout();
        testRasterLayout.setHeight(H);
        testRasterLayout.setWidth(W);
        testRasterLayout.setMinX(MIN_X);
        testRasterLayout.setMinY(MIN_Y);

        testRasterLayout.setTileHeight(TILE_H);
        testRasterLayout.setTileWidth(TILE_W);
        testRasterLayout.setTileGridXOffset(TILE_X);
        testRasterLayout.setTileGridYOffset(TILE_Y);
    }

    @Test
    public void testLayout() {

        assertEquals(testRasterLayout.getHeight(), H);
        assertEquals(testRasterLayout.getWidth(), W);
        assertEquals(testRasterLayout.getMinX(), MIN_X);
        assertEquals(testRasterLayout.getMinY(), MIN_Y);
        assertEquals(testRasterLayout.getTileWidth(), TILE_W);
        assertEquals(testRasterLayout.getTileHeight(), TILE_H);
        assertEquals(testRasterLayout.getTileGridXOffset(), TILE_X);
        assertEquals(testRasterLayout.getTileGridYOffset(), TILE_Y);

        RasterLayout layout2 = new RasterLayout(MIN_X, MIN_Y, W, H, TILE_X, TILE_Y, TILE_W, TILE_H);
        assertEquals(testRasterLayout, layout2);

        Rectangle rect = new Rectangle(MIN_X, MIN_Y, W, H);

        RasterLayout layout3 = new RasterLayout(rect);
        assertNotEquals(testRasterLayout, layout3);

        RasterLayout layout4 = new RasterLayout(MIN_X, MIN_Y, W, H);
        assertNotEquals(testRasterLayout, layout4);

        assertEquals(rect, testRasterLayout.toRectangle());

        BufferedImage bi = new BufferedImage(W, H, BufferedImage.TYPE_BYTE_GRAY);
        RasterLayout layout5 = new RasterLayout(bi);
        assertNotEquals(testRasterLayout, layout5);
    }
}
