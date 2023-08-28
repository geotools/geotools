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

import java.awt.*;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.*;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * @version $Id$
 * @author James Macgill, CCG
 */
public class FillImpl implements Fill, Cloneable {
    public static final Fill DEFAULT =
            new ConstantFill() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Stroke may not be modified");
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
                    return null;
                }

                @Override
                public Object accept(TraversingStyleVisitor visitor, Object extraData) {
                    cannotModifyConstant();
                    return null;
                }
            };
    public static final Fill NULL =
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
                    return GraphicImpl.NULL;
                }

                @Override
                public Object accept(TraversingStyleVisitor visitor, Object extraData) {
                    cannotModifyConstant();
                    return null;
                }
            };
    private FilterFactory filterFactory;
    private Expression color = null;
    private Expression opacity = null;
    private Graphic graphicFill = null;

    /** Creates a new instance of DefaultFill */
    protected FillImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public FillImpl(FilterFactory factory) {
        filterFactory = factory;
    }

    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;
    }

    /**
     * This parameter gives the solid color that will be used for a Fill.<br>
     * The color value is RGB-encoded using two hexadecimal digits per primary-color component, in
     * the order Red, Green, Blue, prefixed with the hash (#) sign. The hexadecimal digits between A
     * and F may be in either upper or lower case. For example, full red is encoded as "#ff0000"
     * (with no quotation marks). The default color is defined to be 50% gray ("#808080").
     *
     * <p>Note: in CSS this parameter is just called Fill and not Color.
     *
     * @return The color of the Fill encoded as a hexidecimal RGB value.
     */
    @Override
    public Expression getColor() {
        return color;
    }

    /**
     * This parameter gives the solid color that will be used for a Fill.<br>
     * The color value is RGB-encoded using two hexidecimal digits per primary-color component, in
     * the order Red, Green, Blue, prefixed with the hash (#) sign. The hexidecimal digits between A
     * and F may be in either upper or lower case. For example, full red is encoded as "#ff0000"
     * (with no quotation marks).
     *
     * <p>Note: in CSS this parameter is just called Fill and not Color.
     *
     * @param rgb The color of the Fill encoded as a hexidecimal RGB value.
     */
    @Override
    public void setColor(Expression rgb) {
        if (color == rgb) return;
        color = rgb;
    }

    public void setColor(String rgb) {
        if (color.toString().equals(rgb)) return;

        setColor(filterFactory.literal(rgb));
    }

    /**
     * This specifies the level of translucency to use when rendering the fill. <br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing
     * totally transparent and 1.0 representing totally opaque, with a linear scale of translucency
     * for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the fill, where 0.0 is completely transparent and 1.0 is completely
     *     opaque.
     */
    @Override
    public Expression getOpacity() {
        return opacity;
    }

    /**
     * Setter for property opacity.
     *
     * @param opacity New value of property opacity.
     */
    @Override
    public void setOpacity(Expression opacity) {
        if (this.opacity == opacity) return;

        this.opacity = opacity;
    }

    public void setOpacity(String opacity) {
        if (this.opacity.toString().equals(opacity)) return;

        setOpacity(filterFactory.literal(opacity));
    }

    /**
     * This parameter indicates that a stipple-fill repeated graphic will be used and specifies the
     * fill graphic to use.
     *
     * @return graphic The graphic to use as a stipple fill. If null then no Stipple fill should be
     *     used.
     */
    @Override
    public Graphic getGraphicFill() {
        return graphicFill;
    }

    /**
     * Setter for property graphic.
     *
     * @param graphicFill New value of property graphic.
     */
    @Override
    public void setGraphicFill(org.geotools.api.style.Graphic graphicFill) {
        if (this.graphicFill == graphicFill) return;
        this.graphicFill = GraphicImpl.cast(graphicFill);
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /** Returns a clone of the FillImpl. */
    @Override
    public Object clone() {
        try {
            FillImpl clone = (FillImpl) super.clone();
            if (graphicFill != null) {
                clone.graphicFill = (Graphic) ((Cloneable) graphicFill).clone();
            }
            return clone;
        } catch (CloneNotSupportedException e) {
            // This will never happen
            throw new RuntimeException("Failed to clone FillImpl");
        }
    }

    /**
     * Generates a hashcode for the FillImpl.
     *
     * @return The hashcode.
     */
    @Override
    public int hashCode() {
        final int PRIME = 1000003;
        int result = 0;
        if (color != null) {
            result = PRIME * result + color.hashCode();
        }
        if (opacity != null) {
            result = PRIME * result + opacity.hashCode();
        }
        if (graphicFill != null) {
            result = PRIME * result + graphicFill.hashCode();
        }

        return result;
    }

    /**
     * Compares a FillImpl with another for equality.
     *
     * <p>Two FillImpls are equal if they contain the same, color, backgroundcolor, opacity and
     * graphicFill.
     *
     * @param oth The other FillImpl
     * @return True if this FillImpl is equal to oth.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof FillImpl) {
            FillImpl other = (FillImpl) oth;
            return Utilities.equals(this.color, other.color)
                    && Utilities.equals(this.opacity, other.opacity)
                    && Utilities.equals(this.graphicFill, other.graphicFill);
        }

        return false;
    }

    static FillImpl cast(org.geotools.api.style.Fill fill) {
        if (fill == null) {
            return null;
        } else if (fill instanceof FillImpl) {
            return (FillImpl) fill;
        } else {
            FillImpl copy = new FillImpl();
            copy.color = fill.getColor();
            copy.graphicFill = GraphicImpl.cast(fill.getGraphicFill());
            copy.opacity = fill.getOpacity();
            return copy;
        }
    }

    public abstract static class ConstantFill implements Fill {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Fill may not be modified");
        }

        @Override
        public void setColor(Expression color) {
            cannotModifyConstant();
        }

        public void setBackgroundColor(Expression backgroundColor) {
            cannotModifyConstant();
        }

        @Override
        public void setOpacity(Expression opacity) {
            cannotModifyConstant();
        }

        @Override
        public void setGraphicFill(org.geotools.api.style.Graphic graphicFill) {
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
