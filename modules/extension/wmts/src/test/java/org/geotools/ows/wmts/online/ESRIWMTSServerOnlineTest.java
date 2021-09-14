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

import java.net.URL;
import java.util.Properties;
import java.util.Set;
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.client.WMTSTileService;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.junit.Test;

public class ESRIWMTSServerOnlineTest extends OnlineTestCase {

    private URL esriWMTS;

    @Override
    protected String getFixtureId() {

        return "wmts-esri";
    }

    @Override
    protected void setUpInternal() throws Exception {

        esriWMTS = new URL(fixture.getProperty("esri_server"));
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put(
                "esri_server",
                "https://sampleserver6.arcgisonline.com/arcgis/rest/services/Toronto/ImageServer/WMTS/1.0.0/WMTSCapabilities.xml");
        return example;
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
            assertTrue(tile.getUrl().toString().toLowerCase().contains("style=default"));
        }
    }
}
