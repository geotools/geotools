/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import org.geotools.renderer.label.LineInfo.LineComponent;

/**
 * Helper class splitting a LabelCacheItem text over multiple lines (if necessary due to newlines or
 * autowrap) and each line into horizonal components, {@link LineComponent}, that can be rendered
 * with a single font (each component might require a different font due to different scripts being
 * used in the labels)
 *
 * @author Andrea Aime - GeoSolutions
 */
class LabelSplitter {

    private static final String SINGLE_CHAR_STRING = " ";

    /** Splits a string on spaces between words, keeping the spaces attached to the */
    private static final Pattern WORD_SPLITTER = Pattern.compile("(?<=\\s)(?=\\S)");

    private static final Pattern NEWLINE_SPLITTER = Pattern.compile("\\n");

    public List<LineInfo> layout(LabelCacheItem labelItem, Graphics2D graphics) {
        String text = labelItem.getLabel();
        Font[] fonts = labelItem.getTextStyle().getFonts();

        // split the label into lines
        int textLength = text.length();
        boolean singleFont =
                fonts.length == 1
                        || textLength == fonts[0].canDisplayUpTo(text.toCharArray(), 0, textLength);
        if (!(text.contains("\n") || labelItem.getAutoWrap() > 0) && singleFont) {
            FontRenderContext frc = graphics.getFontRenderContext();
            TextLayout layout = new TextLayout(text, fonts[0], frc);
            LineInfo lineInfo = new LineInfo();
            List<LineComponent> components =
                    buildLineComponents(text, fonts[0], labelItem, graphics, layout);
            components.forEach(c -> lineInfo.add(c));
            return Collections.singletonList(lineInfo);
        }

        // first split along the newlines
        String[] splitted = NEWLINE_SPLITTER.split(text);

        List<LineInfo> lines = new ArrayList<LineInfo>();
        if (labelItem.getAutoWrap() <= 0) {
            // no need for auto-wrapping, we already have the proper split
            for (String line : splitted) {
                line = checkForEmptyLine(line);

                LineInfo lineInfo = new LineInfo();
                List<FontRange> ranges = buildFontRanges(line, fonts);
                for (FontRange range : ranges) {
                    graphics.setFont(range.font);
                    FontRenderContext frc = graphics.getFontRenderContext();
                    TextLayout layout = new TextLayout(range.text, range.font, frc);
                    List<LineComponent> components =
                            buildLineComponents(
                                    range.text, range.font, labelItem, graphics, layout);
                    components.forEach(c -> lineInfo.add(c));
                }
                lines.add(lineInfo);
            }
        } else {
            // Perform an auto-wrap using the java2d facilities. This
            // is done using a LineBreakMeasurer, but first we need to create
            // some extra objects

            // setup the attributes
            Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
            map.put(TextAttribute.FONT, fonts[0]);

            // accumulate the lines
            for (int i = 0; i < splitted.length; i++) {
                String lineText = checkForEmptyLine(splitted[i]);

                // build the line break iterator that will split lines at word
                // boundaries when the wrapping length is exceeded
                List<FontRange> ranges = buildFontRanges(lineText, fonts);
                AttributedString attributed = buildAttributedLine(lineText, ranges);
                AttributedCharacterIterator iter = attributed.getIterator();
                LineBreakMeasurer lineMeasurer =
                        new LineBreakMeasurer(
                                iter,
                                BreakIterator.getLineInstance(),
                                graphics.getFontRenderContext());
                BreakIterator breaks = BreakIterator.getLineInstance();
                breaks.setText(lineText);

                // setup iteration and start splitting at word boundaries
                int prevPosition = 0;
                while (lineMeasurer.getPosition() < iter.getEndIndex()) {
                    // grab the next portion of text within the wrapping limits
                    TextLayout layout =
                            lineMeasurer.nextLayout(
                                    labelItem.getAutoWrap(), lineText.length(), true);
                    int newPosition = prevPosition;

                    if (layout != null) {
                        newPosition = lineMeasurer.getPosition();
                    } else {
                        int nextBoundary = breaks.following(prevPosition);
                        if (nextBoundary == BreakIterator.DONE) {
                            newPosition = lineText.length();
                        } else {
                            newPosition = nextBoundary;
                        }
                        AttributedCharacterIterator subIter =
                                attributed.getIterator(null, prevPosition, newPosition);
                        layout = new TextLayout(subIter, graphics.getFontRenderContext());
                        lineMeasurer.setPosition(newPosition);
                    }

                    // extract the text, and trim it since leading and trailing spaces
                    // can affect label alignment in an unpleasant way (improper left
                    // or right alignment, or bad centering)
                    List<FontRange> lineRanges = getLineRanges(ranges, prevPosition, newPosition);
                    LineInfo lineInfo = new LineInfo();
                    int lastLineRange = lineRanges.size() - 1;
                    int currentLineRange = 0;
                    for (FontRange range : lineRanges) {
                        int start = Math.max(prevPosition, range.startChar);
                        int end = Math.min(newPosition, range.endChar);
                        String extracted = lineText.substring(start, end);
                        if (extracted.isEmpty()) {
                            continue;
                        }
                        if (currentLineRange == 0 && currentLineRange == lastLineRange) {
                            // single string, remote trailing and leading
                            extracted = extracted.trim();
                        } else if (currentLineRange == 0) {
                            // trim leading whitespace
                            extracted = extracted.replaceAll("^\\s+", "");
                        } else if (currentLineRange == lastLineRange) {
                            // trim training whitespace
                            extracted = extracted.replaceAll("\\s+$", "");
                        }
                        currentLineRange++;
                        AttributedCharacterIterator subIter =
                                attributed.getIterator(null, start, end);
                        graphics.setFont(range.font);
                        layout = new TextLayout(subIter, graphics.getFontRenderContext());
                        List<LineComponent> components =
                                buildLineComponents(
                                        extracted, range.font, labelItem, graphics, layout);
                        components.forEach(c -> lineInfo.add(c));
                    }
                    lines.add(lineInfo);
                    prevPosition = newPosition;
                }
            }
        }

        return lines;
    }

