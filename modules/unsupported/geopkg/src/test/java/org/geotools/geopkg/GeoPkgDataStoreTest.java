package org.geotools.geopkg;

import org.geotools.jdbc.JDBCDataStoreTest;

public class GeoPkgDataStoreTest extends JDBCDataStoreTest {

    @Override
    protected GeoPkgTestSetup createTestSetup() {
        return new GeoPkgTestSetup();
    }

    // following tests disabled, see SpatiaLiteDataStoreTest for details
    @Override
    public void testCreateSchema() throws Exception {
    }

    @Override
    public void testCreateSchemaWithConstraints() throws Exception {
    }

}
