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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.SchemaException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

class AggregatingFeatureSource extends ContentFeatureSource {

    /**
     * The configuration for this feature type
     */
    AggregateTypeConfiguration config;

    public AggregatingFeatureSource(ContentEntry entry, AggregatingDataStore store,
            AggregateTypeConfiguration config) {
        super(entry, null);
        this.config = config;
    }

    public AggregatingDataStore getStore() {
        return (AggregatingDataStore) getEntry().getDataStore();
    }

    @Override
    protected ReferencedEnvelope getBoundsInternal(Query query) throws IOException {
        // schedule all the bound queries
        AggregatingDataStore store = getStore();
        List<Future<ReferencedEnvelope>> allBounds = new ArrayList<Future<ReferencedEnvelope>>();
        for (Map.Entry<Name, String> entry : config.getStoreMap().entrySet()) {
            Future<ReferencedEnvelope> f = store.submit(new BoundsCallable(store, query, entry
                    .getKey(), entry.getValue()));
            allBounds.add(f);
        }

        // aggregate the envelopes
        ReferencedEnvelope result = new ReferencedEnvelope(getSchema()
                .getCoordinateReferenceSystem());
        for (Future<ReferencedEnvelope> future : allBounds) {
            try {
                ReferencedEnvelope bound = future.get();
                if (bound != null) {
                    result.expandToInclude(bound);
                }
            } catch (Exception e) {
                throw new IOException("Failed to get the envelope from a delegate store", e);
            }
        }
        if (result.isNull()) {
            return null;
        } else {
            return result;
        }
    }

    @Override
    protected int getCountInternal(Query query) throws IOException {
        // schedule all the counts
        AggregatingDataStore store = getStore();
        List<Future<Long>> counts = new ArrayList<Future<Long>>();
        for (Map.Entry<Name, String> entry : config.getStoreMap().entrySet()) {
            Future<Long> f = store.submit(new CountCallable(store, query, entry.getKey(), entry
                    .getValue()));
            counts.add(f);
        }

        // aggregate the counts
        long total = 0;
        for (Future<Long> future : counts) {
            try {
                total += future.get();
            } catch (Exception e) {
                throw new IOException("Failed to count on a delegate store", e);
            }
        }
        return (int) total;
    }

    @Override
    protected FeatureReader<SimpleFeatureType, SimpleFeature> getReaderInternal(Query query)
            throws IOException {
        try {
            // get the target schema from the primary store
            DataStore ps = getStore().getStore(config.getPrimaryStore(), false);
            SimpleFeatureType target = DataUtilities.createView(ps, query).getSchema();

            // schedule all the data retrieval operations
            FeatureQueue queue = new FeatureQueue(config.getStoreMap().size());
            AggregatingDataStore store = getStore();
            for (Map.Entry<Name, String> entry : config.getStoreMap().entrySet()) {
                FeatureCallable fc = new FeatureCallable(store, query, entry.getKey(),
                        entry.getValue(), queue, target);
                queue.addSource(fc);
                store.submit(fc);
            }

            // return the feature collection reading from the queue
            if (query.getSortBy() != null && query.getSortBy() != null) {
                throw new UnsupportedOperationException("We can't sort at the moment");
            } else {
                return new QueueReader(queue, target);
            }
        } catch (SchemaException e) {
            throw new IOException("Failed to compute target feature type", e);
        }
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        DataStore store = getStore().getStore(config.getPrimaryStore(), false);
        SimpleFeatureType schema = store.getSchema(config.getName());
        if (schema == null) {
            throw new IOException("Could not find feature type " + schema + " in the primary store");
        }
        return schema;
    }

}
