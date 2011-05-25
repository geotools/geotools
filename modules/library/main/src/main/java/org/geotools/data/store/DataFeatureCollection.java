/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.FeatureReader;
import org.geotools.data.FeatureWriter;
import org.geotools.data.collection.DelegateFeatureReader;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureReaderIterator;
import org.geotools.feature.collection.DelegateSimpleFeatureIterator;
import org.geotools.feature.collection.SubFeatureCollection;
import org.geotools.filter.SortBy2;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.Feature;
import org.opengis.feature.IllegalAttributeException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;


/**
 * A starting point for implementing FeatureCollection's backed onto a FeatureReader.
 * <p>
 * This implementation requires you to implement the following:
 * <ul>
 * <li>getSchema() - this should match reader.getSchema()
 * <li>reader()</br>
 *     features() - override one of these two method to access content
 * <li>getBounds()
 * <li>getCount()
 * <li>collection()
 * </p>
 * <p>
 * This class will implement the 'extra' methods required by FeatureCollection
 * for you (in simple terms based on the FeatureResults API). Anything that is
 * <i>often</i> customised is available to you as a constructor parameters.
 * <p>
 * Enjoy.
 * </p>
 * @author jgarnett
 * @since 2.1.RC0
 *
 * @source $URL$
 */
public abstract class DataFeatureCollection implements SimpleFeatureCollection {
    
	/** logger */
	static Logger LOGGER = org.geotools.util.logging.Logging.getLogger( "org.geotools.data" );
	
    static private int unique = 0;
    
    /**
     * Collection based on a generic collection
     */
    protected DataFeatureCollection(){
        this( "features"+(unique++) );
    }
    /**
     * Collection based on a generic collection
     */
    protected DataFeatureCollection( String id ){
    	this(id,null);
    }
    
    /** Subclass must think about what consitructors it needs. */
    protected DataFeatureCollection( String id, SimpleFeatureType memberType ){
    	this.id = id == null ? "featureCollection" : id;
        this.schema = memberType;
    }
    
    /**
     * To let listeners know that something has changed.
     */
    protected void fireChange(SimpleFeature[] features, int type) {        
        CollectionEvent cEvent = new CollectionEvent(this, features, type);
        
        for (int i = 0, ii = listeners.size(); i < ii; i++) {
            ((CollectionListener) listeners.get(i)).collectionChanged(cEvent);
        }
    }
    protected void fireChange(SimpleFeature feature, int type) {
        fireChange(new SimpleFeature[] {feature}, type);
    }    
    protected void fireChange(Collection coll, int type) {
        SimpleFeature[] features = new SimpleFeature[coll.size()];
        features = (SimpleFeature[]) coll.toArray(features);
        fireChange(features, type);
    }
    
    public  FeatureReader<SimpleFeatureType, SimpleFeature> reader() throws IOException {
    	return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>( getSchema(), features() );
    }
    
    //
    // Feature Results methods
    // 
    // To be implemented by subclass
    //    

    public abstract ReferencedEnvelope getBounds();

    public abstract int getCount() throws IOException;;

    //public abstract SimpleFeatureCollection collection() throws IOException;

    //
    // Additional Subclass "hooks"
    //
    /**
     * Subclass may provide an implementation of this method to indicate
     * that read/write support is provided.
     * <p>
     * All operations that attempt to modify the "data" will
     * use this method, allowing them to throw an "UnsupportedOperationException"
     * in the same manner as Collections.unmodifiableCollection(Collection c), or just
     * return null.
     * </p>
     * @throws UnsupportedOperationException To indicate that write support is not avaiable
     * @return the writer, or null if write support is not available
     */
    protected FeatureWriter<SimpleFeatureType, SimpleFeature> writer() throws IOException {
        return null;
    }
    //
    // SimpleFeatureCollection methods
    // 
    // implemented in terms of feature results
    //
    
    //
    // Content Access
    //
    /** Set of open resource iterators & featureIterators */
    private final Set open = new HashSet();

    /**
     * listeners
     */
    protected List listeners = new ArrayList();

    /** 
     * id used when serialized to gml
     */
    protected String id;

