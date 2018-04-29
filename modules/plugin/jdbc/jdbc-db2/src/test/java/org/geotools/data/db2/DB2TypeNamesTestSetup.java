/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2015, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.db2;

import org.geotools.jdbc.JDBCTypeNamesTestSetup;

public class DB2TypeNamesTestSetup extends JDBCTypeNamesTestSetup {

    protected DB2TypeNamesTestSetup() {
        super(new DB2TestSetup());
    }

    @Override
    protected void createTypes() throws Exception {
        run(
                "CREATE TABLE "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"ftntable\" ("
                        + "\"id\" INT, \"name\" VARCHAR(255), \"geom\" db2gse.st_point)");
        run(
                "CREATE VIEW "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"ftnview\""
                        + " AS SELECT \"id\", \"geom\" FROM "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"ftntable\"");
    }

    @Override
    protected void dropTypes() throws Exception {
        runSafe("DROP VIEW " + DB2TestUtil.SCHEMA_QUOTED + ".\"ftnview\"");
        runSafe("DROP TABLE " + DB2TestUtil.SCHEMA_QUOTED + ".\"ftntable\"");
    }
}
