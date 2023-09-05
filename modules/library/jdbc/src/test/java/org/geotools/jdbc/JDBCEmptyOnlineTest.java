/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2009, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.geotools.api.data.FeatureSource;
import org.geotools.api.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Test;

public abstract class JDBCEmptyOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCEmptyTestSetup createTestSetup();

    @Test
    public void testFeatureSource() throws Exception {

        FeatureSource fs = dataStore.getFeatureSource(tname("empty"));
        assertNotNull(fs);

        ReferencedEnvelope bounds = fs.getBounds();
        assertTrue(bounds.isNull());

        int count = fs.getCount(Query.ALL);
        assertEquals(0, count);
    }

    @Test
    public void testFeatureCollection() throws Exception {
        FeatureSource fs = dataStore.getFeatureSource(tname("empty"));
        FeatureCollection features = fs.getFeatures();

        assertTrue(features.getBounds().isNull());
        assertEquals(0, features.size());

        try (FeatureIterator i = features.features()) {
            assertFalse(i.hasNext());
        }
    }
}
