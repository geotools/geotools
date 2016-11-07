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
        ((GeoPkgTestSetup)delegate).removeTable("person");
        ((GeoPkgTestSetup)delegate).removeTable("zipcode");
        run( "CREATE TABLE person (fid INTEGER PRIMARY KEY AUTOINCREMENT, id INTEGER, name VARCHAR, age INTEGER)");
        run( "INSERT INTO person (id,name,age) VALUES (0, 'Paul', 32)");
        run( "INSERT INTO person (id,name,age) VALUES (1, 'Anne', 40)");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('person', 'features', 'person', 4326)";
        run(sql);
    }

    @Override
    protected void dropPersonTable() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("person");
    }

    @Override
    protected void dropZipCodeTable() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("zipcode");
    }

    @Override
    public void tearDown() throws Exception {
        // TODO Auto-generated method stub
        super.tearDown();
    }

}
