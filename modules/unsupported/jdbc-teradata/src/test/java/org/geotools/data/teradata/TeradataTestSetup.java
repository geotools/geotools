/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.teradata;

import java.util.Properties;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCDataStoreFactory;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataTestSetup extends JDBCTestSetup {

	private static boolean first = true;
	
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);
        
        if (first) {
	        // uncomment to turn up logging        
	        java.util.logging.ConsoleHandler handler = new java.util.logging.ConsoleHandler();
	        handler.setLevel(java.util.logging.Level.FINE);
//	        org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").setLevel(java.util.logging.Level.FINE);
//	        org.geotools.util.logging.Logging.getLogger("org.geotools.data.jdbc").addHandler(handler);
//	        org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").setLevel(java.util.logging.Level.FINE);
//	        org.geotools.util.logging.Logging.getLogger("org.geotools.jdbc").addHandler(handler);
	        first = false;
        }
        
        
        // the unit tests assume a non loose behaviour
        ((TeradataGISDialect) dataStore.getSQLDialect()).setLooseBBOXEnabled(false);

        // the tests assume non estimated extents
        ((TeradataGISDialect) dataStore.getSQLDialect()).setEstimatedExtentsEnabled(false);

        // let's work with the most common schema please
        dataStore.setDatabaseSchema(fixture.getProperty("schema"));
    }


    protected Properties createExampleFixture() {
        Properties fixture = new Properties();
        fixture.put("driver", "com.teradata.jdbc.TeraDriver");
        fixture.put("url", "jdbc:teradata://localhost/DATABASE=geotools,PORT=1025,TMODE=ANSI,CHARSET=UTF8");
        fixture.put("host", "localhost");
        fixture.put("database", "geotools");
        fixture.put("schema", "geotools");
        fixture.put("port", "1025");
        fixture.put("user", "dbc");
        fixture.put("password", "dbc");
        
        return fixture;
    }

    @Override
    public void setUp() throws Exception {
    	super.setUp();
    	
        fixture.getProperty("tessellate_index_key", "ID");
        fixture.getProperty("tessellate_index_u_xmin", "-180");
        fixture.getProperty("tessellate_index_u_ymin", "-90");
        fixture.getProperty("tessellate_index_u_xmax", "180");
        fixture.getProperty("tessellate_index_u_ymax", "90");
        fixture.getProperty("tessellate_index_g_nx", "1000");
        fixture.getProperty("tessellate_index_g_ny", "1000");
        fixture.getProperty("tessellate_index_levels", "1");
        fixture.getProperty("tessellate_index_scale", "0.01");
        fixture.getProperty("tessellate_index_shift", "0");
    }

    protected void setUpData() throws Exception {

        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft1'");
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'ft2'");
        runSafe("DROP TABLE \"ft1\"");
        runSafe("DROP TABLE \"ft2\"");

        run("CREATE TABLE \"ft1\"(" //
                + "\"id\" PRIMARY KEY not null generated always as identity (start with 0) integer, " //
                + "\"geometry\" ST_GEOMETRY, " //
                + "\"intProperty\" int," //
                + "\"doubleProperty\" double precision, " //
                + "\"stringProperty\" varchar(200) casespecific)");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('" + fixture.getProperty("database") + "', '" + fixture.getProperty("schema") + "', 'ft1', 'geometry', 2, 1619, 'POINT')");
//        run("CREATE INDEX FT1_GEOMETRY_INDEX ON \"ft1\" USING GIST (\"geometry\") ");

        run("INSERT INTO \"ft1\" VALUES(0, 'POINT(0 0)', 0, 0.0, 'zero')");
        run("INSERT INTO \"ft1\" VALUES(1, 'POINT(1 1)', 1, 1.1, 'one')");
        run("INSERT INTO \"ft1\" VALUES(2, 'POINT(2 2)', 2, 2.2, 'two')");


    }


    protected JDBCDataStoreFactory createDataStoreFactory() {
        return new TeradataDataStoreFactory();
    }
}
