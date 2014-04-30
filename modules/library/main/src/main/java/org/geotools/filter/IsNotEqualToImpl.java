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
import org.opengis.filter.PropertyIsNotEqualTo;
import org.opengis.filter.expression.Expression;

/**
 * 
 *
 * @source $URL$
 */
public class IsNotEqualToImpl extends MultiCompareFilterImpl
	implements PropertyIsNotEqualTo {
    
    IsEqualsToImpl delegate;

    @Deprecated
    protected IsNotEqualToImpl() {
        this(null, null);
    }

    protected IsNotEqualToImpl(Expression e1, Expression e2) {
        this(e1, e2, true);
    }

    protected IsNotEqualToImpl(MatchAction matchAction) {
        this(null, null, matchAction);
    }

    protected IsNotEqualToImpl(Expression e1, Expression e2, MatchAction matchAction) {
        this(e1, e2, true, matchAction);
    }

    protected IsNotEqualToImpl(Expression expression1, Expression expression2, boolean matchCase) {
        this(expression1, expression2, matchCase, MatchAction.ANY);
    }

    protected IsNotEqualToImpl(Expression expression1, Expression expression2, boolean matchCase,
            MatchAction matchAction) {
        super(expression1, expression2, matchCase, matchAction);
        delegate = new IsEqualsToImpl(expression1, expression2, matchCase);
    }

	@Override
	public boolean evaluateInternal(Object v1, Object v2) {
		return !delegate.evaluateInternal(v1, v2);
	}
	
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit( this, extraData );
	}

}
