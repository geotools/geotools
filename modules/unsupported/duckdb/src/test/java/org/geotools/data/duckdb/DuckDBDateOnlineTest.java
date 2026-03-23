/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.duckdb;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCDateOnlineTest;
import org.geotools.jdbc.JDBCDateTestSetup;
import org.junit.Test;

public class DuckDBDateOnlineTest extends JDBCDateOnlineTest {

    @Override
    protected JDBCDateTestSetup createTestSetup() {
        return new DuckDBDateTestSetup();
    }

    @Test
    public void testDateAttributesUseJdbcDateInstances() throws Exception {
        try (SimpleFeatureIterator features =
                dataStore.getFeatureSource(tname("dates")).getFeatures().features()) {
            SimpleFeature feature = features.next();
            Object value = feature.getAttribute(aname("d"));

            assertEquals(Date.class, value.getClass());
        }
    }
}
