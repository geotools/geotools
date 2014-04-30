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

import org.opengis.filter.FilterVisitor;
import org.opengis.filter.PropertyIsGreaterThanOrEqualTo;
import org.opengis.filter.expression.Expression;
/**
 * @author jdeolive
 *
 *
 *
 * @source $URL$
 */
public class IsGreaterThanOrEqualToImpl extends MultiCompareFilterImpl
		implements PropertyIsGreaterThanOrEqualTo {

    @Deprecated
    protected IsGreaterThanOrEqualToImpl() {
        this(null, null);
    }

    protected IsGreaterThanOrEqualToImpl(Expression expression1, Expression expression2) {
        this(expression1, expression2, false);
    }

    public IsGreaterThanOrEqualToImpl(Expression expr1, Expression expr2, boolean matchCase) {
        super(expr1, expr2, matchCase);
    }

    protected IsGreaterThanOrEqualToImpl(Expression expression1, Expression expression2,
            MatchAction matchAction) {
        this(expression1, expression2, false, matchAction);
    }
    
    public IsGreaterThanOrEqualToImpl( Expression expr1,
            Expression expr2, boolean matchCase, MatchAction matchAction) {
        super(expr1, expr2, matchCase, matchAction);
    }
	
	@Override
	public boolean evaluateInternal(Object v1, Object v2) {
		Object[] values = eval( v1, v2 );
		Comparable value1 = comparable( values[ 0 ] );
		Comparable value2 = comparable( values[ 1 ] );
		
		return value1 != null && value2 != null && compare(value1,value2) >= 0;
	}

	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}
}
