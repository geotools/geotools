package org.h2gis.geotools;

import org.geotools.jdbc.JDBCGeometryTestSetup;

public class H2GISGeometryTestSetup extends JDBCGeometryTestSetup {

    protected H2GISGeometryTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void dropSpatialTable(String tableName) {
        runSafe("DROP TABLE IF EXISTS \"geotools\".\"" + tableName + "\"");
    }
}
