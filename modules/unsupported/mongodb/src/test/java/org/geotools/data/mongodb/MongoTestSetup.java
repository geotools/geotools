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
import java.text.ParseException;
import java.util.Date;
import java.util.Map;
import java.util.Properties;

import com.mongodb.BasicDBList;
import com.mongodb.DB;

public abstract class MongoTestSetup {

    public abstract void setUp(DB db);

    protected abstract void setUpDataStore(MongoDataStore dataStore);

    protected abstract Date getDateProperty(int featureIdx);

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

    /**
     * @param dateAsString ISO-8601 formatted date
     * @return the parsed date
     */
    protected static Date parseDate(String dateAsString) {
        try {
            return FilterToMongo.ISO8601_SDF.parse(dateAsString);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Failed to parse string as ISO-8601 formatted date", e);
        }
    }

}
