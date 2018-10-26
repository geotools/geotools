/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wmts.client.WMTSTileFactory4326Test;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Richard Gould
 * @author ian
 */
public class WebMapTileServerOnlineTest extends OnlineTestCase {
    URL serverURL;

    URL serverWithSpacedLayerNamesURL;

    URL brokenURL;

    private URL featureURL;

    private URL restWMTS;

    private URL esriWMTS;
    private WMTSTileService[] services = new WMTSTileService[2];

    private CoordinateReferenceSystem[] _crs = new CoordinateReferenceSystem[2];
    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUpInternal() throws Exception {
        serverURL = new URL(fixture.getProperty("kvp_server"));
        brokenURL = new URL("http://afjklda.com");
        restWMTS = new URL(fixture.getProperty("rest_server"));
        esriWMTS = new URL(fixture.getProperty("esri_server"));
        services[0] = createRESTService();
        services[1] = createKVPService();

        _crs[0] = CRS.decode("EPSG:31287");
        _crs[1] = CRS.decode("EPSG:3857");
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put("kvp_server", "http://raspberrypi:8080/geoserver/gwc/service/wmts?");
        example.put("kvp_layer", "topp:states");
        example.put("rest_server", "http://raspberrypi:9000/wmts/1.0.0/WMTSCapabilities.xml");
        example.put("rest_layer", "topp:states");
        example.put(
                "esri_server",
                "https://sampleserver6.arcgisonline.com/arcgis/rest/services/Toronto/ImageServer/WMTS/1.0.0/WMTSCapabilities.xml");
        return example;
    }

    /*
     * Class under test for void WebMapServer(URL)
     */
    public void testWebMapTileServerURL() throws Exception {
        WebMapTileServer wms = new WebMapTileServer(serverURL);

        assertNotNull(wms.getCapabilities());

        wms = new WebMapTileServer(restWMTS);
        assertNotNull(wms.getCapabilities());
    }

    public void testGetCapabilities() throws Exception {
        WebMapTileServer wms = new WebMapTileServer(serverURL);

        assertNotNull(wms.getCapabilities());
    }

    public void testIssueGetTileRequestKVP()
            throws ServiceException, IOException, FactoryException {
        WebMapTileServer wmts = new WebMapTileServer(serverURL);
        issueGetTileRequest(wmts);
    }

    public void testIssueGetTileRequestREST()
            throws ServiceException, IOException, FactoryException {
        WebMapTileServer wmts = new WebMapTileServer(restWMTS);
        issueGetTileRequest(wmts);
    }

