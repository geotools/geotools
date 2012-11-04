/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2005-2012, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.EmptyFeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;

/**
 * Implement a feature collection just based on provision of a {@link FeatureIterator}.
 * <p>
 * This implementation asks you to implement:
 * <ul>
 * <li>{@link #features()}</li>
 * <li>
 * This is the direct decentent of the origional {@link AbstractFeatureCollection}
 * and represents the easiest way to package your content as a SimpleFeatureCollection
 * (with no optimisation).
 * <p>
 * As this class provides no optimization, it is strongly recommended that you implement
 * the following methods (which require a whole collection traversal):
 * <ul>
 * <li>{@link #size()}</li>
 * <li>{@link #getBounds()</li>
 * </ul>
 * 
 * @author Jody Garnett (LISAsoft)
 * @source $URL$
 */
public abstract class BaseFeatureCollection implements SimpleFeatureCollection {
    /**
     * id used when serialized to gml
     */
    protected String id;
    protected SimpleFeatureType schema;

    protected BaseFeatureCollection(SimpleFeatureType schema) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = schema;
    }

    public String getID() {
        return id;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    //
    // SimpleFeatureCollection - Feature Access
    //
    /**
     * Subclasses required to implement this method to traverse FeatureCollection contents.
     * <p>
     * Note that {@link SimpleFeatureIterator#close()} is available to clean up
     * after any resource use required during traversal.
     */
    public abstract SimpleFeatureIterator features();

    /**
     * Returns <tt>true</tt> if this collection contains the specified element. <tt></tt>.
     * <p>
     * 
     * This implementation iterates over the elements in the collection, checking each element in turn for equality with the specified element.
     * 
     * @param o object to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains the specified element.
     */
    public boolean contains(Object o) {
        SimpleFeatureIterator e = features();
        try {
            if (o == null) {
                while (e.hasNext()){
                    if (e.next() == null){
                        return true;
                    }
                }
            } else {
                while (e.hasNext()){
                    if (o.equals(e.next())){
                        return true;
                    }
                }
            }
            return false;
        } finally {
            e.close();
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains all of the elements in the specified collection.
     * <p>
     * 
     * @param c collection to be checked for containment in this collection.
     * @return <tt>true</tt> if this collection contains all of the elements in the specified collection.
     * @throws NullPointerException if the specified collection is null.
     * 
     * @see #contains(Object)
     */
    public boolean containsAll(Collection<?> c) {
        SimpleFeatureIterator e = features();
        try {
            while (e.hasNext()){
                SimpleFeature feature = e.next();
                if (!c.contains(feature)){
                    return false;
                }
            }
            return true;
        } finally {
            e.close();
        }
    }

    /**
     * @return <tt>true</tt> if this collection contains no elements.
     */
    public boolean isEmpty() {
        SimpleFeatureIterator iterator = features();
        try {
            return !iterator.hasNext();
        } finally {
            iterator.close();
        }
    }

    /**
     * Array of all the elements.
     * 
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        Object[] result = new Object[size()];
        SimpleFeatureIterator e = null;
        try {
            e = features();
            for (int i = 0; e.hasNext(); i++)
                result[i] = e.next();
            return result;
        } finally {
            e.close();
        }
    }

    @SuppressWarnings("unchecked")
    public <O> O[] toArray(O[] a) {
        int size = size();
        if (a.length < size) {
            a = (O[]) java.lang.reflect.Array.newInstance(a.getClass().getComponentType(), size);
        }
        SimpleFeatureIterator it = features();
        try {
            Object[] result = a;
            for (int i = 0; i < size; i++)
                result[i] = it.next();
            if (a.length > size)
                a[size] = null;
            return a;
        } finally {
            it.close();
        }
    }

    public void accepts(org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) {
        SimpleFeatureIterator iterator = null;
        if (progress == null)
            progress = new NullProgressListener();
        try {
            float size = size();
            float position = 0;
            progress.started();
            for (iterator = features(); !progress.isCanceled() && iterator.hasNext();) {
                try {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    visitor.visit(feature);
                } catch (Exception erp) {
                    progress.exceptionOccurred(erp);
                }
                if (size > 0){
                    progress.progress(position++ / size);
                }
            }
        } finally {
            progress.complete();
            if( iterator != null ){
                iterator.close();
            }
        }
    }

    //
    // Feature Collections API
    //
    public SimpleFeatureCollection subCollection(Filter filter) {
        if (filter == Filter.INCLUDE) {
            return this;
        }
        if( filter == Filter.EXCLUDE) {
            return new EmptyFeatureCollection(schema);
        }
        return new SubFeatureCollection(this, filter);
    }

    public SimpleFeatureCollection sort(SortBy order) {
        return new SubFeatureList(this, order);
    }

    /**
     * Returns the number of elements in this collection.
     * 
     * @return Number of items, or Interger.MAX_VALUE
     */
    public int size(){
        int count = 0;
        SimpleFeatureIterator it = features();
        try {
            while( it.hasNext() ){
                @SuppressWarnings("unused")
                SimpleFeature feature = it.next();
                count++;
            }
        } finally {
            it.close();
        }     
        return count;
    }

    /**
     * Full collection traversal to obtain bounds of FeatureCollection.
     * Subclasees are strong encouraged to override this expensive method
     * (even if just to implement caching).
     */
    public ReferencedEnvelope getBounds(){
        ReferencedEnvelope bounds = null;
        SimpleFeatureIterator it = features();
        try {
            while( it.hasNext() ){
                SimpleFeature feature = it.next();
                BoundingBox bbox = feature.getBounds();
                if( bbox != null ){
                    if( bounds == null ){
                        bounds = new ReferencedEnvelope( bbox );
                    }
                    else {
                        bounds.include( bbox );
                    }
                }
            }
        } finally {
            it.close();
        }     
        return bounds;
    }

}
