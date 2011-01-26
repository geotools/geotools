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
package org.geotools.data.ingres;

import org.geotools.jdbc.JDBCNoPrimaryKeyTestSetup;

public class IngresNoPrimaryKeyTestSetup extends JDBCNoPrimaryKeyTestSetup {

    protected IngresNoPrimaryKeyTestSetup() {
        super(new IngresTestSetup());
    }

    @Override
    protected void createLakeTable() throws Exception {
        run("CREATE TABLE \"lake\"(\"id\" int, "
                + "\"geom\" geometry SRID 4326, \"name\" varchar )");
        
        run("INSERT INTO \"lake\" (\"id\",\"geom\",\"name\") VALUES (0,"
                + "GeomFromText('POLYGON((12 6, 14 8, 16 6, 16 4, 14 4, 12 6))',4326),"
                + "'muddy')");
    }

    @Override
    protected void dropLakeTable() throws Exception {
        runSafe("DROP TABLE \"lake\" CASCADE");
    }

}