    protected SimpleFeatureType schema;
    /**
     * SimpleFeatureIterator is entirely based on iterator().
     * <p>
     * So when we implement FeatureCollection.iterator() this will work
     * out of the box.
     */
    public SimpleFeatureIterator features() {
    	SimpleFeatureIterator iterator = new DelegateSimpleFeatureIterator( this, iterator() );
        open.add( iterator );
        return iterator;
    }
   
    /**
     * Iterator may (or may) not support modification.
     */
    final public Iterator<SimpleFeature> iterator() {
    	Iterator<SimpleFeature> iterator;
		try {
			iterator = openIterator();
		} 
		catch (IOException e) {
			throw new RuntimeException( e );
		}
		
    	open.add( iterator );
    	return iterator;    	    	
    }
    
    /**
     * Returns a FeatureWriterIterator, or FeatureReaderIterator over content.
     * <p>
     * If you have a way to tell that you are readonly please subclass with
     * a less hardcore check - this implementations catches a
     * UnsupportedOpperationsException from wrtier()!
     * 
     * @return Iterator, should be closed closeIterator 
     */
    protected Iterator<SimpleFeature> openIterator() throws IOException
    {    	
    	try {
    	    FeatureWriter writer = writer();
    	    if(writer != null) {
    	        return new FeatureWriterIterator( writer() );
    	    }
        } catch (IOException badWriter) {
            return new NoContentIterator( badWriter );
        } catch( UnsupportedOperationException readOnly ){
        }
        
        try {
            return new FeatureReaderIterator( reader() );
        } catch (IOException e) {
            return new NoContentIterator( e );
        }        
    }

    final public void close( Iterator<SimpleFeature> close ) {
    	try {
			closeIterator( close );
		} 
    	catch (IOException e) {
			LOGGER.log( Level.WARNING, "Error closing iterator", e );
		}
    	open.remove( close );
    }   
    
    protected void closeIterator( Iterator<SimpleFeature> close ) throws IOException
    {
    	if( close == null ){
            // iterator probably failed during consturction !
        }
        else if( close instanceof FeatureReaderIterator ){
            FeatureReaderIterator<SimpleFeature> iterator = (FeatureReaderIterator<SimpleFeature>) close;
            iterator.close(); // only needs package visability
        }
        else if( close instanceof FeatureWriterIterator ){
            FeatureWriterIterator iterator = (FeatureWriterIterator) close;
            iterator.close(); // only needs package visability
        }
    }
    
    public void close( FeatureIterator<SimpleFeature> iterator) {
    	iterator.close();
        open.remove( iterator );        
    }

   
    
