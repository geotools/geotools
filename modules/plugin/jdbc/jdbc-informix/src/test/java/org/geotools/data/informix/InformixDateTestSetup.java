/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.informix;

import org.geotools.jdbc.JDBCDateTestSetup;

public class InformixDateTestSetup extends JDBCDateTestSetup {

    protected InformixDateTestSetup() {
        super(new InformixTestSetup());
    }

    @Override
    protected void createDateTable() throws Exception {
        run(
                "CREATE TABLE dates (id SERIAL PRIMARY KEY, d DATE, dt DATETIME YEAR TO SECOND, t DATETIME HOUR TO SECOND)");

        run("INSERT INTO dates (d, dt, t) VALUES (" + "'2009-06-28', " + "'2009-06-28 15:12:41'," + "'15:12:41')");

        run("INSERT INTO dates (d, dt, t) VALUES (" + "'2009-01-15', " + "'2009-01-15 13:10:12'," + "'13:10:12' )");

        run("INSERT INTO dates (d, dt, t) VALUES (" + "'2009-09-29', " + "'2009-09-29 17:54:23'," + "'17:54:23')");
    }

    @Override
    protected void dropDateTable() throws Exception {
        runSafe("DROP TABLE dates");
    }
}
