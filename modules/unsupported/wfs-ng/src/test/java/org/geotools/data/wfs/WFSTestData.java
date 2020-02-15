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
package org.geotools.data.wfs;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javax.xml.namespace.QName;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.wfs.internal.DescribeFeatureTypeRequest;
import org.geotools.data.wfs.internal.DescribeFeatureTypeResponse;
import org.geotools.data.wfs.internal.GetFeatureRequest;
import org.geotools.data.wfs.internal.GetFeatureResponse;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.ows.ServiceException;
import org.geotools.test.TestData;

/** */
public class WFSTestData {

    /** A class holding the type name and test data location for a feature type */
    public static class TestDataType {
        /** Location of a test data capabilities file */
        public final URL CAPABILITIES;

        /** Location of a test DescribeFeatureType response for the given feature type */
        public final URL SCHEMA;

        /** The type name, including namespace */
        public final QName TYPENAME;

        /** The type name as referred in the capabilities (ej, "topp:states") */
        public final String FEATURETYPENAME;

        /** The FeatureType CRS as declared in the capabilities */
        public final String CRS;

        /** Location of a sample GetFeature response for this feature type */
        public final URL DATA;

        /**
         * @param folder the folder name under {@code test-data} where the test files for this
         *     feature type are stored
         * @param qName the qualified type name (ns + local name)
         * @param featureTypeName the name as stated in the capabilities
         * @param crs the default feature type CRS as stated in the capabilities
         */
        TestDataType(
                final String folder,
                final QName qName,
                final String featureTypeName,
                final String crs) {

            TYPENAME = qName;
            FEATURETYPENAME = featureTypeName;
            CRS = crs;
            CAPABILITIES = url(folder + "/GetCapabilities.xml");
            SCHEMA = url(folder + "/DescribeFeatureType_" + qName.getLocalPart() + ".xsd");
            DATA = url(folder + "/GetFeature_" + qName.getLocalPart() + ".xml");
        }
    }

