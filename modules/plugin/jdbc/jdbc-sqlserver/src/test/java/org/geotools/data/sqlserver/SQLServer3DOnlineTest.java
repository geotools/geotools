package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBC3DOnlineTest;
import org.geotools.jdbc.JDBC3DTestSetup;

public class SQLServer3DOnlineTest extends JDBC3DOnlineTest {

    @Override
    protected void connect() throws Exception {
        super.connect();
        SQLServerDialect ssd = (SQLServerDialect) dialect;
        ssd.setGeometryMetadataTable("GEOMETRY_COLUMNS_3D_TEST");
        ssd.setUseNativeSerialization(true);
    }

    @Override
    protected JDBC3DTestSetup createTestSetup() {
        return new SQLServer3DTestSetup();
    }
}
