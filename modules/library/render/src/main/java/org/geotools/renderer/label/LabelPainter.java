/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2015, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Composite;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.util.List;

import javax.swing.Icon;

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

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

/**
 * This class performs the layouting and painting of the single label (leaving
 * the label cache the task to sort labels and locate the best label points)
 * 
 * @author Andrea Aime
 * 
 *
 *
 *
 * @source $URL$
 */
public class LabelPainter {
    
    /**
     * Epsilon used for comparisons with 0
     */
    static final double EPS = 1e-6;
    
    /**
     * Delegate shape painter used to paint the graphics below the text
     */
    StyledShapePainter shapePainter = new StyledShapePainter();

    /**
     * The current label we're tring to draw
     */
    LabelCacheItem labelItem;

    /**
     * The lines in which the label has been split (if any)
     */
    List<LineInfo> lines;

    /**
     * The graphics object used during painting
     */
    Graphics2D graphics;

    /**
     * Whether we draw text using its {@link Shape} outline, or we use a plain
     * {@link Graphics2D#drawGlyphVector(GlyphVector, float, float)} instead
     */
    LabelRenderingMode labelRenderingMode;

    /**
     * Used to build JTS geometries during label painting
     */
    GeometryFactory gf = new GeometryFactory();

    /**
     * The cached label bounds
     */
    Rectangle2D labelBounds;

    /**
     * The class in charge of splitting the labels in multiple lines/scripts/fonts
     */
    LabelSplitter splitter = new LabelSplitter();

    /**
     * Builds a new painter
     * 
     * @param graphics
     * @param outlineRenderingEnabled
     */
    public LabelPainter(Graphics2D graphics, LabelRenderingMode labelRenderingMode) {
        this.graphics = graphics;
        this.labelRenderingMode = labelRenderingMode;
    }

    /**
     * Sets the current label. The label will be laid out according to the label
     * item settings (curved lines, auto wrapping, curved line usage) and the
     * painter will be ready to draw it.
     * 
     * @param labelItem
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
        for (LineInfo info : lines) {
            Rectangle2D currBounds = info.getBounds();

            // the position at which we start to draw, x and y
            // for x we have to take into consideration alignment as
            // well since that affects the horizontal size of the
            // bounds,
            // for y we don't care right now as we're computing
            // only the total bounds for a text located in the origin
            double minX = (maxWidth - currBounds.getWidth()) * textStyle.getAnchorX()
                    - currBounds.getMinX();
            info.setMinX(minX);

            double lineOffset = info.getLineOffset();
            if (labelBounds == null) {
                labelBounds = currBounds;
                boundsY = currBounds.getMinY() + lineOffset;
            } else {
                Rectangle2D translated = new Rectangle2D.Double(minX, boundsY,
                        currBounds.getWidth(), currBounds.getHeight());
                boundsY += lineOffset;
                labelY += lineOffset;
                labelBounds = labelBounds.createUnion(translated);
            }
            info.setY(labelY);
        }
        normalizeBounds(labelBounds);
    }





    /**
     * If, for any reason, a font size of 0 is provided to the renderer, resulting bounds
     * will become empty and this will ruin most geometric computations dealing with spacing
     * and orientations. Enlarge the envelope a tiny bit
     * @param bounds
     */
    void normalizeBounds(Rectangle2D bounds) {
        if(bounds.isEmpty()) {
            bounds.setRect(bounds.getCenterX() -1 , bounds.getCenterY() -1, 2, 2);
        }
    }


    /**
     * Returns the current label item
     * 
     * @return
     */
    public LabelCacheItem getLabel() {
        return labelItem;
    }

    /**
     * Returns the line height for this label in pixels (for multiline labels,
     * it's the height of the first line)
     * 
     * @return
     */
    public double getLineHeight() {
        return lines.get(0).getLineHeight();
    }
    
    /**
     * The full size above the baseline 
     * @return
     */
    public double getAscent() {
        return lines.get(0).getAscent();
    }

    /**
     * Returns the width of the label, as painted in straight form (
     * 
     * @return
     */
    public int getStraightLabelWidth() {
        return (int) Math.round(getLabelBounds().getWidth());
    }

