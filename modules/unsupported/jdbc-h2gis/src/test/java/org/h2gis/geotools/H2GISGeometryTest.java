package org.h2gis.geotools;

import org.geotools.jdbc.JDBCGeometryOnlineTest;
import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.junit.jupiter.api.Test;

public class H2GISGeometryTest extends JDBCGeometryOnlineTest {

    @Override
    protected JDBCGeometryTestSetup createTestSetup() {
        return new H2GISGeometryTestSetup();
    }

    @Test
    @Override
    public void testLinearRing() {
        // linear ring type is not a supported type in postgis
    }
}
