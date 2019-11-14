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
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

/** @author Stefan Uhrig, SAP SE */
public class HanaTestSetupBase extends JDBCTestSetup {

    private static final String DRIVER_CLASS_NAME = "com.sap.db.jdbc.Driver";

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

        HashMap<String, String> options = new HashMap<String, String>();
        if ("true".equals(useSsl)) {
            options.put("encrypt", "true");
        }

        int port = 0;
        if ((sport != null) && !sport.isEmpty()) {
            port = Integer.parseInt(sport);
        }
        if (port != 0) {
            fixture.setProperty(
                    "url", HanaConnectionParameters.forPort(host, port, options).buildUrl());
            super.setFixture(fixture);
            return;
        }

        int instance = Integer.parseInt(sinstance);
        if ((database == null) || database.isEmpty()) {
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

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new HanaDataStoreFactory();
    }
}
