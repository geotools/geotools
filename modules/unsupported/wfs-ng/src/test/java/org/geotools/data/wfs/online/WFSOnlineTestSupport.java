/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
 *    (C) 2005, David Zwiers
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
package org.geotools.data.wfs.online;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.geotools.data.DataStore;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.impl.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.filter.FilterFactoryFinder;
import org.geotools.filter.IllegalFilterException;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.expression.PropertyName;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Envelope;

/**
 */
public class WFSOnlineTestSupport {

    public void testEmpty() {/**/
    }

    public static WFSContentDataStore getDataStore(URL server, Boolean post) throws IOException {

        Map<String, Serializable> m = new HashMap<String, Serializable>();
        m.put(WFSDataStoreFactory.URL.key, server);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(10000)); // not debug
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(1000000)); // for debug

        if (post != null) {
            m.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.valueOf(post));
        }

        return new WFSDataStoreFactory().createDataStore(m);

    }

    public static void doFeatureType(DataStore wfs, String typeName) throws Exception {

        assertNotNull("No featureTypes", wfs.getTypeNames());

        // post
        SimpleFeatureType ft = wfs.getSchema(typeName);
        assertNotNull("DescribeFeatureType for " + typeName + " resulted in null", ft);

        GeometryDescriptor geometryDescriptor = ft.getGeometryDescriptor();
        List<AttributeDescriptor> attributeDescriptors = ft.getAttributeDescriptors();
        int attributeCount = ft.getAttributeCount();

        assertNotNull("CRS missing ", geometryDescriptor.getCoordinateReferenceSystem());

        assertTrue("POST " + typeName
                + " must have 1 geom and atleast 1 other attribute -- fair assumption",
                geometryDescriptor != null && attributeDescriptors != null && attributeCount > 0);

    }

    public static void doFeatureReader(DataStore wfs, String typeName) throws Exception {
        assertNotNull("No featureTypes", wfs.getTypeNames());

        Query query = new Query(typeName);
        query.setMaxFeatures(5);
        
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertNotNull("FeatureType was null", reader);
            assertTrue("must have 1 feature -- fair assumption", reader.hasNext());

            SimpleFeature next = reader.next();
            assertNotNull(next);
        } finally {
            reader.close();
        }
    }

    public static void doFeatureReaderWithQuery(DataStore wfs, String typeName) throws Exception {

        SimpleFeatureType ft = wfs.getSchema(typeName);
        // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs

        String[] props;
        props = new String[] { ft.getGeometryDescriptor().getLocalName() };

        Query query = new Query(ft.getTypeName());
        query.setPropertyNames(props);
        String fid = null;
        if (get) {
            // get
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,
                    Transaction.AUTO_COMMIT);
            try {
                assertNotNull("FeatureType was null", ft);

                SimpleFeatureType featureType = fr.getFeatureType();
                if (ft.getAttributeCount() > 1) {
                    assertEquals("Query must restrict feature type to only having 1 AttributeType",
                            1, featureType.getAttributeCount());
                }
                assertTrue("must have 1 feature -- fair assumption", fr.hasNext()
                        && featureType != null);
                SimpleFeature feature = fr.next();
                featureType = feature.getFeatureType();
                if (ft.getAttributeCount() > 1) {
                    assertEquals("Query must restrict feature type to only having 1 AttributeType",
                            1, featureType.getAttributeCount());
                }
                assertNotNull("must have 1 feature ", feature);
                fid = feature.getID();
                int j = 0;
                while (fr.hasNext()) {
                    fr.next();
                    j++;
                }
                System.out.println(j + " Features");
            } finally {
                fr.close();
            }
        }

        // test fid filter
        query.setFilter(FilterFactoryFinder.createFilterFactory().createFidFilter(fid));
        if (get) {
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,
                    Transaction.AUTO_COMMIT);
            try {
                assertNotNull("FeatureType was null", ft);
                int j = 0;
                while (fr.hasNext()) {
                    assertEquals(fid, fr.next().getID());
                    j++;
                }
                assertEquals(1, j);
            } finally {
                fr.close();
            }
        }
        if (post) {
            FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderPost(query,
                    Transaction.AUTO_COMMIT);
            try {
                assertNotNull("FeatureType was null", ft);
                int j = 0;
                while (fr.hasNext()) {
                    assertEquals(fid, fr.next().getID());
                    j++;
                }
                assertEquals(1, j);
            } finally {
                fr.close();
            }
        }
    }

    /**
     * Request a subset of available properties
     * 
     * @throws IllegalFilterException
     */
    public static void doFeatureReaderWithBBox(URL url, boolean get, boolean post, int i,
            Envelope bbox) throws NoSuchElementException, IllegalAttributeException, IOException,
            SAXException, IllegalFilterException {
        if (url == null)
            return; // test disabled (must be site specific)
        try {
            System.out.println("FeatureReaderWithFilterTest + " + url);
            WFS_1_0_0_DataStore wfs = getDataStore(url);
            assertNotNull("No featureTypes", wfs.getTypeNames());

            String typeName = wfs.getTypeNames()[i];
            assertNotNull("Null featureType in [0]", typeName);
            SimpleFeatureType featureType = wfs.getSchema(typeName);

            // take atleast attributeType 3 to avoid the undeclared one .. inherited optional attrs
            FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
            Query query = new Query(featureType.getTypeName());
            PropertyName theGeom = ff.property(featureType.getGeometryDescriptor().getName());
            Filter filter = ff.bbox(theGeom, bbox.getMinX(), bbox.getMinY(), bbox.getMaxX(),
                    bbox.getMaxY(), "EPSG:4326");

            query.setFilter(filter);
            // query.setMaxFeatures(3);
            if (get) {
                // get
                FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderGet(query,
                        Transaction.AUTO_COMMIT);
                assertNotNull("GET " + typeName + " FeatureType was null", featureType);

                assertTrue("GET " + typeName + " has next?", fr.hasNext());
                assertNotNull("GET " + typeName + " has feature type", fr.getFeatureType());

                SimpleFeature feature = fr.next();

                assertNotNull("GET " + typeName + " has non null feature", feature);

                int j = 0;
                while (fr.hasNext()) {
                    fr.next();
                    j++;
                }
                System.out.println("bbox selected " + j + " Features");
                fr.close();
            }
            if (post) {
                // post

                FeatureReader<SimpleFeatureType, SimpleFeature> fr = wfs.getFeatureReaderPost(
                        query, Transaction.AUTO_COMMIT);
                assertNotNull("POST " + typeName + "FeatureType was null", featureType);
                assertTrue("POST " + typeName + "must have 1 feature -- fair assumption",
                        fr.hasNext() && fr.getFeatureType() != null && fr.next() != null);
                int j = 0;
                while (fr.hasNext()) {
                    fr.next();
                    j++;
                }
                System.out.println("bbox selected " + j + " Features");
                fr.close();
            }
        } catch (java.net.SocketException se) {
            se.printStackTrace();
        }
    }
}
