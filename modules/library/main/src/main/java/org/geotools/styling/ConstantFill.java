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
 *
 */
package org.geotools.styling;

import java.awt.Color;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.Fill;
import org.geotools.api.style.StyleVisitor;
import org.geotools.filter.ConstantExpression;

/**
 * The Fill object encapsulates the graphical-symbolization parameters for areas of geometries.
 *
 * <p>There are two types of fill: solid-color and repeated graphic fill.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="Fill"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "Fill" specifies the pattern for filling an area geometry.
 *       The allowed CssParameters are: "fill" (color) and "fill-opacity".
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:element ref="sld:GraphicFill" minOccurs="0"/&gt;
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
 * @author James Macgill, CCG
 * @version $Id$
 */
abstract class ConstantFill implements org.geotools.api.style.Fill {
    static final Fill DEFAULT =
            new ConstantFill() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Fill may not be modified");
                }

                final Expression COLOR = ConstantExpression.color(new Color(128, 128, 128));
                final Expression BGCOLOR = ConstantExpression.color(new Color(255, 255, 255, 0));
                final Expression OPACITY = ConstantExpression.ONE;

                @Override
                public Expression getColor() {
                    return COLOR;
                }

                @Override
                public Expression getOpacity() {
                    return OPACITY;
                }

                @Override
                public Graphic getGraphicFill() {
                    return ConstantGraphic.NULL;
                }

                @Override
                public Object accept(StyleVisitor visitor, Object extraData) {
                    cannotModifyConstant();
                    return null;
                }
            };

    static final Fill NULL =
            new ConstantFill() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Stroke may not be modified");
                }

                @Override
                public Expression getColor() {
                    return ConstantExpression.NULL;
                }

                @Override
                public Expression getOpacity() {
                    return ConstantExpression.NULL;
                }

                @Override
                public Graphic getGraphicFill() {
                    return ConstantGraphic.NULL;
                }

                @Override
                public Object accept(StyleVisitor visitor, Object extraData) {
                    cannotModifyConstant();
                    return null;
                }
            };




    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Fill may not be modified");
    }

    public void setColor(Expression color) {
        cannotModifyConstant();
    }

    public void setBackgroundColor(Expression backgroundColor) {
        cannotModifyConstant();
    }

    public void setOpacity(Expression opacity) {
        cannotModifyConstant();
    }

    public void setGraphicFill(org.geotools.api.style.Graphic graphicFill) {
        cannotModifyConstant();
    }

    @Override
    public void accept(StyleVisitor visitor) {
        cannotModifyConstant();
    }

    @Override
    public Object accept(org.geotools.api.style.StyleVisitor visitor, Object data) {
        cannotModifyConstant();
        return null;
    }
};
