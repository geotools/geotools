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

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.vividsolutions.jts.geom.Point;
import java.util.Properties;
import org.geotools.test.OnlineTestCase;
import org.opengis.feature.simple.SimpleFeature;

public abstract class MongoTestSupport extends OnlineTestCase {

    protected MongoTestSetup testSetup;
    protected MongoDataStore dataStore;
    
    protected MongoClient client;

    protected MongoTestSupport(MongoTestSetup testSetup) {
        this.testSetup = testSetup;
    }

    @Override
    protected String getFixtureId() {
        return "mongodb";
    }

    @Override
    protected boolean isOnline() throws Exception {
        return doConnect() != null;
    }

    @Override
    protected void connect() throws Exception {
         setUp(doConnect());
    }

    DB doConnect() throws Exception {
        MongoClientURI clientURI = new MongoClientURI(fixture.getProperty(MongoDataStoreFactory.DATASTORE_URI.key));
        client = new MongoClient(clientURI);
        return client.getDB(clientURI.getDatabase());
    }

    protected void setUp(DB db) throws Exception {
        testSetup.setUp(db);
        dataStore = testSetup.createDataStore(fixture);
    }

    @Override
    protected void tearDownInternal() throws Exception {
        super.tearDownInternal();
        dataStore.dispose();
        client.close();
    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put(MongoDataStoreFactory.DATASTORE_URI.key, "mongodb://geotools:geotools@localhost:27017/geotools");
        return fixture;
    }

    protected void assertFeature(SimpleFeature f) {
        int i = (Integer) f.getAttribute("intProperty");
        assertFeature(f, i);
    }
    
    protected void assertFeature(SimpleFeature f, int i) {
        assertNotNull(f.getDefaultGeometry());
        Point p = (Point) f.getDefaultGeometry();

        assertNotNull(f.getAttribute("intProperty"));

        assertEquals((double)i, p.getX(), 0.1);
        assertEquals((double)i, p.getY(), 0.1);

        assertNotNull(f.getAttribute("doubleProperty"));
        assertEquals(i + i*0.1, (Double)f.getAttribute("doubleProperty"), 0.1);

        assertNotNull(f.getAttribute("stringProperty"));
        assertEquals(toString(i), (String)f.getAttribute("stringProperty"));
    }

    protected String toString(int i) {
        return i == 0 ? "zero" : i == 1 ? "one" : i == 2 ? "two" : null;
    }
}
