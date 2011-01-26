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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCGeometryAssociationTestSupport;
import org.geotools.jdbc.JDBCTestSetup;


public class H2GeometryAssociationTest extends JDBCGeometryAssociationTestSupport {
    protected JDBCTestSetup createTestSetup() {
        return new H2TestSetup() {
                protected void setUpData() throws Exception {
                    super.setUpData();

                    try {
                        run("DROP TABLE \"geotools\".\"geometry\"; COMMIT;");
                    } catch (Exception e) {
                    }

                    String sql = "CREATE TABLE \"geotools\".\"geometry\" ("
                        + "\"id\" VARCHAR, \"name\" VARCHAR, \"description\" VARCHAR,  "
                        + "\"type\" VARCHAR, \"geometry\" POINT" + ")";
                    run(sql);
                    
                    sql = "CALL AddGeometryColumn('geotools', 'geometry', 'geometry', 4326, 'POINT', 2)";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry\" VALUES ("
                        + "'0','name-0','description-0', 'POINT', ST_GeomFromText('POINT(0 0)',4326) "
                        + ");";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry\" VALUES ("
                        + "'1','name-1','description-1', 'POINT', ST_GeomFromText('POINT(1 1)',4326) "
                        + ");";
                    run(sql);

                    try {
                        run("DROP TABLE \"geotools\".\"multi_geometry\"; COMMIT;");
                    } catch (Exception e) {
                    }

                    sql = "CREATE TABLE \"geotools\".\"multi_geometry\" (" + 
                        "\"id\" VARCHAR, \"mgid\" VARCHAR, \"ref\" boolean" + 
                         ")";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry\" VALUES ("
                        + "'2','name-2','description-2','MULTIPOINT', NULL" + ");";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"multi_geometry\" VALUES ('2', '0', false);";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"multi_geometry\" VALUES ('2', '1', false);";
                    run(sql);

                    try {
                        run("DROP TABLE \"geotools\".\"geometry_associations\"; COMMIT;");
                    } catch (Exception e) {
                    }

                    sql = "CREATE TABLE \"geotools\".\"geometry_associations\" ("
                        + "\"fid\" VARCHAR, \"gid\" VARCHAR, \"gname\" VARCHAR, "
                        + "\"ref\" BOOLEAN" + ")";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry_associations\" VALUES ("
                        + "'ga.0', '0', 'geometry', false" + ");";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry_associations\" VALUES ("
                        + "'ga.1', '1', 'geometry', true" + ");";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"geometry_associations\" VALUES ("
                        + "'ga.2', '2', 'geometry', false" + ");";
                    run(sql);

                    try {
                        run("DROP TABLE \"geotools\".\"ga\"; COMMIT;");
                    } catch (Exception e) {
                    }

                    sql = "CREATE TABLE \"geotools\".\"ga\" ("
                        + "\"id\" int AUTO_INCREMENT(1) PRIMARY KEY, " + "\"geometry\" GEOMETRY " + ")";
                    run(sql);
                    
                    sql = "CALL AddGeometryColumn('geotools', 'ga', 'geometry', 4326, 'GEOMETRY', 2)";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"ga\" VALUES (0, NULL);";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"ga\" VALUES (1, NULL);";
                    run(sql);

                    sql = "INSERT INTO \"geotools\".\"ga\" VALUES (2, NULL);";
                    run(sql);
                }

                public void tearDown() throws Exception {
                    super.tearDown();

                    try {
                        run("DROP TABLE \"geotools\".\"fk\"; COMMIT;");
                    } catch (Exception e) {
                    }
                }
            };
    }
}
