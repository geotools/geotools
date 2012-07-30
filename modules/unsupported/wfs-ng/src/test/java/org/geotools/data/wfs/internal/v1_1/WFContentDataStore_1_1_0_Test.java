/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.spy;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.wfs.impl.TestHttpResponse;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.impl.WFSTestData;
import org.geotools.data.wfs.internal.WFSStrategy;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;

/**
 * Unit test suite for {@link WFSContentDataStore}
 * 
 * @author Gabriel Roldan
 * @version $Id$
 * @since 2.5.x
 * 
 * 
 * 
 * @source $URL$
 *         http://gtsvn.refractions.net/trunk/modules/plugin/wfs/src/test/java/org/geotools/data
 *         /wfs/v1_1_0/WFSDataStoreTest.java $
 */
@SuppressWarnings("nls")
public class WFContentDataStore_1_1_0_Test extends WFSTestData {

    /**
     * Test method for {@link WFSContentDataStore#getTypeNames()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetTypeNames() throws IOException {
        String[] expected = { "gubs:GovernmentalUnitCE", "gubs:GovernmentalUnitMCD",
                "gubs:GovernmentalUnitST", "hyd:HydroElementARHI", "hyd:HydroElementARMD",
                "hyd:HydroElementFLHI", "hyd:HydroElementFLMD", "hyd:HydroElementLIHI",
                "hyd:HydroElementLIMD", "hyd:HydroElementPTHI", "hyd:HydroElementPTMD",
                "hyd:HydroElementWBHI", "hyd:HydroElementWBMD", "trans:RoadSeg" };
        Set<String> expectedTypeNames = new HashSet<String>(Arrays.asList(expected));

        WFSStrategy wfs = createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES,
                new StrictWFS_1_1_Strategy());

        WFSContentDataStore ds = new WFSContentDataStore(wfs);

        String[] typeNames = ds.getTypeNames();
        assertNotNull(typeNames);
        Set<String> names = new HashSet<String>(Arrays.asList(typeNames));
        assertEquals(expectedTypeNames.size(), names.size());
        assertEquals(expectedTypeNames, names);
    }

    /**
     * Test method for
     * {@link org.geotools.wfs.WFSConfig.data.WFSContentDataStore#getSchema(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetSchema() throws IOException {
        final InputStream schemaStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.SCHEMA);
        TestHttpResponse httpResponse = new TestHttpResponse("", "UTF-8", schemaStream);
        TestHTTPClient mockHttp = new TestHTTPClient(httpResponse);
        WFSStrategy wfs = createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp,
                new StrictWFS_1_1_Strategy());

        wfs = spy(wfs);
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        when(wfs.buildDescribeFeatureTypeParametersForGET(CUBEWERX_GOVUNITCE.FEATURETYPENAME)).thenReturn(
                describeUrl);

        WFSContentDataStore ds = new WFSContentDataStore(wfs);

        try {
            ds.getSchema("nonExistentTypeName");
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(true);
        }

        SimpleFeatureType schema = ds.getSchema(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        assertNotNull(schema);
    }

    @Test
    public void tesGetFeatureReader() throws IOException {
        final InputStream dataStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse("text/xml; subtype=gml/3.1.1",
                "UTF-8", dataStream);
        TestHTTPClient mockHttp = new TestHTTPClient(httpResponse);

        WFSStrategy wfs = createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp,
                new StrictWFS_1_1_Strategy());

        // override the describe feature type url so it loads from the test resource
        wfs = spy(wfs);
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        when(wfs.buildDescribeFeatureTypeParametersForGET(CUBEWERX_GOVUNITCE.FEATURETYPENAME)).thenReturn(
                describeUrl);

        WFSContentDataStore ds = new WFSContentDataStore(wfs);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        // test data file contains three features...
        assertTrue(featureReader.hasNext());
        assertNotNull(featureReader.next());

        assertTrue(featureReader.hasNext());
        assertNotNull(featureReader.next());

        assertTrue(featureReader.hasNext());
        assertNotNull(featureReader.next());

        assertFalse(featureReader.hasNext());
    }
}
