/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import java.util.Properties;
import org.apache.commons.dbcp.BasicDataSource;
import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class InformixTestSetup extends JDBCTestSetup {

    @Override
    protected void initializeDataSource(BasicDataSource ds, Properties db) {
        super.initializeDataSource(ds, db);

        // ds.setDefaultTransactionIsolation(Connection.TRANSACTION_READ_COMMITTED);
    }

    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new InformixDataStoreFactory();
    }

    @Override
    protected void setUpData() throws Exception {
        // allow time parsing in str_to_date
        //        run("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'NO_ZERO_IN_DATE',''));");
        //        run("SET GLOBAL sql_mode=(SELECT REPLACE(@@sql_mode,'NO_ZERO_DATE',''));");

        try {
            run(
                    "insert into spatial_references "
                            + "(srid, description, auth_name, auth_srid, falsex, falsey, xyunits, falsez, zunits, falsem, munits, "
                            + "srtext) values "
                            + "(26713, 'NAD27 / UTM zone 13N (for GeoTools tests)', 'EPSG', 26713, 0, 0, 1000, 0, 1000, 0, 1000, "
                            + "'PROJCS')");
        } catch (Exception e) {
            //            e.printStackTrace();
        }

        // drop old data
        try {
            run("DROP TABLE ft1;");
        } catch (Exception e) {
            //            e.printStackTrace();
        }

        try {
            run("DROP TABLE ft2;");
        } catch (Exception e) {
            //            e.printStackTrace();
        }

        try {
            run("DROP TABLE ft4;");
        } catch (Exception e) {
            // ignore
        }
        runSafe("DELETE FROM geometry_columns");

        // create some data
        StringBuffer sb = new StringBuffer();
        // JD: COLLATE latin1_general_cs is neccesary to ensure case-sensitive string comparisons
        sb.append("CREATE TABLE ft1 ")
                .append("(id integer PRIMARY KEY , ")
                .append("geometry ST_Point, intProperty integer, ")
                .append("doubleProperty double precision, stringProperty varchar(255));");
        run(sb.toString());

        run("create index ft1_geom_idx on ft1(geometry ST_Geometry_ops) using rtree;");

        // setup so that we can start counting from 0, otherwise 0 is treated as a special value
        //        run("SET sql_mode='NO_AUTO_VALUE_ON_ZERO';");

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (")
                .append("0,ST_GeomFromText('POINT(0 0)',4326)::ST_Point, 0, 0.0,'zero');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (")
                .append("1,ST_GeomFromText('POINT(1 1)',4326)::ST_Point, 1, 1.1,'one');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft1 VALUES (")
                .append("2,ST_GeomFromText('POINT(2 2)',4326)::ST_Point, 2, 2.2,'two');");
        run(sb.toString());
        // ft4
        runft4();
    }

    private void runft4() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ft4 ")
                .append("(id integer PRIMARY KEY , ")
                .append("geometry ST_Point, intProperty integer, ")
                .append("doubleProperty double precision, stringProperty varchar(255));");
        run(sb.toString());

        run("create index ft4_geom_idx on ft4(geometry ST_Geometry_ops) using rtree;");
        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("0,ST_GeomFromText('POINT(0 0)',4326)::ST_Point, 0, 0.0,'zero');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("1,ST_GeomFromText('POINT(1 1)',4326)::ST_Point, 1, 1.1,'one');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("2,ST_GeomFromText('POINT(2 2)',4326)::ST_Point, 1, 1.1,'one_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("3,ST_GeomFromText('POINT(3 3)',4326)::ST_Point, 1, 1.1,'one_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("4,ST_GeomFromText('POINT(4 4)',4326)::ST_Point, 2, 2.2,'two');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("5,ST_GeomFromText('POINT(5 5)',4326)::ST_Point, 2, 2.2,'two_2');");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ft4 VALUES (")
                .append("6,ST_GeomFromText('POINT(6 6)',4326)::ST_Point, 3, 3.3,'three');");
        run(sb.toString());
    }

    @Override
    protected String attributeName(String raw) {
        return super.attributeName(raw.toLowerCase());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected Properties createExampleFixture() {
        Properties p = new Properties();

        p.put("driver", "com.informix.jdbc.IfxDriver");
        p.put("url", "jdbc:informix-sqli://devassloldb01:1071/geotools:INFORMIXSERVER=crs_dev");
        p.put("host", "localhost");
        p.put("port", "1071");
        p.put("user", "gdewar");
        p.put("password", "<password>");

        return p;
    }
}
