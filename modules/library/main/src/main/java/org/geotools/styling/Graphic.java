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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * Direct implementation of Graphic.
 *
 * @author Ian Turton, CCG
 * @author Johann Sorel (Geomatys)
 * @version $Id$
 */
public class Graphic implements org.geotools.api.style.Graphic, Cloneable {


    private final List<GraphicalSymbol> graphics = new ArrayList<>();

    private AnchorPoint anchor;
    private Expression gap;
    private Expression initialGap;

    private Expression rotation = null;
    private Expression size = null;
    private Displacement displacement = null;
    private Expression opacity = null;

    /** Creates a new instance of DefaultGraphic */
    protected Graphic() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public Graphic(FilterFactory factory) {
        this(factory, null, null, null);
    }

    public Graphic(
            FilterFactory factory, AnchorPoint anchor, Expression gap, Expression initialGap) {
        this.anchor = org.geotools.styling.AnchorPoint.cast(anchor);

        if (gap == null) this.gap = ConstantExpression.constant(0);
        else this.gap = gap;
        if (initialGap == null) this.initialGap = ConstantExpression.constant(0);
        else this.initialGap = initialGap;
    }

    @Override
    public List<GraphicalSymbol> graphicalSymbols() {
        return graphics;
    }

    @Override
    public AnchorPoint getAnchorPoint() {
        return anchor;
    }

    public void setAnchorPoint(org.geotools.styling.AnchorPoint anchor) {
        this.anchor = org.geotools.styling.AnchorPoint.cast(anchor);
    }


    public void setAnchorPoint(org.geotools.api.style.AnchorPoint anchorPoint) {
        this.anchor = org.geotools.styling.AnchorPoint.cast(anchorPoint);
    }

    /**
     * This specifies the level of translucency to use when rendering the graphic.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing
     * totally transparent and 1.0 representing totally opaque, with a linear scale of translucency
     * for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the Graphic, where 0.0 is completely transparent and 1.0 is completely
     *     opaque.
     */
    @Override
    public Expression getOpacity() {
        return opacity;
    }

    /**
     * This parameter defines the rotation of a graphic in the clockwise direction about its centre
     * point in decimal degrees. The value encoded as a floating point number.
     *
     * @return The angle of rotation in decimal degrees. Negative values represent counter-clockwise
     *     rotation. The default is 0.0 (no rotation).
     */
    @Override
    public Expression getRotation() {
        return rotation;
    }

    /**
     * This parameter gives the absolute size of the graphic in pixels encoded as a floating point
     * number.
     *
     * <p>The default size of an image format (such as GIFD) is the inherent size of the image. The
     * default size of a format without an inherent size (such as SVG) is defined to be 16 pixels in
     * height and the corresponding aspect in width. If a size is specified, the height of the
     * graphic will be scaled to that size and the corresponding aspect will be used for the width.
     *
     * @return The size of the graphic, the default is context specific. Negative values are not
     *     possible.
     */
    @Override
    public Expression getSize() {
        return size;
    }

    @Override
    public Displacement getDisplacement() {
        return displacement;
    }

    @Override
    public Expression getInitialGap() {
        return initialGap;
    }

    public void setInitialGap(Expression initialGap) {
        this.initialGap = initialGap;
    }

    @Override
    public Expression getGap() {
        return gap;
    }

    public void setGap(Expression gap) {
        this.gap = gap;
    }


    public void setDisplacement(org.geotools.api.style.Displacement offset) {
        this.displacement = Displacement.cast(offset);
    }


    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    /**
     * Setter for property rotation.
     *
     * @param rotation New value of property rotation.
     */

    public void setRotation(Expression rotation) {
        this.rotation = rotation;
    }

    /**
     * Setter for property size.
     *
     * @param size New value of property size.
     */

    public void setSize(Expression size) {
        this.size = size;
    }

    @Override
    public Object accept(StyleVisitor visitor, Object data) {
        return visitor.visit((org.geotools.api.style.GraphicStroke) this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /**
     * Creates a deep copy clone.
     *
     * @return The deep copy clone.
     */
    @Override
    public Object clone() {
        Graphic clone;

        try {
            clone = (Graphic) super.clone();
            clone.graphics.clear();
            clone.graphics.addAll(graphics);

        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e); // this should never happen.
        }

        return clone;
    }

    /**
     * Override of hashcode
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;

        if (graphics != null) {
            result = (PRIME * result) + graphics.hashCode();
        }

        if (rotation != null) {
            result = (PRIME * result) + rotation.hashCode();
        }

        if (size != null) {
            result = (PRIME * result) + size.hashCode();
        }

        if (opacity != null) {
            result = (PRIME * result) + opacity.hashCode();
        }


        return result;
    }

    /**
     * Compares this GraphicImpl with another for equality.
     *
     * <p>Two graphics are equal if and only if they both have the same geometry property name and
     * the same list of symbols and the same rotation, size and opacity.
     *
     * @param oth The other GraphicsImpl to compare with.
     * @return True if this is equal to oth according to the above conditions.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof Graphic) {
            Graphic other = (Graphic) oth;

            return Utilities.equals(this.size, other.size)
                    && Utilities.equals(this.rotation, other.rotation)
                    && Utilities.equals(this.opacity, other.opacity)
                    && Objects.equals(this.graphicalSymbols(), other.graphicalSymbols());
        }

        return false;
    }

    static Graphic cast(org.geotools.api.style.Graphic graphic) {
        if (graphic == null) {
            return null;
        } else if (graphic instanceof Graphic) {
            return (Graphic) graphic;
        } else {
            Graphic copy = new Graphic();
            copy.setAnchorPoint(graphic.getAnchorPoint());
            copy.setDisplacement(graphic.getDisplacement());
            if (graphic.graphicalSymbols() != null) {
                for (GraphicalSymbol item : graphic.graphicalSymbols()) {
                    if (item instanceof org.geotools.api.style.ExternalGraphic) {
                        copy.graphicalSymbols().add(ExternalGraphic.cast(item));
                    } else if (item instanceof org.geotools.api.style.Mark) {
                        copy.graphicalSymbols().add(Mark.cast(item));
                    }
                }
            }
            return copy;
        }
    }
}
