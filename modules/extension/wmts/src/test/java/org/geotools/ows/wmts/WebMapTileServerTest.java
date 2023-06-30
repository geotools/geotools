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
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import org.geotools.TestData;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.http.AbstractHttpClient;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.ServiceException;
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
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.tile.Tile;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
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

    @Test
    public void testMapRequestCRS() throws Exception {

        WebMapTileServer server =
                createServer(
                        new URL("https://geodata.nationaalgeoregister.nl/tiles/service/wmts"),
                        "geodata.nationaalgeoregister.nl.xml");
        WMTSLayer layer = server.getCapabilities().getLayer("brtachtergrondkaartwater");
        ReferencedEnvelope bbox = new ReferencedEnvelope(51, 53, 4, 6, CRS.decode("EPSG:4326"));

        // Try with a CRS that doesn't exist in the layer and expect the first CRS supported by the
        // layer
        GetTileRequest tileRequest1 = server.createGetTileRequest();
        tileRequest1.setLayer(layer);
        tileRequest1.setCRS(CRS.decode("EPSG:4326"));
        tileRequest1.setRequestedBBox(bbox);
        tileRequest1.setRequestedHeight(768);
        tileRequest1.setRequestedWidth(1024);
        Set<Tile> receivedTiles = tileRequest1.getTiles();
        assertFalse(receivedTiles.isEmpty());
        for (Tile t : receivedTiles) {

            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:25831", recvdTileCRS);
        }

        // Now try with a specific  CRS that isn't the first one declared for the layer and expect
        // that one
        GetTileRequest tileRequest2 = server.createGetTileRequest();
        tileRequest2.setLayer(layer);
        tileRequest2.setCRS(CRS.decode("EPSG:3857"));
        tileRequest2.setRequestedBBox(bbox);
        tileRequest2.setRequestedHeight(768);
        tileRequest2.setRequestedWidth(1024);
        Set<Tile> receivedTiles2 = tileRequest2.getTiles();
        assertFalse(receivedTiles2.isEmpty());
        for (Tile t : receivedTiles2) {
            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:3857", recvdTileCRS);
        }

        // Now try with a CRS that exists but is in longitude first format
        GetTileRequest tileRequest3 = server.createGetTileRequest();
        tileRequest3.setLayer(layer);
        tileRequest3.setCRS(CRS.decode("EPSG:3857", true));
        tileRequest3.setRequestedBBox(bbox);
        tileRequest3.setRequestedHeight(768);
        tileRequest3.setRequestedWidth(1024);
        Set<Tile> receivedTiles3 = tileRequest3.getTiles();
        assertFalse(receivedTiles3.isEmpty());
        for (Tile t : receivedTiles3) {
            String recvdTileCRS =
                    t.getExtent()
                            .getCoordinateReferenceSystem()
                            .getIdentifiers()
                            .toArray()[0]
                            .toString();

            assertEquals("EPSG:3857", recvdTileCRS);
        }
    }

    /**
     * Check that servers that don't support KVP work
     *
     * @throws Exception
     */
    @Test
    public void testGEOT6741() throws Exception {
        WebMapTileServer server =
                createServer(
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
                createServer(
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
            throw new RuntimeException("This method should not have been called.");
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
            throw new RuntimeException("This method should not have been called.");
        }
    }

    @Test
    public void testHttpHeadersPassedThrough() throws Exception {

        WebMapTileServer server =
                new WebMapTileServer(
                        new URL("http://dummy.net/"),
                        new CheckHeadersHttpClient(),
                        createCapabilities("getcapa_kvp.xml"));
        GetTileRequest request = server.createGetTileRequest();
        request.getHeaders().put("Authorization", "dummy");

        final CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        request.setCRS(crs);
        request.setLayer(server.getCapabilities().getLayer("spearfish"));
        request.setRequestedBBox(new ReferencedEnvelope(-103.878, -103.729, 44.396, 44.456, crs));
        request.setRequestedWidth(1000);

        Set<Tile> tiles = request.getTiles();
        Assert.assertFalse(tiles.isEmpty());

        tiles.iterator().next().getBufferedImage();
    }

    /**
     * Testing that the template url is chosen with style = "default". Based on the issue GEOS-10395
     */
    @Test
    public void testLayerWithoutDefaultStyle() throws Exception {
        URL capUrl =
                new URL(
                        "https://sgx.geodatenzentrum.de/wmts_topplus_open/1.0.0/WMTSCapabilities.xml");
        WebMapTileServer tileServer = createServer(capUrl, "sgx_geodatenzentrum_de.getcapa.xml");
        Assert.assertEquals(WMTSServiceType.REST, tileServer.getType());

        WMTSLayer layer = tileServer.getCapabilities().getLayer("web_light");
        TileMatrixSet matrixSet =
                tileServer.getCapabilities().getMatrixSet("EU_EPSG_25832_TOPPLUS");

        GetTileRequest tileRequest = tileServer.createGetTileRequest();
        tileRequest.setLayer(layer);
        tileRequest.setRequestedHeight(256);
        tileRequest.setRequestedWidth(256);

        CoordinateReferenceSystem requestCrs = matrixSet.getCoordinateReferenceSystem();
        tileRequest.setCRS(requestCrs);

        double tileMinX = 540903.2072301595;
        double tileMaxX = 580038.9657121699;

        double tileMinY = 5714183.162769842;
        double tileMaxY = 5753318.921251852;

        ReferencedEnvelope requestBbox =
                new ReferencedEnvelope(tileMinX, tileMaxX, tileMinY, tileMaxY, requestCrs);
        tileRequest.setRequestedBBox(requestBbox);

        Iterator<Tile> tiles = tileRequest.getTiles().iterator();
        Assert.assertTrue(tiles.hasNext());

        Tile tile = tiles.next();

        Assert.assertEquals(
                "https://sgx.geodatenzentrum.de/wmts_topplus_open/tile/1.0.0/web_light/default/EU_EPSG_25832_TOPPLUS/05/79/111.png",
                tile.getUrl().toExternalForm());
    }

    @Test
    public void testGetTile()
            throws ServiceException, IOException, NoSuchAuthorityCodeException, FactoryException {
        URL url = new URL("https://sgx.geodatenzentrum.de/wmts_basemapde_schummerung");
        WebMapTileServer wmts = new WebMapTileServer(url);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        WMTSLayer layer = capabilities.getLayer("de_basemapde_web_raster_hillshade");
        TileMatrixSet matrixSet = capabilities.getMatrixSet("GLOBAL_WEBMERCATOR");

        GetTileRequest request = wmts.createGetTileRequest(false);
        request.setLayer(layer);
        CoordinateReferenceSystem requestCrs = matrixSet.getCoordinateReferenceSystem();
        request.setCRS(requestCrs);
        request.setFormat(layer.getFormats().get(0));
        request.setTileMatrixSet(matrixSet.getIdentifier());
        request.setTileMatrix("11");
        request.setTileRow(693);
        request.setTileCol(1094);
        URL finalUrl = request.getFinalURL();

        String expected =
                "https://sgx.geodatenzentrum.de/wmts_basemapde_schummerung/tile/1.0.0/de_basemapde_web_raster_hillshade/default/GLOBAL_WEBMERCATOR/11/693/1094.png";

        Assert.assertEquals(expected, finalUrl.toExternalForm());
    }

    private WebMapTileServer createServer(URL serverUrl, String resourceName) throws Exception {

        WMTSCapabilities capa = createCapabilities(resourceName);
        return new WebMapTileServer(serverUrl, capa);
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
        request.setTileRow(0);
        request.setTileCol(0);

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
        request.setTileRow(0);
        request.setTileCol(0);
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
        request.setTileRow(1);
        request.setTileCol(2);

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
        request.setTileRow(1);
        request.setTileCol(2);

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
        request.setTileRow(0);
        request.setTileCol(0);

        GetTileResponse response = server.issueRequest(request);
        BufferedImage tileImage = response.getTileImage();
        Assert.assertNotNull(tileImage);
    }
}
