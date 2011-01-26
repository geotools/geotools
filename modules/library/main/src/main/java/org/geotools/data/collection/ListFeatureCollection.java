/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.collection;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.collection.AbstractFeatureCollection;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.geometry.BoundingBox;

/**
 * FeatureCollection implementation wrapping around a java.util.List.
 * <p>
 * This implementation wraps around a java.util.List and is suitable
 * for quickly getting something on screen.
 * <p>
 * Usage notes:
 * <ul>
 * <li>This implementation does not use a spatial index, please do not expect spatial operations to be fast.
 * <li>FeatureCollections are not allowed to have duplicates
 * </ul>
 * <p>
 * This implementation is intended to quickly wrap up a list of features and get them on screen; as such
 * it respects various hints about the copying of internal content as provided by the renderer.
 * 
 * @see Hints#FEATURE_DETACHED
 * @author Oliver Gottwald
 * @author Jody
 */
@SuppressWarnings("unchecked")
public class ListFeatureCollection extends AbstractFeatureCollection {
    /** wrapped list of features containing the contents */
     private List<SimpleFeature> list;
     
     /** Cached bounds */
     private ReferencedEnvelope bounds = null;
    
     /**
      * Create a ListFeatureCollection for the provided schema
      * An ArrayList is used internally.
      * @param schema
      */
     public ListFeatureCollection(SimpleFeatureType schema) {
         this(schema, new ArrayList<SimpleFeature>());
     }
     /**
      * Create a ListFeatureCollection around the provided list. The contents
      * of the list should all be of the provided schema for this to make sense.
      * Please keep in mind the feature collection control, no two Features in the list
      * should have the same feature id, and you should not insert the same feature more
      * then once.
      * <p>
      * The provided list is directly used for storage, most feature collection
      * operations just use a simple iterator so there is no performance advantaged
      * to be gained over using an ArrayList vs a LinkedList (other then for the size()
      * method of course).
      * 
      * @param schema
      * @param list
      */
     public ListFeatureCollection(SimpleFeatureType schema, List<SimpleFeature> list ){
         super(schema);
         this.list = list;
     }
     
     @Override
     public int size() {
         return list.size();
     }
    
     @Override
     protected Iterator openIterator() {
         Iterator it = list.iterator();
         return it;
     }
    
     @Override
     protected void closeIterator(Iterator close) {
         // nothing to do there
     }
     
    @Override
    public boolean add(SimpleFeature f) {
         //maintain the bounds
          BoundingBox boundingBox = f.getBounds();
          if (bounds == null){
              bounds = new ReferencedEnvelope(
                      boundingBox.getMinX(), boundingBox.getMaxX(),
                      boundingBox.getMinY(), boundingBox.getMaxY(),
                      schema.getCoordinateReferenceSystem());
          } else {
              bounds.expandToInclude(boundingBox.getMinX(), boundingBox.getMinY());   
              bounds.expandToInclude(boundingBox.getMaxX(), boundingBox.getMaxY());   
          }
        return list.add(f);
    }

    @Override
    public void clear() {
        // maintain the bounds
        bounds = null;
        super.clear();
    }

    @Override
    public SimpleFeatureIterator features() {
        return new ListFeatureIterator(list);
    }

    @Override
    public synchronized ReferencedEnvelope getBounds() {
        if( bounds == null ){
            bounds = calculateBounds();
        }
        return bounds;
    }
    /**
     * Calculate bounds from features
     * @return 
     */
    private ReferencedEnvelope calculateBounds() {
        ReferencedEnvelope extent = new ReferencedEnvelope();
        for( SimpleFeature feature : list ){
            if( feature == null ) continue;
            ReferencedEnvelope bbox = ReferencedEnvelope.reference( feature.getBounds() );
            if( bbox == null || bbox.isEmpty() || bbox.isNull() ) continue;
            extent.expandToInclude( bbox );
        }
        return extent;
    }

    @Override
    public boolean isEmpty() {
        return list.isEmpty();
    }
    /**
     * SimpleFeatureIterator that will use collection close method.
     * @author Jody
     */
    private class ListFeatureIterator implements SimpleFeatureIterator {
        private Iterator<SimpleFeature> iter;
       
        public ListFeatureIterator(List<SimpleFeature> features) {
            iter = features.iterator();
        }
       
        public void close() {
            if( iter instanceof FeatureIterator){
                ((FeatureIterator)iter).close();
            }
        }
        public boolean hasNext() {
            return iter.hasNext();
        }
        public SimpleFeature next() throws NoSuchElementException {
            return iter.next();
        }
    }
    
    @Override
    public SimpleFeatureCollection subCollection(Filter filter) {
        CollectionFeatureSource temp = new CollectionFeatureSource( this );
        return temp.getFeatures(filter);
    }
    
    @Override
    public SimpleFeatureCollection sort(SortBy order) {
        Query subQuery = new Query( getSchema().getTypeName() );
        subQuery.setSortBy( new SortBy[]{ order } );
       
        CollectionFeatureSource temp = new CollectionFeatureSource( this );
        return temp.getFeatures(subQuery);
    }
 }