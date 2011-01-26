/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io;

import java.awt.image.IndexColorModel;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link Palette} and {@link PaletteFactory}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public final class PaletteTest {
    /**
     * Tests the argument check performed by constructor.
     */
    @Test
    public void testConstructor() {
        final PaletteFactory factory = PaletteFactory.getDefault();
        assertEquals(100, new IndexedPalette(factory, "grayscale",    0, 100, 100, 1, 0).upper);
        assertEquals( 50, new IndexedPalette(factory, "grayscale", -100,  50, 100, 1, 0).upper);
        try {
            new IndexedPalette(factory, "grayscale", 0, 100, -100, 1, 0);
            fail("Should not accept negative size.");
        } catch (IllegalArgumentException e) {
            // This is the expected exception.
        }
        try {
            new IndexedPalette(factory, "grayscale", 100, 50, 256, 1, 0);
            fail("Should not accept invalid range.");
        } catch (IllegalArgumentException e) {
            // This is the expected exception.
        }
        assertEquals(40000, new IndexedPalette(factory, "grayscale", 1,  40000, 0xFFFF, 1, 0).upper);
        try {
            new IndexedPalette(factory, "grayscale", -1,  40000, 0xFFFF, 1, 0);
            fail("Should not accept value out of range.");
        } catch (IllegalArgumentException e) {
            // This is the expected exception.
        }
        try {
            new IndexedPalette(factory, "grayscale", 1,  70000, 0xFFFF, 1, 0);
            fail("Should not accept value out of range.");
        } catch (IllegalArgumentException e) {
            // This is the expected exception.
        }
        try {
            new IndexedPalette(factory, "grayscale", -40000,  0, 0xFFFF, 1, 0);
            fail("Should not accept value out of range.");
        } catch (IllegalArgumentException e) {
            // This is the expected exception.
        }
    }

    /**
     * Tests {@link PaletteFactory#getAvailableNames}.
     */
    @Test
    public void testAvailableNames() {
        final List<String> names = Arrays.asList(PaletteFactory.getDefault().getAvailableNames());
        assertFalse(names.isEmpty());
        assertTrue (names.contains("rainbow"));
        assertTrue (names.contains("grayscale"));
        assertTrue (names.contains("bell"));
        assertFalse(names.contains("Donald Duck"));
    }

    /**
     * Tests the cache.
     */
    @Test
    public void testCache() {
        final PaletteFactory factory = PaletteFactory.getDefault();
        final Palette first  = factory.getPalettePadValueFirst("rainbow", 100);
        final Palette second = factory.getPalettePadValueFirst("bell",    100);
        final Palette third  = factory.getPalettePadValueFirst("rainbow", 100);
        assertEquals (first, third);
        assertSame   (first, third);
        assertNotSame(first, second);
    }

    /**
     * Tests the color model.
     *
     * @throws IOException If an I/O operation was required and failed.
     */
    @Test
    public void testColorModel() throws IOException {
        final PaletteFactory  factory = PaletteFactory.getDefault();
        final Palette         palette = factory.getPalettePadValueFirst("rainbow", 100);
        final IndexColorModel icm     = (IndexColorModel) palette.getColorModel();
        assertEquals(100, icm.getMapSize());
        assertEquals(0, icm.getTransparentPixel());
    }
}
