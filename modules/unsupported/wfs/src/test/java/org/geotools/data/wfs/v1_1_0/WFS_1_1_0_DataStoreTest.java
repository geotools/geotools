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
package org.geotools.data.wfs.v1_1_0;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.CUBEWERX_GOVUNITCE;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.createTestProtocol;
import static org.geotools.data.wfs.v1_1_0.DataTestSupport.wfs;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.SchemaNotFoundException;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.wfs.protocol.wfs.GetFeature;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpProtocol;
import org.geotools.data.wfs.v1_1_0.DataTestSupport.TestHttpResponse;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit test suite for {@link WFS_1_1_0_DataStore}
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
public class WFS_1_1_0_DataStoreTest {

    /**
     * Test method for {@link WFS_1_1_0_DataStore#getTypeNames()}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetTypeNames() throws IOException {
        String[] expected = {"gubs:GovernmentalUnitCE", "gubs:GovernmentalUnitMCD",
                "gubs:GovernmentalUnitST", "hyd:HydroElementARHI", "hyd:HydroElementARMD",
                "hyd:HydroElementFLHI", "hyd:HydroElementFLMD", "hyd:HydroElementLIHI",
                "hyd:HydroElementLIMD", "hyd:HydroElementPTHI", "hyd:HydroElementPTMD",
                "hyd:HydroElementWBHI", "hyd:HydroElementWBMD", "trans:RoadSeg"};
        List<String> expectedTypeNames = Arrays.asList(expected);

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);

        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);

        String[] typeNames = ds.getTypeNames();
        assertNotNull(typeNames);
        List<String> names = Arrays.asList(typeNames);
        assertEquals(expectedTypeNames.size(), names.size());
        assertEquals(expectedTypeNames, names);
    }

    /**
     * Test method for
     * {@link org.geotools.wfs.v_1_1_0.data.WFS_1_1_0_DataStore#getSchema(java.lang.String)}.
     * 
     * @throws IOException
     */
    @Test
    public void testGetSchema() throws IOException {
        final InputStream schemaStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.SCHEMA);
        TestHttpResponse httpResponse = new TestHttpResponse("", "UTF-8", schemaStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);

        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);

        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);

        try {
            ds.getSchema("nonExistentTypeName");
            fail("Expected SchemaNotFoundException");
        } catch (SchemaNotFoundException e) {
            assertTrue(true);
        }

        SimpleFeatureType schema = ds.getSchema(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        assertNotNull(schema);
    }

    /**
     * Test for the useDefaultSRS parameter set to true. Query in a CRS different
     * from the DefaultSRS should be done in DefaultSRS and then reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     */
    @Test
    public void tesUseDefaultSRS() throws IOException,
            NoSuchAuthorityCodeException, FactoryException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
    
        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS
                .decode(CUBEWERX_GOVUNITCE.ALTERNATIVECRS);
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ForceCoordinateSystemFeatureReader);
        assertEquals(otherCrs, featureReader.getFeatureType()
                .getCoordinateReferenceSystem());
        GetFeature request = wfs.getRequest();
        assertEquals(CUBEWERX_GOVUNITCE.ALTERNATIVECRS, request.getSrsName());
    
        // use an SRS not supported by server
        CoordinateReferenceSystem unknownCrs = CRS.decode("EPSG:3003");
        query.setCoordinateSystem(unknownCrs);
    
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(unknownCrs, featureReader.getFeatureType()
                .getCoordinateReferenceSystem());
        request = wfs.getRequest();
        assertEquals(CUBEWERX_GOVUNITCE.CRS, request.getSrsName());
    }
    
    /**
     * Test for the useDefaultSRS parameter set to false. Query in a CRS listed in
     * OtherSRS should be done in OtherSRS and not reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     */
    @Test
    public void tesUseOtherSRS() throws IOException, NoSuchAuthorityCodeException,
            FactoryException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);
        ds.setUseDefaultSRS(true);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
    
        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS
                .decode(CUBEWERX_GOVUNITCE.ALTERNATIVECRS);
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(otherCrs, featureReader.getFeatureType()
                .getCoordinateReferenceSystem());
        GetFeature request = wfs.getRequest();
        assertEquals(CUBEWERX_GOVUNITCE.CRS, request.getSrsName());
    
        // use an SRS not supported by server
        CoordinateReferenceSystem unknownCrs = CRS.decode("EPSG:3003");
        query.setCoordinateSystem(unknownCrs);
    
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(unknownCrs, featureReader.getFeatureType()
                .getCoordinateReferenceSystem());
        request = wfs.getRequest();
        assertEquals(CUBEWERX_GOVUNITCE.CRS, request.getSrsName());
    }
    
    /**
     * Test for the useDefaultSRS parameter set to false and OtherSRS specified in
     * urn form. Query in a CRS listed in OtherSRS should be done in OtherSRS and
     * not reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     */
    @Test
    public void tesUseOtherSRSUsingURN() throws IOException,
            NoSuchAuthorityCodeException, FactoryException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
    
        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS.decode(CUBEWERX_GOVUNITCE.URNCRS);
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ForceCoordinateSystemFeatureReader);
    
        assertEquals(GML2EncodingUtils.epsgCode(otherCrs),
                GML2EncodingUtils.epsgCode(featureReader.getFeatureType()
                        .getCoordinateReferenceSystem()));
        GetFeature request = wfs.getRequest();
        assertEquals("urn:ogc:def:crs:EPSG::3857", request.getSrsName());
    }
    
    @Test
    public void tesGetFeatureReader() throws IOException {
        final InputStream dataStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse("text/xml; subtype=gml/3.1.1",
                "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);

        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);

        WFS_1_1_0_DataStore ds = new WFS_1_1_0_DataStore(wfs);
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
