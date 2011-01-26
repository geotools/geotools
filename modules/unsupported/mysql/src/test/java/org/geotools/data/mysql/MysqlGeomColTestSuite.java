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
package org.geotools.data.mysql;

import java.io.IOException;
import java.util.PropertyResourceBundle;
import java.util.logging.Logger;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.geotools.data.DataSourceException;
import org.geotools.data.jdbc.datasource.ManageableDataSource;

import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.io.ParseException;
import com.vividsolutions.jts.io.WKTReader;


public class MysqlGeomColTestSuite extends TestCase {
    /** Standard logging instance */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger("org.geotools.defaultcore");
    private static final String TEST_CATALOG = "US";
    private static final String TEST_SCHEMA = "NY";
    private static final String TEST_F_TABLE = "STREET_LAMP";
    private static final String TEST_G_TABLE = "STREET_LAMP_LOC";
    private static final String TEST_COL_NAME = "GEOMETRY";
    private static final String TEST_WKT_GEOM = "POINT (4 7)";

    /** Factory for producing geometries (from JTS). */
    private static GeometryFactory geometryFactory = new GeometryFactory();

    /** Well Known Text reader (from JTS). */
    private static WKTReader geometryReader = new WKTReader(geometryFactory);
    private MysqlGeomColumn gCol;
    private Geometry testGeo;
    private ManageableDataSource pool;

    public MysqlGeomColTestSuite(String testName) {
        super(testName);
    }

    public static void main(String[] args) {
        junit.textui.TestRunner.run(suite());
    }

    public static Test suite() {
        LOGGER.info("starting suite...");

        TestSuite suite = new TestSuite(MysqlGeomColTestSuite.class);
        LOGGER.info("made suite...");

        return suite;
    }

    public void setUp() throws IOException {
        PropertyResourceBundle resource;
        resource = new PropertyResourceBundle(this.getClass()
                                                  .getResourceAsStream("fixture.properties"));

        String namespace = resource.getString("namespace");
        String host = resource.getString("host");
        int port = Integer.parseInt(resource.getString("port"));
        String database = resource.getString("database");

        String user = resource.getString("user");
        String password = resource.getString("passwd");

        if (namespace.equals("http://www.geotools.org/data/postgis")) {
            throw new IllegalStateException(
                "The fixture.properties file needs to be configured for your own database");
        }

        pool = MySQLDataStoreFactory.getDefaultDataSource(host, user, password, port, database, 10,
                2, false);

        LOGGER.info("creating MysqlConnection connection...");
        //this will eventually be a world accessible mysql database
        //for now it is just on localhost
        LOGGER.info("created new db connection");
        gCol = new MysqlGeomColumn();
        geometryFactory = new GeometryFactory();
        geometryReader = new WKTReader(geometryFactory);

        try {
            testGeo = geometryReader.read(TEST_WKT_GEOM);
        } catch (ParseException e) {
            LOGGER.info("Failed to parse " + e.getMessage());
        }

        LOGGER.info("created new datasource");
    }

    protected void tearDown() throws Exception {
        pool.close();
    }

    public void testFeatureSetters() {
        gCol.setFeaTableCat(TEST_CATALOG);
        assertEquals(gCol.getFeaTableCat(), TEST_CATALOG);
        gCol.setFeaTableSchema(TEST_SCHEMA);
        assertEquals(gCol.getFeaTableSchema(), TEST_SCHEMA);
        gCol.setFeaTableName(TEST_F_TABLE);
        assertEquals(gCol.getFeaTableName(), TEST_F_TABLE);
        gCol.setGeomColName(TEST_COL_NAME);
        assertEquals(gCol.getGeomColName(), TEST_COL_NAME);
    }

    public void testGeomSetters() {
        gCol.setGeomTableCat(TEST_CATALOG);
        assertEquals(gCol.getGeomTableCat(), TEST_CATALOG);
        gCol.setGeomTableSchema(TEST_SCHEMA);
        assertEquals(gCol.getGeomTableSchema(), TEST_SCHEMA);
        gCol.setGeomTableName(TEST_G_TABLE);
        assertEquals(gCol.getGeomTableName(), TEST_G_TABLE);
    }

    public void testTypeSetters() {
        gCol.setStorageType(MysqlGeomColumn.WKB_STORAGE_TYPE);
        assertEquals(gCol.getStorageType(), MysqlGeomColumn.WKB_STORAGE_TYPE);
        gCol.setGeomType(2);
        assertEquals(gCol.getGeomType(), 2);
    }

    public void testData() {
        try {
            testGeo = geometryReader.read(TEST_WKT_GEOM);
            gCol.populateData(4, TEST_WKT_GEOM);
            gCol.populateData(3, "POINT (24 53)");
            assertTrue(gCol.getGeometry(4).equalsExact(testGeo));
            assertNull(gCol.getGeometry(25));
        } catch (ParseException e) {
            LOGGER.info("Failed to parse " + e.getMessage());
        } catch (DataSourceException e) {
            LOGGER.info("caught datasource exception");
            fail("datasource exception");
        }
    }

    public void testRemoveData() {
        try {
            gCol.populateData(15, TEST_WKT_GEOM);
            gCol.removeData(15);
            assertNull(gCol.getGeometry(15));
        } catch (DataSourceException e) {
            LOGGER.info("caught datasource exception");
            fail("datasource exception");
        }
    }

    //    public void testConnectionConstructor(){
    //	
    //
    //	try{
    //	    Connection dbCon = pool.getConnection();
    //	    gCol = new MysqlGeomColumn(dbCon, TEST_F_TABLE);
    //	    dbCon.close();
    //
    //	} catch (SQLException e) {
    //	    LOGGER.info("sql error " + e.getMessage());
    //	    LOGGER.info("sql state: " + e.getSQLState());
    //	    fail("sql error");
    //	} catch (SchemaException e) {
    //	    LOGGER.info("schema error: " + e.getMessage());
    //	    fail("schema error");
    //	}
    //
    //	assertEquals(gCol.getFeaTableCat(), TEST_CATALOG);
    //	assertEquals(gCol.getFeaTableSchema(), TEST_SCHEMA);
    //	assertEquals(gCol.getFeaTableName(), TEST_F_TABLE);
    //	assertEquals(gCol.getGeomColName(), TEST_COL_NAME);
    //	assertEquals(gCol.getGeomTableCat(), TEST_CATALOG);
    //	assertEquals(gCol.getGeomTableSchema(), TEST_SCHEMA);
    //	assertEquals(gCol.getGeomTableName(), TEST_G_TABLE);
    //	assertEquals(gCol.getStorageType(), MysqlGeomColumn.WKB_STORAGE_TYPE);
    //	assertEquals(gCol.getGeomType(), 1);	
    //	try {
    //	assertTrue(gCol.getGeometry(49).equalsExact(testGeo));
    //	} catch (DataSourceException e){
    //	    LOGGER.info("caught datasource exception");
    //	    fail("datasource exception");
    //	}
    //
    //
    //    }
}
