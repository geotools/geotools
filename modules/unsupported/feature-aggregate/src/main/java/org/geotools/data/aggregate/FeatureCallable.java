/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.aggregate;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.FilterAttributeExtractor;
import org.geotools.util.logging.Logging;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;

class FeatureCallable implements Callable<Void> {

    static final Logger LOGGER = Logging.getLogger(FeatureCallable.class);

    Query query;

    AggregatingDataStore store;

    Name storeName;

    String typeName;

    FeatureQueue queue;

    SimpleFeatureBuilder builder;

    private boolean stopped = false;

    public FeatureCallable(AggregatingDataStore store, Query query, Name storeName,
            String typeName, FeatureQueue queue, SimpleFeatureType target) {
        super();
        this.store = store;
        this.query = query;
        this.storeName = storeName;
        this.typeName = typeName;
        this.queue = queue;
        this.builder = new SimpleFeatureBuilder(target);
    }

    @Override
    public Void call() throws Exception {
        SimpleFeatureIterator fi = null;
        int storeId = -1;
        try {
            // get the feature list
            DataStore ds = store.getStore(storeName, store.isTolerant());
            AggregateTypeConfiguration config = store.getTypeConfigurations().get(
                    builder.getFeatureType().getTypeName());
            storeId = config.getStoreIndex(storeName);
            SimpleFeatureSource source = ds.getFeatureSource(typeName);
            Query q = new Query(query);
            q.setTypeName(typeName);
            q.setSortBy(null);
            Filter originalFilter = q.getFilter();
            if (originalFilter != null && !Filter.INCLUDE.equals(originalFilter)) {
                // eliminate the extra attribute the delegate source does not know about
                FilterAttributeExtractor extractor = new FilterAttributeExtractor();
                originalFilter.accept(extractor, null);
                Set<String> filterNames = extractor.getAttributeNameSet();
                Set<String> sourceNames = getSourceAttributes(source);
                if (!sourceNames.containsAll(filterNames)) {
                    MissingPropertiesEraser eraser = new MissingPropertiesEraser(sourceNames);
                    Filter erased = (Filter) originalFilter.accept(eraser, null);
                    q.setFilter(erased);
                }
            }
            fi = source.getFeatures(q).features();

            // put every item in the queue, including the exception
            while (fi.hasNext() && !stopped) {
                SimpleFeature feature = fi.next();

                // build the retyped feature, with a unique id
                for (AttributeDescriptor ad : builder.getFeatureType().getAttributeDescriptors()) {
                    String attribute = ad.getLocalName();
                    Object attValue = feature.getAttribute(attribute);
                    if (attValue != null) {
                        builder.set(attribute, attValue);
                    }
                }
                String id = feature.getID();
                if (id.startsWith(typeName)) {
                    id = id.substring(typeName.length() + 1);
                }
                id = builder.getFeatureType().getTypeName() + "." + storeId + "." + id;
                SimpleFeature sf = builder.buildFeature(id);
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(Level.FINE, "Adding a new feature {0} from store {1}", new Object[] {
                            sf, storeId });
                }
                queue.put(sf);
            }
        } catch (Exception e) {
            String message = "Failed to retrieve features on " + storeName + "/" + typeName;
            if (store.isTolerant()) {
                AggregatingDataStore.LOGGER.log(Level.WARNING, message, e);
                return null;
            } else {
                queue.setException(e);
            }
        } finally {
            LOGGER.log(Level.FINE, "Adding the end marker for store {0}", storeId);
            queue.sourceComplete(this);
            queue.put(FeatureQueue.END_MARKER);
            if (fi != null) {
                fi.close();
            }
        }
        return null;
    }

    /**
     * Collects the attribute names for the specified feature source
     * 
     * @param source
     * @return
     */
    private Set<String> getSourceAttributes(SimpleFeatureSource source) {
        Set<String> result = new HashSet<String>();
        for (AttributeDescriptor ad : source.getSchema().getAttributeDescriptors()) {
            result.add(ad.getLocalName());
        }
        return result;
    }

    /**
     * Stops the callable where it is
     */
    public void shutdown() {
        stopped = true;
    }

}
