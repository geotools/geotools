/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.image.io.mosaic;

import org.junit.*;
import static org.junit.Assert.*;


/**
 * Tests {@link FilenameFormatter}.
 *
 *
 * @source $URL$
 * @version $Id$
 * @author Martin Desruisseaux
 */
public class FilenameFormatterTest {
    /**
     * Tests the creation of a pattern.
     */
    @Test
    public void testGuessPattern() {
        final FilenameFormatter formatter = new FilenameFormatter();
        assertNull  ("Mismatched column number.",        formatter.guessPattern(0,  0,  0, "L1_B1.png"));
        assertEquals("L{level:1}_{column:1}{row:1}.png", formatter.guessPattern(0,  0,  0, "L1_A1.png"));
        assertEquals("L{level:2}_{column:1}{row:1}.png", formatter.guessPattern(0,  0,  0, "L01_A1.png"));
        assertEquals("L{level:1}_{column:2}{row:1}.png", formatter.guessPattern(0,  0,  0, "L1_AA1.png"));
        assertEquals("L{level:1}_{column:1}{row:2}.png", formatter.guessPattern(0,  0,  0, "L1_A01.png"));
        assertEquals("L{level:2}_{column:2}{row:2}.png", formatter.guessPattern(0,  0,  0, "L01_AA01.png"));
        assertEquals("L{level:2}_{column:2}{row:2}.png", formatter.guessPattern(12, 3, 14, "L13_AD15.png"));
        assertNull  ("Mismatched column number.",        formatter.guessPattern(12, 0, 14, "L13_AD15.png"));
        assertNull  ("Mismatched row number.",           formatter.guessPattern(12, 3,  0, "L13_AD15.png"));
        assertNull  ("Mismatched row number.",           formatter.guessPattern( 0, 7,  7, "L01_AH88.png"));
    }

    /**
     * Tests the application of a pattern.
     */
    @Test
    public void testApplyPattern() {
        final FilenameFormatter formatter = new FilenameFormatter();
        formatter.applyPattern("L{level:2}_{column:2}{row:2}.png");
        assertEquals("L{level:2}_{column:2}{row:2}.png", formatter.toString());
        assertEquals("L01_AA01.png", formatter.generateFilename(0,  0,  0));
        assertEquals("L13_AD15.png", formatter.generateFilename(12, 3, 14));

        formatter.applyPattern("L{level:1}_{column:2}{row:3}.png");
        assertEquals("L{level:1}_{column:2}{row:3}.png", formatter.toString());
        assertEquals("L1_AA001.png", formatter.generateFilename(0, 0,  0));
        assertEquals("L3_AD015.png", formatter.generateFilename(2, 3, 14));
    }
}
