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

import java.io.IOException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.visitor.BoundsVisitor;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.sort.SortBy;

/**
 * Used as a reasonable default implementation for subCollection.
 * <p>
 * Note: to implementors, this is not optimal, please do your own
 * thing - your users will thank you.
 * </p>
 * 
 * @author Jody Garnett, Refractions Research, Inc.
 *
 *
 *
 * @source $URL$
 */
public class SubFeatureCollection extends AbstractFeatureCollection {
    
    /** Filter */
    protected Filter filter;
    
    /** Original Collection */
	protected SimpleFeatureCollection collection;
	
    protected FilterFactory ff = CommonFactoryFinder.getFilterFactory( null );
        
    public SubFeatureCollection(SimpleFeatureCollection collection ) {
        this( collection, Filter.INCLUDE );
    }

    /**
     * @param collection Collection or AbstractFeatureCollection
     * @param subfilter
     */
    public SubFeatureCollection(SimpleFeatureCollection collection, Filter subfilter) {
        super(collection.getSchema());
        if (subfilter == null)
            subfilter = Filter.INCLUDE;
        if (subfilter.equals(Filter.EXCLUDE)) {
            throw new IllegalArgumentException("A subcollection with Filter.EXCLUDE would be empty");
        }
        if (collection instanceof SubFeatureCollection) {
            SubFeatureCollection filtered = (SubFeatureCollection) collection;
            if (subfilter.equals(Filter.INCLUDE)) {
                this.collection = filtered.collection;
                this.filter = filtered.filter();
            } else {
                this.collection = filtered.collection;
                this.filter = ff.and(filtered.filter(), subfilter);
            }
        } else if (collection instanceof Collection
                || collection instanceof AbstractFeatureCollection
                || collection instanceof AdaptorFeatureCollection) {
            this.collection = collection;
            this.filter = subfilter;
        }
        else {
            throw new IllegalArgumentException("collection supports java.util.Collection or AbstractFeatureCollection");
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<SimpleFeature> openIterator() {
        if (collection instanceof Collection) {
            Iterator<SimpleFeature> it = ((Collection<SimpleFeature>) collection).iterator();
            return new FilteredIterator<SimpleFeature>(it, filter());
        } else if (collection instanceof AbstractFeatureCollection) {
            Iterator<SimpleFeature> iterator = ((AbstractFeatureCollection) collection).iterator();
            return new FilteredIterator<SimpleFeature>(iterator, filter());
        } else if (collection instanceof AbstractFeatureCollection) {
            Iterator<SimpleFeature> iterator = ((AdaptorFeatureCollection) collection).iterator();
            return new FilteredIterator<SimpleFeature>(iterator, filter());
        } else {
            // JG because we have an implementation of features we should
            // choose a different base class that does not require an Iterator
            throw new UnsupportedOperationException();
        }
    }
    		
	public int size() {
		int count = 0;
		Iterator i = null;		
		try {
			for( i = iterator(); i.hasNext(); count++) i.next();
		}
		finally {
			if( i instanceof FeatureIterator){
				((FeatureIterator<?>)i).close();
			}
		}
		return count;
	}
    
	protected Filter filter(){
	    if( filter == null ){
            filter = createFilter();
        }
        return filter;
    }
	
    /** Override to implement subsetting */
    protected Filter createFilter(){
        return Filter.INCLUDE;
    }
    
	public SimpleFeatureIterator features() {
		return new DelegateSimpleFeatureIterator( this, iterator() );		
	}
	
	
	public void close(SimpleFeatureIterator close) {
		if( close != null ) close.close();
	}

    //
    //
    //
	public SimpleFeatureCollection subCollection(Filter filter) {
		if (filter.equals(Filter.INCLUDE)) {
			return this;
		}
		if (filter.equals(Filter.EXCLUDE)) {
			// TODO implement EmptyFeatureCollection( schema )
		}
		return new SubFeatureCollection(this, filter);
	}

	
	public boolean isEmpty() {
		Iterator iterator = iterator();
		try {
			return !iterator.hasNext();
		}
		finally {
			if( iterator instanceof FeatureIterator){
				((FeatureIterator<?>)iterator).close();
			}
		}
	}
	
	public void accepts(org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress) {
		Iterator iterator = null;
        // if( progress == null ) progress = new NullProgressListener();
        try{
            float size = size();
            float position = 0;            
            progress.started();
            for( iterator = iterator(); !progress.isCanceled() && iterator.hasNext(); progress.progress( position++/size )){
                try {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    visitor.visit(feature);
                }
                catch( Exception erp ){
                    progress.exceptionOccurred( erp );
                }
            }            
        }
        finally {
            progress.complete();            
			if( iterator instanceof FeatureIterator){
				((FeatureIterator<?>)iterator).close();
			}
        }
	}	

	public SimpleFeatureCollection sort(SortBy order) {
		return new SubFeatureList( collection, filter, order );
	}

    public String getID() {
    	return collection.getID();
    }

    // extra methods
    public FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>( getSchema(), features() );
    }

    public int getCount() throws IOException {
        return size();
    }

    public SimpleFeatureCollection collection() throws IOException {
        return this;
    }

	/**
	 * Calculates the bounds of the features without caching.
	 *  
	 * TODO Have some pro look at this code.
	 * author by Stefan Krueger 
	 */
	@Override
	public ReferencedEnvelope getBounds() {
	    BoundsVisitor bounds = new BoundsVisitor();
	    accepts( bounds, new NullProgressListener() );            
            return bounds.getBounds();
	}

}
