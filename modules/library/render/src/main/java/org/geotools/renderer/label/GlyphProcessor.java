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

import java.awt.Rectangle;
import java.awt.font.GlyphVector;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;

/** A class to process glyphs. */
abstract class GlyphProcessor {

    LabelPainter painter;
    LabelCacheItem labelItem;

    public GlyphProcessor(LabelPainter painter) {
        this.painter = painter;
        this.labelItem = painter.labelItem;
    }

    /**
     * Process a glyph
     *
     * @param glyphVector the GlyphVector containing the glyph to process
     * @param g index of the glyph in the GlyphVector
     * @param tx affineTransform to use
     * @param c character to be processed
     * @return a boolean value which exact meaning depends on the concrete implementation
     */
    public abstract boolean process(GlyphVector glyphVector, int g, AffineTransform tx, char c);

    /** Processor used to paint the bounds a glyph */
    public static class BoundsPainter extends GlyphProcessor {

        public BoundsPainter(LabelPainter painter) {
            super(painter);
        }

        public boolean process(GlyphVector glyphVector, int g, AffineTransform tx, char c) {
            if (Character.isWhitespace(c)) return false;
            painter.graphics.draw(
                    tx.createTransformedShape(glyphVector.getGlyphOutline(g)).getBounds2D());
            return true;
        }
    }

    /** Processor used to detect if a glyph has conflict with an other element of the index. */
    public static class ConflictDetector extends GlyphProcessor {

        Rectangle displayArea;
        LabelIndex paintedBounds;
        LabelIndex groupLabels;
        int extraSpace;
        double minDistance;

        public ConflictDetector(
                LabelPainter painter,
                Rectangle displayArea,
                LabelIndex paintedBounds,
                LabelIndex groupLabels) {
            super(painter);
            this.displayArea = displayArea;
            this.paintedBounds = paintedBounds;
            this.groupLabels = groupLabels;
            int space = labelItem.getSpaceAround();
            int haloRadius =
                    Math.round(
                            labelItem.getTextStyle().getHaloFill() != null
                                    ? labelItem.getTextStyle().getHaloRadius()
                                    : 0);
            extraSpace = space + haloRadius;
            minDistance = labelItem.getMinGroupDistance();
        }

        public boolean process(GlyphVector glyphVector, int g, AffineTransform tx, char c) {
            Rectangle2D labelEnvelope =
                    tx.createTransformedShape(glyphVector.getGlyphLogicalBounds(g)).getBounds2D();
            // try to paint the label, the condition under which this happens are complex
            // white space character does not conflict with other labels
            if (Character.isWhitespace(c)) return false;
            else if ((displayArea.contains(labelEnvelope) || labelItem.isPartialsEnabled())
                    && !(labelItem.isConflictResolutionEnabled()
                            && paintedBounds.labelsWithinDistance(labelEnvelope, extraSpace))
                    && !groupLabels.labelsWithinDistance(labelEnvelope, minDistance)) return false;
            else return true; // collision = true
        }
    }

    /** Processor used to add a glyph in an index. */
    public static class IndexAdder extends GlyphProcessor {

        LabelIndex index;

        public IndexAdder(LabelPainter painter, LabelIndex index) {
            super(painter);
            this.index = index;
        }

        public boolean process(GlyphVector glyphVector, int g, AffineTransform tx, char c) {
            if (Character.isWhitespace(c)) return false;
            Rectangle2D labelEnvelope =
                    tx.createTransformedShape(glyphVector.getGlyphOutline(g)).getBounds2D();
            index.addLabel(labelItem, labelEnvelope);
            return true;
        }
    }
}
