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
package org.geotools.image;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.awt.Rectangle;
import java.awt.Shape;
import java.awt.image.BufferedImage;
import java.awt.image.Raster;
import org.junit.Test;

public class DrawableBitSetTest {

    private static final int WIDTH = 50;

    private static final int HEIGHT = 50;

    private static final int MIN = 10;

    private static final int SIZE = 30;

    private static final int MAX = MIN + SIZE;

    private static final int SET_PIXELS = SIZE * SIZE + SIZE * 2 + 1;

    @Test
    public void testPointSet() {
        DrawableBitSet bitset = new DrawableBitSet(WIDTH, HEIGHT);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertFalse(bitset.get(i, j));
            }
        }

        bitset.set(20, 20);
        assertTrue(bitset.get(20, 20));
    }

    @Test
    public void testPointOutside() {
        DrawableBitSet bitset = new DrawableBitSet(WIDTH, HEIGHT);
        boolean success = true;
        try {
            bitset.set(60, 60);
        } catch (ArrayIndexOutOfBoundsException aie) {
            success = false;
        }
        assertFalse(success);
    }

    @Test
    public void testGetImage() {
        DrawableBitSet bitset = new DrawableBitSet(WIDTH, HEIGHT);
        // Draw a rectangle
        Shape rectangle = new Rectangle(MIN, MIN, SIZE, SIZE);
        bitset.set(rectangle);
        BufferedImage image = bitset.getImage();
        assertNotNull(image);
        Raster raster = image.getData();
        assertNotNull(raster);
        int sample;
        int setPixel = 0;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                sample = raster.getSample(i, j, 0);
                if (i >= MIN && j >= MIN && i <= MAX && j <= MAX) {
                    // Check pixels within the rectangle have been set
                    assertEquals(1, sample);
                    setPixel++;
                } else {
                    assertEquals(0, sample);
                }
            }
        }
        assertEquals(SET_PIXELS, setPixel);
    }

    @Test
    public void testDrawShape() {
        DrawableBitSet bitset = new DrawableBitSet(WIDTH, HEIGHT);
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                assertFalse(bitset.get(i, j));
            }
        }

        int setPixel = 0;
        // Draw a rectangle
        Shape rectangle = new Rectangle(MIN, MIN, SIZE, SIZE);
        bitset.set(rectangle);
        boolean value = false;
        for (int i = 0; i < WIDTH; i++) {
            for (int j = 0; j < HEIGHT; j++) {
                value = bitset.get(i, j);
                if (i >= MIN && j >= MIN && i <= MAX && j <= MAX) {
                    // Check pixels within the rectangle have been set
                    assertTrue(value);
                    setPixel++;
                } else {
                    assertFalse(value);
                }
            }
        }
        assertEquals(SET_PIXELS, setPixel);
    }
}
