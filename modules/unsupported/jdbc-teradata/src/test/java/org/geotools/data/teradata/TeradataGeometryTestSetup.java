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
package org.geotools.data.teradata;

import org.geotools.jdbc.JDBCGeometryTestSetup;
import org.geotools.jdbc.JDBCTestSetup;

public class TeradataGeometryTestSetup extends JDBCGeometryTestSetup {

    public TeradataGeometryTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    protected void dropSpatialTable(String tableName) throws Exception {
        runSafe("DELETE FROM SYSSPATIAL.GEOMETRY_COLUMNS WHERE F_TABLE_NAME = '" + tableName + "'");
        runSafe("DROP TRIGGER \"" + tableName + "_geom_mi\"");
        runSafe("DROP TRIGGER \"" + tableName + "_geom_mu\"");
        runSafe("DROP TRIGGER \"" + tableName + "_geom_md\"");
        runSafe("DROP TABLE \"" + tableName + "_geom_idx\"");
        runSafe("DROP TABLE \"" + tableName + "\"");
    }

}