    /**
     * Number of lines for this label (more than 1 if the label has embedded
     * newlines or if we're auto-wrapping it)
     * 
     * @return
     */
    public int getLineCount() {
        return lines.size();
    }

    /**
     * Get the straight label bounds, taking into account halo, shield and line
     * wrapping
     * 
     * @return
     */
    public Rectangle2D getFullLabelBounds() {
        // base bounds (clone them, we're going to alter the bounds directly)
        Rectangle2D bounds = (Rectangle2D) getLabelBounds().clone();

        // take into account halo
        int haloRadius = Math.round(labelItem.getTextStyle().getHaloFill() != null ? labelItem
                .getTextStyle().getHaloRadius() : 0);
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
                shieldBounds = new Rectangle2D.Double(width / 2 + bounds.getMinX() - bounds.getWidth() / 2, 
                        height / 2 + bounds.getMinY() - bounds.getHeight() / 2, width, height);
                shieldBounds = applyMargins(margin, shieldBounds);
            } else {
                // use the shield natural bounds
                shieldBounds = new Rectangle2D.Double(-area.getWidth() / 2 + bounds.getMinX()
                        - bounds.getWidth() / 2, -area.getHeight() / 2 + bounds.getMinY()
                        - bounds.getHeight() / 2, area.getWidth(), area.getHeight());
            }

