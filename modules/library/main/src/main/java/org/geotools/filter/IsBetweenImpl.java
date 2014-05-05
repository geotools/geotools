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
import org.opengis.filter.PropertyIsBetween;
import org.opengis.filter.expression.Expression;

/**
 * Straight implementation of GeoAPI interface.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class IsBetweenImpl extends CompareFilterImpl implements PropertyIsBetween {

	private Expression expression;
	
	protected MatchAction matchAction;

    protected IsBetweenImpl(Expression lower, Expression expression, Expression upper,
            MatchAction matchAction) {
        super(lower, upper);
        this.expression = expression;
        this.matchAction = matchAction;
    }

    protected IsBetweenImpl(Expression lower, Expression expression, Expression upper) {
        this(lower, expression, upper, MatchAction.ANY);
    }
	
	public Expression getExpression() {
		return expression;
	}
	public void setExpression(Expression expression) {
		this.expression = expression;
	}
	
	public MatchAction getMatchAction() {
	        return matchAction;
	}
	
	//@Override
        public boolean evaluate(Object feature) {
            //NC - support for multiple values
            final Object object0 = eval(expression, feature);
            final Object object1 = eval(expression1, feature);
            final Object object2 = eval(expression2, feature);
            
            if(object0 == null) {
                return false;
            }
            
            if (!(object0 instanceof Collection) && !(object1 instanceof Collection) && !(object2 instanceof Collection)) {
                return evaluateInternal(object0, object1, object2);
            }
    
            Collection<Object> oValues = object0 instanceof Collection ? (Collection<Object>) object0
                    : Collections.<Object>singletonList(object0);
            Collection<Object> leftValues = object1 instanceof Collection ? (Collection<Object>) object1
                    : Collections.<Object>singletonList(object1);
            Collection<Object> rightValues = object2 instanceof Collection ? (Collection<Object>) object2
                    : Collections.<Object>singletonList(object2);
    
            int count = 0;
           
            for (Object value1 : leftValues) {
                for (Object value2 : rightValues) {
                    for (Object value0 : oValues) {
                        boolean temp = evaluateInternal(value0, value1, value2);
                        if (temp) {
                           count++;
                        }
                               
                        switch (matchAction){
                           case ONE: if (count > 1) return false; break;
                           case ALL: if (!temp) return false; break;
                           case ANY: if (temp) return true; break;
                        }                        
                    }
                }
            }
    
            switch (matchAction){
                case ONE: return (count == 1);
                case ALL: return true;
                case ANY: return false;
                default: return false;
            }
        }
	
	public boolean evaluateInternal(Object value, Object lower, Object upper) {
		//first try to evaluate the bounds in terms of the middle
		Object o = value;
		if(o == null) {
		    return false;
		}
		
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

    
    public String toString() {
        return "[ " + expression + " BETWEEN " + expression1 + " AND " + expression2 + " ]";
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = super.hashCode();
        result = prime * result + ((expression == null) ? 0 : expression.hashCode());
        result = prime * result + ((matchAction == null) ? 0 : matchAction.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (!super.equals(obj))
            return false;
        if (getClass() != obj.getClass())
            return false;
        IsBetweenImpl other = (IsBetweenImpl) obj;
        if (expression == null) {
            if (other.expression != null)
                return false;
        } else if (!expression.equals(other.expression))
            return false;
        if (matchAction != other.matchAction)
            return false;
        return true;
    }
	
}
