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

import java.io.IOException;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Data store test for mysql.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class SingleStoreDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    public SingleStoreDataStoreOnlineTest() {
        super(1e-6);
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SingleStoreTestSetup();
    }

    @Override
    protected String getCLOBTypeName() {
        // CLOB is supported in SingleStore 8 but not in 5
        return "TEXT";
    }

    // Do not preform create tests
    @Override
    @Ignore
    @Test
    public void testCreateSchemaWithConstraints() throws Exception {}

    @Override
    @Ignore
    @Test
    public void testCreateSchema() throws Exception {}

    @Override
    @Ignore
    @Test
    public void testGetFeatureWriterAppend() {}

    @Override
    @Ignore
    @Test
    public void testGetFeatureWriterWithFilter() {}

    @Ignore
    @Override
    @Test
    public void testCreateSchemaFidColumn() throws Exception {
        super.testCreateSchemaFidColumn();
    }

    @Ignore
    @Override
    @Test
    public void testCreateSchemaUTMCRS() throws Exception {
        super.testCreateSchemaUTMCRS();
    }

    @Ignore
    @Override
    @Test
    public void testGetFeatureWriter() throws IOException {
        super.testGetFeatureWriter();
    }

    @Ignore
    @Override
    @Test
    public void testCreateSchemaWithNativeType() throws Exception {}

    @Ignore
    @Override
    @Test
    public void testCreateSchemaWithNativeTypename() throws Exception {}
}
