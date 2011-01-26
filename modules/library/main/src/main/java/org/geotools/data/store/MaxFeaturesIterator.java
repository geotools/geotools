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

/**
 * Iterator wrapper which caps the number of returned features;
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 * @source $URL$
 */
public class MaxFeaturesIterator<F extends Feature> implements Iterator<F> {

	Iterator<F> delegate;
	long max;
	long counter;
	
	public MaxFeaturesIterator( Iterator<F> delegate, long max ) {
		this.delegate = delegate;
		this.max = max;
		counter = 0;
	}
	
	public Iterator<F> getDelegate() {
		return delegate;
	}
	
	public void remove() {
		delegate.remove();
	}

	public boolean hasNext() {
		return delegate.hasNext() && counter < max; 
	}

	public F next() {
		if ( counter++ <= max ) {
			return delegate.next();
		}
		
		return null;
	}

}
