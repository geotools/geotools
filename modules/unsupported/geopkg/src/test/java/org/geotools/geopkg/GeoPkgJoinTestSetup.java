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
package org.geotools.geopkg;

import org.geotools.geometry.jts.GeometryBuilder;
import org.geotools.jdbc.JDBCJoinTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class GeoPkgJoinTestSetup extends JDBCJoinTestSetup {

    public GeoPkgJoinTestSetup() {
        this(new GeoPkgTestSetup());
    }
    
    public GeoPkgJoinTestSetup(JDBCTestSetup setup) {
        super(setup);
    }

    @Override
    protected void createJoinTable() throws Exception {
        run("CREATE TABLE \"ftjoin\" ( \"id\" int primary key, "
                + "\"name\" VARCHAR, \"geom\" BLOB, \"join1intProperty\" int)");
        String sql = "INSERT INTO gpkg_geometry_columns VALUES ('ftjoin', 'geom', 'POLYGON', 4326, 0, 0)";
        run(sql);
        
        sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('ftjoin', 'features', 'ftjoin', 4326)";
        run(sql);
        GeometryBuilder gb = new GeometryBuilder();
        run( "INSERT INTO \"ftjoin\" VALUES (0, 'zero', X'"+((GeoPkgTestSetup)delegate).toString(gb.polygon(-0.1, -0.1, -0.1, 0.1, 0.1, 0.1, 0.1, -0.1, -0.1, -0.1))+"', 0)");
        run( "INSERT INTO \"ftjoin\" VALUES (1,  'one', X'"+((GeoPkgTestSetup)delegate).toString(gb.polygon(-1.1, -1.1, -1.1, 1.1, 1.1, 1.1, 1.1, -1.1, -1.1, -1.1))+"', 1)");
        run( "INSERT INTO \"ftjoin\" VALUES (2,  'two', X'"+((GeoPkgTestSetup)delegate).toString(gb.polygon(-10, -10, -10, 10, 10, 10, 10, -10, -10, -10))+"', 2)");
        run( "INSERT INTO \"ftjoin\" VALUES (3, 'three', NULL, 3)");
        
        run("CREATE TABLE \"ftjoin2\"(\"id\" int primary key, \"join2intProperty\" int, \"stringProperty2\" varchar)");
        
        sql = "INSERT INTO gpkg_contents (table_name, data_type, identifier, srs_id) VALUES " +
                "('ftjoin2', 'features', 'ftjoin2', 4326)";
        run(sql);
        run( "INSERT INTO \"ftjoin2\" VALUES (0, 0, '2nd zero')");
        run( "INSERT INTO \"ftjoin2\" VALUES (1, 1, '2nd one')");
        run( "INSERT INTO \"ftjoin2\" VALUES (2, 2, '2nd two')");
        run( "INSERT INTO \"ftjoin2\" VALUES (3, 3, '2nd three')");
        
    }

    @Override
    protected void dropJoinTable() throws Exception {
        ((GeoPkgTestSetup)delegate).removeTable("ftjoin");
        ((GeoPkgTestSetup)delegate).removeTable("ftjoin2");
    }

}
