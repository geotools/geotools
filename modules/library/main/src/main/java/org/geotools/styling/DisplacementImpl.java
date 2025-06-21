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
import org.geotools.api.style.Displacement;
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
public class DisplacementImpl implements Cloneable, org.geotools.api.style.Displacement {
    /** Default Displacement instance. */
    public static final Displacement DEFAULT = new ConstantDisplacement() {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Stroke may not be modified");
        }

        @Override
        public Expression getDisplacementX() {
            return ConstantExpression.ZERO;
        }

        @Override
        public Expression getDisplacementY() {
            return ConstantExpression.ZERO;
        }

        @Override
        public Object accept(TraversingStyleVisitor visitor, Object extraData) {
            cannotModifyConstant();
            return null;
        }
    };
    /** Null Displacement instance. */
    public static final Displacement NULL = new ConstantDisplacement() {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Stroke may not be modified");
        }

        @Override
        public Expression getDisplacementX() {
            return ConstantExpression.NULL;
        }

        @Override
        public Expression getDisplacementY() {
            return ConstantExpression.NULL;
        }

        @Override
        public Object accept(TraversingStyleVisitor visitor, Object extraData) {
            cannotModifyConstant();
            return null;
        }
    };
    /** The logger for the default core module. */
    private static final java.util.logging.Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(DisplacementImpl.class);

    private FilterFactory filterFactory;
    private Expression displacementX = null;
    private Expression displacementY = null;

    public DisplacementImpl() {
        this(CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints()));
    }

    public DisplacementImpl(FilterFactory factory) {
        filterFactory = factory;

        try {
            displacementX = filterFactory.literal(0);
            displacementY = filterFactory.literal(0);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultDisplacement: " + ife);
        }
    }

    public DisplacementImpl(Expression dx, Expression dy) {
        filterFactory = CommonFactoryFinder.getFilterFactory(null);
        displacementX = dx;
        displacementY = dy;
    }

    public void setFilterFactory(FilterFactory factory) {
        filterFactory = factory;

        try {
            displacementX = filterFactory.literal(0);
            displacementY = filterFactory.literal(0);
        } catch (org.geotools.filter.IllegalFilterException ife) {
            LOGGER.severe("Failed to build defaultDisplacement: " + ife);
        }
    }

    /**
     * Setter for property displacementX.
     *
     * @param displacementX New value of property displacementX.
     */
    @Override
    public void setDisplacementX(Expression displacementX) {
        this.displacementX = displacementX;
    }
    /**
     * Set displacement x to the provided literal.
     *
     * @param displacementX New value of property displacementX.
     */
    public void setDisplacementX(double displacementX) {
        this.displacementX = filterFactory.literal(displacementX);
    }
    /**
     * Setter for property displacementY.
     *
     * @param displacementY New value of property displacementY.
     */
    @Override
    public void setDisplacementY(Expression displacementY) {
        this.displacementY = displacementY;
    }
    /**
     * Set displacement y to the provided literal.
     *
     * @param displacementY New value of property displacementX.
     */
    public void setDisplacementY(double displacementY) {
        this.displacementY = filterFactory.literal(displacementY);
    }

    /**
     * Getter for property displacementX.
     *
     * @return Value of property displacementX.
     */
    @Override
    public Expression getDisplacementX() {
        return displacementX;
    }

    /**
     * Getter for property displacementY.
     *
     * @return Value of property displacementY.
     */
    @Override
    public Expression getDisplacementY() {
        return displacementY;
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
     * @see org.geotools.api.util.Cloneable#clone()
     */
    @Override
    public Object clone() {
        try {
            return super.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Will not happen");
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

        if (obj instanceof DisplacementImpl) {
            DisplacementImpl other = (DisplacementImpl) obj;

            return Utilities.equals(displacementX, other.displacementX)
                    && Utilities.equals(displacementY, other.displacementY);
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

        if (displacementX != null) {
            result = result * PRIME + displacementX.hashCode();
        }

        if (displacementY != null) {
            result = result * PRIME + displacementY.hashCode();
        }

        return result;
    }

    static DisplacementImpl cast(org.geotools.api.style.Displacement displacement) {
        if (displacement == null) {
            return null;
        } else if (displacement instanceof DisplacementImpl) {
            return (DisplacementImpl) displacement;
        } else {
            DisplacementImpl copy = new DisplacementImpl();
            copy.setDisplacementX(displacement.getDisplacementX());
            copy.setDisplacementY(displacement.getDisplacementY());

            return copy;
        }
    }

    abstract static class ConstantDisplacement implements Displacement {
        private void cannotModifyConstant() {
            throw new UnsupportedOperationException("Constant Displacement may not be modified");
        }

        @Override
        public void setDisplacementX(Expression x) {
            cannotModifyConstant();
        }

        @Override
        public void setDisplacementY(Expression y) {
            cannotModifyConstant();
        }

        @Override
        public void accept(StyleVisitor visitor) {
            cannotModifyConstant();
        }
    }
}
