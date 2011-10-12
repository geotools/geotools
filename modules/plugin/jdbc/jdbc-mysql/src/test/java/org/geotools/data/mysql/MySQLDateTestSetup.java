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

import org.geotools.jdbc.JDBCDateTestSetup;

public class MySQLDateTestSetup extends JDBCDateTestSetup {

    protected MySQLDateTestSetup() {
        super(new MySQLTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE dates (d DATE, dt DATETIME, t TIME)");
        
        run( "INSERT INTO dates VALUES (" +
                "STR_TO_DATE('2009-06-28', '%Y-%m-%d'), " +
                "STR_TO_DATE('2009-06-28 15:12:41', '%Y-%m-%d %H:%i:%s')," +
                "STR_TO_DATE('15:12:41', '%H:%i:%s')  )");
        
        run( "INSERT INTO dates VALUES (" +
                "STR_TO_DATE('2009-01-15', '%Y-%m-%d'), " +
                "STR_TO_DATE('2009-01-15 13:10:12', '%Y-%m-%d %H:%i:%s')," +
                "STR_TO_DATE('13:10:12', '%H:%i:%s')  )");
        
        run( "INSERT INTO dates VALUES (" +
                "STR_TO_DATE('2009-09-29', '%Y-%m-%d'), " +
                "STR_TO_DATE('2009-09-29 17:54:23', '%Y-%m-%d %H:%i:%s')," +
                "STR_TO_DATE('17:54:23', '%H:%i:%s')  )");

    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE dates");
    }

}
