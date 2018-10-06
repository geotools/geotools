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

import static org.geotools.data.mongodb.MongoSchemaDBStore.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;
import static org.junit.Assume.assumeThat;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;
import org.geotools.util.logging.Logging;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/** @author tkunicki@boundlessgeo.com */
public class MongoSchemaDBStoreTest extends MongoSchemaStoreTest<MongoSchemaDBStore> {

    static final Logger LOGGER = Logging.getLogger(MongoSchemaDBStore.class);

    Map<MongoSchemaDBStore, MongoClientURI> clientURIs =
            new HashMap<MongoSchemaDBStore, MongoClientURI>();

    static String host;
    static int port;
    static MongoClient client;

    static boolean defaultDBExists;
    static boolean defaultCollectionExists;

    @BeforeClass
    public static void setupMongo() {
        String portAsString = System.getProperty("embedmongo.port");
        String hostAsString;
        if (portAsString == null) {
            portAsString = System.getProperty("mongo.port");
            hostAsString = System.getProperty("mongo.host");
        } else {
            hostAsString = "localhost";
        }
        if (hostAsString != null) {
            port =
                    portAsString == null || portAsString.isEmpty()
                            ? 27017
                            : Integer.parseInt(portAsString);
            MongoClientURI clientURI = generateURI("localhost", port, null, null);
            try {
                client = new MongoClient(clientURI);
                // perform an operation to determine if we're actually connected
                defaultDBExists = client.getDatabaseNames().contains(DEFAULT_databaseName);
                if (defaultDBExists) {
                    defaultCollectionExists =
                            client.getDB(DEFAULT_databaseName)
                                    .collectionExists(DEFAULT_collectionName);
                }
                LOGGER.info(
                        "Performing "
                                + MongoSchemaDBStoreTest.class.getSimpleName()
                                + " tests with "
                                + clientURI);
            } catch (Exception e) {
                client = null;
                LOGGER.warning(
                        "Exception initializing "
                                + MongoSchemaDBStoreTest.class.getSimpleName()
                                + " tests with "
                                + clientURI
                                + ": "
                                + e);
            } catch (Error e) {
                client = null;
                LOGGER.warning(
                        "Error initializing "
                                + MongoSchemaDBStoreTest.class.getSimpleName()
                                + " tests with "
                                + clientURI
                                + ": "
                                + e);
            }
        }
    }

    @AfterClass
    public static void cleanUp() {
        if (client != null) {
            if (!defaultDBExists) {
                client.dropDatabase(DEFAULT_databaseName);
            } else if (!defaultCollectionExists) {
                client.getDB(DEFAULT_databaseName).getCollection(DEFAULT_collectionName).drop();
            }
            client.close();
        }
    }

    @Before
    public void checkSetup() {
        assumeThat(client, is(notNullValue()));
    }

    @Override
    MongoSchemaDBStore createUniqueStore() throws IOException {
        MongoClientURI clientURI = generateURI(host, port, createUniqueName(), createUniqueName());
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI);
        clientURIs.put(store, clientURI);
        return store;
    }

    @Override
    void destroyUniqueStore(MongoSchemaDBStore store) {
        if (store != null) {
            MongoClientURI clientURI = clientURIs.get(store);
            if (clientURI != null) {
                client.dropDatabase(clientURI.getDatabase());
            }
            store.close();
        }
    }

    static MongoClientURI generateURI(String host, int port) {
        return generateURI(host, port, null, null);
    }

    static MongoClientURI generateURI(String host, int port, String database) {
        return generateURI(host, port, database, null);
    }

    static MongoClientURI generateURI(String host, int port, String database, String collection) {
        StringBuilder builder = new StringBuilder("mongodb://");
        if (host != null && !host.isEmpty()) {
            builder.append("localhost");
        } else {
            builder.append("localhost");
        }
        if (port > -1) {
            builder.append(":").append(port);
        }
        if (database != null && !database.isEmpty()) {
            builder.append("/").append(database);
        }
        if (collection != null && !collection.isEmpty()) {
            builder.append(".").append(collection);
        }
        return new MongoClientURI(builder.toString());
    }

    static String createUniqueName() {
        return Long.toHexString(UUID.randomUUID().getLeastSignificantBits());
    }

    @Test
    public void test_Constructor_String_DefaultDatabaseAndCollection()
            throws URISyntaxException, IOException {
        MongoClientURI clientURI = generateURI(host, port);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI.toString());
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(DEFAULT_databaseName)));
            assertThat(store.collection.getName(), is(equalTo(DEFAULT_collectionName)));
        } finally {
            store.close();
        }
    }

    @Test
    public void test_Constructor_String_DefaultCollection() throws URISyntaxException, IOException {
        String database = createUniqueName();
        MongoClientURI clientURI = generateURI(host, port, database);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI.toString());
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(database)));
            assertThat(store.collection.getName(), is(equalTo(DEFAULT_collectionName)));
        } finally {
            store.close();
            client.dropDatabase(database);
        }
    }

    @Test
    public void test_Constructor_String() throws URISyntaxException, IOException {
        String database = createUniqueName();
        String collection = createUniqueName();
        MongoClientURI clientURI = generateURI(host, port, database, collection);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI.toString());
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(database)));
            assertThat(store.collection.getName(), is(equalTo(collection)));
        } finally {
            store.close();
            client.dropDatabase(database);
        }
    }

    @Test
    public void test_Constructor_URI_DefaultDatabaseAndCollection()
            throws URISyntaxException, IOException {
        MongoClientURI clientURI = generateURI(host, port);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI);
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(DEFAULT_databaseName)));
            assertThat(store.collection.getName(), is(equalTo(DEFAULT_collectionName)));
        } finally {
            store.close();
        }
    }

    @Test
    public void test_Constructor_URI_DefaultCollection() throws URISyntaxException, IOException {
        String database = createUniqueName();
        MongoClientURI clientURI = generateURI(host, port, database);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI);
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(database)));
            assertThat(store.collection.getName(), is(equalTo(DEFAULT_collectionName)));
        } finally {
            store.close();
            client.dropDatabase(database);
        }
    }

    @Test
    public void test_Constructor_URI() throws URISyntaxException, IOException {
        String database = createUniqueName();
        String collection = createUniqueName();
        MongoClientURI clientURI = generateURI(host, port, database, collection);
        MongoSchemaDBStore store = new MongoSchemaDBStore(clientURI);
        try {
            assertThat(store.collection.getDB().getName(), is(equalTo(database)));
            assertThat(store.collection.getName(), is(equalTo(collection)));
        } finally {
            store.close();
            client.dropDatabase(database);
        }
    }
}
