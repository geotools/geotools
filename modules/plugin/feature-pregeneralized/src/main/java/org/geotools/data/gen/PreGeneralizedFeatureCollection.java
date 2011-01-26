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

package org.geotools.data.gen;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;                                                                     
import java.lang.reflect.Method;                                                                                        
import java.lang.reflect.Modifier;  
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.NullProgressListener;
import org.opengis.feature.FeatureVisitor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.sort.SortBy;
import org.opengis.util.ProgressListener;

/**
 * @author Christian Mueller
 * 
 * Implementation of {@link FeatureCollection} for {@link PreGeneralizedSimpleFeature}
 * 
 * This collection is read only, modifying methods result in {@link UnsupportedOperationException}
 *
 * @source $URL$
 */

public class PreGeneralizedFeatureCollection implements
        SimpleFeatureCollection {

    protected SimpleFeatureCollection backendCollection;

    protected SimpleFeatureType featureType;

    protected String geomPropertyName, backendGeomPropertyName;

    protected int[] indexMapping;

    List<CollectionListener> listeners = new ArrayList<CollectionListener>();

    public PreGeneralizedFeatureCollection(
            SimpleFeatureCollection backendCollection,
            SimpleFeatureType featureType, int[] indexMapping, String geomPropertyName,
            String backendGeomPropertyName) {
        super();
        this.backendCollection = backendCollection;
        this.featureType = featureType;
        this.geomPropertyName = geomPropertyName;
        this.backendGeomPropertyName = backendGeomPropertyName;
        this.indexMapping = indexMapping;
    }

    protected UnsupportedOperationException unsupported() {
        return new UnsupportedOperationException(
                "Cannot modify a pregeneralized feature collection");
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.geotools.feature.FeatureCollection#accepts(org.opengis.feature.FeatureVisitor,
     *      org.opengis.util.ProgressListener) Logic copied from DefaultFeatureCollection class
     */
    public void accepts(FeatureVisitor visitor, ProgressListener progress) throws IOException {
        Iterator iterator = null;
        if (progress == null)
            progress = new NullProgressListener();
        try {
            float size = size();
            float position = 0;
            progress.started();
            for (iterator = iterator(); !progress.isCanceled() && iterator.hasNext(); progress
                    .progress(position++ / size)) {
                try {
                    SimpleFeature feature = (SimpleFeature) iterator.next();
                    visitor.visit(feature);
                } catch (Exception erp) {
                    progress.exceptionOccurred(erp);
                }
            }
        } finally {
            progress.complete();
            close(iterator);
        }
    }

    public boolean add(SimpleFeature arg0) {
        throw unsupported();

    }

    public boolean addAll(Collection arg0) {
        throw unsupported();

    }

    public boolean addAll(FeatureCollection arg0) {
        throw unsupported();

    }

    public void addListener(CollectionListener listener) throws NullPointerException {
        listeners.add(listener);

    }

    public void clear() {
        throw unsupported();
    }

    public void close(FeatureIterator<SimpleFeature> it) {
        it.close();

    }

        
   public void close(Iterator it) {                                                                                    
       if (it==null) return;                                                                                           
                                                                                                                       
       try {   // check for a possible close method                                                                    
           Method closeMethod = it.getClass().getMethod("close", new Class[]{});
   
               if (closeMethod!=null) {
                   int mod = closeMethod.getModifiers();
                   if (Modifier.isPublic(mod) && (Modifier.isStatic(mod)==false))
                       closeMethod.invoke(it, new Object[]{});
               }
           } catch (SecurityException e) {
               return;
           } catch (NoSuchMethodException e) {
               return;
           } catch (Exception e) {
               throw new RuntimeException("Error calling close method for "+it.getClass().getName());
       }
   }

    public boolean contains(Object feature) {
        if (feature instanceof PreGeneralizedSimpleFeature)
            return backendCollection.contains(((PreGeneralizedSimpleFeature) feature).feature);
        else
            return backendCollection.contains(feature);

    }

    public boolean containsAll(Collection coll) {
        List searchColl = new ArrayList();
        Iterator it = coll.iterator();
        while (it.hasNext()) {
            Object feature = it.next();
            if (feature instanceof PreGeneralizedSimpleFeature)
                searchColl.add(((PreGeneralizedSimpleFeature) feature).feature);
            else
                searchColl.add(feature);

        }
        return backendCollection.containsAll(searchColl);
    }

    public SimpleFeatureIterator features() {
        return new PreGeneralizedFeatureIterator(backendCollection.features(), featureType,
                indexMapping, geomPropertyName, backendGeomPropertyName);
    }

    public ReferencedEnvelope getBounds() {
        return backendCollection.getBounds();
    }

    public String getID() {
        return "pregeneralizd featurecollection";
    }

    public SimpleFeatureType getSchema() {
        return featureType;
    }

    public boolean isEmpty() {
        return backendCollection.isEmpty();

    }

    public Iterator<SimpleFeature> iterator() {
        final Iterator<SimpleFeature> iterator = backendCollection.iterator();

        return new Iterator<SimpleFeature>() {

            public boolean hasNext() {
                return iterator.hasNext();
            }

            public SimpleFeature next() {
                SimpleFeature feature = (SimpleFeature) iterator.next();
                return new PreGeneralizedSimpleFeature(featureType, indexMapping, feature,
                        geomPropertyName, backendGeomPropertyName);
            }

            public void remove() {
                throw unsupported();
            }
            public void close() {
                backendCollection.close(iterator);
            }
        };
    }

    public void purge() {
        throw unsupported();

    }

    public boolean remove(Object arg0) {
        throw unsupported();

    }

    public boolean removeAll(Collection arg0) {
        throw unsupported();

    }

    public void removeListener(CollectionListener listener) throws NullPointerException {
        listeners.remove(listener);

    }

    public boolean retainAll(Collection arg0) {
        throw unsupported();

    }

    public int size() {
        return backendCollection.size();
    }

    public SimpleFeatureCollection sort(SortBy sortBy) {
        SimpleFeatureCollection fColl = backendCollection.sort(sortBy);
        if (fColl == null)
            return null;
        return new PreGeneralizedFeatureCollection(fColl, featureType, indexMapping,
                geomPropertyName, backendGeomPropertyName);

    }

    public SimpleFeatureCollection subCollection(Filter filter) {
        SimpleFeatureCollection fColl = backendCollection
                .subCollection(filter);
        if (fColl == null)
            return null;
        return new PreGeneralizedFeatureCollection(fColl, featureType, indexMapping,
                geomPropertyName, backendGeomPropertyName);
    }

    public Object[] toArray() {
        Object[] res = backendCollection.toArray();
        for (int i = 0; i < res.length; i++) {
            res[i] = new PreGeneralizedSimpleFeature(getSchema(), indexMapping,
                    (SimpleFeature) res[i], geomPropertyName, backendGeomPropertyName);
        }
        return res;
    }

    public Object[] toArray(Object[] arg0) {
        Object[] res = backendCollection.toArray(arg0);
        for (int i = 0; i < res.length; i++) {
            res[i] = new PreGeneralizedSimpleFeature(getSchema(), indexMapping,
                    (SimpleFeature) res[i], geomPropertyName, backendGeomPropertyName);
        }
        return res;
    }

}
