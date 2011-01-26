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
package org.geotools.data.wfs.v1_0_0;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.MalformedURLException;
import java.net.NoRouteToHostException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Level;

import org.geotools.data.DataStore;
import org.geotools.data.DataUtilities;
import org.geotools.data.DefaultQuery;
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
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.feature.IllegalAttributeException;
import org.geotools.feature.simple.SimpleFeatureBuilder;
import org.geotools.filter.IllegalFilterException;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.Before;
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
import org.xml.sax.SAXException;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Envelope;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.MultiPolygon;
import com.vividsolutions.jts.geom.Polygon;

public class GeoServerOnlineTest {

    public static final String SERVER_URL = "http://localhost:8080/geoserver/wfs?service=WFS&request=GetCapabilities&version=1.0.0";

    public static final String TO_EDIT_TYPE = "topp:states";

    public static final String ATTRIBUTE_TO_EDIT = "STATE_FIPS";

    public static final String NEW_EDIT_VALUE = "newN";

    private static final int EPSG_CODE = 4326;

    private URL url = null;

    @Before
    public void setUp() throws MalformedURLException {
        url = new URL(SERVER_URL);
        if (url != null && url.toString().indexOf("localhost") != -1) {
            InputStream stream = null;
            try {
                stream = url.openStream();
            } catch (Throwable t) {
                System.err.println("Warning you local geoserver is not available. test disabled ");
                url = null;
            } finally {
                if (stream != null)
                    try {
                        stream.close();
                    } catch (IOException e) {
                        // whatever
                    }
            }
        }
    }

    @Test
    public void testTypes() throws IOException, NoSuchElementException {
        if (url == null)
            return;
        WFS_1_0_0_DataStore wfs;
        try {
            wfs = WFSDataStoreReadTest.getDataStore(url);
        } catch (ConnectException e) {
            e.printStackTrace(System.err);
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
            return;
        } catch (NoRouteToHostException e) {
            e.printStackTrace(System.err);
            return;
        }
        String types[] = wfs.getTypeNames();
        String typeName = "unknown";
        for (int i = 0; i < types.length; i++) {
            typeName = types[i];
            if (typeName.equals("topp:geometrytype"))
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

            DefaultQuery query = new DefaultQuery(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES,
                    "work already");
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
    public void testSingleType() throws IOException, NoSuchElementException {
        if (url == null)
            return;
        WFS_1_0_0_DataStore wfs;
        try {
            wfs = WFSDataStoreReadTest.getDataStore(url);
        } catch (ConnectException e) {
            e.printStackTrace(System.err);
            return;
        } catch (UnknownHostException e) {
            e.printStackTrace(System.err);
            return;
        } catch (NoRouteToHostException e) {
            e.printStackTrace(System.err);
            return;
        }
        String typeName = "tiger:poi";
        SimpleFeatureType type = wfs.getSchema(typeName);
        type.getTypeName();
        type.getName().getNamespaceURI();

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();
        // features.getFeatureType();

        DefaultQuery query = new DefaultQuery(typeName, Filter.INCLUDE, 20, Query.ALL_NAMES,
                "work already");
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

    public void XtestFeatureType() throws NoSuchElementException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureType(url, true, true, 0);
    }

    @Test
    public void testFeatureReader() throws NoSuchElementException, IOException,
            IllegalAttributeException, SAXException {
        WFSDataStoreReadTest.doFeatureReader(url, true, true, 0);
    }

    @Test
    public void testFeatureReaderWithFilter() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureReaderWithQuery(url, true, true, 0);
    }

    @Test
    public void testFeatureReaderWithFilterGET() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureReaderWithQuery(url, true, false, 0);
    }

