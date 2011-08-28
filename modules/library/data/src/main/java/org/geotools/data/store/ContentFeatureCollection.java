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
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.filter.SortBy;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;

import com.vividsolutions.jts.geom.Point;

/**
 * A FeatureCollection that completely delegates to a backing FetaureSource#getReader
 * 
 * @author Jody Garnett (Refractions Research, Inc.)
 *
 *
 * @source $URL$
 */
public class ContentFeatureCollection implements SimpleFeatureCollection {
    
    /**
     * feature store the collection originated from.
     */
    protected ContentFeatureSource featureSource;
    protected Query query;
    
    /**
     * feature (possibly retyped from feautre source original) type
     */
    protected SimpleFeatureType featureType;
    /**
     * state of the feature source 
     */
    protected ContentState state;
    
    /** Internal listener storage list */
    protected List<CollectionListener> listeners = new ArrayList<CollectionListener>(2);

    /** Set of open resource iterators */
    protected final Set open = new HashSet();
    
    /**
     * feature listener which listens to the feautre source and 
     * forwards events to its listeners.
     */
    FeatureListener listener = new FeatureListener(){
        public void changed(FeatureEvent featureEvent) {
            if( listeners.isEmpty() ) return;

            SimpleFeatureCollection collection;
            collection = (SimpleFeatureCollection) ContentFeatureCollection.this;
            CollectionEvent event = new CollectionEvent( collection, featureEvent );

            CollectionListener[] notify = (CollectionListener[]) listeners.toArray( new CollectionListener[ listeners.size() ]);
            for( int i=0; i<notify.length; i++ ){
                CollectionListener listener = notify[i];
                try {
                    listener.collectionChanged( event );
                }
                catch (Throwable t ){
                    //TODO: log this
                    //ContentDataStore.LOGGER.log( Level.WARNING, "Problem encountered during notification of "+event, t );
                }
            }
        }           
    };
    
    protected ContentFeatureCollection( ContentFeatureSource featureSource, Query query ) {
        this.featureSource = featureSource;
        this.query = query;
        
        //retype feature type if necessary
        if ( query.getPropertyNames() != Query.ALL_NAMES ) {
            this.featureType = 
                SimpleFeatureTypeBuilder.retype(featureSource.getSchema(), query.getPropertyNames() );
        }
        else {
            this.featureType = featureSource.getSchema();
        }
    }
    
    public SimpleFeatureType getSchema() {
        return featureType;
    }
    
    //Visitors
    public void accepts(org.opengis.feature.FeatureVisitor visitor,
            org.opengis.util.ProgressListener progress) throws IOException {
        featureSource.accepts( query, visitor, progress);
    }
    
    
    //Listeners
    /**
     * Adds a listener for collection events.
     *
     * @param listener The listener to add
     */
    public void addListener(CollectionListener listener) {
        // create the bridge only if we have collection listeners around
        synchronized (listeners) {
            if(listeners.size() == 0) {
                featureSource.addFeatureListener(this.listener);
            }
            
            listeners.add(listener);
        }
    }

    /**
     * Removes a listener for collection events.
     *
     * @param listener The listener to remove
     */
    public void removeListener(CollectionListener listener) {
        // as soon as the listeners are out we clean up
        synchronized (listeners) {
            listeners.remove(listener);
        
            if(listeners.size() == 0)
                featureSource.removeFeatureListener(this.listener);
        }
    }
    
    // Iterators
    public static class WrappingFeatureIterator implements SimpleFeatureIterator {
       
        FeatureReader<SimpleFeatureType, SimpleFeature> delegate;
        
        public WrappingFeatureIterator(  FeatureReader<SimpleFeatureType, SimpleFeature> delegate ) {
            this.delegate = delegate;
        }
        
        public boolean hasNext() {
            try {
                return delegate.hasNext();    
            }
            catch( IOException e ) {
                throw new RuntimeException( e );
            }
            
        }

        public SimpleFeature next() throws java.util.NoSuchElementException {
            try {
                return delegate.next();    
            }
            catch( IOException e ) {
                throw new RuntimeException( e );
            }
        }

        public void close() {
            try {
                delegate.close();    
            }
            catch( IOException e ) {
                throw new RuntimeException( e );
            }
            
        }
    }
    
