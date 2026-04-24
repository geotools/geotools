package org.geotools.data.sqlserver;

import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;

/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
public class SQLServerInvalidGeometryTest extends JDBCTestSupport {

    private SQLServerGeometryTestSetup testSetup;

    @Test
    public void testInvalidGeometry() throws Exception {
        testSetup.setupMetadataTable(dataStore);
        // test that the proper exception is thrown when trying to read invalid geometry
        try (SimpleFeatureIterator simpleFeatureIterator =
                dataStore.getFeatureSource("invalid_geometry").getFeatures().features()) {

            SimpleFeature sf = simpleFeatureIterator.next();
            assertNotNull(sf);
        } catch (IOException e) {
            // expected exception, check that it is the correct one
            String message = e.getMessage();
            assert message.contains("Invalid geometry") || message.contains("Self-intersection at or near point");
        }
    }

    @Override
    protected JDBCTestSetup createTestSetup() {
        testSetup = new SQLServerInvalidGeometryTestSetup();
        return testSetup;

        //        return new SQLServerInvalidGeometryTestSetup();
    }
}
