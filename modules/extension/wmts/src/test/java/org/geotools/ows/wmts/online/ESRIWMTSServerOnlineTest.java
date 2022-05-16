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
import org.geotools.ows.wmts.WebMapTileServer;
import org.geotools.ows.wmts.model.TileMatrix;
import org.geotools.ows.wmts.model.TileMatrixSet;
import org.geotools.ows.wmts.model.WMTSCapabilities;
import org.geotools.ows.wmts.model.WMTSLayer;
import org.geotools.ows.wmts.model.WMTSServiceType;
import org.geotools.ows.wmts.request.GetTileRequest;
import org.geotools.test.OnlineTestCase;
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
     * ESRI ArcGis Servers require that the style is named and not left blank.
     * GetTileRequest has code to set the layer's default style according to the capabilities.
     */
    @Test
    public void testDefaultStyleRequired() throws Exception {
        URL test = esriWMTS;
        WebMapTileServer wmts = new WebMapTileServer(test);
        wmts.setType(WMTSServiceType.KVP);
        WMTSCapabilities capabilities = wmts.getCapabilities();
        WMTSLayer wmtsLayer = capabilities.getLayer("Toronto");
        String matrixSetName = "default028mm";

        TileMatrixSet matrixSet = capabilities.getMatrixSet(matrixSetName);
        TileMatrix matrix = matrixSet.getMatrices().get(0);

        GetTileRequest request = wmts.createGetTileRequest(false);
        request.setLayer(wmtsLayer);
        request.setFormat("image/png");
        request.setTileMatrixSet(matrixSetName);
        request.setTileMatrix(matrix.getIdentifier());
        request.setTileRow(0L);
        request.setTileCol(0L);
        String finalUrl = request.getFinalURL().toExternalForm();
        assertTrue(finalUrl.toLowerCase().contains("style=default"));
    }
}
