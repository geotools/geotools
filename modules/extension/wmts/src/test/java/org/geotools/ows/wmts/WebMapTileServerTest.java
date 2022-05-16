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
package org.geotools.ows.wmts;

import static org.geotools.ows.wmts.WMTSTestUtils.createCapabilities;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Map;
import java.util.Properties;
import org.geotools.TestData;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.http.AbstractHttpClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wmts.WMTSSpecification.GetSingleTileRequest;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.AbstractGetTileRequest;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.ows.wmts.response.GetTileResponse;
import org.geotools.referencing.CRS;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/** @author Emanuele Tajariol (etj at geo-solutions dot it) */
public class WebMapTileServerTest {

    @Test
    public void nasaGetEnvelopeTest() throws Exception {

        WMTSCapabilities caps = createCapabilities("nasa.getcapa.xml");
        WebMapTileServer wmts =
                new WebMapTileServer(
                        new URL("https://gibs.earthdata.nasa.gov/wmts/epsg4326/best/wmts.cgi"),
                        caps);

        Layer layer = caps.getLayer("AMSRE_Surface_Rain_Rate_Night");
        // urn:ogc:def:crs:OGC:1.3:CRS84

        // <ows:WGS84BoundingBox crs="urn:ogc:def:crs:OGC:2:84">
        // <ows:LowerCorner>-180 -90</ows:LowerCorner>
        // <ows:UpperCorner>180 90</ows:UpperCorner>
        // </ows:WGS84BoundingBox>

        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:OGC:1.3:CRS84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("CRS:84")));

        assertNull(wmts.getEnvelope(layer, CRS.decode("EPSG:4326")));

        CoordinateReferenceSystem crs = CRS.decode("CRS:84");

        GeneralEnvelope envelope = wmts.getEnvelope(layer, crs);
        assertNotNull(envelope);
        assertEquals(-90.0, envelope.getMinimum(1), 0.0001);
        assertEquals(-180.0, envelope.getMinimum(0), 0.0001);
        assertEquals(90.0, envelope.getMaximum(1), 0.0001);
        assertEquals(180.0, envelope.getMaximum(0), 0.0001);
    }

    @Test
    public void chGetEnvelopeTest() throws Exception {
        WMTSCapabilities caps = createCapabilities("admin_ch.getcapa.xml");
        WebMapTileServer wmts =
                new WebMapTileServer(
                        new URL("http://wmts.geo.admin.ch/1.0.0/WMTSCapabilities.xml"), caps);

        Layer layer = caps.getLayer("ch.are.alpenkonvention");
        // <ows:SupportedCRS>urn:ogc:def:crs:EPSG:2056</ows:SupportedCRS>
        // <ows:WGS84BoundingBox>
        // <ows:LowerCorner>5.140242 45.398181</ows:LowerCorner>
        // <ows:UpperCorner>11.47757 48.230651</ows:UpperCorner>
        // </ows:WGS84BoundingBox>

        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:OGC:1.3:CRS84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("CRS:84")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("EPSG:2056")));
        assertNotNull(wmts.getEnvelope(layer, CRS.decode("urn:ogc:def:crs:EPSG:2056")));

        assertNull(wmts.getEnvelope(layer, CRS.decode("EPSG:4326")));

        CoordinateReferenceSystem crs = CRS.decode("CRS:84");

        GeneralEnvelope envelope = wmts.getEnvelope(layer, crs);
        assertNotNull(envelope);
        assertEquals(45.398181, envelope.getMinimum(1), 0.0001);
        assertEquals(5.140242, envelope.getMinimum(0), 0.0001);
        assertEquals(48.230651, envelope.getMaximum(1), 0.0001);
        assertEquals(11.47757, envelope.getMaximum(0), 0.0001);
    }

    /**
     * Check that servers that don't support KVP work
     *
     * @throws Exception
     */
    @Test
    public void testGEOT6741() throws Exception {
        WebMapTileServer server =
                WMTSTestUtils.createServer(
                        new URL(
                                "https://data.linz.govt.nz/services%3Bkey%3De501b3b9aa96472a85fe188cc8919487/wmts/1.0.0/layer/50767/WMTSCapabilities.xml"),
                        "linz.xml");

        GetTileRequest tileRequest = server.createGetTileRequest();
        WMTSLayer layer = server.getCapabilities().getLayer("layer-50767");
        tileRequest.setLayer(layer);
        tileRequest.setFormat("image/png");

        String url = ((AbstractGetTileRequest) tileRequest).getTemplateUrl();
        assertFalse(url.contains("REQUEST=GetTile"));
    }

    /** Add ability to parse "broken" URN from WMTS Spec urn:ogc:def:crs:EPSG:6.18:3:3857 */
    @Test
    public void testGEOT6742() throws Exception {
        WebMapTileServer server =
                WMTSTestUtils.createServer(
                        new URL("http://tileservice.charts.noaa.gov/tiles/wmts"),
                        "noaa-tileserver.xml");
        GetTileRequest tileRequest = server.createGetTileRequest();
        WMTSLayer layer = server.getCapabilities().getLayer("83637_2");
        tileRequest.setLayer(layer);
        // exception would have occurred here
        assertNotNull(tileRequest.getFinalURL());
    }

    private static class CheckHeadersHttpClient extends AbstractHttpClient {

        @Override
        public HTTPResponse get(URL url) throws IOException {
            Assert.fail("This method should not have been called.");
            return null;
        }

        @Override
        public HTTPResponse get(URL url, Map<String, String> headers) {
            Assert.assertNotNull(headers);
            String auth = headers.get("Authorization");
            Assert.assertEquals("dummy", auth);
            try {
                return new MockHttpResponse(TestData.file(null, "world.png"), "image/png");
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        @Override
        public HTTPResponse post(URL url, InputStream content, String contentType)
                throws IOException {
            Assert.fail("This method should not have been called.");
            return null;
        }
    }

    @Test
    public void testHttpHeadersPassedThrough() throws Exception {

        WebMapTileServer server =
                new WebMapTileServer(
                        new URL("http://dummy.net/"),
                        new CheckHeadersHttpClient(),
                        createCapabilities("getcapa_kvp.xml"));

        GetSingleTileRequest request = (GetSingleTileRequest) server.createGetTileRequest(false);
        server.getHeaders().put("Authorization", "dummy");

        WMTSLayer layer = server.getCapabilities().getLayer("spearfish");
        request.setLayer(layer);
        request.setFormat("image/png");
        TileMatrixSet matrixSet = server.getCapabilities().getMatrixSets().get(0);
        request.setTileMatrixSet(matrixSet.getIdentifier());
        request.setTileMatrix(matrixSet.getMatrices().get(0).getIdentifier());
        request.setTileRow(0L);
        request.setTileCol(0L);

        // Should trigger a call against CheckHeadersHttpClient
        server.issueRequest(request);
    }

    /**
     * Testing that the template url is chosen with style = "default". Based on the issue GEOS-10395
     */
    @Test
    public void testLayerWithoutDefaultStyle() throws Exception {
        URL capUrl =
                new URL(
                        "https://sgx.geodatenzentrum.de/wmts_topplus_open/1.0.0/WMTSCapabilities.xml");
        WebMapTileServer tileServer =
                WMTSTestUtils.createServer(capUrl, "sgx_geodatenzentrum_de.getcapa.xml");
        Assert.assertEquals(WMTSServiceType.REST, tileServer.getType());

        WMTSLayer layer = tileServer.getCapabilities().getLayer("web_light");
        GetTileRequest tileRequest = tileServer.createGetTileRequest(false);
        tileRequest.setLayer(layer);
        tileRequest.setFormat("image/png");
        tileRequest.setTileMatrixSet("EU_EPSG_25832_TOPPLUS");
        tileRequest.setTileMatrix("05");
        tileRequest.setTileRow(79L);
        tileRequest.setTileCol(111L);
        String finalUrl = tileRequest.getFinalURL().toExternalForm();

        Assert.assertEquals(
                "https://sgx.geodatenzentrum.de/wmts_topplus_open/tile/1.0.0/web_light/default/EU_EPSG_25832_TOPPLUS/05/79/111.png",
                finalUrl);
    }

    @Test
    public void testIssueRequestGivesTileResponseWithImageFetch() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("getcapa_kvp.xml");
        MockHttpClient httpClient = new MockHttpClient();
        httpClient.expectGet(
                new URL(
                        "http://localhost:8080/geoserver/gwc/service/wmts?REQUEST=GetTile&VERSION=1.0.0&SERVICE=WMTS&type=KVP&LAYER=spearfish&STYLE=default&FORMAT=image%2Fpng&TILEMATRIXSET=EPSG%3A4326&TILEMATRIX=EPSG%3A4326%3A0&TILEROW=0&TILECOL=0"),
                new MockHttpResponse(TestData.file(null, "world.png"), "image/png"));

        Properties props = new Properties();
        final URL serverUrl = new URL("http://localhost:8080/geoserver/gwc/service/wmts");
        GetSingleTileRequest request =
                new WMTSSpecification.GetKVPTileRequest(serverUrl, props, httpClient);

        WebMapTileServer server = new WebMapTileServer(serverUrl, httpClient, capabilities);
        request.setLayer(server.getCapabilities().getLayer("spearfish"));
        request.setStyle("default");
        request.setFormat("image/png");
        request.setTileMatrixSet("EPSG:4326");
        request.setTileMatrix("EPSG:4326:0");
        request.setTileRow(0L);
        request.setTileCol(0L);

        GetTileResponse response = server.issueRequest(request);
        BufferedImage tileImage = response.getTileImage();
        Assert.assertNotNull(tileImage);
    }

    /** Check that TIME is set when requestedTime is set on GetTileRequest */
    @Test
    public void testIssueRequestWithTime() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("getcapa_kvp.xml");
        MockHttpClient httpClient = new MockHttpClient();
        URL serverUrl = new URL("http://localhost:8080/geoserver/gwc/service/wmts");

        httpClient.expectGet(
                new URL(
                        "http://localhost:8080/geoserver/gwc/service/wmts?REQUEST=GetTile&VERSION=1.0.0&SERVICE=WMTS&type=KVP&"
                                + "LAYER=spearfish&STYLE=default&FORMAT=image%2Fpng&TILEMATRIXSET=EPSG%3A4326&"
                                + "TILEMATRIX=EPSG%3A4326%3A0&TILEROW=0&TILECOL=0&TIME=2020-01-01"),
                new MockHttpResponse(TestData.file(null, "world.png"), "image/png"));

        WebMapTileServer server = new WebMapTileServer(serverUrl, httpClient, capabilities);
        GetSingleTileRequest request = (GetSingleTileRequest) server.createGetTileRequest(false);
        request.setLayer(server.getCapabilities().getLayer("spearfish"));
        request.setStyle("default");
        request.setFormat("image/png");
        request.setTileMatrixSet("EPSG:4326");
        request.setTileMatrix("EPSG:4326:0");
        request.setTileRow(0L);
        request.setTileCol(0L);
        request.setRequestedTime("2020-01-01");

        GetTileResponse response = server.issueRequest(request);
        Assert.assertNotNull(response.getTileImage());
    }

    @Test
    public void testIssueRequestWithRestTileResponseWithImage() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("basemapGetCapa.xml");
        MockHttpClient httpClient = new MockHttpClient();
        httpClient.expectGet(
                new URL("https://maps.wien.gv.at/basemap/bmapoverlay/normal/EPSG%3A4326/2/1/2.png"),
                new MockHttpResponse(TestData.file(null, "world.png"), "image/png"));

        Properties props = new Properties();
        GetSingleTileRequest request =
                new WMTSSpecification.GetRestTileRequest(
                        capabilities.getService().getOnlineResource(), props, httpClient);

        WebMapTileServer server =
                new WebMapTileServer(
                        new URL("https://maps1.wien.gv.at/basemap"), httpClient, capabilities);
        request.setLayer(server.getCapabilities().getLayer("bmapoverlay"));
        request.setStyle("normal");
        request.setFormat("image/png");
        request.setTileMatrixSet("EPSG:4326");
        request.setTileMatrix("2");
        request.setTileRow(1L);
        request.setTileCol(2L);

        GetTileResponse response = server.issueRequest(request);
        BufferedImage tileImage = response.getTileImage();
        Assert.assertNotNull(tileImage);
    }

    @Test
    public void testIssueRequestWithSpecialChars() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("basemapGetCapa.xml");
        MockHttpClient httpClient = new MockHttpClient();
        httpClient.expectGet(
                new URL(
                        "https://maps.wien.gv.at/basemap/bmapoverlay/style%3D%20auto/%20%22%3C%3E%23%25%7C/2/1/2.png"),
                new MockHttpResponse(TestData.file(null, "world.png"), "image/png"));

        Properties props = new Properties();
        GetSingleTileRequest request =
                new WMTSSpecification.GetRestTileRequest(
                        capabilities.getService().getOnlineResource(), props, httpClient);

        WebMapTileServer server =
                new WebMapTileServer(
                        new URL("https://maps1.wien.gv.at/basemap"), httpClient, capabilities);
        request.setLayer(server.getCapabilities().getLayer("bmapoverlay"));
        request.setStyle("style= auto");
        request.setFormat("image/png");
        request.setTileMatrixSet(" \"<>#%|");
        request.setTileMatrix("2");
        request.setTileRow(1L);
        request.setTileCol(2L);

        GetTileResponse response = server.issueRequest(request);
        BufferedImage tileImage = response.getTileImage();
        Assert.assertNotNull(tileImage);
    }

    @Test
    public void testKvpRequestWithSpecialCharacters() throws Exception {
        WMTSCapabilities capabilities = createCapabilities("getcapa_specialchars_kvp.xml");
        MockHttpClient httpClient = new MockHttpClient();
        httpClient.expectGet(
                new URL(
                        "http://localhost:8080/geoserver/gwc/service/wmts?REQUEST=GetTile&VERSION=1.0.0&SERVICE=WMTS&type=KVP&LAYER=spear%25fish%2099&STYLE=style%3D%20auto&FORMAT=image%2Fpng&TILEMATRIXSET=%20%22%3C%3E%23%25%7C&TILEMATRIX=EPSG%3A4326%3A0&TILEROW=0&TILECOL=0"),
                new MockHttpResponse(TestData.file(null, "world.png"), "image/png"));

        Properties props = new Properties();
        final URL serverUrl = new URL("http://localhost:8080/geoserver/gwc/service/wmts");
        GetSingleTileRequest request =
                new WMTSSpecification.GetKVPTileRequest(serverUrl, props, httpClient);

        WebMapTileServer server = new WebMapTileServer(serverUrl, httpClient, capabilities);
        // Set up the request so that there are special characters in every field
        request.setLayer(server.getCapabilities().getLayer("spear%fish 99"));
        request.setStyle("style= auto");
        request.setFormat("image/png");
        request.setTileMatrixSet(" \"<>#%|");
        request.setTileMatrix("EPSG:4326:0");
        request.setTileRow(0L);
        request.setTileCol(0L);

        GetTileResponse response = server.issueRequest(request);
        BufferedImage tileImage = response.getTileImage();
        Assert.assertNotNull(tileImage);
    }
}