    public void issueGetTileRequest(WebMapTileServer wmts)
            throws ServiceException, FactoryException {

        WMTSCapabilities capabilities = wmts.getCapabilities();

        GetTileRequest request = wmts.createGetTileRequest();

        // request.setVersion("1.1.1");

        WMTSLayer layer = (WMTSLayer) capabilities.getLayer("topp:states");
        assertNotNull(layer);
        request.setLayer(layer);

        request.setRequestedWidth(800);
        request.setRequestedHeight(400);

        ReferencedEnvelope re = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        request.setRequestedBBox(re);

        // System.out.println(request.getFinalURL());
        Set<Tile> responses = (Set<Tile>) wmts.issueRequest(request);
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // System.out.println("Content Type: " + response.getContentType());
            // System.out.println(response.getTileIdentifier());
            BufferedImage image = response.getBufferedImage();
            assertEquals(256, image.getHeight());
        }
    }

    public void testGetEnvelope() throws Exception {
        WebMapTileServer wms = new WebMapTileServer(serverURL);

        WMTSCapabilities caps = wms.getCapabilities();

        Layer layer = (Layer) caps.getLayer("topp:states");
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");

        GeneralEnvelope envelope = wms.getEnvelope(layer, crs);
        assertNotNull(envelope);
        // <ows:LowerCorner>-134.731422 24.955967</ows:LowerCorner>
        // <ows:UpperCorner>-66.969849 49.371735</ows:UpperCorner>
        assertEquals(-124.731422, envelope.getMinimum(1), 0.0001);
        assertEquals(24.955967, envelope.getMinimum(0), 0.0001);
        assertEquals(-66.969849, envelope.getMaximum(1), 0.0001);
        assertEquals(49.371735, envelope.getMaximum(0), 0.0001);
    }

    private WMTSTileService createKVPService() throws Exception {
        try {
            URL capaResource =
                    getClass()
                            .getClassLoader()
                            .getResource("test-data/geosolutions_getcapa_kvp.xml");
            assertNotNull("Can't find KVP getCapa file", capaResource);
            File capaFile = new File(capaResource.toURI());
            WMTSCapabilities capa = WMTSTileFactory4326Test.createCapabilities(capaFile);

            String baseURL =
                    "http://demo.geo-solutions.it/geoserver/gwc/service/wmts?REQUEST=getcapabilities";

            WMTSLayer layer = capa.getLayer("unesco:Unesco_point");
            TileMatrixSet matrixSet = capa.getMatrixSet("EPSG:900913");
            assertNotNull(layer);
            assertNotNull(matrixSet);

            return new WMTSTileService(baseURL, WMTSServiceType.KVP, layer, null, matrixSet);

        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    private WMTSTileService createRESTService() throws Exception {
        try {
            URL capaResource =
                    getClass().getClassLoader().getResource("test-data/zamg.getcapa.xml");
            assertNotNull("Can't find REST getCapa file", capaResource);
            File capaFile = new File(capaResource.toURI());
            WMTSCapabilities capa = WMTSTileFactory4326Test.createCapabilities(capaFile);

            String baseURL =
                    "http://wmsx.zamg.ac.at/mapcacheStatmap/wmts/1.0.0/WMTSCapabilities.xml";
            return new WMTSTileService(
                    baseURL,
                    WMTSServiceType.REST,
                    capa.getLayer("grey"),
                    null,
                    capa.getMatrixSet("statmap"));

        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    @Test
    public void testScales() {
        // double[][] expected =
        // {{20,31},{559082264.029,5.590822639508929E8},{1066.36479192,1066.36479192}};
        // double[][] expected =
        // {{14,31},{559082264.029,2.925714285714286E7},{1066.36479192,1066.36479192}};
        double[][] expected = {
            {14, 2.925714285714286E7, 3571.4285714285716}, // REST
            {31, 5.590822639508929E8, 68247.34667369298}
        }; // KVP
        double delta = 0.00001;
        for (int i = 0; i < services.length; i++) {
            double[] scales = services[i].getScaleList();
            String msg = services[i].getType() + "::" + services[i].getLayerName();
            assertEquals(msg, (int) expected[i][0], scales.length);
            assertEquals(msg, expected[i][1], scales[0], delta);
            assertEquals(msg, expected[i][2], scales[13], delta);
        }
    }

    @Test
    public void testCRS() throws NoSuchAuthorityCodeException, FactoryException {
        for (int i = 0; i < services.length; i++) {
            CoordinateReferenceSystem crs = services[i].getProjectedTileCrs();
            assertEquals(
                    "Mismatching CRS in " + services[i].getName(),
                    _crs[i].getName(),
                    crs.getName());
        }
    }

    @Test
    public void testWebMercatorBounds() {
        ReferencedEnvelope[] expected = new ReferencedEnvelope[2];
        expected[0] = new ReferencedEnvelope(0.0, 180.0, -1.0, 0.0, _crs[0]);
        // expected[1] = new
        // ReferencedEnvelope(-180.0,180.0,-85.06,85.06,DefaultGeographicCRS.WGS84);
        expected[1] =
                new ReferencedEnvelope(
                        7.4667, 18.0339, 36.6749, 46.6564, DefaultGeographicCRS.WGS84);

        double delta = 0.001;

        for (int i = 1; i < 2; i++) { // FIXME: fix env for rest
            ReferencedEnvelope env = services[i].getBounds();
            String msg = services[i].getType() + "::" + services[i].getLayerName();
            assertEquals(msg, expected[i].getMinimum(1), env.getMinimum(1), delta);
            assertEquals(msg, expected[i].getMinimum(0), env.getMinimum(0), delta);
            assertEquals(msg, expected[i].getMaximum(1), env.getMaximum(1), delta);
            assertEquals(msg, expected[i].getMaximum(0), env.getMaximum(0), delta);
        }
    }

    @Test
    public void testFindTilesInExtent() {
        ReferencedEnvelope env =
                new ReferencedEnvelope(-80, 80, -180.0, 180.0, DefaultGeographicCRS.WGS84);
        int million = (int) 1e6;
        int scales[] = {100 * million, 25 * million, 10 * million, million, 500000};
        for (int i = 0; i < services.length; i++) {
            for (int k = 0; k < scales.length; k++) {
                Set<Tile> tiles = services[i].findTilesInExtent(env, scales[k], true, 100);
                // System.out.println(tiles.size());
                assertTrue(tiles.size() > 0);
            }
        }
    }
    /*
     * ESRI ArcGis Servers require that the style is named and not left blank. Sadly the WMTS specification agrees with them.
     * See GEOT-6017
     */
    @Test
    public void testDefaultStyleRequired() throws Exception {
        URL test = esriWMTS;
        WebMapTileServer wmts = new WebMapTileServer(test);
        wmts.setType(WMTSServiceType.KVP);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        WMTSLayer wmtsLayer = capabilities.getLayer("Toronto");

        TileMatrixSet matrixSet = capabilities.getMatrixSet("default028mm");
        WMTSTileService service =
                new WMTSTileService(
                        test.toExternalForm(), WMTSServiceType.KVP, wmtsLayer, null, matrixSet);

        GetTileRequest request = wmts.createGetTileRequest();
        request.setLayer(wmtsLayer);
        request.setRequestedBBox(service.getBounds());
        request.setRequestedWidth(256);
        request.setRequestedHeight(256);
        request.setCRS(DefaultGeographicCRS.WGS84);
        Set<Tile> tiles = request.getTiles();
        for (Tile tile : tiles) {
            assertTrue(tile.getUrl().toString().contains("style=default"));
        }
    }

    @Override
    protected String getFixtureId() {
        return "wmts";
    }
}
