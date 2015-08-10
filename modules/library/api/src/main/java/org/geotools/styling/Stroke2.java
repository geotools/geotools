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
package org.geotools.styling;

import org.geotools.filter.ConstantExpression;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;

import java.awt.*;

/**
 * Contains method, representing dasharray as Expression[].
 *
 * @author Igor Volkov
 *
 * @source $URL$
 */
public interface Stroke2 extends Stroke {
    /**
     * This parameter encodes the dash pattern as a sequence of expressions.
     */
    Expression[] getDashExpressionArray();

    /**
     * This parameter encodes the dash pattern as a sequence of expressions.<br>
     * The first expression gives the length in pixels of the dash to draw, the
     * second gives the amount of space to leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by
     * repeating it twice to give an even number of values.
     *
     * For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    void setDashExpressionArray(Expression[] dashExpressionArray);

    /**
     * Default Stroke capturing the defaults indicated by the standard.
     */
    static final Stroke2 DEFAULT = new ConstantStroke2() {
        public Expression[] getDashExpressionArray() {
            return null;
        }
        public Expression getColor() {
            return ConstantExpression.BLACK;
        }

        public Color getColor(SimpleFeature f) {
            return Color.BLACK;
        }

        public Expression getWidth() {
            return ConstantExpression.ONE;
        }

        public Expression getOpacity() {
            return ConstantExpression.ONE;
        }

        public Expression getLineJoin() {
            return ConstantExpression.constant("miter");
        }

        public Expression getLineCap() {
            return ConstantExpression.constant("butt");
        }

        public float[] getDashArray() {
            return null;
        }

        public Expression getDashOffset() {
            return ConstantExpression.ZERO;
        }

        public Graphic getGraphicFill() {
            return Graphic.DEFAULT;
        }

        public Graphic getGraphicStroke() {
            return Graphic.NULL;
        }

        public Object clone() {
            return this; // we are constant
        }
    };

    /**
     * Null Stroke capturing the defaults indicated by the standard.
     * <p>
     * This is a NullObject, it purpose is to prevent client code from having
     * to do null checking.
     * </p>
     */
    static final Stroke2 NULL = new ConstantStroke2() {
        public Expression[] getDashExpressionArray() {
            return new Expression[] {  };
        }
        public Expression getColor() {
            return ConstantExpression.NULL;
        }

        public Color getColor(SimpleFeature f) {
            return Color.BLACK;
        }

        public Expression getWidth() {
            return ConstantExpression.NULL;
        }

        public Expression getOpacity() {
            return ConstantExpression.NULL;
        }

        public Expression getLineJoin() {
            return ConstantExpression.NULL;
        }

        public Expression getLineCap() {
            return ConstantExpression.NULL;
        }

        public float[] getDashArray() {
            return new float[] {  };
        }

        public Expression getDashOffset() {
            return ConstantExpression.NULL;
        }

        public Graphic getGraphicFill() {
            return Graphic.NULL;
        }

        public Graphic getGraphicStroke() {
            return Graphic.NULL;
        }
    };

    abstract class ConstantStroke2 implements Stroke2 {
        public void setDashExpressionArray(Expression[] dashExpressionArray) {
            cannotModifyConstant();
        }
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Stroke may not be modified");
        }

        public void setColor(Expression color) {
            cannotModifyConstant();
        }

        public void setWidth(Expression width) {
            cannotModifyConstant();
            ;
        }

        public void setOpacity(Expression opacity) {
            cannotModifyConstant();
        }

        public void setLineJoin(Expression lineJoin) {
            cannotModifyConstant();
        }

        public void setLineCap(Expression lineCap) {
            cannotModifyConstant();
        }

        public void setDashArray(float[] dashArray) {
            cannotModifyConstant();
        }

        public void setDashOffset(Expression dashOffset) {
            cannotModifyConstant();
        }

        public void setGraphicFill(org.opengis.style.Graphic graphicFill) {
            cannotModifyConstant();
        }

        public void setGraphicStroke(org.opengis.style.Graphic graphicStroke) {
            cannotModifyConstant();
        }

        public void accept(org.geotools.styling.StyleVisitor visitor) {
            cannotModifyConstant();
        }

        public Object accept(org.opengis.style.StyleVisitor visitor, Object data) {
            cannotModifyConstant();
            return null;
        }
    }

}
