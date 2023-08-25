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

import java.util.Collections;
import java.util.List;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Symbol;
import org.geotools.filter.ConstantExpression;

/**
 * A Graphic is a "graphical symbol" with an inherent shape, color(s), and possibly size.
 *
 * <p>A "graphic" can very informally be defined as "a little picture" and can be of either a raster
 * or vector graphic source type. The term graphic is used since the term "symbol" is similar to
 * "symbolizer" which is used in a difference context in SLD. The graphical symbol to display can be
 * provided either as an external graphical resource or as a Mark.<br>
 * Multiple external URLs and marks can be referenced with the proviso that they all provide
 * equivalent graphics in different formats. The 'hot spot' to use for positioning the rendering at
 * a point must either be inherent from the external format or be defined to be the "central point"
 * of the graphic.
 *
 * <p>The details of this object are taken from the <a
 * href="https://portal.opengeospatial.org/files/?artifact_id=1188">OGC Styled-Layer Descriptor
 * Report (OGC 02-070) version 1.0.0.</a>:
 *
 * <pre><code>
 * &lt;xsd:element name="Graphic"&gt;
 *   &lt;xsd:annotation&gt;
 *     &lt;xsd:documentation&gt;
 *       A "Graphic" specifies or refers to a "graphic symbol" with inherent
 *       shape, size, and coloring.
 *     &lt;/xsd:documentation&gt;
 *   &lt;/xsd:annotation&gt;
 *   &lt;xsd:complexType&gt;
 *     &lt;xsd:sequence&gt;
 *       &lt;xsd:choice minOccurs="0" maxOccurs="unbounded"&gt;
 *         &lt;xsd:element ref="sld:ExternalGraphic"/&gt;
 *         &lt;xsd:element ref="sld:Mark"/&gt;
 *       &lt;/xsd:choice&gt;
 *       &lt;xsd:sequence&gt;
 *         &lt;xsd:element ref="sld:Opacity" minOccurs="0"/&gt;
 *         &lt;xsd:element ref="sld:Size" minOccurs="0"/&gt;
 *         &lt;xsd:element ref="sld:Rotation" minOccurs="0"/&gt;
 *       &lt;/xsd:sequence&gt;
 *     &lt;/xsd:sequence&gt;
 *   &lt;/xsd:complexType&gt;
 * &lt;/xsd:element&gt;
 * </code></pre>
 *
 * <p>Renderers can ue this information when displaying styled features, though it must be
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
 * @task REVISIT: There are no setter methods in this interface, is this a problem?
 */
class ConstantGraphic extends Graphic {
    /**
     * A default Graphic instance.
     *
     * <p>For some attributes the standard does not define a default, so a reasonable value is
     * supplied.
     */
    public static final Graphic DEFAULT =
            new ConstantGraphic() {

                @Override
                public Expression getOpacity() {
                    return ConstantExpression.ONE;
                }

                @Override
                public Expression getSize() {
                    // default size is unknown, it depends on the target
                    return Expression.NIL;
                }

                @Override
                public Displacement getDisplacement() {
                    return Displacement.DEFAULT;
                }

                @Override
                public Expression getRotation() {
                    return ConstantExpression.ZERO;
                }
            };
    /**
     * Indicates an absence of graphic.
     *
     * <p>This value is used to indicate that the Graphics based operation should be skipped. Aka
     * this is used by Stroke.Stroke as placeholders for GRAPHIC_FILL and GRAPHIC_STROKE.
     */
    public static final Graphic NULL =
            new ConstantGraphic() {

                @Override
                public List<GraphicalSymbol> graphicalSymbols() {
                    return Collections.emptyList();
                }

                @Override
                public Expression getOpacity() {
                    return ConstantExpression.NULL;
                }

                @Override
                public Expression getSize() {
                    return ConstantExpression.NULL;
                }

                @Override
                public Displacement getDisplacement() {
                    return Displacement.NULL;
                }

                @Override
                public Expression getRotation() {
                    return ConstantExpression.NULL;
                }

                @Override
                public AnchorPoint getAnchorPoint() {
                    return AnchorPoint.NULL;
                }
            };

    private void cannotModifyConstant() {
        throw new UnsupportedOperationException("Constant Graphic may not be modified");
    }

    @Override
    public void setDisplacement(org.geotools.api.style.Displacement offset) {
        cannotModifyConstant();
    }

    public void setExternalGraphics(ExternalGraphic... externalGraphics) {
        cannotModifyConstant();
    }

    public void addExternalGraphic(ExternalGraphic externalGraphic) {
        cannotModifyConstant();
    }

    public void setMarks(Mark... marks) {
        cannotModifyConstant();
    }

    public void addMark(Mark mark) {
        cannotModifyConstant();
    }

    public void setGap(Expression gap) {
        cannotModifyConstant();
    }

    public void setInitialGap(Expression initialGap) {
        cannotModifyConstant();
    }

    public void setSymbols(Symbol... symbols) {
        cannotModifyConstant();
    }

    public void addSymbol(Symbol symbol) {
        cannotModifyConstant();
    }

    @Override
    public void setOpacity(Expression opacity) {
        cannotModifyConstant();
    }

    @Override
    public void setSize(Expression size) {
        cannotModifyConstant();
    }

    @Override
    public void setRotation(Expression rotation) {
        cannotModifyConstant();
    }

    public void setAnchorPoint(AnchorPoint anchor) {
        cannotModifyConstant();
    }

    @Override
    public List<GraphicalSymbol> graphicalSymbols() {
        return Collections.emptyList();
    }

    @Override
    public AnchorPoint getAnchorPoint() {
        return org.geotools.styling.AnchorPoint.DEFAULT;
    }

    @Override
    public void setAnchorPoint(org.geotools.api.style.AnchorPoint anchorPoint) {
        cannotModifyConstant();
    }

    @Override
    public Expression getGap() {
        return ConstantExpression.constant(0);
    }

    @Override
    public Expression getInitialGap() {
        return ConstantExpression.constant(0);
    }
}
