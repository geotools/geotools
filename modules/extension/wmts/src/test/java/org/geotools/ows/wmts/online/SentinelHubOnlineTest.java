/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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

import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.net.URL;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
import java.util.logging.Logger;
import javax.media.jai.PlanarImage;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverage.grid.GridEnvelope2D;
import org.geotools.coverage.grid.GridGeometry2D;
import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.WMTSSpecification.GetSingleTileRequest;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.map.WMTSCoverageReader;
import org.geotools.ows.wmts.model.TileMatrix;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.TileMatrixSetLink;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.ows.wmts.response.GetTileResponse;
import org.geotools.parameter.Parameter;
import org.geotools.referencing.CRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.geotools.tile.TileIdentifier;
import org.geotools.util.logging.Logging;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.parameter.GeneralParameterValue;

/**
 * Testing out the Sentinel Hub WMTS service.
 *
 * <p>You should provide your own instance-id by signing up at sentinel-hub.com
 *
 * @author Roar Brænden
 */
public class SentinelHubOnlineTest extends OnlineTestCase {

    private static Logger LOGGER = Logging.getLogger(SentinelHubOnlineTest.class);

    private String instanceId;

    private WebMapTileServer server;

    @Override
    protected String getFixtureId() {
        return "wmts-sentinel";
    }

    @Override
    protected Properties createExampleFixture() {
        Properties props = new Properties();
        props.put("instance-id", "<sentinel hub instance id>");
        return props;
    }

    @Override
    protected void connect() throws Exception {
        instanceId = this.fixture.getProperty("instance-id");
        Objects.requireNonNull(instanceId, "instance id must be specified.");

        URL sentinelUrl =
                new URL(
                        "https://services.sentinel-hub.com/ogc/wmts/"
                                + instanceId
                                + "?showLogo=false");
        server = new WebMapTileServer(sentinelUrl);
    }

    @Test
    public void testGetCapabilitiesTileSets() throws Exception {
        WMTSCapabilities capabilities = server.getCapabilities();
        WMTSLayer naturalColor = null;
        for (WMTSLayer layer : capabilities.getLayerList()) {
            LOGGER.fine(layer.getName());
            if ("NATURAL-COLOR".equals(layer.getName())) {
                naturalColor = layer;
                break;
            }
        }

        Assert.assertNotNull("We didn't find NATURAL-COLOR", naturalColor);

        TileMatrixSetLink matrixSetLink = null;
        for (Entry<String, TileMatrixSetLink> entry :
                naturalColor.getTileMatrixLinks().entrySet()) {
            if ("UTM32N".equals(entry.getKey())) {
                matrixSetLink = entry.getValue();
                break;
            }
        }

        Assert.assertNotNull("We didn't find UTM32N", matrixSetLink);

        TileMatrix matrix13 = null;
        for (TileMatrix matrix : capabilities.getMatrixSet("UTM32N").getMatrices()) {
            if ("13".equals(matrix.getIdentifier())) {
                matrix13 = matrix;
                break;
            }
        }
        Assert.assertNotNull("Didn't find 13 as matrix identifier.", matrix13);
        boolean foundPng = false;
        for (String format : naturalColor.getFormats()) {
            if ("image/png".equals(format)) {
                foundPng = true;
                break;
            }
        }

        Assert.assertTrue("We didn't find format image/png", foundPng);
    }

    @Test
    public void testFindTilesInExtent() throws Exception {
        WMTSCapabilities capabilities = server.getCapabilities();
        WMTSLayer layer = capabilities.getLayer("NATURAL-COLOR");
        TileMatrixSet matrixSet = capabilities.getMatrixSet("UTM32N");

        WMTSTileService service = new WMTSTileService(server, layer, matrixSet);
        ReferencedEnvelope extent =
                new ReferencedEnvelope(570890, 580064, 6776512, 6784376, CRS.decode("EPSG:32632"));

        Set<Tile> tiles = service.findTilesInExtent(extent, 10000.0, skipOnFailure, 100);
        int z = 0,
                minR = Integer.MAX_VALUE,
                maxR = Integer.MIN_VALUE,
                minC = Integer.MAX_VALUE,
                maxC = Integer.MIN_VALUE;
        BufferedImage image = null;
        for (Tile tile : tiles) {
            TileIdentifier identifier = tile.getTileIdentifier();
            LOGGER.fine(identifier.getId());
            z = identifier.getZ();
            minR = Math.min(minR, identifier.getY());
            maxR = Math.max(maxR, identifier.getY());
            minC = Math.min(minC, identifier.getX());
            maxC = Math.max(maxC, identifier.getX());
            if (identifier.getX() == 372 && identifier.getY() == 2250) {
                image = service.loadImageTileImage(tile);
            }
        }

        Assert.assertEquals(2247, minR);
        Assert.assertEquals(2254, maxR);
        Assert.assertEquals(368, minC);
        Assert.assertEquals(376, maxC);
        Assert.assertEquals("16", service.getTileMatrix(z).getIdentifier());

        Assert.assertNotNull(image);
        checkForLogo(image);
    }

    @Test
    public void testGetTileRequest() throws Exception {
        GetTileRequest request = server.createGetTileRequest(false);
        WMTSCapabilities capabilities = server.getCapabilities();
        WMTSLayer layer = capabilities.getLayer("NATURAL-COLOR");
        request.setLayer(layer);
        request.setFormat("image/png");
        request.setTileMatrixSet("UTM32N");
        request.setTileMatrix("16");
        request.setTileRow(2250);
        request.setTileCol(372);

        GetTileResponse response = server.issueRequest((GetSingleTileRequest) request);
        BufferedImage image = response.getTileImage();
        Assert.assertNotNull(image);
        checkForLogo(image);
    }

    @Test
    public void testSentinelCoverage() throws Exception {
        WMTSLayer layer = server.getCapabilities().getLayer("NATURAL-COLOR");
        WMTSCoverageReader reader = new WMTSCoverageReader(server, layer);
        int width = 4100;
        int height = 3512;
        ReferencedEnvelope extent =
                new ReferencedEnvelope(570890, 580064, 6776512, 6784376, CRS.decode("EPSG:32632"));
        Parameter<GridGeometry2D> readGG =
                (Parameter<GridGeometry2D>) AbstractGridFormat.READ_GRIDGEOMETRY2D.createValue();
        readGG.setValue(
                new GridGeometry2D(new GridEnvelope2D(new Rectangle(width, height)), extent));
        GeneralParameterValue[] parameters = {readGG};
        GridCoverage2D coverage = reader.read(parameters);
        RenderedImage image = coverage.getRenderedImage();
        Assert.assertNotNull(image);
        checkForLogo(PlanarImage.wrapRenderedImage(image).getAsBufferedImage());
    }

    private void checkForLogo(BufferedImage image) {
        int colorLogo = image.getRGB(25, 495);
        Assert.assertNotEquals("Looks like image contains logo", -1, colorLogo);
    }
}
