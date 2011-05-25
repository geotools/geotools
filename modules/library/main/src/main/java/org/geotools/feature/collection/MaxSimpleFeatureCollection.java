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
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/library/main/src/main/java/org/geotools/feature/collection/MaxSimpleFeatureCollection.java $
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
	
	public  FeatureReader<SimpleFeatureType,SimpleFeature> reader() throws IOException {
		return new DelegateFeatureReader<SimpleFeatureType,SimpleFeature>( getSchema(), features() );
	}
	
	public SimpleFeatureIterator features() {
		return new DelegateSimpleFeatureIterator( this, iterator() );
	}

	public void close(SimpleFeatureIterator close) {
		close.close();
	}

	public Iterator<SimpleFeature> iterator() {
		return new MaxFeaturesIterator<SimpleFeature>( delegate.iterator(), start, max );
	}
	
	public void close(Iterator<SimpleFeature> close) {
		Iterator<SimpleFeature> iterator = ((MaxFeaturesIterator<SimpleFeature>)close).getDelegate();
		delegate.close( iterator );
	}

	public SimpleFeatureCollection subCollection(Filter filter) {
		throw new UnsupportedOperationException();
	}

	public SimpleFeatureCollection sort(SortBy order) {
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
	
	public boolean add(SimpleFeature o) {
		long size = delegate.size();
		if ( size < max ) {
			return delegate.add( o );	
		}
		
		return false;
	}

	public boolean addAll(Collection c) {
		boolean changed = false;
		
		for ( Iterator<SimpleFeature> i = c.iterator(); i.hasNext(); ) {
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

	public ReferencedEnvelope getBounds() {
		//calculate manually
		return ReferencedEnvelope.reference( DataUtilities.bounds( this ) );
	}
}
