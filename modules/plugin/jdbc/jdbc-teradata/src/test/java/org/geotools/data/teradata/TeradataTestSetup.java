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

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataTestSetup extends JDBCTestSetup {

    protected int srid4326 = 1619;

    public int getSrid4326() {
        return srid4326;
    }
    @Override
    protected void initializeDatabase() throws Exception {
        super.initializeDatabase();
        
        //figure out the 4326 native srid
        Connection cx = getConnection();
        try {
            Statement st = cx.createStatement();
            try {
                ResultSet rs = st.executeQuery("SELECT srid FROM sysspatial.spatial_ref_sys " 
                    + "WHERE auth_srid = 4326");
                rs.next();
                srid4326 = rs.getInt(1);
                rs.close();
            }
            finally {
                st.close();
            }
        }
        finally {
            cx.close();
        }
    }
    
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        // the unit tests assume a non loose behaviour
        ((TeradataDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);

        // let's work with the most common schema please
        if (dataStore.getDatabaseSchema() == null) {
            dataStore.setDatabaseSchema(fixture.getProperty("schema"));
        }
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
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('"
                + fixture.getProperty("database") + "', '" + fixture.getProperty("schema")
                + "', 'ft1', 'geometry', 2, " + srid4326 + ", 'POINT')");
        run("CREATE MULTISET TABLE \"ft1_geometry_idx\""
                + " (id INTEGER NOT NULL, cellid INTEGER NOT NULL) PRIMARY INDEX (cellid)");
        
        run("INSERT INTO \"ft1\" VALUES(0, 'POINT(0 0)', 0, 0.0, 'zero')");
        run("INSERT INTO \"ft1\" VALUES(1, 'POINT(1 1)', 1, 1.1, 'one')");
        run("INSERT INTO \"ft1\" VALUES(2, 'POINT(2 2)', 2, 2.2, 'two')");

    }

    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new TeradataDataStoreFactory();
    }
}
