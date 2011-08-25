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
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.util.logging.Logging;
import org.opengis.feature.type.Name;

class BoundsCallable implements Callable<ReferencedEnvelope> {
    
    static final Logger LOGGER = Logging.getLogger(FeatureCallable.class);

    Query query;

    AggregatingDataStore store;

    Name storeName;

    String typeName;

    public BoundsCallable(AggregatingDataStore store, Query query, Name storeName, String typeName) {
        super();
        this.store = store;
        this.query = query;
        this.storeName = storeName;
        this.typeName = typeName;
    }

    @Override
    public ReferencedEnvelope call() throws Exception {
        try {
            DataStore ds = store.getStore(storeName, store.isTolerant());
            SimpleFeatureSource source = ds.getFeatureSource(typeName);
            Query q = new Query(query);
            q.setTypeName(typeName);
            ReferencedEnvelope env = (ReferencedEnvelope) source.getBounds(q);
            if(LOGGER.isLoggable(Level.FINE)) {
                LOGGER.log(Level.FINE, "Retrieved bounds {0} form store {1}", new Object[] {env, storeName});
            }
            return env;
        } catch (Exception e) {
            String message = "Failed to get the bounds on " + storeName + "/" + typeName;
            if (store.isTolerant()) {
                AggregatingDataStore.LOGGER.log(Level.WARNING, message, e);
                return null;
            } else {
                throw new IOException(message, e);
            }
        }
    }

}
