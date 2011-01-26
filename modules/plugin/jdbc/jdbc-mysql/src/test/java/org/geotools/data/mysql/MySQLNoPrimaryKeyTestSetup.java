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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class MySQLNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {
    
    public MySQLNoPrimaryKeyTestSetup() {
        super(new MySQLTestSetup());
    }

    protected void createLakeTable() throws Exception {
        run("CREATE TABLE lake(id int, "
            + "geom POLYGON, name varchar(255) ) ENGINE=InnoDB;");

        run("INSERT INTO lake (id,geom,name) VALUES ( 0,"
            + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326)," + "'muddy')");
    }

    protected void dropLakeTable() throws Exception {
        run("DROP TABLE lake");
    }

}
