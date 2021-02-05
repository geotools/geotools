package org.geotools.data.h2;

import org.geotools.jdbc.JDBCGeometryTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GeometryTestSetup extends JDBCGeometryTestSetup {

    protected H2GeometryTestSetup() {
        super(new H2TestSetup());
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("CALL DropGeometryColumns('geotools', '" + tableName + "'");
    }
}
