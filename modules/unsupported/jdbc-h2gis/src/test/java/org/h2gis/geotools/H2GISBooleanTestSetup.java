/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCBooleanTestSetup;
import org.geotools.jdbc.JDBCDataStore;

@SuppressWarnings("PMD.UnitTestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISBooleanTestSetup extends JDBCBooleanTestSetup {

    protected H2GISBooleanTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected void createBooleanTable() throws Exception {
        run("CREATE TABLE \"b\" (fid int AUTO_INCREMENT(0) PRIMARY KEY, \"id\" int, \"boolProperty\" boolean)");
        run("INSERT INTO \"b\" (\"id\",\"boolProperty\") VALUES (0, false)");
        run("INSERT INTO \"b\" (\"id\",\"boolProperty\") VALUES (1, true)");
    }

    @Override
    protected void dropBooleanTable() throws Exception {
        run("DROP TABLE \"b\"");
    }
}
