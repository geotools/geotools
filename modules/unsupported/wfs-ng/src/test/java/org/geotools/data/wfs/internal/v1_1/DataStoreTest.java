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
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import static org.geotools.data.wfs.internal.v1_1.DataTestSupport.*;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ForceCoordinateSystemFeatureReader;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.wfs.impl.TestHttpResponse;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.opengis.feature.simple.SimpleFeatureType;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * Unit test suite for WFSContentDataStore
 * 
 * @author Gabriel Roldan
 * @author Niels Charlier
 *
 *
 */
@SuppressWarnings("nls")
public class DataStoreTest {

    /**
     * Test method for {@link WFS_1_1_0_DataStore#getTypeNames()}.
     * 
     * @throws IOException
     * @throws ServiceException 
     */
    @Test
    public void testGetTypeNames() throws IOException, ServiceException {
        String[] expected = {"gubs_GovernmentalUnitCE", "gubs_GovernmentalUnitMCD",
                "gubs_GovernmentalUnitST", "hyd_HydroElementARHI", "hyd_HydroElementARMD",
                "hyd_HydroElementFLHI", "hyd_HydroElementFLMD", "hyd_HydroElementLIHI",
                "hyd_HydroElementLIMD", "hyd_HydroElementPTHI", "hyd_HydroElementPTMD",
                "hyd_HydroElementWBHI", "hyd_HydroElementWBMD", "trans_RoadSeg"};
        List<String> expectedTypeNames = Arrays.asList(expected);

        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);

        WFSContentDataStore ds = new WFSContentDataStore(wfs);

        String[] typeNames = ds.getTypeNames();
        assertNotNull(typeNames);
        List<String> names = Arrays.asList(typeNames);
        assertEquals(expectedTypeNames.size(), names.size());
        assertEquals(new HashSet<String>(expectedTypeNames), new HashSet<String>(names));
    }

    /**
     * Test method for
     * {@link org.geotools.wfs.v_1_1_0.data.WFS_1_1_0_DataStore#getSchema(java.lang.String)}.
     * 
     * @throws IOException
     * @throws ServiceException 
     */
    @Test
    public void testGetSchema() throws IOException, ServiceException {
        final InputStream schemaStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.SCHEMA);
        TestHttpResponse httpResponse = new TestHttpResponse("", "UTF-8", schemaStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);

        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);

        WFSContentDataStore ds = new WFSContentDataStore(wfs);

        try {
            ds.getSchema("nonExistentTypeName");
            fail("Expected SchemaNotFoundException");
        } catch (IOException e) {
            assertTrue(true);
        }

        SimpleFeatureType schema = ds.getSchema(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        assertNotNull(schema);
    }

    @Test
    public void testGetDefaultOutputFormat() throws IOException, ServiceException {
        final InputStream schemaStream = TestData.openStream(this, "CubeWerx_nsdi/gml212.xml");
        TestHttpResponse httpResponse = new TestHttpResponse("text/xml; subtype=gml/2.1.2", "UTF-8", schemaStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);

        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);

        WFSContentDataStore ds = new WFSContentDataStore(wfs);
        
        wfs.setOutputformatOverride("text/xml; subtype=gml/2.1.2");

        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);
        
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        while(featureReader.hasNext()) {
            SimpleFeature feature = featureReader.next();
            System.out.println(feature.getDefaultGeometry());
        }
        GetFeatureRequest request = wfs.getRequest();
        assertEquals("text/xml; subtype=gml/2.1.2", request.getOutputFormat());
    }

    
    /**
     * Test for the useDefaultSRS parameter set to true. Query in a CRS different
     * from the DefaultSRS should be done in DefaultSRS and then reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     * @throws ServiceException 
     */
    @Test
    public void tesUseDefaultSRS() throws IOException,
            NoSuchAuthorityCodeException, FactoryException, ServiceException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFSContentDataStore ds = new WFSContentDataStore(wfs);
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
        GetFeatureRequest request = wfs.getRequest();
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
        assertNull(request.getSrsName());
    }
    
    /**
     * Test for the useDefaultSRS parameter set to false. Query in a CRS listed in
     * OtherSRS should be done in OtherSRS and not reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     * @throws ServiceException 
     */
    @Test
    public void tesUseOtherSRS() throws IOException, NoSuchAuthorityCodeException,
            FactoryException, ServiceException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFSContentDataStore ds = new WFSContentDataStore(wfs);
        wfs.setUseDefaultSrs(true);
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
        GetFeatureRequest request = wfs.getRequest();
        assertNull(request.getSrsName());
    
        // use an SRS not supported by server
        CoordinateReferenceSystem unknownCrs = CRS.decode("EPSG:3003");
        query.setCoordinateSystem(unknownCrs);
    
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(unknownCrs, featureReader.getFeatureType()
                .getCoordinateReferenceSystem());
        request = wfs.getRequest();
        assertNull(request.getSrsName());
    }
    
    /**
     * Test for the useDefaultSRS parameter set to false and OtherSRS specified in
     * urn form. Query in a CRS listed in OtherSRS should be done in OtherSRS and
     * not reprojected.
     * 
     * @throws IOException
     * @throws NoSuchAuthorityCodeException
     * @throws FactoryException
     * @throws ServiceException 
     */
    @Test
    public void tesUseOtherSRSUsingURN() throws IOException,
            NoSuchAuthorityCodeException, FactoryException, ServiceException {
        final InputStream dataStream = TestData.openStream(this,
                CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse(
                "text/xml; subtype=gml/3.1.1", "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);
    
        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);
    
        WFSContentDataStore ds = new WFSContentDataStore(wfs);
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
        GetFeatureRequest request = wfs.getRequest();
        assertEquals("urn:ogc:def:crs:EPSG::3857", request.getSrsName());
    }
    
    @Test
    public void tesGetFeatureReader() throws IOException, ServiceException {
        final InputStream dataStream = TestData.openStream(this, CUBEWERX_GOVUNITCE.DATA);
        TestHttpResponse httpResponse = new TestHttpResponse("text/xml; subtype=gml/3.1.1",
                "UTF-8", dataStream);
        TestHttpProtocol mockHttp = new TestHttpProtocol(httpResponse);
        createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES, mockHttp);

        // override the describe feature type url so it loads from the test resource
        URL describeUrl = TestData.getResource(this, CUBEWERX_GOVUNITCE.SCHEMA);
        wfs.setDescribeFeatureTypeURLOverride(describeUrl);

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
