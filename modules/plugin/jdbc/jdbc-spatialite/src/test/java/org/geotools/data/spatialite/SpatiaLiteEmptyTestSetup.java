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

import org.geotools.jdbc.JDBCEmptyTestSetup;

public class SpatiaLiteEmptyTestSetup extends JDBCEmptyTestSetup {

    protected SpatiaLiteEmptyTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createEmptyTable() throws Exception {
        run( "CREATE TABLE empty( id INTEGER )" );
        run( "SELECT AddGeometryColumn('empty','geom',4326,'POINT',2)");
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        run( "DROP TABLE empty");
        run( "DELETE FROM geometry_columns where f_table_name = 'empty'");
    }

}
