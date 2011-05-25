/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * Sets up various dummy tables/sequences, for extension.
 * 
 * @author Cory Horner, Refractions Research
 *
 *
 * @source $URL$
 */
public class AbstractPostgisOnlineTestCase extends PostgisOnlineTestCase {
    
    public static final String TEST_SCHEMA = "gt_test";

    protected PostgisDataStore ds;
    
    /** simple table with serial (int4) primary key */
    final protected String table1 = "tmp_pgtest1";
    /** simple table with int4 primary key and sequence as default value */
    final protected String table2 = "tmp_pgtest2";
    /** simple table with bigserial (int8) primary key */
    final protected String table3 = "tmp_pgtest3";
    /** simple table with int8 primary key and sequence as default value */
    final protected String table4 = "tmp_pgtest4";
    /** simple table with serial (int4) primary key, WITHOUT OIDS, and space in name */
    final protected String table5 = "tmp_pgtest 5";
    /** simple table with int4 primary key, sequence as default value, WITHOUT OIDS, and space in name */
    final protected String table6 = "tmp_pgtest 6";
    /** just like table1, but will be inserted in a different schema */
    final protected String table7 =  "tmp_pgtest_7";
    /** just like table1, but with no primary key */
    final protected String table8 =  "tmp_pgtest_8";
    
    protected void connect() throws Exception {
        super.connect();
        ds = (PostgisDataStore) dataStore;
        //create dummy tables
        Statement st = getConnection().createStatement();
        dropTables(st);
        purgeGeometryColumns(st);
        createTables(st);
        setupGeometryColumns(st);
        st.close();
    }

    protected void disconnect() throws Exception {
        Statement st = getConnection().createStatement();
        purgeGeometryColumns(st);
        dropTables(st);
        st.close();
        //ds.getConnectionPool().close(); //is this killing our other tests?
        super.disconnect();
    }
    
    public Connection getConnection() throws Exception {
        return ds.getDataSource().getConnection();
    }
    
    protected void setupGeometryColumns(Statement st) throws Exception {
        //subclasses should override if they want more or less geometry columns
        String preSql = "INSERT INTO geometry_columns (f_table_catalog, f_table_schema, f_table_name, f_geometry_column, coord_dimension, srid, type) VALUES ('',";
        String postSql = ", 'the_geom', 2, 4326, 'POINT')";
        String sql = preSql + "'public', '" + table1 + "'" + postSql;
        st.execute(sql);
        sql = preSql + "'public', '" + table2 + "'" + postSql;
        st.execute(sql);
        sql = preSql + "'public', '" + table3 + "'" + postSql;
        st.execute(sql);
        sql = preSql + "'" + TEST_SCHEMA +"', '" + table7 + "'" + postSql;
        st.execute(sql);
        sql = preSql + "'public', '" + table8 + "'" + postSql;
        st.execute(sql);
    }
    
    protected void purgeGeometryColumns(Statement st) throws Exception {
        String sql = "DELETE FROM geometry_columns WHERE f_table_name LIKE 'tmp_pgtest%'";
        st.execute(sql);
    }
    
    protected void createTables(Statement st) throws Exception {
        createTable1(st);
        createTable2(st);
        createTable3(st);
        createTable4(st);
        createTable5(st);
        createTable6(st);
        createTestSchema(st);
        createTable7(st);
        createTable8(st);
    }
    
    protected void createTestSchema(Statement st) throws Exception {
        String sql = "CREATE SCHEMA " + TEST_SCHEMA;
        st.execute(sql);
    }
    
    protected void createTable1(Statement st) throws Exception {
        String sql = "CREATE TABLE " + table1 + "(" + "fid serial NOT NULL,"
                + "name varchar(10), the_geom geometry, " + "CONSTRAINT " + table1
                + "_pkey PRIMARY KEY (fid)" + ") WITH OIDS;";
        st.execute(sql);
    }

    protected void createTable2(Statement st) throws Exception {
        String sql = "CREATE SEQUENCE " + table2
                + "_fid_seq INCREMENT 1 MINVALUE 1 "
                + "MAXVALUE 9223372036854775807 START 1001 CACHE 1;"
                + "CREATE TABLE " + table2 + "("
                + "fid int4 NOT NULL DEFAULT nextval('" + table2
                + "_fid_seq'::text), name varchar(10), the_geom geometry, "
                + "CONSTRAINT " + table2 + "_pkey PRIMARY KEY (fid)"
                + ") WITH OIDS;";
        st.execute(sql);
    }

