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
import org.geotools.data.wmts.model.WMTSLayer;
import org.geotools.data.wmts.model.WMTSCapabilities;
import org.geotools.data.wmts.request.GetTileRequest;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.geotools.test.OnlineTestCase;
import org.geotools.tile.Tile;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * @author Richard Gould
 * @author ian
 *
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

    public void testIssueGetTileRequestKVP() throws ServiceException, IOException, FactoryException {
        WebMapTileServer wmts = new WebMapTileServer(serverURL);
        issueGetTileRequest(wmts);
    }
    public void testIssueGetTileRequestREST() throws ServiceException, IOException, FactoryException {
        WebMapTileServer wmts = new WebMapTileServer(restWMTS);
        issueGetTileRequest(wmts);
    }
    public void issueGetTileRequest(WebMapTileServer wmts) throws ServiceException, FactoryException  {
        

        WMTSCapabilities capabilities = wmts.getCapabilities();

        GetTileRequest request = wmts.createGetTileRequest();

        // request.setVersion("1.1.1");

        WMTSLayer[] layers = WMTSUtils.getNamedLayers(capabilities);
        
        WMTSLayer layer = (WMTSLayer) capabilities.getLayer("topp:states");
        assertNotNull(layer);
        request.setLayer(layer);

        Set<String> srss = WMTSUtils.getSRSs(capabilities);
        assertNotNull(srss);
        request.setRequestedWidth(800);
        request.setRequestedHeight(400);

        String format = "image/png";
        List<String> formats = layer.getFormats();
        assertTrue(formats.size() > 0);
        if (!formats.contains("image/png")) {
            format = (String) formats.get(0);
        }
//        request.setFormat(format);

        ReferencedEnvelope re = new ReferencedEnvelope(-180, 180, -90, 90, CRS.decode("EPSG:4326"));
        request.setRequestedBBox(re);

        // System.out.println(request.getFinalURL());
        Set<Tile> responses = (Set<Tile>) wmts.issueRequest(request);
        assertFalse(responses.isEmpty());
        for (Tile response : responses) {
            // System.out.println("Content Type: " + response.getContentType());
            //System.out.println(response.getTileIdentifier());
            BufferedImage image = response.getBufferedImage();
            assertEquals(256, image.getHeight());
        }
    }

    @Test
    public void testIssueGetMapRequestWithSpacedLayerNames() throws Exception {
    /*    WebMapServer wms = new WebMapServer(serverWithSpacedLayerNamesURL);

        WMSCapabilities capabilities = wms.getCapabilities();

        GetMapRequest request = wms.createGetMapRequest();

        Layer[] layers = WMSUtils.getNamedLayers(capabilities);
        Iterator<?> iter = Arrays.asList(layers).iterator();
        boolean atLeastOneLayerNameContainsSpaces = false;
        while (iter.hasNext()) {

            Layer layer = (Layer) iter.next();
            if (layer.getName().contains(" ")) {
                atLeastOneLayerNameContainsSpaces = true;
                request.addLayer(layer);
            }
        }

        // for the test to make sense at least one layer name must contain spaces
        assertTrue(atLeastOneLayerNameContainsSpaces);

        Set<?> srss = WMSUtils.getSRSs(capabilities);
        request.setSRS((String) srss.iterator().next());
        request.setDimensions("400", "300");
        String format = "image/gif";
        List<String> formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        if (!formats.contains("image/gif")) {
            format = (String) formats.get(0);
        }
        request.setFormat(format);

        request.setBBox("-93.239328320802,44.8440037593985,-92.976671679198,45.0409962406015");

        GetMapResponse response = (GetMapResponse) wms.issueRequest(request);

        BufferedImage image = ImageIO.read(response.getInputStream());
        assertNotNull(image);*/
    }

    public void testIssueGetFeatureInfoRequest() throws Exception {
        /*
         * TODO fix this
         * 
         * // http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?LAYERS=DEMO&FORMAT=image/png&TRANSPARENT=TRUE&HEIGHT=213&REQUEST=GetMap&BBOX=-172.367,35.
         * 667300000000004,-11.562400000000014,83.8293&WIDTH=710&STYLES=&SRS=EPSG:4326&VERSION=1.1.1
         * 
         * WebMapServer wms = new WebMapServer(featureURL); WMSCapabilities capabilities = wms.getCapabilities();
         * 
         * assertNotNull(capabilities);
         * 
         * GetMapRequest getMapRequest = wms.createGetMapRequest();
         * 
         * List layers = Arrays.asList(WMSUtils.getNamedLayers(capabilities)); List simpleLayers = new ArrayList(); Iterator iter = layers.iterator();
         * while (iter.hasNext()) { Layer layer = (Layer) iter.next(); SimpleLayer sLayer = new SimpleLayer(layer.getName(), "");
         * simpleLayers.add(sLayer); List styles = layer.getStyles(); if (styles.size() == 0) { sLayer.setStyle(""); continue; } Random random = new
         * Random(); int randomInt = random.nextInt(styles.size()); sLayer.setStyle((String) styles.get(randomInt)); }
         * getMapRequest.setLayers(simpleLayers);
         * 
         * getMapRequest.setSRS("EPSG:4326"); getMapRequest.setDimensions("400", "400"); getMapRequest.setFormat("image/png");
         * 
         * getMapRequest.setBBox("-114.01268,59.4596930,-113.26043,60.0835794"); URL url2 = getMapRequest.getFinalURL();
         * 
         * GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getMapRequest);
         * request.setQueryLayers(WMSUtils.getQueryableLayers(capabilities)); request.setQueryPoint(200, 200); request.setInfoFormat("text/html");
         * 
         * System.out.println(request.getFinalURL());
         * 
         * GetFeatureInfoResponse response = (GetFeatureInfoResponse) wms.issueRequest(request); System.out.println(response.getContentType());
         * assertTrue( response.getContentType().indexOf("text/html") != -1 ); BufferedReader in = new BufferedReader(new
         * InputStreamReader(response.getInputStream())); String line;
         * 
         * boolean textFound = false; while ((line = in.readLine()) != null) { System.out.println(line); if
         * (line.indexOf("Wood Buffalo National Park") != -1) { textFound = true; } } assertTrue(textFound);
         */
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

/*        crs = CRS.decode("EPSG:42304");
        envelope = wms.getEnvelope(layer, crs);

        // minx="-2.2e+06" miny="-712631" maxx="3.0728e+06" maxy="3.84e+06" />
        assertEquals(envelope.getMinimum(0), -2.2e+06, 0.0);
        assertEquals(envelope.getMinimum(1), -712631, 0.0);
        assertEquals(envelope.getMaximum(0), 3.0728e+06, 0.0);
        assertEquals(envelope.getMaximum(1), 3.84e+06, 0.0);*/

/*        layer = (Layer) caps.getLayerList().get(2);
        crs = CRS.decode("EPSG:4326");

        envelope = wms.getEnvelope(layer, crs);

        // minx="-178.838" miny="31.8844" maxx="179.94" maxy="89.8254" />
        assertEquals(envelope.getMinimum(0), -178.838, 0.0);
        assertEquals(envelope.getMinimum(1), 31.8844, 0.0);
        assertEquals(envelope.getMaximum(0), 179.94, 0.0);
        assertEquals(envelope.getMaximum(1), 89.8254, 0.0);*/
    }

//    public void testServiceExceptions() throws Exception {
//        WebMapTileServer wms = new WebMapTileServer(serverURL);
//        GetTileRequest request = wms.createGetTileRequest();
//        request.setLayer("NoLayer");
//        try {
//            // System.out.println(request.getFinalURL());
//            Set<Tile> response = wms.issueRequest(request);
//            fail("this should have thrown an exception");
//        } catch (ServiceException e) {
//            // e.printStackTrace();
//        }
//    }

    @Override
    protected String getFixtureId() {
    
        return "wmts";
    }
}
