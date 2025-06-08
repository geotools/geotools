/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.hana;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import tech.units.indriya.function.MultiplyConverter;

/** @author Stefan Uhrig, SAP SE */
public class HanaTestSetupBase extends JDBCTestSetup {

    private static final String DRIVER_CLASS_NAME = "com.sap.db.jdbc.Driver";

    private static AtomicInteger schemaCounter = new AtomicInteger();

    private static Object workaroundLock = new Object();

    private static boolean workaroundsApplied = false;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        synchronized (workaroundLock) {
            if (!workaroundsApplied) {
                // Workaround for issue https://github.com/unitsofmeasurement/indriya/issues/371
                MultiplyConverter.ofPiExponent(1).getValue();
                workaroundsApplied = true;
            }
        }
    }

    @Override
    public boolean canResetSchema() {
        return false;
    }

    @Override
    protected boolean useDelegateDataSource() {
        return true;
    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("host", "localhost");
        fixture.put("port", "30015");
        fixture.put("user", "myuser");
        fixture.put("password", "mypassword");
        fixture.put("use ssl", "false");
        return fixture;
    }

    @Override
    public void setFixture(Properties fixture) {
        fixture.setProperty("driver", DRIVER_CLASS_NAME);
        String host = fixture.getProperty("host");
        String sport = fixture.getProperty("port");
        String sinstance = fixture.getProperty("instance");
        String database = fixture.getProperty("database");
        String useSsl = fixture.getProperty("use ssl");

        if (fixture.getProperty("schemabase") != null && fixture.getProperty("schema") == null) {
            String schemaBase = fixture.getProperty("schemabase");
            int counter = schemaCounter.getAndIncrement();
            String schema = schemaBase + "_" + counter;
            fixture.setProperty("schema", schema);
        }

        HashMap<String, String> options = new HashMap<>();
        if ("true".equals(useSsl)) {
            options.put("encrypt", "true");
        }

        int port = 0;
        if (sport != null && !sport.isEmpty()) {
            port = Integer.parseInt(sport);
        }
        if (port != 0) {
            fixture.setProperty(
                    "url", HanaConnectionParameters.forPort(host, port, options).buildUrl());
            super.setFixture(fixture);
            return;
        }

        int instance = Integer.parseInt(sinstance);
        if (database == null || database.isEmpty()) {
            fixture.setProperty(
                    "url",
                    HanaConnectionParameters.forSingleContainer(host, instance, options)
                            .buildUrl());
            super.setFixture(fixture);
            return;
        }

        fixture.setProperty(
                "url",
                HanaConnectionParameters.forMultiContainer(host, instance, database, options)
                        .buildUrl());
        super.setFixture(fixture);
    }

    protected void setCommonDataSourceOptions(BasicDataSource dataSource) {
        dataSource.setDriverClassName(fixture.getProperty("driver"));
        dataSource.setUrl(fixture.getProperty("url"));

        if (fixture.containsKey("user")) {
            dataSource.setUsername(fixture.getProperty("user"));
        } else if (fixture.containsKey("username")) {
            dataSource.setUsername(fixture.getProperty("username"));
        }
        if (fixture.containsKey("password")) {
            dataSource.setPassword(fixture.getProperty("password"));
        }
    }

    @Override
    public void tearDown() throws Exception {}

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new HanaDataStoreFactory();
    }
}
