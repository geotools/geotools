/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import static org.geotools.data.DataUtilities.createType;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Arrays;
import java.util.List;
import javax.xml.namespace.QName;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.memory.MemoryFeatureCollection;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.store.ContentFeatureCollection;
import org.geotools.data.store.ContentFeatureSource;
import org.geotools.data.wfs.integration.IntegrationTestWFSClient;
import org.geotools.data.wfs.internal.WFSException;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.feature.NameImpl;
import org.geotools.feature.simple.SimpleFeatureImpl;
import org.geotools.filter.identity.FeatureIdImpl;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;

public class WFSFeatureStoreTest {

    private static final QName TYPE1 = new QName("http://www.census.gov", "poi", "tiger");

    private static SimpleFeatureType featureType1;

    private static Name simpleTypeName1;

    private WFSDataStore dataStore;

    private IntegrationTestWFSClient wfs;

    @BeforeClass
    public static void oneTimeSetUp() throws Exception {
        simpleTypeName1 =
                new NameImpl(
                        TYPE1.getNamespaceURI(), TYPE1.getPrefix() + "_" + TYPE1.getLocalPart());

        featureType1 =
                createType(
                        "http://example.com/1",
                        simpleTypeName1.getLocalPart(),
                        "the_geom:Point:srid=4326,NAME:String,THUMBNAIL:String,MAINPAGE:String");
    }

    @Before
    public void setUp() throws Exception {
        wfs =
                new IntegrationTestWFSClient(
                        "GeoServer_1.7.x/1.0.0/", WFSTestData.getGmlCompatibleConfig());
        dataStore = new WFSDataStore(wfs);
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testAddFeaturesAutoCommit() throws Exception {
        GeometryFactory geomfac = new GeometryFactory(new PrecisionModel(10));
        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();

        ContentFeatureSource source =
                (ContentFeatureSource) dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSFeatureStore);

        WFSFeatureStore store = (WFSFeatureStore) source;

        MemoryFeatureCollection collection = new MemoryFeatureCollection(featureType1);

        Coordinate insideCoord = new Coordinate(5.2, 7.5);
        Point myPoint = geomfac.createPoint(insideCoord);

        SimpleFeature feat =
                new SimpleFeatureImpl(
                        Arrays.asList(
                                new Object[] {myPoint, "mypoint", "pics/x.jpg", "pics/y.jpg"}),
                        featureType1,
                        new FeatureIdImpl("myid"));

        collection.add(feat);

        List<FeatureId> fids = store.addFeatures((SimpleFeatureCollection) collection);

        assertEquals(1, fids.size());

        ContentFeatureCollection coll = store.getFeatures();
        assertEquals(4, coll.size());

        coll =
                store.getFeatures(
                        new Query(
                                simpleTypeName1.getLocalPart(),
                                filterfac.equals(
                                        filterfac.property("NAME"), filterfac.literal("mypoint"))));
        assertEquals(1, coll.size());

        SimpleFeature feature = coll.features().next();
        assertEquals(feat.getAttributes(), feature.getAttributes());
    }

    @Test
    public void testRemoveFeaturesAutoCommit() throws Exception {

        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();

        ContentFeatureSource source =
                (ContentFeatureSource) dataStore.getFeatureSource(simpleTypeName1);

        WFSFeatureStore store = (WFSFeatureStore) source;

        Filter filter = filterfac.id(filterfac.featureId("poi.2"));

        store.removeFeatures(filter);

        ContentFeatureCollection coll = store.getFeatures();
        assertEquals(2, coll.size());

        coll = store.getFeatures(new Query(simpleTypeName1.getLocalPart(), filter));
        assertEquals(0, coll.size());
    }

    @Test
    public void testUpdateFeaturesAutoCommit() throws Exception {

        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();

        ContentFeatureSource source =
                (ContentFeatureSource) dataStore.getFeatureSource(simpleTypeName1);

        WFSFeatureStore store = (WFSFeatureStore) source;

        Filter filter = filterfac.id(filterfac.featureId("poi.2"));

        store.modifyFeatures("NAME", "blah", filter);

        ContentFeatureCollection coll =
                store.getFeatures(new Query(simpleTypeName1.getLocalPart(), filter));
        assertEquals(1, coll.size());

        SimpleFeature feature = coll.features().next();
        assertEquals("blah", feature.getAttribute("NAME"));
    }

