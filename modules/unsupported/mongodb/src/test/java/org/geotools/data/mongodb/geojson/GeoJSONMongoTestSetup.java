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
import org.geotools.data.mongodb.MongoDataStore;
import org.geotools.data.mongodb.MongoTestSetup;

public class GeoJSONMongoTestSetup extends MongoTestSetup {

    @Override
    protected void setUpDataStore(MongoDataStore dataStore) {

    }

    @Override
    public void setUp(DB db) {
        DBCollection ft1 = db.getCollection("ft1");
        ft1.drop();

        ft1.save(BasicDBObjectBuilder.start()
            .add("id", "ft1.0")
            .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(0,0))
            .pop()
            .push("properties")
                .add("intProperty", 0)
                .add("doubleProperty", 0.0)
                .add("stringProperty", "zero")
            .pop()
        .get());
        ft1.save(BasicDBObjectBuilder.start()
            .add("id", "ft1.1")
            .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(1,1))
            .pop()
            .push("properties")
                .add("intProperty", 1)
                .add("doubleProperty", 1.1)
                .add("stringProperty", "one")
            .pop()
        .get());
        ft1.save(BasicDBObjectBuilder.start()
            .add("id", "ft1.2")
            .push("geometry")
                .add("type", "Point")
                .add("coordinates", list(2,2))
            .pop()
            .push("properties")
                .add("intProperty", 2)
                .add("doubleProperty", 2.2)
                .add("stringProperty", "two")
            .pop()
        .get());

        ft1.ensureIndex(new BasicDBObject("geometry.coordinates", "2d"));

        DBCollection ft2 = db.getCollection("ft2");
        ft2.drop();
    }

}
