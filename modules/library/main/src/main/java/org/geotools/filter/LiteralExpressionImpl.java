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

import java.math.BigDecimal;

import org.geotools.util.Converters;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.expression.ExpressionVisitor;
import org.opengis.filter.expression.Literal;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Defines an expression that holds a literal for return.
 *
 * @author Rob Hranac, Vision for New York
 * @source $URL$
 * @version $Id$
 */
public class LiteralExpressionImpl extends DefaultExpression
    implements LiteralExpression {
    
    private static BigDecimal MAX_LONG = BigDecimal.valueOf(Long.MAX_VALUE);
    private static BigDecimal MIN_LONG = BigDecimal.valueOf(Long.MIN_VALUE);
    
    /** Holds a reference to the literal. */
    private Object literal = null;
    
    /** The converted value guessed inside evaluate(Feature) **/
    private Object parsedValue = null;

    /**
     * Constructor with literal.
     */
    protected LiteralExpressionImpl() {
    }

    /**
     * Constructor with literal.
     *
     * @param literal The literal to store inside this expression.
     *
     * @throws IllegalFilterException This literal type is not in scope.
     */
    protected LiteralExpressionImpl(Object literal)
        throws IllegalFilterException {
        this.setLiteral(literal);
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for integers an Integer object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The integer to store inside this expression.
     */
    protected LiteralExpressionImpl(int value) {
        try {
            this.setLiteral(new Integer(value));
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and Integer is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Integers");
        }
    }
    
    protected LiteralExpressionImpl(long value) {
        try {
            this.setLiteral(new Long(value));
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and Double is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Longs");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for doubles an Double object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(double value) {
        try {
            this.setLiteral(new Double(value));
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and Double is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Doubles");
        }
    }

    /**
     * Constructor with literal. This alternative constructor is a convinience
     * one for doubles an Double object will be constructed, and no
     * IllegalFilterException can ever be thrown.
     *
     * @param value The double to store inside this expression.
     */
    protected LiteralExpressionImpl(String value) {
        try {
            this.setLiteral(value);
        } catch (IllegalFilterException ile) {
            //this is imposible as this is only thrown for
            //invalid types, and String is a valid type
            throw new AssertionError(
                "LiteralExpressionImpl is broken, it should accept Strings");
        }
    }

    /**
     * Returns the literal type.
     *
     * @return the short representation of the expression type.
     */
    public short getType() {
        return expressionType;
    }

    /**
     * This method calls {@link #setValue(Object)}.
     * 
     * @deprecated use {@link #setValue(Object)}.
     * 
     */
    public final void setLiteral(Object literal) throws IllegalFilterException {
        setValue(literal);
    }

    /**
     * This method calls {@link #getValue()}.
     * 
     * @deprecated use {@link #getValue()}.
     * 
     */
    public final Object getLiteral() {
        return getValue();
    }

    /**
     * Retrieves the literal of this expression.
     *
     * @return the literal held by this expression.
     * 
     */
    public Object getValue() {
    	return literal;
    }
    
    /**
     * Sets the literal.
     *
     * @param literal The literal to store inside this expression.
     *
     * @throws IllegalFilterException This literal type is not in scope.
     */
    public final void setValue(Object literal) {
    	if (literal instanceof Double) {
            expressionType = LITERAL_DOUBLE;
        } else if (literal instanceof Integer) {
            expressionType = LITERAL_INTEGER;
        } else if (literal instanceof Long) {
            expressionType = LITERAL_LONG;
        } else if (literal instanceof String) {
            expressionType = LITERAL_STRING;
        } else if (literal instanceof Geometry) {
            expressionType = LITERAL_GEOMETRY;
        } else {
            expressionType = LITERAL_UNDECLARED;
        }

        this.literal = literal;
    }
    
    
    /**
     * Gets the value of this literal.
     *
     * @param feature Required by the interface but not used.
     *
     * @return the literal held by this expression.  Ignores the passed in
     *         feature.  The literal held by this expression is almost invariably
     *         a java.lang.String (so that no leading-zeros are lost during a string->
     *         Class conversion.  This method will attempt to form the internal
     *         String into a Integer, Double or BigInteger, before failing and
     *         defaulting to a String.  To speed things up significantly, use the
     *         evaluate(Object, Class) method so that we don't have to guess
     *         at what you expect back from this evaluate method!
     *
     * @throws IllegalArgumentException Feature does not match declared schema.
     */
    public Object evaluate(SimpleFeature feature)
    	throws IllegalArgumentException {
    	return evaluate((Object)feature);
    }

    public Object evaluate(Object feature) {
        //hrm.  Well, now that' we're always storing the internals of 
        //Literals as strings, we need to be slightly smart about how we
        //return what's inside.  Some (err, lots) of code relies on this
        //method to return an instance of the correct TYPE.  I guess we should
        //try and be somewhat smart about this.
        
        //ASSERTION: literal is always a string.
        
        // caching, don't try to reparse over and over the same literal
        if(parsedValue != null)
            return parsedValue;
        
        // actual parsing
        if (literal == null || !(literal instanceof String)) {
            parsedValue = literal;
        } else {
            String s = (String) literal;
            
            // check if it's a number
            try {
                BigDecimal bd = new BigDecimal(s);
                
                // check if it has a decimal part 
                if(bd.scale() > 0) {
                    double d = bd.doubleValue();
                    // if too big for double, it will become infinite
                    if(!Double.isInfinite(d))
                        parsedValue = d;
                    else
                        parsedValue = bd;
                } else {
                    // it's integral, see if we can convert it to a long or int
                    if(bd.compareTo(MIN_LONG) >= 0 && bd.compareTo(MAX_LONG) <= 0) {
                        // in range to be a long 
                        long l = bd.longValue();
                        // if this test passes, it's actually an int
                        if((int) l == l)
                            parsedValue = new Integer((int) l);
                        else
                            parsedValue = new Long(l);
                    } else {
                        // out of range, switch to big decimal
                        parsedValue = bd.toBigInteger();
                    }
                }
            } catch(Exception e) {
                // ok, it's not a number, let's keep it as it is
                parsedValue = literal;
            }
            
            
        }  
        return parsedValue;
    }
    
    public Object evaluate(Object feature, Class context) {
        return Converters.convert(literal, context);
    }

    /**
     * Return this filter as a string.
     *
     * @return String representation of this geometry filter.
     */
    public String toString() {
        return literal == null ? "NULL" : literal.toString();
    }

    /**
     * Compares this filter to the specified object.  Returns true  if the
     * passed in object is the same as this expression.  Checks  to make sure
     * the expression types are the same as well as the literals.
     *
     * @param obj - the object to compare this ExpressionLiteral against.
     *
     * @return true if specified object is equal to this expression; false
     *         otherwise.
     *
     * @task REVISIT: missmatched types now considered not equal. This may be a
     *       problem when comparing Doubles and Integers
     */
    public boolean equals(Object obj) {
        if (obj instanceof LiteralExpressionImpl) {
            LiteralExpressionImpl expLit = (LiteralExpressionImpl) obj;
            // This is a problem.  The Expression with type String of "2.0"
            // should be equals to the Expression with type Integer of "2.0"
            // Same thing with doubles and integers (as noted in the javadocs)

            if (this.literal == null) {
                return expLit.literal == null;
            }
            if (this.expressionType == expLit.expressionType) {
                if(this.literal.equals(expLit.literal)) {
                    return true;
                } else if(expressionType == LITERAL_GEOMETRY && this.literal instanceof Geometry &&
                        expLit.literal instanceof Geometry) {
                    // deal with JTS stupid equal implementation
                    if(((Geometry) literal).equals((Geometry) expLit.literal)) {
                        return true;
                    }
                }
            }

            if (expressionType == LITERAL_GEOMETRY) {
                return ((Geometry) this.literal).equals((Geometry) expLit.evaluate(null, Geometry.class));
            } else if (expressionType == LITERAL_INTEGER) {
                return ((Integer) this.literal).equals((Integer) expLit.evaluate(null, Integer.class));
            } else if (expressionType == LITERAL_STRING) {
                return ((String) this.literal).equals((String) expLit.evaluate(null, String.class));
            } else if (expressionType == LITERAL_DOUBLE) {
                return ((Double) this.literal).equals((Double) expLit.evaluate(null, Double.class));
            } else if (expressionType == LITERAL_LONG) {
                return ((Long) this.literal).equals((Long) expLit.evaluate(null, Long.class));                
            } else {
                return true;
            }
        }
        else if (obj instanceof Literal) {
            // some other Literal implementation like ConstantExpression
            Literal other = (Literal) obj;
            return equals( new LiteralExpressionImpl( other.getValue() ) );
        } else {
            return false;
        }
    }

    /**
     * Override of hashCode method.
     *
     * @return the hash code for this literal expression
     */
    public int hashCode() {
        int result = 17;

        result = (37 * result) + ((literal == null) ? 0 : literal.hashCode());
        result = (37 * result) + expressionType;

        return result;
    }

    /**
     * Used by FilterVisitors to perform some action on this filter instance.
     * Typicaly used by Filter decoders, but may also be used by any thing
     * which needs infomration from filter structure. Implementations should
     * always call: visitor.visit(this); It is importatant that this is not
     * left to a parent class unless the parents API is identical.
     *
     * @param visitor The visitor which requires access to this filter, the
     *        method must call visitor.visit(this);
     */
    public Object accept(ExpressionVisitor visitor, Object extraData) {
    	return visitor.visit(this,extraData);
    }
}
