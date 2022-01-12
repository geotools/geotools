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
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javax.media.jai.Interpolation;
import javax.xml.parsers.ParserConfigurationException;
import net.opengis.wmts.v_1.CapabilitiesType;
import org.apache.commons.lang3.NotImplementedException;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.AbstractHttpClient;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.geotools.wmts.WMTSConfiguration;
import org.geotools.xsd.Parser;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/** @author ian */
public class WMTSCoverageReaderTest {

    private static final String KVP_CAPA_RESOURCENAME = "test-data/getcapa_kvp.xml";

    private static final String REST_CAPA_RESOURCENAME = "test-data/admin_ch.getcapa.xml";

    @Test
    public void testRESTInitMapRequest() throws Exception {
        WebMapTileServer server =
                createServer(
                        new URL("http://wmts.geo.admin.ch/1.0.0/WMTSCapabilities.xml"),
                        REST_CAPA_RESOURCENAME);
        WMTSLayer layer =
                server.getCapabilities().getLayer("ch.are.agglomerationen_isolierte_staedte");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox = new ReferencedEnvelope(5, 12, 45, 49, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    @Test
    public void testKVPInitMapRequest() throws Exception {
        WebMapTileServer server =
                createServer(
                        new URL("http://localhost:8080/geoserver/gwc/service/wmts"),
                        KVP_CAPA_RESOURCENAME);
        WMTSLayer layer = server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        testInitMapRequest(wcr, bbox);
    }

    @Test
    public void testKVPInitMapRequestJpegOnly() throws Exception {
        WebMapTileServer server =
                createServer(
                        new URL("http://localhost:8080/geoserver/gwc/service/wmts"),
                        KVP_CAPA_RESOURCENAME);
        WMTSLayer layer = server.getCapabilities().getLayer("topp:states_jpeg");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        Set<Tile> responses = testInitMapRequest(wcr, bbox);
        assertFalse(responses.isEmpty());
        URL url = responses.iterator().next().getUrl();
        assertThat(
                "Expect format=image/jpeg in the request url",
                url.toString(),
                containsString("Format=image%2Fjpeg"));
    }

    @Test
    public void testWMTSServerWithCustomHttpClient() throws Exception {
        // Test basic authentication properties are set for requests.
        // GetCapabilities request is dependent on HTTPClient class.
        // GetTile request is dependent on HttpClient class from common http library.
        final URL wmtsUrl = new URL("http://fake.local/wmts");

        // Mock HTTPClient
        final File kvpCapaFile = getResourceFile(KVP_CAPA_RESOURCENAME);
        HTTPResponse getCapabilitiesResponse =
                new MockHttpResponse(kvpCapaFile, "application/xml; UTF-8");

        final File worldFile = getResourceFile("test-data/world.png");
        HTTPResponse getTileResponse = new MockHttpResponse(worldFile, "application/xml; UTF-8");

        final Map<String, String> getTileHeadersCalled = new HashMap<>();

        HTTPClient owsHttpClient =
                new AbstractHttpClient() {

                    @Override
                    public HTTPResponse get(URL url) throws IOException {
                        throw new NotImplementedException(
                                "For this test. GET with headers should be called.\n"
                                        + url.toExternalForm());
                    }

                    @Override
                    public HTTPResponse get(URL url, Map<String, String> headers) {
                        getTileHeadersCalled.clear();
                        getTileHeadersCalled.putAll(headers);
                        if (url.toString().toLowerCase().contains("request=getcapabilities")) {
                            return getCapabilitiesResponse;
                        } else if (url.toString().toLowerCase().contains("request=gettile")) {
                            return getTileResponse;
                        } else {
                            throw new NotImplementedException(
                                    "request is not implemented. " + url.toString());
                        }
                    }

                    @Override
                    public HTTPResponse post(
                            URL url, InputStream postContent, String postContentType)
                            throws IOException {
                        throw new UnsupportedOperationException("POST shouldn't be called.");
                    }
                };
        owsHttpClient.setUser("username");
        owsHttpClient.setPassword("userpassword");

        WebMapTileServer server =
                new WebMapTileServer(
                        wmtsUrl, owsHttpClient, Collections.singletonMap("header1", "value1"));
        assertNotNull(server.getCapabilities());
        assertTrue(
                "Expected header1 in the GetCapabilities request",
                getTileHeadersCalled.containsKey("header1"));

        WMTSLayer layer = server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox =
                new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        Set<Tile> responses = testInitMapRequest(wcr, bbox);
        assertFalse(responses.isEmpty());
        Tile firstTile = responses.iterator().next();
        BufferedImage bufferedImage = firstTile.getBufferedImage();
        assertNotNull(bufferedImage);

        // check headers defined at WebMapTileServer are sent with GetTileRequest
        assertNotNull(getTileHeadersCalled);
        assertTrue(
                "Expected header1 in the GetTile request",
                getTileHeadersCalled.containsKey("header1"));
    }

    @Test
    public void testGEOT6754() throws Exception {
        HTTPClient httpClientMock =
                new MockHttpClient() {
                    @Override
                    public HTTPResponse get(URL url, Map<String, String> headers)
                            throws IOException {
                        if (url.toString().toLowerCase().contains("request=gettile")) {
                            final File worldFile =
                                    WMTSCoverageReaderTest.this.getResourceFile(
                                            "test-data/world.png");
                            return new MockHttpResponse(worldFile, "application/xml; UTF-8");
                        } else {
                            throw new NotImplementedException(
                                    "request is not implemented. " + url.toString());
                        }
                    }
                };
        File fileCapabilities = getResourceFile("test-data/topp_states.getcapa.xml");
        WMTSCapabilities capabilities = createCapabilities(fileCapabilities);
        WebMapTileServer server =
                new WebMapTileServer(
                        new URL("http://fake.fake/fake"), httpClientMock, capabilities);

        WMTSLayer layer = server.getCapabilities().getLayer("topp:states");
        WMTSCoverageReader coverageReader = new WMTSCoverageReader(server, layer);

        ReferencedEnvelope requestedEnvelope =
                new ReferencedEnvelope(-100, -98, 41, 43, CRS.decode("EPSG:4326", true));
        GridGeometry2D gridGeometry =
                new GridGeometry2D(new GridEnvelope2D(0, 0, 1018, 632), requestedEnvelope);

        final Parameter<Interpolation> paramInterpolation =
                (Parameter<Interpolation>) AbstractGridFormat.INTERPOLATION.createValue();
        paramInterpolation.setValue(Interpolation.getInstance(Interpolation.INTERP_NEAREST));
        Parameter<GridGeometry2D> paramGridGeometry =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        paramGridGeometry.setValue(gridGeometry);

        coverageReader.read(new GeneralParameterValue[] {paramInterpolation, paramGridGeometry});
    }

    @Test
    public void testRESTInitMapRequestWithJpegTemplatesOnly() throws Exception {
        WebMapTileServer server =
                createServer(
                        new URL("https://maps.wien.gv.at/basemap"),
                        "test-data/GetCapaJPEGOnly.xml");
        WMTSLayer layer = server.getCapabilities().getLayer("bmaphidpi");
        WMTSCoverageReader wcr = new WMTSCoverageReader(server, layer);
        ReferencedEnvelope bbox = new ReferencedEnvelope(5, 12, 45, 49, CRS.decode("EPSG:4326"));
        int width = 400;
        int height = 200;
        ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
        assertNotNull(grid);
        GetTileRequest mapRequest = wcr.getTileRequest();
        String templateURL = mapRequest.getFinalURL().toExternalForm();
        assertEquals(
                "https://maps.wien.gv.at/basemap/bmaphidpi/{Style}/{TileMatrixSet}/{TileMatrix}/{TileRow}/{TileCol}.jpeg",
                templateURL);
    }

    protected Set<Tile> testInitMapRequest(WMTSCoverageReader wcr, ReferencedEnvelope bbox)
            throws Exception {

        int width = 400;
        int height = 200;
        ReferencedEnvelope grid = wcr.initTileRequest(bbox, width, height, null);
        assertNotNull(grid);
        GetTileRequest mapRequest = wcr.getTileRequest();
        mapRequest.setCRS(grid.getCoordinateReferenceSystem());
        Set<Tile> responses = mapRequest.getTiles();
        for (Tile t : responses) {
            /*System.out.println(t);
            // System.out.println(t.getTileIdentifier() + " " + t.getExtent());*/
            assertNotNull(t);
        }
        return responses;
    }

    private WebMapTileServer createServer(URL serverUrl, String resourceName) throws Exception {

        File capaFile = getResourceFile(resourceName);
        WMTSCapabilities capa = createCapabilities(capaFile);
        return new WebMapTileServer(serverUrl, capa);
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
}