    protected void createTable3(Statement st) throws Exception {
        String sql = "CREATE TABLE " + table3 + "(" + "fid bigserial NOT NULL, "
                + "name varchar(10), the_geom geometry, " + "CONSTRAINT " + table3
                + "_pkey PRIMARY KEY (fid)" + ") WITH OIDS;";
        st.execute(sql);
    }

    protected void createTable4(Statement st) throws Exception {
        String sql = "CREATE SEQUENCE " + table4
                + "_fid_seq INCREMENT 1 MINVALUE 1 "
                + "MAXVALUE 9223372036854775807 START 1000001 CACHE 1;"
                + "CREATE TABLE " + table4 + "("
                + "fid int8 NOT NULL DEFAULT nextval('" + table4
                + "_fid_seq'::text)," + "name varchar(10)," + "CONSTRAINT "
                + table4 + "_pkey PRIMARY KEY (fid)" + ") WITH OIDS;";
        st.execute(sql);
    }

    protected void createTable5(Statement st) throws Exception{
        String sql = "CREATE TABLE \"" + table5 + "\" ("
                + "fid serial NOT NULL," + "name varchar(10),"
                + "CONSTRAINT \"" + table5 + "_pkey\" PRIMARY KEY (fid)"
                + ") WITHOUT OIDS;";
        st.execute(sql);
    }

    protected void createTable6(Statement st) throws Exception {
        String sql = "CREATE SEQUENCE \"" + table6
                + "_fid_seq\" INCREMENT 1 MINVALUE 1 "
                + "MAXVALUE 9223372036854775807 START 1001 CACHE 1;"
                + "CREATE TABLE \"" + table6 + "\" ("
                + "fid int4 NOT NULL DEFAULT nextval('\"" + table6
                + "_fid_seq\"'::text)," + "name varchar(10)," + "CONSTRAINT \""
                + table6 + "_pkey\" PRIMARY KEY (fid)" + ") WITHOUT OIDS;";
        st.execute(sql);
    }
    
    protected void createTable7(Statement st) throws Exception {
        String sql = "CREATE TABLE " + TEST_SCHEMA + "." + table7 + "(" + "fid serial NOT NULL,"
        + "name varchar(10), the_geom geometry, " + "CONSTRAINT " + table7
        + "_pkey PRIMARY KEY (fid)" + ") WITH OIDS;";
        st.execute(sql);
    }

    protected void createTable8(Statement st) throws Exception {
        String sql = "CREATE TABLE " + table8 + "(" 
        + "name varchar(10), the_geom geometry) WITHOUT OIDS;";
        st.execute(sql);
    }
    
    protected void dropTables(Statement st) throws Exception {
        dropTable(st, table1);
        dropTable(st, table2);
        dropSequence(st, table2 + "_fid_seq");
        dropTable(st, table3);
        dropTable(st, table4);
        dropSequence(st, table4 + "_fid_seq");
        dropTable(st, table5);
        dropTable(st, table6);
        dropSequence(st, table6 + "_fid_seq");
        dropTable(st, table7);
        dropTable(st, table8);
        dropSequence(st, TEST_SCHEMA + "." + table7 + "_fid_seq");
        dropSchema(st, TEST_SCHEMA);
    }
    
    protected void dropTable(Statement st, String tableName) throws Exception {
        String sql = "SELECT schemaname, tablename FROM pg_tables WHERE tablename = '" + tableName + "'";
        ResultSet rs = st.executeQuery(sql);
        boolean exists = rs.next();
        String schemaName = "public";
        if(exists) {
            schemaName = rs.getString(1);
        }
        rs.close();
        if (exists) {
            sql = "DROP TABLE \"" + schemaName + "\".\"" + tableName + "\"";
            st.execute(sql);
        }
    }

    protected void dropSequence(Statement st, String sequenceName) throws Exception {
        String sql = "SELECT COUNT(relid) FROM pg_statio_all_sequences WHERE relname = '" + sequenceName + "'";
        ResultSet rs = st.executeQuery(sql);
        rs.next();
        int exists = rs.getInt(1);
        rs.close();
        if (exists > 0) {
            sql = "DROP SEQUENCE \"" + sequenceName + "\"";
            st.execute(sql);
        }
    }

    protected String getFixtureId() {
        return "postgis.typical";
    }
    
    protected void dropSchema(Statement st, String schemaName) throws Exception {
        String sql = "SELECT nspname FROM pg_namespace WHERE nspname = '" + schemaName + "'";
        ResultSet rs = st.executeQuery(sql);
        boolean exists = rs.next();
        rs.close();
        if(exists) {
            sql = "DROP SCHEMA " + schemaName;
            st.execute(sql);
        }
    }
}
