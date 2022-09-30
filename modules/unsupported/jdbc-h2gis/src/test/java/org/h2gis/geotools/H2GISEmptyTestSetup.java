/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCEmptyTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISEmptyTestSetup extends JDBCEmptyTestSetup {

    protected H2GISEmptyTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
    }

    @Override
    protected void createEmptyTable() throws Exception {
        run("CREATE TABLE \"geotools\".\"empty\" (id int, geom blob)");
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'empty'");
        runSafe("DROP TABLE \"geotools\".\"empty\"");
    }
}
