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

import java.util.List;
import org.geotools.api.filter.expression.Expression;

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
public abstract class ConstantStroke extends Stroke {
    /**
     * Default Stroke capturing the defaults indicated by the standard.
     *
     * <p>For some attributes the standard does not define a default, so a reasonable value is
     * supplied.
     */
    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Stroke may not be modified");
    }

    public void setColor(Expression color) {
        cannotModifyConstant();
    }

    public void setWidth(Expression width) {
        cannotModifyConstant();
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

    public void setDashArray(List<Expression> dashArray) {
        cannotModifyConstant();
    }

    public void setDashOffset(Expression dashOffset) {
        cannotModifyConstant();
    }

    public void setGraphicFill(org.geotools.api.style.Graphic graphicFill) {
        cannotModifyConstant();
    }

    public void setGraphicStroke(org.geotools.api.style.Graphic graphicStroke) {
        cannotModifyConstant();
    }

    @Override
    public void accept(org.geotools.api.style.StyleVisitor visitor) {
        cannotModifyConstant();
    }

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
    public abstract List<Expression> dashArray();
}
