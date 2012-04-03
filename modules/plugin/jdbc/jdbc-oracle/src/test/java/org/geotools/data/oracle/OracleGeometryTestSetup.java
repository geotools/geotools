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

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCGeometryTestSetup;

public class OracleGeometryTestSetup extends JDBCGeometryTestSetup {

    protected OracleGeometryTestSetup() {
        super(new OracleTestSetup());
    }
    
    @Override
    public void setUp() throws Exception {
        super.setUp();
        
        // clean up
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'COLA_MARKETS_CS'" );
        runSafe("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = 'GTMETA'" );
        runSafe("DROP TABLE COLA_MARKETS_CS PURGE");
        runSafe("DROP TABLE GTMETA PURGE");
        runSafe("DROP TABLE GEOMETRY_COLUMNS PURGE");
        
        // create the cola markets table
        run("CREATE TABLE cola_markets_cs (" + 
        		"  mkt_id NUMBER PRIMARY KEY," + 
        		"  name VARCHAR2(32)," + 
        		"  shape SDO_GEOMETRY)");
        
        // metadata
        run("INSERT INTO user_sdo_geom_metadata " + 
        		"    (TABLE_NAME," + 
        		"     COLUMN_NAME," + 
        		"     DIMINFO," + 
        		"     SRID)" + 
        		"  VALUES (" + 
        		"  'cola_markets_cs'," + 
        		"  'shape'," + 
        		"  SDO_DIM_ARRAY(" + 
        		"    SDO_DIM_ELEMENT('Longitude', -180, 180, 10), " + 
        		"    SDO_DIM_ELEMENT('Latitude', -90, 90, 10) " + 
        		"     )," + 
        		"  8307)");
        
        // index
        run("CREATE INDEX cola_spatial_idx_cs" + 
        		" ON cola_markets_cs(shape)" + 
        		" INDEXTYPE IS MDSYS.SPATIAL_INDEX");
        
        // insert data with simple geometry fallback
        run("INSERT INTO cola_markets_cs VALUES(" + 
        		"  1," + 
        		"  'polyfallback'," + 
        		"  SDO_GEOMETRY(" + 
        		"    2003," + 
        		"    8307," + 
        		"    NULL," + 
        		"    SDO_ELEM_INFO_ARRAY(1,0,57, 11,1003,3)," + 
        		"    SDO_ORDINATE_ARRAY(6,6, 12,6, 9,8, 6,10, 12,10, 6,4, 12,12)" + 
        		"  )" + 
        		")");
        
        String sql = "CREATE TABLE gtmeta (" 
                + "id INT, geometry MDSYS.SDO_GEOMETRY, intProperty INT, "
                + "doubleProperty FLOAT, stringProperty VARCHAR(255))";
        run(sql);
        
        sql = "INSERT INTO USER_SDO_GEOM_METADATA (TABLE_NAME, COLUMN_NAME, DIMINFO, SRID ) " + 
                "VALUES ('GTMETA','GEOMETRY',MDSYS.SDO_DIM_ARRAY(MDSYS.SDO_DIM_ELEMENT('X',-180,180,0.5), " + 
                "MDSYS.SDO_DIM_ELEMENT('Y',-90,90,0.5)), 4326)";
        run(sql);
        
        sql = "CREATE INDEX GTMETA_GEOMETRY_IDX ON GTMETA(GEOMETRY) INDEXTYPE IS MDSYS.SPATIAL_INDEX";
        run(sql);
        
        sql = "INSERT INTO GTMETA VALUES (0," +
            "MDSYS.SDO_GEOMETRY(2001,4326,SDO_POINT_TYPE(0.0,0.0,NULL),NULL,NULL), 0, 0.0,'zero')";
        run(sql);
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DROP TABLE " + tableName + " PURGE");
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = '" + tableName.toUpperCase()
                + "'");
    }

    public void setupGeometryColumns(JDBCDataStore dataStore) throws Exception {
        String schema = dataStore.getDatabaseSchema();
        
        String sql = "CREATE TABLE GEOMETRY_COLUMNS(F_TABLE_SCHEMA VARCHAR(30), F_TABLE_NAME VARCHAR(30), " +
                "F_GEOMETRY_COLUMN VARCHAR(30), COORD_DIMENSION INTEGER, SRID INTEGER, TYPE VARCHAR(30))";
        run(sql);
        
        // register it in the override table with a different srs, so that we're sure it's getting read
        sql = "INSERT INTO GEOMETRY_COLUMNS (F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, TYPE) " + 
              "VALUES ('" + schema + "', 'GTMETA','GEOMETRY', 2, 4269, 'POINT')";
        run(sql);
        
        ((OracleDialect) dataStore.getSQLDialect()).setGeometryMetadataTable("GEOMETRY_COLUMNS");
    }

}
