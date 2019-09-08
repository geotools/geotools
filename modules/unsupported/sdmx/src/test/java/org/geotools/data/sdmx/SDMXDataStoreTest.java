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
package org.geotools.data.sdmx;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import it.bancaditalia.oss.sdmx.client.RestSdmxClient;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import org.apache.commons.httpclient.HttpStatus;
import org.geotools.feature.NameImpl;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.type.Name;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RestSdmxClient.class, HttpURLConnection.class, URL.class})
public class SDMXDataStoreTest {

    private SDMXDataStore dataStore;
    private URL urlMock;
    private HttpURLConnection clientMock;

    @Before
    public void setUp() throws Exception {}

    @After
    public void tearDown() throws Exception {}

    @Test
    public void testTypeNameComposition() throws Exception {

        assertEquals("population__SDMX", SDMXDataStore.composeDataflowTypeName("population"));

        assertEquals(
                "population__SDMX__DIMENSIONS",
                SDMXDataStore.composeDimensionTypeName("population"));

        assertEquals(false, SDMXDataStore.isDataflowName("population"));
        assertEquals(false, SDMXDataStore.isDataflowName("population_SDMX"));
        assertEquals(true, SDMXDataStore.isDataflowName("population__SDMX"));

        assertEquals(false, SDMXDataStore.isDimensionName("population"));
        assertEquals(false, SDMXDataStore.isDimensionName(""));
        assertEquals(false, SDMXDataStore.isDimensionName("population_SDMX__DIMENSIONS"));
        assertEquals(false, SDMXDataStore.isDimensionName(""));
        assertEquals(true, SDMXDataStore.isDimensionName("population__SDMX__DIMENSIONS"));

        assertEquals(
                "population", SDMXDataStore.extractDataflowName("population__SDMX__DIMENSIONS"));
        assertEquals("population", SDMXDataStore.extractDataflowName("population__SDMX"));
        assertEquals("", SDMXDataStore.extractDataflowName("population"));
        assertEquals("", SDMXDataStore.extractDataflowName("__SDMX"));
        assertEquals("", SDMXDataStore.extractDataflowName("_SDMX"));
        assertEquals("", SDMXDataStore.extractDataflowName("population__SDMX_X"));
        assertEquals("", SDMXDataStore.extractDataflowName(""));
    }

    @Test
    public void testTypeName() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        assertEquals(4, names.size());
        assertTrue(names.contains(new NameImpl(Helper.NAMESPACE, Helper.T04)));
        assertTrue(names.contains(new NameImpl(Helper.NAMESPACE, Helper.T04_DIMENSIONS)));
        assertNotNull(this.dataStore.getEntry(new NameImpl(Helper.NAMESPACE, Helper.T04)));
        assertTrue(names.contains(new NameImpl(Helper.NAMESPACE, Helper.SEIFA_LGA)));
        assertTrue(names.contains(new NameImpl(Helper.NAMESPACE, Helper.SEIFA_LGA_DIMENSIONS)));
        assertNotNull(this.dataStore.getEntry(new NameImpl(Helper.NAMESPACE, Helper.SEIFA_LGA)));
    }

    @Test
    public void testSchema() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        assertEquals(4, this.dataStore.createTypeNames().size());

        assertNotNull(this.dataStore.getFeatureSource(Helper.T04).getSchema());
        assertEquals(
                9, this.dataStore.getFeatureSource(Helper.T04).getSchema().getAttributeCount());
        assertNotNull(this.dataStore.getFeatureSource(Helper.SEIFA_LGA).getSchema());
        assertEquals(
                5,
                this.dataStore.getFeatureSource(Helper.SEIFA_LGA).getSchema().getAttributeCount());
    }
}
