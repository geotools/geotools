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
package org.geotools.data.mongodb.geojson;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import java.util.Date;
import org.geotools.data.mongodb.MongoDataStore;
import org.geotools.data.mongodb.MongoTestSetup;

public class GeoJSONMongoTestSetup extends MongoTestSetup {

    static Date[] dateValues =
            new Date[] {
                parseDate("2015-01-01T00:00:00.000Z"),
                parseDate("2015-01-01T16:30:00.000Z"),
                parseDate("2015-01-01T21:30:00.000Z")
            };

    @Override
    protected void setUpDataStore(MongoDataStore dataStore) {}

    @Override
    public void setUp(DB db) {
        DBCollection ft1 = db.getCollection("ft1");
        ft1.drop();

        ft1.save(
                BasicDBObjectBuilder.start()
                        .add("id", "ft1.0")
                        .push("geometry")
                        .add("type", "Point")
                        .add("coordinates", list(0, 0))
                        .pop()
                        .push("properties")
                        .add("intProperty", 0)
                        .add("doubleProperty", 0.0)
                        .add("stringProperty", "zero")
                        .add(
                                "listProperty",
                                list(
                                        new BasicDBObject("value", 0.1),
                                        new BasicDBObject("value", 0.2)))
                        .add("dateProperty", getDateProperty(0))
                        .pop()
                        .get());
        ft1.save(
                BasicDBObjectBuilder.start()
                        .add("id", "ft1.1")
                        .push("geometry")
                        .add("type", "Point")
                        .add("coordinates", list(1, 1))
                        .pop()
                        .push("properties")
                        .add("intProperty", 1)
                        .add("doubleProperty", 1.1)
                        .add("stringProperty", "one")
                        .add(
                                "listProperty",
                                list(
                                        new BasicDBObject("value", 1.1),
                                        new BasicDBObject("value", 1.2)))
                        .add("dateProperty", getDateProperty(1))
                        .pop()
                        .get());
        ft1.save(
                BasicDBObjectBuilder.start()
                        .add("id", "ft1.2")
                        .push("geometry")
                        .add("type", "Point")
                        .add("coordinates", list(2, 2))
                        .pop()
                        .push("properties")
                        .add("intProperty", 2)
                        .add("doubleProperty", 2.2)
                        .add("stringProperty", "two")
                        .add(
                                "listProperty",
                                list(
                                        new BasicDBObject("value", 2.1),
                                        new BasicDBObject("value", 2.2)))
                        .add("dateProperty", getDateProperty(2))
                        .pop()
                        .get());

        ft1.createIndex(new BasicDBObject("geometry", "2dsphere"));
        ft1.createIndex(new BasicDBObject("properties.listProperty.value", 1));

        DBCollection ft2 = db.getCollection("ft2");
        ft2.drop();
    }

    @Override
    protected Date getDateProperty(int featureIdx) {
        if (featureIdx < dateValues.length) {
            return dateValues[featureIdx];
        }

        return null;
    }
}
