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

import org.geotools.jdbc.JDBCEmptyTestSetup;

public class InformixEmptyTestSetup extends JDBCEmptyTestSetup {

    protected InformixEmptyTestSetup() {
        super(new InformixTestSetup());
    }

    @Override
    protected void createEmptyTable() throws Exception {
        run("CREATE TABLE empty (id int, geom ST_GEOMETRY) ");
    }

    @Override
    protected void dropEmptyTable() throws Exception {
        runSafe("DROP TABLE empty");
    }
}
