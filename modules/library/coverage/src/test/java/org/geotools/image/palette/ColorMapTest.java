/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.palette;

import static org.junit.Assert.*;

import java.util.HashSet;
import java.util.Set;
import org.geotools.image.palette.ColorMap.ColorEntry;
import org.junit.Test;

public class ColorMapTest {

    @Test
    public void testSimpleIncrement() {
        ColorMap map = new ColorMap();

        map.increment(255, 255, 255, 255);
        assertEquals(1, map.get(255, 255, 255, 255));
        map.increment(255, 255, 255, 255);
        assertEquals(2, map.get(255, 255, 255, 255));
        assertEquals(1, map.size());
    }

    @Test
    public void testPutIncrement() {
        ColorMap map = new ColorMap();

        map.put(255, 255, 255, 255, 10);
        assertEquals(10, map.get(255, 255, 255, 255));
        map.increment(255, 255, 255, 255);
        assertEquals(11, map.get(255, 255, 255, 255));
        assertEquals(1, map.size());
    }

    @Test
    public void testRehash() {
        ColorMap map = new ColorMap(16);
        assertEquals(16, map.table.length);

        // this will force the map to rehash
        for (int i = 1; i < 20; i++) {
            map.put(255, 255, 255, i, i);
        }
        assertEquals(64, map.table.length);
        assertEquals((int) (64 * ColorMap.DEFAULT_LOAD_FACTOR), map.threshold);

        // check we can still retrieve the data as expected
        for (int i = 1; i < 20; i++) {
            assertEquals(i, map.get(255, 255, 255, i));
        }
    }

    @Test
    public void testIterate() {
        ColorMap map = new ColorMap(16);
        assertEquals(16, map.table.length);

        // this will force the map to rehash
        for (int i = 1; i < 20; i++) {
            map.put(255, 255, 255, i, i);
        }
        assertEquals(64, map.table.length);

        Set<Integer> colors = new HashSet<Integer>();
        for (ColorEntry ce : map) {
            Integer color = ce.color;
            assertFalse(colors.contains(color));
            colors.add(color);
            assertEquals(ColorUtils.alpha(ce.color), ce.value);
        }
        assertEquals(map.size, colors.size());
    }
}
