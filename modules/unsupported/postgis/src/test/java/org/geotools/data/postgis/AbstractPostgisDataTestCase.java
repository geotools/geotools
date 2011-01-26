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
package org.geotools.data.postgis;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;

import org.geotools.data.DataStore;
import org.geotools.data.DataTestCase;
import org.geotools.data.jdbc.datasource.ManageableDataSource;
import org.geotools.data.jdbc.fidmapper.BasicFIDMapper;
import org.geotools.data.jdbc.fidmapper.TypedFIDMapper;
import org.opengis.feature.simple.SimpleFeature;

import com.vividsolutions.jts.geom.Geometry;

public class AbstractPostgisDataTestCase extends DataTestCase {

	static boolean WKB_ENABLED = true;
	static boolean CHECK_TYPE = false;
	
	protected PostgisTests.Fixture f;
	protected ManageableDataSource pool;
	protected DataStore data;
	
	public AbstractPostgisDataTestCase(String name) {
		super(name);
	}

	public String getFixtureFile() {
		return "fixture.properties";
	}
	
    protected void setUp() throws Exception {
        super.setUp();

        f = PostgisTests.newFixture(getFixtureFile());
        pool = PostgisDataStoreFactory.getDefaultDataSource(f.host, f.user, f.password, f.port.intValue(), f.database, 10, 2, false);

        setupDbTables();

        if (CHECK_TYPE) {
            checkTypesInDataBase();
            CHECK_TYPE = false; // just once
        }
       	
        data = newDataStore();
        
    }

    protected void setupDbTables() throws Exception {
        setUpRoadTable();
        setUpRiverTable();
        setUpLakeTable();
    }

    protected DataStore newDataStore() throws IOException {
        PostgisDataStore pg = new PostgisDataStore(pool, f.schema, getName(),
                PostgisDataStore.OPTIMIZE_SQL);
        pg.setWKBEnabled(WKB_ENABLED);
        pg.setEstimatedExtent( true );
        pg.setFIDMapper("road",
            new TypedFIDMapper(new BasicFIDMapper("fid", 255, false), "road"));
        pg.setFIDMapper("river",
            new TypedFIDMapper(new BasicFIDMapper("fid", 255, false), "river"));
        pg.setFIDMapper("testset",
            new TypedFIDMapper(new BasicFIDMapper("gid", 255, true), "testset"));
        return pg;
    }
    
    
    protected void tearDown() throws Exception {
        data.dispose();
        data = null;
        super.tearDown();
    }
    
    protected void checkTypesInDataBase() throws SQLException {
        Connection conn = pool.getConnection();

        try {
            DatabaseMetaData md = conn.getMetaData();
            ResultSet rs = 
                //md.getTables( catalog, null, null, null );
                md.getTables(null, "public", "%", new String[] { "TABLE", });
            ResultSetMetaData rsmd = rs.getMetaData();
            int NUM = rsmd.getColumnCount();
            System.out.print(" ");

            for (int i = 1; i <= NUM; i++) {
                System.out.print(rsmd.getColumnName(i));
                System.out.flush();
                System.out.print(":");
                System.out.flush();
                System.out.print(rsmd.getColumnClassName(i));
                System.out.flush();

                if (i < NUM) {
                    System.out.print(",");
                    System.out.flush();
                }
            }

            System.out.println();

            while (rs.next()) {
                System.out.print(rs.getRow());
                System.out.print(":");
                System.out.flush();

                for (int i = 1; i <= NUM; i++) {
                    System.out.print(rsmd.getColumnName(i));
                    System.out.flush();
                    System.out.print("=");
                    System.out.flush();
                    System.out.print(rs.getString(i));
                    System.out.flush();

                    if (i < NUM) {
                        System.out.print(",");
                        System.out.flush();
                    }
                }

                System.out.println();
            }
        } finally {
            conn.close();
        }
    }

    protected void setUpRoadTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','road','geom')");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".road CASCADE");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();

            //postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".road (fid varchar PRIMARY KEY, id int )");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                + "', 'road', 'geom', 4326, 'LINESTRING', 2);");
            s.execute("ALTER TABLE " + f.schema + ".road add name varchar;");

