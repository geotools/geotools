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
package org.geotools.renderer;

import org.geotools.renderer.ScreenMap;

import junit.framework.TestCase;

/**
 *
 * @source $URL$
 */
public class ScreenMapTest extends TestCase {
    private int xmin;
    private int width;
    private int height;
    private int ymin;

    /*
     * Test method for 'org.geotools.renderer.shape.ScreenMap.set(int, int)'
     */
    public void testSet() {
        ymin = xmin = 0;
        height = width = 8;
        ScreenMap map = new ScreenMap(0, 0, 8, 8);

        for( int x = 0; x < 8; x++ ) {
            for( int y = 0; y < 8; y++ ) {
                assertEquals(false, map.get(x, y));
            }
        }

        setOne(map, 0, 0, true, false);
        setOne(map, 0, 0, false, true);
        setOne(map, 3, 4, true, false);
        setAll(map, true);
        setAll(map, false);
    }

    private void setOne( ScreenMap map, int xconst, int yconst, boolean bool,
            boolean expectedOldValue ) {
        assertEquals(expectedOldValue, map.get(xconst, yconst));
        map.set(xconst, yconst, bool);

        for( int x = xmin; x < width; x++ ) {
            for( int y = ymin; y < height; y++ ) {
                if ((x == xconst) && (y == yconst)) {
                    assertEquals("x=" + x + " y=" + y, bool, map.get(x, y));
                } else {
                    assertEquals("x=" + x + " y=" + y, false, map.get(x, y));
                }
            }
        }
    }

    private void setAll( ScreenMap map, boolean value ) {
        for( int x = xmin; x < width; x++ ) {
            for( int y = ymin; y < height; y++ ) {
                map.set(x, y, value);
            }
        }

        for( int x = xmin; x < width; x++ ) {
            for( int y = ymin; y < height; y++ ) {
                assertEquals(value, map.get(x, y));
            }
        }
    }

    public void testSubsetScreen() throws Exception {
        xmin = 478;
        ymin = 0;
        width = 283;
        height = 452;
        ScreenMap map = new ScreenMap(xmin, ymin, width + 1, height + 1);

        // test 4 corners
        setOne(map, xmin, ymin, true, false);
        setOne(map, xmin, ymin, false, true);

        setOne(map, xmin + width-1, ymin, true, false);
        setOne(map, xmin + width-1, ymin, false, true);

        setOne(map, xmin + width-1, ymin + height-1, true, false);
        setOne(map, xmin + width-1, ymin + height-1, false, true);

        setOne(map, xmin, ymin + height-1, true, false);
        setOne(map, xmin, ymin + height-1, false, true);

        // test a couple edges
        setOne(map, xmin + 7, ymin, true, false);
        setOne(map, xmin + 7, ymin, false, true);

        setOne(map, xmin + width-1, ymin + 10, true, false);
        setOne(map, xmin + width-1, ymin + 10, false, true);

        setOne(map, xmin + 5, ymin + height-1, true, false);
        setOne(map, xmin + 5, ymin + height-1, false, true);

        setOne(map, xmin, ymin + 7, true, false);
        setOne(map, xmin, ymin + 7, false, true);
        
        // test the case I know fails
        setOne(map, 728, 427, true, false);
        
        setAll(map, true);
        setAll(map, false);
        
    }
}
