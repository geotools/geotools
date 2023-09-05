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

import static org.junit.Assert.assertEquals;

import org.geotools.api.data.FeatureReader;
import org.geotools.api.data.Query;
import org.geotools.api.data.Transaction;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.junit.Test;

public abstract class JDBCBooleanOnlineTest extends JDBCTestSupport {

    @Override
    protected abstract JDBCBooleanTestSetup createTestSetup();

    @Test
    public void testGetSchema() throws Exception {
        SimpleFeatureType ft = dataStore.getSchema(tname("b"));
        assertEquals(Boolean.class, ft.getDescriptor("boolProperty").getType().getBinding());
    }

    @Test
    public void testGetFeatures() throws Exception {
        try (FeatureReader r =
                dataStore.getFeatureReader(new Query(tname("b")), Transaction.AUTO_COMMIT)) {
            r.hasNext();
            SimpleFeature f = (SimpleFeature) r.next();
            assertEquals(Boolean.FALSE, f.getAttribute("boolProperty"));

            r.hasNext();
            f = (SimpleFeature) r.next();
            assertEquals(Boolean.TRUE, f.getAttribute("boolProperty"));
        }
    }
}
