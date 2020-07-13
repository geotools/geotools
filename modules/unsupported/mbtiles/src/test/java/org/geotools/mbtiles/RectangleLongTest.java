/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.mbtiles;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class RectangleLongTest {

    @Test
    public void expandToInclude() {
        // emtpy one
        RectangleLong rect = new RectangleLong();
        assertTrue(rect.isNull());
        rect.expandToInclude(new MBTilesTileLocation(0, 10, 20));
        assertEquals(new RectangleLong(10, 10, 20, 20), rect);
        assertFalse(rect.isNull());
    }

    @Test
    public void isNull() {
        assertTrue(new RectangleLong().isNull());
        assertFalse(new RectangleLong(1, 2, 3, 4).isNull());
    }

    @Test
    public void intersectionContains() {
        RectangleLong r1 = new RectangleLong(0, 10, 0, 10);
        RectangleLong r2 = new RectangleLong(5, 7, 5, 7);
        assertTrue(r1.intersects(r2));
        assertEquals(r2, r1.intersection(r2));
        assertEquals(r2, r2.intersection(r1));
    }

    @Test
    public void intersectionOverlap() {
        RectangleLong r1 = new RectangleLong(0, 10, 0, 10);
        RectangleLong r2 = new RectangleLong(5, 15, 5, 15);
        assertTrue(r1.intersects(r2));
        RectangleLong expected = new RectangleLong(5, 10, 5, 10);
        assertEquals(expected, r1.intersection(r2));
        assertEquals(expected, r2.intersection(r1));
    }

    @Test
    public void intersectionNone() {
        RectangleLong r1 = new RectangleLong(0, 10, 0, 10);
        RectangleLong r2 = new RectangleLong(15, 20, 15, 20);
        assertFalse(r1.intersects(r2));
        RectangleLong expected = new RectangleLong();
        assertEquals(expected, r1.intersection(r2));
        assertEquals(expected, r2.intersection(r1));
    }

    @Test
    public void testExpandToIncludeRect() {
        // inside
        assertEquals(
                new RectangleLong(0, 10, 0, 10), expand0_10_0_10(new RectangleLong(5, 6, 5, 6)));
        // containing
        assertEquals(
                new RectangleLong(-10, 20, -10, 20),
                expand0_10_0_10(new RectangleLong(-10, 20, -10, 20)));
        // overlapping, various directions
        assertEquals(
                new RectangleLong(0, 15, 0, 10), expand0_10_0_10(new RectangleLong(5, 15, 0, 10)));
        assertEquals(
                new RectangleLong(-5, 10, 0, 10), expand0_10_0_10(new RectangleLong(-5, 5, 0, 10)));
        assertEquals(
                new RectangleLong(0, 10, 0, 15), expand0_10_0_10(new RectangleLong(0, 10, 5, 15)));
        assertEquals(
                new RectangleLong(0, 10, -5, 10), expand0_10_0_10(new RectangleLong(0, 10, -5, 5)));
        // disjoint
        assertEquals(
                new RectangleLong(0, 30, 0, 10), expand0_10_0_10(new RectangleLong(20, 30, 0, 10)));
    }

    public RectangleLong expand0_10_0_10(RectangleLong expand) {
        RectangleLong r1 = new RectangleLong(0, 10, 0, 10);
        r1.expandToInclude(expand);
        return r1;
    }
}
