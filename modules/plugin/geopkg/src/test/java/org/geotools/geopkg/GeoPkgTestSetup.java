/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.geopkg;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.geopkg.geom.GeoPkgGeomWriter;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.util.SqlUtil;
import org.locationtech.jts.geom.Geometry;

public class GeoPkgTestSetup extends JDBCTestSetup {

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new GeoPkgDataStoreFactory();
    }

    @Override
    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        super.initializeDataSource(ds, db);

        ds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected void setUpData() throws Exception {
        super.setUpData();

        removeTable("ft1");
        removeTable("ft2");
        removeTable("ft3");

        GeometryBuilder gb = new GeometryBuilder();

        // create some data
        String sql = "CREATE TABLE ft1 (id INTEGER PRIMARY KEY, geometry BLOB)";
        run(sql);

        sql = "ALTER TABLE ft1 add intProperty INTEGER";
        run(sql);

        sql = "ALTER TABLE ft1 add doubleProperty DOUBLE";
        run(sql);

        sql = "ALTER TABLE ft1 add stringProperty VARCHAR(255)";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (" + "0,X'" + toString(gb.point(0, 0)) + "', 0, 0.0,'zero');";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (" + "1,X'" + toString(gb.point(1, 1)) + "', 1, 1.1,'one');";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (" + "2,X'" + toString(gb.point(2, 2)) + "', 2, 2.2,'two');";
        run(sql);

        sql = "INSERT INTO gpkg_geometry_columns VALUES ('ft1', 'geometry', 'POINT', 4326, 0, 0)";
        run(sql);

        sql =
                "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES "
                        + "('ft1', 'features', 'ft1', 4326)";
        run(sql);
    }

    /** @param tableName */
    void removeTable(String tableName) {
        // drop old data
        runSafe("DROP TABLE IF EXISTS " + tableName);
        runSafe("DROP VIEW  IF EXISTS " + tableName);
        runSafe("DELETE FROM gpkg_geometry_columns where table_name ='" + tableName + "'");
        runSafe("DELETE FROM gpkg_contents where table_name ='" + tableName + "'");
        runSafe("DELETE FROM gpkg_extensions where table_name ='" + tableName + "'");
        runSafe("DELETE FROM gt_pk_metadata where table_name ='" + tableName + "'");
    }

    String toString(Geometry g) throws IOException {
        byte[] bytes = new GeoPkgGeomWriter().write(g);
        return toHexString(bytes);
    }

    public static String toHexString(byte[] bytes) {
        final char[] hexArray = {
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'
        };
        char[] hexChars = new char[bytes.length * 2];
        int v;
        for (int j = 0; j < bytes.length; j++) {
            v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.sqlite.JDBC");
        fixture.put("url", "jdbc:sqlite:target/geotools");
        return fixture;
    }

    protected void createSpatialIndex(
            String tableName, String geometryColumn, String primaryKeyColumn)
            throws SQLException, IOException {
        Map<String, String> properties = new HashMap<>();
        properties.put("t", tableName);
        properties.put("c", geometryColumn);
        properties.put("i", primaryKeyColumn);

        try (InputStream is =
                        GeoPackage.class.getResourceAsStream(GeoPackage.SPATIAL_INDEX + ".sql");
                Connection cx = getConnection()) {
            SqlUtil.runScript(is, cx, properties);
        }
    }
}
