/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_1;

import static org.geotools.data.wfs.WFSTestData.MockHTTPClient;
import static org.geotools.data.wfs.WFSTestData.createTestProtocol;
import static org.geotools.data.wfs.WFSTestData.stream;
import static org.geotools.data.wfs.WFSTestData.url;
import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.WFSTestData.TestWFSClient;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.FilterFactory2;
import org.opengis.filter.identity.FeatureId;
import org.opengis.filter.spatial.BBOX;

/**
 * Tests axis order flipping handling.
 *
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 * @author Niels Charlier
 */
public class AxisOrderTest {

    private String typeName = "comuni_comuni11";

    @Test
    public void testGetFeatureWithNorthEastAxisOrderOutputEPSG4326() throws Exception {

        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeatureById4326.xml"));

        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1, true);
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        double x = geometry.getCoordinate().x;
        double y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, y, 0);
        assertEquals(41.587164718505285, x, 0);

        // specify axis order: results should be the same
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST,
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        source = ds.getFeatureSource(typeName);
        feature = iterate(source.getFeatures(query), 1, true);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, y, 0);
        assertEquals(41.587164718505285, x, 0);
    }

    @Test
    public void testGetFeatureWithNorthEastAxisOrderFilter() throws Exception {

        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeaturesByBBox.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);

        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT,
                WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query =
                new Query(
                        typeName,
                        ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857"));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        source.getFeatures(query).features();
        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates are inverted
        assertEquals(815134.0, filter.getMinX(), 0.0);
        assertEquals(4623055.0, filter.getMinY(), 0.0);
        assertEquals(820740.0, filter.getMaxX(), 0.0);
        assertEquals(4629904.0, filter.getMaxY(), 0.0);
    }

    @Test
    public void testGetFeatureWithEastNorthAxisOrderFilter() throws Exception {

        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeaturesByBBox.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT,
                WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query =
                new Query(
                        typeName,
                        ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857"));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        source.getFeatures(query).features();
        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates are NOT inverted
        assertEquals(4623055.0, filter.getMinX(), 0.0);
        assertEquals(815134.0, filter.getMinY(), 0.0);
        assertEquals(4629904.0, filter.getMaxX(), 0.0);
        assertEquals(820740.0, filter.getMaxY(), 0.0);
    }

    @Test
    public void testGetFeatureWithCompliantAxisOrderFilter() throws Exception {

        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeaturesByBBox.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query =
                new Query(
                        typeName,
                        ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857"));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        source.getFeatures(query).features();
        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates are NOT inverted
        assertEquals(4623055.0, filter.getMinX(), 0.0);
        assertEquals(815134.0, filter.getMinY(), 0.0);
        assertEquals(4629904.0, filter.getMaxX(), 0.0);
        assertEquals(820740.0, filter.getMaxY(), 0.0);
    }

    @Test
    public void testGetFeatureWithEastNorthAxisOrderOutputEPSG4326() throws Exception {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeatureById4326.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);
        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1, true);
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        double x = geometry.getCoordinate().x;
        double y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, y, 0);
        assertEquals(41.587164718505285, x, 0);

        // specify axis order: results should be inverted
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH,
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        source = ds.getFeatureSource(typeName);
        feature = iterate(source.getFeatures(query), 1, true);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, x, 0);
        assertEquals(41.587164718505285, y, 0);
    }

    @Test
    public void testGetFeatureWithEastNorthAxisOrderOutputEPSG3857() throws Exception {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        stream("axisorder/GetFeatureById.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        "axisorder/GetCapabilities.xml", new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(url("axisorder/DescribeFeatureType.xsd"));

        WFSDataStore ds = new WFSDataStore(wfs);
        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();
        Set<FeatureId> fids = new HashSet<FeatureId>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1, true);
        Geometry geometry = (Geometry) feature.getDefaultGeometry();
        double x = geometry.getCoordinate().x;
        double y = geometry.getCoordinate().y;
        assertEquals(4629462.0, x, 0);
        assertEquals(819841.0, y, 0);

        // specify axis order: results should be inverted
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH,
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        source = ds.getFeatureSource(typeName);
        feature = iterate(source.getFeatures(query), 1, true);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(4629462.0, x, 0);
        assertEquals(819841.0, y, 0);
    }

    private static SimpleFeature iterate(
            SimpleFeatureCollection features, int expectedSize, boolean getSize) {
        int size = -1;
        if (getSize) {
            size = features.size();
            if (size > -1) {
                assertEquals(expectedSize, size);
            }
        }

        size = 0;
        SimpleFeatureIterator reader = features.features();
        SimpleFeature sf = null;
        try {
            while (reader.hasNext()) {
                if (sf == null) {
                    sf = reader.next();
                } else {
                    reader.next().getIdentifier();
                }
                size++;
            }
        } finally {
            reader.close();
        }

        assertEquals(expectedSize, size);

        return sf;
    }
}
