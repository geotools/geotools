/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.factory.Hints;
import org.geotools.util.ConverterFactory;
import org.geotools.util.Converters;
import org.opengis.filter.BinaryComparisonOperator;
import org.opengis.filter.expression.Expression;

/**
 * Abstract implemention for binary filters.
 * 
 * @author Justin Deoliveira, The Open Planning Project, jdeolive@openplans.org
 *
 * @source $URL$
 */
public abstract class BinaryComparisonAbstract extends AbstractFilter 
	implements BinaryComparisonOperator {

	protected Expression expression1;
	protected Expression expression2;

	boolean matchingCase;
	
	protected BinaryComparisonAbstract(org.opengis.filter.FilterFactory factory) {
		this(factory,null,null);
	}
	
	protected BinaryComparisonAbstract(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2 ) {
		this(factory,expression1,expression2,true);
	}
	
	protected BinaryComparisonAbstract(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2, boolean matchingCase ) {
		super(factory);
		this.expression1 = expression1;
		this.expression2 = expression2;		
		this.matchingCase = matchingCase;
	} 
	
	public Expression getExpression1() {
		return expression1;
	}

	public void setExpression1(Expression expression) {
		this.expression1 = expression;
	}
	
	public Expression getExpression2() {
		return expression2;
	}
	
	public void setExpression2(Expression expression) {
		this.expression2 = expression;
	}
	
	public boolean isMatchingCase() {
		return matchingCase;
	}
	
	public Filter and(org.opengis.filter.Filter filter) {        
		return (Filter) factory.and(this, filter);
	}

	public Filter or(org.opengis.filter.Filter filter) {
		return (Filter) factory.or(this, filter);
	}

	public Filter not() {
		return (Filter) factory.not(this);
	}

    /**
     * Convenience method which evaluates the expressions and trys to align the values to be of the
     * same type.
     * <p>
     * If the values can not be aligned, the original values are returned.
     * </p>
     * 
     * @return
     */
    protected Object[] eval(Object object) {
        Object v1 = eval(getExpression1(), object);
        Object v2 = eval(getExpression2(), object);
    
        if (v1 != null && v2 != null) {
            // try to convert so that values are of same type
            if (v1.getClass().equals(v2.getClass())) {
                // nothing to do
                return new Object[] { v1, v2 };
            }
    
            // try safe conversions
            Hints hints = new Hints(ConverterFactory.SAFE_CONVERSION, Boolean.TRUE);
            Object o = Converters.convert(v2, v1.getClass(), hints);
            if (o != null) {
                return new Object[] { v1, o };
            }
            // try the other way
            o = Converters.convert(v1, v2.getClass(), hints);
            if (o != null) {
                return new Object[] { o, v2 };
            }
    
            // unsafe conversions
            hints.put(ConverterFactory.SAFE_CONVERSION, Boolean.FALSE);
            o = Converters.convert(v2, v1.getClass(), hints);
            if (o != null) {
                return new Object[] { v1, o };
            }
            o = Converters.convert(v1, v2.getClass(), hints);
            if (o != null) {
                return new Object[] { o, v2 };
            }
    
        }
    
        return new Object[] { v1, v2 };
    }

	/**
	 * Wraps an object in a Comparable.
	 * @param value The original value.
	 * @return A comparable
	 */
	protected final Comparable comparable( Object value ){
		if ( value == null ) {
			return null;
		} else if( value instanceof Comparable ){
			return (Comparable) value;
		} else {
			return String.valueOf( value );
		}
	}
}
