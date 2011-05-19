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

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    public TeradataNoPrimaryKeyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }
    
    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }

    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"id\" int, \"geom\" ST_GEOMETRY, \"name\" varchar(200) )");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', '"
                + fixture.getProperty("schema") + "', 'lake', 'geom', 2, " + getDelegate().getSrid4326() + ", 'POLYGON')");
        // run("CREATE INDEX LAKE_GEOM_INDEX ON \"lake\" USING GIST (\"geom\") ");

        run("INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES (0,'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))','muddy')");
    }

    protected void dropLakeTable() throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'lake'");
        runSafe("DROP TRIGGER \"lake_geom_mi\"");
        runSafe("DROP TRIGGER \"lake_geom_mu\"");
        runSafe("DROP TRIGGER \"lake_geom_md\"");
        runSafe("DROP TABLE \"lake_geom_idx\"");
        runSafe("DROP TABLE \"lake\"");
    }

}
