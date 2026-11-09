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

import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.LineMetrics;
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
 * Helper class splitting a LabelCacheItem text over multiple lines (if necessary due to newlines or autowrap) and each
 * line into horizonal components, {@link LineComponent}, that can be rendered with a single font (each component might
 * require a different font due to different scripts being used in the labels)
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
                fonts.length == 1 || textLength == fonts[0].canDisplayUpTo(text.toCharArray(), 0, textLength);
        if (!(text.contains("\n") || labelItem.getAutoWrap() > 0) && singleFont) {
            LineInfo lineInfo = new LineInfo();
            List<LineComponent> components = buildLineComponents(text, fonts[0], labelItem, graphics);
            components.forEach(c -> lineInfo.add(c));
            return Collections.singletonList(lineInfo);
        }

        // first split along the newlines; text made only of newlines still gets one empty line, so it has bounds
        String[] splitted = NEWLINE_SPLITTER.split(text);
        if (splitted.length == 0) {
            splitted = new String[] {""};
        }

        List<LineInfo> lines = new ArrayList<>();
        if (labelItem.getAutoWrap() <= 0) {
            // no need for auto-wrapping, we already have the proper split
            for (String line : splitted) {
                line = checkForEmptyLine(line);

                LineInfo lineInfo = new LineInfo();
                List<FontRange> ranges = buildFontRanges(line, fonts);
                for (FontRange range : ranges) {
                    graphics.setFont(range.font);
                    List<LineComponent> components = buildLineComponents(range.text, range.font, labelItem, graphics);
                    components.forEach(c -> lineInfo.add(c));
                }
                lines.add(lineInfo);
            }
        } else {
            // Perform an auto-wrap, splitting each line at word boundaries

            // setup the attributes
            Map<TextAttribute, Object> map = new HashMap<>();
            map.put(TextAttribute.FONT, fonts[0]);

            // balanced wrapping aims all lines of the label at the same width, whatever its newlines
            FontRenderContext frc = graphics.getFontRenderContext();
            int wrapWidth = labelItem.getAutoWrap();
            double target = labelItem.isAutoWrapBalanced() ? balancedTarget(splitted, fonts, wrapWidth, frc) : 0;

            // accumulate the lines
            for (int p = 0; p < splitted.length; p++) {
                String lineText = checkForEmptyLine(splitted[p]);
                List<FontRange> ranges = buildFontRanges(lineText, fonts);
                List<Integer> lineEnds = labelItem.isAutoWrapBalanced()
                        ? balancedLineEnds(lineText, advances(lineText, ranges, frc), target, p == splitted.length - 1)
                        : greedyLineEnds(lineText, ranges, wrapWidth, frc);

                int prevPosition = 0;
                for (int newPosition : lineEnds) {
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
                        graphics.setFont(range.font);
                        List<LineComponent> components =
                                buildLineComponents(extracted, range.font, labelItem, graphics);
                        components.forEach(c -> lineInfo.add(c));
                    }
                    lines.add(lineInfo);
                    prevPosition = newPosition;
                }
            }
        }

        return lines;
    }

    /** Ends each line at the last word boundary that fits in the wrap width. */
    private List<Integer> greedyLineEnds(String text, List<FontRange> ranges, int wrapWidth, FontRenderContext frc) {
        AttributedCharacterIterator iter = buildAttributedLine(text, ranges).getIterator();
        LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(iter, BreakIterator.getLineInstance(), frc);
        BreakIterator breaks = BreakIterator.getLineInstance();
        breaks.setText(text);

        List<Integer> ends = new ArrayList<>();
        int position = 0;
        while (lineMeasurer.getPosition() < iter.getEndIndex()) {
            // grab the next portion of text within the wrapping limits, or the next word if even that does not fit
            TextLayout layout = lineMeasurer.nextLayout(wrapWidth, text.length(), true);
            if (layout != null) {
                position = lineMeasurer.getPosition();
            } else {
                int nextBoundary = breaks.following(position);
                position = nextBoundary == BreakIterator.DONE ? text.length() : nextBoundary;
                lineMeasurer.setPosition(position);
            }
            ends.add(position);
        }
        return ends;
    }

    /** Width each line aims at: the label width spread evenly over the fewest lines the wrap width allows. */
    private double balancedTarget(String[] paragraphs, Font[] fonts, int wrapWidth, FontRenderContext frc) {
        double total = 0;
        for (String paragraph : paragraphs) {
            String text = checkForEmptyLine(paragraph);
            for (double advance : advances(text, buildFontRanges(text, fonts), frc)) {
                total += advance;
            }
        }
        return total / Math.max(1, Math.ceil(total / wrapWidth));
    }

    /** Advance of each character, letter spacing included, kerning left out. */
    private double[] advances(String text, List<FontRange> ranges, FontRenderContext frc) {
        double[] advances = new double[text.length()];
        for (FontRange range : ranges) {
            // glyph positions include the font tracking, glyph metrics do not
            GlyphVector gv = range.font.createGlyphVector(frc, range.text);
            for (int i = 0; i < range.text.length(); i++) {
                double x = gv.getGlyphPosition(i).getX();
                advances[range.startChar + i] = gv.getGlyphPosition(i + 1).getX() - x;
            }
        }
        return advances;
    }

    /**
     * Picks the word boundaries that keep every line closest to the target width, even if a line then goes past the
     * wrap width. The idea comes from the Mapbox GL label layout.
     *
     * <p>Trying every split would be slow: each word boundary is a break or not, so 20 words give over 500,000 splits.
     * Instead, the search finds the best layout of the first word, then of the first two words, and so on. Each step
     * reuses the earlier results and adds one new line. For "Galleria Vittorio Emanuele Secondo":
     *
     * <pre>{@code
     * 0 Galleria 1 Vittorio 2 Emanuele 3 Secondo 4      line(a, b): cost of one line from boundary a to boundary b
     *
     * cost[1] = line(0, 1)
     * cost[2] = min(line(0, 2), cost[1] + line(1, 2))
     * cost[3] = min(line(0, 3), cost[1] + line(1, 3), cost[2] + line(2, 3))
     * cost[4] = min(line(0, 4), cost[1] + line(1, 4), cost[2] + line(2, 4), cost[3] + line(3, 4))
     * }</pre>
     *
     * <p>cost[4] is the best layout of the whole label. Reading the winners back from the end gives the lines: cost[4]
     * picked cost[2] + line(2, 4), and cost[2] picked line(0, 2), so "Galleria Vittorio" and "Emanuele Secondo". For n
     * words that is about n^2 / 2 line checks.
     */
    private List<Integer> balancedLineEnds(String text, double[] advances, double target, boolean lastParagraph) {
        // widthBefore[i] is the width of the text before character i
        double[] widthBefore = new double[text.length() + 1];
        for (int i = 0; i < text.length(); i++) {
            widthBefore[i + 1] = widthBefore[i] + advances[i];
        }

        // line boundaries to choose from: the start of the text, then each word boundary up to the end of the text
        BreakIterator words = BreakIterator.getLineInstance();
        words.setText(text);
        List<Integer> boundaries = new ArrayList<>(List.of(0));
        for (int end = words.next(); end != BreakIterator.DONE; end = words.next()) {
            boundaries.add(end);
        }

        // cost[to]: lowest cost of the lines up to boundary to; lineStart[to]: where the last of them starts
        int count = boundaries.size();
        double[] cost = new double[count];
        int[] lineStart = new int[count];
        for (int to = 1; to < count; to++) {
            boolean lastLine = lastParagraph && to == count - 1;
            cost[to] = Double.POSITIVE_INFINITY;
            for (int from = 0; from < to; from++) {
                double width = lineWidth(text, widthBefore, boundaries.get(from), boundaries.get(to));
                double total = cost[from] + lineCost(width, target, lastLine);
                if (total <= cost[to]) {
                    cost[to] = total;
                    lineStart[to] = from;
                }
            }
        }

        // walk back from the end of the text to collect the chosen line ends
        List<Integer> ends = new ArrayList<>();
        for (int to = count - 1; to > 0; to = lineStart[to]) {
            ends.add(0, boundaries.get(to));
        }
        return ends;
    }

    /** Width of the text between start and end, trailing whitespace left out. */
    private static double lineWidth(String text, double[] widthBefore, int start, int end) {
        int last = end;
        while (last > start && Character.isWhitespace(text.charAt(last - 1))) {
            last--;
        }
        return widthBefore[last] - widthBefore[start];
    }

    /** Squared distance from the target width. On the last line, going over costs double and staying under half. */
    private static double lineCost(double width, double target, boolean lastLine) {
        double delta = width - target;
        double cost = delta * delta;
        if (lastLine) {
            cost *= delta > 0 ? 2 : 0.5;
        }
        return cost;
    }

    private List<LineComponent> buildLineComponents(
            String text, Font font, LabelCacheItem labelItem, Graphics2D graphics) {
        final double wordSpacing = labelItem.getWordSpacing();
        if (text.trim().indexOf(' ') == -1 || wordSpacing <= 0) {
            // no word spacing
            GlyphVector gv = layoutSentence(text, labelItem, graphics, font);
            LineMetrics metrics = font.getLineMetrics(text, graphics.getFontRenderContext());
            LineComponent component = new LineComponent(text, gv, metrics);
            return Arrays.asList(component);
        } else {
            // java does not support word spacing, we need to fake it. Since the machinery
            // works against LineCompoennts we'll insert extra ones between the words
            // having the right extra size
            String[] parts = WORD_SPLITTER.split(text);
            List<LineComponent> result = new ArrayList<>();
            for (int i = 0; i < parts.length; i++) {
                String part = parts[i];
                GlyphVector gv = layoutSentence(part, labelItem, graphics, font);
                LineMetrics metrics = font.getLineMetrics(part, graphics.getFontRenderContext());
                LineComponent component = new LineComponent(part, gv, metrics);
                result.add(component);
                if (i < parts.length - 1) {
                    // add a fake space with a tracking adjusting its size to the
                    // desired extra word spacing
                    double tracking = wordSpacing / font.getSize();
                    Font spacerFont = font.deriveFont(Collections.singletonMap(TextAttribute.TRACKING, tracking));
                    GlyphVector gvWhiteSpace = layoutSentence(SINGLE_CHAR_STRING, labelItem, graphics, spacerFont);
                    LineMetrics metricsWhiteSpace =
                            spacerFont.getLineMetrics(SINGLE_CHAR_STRING, graphics.getFontRenderContext());
                    LineComponent spacer = new LineComponent(SINGLE_CHAR_STRING, gvWhiteSpace, metricsWhiteSpace);
                    result.add(spacer);
                }
            }
            return result;
        }
    }

    private List<FontRange> getLineRanges(List<FontRange> ranges, int prevPosition, int newPosition) {
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
            Map<TextAttribute, Object> map = new HashMap<>();
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

    /** Fix for GEOT-4789: a label line cannot be empty, to avoid exceptions in layout and measuring. */
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
                        graphics.getFontRenderContext(), chars, 0, length, Font.LAYOUT_RIGHT_TO_LEFT);
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
                        graphics.getFontRenderContext(), chars2, 0, length, Font.LAYOUT_RIGHT_TO_LEFT);
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
                    for (Font font : fonts) {
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
