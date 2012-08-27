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
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultTransaction;
import org.geotools.data.FeatureEvent;
import org.geotools.data.FeatureListener;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.simple.SimpleFeatureStore;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.impl.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.FilterVisitor;
import org.opengis.filter.Id;
import org.opengis.filter.PropertyIsNull;
import org.opengis.filter.expression.Expression;
import org.opengis.filter.expression.PropertyName;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX;
import org.opengis.geometry.BoundingBox;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

/**
 * 
 * 
 * @source $URL$
 */
public class GeoServerOnlineTest {

    private static final FilterFactory2 FF = CommonFactoryFinder.getFilterFactory2(GeoTools
            .getDefaultHints());

    public static final String SERVER_URL_100 = "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.0.0";

    public static final String SERVER_URL_110 = "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.1.0";

    public static final String TO_EDIT_TYPE = "topp_states";

    public static final String ATTRIBUTE_TO_EDIT = "STATE_FIPS";

    public static final String NEW_EDIT_VALUE = "newN";

    private static final int EPSG_CODE = 4326;

    private URL url_100;

    private URL url_110;

    private WFSContentDataStore wfs100;

    private WFSContentDataStore wfs110;

    @Before
    public void setUp() throws Exception {
        url_100 = new URL(SERVER_URL_100);
        url_110 = new URL(SERVER_URL_110);
        if (url_100 != null && url_100.toString().indexOf("localhost") != -1) {
            InputStream stream = null;
            try {
                stream = url_100.openStream();
            } catch (Throwable t) {
                System.err.println("Warning you local geoserver is not available. test disabled ");
                url_100 = null;
                url_110 = null;
            } finally {
                IOUtils.closeQuietly(stream);
            }
        }
        if (url_100 != null) {
            Map<String, Serializable> params;

            params = new HashMap<String, Serializable>();
            params.put(WFSDataStoreFactory.URL.key, url_100);

            wfs100 = new WFSDataStoreFactory().createDataStore(params);

            params = new HashMap<String, Serializable>();
            params.put(WFSDataStoreFactory.URL.key, url_110);
            wfs110 = new WFSDataStoreFactory().createDataStore(params);

            assertEquals("1.0.0", wfs100.getInfo().getVersion());
            assertEquals("1.1.0", wfs110.getInfo().getVersion());
        }
    }

    @After
    public void tearDown() throws Exception {
        if (url_100 != null) {
            wfs100.dispose();
            wfs110.dispose();
        }
    }

    @Test
    public void testTypesWFS_1_0_0() throws IOException, NoSuchElementException {
        testTypes(wfs100);
    }

    @Test
    public void testTypesWFS_1_1_0() throws IOException, NoSuchElementException {
        testTypes(wfs110);
    }

    private void testTypes(DataStore wfs) throws IOException, NoSuchElementException {
        if (url_100 == null)
            return;

        String types[] = wfs.getTypeNames();
        String typeName = "unknown";
        for (int i = 0; i < types.length; i++) {
            typeName = types[i];
            if (typeName.equals("topp_geometrytype"))
                continue;
            SimpleFeatureType type = wfs.getSchema(typeName);
            type.getTypeName();
            type.getName().getNamespaceURI();

            SimpleFeatureSource source = wfs.getFeatureSource(typeName);
            source.getBounds();

            SimpleFeatureCollection features = source.getFeatures();
            features.getBounds();
            features.getSchema();
            // features.getFeatureType();

            Query query = new Query(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES, "work already");
            features = source.getFeatures(query);
            features.size();
            Iterator reader = features.iterator();
            while (reader.hasNext()) {
                SimpleFeature feature = (SimpleFeature) reader.next();
            }
            features.close(reader);

            SimpleFeatureIterator iterator = features.features();
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
            }
            features.close(iterator);
        }
    }

    @Test
    public void testSingleType_WFS_1_0() throws IOException, NoSuchElementException {
        testSingleType(wfs100);
    }

    @Test
    public void testSingleType_WFS_1_1() throws IOException, NoSuchElementException {
        testSingleType(wfs110);
    }

    private void testSingleType(DataStore wfs) throws IOException, NoSuchElementException {
        if (url_100 == null)
            return;

        String typeName = "tiger_poi";
        SimpleFeatureType type = wfs.getSchema(typeName);
        type.getTypeName();
        type.getName().getNamespaceURI();

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();
        // features.getFeatureType();

        Query query = new Query(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES, "work already");
        features = source.getFeatures(query);
        features.size();

        Iterator reader = features.iterator();
        while (reader.hasNext()) {
            SimpleFeature feature = (SimpleFeature) reader.next();
            System.out.println(feature);
        }
        features.close(reader);

        SimpleFeatureIterator iterator = features.features();
        while (iterator.hasNext()) {
            SimpleFeature feature = iterator.next();
        }
        features.close(iterator);
    }

