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

import java.awt.Color;

import org.geotools.filter.ConstantExpression;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.Expression;

/**
 * The Stroke object encapsulates the graphical-symbolization parameters for
 * linear geometries.
 * <p>
 * There are three basic types of stroke: solid color, graphic fill (stipple),
 * and repeated linear graphic stroke.
 * A repeated linear graphic is plotted linearly and has its graphic symbol
 * bent around the curves of the line string.  A GraphicFill has the pixels
 * of the line rendered with a repeating area-fill pattern.<p>
 * If neither a graphic fill nor graphic stroke element are given, then the
 * line symbolizer should render a solid color.
 * <p>
 * The details of this object are taken from the
 * <a href="https://portal.opengeospatial.org/files/?artifact_id=1188">
 * OGC Styled-Layer Descriptor Report (OGC 02-070) version 1.0.0.</a>:
 * <pre><code>
 * &lt;xsd:element name="Stroke"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "Stroke" specifies the appearance of a linear geometry.  It is
 *       defined in parallel with SVG strokes.  The following CssParameters
 *       may be used: "stroke" (color), "stroke-opacity", "stroke-width",
 *       "stroke-linejoin", "stroke-linecap", "stroke-dasharray", and
 *       "stroke-dashoffset".
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:choice minOccurs="0"&gt;
 *         &lt;xsd:element ref="sld:GraphicFill"/&gt;
 *         &lt;xsd:element ref="sld:GraphicStroke"/&gt;
 *       &lt;/xsd:choice&gt;
 *       &lt;xsd:element ref="sld:CssParameter" minOccurs="0"
 *                    maxOccurs="unbounded"/&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 * <p>
 * Renderers can use this information when displaying styled features,
 * though it must be remembered that not all renderers will be able to
 * fully represent strokes as set out by this interface.  For example, opacity
 * may not be supported.
 * <p>
 * Notes:
 * <ul>
 * <li>The graphical parameters and their values are derived from SVG/CSS2
 * standards with names and semantics which are as close as possible.
 * </ul>
 * </p>
 *
 * @source $URL$
 * @version $Id$
 * @author James Macgill
 */
public interface Stroke extends org.opengis.style.Stroke {
    /**
     * Default Stroke capturing the defaults indicated by the standard.
     * <p>
     * For some attributes the standard does not define a default, so a
     * reasonable value is supplied.
     * </p>
     */
    static final Stroke DEFAULT = new ConstantStroke() {
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
    static final Stroke NULL = new ConstantStroke() {
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

    /**
     * This parameter gives the solid color that will be used for a stroke.<br>
     * The color value returned here as a Java Color object, this is a convinence method
     * that goes above
     * The default color is defined to be Color.BLACK
     *
     * Note: in CSS this parameter is just called Stroke and not Color.
     *
     * @return The color of the stroke as a Color object
     **/
    @Deprecated
    Color getColor(SimpleFeature f);

    /**
     * This parameter gives the solid color that will be used for a stroke.<br>
     * The color value is RGB-encoded using two hexidecimal digits per
     * primary-color component in the order Red, Green, Blue, prefixed wih
     * the hash (#) sign.  The hexidecimal digits between A and F may be in
     * either upper or lower case.  For example, full red is encoded as
     * "#ff0000" (with no quotation marks).
     *
     * Note: in CSS this parameter is just called Stroke and not Color.
     */
    void setColor(Expression color);

    /**
     * This parameter gives the absolute width (thickness) of a stroke in
     * pixels encoded as a float.
     * Fractional numbers are allowed but negative
     * numbers are not.
     */
    void setWidth(Expression width);

    /**
     * This specifies the level of translucency to use when rendering the
     * stroke.<br>
     * The value is encoded as a floating-point value between 0.0 and
     * 1.0 with 0.0 representing totally transparent and 1.0 representing
     * totally opaque.  A linear scale of translucency is used for intermediate
     * values.<br>
     * For example, "0.65" would represent 65% opacity.
     */
    void setOpacity(Expression opacity);

    /**
     * This parameter controls how line strings should be joined together.
     */
    Expression getLineJoin();

    /**
     * This parameter controls how line strings should be joined together.
     */
    void setLineJoin(Expression lineJoin);

    /**
     * This parameter controls how line strings should be capped.
     */
    Expression getLineCap();

    /**
     * This parameter controls how line strings should be capped.
     */
    void setLineCap(Expression lineCap);

    /**
     * This parameter encodes the dash pattern as a seqeuence of floats.<br>
     * The first number gives the length in pixels of the dash to draw, the
     * second gives the amount of space to leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by
     * repeating it twice to give an even number of values.
     *
     * For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    float[] getDashArray();

    /**
     * This parameter encodes the dash pattern as a seqeuence of floats.<br>
     * The first number gives the length in pixels of the dash to draw, the
     * second gives the amount of space to leave, and this pattern repeats.<br>
     * If an odd number of values is given, then the pattern is expanded by
     * repeating it twice to give an even number of values.
     *
     * For example, "2 1 3 2" would produce:<br>
     * <code>--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;
     * --&nbsp;---&nbsp;&nbsp;--&nbsp;---&nbsp;&nbsp;--</code>
     */
    void setDashArray(float[] dashArray);

    /**
     * A dash array need not start from the beginning.  This method allows for
     * an offset into the dash array before starting it.
     */
    Expression getDashOffset();

    /**
     * A dash array need not start from the beginning.  This method allows for
     * an offset into the dash array before starting it.
     */
    void setDashOffset(Expression dashOffset);

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be
     * used and specifies the fill graphic to use.
     *
     * @return The graphic to use as a stipple fill.
     *         If null, then no Stipple fill should be used.
     */
    Graphic getGraphicFill();

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be
     * used and specifies the fill graphic to use.
     */
    void setGraphicFill(org.opengis.style.Graphic graphicFill);

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke
     * type will be used and specifies the graphic to use.
     *
     * Proper stroking with a linear graphic requires two "hot-spot" points
     * within the space of the graphic to indicate where the rendering line
     * starts and stops.
     * In the case of raster images with no special mark-up, this line will
     * be assumed to be the middle pixel row of the image, starting from the
     * first pixel column and ending at the last pixel column.
     *
     * @return The graphic to use as a linear graphic.
     *         If null, then no graphic stroke should be used.
     */
    Graphic getGraphicStroke();

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke
     * type will be used and specifies the graphic to use.
     *
     * Proper stroking with a linear graphic requires two "hot-spot" points
     * within the space of the graphic to indicate where the rendering line
     * starts and stops.
     * In the case of raster images with no special mark-up, this line will
     * be assumed to be the middle pixel row of the image, starting from the
     * first pixel column and ending at the last pixel column.
     */
    void setGraphicStroke(org.opengis.style.Graphic graphicStroke);

    void accept(org.geotools.styling.StyleVisitor visitor);

}

abstract class ConstantStroke implements Stroke {
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
