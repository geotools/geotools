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
import org.geotools.api.style.StyleVisitor;
import org.geotools.api.style.TraversingStyleVisitor;
import org.geotools.api.util.Cloneable;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.ConstantExpression;
import org.geotools.util.Utilities;
import org.geotools.util.factory.GeoTools;

/**
 * Direct implementation of AnchorPoint.
 *
 * @author Ian Turton, CCG
 * @version $Id$
 */
public class AnchorPointImpl implements org.geotools.api.style.AnchorPoint, Cloneable {

    /**
     * get the x coordinate of the anchor point
     *
     * @return the expression which represents the X coordinate
     */
    static final FilterFactory ff = CommonFactoryFinder.getFilterFactory();

    static final AnchorPoint DEFAULT =
            new AnchorPointImpl(ff, ConstantExpression.constant(0.5), ConstantExpression.constant(0.5)) {
                private void cannotModifyConstant() {
                    throw new UnsupportedOperationException("Constant Stroke may not be modified");
                }

                @Override
                public void accept(StyleVisitor visitor) {
                    visitor.visit(this);
                }

                @Override
                public Object accept(TraversingStyleVisitor visitor, Object data) {
                    cannotModifyConstant();
                    return null;
                }

                @Override
                public Expression getAnchorPointX() {
                    return ConstantExpression.constant(0.5);
                }

                @Override
                public void setAnchorPointX(Expression x) {
                    cannotModifyConstant();
                }

                @Override
                public Expression getAnchorPointY() {
                    return ConstantExpression.constant(0.5);
                }

                @Override
                public void setAnchorPointY(Expression y) {
                    cannotModifyConstant();
                }
            };
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(AnchorPointImpl.class);

    private final FilterFactory filterFactory;
    private Expression anchorPointX = null;
    private Expression anchorPointY = null;

    public AnchorPointImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    /** Creates a new instance of DefaultAnchorPoint */
    public AnchorPointImpl(FilterFactory filterFactory) {
        this.filterFactory = filterFactory;
        try {
            anchorPointX = filterFactory.literal(0.0);
            anchorPointY = filterFactory.literal(0.5);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultAnchorPoint: " + ife);
        }
    }

    public AnchorPointImpl(FilterFactory filterFactory, Expression x, Expression y) {
        this.filterFactory = filterFactory;
        anchorPointX = x;
        anchorPointY = y;
    }

    static AnchorPointImpl cast(org.geotools.api.style.AnchorPoint anchor) {
        if (anchor == null) {
            return null;
        } else if (anchor instanceof AnchorPointImpl) {
            return (AnchorPointImpl) anchor;
        } else {
            AnchorPointImpl copy = new AnchorPointImpl();
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
    @Override
    public void setAnchorPointX(Expression anchorPointX) {
        this.anchorPointX = anchorPointX;
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
     * Setter for property anchorPointY.
     *
     * @param anchorPointY New value of property anchorPointY.
     */
    @Override
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

        if (obj instanceof AnchorPointImpl) {
            AnchorPointImpl other = (AnchorPointImpl) obj;

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
            result = result * PRIME + anchorPointX.hashCode();
        }

        if (anchorPointY != null) {
            result = result * PRIME + anchorPointY.hashCode();
        }

        return result;
    }
}
