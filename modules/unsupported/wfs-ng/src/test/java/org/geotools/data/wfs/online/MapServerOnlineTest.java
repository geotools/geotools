/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.TreeSet;
import org.geotools.data.DataStore;
import org.geotools.data.DataStoreFinder;
import org.geotools.data.Query;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.WFSDataStoreFactory;
import org.geotools.factory.CommonFactoryFinder;
import org.geotools.factory.GeoTools;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.GeometryDescriptor;
import org.opengis.filter.Filter;
import org.opengis.filter.FilterFactory;
import org.opengis.filter.FilterFactory2;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @source $URL$ */
public class MapServerOnlineTest {

    public static final String SERVER_URL_100 =
            "http://demo.mapserver.org/cgi-bin/wfs?SERVICE=WFS&VERSION=1.0.0&REQUEST=GetCapabilities";

    public static final String SERVER_URL_110 =
            "http://demo.mapserver.org/cgi-bin/wfs?SERVICE=WFS&VERSION=1.1.0&REQUEST=GetCapabilities";

    public static final String SERVER_URL_200 =
            "http://www502.regione.toscana.it/wmsraster/com.rt.wms.RTmap/wms?map=wmscartoteca&service=wfs&request=getcapabilities&version=2.0.0";

    private URL url_100;

    private URL url_110;

    private URL url_200;

    private WFSDataStore wfs100;

    private WFSDataStore wfs110;

    private WFSDataStore wfs110_with_get;

    private WFSDataStore wfs200;

    @Before
    public void setUp() throws Exception {
        url_100 = new URL(SERVER_URL_100);
        url_110 = new URL(SERVER_URL_110);
        url_200 = new URL(SERVER_URL_200);
        if (url_100 != null) {
            try {
                Map<String, Serializable> params;
                params = new HashMap<String, Serializable>();
                params.put(WFSDataStoreFactory.URL.key, url_100);
                params.put(WFSDataStoreFactory.WFS_STRATEGY.key, "mapserver");
                params.put(WFSDataStoreFactory.GML_COMPATIBLE_TYPENAMES.key, Boolean.TRUE);
                wfs100 = new WFSDataStoreFactory().createDataStore(params);

                params = new HashMap<String, Serializable>();
                params.put(WFSDataStoreFactory.URL.key, url_110);
                params.put(WFSDataStoreFactory.WFS_STRATEGY.key, "mapserver");
                params.put(WFSDataStoreFactory.GML_COMPATIBLE_TYPENAMES.key, Boolean.TRUE);
                wfs110 = new WFSDataStoreFactory().createDataStore(params);

                params.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);
                wfs110_with_get = new WFSDataStoreFactory().createDataStore(params);

                params = new HashMap<String, Serializable>();
                params.put(WFSDataStoreFactory.URL.key, url_200);
                params.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);
                wfs200 = new WFSDataStoreFactory().createDataStore(params);

                assertEquals("1.0.0", wfs100.getInfo().getVersion());
                assertEquals("1.1.0", wfs110.getInfo().getVersion());
                assertEquals("1.1.0", wfs110_with_get.getInfo().getVersion());
                assertEquals("2.0.0", wfs200.getInfo().getVersion());
            } catch (Exception e) {
                System.err.println("Server is not available. test disabled ");
                url_100 = null;
            }
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
    public void testSingleType_WFS_1_0() throws IOException, NoSuchElementException {
        testSingleType(wfs100);
    }

    @Test
    public void testSingleType_WFS_1_1() throws IOException, NoSuchElementException {
        testSingleType(wfs110);
    }

    @Test
    public void testSingleType_WFS_1_1_with_get() throws IOException, NoSuchElementException {
        testSingleType(wfs110_with_get);
    }

    @Test
    public void testSingleType_WFS_2_0() throws IOException, NoSuchElementException {
        testSingleType2(wfs200);
    }

