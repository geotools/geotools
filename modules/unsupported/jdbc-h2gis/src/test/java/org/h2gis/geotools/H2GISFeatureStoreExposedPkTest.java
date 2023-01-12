/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.h2gis.geotools;

import java.io.IOException;
import java.sql.SQLException;
import org.geotools.jdbc.JDBCFeatureStoreExposePkOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.jupiter.api.Test;

/**
 * FeatureStore test for H2.
 *
 * @author Justin Deoliveira, The Open Planning Project
 */
public class H2GISFeatureStoreExposedPkTest extends JDBCFeatureStoreExposePkOnlineTest {
    @Override
    protected JDBCTestSetup createTestSetup() {
        return new H2GISTestSetup();
    }

    @Test
    @Override
    public void testAddInTransaction() throws IOException {
        // does not work, see GEOT-2832
    }

    @Test
    @Override
    public void testExternalConnection() throws IOException, SQLException {
        // does not work, see GEOT-2832
    }
}
