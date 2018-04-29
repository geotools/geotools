package org.geotools.data.h2;

import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;

public class H2GeometryTest extends JDBCGeometryOnlineTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new H2GeometryTestSetup();
    }

    @Override
    public void testLinearRing() throws Exception {
        // linear ring type is not a supported type in postgis
    }
}
