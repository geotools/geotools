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
package org.geotools.data.spatialite;

import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

public class SpatiaLitePrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    public SpatiaLitePrimaryKeyFinderTestSetup() {
        super(new SpatiaLiteTestSetup());
    }

    @Override
    protected void createMetadataTable() throws Exception {
        run("CREATE TABLE gt_pk_metadata (table_schema varchar(255), table_name varchar(255), pk_column varchar(255), " +
        "pk_column_idx int, pk_policy varchar(255), pk_sequence varchar(255))");
    }
    
    @Override
    protected void dropMetadataTable() throws Exception {
        runSafe("DROP TABLE gt_pk_metadata");
    }
    
    @Override
    protected void createPlainTable() throws Exception {
        run("CREATE TABLE plaintable (key1 int, key2 int, name varchar(255))"); 
        run("SELECT AddGeometryColumn('plaintable', 'geom', 4326, 'POINT', 2)");
        run("INSERT INTO plaintable VALUES (1, 2, 'one', GeomFromText('POINT(0 0)', 4326))");
        run("INSERT INTO plaintable VALUES (2, 3, 'two', GeomFromText('POINT(1 1)', 4326))");
        run("INSERT INTO plaintable VALUES (3, 4, 'three', GeomFromText('POINT(2 2)', 4326))");
    }
    
    @Override
    protected void dropPlainTable() throws Exception {
        runSafe("DROP TABLE plaintable");
        runSafe("DELETE FROM geometry_columns WHERE f_table_name = 'plaintable'");
    }
    
    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW assignedsinglepk as SELECT * from plaintable");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
    }
    
    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        runSafe("DROP VIEW assignedsinglepk");
    }
    
    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW assignedmultipk AS SELECT * from plaintable");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key2', 1, 'assigned', NULL)");
    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        runSafe("DROP VIEW assignedmultipk");
    }
    
    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
    }
    
    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
    }

}
