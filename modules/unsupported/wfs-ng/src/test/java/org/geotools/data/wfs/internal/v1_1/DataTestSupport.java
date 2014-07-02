/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import javax.xml.namespace.QName;

import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.TestHttpResponse;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.ows.ServiceException;
import org.geotools.test.TestData;

@SuppressWarnings("nls")
/**
 * 
 *
 * @source $URL$
 */
public final class DataTestSupport {

    /**
     * A class holding the type name and test data location for a feature type
     */
    public static class TestDataType {
        /**
         * Location of a test data capabilities file
         */
        final String CAPABILITIES;

        /**
         * Location of a test DescribeFeatureType response for the given feature type
         */
        final String SCHEMA;

        /**
         * The type name, including namespace
         */
        final QName TYPENAME;

        /**
         * The type name as referred in the capabilities (ej, "topp:states")
         */
        final String FEATURETYPENAME;

        /**
         * The FeatureType CRS as declared in the capabilities
         */
        final String CRS;
        
        /**
         * The FeatureType Alternative CRS (if available) as declared in the capabilities
         */
        final String ALTERNATIVECRS;
        
        /**
         * The FeatureType Alternative CRS in URN format (if available) as declared in
         * the capabilities
         */
        final String URNCRS;

        /**
         * Location of a sample GetFeature response for this feature type
         */
        final String DATA;

        /**
         * @param folder
         *            the folder name under {@code test-data} where the test files for this feature
         *            type are stored
         * @param qName
         *            the qualified type name (ns + local name)
         * @param featureTypeName
         *            the name as stated in the capabilities
         * @param crs
         *            the default feature type CRS as stated in the capabilities
         */
        TestDataType(final String folder, final QName qName, final String featureTypeName,
                final String crs) {
            TYPENAME = qName;
            FEATURETYPENAME = featureTypeName;
            CRS = crs;
            ALTERNATIVECRS = crs;
            URNCRS = crs;
            CAPABILITIES = folder + "/GetCapabilities_1_1_0.xml";
            SCHEMA = folder + "/DescribeFeatureType_" + qName.getLocalPart() + ".xsd";
            DATA = folder + "/GetFeature_" + qName.getLocalPart() + ".xml";

            checkResource(CAPABILITIES);
            checkResource(SCHEMA);
            checkResource(DATA);
        }
        
        /**
         * @param folder
         *            the folder name under {@code test-data} where the test files for this feature
         *            type are stored
         * @param qName
         *            the qualified type name (ns + local name)
         * @param featureTypeName
         *            the name as stated in the capabilities
         * @param crs
         *            the default feature type CRS as stated in the capabilities
         * @param alternativecrs
         *            the default feature type CRS as stated in the capabilities
         */
        TestDataType(final String folder, final QName qName, final String featureTypeName,
                final String crs, final String alternativecrs, final String urncrs) {
            TYPENAME = qName;
            FEATURETYPENAME = featureTypeName;
            CRS = crs;
            ALTERNATIVECRS = alternativecrs;
            URNCRS = urncrs;
            CAPABILITIES = folder + "/GetCapabilities_1_1_0.xml";
            SCHEMA = folder + "/DescribeFeatureType_" + qName.getLocalPart() + ".xsd";
            DATA = folder + "/GetFeature_" + qName.getLocalPart() + ".xml";

            checkResource(CAPABILITIES);
            checkResource(SCHEMA);
            checkResource(DATA);
        }

        private void checkResource(String resource) {
            try {
                TestData.url(this, resource);
            } catch (FileNotFoundException e) {
                throw new IllegalArgumentException(e);
            }
        }

    }

    public static final TestDataType GEOS_ARCHSITES = new TestDataType("geoserver", new QName(
            "http://www.openplans.org/spearfish", "archsites"), "sf_archsites", "EPSG:26713");

    public static final TestDataType GEOS_POI = new TestDataType("geoserver", new QName(
            "http://www.census.gov", "poi"), "tiger_poi", "EPSG:4326");

