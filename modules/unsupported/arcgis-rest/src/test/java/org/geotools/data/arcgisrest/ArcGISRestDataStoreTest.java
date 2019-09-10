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
package org.geotools.data.arcgisrest;

import static org.junit.Assert.*;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.List;
import org.apache.http.HttpEntity;
import org.apache.http.HttpStatus;
import org.apache.http.StatusLine;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.geotools.data.FeatureSource;
import org.geotools.data.Query;
import org.geotools.feature.FeatureCollection;
import org.geotools.feature.FeatureIterator;
import org.geotools.feature.NameImpl;
import org.geotools.referencing.CRS;
import org.geotools.util.UnsupportedImplementationException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.feature.type.Name;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({HttpClients.class, HttpRequestBase.class, ArcGISRestDataStore.class})
public class ArcGISRestDataStoreTest {

    public static String TYPENAME1 = "LGAProfiles2014Beta";
    public static String TYPENAME2 = "Airports_2";
    public static String TYPENAME3 = "Airports_3";
    public static String TYPENAME4 = "Principal Bicycle Network";
    public static String TYPENAME5 = "OS_WalkableCatchment_400m_SP";
    public static String TYPENAME6 = "LGAProfiles2014Beta_2";

    private ArcGISRestDataStore dataStore;

    private CloseableHttpClient clientMock;
    private HttpGet getMock;
    private HttpPost postMock;
    private CloseableHttpResponse responseMock;
    private StatusLine responseMockStatusLine;
    private HttpEntity responseMockEntity;

    @Before
    public void setUp() throws Exception {
        this.clientMock = PowerMockito.mock(CloseableHttpClient.class);
        this.getMock = PowerMockito.mock(HttpGet.class);
        this.postMock = PowerMockito.mock(HttpPost.class);
        this.responseMock = PowerMockito.mock(CloseableHttpResponse.class);
        this.responseMockStatusLine = PowerMockito.mock(StatusLine.class);
        this.responseMockEntity = PowerMockito.mock(HttpEntity.class);
    }

    @After
    public void tearDown() throws Exception {
        this.clientMock = PowerMockito.mock(CloseableHttpClient.class);
        this.getMock = PowerMockito.mock(HttpGet.class);
        this.postMock = PowerMockito.mock(HttpPost.class);
        this.responseMock = PowerMockito.mock(CloseableHttpResponse.class);
        this.responseMockStatusLine = PowerMockito.mock(StatusLine.class);
        this.responseMockEntity = PowerMockito.mock(HttpEntity.class);
    }

