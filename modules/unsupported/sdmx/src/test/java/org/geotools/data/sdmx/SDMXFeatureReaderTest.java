/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2010, Open Source Geospatial Foundation (OSGeo)
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
import java.io.ByteArrayInputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Logger;
import org.apache.commons.httpclient.HttpStatus;
import org.geotools.data.Query;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.filter.Filter;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({RestSdmxClient.class, HttpURLConnection.class, URL.class})
public class SDMXFeatureReaderTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.arcgisrest");

    private SDMXDataStore dataStore;
    private URL urlMock;
    private HttpURLConnection clientMock;

    SDMXFeatureReader reader;
    SDMXDataflowFeatureSource dfSource;
    SDMXDimensionFeatureSource dimSource;
    SimpleFeatureType fType;

    @Test
    public void queryExpression() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/query-t04.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04);

        assertEquals("......", this.dfSource.buildConstraints(Query.ALL));

        Filter filter =
                ECQL.toFilter(
                        "MEASURE='3' and "
                                + "MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='0' and "
                                + "REGIONTYPE='AUS' and "
                                + "REGION='0' and "
                                + "FREQUENCY='A'");
        assertEquals("3.TOT.TOT.0.AUS.0.A", this.dfSource.buildConstraints(new Query("", filter)));

        filter =
                ECQL.toFilter(
                        "MEASURE in ('1', '2', '3') and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1' and "
                                + "REGIONTYPE='STE' and "
                                + "REGION in ('1','2','3','4') and "
                                + "FREQUENCY='A'");
        assertEquals(
                "1+2+3.TOT.TOT.1.STE.1+2+3+4.A",
                this.dfSource.buildConstraints(new Query("", filter)));

        filter =
                ECQL.toFilter(
                        "MEASURE in ('1', '2', '3') and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1'");
        assertEquals("1+2+3.TOT.TOT.1...", this.dfSource.buildConstraints(new Query("", filter)));
    }

    @Test
    public void noFeatures() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_NOT_FOUND);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(new ByteArrayInputStream("".getBytes()));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04);

        this.dfSource.buildFeatureType();
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(Query.ALL);

        assertFalse(this.reader.hasNext());
        assertNull(this.reader.next());
    }

    @Test
    public void readFeaturesMeasure1() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/query-t04-1.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04);

        this.dfSource.buildFeatureType();
        Query query = new Query();
        query.setFilter(
                ECQL.toFilter(
                        "MEASURE = 1 and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1' and "
                                + "REGIONTYPE='STE' and "
                                + "REGION in ('1','2','3','4') and "
                                + "FREQUENCY='A'"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("2001", feat.getAttribute(1));
                assertEquals(2468518.0, feat.getAttribute(2));
                assertEquals("TOT", feat.getAttribute(3));
                assertEquals("TOT", feat.getAttribute(4));
                assertEquals("1", feat.getAttribute(5));
                assertEquals("STE", feat.getAttribute(6));
                assertEquals("1", feat.getAttribute(7));
                assertEquals("A", feat.getAttribute(8));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(3, nObs);
    }

    @Test
    public void readFeaturesMeasure2() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/query-t04-2.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04);

        this.dfSource.buildFeatureType();
        Query query = new Query();
        query.setFilter(
                ECQL.toFilter(
                        "MEASURE = 2 and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1' and "
                                + "REGIONTYPE='STE' and "
                                + "REGION in ('1','2','3','4') and "
                                + "FREQUENCY='A'"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertEquals(2583729.0, feat.getAttribute(2));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(3, nObs);
    }

    @Test
    public void readFeaturesMeasure123() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/query-t04-321.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04);

        this.dfSource.buildFeatureType();
        Query query = new Query();
        query.setFilter(
                ECQL.toFilter(
                        "MEASURE in ('3','2', '1') and MSTP='TOT' and "
                                + "AGE='TOT' and "
                                + "STATE='1' and "
                                + "REGIONTYPE='STE' and "
                                + "REGION in ('1','2','3','4') and "
                                + "FREQUENCY='A'"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("2001", feat.getAttribute(1));
                // Only the first measure is returned
                assertEquals(2468518.0, feat.getAttribute(2));
                assertEquals("TOT", feat.getAttribute(3));
                assertEquals("TOT", feat.getAttribute(4));
                assertEquals("1", feat.getAttribute(5));
                assertEquals("STE", feat.getAttribute(6));
                assertEquals("1", feat.getAttribute(7));
                assertEquals("A", feat.getAttribute(8));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(9, nObs);
    }

    @Test
    public void readFeaturesAllDimensions() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource) this.dataStore.getFeatureSource(Helper.T04_DIMENSIONS);

        this.dimSource.buildFeatureType();
        Query query = new Query();
        query.setFilter(ECQL.toFilter("coDE = 'aLL'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("MSTP", feat.getAttribute(SDMXDataStore.CODE_KEY));
                assertEquals(
                        "Registered Marital Status",
                        feat.getAttribute(SDMXDataStore.DESCRIPTION_KEY));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(7, nObs);
    }

    @Test
    public void readFeaturesDimensionAge() throws Exception {

        this.urlMock = PowerMockito.mock(URL.class);
        this.clientMock = PowerMockito.mock(HttpURLConnection.class);

        PowerMockito.whenNew(URL.class).withAnyArguments().thenReturn(this.urlMock);
        PowerMockito.when(this.urlMock.openConnection()).thenReturn(this.clientMock);
        when(clientMock.getResponseCode())
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK)
                .thenReturn(HttpStatus.SC_OK);
        when(clientMock.getInputStream())
                .thenReturn(Helper.readXMLAsStream("test-data/abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-census2011-t04-abs.xml"))
                .thenReturn(Helper.readXMLAsStream("test-data/abs-seifa-lga.xml"));

        this.dataStore = (SDMXDataStore) Helper.createDefaultSDMXTestDataStore();
        this.fType = this.dataStore.getFeatureSource(Helper.T04_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource) this.dataStore.getFeatureSource(Helper.T04_DIMENSIONS);

        this.dimSource.buildFeatureType();
        Query query = new Query();
        query.setFilter(ECQL.toFilter("cOdE = 'AgE'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("A65", feat.getAttribute(SDMXDataStore.CODE_KEY));
                // Only the first measure is returned
                assertEquals("65 - 69", feat.getAttribute(SDMXDataStore.DESCRIPTION_KEY));
            }
            String s =
                    feat.getID()
                            + "|"
                            + feat.getType().getGeometryDescriptor().getLocalName()
                            + ":"
                            + feat.getDefaultGeometry();
            for (int i = 1; i < feat.getAttributeCount(); i++) {
                s +=
                        "|"
                                + feat.getType().getDescriptor(i).getLocalName()
                                + ":"
                                + feat.getAttribute(i);
            }
            System.out.println(s);
            nObs++;
        }

        assertEquals(17, nObs);
    }
}
