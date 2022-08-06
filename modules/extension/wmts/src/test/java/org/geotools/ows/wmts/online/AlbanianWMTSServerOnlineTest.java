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

import java.awt.Image;
import java.net.URL;
import java.util.Properties;
import java.util.Set;

import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.map.WMTSMapLayer;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.CRS;
import org.geotools.tile.Tile;
import org.junit.Test;
import org.opengis.referencing.FactoryException;

public class AlbanianWMTSServerOnlineTest extends WMTSMapLayerOnlineTest {


    @Override
    protected String getFixtureId() {

        return "wmts-albanian";
    }

    @Override
    protected void setUpInternal() throws Exception {
    	
        serverURL = new URL(fixture.getProperty("albanian_server"));

        httpClient = new CountHttpGetClient();
        WebMapTileServer server = new WebMapTileServer(serverURL, httpClient);
        WMTSLayer wlayer = server.getCapabilities().getLayer("orthophoto_2007:OrthoImagery");
        assertNotNull(wlayer);
        kvpMapLayer = new WMTSMapLayer(server, wlayer);
    }
    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put(
                "albanian_server",
                "https://geoportal.asig.gov.al/service/wmts");
        return example;
    }

    /*
     * There are servers out there that don't seem to be able to cope with URL-encoded urls!
     */
    @Test
    public void testDefaultStyleRequired() throws Exception {
        URL test = serverURL;
        WebMapTileServer wmts = new WebMapTileServer(test);
        wmts.setType(WMTSServiceType.KVP);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        WMTSLayer wmtsLayer = capabilities.getLayer("orthophoto_2007:OrthoImagery");

        TileMatrixSet matrixSet = capabilities.getMatrixSet("EPSG:4326");
        WMTSTileService service =
                new WMTSTileService(
                        test.toExternalForm(), WMTSServiceType.KVP, wmtsLayer, null, matrixSet);

        GetTileRequest request = wmts.createGetTileRequest();
        request.setLayer(wmtsLayer);
        request.setRequestedBBox(service.getBounds());
        request.setRequestedWidth(256);
        request.setRequestedHeight(256);
        request.setCRS(CRS.decode("EPSG:4326"));
        Set<Tile> tiles = request.getTiles();
        for (Tile tile : tiles) {
        	System.out.println(tile.getTileState());
        	tile.loadImageTileImage(tile);
        	Image img = tile.getBufferedImage();
        }
    }
    @Test
    @Override
    public void testGetCoordinateReferenceSystem() throws FactoryException {

        assertEquals(
                "wrong CRS",
                "EPSG:4326",
                CRS.lookupIdentifier(kvpMapLayer.getCoordinateReferenceSystem(), true));
    }
    /** */
    @Override
     void checkEnv(ReferencedEnvelope env) throws FactoryException {
        assertEquals(
                "wrong CRS",
                "EPSG:4326",
                CRS.lookupIdentifier(env.getCoordinateReferenceSystem(), true));
        assertEquals(env.getMinimum(0), 39.538499, 0.001);
        assertEquals(env.getMinimum(1), 18.773065, 0.001);
        assertEquals(env.getMaximum(0), 42.758209, 0.001);
        assertEquals(env.getMaximum(1), 21.179343, 0.001);
    }
}