    /** Default implementation based on getCount() - this may be expensive */
    public int size() {
        try {
            return getCount();
        } catch (IOException e) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.log(Level.FINE, "IOException while calculating size() of FeatureCollection", e);
            return 0;
        }
    }
    public void purge(){    	
    	for( Iterator i = open.iterator(); i.hasNext(); ){
    		Object iterator =  i.next();
    		try {
    			if( iterator instanceof Iterator ){
    				closeIterator( (Iterator) iterator );
    			}
    			if( iterator instanceof FeatureIterator){
    				( (SimpleFeatureIterator) iterator ).close();
    			}
    		}
    		catch( Throwable e){
    			// TODO: Log e = ln
    		}
    		finally {
    			i.remove();
    		}
    	}
    }
    //
    // Off into implementation land!
    //
    /**
     * Default implementation based on creating an reader, testing hasNext, and closing.
     * <p>
     * For once the Collections API does not give us an escape route, we *have* to check the data.
     * </p>
     */
    public boolean isEmpty() {
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
        try {
            reader = reader();
            try {
                return !reader.hasNext();
            } catch (IOException e) {
                return true; // error seems like no features are available 
            }
        } catch (IOException e) {
            return true;
        }
        finally {
            if( reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    // return value already set
                }
            }
        }
    }

    public boolean contains( Object o ) {
        if( !(o instanceof SimpleFeature) ) return false;
        SimpleFeature value = (SimpleFeature) o;
        String ID = value.getID();
        
         FeatureReader<SimpleFeatureType, SimpleFeature> reader = null;
        try {
            reader = reader();
            try {
                while( reader.hasNext() ){
                    SimpleFeature feature = reader.next();
                    if( !ID.equals( feature.getID() )){
                        continue; // skip with out full equal check
                    }
                    if( value.equals( feature )) return true;
                }
                return false; // not found
            } catch (IOException e) {
                return false; // error seems like no features are available 
            } catch (NoSuchElementException e) {
                return false; // error seems like no features are available
            } catch (IllegalAttributeException e) {
                return false; // error seems like no features are available
            }
        } catch (IOException e) {
            return false;
        }
        finally {
            if( reader != null ){
                try {
                    reader.close();
                } catch (IOException e) {
                    // return value already set
                }
            }
        }
    }

    public Object[] toArray() {
        return toArray( new SimpleFeature[ size() ]);
    }

    public Object[] toArray( Object[] array ) {
        List list = new ArrayList();
        Iterator i = iterator();
        try {
            while( i.hasNext() ){
                list.add( i.next() );
            }
        }
        finally {
            close( i );
        }
        return list.toArray( array );
    }

    public boolean add( SimpleFeature arg0 ) {
        return false;
    }

    public boolean remove( Object arg0 ) {
        return false;
    }

    public boolean containsAll( Collection<?> collection ) {
    	for (Object o: collection) {
    		if (contains(o)==false) return false;
    	}
    	return true;
    }

    /**
     * Optimized implementation of addAll that recognizes the
     * use of collections obtained with subCollection( filter ).
     * <p>
     * This method is constructed by either:
     * <ul>
     * <li>Filter OR
     * <li>Removing an extact match of Filter AND
     * </ul>
     * 
     */
    public boolean addAll(Collection collection) {
        if( collection instanceof FeatureCollection ){
            return addAll( (FeatureCollection<?,?>) collection );
        }
        try {
            FeatureWriter writer = writer();
            if(writer == null) {
                return false;
            }
            try {
                // skip to end
                 while( writer.hasNext() ){
                    Feature feature = writer.next();
                }
                for( Object obj : collection ){
                    if( obj instanceof SimpleFeature){
                        SimpleFeature copy = (SimpleFeature) obj;
                        SimpleFeature feature = (SimpleFeature) writer.next();
                        
                        feature.setAttributes( copy.getAttributes() );
                        writer.write();
                    }
                }
            }
            finally {
                if( writer != null ) writer.close();
            }
            return true;
        }
        catch( IOException ignore ){
            return false;
        }
    }
    public boolean addAll(FeatureCollection resource) {
        return false;
    }
    public boolean removeAll( Collection arg0 ) {        
        return false;
    }
    public boolean retainAll( Collection arg0 ) {
        return false;
    }
    public void clear() {        
    }
    
    public void accepts(org.opengis.feature.FeatureVisitor visitor, org.opengis.util.ProgressListener progress) {
    	Iterator iterator = null;
        if (progress == null) progress = new NullProgressListener();
        try{
            float size = size();
            float position = 0;            
            progress.started();
        	for( iterator = iterator(); !progress.isCanceled() && iterator.hasNext(); progress.progress( 100.0f * position++ / size )){
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
        	close( iterator );
        }
    }
    
    /**
     * Will return an optimized subCollection based on access
     * to the origional FeatureSource.
     * <p>
     * The subCollection is constructed by using an AND Filter.
     * For the converse of this opperation please see
     * collection.addAll( Collection ), it has been optimized
     * to be aware of these filter based SubCollections.
     * </p>
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
     * Construct a sorted view of this content.
     * <p>
     * Sorts may be combined togther in a stable fashion, in congruence
     * with the Filter 1.1 specification.
     * </p>
     * This method should also be able to handle GeoTools specific
     * sorting through detecting order as a SortBy2 instance.
     * 
     * @param order
     * 
     * @since GeoTools 2.2, Filter 1.1
     * @return FeatureList sorted according to provided order

     */
    public SimpleFeatureCollection sort(SortBy order) {
    	if( order instanceof SortBy2){
    		SortBy2 advanced = (SortBy2) order;
    		return sort( advanced );
    	}
    	return null; // new OrderedFeatureList( this, order );
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
    	return null;
    }
    public String getID() {
    	return id;
    }
    public final void addListener(CollectionListener listener) throws NullPointerException {
    	listeners.add(listener);
    }
    public final void removeListener(CollectionListener listener)
            throws NullPointerException {
            	listeners.remove(listener);
            }
    public SimpleFeatureType getSchema() {
    	return schema;
    }
 
}
