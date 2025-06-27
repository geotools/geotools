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
package org.geotools.data.util;

import org.junit.Assert;
import org.junit.Test;

public class ScreenMapTest {
    private int xmin;

    private int width;

    private int height;

    private int ymin;

    /*
     * Test method for 'org.geotools.renderer.shape.ScreenMap.set(int, int)'
     */
    @Test
    public void testSet() {
        ymin = xmin = 0;
        height = width = 8;
        ScreenMap map = new ScreenMap(0, 0, 8, 8);

        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                Assert.assertFalse(map.get(x, y));
            }
        }

        setOne(map, 0, 0, true, false);
        setOne(map, 0, 0, false, true);
        setOne(map, 3, 4, true, false);
        setAll(map, true);
        setAll(map, false);
    }

    private void setOne(ScreenMap map, int xconst, int yconst, boolean bool, boolean expectedOldValue) {
        Assert.assertEquals(expectedOldValue, map.get(xconst, yconst));
        map.set(xconst, yconst, bool);

        for (int x = xmin; x < width; x++) {
            for (int y = ymin; y < height; y++) {
                if (x == xconst && y == yconst) {
                    Assert.assertEquals("x=" + x + " y=" + y, bool, map.get(x, y));
                } else {
                    Assert.assertFalse("x=" + x + " y=" + y, map.get(x, y));
                }
            }
        }
    }

    private void setAll(ScreenMap map, boolean value) {
        for (int x = xmin; x < width; x++) {
            for (int y = ymin; y < height; y++) {
                map.set(x, y, value);
            }
        }

        for (int x = xmin; x < width; x++) {
            for (int y = ymin; y < height; y++) {
                Assert.assertEquals(value, map.get(x, y));
            }
        }
    }

    @Test
    public void testSubsetScreen() throws Exception {
        xmin = 478;
        ymin = 0;
        width = 283;
        height = 452;
        ScreenMap map = new ScreenMap(xmin, ymin, width + 1, height + 1);

        // test 4 corners
        setOne(map, xmin, ymin, true, false);
        setOne(map, xmin, ymin, false, true);

        setOne(map, xmin + width - 1, ymin, true, false);
        setOne(map, xmin + width - 1, ymin, false, true);

        setOne(map, xmin + width - 1, ymin + height - 1, true, false);
        setOne(map, xmin + width - 1, ymin + height - 1, false, true);

        setOne(map, xmin, ymin + height - 1, true, false);
        setOne(map, xmin, ymin + height - 1, false, true);

        // test a couple edges
        setOne(map, xmin + 7, ymin, true, false);
        setOne(map, xmin + 7, ymin, false, true);

        setOne(map, xmin + width - 1, ymin + 10, true, false);
        setOne(map, xmin + width - 1, ymin + 10, false, true);

        setOne(map, xmin + 5, ymin + height - 1, true, false);
        setOne(map, xmin + 5, ymin + height - 1, false, true);

        setOne(map, xmin, ymin + 7, true, false);
        setOne(map, xmin, ymin + 7, false, true);

        // test the case I know fails
        setOne(map, 728, 427, true, false);

        setAll(map, true);
        setAll(map, false);
    }

    @Test
    public void testOutsideScreen() throws Exception {
        xmin = 0;
        ymin = 0;
        width = 10;
        height = 10;
        ScreenMap map = new ScreenMap(xmin, ymin, width, height);

        // check points outside the screen
        Assert.assertFalse(map.checkAndSet(-10, -10));
        Assert.assertFalse(map.checkAndSet(-10, -10));
        Assert.assertFalse(map.checkAndSet(-10, 10));
        Assert.assertFalse(map.checkAndSet(-10, 10));
        Assert.assertFalse(map.checkAndSet(20, 10));
        Assert.assertFalse(map.checkAndSet(20, 10));
        Assert.assertFalse(map.checkAndSet(20, -10));
        Assert.assertFalse(map.checkAndSet(20, -10));

        // also control "get"
        Assert.assertFalse(map.get(-10, -10));
        Assert.assertFalse(map.get(-10, -10));
        Assert.assertFalse(map.get(-10, 10));
        Assert.assertFalse(map.get(-10, 10));
        Assert.assertFalse(map.get(20, 10));
        Assert.assertFalse(map.get(20, 10));
        Assert.assertFalse(map.get(20, -10));
        Assert.assertFalse(map.get(20, -10));

        // compare with one inside
        Assert.assertFalse(map.checkAndSet(0, 0));
        Assert.assertTrue(map.checkAndSet(0, 0));
        Assert.assertTrue(map.get(0, 0));
    }
}
