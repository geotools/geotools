/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2008, Open Geospatial Consortium Inc.
 *
 *    All Rights Reserved. http://www.opengis.org/legal/
 */
package org.geotools.api.style;

import java.util.List;
import org.geotools.api.filter.expression.Expression;

/**
 * Contains all the information needed to draw styled lines. Stroke objects are contained by {@link
 * LineSymbolizer}s and {@link PolygonSymbolizer}s. There are three basic types of strokes:
 * solid-color, {@code GraphicFill} (stipple), and repeated linear {@code GraphicStroke}. A repeated
 * linear graphic is plotted linearly and has its graphic symbol bent around the curves of the line
 * string, and a graphic fill has the pixels of the line rendered with a repeating area-fill
 * pattern. If neither a {@link #getGraphicFill GraphicFill} nor {@link #getGraphicStroke
 * GraphicStroke} element is given, then the line symbolizer will render a solid color.
 *
 * @version <A HREF="http://www.opengeospatial.org/standards/symbol">Symbology Encoding
 *     Implementation Specification 1.1.0</A>
 * @author Open Geospatial Consortium
 * @author Johann Sorel (Geomatys)
 * @author Chris Dillard (SYS Technologies)
 * @since GeoAPI 2.2
 */
public interface Stroke {

    /**
     * Default Stroke capturing the defaults indicated by the standard.
     *
     * <p>For some attributes the standard does not define a default, so a reasonable value is
     * supplied.
     */

    /**
     * A dash array need not start from the beginning. This method allows for an offset into the
     * dash array before starting it.
     */
    void setDashOffset(Expression dashOffset);

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     *
     * @return The graphic to use as a stipple fill. If null, then no Stipple fill should be used.
     */
    Graphic getGraphicFill();

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     */
    void setGraphicFill(Graphic graphicFill);

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and
     * specifies the graphic to use.
     *
     * <p>Proper stroking with a linear graphic requires two "hot-spot" points within the space of
     * the graphic to indicate where the rendering line starts and stops. In the case of raster
     * images with no special mark-up, this line will be assumed to be the middle pixel row of the
     * image, starting from the first pixel column and ending at the last pixel column.
     *
     * @return The graphic to use as a linear graphic. If null, then no graphic stroke should be
     *     used.
     */
    Graphic getGraphicStroke();

    // *************************************************************
    // SVG PARAMETERS
    // *************************************************************

    /**
     * Indicates the color of the line if it is to be solid-color filled. The format of color values
     * is {@code "#rrggbb"} where {@code rr}, {@code gg}, and {@code bb}, are red, green, and blue
     * intensity values, respectively, represented as two digit hexadecimal integers. The
     * hexadecimal digits between {@code A} and {@code F} may be in either uppercase or lowercase.
     * If null, the default color is {@code "#000000"}, black.
     *
     * @return Expression
     */
    Expression getColor();

    /**
     * Indicates the level of translucency as a floating point number whose value is between 0.0 and
     * 1.0 (inclusive). A value of zero means completely transparent. A value of 1.0 means
     * completely opaque. If null, the default value is 1.0, totally opaque.
     *
     * @return expression
     */
    Expression getOpacity();

    /**
     * Gives the absolute width in uoms of the line stroke as a floating point number. Fractional
     * numbers are allowed (with system-dependent interpretation), but negative numbers are not. If
     * null, the default value is 1.0.
     *
     * @return expression
     */
    Expression getWidth();

    /**
     * This parameter gives the solid color that will be used for a stroke.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component in
     * the order Red, Green, Blue, prefixed wih the hash (#) sign. The hexidecimal digits between A
     * and F may be in either upper or lower case. For example, full red is encoded as "#ff0000"
     * (with no quotation marks).
     *
     * <p>Note: in CSS this parameter is just called Stroke and not Color.
     */
    void setColor(Expression color);

