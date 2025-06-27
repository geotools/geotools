/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.geotools.api.style.TextSymbolizer.GraphicPlacement.INDEPENDENT;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.geom.AffineTransform;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Icon;
import org.geotools.geometry.jts.LiteShape;
import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.TransformedShape;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.label.LabelCacheItem.GraphicResize;
import org.geotools.renderer.label.LineInfo.LineComponent;
import org.geotools.renderer.lite.StyledShapePainter;
import org.geotools.renderer.style.GraphicStyle2D;
import org.geotools.renderer.style.IconStyle2D;
import org.geotools.renderer.style.MarkStyle2D;
import org.geotools.renderer.style.Style2D;
import org.geotools.renderer.style.TextStyle2D;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.LineString;

/**
 * This class performs the layouting and painting of the single label (leaving the label cache the task to sort labels
 * and locate the best label points)
 *
 * @author Andrea Aime
 */
public class LabelPainter {

    /** Epsilon used for comparisons with 0 */
    static final double EPS = 1e-6;

    /** Delegate shape painter used to paint the graphics below the text */
    StyledShapePainter shapePainter = new StyledShapePainter();

    /** The current label we're tring to draw */
    LabelCacheItem labelItem;

    /** The lines in which the label has been split (if any) */
    List<LineInfo> lines;

    /** The graphics object used during painting */
    Graphics2D graphics;

    /**
     * Whether we draw text using its {@link Shape} outline, or we use a plain
     * {@link Graphics2D#drawGlyphVector(GlyphVector, float, float)} instead
     */
    LabelRenderingMode labelRenderingMode;

    /** Used to build JTS geometries during label painting */
    GeometryFactory gf = new GeometryFactory();

    /** The cached label bounds */
    Rectangle2D labelBounds;

    /** The class in charge of splitting the labels in multiple lines/scripts/fonts */
    LabelSplitter splitter = new LabelSplitter();

    /** Builds a new painter */
    public LabelPainter(Graphics2D graphics, LabelRenderingMode labelRenderingMode) {
        this.graphics = graphics;
        this.labelRenderingMode = labelRenderingMode;
    }

    /**
     * Sets the current label. The label will be laid out according to the label item settings (curved lines, auto
     * wrapping, curved line usage) and the painter will be ready to draw it.
     */
    public void setLabel(LabelCacheItem labelItem) {
        this.labelItem = labelItem;
        TextStyle2D textStyle = labelItem.getTextStyle();
        textStyle.setLabel(labelItem.getLabel());

        // reset previous caches
        labelBounds = null;
        lines = null;

        // layout the label elements
        lines = splitter.layout(labelItem, graphics);

        // compute the max line length
        double maxWidth = 0;
        for (LineInfo line : lines) {
            maxWidth = Math.max(line.getWidth(), maxWidth);
        }

        // now that we know how big each line and how big is the longest,
        // we can layout the items and compute the total bounds
        double boundsY = 0;
        double labelY = 0;
        LineInfo previous = null;
        for (LineInfo info : lines) {
            Rectangle2D currBounds = info.getBounds();

            // the position at which we start to draw, x and y
            // for x we have to take into consideration alignment as
            // well since that affects the horizontal size of the
            // bounds,
            // for y we don't care right now as we're computing
            // only the total bounds for a text located in the origin
            double minX = (maxWidth - currBounds.getWidth()) * textStyle.getAnchorX() - currBounds.getMinX();
            info.setMinX(minX);

            double descentLeading = previous == null ? info.getDescentLeading() : previous.getDescentLeading();
            double lineOffset = currBounds.getHeight() + descentLeading;
            if (labelBounds == null) {
                labelBounds = currBounds;
                boundsY = currBounds.getMinY() + lineOffset;
            } else {
                Rectangle2D translated =
                        new Rectangle2D.Double(minX, boundsY, currBounds.getWidth(), currBounds.getHeight());
                boundsY += lineOffset;
                labelY += lineOffset;
                labelBounds = labelBounds.createUnion(translated);
            }
            info.setY(labelY);
            previous = info;
        }
        normalizeBounds(labelBounds);
    }

