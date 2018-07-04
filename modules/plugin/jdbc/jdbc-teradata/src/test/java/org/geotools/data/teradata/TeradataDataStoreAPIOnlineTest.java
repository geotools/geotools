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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;

/** @source $URL$ */
public class TeradataDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {

    @Override
    protected void connect() throws Exception {
        super.connect();
        dataStore.setDatabaseSchema(fixture.getProperty("user"));
    }

    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new TeradataDataStoreAPITestSetup(new TeradataTestSetup());
    }

    public void testGetFeatureWriterConcurrency() throws Exception {
        // Teradata will lock indefinitely, won't throw an exception
    }

    public void testTransactionIsolation() throws Exception {
        // TODO implement writing
    }
}
