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

import org.geotools.jdbc.JDBCFeatureWriterOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Ignore;

@Ignore // writing not supported by SingleStore yet
public class SingleStoreFeatureWriterOnlineTest extends JDBCFeatureWriterOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SingleStoreTestSetup();
    }
}
