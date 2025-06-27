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
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.ExternalGraphic;
import org.geotools.api.style.Graphic;
import org.geotools.api.style.GraphicFill;
import org.geotools.api.style.GraphicStroke;
import org.geotools.api.style.GraphicalSymbol;
import org.geotools.api.style.Mark;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.Symbol;
import org.geotools.api.style.TraversingStyleVisitor;
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
public class GraphicImpl
        implements org.geotools.api.style.GraphicLegend,
                org.geotools.api.style.Graphic,
                org.geotools.api.style.GraphicFill,
                org.geotools.api.style.GraphicStroke,
                Cloneable {
    /**
     * A default Graphic instance.
     *
     * <p>For some attributes the standard does not define a default, so a reasonable value is supplied.
     */
    public static final Graphic DEFAULT = new ConstantGraphic() {

        @Override
        public List<GraphicalSymbol> graphicalSymbols() {
            return Collections.emptyList();
        }

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
            return DisplacementImpl.DEFAULT;
        }

        @Override
        public Expression getRotation() {
            return ConstantExpression.ZERO;
        }
    };
    /**
     * Indicates an absense of graphic.
     *
     * <p>This value is used to indicate that the Graphics based opperation should be skipped. Aka this is used by
     * Stroke.Stroke as place holders for GRAPHIC_FILL and GRAPHIC_STROKE.
     */
    public static final Graphic NULL = new ConstantGraphic() {

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
            return DisplacementImpl.NULL;
        }

        @Override
        public Expression getRotation() {
            return ConstantExpression.NULL;
        }

        //            public String getGeometryPropertyName() {
        //                return "";
        //            }

    };
    /** The logger for the default core module. */
    // private static final java.util.logging.Logger LOGGER =
    // org.geotools.util.logging.Logging.getLogger(GraphicImpl.class);

    private final List<GraphicalSymbol> graphics = new ArrayList<>();

    private AnchorPointImpl anchor;
    private Expression gap;
    private Expression initialGap;

    private Expression rotation = null;
    private Expression size = null;
    private DisplacementImpl displacement = null;
    private Expression opacity = null;

    /** Creates a new instance of DefaultGraphic */
    protected GraphicImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public GraphicImpl(FilterFactory factory) {
        this(factory, null, null, null);
    }

    public GraphicImpl(FilterFactory factory, AnchorPoint anchor, Expression gap, Expression initialGap) {
        this.anchor = AnchorPointImpl.cast(anchor);

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

    @Override
    public void setAnchorPoint(AnchorPoint anchor) {
        this.anchor = AnchorPointImpl.cast(anchor);
    }

    /**
     * This specifies the level of translucency to use when rendering the graphic.<br>
     * The value is encoded as a floating-point value between 0.0 and 1.0 with 0.0 representing totally transparent and
     * 1.0 representing totally opaque, with a linear scale of translucency for intermediate values.<br>
     * For example, "0.65" would represent 65% opacity. The default value is 1.0 (opaque).
     *
     * @return The opacity of the Graphic, where 0.0 is completely transparent and 1.0 is completely opaque.
     */
    @Override
    public Expression getOpacity() {
        return opacity;
    }

    /**
     * This parameter defines the rotation of a graphic in the clockwise direction about its centre point in decimal
     * degrees. The value encoded as a floating point number.
     *
     * @return The angle of rotation in decimal degrees. Negative values represent counter-clockwise rotation. The
     *     default is 0.0 (no rotation).
     */
    @Override
    public Expression getRotation() {
        return rotation;
    }

    /**
     * This paramteter gives the absolute size of the graphic in pixels encoded as a floating point number.
     *
     * <p>The default size of an image format (such as GIFD) is the inherent size of the image. The default size of a
     * format without an inherent size (such as SVG) is defined to be 16 pixels in height and the corresponding aspect
     * in width. If a size is specified, the height of the graphic will be scaled to that size and the corresponding
     * aspect will be used for the width.
     *
     * @return The size of the graphic, the default is context specific. Negative values are not possible.
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

    @Override
    public void setInitialGap(Expression initialGap) {
        this.initialGap = initialGap;
    }

    @Override
    public Expression getGap() {
        return gap;
    }

    @Override
    public void setGap(Expression gap) {
        this.gap = gap;
    }

    @Override
    public void setDisplacement(org.geotools.api.style.Displacement offset) {
        this.displacement = DisplacementImpl.cast(offset);
    }

    @Override
    public void setOpacity(Expression opacity) {
        this.opacity = opacity;
    }

    /**
     * Setter for property rotation.
     *
     * @param rotation New value of property rotation.
     */
    @Override
    public void setRotation(Expression rotation) {
        this.rotation = rotation;
    }

    /**
     * Setter for property size.
     *
     * @param size New value of property size.
     */
    @Override
    public void setSize(Expression size) {
        this.size = size;
    }

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
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
        GraphicImpl clone;

        try {
            clone = (GraphicImpl) super.clone();
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
            result = PRIME * result + graphics.hashCode();
        }

        if (rotation != null) {
            result = PRIME * result + rotation.hashCode();
        }

        if (size != null) {
            result = PRIME * result + size.hashCode();
        }

        if (opacity != null) {
            result = PRIME * result + opacity.hashCode();
        }

        //        if (gap != null) {
        //            result = (PRIME * result) + gap.hashCode();
        //        }
        //
        //        if (initialGap != null) {
        //            result = (PRIME * result) + initialGap.hashCode();
        //        }

        return result;
    }

    /**
     * Compares this GraphicImpl with another for equality.
     *
     * <p>Two graphics are equal if and only if they both have the same geometry property name and the same list of
     * symbols and the same rotation, size and opacity.
     *
     * @param oth The other GraphicsImpl to compare with.
     * @return True if this is equal to oth according to the above conditions.
     */
    @Override
    public boolean equals(Object oth) {
        if (this == oth) {
            return true;
        }

        if (oth instanceof GraphicImpl) {
            GraphicImpl other = (GraphicImpl) oth;

            return Utilities.equals(this.size, other.size)
                    && Utilities.equals(this.rotation, other.rotation)
                    && Utilities.equals(this.opacity, other.opacity)
                    && Objects.equals(this.graphicalSymbols(), other.graphicalSymbols());
        }

        return false;
    }

    static GraphicImpl cast(org.geotools.api.style.Graphic graphic) {
        if (graphic == null) {
            return null;
        } else if (graphic instanceof GraphicImpl) {
            return (GraphicImpl) graphic;
        } else {
            GraphicImpl copy = new GraphicImpl();
            copy.setAnchorPoint(graphic.getAnchorPoint());
            copy.setDisplacement(graphic.getDisplacement());
            if (graphic.graphicalSymbols() != null) {
                for (GraphicalSymbol item : graphic.graphicalSymbols()) {
                    if (item instanceof org.geotools.api.style.ExternalGraphic) {
                        copy.graphicalSymbols().add(ExternalGraphicImpl.cast(item));
                    } else if (item instanceof org.geotools.api.style.Mark) {
                        copy.graphicalSymbols().add(MarkImpl.cast(item));
                    }
                }
            }
            return copy;
        }
    }

    public abstract static class ConstantGraphic implements Graphic, GraphicStroke, GraphicFill {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Graphic may not be modified");
        }

        @Override
        public void setDisplacement(Displacement offset) {
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

        @Override
        public void setGap(Expression gap) {
            cannotModifyConstant();
        }

        @Override
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

        @Override
        public void setAnchorPoint(AnchorPoint anchor) {
            cannotModifyConstant();
        }

        @Override
        public Object accept(TraversingStyleVisitor visitor, Object data) {
            return visitor.visit((GraphicStroke) this, data);
        }

        @Override
        public void accept(StyleVisitor visitor) {
            visitor.visit(this);
        }

        @Override
        public List<GraphicalSymbol> graphicalSymbols() {
            return Collections.emptyList();
        }

        @Override
        public AnchorPoint getAnchorPoint() {
            return AnchorPointImpl.DEFAULT;
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
}