    /**
     * If, for any reason, a font size of 0 is provided to the renderer, resulting bounds will become empty and this
     * will ruin most geometric computations dealing with spacing and orientations. Enlarge the envelope a tiny bit
     */
    void normalizeBounds(Rectangle2D bounds) {
        if (bounds != null && bounds.isEmpty()) {
            bounds.setRect(bounds.getCenterX() - 1, bounds.getCenterY() - 1, 2, 2);
        }
    }

    /** Returns the current label item */
    public LabelCacheItem getLabel() {
        return labelItem;
    }

    /** Returns the line height for this label in pixels (for multiline labels, it's the height of the first line) */
    public double getLineHeight() {
        return lines.get(0).getLineHeight();
    }

    /**
     * Returns appropriate line height for the given displacemntY Should give Top line for max displacementY (1) and
     * Bottom line for min displacemntY (0)
     *
     * @return height of appropriate line for passed label displacementY
     */
    public double getLineHeightForAnchorY(double anchorY) {
        // null checks
        if (lines == null) return 0;
        if (lines.isEmpty()) return 0;

        // validation checks
        anchorY = anchorY < 0 ? 0 : anchorY;
        anchorY = anchorY > 1 ? 1 : anchorY;

        if (anchorY == 0) return lines.get(getLineCount() - 1).getLineHeight();
        if (anchorY == 1) return lines.get(0).getLineHeight();
        // return average height
        return lines.stream().mapToDouble(l -> l.getLineHeight()).average().orElse(0);
    }

    /** The full size above the baseline */
    public double getAscent() {
        return lines.get(0).getAscent();
    }

    /** Returns the width of the label, as painted in straight form ( */
    public int getStraightLabelWidth() {
        return (int) Math.round(getLabelBounds().getWidth());
    }

    /** Number of lines for this label (more than 1 if the label has embedded newlines or if we're auto-wrapping it) */
    public int getLineCount() {
        return lines.size();
    }

    /** Get the straight label bounds, taking into account halo, shield and line wrapping */
    public Rectangle2D getFullLabelBounds() {
        // base bounds (clone them, we're going to alter the bounds directly)
        Rectangle2D bounds = (Rectangle2D) getLabelBounds().clone();

        // take into account halo
        int haloRadius = Math.round(
                labelItem.getTextStyle().getHaloFill() != null
                        ? labelItem.getTextStyle().getHaloRadius()
                        : 0);
        bounds.add(bounds.getMinX() - haloRadius, bounds.getMinY() - haloRadius);
        bounds.add(bounds.getMaxX() + haloRadius, bounds.getMaxY() + haloRadius);

        // if there is a shield, expand the bounds to account for it as well
        if (labelItem.getTextStyle().getGraphic() != null) {
            Rectangle2D area = labelItem.getTextStyle().getGraphicDimensions();
            // center the graphics on the labels back
            Rectangle2D shieldBounds;
            // handle the image resizing and margins
            int[] margin = labelItem.getGraphicMargin();
            GraphicResize mode = labelItem.getGraphicsResize();
            if (mode == GraphicResize.STRETCH) {
                // it's really the label bounds + margin
                shieldBounds = applyMargins(margin, bounds);
            } else if (mode == GraphicResize.PROPORTIONAL) {
                // the shield will be inflated in proportion to its size
                double factor = 1;
                if (bounds.getWidth() > bounds.getHeight()) {
                    factor = bounds.getWidth() / area.getWidth();
                } else {
                    factor = bounds.getHeight() / area.getHeight();
                }
                double width = area.getWidth() * factor;
                double height = area.getHeight() * factor;
                shieldBounds = new Rectangle2D.Double(
                        -width / 2 + bounds.getMinX() + bounds.getWidth() / 2,
                        -height / 2 + bounds.getMinY() + bounds.getHeight() / 2,
                        width,
                        height);
                shieldBounds = applyMargins(margin, shieldBounds);
            } else {
                // use the shield natural bounds
                shieldBounds = new Rectangle2D.Double(
                        -area.getWidth() / 2 + bounds.getMinX() + bounds.getWidth() / 2,
                        -area.getHeight() / 2 + bounds.getMinY() + bounds.getHeight() / 2,
                        area.getWidth(),
                        area.getHeight());
            }

            bounds = bounds.createUnion(shieldBounds);
        }

        normalizeBounds(bounds);

        return bounds;
    }

