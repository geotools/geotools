/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.postgis;

import java.io.IOException;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class PostGISJsonOnlineTest extends JDBCTestSupport {

    PostGISJsonTestSetup pgJsonTestSetup;

    @Override
    protected JDBCTestSetup createTestSetup() {
        pgJsonTestSetup = new PostGISJsonTestSetup();
        return pgJsonTestSetup;
    }

    @Test
    public void testJsonAttributesMappedToString() throws Exception {
        String name = "numberEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        SimpleFeatureType featureType = feature.getFeatureType();
        AttributeDescriptor descriptor = featureType.getDescriptor("jsonColumn");
        assertTrue(String.class.equals(descriptor.getType().getBinding()));

        if (pgJsonTestSetup.supportJsonB) {
            descriptor = featureType.getDescriptor("jsonbColumn");
            assertTrue(String.class.equals(descriptor.getType().getBinding()));
        }
    }

    @Test
    public void testNumberEntry() throws Exception {
        String name = "numberEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");

        assertTrue(jsonColumnValue.contains("1e-3"));

        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            // in JSONB, numbers will be printed according to the behavior of the underlying numeric
            // type.
            // In practice this means that numbers entered with E notation will be printed without
            // it
            assertTrue(jsonbColumnValue.contains("0.001"));
        }
    }

    @Test
    public void testEntryWithSpaces() throws Exception {
        SimpleFeature feature = getSingleFeatureByName("entryWithSpaces");
        assertEquals("{\"title\"    :    \"Title\" }", feature.getAttribute("jsonColumn"));
        // JSONB does not preserve white spaces
        if (pgJsonTestSetup.supportJsonB) {
            assertEquals("{\"title\": \"Title\"}", feature.getAttribute("jsonbColumn"));
        }
    }

    @Test
    public void testDuppedKeyEntry() throws Exception {
        String name = "duppedKeyEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertTrue(jsonColumnValue.contains("Title1"));
        assertTrue(jsonColumnValue.contains("Title2"));

        // JSONB does not keep duplicate object keys
        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            assertFalse(jsonbColumnValue.contains("Title1"));
            assertTrue(jsonbColumnValue.contains("Title2"));
        }
    }

    @Test
    public void testNullKeyEntry() throws Exception {
        String name = "nullKey";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertTrue(jsonColumnValue.contains("{\"title\":null}"));
    }

    @Test
    public void testNullEntry() throws Exception {
        String name = "nullEntry";
        SimpleFeature feature = getSingleFeatureByName(name);
        String jsonColumnValue = (String) feature.getAttribute("jsonColumn");
        assertNull(jsonColumnValue);
        if (pgJsonTestSetup.supportJsonB) {
            String jsonbColumnValue = (String) feature.getAttribute("jsonbColumn");
            assertNull(jsonbColumnValue);
        }
    }

    private SimpleFeature getSingleFeatureByName(String name) throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("jsontest"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal(name), true);
        Query q = new Query(tname("jsontest"), filter);
        return DataUtilities.first(fs.getFeatures(q));
    }
}
