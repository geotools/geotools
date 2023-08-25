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
import org.geotools.api.style.LabelPlacement;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class PointPlacement
        implements Cloneable, org.geotools.api.style.PointPlacement, LabelPlacement {
    public static final AnchorPoint DEFAULT_ANCHOR_POINT =
            new AnchorPoint() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException(
                            "Constant AnchorPoint may not be modified");
                }

                @Override
                public void setAnchorPointX(Expression x) {
                    cannotModifyConstant();
                }

                @Override
                public void setAnchorPointY(Expression y) {
                    cannotModifyConstant();
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
            org.geotools.util.logging.Logging.getLogger(PointPlacement.class);

    private AnchorPoint anchorPoint = new AnchorPoint();
    private Displacement displacement = new Displacement();
    private Expression rotation = null;

    public PointPlacement() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public PointPlacement(FilterFactory factory) {
        try {
            rotation = factory.literal(Integer.valueOf(0));
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultPointPlacement: " + ife);
        }
    }

    /**
     * Returns the AnchorPoint which identifies the location inside a text label to use as an
     * "anchor" for positioning it relative to a point geometry.
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
    public void setAnchorPoint(org.geotools.api.style.AnchorPoint anchorPoint) {
        if (this.anchorPoint == anchorPoint) {
            return;
        }
        this.anchorPoint = AnchorPoint.cast(anchorPoint);
    }

    /**
     * Returns the Displacement which gives X and Y offset displacements to use for rendering a text
     * label near a point.
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
    public void setDisplacement(org.geotools.api.style.Displacement displacement) {
        if (this.displacement == displacement) {
            return;
        }
        this.displacement = Displacement.cast(displacement);
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
    public void setRotation(Expression rotation) {
        this.rotation = rotation;
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
            PointPlacement clone = (PointPlacement) super.clone();
            clone.anchorPoint = (AnchorPoint) ((Cloneable) anchorPoint).clone();
            clone.displacement = (Displacement) ((Cloneable) displacement).clone();

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

        if (obj instanceof PointPlacement) {
            PointPlacement other = (PointPlacement) obj;

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
            result = (result * PRIME) + anchorPoint.hashCode();
        }

        if (displacement != null) {
            result = (result * PRIME) + displacement.hashCode();
        }

        if (rotation != null) {
            result = (result * PRIME) + rotation.hashCode();
        }

        return result;
    }

    static PointPlacement cast(org.geotools.api.style.LabelPlacement placement) {
        if (placement == null) {
            return null;
        } else if (placement instanceof PointPlacement) {
            return (PointPlacement) placement;
        } else if (placement instanceof org.geotools.api.style.PointPlacement) {
            org.geotools.api.style.PointPlacement pointPlacement =
                    (org.geotools.api.style.PointPlacement) placement;
            PointPlacement copy = new PointPlacement();
            copy.setAnchorPoint(AnchorPoint.cast(pointPlacement.getAnchorPoint()));
            copy.setDisplacement(Displacement.cast(pointPlacement.getDisplacement()));
            return copy;
        }
        return null;
    }
}
