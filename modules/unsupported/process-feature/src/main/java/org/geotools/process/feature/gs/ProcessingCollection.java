/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2011, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2001-2007 TOPP - www.openplans.org.
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
package org.geotools.process.feature.gs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import org.geotools.data.DataUtilities;
import org.geotools.data.store.FilteringFeatureCollection;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.SortedSimpleFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.FeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * Abstract base class to ease the implementation of a streaming processing collection, that is, one
 * that tries to compute the results on the fly as the iterator is traversed.
 * 
 * Besides the few methods that the implementor actually needs to override it suggested to consider
 * overriding also the followings to get extra performance gains:
 * <ul>
 * {@link #subCollection(Filter)} {@link #sort(SortBy)}
 * </ul>
 * 
 * @author Andrea Aime - GeoSolutions
 * 
 * @param <T>
 * @param <F>
 */
public abstract class ProcessingCollection<T extends FeatureType, F extends Feature>
        implements FeatureCollection<T, F> {

    private T schema;

    private String id;

    public ProcessingCollection() {
        id = getClass().getSimpleName() + "-" + UUID.randomUUID().toString();
    }

    /**
     * Streams out the output features
     */
    @Override
    public abstract FeatureIterator<F> features();

    @Override
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        FeatureIterator<F> iterator = null;
        float size = progress != null ? size() : 0;
        if (progress == null) {
            progress = new NullProgressListener();
        }
        try {
            float position = 0;
            progress.started();
            iterator = features();
            while (iterator.hasNext()) {
                try {
                    F feature = iterator.next();
                    visitor.visit(feature);
                    if (size > 0) {
                        progress.progress(position++ / size);
                    }
                } catch (Exception erp) {
                    progress.exceptionOccurred(erp);
                    throw new IOException("Error occurred while iterating over features", erp);
                }
            }
        } finally {
            progress.complete();
            if( iterator != null ){
            	iterator.close();
            }
        }

    }

    /**
     * Convenience implementation that just wraps this collection into a
     * {@link FilteringFeatureCollection}. Subclasses might want to override this in case the filter
     * can be cascaded to their data sources.
     * 
     * @param filter
     * @return
     */
    @Override
    public FeatureCollection<T, F> subCollection(Filter filter) {
        return new FilteringFeatureCollection<T, F>(this, filter);
    }

    /**
     * Convenience implementation that
     */
    @SuppressWarnings("unchecked")
	@Override
    public FeatureCollection<T, F> sort(SortBy order) {
        if (schema instanceof SimpleFeatureType) {
            // go for the most efficient way if possible, otherwise rely on pure in memory
            // sorting...
            return (FeatureCollection<T, F>) new SortedSimpleFeatureCollection(
                    DataUtilities
                            .simple((FeatureCollection<SimpleFeatureType, SimpleFeature>) this),
                    new SortBy[] { order });
        } else {
            // hmm... we don't even have a basic non simple collection... need to implement one
            // before
            // going here
            throw new UnsupportedOperationException("Cannot sort on complex features at the moment");
        }
    }

    /**
     * The bounds of features in the output. If the bounds are not known in advance once can call the
     * getFeatureBounds() which will build it from the features as they are returned from the feature
     * iterator.
     */
    @Override
    public abstract ReferencedEnvelope getBounds();

    /**
     * Builds once and for all the target feature type. The results are available by calling getSchema()
     * 
     * @return
     */
    protected abstract T buildTargetFeatureType();

    /**
     * The number of features in the output. If the size is not known in advance once can call the
     * getFeatureCount() which will count the features as they are returned from the feature
     * iterator.
     */
    @Override
    public abstract int size();

    /**
     * Convenience method that counts features by traversing the feature iterator.
     * 
     * @return
     */
    protected int getFeatureCount() {
        FeatureIterator<F> fi = null;
        try {
            fi = features();
            int count = 0;
            while (fi.hasNext()) {
                fi.next();
                count++;
            }
            return count;
        } finally {
        	if( fi != null ){
        		fi.close();
        	}
        }
    }
    
    /**
     * Convenience method that computes the feature bounds by traversing the feature iterator.
     * 
     * @return
     */
    protected ReferencedEnvelope getFeatureBounds() {
        FeatureIterator<F> fi = null;
        try {
            fi = features();
            ReferencedEnvelope bounds = null;
            while (fi.hasNext()) {
                F feature = fi.next();
                ReferencedEnvelope featureEnvelope = null;
                if(feature != null && feature.getBounds() != null) {
                    featureEnvelope = ReferencedEnvelope.reference(feature.getBounds());
                }
                
                if(featureEnvelope != null) {
                    if(bounds == null) {
                        bounds = new ReferencedEnvelope(featureEnvelope);
                    } else {
                        bounds.expandToInclude(featureEnvelope);
                    }
                }
            }
            
            return bounds;
        } finally {
            if( fi != null ){
            	fi.close();
            }
        }
    }

    @Override
    public T getSchema() {
        if(schema == null) {
            schema = buildTargetFeatureType();
        }
        
        return schema;
    }

    @Override
    public String getID() {
        return id;
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element. <tt></tt>.
     * <p>
     * 
     * This implementation iterates over the elements in the collection, checking each element in
     * turn for equality with the specified element.
     * 
     * @param o object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contains(Object o) {
        FeatureIterator<F> e = null;
        try {
            e = features();
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
            if( e != null ){
            	e.close();
            }
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements in the specified
     * collection.
     * <p>
     * 
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements in the specified
     *         collection.
     * @throws NullPointerException if the specified collection is null.
     * 
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        Iterator<?> e = c.iterator();
        while (e.hasNext()){
                if (!contains(e.next())){
                    return false;
                }
        }
        return true;        
    }

    @Override
    public boolean isEmpty() {
        FeatureIterator<F> fi = null;
        try {
            fi = features();
            return !fi.hasNext();
        } finally {
        	if( fi != null ){
        		fi.close();
        	}
        }
    }

    /**
     * Utility to get all the features to implement the toArray methods
     */
    protected List<F> toList() {
        ArrayList<F> result = new ArrayList<F>();
        FeatureIterator<F> fi = null;
        try {
            fi = features();
            while (fi.hasNext()) {
                result.add(fi.next());
            }
            return result;
        } finally {
        	if( fi != null ){
        		fi.close();
        	}
        }
    }

    public Object[] toArray() {
        return toList().toArray();
    }

    public <O> O[] toArray(O[] a) {
        return toList().toArray(a);
    }

}
