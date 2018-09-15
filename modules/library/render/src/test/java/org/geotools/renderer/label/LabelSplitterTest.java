/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2018 , Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.renderer.label;

import static org.junit.Assert.assertEquals;

import java.awt.Font;
import java.util.List;
import org.geotools.renderer.label.LabelSplitter.FontRange;
import org.junit.Test;

public class LabelSplitterTest {

    @Test
    public void testFontRangeWithUnrenderableCharsInMiddle() {
        LabelSplitter splitter = new LabelSplitter();
        String text = "Some text ነ𐩬 here";
        Font[] fonts = {
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10)
        };

        List<FontRange> ranges = splitter.buildFontRanges(text, fonts);
        assertEquals(3, ranges.size());

        FontRange startRange = ranges.get(0);
        assertEquals(0, startRange.startChar);
        assertEquals(10, startRange.endChar);
        assertEquals(fonts[0], startRange.font);
        assertEquals("Some text ", startRange.text);

        FontRange middleRange = ranges.get(1);
        assertEquals(10, middleRange.startChar);
        assertEquals(13, middleRange.endChar);
        assertEquals(fonts[0], middleRange.font);
        assertEquals("ነ𐩬", middleRange.text);

        FontRange endRange = ranges.get(2);
        assertEquals(13, endRange.startChar);
        assertEquals(18, endRange.endChar);
        assertEquals(fonts[0], endRange.font);
        assertEquals(" here", endRange.text);
    }

    @Test
    public void testFontRangeWithRenderableCharAtStartAndSpecialCharsAtEnd() {
        LabelSplitter splitter = new LabelSplitter();
        String text = "𦘦字字字字字字字字";
        Font[] fonts = {
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Times New Roman", Font.PLAIN, 10),
            new Font("Lucida", Font.PLAIN, 10)
        };

        List<FontRange> ranges = splitter.buildFontRanges(text, fonts);
        assertEquals(2, ranges.size());

        FontRange startRange = ranges.get(0);
        assertEquals(0, startRange.startChar);
        assertEquals(2, startRange.endChar);
        assertEquals(fonts[0], startRange.font);
        assertEquals("𦘦", startRange.text);

        FontRange endRange = ranges.get(1);
        assertEquals(2, endRange.startChar);
        assertEquals(10, endRange.endChar);
        assertEquals(fonts[5], endRange.font);
        assertEquals("字字字字字字字字", endRange.text);
    }
}
