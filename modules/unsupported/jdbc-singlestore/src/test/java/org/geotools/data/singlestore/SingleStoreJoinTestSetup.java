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

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCJoinTestSetup;

public class SingleStoreJoinTestSetup extends JDBCJoinTestSetup {

    public SingleStoreJoinTestSetup() {
        super(new SingleStoreTestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE ROWSTORE TABLE ftjoin ")
                .append("(id INT PRIMARY KEY, ")
                .append("name VARCHAR(255) COLLATE latin1_general_cs, ")
                .append("geom GEOGRAPHY, ")
                .append("join1intProperty INT, ")
                .append("SHARD KEY (id));");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
                .append("0, 'zero', \"POLYGON((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))\", 0);");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
                .append("1, 'one', \"POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))\", 1);");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
                .append("2, 'two', \"POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))\", 2);");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (").append("3, 'three', NULL, 3);");
        run(sb.toString());

        run("CREATE TABLE ftjoin2(id int PRIMARY KEY, join2intProperty int, stringProperty2 varchar(255))");
        run("INSERT INTO ftjoin2 VALUES (0, 0, '2nd zero')");
        run("INSERT INTO ftjoin2 VALUES (1, 1, '2nd one')");
        run("INSERT INTO ftjoin2 VALUES (2, 2, '2nd two')");
        run("INSERT INTO ftjoin2 VALUES (3, 3, '2nd three')");
    }

    @Override
    protected void dropJoinTable() throws Exception {
        runSafe("DROP TABLE ftjoin");
        runSafe("DROP TABLE ftjoin2");
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        dataStore.setDatabaseSchema(null);
    }
}
