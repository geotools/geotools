/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.jdbc;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

/**
 * JDBC {@link FeatureWriter}-implementation tampers with input-data.
 *
 * @author Burkhard Strauss
 * @see GEOT-6264
 */
public abstract class JDBCFeatureWriterOnlineTest extends JDBCTestSupport {

    @Test
    public void testNext() throws Exception {

        try (FeatureWriter writer =
                dataStore.getFeatureWriter(tname("ft1"), Transaction.AUTO_COMMIT)) {
            assertTrue(writer.hasNext());
            final SimpleFeature feature = (SimpleFeature) writer.next();
            assertEquals("POINT (0 0)", feature.getAttribute(0).toString());
            assertEquals(0, (int) (Integer) feature.getAttribute(1));
            assertEquals(0.0, (double) (Double) feature.getAttribute(2), 0.0);
            assertEquals("zero", feature.getAttribute(3));
        }
        try (FeatureWriter writer =
                dataStore.getFeatureWriter(tname("ft1"), Transaction.AUTO_COMMIT)) {
            assertTrue(writer.hasNext());
            final SimpleFeature feature = (SimpleFeature) writer.next();
            final List<Object> attributes = feature.getAttributes();
            for (Object attribute : attributes) {
                assertNotNull(attribute);
            }
        }
    }
}