    /**
     * {@link BBOX} support?
     */
    @Test
    public void testDataStoreSupportsPlainBBOXInterface() throws Exception {
        if( url == null) return;
        final WFS_1_0_0_DataStore wfs = WFSDataStoreReadTest.getDataStore(url);
        final SimpleFeatureType ft = wfs.getSchema(TO_EDIT_TYPE);
        final ReferencedEnvelope bounds = wfs.getFeatureSource(TO_EDIT_TYPE).getBounds();
        
        final FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        final BBOX bbox = ff.bbox("the_geom", bounds.getMinX(), bounds.getMinY(), bounds.getMaxX(), bounds.getMaxY(), null);
        
        /**
         * This one does not implement the deprecated geotools filter interfaces
         */
        final BBOX strictBBox = new BBOX() {
            
            public boolean evaluate(Object object) {
                return bbox.evaluate(object);
            }
            
            public Object accept(FilterVisitor visitor, Object extraData) {
                return bbox.accept(visitor, extraData);
            }
            
            public Expression getExpression2() {
                return bbox.getExpression2();
            }
            
            public Expression getExpression1() {
                return bbox.getExpression1();
            }
            
            public String getSRS() {
                return bbox.getSRS();
            }
            
            public String getPropertyName() {
                return bbox.getPropertyName();
            }
            
            public double getMinY() {
                return bbox.getMinY();
            }
            
            public double getMinX() {
                return bbox.getMinX();
            }
            
            public double getMaxY() {
                return bbox.getMaxY();
            }
            
            public double getMaxX() {
                return bbox.getMaxX();
            }
        };
        
        final DefaultQuery query = new DefaultQuery(ft.getTypeName());
        query.setFilter(strictBBox);
        
        FeatureReader<SimpleFeatureType, SimpleFeature> reader;
        
        reader = wfs.getFeatureReaderGet(query, Transaction.AUTO_COMMIT);        
        assertNotNull(reader);

        reader = wfs.getFeatureReaderPost(query, Transaction.AUTO_COMMIT);        
        assertNotNull(reader);
    }

    @Test
    public void testFeatureReaderWithFilterPOST() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException {
        WFSDataStoreReadTest.doFeatureReaderWithQuery(url, false, true, 0);
    }

    // RR change the data?
    // NOPE, it's in Lat-Long for the Env, BCAlbers for the data
    @Test
    public void testFeatureReaderWithFilterBBoxGET() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException, IllegalFilterException {
        // minx,miny,maxx,maxy
        if (url == null)
            return;

        Map m = new HashMap();
        m.put(WFSDataStoreFactory.URL.key, url);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
        DataStore post = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory()).createDataStore(m);
        String typeName = post.getTypeNames()[0];

