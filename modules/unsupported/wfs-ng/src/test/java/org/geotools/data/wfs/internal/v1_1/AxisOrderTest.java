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

import static org.geotools.data.wfs.WFSTestData.url;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.TestHttpClient;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.TestWFSClient;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.filter.identity.FeatureIdImpl;
import org.geotools.geometry.jts.JTS;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.filter.Filter;
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

    private QName qTypeName = new QName("http://www.tinyows.org/", "comuni11", "comuni");

    private FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2();

    @Test
    public void testGetFeatureWithNorthEastAxisOrderOutputEPSG4326() throws Exception {
        Set<FeatureId> fids = new HashSet<>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        TestWFSClient wfs = createWFSClient();
        wfs.mockGetFeatureRequest(
                url("axisorder/GetFeatureById4326.xml"), qTypeName, query.getFilter());

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        WFSDataStore ds = new WFSDataStore(wfs);

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1);
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
        feature = iterate(source.getFeatures(query), 1);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, y, 0);
        assertEquals(41.587164718505285, x, 0);
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testGetFeatureWithEastNorthAxisOrderFilter() throws Exception {
        final Filter bbox =
                ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857");

        TestWFSClient wfs = createWFSClient();
        wfs.mockGetFeatureRequest(url("axisorder/GetFeaturesByBBox.xml"), qTypeName, bbox);

        WFSDataStore ds = new WFSDataStore(wfs);

        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT,
                WFSDataStoreFactory.AXIS_ORDER_EAST_NORTH);

        Query query = new Query(typeName, bbox);

        ds.getFeatureSource(typeName).getFeatures(query).features();

        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates are NOT inverted
        assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(
                                4623055.0, 4629904.0, 815134.0, 820740.0, CRS.decode("EPSG:3857")),
                        filter.getBounds(),
                        1e-6));
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testGetFeatureWithNorthEastAxisOrderFilter() throws Exception {
        TestWFSClient wfs = createWFSClient();

        wfs.mockGetFeatureRequest(
                url("axisorder/GetFeaturesByBBox.xml"),
                qTypeName,
                ff.bbox("the_geom", 815134.0, 4623055.0, 820740.0, 4629904.0, "EPSG:3857"));

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT,
                WFSDataStoreFactory.AXIS_ORDER_NORTH_EAST);

        WFSDataStore ds = new WFSDataStore(wfs);

        Query query =
                new Query(
                        typeName,
                        ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857"));

        ds.getFeatureSource(typeName).getFeatures(query).features();

        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates ARE inverted (EPSG:3857 is EAST/NORTH, so if we ask for NORTH/EAST,
        // the filter
        // should be inverted)
        assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(
                                815134.0, 820740.0, 4623055.0, 4629904.0, CRS.decode("EPSG:3857")),
                        filter.getBounds(),
                        0));
    }

    @Test
    @SuppressWarnings("PMD.SimplifiableTestAssertion")
    public void testGetFeatureWithCompliantAxisOrderFilter() throws Exception {
        final Filter bbox =
                ff.bbox("the_geom", 4623055.0, 815134.0, 4629904.0, 820740.0, "EPSG:3857");

        TestWFSClient wfs = createWFSClient();

        WFSDataStore ds = new WFSDataStore(wfs);
        wfs.mockGetFeatureRequest(url("axisorder/GetFeaturesByBBox.xml"), qTypeName, bbox);

        // axis order used will be NORTH / EAST
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        Query query = new Query(typeName, bbox);

        ds.getFeatureSource(typeName).getFeatures(query).features();

        BBOX filter = (BBOX) wfs.getRequest().getFilter();

        // filter coordinates are NOT inverted
        assertTrue(
                JTS.equals(
                        new ReferencedEnvelope(
                                4623055.0, 4629904.0, 815134.0, 820740.0, CRS.decode("EPSG:3857")),
                        filter.getBounds(),
                        1e-6));
    }

    @Test
    public void testGetFeatureWithEastNorthAxisOrderOutputEPSG4326() throws Exception {
        Set<FeatureId> fids = new HashSet<>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        TestWFSClient wfs = createWFSClient();
        wfs.mockGetFeatureRequest(
                url("axisorder/GetFeatureById4326.xml"), qTypeName, query.getFilter());

        WFSDataStore ds = new WFSDataStore(wfs);
        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1);
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
        feature = iterate(source.getFeatures(query), 1);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(7.344559874483752, x, 0);
        assertEquals(41.587164718505285, y, 0);
    }

    @Test
    public void testGetFeatureWithEastNorthAxisOrderOutputEPSG3857() throws Exception {
        Set<FeatureId> fids = new HashSet<>();
        fids.add(new FeatureIdImpl("comuni11.2671"));
        Query query = new Query(typeName, ff.id(fids));

        TestWFSClient wfs = createWFSClient();
        wfs.mockGetFeatureRequest(
                url("axisorder/GetFeatureById.xml"), qTypeName, query.getFilter());

        WFSDataStore ds = new WFSDataStore(wfs);
        // axis order used will be EAST / NORTH
        wfs.setAxisOrderOverride(
                WFSDataStoreFactory.AXIS_ORDER_COMPLIANT, WFSDataStoreFactory.AXIS_ORDER_COMPLIANT);

        SimpleFeatureSource source = ds.getFeatureSource(typeName);
        SimpleFeature feature = iterate(source.getFeatures(query), 1);
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
        feature = iterate(source.getFeatures(query), 1);
        geometry = (Geometry) feature.getDefaultGeometry();
        x = geometry.getCoordinate().x;
        y = geometry.getCoordinate().y;
        assertEquals(4629462.0, x, 0);
        assertEquals(819841.0, y, 0);
    }

    private TestWFSClient createWFSClient()
            throws ServiceException, FileNotFoundException, IOException {

        URL capabilitiesUrl =
                new URL("http://127.0.0.1/cgi-bin/tinyows?REQUEST=GetCapabilities&SERVICE=WFS");

        TestHttpClient httpClient = new TestHttpClient();
        httpClient.expectGet(
                capabilitiesUrl,
                new TestHttpResponse(
                        TestData.url(WFSTestData.class, "axisorder/GetCapabilities.xml"),
                        "text/xml"));

        TestWFSClient wfs = new TestWFSClient(capabilitiesUrl, httpClient);
        wfs.mockDescribeFeatureTypeRequest(
                url("axisorder/DescribeFeatureType.xsd"),
                new QName("http://www.tinyows.org/", "comuni11", "comuni"));

        return wfs;
    }

    private static SimpleFeature iterate(SimpleFeatureCollection features, int expectedSize) {
        int size = 0;

        SimpleFeature sf = null;
        try (SimpleFeatureIterator reader = features.features()) {
            while (reader.hasNext()) {
                if (sf == null) {
                    sf = reader.next();
                } else {
                    reader.next().getIdentifier();
                }
                size++;
            }
        }

        assertEquals(expectedSize, size);

        return sf;
    }
}
