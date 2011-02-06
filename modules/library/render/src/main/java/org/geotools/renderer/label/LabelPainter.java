/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.LineMetrics;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;
import java.text.Bidi;
import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.media.jai.Interpolation;
import javax.swing.Icon;

import org.geotools.geometry.jts.LiteShape2;
import org.geotools.geometry.jts.TransformedShape;
import org.geotools.renderer.label.LabelCacheImpl.LabelRenderingMode;
import org.geotools.renderer.label.LabelCacheItem.GraphicResize;
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
        labelItem.getTextStyle().setLabel(labelItem.getLabel());

        // reset previous caches
        labelBounds = null;
        lines = null;

        // split the label into lines
        String text = labelItem.getLabel();
        // set the multiline labeller only if we're not using curved labels, and
        // also only if makes sense to have multiple lines (at least a newline
        if (!(text.contains("\n") || labelItem.getAutoWrap() > 0)
                || labelItem.isFollowLineEnabled()) {
            FontRenderContext frc = graphics.getFontRenderContext();
            TextLayout layout = new TextLayout(text, labelItem.getTextStyle().getFont(), frc);
            LineInfo line = new LineInfo(text, layoutSentence(text, labelItem), layout);
            labelBounds = line.gv.getVisualBounds();
            normalizeBounds(labelBounds);
            lines = Collections.singletonList(line);
            return;
        } 
        
        // first split along the newlines
        String[] splitted = text.split("\\n");
        
        lines = new ArrayList<LineInfo>();
        if(labelItem.getAutoWrap() <= 0) {
            // no need for auto-wrapping, we already have the proper split
            for (String line : splitted) {
                FontRenderContext frc = graphics.getFontRenderContext();
                TextLayout layout = new TextLayout(line, labelItem.getTextStyle().getFont(), frc);
                LineInfo info = new LineInfo(line, layoutSentence(line, labelItem), layout);
                lines.add(info);
            }
        } else {
            // Perform an auto-wrap using the java2d facilities. This
            // is done using a LineBreakMeasurer, but first we need to create
            // some extra objects

            // setup the attributes
            Map<TextAttribute, Object> map = new HashMap<TextAttribute, Object>();
            map.put(TextAttribute.FONT, labelItem.getTextStyle().getFont());

            // accumulate the lines
            for (int i = 0; i < splitted.length; i++) {
                String line = splitted[i];

                // build the line break iterator that will split lines at word
                // boundaries when the wrapping length is exceeded
                AttributedString attributed = new AttributedString(line, map);
                AttributedCharacterIterator iter = attributed.getIterator();
                LineBreakMeasurer lineMeasurer = new LineBreakMeasurer(iter, BreakIterator
                        .getWordInstance(), graphics.getFontRenderContext());
                BreakIterator breaks = BreakIterator.getWordInstance();
                breaks.setText(line);

                // setup iteration and start splitting at word boundaries
                int prevPosition = 0;
                while (lineMeasurer.getPosition() < iter.getEndIndex()) {
                    // grab the next portion of text within the wrapping limits
                    TextLayout layout = lineMeasurer.nextLayout(labelItem.getAutoWrap(), line.length(), true);
                    int newPosition = prevPosition;

                    if (layout != null) {
                        newPosition = lineMeasurer.getPosition();
                    } else {
                        int nextBoundary = breaks.following(prevPosition);
                        if (nextBoundary == BreakIterator.DONE) {
                            newPosition = line.length();
                        } else {
                            newPosition = nextBoundary;
                        }
                        AttributedCharacterIterator subIter = attributed.getIterator(null, prevPosition, newPosition);
                        layout = new TextLayout(subIter, graphics.getFontRenderContext());
                        lineMeasurer.setPosition(newPosition);
                    }

                    // extract the text, and trim it since leading and trailing
                    // and ... spaces can affect label alignment in an
                    // unpleasant way (improper left or right alignment, or bad
                    // centering)

                    String extracted = line.substring(prevPosition, newPosition).trim();
                    if(!"".equals(extracted)) {
	                    LineInfo info = new LineInfo(extracted, layoutSentence(extracted, labelItem),
	                            layout);
	                    lines.add(info);
                    }
                    prevPosition = newPosition;
                }
            }
        }
        
        // compute the max line length
        double maxWidth = 0;
        for (LineInfo line : lines) {
            maxWidth = Math.max(line.gv.getVisualBounds().getWidth(), maxWidth);
        }

        // now that we know how big each line and how big is the longest,
        // we can layout the items and compute the total bounds
        double boundsY = 0;
        double labelY = 0;
        for (LineInfo info : lines) {
            Rectangle2D currBounds = info.gv.getVisualBounds();
            TextLayout layout = info.layout;

            // the position at which we start to draw, x and y
            // for x we have to take into consideration alignment as
            // well since that affects the horizontal size of the
            // bounds,
            // for y we don't care right now as we're computing
            // only the total bounds for a text located in the origin
            double minX = (maxWidth - currBounds.getWidth())
                    * labelItem.getTextStyle().getAnchorX() - currBounds.getMinX();
            info.x = minX;

            if (labelBounds == null) {
                labelBounds = currBounds;
                boundsY = currBounds.getMinY() + layout.getAscent() + layout.getDescent()
                        + layout.getLeading();
            } else {
                Rectangle2D translated = new Rectangle2D.Double(minX, boundsY, currBounds
                        .getWidth(), currBounds.getHeight());
                boundsY += layout.getAscent() + layout.getDescent() + layout.getLeading();
                labelY += layout.getAscent() + layout.getDescent() + layout.getLeading();
                labelBounds = labelBounds.createUnion(translated);
            }
            info.y = labelY;
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
     * Turns a string into the corresponding {@link GlyphVector}
     * 
     * @param label
     * @param item
     * @return
     */
    GlyphVector layoutSentence(String label, LabelCacheItem item) {
        final Font font = item.getTextStyle().getFont();
        final char[] chars = label.toCharArray();
        final int length = label.length();
        if (Bidi.requiresBidi(chars, 0, length)) {
            Bidi bidi = new Bidi(label, Bidi.DIRECTION_DEFAULT_LEFT_TO_RIGHT);
            if (bidi.isRightToLeft()) {
                return font.layoutGlyphVector(graphics.getFontRenderContext(), chars, 0, length,
                        Font.LAYOUT_RIGHT_TO_LEFT);
            } else if (bidi.isMixed()) {
                String r = "";
                for (int i=0; i<bidi.getRunCount(); i++) {
                    String s1 = label.substring(bidi.getRunStart(i), bidi.getRunLimit(i));
                    if (bidi.getRunLevel(i)%2==0) {
                        s1 = new StringBuffer(s1).reverse().toString();
                    }
                    r = r + s1;
                }
                char[] chars2 = r.toCharArray();
                return font.layoutGlyphVector(graphics.getFontRenderContext(), chars2, 0, length,
                        Font.LAYOUT_RIGHT_TO_LEFT);
            } 
        } 
        return font.createGlyphVector(graphics.getFontRenderContext(), chars);
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
        return lines.get(0).gv.getVisualBounds().getHeight() - lines.get(0).layout.getDescent();
    }
    
    /**
     * The full size above the baseline 
     * @return
     */
    public double getAscent() {
        return lines.get(0).layout.getAscent();
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

                graphic = resizeGraphic(graphic);
                shapePainter.paint(graphics, tempShape, graphic, graphic.getMaxScale());
            }
            
            // 0 is unfortunately an acceptable value if people only want to draw shields
            // (to leverage conflict resolution, priority when placing symbols)
            if(labelItem.getTextStyle().getFont().getSize() == 0)
                return;

            // draw the label
            if (lines.size() == 1) {
                drawGlyphVector(lines.get(0).gv);
            } else {
                // for multiline labels we have to go thru the lines and apply
                // the proper transformation
                // to position each row within the label bounds
                AffineTransform lineTx = new AffineTransform(transform);
                for (LineInfo line : lines) {
                    lineTx.setTransform(transform);
                    lineTx.translate(line.x, line.y);
                    graphics.setTransform(lineTx);
                    drawGlyphVector(line.gv);
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
                    resized.setSize((int) Math.round(bounds.getHeight() * width / bounds.getWidth()));
                } else {
                    resized.setSize((int) height);
                }
            } else {
                TransformedShape tss = new TransformedShape();
                tss.shape = original;
                tss.setTransform(AffineTransform.getScaleInstance(width / bounds.getWidth(), height / bounds.getHeight()));
                resized.setShape(tss);
                resized.setSize((int) height);
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
        
        GlyphVector glyphVector = lines.get(0).gv;
        AffineTransform oldTransform = graphics.getTransform();
        try {
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
            final int numGlyphs = glyphVector.getNumGlyphs();
            float nextAdvance = glyphVector.getGlyphMetrics(0).getAdvance() * 0.5f;
            Shape[] outlines = new Shape[numGlyphs];
            AffineTransform[] transforms = new AffineTransform[numGlyphs];
            for (int i = 0; i < numGlyphs; i++) {
                outlines[i] = glyphVector.getGlyphOutline(i);
                Point2D p = glyphVector.getGlyphPosition(i);
                float advance = nextAdvance;
                nextAdvance = i < numGlyphs - 1 ? glyphVector.getGlyphMetrics(i + 1).getAdvance() * 0.5f
                        : 0;

                c = cursor.getCurrentPosition(c);
                AffineTransform t = new AffineTransform();
                t.setToTranslation(c.x, c.y);
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
        } finally {
            graphics.setTransform(oldTransform);
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
        if(lm.getHeight() > 0)
            return (Math.abs(lm.getStrikethroughOffset()) + lm.getDescent() + lm.getLeading())
                    / lm.getHeight();
        else 
            return 0;
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

    /**
     * Core information needed to draw out a line of text
     */
    private static class LineInfo {
        // the coordinates at which the label should be drawn withing the global
        // label bounds (so these are relative coordinates)
        double x;

        double y;

        // the text to be drawn
        String text;

        // the text represented as a glyph vector
        GlyphVector gv;

        // the text layout
        TextLayout layout;

        public LineInfo(String text, GlyphVector gv, TextLayout layout) {
            super();
            this.text = text;
            this.gv = gv;
            this.layout = layout;
        }

        public LineInfo(String text, GlyphVector gv) {
            super();
            this.text = text;
            this.gv = gv;
        }

    }
}
