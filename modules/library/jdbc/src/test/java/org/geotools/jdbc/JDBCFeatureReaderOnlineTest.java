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
package org.geotools.jdbc;

import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.type.PropertyDescriptor;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public abstract class JDBCFeatureReaderOnlineTest extends JDBCTestSupport {

    public void testNext() throws Exception {
        Query query = new Query(tname("ft1"));
        try (FeatureReader reader = dataStore.getFeatureReader(query, Transaction.AUTO_COMMIT)) {
            assertTrue(reader.hasNext());
            SimpleFeature feature = (SimpleFeature) reader.next();

            // feature tests
            // test getName
            assertEquals(feature.getName(), reader.getFeatureType().getName());
            // test getters
            int i = 0;
            for (PropertyDescriptor desc : reader.getFeatureType().getDescriptors()) {
                assertTrue(i < feature.getAttributeCount());
                assertEquals(feature.getAttribute(desc.getName()), feature.getAttribute(i++));
            }
            // test set
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

            assertTrue(g.getUserData() instanceof CoordinateReferenceSystem);
        }
    }
}
