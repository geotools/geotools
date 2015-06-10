/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 * 
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2014-2015, Boundless
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
package org.geotools.data.mongodb;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;

import com.mongodb.BasicDBList;
import com.mongodb.DB;

public abstract class MongoTestSetup {

    public abstract void setUp(DB db);

    protected abstract void setUpDataStore(MongoDataStore dataStore);

    public MongoDataStore createDataStore(Properties fixture) throws IOException {
        MongoDataStore dataStore = new MongoDataStoreFactory().createDataStore((Map)fixture);
        setUpDataStore(dataStore);
        return dataStore;
    }

    protected BasicDBList list(Object... values) {
        BasicDBList list = new BasicDBList();
        for (Object v : values) {
            list.add(v);
        }
        return list;
    }
}