//    public void XtestFeatureType() throws NoSuchElementException, IOException, SAXException {
//        WFSDataStoreReadTest.doFeatureType(url_100, true, true, 0);
//    }
//
//    @Test
//    public void testFeatureReader() throws NoSuchElementException, IOException,
//            IllegalAttributeException, SAXException {
//        WFSDataStoreReadTest.doFeatureReader(url_100, true, true, 0);
//    }
//
//    @Test
//    public void testFeatureReaderWithFilter() throws NoSuchElementException,
//            IllegalAttributeException, IOException, SAXException {
//        WFSDataStoreReadTest.doFeatureReaderWithQuery(url_100, true, true, 0);
//    }
//
//    @Test
//    public void testFeatureReaderWithFilterGET() throws NoSuchElementException,
//            IllegalAttributeException, IOException, SAXException {
//        WFSDataStoreReadTest.doFeatureReaderWithQuery(url_100, true, false, 0);
//    }
//
//    @Test
//    public void testSupportsPlainBBOXInterface_100() throws Exception {
//        testDataStoreSupportsPlainBBOXInterface(wfs100);
//    }
//
//    @Test
//    public void testSupportsPlainBBOXInterface_110() throws Exception {
//        testDataStoreSupportsPlainBBOXInterface(wfs110);
//    }

    /**
     * {@link BBOX} support?
     */
    private void testDataStoreSupportsPlainBBOXInterface(final DataStore wfs) throws Exception {
        if (url_100 == null)
            return;

        final SimpleFeatureType ft = wfs.getSchema(TO_EDIT_TYPE);
        SimpleFeatureSource featureSource = wfs.getFeatureSource(TO_EDIT_TYPE);
        final ReferencedEnvelope bounds = featureSource.getBounds();

        String srsName = CRS.toSRS(bounds.getCoordinateReferenceSystem());

        final BBOX bbox = FF.bbox("the_geom", bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(),
                bounds.getMaxY(), srsName);

        /**
         * This one does not implement the deprecated geotools filter interfaces
         */
        final BBOX strictBBox = new BBOX() {

            @Override
            public boolean evaluate(Object object) {
                return bbox.evaluate(object);
            }

            @Override
            public Object accept(FilterVisitor visitor, Object extraData) {
                return bbox.accept(visitor, extraData);
            }

            @Override
            public Expression getExpression2() {
                return bbox.getExpression2();
            }

            @Override
            public Expression getExpression1() {
                return bbox.getExpression1();
            }

            @Override
            public String getSRS() {
                return bbox.getSRS();
            }

            @Override
            public String getPropertyName() {
                return bbox.getPropertyName();
            }

            @Override
            public double getMinY() {
                return bbox.getMinY();
            }

            @Override
            public double getMinX() {
                return bbox.getMinX();
            }

            @Override
            public double getMaxY() {
                return bbox.getMaxY();
            }

            @Override
            public double getMaxX() {
                return bbox.getMaxX();
            }

            @Override
            public MatchAction getMatchAction() {
                return MatchAction.ANY;
            }
        };

        final Query query = new Query(ft.getTypeName());
        query.setPropertyNames(new String[] { "the_geom" });
        query.setFilter(strictBBox);

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(reader);

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(reader);
    }

    @Test
    public void testDataStoreHandlesAxisFlipping_1_0_0() throws Exception {
        testDataStoreHandlesAxisFlipping(wfs100);
    }

    @Test
    public void testDataStoreHandlesAxisFlipping_1_1_0() throws Exception {
        testDataStoreHandlesAxisFlipping(wfs110);
    }

    private void testDataStoreHandlesAxisFlipping(final DataStore wfs) throws Exception {
        if (url_100 == null)
            return;

        final SimpleFeatureType ft = wfs.getSchema(TO_EDIT_TYPE);
        final SimpleFeatureSource featureSource = wfs.getFeatureSource(TO_EDIT_TYPE);
        final ReferencedEnvelope bounds = featureSource.getBounds();

        CoordinateReferenceSystem wgs84LonLat = CRS.decode("EPSG:4326", true);
        CoordinateReferenceSystem wgs84LatLon = CRS.decode("EPSG:4326", false);

        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(wgs84LonLat));
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(wgs84LatLon));

        ReferencedEnvelope lonLat = bounds.transform(wgs84LonLat, true);
        ReferencedEnvelope latLon = bounds.transform(wgs84LatLon, true);

        String latLonSrsName = "urn:x-ogc:def:crs:EPSG:4326";
        String lonLatSrsName = "EPSG:4326";

        final BBOX lonLatFilter = FF.bbox("the_geom", lonLat.getMinimum(0), lonLat.getMinimum(1),
                lonLat.getMaximum(0), lonLat.getMaximum(1), lonLatSrsName);

        final BBOX latLonFiler = FF.bbox("the_geom", latLon.getMinimum(0), latLon.getMinimum(1),
                latLon.getMaximum(0), latLon.getMaximum(1), latLonSrsName);

        final Query query = new Query(ft.getTypeName());
        query.setPropertyNames(new String[] { "the_geom" });
        query.setFilter(lonLatFilter);

        final int expectedCount = wfs.getFeatureSource(query.getTypeName()).getFeatures().size();

        FeatureReader<SimpleFeatureType, SimpleFeature> reader;

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(expectedCount, count);
        } finally {
            reader.close();
        }

        query.setFilter(latLonFiler);

        reader = wfs.getFeatureReader(query, Transaction.AUTO_COMMIT);
        try {
            assertTrue(reader.hasNext());
            int count = 0;
            while (reader.hasNext()) {
                reader.next();
                count++;
            }
            assertEquals(expectedCount, count);
        } finally {
            reader.close();
        }
    }

