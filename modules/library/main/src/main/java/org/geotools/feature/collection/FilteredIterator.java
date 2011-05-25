/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.feature.collection;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.geotools.feature.FeatureCollection;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;

/**
 * Provides an implementation of Iterator that will filter
 * contents using the provided filter.
 * <p>
 * This is a *Generic* iterator not limited to Feature, this
 * will become more interesting as Filter is able to evaulate
 * itself with more things then just Features.
 * </p>
 * <p>
 * This also explains the use of Collection (where you may
 * have expected a FeatureCollection). However
 * <code>FeatureCollectoin.close( iterator )</code> will be
 * called on the internal delgate.
 * </p>
 *  
 * @author Jody Garnett, Refractions Research, Inc.
 *
 * @source $URL$
 */
public class FilteredIterator<F extends Feature> implements Iterator<F> {
	/** Used to close the delgate, or null */
	private FeatureCollection<? extends FeatureType, F> collection;
	private Iterator<F> delegate;
	private Filter filter;

	private F next;
	
	public FilteredIterator(Iterator<F> iterator, Filter filter) {
		this.collection = null;
		this.delegate = iterator;
		this.filter = filter;
	}
	
	public FilteredIterator(FeatureCollection<? extends FeatureType, F> collection, Filter filter) {
		this.collection = collection;
		this.delegate = collection.iterator();
		this.filter = filter;
		next = getNext();
	}
	
	/** Package protected, please use SubFeatureCollection.close( iterator ) */
	void close(){
		if( collection != null ){
			collection.close( delegate );
		}
		collection = null;
		delegate = null;
		filter = null;
		next = null;
	}
	
	private F getNext() {
		F item = null;
		while (delegate.hasNext()) {
			item = delegate.next();
			if (filter.evaluate( item )){
				return item;
			}
		}
		return null;
	}

	public boolean hasNext() {
		return next != null;
	}

	public F next() {
		if(next == null){
			throw new NoSuchElementException();
		}
		F current = next;
		next = getNext();
		return current;
	}

	public void remove() {
		if( delegate == null ) throw new IllegalStateException();
		
	    delegate.remove();
	}
}
