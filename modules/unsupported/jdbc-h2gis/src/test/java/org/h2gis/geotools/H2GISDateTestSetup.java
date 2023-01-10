/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
 *
 */
package org.h2gis.geotools;

import org.geotools.jdbc.JDBCDateTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISDateTestSetup extends JDBCDateTestSetup {

    public H2GISDateTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        String sql = "CREATE SCHEMA \"geotools\";";
        runSafe(sql);

        run("CREATE TABLE \"geotools\".\"dates\" (\"d\" DATE, \"dt\" TIMESTAMP, \"t\" TIME)");

        run(
                "INSERT INTO \"geotools\".\"dates\" VALUES ("
                        + "PARSEDATETIME('2009-06-28', 'yyyy-MM-dd'), "
                        + "PARSEDATETIME('2009-06-28 15:12:41', 'yyyy-MM-dd HH:mm:ss'),"
                        + "PARSEDATETIME('15:12:41', 'HH:mm:ss')  )");

        run(
                "INSERT INTO \"geotools\".\"dates\" VALUES ("
                        + "PARSEDATETIME('2009-01-15', 'yyyy-MM-dd'), "
                        + "PARSEDATETIME('2009-01-15 13:10:12', 'yyyy-MM-dd HH:mm:ss'),"
                        + "PARSEDATETIME('13:10:12', 'HH:mm:ss')  )");

        run(
                "INSERT INTO \"geotools\".\"dates\" VALUES ("
                        + "PARSEDATETIME('2009-09-29', 'yyyy-MM-dd'), "
                        + "PARSEDATETIME('2009-09-29 17:54:23', 'yyyy-MM-dd HH:mm:ss'),"
                        + "PARSEDATETIME('17:54:23', 'HH:mm:ss')  )");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE \"geotools\".\"dates\"");
    }
}
