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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class SpatiaLiteNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected SpatiaLiteNoPrimaryKeyTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createLakeTable() throws Exception {
        run( "CREATE TABLE lake (id INTEGER )");
        run( "SELECT AddGeometryColumn('lake','geom',4326,'POLYGON',2)");
        run( "ALTER TABLE lake add name VARCHAR");
        
        run( "INSERT INTO lake VALUES (0," +
            "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),'muddy')");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        run( "DROP TABLE lake");
        run( "DELETE FROM geometry_columns WHERE f_table_name = 'lake'");
    }

}
