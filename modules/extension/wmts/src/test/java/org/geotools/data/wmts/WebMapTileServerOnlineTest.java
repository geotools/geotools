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
package org.geotools.data.wmts;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import org.geotools.data.ows.Layer;
import org.geotools.data.wmts.model.WMTSCapabilities;
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.data.wmts.request.GetTileRequest;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Richard Gould
 * @author ian
 * @source $URL$
 */
public class WebMapTileServerOnlineTest extends OnlineTestCase {
    URL serverURL;

    URL serverWithSpacedLayerNamesURL;

    URL brokenURL;

    private URL featureURL;

    private URL restWMTS;

    /*
     * @see TestCase#setUp()
     */
    @Override
    protected void setUpInternal() throws Exception {
        serverURL = new URL(fixture.getProperty("kvp_server"));
        brokenURL = new URL("http://afjklda.com");
        restWMTS = new URL(fixture.getProperty("rest_server"));
    }

    @Override
    protected Properties createExampleFixture() {
        Properties example = new Properties();
        example.put("kvp_server", "http://raspberrypi:8080/geoserver/gwc/service/wmts?");
        example.put("kvp_layer", "topp:states");
        example.put("rest_server", "http://raspberrypi:9000/wmts/1.0.0/WMTSCapabilities.xml");
        example.put("rest_layer", "topp:states");
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

        String format = "image/png";
        List<String> formats = layer.getFormats();
        assertTrue(formats.size() > 0);
        if (!formats.contains("image/png")) {
            format = (String) formats.get(0);
        }
        // request.setFormat(format);

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

    @Override
    protected String getFixtureId() {
        return "wmts";
    }
}
