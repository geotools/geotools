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
package org.geotools.styling;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.filter.expression.Literal;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.Stroke;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * Provides a Java representation of the Stroke object in an SLD document. A stroke defines how a line is rendered.
 *
 * @author James Macgill, CCG
 * @version $Id$
 */
public class StrokeImpl implements Stroke, Cloneable {
    private FilterFactory filterFactory;
    private Expression color;
    private List<Expression> dashArray;
    private Expression dashOffset;
    private GraphicImpl fillGraphic;
    private GraphicImpl strokeGraphic;
    private Expression lineCap;
    private Expression lineJoin;
    private Expression opacity;
    private Expression width;

    /** Creates a new instance of Stroke */
    protected StrokeImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    protected StrokeImpl(FilterFactory factory) {
        filterFactory = factory;
    }

    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    /**
     * This parameter gives the solid color that will be used for a stroke.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component in the order Red, Green,
     * Blue, prefixed with the hash (#) sign. The hexidecimal digits between A and F may be in either upper or lower
     * case. For example, full red is encoded as "#ff0000" (with no quotation marks). The default color is defined to be
     * black ("#000000"). Note: in CSS this parameter is just called Stroke and not Color.
     *
     * @return The color of the stroke encoded as a hexidecimal RGB value.
     */
    @Override
    public Expression getColor() {
        return color;
    }

    /**
     * This parameter sets the solid color that will be used for a stroke.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component in the order Red, Green,
     * Blue, prefixed with the hash (#) sign. The hexidecimal digits between A and F may be in either upper or lower
     * case. For example, full red is encoded as "#ff0000" (with no quotation marks). The default color is defined to be
     * black ("#000000"). Note: in CSS this parameter is just called Stroke and not Color.
     *
     * @param color The color of the stroke encoded as a hexidecimal RGB value. This must not be null.
     */
    @Override
    public void setColor(Expression color) {
        if (this.color == color) {
            return;
        }
        this.color = color;
    }

    /**
     * This parameter sets the solid color that will be used for a stroke.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component in the order Red, Green,
     * Blue, prefixed with the hash (#) sign. The hexidecimal digits between A and F may be in either upper or lower
     * case. For example, full red is encoded as "#ff0000" (with no quotation marks). The default color is defined to be
     * black ("#000000"). Note: in CSS this parameter is just called Stroke and not Color.
     *
     * @param color The color of the stroke encoded as a hexidecimal RGB value.
     */
    public void setColor(String color) {
        setColor(filterFactory.literal(color));
    }

    /**
     * Shortcut to retrieve dash array in the case where all expressions are literal numbers. Return the default value
     * if one of the expressions is not a literal.
     */
    @Override
    public float[] getDashArray() {
        if (dashArray == null) {
            return DEFAULT.getDashArray();
        }
        float[] values = new float[dashArray.size()];
        int index = 0;
        for (Expression expression : dashArray) {
            if (expression instanceof Literal) {
                Literal literal = (Literal) expression;
                values[index] = literal.evaluate(null, Float.class);
            } else {
                throw new RuntimeException("Dash array is not literal: '" + expression + "'.");
            }
            index++;
        }
        return values;
    }

    /** Shortcut to define dash array using literal numbers. */
    @Override
    public void setDashArray(float[] literalDashArray) {
        if (literalDashArray != null) {
            dashArray = new ArrayList<>(literalDashArray.length);
            for (float value : literalDashArray) {
                dashArray.add(filterFactory.literal(value));
            }
        }
    }

    /**
     * This parameter encodes the dash pattern as a list of expressions.<br>
     * The first expression gives the length in pixels of the dash to draw, the second gives the amount of space to
     * leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by repeating it twice to give an even number of
     * values.
     *
     * <p>For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    @Override
    public List<Expression> dashArray() {
        if (dashArray == null) {
            return DEFAULT.dashArray();
        }
        return dashArray;
    }

    /**
     * This parameter encodes the dash pattern as a list of expressions.<br>
     * The first expression gives the length in pixels of the dash to draw, the second gives the amount of space to
     * leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by repeating it twice to give an even number of
     * values.
     *
     * <p>For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    @Override
    public void setDashArray(List<Expression> dashArray) {
        this.dashArray = dashArray;
    }

    /**
     * This param determines where the dash pattern should start from.
     *
     * @return where the dash should start from.
     */
    @Override
    public Expression getDashOffset() {
        if (dashOffset == null) {
            return DEFAULT.getDashOffset();
        }

        return dashOffset;
    }

