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
package org.geotools.data.h2;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCDataStoreAPIOnlineTest;
import org.geotools.jdbc.JDBCDataStoreAPITestSetup;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.AttributeType;

public class H2DataStoreAPITest extends JDBCDataStoreAPIOnlineTest {
    @Override
    protected JDBCDataStoreAPITestSetup createTestSetup() {
        return new H2DataStoreAPITestSetup();
    }

    @Override
    public void testTransactionIsolation() throws Exception {
        // JD: h2 does table level locking so this test fails.
    }

    @Override
    public void testGetFeatureWriterConcurrency() throws Exception {
        // Blocks and times out
    }

    @Test
    public void testGetComments() throws Exception {
        // H2 comment retrieval is always on, so this should work
        ContentFeatureSource featureSource = dataStore.getFeatureSource(tname("lake"));
        SimpleFeatureType simpleFeatureType = featureSource.getSchema();
        AttributeDescriptor attributeDescriptor = simpleFeatureType.getDescriptor("name");
        AttributeType attributeType = attributeDescriptor.getType();
        assertEquals("This is a text column", attributeType.getDescription().toString());
        AttributeDescriptor attributeDescriptor2 = simpleFeatureType.getDescriptor("geom");
        AttributeType attributeType2 = attributeDescriptor2.getType();
        assertNull(attributeType2.getDescription()); // no comment on GEOM
    }
}
