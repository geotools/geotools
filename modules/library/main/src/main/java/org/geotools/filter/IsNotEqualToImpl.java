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

public class IsNotEqualToImpl extends CompareFilterImpl
	implements PropertyIsNotEqualTo {
    
    IsEqualsToImpl delegate;

	protected IsNotEqualToImpl(org.opengis.filter.FilterFactory factory) {
		this(factory,null,null);
	}
	
	protected IsNotEqualToImpl(org.opengis.filter.FilterFactory factory, Expression e1, Expression e2) {
		this( factory, e1, e2, true );
	}
	
	protected IsNotEqualToImpl(org.opengis.filter.FilterFactory factory, Expression expression1, Expression expression2, boolean matchCase ) {
		super(factory, expression1, expression2, matchCase);
		delegate = new IsEqualsToImpl(factory, expression1, expression2, matchCase);
		
		//backwards compat with old type system
		this.filterType = COMPARE_NOT_EQUALS;
	}

	//@Override
	public boolean evaluate(Object feature) {
		return !delegate.evaluate(feature);
	}
	
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit( this, extraData );
	}

}
