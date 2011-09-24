/*
 *    GeoTools - +The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.gce.imagemosaic.jdbc.custom;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.sql.PreparedStatement;
import java.util.StringTokenizer;

import org.geotools.gce.imagemosaic.jdbc.AbstractTest;
import org.geotools.gce.imagemosaic.jdbc.Config;
import org.geotools.gce.imagemosaic.jdbc.DBDialect;

import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

public class PGRasterOnlineTest extends AbstractTest {
    static protected DBDialect dialect = null;
    public PGRasterOnlineTest(String test) {
            super(test);
    }

    
    public static Test suite() {
        TestSuite suite = new TestSuite();

        PGRasterOnlineTest test = new PGRasterOnlineTest("");

        if (test.checkPreConditions() == false) {
                return suite;
        }

        suite.addTest(new PGRasterOnlineTest("testGetConnection"));
        
        // Test with in db pgraster
        suite.addTest(new PGRasterOnlineTest("testDrop"));
        suite.addTest(new PGRasterOnlineTest("testCreate2"));        
        suite.addTest(new PGRasterOnlineTest("testImage1"));
        suite.addTest(new PGRasterOnlineTest("testFullExtent"));
        suite.addTest(new PGRasterOnlineTest("testNoData"));
        suite.addTest(new PGRasterOnlineTest("testPartial"));
        suite.addTest(new PGRasterOnlineTest("testVienna"));
        suite.addTest(new PGRasterOnlineTest("testViennaEnv"));
        suite.addTest(new PGRasterOnlineTest("testOutputTransparentColor"));
        suite.addTest(new PGRasterOnlineTest("testOutputTransparentColor2"));
        
        // Test with out db pgraster
        /* not ready yet, segmentation faults in postgres server process
        suite.addTest(new PGRasterOnlineTest("testDrop"));
        suite.addTest(new PGRasterOnlineTest("testCreate"));
        suite.addTest(new PGRasterOnlineTest("testImage1Joined"));
        suite.addTest(new PGRasterOnlineTest("testFullExtentJoined"));
        suite.addTest(new PGRasterOnlineTest("testNoDataJoined"));
        suite.addTest(new PGRasterOnlineTest("testPartialJoined"));
        suite.addTest(new PGRasterOnlineTest("testViennaJoined"));
        suite.addTest(new PGRasterOnlineTest("testViennaEnvJoined"));
        */

        
        suite.addTest(new PGRasterOnlineTest("testCloseConnection"));

        return suite;
}
    
    
    protected String getSrsId() {
        return "4326";
}

    
    @Override
    public String getConfigUrl() {
            return "file:target/resources/oek.pgraster.xml";
    }


    protected String getSubDir() {
        return "pgraster";
    }


    @Override
    protected DBDialect getDBDialect() {
        if (dialect != null) {
                return dialect;
        }

        Config config = null;

        try {
                config = Config.readFrom(new URL(getConfigUrl()));
        } catch (Exception e) {
                throw new RuntimeException(e);
        }

        dialect = DBDialect.getDBDialect(config);

        return dialect;
    }
    

    @Override
    protected String getDriverClassName() {
        return "org.postgresql.Driver";
    }

    @Override
    protected String getJDBCUrl(String host, Integer port, String dbName) {
        return "jdbc:postgresql://" + host + ":" + port + "/" + dbName;
    }


    @Override
    protected String getXMLConnectFragmentName() {
        return "connect.pgraster.xml.inc";
    }


    @Override
    public void testCreate() {
        executeCreate(Connection,new String [] { "pgrasterfs.sql","1/pgrasterfs.sql","2/pgrasterfs.sql"},true );
    }
    
    public void testCreate2() {
        executeCreate(Connection,new String [] { "pgraster.sql","1/pgraster.sql","2/pgraster.sql",},false );
    }


    
    void executeCreate(java.sql.Connection con,String[] scriptNames, boolean outdb) {
        
                        
        try {

            String createMasterStatement = "create table MOSAIC (NAME varchar(254) not null," +
            "TileTable varchar(254)not null," +
            "minX FLOAT8,minY FLOAT8, maxX FLOAT8, maxY FLOAT8,resX FLOAT8, resY FLOAT8," +
            "primary key (NAME,TileTable))";        
                                      
            con.prepareStatement(createMasterStatement).execute();
            
            PreparedStatement ps = con.prepareStatement("insert into MOSAIC(NAME,TileTable) values (?,?)");
            ps.setString(1, "oek");
            ps.setString(2, "rtable1");
            ps.execute();
            ps.setString(1, "oek");
            ps.setString(2, "rtable2");
            ps.execute();
            ps.setString(1, "oek");
            ps.setString(2, "rtable3");
            ps.execute();
            
            
            con.prepareStatement(
                    "CREATE TABLE \"public\".\"rtable1\" (rid serial PRIMARY KEY, \"filename\" text)").execute();
            
            con.prepareStatement(
                    "SELECT AddRasterColumn('public','rtable1','rast',4326, ARRAY['8BUI','8BUI','8BUI','8BUI'], "+outdb+
                    ", false, null, 0.007547169811321, -0.005102040816327, null, null, null)").execute();
            
            con.prepareStatement(
                    "CREATE TABLE \"public\".\"rtable2\" (rid serial PRIMARY KEY, \"filename\" text)").execute();
            
            con.prepareStatement(
                    "SELECT AddRasterColumn('public','rtable2','rast',4326, ARRAY['8BUI','8BUI','8BUI','8BUI'], "+outdb+
                    ", false, null, 0.015094339622642, -0.010204081632653, null, null, null)").execute();
            
            con.prepareStatement(
                    "CREATE TABLE \"public\".\"rtable3\" (rid serial PRIMARY KEY, \"filename\" text)").execute();
            
            con.prepareStatement(
                    "SELECT AddRasterColumn('public','rtable3','rast',4326, ARRAY['8BUI','8BUI','8BUI','8BUI'], "+outdb+
                    ", false, null, 0.030188679245283, -0.020408163265306, null, null, null)").execute();

//            con.prepareStatement("alter table raster1 drop constraint enforce_srid_rast").execute();
//            con.prepareStatement("alter table raster2 drop constraint enforce_srid_rast").execute();
//            con.prepareStatement("alter table raster3 drop constraint enforce_srid_rast").execute(); 
            
            for (String scriptName: scriptNames) {
                InputStream scriptIn = new URL("file:target/resources/"+scriptName).openStream();
                final char[] buffer = new char[0x10000];
                StringBuilder out = new StringBuilder();
                Reader in = new InputStreamReader(scriptIn, "UTF-8");
                int read;
                do {
                  read = in.read(buffer, 0, buffer.length);
                  if (read>0) {
                    out.append(buffer, 0, read);
                  }
                } while (read>=0);
                
                StringTokenizer tok = new StringTokenizer(out.toString(),";");
                while (tok.hasMoreTokens()) {
                    String statement = tok.nextToken();
                    con.prepareStatement(statement).execute();
                }                
            }

            con.prepareStatement(
                    "CREATE INDEX \"rtable1_rast_gist_idx\" ON \"public\".\"rtable1\" USING GIST (st_convexhull(rast))").execute();
            con.prepareStatement(
                    "CREATE INDEX \"rtable2_rast_gist_idx\" ON \"public\".\"rtable2\" USING GIST (st_convexhull(rast))").execute();            
            con.prepareStatement(
                    "CREATE INDEX \"rtable3_rast_gist_idx\" ON \"public\".\"rtable3\" USING GIST (st_convexhull(rast))").execute();            


            con.commit();
            //con.close();
        } catch (Exception e) {                   
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }                    

    }


    @Override
    public void testDrop() {

        java.sql.Connection con = null;
        try {
            con = getDBDialect().getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());            
        }        
        try {            
            con.prepareStatement("DROP TABLE MOSAIC").execute();
            con.commit();
        } catch (Exception e) {}
        try {
            con.prepareStatement("SELECT DropRasterColumn ('public','rtable1','rast')").execute();
            con.commit();
        } catch (Exception e) {}
        try {
            con.prepareStatement("DROP TABLE rtable1").execute();
            con.commit();
        } catch (Exception e) {}
        try {
            con.prepareStatement("SELECT DropRasterColumn ('public','rtable2','rast')").execute();
            con.commit();
        } catch (Exception e) {}
        
        try {
            con.prepareStatement("DROP TABLE rtable2").execute();
            con.commit();
        } catch (Exception e) {}
        try {            
            con.prepareStatement("SELECT DropRasterColumn ('public','rtable3','rast')").execute();
            con.commit();
        } catch (Exception e) {}               
        try {            
            con.prepareStatement("DROP TABLE rtable3").execute();
            con.commit();
        } catch (Exception e) {}       
        
        try {
            con.close();            
        } catch (Exception e) {}                            
    }

    protected String[] getTileTableNames() {
        return new String[] { "rtable1", "rtable2", "rtable3" };
}

}
