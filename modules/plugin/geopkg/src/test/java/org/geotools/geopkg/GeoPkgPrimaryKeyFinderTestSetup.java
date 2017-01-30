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
package org.geotools.geopkg;

import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCPrimaryKeyFinderTestSetup;

import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 *
 * @source $URL$
 */
public class GeoPkgPrimaryKeyFinderTestSetup extends JDBCPrimaryKeyFinderTestSetup {

    public GeoPkgPrimaryKeyFinderTestSetup() {
        super(new GeoPkgTestSetup());
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
        run("CREATE TABLE plaintable (key1 integer primary key, key2 int, name varchar(255),geom BLOB)"); 
        //run("SELECT AddGeometryColumn('plaintable', 'geom', 4326, 'POINT', 2)");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('plaintable', 'features', 'plaintable', 4326)";
        run(sql);
        sql = "INSERT INTO gpkg_geometry_columns VALUES ('plaintable', 'geom', 'POINT', 4326, 0, 0)";
        run(sql);
        GeometryBuilder gb = new GeometryBuilder();
        GeoPkgTestSetup setup = (GeoPkgTestSetup)delegate;

        run("INSERT INTO plaintable VALUES (1, 2, 'one', X'"+setup.toString(gb.point(0,0))+"')");
        run("INSERT INTO plaintable VALUES (2, 3, 'two', X'"+setup.toString(gb.point(1,1))+"')");
        run("INSERT INTO plaintable VALUES (3, 4, 'three', X'"+setup.toString(gb.point(2,2))+"')");
    }
    
    @Override
    protected void dropPlainTable() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("plaintable");
    }
    
    @Override
    protected void createAssignedSinglePkView() throws Exception {
        run("CREATE VIEW assignedsinglepk as SELECT * from plaintable");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedsinglepk', 'key1', 0, 'assigned', NULL)");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('assignedsinglepk', 'features', 'assignedsinglepk', 4326)";
        run(sql);
    }
    
    @Override
    protected void dropAssignedSinglePkView() throws Exception {
        
        ((GeoPkgTestSetup)delegate).removeTable("assignedsinglepk");
    }
    
    @Override
    protected void createAssignedMultiPkView() throws Exception {
        run("CREATE VIEW assignedmultipk AS SELECT * from plaintable");
        String sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('assignedmultipk', 'features', 'assignedmultipk', 4326)";
        run(sql);
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key1', 0, 'assigned', NULL)");
        run("INSERT INTO gt_pk_metadata VALUES"
                + "(NULL, 'assignedmultipk', 'key2', 1, 'assigned', NULL)");
    }

    @Override
    protected void dropAssignedMultiPkView() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("assignedmultipk");
    
    }
    
    @Override
    protected void createSequencedPrimaryKeyTable() throws Exception {
    }
    
    @Override
    protected void dropSequencedPrimaryKeyTable() throws Exception {
    }

}
