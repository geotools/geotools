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
import java.util.logging.Logger;
import org.geotools.data.DataUtilities;
import org.geotools.data.Query;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.jdbc.JDBCTestSetup;
import org.geotools.jdbc.JDBCTestSupport;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.PropertyIsEqualTo;

public class PostGISHStoreOnlineTest extends JDBCTestSupport {

    private static final Logger LOGGER = Logger.getLogger(PostGISHStoreOnlineTest.class.getName());

    private PostGISHStoreTestSetup pgHstoreSetup;

    @Override
    protected JDBCTestSetup createTestSetup() {
        pgHstoreSetup = new PostGISHStoreTestSetup();
        return pgHstoreSetup;
    }

    private boolean skipTests() {
        return pgHstoreSetup.hasException;
    }

    @Test
    public void testReportingException() throws Exception {
        if (skipTests()) {
            LOGGER.warning("HSTORE tests will be skipped due to previous exception");
        }
    }

    @Test
    public void testSinglePair() throws Exception {
        if (skipTests()) {
            return;
        }
        String name = "singlepair";
        Object object = getHstoreColumnForFeatureWithName(name);
        assertNotNull(object);
        assertTrue(object instanceof HStore);
        HStore hstore = (HStore) object;
        assertTrue(hstore.size() == 1);
        assertTrue(hstore.containsKey("key1"));
        assertTrue(hstore.get("key1").equals("value1"));
        LOGGER.info(name + " hstore content: " + hstore.toString());
    }

    @Test
    public void testDoublePair() throws Exception {
        if (skipTests()) {
            return;
        }
        String name = "doublepair";
        Object object = getHstoreColumnForFeatureWithName(name);
        assertNotNull(object);
        assertTrue(object instanceof HStore);
        HStore hstore = (HStore) object;
        assertTrue(hstore.size() == 2);
        assertTrue(hstore.containsKey("key2"));
        assertTrue(hstore.get("key2").equals("value2"));
        assertTrue(hstore.containsKey("key3"));
        assertTrue(hstore.get("key3").equals("value3"));
        LOGGER.info(name + " hstore content: " + hstore.toString());
    }

    @Test
    public void testPairWithNullValue() throws Exception {
        if (skipTests()) {
            return;
        }
        String name = "pairwithnullvalue";
        Object object = getHstoreColumnForFeatureWithName(name);
        assertNotNull(object);
        assertTrue(object instanceof HStore);
        HStore hstore = (HStore) object;
        assertTrue(hstore.size() == 1);
        assertTrue(hstore.containsKey("key4"));
        assertNull(hstore.get("key4"));
        LOGGER.info(name + " hstore content: " + hstore.toString());
    }

    @Test
    public void testEmpty() throws Exception {
        if (skipTests()) {
            return;
        }
        String name = "emptycontent";
        Object object = getHstoreColumnForFeatureWithName(name);
        assertNotNull(object);
        assertTrue(object instanceof HStore);
        HStore hstore = (HStore) object;
        assertTrue(hstore.isEmpty());
        LOGGER.info(name + " hstore content: " + hstore.toString());
    }

    @Test
    public void testNullEntry() throws Exception {
        if (skipTests()) {
            return;
        }
        Object object = getHstoreColumnForFeatureWithName("nullcontent");
        assertNull(object);
    }

    private Object getHstoreColumnForFeatureWithName(String name) throws IOException {
        SimpleFeature feature = getSingleFeatureByName(name);
        return feature.getAttribute("mapping");
    }

    private SimpleFeature getSingleFeatureByName(String name) throws IOException {
        ContentFeatureSource fs = dataStore.getFeatureSource(tname("hstoretest"));
        FilterFactory ff = dataStore.getFilterFactory();
        PropertyIsEqualTo filter = ff.equal(ff.property(aname("name")), ff.literal(name), true);
        Query q = new Query(tname("hstoretest"), filter);
        return DataUtilities.first(fs.getFeatures(q));
    }
}
