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

import java.util.List;
import org.geotools.data.FeatureWriter;
import org.geotools.data.Transaction;
import org.opengis.feature.simple.SimpleFeature;

/**
 * JDBC {@link FeatureWriter}-implementation tampers with input-data.
 *
 * @author Burkhard Strauss
 * @see GEOT-6264
 */
public abstract class JDBCFeatureWriterOnlineTest extends JDBCTestSupport {

    public void testNext() throws Exception {

        try (FeatureWriter writer =
                dataStore.getFeatureWriter(tname("ft1"), Transaction.AUTO_COMMIT)) {
            assertTrue(writer.hasNext());
            final SimpleFeature feature = (SimpleFeature) writer.next();
            assertTrue(feature.getAttribute(0).toString().equals("POINT (0 0)"));
            assertTrue((Integer) feature.getAttribute(1) == 0);
            assertTrue((Double) feature.getAttribute(2) == 0.0);
            assertTrue(((String) feature.getAttribute(3)).equals("zero"));
        }
        try (FeatureWriter writer =
                dataStore.getFeatureWriter(tname("ft1"), Transaction.AUTO_COMMIT)) {
            assertTrue(writer.hasNext());
            final SimpleFeature feature = (SimpleFeature) writer.next();
            final List<Object> attributes = feature.getAttributes();
            for (int k = 0; k < attributes.size(); k++) {
                assertTrue(attributes.get(k) != null);
            }
        }
    }
}