        Envelope bbox = post.getFeatureSource(typeName).getBounds();
        WFSDataStoreReadTest.doFeatureReaderWithBBox(url, true, false, 0, bbox);
    }

    @Test
    public void testFeatureReaderWithFilterBBoxPOST() throws NoSuchElementException,
            IllegalAttributeException, IOException, SAXException, IllegalFilterException {
        if (url == null)
            return;

        Map m = new HashMap();
        m.put(WFSDataStoreFactory.URL.key, url);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
        DataStore post = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory()).createDataStore(m);

        String typeName = post.getTypeNames()[0];
        Envelope bbox = post.getFeatureSource(typeName).getBounds();

        WFSDataStoreReadTest.doFeatureReaderWithBBox(url, true, false, 0, bbox);
    }

    /**
     * Tests case where filter is makes use of 2 different attributes but Query object only requests
     * 1 of the two attributes. This is a fix for a bug that has occurred.
     */
    @Test
    public void testFeatureReaderWithQuery() throws Exception {
        if (url == null)
            return;
        Map m = new HashMap();
        m.put(WFSDataStoreFactory.URL.key, url);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(100000));
        WFS_1_0_0_DataStore wfs = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory())
                .createDataStore(m);
        FilterFactory2 fac = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());

        Filter filter = fac.equals(fac.property("NAME"), fac.literal("E 58th St"));

        Query query = new DefaultQuery("tiger:tiger_roads", filter);
        FeatureReader<SimpleFeatureType, SimpleFeature> reader = wfs.getFeatureReader(query,
                new DefaultTransaction());
        int expected = 0;
        while (reader.hasNext()) {
            expected++;
            reader.next();
        }
        query = new DefaultQuery("tiger:tiger_roads", filter, 100, new String[] { "CFCC" }, "");
        reader = wfs.getFeatureReader(query, new DefaultTransaction());
        int count = 0;
        while (reader.hasNext()) {
            count++;
            reader.next();
        }

        assertEquals(expected, count);
    }

    /**
     * Writing test that only engages against a remote geoserver.
     * <p>
     * Makes reference to the standard featureTypes that geoserver ships with.
     * </p>
     */
    @Test
    public void testWrite() throws NoSuchElementException, IllegalFilterException, IOException,
            IllegalAttributeException {
        if (url == null)
            return;

        Map m = new HashMap();
        m.put(WFSDataStoreFactory.URL.key, url);
        m.put(WFSDataStoreFactory.TIMEOUT.key, new Integer(10000000));
        DataStore post = (WFS_1_0_0_DataStore) (new WFSDataStoreFactory()).createDataStore(m);
        String typename = TO_EDIT_TYPE;
        SimpleFeatureType ft = post.getSchema(typename);
        SimpleFeatureSource fs = post.getFeatureSource(typename);
        class Watcher implements FeatureListener {
            public int count = 0;

            public void changed(FeatureEvent featureEvent) {
                System.out.println("Event " + featureEvent);
                count++;
            }
        }
        Watcher watcher = new Watcher();
        fs.addFeatureListener(watcher);

        Id startingFeatures = createFidFilter(fs);
        FilterFactory2 filterFac = CommonFactoryFinder
                .getFilterFactory2(GeoTools.getDefaultHints());
        try {
            GeometryFactory gf = new GeometryFactory();
            MultiPolygon mp = gf.createMultiPolygon(new Polygon[] { gf.createPolygon(gf
                    .createLinearRing(new Coordinate[] { new Coordinate(-88.071564, 37.51099),
                            new Coordinate(-88.467644, 37.400757),
                            new Coordinate(-90.638329, 42.509361),
                            new Coordinate(-89.834618, 42.50346),
                            new Coordinate(-88.071564, 37.51099) }), new LinearRing[] {}) });
            mp.setUserData("http://www.opengis.net/gml/srs/epsg.xml#" + EPSG_CODE);

            PropertyName geometryAttributeExpression = filterFac.property(ft
                    .getGeometryDescriptor().getLocalName());
            PropertyIsNull geomNullCheck = filterFac.isNull(geometryAttributeExpression);
            Query query = new DefaultQuery(typename, filterFac.not(geomNullCheck), 1,
                    Query.ALL_NAMES, null);
            SimpleFeatureIterator inStore = fs.getFeatures(query).features();

            SimpleFeature f, f2;
            try {
                SimpleFeature feature = inStore.next();

                SimpleFeature copy = SimpleFeatureBuilder.deep(feature);
                SimpleFeature copy2 = SimpleFeatureBuilder.deep(feature);

                f = SimpleFeatureBuilder.build(ft, copy.getAttributes(), null);
                f2 = SimpleFeatureBuilder.build(ft, copy2.getAttributes(), null);
                assertFalse("Max Feature failed", inStore.hasNext());
            } finally {
                inStore.close();
            }

            org.geotools.util.logging.Logging.getLogger("org.geotools.data.wfs").setLevel(
                    Level.FINE);
            SimpleFeatureCollection inserts = DataUtilities
                    .collection(new SimpleFeature[] { f, f2 });
            Id fp = WFSDataStoreWriteOnlineTest.doInsert(post, ft, inserts);

            // / okay now count ...
            FeatureReader<SimpleFeatureType, SimpleFeature> count = post.getFeatureReader(
                    new DefaultQuery(ft.getTypeName()), Transaction.AUTO_COMMIT);
            int i = 0;
            while (count.hasNext() && i < 3) {
                f = count.next();
                i++;
            }
            count.close();

            WFSDataStoreWriteOnlineTest.doDelete(post, ft, fp);
            WFSDataStoreWriteOnlineTest.doUpdate(post, ft, ATTRIBUTE_TO_EDIT, NEW_EDIT_VALUE);
            // assertFalse("events not fired", watcher.count == 0);
        } finally {
            try {
                ((SimpleFeatureStore) fs).removeFeatures(filterFac
                        .not(startingFeatures));
            } catch (Exception e) {
                System.out.println(e);
            }
        }
    }

    private Id createFidFilter(SimpleFeatureSource fs)
            throws IOException {
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
