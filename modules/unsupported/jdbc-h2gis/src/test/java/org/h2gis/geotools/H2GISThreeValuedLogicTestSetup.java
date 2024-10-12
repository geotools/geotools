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

import org.geotools.jdbc.JDBCThreeValuedLogicTestSetup;

@SuppressWarnings("PMD.JUnit4TestShouldUseTestAnnotation") // not yet a JUnit4 test
public class H2GISThreeValuedLogicTestSetup extends JDBCThreeValuedLogicTestSetup {

    public H2GISThreeValuedLogicTestSetup() {
        super(new H2GISTestSetup());
    }

    @Override
    protected void createAbcTable() throws Exception {
        run("CREATE TABLE \"geotools\".\"abc\"(\"name\" varchar(10), \"a\" int, \"b\" int, \"c\" int)");
        run("INSERT INTO \"geotools\".\"abc\" VALUES('n_n_n', null, null, null)");
        run("INSERT INTO \"geotools\".\"abc\" VALUES('a_b_c', 1, 2, 3)");
    }

    /** Drops the "testlob" table */
    @Override
    protected void dropAbcTable() throws Exception {
        runSafe("DROP TABLE \"geotools\".\"abc\"");
    }
}