    Rectangle2D applyMargins(int[] margin, Rectangle2D bounds) {
        if (bounds != null && margin != null) {
            double xmin = bounds.getMinX() - margin[3];
            double ymin = bounds.getMinY() - margin[0];
            double width = bounds.getWidth() + margin[1] + margin[3];
            double height = bounds.getHeight() + margin[0] + margin[2];
            return new Rectangle2D.Double(xmin, ymin, width, height);
        } else {
            return bounds;
        }
    }

    /** Get the straight label bounds, without taking into account halo and shield */
    public Rectangle2D getLabelBounds() {
        return labelBounds;
    }

    /** Paints the label as a non curved one. The positioning and rotation are provided by the transformation */
    public void paintStraightLabel(AffineTransform transform) throws Exception {
        paintStraightLabel(transform, null);
    }

    /** Paints the label as a non curved one. The positioning and rotation are provided by the transformation */
    public void paintStraightLabel(AffineTransform transform, Coordinate labelPoint) throws Exception {
        AffineTransform oldTransform = graphics.getTransform();
        try {

            // draw the label shield first, underneath the halo
            Style2D graphic = labelItem.getTextStyle().getGraphic();
            if (graphic != null) {

                Coordinate center;
                if (labelPoint != null && labelItem.getGraphicPlacement() == INDEPENDENT) {
                    center = labelPoint;
                    LiteShape2 tempShape = new LiteShape2(gf.createPoint(center), null, null, false, false);

                    // resize graphic and transform it based on the position of the last line
                    graphic = resizeGraphic(graphic);
                    if (graphic != null) {
                        shapePainter.paint(graphics, tempShape, graphic, graphic.getMaxScale());
                    }
                } else {

                    // take into account the graphic margins, if any
                    double offsetY = 0;
                    double offsetX = 0;
                    final int[] margin = labelItem.getGraphicMargin();
                    if (margin != null) {
                        offsetX = margin[1] - margin[3];
                        offsetY = margin[2] - margin[0];
                    }
                    LineInfo lastLine = lines.get(lines.size() - 1);

                    center = new Coordinate(
                            labelBounds.getMinX() + labelBounds.getWidth() / 2.0 + offsetX,
                            labelBounds.getMinY()
                                    + lastLine.getBounds().getHeight()
                                    - 1.0 * labelBounds.getHeight() / 2.0
                                    + offsetY);
                    LiteShape2 tempShape = new LiteShape2(gf.createPoint(center), null, null, false, false);

                    // resize graphic and transform it based on the position of the last line
                    graphic = resizeGraphic(graphic);
                    if (graphic != null) {
                        AffineTransform graphicTx = new AffineTransform(transform);

                        graphicTx.translate(lastLine.getComponents().get(0).getX(), lastLine.getY());
                        graphics.setTransform(graphicTx);
                        shapePainter.paint(graphics, tempShape, graphic, graphic.getMaxScale());
                    }
                }
            }

            AffineTransform newTransform = new AffineTransform(oldTransform);
            newTransform.concatenate(transform);
            graphics.setTransform(newTransform);

            // 0 is unfortunately an acceptable value if people only want to draw shields
            // (to leverage conflict resolution, priority when placing symbols)
            if (labelItem.getTextStyle().getFont().getSize() == 0) return;

            // draw the label
            if (lines.size() == 1 && lines.get(0).getComponents().size() == 1) {
                LineComponent component = lines.get(0).getComponents().get(0);
                drawGlyphVector(component);

            } else {
                // for multiline labels we have to go thru the lines and apply
                // the proper transformation
                // to position each row within the label bounds
                AffineTransform lineTx = new AffineTransform();
                for (LineInfo line : lines) {
                    for (LineComponent component : line.getComponents()) {
                        lineTx.setTransform(newTransform);
                        lineTx.translate(component.getX(), line.getY());
                        graphics.setTransform(lineTx);
                        drawGlyphVector(component);
                    }
                }
            }
        } finally {
            graphics.setTransform(oldTransform);
        }
    }

