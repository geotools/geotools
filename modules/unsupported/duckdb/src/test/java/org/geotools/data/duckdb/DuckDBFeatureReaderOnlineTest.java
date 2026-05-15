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
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;
import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.type.PropertyDescriptor;
import org.geotools.jdbc.JDBCFeatureReaderOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;

public class DuckDBFeatureReaderOnlineTest extends JDBCFeatureReaderOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new DuckDBTestSetup();
    }

    @Override
    @Test
    public void testNext() throws Exception {
        Query query = new Query(tname("ft1"));
        try (FeatureReader reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(reader.hasNext());
            SimpleFeature feature = (SimpleFeature) reader.next();

            assertEquals(feature.getName(), reader.getFeatureType().getName());
            int i = 0;
            for (PropertyDescriptor desc : reader.getFeatureType().getDescriptors()) {
                assertTrue(i < feature.getAttributeCount());
                assertEquals(feature.getAttribute(desc.getName()), feature.getAttribute(i++));
            }

            List<Object> attrs = feature.getAttributes();
            try {
                attrs.set(2, 1.1D);
                feature.setAttributes(attrs);
            } catch (Exception e) {
                fail();
            }
            assertEquals(attrs.get(2), feature.getAttribute(2));

            Geometry g = (Geometry) feature.getDefaultGeometry();
            assertNotNull(g);
        }
    }
}
