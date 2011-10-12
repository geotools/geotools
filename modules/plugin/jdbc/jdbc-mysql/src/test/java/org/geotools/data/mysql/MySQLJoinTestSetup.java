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
package org.geotools.data.mysql;

import org.geotools.jdbc.JDBCDataStore;
import org.geotools.jdbc.JDBCJoinTestSetup;

public class MySQLJoinTestSetup extends JDBCJoinTestSetup {

    public MySQLJoinTestSetup() {
        super(new MySQLTestSetup());
    }

    @Override
    protected void createJoinTable() throws Exception {
      //create some data
        StringBuffer sb = new StringBuffer();
        sb.append("CREATE TABLE ftjoin ").append("(id int, ")
          .append("name VARCHAR(255) COLLATE latin1_general_cs, geom POLYGON) ENGINE=InnoDB;");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
          .append("0, 'zero', GeometryFromText('POLYGON ((-0.1 -0.1, -0.1 0.1, 0.1 0.1, 0.1 -0.1, -0.1 -0.1))',4326));");
        run(sb.toString());

        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
        .append("1, 'one', GeometryFromText('POLYGON ((-1.1 -1.1, -1.1 1.1, 1.1 1.1, 1.1 -1.1, -1.1 -1.1))',4326));");
        run(sb.toString());
      
        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
        .append("2, 'two', GeometryFromText('POLYGON ((-10 -10, -10 10, 10 10, 10 -10, -10 -10))',4326));");
        run(sb.toString());
        
        sb = new StringBuffer();
        sb.append("INSERT INTO ftjoin VALUES (")
        .append("3, 'three', NULL);");
        run(sb.toString());
    }

    @Override
    protected void dropJoinTable() throws Exception {
        runSafe("DROP TABLE ftjoin");
    }

    @Override
    protected void setUpDataStore(JDBCDataStore dataStore) {
        dataStore.setDatabaseSchema(null);
    }
}