    @Test
    public void testTransaction() throws Exception {
        GeometryFactory geomfac = new GeometryFactory(new PrecisionModel(10));
        FilterFactory2 filterfac = CommonFactoryFinder.getFilterFactory2();

        ContentFeatureSource source =
                (ContentFeatureSource) dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSFeatureStore);

        WFSFeatureStore store = (WFSFeatureStore) source;

        MemoryFeatureCollection collection = new MemoryFeatureCollection(featureType1);

        Coordinate insideCoord = new Coordinate(5.2, 7.5);
        Point myPoint = geomfac.createPoint(insideCoord);

        SimpleFeature feat =
                new SimpleFeatureImpl(
                        Arrays.asList(
                                new Object[] {myPoint, "mypoint", "pics/x.jpg", "pics/y.jpg"}),
                        featureType1,
                        new FeatureIdImpl("myid"));

        collection.add(feat);

        Transaction transaction = new DefaultTransaction();
        store.setTransaction(transaction);

        List<FeatureId> fids = store.addFeatures((SimpleFeatureCollection) collection);
        assertEquals(1, fids.size());

        Filter filterRemove = filterfac.id(filterfac.featureId("poi.2"));
        store.removeFeatures(filterRemove);

        Filter filterUpdate = filterfac.id(filterfac.featureId("poi.3"));
        store.modifyFeatures("NAME", "blah", filterUpdate);

        transaction.commit();

        ContentFeatureCollection coll = store.getFeatures();
        SimpleFeatureIterator it = coll.features();
        // while (it.hasNext()) {
        // System.err.println(it.next());
        // }
        assertEquals(3, coll.size());

        coll =
                store.getFeatures(
                        new Query(
                                simpleTypeName1.getLocalPart(),
                                filterfac.equals(
                                        filterfac.property("NAME"), filterfac.literal("mypoint"))));
        it = coll.features();
        // while (it.hasNext()) {
        // System.err.println(it.next());
        // }
        assertEquals(1, coll.size());

        SimpleFeature feature = coll.features().next();
        assertEquals(feat.getAttributes(), feature.getAttributes());

        coll = store.getFeatures(new Query(simpleTypeName1.getLocalPart(), filterRemove));
        assertEquals(0, coll.size());

        coll = store.getFeatures(new Query(simpleTypeName1.getLocalPart(), filterUpdate));
        assertEquals(1, coll.size());

        feature = coll.features().next();
        assertEquals("blah", feature.getAttribute("NAME"));
    }

    /** Tests that WFS Transactions causing an ExceptionReport also end in a proper WFSException. */
    @Test
    public void testTransactionExceptionReport() throws IOException {
        // makes test to use GeoServer_1.7.x/1.0.0/TransactionFailure_poi.xml
        wfs.setFailOnTransaction(true);

        ContentFeatureSource source =
                (ContentFeatureSource) dataStore.getFeatureSource(simpleTypeName1);
        assertNotNull(source);
        assertTrue(source instanceof WFSFeatureStore);

        WFSFeatureStore store = (WFSFeatureStore) source;

        MemoryFeatureCollection collection = new MemoryFeatureCollection(featureType1);

        SimpleFeature feat =
                new SimpleFeatureImpl(
                        Arrays.asList(new Object[] {null, "mypoint", "pics/x.jpg", "pics/y.jpg"}),
                        featureType1,
                        new FeatureIdImpl("myid"));

        collection.add(feat);

        Transaction transaction = new DefaultTransaction();
        store.setTransaction(transaction);

        List<FeatureId> fids = store.addFeatures((SimpleFeatureCollection) collection);
        assertEquals(1, fids.size());

        try {
            transaction.commit();
        } catch (WFSException e) {
            // WFS 1.0.0: Parser fails to parse the response properly
            // probably because the parser configuration for WFS 1.0 misses
            // ExceptionReport bindings.
            // So here, in WFS 1.0 case textual information is available only.
            StringWriter writer = new StringWriter();
            PrintWriter printer = new PrintWriter(writer);
            e.printStackTrace(printer);
            printer.close();
            assertTrue(writer.toString().contains("MyErrorMessage"));
            return;
        }
        Assert.fail("Expected WFSException.");
    }
}
