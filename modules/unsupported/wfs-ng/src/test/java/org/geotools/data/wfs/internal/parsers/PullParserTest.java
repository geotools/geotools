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
package org.geotools.data.wfs.internal.parsers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketTimeoutException;
import java.net.URL;
import javax.xml.namespace.QName;
import org.geotools.TestData;
import org.geotools.data.wfs.internal.GetParser;
import org.geotools.feature.simple.SimpleFeatureTypeBuilder;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.referencing.CRS;
import org.geotools.wfs.v2_0.WFSConfiguration;
import org.geotools.xsd.Configuration;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.locationtech.jts.geom.Point;
import org.geotools.api.feature.simple.SimpleFeature;
import org.geotools.api.feature.simple.SimpleFeatureType;
import org.geotools.api.feature.type.FeatureType;

public class PullParserTest extends AbstractGetFeatureParserTest {

    public PullParserTest() {
        setSupportsCount(false);
    }

    @Override
    protected GetParser<SimpleFeature> getParser(
            final QName featureName,
            final URL schemaLocation,
            final SimpleFeatureType featureType,
            final URL getFeaturesRequest,
            String axisOrder,
            Configuration wfsConfiguration)
            throws IOException {

        @SuppressWarnings("PMD.CloseResource") // wrapped and returned
        InputStream inputStream = new BufferedInputStream(getFeaturesRequest.openStream());
        GetParser<SimpleFeature> parser =
                new PullParserFeatureReader(wfsConfiguration, inputStream, featureType, axisOrder);

        return parser;
    }

    @Override
    @Test
    @Ignore
    public void testParseGeoServer_States_100() {
        // TODO: support custom number format parsing in coordinates, such as
        // <gml:coordinates xmlns:gml="http://www.opengis.net/gml" decimal="#" cs="$" ts="_">
        // 0#0$0#0_0#0$10#0_10#0$10#0_10#0$0#0_0#0$0#0
        // </gml:coordinates>
    }

    /**
     * We inject an HTTPClient that are used for fetching the schema specified in
     * wfs_get_feature.xml
     */
    @Test
    public void testUsingHttpClientForSchema() throws Exception {
        Configuration wfsConfiguration = new WFSConfiguration();
        try (InputStream inputStream = TestData.openStream(this, "wfs_get_feature.xml")) {
            FeatureType featureType = createMjosovervakFeatureType();
            String axisOrder = null;
            HTTPClient httpClient = createMjosovervakHttpClient();
            GetParser<SimpleFeature> parser =
                    new PullParserFeatureReader(
                            wfsConfiguration, inputStream, featureType, axisOrder, httpClient);
            SimpleFeature first = parser.parse();
            Assert.assertNotNull(first);
            Assert.assertEquals("Svanfoss", first.getAttribute("STATION_NAME"));

            SimpleFeature second = parser.parse();
            Assert.assertNotNull(second);
            Assert.assertEquals("M071", second.getAttribute("STATION_CODE"));

            SimpleFeature third = parser.parse();
            Assert.assertNull(third);
        }
    }

    /** A failing Schema will result in an IOException, and not a ClassCastException */
    @Test(expected = IOException.class)
    public void testSchemaHttpError() throws Exception {
        Configuration wfsConfiguration = new WFSConfiguration();
        try (InputStream inputStream = TestData.openStream(this, "wfs_get_feature.xml")) {
            FeatureType featureType = createMjosovervakFeatureType();
            String axisOrder = null;
            HTTPClient httpClient = createFailingHttpClient();
            GetParser<SimpleFeature> parser =
                    new PullParserFeatureReader(
                            wfsConfiguration, inputStream, featureType, axisOrder, httpClient);
            parser.parse();
        }
    }

    private SimpleFeatureType createMjosovervakFeatureType() throws Exception {
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setNamespaceURI("http://www.aquamonitor.no/");
        builder.setName("Mjosovervak_stations");
        builder.crs(CRS.decode("urn:ogc:def:crs:EPSG::4326")).add("the_geom", Point.class);
        builder.add("SAMPLE_POINT_ID", Integer.class);
        builder.add("LATITUDE", Double.class);
        builder.add("LONGITUDE", Double.class);
        builder.add("PROJECT_ID", Integer.class);
        builder.add("PROJECT_NAME", String.class);
        builder.add("STATION_ID", Integer.class);
        builder.add("STATION_TYPE_ID", Integer.class);
        builder.add("STATION_TYPE", String.class);
        builder.add("STATION_CODE", String.class);
        builder.add("STATION_NAME", String.class);
        builder.add("FULL_STATION_NAME", String.class);

        return builder.buildFeatureType();
    }

    private HTTPClient createMjosovervakHttpClient() throws Exception {
        MockHttpClient mockClient = new MockHttpClient();
        try (InputStream xsdResponse = TestData.openStream(this, "Mjosovervak_stations.xsd")) {
            mockClient.expectGet(
                    new URL(
                            "https://aquamonitor.niva.no/geoserver/no.niva.aquamonitor/Mjosovervak_stations/wfs?service=WFS&version=2.0.0&request=DescribeFeatureType&typeName=no.niva.aquamonitor%3AMjosovervak_stations"),
                    new MockHttpResponse(xsdResponse, "text/xml"));
        }
        return mockClient;
    }

    private HTTPClient createFailingHttpClient() throws Exception {
        MockHttpClient mockClient = new MockHttpClient();
        try (InputStream xsdResponse = TestData.openStream(this, "Mjosovervak_stations.xsd")) {
            mockClient.expectGet(
                    new URL(
                            "https://aquamonitor.niva.no/geoserver/no.niva.aquamonitor/Mjosovervak_stations/wfs?service=WFS&version=2.0.0&request=DescribeFeatureType&typeName=no.niva.aquamonitor%3AMjosovervak_stations"),
                    new MockFailingHttpResponse());
        }
        return mockClient;
    }

    private static class MockFailingHttpResponse implements HTTPResponse {

        @Override
        public InputStream getResponseStream() throws IOException {
            throw new SocketTimeoutException();
        }

        @Override
        public String getResponseCharset() {
            return null;
        }

        @Override
        public void dispose() {}

        @Override
        public String getContentType() {
            return null;
        }

        @Override
        public String getResponseHeader(String headerName) {
            return null;
        }
    }
}
