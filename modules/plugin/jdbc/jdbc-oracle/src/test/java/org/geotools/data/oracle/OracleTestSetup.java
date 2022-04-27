/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.oracle;

import java.util.Properties;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class OracleTestSetup extends JDBCTestSetup {

    @Override
    protected String typeName(String raw) {
        return raw.toUpperCase();
    }

    @Override
    public boolean canResetSchema() {
        return false;
    }

    @Override
    protected String attributeName(String raw) {
        return raw.toUpperCase();
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new OracleNGDataStoreFactory();
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        // tests do assume the dialect is working in non loose mode
        ((OracleDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);
        ((OracleDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);
        dataStore.setDatabaseSchema(fixture.getProperty("schema").toUpperCase());
    }

    @Override
    @SuppressWarnings("PMD.AvoidUsingHardCodedIP")
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "oracle.jdbc.OracleDriver");
        fixture.put("url", "jdbc:oracle:thin:@127.0.0.1:1521:XE");
        fixture.put("host", "127.0.0.1");
        fixture.put("port", "1521");
        fixture.put("database", "XE");
        fixture.put("username", "GEOTOOLS");
        fixture.put("user", "GEOTOOLS");
        fixture.put("schema", "GEOTOOLS");
        fixture.put("password", "geotools");
        fixture.put("dbtype", "Oracle");
        return fixture;
    }

    @Override
    protected void setUpData() throws Exception {
        // drop old data
        try {
            // left here so that dbs with old tests can run the test safely, we previously had
            // this trigger
            run("DROP TRIGGER ft1_pkey_trigger");
        } catch (Exception e) {
        }

        try {
            run("DROP SEQUENCE ft1_pkey_seq");
        } catch (Exception e) {
        }
        try {
            run("DROP SEQUENCE ft3_pkey_seq");
        } catch (Exception e) {
        }

        try {
            run("DROP SEQUENCE ft4_pkey_seq");
        } catch (Exception e) {
        }

        deleteSpatialTable("FT1");
        deleteSpatialTable("FT2");
        deleteSpatialTable("FT3");
        deleteSpatialTable("FT4");

        String sql =
                "CREATE TABLE ft1 ("
                        + "id INT, geometry MDSYS.SDO_GEOMETRY, intProperty INT, "
                        + "doubleProperty FLOAT, stringProperty VARCHAR(255)"
                        + ", PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE ft1_pkey_seq";
        run(sql);

        sql =
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                        + "VALUES ('ft1','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);

        sql =
                "CREATE INDEX FT1_GEOMETRY_IDX ON FT1(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (0," + pointSql(4326, 0, 0) + ", 0, 0.0,'zero')";
        run(sql);
        sql = "INSERT INTO ft1 VALUES (1," + pointSql(4326, 1, 1) + ", 1, 1.1,'one')";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (2," + pointSql(4326, 2, 2) + ", 2, 2.2,'two')";
        run(sql);
        // Create a table with a reserved names as column names
        sql =
                "CREATE TABLE ft3 ("
                        + "id INT, geometry MDSYS.SDO_GEOMETRY, \"DATE\" INT, "
                        + "\"NUMBER\" FLOAT, \"LEVEL\" VARCHAR(255)"
                        + ", PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE ft3_pkey_seq";
        run(sql);

        sql =
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                        + "VALUES ('ft3','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);

        sql =
                "CREATE INDEX FT3_GEOMETRY_IDX ON FT3(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);

        sql = "INSERT INTO ft3 VALUES (0," + pointSql(4326, 0, 0) + ", 0, 0.0,'zero')";
        run(sql);
        sql = "INSERT INTO ft3 VALUES (1," + pointSql(4326, 1, 1) + ", 1, 1.1,'one')";
        run(sql);

        sql = "INSERT INTO ft3 VALUES (2," + pointSql(4326, 2, 2) + ", 2, 2.2,'two')";
        run(sql);

        runft4();
    }

    private void runft4() throws Exception {
        String sql =
                "CREATE TABLE ft4 ("
                        + "id INT, geometry MDSYS.SDO_GEOMETRY, intProperty INT, "
                        + "doubleProperty FLOAT, stringProperty VARCHAR(255)"
                        + ", PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE ft4_pkey_seq";
        run(sql);

        sql =
                "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) "
                        + "VALUES ('ft4','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), "
                        + "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);

        sql =
                "CREATE INDEX ft4_GEOMETRY_IDX ON FT4(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (0," + pointSql(4326, 0, 0) + ", 0, 0.0,'zero')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (1," + pointSql(4326, 1, 1) + ", 1, 1.1,'one')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (2," + pointSql(4326, 2, 2) + ", 1, 1.1,'one_2')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (3," + pointSql(4326, 3, 3) + ", 1, 1.1,'one_2')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (4," + pointSql(4326, 4, 4) + ", 2, 2.2,'two')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (5," + pointSql(4326, 5, 5) + ", 2, 2.2,'two_2')";
        run(sql);

        sql = "INSERT INTO ft4 VALUES (6," + pointSql(4326, 6, 6) + ", 3, 3.3,'three')";
        run(sql);
    }

    protected void deleteSpatialTable(String name) throws Exception {
        try {
            run("DROP TABLE " + name + " purge");
        } catch (Exception e) {
        }

        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = '" + name + "'");
    }

    protected String pointSql(int srid, double x, double y) {
        return "MDSYS.SDO_GEOMETRY(2001,"
                + srid
                + ",SDO_POINT_TYPE("
                + x
                + ","
                + y
                + ",NULL),NULL,NULL)";
    }
}
