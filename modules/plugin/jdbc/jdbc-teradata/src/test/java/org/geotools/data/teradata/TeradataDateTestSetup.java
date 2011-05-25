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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCDateTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataDateTestSetup extends JDBCDateTestSetup {


    public TeradataDateTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }


    protected void createDateTable() throws Exception {
        run("CREATE TABLE dates (d DATE, dt TIMESTAMP, t TIME)");

        //_date('1998/05/31:12:00:00AM', 'yyyy/mm/dd:hh:mi:ssam'));
        
        run("INSERT INTO DATES VALUES ('1969-12-23' (DATE, format 'yyyy-MM-dd'), '2009-06-28 15:12:41' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '15:12:41' (TIME, format 'HH:mi:ss') );");
        run("INSERT INTO DATES VALUES ('2009-01-15' (DATE, format 'yyyy-MM-dd'), '2009-01-15 13:10:12' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '13:10:12' (TIME, format 'HH:mi:ss') );");
        run("INSERT INTO DATES VALUES ('2009-09-29' (DATE, format 'yyyy-MM-dd'), '2009-09-29 17:54:23' (TIMESTAMP, format 'YYYY-MM-ddBHH:mi:ss'), '17:54:23' (TIME, format 'HH:mi:ss') );");

    }


    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE DATES");
    }

}