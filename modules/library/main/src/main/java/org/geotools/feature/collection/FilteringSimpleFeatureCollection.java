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
package org.geotools.feature.collection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.FilteringFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.sort.SortBy;

/**
 * Decorates a feature collection with one that filters content.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 * @source $URL$
 */
public class FilteringSimpleFeatureCollection extends DecoratingSimpleFeatureCollection  {

	/**
	 * The original feature collection.
	 */
	SimpleFeatureCollection delegate;
	/**
	 * the filter
	 */
	Filter filter;
	
	public FilteringSimpleFeatureCollection( FeatureCollection<SimpleFeatureType,SimpleFeature> delegate, Filter filter ) {
	    this( DataUtilities.simple( delegate), filter );
	}
	
	public FilteringSimpleFeatureCollection( SimpleFeatureCollection delegate, Filter filter ) {
		super(delegate);
		this.delegate = delegate;
		this.filter = filter;
	}
	
	public SimpleFeatureIterator features() {
	    return new FilteringFeatureIterator( delegate.features(), filter );
	}

	public void close(SimpleFeatureIterator close) {
		close.close();
	}

	public SimpleFeatureCollection subCollection(Filter filter) {
	    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
	    Filter subFilter = ff.and( this.filter, filter );
	    
	    return new FilteringSimpleFeatureCollection( delegate, subFilter );
	}

	public SimpleFeatureCollection sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		int count = 0;
		SimpleFeatureIterator i = features();
		try {
			while( i.hasNext() ) {
				count++; i.next();
			}
			
			return count;
		}
		finally {
		    i.close();
		}
	}

	public boolean isEmpty() {
		return size() == 0;
	}

	public Object[] toArray() {
		return toArray( new Object[ size() ] );
	}

	public <T> T[] toArray(T[] a) {
		List<SimpleFeature> list = new ArrayList<SimpleFeature>();
		SimpleFeatureIterator i = features();
		try {
			while( i.hasNext() ) {
				list.add( i.next() );
			}
			
			return list.toArray( a );
		}
		finally {
			i.close();
		}
	}

	public boolean contains(Object o) {
		return delegate.contains( o ) && filter.evaluate( o );
	}

	public boolean containsAll(Collection<?> c) {
		for ( Iterator<?> i = c.iterator(); i.hasNext(); ) {
			if ( !contains( i.next() ) ) {
				return false;
			}
		}
		
		return true;
	}

	public  FeatureReader<SimpleFeatureType,SimpleFeature> reader() throws IOException {
		return new DelegateFeatureReader<SimpleFeatureType,SimpleFeature>( getSchema(), features() );
	}

	public ReferencedEnvelope getBounds() {
		//calculate manually
		return DataUtilities.bounds( this );
	}

}
