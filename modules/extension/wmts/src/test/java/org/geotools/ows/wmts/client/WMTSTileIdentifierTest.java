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
package org.geotools.ows.wmts.client;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.io.File;
import java.net.URISyntaxException;
import java.net.URL;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.tile.TileIdentifier;
import org.geotools.tile.impl.ZoomLevel;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WMTSTileIdentifierTest {
    private WMTSTileService service;

    protected TileIdentifier tileId;

    @Before
    public void beforeTest() throws Exception {
        this.tileId = createTestTileIdentifier(5, 10, 12, "SomeService");
    }

    @After
    public void afterTest() {
        this.tileId = null;
    }

    protected TileIdentifier createTestTileIdentifier(int z, int x, int y, String name)
            throws Exception {
        if (service == null) {
            this.setup();
        }
        return createTestTileIdentifier(new WMTSZoomLevel(z, service), x, y, name);
    }

    @Before
    public void setup() throws Exception {
        service = createKVPService();
    }

    private WMTSTileService createKVPService() throws Exception {
        try {
            URL capaKvp = getClass().getClassLoader().getResource("test-data/getcapa_kvp.xml");
            assertNotNull(capaKvp);
            File capaFile = new File(capaKvp.toURI());
            WMTSCapabilities capa = WMTSTileFactory4326Test.createCapabilities(capaFile);

            String baseURL =
                    "http://demo.geo-solutions.it/geoserver/gwc/service/wmts?REQUEST=getcapabilities";

            WMTSLayer layer = capa.getLayer("spearfish");
            TileMatrixSet matrixSet = capa.getMatrixSet("EPSG:4326");
            assertNotNull(layer);
            assertNotNull(matrixSet);

            return new WMTSTileService(baseURL, WMTSServiceType.KVP, layer, null, matrixSet);
        } catch (URISyntaxException ex) {
            fail(ex.getMessage());
            return null;
        }
    }

    @Test
    public void testGetId() {
        Assert.assertEquals("SomeService_5_10_12", this.tileId.getId());
    }

    @Test
    public void testGetCode() {
        Assert.assertEquals("5/10/12", this.tileId.getCode());
    }

    @Test
    public void testGetRightNeighbour() {
        WMTSTileIdentifier neighbour =
                new WMTSTileIdentifier(11, 12, new WMTSZoomLevel(5, service), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getRightNeighbour());
    }

    @Test
    public void testGetLowertNeighbour() {
        WMTSTileIdentifier neighbour =
                new WMTSTileIdentifier(10, 13, new WMTSZoomLevel(5, service), "SomeService");

        Assert.assertEquals(neighbour, this.tileId.getLowerNeighbour());
    }

    protected TileIdentifier createTestTileIdentifier(
            ZoomLevel zoomLevel, int x, int y, String name) {
        return new WMTSTileIdentifier(x, y, zoomLevel, name);
    }
}