    public static final TestDataType GEOS_ROADS = new TestDataType("geoserver", new QName(
            "http://www.openplans.org/spearfish", "roads"), "sf_roads", "EPSG:26713");
    
    public static final TestDataType GEOS_CURVE_ROADS = new TestDataType("geoserver", new QName(
            "http://www.openplans.org/spearfish", "curveroads"), "sf_curveroads", "EPSG:26713");

    public static final TestDataType GEOS_STATES = new TestDataType("geoserver", new QName(
            "http://www.openplans.org/topp", "states"), "topp_states", "EPSG:4326");

    public static final TestDataType GEOS_TASMANIA_CITIES = new TestDataType("geoserver",
            new QName("http://www.openplans.org/topp", "tasmania_cities"), "topp:tasmania_cities",
            "EPSG:4326");

    public static final TestDataType GEOS_TIGER_ROADS = new TestDataType("geoserver", new QName(
            "http://www.census.gov", "tiger_roads"), "tiger_tiger_roads", "EPSG:4326");

    public static final TestDataType CUBEWERX_GOVUNITCE = new TestDataType("CubeWerx_nsdi",
            new QName("http://www.fgdc.gov/framework/073004/gubs", "GovernmentalUnitCE"),
            "gubs_GovernmentalUnitCE", "EPSG:4269", "EPSG:4326", "EPSG:3857");

    public static final TestDataType CUBEWERX_ROADSEG = new TestDataType("CubeWerx_nsdi",
            new QName("http://www.fgdc.gov/framework/073004/transportation", "RoadSeg"),
            "trans:RoadSeg", "EPSG:4269");

    public static final TestDataType IONIC_STATISTICAL_UNIT = new TestDataType("Ionic", new QName(
            "http://www.fgdc.gov/fgdc/gubs", "StatisticalUnit"), "gubs:StatisticalUnit",
            "EPSG:4269");
    
    public static final TestDataType MAPSRV_GOVUNITCE = new TestDataType("MapServer",
            new QName("", "GovernmentalUnitCE"),
            "GovernmentalUnitCE", "EPSG:4269");

    public static TestWFS_1_1_0_Client wfs;
    
    /**
     * Creates the test {@link #wfs} with the provided connection factory that parses the
     * capabilities object from the test xml file pointed out by {@code capabilitiesFileName}
     * <p>
     * Tests methods call this one to set up a protocolHandler to test
     * </p>
     * 
     * @param capabilitiesFileName
     *            the relative path under {@code test-data} for the file containing the
     *            WFS_Capabilities document.
     * @throws IOException
     */
    public static void createTestProtocol(String capabilitiesFileName) throws IOException, ServiceException {
        createTestProtocol(capabilitiesFileName, new SimpleHttpClient());
    }

    /**
     * Creates the test {@link #wfs} with the provided connection factory that parses the
     * capabilities object from the test xml file pointed out by {@code capabilitiesFileName}
     * <p>
     * Tests methods call this one to set up a protocolHandler to test
     * </p>
     * 
     * @param capabilitiesFileName
     *            the relative path under {@code test-data} for the file containing the
     *            WFS_Capabilities document.
     * @throws IOException
     */
    public static void createTestProtocol(String capabilitiesFileName, HTTPClient http) throws IOException, ServiceException {
        wfs = new TestWFS_1_1_0_Client(DataTestSupport.class.getResource("test-data/" + capabilitiesFileName), http);
    }
        
    public static class MutableWFSConfig extends WFSConfig {
        
        String axisOrder = super.getAxisOrder();
        String axisOrderFilter = super.getAxisOrder();
        boolean useDefaultSrs = super.isUseDefaultSrs();
        String outputFormatOverride = super.getOutputformatOverride();
        
        public void setAxisOrder(String axisOrder){
            this.axisOrder = axisOrder;
        }
        
        public void setAxisOrderFilter(String axisOrderFilter){
            this.axisOrderFilter = axisOrderFilter;
        }
        
