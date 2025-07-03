/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2025, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.singlestore;

import org.geotools.jdbc.JDBCJoinOnlineTest;
import org.geotools.jdbc.JDBCJoinTestSetup;
import org.junit.Ignore;
import org.junit.Test;

public class SingleStoreJoinOnlineTest extends JDBCJoinOnlineTest {

    @Override
    protected JDBCJoinTestSetup createTestSetup() {
        return new SingleStoreJoinTestSetup();
    }

    @Override
    @Ignore
    @Test
    public void testSimpleJoin() throws Exception {
        // singlestore returns data in unpredictable order, need to find if we can perform sort with join
        // super.testSimpleJoin();
    }

    @Override
    @Ignore
    @Test
    public void testSimpleJoinInvertedAliases() throws Exception {
        // singlestore returns data in unpredictable order, need to find if we can perform sort with join
        // super.testSimpleJoinInvertedAliases();
    }

    @Override
    @Ignore
    @Test
    public void testSimpleJoinOnPrimaryKey() throws Exception {
        // singlestore returns data in unpredictable order, need to find if we can perform sort with join
        // super.testSimpleJoinOnPrimaryKey();
    }
}
