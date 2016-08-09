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
package org.geotools.geopkg;


/**
 * 
 *
 * @source $URL$
 */
public class GeoPkgGeometrylessTestSetup extends org.geotools.jdbc.JDBCGeometrylessTestSetup {

    protected GeoPkgGeometrylessTestSetup() {
        super(new GeoPkgTestSetup());
    }

    @Override
    protected void createPersonTable() throws Exception {
        //drop old data
        runSafe("DROP TABLE person");
        //runSafe("DROP TABLE ft2");
        runSafe("DELETE FROM gpkg_geometry_columns where table_name in ('person','zipcode')");
        runSafe("DELETE FROM gpkg_contents where table_name in ('person','zipcode')");
        run( "CREATE TABLE person (fid INTEGER PRIMARY KEY AUTOINCREMENT, id INTEGER, name VARCHAR, age INTEGER)");
        run( "INSERT INTO person (id,name,age) VALUES (0, 'Paul', 32)");
        run( "INSERT INTO person (id,name,age) VALUES (1, 'Anne', 40)");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('person', 'features', 'person', 4326)";
        run(sql);
    }

    @Override
    protected void dropPersonTable() throws Exception {
        runSafe( "DROP TABLE person");
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        runSafe( "DROP TABLE zipcode");
        runSafe("DELETE FROM gpkg_geometry_columns where table_name in ('zipcode')");
        runSafe("DELETE FROM gpkg_contents where table_name in ('zipcode')");
    }

    @Override
    public void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }

}
