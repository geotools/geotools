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
package org.geotools.data.store;

import java.util.Iterator;

import org.opengis.feature.Feature;
import org.opengis.filter.Filter;

/**
 * Decorates a {@link org.geotools.feature.Feature} iterator with one that
 * filters content.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class FilteringIterator<F extends Feature> implements Iterator<F> {

	/**
	 * Delegate iterator
	 */
	Iterator<F> delegate;
	/**
	 * The Filter
	 */
	Filter filter;
	/**
	 * Next feature
	 */
	F next;
	
	public FilteringIterator( Iterator<F> delegate, Filter filter ) {
		this.delegate = delegate;
		this.filter = filter;
	}
	
	public Iterator<F> getDelegate() {
		return delegate;
	}
	
	public void remove() {
		delegate.remove();
	}

	public boolean hasNext() {
		if ( next != null ) {
			return true;
		}
		
		while( delegate.hasNext() ) {
			F peek =  delegate.next();
			if ( filter.evaluate( peek ) ) {
				next = peek;
				break;
			}
		}
		
		return next != null;
	}

	public F next() {
		F f = next;
		next = null;
		return f;
	}

}
