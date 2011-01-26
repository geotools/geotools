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
package org.geotools.coverage.processing;

import java.awt.Color;
import javax.measure.unit.SI;

import org.geotools.coverage.Category;
import org.geotools.coverage.GridSampleDimension;
import org.geotools.util.MeasurementRange;
import org.geotools.util.NumberRange;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link ColorMap}.
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class ColorMapTest {
    /**
     * The grid sample dimension to be used for testing purpose.
     */
    private GridSampleDimension band;

    /**
     * The color map to test. Will initially match the grid sample dimension.
     */
    private ColorMap map;

    /**
     * A initially empty ARGB array with expected length.
     */
    private int[] ARGB;

    /**
     * Creates a sample dimension and a color map for testing purpose.
     */
    @Before
    public void setUp() {
        band = new GridSampleDimension("Temperature", new Category[] {
            new Category("Sea",         Color.BLUE,  2),
            new Category("Lands",       Color.GREEN, 1),
            new Category("Clouds",      Color.GRAY,  0),
            new Category("Temperature", new Color[] {Color.CYAN, Color.YELLOW}, 3, 256, 0.1, 0),
        }, SI.CELSIUS);

        map = new ColorMap();
        map.setColor ("Sea",    Color.BLUE);
        map.setColor ("Lands",  Color.GREEN);
        map.setColor ("Clouds", Color.GRAY);
        map.setColors("Temperature", new Color[] {Color.CYAN, Color.YELLOW});

        ARGB = new int[256];
    }

    /**
     * Operations that should not modify the sample dimension.
     */
    @Test
    public void testIdentity() {
        assertEquals(map, map);
        assertSame("No category changes expected.", band, map.recolor(band, null));

        map.setColor("Sea", null);
        assertSame("No category changes expected.", band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(),   ARGB[  0]);
        assertEquals(Color.GREEN .getRGB(),   ARGB[  1]);
        assertEquals("No change expected", 0, ARGB[  2]);
        assertEquals(Color.CYAN  .getRGB(),   ARGB[  3]);
        assertEquals(Color.YELLOW.getRGB(),   ARGB[255]);

        // Some interpolated values
        assertEquals("ff01fffe", Integer.toHexString(ARGB[  4]));
        assertEquals("ff02fffd", Integer.toHexString(ARGB[  5]));
        assertEquals("fffdff02", Integer.toHexString(ARGB[253]));
        assertEquals("fffeff01", Integer.toHexString(ARGB[254]));
    }

    /**
     * Simple color changes.
     */
    @Test
    public void testSimpleChange() {
        map.setColor("Lands", Color.RED);
        map.setColors("Temperature", new Color[] {Color.WHITE, Color.YELLOW, Color.ORANGE});
        assertNotSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(), ARGB[  0]);
        assertEquals(Color.RED   .getRGB(), ARGB[  1]);
        assertEquals(Color.BLUE  .getRGB(), ARGB[  2]);
        assertEquals(Color.WHITE .getRGB(), ARGB[  3]);
        assertEquals(Color.ORANGE.getRGB(), ARGB[255]);
        ensureFilledARGB();
    }

    /**
     * Applies a relative range.
     */
    @Test
    public void testRelativeRange() {
        map.setRelativeRange("Temperature", NumberRange.create(20, 70));
        assertSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(), ARGB[  0]);
        assertEquals(Color.GREEN .getRGB(), ARGB[  1]);
        assertEquals(Color.BLUE  .getRGB(), ARGB[  2]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  3]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  4]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  5]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[253]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[254]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[255]);
        ensureFilledARGB();
    }

    /**
     * Applies a geophysics range.
     */
    @Test
    public void testGeophysicsRange() {
        map.setGeophysicsRange("Temperature", MeasurementRange.create(5, 20, SI.CELSIUS));
        assertSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(), ARGB[  0]);
        assertEquals(Color.GREEN .getRGB(), ARGB[  1]);
        assertEquals(Color.BLUE  .getRGB(), ARGB[  2]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  3]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  4]);
        assertEquals(Color.CYAN  .getRGB(), ARGB[  5]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[253]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[254]);
        assertEquals(Color.YELLOW.getRGB(), ARGB[255]);
        ensureFilledARGB();
    }

    /**
     * Recolor the quantitative range, without prior knowledge of its name.
     */
    @Test
    public void testAnyQuantitativeCategory() {
        map.setColor ("Sea",         null);
        map.setColor ("Lands",       null);
        map.setColors("Temperature", null);
        assertSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY.getRGB(),     ARGB[  0]);
        assertEquals("No change expected", 0, ARGB[  1]);
        assertEquals("No change expected", 0, ARGB[  2]);
        assertEquals("No change expected", 0, ARGB[  3]);
        assertEquals("No change expected", 0, ARGB[255]);

        map.setColors(ColorMap.ANY_QUANTITATIVE_CATEGORY, new Color[] {Color.WHITE, Color.ORANGE});
        assertNotSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(),   ARGB[  0]);
        assertEquals("No change expected", 0, ARGB[  1]);
        assertEquals("No change expected", 0, ARGB[  2]);
        assertEquals(Color.WHITE .getRGB(),   ARGB[  3]);
        assertEquals(Color.ORANGE.getRGB(),   ARGB[255]);

        map.setResetUnspecifiedColors(true);
        assertNotSame(band, map.recolor(band, ARGB));
        assertEquals(Color.GRAY  .getRGB(),   ARGB[  0]);
        assertEquals(Color.GREEN .getRGB(),   ARGB[  1]);
        assertEquals(Color.BLUE  .getRGB(),   ARGB[  2]);
        assertEquals(Color.WHITE .getRGB(),   ARGB[  3]);
        assertEquals(Color.ORANGE.getRGB(),   ARGB[255]);
    }

    /**
     * Ensures that the {@link #ARGB} array contains no unitialized element.
     */
    private void ensureFilledARGB() {
        for (int i=0; i<ARGB.length; i++) {
            assertFalse(String.valueOf(i), ARGB[i] == 0);
        }
    }
}
