/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.ows.wmts.online;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;
import java.util.Set;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileFactory4326Test;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.map.WMTSMapLayer;
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

public class RestWMTSOnlineTest extends OnlineTestCase {

    private URL restWMTS;

    private WMTSTileService service;

    private CoordinateReferenceSystem expectedCrs;

    private WMTSMapLayer restMapLayer;

    @Override
    protected String getFixtureId() {
        return "wmts-rest";
    }

    @Override
    protected void setUpInternal() throws Exception {

        restWMTS = new URL(fixture.getProperty("rest_server"));
        service = createRESTService();
        expectedCrs = CRS.decode("EPSG:31287");

        WebMapTileServer server = new WebMapTileServer(restWMTS);
        WMTSLayer wlayer = server.getCapabilities().getLayer("topp:states");
        restMapLayer = new WMTSMapLayer(server, wlayer);
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put("rest_server", "http://raspberrypi:9000/wmts/1.0.0/WMTSCapabilities.xml");
        example.put("rest_layer", "topp:states");
        return example;
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

            assertNotNull("Can't find layer grey", capa.getLayer("grey"));

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
    public void testWebMapTileServerURL() throws Exception {

        WebMapTileServer wms = new WebMapTileServer(restWMTS);
        assertNotNull(wms.getCapabilities());
    }

    @Test
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

        WMTSLayer layer = capabilities.getLayer("topp:states");
        assertNotNull(layer);
        request.setLayer(layer);

        request.setRequestedWidth(800);
        request.setRequestedHeight(400);

        ReferencedEnvelope re = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        request.setRequestedBBox(re);

        // System.out.println(request.getFinalURL());
        Set<Tile> responses = request.getTiles();
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // System.out.println("Content Type: " + response.getContentType());
            // System.out.println(response.getTileIdentifier());
            BufferedImage image = response.getBufferedImage();
            assertEquals(256, image.getHeight());
        }
    }

    @Test
    public void testScales() {
        // double[][] expected =
        // {{20,31},{559082264.029,5.590822639508929E8},{1066.36479192,1066.36479192}};
        // double[][] expected =
        // {{14,31},{559082264.029,2.925714285714286E7},{1066.36479192,1066.36479192}};
        double[] expected = {14, 2.925714285714286E7, 3571.4285714285716};
        double delta = 0.00001;
        double[] scales = service.getScaleList();
        String msg = "Wrong scales of REST::grey";
        assertEquals(msg, (int) expected[0], scales.length);
        assertEquals(msg, expected[1], scales[0], delta);
        assertEquals(msg, expected[2], scales[13], delta);
    }

    @Test
    public void testCRS() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = service.getProjectedTileCrs();
        assertEquals(
                "Mismatching CRS in " + service.getName(), expectedCrs.getName(), crs.getName());
    }

    @Test
    public void testGetCoordinateReferenceSystem() throws FactoryException {

        assertEquals(
                "wrong CRS",
                "EPSG:4326",
                CRS.lookupIdentifier(restMapLayer.getCoordinateReferenceSystem(), true));
    }

    @Test
    public void testWebMercatorBounds() {
        ReferencedEnvelope expected = new ReferencedEnvelope(0.0, 180.0, -1.0, 0.0, expectedCrs);

        double delta = 0.001;

        ReferencedEnvelope env = service.getBounds();
        String msg = "Wrong bounds of REST::grey";
        assertEquals(msg, expected.getMinimum(1), env.getMinimum(1), delta);
        assertEquals(msg, expected.getMinimum(0), env.getMinimum(0), delta);
        assertEquals(msg, expected.getMaximum(1), env.getMaximum(1), delta);
        assertEquals(msg, expected.getMaximum(0), env.getMaximum(0), delta);
    }

    @Test
    public void testFindTilesInExtent() {
        ReferencedEnvelope env =
                new ReferencedEnvelope(-80, 80, -180.0, 180.0, DefaultGeographicCRS.WGS84);
        int million = (int) 1e6;
        int[] scales = {100 * million, 25 * million, 10 * million, million, 500000};
        for (int scale : scales) {
            Set<Tile> tiles = service.findTilesInExtent(env, scale, true, 100);
            assertFalse("findTilesInExtent shouldn't return empty sets.", tiles.isEmpty());
        }
    }

    @Test
    public void testIsNativelySupported() throws NoSuchAuthorityCodeException, FactoryException {
        CoordinateReferenceSystem crs = DefaultGeographicCRS.WGS84;
        assertTrue(restMapLayer.isNativelySupported(crs));
    }
}