    /** Resizes the graphic according to the resize mode, label size and margins */
    Style2D resizeGraphic(Style2D graphic) {
        final GraphicResize mode = labelItem.graphicsResize;

        // if no resize, nothing to do
        if (mode == GraphicResize.NONE || mode == null) {
            return graphic;
        }

        // compute the new width and height
        double width = labelBounds.getWidth();
        double height = labelBounds.getHeight();
        final int[] margin = labelItem.graphicMargin;
        if (margin != null) {
            width += margin[1] + margin[3];
            height += margin[0] + margin[2];
        }

        // just in case someone specified negative margins for some reason
        if (width <= 0 || height <= 0) {
            return null;
        }
        width = Math.max(Math.round(width), 1);
        height = Math.max(Math.round(height), 1);

        if (graphic instanceof MarkStyle2D) {
            MarkStyle2D mark = (MarkStyle2D) graphic;

            Shape original = mark.getShape();
            Rectangle2D bounds = original.getBounds2D();
            MarkStyle2D resized = (MarkStyle2D) mark.clone();
            if (mode == GraphicResize.PROPORTIONAL) {
                if (width > height) {
                    resized.setSize(Math.round(bounds.getHeight() * width / bounds.getWidth()));
                } else {
                    resized.setSize(height);
                }
            } else {
                TransformedShape tss = new TransformedShape();
                tss.shape = original;
                tss.setTransform(
                        AffineTransform.getScaleInstance(width / bounds.getWidth(), height / bounds.getHeight()));
                resized.setShape(tss);
                resized.setSize(height);
            }

            return resized;
        } else if (graphic instanceof IconStyle2D) {
            IconStyle2D iconStyle = (IconStyle2D) graphic;
            IconStyle2D resized = (IconStyle2D) iconStyle.clone();

            final Icon icon = iconStyle.getIcon();
            AffineTransform at;
            if (mode == GraphicResize.PROPORTIONAL) {
                double factor;
                if (width > height) {
                    factor = width / icon.getIconWidth();
                } else {
                    factor = height / icon.getIconHeight();
                }
                at = AffineTransform.getScaleInstance(factor, factor);
            } else {
                at = AffineTransform.getScaleInstance(width / icon.getIconWidth(), height / icon.getIconHeight());
            }
            resized.setIcon(new TransformedIcon(icon, at));
            return resized;
        } else if (graphic instanceof GraphicStyle2D) {
            GraphicStyle2D gstyle = (GraphicStyle2D) graphic;
            GraphicStyle2D resized = (GraphicStyle2D) graphic.clone();
            BufferedImage image = gstyle.getImage();

            AffineTransform at;
            if (mode == GraphicResize.PROPORTIONAL) {
                double factor;
                if (width > height) {
                    factor = width / image.getWidth();
                } else {
                    factor = height / image.getHeight();
                }
                at = AffineTransform.getScaleInstance(factor, factor);
            } else {
                at = AffineTransform.getScaleInstance(width / image.getWidth(), height / image.getHeight());
            }

            AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            image = ato.filter(image, null);
            resized.setImage(image);
            return resized;
        } else {
            return graphic;
        }
    }