    @Test
    public void testHTTPError() throws Exception {
        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class).withNoArguments().thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_NOT_FOUND);

        try {
            this.dataStore =
                    (ArcGISRestDataStore)
                            ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
            List<Name> names = this.dataStore.createTypeNames();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("404"));
        }
    }

    @Test
    public void testServiceError() throws Exception {
        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock).thenReturn(responseMock);
        when(responseMock.getStatusLine())
                .thenReturn(responseMockStatusLine)
                .thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity())
                .thenReturn(responseMockEntity)
                .thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/error.json"));

        try {
            this.dataStore =
                    (ArcGISRestDataStore)
                            ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
            List<Name> names = this.dataStore.createTypeNames();
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("400 Cannot perform query"));
        }
    }

    @Test
    public void testVictoadsOpenData() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock).thenReturn(responseMock);
        when(responseMock.getStatusLine())
                .thenReturn(responseMockStatusLine)
                .thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity())
                .thenReturn(responseMockEntity)
                .thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/error.json"));

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock)
                .thenReturn(getMock)
                .thenReturn(getMock)
                .thenReturn(getMock)
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(responseMockStatusLine.getStatusCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_BAD_REQUEST)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/wsServiceInDistribution.json"))
                .thenReturn(ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/error.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset2.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset2.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        assertEquals(2, names.size());
        assertEquals(TYPENAME1, names.get(0).getLocalPart());
        assertEquals(ArcGISRestDataStoreFactoryTest.NAMESPACE, names.get(0).getNamespaceURI());

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();
        assertTrue(src instanceof ArcGISRestFeatureSource);
        src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME6)));
        src.getSchema();
        assertTrue(src instanceof ArcGISRestFeatureSource);
    }

    @Test(expected = UnsupportedImplementationException.class)
    public void testUnsupportedAPIVersion() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/unsupportedCatalog.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultArcGISServerTestDataStore();
    }

    @Test
    public void testCreateTypeNamesFromArcGISOnline() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity())
                .thenReturn(responseMockEntity)
                .thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        assertEquals(1, names.size());
        assertEquals(TYPENAME1, names.get(0).getLocalPart());
        assertEquals(ArcGISRestDataStoreFactoryTest.NAMESPACE, names.get(0).getNamespaceURI());

        assertNotNull(
                this.dataStore.getEntry(
                        new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
    }

    @Test
    public void testCreateTypeNamesFromArcGISServer() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/FeatureServerAirport.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/airport2Dataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/airport3Dataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultArcGISServerTestDataStore();
        List<Name> names = this.dataStore.createTypeNames();

        assertEquals(2, names.size());
        assertEquals(TYPENAME2, names.get(0).getLocalPart());
        assertEquals(TYPENAME3, names.get(1).getLocalPart());
        assertEquals(ArcGISRestDataStoreFactoryTest.NAMESPACE, names.get(0).getNamespaceURI());

        assertNotNull(
                this.dataStore.getEntry(
                        new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME2)));
    }

    @Test
    public void testCreateFeatureSourceAndCountFeature() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMock.getEntity().getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();
        assertTrue(src instanceof ArcGISRestFeatureSource);
        assertEquals("LGAProfiles2014Beta", src.getInfo().getName());
        assertEquals(
                ArcGISRestDataStoreFactoryTest.NAMESPACE, src.getInfo().getSchema().toString());
        assertEquals(CRS.decode("EPSG:3857"), src.getInfo().getCRS());
        assertEquals("LGA Profile 2014 (beta)", src.getInfo().getTitle());
        assertEquals(15661191, src.getInfo().getBounds().getMinX(), 1);
        assertEquals(-4742385, src.getInfo().getBounds().getMinY(), 1);
        assertEquals(16706777, src.getInfo().getBounds().getMaxX(), 1);
        assertEquals(-4022464, src.getInfo().getBounds().getMaxY(), 1);
        assertEquals(
                "[Health and Human Services, LGA, LGA Profiles]",
                src.getInfo().getKeywords().toString());
        assertEquals(
                "<div>2014 Local Government Area Profiles</div><div><br /></div>https://www2.health.vic.gov.au/about/reporting-planning-data/gis-and-planning-products/geographical-profiles<div>&gt; Please read the data definistions at the link above</div><div>&gt; xls and pdf documents area available at the link above</div><div>&gt; This is a beta release of the 2014 LGA profiles in this format. Field names and types may change during the beta phase. </div><div><br /></div><div>Last updated : 24 May 2016</div><div>Owning agency : Department of Health and Human Services, Victoria</div><div>Copyright statement : https://www.health.vic.gov.au/copyright</div><div>Licence name : https://www.health.vic.gov.au/data-license</div><div>Disclaimer: https://www.health.vic.gov.au/data-disclaimer</div><div>Attribution statement: https://www.health.vic.gov.au/data-attribution</div><div><br /></div><div>Off-line access : Department of Health and Human Services, GPO Box 4057, Melbourne Victoria, 3001</div><div><br /></div><div>Geographic coverage-jurisdiction : Victoria</div>",
                src.getInfo().getDescription());

        // Feature count test
        PowerMockito.whenNew(HttpPost.class).withNoArguments().thenReturn(postMock);
        when(clientMock.execute(postMock)).thenReturn(responseMock);

        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/count.json"));

        assertEquals(79, src.getCount(new Query()));
    }

    @Test
    public void testFeatures() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);

        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaDataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME1)));
        src.getSchema();

        // Test feature iteration
        PowerMockito.whenNew(HttpPost.class).withNoArguments().thenReturn(this.postMock);
        when(this.clientMock.execute(postMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/lgaFeatures.geo.json"));

        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = src.getFeatures(new Query());
        FeatureIterator iter = fc.features();

        assertEquals(CRS.decode("EPSG:3857"), fc.getSchema().getCoordinateReferenceSystem());
        assertEquals(true, iter.hasNext());
        SimpleFeature sf = (SimpleFeature) iter.next();
        assertEquals(true, iter.hasNext());
        sf = (SimpleFeature) iter.next();
        assertEquals(
                "POINT (16421261.466298774 -4592239.022226746)",
                ((Geometry) (sf.getAttribute("geometry"))).getCentroid().toString());
        assertEquals("Wellington (S)", sf.getAttribute("LGA"));
        assertEquals(false, iter.hasNext());
        assertEquals(false, iter.hasNext());
    }

    @Test
    public void testFeaturesWithDate() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream("test-data/catalog.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/bicycleDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/bicycleDataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultOpenDataTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME4)));
        src.getSchema();

        PowerMockito.whenNew(HttpPost.class).withNoArguments().thenReturn(this.postMock);
        when(this.clientMock.execute(postMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMockEntity.getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/bicycleFeatures.geo.json"));

        FeatureCollection<SimpleFeatureType, SimpleFeature> fc = src.getFeatures(new Query());
        FeatureIterator iter = fc.features();

        assertEquals(true, iter.hasNext());
        SimpleFeature sf = (SimpleFeature) iter.next();
        assertEquals("ROAD", sf.getAttribute("LOCAL_TYPE"));
        assertEquals(5532, sf.getAttribute("RD_NUM"));
        assertNull(sf.getAttribute("VERI_DATE"));
        assertEquals(true, iter.hasNext());
        sf = (SimpleFeature) iter.next();
        assertEquals("ROAD", sf.getAttribute("LOCAL_TYPE"));
        assertEquals(5068, sf.getAttribute("RD_NUM"));
        assertEquals(
                (new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSX"))
                        .parse("2011-08-02T00:00:00.000Z"),
                sf.getAttribute("VERI_DATE"));
    }

    @Test
    public void testSourceWithWKT() throws Exception {

        PowerMockito.mockStatic(HttpClients.class);
        PowerMockito.when(HttpClients.createDefault()).thenReturn(clientMock);
        PowerMockito.whenNew(HttpGet.class)
                .withNoArguments()
                .thenReturn(getMock)
                .thenReturn(getMock);
        when(clientMock.execute(getMock)).thenReturn(responseMock);
        when(responseMock.getStatusLine()).thenReturn(responseMockStatusLine);
        when(responseMockStatusLine.getStatusCode()).thenReturn(HttpStatus.SC_OK);
        when(responseMock.getEntity()).thenReturn(responseMockEntity);
        when(responseMock.getEntity().getContent())
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/FeatureServerLandUse.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/walkablecatchmentDataset.json"))
                .thenReturn(
                        ArcGISRestDataStoreFactoryTest.readJSONAsStream(
                                "test-data/walkablecatchmentDataset.json"));

        this.dataStore =
                (ArcGISRestDataStore)
                        ArcGISRestDataStoreFactoryTest.createDefaultArcGISServerTestDataStore();
        this.dataStore.createTypeNames();

        FeatureSource<SimpleFeatureType, SimpleFeature> src =
                this.dataStore.createFeatureSource(
                        this.dataStore.getEntry(
                                new NameImpl(ArcGISRestDataStoreFactoryTest.NAMESPACE, TYPENAME5)));
        src.getSchema();
    }
}
