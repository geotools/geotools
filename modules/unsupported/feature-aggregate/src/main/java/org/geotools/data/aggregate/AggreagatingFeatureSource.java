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
import java.util.concurrent.Future;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureReader;
import org.geotools.data.store.ContentEntry;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.feature.SchemaException;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

import com.vividsolutions.jts.geom.Envelope;

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
        for (SourceType st : config.getSourceTypes()) {
            Future<ReferencedEnvelope> f = store.submit(new BoundsCallable(store, query, st
                    .getStoreName(), st.getTypeName()));
            allBounds.add(f);
        }

        // aggregate the envelopes
        ReferencedEnvelope result = new ReferencedEnvelope(getSchema()
                .getCoordinateReferenceSystem());
        for (Future<ReferencedEnvelope> future : allBounds) {
            try {
                ReferencedEnvelope bound = future.get();
                if (bound != null) {
                    // the inputs might have slightly different crs, compensate
                    Envelope env = new Envelope(bound);
                    result.expandToInclude(env);
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
        for (SourceType st : config.getSourceTypes()) {
            Future<Long> f = store.submit(new CountCallable(store, query, st.getStoreName(), st.getTypeName()));
            counts.add(f);
        }

        // aggregate the counts
        long total = 0;
        for (Future<Long> future : counts) {
            try {
                long count = future.get();
                if (count > 0) {
                    total += count;
                } else {
                    // one of the sources found it was too costly to count
                    return -1;
                }
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
            Query psQuery = new Query(query);
            Name psName = config.getPrimarySourceType().getStoreName();
            psQuery.setTypeName(config.getPrimarySourceType().getTypeName());
            DataStore ps = getStore().getStore(psName, false);
            SimpleFeatureType target = DataUtilities.createView(ps, psQuery).getSchema();
            target = retypeNameSchema(target);

            // schedule all the data retrieval operations
            FeatureQueue queue = new FeatureQueue(config.getSourceTypes().size());
            AggregatingDataStore store = getStore();
            for (SourceType st : config.getSourceTypes()) {
                FeatureCallable fc = new FeatureCallable(store, query, st.getStoreName(),
                        st.getTypeName(), queue, target);
                queue.addSource(fc);
                store.submit(fc);
            }

            // build a reader out of the queue
            SimpleFeatureReader reader = new QueueReader(queue, target);
            return reader;
        } catch (SchemaException e) {
            throw new IOException("Failed to compute target feature type", e);
        }
    }

    @Override
    protected SimpleFeatureType buildFeatureType() throws IOException {
        Name ps = config.getPrimarySourceType().getStoreName();
        DataStore store = getStore().getStore(ps, false);
        SimpleFeatureType schema = store.getSchema(config.getPrimarySourceType().getTypeName());
        if (schema == null) {
            throw new IOException("Could not find feature type " + schema + " in the primary store");
        }

        schema = retypeNameSchema(schema);

        return schema;
    }

    /**
     * Given a source store schema it renames it and forces in the right namespace
     * 
     * @param schema
     * @return
     */
    private SimpleFeatureType retypeNameSchema(SimpleFeatureType schema) {
        SimpleFeatureTypeBuilder tb = new SimpleFeatureTypeBuilder();
        tb.init(schema);
        tb.setName(config.getName());
        tb.setNamespaceURI(getStore().getNamespaceURI());
        schema = tb.buildFeatureType();
        return schema;
    }

    @Override
    protected boolean canFilter() {
        return true;
    }

    @Override
    protected boolean canRetype() {
        return true;
    }

}
