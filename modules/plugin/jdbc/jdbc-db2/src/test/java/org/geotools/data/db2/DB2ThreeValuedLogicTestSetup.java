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

import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

public class DB2ThreeValuedLogicTestSetup extends JDBCThreeValuedLogicTestSetup {

    public DB2ThreeValuedLogicTestSetup(JDBCTestSetup delegate) {
        super(delegate);
    }

    @Override
    protected void createAbcTable() throws Exception {
        run(
                "CREATE TABLE "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"abc\"(\"name\" varchar(10), \"a\" int, \"b\" int, \"c\" int)");
        run(
                "INSERT INTO "
                        + DB2TestUtil.SCHEMA_QUOTED
                        + ".\"abc\" VALUES('n_n_n', null, null, null)");
        run("INSERT INTO " + DB2TestUtil.SCHEMA_QUOTED + ".\"abc\" VALUES('a_b_c', 1, 2, 3)");
    }

    @Override
    protected void dropAbcTable() throws Exception {
        runSafe("DROP TABLE " + DB2TestUtil.SCHEMA_QUOTED + ".\"abc\"");
    }
}
