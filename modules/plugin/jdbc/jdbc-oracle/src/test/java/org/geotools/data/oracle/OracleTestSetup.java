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

public class OracleTestSetup extends JDBCTestSetup {
    
    @Override
    protected String typeName(String raw) {
        return raw.toUpperCase();
    }
    @Override
    protected String attributeName(String raw) {
        return raw.toUpperCase();
    }
    
    @Override
    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new OracleNGDataStoreFactory();
    }
    
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        // tests do assume the dialect is working in non loose mode
        ((OracleDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);
        ((OracleDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);
        dataStore.setDatabaseSchema(fixture.getProperty("user").toUpperCase());
    }
    
    @Override
    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "oracle.jdbc.driver.OracleDriver");
        fixture.put("url", "jdbc:oracle:thin:@192.168.1.200:1521:xe");
        fixture.put("host", "192.168.1.200");
        fixture.put("port", "1521");
        fixture.put("database", "xe");
        fixture.put("username", "geoserver");
        fixture.put("password", "postgis");
        fixture.put("dbtype", "Oracle" );
        return fixture;
    }
    
    @Override
    protected void setUpData() throws Exception {
        //drop old data
        try {
            // left here so that dbs with old tests can run the test safely, we previously had
            // this trigger
            run("DROP TRIGGER ft1_pkey_trigger");
        } catch (Exception e) {
        }
        try {
            run("DROP TABLE ft1 purge");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            run("DROP SEQUENCE ft1_pkey_seq");
        } catch (Exception e) {
        }
     
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'FT1'");
        
        try {
            run("DROP TABLE ft2 purge");
        } catch (Exception e) {
        }
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'FT2'");
        

        String sql = "CREATE TABLE ft1 (" 
            + "id INT, geometry MDSYS.SDO_GEOMETRY, intProperty INT, "
            + "doubleProperty FLOAT, stringProperty VARCHAR(255)"
            + ", PRIMARY KEY(id))";
        run(sql);
        sql = "CREATE SEQUENCE ft1_pkey_seq";
        run(sql);
        
        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) " + 
         "VALUES ('ft1','geometry',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
         "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);
        
        sql = "CREATE INDEX FT1_GEOMETRY_IDX ON FT1(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX" //
                        + " PARAMETERS ('SDO_INDX_DIMS=2 LAYER_GTYPE=\"POINT\"')";
        run(sql);
        
        sql = "INSERT INTO ft1 VALUES (0," +
            "MDSYS.SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE(0.0,0.0,NULL),NULL,NULL), 0, 0.0,'zero')";
        run(sql);
        sql = "INSERT INTO ft1 VALUES (1," + 
            "MDSYS.SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE(1.0,1.0,NULL),NULL,NULL), 1, 1.1,'one')";
        run(sql);

        sql = "INSERT INTO ft1 VALUES (2," + 
            "MDSYS.SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE(2.0,2.0,NULL),NULL,NULL), 2, 2.2,'two')";
        run(sql);
    }
    

}
