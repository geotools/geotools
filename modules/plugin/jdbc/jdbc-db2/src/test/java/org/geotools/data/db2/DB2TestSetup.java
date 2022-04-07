/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General  License for more details.
 */
package org.geotools.data.db2;

import java.sql.Connection;
import java.util.Properties;
import java.util.logging.Logger;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.util.logging.Logging;

/**
 * Test harness for db2.
 *
 * @author Christian Mueller
 */
@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class DB2TestSetup extends JDBCTestSetup {

    protected static final Logger LOGGER = Logging.getLogger(DB2TestSetup.class);

    //    protected SQLDialect createSQLDialect(JDBCDataStore dataStore) {
    //        return new DB2SQLDialect(dataStore);
    //    }

    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "com.ibm.db2.jcc.DB2Driver");
        fixture.put("url", "jdbc:db2://localhost:50001/geotools");
        fixture.put("user", "db2inst1");
        fixture.put("passwd", "db2inst1");
        fixture.put("database", "geotools");
        fixture.put("port", "50001");
        fixture.put("host", "localhost");

        return fixture;
    }

    @Override
    protected void setUpData() throws Exception {

        try (Connection con = getDataSource().getConnection()) {

            // drop old data

            DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "ft1", "geometry", con);
            DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "ft1", con);
            DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "ft2", "geometry", con);
            DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "ft2", con);

            DB2Util.executeUnRegister(DB2TestUtil.SCHEMA, "ft4", "geometry", con);
            DB2TestUtil.dropTable(DB2TestUtil.SCHEMA, "ft4", con);

            // create some data
            StringBuffer sb = new StringBuffer();
            sb.append("CREATE TABLE " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft1\" ")
                    .append("(\"id\" int  PRIMARY KEY not null , ")
                    .append("\"geometry\" db2gse.ST_POINT, \"intProperty\" int, ")
                    .append("\"doubleProperty\" double, \"stringProperty\" varchar(255))");
            con.prepareStatement(sb.toString()).execute();

            DB2Util.executeRegister(
                    DB2TestUtil.SCHEMA, "ft1", "geometry", DB2TestUtil.SRSNAME, con);

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft1\" VALUES (")
                    .append(
                            "0,db2gse.st_PointFromText('POINT(0 0)',"
                                    + DB2TestUtil.SRID
                                    + "), 0, 0.0,'zero')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft1\" VALUES (")
                    .append(
                            "1,db2gse.st_PointFromText('POINT(1 1)',"
                                    + DB2TestUtil.SRID
                                    + "), 1, 1.1,'one')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft1\" VALUES (")
                    .append(
                            "2,db2gse.st_PointFromText('POINT(2 2)',"
                                    + DB2TestUtil.SRID
                                    + "), 2, 2.2,'two')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("CREATE TABLE " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" ")
                    .append("(\"id\" int  PRIMARY KEY not null , ")
                    .append("\"geometry\" db2gse.ST_POINT, \"intProperty\" int, ")
                    .append("\"doubleProperty\" double, \"stringProperty\" varchar(255))");
            con.prepareStatement(sb.toString()).execute();

            DB2Util.executeRegister(
                    DB2TestUtil.SCHEMA, "ft4", "geometry", DB2TestUtil.SRSNAME, con);

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "0,db2gse.st_PointFromText('POINT(0 0)',"
                                    + DB2TestUtil.SRID
                                    + "), 0, 0.0,'zero')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "1,db2gse.st_PointFromText('POINT(1 1)',"
                                    + DB2TestUtil.SRID
                                    + "), 1, 1.1,'one')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "2,db2gse.st_PointFromText('POINT(2 2)',"
                                    + DB2TestUtil.SRID
                                    + "), 1, 1.1,'one_2')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "3,db2gse.st_PointFromText('POINT(3 3)',"
                                    + DB2TestUtil.SRID
                                    + "), 1, 1.1,'one_2')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "4,db2gse.st_PointFromText('POINT(4 4)',"
                                    + DB2TestUtil.SRID
                                    + "), 2, 2.2,'two')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "5,db2gse.st_PointFromText('POINT(5 5)',"
                                    + DB2TestUtil.SRID
                                    + "), 2, 2.2,'two_2')");
            con.prepareStatement(sb.toString()).execute();

            sb = new StringBuffer();
            sb.append("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"ft4\" VALUES (")
                    .append(
                            "6,db2gse.st_PointFromText('POINT(6 6)',"
                                    + DB2TestUtil.SRID
                                    + "), 3, 3.3,'three')");
            con.prepareStatement(sb.toString()).execute();
        }
    }

    @Override
    protected void initializeDatabase() throws Exception {
        super.initializeDatabase();
        try (Connection con = getDataSource().getConnection()) {
            DB2TestUtil.enableDB(con);
        }
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new DB2NGDataStoreFactory();
    }
}
