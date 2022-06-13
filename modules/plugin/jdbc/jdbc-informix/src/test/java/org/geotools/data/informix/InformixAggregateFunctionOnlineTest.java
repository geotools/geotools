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

import org.geotools.jdbc.JDBCAggregateFunctionOnlineTest;
import org.geotools.jdbc.JDBCAggregateTestSetup;

public class InformixAggregateFunctionOnlineTest extends JDBCAggregateFunctionOnlineTest {

    @Override
    protected JDBCAggregateTestSetup createTestSetup() {
        return new InformixAggregateTestSetup();
    }

    @Override
    public void testUniqueMultipleAttributes() {
        // skip this test as it requires "ft4"
    }

    @Override
    public void testUniqueMultipleAttributes2() {
        // skip this test as it requires "ft4"
    }

    @Override
    public void testUniqueMultipleAttributes3() {
        // skip this test as it requires "ft4"
    }

}
