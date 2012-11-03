/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.versioning.decorator;

import java.io.Closeable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataSourceException;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.collection.ListFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.SimpleFeatureIteratorImpl;
import org.geotools.feature.collection.SubFeatureCollection;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.SortBy2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;

/**
 * SimpleFeatureCollection accessing revision information.
 */
public class DefaultVersionedFeatureCollection implements SimpleFeatureCollection {

    public DefaultVersionedFeatureCollection(
            FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        this( collection.getID(), collection.getSchema() );
        init(collection);
    }

    public DefaultVersionedFeatureCollection(String id,SimpleFeatureType memberType) {
        this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;       
    }

    private static Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.feature");

    /**
     * Contents of collection, referenced by FeatureID.
     * <p>
     * This use will result in collections that are sorted by FID, in keeping
     * with shapefile etc...
     * </p>
     */
    private SortedMap<String,SimpleFeature> contents = new TreeMap<String,SimpleFeature>();

    /** Internal envelope of bounds. */
    private ReferencedEnvelope bounds = null;
    /** 
     * id used when serialized to gml
     */
    private String id;

    private SimpleFeatureType schema;

    /**
     * Used by FeatureSourceDecorator to stage versioned content.
     * @param feature
     * @return
     */
    boolean add(SimpleFeature feature) {
        if (feature == null) {
            return false; // cannot add null!
        }
        final String ID = getKey(feature.getIdentifier());
        if (ID == null) {
            return false; // ID is required!
        }
        if (contents.containsKey(ID)) {
            return false; // feature all ready present
        }    
        if (this.schema == null) {
            this.schema = feature.getFeatureType();
        }
        SimpleFeatureType childType = (SimpleFeatureType) getSchema();
        if (!feature.getFeatureType().equals(childType)) {
            LOGGER.warning("Feature Collection contains a heterogeneous"
                    + " mix of features");
        }
        contents.put(ID, feature);
        return true;
    }