            bounds = bounds.createUnion(shieldBounds);
        }
        
        normalizeBounds(bounds);

        return bounds;
    }
    
    Rectangle2D applyMargins(int[] margin, Rectangle2D bounds) {
        if(bounds != null) {
            double xmin = bounds.getMinX() - margin[3];
            double ymin = bounds.getMinY() - margin[0];
            double width = bounds.getWidth() + margin[1] + margin[3];
            double height = bounds.getHeight() + margin[0] + margin[2];
            return new Rectangle2D.Double(xmin, ymin, width, height);
        } else {
            return bounds;
        }
    }

    /**
     * Get the straight label bounds, without taking into account halo and
     * shield
     * 
     * @return
     */
    public Rectangle2D getLabelBounds() {
        return labelBounds;
    }

    /**
     * Paints the label as a non curved one. The positioning and rotation are
     * provided by the transformation
     * 
     * @param transform
     * @throws Exception
     */
    public void paintStraightLabel(AffineTransform transform) throws Exception {
        AffineTransform oldTransform = graphics.getTransform();
        try {
            AffineTransform newTransform = new AffineTransform(oldTransform);
            newTransform.concatenate(transform);
            graphics.setTransform(newTransform);

            // draw the label shield first, underneath the halo
            Style2D graphic = labelItem.getTextStyle().getGraphic();
            if (graphic != null) {
                // take into account the graphic margins, if any
                double offsetY = 0;
                double offsetX = 0;
                final int[] margin = labelItem.getGraphicMargin();
                if(margin != null) {
                    offsetX = margin[1] - margin[3];
                    offsetY = margin[2] - margin[0];
                }
                LiteShape2 tempShape = new LiteShape2(gf.createPoint(new Coordinate(labelBounds
                        .getWidth() / 2.0 + offsetX, -1.0 * labelBounds.getHeight() / 2.0 + offsetY)), null, null,
                        false, false);

                // resize graphic and transform it based on the position of the last line
                graphic = resizeGraphic(graphic);
                AffineTransform graphicTx = new AffineTransform(transform);
                LineInfo lastLine = lines.get(lines.size() - 1);
                graphicTx.translate(lastLine.getComponents().get(0).getX(), lastLine.getY());
                graphics.setTransform(graphicTx);
                shapePainter.paint(graphics, tempShape, graphic, graphic.getMaxScale());
            }
            
            // 0 is unfortunately an acceptable value if people only want to draw shields
            // (to leverage conflict resolution, priority when placing symbols)
            if(labelItem.getTextStyle().getFont().getSize() == 0)
                return;

            // draw the label
            if (lines.size() == 1 && lines.get(0).getComponents().size() == 1) {
                drawGlyphVector(lines.get(0).getComponents().get(0).getGlyphVector());
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
                        drawGlyphVector(component.getGlyphVector());
                    }

                }
            }
        } finally {
            graphics.setTransform(oldTransform);
        }
    }

    /**
     * Resizes the graphic according to the resize mode, label size and margins
     * @param graphic
     * @return
     */
    private Style2D resizeGraphic(Style2D graphic) {
        final GraphicResize mode = labelItem.graphicsResize;
        
        // if no resize, nothing to do
        if(mode == GraphicResize.NONE || mode == null) {
            return graphic;
        }
        
        // compute the new width and height
        double width = labelBounds.getWidth();
        double height = labelBounds.getHeight();
        final int[] margin = labelItem.graphicMargin;
        if(margin != null) {
            width +=  margin[1] + margin[3]; 
            height += margin[0] + margin[2];
        } 
        width = Math.round(width);
        height = Math.round(height);
        
        // just in case someone specified negative margins for some reason
        if(width <= 0 || height <= 0) {
            return null;
        }
        
        if(graphic instanceof MarkStyle2D) {
            MarkStyle2D mark = (MarkStyle2D) graphic;
            
            Shape original = mark.getShape();
            Rectangle2D bounds = original.getBounds2D();
            MarkStyle2D resized = (MarkStyle2D) mark.clone();
            if(mode == GraphicResize.PROPORTIONAL) {
                if(width > height) {
                    resized.setSize(Math.round(bounds.getHeight() * width / bounds.getWidth()));
                } else {
                    resized.setSize(height);
                }
            } else {
                TransformedShape tss = new TransformedShape();
                tss.shape = original;
                tss.setTransform(AffineTransform.getScaleInstance(width / bounds.getWidth(), height / bounds.getHeight()));
                resized.setShape(tss);
                resized.setSize(height);
            }
            
            return resized;
        } else if(graphic instanceof IconStyle2D) {
            IconStyle2D iconStyle = (IconStyle2D) graphic;
            IconStyle2D resized = (IconStyle2D) iconStyle.clone();
            
            
            final Icon icon = iconStyle.getIcon();
            AffineTransform at;
            if(mode == GraphicResize.PROPORTIONAL) {
                double factor;
                if(width > height) {
                    factor = width / icon.getIconWidth(); 
                } else {
                    factor = height / icon.getIconHeight();
                }
                at = AffineTransform.getScaleInstance(factor, factor);
            } else {
                at = AffineTransform.getScaleInstance(width / icon.getIconWidth(), 
                        height / icon.getIconHeight());
            }
            resized.setIcon(new TransformedIcon(icon, at));
            return resized;
        } else if(graphic instanceof GraphicStyle2D) {
            GraphicStyle2D gstyle = (GraphicStyle2D) graphic;
            GraphicStyle2D resized = (GraphicStyle2D) graphic.clone();
            BufferedImage image = gstyle.getImage();
            
            AffineTransform at;
            if(mode == GraphicResize.PROPORTIONAL) {
                double factor;
                if(width > height) {
                    factor = width / image.getWidth();
                } else {
                    factor = height / image.getHeight();
                }
                at = AffineTransform.getScaleInstance(factor, factor);
            } else {
                at = AffineTransform.getScaleInstance(width / image.getWidth(), 
                        height / image.getHeight());
            }
            
            AffineTransformOp ato = new AffineTransformOp(at, AffineTransformOp.TYPE_BILINEAR);
            image = ato.filter(image, null);
            resized.setImage(image);
            return resized;
        } else {
            return graphic;
        }
    }

    /**
     * Draws the glyph vector respecting the label item options
     * 
     * @param gv
     */
    private void drawGlyphVector(GlyphVector gv) {
        java.awt.Shape outline = gv.getOutline();
        if (labelItem.getTextStyle().getHaloFill() != null) {
            configureHalo();
            graphics.draw(outline);
        }
        configureLabelStyle();
        
        if(labelRenderingMode == LabelRenderingMode.STRING) {
            graphics.drawGlyphVector(gv, 0, 0);
        } else if(labelRenderingMode == LabelRenderingMode.OUTLINE) {
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

    /**
     * Configures the graphic to do the halo drawing
     */
    private void configureHalo() {
        graphics.setPaint(labelItem.getTextStyle().getHaloFill());
        graphics.setComposite(labelItem.getTextStyle().getHaloComposite());
        float haloRadius = labelItem.getTextStyle().getHaloFill() != null ? labelItem.getTextStyle().getHaloRadius() : 0;
        graphics.setStroke(new BasicStroke(2 * haloRadius, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
    }

    /**
     * Configures the graphic to do the text drawing
     */
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

    /**
     * Paints a label that follows the line, centered in the current cursor
     * position
     * 
     * @param cursor
     */
    public void paintCurvedLabel(LineStringCursor cursor) {
        // 0 is unfortunately an acceptable value if people only want to draw shields
        if(labelItem.getTextStyle().getFont().getSize() == 0)
            return;
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
        if (startOrdinate < 0)
            startOrdinate = 0;
        cursor.moveTo(startOrdinate);
        for (LineComponent component : line.getComponents()) {
            GlyphVector glyphVector = component.getGlyphVector();
            try {
                final int numGlyphs = glyphVector.getNumGlyphs();
                float nextAdvance = glyphVector.getGlyphMetrics(0).getAdvance() * 0.5f;
                double start = cursor.getCurrentOrdinate();
                Shape[] outlines = new Shape[numGlyphs];
                AffineTransform[] transforms = new AffineTransform[numGlyphs];
                for (int i = 0; i < numGlyphs; i++) {
                    outlines[i] = glyphVector.getGlyphOutline(i);
                    Point2D p = glyphVector.getGlyphPosition(i);
                    float advance = nextAdvance;
                    nextAdvance = i < numGlyphs - 1
                            ? glyphVector.getGlyphMetrics(i + 1).getAdvance() * 0.5f : 0;

                    c = cursor.getCurrentPosition(c);
                    AffineTransform t = new AffineTransform(graphics.getTransform());
                    t.translate(c.x, c.y);
                    t.rotate(cursor.getCurrentAngle());
                    t.translate(-p.getX() - advance, -p.getY() + getLineHeight() * anchorY);
                    transforms[i] = t;

                    cursor.moveTo(cursor.getCurrentOrdinate() + advance + nextAdvance);
                }

                // draw halo and label
                if (labelItem.getTextStyle().getHaloFill() != null) {
                    configureHalo();
                    for (int i = 0; i < numGlyphs; i++) {
                        graphics.setTransform(transforms[i]);
                        graphics.draw(outlines[i]);
                    }
                }
                configureLabelStyle();
                for (int i = 0; i < numGlyphs; i++) {
                    graphics.setTransform(transforms[i]);
                    graphics.fill(outlines[i]);
                }

                // take into account eventual spaces at the end of the glyph
                cursor.moveTo(start + glyphVector.getGlyphPosition(numGlyphs).getX());
            } finally {
                graphics.setTransform(oldTransform);
            }
        }

    }

    /**
     * Vertical centering is not trivial, because visually we want centering on
     * characters such as a,m,e, and not centering on d,g whose center is
     * affected by the full ascent or the full descent. This method tries to
     * computes the y anchor taking into account those.
     */
    public double getLinePlacementYAnchor() {
        TextStyle2D textStyle = getLabel().getTextStyle();
        LineMetrics lm = textStyle.getFont().getLineMetrics(textStyle.getLabel(),
                graphics.getFontRenderContext());
        
        // gracefully handle font size = 0
        if (lm.getHeight() > 0) {
            return (Math.abs(lm.getStrikethroughOffset()) + lm.getDescent() + lm.getLeading())
                    / lm.getHeight();
        } else {
            return 0;
        }
    }

    /**
     * Returns true if a label placed in the current cursor position would look
     * upwards or not, defining upwards a label whose bottom to top direction is
     * greater than zero, and less or equal to 180 degrees.
     * 
     * @param cursor
     * @return
     */
    boolean isLabelUpwards(LineStringCursor cursor) {
        // label angle is orthogonal to the line direction
        double labelAngle = cursor.getCurrentAngle() + Math.PI / 2;
        // normalize the angle so that it's comprised between 0 and 360°
        labelAngle = labelAngle % (Math.PI * 2);
        return labelAngle >= 0 && labelAngle < Math.PI;
    }


}