    /**
     * This parameter gives the absolute width (thickness) of a stroke in pixels encoded as a float.
     * Fractional numbers are allowed but negative numbers are not.
     */
    void setWidth(Expression width);

    /**
     * This specifies the level of translucency to use when rendering the stroke.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing
     * totally transparent and 1.0 representing totally opaque. A linear scale of translucency is
     * used for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity.
     */
    void setOpacity(Expression opacity);

    /** This parameter controls how line strings should be joined together. */
    Expression getLineJoin();

    /** This parameter controls how line strings should be joined together. */
    void setLineJoin(Expression lineJoin);

    /** This parameter controls how line strings should be capped. */
    Expression getLineCap();

    /**
     * If present, indicates the dash pattern as a space-separated sequence of floating point
     * numbers. The first number represents the length of the first dash to draw. The second number
     * represents the length of space to leave. This continues to the end of the list then repeats.
     * If the list contains an odd number of values, then before rendering the list is enlarged by
     * repeating the last value. If this parameter is omitted, lines will be drawn as solid and
     * unbroken.
     *
     * @return expression
     */
    float[] getDashArray();

    /** This parameter controls how line strings should be capped. */
    void setLineCap(Expression lineCap);

    /** Shortcut to define dash array using literal numbers. */
    void setDashArray(float[] dashArray);

    /**
     * This parameter encodes the dash pattern as a seqeuence of floats.<br>
     * The first number gives the length in pixels of the dash to draw, the second gives the amount
     * of space to leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by repeating it twice to
     * give an even number of values.
     *
     * <p>For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    List<Expression> dashArray();

    /**
     * This parameter encodes the dash pattern as a list of expressions.<br>
     * The first expression gives the length in pixels of the dash to draw, the second gives the
     * amount of space to leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by repeating it twice to
     * give an even number of values.
     *
     * <p>For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    void setDashArray(List<Expression> dashArray);

    /**
     * A dash array need not start from the beginning. This method allows for an offset into the
     * dash array before starting it.
     */
    Expression getDashOffset();

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
    Object accept(TraversingStyleVisitor visitor, Object extraData);

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and
     * specifies the graphic to use.
     *
     * <p>Proper stroking with a linear graphic requires two "hot-spot" points within the space of
     * the graphic to indicate where the rendering line starts and stops. In the case of raster
     * images with no special mark-up, this line will be assumed to be the middle pixel row of the
     * image, starting from the first pixel column and ending at the last pixel column.
     */
    void setGraphicStroke(Graphic graphicStroke);

    void accept(StyleVisitor visitor);

    abstract class ConstantStroke implements Stroke {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Stroke may not be modified");
        }

        @Override
        public void setColor(Expression color) {
            cannotModifyConstant();
        }

        @Override
        public void setWidth(Expression width) {
            cannotModifyConstant();
        }

        @Override
        public void setOpacity(Expression opacity) {
            cannotModifyConstant();
        }

        @Override
        public void setLineJoin(Expression lineJoin) {
            cannotModifyConstant();
        }

        @Override
        public void setLineCap(Expression lineCap) {
            cannotModifyConstant();
        }

        @Override
        public void setDashArray(float[] dashArray) {
            cannotModifyConstant();
        }

        @Override
        public void setDashArray(List<Expression> dashArray) {
            cannotModifyConstant();
        }

        @Override
        public void setDashOffset(Expression dashOffset) {
            cannotModifyConstant();
        }

        @Override
        public void setGraphicFill(org.geotools.api.style.Graphic graphicFill) {
            cannotModifyConstant();
        }

        @Override
        public void setGraphicStroke(org.geotools.api.style.Graphic graphicStroke) {
            cannotModifyConstant();
        }

        @Override
        public void accept(StyleVisitor visitor) {
            cannotModifyConstant();
        }

        @Override
        public Object accept(TraversingStyleVisitor visitor, Object data) {
            cannotModifyConstant();
            return null;
        }
    }
}