    /**
     * Adds all of the elements in the specified collection to this collection
     * (optional operation).  The behavior of this operation is undefined if
     * the specified collection is modified while the operation is in
     * progress. (This implies that the behavior of this call is undefined if
     * the specified collection is this collection, and this collection is
     * nonempty.)
     *
     * @param collection elements to be inserted into this collection.
     *
     * @return <tt>true</tt> if this collection changed as a result of the call
     */
    public void init(FeatureCollection<SimpleFeatureType, SimpleFeature> collection) {
        FeatureIterator<SimpleFeature> iterator = collection.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                if (feature == null) {
                    continue; // cannot add null!
                }
                final String ID = getKey(feature.getIdentifier());
                if (ID == null) {
                    continue; // ID is required!
                }
                if (contents.containsKey(ID)) {
                    continue; // feature all ready present
                }    
                if (this.schema == null) {
                    this.schema = feature.getFeatureType();
                }
                SimpleFeatureType childType = (SimpleFeatureType) getSchema();
                if (!feature.getFeatureType().equals(childType)) {
                    LOGGER.warning("Feature Collection contains a heterogeneous"
                            + " mix of features");
                }
                contents.put(ID, feature);
            }
        }
        finally {
            iterator.close();
        }
    }

    private String getKey(FeatureId id) {
        if (id.getFeatureVersion() == null) {
            return id.getID();
        } else {
            return id.getID() + FeatureId.VERSION_SEPARATOR
                    + id.getFeatureVersion();
        }
    }

    /**
     * Returns <tt>true</tt> if this collection contains the specified element.
     * More formally, returns <tt>true</tt> if and only if this collection
     * contains at least one element <tt>e</tt> such that <tt>(o==null ?
     * e==null : o.equals(e))</tt>.
     * 
     * @param o element whose presence in this collection is to be tested.
     * @return <tt>true</tt> if this collection contains the specified element
     */
    public boolean contains(Object o) {
        // The contract of Set doesn't say we have to cast here, but I think its
        // useful for client sanity to get a ClassCastException and not just a
        // false.
        if (!(o instanceof SimpleFeature)) {
            return false;
        }
    
        SimpleFeature feature = (SimpleFeature) o;
        String ID = getKey(feature.getIdentifier());
    
        return contents.containsKey(ID); // || contents.containsValue( feature
        // );
    }

    @Override
    public boolean containsAll(Collection<?> collection) {
        Iterator<?> iterator = collection.iterator();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                String ID = getKey(feature.getIdentifier());
                if (!contents.containsKey(ID)) {
                    return false;
                }
            }
            return true;
        } finally {
            DataUtilities.close( iterator );
        }
    }

    /**
     * Will return an optimized subCollection based on access
     * to the origional MemoryFeatureCollection.
     * <p>
     * This method is intended in a manner similar to subList,
     * example use:
     * <code>
     * collection.subCollection( myFilter ).clear()
     * </code>
     * </p>    
     * @param filter Filter used to determine sub collection.
     * @since GeoTools 2.2, Filter 1.1
     */
    public SimpleFeatureCollection subCollection(Filter filter) {
        if( filter == Filter.INCLUDE ){
            return this;
        }               
        return new SubFeatureCollection( this, filter );
    }

    /**
     * Gets a SimpleFeatureIterator of this feature collection.  This allows
     * iteration without having to cast.
     *
     * @return the SimpleFeatureIterator for this collection.
     */
    public SimpleFeatureIterator features() {
        return new SimpleFeatureIteratorImpl( contents.values() );
    }

    public  FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
        final SimpleFeatureIterator iterator = features(); 
        return new FeatureReader<SimpleFeatureType, SimpleFeature>(){
            public SimpleFeatureType getFeatureType() {
                return getSchema();
            }
            public SimpleFeature next() throws NoSuchElementException {
                return iterator.next();
            }

            public boolean hasNext() throws IOException {
                return iterator.hasNext();
            }

            public void close() throws IOException {
                iterator.close();
            }            
        };
    }

    public int getCount() throws IOException {
        return contents.size();
    }

    /**
     * Gets the bounding box for the features in this feature collection.
     * 
     * @return the envelope of the geometries contained by this feature
     *         collection.
     */
    public ReferencedEnvelope getBounds() {
        if (bounds == null) {
            bounds = new ReferencedEnvelope();
    
            for (Iterator<SimpleFeature> i = contents.values().iterator(); i.hasNext();) {
                BoundingBox geomBounds = i.next().getBounds();
                // IanS - as of 1.3, JTS expandToInclude ignores "null" Envelope
                // and simply adds the new bounds...
                // This check ensures this behavior does not occur.
                if ( ! geomBounds.isEmpty() ) {
                    bounds.include(geomBounds);
                }
            }
        }
        return bounds;
    }

    public SimpleFeatureCollection collection() throws IOException {
        ListFeatureCollection copy = new ListFeatureCollection( getSchema() );
        List<SimpleFeature> list = new ArrayList<SimpleFeature>( contents.size() );
        SimpleFeatureIterator iterator = features();
        try {
            while( iterator.hasNext() ){
                SimpleFeature feature = iterator.next();
                SimpleFeature duplicate;
                try {                
                    duplicate = SimpleFeatureBuilder.copy(feature);
                } catch (org.opengis.feature.IllegalAttributeException e) {
                    throw new DataSourceException( "Unable to copy "+feature.getID(), e );
                }
                list.add( duplicate );
            }
        }
        finally {
            iterator.close();
        }
        copy.addAll( list );
        return copy;
    }

    /**
     * Optimization time ... grab the fid set so other can quickly test membership
     * during removeAll/retainAll implementations.
     * 
     * @return Set of fids.
     */
    public Set fids() {
        return Collections.unmodifiableSet( contents.keySet() );
    }

    public void accepts(org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress) {
        FeatureIterator<SimpleFeature> iterator = null;
        if (progress == null) progress = new NullProgressListener();
        try{
            float size = size();
            float position = 0;            
            progress.started();
            for( iterator = features(); !progress.isCanceled() && iterator.hasNext(); progress.progress( position++/size )){
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
            iterator.close();
        }       
    }

    /**
     * Construct a sorted view of this content.
     * <p>
     * Sorts may be combined togther in a stable fashion, in congruence
     * with the Filter 1.1 specification.
     * </p>
     * <p>
     * This method should also be able to handle GeoTools specific
     * sorting through detecting order as a SortBy2 instance.
     * </p>
     * 
     * @since GeoTools 2.2, Filter 1.1
     * @param order Filter 1.1 SortBy Construction of a Sort
     * 
     * @return FeatureList sorted according to provided order
     * 
     */
    public SimpleFeatureCollection sort(SortBy order) {
        if( order == SortBy.NATURAL_ORDER ){
            return this;
        }
        if( order instanceof SortBy2){
            SortBy2 advanced = (SortBy2) order;
            return sort( advanced );
        }
        return null;
    }
    /**
     * Allows for "Advanced" sort capabilities specific to the
     * GeoTools platform!
     * <p>
     * Advanced in this case really means making use of a generic
     * Expression, rather then being limited to PropertyName.
     * </p>
     * @param order GeoTools SortBy
     * @return FeatureList sorted according to provided order
     */
    public SimpleFeatureCollection sort(SortBy2 order ){
        if( order == SortBy.NATURAL_ORDER ){
            return this;
        }
        else if ( order == SortBy.REVERSE_ORDER ){
            // backwards
        }
        // custom
        return null; // new OrderedFeatureList( order, compare );
    }
      
    public void validate() {        
    }

    public String getID() {
        return id;
    }

    public SimpleFeatureType getSchema() {
        return schema;
    }

    /**
     * Removes all of the elements from this collection (optional operation).
     * This collection will be empty after this method returns unless it
     * throws an exception.
     */
    public void clear() {
        if(contents.isEmpty() ) {
            return;
        }
    
        SimpleFeature[] oldFeatures = new SimpleFeature[contents.size()];
        oldFeatures = (SimpleFeature[]) contents.values().toArray(oldFeatures);
    
        contents.clear();
    }

    /**
     * Returns <tt>true</tt> if this collection contains no elements.
     *
     * @return <tt>true</tt> if this collection contains no elements
     */
    public boolean isEmpty() {
        return contents.isEmpty();
    }

    /**
     * Returns the number of elements in this collection.  If this collection
     * contains more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this collection
     */
    public int size() {
        return contents.size();
    }

    /**
     * Returns an array containing all of the elements in this collection.  If
     * the collection makes any guarantees as to what order its elements are
     * returned by its iterator, this method must return the elements in the
     * same order.
     * 
     * <p>
     * The returned array will be "safe" in that no references to it are
     * maintained by this collection.  (In other words, this method must
     * allocate a new array even if this collection is backed by an array).
     * The caller is thus free to modify the returned array.
     * </p>
     * 
     * <p>
     * This method acts as bridge between array-based and collection-based
     * APIs.
     * </p>
     *
     * @return an array containing all of the elements in this collection
     */
    public Object[] toArray() {
        return contents.values().toArray();
    }

    /**
     * Returns an array containing all of the elements in this collection; the
     * runtime type of the returned array is that of the specified array. If
     * the collection fits in the specified array, it is returned therein.
     * Otherwise, a new array is allocated with the runtime type of the
     * specified array and the size of this collection.
     * 
     * <p>
     * If this collection fits in the specified array with room to spare (i.e.,
     * the array has more elements than this collection), the element in the
     * array immediately following the end of the collection is set to
     * <tt>null</tt>.  This is useful in determining the length of this
     * collection <i>only</i> if the caller knows that this collection does
     * not contain any <tt>null</tt> elements.)
     * </p>
     * 
     * <p>
     * If this collection makes any guarantees as to what order its elements
     * are returned by its iterator, this method must return the elements in
     * the same order.
     * </p>
     * 
     * <p>
     * Like the <tt>toArray</tt> method, this method acts as bridge between
     * array-based and collection-based APIs.  Further, this method allows
     * precise control over the runtime type of the output array, and may,
     * under certain circumstances, be used to save allocation costs
     * </p>
     * 
     * <p>
     * Suppose <tt>l</tt> is a <tt>List</tt> known to contain only strings. The
     * following code can be used to dump the list into a newly allocated
     * array of <tt>String</tt>:
     * <pre>
     *     String[] x = (String[]) v.toArray(new String[0]);
     * </pre>
     * </p>
     * 
     * <p>
     * Note that <tt>toArray(new Object[0])</tt> is identical in function to
     * <tt>toArray()</tt>.
     * </p>
     *
     * @param a the array into which the elements of this collection are to be
     *        stored, if it is big enough; otherwise, a new array of the same
     *        runtime type is allocated for this purpose.
     *
     * @return an array containing the elements of this collection
     */
    public Object[] toArray(Object[] a) {
        return contents.values().toArray(a != null ? a : new Object[ contents.size() ]);
    }
}
