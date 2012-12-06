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
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.Feature;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * SimpleFeatureCollection wrapper which limits the number of features returned.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 * @source $URL$
 */
public class MaxFeaturesFeatureCollection<T extends FeatureType, F extends Feature> extends
        DecoratingFeatureCollection<T, F>  {

	FeatureCollection<T, F> delegate;
	long max;
	
	public MaxFeaturesFeatureCollection( FeatureCollection<T, F> delegate, long max ) {
		super(delegate);
		this.delegate = delegate;
		this.max = max;
	}
	
	public  FeatureReader<T, F> reader() throws IOException {
		return new DelegateFeatureReader<T, F>( getSchema(), features() );
	}
	
	public FeatureIterator<F> features() {
		return new MaxFeaturesIterator<F>( delegate.features(), max );
	}

	public FeatureCollection<T, F> subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	public FeatureCollection<T, F> sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		return (int) Math.min( delegate.size(), max );
	}

	public boolean isEmpty() {
		return delegate.isEmpty() || max == 0;
	}

	public Object[] toArray() {
		return toArray( new Object[ size() ] );
	}

	public <O> O[] toArray(O[] a) {
		List<F> list = new ArrayList<F>();
		FeatureIterator<F> i = features();
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
	
	public boolean containsAll(Collection<?>c) {
		for ( Iterator<?> i = c.iterator(); i.hasNext(); ) {
			if ( !contains( i.next() ) ) {
				return false;
			}
		}		
		return true;
	}

	public ReferencedEnvelope getBounds() {
		//calculate manually
		return DataUtilities.bounds( this );
	}
}
