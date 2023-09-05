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
package org.geotools.data.sqlserver;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.Query;
import org.geotools.api.data.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.jdbc.JDBCFeatureSourceOnlineTest;
import org.geotools.jdbc.JDBCTestSetup;

public class SQLServerFeatureSourceOnlineTest extends JDBCFeatureSourceOnlineTest {

    @Override
    protected JDBCTestSetup createTestSetup() {
        return new SqlServerNativeSerializationTestSetup();
    }

    @Override
    public void testGetFeaturesWithOffset() throws Exception {
        SimpleFeatureSource featureSource = dataStore.getFeatureSource(tname("ft_from"));
        Query q = new Query(featureSource.getSchema().getTypeName());
        q.setPropertyNames(aname("ORIGIN_FROM"));
        q.setStartIndex(1);
        q.setMaxFeatures(1);
        SimpleFeatureCollection features = featureSource.getFeatures(q);

        // check size
        assertEquals(1, features.size());

        // check actual iteration
        int count = 0;
        try (SimpleFeatureIterator it = features.features()) {
            assertTrue(it.hasNext());
            it.next();
            count++;
        }
        assertEquals(1, count);
    }
}
