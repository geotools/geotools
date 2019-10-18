/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wmts.map;

import static org.hamcrest.CoreMatchers.containsString;
import static org.junit.Assert.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.*;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.apache.commons.lang3.NotImplementedException;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.MockHttpClient;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** @author ian */
public class WMTSCoverageReaderTest {

    private static final String KVP_CAPA_RESOURCENAME = "test-data/getcapa_kvp.xml";

    private static final String REST_CAPA_RESOURCENAME = "test-data/admin_ch.getcapa.xml";

    @Test
    public void testRESTInitMapRequest() throws Exception {
        WebMapTileServer server = createServer(REST_CAPA_RESOURCENAME);
        WMTSLayer layer =
                (WMTSLayer)
                        server.getCapabilities()
                                .getLayer("ch.are.agglomerationen_isolierte_staedte");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox = new ReferencedEnvelope(5, 12, 45, 49, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    @Test
    public void testKVPInitMapRequest() throws Exception {
        WebMapTileServer server = createServer(KVP_CAPA_RESOURCENAME);
        WMTSLayer layer = server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    @Test
    public void testKVPInitMapRequestJpegOnly() throws Exception {
        WebMapTileServer server = createServer(KVP_CAPA_RESOURCENAME);
        WMTSLayer layer = server.getCapabilities().getLayer("topp:states_jpeg");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        List<Tile> responses = testInitMapRequest(wcr, bbox);
        assertFalse(responses.isEmpty());
        URL url = responses.get(0).getUrl();
        assertThat(
                "Expect format=image/jpeg in the request url",
                url.toString(),
                containsString("format=image%2Fjpeg"));
    }

    @Test
    public void testWMTSServerWithCustomHttpClient() throws Exception {
        // Test basic authentication properties are set for requests.
        // GetCapabilities request is dependent on HTTPClient class.
        // GetTile request is dependent on HttpClient class from common http library.
        final URL wmtsUrl = new URL("http://fake.local/wmts");

        // Mock HTTPClient
        MockHTTPResponse getCapabilitiesResponse = new MockHTTPResponse(KVP_CAPA_RESOURCENAME);
        MockHTTPResponse getTileResponse = new MockHTTPResponse("test-data/world.png");
        final Map<String, String> getTileHeadersCalled = new HashMap<>();
        HTTPClient owsHttpClient =
                new MockHttpClient() {
                    @Override
                    public HTTPResponse get(URL url, Map<String, String> headers) {
                        if (url.toString().toLowerCase().contains("request=getcapabilities")) {
                            return getCapabilitiesResponse;
                        } else if (url.toString().toLowerCase().contains("request=gettile")) {
                            getTileHeadersCalled.clear();
                            getTileHeadersCalled.putAll(headers);
                            return getTileResponse;
                        } else {
                            throw new NotImplementedException(
                                    "request is not implemented. " + url.toString());
                        }
                    }
                };
        owsHttpClient.setUser("username");
        owsHttpClient.setPassword("userpassword");

        WebMapTileServer server = new WebMapTileServer(wmtsUrl, owsHttpClient, null);
        server.getHeaders().put("header1", "value1");
        assertNotNull(server.getCapabilities());

        WMTSLayer layer = server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        List<Tile> responses = testInitMapRequest(wcr, bbox);
        assertFalse(responses.isEmpty());
        BufferedImage bufferedImage = responses.get(0).loadImageTileImage(responses.get(0));
        assertNotNull(bufferedImage);

        // check headers defined at WebMapTileServer are sent with GetTileRequest
        assertNotNull(getTileHeadersCalled);
        assertTrue(
                "Expected header1 in the GetTile request",
                getTileHeadersCalled.containsKey("header1"));
    }

    public List<Tile> testInitMapRequest(WMTSCoverageReader wcr, ReferencedEnvelope bbox)
            throws Exception {

        int width = 400;
        int height = 200;
        ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
        assertNotNull(grid);
        GetTileRequest mapRequest = wcr.getTileRequest();
        mapRequest.setCRS(grid.getCoordinateReferenceSystem());
        Set<Tile> responses = wcr.wmts.issueRequest(mapRequest);
        for (Tile t : responses) {
            /*System.out.println(t);
            // System.out.println(t.getTileIdentifier() + " " + t.getExtent());*/
            assertNotNull(t);
        }
        return new ArrayList<>(responses);
    }

    private WebMapTileServer createServer(String resourceName) throws Exception {

        File capaFile = getResourceFile(resourceName);
        WMTSCapabilities capa = createCapabilities(capaFile);
        return new WebMapTileServer(capa);
    }

    private WMTSCapabilities createCapabilities(File capa) throws ServiceException {
        Object object;
        try (InputStream inputStream = new FileInputStream(capa)) {
            Parser parser = new Parser(new WMTSConfiguration());

            object = parser.parse(new InputSource(inputStream));

        } catch (SAXException | ParserConfigurationException | IOException e) {
            throw (ServiceException) new ServiceException("Error while parsing XML.").initCause(e);
        }
        if (object instanceof ServiceException) {
            throw (ServiceException) object;
        }

        return new WMTSCapabilities((CapabilitiesType) object);
    }

    private File getResourceFile(String resourceName) {
        try {
            URL resourceURL = getClass().getClassLoader().getResource(resourceName);
            assertNotNull("Can't find resource " + resourceName, resourceURL);
            File resourceFile = new File(resourceURL.toURI());
            assertTrue("Can't find resource file " + resourceURL, resourceFile.exists());

            return resourceFile;
        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    private class MockHTTPResponse implements HTTPResponse {
        private InputStream stream;

        public MockHTTPResponse(String resourceName) throws FileNotFoundException {
            File resourceFile = WMTSCoverageReaderTest.this.getResourceFile(resourceName);

            this.stream = new FileInputStream(resourceFile);
        }

        @Override
        public void dispose() {
            try {
                this.stream.close();
            } catch (IOException e) {
            }
        }

        @Override
        public String getContentType() {
            return "application/xml; UTF-8";
        }

        @Override
        public String getResponseHeader(String headerName) {
            return null;
        }

        @Override
        public InputStream getResponseStream() throws IOException {
            return this.stream;
        }

        @Override
        public String getResponseCharset() {
            return StandardCharsets.UTF_8.toString();
        }
    }
}
