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

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;

/** @author tkunicki@boundlessgeo.com */
public class MongoSchemaDBStore implements MongoSchemaStore {

    static final String DEFAULT_databaseName = "geotools";
    static final String DEFAULT_collectionName = "schemas";

    final MongoClient client;
    final DBCollection collection;

    public MongoSchemaDBStore(String uri) throws IOException {
        this(new MongoClientURI(uri));
    }

    public MongoSchemaDBStore(MongoClientURI uri) throws IOException {
        client = new MongoClient(uri);

        String databaseName = uri.getDatabase();
        if (databaseName == null) {
            databaseName = DEFAULT_databaseName;
        }
        DB database = client.getDB(databaseName);

        String collectionName = uri.getCollection();
        if (collectionName == null) {
            collectionName = DEFAULT_collectionName;
        }
        collection = database.getCollection(collectionName);
        collection.createIndex(
                new BasicDBObject(FeatureTypeDBObject.KEY_typeName, 1),
                new BasicDBObject("unique", true));
    }

    @Override
    public void storeSchema(SimpleFeatureType schema) throws IOException {
        if (schema != null) {
            String typeName = schema.getTypeName();
            if (typeName != null) {
                collection.update(
                        new BasicDBObject(FeatureTypeDBObject.KEY_typeName, schema.getTypeName()),
                        FeatureTypeDBObject.convert(schema),
                        true,
                        false);
            }
        }
    }

    @Override
    public SimpleFeatureType retrieveSchema(Name name) throws IOException {
        if (name == null) {
            return null;
        }
        String typeName = name.getLocalPart();
        if (typeName == null) {
            return null;
        }
        DBObject document =
                collection.findOne(new BasicDBObject(FeatureTypeDBObject.KEY_typeName, typeName));
        SimpleFeatureType featureType = null;
        if (document != null) {
            try {
                featureType = FeatureTypeDBObject.convert(document, name);
            } catch (RuntimeException e) {
                // bah, maybe should use typed exception here...
            }
        }
        return featureType;
    }

    @Override
    public void deleteSchema(Name name) throws IOException {
        if (name == null) {
            return;
        }
        String typeName = name.getLocalPart();
        if (typeName == null) {
            return;
        }
        collection.remove(new BasicDBObject(FeatureTypeDBObject.KEY_typeName, typeName));
    }

    @Override
    public List<String> typeNames() {
        DBCursor cursor =
                collection.find(
                        new BasicDBObject(),
                        new BasicDBObject(FeatureTypeDBObject.KEY_typeName, 1));
        List<String> typeNames = new ArrayList<String>(cursor.count());
        while (cursor.hasNext()) {
            DBObject document = cursor.next();
            if (document != null) {
                Object typeName = document.get(FeatureTypeDBObject.KEY_typeName);
                if (typeName instanceof String) {
                    typeNames.add((String) typeName);
                }
            }
        }
        return typeNames;
    }

    @Override
    public void close() {
        client.close();
    }
}
