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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.DecoratingFeatureCollection;
import org.geotools.feature.collection.DelegateFeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Decorates a feature collection with one that filters content.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 * @source $URL$
 */
public class FilteringFeatureCollection<T extends FeatureType, F extends Feature> extends DecoratingFeatureCollection<T, F>  {

	/**
	 * The original feature collection.
	 */
	FeatureCollection<T, F> delegate;
	/**
	 * the filter
	 */
	Filter filter;
	
	public FilteringFeatureCollection( FeatureCollection<T, F> delegate, Filter filter ) {
		super(delegate);
		this.delegate = delegate;
		this.filter = filter;
	}
	
	public FeatureIterator<F> features() {
		return new DelegateFeatureIterator<F>( this, iterator() );
	}

	public void close(FeatureIterator<F> close) {
		close.close();
	}

	public Iterator<F> iterator() {
		return new FilteringIterator<F>( delegate.iterator(), filter );
	}
	
	public void close(Iterator<F> close) {
		FilteringIterator<F> filtering = (FilteringIterator<F>) close;
		delegate.close( filtering.getDelegate() );
	}

	public FeatureCollection<T, F> subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	public FeatureCollection<T, F> sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		int count = 0;
		Iterator<F> i = iterator();
		try {
			while( i.hasNext() ) {
				count++; i.next();
			}
			
			return count;
		}
		finally {
			close( i );
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object[] toArray() {
		return toArray( new Object[ size() ] );
	}

	public Object[] toArray(Object[] a) {
		List list = new ArrayList();
		Iterator i = iterator();
		try {
			while( i.hasNext() ) {
				list.add( i.next() );
			}
			
			return list.toArray( a );
		}
		finally {
			close( i );
		}
	}
	
	public boolean add(F o) {
		if ( !filter.evaluate( o ) ) {
			return false;
		}
		
		return delegate.add( o );
	}

	public boolean contains(Object o) {
		return delegate.contains( o ) && filter.evaluate( o );
	}

	public boolean addAll(Collection c) {
		boolean changed = false;
		
		for ( Iterator<F> i = c.iterator(); i.hasNext(); ) {
			changed = changed | add( i.next() );
		}
		
		return changed;
	}

	public boolean containsAll(Collection c) {
		for ( Iterator i = c.iterator(); i.hasNext(); ) {
			if ( !contains( i.next() ) ) {
				return false;
			}
		}
		
		return true;
	}

	public  FeatureReader<T, F> reader() throws IOException {
		return new DelegateFeatureReader<T, F>( getSchema(), features() );
	}

	public ReferencedEnvelope getBounds() {
		//calculate manually
		return ReferencedEnvelope.reference( DataUtilities.bounds( this ) );
	}

}