    public SimpleFeatureIterator features(){
        try {
            return new WrappingFeatureIterator( featureSource.getReader(query) );    
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public void close( FeatureIterator<SimpleFeature> iterator ) {
        iterator.close();
    }
    
    public static class WrappingIterator implements Iterator {
    
         FeatureReader<SimpleFeatureType, SimpleFeature> delegate;
        
        public  WrappingIterator(  FeatureReader<SimpleFeatureType, SimpleFeature> delegate ) {
            this.delegate = delegate;
        }
        
        public boolean hasNext() {
            try {
                return delegate.hasNext();    
            }
            catch( IOException e ) {
                throw new RuntimeException( e );
            }
            
        }

        public Object next() {
            try {
                return delegate.next();    
            }
            catch( IOException e ) {
                throw new RuntimeException( e );
            }
        }

    
       public void remove() {
           throw new UnsupportedOperationException();
       }
    }
    
    public Iterator iterator() {
        try {
            return new WrappingIterator( featureSource.getReader(query) );    
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public void close(Iterator close) {
        try {
            ((WrappingIterator)close).delegate.close();    
        }
        catch( IOException e ) {
            throw new RuntimeException( e );
        }
    }
    
    public ReferencedEnvelope getBounds() {
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
        try {
             ReferencedEnvelope result = featureSource.getBounds(query);
             if(result != null) {
                 return result;
             }
             
             // ops, we have to compute the results by hand. Let's load just the
             // geometry attributes though
             Query q = new Query(query);
             List<String> geometries = new ArrayList<String>();
             for (AttributeDescriptor ad : getSchema().getAttributeDescriptors()) {
                if(ad instanceof GeometryDescriptor) {
                    geometries.add(ad.getLocalName());
                }
            }
            // no geometries, no bounds
            if(geometries.size() == 0) {
                return new ReferencedEnvelope();
            } else {
                q.setPropertyNames(geometries);
            }
            // grab the features and scan through them
            reader = featureSource.getReader(q);
            while(reader.hasNext()) {
                SimpleFeature f = reader.next();
                ReferencedEnvelope featureBounds = ReferencedEnvelope.reference(f.getBounds());
                if(result == null) {
                    result = featureBounds;
                } else if(featureBounds != null){
                    result.expandToInclude(featureBounds);
                }
            }
            // return the results if we got any, or return an empty one otherwise
            if(result != null) {
                return result;
            } else {
                return new ReferencedEnvelope(getSchema().getCoordinateReferenceSystem());
            }
        } catch( IOException e ) {
            throw new RuntimeException( e );
        } finally {
            if(reader != null) {
                try {
                    reader.close();
                } catch (IOException ex) {
                    // we tried...
                }
            }
        }
    }
    
    public int size() {
        FeatureReader fr = null;
        try {
           int size = featureSource.getCount(query);
           if(size >= 0) {
               return size;
           } else {
               // we have to iterate, probably best if we do a minimal query that
               // only loads a short attribute
               AttributeDescriptor chosen = null;
               for (AttributeDescriptor ad : getSchema().getAttributeDescriptors()) {
                   if(chosen == null || size(ad) < size(chosen)) {
                       chosen = ad;
                   } 
               }
               // build the minimal query
               Query q = new Query(query);
               if(chosen != null) {
                   q.setPropertyNames(Collections.singletonList(chosen.getLocalName()));
               }
               // bean counting...
               fr = featureSource.getReader(q);
               int count = 0;
               while(fr.hasNext()) {
                   fr.next();
                   count++;
               }
               return count;
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            if(fr != null) {
                try {
                    fr.close();
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }
    
    /**
     * Quick heuristic used to find a small attribute to use when 
     * scanning over the entire collection just to get the size of the 
     * collection. 
     * The result is indicative, just an heuristic to quickly compare attributes
     * in order to find a suitably small one,
     * it's not meant to be the actual size of an attribute in bytes.
     * @param ad
     * @return
     */
    int size(AttributeDescriptor ad) {
         Class<?> binding = ad.getType().getBinding();
         if(binding.isPrimitive() || Number.class.isAssignableFrom(binding) || 
                 Date.class.isAssignableFrom(binding)) {
             return 4;
         } else if(binding.equals(String.class)) {
             int fieldLen = FeatureTypes.getFieldLength(ad);
             if(fieldLen > 0) {
                 return fieldLen * 2;
             } else {
                 return Integer.MAX_VALUE;
             }
         } else if(Point.class.isAssignableFrom(binding)) {
             return 4 * 3;
         } else {
             return Integer.MAX_VALUE;
         }
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public boolean add(SimpleFeature o) {
        return addAll(Collections.singletonList(o));
    }

    ContentFeatureStore ensureFeatureStore() {
        if ( featureSource instanceof ContentFeatureStore ) {
            return (ContentFeatureStore) featureSource;
        }
        
        throw new UnsupportedOperationException( "read only" );
    }
    public boolean addAll(Collection c) {
        ContentFeatureStore featureStore = ensureFeatureStore();
        
        try {
            List<FeatureId> ids = featureStore.addFeatures(c);
            return ids.size() == c.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public boolean addAll(FeatureCollection c) {
        ContentFeatureStore featureStore = ensureFeatureStore();
        try {
            List<FeatureId> ids;
            ids = featureStore.addFeatures( c );
            return ids.size() == c.size();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    public void clear() {
        ContentFeatureStore featureStore = ensureFeatureStore();
        
        try { 
            featureStore.removeFeatures(query.getFilter());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public void purge() {
        //do nothing
    }
    
    public SimpleFeatureCollection sort(SortBy order) {
        return sort( (org.opengis.filter.sort.SortBy) order );
    }
    
    public SimpleFeatureCollection sort(org.opengis.filter.sort.SortBy sort) {
        Query query = new DefaultQuery();
        ((DefaultQuery)query).setSortBy( new org.opengis.filter.sort.SortBy[]{sort});

        query = DataUtilities.mixQueries( this.query, query, null );
        return new ContentFeatureCollection( featureSource, query );    
    }
    
    public SimpleFeatureCollection subCollection(Filter filter) {
        Query query = new DefaultQuery();
        ((DefaultQuery)query).setFilter( filter );
        
        query = DataUtilities.mixQueries(this.query, query, null);
        return new ContentFeatureCollection( featureSource, query );    
    }
    
    //
    // The following were slated to be unsupported by a proposal from
    // jdeolive; since it has not been accepted the following
    // implementations are provided but not recommended
    //
    // There are completely implemented in terms of reader.
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
        // TODO: base this on reader
        SimpleFeatureIterator e = null;
        try {
            e = this.features();
            if (o==null) {
                while (e.hasNext()){
                    if (e.next()==null){
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
        }
        finally {
            if( e != null ){
                e.close();
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
        // TODO: base this on reader
        Iterator<?> e = c.iterator();
        try {
            while (e.hasNext()){
                if(!contains(e.next())){
                    return false;
                }
            }
            return true;
        } finally {
            close( e );
        }
    }

    public boolean remove(Object o) {
//        if( featureSource instanceof SimpleFeatureStore){
//            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
//            if( o instanceof SimpleFeature ){
//                SimpleFeature feature = (SimpleFeature) o;
//                FeatureId fid = feature.getIdentifier();
//                FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
//                Set<FeatureId> ids = Collections.singleton(fid);
//                Filter remove = ff.id(ids);
//                try {
//                    featureStore.removeFeatures( remove );
//                    return true;
//                } catch (IOException e) {
//                    //LOGGER.log(Level.FINER, e.getMessage(), e);
//                    return false; // unable to remove
//                }
//            }
//            else {
//                return false; // nothing to do; we can only remove features
//            }
//        }
        throw new UnsupportedOperationException("Content is not writable; FeatureStore not available" );
    }

    public boolean removeAll(Collection collection) {
//        if( featureSource instanceof SimpleFeatureStore){
//            SimpleFeatureStore featureStore = (SimpleFeatureStore) featureSource;
//            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
//            Set<FeatureId> ids = new HashSet<FeatureId>();
//            for( Object o : collection ){
//                if( o instanceof SimpleFeature){
//                    SimpleFeature feature = (SimpleFeature) o;
//                    FeatureId fid = feature.getIdentifier();
//                    ids.add( fid );
//                }
//            }
//            Filter remove = ff.id(ids);
//            try {
//                featureStore.removeFeatures( remove );
//                return true;
//            } catch (IOException e) {
//                //LOGGER.log(Level.FINER, e.getMessage(), e);
//                return false; // unable to remove
//            }
//        }
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(Collection collection) {
        throw new UnsupportedOperationException();
    }
    /**
     * Array of all the elements.
     * 
     * @return an array containing all of the elements in this collection.
     */
    public Object[] toArray() {
        // code based on AbstractFeatureCollection
        // TODO: base this on reader
        ArrayList<SimpleFeature> array = new ArrayList<SimpleFeature>();
        Iterator<SimpleFeature> e = null;
        try {
            e = iterator();
            while( e.hasNext() ){
                array.add( e.next() );
            }
            return array.toArray( new SimpleFeature[array.size()]);
        } finally {
            close( e );
        }
    }

    public <T> T[] toArray(T[] array) {
        int size = size();
        if (array.length < size){
            array = (T[])java.lang.reflect.Array.newInstance(array.getClass().getComponentType(), size);
         }
        Iterator<SimpleFeature> it = iterator();
        try {
            Object[] result = array;
            for (int i=0; it.hasNext() && i<size; i++){
                result[i] = it.next();
            }
            if (array.length > size){
                array[size] = null;
            }
            return array;
        }
        finally {
            close( it );
        }
    }
    public String getID() {
        throw new UnsupportedOperationException();
    }

}
