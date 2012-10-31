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
import org.geotools.data.store.MaxFeaturesIterator;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * SimpleFeatureCollection wrapper which limits the number of features returned.
 * 
 * @author Justin Deoliveira, The Open Planning Project
 *
 *
 *
 *
 * @source $URL$
 */
public class MaxSimpleFeatureCollection extends
        DecoratingSimpleFeatureCollection {

	SimpleFeatureCollection delegate;
	long start;
	long max;
	
    public MaxSimpleFeatureCollection(FeatureCollection<SimpleFeatureType, SimpleFeature> delegate,
            long max) {
        this(DataUtilities.simple(delegate), 0, max);
    }

    public MaxSimpleFeatureCollection(SimpleFeatureCollection delegate, long max) {
        this(delegate, 0, max);
    }

    public MaxSimpleFeatureCollection(SimpleFeatureCollection delegate, long start, long max) {
        super(delegate);
        this.delegate = delegate;
        this.start = start;
        this.max = max;
    }
	
	FeatureReader<SimpleFeatureType,SimpleFeature> reader() throws IOException {
	    return new DelegateFeatureReader<SimpleFeatureType,SimpleFeature>( getSchema(), features() );
	}
	
	public SimpleFeatureIterator features() {
		return new MaxFeaturesSimpleFeatureIterator( delegate.features(), start, max );
	}

	public SimpleFeatureCollection subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	public SimpleFeatureCollection sort(SortBy order) {
		throw new UnsupportedOperationException();
	}

	public int size() {
		int size = delegate.size();
		if(size < start) {
		    return 0;
		} else {
		    return (int) Math.min( size - start, max );
		}
	}

	public boolean isEmpty() {
		return delegate.isEmpty() || max == 0 || delegate.size() - start < 1;
	}

	public Object[] toArray() {
		return toArray( new Object[ size() ] );
	}	
    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] a) {
		List<T> list = new ArrayList<T>();
		SimpleFeatureIterator i = features();
		try {
			while( i.hasNext() ) {
				list.add( (T)i.next() );
			}			
			return list.toArray( a );
		}
		finally {
		    i.close();
		}
	}
	
	public boolean containsAll(Collection<?> c) {
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
