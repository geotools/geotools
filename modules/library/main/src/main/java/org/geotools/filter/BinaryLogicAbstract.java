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

import java.util.List;

import org.opengis.filter.BinaryLogicOperator;

/**
 * @author jdeolive
 *
 *
 * @source $URL$
 */
public abstract class BinaryLogicAbstract extends AbstractFilter implements BinaryLogicOperator {
	protected List/*<Filter>*/ children;
	
	protected BinaryLogicAbstract(org.opengis.filter.FilterFactory factory, List/*<Filter>*/ children ) {
		super(factory);
		this.children = children;
	}
	
	public List/*<Filter>*/ getChildren() {
		return children;
	}
	
	public void setChildren(List children) {
		this.children = children;
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
}
