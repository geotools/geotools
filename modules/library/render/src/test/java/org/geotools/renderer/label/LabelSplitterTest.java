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
import java.awt.Graphics2D;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.referencing.operation.transform.ProjectiveTransform;
import org.geotools.renderer.label.LabelSplitter.FontRange;
import org.geotools.renderer.label.LineInfo.LineComponent;
import org.geotools.renderer.lite.LineTest;
import org.geotools.renderer.style.FontCache;
import org.geotools.renderer.style.TextStyle2D;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;

public class LabelSplitterTest {

    @Before
    public void setUp() throws IOException, FontFormatException {
        FontCache.getDefaultInstance()
                .registerFont(Font.createFont(
                        Font.TRUETYPE_FONT,
                        TestData.getResource(LineTest.class, "Vera.ttf").openStream()));
        FontCache.getDefaultInstance()
                .registerFont(Font.createFont(
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

    /** At 90 pixels "Emanuele Secondo" (94 pixels) does not fit, so the greedy wrap leaves "Secondo" alone. */
    @Test
    public void testAutoWrapGreedy() throws Exception {
        LabelCacheItem item = labelItem("Galleria Vittorio Emanuele Secondo", vera());
        item.setAutoWrap(90);
        assertEquals(List.of("Galleria Vittorio", "Emanuele", "Secondo"), lineTexts(item));
    }

    /** The label is 171 pixels wide: two lines aiming at 85.5 pixels, the second goes past the 90 pixel wrap. */
    @Test
    public void testAutoWrapBalanced() throws Exception {
        LabelCacheItem item = labelItem("Galleria Vittorio Emanuele Secondo", vera());
        item.setAutoWrap(90);
        item.setAutoWrapBalanced(true);
        assertEquals(List.of("Galleria Vittorio", "Emanuele Secondo"), lineTexts(item));
    }

    @Test
    public void testAutoWrapBalancedShortLabel() throws Exception {
        LabelCacheItem item = labelItem("Galleria Vittorio", vera());
        item.setAutoWrap(90);
        item.setAutoWrapBalanced(true);
        assertEquals(List.of("Galleria Vittorio"), lineTexts(item));
    }

    /**
     * The whole label is 303 pixels wide, so all lines aim at 303 / 4 = 75.75 pixels, and only the end of the label
     * counts as the last line. Greedy wrapping, or a target per paragraph, would split it differently.
     */
    @Test
    public void testAutoWrapBalancedNewlines() throws Exception {
        LabelCacheItem item = labelItem("Piazza del Duomo di Milano\nGalleria Vittorio Emanuele Secondo", vera());
        item.setAutoWrap(80);
        item.setAutoWrapBalanced(true);
        assertEquals(
                List.of("Piazza del", "Duomo di Milano", "Galleria Vittorio", "Emanuele Secondo"), lineTexts(item));
    }

    /** Letter spacing adds a pixel per character: 91 pixels, past the 90 pixel wrap, so two lines. */
    @Test
    public void testAutoWrapBalancedLetterSpacing() throws Exception {
        Font tracked = vera().deriveFont(Map.of(TextAttribute.TRACKING, 0.1));
        LabelCacheItem item = labelItem("Galleria Vittorio", tracked);
        item.setAutoWrap(90);
        item.setAutoWrapBalanced(true);
        assertEquals(List.of("Galleria", "Vittorio"), lineTexts(item));
    }

    private Font vera() {
        return FontCache.getDefaultInstance().getFont("Bitstream Vera Sans").deriveFont(10f);
    }

    private LabelCacheItem labelItem(String label, Font font) throws Exception {
        TextStyle2D style = new TextStyle2D();
        style.setFont(font);
        LiteShape2 point = new LiteShape2(
                new GeometryFactory().createPoint(new Coordinate(10, 10)),
                ProjectiveTransform.create(new AffineTransform()),
                null,
                false);
        return new LabelCacheItem("layer", style, point, label, new StyleFactoryImpl().createTextSymbolizer());
    }

    /** Splits the label into lines and returns the text of each one. */
    private List<String> lineTexts(LabelCacheItem item) {
        Graphics2D graphics = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics();
        try {
            return new LabelSplitter()
                    .layout(item, graphics).stream().map(this::lineText).toList();
        } finally {
            graphics.dispose();
        }
    }

    private String lineText(LineInfo line) {
        return line.getComponents().stream().map(LineComponent::getText).collect(Collectors.joining());
    }
}
