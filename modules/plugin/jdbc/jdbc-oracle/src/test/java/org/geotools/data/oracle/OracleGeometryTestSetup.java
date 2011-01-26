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
        runSafe("DROP TABLE COLA_MARKETS_CS PURGE");
        
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
    }

    @Override
    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DROP TABLE " + tableName + " PURGE");
        run("DELETE FROM USER_SDO_GEOM_METADATA WHERE TABLE_NAME = '" + tableName.toUpperCase()
                + "'");
    }

}
