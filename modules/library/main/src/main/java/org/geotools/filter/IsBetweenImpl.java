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

import java.util.Collection;
import java.util.Collections;

import org.geotools.util.Converters;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.expression.Expression;

/**
 * Straight implementation of GeoAPI interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class IsBetweenImpl extends CompareFilterImpl implements BetweenFilter {

	private Expression expression;

	protected IsBetweenImpl(org.opengis.filter.FilterFactory factory, Expression lower, Expression expression, Expression upper ){
		super( factory, lower, upper );
		this.expression = expression;
		
		//backwards compatability
		filterType = FilterType.BETWEEN;
	}
	
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	//@Override
        public boolean evaluate(Object feature) {
	    //NC - support for multiple values
            final Object object0 = eval(expression, feature);
            final Object object1 = eval(expression1, feature);
            final Object object2 = eval(expression2, feature);
            
            if (!(object0 instanceof Collection) && !(object1 instanceof Collection) && !(object2 instanceof Collection)) {
                return evaluateInternal(object0, object1, object2);
            }
    
            Collection<Object> oValues = object0 instanceof Collection ? (Collection<Object>) object0
                    : Collections.<Object>singletonList(object0);
            Collection<Object> leftValues = object1 instanceof Collection ? (Collection<Object>) object1
                    : Collections.<Object>singletonList(object1);
            Collection<Object> rightValues = object2 instanceof Collection ? (Collection<Object>) object2
                    : Collections.<Object>singletonList(object2);
    
            for (Object value1 : leftValues) {
                for (Object value2 : rightValues) {
                    for (Object value0 : oValues) {
                        if (evaluateInternal(value0, value1, value2)) {
                            return true;
                        }
                    }
                }
            }
    
            return false;
        }
	
	public boolean evaluateInternal(Object value, Object lower, Object upper) {
		//first try to evaluate the bounds in terms of the middle
		Object o = value;
		Object l = Converters.convert(lower, o.getClass());
		Object u = Converters.convert(upper, o.getClass());
		if ( l == null || u == null ) {
			//that didn't work try converting all to same type as lower
			l = lower;
			o = Converters.convert(value, l.getClass());
			u = Converters.convert(upper, l.getClass());
			
			if ( o == null || u == null ) {
				//ok last try, try evaluating all in terms of upper
				u = upper;
				o = Converters.convert(value, u.getClass());
				l = Converters.convert(lower, u.getClass());
				
				if ( o == null || l == null ) {
					//no dice
					return false;
				}
			}
		}
		
		Comparable lc = comparable( l );
		Comparable uc = comparable( u );
		
		return lc.compareTo( o ) <= 0 && uc.compareTo( o ) >= 0;
	}

	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit( this, extraData );
	}

	public Expression getLowerBoundary() {
		return getExpression1();
	}

	public void setLowerBoundary(Expression lowerBoundary) {
		setExpression1( lowerBoundary );
	}

	public Expression getUpperBoundary() {
		return getExpression2();
	}

	public void setUpperBoundary(Expression upperBoundary) {
		setExpression2( upperBoundary );
	}
	
	/**
	 * @deprecated use {@link #getExpression()}
	 */
	public final org.geotools.filter.Expression getMiddleValue() {
		return (org.geotools.filter.Expression) getExpression();
	}
	
	/**
	 * @deprecated use {@link #setExpression(Expression) }
	 */
	public void addMiddleValue(org.geotools.filter.Expression middleValue) {
		setExpression( middleValue );
	}
    
    public String toString() {
        return "[ " + expression + " BETWEEN " + expression1 + " AND " + expression2 + " ]";
    }
	
}