    /** Draws the glyph vector respecting the label item options */
    private void drawGlyphVector(LineComponent component) {
        LineMetrics metrics = computeLineMetricsIfNeeded(component);
        GlyphVector gv = component.getGlyphVector();
        java.awt.Shape outline = gv.getOutline();
        if (labelItem.getTextStyle().getHaloFill() != null) {
            configureHalo();
            graphics.draw(outline);
            // draw underline and strikethrough halo if needed
            drawStraightLabelUnderlineIfNeeded(outline, metrics, true);
            drawStraightLabelStrikethroughIfNeeded(outline, metrics, true);
        }
        configureLabelStyle();
        // draw the under line and strikethrough
        drawStraightLabelUnderlineIfNeeded(outline, metrics, false);
        drawStraightLabelStrikethroughIfNeeded(outline, metrics, false);
        if (labelRenderingMode == LabelRenderingMode.STRING) {
            graphics.drawGlyphVector(gv, 0, 0);
        } else if (labelRenderingMode == LabelRenderingMode.OUTLINE) {
            graphics.fill(outline);
        } else {
            AffineTransform tx = graphics.getTransform();
            if (Math.abs(tx.getShearX()) >= EPS || Math.abs(tx.getShearY()) > EPS) {
                graphics.fill(outline);
            } else {
                graphics.drawGlyphVector(gv, 0, 0);
            }
        }
    }

    /** Computes a line component metrics only if the current label is underlined. */
    private LineMetrics computeLineMetricsIfNeeded(LineComponent component) {
        if (labelItem.isTextUnderlined() || labelItem.isTextStrikethrough()) {
            return component.computeLineMetrics(graphics.getFontRenderContext());
        }
        return null;
    }

    /**
     * Draws a line under the text with the same color of the text and with the same width using the provided thickness
     * and offset.
     */
    private void drawStraightLabelUnderlineIfNeeded(java.awt.Shape outline, LineMetrics metrics, boolean drawingHalo) {
        // let's see if text underline is enabled for this label or we have something to draw
        if (!labelItem.isTextUnderlined()) {
            // text underline not enabled or nothing to draw
            return;
        }
        // get needed metrics values
        float thickness = metrics.getUnderlineThickness();
        float offset = metrics.getUnderlineOffset() * 2;
        drawStraightLabelLine(outline, drawingHalo, thickness, offset);
    }

    /**
     * Draws a line under the text with the same color of the text and with the same width using the provided thickness
     * and offset.
     */
    private void drawStraightLabelStrikethroughIfNeeded(
            java.awt.Shape outline, LineMetrics metrics, boolean drawingHalo) {
        // let's see if text strikethrough is enabled for this label or we have something to draw
        if (!labelItem.isTextStrikethrough()) {
            // text strikethrough not enabled or nothing to draw
            return;
        }
        // get needed metrics values
        float thickness = metrics.getStrikethroughThickness();
        float offset = metrics.getStrikethroughOffset();
        drawStraightLabelLine(outline, drawingHalo, thickness, offset);
    }

    private void drawStraightLabelLine(java.awt.Shape outline, boolean drawingHalo, float thickness, float offset) {
        Rectangle2D bounds = outline.getBounds2D().getBounds();
        double minX = bounds.getMinX();
        double maxX = bounds.getMaxX();
        if (Math.abs(maxX - minX) < 0.0000001) {
            // nothing to draw
            return;
        }

        // let's se if we are drawing the halo around the underline line
        if (drawingHalo) {
            // when drawing the halo we assume that the correct halo configuration has been set
            graphics.draw(new Line2D.Double(minX, offset, maxX, offset));
        } else {
            // storing the current stroke and setting the stroke according to the specified
            // thickness
            Stroke currentStroke = graphics.getStroke();
            graphics.setStroke(new BasicStroke(thickness));
            // we draw a line with the same color of the text and a stroke of 2
            graphics.draw(new Line2D.Double(minX, offset, maxX, offset));
            // we need to restore the previous stroke
            graphics.setStroke(currentStroke);
        }
    }

