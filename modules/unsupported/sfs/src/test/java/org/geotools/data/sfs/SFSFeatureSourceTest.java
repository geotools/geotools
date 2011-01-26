/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.sfs;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.AttributeDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;


public class SFSFeatureSourceTest extends OnlineTest {

    /* layerName for mock service*/
    private static final String FEATURESOURCE = "layerAsia";

    public SFSFeatureSourceTest(String testName) {
        super(testName);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
    }

    @Override
    protected void tearDown() throws Exception {
        super.tearDown();
    }

    public void testGetFeatureSource() throws MalformedURLException, IOException, NoSuchAuthorityCodeException, FactoryException {
        if (super.onlineTest("testGetFeatureSource")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());
            SFSFeatureSource odsfs = (SFSFeatureSource) ods.getFeatureSource(FEATURESOURCE);
            assertNotNull(odsfs);
        }
    }

    public void testGetSchema() throws MalformedURLException, IOException, NoSuchAuthorityCodeException, FactoryException {

        if (super.onlineTest("testGetSchema")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());
            SFSFeatureSource odsfs = (SFSFeatureSource) ods.getFeatureSource(FEATURESOURCE);
            SimpleFeatureType type = odsfs.getSchema();
            assertEquals(NAMESPACE, type.getName().getNamespaceURI());
            assertEquals(3, type.getAttributeCount());
            Set<String> attributes = new HashSet<String>();
            for (AttributeDescriptor ad : type.getAttributeDescriptors()) {
                attributes.add(ad.getLocalName());
            }
            assertEquals(3, attributes.size());
            assertTrue(attributes.contains("NAME"));
            assertTrue(attributes.contains("the_geom"));
            assertTrue(attributes.contains("CFCC"));
        }
    }

    public void testCountFeatures() throws MalformedURLException, IOException, NoSuchAuthorityCodeException, FactoryException {
        if (super.onlineTest("testCountFeatures")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());
            SFSFeatureSource odsfs = (SFSFeatureSource) ods.getFeatureSource(FEATURESOURCE);

            int count = odsfs.getCount(new Query(FEATURESOURCE, Filter.INCLUDE));
            assertEquals(2, count);
        }
    }

    public void testFeatureBounds() throws MalformedURLException, IOException, NoSuchAuthorityCodeException, FactoryException {
        if (super.onlineTest("testFeatureBounds")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());
            SFSFeatureSource odsfs = (SFSFeatureSource) ods.getFeatureSource(FEATURESOURCE);

            ReferencedEnvelope env = odsfs.getBoundsInternal(new Query(FEATURESOURCE, Filter.INCLUDE));
            /* Remember the axis order is flipped so this also test if flipaxis is working or not*/
            assertEquals(-40.0, env.getMinX());
            assertEquals(-10.0, env.getMinY());
            assertEquals(80.0, env.getMaxX());
            assertEquals(30.0, env.getMaxY());
        }
    }

    public void testGetFeatureReader() throws Exception {
        if (super.onlineTest("testGetFeatureReader")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());

            FeatureReader<SimpleFeatureType, SimpleFeature> r = ods.getFeatureReader(new Query(FEATURESOURCE), Transaction.AUTO_COMMIT);
            assertNotNull(r);
            assertTrue(r.hasNext());
        }
    }
    
    public void testGetFeatureReaderWithOrFilter() throws Exception {
        if (super.onlineTest("testGetFeatureReader")) {
            SFSDataStoreFactory factory = new SFSDataStoreFactory();
            SFSDataStore ods = (SFSDataStore) factory.createDataStore(createParams());
            
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);
            Filter f1 = ff.equals(ff.property("CFCC"), ff.literal("A41"));
            Filter f2 = ff.equals(ff.property("CFCC"), ff.literal("A42"));
            Filter ored = ff.or(f1, f2);

            FeatureReader<SimpleFeatureType, SimpleFeature> r = ods.getFeatureReader(new Query(FEATURESOURCE, ored), Transaction.AUTO_COMMIT);
            assertNotNull(r);
            assertTrue(r.hasNext());
            r.next();
            assertTrue(r.hasNext());
            r.close();
        }
    }

    public void testOfflineGetFeatureSource() throws Exception {
        String _jsonText = "[{"
                + "   \"name\": \"layerAsia\","
                + "   \"bbox\": [-10,-40,30,80],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:4326\","
                + "   \"axisorder\": \"yx\"},"
                + "{"
                + "   \"name\": \"layerAmerica\","
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" ,"
                + "   \"axisorder\": \"xy\" },"
                + "{"
                + "   \"name\": \"layerEurope\","
                + "   \"bbox\": [15000000,49000000,18000000,52000000],"
                + "   \"crs\": \"urn:ogc:def:crs:EPSG:32632\" "
                + " }]";

        SFSDataStore ods = new SFSDataStore(_jsonText, null);

        SFSFeatureSource odsfs = (SFSFeatureSource) ods.getFeatureSource(FEATURESOURCE);
        assertNotNull(odsfs);
    }
}
