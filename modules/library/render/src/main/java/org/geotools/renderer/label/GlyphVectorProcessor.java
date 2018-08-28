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

import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import org.locationtech.jts.geom.Coordinate;

/** Process all the glyphs of label. */
abstract class GlyphVectorProcessor {

    List<AffineTransform> transforms;
    LabelPainter painter;

    public GlyphVectorProcessor(LabelPainter painter) {
        transforms = new ArrayList<>();
        this.painter = painter;
    }

    public boolean process(GlyphProcessor processor) {
        return process(processor, false);
    }

    public boolean process(GlyphProcessor processor, boolean stopIfTrue) {
        int glyphCount = 0;
        boolean ret = false;
        for (LineInfo lineInfo : painter.lines) {
            for (LineInfo.LineComponent lineComponent : lineInfo.getComponents()) {
                GlyphVector glyphVector = lineComponent.getGlyphVector();
                for (int g = 0; g < glyphVector.getNumGlyphs(); g++, glyphCount++) {
                    char c = getChar(lineComponent, glyphVector, g);
                    // warning : do "process || ret", not "ret || process"
                    ret = processor.process(glyphVector, g, transforms.get(glyphCount), c) || ret;
                    if (ret && stopIfTrue) return true;
                }
            }
        }
        return ret;
    }

    /** Return the character corresponding to this glyphVector index */
    public char getChar(LineInfo.LineComponent component, GlyphVector glyphVector, int g) {
        return component.getText().charAt(glyphVector.getGlyphCharIndex(g));
    }

    /**
     * Create a Straight GlyphVectorProcessor, pre-compute the affine transforms for each glyph
     * along a straight line.
     */
    public static class Straight extends GlyphVectorProcessor {

        public Straight(LabelPainter painter, AffineTransform tx) {
            super(painter);
            for (LineInfo lineInfo : painter.lines) {
                for (LineInfo.LineComponent lineComponent : lineInfo.getComponents()) {
                    AffineTransform componentTx = new AffineTransform(tx);
                    componentTx.translate(lineComponent.getX(), lineInfo.getY());
                    for (int i = 0; i < lineComponent.getGlyphVector().getNumGlyphs(); i++) {
                        transforms.add(componentTx);
                    }
                }
            }
        }
    }

    /**
     * Create a Curved GlyphVectorProcessor, pre-compute the affine transforms for each glyph along
     * a curved line. Does not consider letter orientation (only letter position). (taken from
     * LabelPainter#paintCurvedLabel).
     */
    public static class Curved extends GlyphVectorProcessor {

        public Curved(LabelPainter painter, LineStringCursor cursor) {
            super(painter);

            // for labels following lines, one can only have one line of text
            AffineTransform oldTransform = painter.graphics.getTransform();
            LineInfo lineInfo = painter.lines.get(0);

            // first off, check if we are walking the line so that the label is
            // looking up, if not, reverse the line
            if (!painter.isLabelUpwards(cursor) && painter.getLabel().isForceLeftToRightEnabled()) {
                LineStringCursor reverse = cursor.reverse();
                reverse.moveTo(cursor.getLineStringLength() - cursor.getCurrentOrdinate());
                cursor = reverse;
            }

            // find out the true centering position
            double anchorY = painter.getLinePlacementYAnchor();

            // init, move to the starting position
            double mid = cursor.getCurrentOrdinate();
            Coordinate c = new Coordinate();
            c = cursor.getCurrentPosition(c);

            double startOrdinate = mid - painter.getStraightLabelWidth() / 2;
            if (startOrdinate < 0) startOrdinate = 0;
            cursor.moveTo(startOrdinate);

            final double lineHeight = painter.getLineHeight();
            for (LineInfo.LineComponent lineComponent : lineInfo.getComponents()) {
                GlyphVector gv = lineComponent.getGlyphVector();
                try {
                    final int numGlyphs = gv.getNumGlyphs();
                    float nextAdvance = gv.getGlyphMetrics(0).getAdvance() * 0.5f;
                    double start = cursor.getCurrentOrdinate();
                    for (int i = 0; i < numGlyphs; i++) {
                        Point2D p = gv.getGlyphPosition(i);
                        float advance = nextAdvance;
                        nextAdvance =
                                i < numGlyphs - 1
                                        ? gv.getGlyphMetrics(i + 1).getAdvance() * 0.5f
                                        : 0;
                        c = cursor.getCurrentPosition(c);
                        AffineTransform t = new AffineTransform();
                        t.setToTranslation(c.x, c.y);
                        t.rotate(cursor.getCurrentAngle());
                        t.translate(-p.getX() - advance, -p.getY() + lineHeight * anchorY);
                        transforms.add(t);

                        cursor.moveTo(cursor.getCurrentOrdinate() + advance + nextAdvance);
                    }
                    // take into account eventual spaces at the end of the glyph
                    cursor.moveTo(start + gv.getGlyphPosition(numGlyphs).getX());
                } finally {
                    painter.graphics.setTransform(oldTransform);
                }
            }
        }
    }
}
