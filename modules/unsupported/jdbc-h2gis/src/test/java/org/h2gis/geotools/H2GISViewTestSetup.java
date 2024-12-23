/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCViewTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISViewTestSetup extends JDBCViewTestSetup {

    protected H2GISViewTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        super.setUpDataStore(dataStore);

        dataStore.setDatabaseSchema(null);
    }

    @Override
    protected void createLakesTable() throws Exception {
        run("DROP VIEW IF EXISTS \"lakesview\"");
        run("DROP TABLE IF EXISTS \"lakes\"");
        run(
                "CREATE TABLE \"lakes\"(\"fid\" int primary key, \"id\" int, \"geom\" GEOMETRY(POLYGON, 4326), \"name\" varchar )");
        run("INSERT INTO \"lakes\" (\"fid\", \"id\",\"geom\",\"name\") VALUES ( 0, 0,"
                + "ST_GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakesTable() {
        runSafe("CALL DropGeometryColumn(NULL, 'lakes', 'geom')");
        runSafe("DROP TABLE \"lakes\"");
    }

    @Override
    protected void createLakesView() throws Exception {
        run("CREATE VIEW \"lakesview\" as select * from \"lakes\"");
    }

    @Override
    protected void dropLakesView() throws Exception {
        run("CALL DropGeometryColumn(NULL, 'lakesview', 'geom')");
        runSafe("DROP VIEW \"lakesview\"");
    }

    @Override
    protected void createLakesViewPk() {
        // TODO Auto-generated method stub

    }

    @Override
    protected void dropLakesViewPk() {
        // TODO Auto-generated method stub

    }
}