            for (int i = 0; i < roadFeatures.length; i++) {
                SimpleFeature feature = roadFeatures[i];

                //strip out the road. 
                String fid = feature.getID().substring("road.".length());
                String ql = "INSERT INTO " + f.schema + ".road (fid,id,geom,name) VALUES ("
                    + "'" + fid + "'," + feature.getAttribute("id") + ","
                    + "GeometryFromText('"
                    + ((Geometry) feature.getAttribute("geom")).toText() + "', 4326 ),"
                    + "'" + feature.getAttribute("name") + "')";

                s.execute(ql);
            }
            
            s.execute( "VACUUM ANALYZE " + f.schema + ".road" );
        } finally {
            conn.close();
        }
    }

    protected void setUpLakeTable() throws Exception {
        Connection conn = pool.getConnection();
        conn.setAutoCommit(true);

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','lake','geom')");
        } catch (Exception ignore) {}

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".lake CASCADE");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            //postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".lake ( id int ) WITH OIDS");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                + "', 'lake', 'geom', 4326, 'POLYGON', 2);");
            s.execute("ALTER TABLE " + f.schema + ".lake add name varchar;");
            
            for (int i = 0; i < lakeFeatures.length; i++) {
                SimpleFeature feature = lakeFeatures[i];

                //strip out the lake. 
                String ql = "INSERT INTO " + f.schema + ".lake (id,geom,name) VALUES ("
                    + feature.getAttribute("id") + "," + "GeometryFromText('"
                    + ((Geometry) feature.getAttribute("geom")).toText() + "', 4326 ),"
                    + "'" + feature.getAttribute("name") + "')";

                s.execute(ql);
            }
            
            s.execute( "VACUUM ANALYZE " + f.schema + ".lake" );
        } finally {
            conn.close();
        }
    }

    protected void killTestTables() throws Exception {
        Connection conn = pool.getConnection();

        try {
	        Statement s = conn.createStatement();
	        
	        try {
	            s.execute("SELECT dropgeometrycolumn( '" + f.schema
	                + "','road','geom')");
	        } catch (Exception ignore) {}
	        
	        try {
	            s.execute("SELECT dropgeometrycolumn( '" + f.schema
	                + "','river','geom')");
	        } catch (Exception ignore) {}
	
	        try {
	            s.execute("SELECT dropgeometrycolumn( '" + f.schema
	                + "','lake','geom')");
	        } catch (Exception ignore) {}
	        
	        try {
	        	s.execute("DROP TABLE " + f.schema + ".road");
	        } catch (Exception ignore) {}
	        
	        try {    
	            s.execute("DROP TABLE " + f.schema + ".river");
	        } catch (Exception ignore) {}
	        
	        try {    
	            s.execute("DROP TABLE " + f.schema + ".lake");
	        } catch (Exception ignore) {}
        
        }
	    finally {
            conn.close();
        }
    }

    protected void setUpRiverTable() throws Exception {
        Connection conn = pool.getConnection();

        try {
            Statement s = conn.createStatement();
            s.execute("SELECT dropgeometrycolumn( '" + f.schema
                + "','river','geom')");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();
            s.execute("DROP TABLE " + f.schema + ".river CASCADE");
        } catch (Exception ignore) {
        }

        try {
            Statement s = conn.createStatement();

            //postgis = new PostgisDataSource(connection, FEATURE_TABLE);
            s.execute("CREATE TABLE " + f.schema + ".river(fid varchar PRIMARY KEY, id int)");
            s.execute("SELECT AddGeometryColumn('" + f.schema
                + "', 'river', 'geom', 4326, 'MULTILINESTRING', 2);");
            s.execute("ALTER TABLE " + f.schema + ".river add river varchar");
            s.execute("ALTER TABLE " + f.schema + ".river add flow float8");
            
            for (int i = 0; i < riverFeatures.length; i++) {
                SimpleFeature feature = riverFeatures[i];
                String fid = feature.getID().substring("river.".length());
                s.execute(
                    "INSERT INTO " + f.schema + ".river (fid, id, geom, river, flow) VALUES ("
                    + "'" + fid + "'," + feature.getAttribute("id") + ","
                    + "GeometryFromText('" + feature.getAttribute("geom").toString()
                    + "', 4326 )," + "'" + feature.getAttribute("river") + "',"
                    + feature.getAttribute("flow") + ")");
            }
            
            s.execute( "VACUUM ANALYZE " + f.schema + ".river" );
        } finally {
            conn.close();
        }
    }

   

}
