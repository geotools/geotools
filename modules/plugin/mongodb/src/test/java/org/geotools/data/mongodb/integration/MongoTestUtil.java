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
package org.geotools.data.mongodb.integration;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.MatcherAssert.assertThat;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.MongoClient;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.LinkedHashSet;
import java.util.Set;
import org.geotools.TestData;
import org.geotools.api.feature.Feature;
import org.geotools.api.feature.GeometryAttribute;
import org.geotools.api.feature.Property;
import org.geotools.data.mongodb.MongoGeometryBuilder;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

/** @author tkunicki@boundlessgeo.com */
@SuppressWarnings("deprecation") // DB was replaced by MongoDatabase but API is not the same
public class MongoTestUtil {

    static final int PORT;

    static {
        String portAsString = System.getProperty("embedmongo.port");
        int port = 27017;
        if (!(portAsString == null || portAsString.isEmpty())) {
            try {
                port = Integer.parseInt(portAsString);
            } catch (NumberFormatException e) {
                // System.out.println("Exception extracting EmbedMongo port from property");
            }
        }
        PORT = port;
        // System.out.println("EmbedMongo Port is " + PORT);
    }

    @Test
    public void testConnect() throws UnknownHostException {
        try (MongoClient mc = new MongoClient("localhost", PORT)) {
            assertThat(mc, is(notNullValue()));
            DB db = mc.getDB("db");
            DBCollection coll = db.getCollection("dbc");
            BasicDBObject bdo =
                    new BasicDBObject("name", "MongoDB")
                            .append("type", "database")
                            .append("count", 1)
                            .append("info", new BasicDBObject("x", 203).append("y", 102));

            coll.insert(bdo);
            // System.out.println(coll.findOne());
        }
    }

    @Test
    public void testLoad() throws IOException {
        try (MongoClient mc = new MongoClient("localhost", PORT)) {
            DBCollection dbc = grabDBCollection(mc, "db", "dbc", true);
            ShapefileDataStore sds = loadShapefile("shapes/statepop.shp");
            loadFeatures(dbc, sds.getFeatureSource().getFeatures());
        }
    }

    public void loadFeatures(DBCollection coll, FeatureCollection<?, ?> collection) {
        MongoGeometryBuilder gBuilder = new MongoGeometryBuilder();

        try (FeatureIterator<?> iterator = collection.features()) {
            while (iterator.hasNext()) {
                Feature f = iterator.next();
                Set<Property> pSet = new LinkedHashSet<>(f.getProperties());
                BasicDBObjectBuilder bdoBuilder = BasicDBObjectBuilder.start();

                GeometryAttribute gAttr = f.getDefaultGeometryProperty();
                bdoBuilder.add("type", "feature");
                bdoBuilder.add("geometry", gBuilder.toObject((Geometry) gAttr.getValue()));
                pSet.remove(gAttr);

                bdoBuilder.push("properties");
                for (Property p : pSet) {
                    if (!(p instanceof GeometryAttribute)) {
                        bdoBuilder.add(p.getName().getLocalPart(), p.getValue());
                    }
                }
                bdoBuilder.pop();
                coll.insert(bdoBuilder.get());
                coll.createIndex(new BasicDBObject("geometry", "2dsphere"));
            }
        }
    }

    private ShapefileDataStore loadShapefile(String path) throws FileNotFoundException {
        URL url = TestData.url(path);
        assertThat(url, is(notNullValue()));
        ShapefileDataStore store = new ShapefileDataStore(url);
        assertThat(store, is(notNullValue()));
        return store;
    }

    private DBCollection grabDBCollection(
            MongoClient client, String dbName, String dbcName, boolean init)
            throws UnknownHostException {
        DB db = client.getDB(dbName);
        DBCollection dbc = db.getCollection(dbcName);
        if (init) {
            dbc.dropIndexes();
            dbc.drop();
        }
        return dbc;
    }
}
