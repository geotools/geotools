/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.dggs.clickhouse;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.DefaultRepository;
import org.geotools.dggs.DGGSFactoryFinder;
import org.geotools.dggs.datastore.DGGSDataStore;
import org.geotools.dggs.datastore.DGGSStoreFactory;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.test.OnlineTestCase;
import org.geotools.util.logging.Logging;

public abstract class ClickHouseOnlineTestCase<I> extends OnlineTestCase {

    static final Logger LOGGER = Logging.getLogger(ClickHouseOnlineTestCase.class);

    protected DGGSDataStore<I> dataStore;

    @Override
    protected void connect() throws Exception {
        this.dataStore = getDataStore();
        setupTestData(dataStore);
    }

    protected DefaultRepository buildRepository(Properties props) throws Exception {
        Map<String, Object> ch = new HashMap<>();

        String db = props.getProperty("database", "default");
        String baseUrl = props.getProperty("url");
        String host = props.getProperty("host", "localhost");
        String port = props.getProperty("port", "8123");
        String user = props.getProperty("user", "default");
        String passwd = props.getProperty("password", "pwd");
        if (baseUrl == null) {
            baseUrl = "jdbc:clickhouse://" + host + ":" + port + "/" + db;
        }
        ch.put("dbtype", props.getProperty("dbtype", "clickhouse")); // your JDBC plugin’s type
        ch.put("host", host);
        ch.put("port", Integer.parseInt(port));
        ch.put("database", db);
        ch.put("user", user);
        ch.put("passwd", passwd);

        try (java.sql.Connection c = java.sql.DriverManager.getConnection(baseUrl, user, passwd)) {
            try (java.sql.Statement st = c.createStatement()) {
                st.execute("CREATE DATABASE IF NOT EXISTS " + db);
            }
        }

        ClickHouseJDBCDataStoreFactory factory = new ClickHouseJDBCDataStoreFactory();
        JDBCDataStore clickHouseStore = factory.createDataStore(ch);
        if (clickHouseStore == null) {
            throw new IllegalStateException("Could not create ClickHouse DataStore from properties: " + ch);
        }

        DefaultRepository repo = new DefaultRepository();
        repo.register("test", clickHouseStore);
        return repo;
    }

    public static Map<String, Object> toParamMap(Properties props) {
        Map<String, Object> m = new HashMap<>();
        for (String k : props.stringPropertyNames()) {
            m.put(k, props.getProperty(k));
        }
        return m;
    }

    /** Subclasses should override this to create the test data. */
    protected abstract void setupTestData(DGGSDataStore<I> dataStore) throws Exception;

    @SuppressWarnings("unchecked")
    protected DGGSDataStore<I> getDataStore() throws Exception {
        String dggsId = getDGGSId();
        if (DGGSFactoryFinder.getFactory(dggsId).isEmpty()) {
            throw new Exception(dggsId + " is not present, skipping the test");
        }

        DGGSStoreFactory factory = new DGGSStoreFactory();
        Map<String, Object> params = toParamMap(fixture);
        params.put(DGGSStoreFactory.STORE_NAME.key, "test");
        // Use the exact Param key your DGGSStoreFactory declares, e.g. "repository"
        params.put(DGGSStoreFactory.REPOSITORY.key, buildRepository(fixture));
        params.put(DGGSStoreFactory.ZONE_ID_COLUMN_NAME.key, "zoneId");

        return (DGGSDataStore<I>) factory.createDataStore(params);
    }

    @Override
    protected boolean isOnline() {
        String dggsId = getDGGSId();
        boolean present = DGGSFactoryFinder.getFactory(dggsId).isPresent();
        if (!present) {
            LOGGER.log(Level.WARNING, dggsId + " is not present, skipping the test");
        }
        return present;
    }

    /**
     * Allows test to create a sample fixture for users.
     *
     * <p>If this method returns a value the first time a fixture is looked up and not found this method will be called
     * to create a fixture file with teh same id, but suffixed with .template.
     */
    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", ClickHouseJDBCDataStoreFactory.DRIVER_CLASSNAME);
        fixture.put("url", "jdbc:clickhouse://localhost:8123/test");
        fixture.put("host", "localhost");
        fixture.put("database", "default");
        fixture.put("port", "8123");
        fixture.put("user", "default");
        fixture.put("password", "");
        fixture.put(DGGSStoreFactory.DGGS_FACTORY_ID.key, getDGGSId());
        return fixture;
    }

    protected abstract String getDGGSId();

    @Override
    protected String getFixtureId() {
        return "clickhouse-dggs-" + getDGGSId();
    }
}
