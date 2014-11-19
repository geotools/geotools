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
package org.geotools.data.sqlserver;

import org.geotools.jdbc.JDBCDateTestSetup;

public class SQLServerDateTestSetup extends JDBCDateTestSetup {

    protected SQLServerDateTestSetup() {
        super(new SQLServerTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        run( "CREATE TABLE dates (d DATE, dt DATETIME, t TIME)");
        
        run( "INSERT INTO dates VALUES (" +
            "CAST('2009-06-28' as DATE), " +
            "CAST('2009-06-28 15:12:41' as DATETIME)," +
            "CAST('15:12:41' as TIME)  )");
        
        run( "INSERT INTO dates VALUES (" +
                "CAST('2009-01-15' as DATE), " +
                "CAST('2009-01-15 13:10:12' as DATETIME)," +
                "CAST('13:10:12' as TIME)  )");
        
        run( "INSERT INTO dates VALUES (" +
                "CAST('2009-09-29' as DATE), " +
                "CAST('2009-09-29 17:54:23' as DATETIME)," +
                "CAST('17:54:23' as TIME)  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE dates");
    }

}
