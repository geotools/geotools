/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.junit.Ignore;
import org.junit.Test;

public class SingleStoreDataStoreAPIOnlineTest extends JDBCDataStoreAPIOnlineTest {
    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new SingleStoreDataStoreAPITestSetup();
    }

    @Override
    @Ignore
    @Test
    public void testGetFeatureWriterConcurrency() throws Exception {
        // Times out because the table is locked (writing not supported in SingleStore yet)
    }

    @Override
    @Ignore
    @Test
    public void testCreateSchema() throws Exception {
        // not supporting table creation for the moment
    }
}
