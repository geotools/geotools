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

import org.geotools.api.filter.FilterFactory;
import org.geotools.api.filter.expression.Expression;
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * An AnchorPoint identifies the location inside a label or graphic to use as an "anchor" for
 * positioning it relative to a point geometry.
 *
 * @author Ian Turton
 * @version $Id$
 */
public  class AnchorPoint implements org.geotools.api.style.AnchorPoint, Cloneable {
    /** The logger for the default core module. */
    protected static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AnchorPoint.class);
    protected FilterFactory filterFactory;
    protected Expression anchorPointX = null;
    protected Expression anchorPointY = null;

    public AnchorPoint() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }
    /** Creates a new instance of DefaultAnchorPoint */
    public AnchorPoint(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
        try {
            anchorPointX = filterFactory.literal(0.0);
            anchorPointY = filterFactory.literal(0.5);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultAnchorPoint: " + ife);
        }
    }

    public AnchorPoint(FilterFactory filterFactory, Expression x, Expression y) {
        this.filterFactory = filterFactory;
        anchorPointX = x;
        anchorPointY = y;
    }

    static AnchorPoint cast(org.geotools.api.style.AnchorPoint anchor) {
        if (anchor == null) {
            return null;
        } else if (anchor instanceof AnchorPoint) {
            return (AnchorPoint) anchor;
        } else {
            AnchorPoint copy = new AnchorPoint();
            copy.setAnchorPointX(anchor.getAnchorPointX());
            copy.setAnchorPointY(anchor.getAnchorPointY());
            return copy;
        }
    }

    /**
     * Getter for property anchorPointX.
     *
     * @return Value of property anchorPointX.
     */
    @Override
    public Expression getAnchorPointX() {
        return anchorPointX;
    }

    /**
     * Setter for property anchorPointX.
     *
     * @param anchorPointX New value of property anchorPointX.
     */
    public void setAnchorPointX(Expression anchorPointX) {
        this.anchorPointX = anchorPointX;
    }

    /**
     * Define the anchor point.
     *
     * @param x Literal value of property anchorPointX
     */
    public void setAnchorPointX(double x) {
        this.anchorPointX = filterFactory.literal(x);
    }

    /**
     * Getter for property anchorPointY.
     *
     * @return Value of property anchorPointY.
     */
    @Override
    public Expression getAnchorPointY() {
        return anchorPointY;
    }

    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor   the style visitor
     * @param extraData
     */
    @Override
    public Object accept(StyleVisitor visitor, Object extraData) {
        return visitor.visit((org.geotools.api.style.AnchorPoint)this, extraData);
    }

    /**
     * Setter for property anchorPointY.
     *
     * @param anchorPointY New value of property anchorPointY.
     */
    public void setAnchorPointY(Expression anchorPointY) {
        this.anchorPointY = anchorPointY;
    }

    /**
     * Define the anchor point.
     *
     * @param x Literal value of property anchorPointX
     */
    public void getAnchorPointY(double x) {
        this.anchorPointY = filterFactory.literal(x);
    }





    static AnchorPoint DEFAULT =
            new AnchorPoint() {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Stroke may not be modified");
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
                public void accept(org.geotools.api.style.StyleVisitor visitor) {
                    cannotModifyConstant();
                }

                @Override
                public Object accept(StyleVisitor visitor, Object data) {
                    cannotModifyConstant();
                    return null;
                }

                @Override
                public Expression getAnchorPointX() {
                    return ConstantExpression.constant(0.5);
                }

                @Override
                public Expression getAnchorPointY() {
                    return ConstantExpression.constant(0.5);
                }
            };



    /**
     * calls the visit method of a StyleVisitor
     *
     * @param visitor the style visitor
     */
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
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Never happen");
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

        if (obj instanceof AnchorPoint) {
            AnchorPoint other = (AnchorPoint) obj;

            return Utilities.equals(this.anchorPointX, other.anchorPointX)
                    && Utilities.equals(this.anchorPointY, other.anchorPointY);
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

        if (anchorPointX != null) {
            result = (result * PRIME) + anchorPointX.hashCode();
        }

        if (anchorPointY != null) {
            result = (result * PRIME) + anchorPointY.hashCode();
        }

        return result;
    }
}