    /** Configures the graphic to do the halo drawing */
    private void configureHalo() {
        graphics.setPaint(labelItem.getTextStyle().getHaloFill());
        graphics.setComposite(labelItem.getTextStyle().getHaloComposite());
        float haloRadius = labelItem.getTextStyle().getHaloFill() != null
                ? labelItem.getTextStyle().getHaloRadius()
                : 0;
        graphics.setStroke(new BasicStroke(2 * haloRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /** Configures the graphic to do the text drawing */
    private void configureLabelStyle() {
        // DJB: added this because several people were using
        // "font-color" instead of fill
        // It legal to have a label w/o fill (which means dont
        // render it)
        // This causes people no end of trouble.
        // If they dont want to colour it, then they should use a
        // filter
        // DEFAULT (no <Fill>) --> BLACK
        // NOTE: re-reading the spec says this is the correct
        // assumption.
        Paint fill = labelItem.getTextStyle().getFill();
        Composite comp = labelItem.getTextStyle().getComposite();
        if (fill == null) {
            fill = Color.BLACK;
            comp = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f); // 100%
            // opaque
        }
        graphics.setPaint(fill);
        graphics.setComposite(comp);
    }

    /** Paints a label that follows the line, centered in the current cursor position */
    public void paintCurvedLabel(LineStringCursor cursor) {
        // 0 is unfortunately an acceptable value if people only want to draw shields
        if (labelItem.getTextStyle().getFont().getSize() == 0) return;
        AffineTransform oldTransform = graphics.getTransform();
        LineInfo line = lines.get(0);

        // first off, check if we are walking the line so that the label is
        // looking up, if not, reverse the line
        if (!isLabelUpwards(cursor) && labelItem.isForceLeftToRightEnabled()) {
            LineStringCursor reverse = cursor.reverse();
            reverse.moveTo(cursor.getLineStringLength() - cursor.getCurrentOrdinate());
            cursor = reverse;
        }

        // find out the true centering position
        double anchorY = getLinePlacementYAnchor();

        // init, move to the starting position
        double mid = cursor.getCurrentOrdinate();
        Coordinate c = new Coordinate();
        c = cursor.getCurrentPosition(c);
        graphics.setPaint(Color.BLACK);

        double startOrdinate = mid - getStraightLabelWidth() / 2;
        if (startOrdinate < 0) startOrdinate = 0;
        cursor.moveTo(startOrdinate);

        // store the computed outlines an transformations
        List<Shape[]> allOutlines = new ArrayList<>();
        List<AffineTransform[]> allTransforms = new ArrayList<>();

        try {
            for (LineComponent component : line.getComponents()) {
                GlyphVector glyphVector = component.getGlyphVector();
                final int numGlyphs = glyphVector.getNumGlyphs();
                float nextAdvance = glyphVector.getGlyphMetrics(0).getAdvance() * 0.5f;
                double start = cursor.getCurrentOrdinate();
                Shape[] outlines = new Shape[numGlyphs];
                AffineTransform[] transforms = new AffineTransform[numGlyphs];
                final Font font = component.getGlyphVector().getFont();
                Number tracking = (Number) font.getAttributes().get(TextAttribute.TRACKING);
                for (int i = 0; i < numGlyphs; i++) {
                    outlines[i] = glyphVector.getGlyphOutline(i);
                    Point2D p = glyphVector.getGlyphPosition(i);
                    float advance = nextAdvance;
                    if (tracking != null) {
                        advance = advance + font.getSize2D() * tracking.floatValue();
                    }
                    nextAdvance = i < numGlyphs - 1
                            ? glyphVector.getGlyphMetrics(i + 1).getAdvance() * 0.5f
                            : 0;

                    c = cursor.getCurrentPosition(c);
                    AffineTransform t = new AffineTransform(graphics.getTransform());
                    t.translate(c.x, c.y);
                    t.rotate(cursor.getCurrentAngle());
                    t.translate(-p.getX() - advance, -p.getY() + getLineHeight() * anchorY);
                    transforms[i] = t;

                    cursor.moveTo(cursor.getCurrentOrdinate() + advance + nextAdvance);
                }

                allOutlines.add(outlines);
                allTransforms.add(transforms);

                // take into account eventual spaces at the end of the glyph
                cursor.moveTo(start + glyphVector.getGlyphPosition(numGlyphs).getX());
            }

            // draw halo and label
            // extracting label first line component and compute its metrics
            LineComponent component = line.getComponents().get(0);
            final LineMetrics metrics = computeLineMetricsIfNeeded(component);
            if (labelItem.getTextStyle().getHaloFill() != null) {
                configureHalo();
                if (labelItem.isTextUnderlined()) {
                    // we need to draw the underline halo
                    drawCurvedUnderline(line, cursor, startOrdinate, true, metrics);
                }
                if (labelItem.isTextStrikethrough()) {
                    // we need to draw the strikethrough halo
                    drawCurvedStrikethrough(line, cursor, startOrdinate, true, metrics);
                }
                drawOrFillOutlines(allOutlines, allTransforms, false);
            }
            graphics.setTransform(oldTransform);
            configureLabelStyle();
            if (labelItem.isTextUnderlined()) {
                drawCurvedUnderline(line, cursor, startOrdinate, false, metrics);
            }
            if (labelItem.isTextStrikethrough()) {
                drawCurvedStrikethrough(line, cursor, startOrdinate, false, metrics);
            }
            drawOrFillOutlines(allOutlines, allTransforms, true);
        } finally {
            graphics.setTransform(oldTransform);
        }
    }

    /** Helper method that will draw the underline of a curved label using the context of the cursor. */
    private void drawCurvedUnderline(
            LineInfo line, LineStringCursor cursor, double startOrdinate, boolean drawingHalo, LineMetrics metrics) {
        final float lineOffset = metrics.getUnderlineOffset() * 2;
        final float lineThickness = metrics.getUnderlineThickness();
        drawCurvedLine(line, cursor, startOrdinate, drawingHalo, lineOffset, lineThickness);
    }

    /** Helper method that will draw the underline of a curved label using the context of the cursor. */
    private void drawCurvedStrikethrough(
            LineInfo line, LineStringCursor cursor, double startOrdinate, boolean drawingHalo, LineMetrics metrics) {
        final float lineOffset = metrics.getStrikethroughOffset();
        final float lineThickness = metrics.getStrikethroughThickness();
        drawCurvedLine(line, cursor, startOrdinate, drawingHalo, lineOffset, lineThickness);
    }

    private void drawCurvedLine(
            LineInfo line,
            LineStringCursor cursor,
            double startOrdinate,
            boolean drawingHalo,
            final float lineOffset,
            final float lineThickness) {
        // the cursor is in the last char of the label
        double endOrdinate = cursor.getCurrentOrdinate();
        // compute the advance based on the first char of the label
        GlyphVector glyphVector = line.getComponents().get(0).getGlyphVector();
        double advance = glyphVector.getGlyphMetrics(0).getAdvance() * 0.5f;
        // extract from the linestring the portion associated with the layer
        LineString labelLineString = cursor.getSubLineString(startOrdinate - advance, endOrdinate - advance);
        // compute the underline linestring

        LiteShape underlineLineString = computeCurvedLine(labelLineString, lineOffset);
        if (drawingHalo) {
            // when drawing the halo we assume that the correct halo configuration has been set
            graphics.draw(underlineLineString);
        } else {
            // string the current stroke to restore it back
            Stroke oldStroke = graphics.getStroke();
            try {
                // if we are not drawing the halo we need to set the proper stroke

                graphics.setStroke(new BasicStroke(lineThickness));
                // draw the underline
                graphics.draw(underlineLineString);
            } finally {
                graphics.setStroke(oldStroke);
            }
        }
    }

    /** Helper method that go through all the outlines and transformations a draw or fill them. */
    private void drawOrFillOutlines(List<Shape[]> allOutlines, List<AffineTransform[]> allTransforms, boolean fill) {
        for (int i = 0; i < allOutlines.size(); i++) {
            Shape[] outlines = allOutlines.get(i);
            AffineTransform[] transforms = allTransforms.get(i);
            int numGlyphs = outlines.length;
            for (int j = 0; j < numGlyphs; j++) {
                graphics.setTransform(transforms[j]);
                if (fill) {
                    graphics.fill(outlines[j]);
                } else {
                    graphics.draw(outlines[j]);
                }
            }
        }
    }

    /**
     * Given the portion of the linestring associated with the label and label metrics, this method will compute a
     * proper underline.
     *
     * @param lineOffset TODO
     */
    private LiteShape computeCurvedLine(LineString labelLineString, float lineOffset) {
        Coordinate[] coordinates = labelLineString.getCoordinates();
        Coordinate[] parallelCoordinates = new Coordinate[coordinates.length];
        double anchorOffset = getLinePlacementYAnchor() * getLineHeight();
        for (int i = 0; i < coordinates.length - 1; i++) {
            // let's compute some basic info for the current segment
            Coordinate coordinateA = coordinates[i];
            Coordinate coordinateB = coordinates[i + 1];
            double dx = coordinateB.x - coordinateA.x;
            double dy = coordinateB.y - coordinateA.y;
            double length = Math.sqrt(Math.pow(dx, 2) + Math.pow(dy, 2));
            double offset = -(anchorOffset + lineOffset);
            // compute the parallel coordinates
            double x1 = coordinateA.x + offset * (coordinateB.y - coordinateA.y) / length;
            double x2 = coordinateB.x + offset * (coordinateB.y - coordinateA.y) / length;
            double y1 = coordinateA.y + offset * (coordinateA.x - coordinateB.x) / length;
            double y2 = coordinateB.y + offset * (coordinateA.x - coordinateB.x) / length;
            parallelCoordinates[i] = new Coordinate(x1, y1);
            parallelCoordinates[i + 1] = new Coordinate(x2, y2);
        }
        // build the parallel linestring and wrap it in a lite shape
        LineString lineString = labelLineString.getFactory().createLineString(parallelCoordinates);
        return new LiteShape(lineString, null, true);
    }

    /**
     * Vertical centering is not trivial, because visually we want centering on characters such as a,m,e, and not
     * centering on d,g whose center is affected by the full ascent or the full descent. This method tries to computes
     * the y anchor taking into account those.
     */
    public double getLinePlacementYAnchor() {
        LabelCacheItem item = getLabel();
        TextStyle2D textStyle = item.getTextStyle();
        LineMetrics lm = textStyle.getFont().getLineMetrics(item.getLabel(), graphics.getFontRenderContext());

        // gracefully handle font size = 0
        if (lm.getHeight() > 0) {
            return (Math.abs(lm.getStrikethroughOffset()) + lm.getDescent() + lm.getLeading()) / lm.getHeight();
        } else {
            return 0;
        }
    }

    /**
     * Returns true if a label placed in the current cursor position would look upwards or not, defining upwards a label
     * whose bottom to top direction is greater than zero, and less or equal to 180 degrees.
     */
    boolean isLabelUpwards(LineStringCursor cursor) {
        // label angle is orthogonal to the line direction
        double labelAngle = cursor.getCurrentAngle() + Math.PI / 2;
        // normalize the angle so that it's comprised between 0 and 360°
        labelAngle = labelAngle % (Math.PI * 2);
        return labelAngle >= 0 && labelAngle < Math.PI;
    }
}
