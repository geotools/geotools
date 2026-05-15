/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Properties;
import javax.sql.DataSource;
import org.geotools.data.duckdb.datasource.DuckdbDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.locationtech.jts.geom.Geometry;
import org.locationtech.jts.io.ParseException;
import org.locationtech.jts.io.WKTReader;

/** JDBC online test setup for DuckDB. */
public class DuckDBTestSetup extends JDBCTestSetup {

    @Override
    protected boolean useDelegateDataSource() {
        // Ensure JDBCDelegatingTestSetup-based suites reuse this DuckDB-specific datasource
        // (guarded init SQL + pooled duplication), avoiding mixed connection configurations.
        return true;
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new DuckDBJDBCDataStoreFactory();
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected void setUpData() throws Exception {
        removeTable("ft1");
        removeTable("ft2");
        removeTable("ft3");
        removeTable("ft4");

        run("CREATE TABLE \"ft1\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"geometry\" GEOMETRY, "
                + "\"intProperty\" INTEGER, "
                + "\"doubleProperty\" DOUBLE, "
                + "\"stringProperty\" VARCHAR"
                + ")");
        run("INSERT INTO \"ft1\" VALUES (0, " + geometryLiteral("POINT(0 0)") + ", 0, 0.0, 'zero')");
        run("INSERT INTO \"ft1\" VALUES (1, " + geometryLiteral("POINT(1 1)") + ", 1, 1.1, 'one')");
        run("INSERT INTO \"ft1\" VALUES (2, " + geometryLiteral("POINT(2 2)") + ", 2, 2.2, 'two')");

        run("CREATE TABLE \"ft3\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"gEoMeTrY\" GEOMETRY, "
                + "\"intProperty\" INTEGER, "
                + "\"doubleProperty\" DOUBLE, "
                + "\"stringProperty\" VARCHAR"
                + ")");
        run("INSERT INTO \"ft3\" VALUES (0, " + geometryLiteral("POINT(0 0)") + ", 0, 0.0, 'zero')");

        run("CREATE TABLE \"ft4\" ("
                + "\"id\" INTEGER PRIMARY KEY, "
                + "\"geometry\" GEOMETRY, "
                + "\"intProperty\" INTEGER, "
                + "\"doubleProperty\" DOUBLE, "
                + "\"stringProperty\" VARCHAR"
                + ")");
        run("INSERT INTO \"ft4\" VALUES (0, " + geometryLiteral("POINT(0 0)") + ", 0, 0.0, 'zero')");
        run("INSERT INTO \"ft4\" VALUES (1, " + geometryLiteral("POINT(1 1)") + ", 1, 1.1, 'one')");
        run("INSERT INTO \"ft4\" VALUES (2, " + geometryLiteral("POINT(2 2)") + ", 1, 1.1, 'one_2')");
        run("INSERT INTO \"ft4\" VALUES (3, " + geometryLiteral("POINT(3 3)") + ", 1, 1.1, 'one_2')");
        run("INSERT INTO \"ft4\" VALUES (4, " + geometryLiteral("POINT(4 4)") + ", 2, 2.2, 'two')");
        run("INSERT INTO \"ft4\" VALUES (5, " + geometryLiteral("POINT(5 5)") + ", 2, 2.2, 'two_2')");
        run("INSERT INTO \"ft4\" VALUES (6, " + geometryLiteral("POINT(6 6)") + ", 3, 3.3, 'three')");
    }

    protected void removeTable(String tableName) {
        runSafe("DROP VIEW IF EXISTS \"" + tableName + "\"");
        runSafe("DROP TABLE IF EXISTS \"" + tableName + "\"");
    }

    @Override
    protected Properties createOfflineFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "org.duckdb.DuckDBDriver");
        fixture.put("url", "jdbc:duckdb:target/duckdb/geotools.duckdb");
        fixture.put("database", "target/duckdb/geotools.duckdb");
        return fixture;
    }

    @Override
    protected Properties createExampleFixture() {
        return createOfflineFixture();
    }

    protected String geometryLiteral(String wkt) throws IOException, ParseException {
        Geometry geometry = new WKTReader().read(wkt);
        geometry.setSRID(4326);
        StringBuffer sql = new StringBuffer("ST_GeomFromHEXEWKB('");
        HexWKBEncoder.encode(geometry, sql);
        sql.append("')");
        return sql.toString();
    }

    @Override
    protected DataSource createDataSource() throws IOException {
        Properties db = fixture;
        ensureDatabaseDirectory(db);

        DuckdbDataSource dataSource = new DuckdbDataSource(List.of("install spatial", "load spatial"));
        dataSource.setDriverClassName(db.getProperty("driver"));
        dataSource.setUrl(db.getProperty("url"));

        if (db.containsKey("user")) {
            dataSource.setUsername(db.getProperty("user"));
        } else if (db.containsKey("username")) {
            dataSource.setUsername(db.getProperty("username"));
        }
        if (db.containsKey("password")) {
            dataSource.setPassword(db.getProperty("password"));
        }

        dataSource.setPoolPreparedStatements(true);
        dataSource.setAccessToUnderlyingConnectionAllowed(true);
        dataSource.setMinIdle(1);
        dataSource.setMaxActive(4);
        dataSource.setMaxWait(5000);
        dataSource.setValidationQuery("SELECT 1");
        dataSource.setTestOnBorrow(true);

        return dataSource;
    }

    private void ensureDatabaseDirectory(Properties db) {
        String database = db.getProperty("database");
        if (database != null) {
            File file = new File(database);
            File parent = file.getAbsoluteFile().getParentFile();
            if (parent != null) {
                parent.mkdirs();
            }
        }
    }

    @Override
    protected Connection getConnection() throws SQLException, IOException {
        return super.getConnection();
    }
}
