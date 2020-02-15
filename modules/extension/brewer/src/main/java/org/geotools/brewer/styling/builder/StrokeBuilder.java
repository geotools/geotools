/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.brewer.styling.builder;

import java.awt.Color;
import java.util.List;
import org.geotools.styling.Stroke;
import org.geotools.util.Converters;
import org.opengis.filter.expression.Expression;

public class StrokeBuilder extends AbstractStyleBuilder<Stroke> {
    Expression color;

    Expression width;

    Expression opacity;

    Expression lineCap;

    Expression lineJoin;

    float[] dashArray = null;

    List<Expression> dashArrayExpressions = null;

    Expression dashOffset;

    GraphicBuilder graphicFill = new GraphicBuilder(this).unset();

    GraphicBuilder graphicStroke = new GraphicBuilder(this).unset();

    public StrokeBuilder() {
        this(null);
    }

    public StrokeBuilder(AbstractStyleBuilder<?> parent) {
        super(parent);
        reset();
    }

    public StrokeBuilder unset() {
        return (StrokeBuilder) super.unset();
    }

    /** Reset stroke to default values. */
    public StrokeBuilder reset() {
        color = Stroke.DEFAULT.getColor();
        width = Stroke.DEFAULT.getWidth();
        opacity = Stroke.DEFAULT.getOpacity();
        lineCap = Stroke.DEFAULT.getLineCap();
        lineJoin = Stroke.DEFAULT.getLineJoin();
        dashArray = Stroke.DEFAULT.getDashArray();
        dashArrayExpressions = Stroke.DEFAULT.dashArray();
        dashOffset = Stroke.DEFAULT.getDashOffset();
        graphicFill.unset();
        graphicStroke.unset();
        unset = false;
        return this;
    }

    @Override
    public StrokeBuilder reset(Stroke original) {
        return reset((org.opengis.style.Stroke) original);
    }

    /** Reset builder to provided original stroke. */
    public StrokeBuilder reset(org.opengis.style.Stroke stroke) {
        if (stroke == null) {
            return unset();
        }
        color = stroke.getColor();
        width = stroke.getWidth();
        opacity = stroke.getOpacity();
        lineCap = stroke.getLineCap();
        lineJoin = stroke.getLineJoin();
        dashArray = stroke.getDashArray();
        dashArrayExpressions = (stroke instanceof Stroke) ? ((Stroke) stroke).dashArray() : null;
        dashOffset = stroke.getDashOffset();
        graphicFill.reset(stroke.getGraphicFill());
        graphicStroke.reset(stroke.getGraphicStroke());
        unset = false;
        return this;
    }

    public StrokeBuilder color(Expression color) {
        unset = false;
        this.color = color;
        return this;
    }

    public StrokeBuilder color(Color color) {
        return color(literal(color));
    }

    public StrokeBuilder color(String cqlExpression) {
        return color(cqlExpression(cqlExpression));
    }

    public StrokeBuilder colorHex(String hex) {
        Color color = Converters.convert(hex, Color.class);
        if (color == null) {
            throw new IllegalArgumentException(
                    "The provided expression could not be turned into a color: " + hex);
        }
        return color(color);
    }

    public StrokeBuilder width(Expression width) {
        unset = false;
        this.width = width;
        return this;
    }

    public StrokeBuilder width(double width) {
        return width(literal(width));
    }

    public StrokeBuilder width(String cqlExpression) {
        return width(cqlExpression(cqlExpression));
    }

    public StrokeBuilder opacity(Expression opacity) {
        unset = false;
        this.opacity = opacity;
        return this;
    }

    public StrokeBuilder opacity(double opacity) {
        return opacity(literal(opacity));
    }

    public StrokeBuilder opacity(String cqlExpression) {
        return opacity(cqlExpression(cqlExpression));
    }

    public StrokeBuilder lineCap(Expression lineCap) {
        unset = false;
        this.lineCap = lineCap;
        return this;
    }

    public StrokeBuilder lineCap(String cqlExpression) {
        return lineCap(cqlExpression(cqlExpression));
    }

    public StrokeBuilder lineCapName(String cap) {
        return lineCap(literal(cap));
    }

    public StrokeBuilder lineJoin(Expression lineJoin) {
        unset = false;
        this.lineJoin = lineJoin;
        return this;
    }

    public StrokeBuilder lineJoin(String cqlExpression) {
        return lineJoin(cqlExpression(cqlExpression));
    }

    public StrokeBuilder lineJoinName(String join) {
        return lineJoin(literal(join));
    }

    public StrokeBuilder dashArray(float... dashArray) {
        this.dashArray = dashArray;
        unset = false;
        return this;
    }

    public StrokeBuilder dashArray(List<Expression> dashArrayExpressions) {
        this.dashArrayExpressions = dashArrayExpressions;
        return this;
    }

    public StrokeBuilder dashOffset(Expression dashOffset) {
        this.dashOffset = dashOffset;
        return this;
    }

    public StrokeBuilder dashOffset(double dashOffset) {
        return dashOffset(literal(dashOffset));
    }

    public StrokeBuilder dashOffset(String cqlExpression) {
        return dashOffset(cqlExpression(cqlExpression));
    }

    public GraphicBuilder graphicStroke() {
        unset = false;
        return graphicStroke;
    }

    public GraphicBuilder fillBuilder() {
        unset = false;
        return graphicFill;
    }

    public Stroke build() {
        if (unset) {
            return null;
        }
        Stroke stroke =
                sf.createStroke(
                        color,
                        width,
                        opacity,
                        lineJoin,
                        lineCap,
                        dashArray,
                        dashOffset,
                        graphicFill.build(),
                        this.graphicStroke.build());
        if (dashArrayExpressions != null && !dashArrayExpressions.isEmpty()) {
            stroke.setDashArray(dashArrayExpressions);
        }
        if (parent == null) {
            reset();
        }
        return stroke;
    }

    @Override
    protected void buildStyleInternal(StyleBuilder sb) {
        sb.featureTypeStyle().rule().line().stroke().init(this);
    }
}