    /**
     * This param determines where the dash pattern should start from.
     *
     * @param dashOffset The distance into the dash pattern that should act as the start.
     */
    @Override
    public void setDashOffset(Expression dashOffset) {
        if (dashOffset == null) {
            return;
        }

        this.dashOffset = dashOffset;
    }

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the fill graphic to use.
     *
     * @return The graphic to use as a stipple fill. If null, then no Stipple fill should be used.
     */
    @Override
    public Graphic getGraphicFill() {
        return fillGraphic;
    }

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the fill graphic to use.
     *
     * @param fillGraphic The graphic to use as a stipple fill. If null, then no Stipple fill should be used.
     */
    @Override
    public void setGraphicFill(org.geotools.api.style.Graphic fillGraphic) {
        if (this.fillGraphic == fillGraphic) {
            return;
        }
        this.fillGraphic = GraphicImpl.cast(fillGraphic);
    }

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and specifies the
     * graphic to use. Proper stroking with a linear graphic requires two "hot-spot" points within the space of the
     * graphic to indicate where the rendering line starts and stops. In the case of raster images with no special
     * mark-up, this line will be assumed to be the middle pixel row of the image, starting from the first pixel column
     * and ending at the last pixel column.
     *
     * @return The graphic to use as a linear graphic. If null, then no graphic stroke should be used.
     */
    @Override
    public Graphic getGraphicStroke() {
        return strokeGraphic;
    }

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and specifies the
     * graphic to use. Proper stroking with a linear graphic requires two "hot-spot" points within the space of the
     * graphic to indicate where the rendering line starts and stops. In the case of raster images with no special
     * mark-up, this line will be assumed to be the middle pixel row of the image, starting from the first pixel column
     * and ending at the last pixel column.
     *
     * @param strokeGraphic The graphic to use as a linear graphic. If null, then no graphic stroke should be used.
     */
    @Override
    public void setGraphicStroke(org.geotools.api.style.Graphic strokeGraphic) {
        if (this.strokeGraphic == strokeGraphic) {
            return;
        }
        this.strokeGraphic = GraphicImpl.cast(strokeGraphic);
    }

    /**
     * This parameter controls how line strings should be capped.
     *
     * @return The cap style. This will be one of "butt", "round" and "square" There is no defined default.
     */
    @Override
    public Expression getLineCap() {
        if (lineCap == null) {
            // ConstantExpression.constant("miter")
            return DEFAULT.getLineCap();
        }
        return lineCap;
    }

    /**
     * This parameter controls how line strings should be capped.
     *
     * @param lineCap The cap style. This can be one of "butt", "round" and "square" There is no defined default.
     */
    @Override
    public void setLineCap(Expression lineCap) {
        if (lineCap == null) {
            return;
        }
        this.lineCap = lineCap;
    }

    /**
     * This parameter controls how line strings should be joined together.
     *
     * @return The join style. This will be one of "mitre", "round" and "bevel". There is no defined default.
     */
    @Override
    public Expression getLineJoin() {
        if (lineCap == null) {
            // ConstantExpression.constant("miter")
            return DEFAULT.getLineJoin();
        }
        return lineJoin;
    }

    /**
     * This parameter controls how line strings should be joined together.
     *
     * @param lineJoin The join style. This will be one of "mitre", "round" and "bevel". There is no defined default.
     */
    @Override
    public void setLineJoin(Expression lineJoin) {
        if (lineJoin == null) {
            return;
        }
        this.lineJoin = lineJoin;
    }