//    @Test
//    public void testFeatureReaderWithFilterPOST() throws Exception {
//        WFSDataStoreReadTest.doFeatureReaderWithQuery(url_100, false, true, 0);
//    }

//    @Test
//    public void testFeatureReaderWithFilterBBoxGET() throws Exception {
//        // minx,miny,maxx,maxy
//        if (url_100 == null)
//            return;
//
//        Map<String, Serializable> m = new HashMap<String, Serializable>();
//        m.put(WFSDataStoreFactory.URL.key, url_100);
//        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
//        m.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);// favor GET over POST
//
//        DataStore get = new WFSDataStoreFactory().createDataStore(m);
//
//        String typeName = get.getTypeNames()[0];
//
//        Envelope bbox = get.getFeatureSource(typeName).getBounds();
//        WFSDataStoreReadTest.doFeatureReaderWithBBox(url_100, true, false, 0, bbox);
//    }

//    @Test
//    public void testFeatureReaderWithFilterBBoxPOST() throws Exception {
//        if (url_100 == null)
//            return;
//
//        Map m = new HashMap();
//        m.put(WFSDataStoreFactory.URL.key, url_100);
//        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
//        DataStore post = new WFSDataStoreFactory().createDataStore(m);
//
//        String typeName = post.getTypeNames()[0];
//        Envelope bbox = post.getFeatureSource(typeName).getBounds();
//
//        WFSDataStoreReadTest.doFeatureReaderWithBBox(url_100, true, false, 0, bbox);
//    }

//    /**
//     * Tests case where filter is makes use of 2 different attributes but Query object only requests
//     * 1 of the two attributes. This is a fix for a bug that has occurred.
//     */
//    @Test
//    public void testFeatureReaderWithQuery() throws Exception {
//        if (url_100 == null)
//            return;
//        Map m = new HashMap();
//        m.put(WFSDataStoreFactory.URL.key, url_100);
//        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
//        WFS_1_0_0_DataStore wfs = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory())
//                .createDataStore(m);
//
//        Filter filter = FF.equals(FF.property("NAME"), FF.literal("E 58th St"));
//
//        Query query = new Query("tiger_tiger_roads", filter);
//        FeatureReader<SimpleFeatureType, SimpleFeature> reader = wfs.getFeatureReader(query,
//                new DefaultTransaction());
//        int expected = 0;
//        while (reader.hasNext()) {
//            expected++;
//            reader.next();
//        }
//        query = new Query("tiger_tiger_roads", filter, 100, new String[] { "CFCC" }, "");
//        reader = wfs.getFeatureReader(query, new DefaultTransaction());
//        int count = 0;
//        while (reader.hasNext()) {
//            count++;
//            reader.next();
//        }
//
//        assertEquals(expected, count);
//    }