    private List<LineComponent> buildLineComponents(
            String text,
            Font font,
            LabelCacheItem labelItem,
            Graphics2D graphics,
            TextLayout layout) {
        final double wordSpacing = labelItem.getWordSpacing();
        if (text.trim().indexOf(' ') == -1 || wordSpacing <= 0) {
            // no word spacing
            LineComponent component =
                    new LineComponent(
                            text, layoutSentence(text, labelItem, graphics, font), layout);
            return Arrays.asList(component);
        } else {
            // java does not support word spacing, we need to fake it. Since the machinery
            // works against LineCompoennts we'll insert extra ones between the words
            // having the right extra size
            String[] parts = WORD_SPLITTER.split(text);
            List<LineComponent> result = new ArrayList<>();
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                LineComponent component =
                        new LineComponent(
                                part, layoutSentence(part, labelItem, graphics, font), layout);
                result.add(component);
                if (i < parts.length - 1) {
                    // add a fake space with a tracking adjusting its size to the
                    // desired extra word spacing
                    double tracking = wordSpacing / font.getSize();
                    Font spacerFont =
                            font.deriveFont(
                                    Collections.singletonMap(TextAttribute.TRACKING, tracking));
                    TextLayout spacerLayout =
                            new TextLayout(
                                    SINGLE_CHAR_STRING,
                                    spacerFont,
                                    graphics.getFontRenderContext());
                    LineComponent spacer =
                            new LineComponent(
                                    SINGLE_CHAR_STRING,
                                    layoutSentence(
                                            SINGLE_CHAR_STRING, labelItem, graphics, spacerFont),
                                    spacerLayout);
                    result.add(spacer);
                }
            }
            return result;
        }
    }

    private List<FontRange> getLineRanges(
            List<FontRange> ranges, int prevPosition, int newPosition) {
        int start = -1;
        int end = ranges.size();
        for (int i = 0; i < ranges.size(); i++) {
            FontRange range = ranges.get(i);
            if (start == -1) {
                if (range.endChar > prevPosition) {
                    start = i;
                } else {
                    continue;
                }
            }
            if (range.startChar > newPosition) {
                end = i;
                break;
            }
        }

        return ranges.subList(start, end);
    }

    private AttributedString buildAttributedLine(String line, List<FontRange> ranges) {
        if (ranges.size() == 1) {
            // create a uniform attribute AttributedString
            Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
            map.put(TextAttribute.FONT, ranges.get(0).font);
            AttributedString as = new AttributedString(line, map);
            return as;
        }

        // build a multifont attributed string
        AttributedString as = new AttributedString(line);
        for (FontRange range : ranges) {
            as.addAttribute(TextAttribute.FONT, range.font, range.startChar, range.endChar);
        }

        return as;
    }

    /**
     * Fix for GEOT-4789: a label line cannot be empty, to avoid exceptions in layout and measuring.
     */
    private String checkForEmptyLine(String line) {
        if (line == null || line.equals("")) {
            return SINGLE_CHAR_STRING;
        }
        return line;
    }

    /** Turns a string into the corresponding {@link GlyphVector} */
    GlyphVector layoutSentence(String label, LabelCacheItem item, Graphics2D graphics, Font font) {
        final char[] chars = label.toCharArray();
        final int length = label.length();
        if (Bidi.requiresBidi(chars, 0, length)) {
            Bidi bidi = new Bidi(label, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
            if (bidi.isRightToLeft()) {
                return font.layoutGlyphVector(
                        graphics.getFontRenderContext(),
                        chars,
                        0,
                        length,
                        Font.LAYOUT_RIGHT_TO_LEFT);
            } else if (bidi.isMixed()) {
                String r = "";
                for (int i = 0; i < bidi.getRunCount(); i++) {
                    String s1 = label.substring(bidi.getRunStart(i), bidi.getRunLimit(i));
                    if (bidi.getRunLevel(i) % 2 == 0) {
                        s1 = new StringBuffer(s1).reverse().toString();
                    }
                    r = r + s1;
                }
                char[] chars2 = r.toCharArray();
                return font.layoutGlyphVector(
                        graphics.getFontRenderContext(),
                        chars2,
                        0,
                        length,
                        Font.LAYOUT_RIGHT_TO_LEFT);
            }
        }
        return font.layoutGlyphVector(graphics.getFontRenderContext(), chars, 0, chars.length, 0);
    }

    List<FontRange> buildFontRanges(String text, Font[] fonts) {
        if (fonts.length == 1) {
            return Arrays.asList(new FontRange(text, 0, text.length(), fonts[0]));
        }

        List<FontRange> result = new ArrayList<>();

        int start = 0;
        int lastSupportedChar = 0;
        char[] chars = text.toCharArray();
        while (start < chars.length) {
            for (int i = 0; i < fonts.length; ) {
                Font font = fonts[i];
                int newPosition = font.canDisplayUpTo(chars, start, chars.length);
                if (newPosition == -1) {
                    result.add(new FontRange(text, start, chars.length, font));
                    start = chars.length;
                    lastSupportedChar = start;
                    break;
                } else if (newPosition > start) {
                    result.add(new FontRange(text, start, newPosition, font));
                    start = newPosition;
                    lastSupportedChar = start;
                    // restart the scan, a previous font might be able to
                    // work off the next text segment
                    if (i > 0) {
                        // start from scratch
                        i = 0;
                    }
                } else {
                    i++;
                }
            }
            if (start < chars.length) {
                // it seems we have some chars that cannot be rendered by any font
                int base = start;
                start++;
                boolean foundFont = false;
                while (start < chars.length && !foundFont) {
                    char curr = chars[start];
                    for (int i = 0; i < fonts.length; i++) {
                        Font font = fonts[i];
                        if (font.canDisplay(curr)) {
                            foundFont = true;
                            result.add(new FontRange(text, base, start, fonts[0]));
                            break;
                        }
                    }
                    if (!foundFont) {
                        start++;
                    }
                }
            }
        }

        if (lastSupportedChar < chars.length) {
            result.add(new FontRange(text, lastSupportedChar, chars.length, fonts[0]));
        }

        return result;
    }

    /**
     * A range of characters that can be rendered with a single font
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class FontRange {
        int startChar;

        int endChar;

        Font font;

        String text;

        public FontRange(String fullText, int startChar, int endChar, Font font) {
            super();
            this.text = fullText.substring(startChar, endChar);
            this.startChar = startChar;
            this.endChar = endChar;
            this.font = font;
        }

        /*
         * (non-Javadoc)
         *
         * @see java.lang.Object#toString()
         */
        @Override
        public String toString() {
            return "FontRange [font=" + font + ", text=" + text + "]";
        }
    }
}
