/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.IOException;
import java.util.Map;
import org.geotools.jdbc.JDBCDataStoreOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Ignore;
import org.junit.Test;

/** Read-oriented JDBC datastore online tests for DuckDB. */
public class DuckDBDataStoreOnlineTest extends JDBCDataStoreOnlineTest {

    private static final String CONSTRAINTS_LENGTH_NOT_ENFORCED =
            "DuckDB does not enforce VARCHAR length constraints for createSchema constraints test";
    private static final String NATIVE_TYPENAME_CLOB_UNSUPPORTED =
            "DuckDB does not support CLOB native type name used by JDBCDataStoreOnlineTest";
    private static final String NATIVE_TYPE_GETTYPEINFO_UNSUPPORTED =
            "DuckDB JDBC DatabaseMetaData#getTypeInfo() is not implemented";
    private static final String INDEX_METADATA_INCOMPLETE =
            "DuckDB JDBC index metadata does not expose created index columns consistently";
    private static final String CRS_METADATA_NOT_PRESERVED =
            "DuckDB native geometry metadata does not preserve the expected UTM CRS round-trip in this test";

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Override
    protected Map<String, Object> createDataStoreFactoryParams() throws Exception {
        Map<String, Object> params = super.createDataStoreFactoryParams();
        params.put(AbstractDuckDBDataStoreFactory.READ_ONLY.key, Boolean.FALSE);
        return params;
    }

    @Override
    @Test
    public void testCreateSchema() throws Exception {
        super.testCreateSchema();
    }

    @Override
    @Test
    @Ignore(CONSTRAINTS_LENGTH_NOT_ENFORCED)
    public void testCreateSchemaWithConstraints() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(NATIVE_TYPENAME_CLOB_UNSUPPORTED)
    public void testCreateSchemaWithNativeTypename() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(NATIVE_TYPE_GETTYPEINFO_UNSUPPORTED)
    public void testCreateSchemaWithNativeType() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    public void testRemoveSchema() throws Exception {
        super.testRemoveSchema();
    }

    @Override
    @Test
    @Ignore(INDEX_METADATA_INCOMPLETE)
    public void testSimpleIndex() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(INDEX_METADATA_INCOMPLETE)
    public void testMultiColumnIndex() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    @Ignore(CRS_METADATA_NOT_PRESERVED)
    public void testCreateSchemaUTMCRS() throws Exception {
        // Ignored on purpose. See @Ignore reason.
    }

    @Override
    @Test
    public void testCreateSchemaFidColumn() throws Exception {
        super.testCreateSchemaFidColumn();
    }

    @Override
    @Test
    public void testGetFeatureWriter() throws IOException {
        super.testGetFeatureWriter();
    }

    @Override
    @Test
    public void testGetFeatureWriterWithFilter() throws IOException {
        super.testGetFeatureWriterWithFilter();
    }

    @Override
    @Test
    public void testGetFeatureWriterAppend() throws IOException {
        super.testGetFeatureWriterAppend();
    }
}
