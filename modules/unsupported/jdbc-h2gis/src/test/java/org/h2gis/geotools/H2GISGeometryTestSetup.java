package org.h2gis.geotools;

import org.geotools.jdbc.JDBCGeometryTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISGeometryTestSetup extends JDBCGeometryTestSetup {

    protected H2GISGeometryTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void dropSpatialTable(String tableName) {
        runSafe("DROP TABLE IF EXISTS \"geotools\".\"" + tableName + "\"");
    }
}
