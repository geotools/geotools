/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import org.geotools.jdbc.JDBCAggregateTestSetup;

public class SingleStoreAggregateTestSetup extends JDBCAggregateTestSetup {

    protected SingleStoreAggregateTestSetup() {
        super(new SingleStoreTestSetup());
    }

    @Override
    protected void createAggregateTable() throws Exception {

        run("CREATE ROWSTORE TABLE aggregate (" + "fid INT AUTO_INCREMENT PRIMARY KEY,"
                + "id INT,"
                + "geom GEOGRAPHY NOT NULL,"
                + "name VARCHAR(255)"
                + ");");

        run(
                "INSERT INTO aggregate (id,name,geom) VALUES ( 0,'muddy1', 'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))')");
        run(
                "INSERT INTO aggregate (id,name,geom) VALUES ( 1,'muddy1', 'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))')");
        run(
                "INSERT INTO aggregate (id,name,geom) VALUES ( 2,'muddy2', 'POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))')");
    }

    @Override
    protected void dropAggregateTable() throws Exception {
        run("DROP TABLE aggregate");
    }
}