    /**
     * This specifies the level of translucency to use when rendering the stroke.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing totally transparent and
     * 1.0 representing totally opaque. A linear scale of translucency is used for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the stroke, where 0.0 is completely transparent and 1.0 is completely opaque.
     */
    @Override
    public Expression getOpacity() {
        if (lineCap == null) {
            return DEFAULT.getOpacity();
        }
        return opacity;
    }

    /**
     * This specifies the level of translucency to use when rendering the stroke.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing totally transparent and
     * 1.0 representing totally opaque. A linear scale of translucency is used for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @param opacity The opacity of the stroke, where 0.0 is completely transparent and 1.0 is completely opaque.
     */
    @Override
    public void setOpacity(Expression opacity) {
        if (opacity == null) {
            return;
        }
        this.opacity = opacity;
    }

    /**
     * This parameter gives the absolute width (thickness) of a stroke in pixels encoded as a float. The default is 1.0.
     * Fractional numbers are allowed but negative numbers are not.
     *
     * @return The width of the stroke in pixels. This may be fractional but not negative.
     */
    @Override
    public Expression getWidth() {
        if (width == null) {
            return filterFactory.literal(1.0);
        }
        return width;
    }

    /**
     * This parameter sets the absolute width (thickness) of a stroke in pixels encoded as a float. The default is 1.0.
     * Fractional numbers are allowed but negative numbers are not.
     *
     * @param width The width of the stroke in pixels. This may be fractional but not negative.
     */
    @Override
    public void setWidth(Expression width) {
        this.width = width;
    }

    @Override
    public String toString() {
        StringBuffer out = new StringBuffer("org.geotools.styling.StrokeImpl:\n");
        out.append("\tColor " + this.color + "\n");
        out.append("\tWidth " + this.width + "\n");
        out.append("\tOpacity " + this.opacity + "\n");
        out.append("\tLineCap " + this.lineCap + "\n");
        out.append("\tLineJoin " + this.lineJoin + "\n");
        out.append("\tDash Array " + this.dashArray + "\n");
        out.append("\tDash Offset " + this.dashOffset + "\n");
        out.append("\tFill Graphic " + this.fillGraphic + "\n");
        out.append("\tStroke Graphic " + this.strokeGraphic);

        return out.toString();
    }

    public java.awt.Color getColor(SimpleFeature feature) {
        return java.awt.Color.decode((String) this.getColor().evaluate(feature));
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Clone the StrokeImpl object.
     *
     * <p>The clone is a deep copy of the original, except for the expression values which are immutable.
     */
    @Override
    public Object clone() {
        try {
            StrokeImpl clone = (StrokeImpl) super.clone();

            if (dashArray != null) {
                clone.setDashArray(new ArrayList<>(dashArray));
            }

            if (fillGraphic != null && fillGraphic instanceof Cloneable) {
                clone.fillGraphic = (GraphicImpl) fillGraphic.clone();
            }

            if (strokeGraphic != null && fillGraphic instanceof Cloneable) {
                clone.strokeGraphic = (GraphicImpl) strokeGraphic.clone();
            }

            return clone;
        } catch (CloneNotSupportedException e) {
            // This will never happen
            throw new RuntimeException("Failed to clone StrokeImpl");
        }
    }

    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (color != null) {
            result = PRIME * result + color.hashCode();
        }

        if (dashOffset != null) {
            result = PRIME * result + dashOffset.hashCode();
        }

        if (fillGraphic != null) {
            result = PRIME * result + fillGraphic.hashCode();
        }

        if (strokeGraphic != null) {
            result = PRIME * result + strokeGraphic.hashCode();
        }

        if (lineCap != null) {
            result = PRIME * result + lineCap.hashCode();
        }

        if (lineJoin != null) {
            result = PRIME * result + lineJoin.hashCode();
        }

        if (opacity != null) {
            result = PRIME * result + opacity.hashCode();
        }

        if (width != null) {
            result = PRIME * result + width.hashCode();
        }

        if (dashArray != null) {
            result = PRIME * result + dashArray.hashCode();
        }

        return result;
    }

