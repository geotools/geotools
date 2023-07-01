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

import java.util.logging.Logger;
import org.geotools.data.Query;
import org.geotools.filter.text.ecql.ECQL;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;

public class SDMXFeatureReaderSystemTest {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.arcgisrest");

    private SDMXDataStore dataStore;

    SDMXFeatureReader reader;
    SDMXDataflowFeatureSource dfSource;
    SDMXDimensionFeatureSource dimSource;
    SimpleFeatureType fType;

    // NOTE: There is a persisten XML error, although the XML returned by the request seems OK
    /*
        May 29, 2020 9:06:18 AM it.bancaditalia.oss.sdmx.client.RestSdmxClient runQuery
    INFO: Contacting web service with query: http://stat.data.abs.gov.au/restsdmx/sdmx.ashx/GetDataStructure/ABS_CENSUS2011_B40_SA1_SA/ABS
    May 29, 2020 9:06:18 AM it.bancaditalia.oss.sdmx.client.RestSdmxClient runQuery
    SEVERE: Exception caught parsing results from call to provider ABS
    May 29, 2020 9:06:18 AM org.geotools.data.sdmx.SDMXDataStore lambda$null$0
    SEVERE: Error getting SDMX DSD
    it.bancaditalia.oss.sdmx.exceptions.SdmxXmlParsingException: Error detected while parsing SDMX response: ParseError at [row,col]:[29,38]
    Message: The entity "nbsp" was referenced, but not declared.
            at it.bancaditalia.oss.sdmx.exceptions.SdmxExceptionFactory.wrap(SdmxExceptionFactory.java:125)
            at it.bancaditalia.oss.sdmx.client.RestSdmxClient.runQuery(RestSdmxClient.java:411)
            at it.bancad
         */
    @Test
    public void readFeaturesMeasureSDMX_2_0() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore();
        this.dataStore.createTypeNames();

        assertEquals(this.dataStore.dataflowStructures.size(), this.dataStore.dataflows.size());

        Query query = new Query();
        this.fType = this.dataStore.getFeatureSource(Helper.ERP_LGA_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource)
                        this.dataStore.getFeatureSource(Helper.ERP_LGA_DIMENSIONS);
        this.dimSource.buildFeatureType();
        query.setFilter(ECQL.toFilter("CODE='AGE'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        query.setFilter(ECQL.toFilter("CODE='LGA_2016'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        this.fType = this.dataStore.getFeatureSource(Helper.T04_LGA).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04_LGA);
        this.dfSource.buildFeatureType();
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

            // Check only the first result row
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

            nObs++;
        }

