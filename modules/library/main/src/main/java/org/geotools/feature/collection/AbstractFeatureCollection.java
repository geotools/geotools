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

import java.util.Collection;
import java.util.Iterator;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;

/**
 * Implement a feature collection just based on provision of iterator.
 * 
 * @author Jody Garnett (Refractions Research Inc)
 *
 * @source $URL$
 */
public abstract class AbstractFeatureCollection implements SimpleFeatureCollection {
    /** 
     * id used when serialized to gml
     */
    protected String id;
    protected SimpleFeatureType schema;

    protected AbstractFeatureCollection(SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }

    //
    // SimpleFeatureCollection - Feature Access
    // 
	public SimpleFeatureIterator features() {
        Iterator<SimpleFeature> iterator = openIterator();
        if( iterator instanceof SimpleFeatureIterator ){
        	return (SimpleFeatureIterator) iterator;
        }
        else {
        	SimpleFeatureIterator iter = new DelegateSimpleFeatureIterator( this, iterator );
        	return iter;
        }
    }
    
    /**
     * Factory method used to open an iterator over collection contents
     * for use by {@link #iterator()} and {@link #features()}.
     * <p>
     * If you return an instance of FeatureIterator some effort
     * is taken to call the {@link FeatureIterator#close()} internally, however
     * we cannot offer any assurance that client code using {@link #iterator()}
     * will perform the same check.
     * 
     * @return Iterator over collection contents
     */
    abstract protected Iterator<SimpleFeature> openIterator();
    
    /**
     * Returns the number of elements in this collection.
     * 
     * @return Number of items, or Interger.MAX_VALUE
     */
    public abstract int size();
    
    /**
     * Returns <tt>true</tt> if this collection contains the specified
     * element.
     * <tt></tt>.<p>
     *
     * This implementation iterates over the elements in the collection,
     * checking each element in turn for equality with the specified element.
     *
     * @param o object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contains(Object o) {
        Iterator<SimpleFeature> e = null;
        e = iterator();
        try {
			if (o == null) {
				while (e.hasNext())
					if (e.next() == null)
						return true;
			} else {
				while (e.hasNext())
					if (o.equals(e.next()))
						return true;
			}
			return false;
		} finally {
			if( e instanceof FeatureIterator ){
				((FeatureIterator<?>)e).close();				
			}
		}
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements
     * in the specified collection. <p>
     * 
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements
     *         in the specified collection.
     * @throws NullPointerException if the specified collection is null.
     * 
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        while (e.hasNext()){
            if(!contains(e.next())){
               return false;
            }
        }
        return true;
    }
    //
    // Contents
    //
    //    
    /**
     * Provides acess to {@link #openIterator()} used to traverse collection contents.
     * <p>
     * You are asked to perform the following check when finished with the iterator:<pre> if( e instanceof FeatureIterator ){
	 *    (FeatureIterator<?>)iterator).close();				
	 * }</pre>
     * @return Iterator traversing collection contents
     */
	final public Iterator<SimpleFeature> iterator(){
    	Iterator<SimpleFeature> iterator = openIterator();
        return iterator;
    }
    
    /**
     * @return <tt>true</tt> if this collection contains no elements.
     */
    public boolean isEmpty() {
    	Iterator<SimpleFeature> iterator = iterator();
    	try {
	        return !iterator.hasNext();
    	}
    	finally {
    		if( iterator instanceof FeatureIterator){
    			((FeatureIterator<?>)iterator).close();
    		}
    	}
    }

    /**
     * Array of all the elements.
     * 
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        Object[] result = new Object[size()];
        Iterator<SimpleFeature> e = null;
        try {
	        e = iterator();
	        for (int i=0; e.hasNext(); i++)
	            result[i] = e.next();
	        return result;
        }
        finally {
        	if( e instanceof FeatureIterator){
        		((FeatureIterator<?>)e).close();
        	}
        }
    }

    @SuppressWarnings("unchecked")
	public <O> O[] toArray(O[] a) {
        int size = size();
        if (a.length < size){
            a = (O[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
         }
        Iterator<SimpleFeature> it = iterator();
        try {
	        Object[] result = a;
	        for (int i=0; i<size; i++)
	            result[i] = it.next();
	        if (a.length > size)
	        a[size] = null;
	        return a;
        }
        finally {
        	if( it instanceof FeatureIterator){
        		((FeatureIterator<?>)it).close();
        	}
        }
    }

	public void accepts(org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress) {
    	Iterator<SimpleFeature> iterator = null;
    	if( progress == null ) progress = new NullProgressListener();
        try{
            float size = size();
            float position = 0;            
            progress.started();
            for( iterator = iterator(); !progress.isCanceled() && iterator.hasNext();){
                if (size > 0) progress.progress( position++/size );
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
    
    //
    // Feature Collections API
    //    
    public SimpleFeatureCollection subCollection( Filter filter ) {
        if( filter == Filter.INCLUDE ){
            return this;
        }        
        return new SubFeatureCollection( this, filter );
    }

    public SimpleFeatureCollection sort( SortBy order ) {
        return new SubFeatureList(this, order );
    }

    public String getID() {
    	return id;
    }

    public SimpleFeatureType getSchema() {
    	return schema;
    }

    /**
     * Subclasses need to override this.
     */
    public abstract ReferencedEnvelope getBounds();
    
}