    /**
     * Compares this stroke with another stroke for equality.
     *
     * @param oth The other StrokeImpl to compare
     * @return True if this and oth are equal.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth == null) {
            return false;
        }

        if (oth.getClass() != getClass()) {
            return false;
        }

        StrokeImpl other = (StrokeImpl) oth;

        // check the color first - most likely to change
        if (!Utilities.equals(getColor(), other.getColor())) {
            return false;
        }

        // check the width
        if (!Utilities.equals(getWidth(), other.getWidth())) {
            return false;
        }

        if (!Utilities.equals(getLineCap(), other.getLineCap())) {
            return false;
        }

        if (!Utilities.equals(getLineJoin(), other.getLineJoin())) {
            return false;
        }

        if (!Utilities.equals(getOpacity(), other.getOpacity())) {
            return false;
        }

        if (!Utilities.equals(getGraphicFill(), other.getGraphicFill())) {
            return false;
        }

        if (!Utilities.equals(getGraphicStroke(), other.getGraphicStroke())) {
            return false;
        }

        if (!Utilities.equals(dashArray(), other.dashArray())) {
            return false;
        }

        return true;
    }

    static StrokeImpl cast(org.geotools.api.style.Stroke stroke) {
        if (stroke == null) {
            return null;
        } else if (stroke instanceof StrokeImpl) {
            return (StrokeImpl) stroke;
        } else {
            StrokeImpl copy = new StrokeImpl();
            copy.setColor(stroke.getColor());
            copy.setDashArray(stroke.getDashArray());
            copy.setDashOffset(stroke.getDashOffset());
            copy.setGraphicFill(GraphicImpl.cast(stroke.getGraphicFill()));
            copy.setGraphicStroke(GraphicImpl.cast(stroke.getGraphicStroke()));
            copy.setLineCap(stroke.getLineCap());
            copy.setLineJoin(stroke.getLineJoin());
            copy.setOpacity(stroke.getOpacity());
            copy.setWidth(stroke.getWidth());

            return copy;
        }
    }

    public static Stroke DEFAULT = new ConstantStroke() {
        @Override
        public Expression getColor() {
            return ConstantExpression.BLACK;
        }

        @Override
        public Expression getWidth() {
            return ConstantExpression.ONE;
        }

        @Override
        public Expression getOpacity() {
            return ConstantExpression.ONE;
        }

        @Override
        public Expression getLineJoin() {
            return ConstantExpression.constant("miter");
        }

        @Override
        public Expression getLineCap() {
            return ConstantExpression.constant("butt");
        }

        @Override
        public float[] getDashArray() {
            return null;
        }

        @Override
        public List<Expression> dashArray() {
            return null;
        }

        @Override
        public Expression getDashOffset() {
            return ConstantExpression.ZERO;
        }

        @Override
        public Graphic getGraphicFill() {
            return GraphicImpl.DEFAULT;
        }

        @Override
        public Graphic getGraphicStroke() {
            return GraphicImpl.NULL;
        }

        @Override
        public Object clone() {
            return this; // we are constant
        }
    };
    /**
     * Null Stroke capturing the defaults indicated by the standard.
     *
     * <p>This is a NullObject, it purpose is to prevent client code from having to do null checking.
     */
    public static final Stroke NULL = new ConstantStroke() {
        @Override
        public Expression getColor() {
            return ConstantExpression.NULL;
        }

        @Override
        public Expression getWidth() {
            return ConstantExpression.NULL;
        }

        @Override
        public Expression getOpacity() {
            return ConstantExpression.NULL;
        }

        @Override
        public Expression getLineJoin() {
            return ConstantExpression.NULL;
        }

        @Override
        public Expression getLineCap() {
            return ConstantExpression.NULL;
        }

        @Override
        public float[] getDashArray() {
            return new float[] {};
        }

        @Override
        public List<Expression> dashArray() {
            return Collections.emptyList();
        }

        @Override
        public Expression getDashOffset() {
            return ConstantExpression.NULL;
        }

        @Override
        public Graphic getGraphicFill() {
            return GraphicImpl.NULL;
        }

        @Override
        public Graphic getGraphicStroke() {
            return GraphicImpl.NULL;
        }
    };
}
