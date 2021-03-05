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

import java.util.Collections;
import java.util.List;
import org.geotools.filter.ConstantExpression;
import org.opengis.filter.expression.Expression;

/**
 * The Stroke object encapsulates the graphical-symbolization parameters for linear geometries.
 *
 * <p>There are three basic types of stroke: solid color, graphic fill (stipple), and repeated
 * linear graphic stroke. A repeated linear graphic is plotted linearly and has its graphic symbol
 * bent around the curves of the line string. A GraphicFill has the pixels of the line rendered with
 * a repeating area-fill pattern.
 *
 * <p>If neither a graphic fill nor graphic stroke element are given, then the line symbolizer
 * should render a solid color.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
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
 *
 * <p>Renderers can use this information when displaying styled features, though it must be
 * remembered that not all renderers will be able to fully represent strokes as set out by this
 * interface. For example, opacity may not be supported.
 *
 * <p>Notes:
 *
 * <ul>
 *   <li>The graphical parameters and their values are derived from SVG/CSS2 standards with names
 *       and semantics which are as close as possible.
 * </ul>
 *
 * @version $Id$
 * @author James Macgill
 */
public interface Stroke extends org.opengis.style.Stroke {
    /**
     * Default Stroke capturing the defaults indicated by the standard.
     *
     * <p>For some attributes the standard does not define a default, so a reasonable value is
     * supplied.
     */
    static final Stroke DEFAULT =
            new ConstantStroke() {
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
                    return Graphic.DEFAULT;
                }

                @Override
                public Graphic getGraphicStroke() {
                    return Graphic.NULL;
                }

                @Override
                public Object clone() {
                    return this; // we are constant
                }
            };

    /**
     * Null Stroke capturing the defaults indicated by the standard.
     *
     * <p>This is a NullObject, it purpose is to prevent client code from having to do null
     * checking.
     */
    static final Stroke NULL =
            new ConstantStroke() {
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
                    return Graphic.NULL;
                }

                @Override
                public Graphic getGraphicStroke() {
                    return Graphic.NULL;
                }
            };

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
    @Override
    Expression getLineJoin();

    /** This parameter controls how line strings should be joined together. */
    void setLineJoin(Expression lineJoin);

    /** This parameter controls how line strings should be capped. */
    @Override
    Expression getLineCap();

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
    @Override
    Expression getDashOffset();

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
    @Override
    Graphic getGraphicFill();

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     */
    void setGraphicFill(org.opengis.style.Graphic graphicFill);

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
    @Override
    Graphic getGraphicStroke();

    /**
     * This parameter indicates that a repeated-linear-graphic graphic stroke type will be used and
     * specifies the graphic to use.
     *
     * <p>Proper stroking with a linear graphic requires two "hot-spot" points within the space of
     * the graphic to indicate where the rendering line starts and stops. In the case of raster
     * images with no special mark-up, this line will be assumed to be the middle pixel row of the
     * image, starting from the first pixel column and ending at the last pixel column.
     */
    void setGraphicStroke(org.opengis.style.Graphic graphicStroke);

    void accept(org.geotools.styling.StyleVisitor visitor);
}

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
    public void setGraphicFill(org.opengis.style.Graphic graphicFill) {
        cannotModifyConstant();
    }

    @Override
    public void setGraphicStroke(org.opengis.style.Graphic graphicStroke) {
        cannotModifyConstant();
    }

    @Override
    public void accept(org.geotools.styling.StyleVisitor visitor) {
        cannotModifyConstant();
    }

    @Override
    public Object accept(org.opengis.style.StyleVisitor visitor, Object data) {
        cannotModifyConstant();
        return null;
    }
}
