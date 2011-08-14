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

import org.geotools.data.DataStore;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureSource;
import org.opengis.feature.type.Name;

class CountCallable implements Callable<Long> {

    Query query;

    AggregatingDataStore store;

    Name storeName;

    String typeName;

    public CountCallable(AggregatingDataStore store, Query query, Name storeName, String typeName) {
        super();
        this.store = store;
        this.query = query;
        this.storeName = storeName;
        this.typeName = typeName;
    }

    @Override
    public Long call() throws Exception {
        try {
            DataStore ds = store.getStore(storeName, store.isTolerant());
            SimpleFeatureSource source = ds.getFeatureSource(typeName);
            Query q = new Query(query);
            q.setTypeName(typeName);
            return (long) source.getCount(q);
        } catch (Exception e) {
            String message = "Failed to count on " + storeName + "/" + typeName;
            if (store.isTolerant()) {
                AggregatingDataStore.LOGGER.log(Level.WARNING, message, e);
                return 0l;
            } else {
                throw new IOException(message, e);
            }
        }
    }

}
