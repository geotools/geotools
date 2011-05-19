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

import org.geotools.jdbc.JDBCEmptyTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataEmptyTestSetup extends JDBCEmptyTestSetup {

    public TeradataEmptyTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    public TeradataTestSetup getDelegate() {
        return (TeradataTestSetup) delegate;
    }
    
    protected void createEmptyTable() throws Exception {
        run("CREATE TABLE \"empty\"(\"key\" PRIMARY KEY not null generated always as identity (start with 0) integer, geom ST_GEOMETRY)");
        run("INSERT INTO SYSSPATIAL.GEOMETRY_COLUMNS (F_TABLE_CATALOG, F_TABLE_SCHEMA, F_TABLE_NAME, " +
                "F_GEOMETRY_COLUMN, COORD_DIMENSION, SRID, GEOM_TYPE) VALUES ('', " +
                "'" + fixture.getProperty("schema") + "', 'empty', 'geom', 2, " + getDelegate().getSrid4326() + " , 'GEOMETRY');");
    }


    protected void dropEmptyTable() throws Exception {
        runSafe("DELETE SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = 'empty'");
        runSafe("DROP TABLE \"empty\"");
    }

}
