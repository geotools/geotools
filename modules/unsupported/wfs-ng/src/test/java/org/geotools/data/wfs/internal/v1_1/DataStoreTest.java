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

import static org.geotools.data.wfs.WFSTestData.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import org.geotools.data.FeatureReader;
import org.geotools.data.Query;
import org.geotools.data.Transaction;
import org.geotools.data.crs.ReprojectFeatureReader;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.gml2.bindings.GML2EncodingUtils;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.junit.Test;
import org.locationtech.jts.geom.Polygon;
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
 */
@SuppressWarnings("nls")
public class DataStoreTest {

    /** Test method for {@link WFS_1_1_0_DataStore#getTypeNames()}. */
    @Test
    public void testGetTypeNames() throws IOException, ServiceException {
        String[] expected = {
            "gubs_GovernmentalUnitCE",
            "gubs_GovernmentalUnitMCD",
            "gubs_GovernmentalUnitST",
            "hyd_HydroElementARHI",
            "hyd_HydroElementARMD",
            "hyd_HydroElementFLHI",
            "hyd_HydroElementFLMD",
            "hyd_HydroElementLIHI",
            "hyd_HydroElementLIMD",
            "hyd_HydroElementPTHI",
            "hyd_HydroElementPTMD",
            "hyd_HydroElementWBHI",
            "hyd_HydroElementWBMD",
            "trans_RoadSeg"
        };
        List<String> expectedTypeNames = Arrays.asList(expected);

        TestWFSClient wfs = createTestProtocol(CUBEWERX_GOVUNITCE.CAPABILITIES);

        WFSDataStore ds = new WFSDataStore(wfs);

        String[] typeNames = ds.getTypeNames();
        assertNotNull(typeNames);
        List<String> names = Arrays.asList(typeNames);
        assertEquals(expectedTypeNames.size(), names.size());
        assertEquals(new HashSet<String>(expectedTypeNames), new HashSet<String>(names));
    }

    /**
     * Test method for {@link
     * org.geotools.wfs.v_1_1_0.data.WFS_1_1_0_DataStore#getSchema(java.lang.String)}.
     */
    @Test
    public void testGetSchema() throws IOException, ServiceException {
        TestHttpResponse httpResponse =
                new TestHttpResponse("", "UTF-8", CUBEWERX_GOVUNITCE.SCHEMA.openStream());
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);

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
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/2.1.2",
                        "UTF-8",
                        stream("CubeWerx_nsdi/1.1.0/gml212.xml"));
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);

        wfs.setOutputformatOverride("text/xml; subtype=gml/2.1.2");

        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);

        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        while (featureReader.hasNext()) {
            SimpleFeature feature = featureReader.next();
            Object geometry = feature.getDefaultGeometry();
            assertNotNull(geometry);
            assertTrue(geometry instanceof Polygon);
        }
        GetFeatureRequest request = wfs.getRequest();
        assertEquals("text/xml; subtype=gml/2.1.2", request.getOutputFormat());
    }

    /**
     * Test for the useDefaultSRS parameter set to true. Query in a CRS different from the
     * DefaultSRS should be done in DefaultSRS and then reprojected.
     */
    @Test
    public void tesUseDefaultSRS()
            throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        CUBEWERX_GOVUNITCE.DATA.openStream());
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);

        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS.decode(CUBEWERX_GOVUNITCE.CRS);
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertEquals(otherCrs, featureReader.getFeatureType().getCoordinateReferenceSystem());
        GetFeatureRequest request = wfs.getRequest();
        assertEquals(CUBEWERX_GOVUNITCE.CRS, request.getSrsName());

        // use an SRS not supported by server
        CoordinateReferenceSystem unknownCrs = CRS.decode("EPSG:3003");
        query.setCoordinateSystem(unknownCrs);

        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(unknownCrs, featureReader.getFeatureType().getCoordinateReferenceSystem());
        request = wfs.getRequest();
        assertNull(request.getSrsName());
    }

    /**
     * Test for the useDefaultSRS parameter set to false. Query in a CRS listed in OtherSRS should
     * be done in OtherSRS and not reprojected.
     */
    @Test
    public void tesUseOtherSRS()
            throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        CUBEWERX_GOVUNITCE.DATA.openStream());
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);
        wfs.setUseDefaultSrs(true);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);

        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS.decode("EPSG:4326");
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(otherCrs, featureReader.getFeatureType().getCoordinateReferenceSystem());
        GetFeatureRequest request = wfs.getRequest();
        assertNull(request.getSrsName());

        // use an SRS not supported by server
        CoordinateReferenceSystem unknownCrs = CRS.decode("EPSG:3003");
        query.setCoordinateSystem(unknownCrs);

        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);
        assertTrue(featureReader instanceof ReprojectFeatureReader);
        assertEquals(unknownCrs, featureReader.getFeatureType().getCoordinateReferenceSystem());
        request = wfs.getRequest();
        assertNull(request.getSrsName());
    }

    /**
     * Test for the useDefaultSRS parameter set to false and OtherSRS specified in urn form. Query
     * in a CRS listed in OtherSRS should be done in OtherSRS and not reprojected.
     */
    @Test
    public void tesUseOtherSRSUsingURN()
            throws IOException, NoSuchAuthorityCodeException, FactoryException, ServiceException {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        CUBEWERX_GOVUNITCE.DATA.openStream());
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);
        Query query = new Query(CUBEWERX_GOVUNITCE.FEATURETYPENAME);

        // use the OtherSRS
        CoordinateReferenceSystem otherCrs = CRS.decode("EPSG:3857");
        query.setCoordinateSystem(otherCrs);
        FeatureReader<SimpleFeatureType, SimpleFeature> featureReader;
        featureReader = ds.getFeatureReader(query, Transaction.AUTO_COMMIT);
        assertNotNull(featureReader);

        assertEquals(
                GML2EncodingUtils.epsgCode(otherCrs),
                GML2EncodingUtils.epsgCode(
                        featureReader.getFeatureType().getCoordinateReferenceSystem()));
        // GetFeatureRequest request = wfs.getRequest();
        // assertEquals("urn:ogc:def:crs:EPSG::3857", request.getSrsName());
    }

    @Test
    public void tesGetFeatureReader() throws IOException, ServiceException {
        TestHttpResponse httpResponse =
                new TestHttpResponse(
                        "text/xml; subtype=gml/3.1.1",
                        "UTF-8",
                        CUBEWERX_GOVUNITCE.DATA.openStream());
        TestWFSClient wfs =
                createTestProtocol(
                        CUBEWERX_GOVUNITCE.CAPABILITIES, new MockHTTPClient(httpResponse));

        // override the describe feature type url so it loads from the test resource
        wfs.setDescribeFeatureTypeURLOverride(CUBEWERX_GOVUNITCE.SCHEMA);

        WFSDataStore ds = new WFSDataStore(wfs);
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
