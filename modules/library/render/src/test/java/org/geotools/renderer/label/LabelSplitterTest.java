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
import java.awt.FontFormatException;
import java.io.IOException;
import java.util.List;
import org.geotools.renderer.label.LabelSplitter.FontRange;
import org.geotools.renderer.lite.LineTest;
import org.geotools.renderer.style.FontCache;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

public class LabelSplitterTest {

    @Before
    public void setUp() throws IOException, FontFormatException {
        FontCache.getDefaultInstance()
                .registerFont(
                        Font.createFont(
                                Font.TRUETYPE_FONT,
                                TestData.getResource(LineTest.class, "Vera.ttf").openStream()));
        FontCache.getDefaultInstance()
                .registerFont(
                        Font.createFont(
                                Font.TRUETYPE_FONT,
                                TestData.getResource(LineTest.class, "DroidSansFallback.ttf")
                                        .openStream()));
    }

    @Test
    public void testFontRangeWithUnrenderableCharsInMiddle() {
        LabelSplitter splitter = new LabelSplitter();

        String startText = "Some text ";
        // Represents two characters, namely the Ethiopic Syllable 'Na'
        // and Old South Arabian Letter 'Nun'.
        String naNun = "\u1290\ud802\ude6c";
        String endText = " here";
        String text = startText + naNun + endText;

        Font font = FontCache.getDefaultInstance().getFont("Bitstream Vera Sans");
        Font[] fonts = {font, font, font, font, font, font};

        List<FontRange> ranges = splitter.buildFontRanges(text, fonts);
        assertEquals(3, ranges.size());

        FontRange startRange = ranges.get(0);
        assertEquals(0, startRange.startChar);
        assertEquals(10, startRange.endChar);
        assertEquals(font, startRange.font);
        assertEquals(startText, startRange.text);

        FontRange middleRange = ranges.get(1);
        assertEquals(10, middleRange.startChar);
        assertEquals(13, middleRange.endChar);
        assertEquals(font, middleRange.font);
        assertEquals(naNun, middleRange.text);

        FontRange endRange = ranges.get(2);
        assertEquals(13, endRange.startChar);
        assertEquals(18, endRange.endChar);
        assertEquals(font, endRange.font);
        assertEquals(endText, endRange.text);
    }

    @Test
    public void testFontRangeWithUnrenderableCompositeCharAtStart() {
        Font font = FontCache.getDefaultInstance().getFont("Bitstream Vera Sans");
        Font fallbackFont = FontCache.getDefaultInstance().getFont("Droid Sans Fallback");

        String compositeChar = "\ud859\ude26";
        String remainingChars = "\u5b57\u5b57\u5b57\u5b57\u5b57\u5b57\u5b57\u5b57";

        LabelSplitter splitter = new LabelSplitter();
        String text = compositeChar + remainingChars;
        Font[] fonts = {font, font, font, font, font, fallbackFont};

        List<FontRange> ranges = splitter.buildFontRanges(text, fonts);
        assertEquals(2, ranges.size());

        FontRange startRange = ranges.get(0);
        assertEquals(0, startRange.startChar);
        assertEquals(2, startRange.endChar);
        assertEquals(font, startRange.font);
        assertEquals(compositeChar, startRange.text);

        FontRange endRange = ranges.get(1);
        assertEquals(2, endRange.startChar);
        assertEquals(10, endRange.endChar);
        assertEquals(fallbackFont, endRange.font);
        assertEquals(remainingChars, endRange.text);
    }
}
