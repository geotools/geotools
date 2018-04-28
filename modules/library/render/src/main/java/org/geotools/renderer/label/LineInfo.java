/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */

package org.geotools.renderer.label;

import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.awt.font.LineMetrics;
import java.awt.font.TextLayout;
import java.awt.geom.Rectangle2D;
import java.util.ArrayList;
import java.util.List;

/** Core information needed to draw out a line of text */
class LineInfo {

    /**
     * Part of a line that can be rendered with a uniform font
     *
     * @author Andrea Aime - GeoSolutions
     */
    static class LineComponent {

        // the coordinates at which the label should be drawn within the global
        // label bounds (so these are relative coordinates)
        private double x;

        // the text to be drawn
        private String text;

        // the text represented as a glyph vector
        private GlyphVector gv;

        // the text layout
        private TextLayout layout;

        Rectangle2D visualBounds;

        LineComponent(String text, GlyphVector gv, TextLayout layout) {
            this.text = text;
            this.gv = gv;
            this.layout = layout;
        }

        Rectangle2D getVisualBounds() {
            if (visualBounds == null) {
                visualBounds = gv.getVisualBounds();
            }
            return visualBounds;
        }

        double getX() {
            return x;
        }

        void setX(double x) {
            this.x = x;
        }

        String getText() {
            return text;
        }

        GlyphVector getGlyphVector() {
            return gv;
        }

        TextLayout getLayout() {
            return layout;
        }

        /**
         * Computes some metrics for this part of the line taking in account the provided rendering
         * context. This methods will always recompute the metrics even if the same font rendering
         * context is provided.
         */
        LineMetrics computeLineMetrics(FontRenderContext fontRenderContext) {
            return gv.getFont().getLineMetrics(text, fontRenderContext);
        }
    }

    // the coordinates at which the label should be drawn within the global
    // label bounds (so these are relative coordinates)
    private double y;

    /** The components of the line */
    private List<LineComponent> components;

    LineInfo() {
        components = new ArrayList<>();
    }

    LineInfo(LineComponent component) {
        this();
        components.add(component);
    }

    void add(LineComponent component) {
        components.add(component);
    }

    double getWidth() {
        double width = 0;
        for (LineComponent lineComponent : components) {
            width += lineComponent.getGlyphVector().getLogicalBounds().getWidth();
        }
        return width;
    }

    Rectangle2D getBounds() {
        Rectangle2D vb = null;
        for (LineComponent lineComponent : components) {
            Rectangle2D componentVisualBounds = lineComponent.getGlyphVector().getVisualBounds();
            Rectangle2D componentLogicalBounds = lineComponent.getGlyphVector().getLogicalBounds();
            // the logical bounds include the spaces, we want them in the horizontal direction
            // in order to compose the element in the row, but we need the visual bounds for
            // vertical alignment
            Rectangle2D componentBounds =
                    new Rectangle2D.Double(
                            componentLogicalBounds.getX(),
                            componentVisualBounds.getY(),
                            componentLogicalBounds.getWidth(),
                            componentVisualBounds.getHeight());
            if (vb == null) {
                vb = componentBounds;
            } else {
                Rectangle2D other =
                        new Rectangle2D.Double(
                                vb.getMaxX(),
                                vb.getMinY(),
                                componentBounds.getWidth(),
                                componentBounds.getHeight());
                vb = vb.createUnion(other);
            }
        }

        return vb;
    }

    void setMinX(double minX) {
        double x = minX;
        for (LineComponent component : components) {
            component.setX(x);
            // use the logical bounds to have spaces taken into account
            x += component.getGlyphVector().getLogicalBounds().getWidth();
        }
    }

    float getLineOffset() {
        float offset = Float.NEGATIVE_INFINITY;
        for (LineComponent component : components) {
            float co =
                    component.getLayout().getAscent()
                            + component.getLayout().getDescent()
                            + component.getLayout().getLeading();
            if (co > offset) {
                offset = co;
            }
        }

        return offset;
    }

    double getLineHeight() {
        double height = Float.NEGATIVE_INFINITY;
        for (LineComponent component : components) {
            double ch =
                    component.getGlyphVector().getVisualBounds().getHeight()
                            - component.getLayout().getDescent();
            if (ch > height) {
                height = ch;
            }
        }

        return height;
    }

    float getAscent() {
        float ascent = Float.NEGATIVE_INFINITY;
        for (LineComponent component : components) {
            float ca = component.getLayout().getAscent();
            if (ca > ascent) {
                return ascent;
            }
        }

        return ascent;
    }

    List<LineComponent> getComponents() {
        return components;
    }

    double getY() {
        return y;
    }

    void setY(double y) {
        this.y = y;
    }
}