        assertEquals(3, nObs);
    }

    // NOTE: on some DSDs this tosses out the error (probaly due to incorrect test OECD dataflows):
    /*
    INFO: Added SDMX feature types: OECD_____SDD____NAPENS_ALL____3_____0__SDMX, OECD_____SDD____NAPENS_ALL____3_____0__SDMX__DIMENSIONS
    May 27, 2020 8:37:14 AM org.geotools.data.sdmx.SDMXDataStore lambda$createTypeNames$0
    SEVERE: Error getting SDMX DSD
    it.bancaditalia.oss.sdmx.exceptions.SdmxInvalidParameterException: getDataFlowStructure(): Null dsd in input
            at it.bancaditalia.oss.sdmx.client.RestSdmxClient.getDataFlowStructure(RestSdmxClient.java:193)
            at org.geotools.data.sdmx.SDMXDataStore.lambda$createTypeNames$0(SDMXDataStore.java:205)
            at java.util.HashMap.forEach(HashMap.java:1289)
            at org.geotools.data.sdmx.SDMXDataStore.createTypeNames(SDMXDataStore.java:201)
            at org.geotools.data.store.ContentDataStore.entry(ContentDataStore.java:499)
            at org.geotools.data.store.ContentDataStore.ensureEntry(ContentDataStore.java:532)
            at org.geotools.data.store.ContentDataStore.getFeatureSource(ContentDataStore.java:333)
            at org.geotools.data.store.ContentDataStore.getFeatureSource(ContentDataStore.java:305)
            at org.geotools.data.sdmx.SDMXFeatureReaderSystemTest.readFeaturesMeasureSDMX21Endpoint(SDMXFeatureReaderSystemTest.java:174)
            at sun.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
            at sun.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:62)
            at sun.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
            at java.lang.reflect.Method.invoke(Method.java:498)
            at org.junit.runners.model.FrameworkMethod$1.runReflectiveCall(FrameworkMethod.java:47)
            at org.junit.internal.runners.model.ReflectiveCallable.run(ReflectiveCallable.java:12)
            at org.junit.runners.model.FrameworkMethod.invokeExplosively(FrameworkMethod.java:44)
            at org.junit.internal.runners.statements.InvokeMethod.evaluate(InvokeMethod.java:17)
            at org.junit.runners.ParentRunner.runLeaf(ParentRunner.java:271)
            at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:70)
            at org.junit.runners.BlockJUnit4ClassRunner.runChild(BlockJUnit4ClassRunner.java:50)
            at org.junit.runners.ParentRunner$3.run(ParentRunner.java:238)
            at org.junit.runners.ParentRunner$1.schedule(ParentRunner.java:63)
            at org.junit.runners.ParentRunner.runChildren(ParentRunner.java:236)
            at org.junit.runners.ParentRunner.access$000(ParentRunner.java:53)
            at org.junit.runners.ParentRunner$2.evaluate(ParentRunner.java:229)
            at org.junit.runners.ParentRunner.run(ParentRunner.java:309)
            at org.apache.maven.surefire.junit4.JUnit4Provider.execute(JUnit4Provider.java:264)
            at org.apache.maven.surefire.junit4.JUnit4Provider.executeTestSet(JUnit4Provider.java:153)
            at org.apache.maven.surefire.junit4.JUnit4Provider.invoke(JUnit4Provider.java:124)
            at org.apache.maven.surefire.booter.ForkedBooter.invokeProviderInSameClassLoader(ForkedBooter.java:200)
            at org.apache.maven.surefire.booter.ForkedBooter.runSuitesInProcess(ForkedBooter.java:153)
            at org.apache.maven.surefire.booter.ForkedBooter.main(ForkedBooter.java:103)
        */
    @Test
    public void readFeaturesMeasureSDMX_2_1() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore2();
        this.dataStore.createTypeNames();

        assertEquals(this.dataStore.dataflowStructures.size(), this.dataStore.dataflows.size());

        Query query = new Query();
        this.fType = this.dataStore.getFeatureSource(Helper.T04SA_DIMENSIONS).getSchema();
        this.dimSource =
                (SDMXDimensionFeatureSource)
                        this.dataStore.getFeatureSource(Helper.T04SA_DIMENSIONS);
        this.dimSource.buildFeatureType();
        query.setFilter(ECQL.toFilter("CODE='AGE'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        query.setFilter(ECQL.toFilter("CODE='SEX_ABS'"));
        this.reader = (SDMXFeatureReader) this.dimSource.getReader(query);
        assertTrue(this.reader.hasNext());

        this.fType = this.dataStore.getFeatureSource(Helper.KN_NIS).getSchema();
        this.dfSource = (SDMXDataflowFeatureSource) this.dataStore.getFeatureSource(Helper.T04SA);
        this.dfSource.buildFeatureType();
        query.setFilter(
                ECQL.toFilter(
                        "AGE='TT' and SEX_ABS='1' and REGIONTYPE in ('SA2') and TIME in ('2016') and INGP_2016 in ('1') and ASGS_2016 in ('311051328')"));
        this.reader = (SDMXFeatureReader) this.dfSource.getReader(query);

        assertTrue(this.reader.hasNext());
        SimpleFeature feat;
        int nObs = 0;
        while (this.reader.hasNext()) {
            feat = this.reader.next();
            assertNotNull(feat);
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

            // Check only the first result row
            if (nObs == 0) {
                assertNotNull(feat.getID());
                assertNull(feat.getDefaultGeometry());
                assertEquals("2016", feat.getAttribute(1));
                assertEquals(3099.0, feat.getAttribute(2));
            }

            nObs++;
        }

        assertEquals(1, nObs);
    }

    @Test
    public void readFeaturesMeasureSDMX_Parallel_2_0() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore();
        this.dataStore.setConcurrency(20);
        this.dataStore.createTypeNames();
        assertEquals(this.dataStore.dataflowStructures.size(), this.dataStore.dataflows.size());
    }

    @Test
    public void readFeaturesMeasureSDMX_Parallel_2_1() throws Exception {

        this.dataStore = (SDMXDataStore) Helper.createSDMXTestDataStore2();
        this.dataStore.setConcurrency(20);
        this.dataStore.createTypeNames();
        assertEquals(this.dataStore.dataflowStructures.size(), this.dataStore.dataflows.size());
    }
}
