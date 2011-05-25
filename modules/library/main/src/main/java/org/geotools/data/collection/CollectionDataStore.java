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
package org.geotools.data.collection;

import java.io.IOException;
import java.util.Iterator;

import org.geotools.data.AbstractDataStore;
import org.geotools.data.DataSourceException;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.feature.CollectionEvent;
import org.geotools.feature.CollectionListener;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureCollections;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.FeatureTypes;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;

import com.vividsolutions.jts.geom.Geometry;


/**
 * Simple data store wrapper for feature collections. Allows to use feature collections in the user
 * interface layer and everything else where a data store or a feature source is needed.
 *
 * @source $URL$
 */
public class CollectionDataStore extends AbstractDataStore {
    SimpleFeatureType featureType;
    FeatureCollection<SimpleFeatureType,SimpleFeature> collection;

    /**
     * Builds a data store wrapper around an empty collection.
     *
     * @param collection
     */
    public CollectionDataStore(SimpleFeatureType schema) {
        this.collection = FeatureCollections.newCollection();
        this.featureType = schema;
        collection.addListener(new FeatureCollectionListener());
    }
    
    /**
     * Builds a data store wrapper on top of a feature collection
     *
     * @param collection
     */
    public CollectionDataStore(FeatureCollection<SimpleFeatureType,SimpleFeature> collection) {
        this.collection = collection;
        if (collection.size() == 0) {
            this.featureType = FeatureTypes.EMPTY;
        } else {
            this.featureType = collection.getSchema();
//            Iterator iter = null;
//            try {
//                iter = collection.iterator();
//                this.featureType = ((SimpleFeature) iter.next()).getFeatureType();
//            } finally {
//                if (iter != null)
//                    collection.close(iter);
//            }
        }
        collection.addListener(new FeatureCollectionListener());
    }

    /**
     * @see org.geotools.data.DataStore#getTypeNames()
     */
    public String[] getTypeNames() {
        return new String[] { featureType.getTypeName() };
    }

    /**
     * @see org.geotools.data.DataStore#getSchema(java.lang.String)
     */
    public SimpleFeatureType getSchema(String typeName) throws IOException {
        if ((typeName != null) && typeName.equals(featureType.getTypeName())) {
            return featureType;
        }

        throw new IOException(typeName + " not available");
    }

    /**
     * Provides  FeatureReader<SimpleFeatureType, SimpleFeature> over the entire contents of <code>typeName</code>.
     * 
     * <p>
     * Implements getFeatureReader contract for AbstractDataStore.
     * </p>
     *
     * @param typeName
     *
     *
     * @throws IOException If typeName could not be found
     * @throws DataSourceException See IOException
     *
     * @see org.geotools.data.AbstractDataStore#getFeatureSource(java.lang.String)
     */
    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(final String typeName)
        throws IOException {
    	return new DelegateFeatureReader<SimpleFeatureType, SimpleFeature>( getSchema(typeName), collection.features() );
    }

    /**
     * Returns the feature collection held by this data store
     *
     */
    public FeatureCollection<SimpleFeatureType,SimpleFeature> getCollection() {
        return (SimpleFeatureCollection) collection;
    }

    /**
     * @throws SchemaNotFoundException 
     * @see org.geotools.data.AbstractDataStore#getBounds(java.lang.String,
     *      org.geotools.data.Query)
     */
    protected ReferencedEnvelope getBounds(Query query) throws SchemaNotFoundException{
        String featureTypeName = query.getTypeName();
        if (!featureType.getTypeName().equals(featureTypeName)) {
            throw new SchemaNotFoundException(featureTypeName);
        }

        return getBoundsInternal(query);
    }

    /**
     * @param query
     */
    protected ReferencedEnvelope getBoundsInternal(Query query) {
        FeatureIterator<SimpleFeature> iterator = collection.features();
        ReferencedEnvelope envelope = new ReferencedEnvelope( featureType.getCoordinateReferenceSystem() );

        if (iterator.hasNext()) {
            int count = 1;
            Filter filter = query.getFilter();
            
            while (iterator.hasNext() && (count < query.getMaxFeatures())) {
                SimpleFeature feature = iterator.next();

                if (filter.evaluate(feature)) {
                    count++;
                    envelope.expandToInclude(((Geometry)feature.getDefaultGeometry()).getEnvelopeInternal());
                }
            }
        }
        return envelope;
        
    }

    /**
     * @see org.geotools.data.AbstractDataStore#getCount(java.lang.String, org.geotools.data.Query)
     */
    protected int getCount(Query query)
        throws IOException {
        String featureTypeName = query.getTypeName();
        if (!featureType.getTypeName().equals(featureTypeName)) {
            throw new SchemaNotFoundException(featureTypeName);
        }
            int count = 0;
            FeatureIterator<SimpleFeature>  iterator = collection.features();

            Filter filter = query.getFilter();

            while (iterator.hasNext() && (count < query.getMaxFeatures())) {
                if (filter.evaluate(iterator.next())) {
                    count++;
                }
            }

            return count;
    }

    /**
     * Simple listener that forwards collection events into data store events
     *
     * @author aaime
     */
    private class FeatureCollectionListener implements CollectionListener {
        public void collectionChanged(CollectionEvent tce) {
            String typeName = featureType.getTypeName();
            ReferencedEnvelope bounds = null;

            bounds = getBoundsInternal(Query.ALL);

            switch (tce.getEventType()) {
            case CollectionEvent.FEATURES_ADDED:
                listenerManager.fireFeaturesAdded(typeName, Transaction.AUTO_COMMIT, bounds, false);

                break;

            case CollectionEvent.FEATURES_CHANGED:
                listenerManager.fireFeaturesChanged(typeName, Transaction.AUTO_COMMIT, bounds, false);

                break;

            case CollectionEvent.FEATURES_REMOVED:
                listenerManager.fireFeaturesRemoved(typeName, Transaction.AUTO_COMMIT, bounds, false);

                break;
            }
        }
    }
}
