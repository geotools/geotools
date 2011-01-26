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

package org.geotools.gce.imagemosaic.jdbc;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;


import junit.framework.Assert;
import junit.framework.Test;
import junit.framework.TestSuite;

public class GeoRasterOnlineTest extends AbstractTest {
    static protected DBDialect dialect = null;
    static String CREATE_RDT = 
        "CREATE TABLE RASTER_RDT OF SDO_RASTER"+
        " (PRIMARY KEY (rasterID, pyramidLevel, bandBlockNumber,"+
        " rowBlockNumber, columnBlockNumber))"+
        " LOB(rasterBlock) STORE AS rdt_1_rbseg"+
        " ("+
        " CHUNK 8192"+
        " CACHE READS"+
        " NOLOGGING"+
        " PCTVERSION 0"+
        " STORAGE (PCTINCREASE 0)"+
        ")";

    public GeoRasterOnlineTest(String test) {
            super(test);
    }

    
    public static Test suite() {
        TestSuite suite = new TestSuite();

        GeoRasterOnlineTest test = new GeoRasterOnlineTest("");

        if (test.checkPreConditions() == false) {
                return suite;
        }

        suite.addTest(new GeoRasterOnlineTest("testGetConnection"));
        suite.addTest(new GeoRasterOnlineTest("testDrop"));
        suite.addTest(new GeoRasterOnlineTest("testCreate"));        
        suite.addTest(new GeoRasterOnlineTest("testImage1"));
        suite.addTest(new GeoRasterOnlineTest("testFullExtent"));
        suite.addTest(new GeoRasterOnlineTest("testNoData"));
        suite.addTest(new GeoRasterOnlineTest("testPartial"));
        suite.addTest(new GeoRasterOnlineTest("testVienna"));
        suite.addTest(new GeoRasterOnlineTest("testViennaEnv"));
        suite.addTest(new GeoRasterOnlineTest("testOutputTransparentColor"));
        suite.addTest(new GeoRasterOnlineTest("testOutputTransparentColor2"));        
        suite.addTest(new GeoRasterOnlineTest("testDrop"));
        suite.addTest(new GeoRasterOnlineTest("testCloseConnection"));

        return suite;
}
    
    
    protected String getSrsId() {
        return "4326";
}

    
    @Override
    public String getConfigUrl() {
        // TODO, not existing
            return "file:target/resources/oek.georaster.xml";
    }


    protected String getSubDir() {
        return "georaster";
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
        return "oracle.jdbc.OracleDriver";
    }

    @Override
    protected String getJDBCUrl(String host, Integer port, String dbName) {
        return "jdbc:oracle:thin:@" + host + ":" + port + ":" + dbName;
    }


    @Override
    protected String getXMLConnectFragmentName() {
        return "connect.oracle.xml.inc";
    }


    @Override
    public void testCreate() {
        
        
        try {
            java.sql.Connection con = dialect.getConnection();
            con.prepareStatement("CREATE TABLE RASTER (NAME VARCHAR(64) ,  IMAGE SDO_GEORASTER)").execute();
            con.prepareCall("{ call sdo_geor_utl.createDMLTrigger('RASTER', 'IMAGE') }").execute();
            con.prepareStatement(CREATE_RDT).execute();
            con.prepareStatement("INSERT INTO RASTER VALUES ('oek', sdo_geor.init('RASTER_RDT'))").execute();
            con.prepareStatement("CREATE TABLE blob_table (blob_col BLOB, blobid NUMBER unique, clob_col CLOB)").execute();
            
            InputStream imageIn = new URL("file:target/resources/baseimage/map.tif").openStream();
            ByteArrayOutputStream imageOut = new ByteArrayOutputStream();
            int in; 
            while ((in = imageIn.read())!=-1) {
                imageOut.write(in);
            }
            
            InputStream worldIn = new URL("file:target/resources/baseimage/map.tfw").openStream();
            ByteArrayOutputStream worldOut = new ByteArrayOutputStream(); 
            
            while ((in=worldIn.read())!=-1) {
                worldOut.write(in);
            }
           
            
            PreparedStatement ps = con.prepareStatement("INSERT INTO blob_table values(?,?,?)");
            ps.setBytes(1, imageOut.toByteArray());
            ps.setInt(2, 1);
            String world = new String(worldOut.toByteArray());
            ps.setString(3, world );
            ps.execute();
            
            

            String importString = 
            "DECLARE"+
            " geor1 SDO_GEORASTER;"+
            " lobd1 BLOB;"+
            " lobd2 CLOB;"+
            " BEGIN"+
            " SELECT clob_col into lobd2 from blob_table WHERE blobid = 1 for update;"+
            " SELECT blob_col into lobd1 from blob_table WHERE blobid = 1 for update;"+
            " SELECT image INTO geor1 from raster WHERE name = 'oek' for update;"+
            " sdo_geor.importFrom(geor1,'', 'TIFF', lobd1, 'WORLDFILE', lobd2);"+
            " sdo_geor.setModelSRID(geor1, 4326);"+
            " UPDATE raster SET image = geor1 WHERE name = 'oek';"+                        
            " COMMIT;"+
            " END;";
            CallableStatement cs =con.prepareCall(importString);
            cs.execute();
            
            String createPyramidString = 
              "declare"+
              " gr mdsys.sdo_georaster;"+
              " begin"+
              "  select image into gr"+ 
              "    from raster where name = 'oek' for update;"+             
              " sdo_geor.generatePyramid(gr, 'rLevel=2 resampling=NN');"+
              " update raster set image = gr where name='oek';"+
              " commit;"+
              " end;";
            cs =con.prepareCall(createPyramidString);
            cs.execute();
            

            
            
            imageIn.close();
            imageOut.close();
            worldIn.close();
            worldOut.close();
            
            con.prepareStatement("DROP TABLE blob_table").execute();
            con.commit();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());
        }
    }


    @Override
    public void testDrop() {

        java.sql.Connection con = null;
        try {
            con = dialect.getConnection();
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail(e.getMessage());            
        }        
        try {
            con.prepareStatement("DROP TABLE RASTER_RDT").execute();
            con.commit();
        } catch (Exception e) {}
        try {
            con.prepareStatement("DROP TABLE RASTER").execute();
            con.commit();
        } catch (Exception e) {}

        try {
            con.prepareStatement("DROP TABLE blob_table").execute();
            con.commit();
        } catch (Exception e) {}
        
        try {
            con.close();            
        } catch (Exception e) {}                            
    }

}
