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

import java.util.Iterator;
import java.util.List;

import org.opengis.filter.Filter;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Or;
/**
 * @author jdeolive
 *
 * @source $URL$
 */
public class OrImpl extends LogicFilterImpl implements Or {
	
	protected OrImpl(org.opengis.filter.FilterFactory factory, List/*<Filter>*/ children) {
		super(factory, children );
		
		//backwards compatability with old type system
		filterType = LOGIC_OR;
	}
	
	public boolean evaluate(Object feature) {
		for (Iterator itr = children.iterator(); itr.hasNext();) {
			Filter filter = (Filter)itr.next();
			if( filter.evaluate( feature )) {
                return true;
            }
		}
		return false;
	}
	
	public Object accept(FilterVisitor visitor, Object extraData) {
		return visitor.visit(this,extraData);
	}
	
}

