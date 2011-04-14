/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataTestSetup extends JDBCTestSetup {

    private static boolean first = true;

    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        if (first) {
            // uncomment to turn up logging
            // java.util.logging.ConsoleHandler handler = new
            // java.util.logging.ConsoleHandler();
            // handler.setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").addHandler(handler);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").setLevel(java.util.logging.Level.FINE);
            // org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").addHandler(handler);
            first = false;
        }

        // the unit tests assume a non loose behaviour
        ((TeradataGISDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);

        // let's work with the most common schema please
        dataStore.setDatabaseSchema(fixture.getProperty("schema"));
    }

    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "com.teradata.jdbc.TeraDriver");
        fixture.put("url",
                "jdbc:teradata://localhost/DATABASE=geotools,PORT=1025,TMODE=ANSI,CHARSET=UTF8");
        fixture.put("host", "localhost");
        fixture.put("database", "geotools");
        fixture.put("schema", "geotools");
        fixture.put("port", "1025");
        fixture.put("user", "dbc");
        fixture.put("password", "dbc");

        return fixture;
    }

    @Override
    public void setUp() throws Exception {
        super.setUp();
    }

    protected void setUpData() throws Exception {

        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft1'");
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft2'");
        runSafe("DROP TRIGGER \"ft1_geometry_mi\"");
        runSafe("DROP TRIGGER \"ft1_geometry_mu\"");
        runSafe("DROP TRIGGER \"ft1_geometry_md\"");
        runSafe("DROP TABLE \"ft1_geometry_idx\"");
        runSafe("DROP TABLE \"ft1\"");
        runSafe("DROP TRIGGER \"ft2_geometry_mi\"");
        runSafe("DROP TRIGGER \"ft2_geometry_mu\"");
        runSafe("DROP TRIGGER \"ft2_geometry_md\"");
        runSafe("DROP TABLE \"ft2_geometry_idx\"");
        runSafe("DROP TABLE \"ft2\"");

        run("CREATE TABLE \"ft1\"(" //
                + "\"id\" PRIMARY KEY not null integer, " //
                + "\"geometry\" ST_GEOMETRY, " //
                + "\"intProperty\" int," //
                + "\"doubleProperty\" double precision, " //
                + "\"stringProperty\" varchar(200) casespecific)");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('"
                + fixture.getProperty("database")
                + "', '"
                + fixture.getProperty("schema")
                + "', 'ft1', 'geometry', 2, 1619, 'POINT')");
        run("CREATE MULTISET TABLE \"ft1_geometry_idx\""
                + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
        run("CREATE TRIGGER \"ft1_geometry_mi\" AFTER INSERT ON ft1"
                + "  REFERENCING NEW TABLE AS nt" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
                + "  (" + "    INSERT INTO \"ft1_geometry_idx\"" + "    SELECT" + "    id,"
                + "    sysspatial.tessellate_index ("
                + "      \"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(),"
                + "      \"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(),"
                + "      \"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(),"
                + "      \"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(),"
                + "      -180, -90, 180, 90, 100, 100, 1, 0.01, 0)"
                + "    FROM nt WHERE \"geometry\" IS NOT NULL;" + "  ) " + "END");
        // run("CREATE TRIGGER \"ft1_geometry_mi\" AFTER INSERT ON ft1"
        // + "  REFERENCING NEW AS nt"
        // + "  FOR EACH ROW"
        // + "    WHEN (nt.\"geometry\" IS NOT NULL) ( "
        // +
        // "      INSERT INTO \"ft1_geometry_idx\" (id, cellid) VALUES (nt.id, "
        // + "      sysspatial.tessellate_index ("
        // +
        // "        nt.\"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_X(),"
        // +
        // "        nt.\"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(1).ST_Y(),"
        // +
        // "        nt.\"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_X(),"
        // +
        // "        nt.\"geometry\".ST_Envelope().ST_ExteriorRing().ST_PointN(3).ST_Y(),"
        // + "        -180, -90, 180, 90, 100, 100, 1, 0.01, 0));"
        // + "  ); ");
        run("CREATE TRIGGER \"ft1_geometry_md\" AFTER DELETE ON \"ft1\""
                + "  REFERENCING OLD TABLE AS ot" + "  FOR EACH STATEMENT" + "  BEGIN ATOMIC"
                + "  (" + "    DELETE FROM \"ft1_geometry_idx\" WHERE ID IN (SELECT ID from ot);"
                + "  )" + "END");
        run("INSERT INTO \"ft1\" VALUES(0, 'POINT(0 0)', 0, 0.0, 'zero')");
        run("INSERT INTO \"ft1\" VALUES(1, 'POINT(1 1)', 1, 1.1, 'one')");
        run("INSERT INTO \"ft1\" VALUES(2, 'POINT(2 2)', 2, 2.2, 'two')");

    }

    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new TeradataDataStoreFactory();
    }
}
