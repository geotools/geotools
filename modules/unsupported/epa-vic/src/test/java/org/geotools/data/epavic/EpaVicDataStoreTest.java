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
package org.geotools.data.epavic;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.List;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpMethod;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.data.epavic.schema.Sites;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.referencing.CRS;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Point;
import org.mockito.ArgumentCaptor;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpMethod.class, EpaVicDatastore.class})
public class EpaVicDataStoreTest {

    public static String TYPENAME1 = "measurement";

    private EpaVicDatastore dataStore;

    private HttpClient clientMock;

    private GetMethod getMock;

    private Query q;

    @Before
    public void setUp() throws Exception {
        q =
                new Query(
                        "measurement",
                        ECQL.toFilter(
                                "MonitorId='PM10' AND TimeBaseId='24HR_RAV' "
                                        + "AND DateTimeRecorded BETWEEN '2018-03-21T10:00:00' AND '2019-03-23T10:00:00'"));
    }

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testHTTPError() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class).withNoArguments().thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class).withNoArguments().thenReturn(getMock);
        when(clientMock.executeMethod(getMock)).thenReturn(HttpStatus.SC_NOT_FOUND);

        try {
            this.dataStore =
                    (EpaVicDatastore)
                            EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
            List<Name> names = this.dataStore.createTypeNames();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("404"));
        }
    }

    @Test
    public void testServiceError() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/measurements.json"));

        try {
            this.dataStore =
                    (EpaVicDatastore)
                            EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
            List<Name> names = this.dataStore.createTypeNames();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("400 Cannot perform query"));
        }
    }

    @Test
    public void testCreateTypeNamesMeasurements() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream(
                                "test-data/9measurements.json"));

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        assertEquals(1, names.size());
        assertEquals(TYPENAME1, names.get(0).getLocalPart());
        assertEquals(EpaVicDataStoreFactoryTest.NAMESPACE, names.get(0).getNamespaceURI());

        assertNotNull(
                this.dataStore.getEntry(
                        new NameImpl(EpaVicDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
    }

    @Test
    public void testCreateFeatureSourceAndCountFeature() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/sites.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/9measurements.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream(
                                "test-data/9measurements.json"));

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(EpaVicDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();
        assertTrue(src instanceof EpaVicFeatureSource);
        assertEquals("measurement", src.getInfo().getName());
        assertEquals(EpaVicDataStoreFactoryTest.NAMESPACE, src.getInfo().getSchema().toString());
        assertEquals(CRS.decode("EPSG:4283"), src.getInfo().getCRS());

        // Feature count test
        assertEquals(18, src.getCount(q));
    }

    @Test
    public void testFeatures() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/sites.json"))
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/monitors.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/9measurements.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream(
                                "test-data/9measurements.json"));

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(EpaVicDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();

        // Test feature iteration
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = src.getFeatures(q);
        FeatureIterator iter = fc.features();

        assertEquals(CRS.decode("EPSG:4283"), fc.getSchema().getCoordinateReferenceSystem());
        assertTrue(iter.hasNext());
        SimpleFeature sf = (SimpleFeature) iter.next();
        Point p = (Point) sf.getDefaultGeometry();
        assertEquals(145.0306, p.getX(), 0.001);
        assertEquals(-37.77832, p.getY(), 0.001);
        assertEquals(true, iter.hasNext());

        while (iter.hasNext()) {
            iter.next();
        }
    }

    @Test
    public void testReadSites() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/sites.json"));

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        Sites sites = this.dataStore.retrieveSitesJSON();

        assertEquals(2, sites.getSites().size());
    }

    @Test
    public void testFeaturesWithFilter() throws Exception {

        this.clientMock = PowerMockito.mock(HttpClient.class);
        PowerMockito.whenNew(HttpClient.class)
                .withNoArguments()
                .thenReturn(clientMock)
                .thenReturn(clientMock);
        this.getMock = PowerMockito.mock(GetMethod.class);
        PowerMockito.whenNew(GetMethod.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.executeMethod(getMock))
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(getMock.getResponseBodyAsStream())
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/sites.json"))
                .thenReturn(EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/monitors.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream("test-data/9measurements.json"))
                .thenReturn(
                        EpaVicDataStoreFactoryTest.readJSONAsStream(
                                "test-data/9measurements.json"));

        this.dataStore =
                (EpaVicDatastore) EpaVicDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(EpaVicDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();

        // Test feature iteration
        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = src.getFeatures(q);
        FeatureIterator iter = fc.features();
        if (iter.hasNext()) {
            iter.next();
        }

        NameValuePair[] check = new NameValuePair[4];
        check[0] = new NameValuePair(EpaVicFeatureSource.FROMDATE, "2018032110");
        check[1] = new NameValuePair(EpaVicFeatureSource.MONITORID, "PM10");
        check[2] = new NameValuePair(EpaVicFeatureSource.TIMEBASEID, "24HR_RAV");
        check[3] = new NameValuePair(EpaVicFeatureSource.TODATE, "2019032310");

        ArgumentCaptor<NameValuePair[]> captor = ArgumentCaptor.forClass(NameValuePair[].class);
        verify(getMock, times(2)).setQueryString(captor.capture());
        NameValuePair[] getMethodCalled = captor.getValue();
        assertArrayEquals(check, getMethodCalled);

        while (iter.hasNext()) {
            iter.next();
        }
    }
}