//    /**
//     * Writing test that only engages against a remote geoserver.
//     * <p>
//     * Makes reference to the standard featureTypes that geoserver ships with.
//     * </p>
//     * NOTE: Ignoring this test for now because it edits topp:states and GeoServer doesn't return
//     * the correct Feature IDs on transactions against shapefiles
//     */
//    @Test
//    @Ignore
//    public void testWrite() throws NoSuchElementException, IllegalFilterException, IOException,
//            IllegalAttributeException {
//        if (url_100 == null)
//            return;
//
//        Map m = new HashMap();
//        m.put(WFSDataStoreFactory.URL.key, url_100);
//        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(10000000));
//        DataStore post = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory()).createDataStore(m);
//        String typename = TO_EDIT_TYPE;
//        SimpleFeatureType ft = post.getSchema(typename);
//        SimpleFeatureSource fs = post.getFeatureSource(typename);
//        class Watcher implements FeatureListener {
//            public int count = 0;
//
//            public void changed(FeatureEvent featureEvent) {
//                System.out.println("Event " + featureEvent);
//                count++;
//            }
//        }
//        Watcher watcher = new Watcher();
//        fs.addFeatureListener(watcher);
//
//        Id startingFeatures = createFidFilter(fs);
//        FilterFactory2 filterFac = CommonFactoryFinder
//                .getFilterFactory2(GeoTools.getDefaultHints());
//        try {
//            GeometryFactory gf = new GeometryFactory();
//            MultiPolygon mp = gf.createMultiPolygon(new Polygon[] { gf.createPolygon(
//                    gf.createLinearRing(new Coordinate[] { new Coordinate(-88.071564, 37.51099),
//                            new Coordinate(-88.467644, 37.400757),
//                            new Coordinate(-90.638329, 42.509361),
//                            new Coordinate(-89.834618, 42.50346),
//                            new Coordinate(-88.071564, 37.51099) }), new LinearRing[] {}) });
//            mp.setUserData("http://www.opengis.net/gml/srs/epsg.xml#" + EPSG_CODE);
//
//            PropertyName geometryAttributeExpression = filterFac.property(ft
//                    .getGeometryDescriptor().getLocalName());
//            PropertyIsNull geomNullCheck = filterFac.isNull(geometryAttributeExpression);
//            Query query = new Query(typename, filterFac.not(geomNullCheck), 1, Query.ALL_NAMES,
//                    null);
//            SimpleFeatureIterator inStore = fs.getFeatures(query).features();
//
//            SimpleFeature f, f2;
//            try {
//                SimpleFeature feature = inStore.next();
//
//                SimpleFeature copy = SimpleFeatureBuilder.deep(feature);
//                SimpleFeature copy2 = SimpleFeatureBuilder.deep(feature);
//
//                f = SimpleFeatureBuilder.build(ft, copy.getAttributes(), null);
//                f2 = SimpleFeatureBuilder.build(ft, copy2.getAttributes(), null);
//                assertFalse("Max Feature failed", inStore.hasNext());
//            } finally {
//                inStore.close();
//            }
//
//            org.geotools.util.logging.Logging.getLogger("org.geotools.data.wfs").setLevel(
//                    Level.FINE);
//            SimpleFeatureCollection inserts = DataUtilities
//                    .collection(new SimpleFeature[] { f, f2 });
//            Id fp = WFSDataStoreWriteOnlineTest.doInsert(post, ft, inserts);
//
//            // / okay now count ...
//            FeatureReader<SimpleFeatureType, SimpleFeature> count = post.getFeatureReader(
//                    new Query(ft.getTypeName()), Transaction.AUTO_COMMIT);
//            int i = 0;
//            while (count.hasNext() && i < 3) {
//                f = count.next();
//                i++;
//            }
//            count.close();
//
//            WFSDataStoreWriteOnlineTest.doDelete(post, ft, fp);
//            WFSDataStoreWriteOnlineTest.doUpdate(post, ft, ATTRIBUTE_TO_EDIT, NEW_EDIT_VALUE);
//            // assertFalse("events not fired", watcher.count == 0);
//        } finally {
//            try {
//                ((SimpleFeatureStore) fs).removeFeatures(filterFac.not(startingFeatures));
//            } catch (Exception e) {
//                System.out.println(e);
//            }
//        }
//    }
//
    private Id createFidFilter(SimpleFeatureSource fs) throws IOException {
        SimpleFeatureIterator iter = fs.getFeatures().features();
        FilterFactory2 ffac = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Set fids = new HashSet();
        try {
            while (iter.hasNext()) {
                String id = iter.next().getID();
                FeatureId fid = ffac.featureId(id);
                fids.add(fid);
            }
            Id filter = ffac.id(fids);
            return filter;
        } finally {
            iter.close();
        }
    }
}
