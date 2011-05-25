/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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

import org.geotools.jdbc.JDBCViewTestSetup;

public class TeradataViewTestSetup extends JDBCViewTestSetup {

    public TeradataViewTestSetup() {
        super(new TeradataTestSetup());
    }
    
    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("CREATE TABLE lakes (fid NOT NULL PRIMARY KEY INTEGER, id INTEGER, geom ST_Geometry, " +
            "name VARCHAR(100))");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + 
                fixture.getProperty("database") + "', 'lakes', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");
        
        run("INSERT INTO lakes VALUES (0, 0, 'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))', 'muddy')");
    }

    @Override
    protected void dropLakesTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakes'");
        runSafe("DROP TABLE lakes");
    }
    
    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW lakesview AS SELECT * FROM lakes");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME," +
            " F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '" + 
                fixture.getProperty("database") + "', 'lakesview', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");
    }

    @Override
    protected void dropLakesView() throws Exception {
        run("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lakesview'");
        runSafe("DROP VIEW lakesview");
    }

    @Override
    protected void createLakesViewPk() throws Exception {
    }

    

    @Override
    protected void dropLakesViewPk() throws Exception {
    }

}
