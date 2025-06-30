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
package org.geotools.filter;

import static org.geotools.filter.Filters.getExpressionType;

import org.geotools.api.filter.expression.ExpressionVisitor;
import org.geotools.api.filter.expression.Literal;
import org.geotools.util.Converters;
import org.locationtech.jts.geom.Envelope;
import org.locationtech.jts.geom.Geometry;

/**
 * Defines an expression that holds a literal for return.
 *
 * @author Rob Hranac, Vision for New York
 * @version $Id$
 */
public class LiteralExpressionImpl extends DefaultExpression implements Literal {

    /** Holds a reference to the literal. */
    private Object literal = null;

    /** Constructor with literal. */
    protected LiteralExpressionImpl() {}

    /**
     * Constructor with literal.
     *
     * @param literal The literal to store inside this expression.
     * @throws IllegalFilterException This literal type is not in scope.
     */
    public LiteralExpressionImpl(Object literal) throws IllegalFilterException {
        this.setValue(literal);
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience one for integers an Integer object will
     * be constructed, and no IllegalFilterException can ever be thrown.
     *
     * @param value The integer to store inside this expression.
     */
    protected LiteralExpressionImpl(int value) {
        try {
            this.setValue(Integer.valueOf(value));
        } catch (IllegalFilterException ile) {
            // this is imposible as this is only thrown for
            // invalid types, and Integer is a valid type
            throw new AssertionError("LiteralExpressionImpl is broken, it should accept Integers");
        }
    }

    protected LiteralExpressionImpl(long value) {
        try {
            this.setValue(Long.valueOf(value));
        } catch (IllegalFilterException ile) {
            // this is imposible as this is only thrown for
            // invalid types, and Double is a valid type
            throw new AssertionError("LiteralExpressionImpl is broken, it should accept Longs");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience one for doubles an Double object will be
     * constructed, and no IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(double value) {
        try {
            this.setValue(Double.valueOf(value));
        } catch (IllegalFilterException ile) {
            // this is imposible as this is only thrown for
            // invalid types, and Double is a valid type
            throw new AssertionError("LiteralExpressionImpl is broken, it should accept Doubles");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience one for doubles an Double object will be
     * constructed, and no IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(String value) {
        try {
            this.setValue(value);
        } catch (IllegalFilterException ile) {
            // this is imposible as this is only thrown for
            // invalid types, and String is a valid type
            throw new AssertionError("LiteralExpressionImpl is broken, it should accept Strings");
        }
    }

    /**
     * Retrieves the literal of this expression.
     *
     * @return the literal held by this expression.
     */
    @Override
    public Object getValue() {
        return literal;
    }

    /**
     * Sets the literal.
     *
     * @param literal The literal to store inside this expression.
     * @throws IllegalFilterException This literal type is not in scope.
     */
    public final void setValue(Object literal) {
        this.literal = literal;
    }

    @Override
    public Object evaluate(Object feature) {
        return literal;
    }

    @Override
    public <T> T evaluate(Object feature, Class<T> context) {
        return Converters.convert(literal, context);
    }

    /**
     * Return this filter as a string.
     *
     * @return String representation of this geometry filter.
     */
    @Override
    public String toString() {
        return literal == null ? "NULL" : literal.toString();
    }

    /**
     * Compares this filter to the specified object. Returns true if the passed in object is the same as this
     * expression. Checks to make sure the expression types are the same as well as the literals.
     *
     * @param obj - the object to compare this ExpressionLiteral against.
     * @return true if specified object is equal to this expression; false otherwise.
     * @task REVISIT: missmatched types now considered not equal. This may be a problem when comparing Doubles and
     *     Integers
     */
    @Override
    public boolean equals(Object obj) {
        if (obj instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl expLit = (LiteralExpressionImpl) obj;
            // This is a problem.  The Expression with type String of "2.0"
            // should be equals to the Expression with type Integer of "2.0"
            // Same thing with doubles and integers (as noted in the javadocs)

            // null handling
            if (this.literal == null) {
                return expLit.literal == null;
            } else if (expLit.literal == null) {
                return false;
            }

            // direct comparison if same type
            if (getExpressionType(this) == getExpressionType(expLit)) {
                if (this.literal.equals(expLit.literal)) {
                    return true;
                }
            }

            // do the conversion dance
            int expressionType = getExpressionType(this);
            if (expressionType == ExpressionType.LITERAL_GEOMETRY && this.literal instanceof Geometry) {
                return ((Geometry) this.literal).equalsExact(expLit.evaluate(null, Geometry.class));
            } else if (expressionType == ExpressionType.LITERAL_GEOMETRY && this.literal instanceof Envelope) {
                return this.literal.equals(expLit.evaluate(null, Envelope.class));
            } else if (expressionType == ExpressionType.LITERAL_INTEGER) {
                return this.literal.equals(expLit.evaluate(null, Integer.class));
            } else if (expressionType == ExpressionType.LITERAL_STRING) {
                return this.literal.equals(expLit.evaluate(null, String.class));
            } else if (expressionType == ExpressionType.LITERAL_DOUBLE) {
                return this.literal.equals(expLit.evaluate(null, Double.class));
            } else if (expressionType == ExpressionType.LITERAL_LONG) {
                return this.literal.equals(expLit.evaluate(null, Long.class));
            } else {
                // try to convert the other to the current type
                Object other = expLit.evaluate(null, this.literal.getClass());
                if (other != null) {
                    return other.equals(this.literal);
                }
                // converters might be one way, try the opposite
                other = expLit.getValue();
                Object converted = this.evaluate(null, other.getClass());
                if (converted != null) {
                    return converted.equals(other);
                }
                // final attemp with a string to string comparison
                String str1 = this.evaluate(null, String.class);
                String str2 = expLit.evaluate(null, String.class);
                return str1 != null && str1.equals(str2);
            }
        } else if (obj instanceof Literal) {
            // some other Literal implementation like ConstantExpression
            Literal other = (Literal) obj;
            return equals(new LiteralExpressionImpl(other.getValue()));
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return the hash code for this literal expression
     */
    @Override
    public int hashCode() {
        int result = 17;

        result = 37 * result + (literal == null ? 0 : literal.hashCode());
        result = 37 * result + Filters.getExpressionType(this);

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance. Typicaly used by Filter decoders, but may
     * also be used by any thing which needs infomration from filter structure. Implementations should always call:
     * visitor.visit(this); It is importatant that this is not left to a parent class unless the parents API is
     * identical.
     *
     * @param visitor The visitor which requires access to this filter, the method must call visitor.visit(this);
     */
    @Override
    public Object accept(ExpressionVisitor visitor, Object extraData) {
        return visitor.visit(this, extraData);
    }
}