        @Override
        public String getAxisOrder() {
            return axisOrder;
        }

        @Override
        public String getAxisOrderFilter() {
            return axisOrderFilter;
        }

        @Override
        public boolean isUseDefaultSrs() {
            return useDefaultSrs;
        }

        public void setUseDefaultSrs(boolean useDefaultSrs) {
            this.useDefaultSrs = useDefaultSrs;
        }

        @Override
        public String getOutputformatOverride() {
            return outputFormatOverride;
        }

        public void setOutputformatOverride(String outputFormatOverride) {
            this.outputFormatOverride = outputFormatOverride;
        }   
    }
    
    public static class TestWFS_1_1_0_Client extends WFSClient {

        private URL describeFeatureTypeUrlOverride;

        private GetFeatureRequest request;
        
        public TestWFS_1_1_0_Client(URL capabilitiesURL, HTTPClient http) throws IOException, ServiceException {
            super(capabilitiesURL, http, new MutableWFSConfig());
        }

        /**
         * Allows to set an overriding url for the {@link #getDescribeFeatureTypeURLGet(String)} operation, for test purposes so it is not actually
         * needed to download the schema from the internet but from a resource file
         * 
         * @param url
         */
        public void setDescribeFeatureTypeURLOverride(URL url) {
            this.describeFeatureTypeUrlOverride = url;
        }
        
        public void setAxisOrderOverride(String axisOrder, String axisOrderFilter) {
            ((MutableWFSConfig) config).setAxisOrder(axisOrder);
            ((MutableWFSConfig) config).setAxisOrderFilter(axisOrderFilter);
        }

        public void setOutputformatOverride(String outputformatOverride) {
            ((MutableWFSConfig) config).setOutputformatOverride(outputformatOverride);
        }

        public void setUseDefaultSrs(boolean useDefaultSrs) {
            ((MutableWFSConfig) config).setUseDefaultSrs(useDefaultSrs);
        }

        @Override
        public DescribeFeatureTypeResponse issueRequest(DescribeFeatureTypeRequest request) throws IOException {
            if (describeFeatureTypeUrlOverride == null) {
                return super.issueRequest(request);
            }            
            HTTPResponse response = new TestHttpResponse(request.getOutputFormat(), "UTF-8", describeFeatureTypeUrlOverride);
            try {
                return new DescribeFeatureTypeResponse(request, response);
            } catch (ServiceException e) {
                throw new IOException(e);
            }
        }

        @Override
        public GetFeatureResponse issueRequest(GetFeatureRequest request) throws IOException {
            this.request = request;
            return super.issueRequest(request);
        }

        /**
         * @return the request
         */
        public GetFeatureRequest getRequest() {
            return request;
        }

    }
    
    public static class TestHttpProtocol extends SimpleHttpClient {

        private HTTPResponse mockResponse;

        public URL targetUrl;

        public String postCallbackContentType;

        public ByteArrayOutputStream postCallbackEncodedRequestBody;
        
        public URL capabilitiesURL;

        public TestHttpProtocol(HTTPResponse mockResponse) {
            this.mockResponse = mockResponse;
        }
        
        public TestHttpProtocol setCapabilities(URL capabilitiesURL){
            this.capabilitiesURL = capabilitiesURL;
            return this;
        }

        @Override
        public HTTPResponse get(final URL baseUrl) throws IOException {
            if (baseUrl.getProtocol().equals("file")) {
                return new TestHttpResponse(baseUrl, "text/xml");
            }
            this.targetUrl = baseUrl;
            return mockResponse;
        }

        @Override
        public HTTPResponse post(final URL url, final InputStream postContent,
                final String postContentType) throws IOException {
            
            this.targetUrl = url;
            this.postCallbackContentType = postContentType;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            this.postCallbackEncodedRequestBody = out;
            IOUtils.copy(postContent, postCallbackEncodedRequestBody);
            return mockResponse;
        }
    }

}
