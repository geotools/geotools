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

// OpenGIS dependencies

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.AnchorPoint;
import org.geotools.api.style.Displacement;
import org.geotools.api.style.PointPlacement;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class PointPlacementImpl implements PointPlacement, Cloneable {
    public static final AnchorPoint DEFAULT_ANCHOR_POINT = new AnchorPoint() {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant AnchorPoint may not be modified");
        }

        @Override
        public void setAnchorPointX(Expression x) {
            cannotModifyConstant();
        }

        @Override
        public void setAnchorPointY(Expression y) {
            cannotModifyConstant();
        }

        /**
         * calls the visit method of a StyleVisitor
         *
         * @param visitor the style visitor
         */
        @Override
        public void accept(StyleVisitor visitor) {}

        @Override
        public Object accept(TraversingStyleVisitor visitor, Object data) {
            cannotModifyConstant();
            return null;
        }

        @Override
        public Expression getAnchorPointX() {
            return ConstantExpression.constant(0.0);
        }

        @Override
        public Expression getAnchorPointY() {
            return ConstantExpression.constant(0.5);
        }
    };
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(PointPlacementImpl.class);

    // TODO: make container ready
    private final FilterFactory filterFactory;
    private AnchorPointImpl anchorPoint = new AnchorPointImpl();
    private DisplacementImpl displacement = new DisplacementImpl();
    private Expression rotation = null;

    public PointPlacementImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public PointPlacementImpl(FilterFactory factory) {
        filterFactory = factory;
        try {
            rotation = filterFactory.literal(Integer.valueOf(0));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultPointPlacement: " + ife);
        }
    }

    /**
     * Returns the AnchorPoint which identifies the location inside a text label to use as an "anchor" for positioning
     * it relative to a point geometry.
     *
     * @return Label's AnchorPoint.
     */
    @Override
    public AnchorPoint getAnchorPoint() {
        return anchorPoint;
    }

    /**
     * Setter for property anchorPoint.
     *
     * @param anchorPoint New value of property anchorPoint.
     */
    @Override
    public void setAnchorPoint(org.geotools.api.style.AnchorPoint anchorPoint) {
        if (this.anchorPoint == anchorPoint) {
            return;
        }
        this.anchorPoint = AnchorPointImpl.cast(anchorPoint);
    }

    /**
     * Returns the Displacement which gives X and Y offset displacements to use for rendering a text label near a point.
     *
     * @return The label displacement.
     */
    @Override
    public Displacement getDisplacement() {
        return displacement;
    }

    /**
     * Setter for property displacement.
     *
     * @param displacement New value of property displacement.
     */
    @Override
    public void setDisplacement(org.geotools.api.style.Displacement displacement) {
        if (this.displacement == displacement) {
            return;
        }
        this.displacement = DisplacementImpl.cast(displacement);
    }

    /**
     * Returns the rotation of the label.
     *
     * @return The rotation of the label.
     */
    @Override
    public Expression getRotation() {
        return rotation;
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

    @Override
    public Object accept(TraversingStyleVisitor visitor, Object data) {
        return visitor.visit(this, data);
    }

    @Override
    public void accept(StyleVisitor visitor) {
        visitor.visit(this);
    }

    /* (non-Javadoc)
     * @see Cloneable#clone()
     */
    @Override
    public Object clone() {
        try {
            PointPlacementImpl clone = (PointPlacementImpl) super.clone();
            clone.anchorPoint = (AnchorPointImpl) anchorPoint.clone();
            clone.displacement = (DisplacementImpl) displacement.clone();

            return clone;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Won't happen");
        }
    }

    /* (non-Javadoc)
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (obj instanceof PointPlacementImpl) {
            PointPlacementImpl other = (PointPlacementImpl) obj;

            return Utilities.equals(anchorPoint, other.anchorPoint)
                    && Utilities.equals(displacement, other.displacement)
                    && Utilities.equals(rotation, other.rotation);
        }

        return false;
    }

    /* (non-Javadoc)
     * @see java.lang.Object#hashCode()
     */
    @Override
    public int hashCode() {
        final int PRIME = 37;
        int result = 17;

        if (anchorPoint != null) {
            result = result * PRIME + anchorPoint.hashCode();
        }

        if (displacement != null) {
            result = result * PRIME + displacement.hashCode();
        }

        if (rotation != null) {
            result = result * PRIME + rotation.hashCode();
        }

        return result;
    }

    static PointPlacementImpl cast(org.geotools.api.style.LabelPlacement placement) {
        if (placement == null) {
            return null;
        } else if (placement instanceof PointPlacementImpl) {
            return (PointPlacementImpl) placement;
        } else if (placement instanceof org.geotools.api.style.PointPlacement) {
            org.geotools.api.style.PointPlacement pointPlacement = (org.geotools.api.style.PointPlacement) placement;
            PointPlacementImpl copy = new PointPlacementImpl();
            copy.setAnchorPoint(AnchorPointImpl.cast(pointPlacement.getAnchorPoint()));
            copy.setDisplacement(DisplacementImpl.cast(pointPlacement.getDisplacement()));
            return copy;
        }
        return null;
    }
}