    public static final TestDataType GEOS_ARCHSITES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/spearfish", "archsites"),
                    "sf_archsites",
                    "EPSG:26713");

    public static final TestDataType GEOS_POI_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.census.gov", "poi"),
                    "tiger_poi",
                    "EPSG:4326");

    public static final TestDataType GEOS_POI_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.census.gov", "poi"),
                    "tiger_poi",
                    "EPSG:4326");

    public static final TestDataType GEOS_ROADS_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/spearfish", "roads"),
                    "sf_roads",
                    "EPSG:26713");

    public static final TestDataType GEOS_ROADS_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.openplans.org/spearfish", "roads"),
                    "sf_roads",
                    "EPSG:26713");

    public static final TestDataType GEOS_STATES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/topp", "states"),
                    "topp_states",
                    "EPSG:4326");

    public static final TestDataType GEOS_STATES_10 =
            new TestDataType(
                    "GeoServer_1.7.x/1.0.0",
                    new QName("http://www.openplans.org/topp", "states"),
                    "topp_states",
                    "EPSG:4326");

    public static final TestDataType GEOS_TASMANIA_CITIES_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.openplans.org/topp", "tasmania_cities"),
                    "topp:tasmania_cities",
                    "EPSG:4326");

    public static final TestDataType GEOS_TIGER_ROADS_11 =
            new TestDataType(
                    "GeoServer_2.0/1.1.0",
                    new QName("http://www.census.gov", "tiger_roads"),
                    "tiger:tiger_roads",
                    "EPSG:4326");

    public static final TestDataType CUBEWERX_GOVUNITCE =
            new TestDataType(
                    "CubeWerx_nsdi/1.1.0",
                    new QName("http://www.fgdc.gov/framework/073004/gubs", "GovernmentalUnitCE"),
                    "gubs_GovernmentalUnitCE",
                    "EPSG:4269");

    public static final TestDataType CUBEWERX_ROADSEG =
            new TestDataType(
                    "CubeWerx_nsdi/1.1.0",
                    new QName("http://www.fgdc.gov/framework/073004/transportation", "RoadSeg"),
                    "trans_RoadSeg",
                    "EPSG:4269");

    public static final TestDataType IONIC_STATISTICAL_UNIT =
            new TestDataType(
                    "Ionic_unknown/1.1.0",
                    new QName("http://www.fgdc.gov/fgdc/gubs", "StatisticalUnit"),
                    "gubs_StatisticalUnit",
                    "EPSG:4269");

    /**
     * Creates the test {@link #wfs} with a default connection factory that parses the capabilities
     * object from the test xml file pointed out by {@code capabilitiesFileName}
     *
     * <p>Tests methods call this one to set up a protocolHandler to test
     *
     * @param capabilitiesFileName the relative path under {@code test-data} for the file containing
     *     the WFS_Capabilities document.
     */
    public static TestWFSClient createTestProtocol(String capabilitiesFileName)
            throws ServiceException, FileNotFoundException, IOException {
        return createTestProtocol(capabilitiesFileName, new MockHTTPClient(null));
    }

    /**
     * Creates the test {@link #wfs} with the provided connection factory that parses the
     * capabilities object from the test xml file pointed out by {@code capabilitiesURL}
     *
     * <p>Tests methods call this one to set up a protocolHandler to test
     */
    public static TestWFSClient createTestProtocol(URL capabilitiesURL)
            throws ServiceException, IOException {
        return new TestWFSClient(capabilitiesURL, new MockHTTPClient(null));
    }

    /**
     * Creates the test {@link #wfs} with the provided connection factory that parses the
     * capabilities object from the test xml file pointed out by {@code capabilitiesFileName}
     *
     * <p>Tests methods call this one to set up a protocolHandler to test
     *
     * @param capabilitiesFileName the relative path under {@code test-data} for the file containing
     *     the WFS_Capabilities document.
     */
    public static TestWFSClient createTestProtocol(String capabilitiesFileName, HTTPClient http)
            throws ServiceException, FileNotFoundException, IOException {
        return createTestProtocol(TestData.url(WFSTestData.class, capabilitiesFileName), http);
    }

    /**
     * Creates the test {@link #wfs} with the provided connection factory that parses the
     * capabilities object from the test xml file pointed out by {@code capabilitiesURL}
     *
     * <p>Tests methods call this one to set up a protocolHandler to test
     */
    public static TestWFSClient createTestProtocol(URL capabilitiesURL, HTTPClient http)
            throws ServiceException, IOException {
        return new TestWFSClient(capabilitiesURL, http);
    }

    public static class MockHTTPClient extends AbstractTestHTTPClient {

        private HTTPResponse mockResponse;

        public URL targetUrl;

        public String postCallbackContentType;

        public long postCallbackContentLength = -1;

        public ByteArrayOutputStream postCallbackEncodedRequestBody;

        public MockHTTPClient(HTTPResponse mockResponse) {
            this.mockResponse = mockResponse;
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
        public HTTPResponse post(
                final URL url, final InputStream postContent, final String postContentType)
                throws IOException {
            this.targetUrl = url;
            this.postCallbackContentType = postContentType;
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            IOUtils.copy(postContent, out);
            this.postCallbackEncodedRequestBody = out;
            this.postCallbackContentLength = out.size();
            return mockResponse;
        }
    }

    public static URL url(String resource) {
        URL url = WFSTestData.class.getResource("test-data/" + resource);
        assertNotNull("resource not found: " + resource, url);
        return url;
    }

    public static InputStream stream(String resource) {
        InputStream stream = WFSTestData.class.getResourceAsStream("test-data/" + resource);
        assertNotNull("resource not found: " + resource, stream);
        return stream;
    }

    protected static class MutableWFSConfig extends WFSConfig {

        public void setAxisOrder(String axisOrder) {
            this.axisOrder = axisOrder;
        }

        public void setAxisOrderFilter(String axisOrderFilter) {
            this.axisOrderFilter = axisOrderFilter;
        }

        public void setUseDefaultSrs(boolean useDefaultSrs) {
            this.useDefaultSrs = useDefaultSrs;
        }

        public void setOutputformatOverride(String outputFormatOverride) {
            this.outputformatOverride = outputFormatOverride;
        }

        public void setProtocol(Boolean protocol) {
            if (protocol == null) {
                this.preferredMethod = PreferredHttpMethod.AUTO;
            } else {
                this.preferredMethod =
                        protocol.booleanValue()
                                ? PreferredHttpMethod.HTTP_POST
                                : PreferredHttpMethod.HTTP_GET;
            }
        }

        public void setGmlCompatibleTypeNames(boolean gmlCompatibleTypeNames) {
            this.gmlCompatibleTypenames = gmlCompatibleTypeNames;
        }
    }

    public static WFSConfig getGmlCompatibleConfig() {
        MutableWFSConfig config = new MutableWFSConfig();
        config.setGmlCompatibleTypeNames(true);
        return config;
    }

    public static class TestWFSClient extends WFSClient {

        private URL describeFeatureTypeUrlOverride;

        private GetFeatureRequest request;

        public TestWFSClient(URL capabilitiesURL, HTTPClient http)
                throws IOException, ServiceException {
            super(capabilitiesURL, http, getGmlCompatibleConfig());
        }

        /**
         * Allows to set an overriding url for the {@link #getDescribeFeatureTypeURLGet(String)}
         * operation, for test purposes so it is not actually needed to download the schema from the
         * internet but from a resource file
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

        public void setProtocol(Boolean protocol) {
            ((MutableWFSConfig) config).setProtocol(protocol);
        }

        @Override
        public DescribeFeatureTypeResponse issueRequest(DescribeFeatureTypeRequest request)
                throws IOException {
            if (describeFeatureTypeUrlOverride == null) {
                return super.issueRequest(request);
            }
            HTTPResponse response =
                    new TestHttpResponse(
                            request.getOutputFormat(), "UTF-8", describeFeatureTypeUrlOverride);
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

        /** @return the request */
        public GetFeatureRequest getRequest() {
            return request;
        }
    }
}