    private void testSingleType(DataStore wfs) throws IOException, NoSuchElementException {
        if (url_100 == null) return;

        String typeName = "ms_cities";
        SimpleFeatureType type = wfs.getSchema(typeName);
        type.getTypeName();
        type.getName().getNamespaceURI();
        assertEquals(typeName, type.getName().getLocalPart());

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();

        GeometryDescriptor geometryDesc = wfs.getSchema(typeName).getGeometryDescriptor();
        CoordinateReferenceSystem crs = geometryDesc.getCoordinateReferenceSystem();

        ReferencedEnvelope env = new ReferencedEnvelope(-59, -58, -35, -34, crs);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter filter = ff.bbox(ff.property("msGeometry"), env);
        Query query = new Query();
        query.setTypeName(typeName);
        query.setFilter(filter);
        // test property names
        query.setPropertyNames(new String[] {"POPULATION", "NAME"});

        features = source.getFeatures(query);

        int size = features.size();
        assertEquals(2, size);

        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature.getID());
            }
        } finally {
            iterator.close();
        }
    }

    private void testSingleType2(DataStore wfs) throws IOException, NoSuchElementException {
        if (url_100 == null) return;

        String typeName = "ms_rt_cartoteca.ctr10k.dxf";
        SimpleFeatureType type = wfs.getSchema(typeName);
        type.getTypeName();
        type.getName().getNamespaceURI();
        assertEquals(typeName, type.getName().getLocalPart());

        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        source.getBounds();

        SimpleFeatureCollection features = source.getFeatures();
        features.getBounds();
        features.getSchema();

        GeometryDescriptor geometryDesc = wfs.getSchema(typeName).getGeometryDescriptor();
        CoordinateReferenceSystem crs = geometryDesc.getCoordinateReferenceSystem();

        ReferencedEnvelope env =
                new ReferencedEnvelope(
                        4773438.659659, 648995.437355, 4773439.659659001, 648996.437355001, crs);

        FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(GeoTools.getDefaultHints());
        Filter filter = ff.bbox(ff.property("Geometry"), env);
        Query query = new Query();
        query.setTypeName(typeName);
        query.setFilter(filter);
        // test property names
        query.setPropertyNames(new String[] {"codice", "nome"});

        features = source.getFeatures(query);

        int size = features.size();
        assertEquals(308, size);

        SimpleFeatureIterator iterator = features.features();
        try {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                System.out.println(feature.getID());
            }
        } finally {
            iterator.close();
        }
    }

    @Test
    public void testWFSFilter() throws IOException, NoSuchElementException {

        String[] expectedA = {
            "USGS_TNM_Structures.10",
            "USGS_TNM_Structures.16",
            "USGS_TNM_Structures.101",
            "USGS_TNM_Structures.147",
            "USGS_TNM_Structures.182",
            "USGS_TNM_Structures.205",
            "USGS_TNM_Structures.212",
            "USGS_TNM_Structures.220",
            "USGS_TNM_Structures.289",
            "USGS_TNM_Structures.366"
        };
        TreeSet<String> expected = new TreeSet<>();
        expected.addAll(Arrays.asList(expectedA));
        // Set the capabilities URL to the back end I am referencing
        String getCapabilities =
                "http://cartowfs.nationalmap.gov/arcgis/services/structures/MapServer/WFSServer?request=GetCapabilities&service=WFS&version=1.0.0";

        Map<String, Object> connectionParameters = new HashMap<String, Object>();
        connectionParameters.put(WFSDataStoreFactory.URL.key, getCapabilities);
        connectionParameters.put(WFSDataStoreFactory.LENIENT.key, Boolean.TRUE);
        connectionParameters.put(WFSDataStoreFactory.TIMEOUT.key, 60);
        // uncomment to force GET (sometimes helps).
        connectionParameters.put(WFSDataStoreFactory.PROTOCOL.key, Boolean.FALSE);

        // Attempt to connect to the datastore.
        DataStore data = DataStoreFinder.getDataStore(connectionParameters);

        String typeNames[] = data.getTypeNames();
        String typeName = typeNames[0];

        SimpleFeatureSource source = data.getFeatureSource(typeName);

        FilterFactory ff = CommonFactoryFinder.getFilterFactory(GeoTools.getDefaultHints());
        Filter filter = ff.equals(ff.property("STATE"), ff.literal("MO"));

        Query query = new Query();
        query.setTypeName(typeName);

        query.setFilter(filter);
        SimpleFeatureCollection features = source.getFeatures(query);
        assertTrue(features.size() > 10);
        // Iterator through all the features and print them out.
        int count = 0;
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext() && count < 10) {
                SimpleFeature feature = iterator.next();
                assertTrue(expected.contains(feature.getID()));
                count++;
            }
        }

        query.setMaxFeatures(10);
        features = source.getFeatures(query);
        assertEquals(10, features.size());
        // Iterator through all the features and print them out.
        try (SimpleFeatureIterator iterator = features.features()) {
            while (iterator.hasNext()) {
                SimpleFeature feature = iterator.next();
                assertTrue(expected.contains(feature.getID()));
            }
        }
    }
}
