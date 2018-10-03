/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.h2;

import org.geotools.jdbc.JDBCNativeFilterOnlineTest;
import org.opengis.filter.NativeFilter;

public final class H2NativeFilterOnlineTest extends JDBCNativeFilterOnlineTest {

    @Override
    protected H2NativeFilterTestSetup createTestSetup() {
        return new H2NativeFilterTestSetup(new H2TestSetup());
    }

    @Override
    protected NativeFilter getNativeFilter() {
        return filterFactory.nativeFilter(
                "(\"type\" = 'temperature' OR \"type\" = 'wind') AND \"value\" > 15");
    }
}
