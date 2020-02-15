/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2008-2012, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.ows.wms.test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import javax.imageio.ImageIO;
import javax.mail.internet.ContentType;
import junit.framework.TestCase;
import org.apache.commons.io.IOUtils;
import org.apache.commons.io.output.StringBuilderWriter;
import org.geotools.brewer.styling.builder.NamedLayerBuilder;
import org.geotools.brewer.styling.builder.StyleBuilder;
import org.geotools.brewer.styling.builder.StyledLayerDescriptorBuilder;
import org.geotools.data.ResourceInfo;
import org.geotools.data.ServiceInfo;
import org.geotools.data.ows.OperationType;
import org.geotools.data.ows.Specification;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.ows.wms.*;
import org.geotools.ows.wms.request.GetFeatureInfoRequest;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.request.GetStylesRequest;
import org.geotools.ows.wms.response.GetFeatureInfoResponse;
import org.geotools.ows.wms.response.GetMapResponse;
import org.geotools.ows.wms.response.GetStylesResponse;
import org.geotools.referencing.CRS;
import org.geotools.referencing.CRS.AxisOrder;
import org.geotools.referencing.crs.DefaultGeographicCRS;
import org.geotools.styling.NamedLayer;
import org.geotools.styling.Style;
import org.geotools.styling.StyleFactory;
import org.geotools.styling.StyleFactoryImpl;
import org.geotools.styling.StyledLayerDescriptor;
import org.geotools.util.factory.GeoTools;
import org.geotools.xml.styling.SLDParser;
import org.geotools.xml.styling.SLDTransformer;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

/**
 * This test case assume you have a default GeoServer 2.2 installed on 127.0.0.1 (ie localhost).
 *
 * <p>This is being used to look at WMS 1.1.1 vs WMS 1.3.0 compatibility issues for uDig.
 *
 * <p>
 *
 * <pre>
 * <code>
 * &lt;Layer queryable="1"&gt;
 * &lt;Name&gt;nurc:Img_Sample&lt;/Name&gt;
 *     &lt;Title&gt;North America sample imagery&lt;/Title&gt;
 *     &lt;Abstract/&gt;
 *     &lt;KeywordList&gt;
 *     &lt;Keyword&gt;WCS&lt;/Keyword&gt;
 *     &lt;Keyword&gt;worldImageSample&lt;/Keyword&gt;
 *     &lt;Keyword&gt;worldImageSample_Coverage&lt;/Keyword&gt;
 *     &lt;/KeywordList&gt;
 *     &lt;CRS&gt;EPSG:4326&lt;/CRS&gt;
 *     &lt;CRS&gt;CRS:84&lt;/CRS&gt;
 *     &lt;EX_GeographicBoundingBox&gt;
 *         &lt;westBoundLongitude&gt;-130.85168&lt;/westBoundLongitude&gt;
 *         &lt;eastBoundLongitude&gt;-62.0054&lt;/eastBoundLongitude&gt;
 *         &lt;southBoundLatitude&gt;20.7052&lt;/southBoundLatitude&gt;
 *         &lt;northBoundLatitude&gt;54.1141&lt;/northBoundLatitude&gt;
 *     &lt;/EX_GeographicBoundingBox&gt;
 *     &lt;BoundingBox CRS="CRS:84" minx="-130.85168" miny="20.7052" maxx="-62.0054" maxy="54.1141"/&gt;
 *     &lt;BoundingBox CRS="EPSG:4326" minx="20.7052" miny="-130.85168" maxx="54.1141" maxy="-62.0054"/&gt;
 *     &lt;Style&gt;
 *     &lt;Name&gt;raster&lt;/Name&gt;
 *     &lt;Title&gt;Default Raster&lt;/Title&gt;
 *     &lt;Abstract&gt;
 *     A sample style that draws a raster, good for displaying imagery
 *     &lt;/Abstract&gt;
 *     &lt;LegendURL width="20" height="20"&gt;
 *         &lt;Format&gt;image/png&lt;/Format&gt;
 *         &lt;OnlineResource xmlns:xlink="http://www.w3.org/1999/xlink" xlink:type="simple" xlink:href="http://localhost:8080/geoserver/ows?service=WMS&request=GetLegendGraphic&format=image%2Fpng&width=20&height=20&layer=Img_Sample"/&gt;
 *     &lt;/LegendURL&gt;
 *     &lt;/Style&gt;
 * &lt;/Layer&gt;
 * </code>
 * </pre>
 *
 * @author Jody Garnett
 */
public class LocalGeoServerOnlineTest extends TestCase {

    private static String LOCAL_GEOSERVER = "http://127.0.0.1:8080/geoserver/ows?SERVICE=WMS&";
    private static String LOCAL_LAYERS = "test_shp:TRONCON_ROUTE";

    private static WebMapServer wms;

    private static WMSCapabilities capabilities;

    private static URL serverURL;

    static {
        try {
            serverURL = new URL(LOCAL_GEOSERVER);
        } catch (MalformedURLException e) {
            serverURL = null;
        }
        ;
    }

    @Override
    protected void setUp() throws Exception {
        // System.out.println("CRS configured to
        // forceXY"+System.getProperty("org.geotools.referencing.forceXY"));
        // Hints.putSystemDefault(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, Boolean.TRUE);
        if (wms == null) {
            // do setup once!
            if (serverURL != null) {
                try {
                    wms = new WebMapServer(serverURL, new MultithreadedHttpClient());
                    capabilities = wms.getCapabilities();
                } catch (Exception eek) {
                    serverURL = null;
                    throw eek;
                }
            }
        }
    }

    public void testCRSEnvelope() {
        CRSEnvelope test = new CRSEnvelope(null, -20, -100, 20, 100);
        test.setSRSName("EPSG:4326", false);
        CoordinateReferenceSystem crs = test.getCoordinateReferenceSystem();
        assertEquals(AxisOrder.NORTH_EAST, CRS.getAxisOrder(crs));

        test = new CRSEnvelope(null, 100, -20, 100, 20);
        test.setSRSName("EPSG:4326", true);
        crs = test.getCoordinateReferenceSystem();
        assertEquals(AxisOrder.EAST_NORTH, CRS.getAxisOrder(crs));
    }

    public void testLocalGeoServer() {
        assertNotNull(wms);
        assertNotNull(capabilities);

        assertEquals("Version Negotiation", "1.3.0", capabilities.getVersion());
        Layer root = capabilities.getLayer();
        assertNotNull(root);
        assertNull("root layer does not have a name", root.getName());
        assertNotNull("title", root.getTitle());
    }

    public void testStates() {
        Layer states = find("topp:states");
        assertNotNull(states);

        ResourceInfo info = wms.getInfo(states);
        assertNotNull(info);
        assertEquals(states.getTitle(), info.getTitle());

        ReferencedEnvelope bounds = info.getBounds();
        assertNotNull(bounds);
        assertFalse(bounds.isEmpty());
    }

    private Layer find(String name, WMSCapabilities caps) {
        for (Layer layer : caps.getLayerList()) {
            if (name.equals(layer.getName())) {
                return layer;
            }
        }
        return null;
    }

    private Layer find(String name) {
        return find(name, capabilities);
    }

    public void testServiceInfo() {
        ServiceInfo info = wms.getInfo();
        assertNotNull(info);

        assertEquals(serverURL, wms.getCapabilities().getRequest().getGetCapabilities().getGet());
        assertEquals("GeoServer Web Map Service", info.getTitle());

        assertNotNull(info.getDescription());
    }

    String axisName(CoordinateReferenceSystem crs, int dimension) {
        return crs.getCoordinateSystem().getAxis(dimension).getName().getCode();
    }

    public void testImgSample130() throws Exception {
        Layer water_bodies = find("topp:tasmania_water_bodies");
        assertNotNull("Img_Sample layer found", water_bodies);
        CRSEnvelope latLon = water_bodies.getLatLonBoundingBox();
        assertEquals(
                "LatLonBoundingBox axis 0 name",
                "Geodetic longitude",
                axisName(latLon.getCoordinateReferenceSystem(), 0));
        assertEquals(
                "LatLonBoundingBox axis 0 name",
                "Geodetic latitude",
                axisName(latLon.getCoordinateReferenceSystem(), 1));

        boolean globalXY = Boolean.getBoolean("org.geotools.referencing.forceXY");

        CRSEnvelope bounds = water_bodies.getBoundingBoxes().get("EPSG:4326");
        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        if (globalXY) {
            // ensure WMS CRSEnvelope returned according to application globalXY setting
            assertEquals("EPSG:4326", AxisOrder.EAST_NORTH, CRS.getAxisOrder(boundsCRS));
        } else {
            assertEquals("EPSG:4326", AxisOrder.NORTH_EAST, CRS.getAxisOrder(boundsCRS));
        }
        if (CRS.getAxisOrder(boundsCRS) == AxisOrder.EAST_NORTH) {
            assertEquals("axis order 0 min", latLon.getMinimum(1), bounds.getMinimum(1));
            assertEquals("axis order 1 min", latLon.getMinimum(0), bounds.getMinimum(0));
            assertEquals("axis order 1 max", latLon.getMaximum(0), bounds.getMaximum(0));
            assertEquals("axis order 1 min", latLon.getMaximum(1), bounds.getMaximum(1));
        }

        if (CRS.getAxisOrder(boundsCRS) == AxisOrder.NORTH_EAST) {
            assertEquals("axis order 0 min", latLon.getMinimum(1), bounds.getMinimum(0));
            assertEquals("axis order 1 min", latLon.getMinimum(0), bounds.getMinimum(1));
            assertEquals("axis order 1 max", latLon.getMaximum(0), bounds.getMaximum(1));
            assertEquals("axis order 1 min", latLon.getMaximum(1), bounds.getMaximum(0));
        }

        // GETMAP
        checkGetMap(wms, water_bodies, DefaultGeographicCRS.WGS84);
        checkGetMap(wms, water_bodies, CRS.decode("CRS:84"));
        checkGetMap(wms, water_bodies, CRS.decode("EPSG:4326"));
        checkGetMap(wms, water_bodies, CRS.decode("urn:x-ogc:def:crs:EPSG::4326"));

        // GETFEATURE INFO
        checkGetFeatureInfo(wms, water_bodies, DefaultGeographicCRS.WGS84);
        checkGetFeatureInfo(wms, water_bodies, CRS.decode("CRS:84"));
        checkGetFeatureInfo(wms, water_bodies, CRS.decode("EPSG:4326"));
        checkGetFeatureInfo(wms, water_bodies, CRS.decode("urn:x-ogc:def:crs:EPSG::4326"));
    }

    public void testImageSample111() throws Exception {
        WebMapServer wms111 = new WebMapServer(new URL(serverURL + "&VERSION=1.1.1"));
        WMSCapabilities caps = wms111.getCapabilities();
        assertEquals("1.1.1", caps.getVersion());

        Layer water_bodies = find("topp:tasmania_water_bodies", caps);
        assertNotNull("Img_Sample layer found", water_bodies);
        CRSEnvelope latLon = water_bodies.getLatLonBoundingBox();
        assertEquals(
                "LatLonBoundingBox axis 0 name",
                "Geodetic longitude",
                axisName(latLon.getCoordinateReferenceSystem(), 0));
        assertEquals(
                "LatLonBoundingBox axis 1 name",
                "Geodetic latitude",
                axisName(latLon.getCoordinateReferenceSystem(), 1));

        CRSEnvelope bounds = water_bodies.getBoundingBoxes().get("EPSG:4326");
        CoordinateReferenceSystem boundsCRS = bounds.getCoordinateReferenceSystem();
        assertEquals("EPSG:4326", AxisOrder.EAST_NORTH, CRS.getAxisOrder(boundsCRS));
        ;

        assertEquals("axis order 0 min", latLon.getMinimum(0), bounds.getMinimum(0));
        assertEquals("axis order 1 min", latLon.getMinimum(1), bounds.getMinimum(1));
        assertEquals("axis order 1 max", latLon.getMaximum(0), bounds.getMaximum(0));
        assertEquals("axis order 1 min", latLon.getMaximum(1), bounds.getMaximum(1));

        // GETMAP
        checkGetMap(wms111, water_bodies, DefaultGeographicCRS.WGS84);
        checkGetMap(wms111, water_bodies, CRS.decode("CRS:84"));
        checkGetMap(wms111, water_bodies, CRS.decode("EPSG:4326"));
        checkGetMap(wms111, water_bodies, CRS.decode("urn:x-ogc:def:crs:EPSG::4326"));

        // GETFEATURE INFO
        checkGetFeatureInfo(wms111, water_bodies, DefaultGeographicCRS.WGS84);
        checkGetFeatureInfo(wms111, water_bodies, CRS.decode("CRS:84"));
        checkGetFeatureInfo(wms111, water_bodies, CRS.decode("EPSG:4326"));
        checkGetFeatureInfo(wms111, water_bodies, CRS.decode("urn:x-ogc:def:crs:EPSG::4326"));
    }

    private String format(OperationType operationType, String search) {
        for (String format : operationType.getFormats()) {
            if (format.contains(search)) {
                return format;
            }
        }
        return null; // not found
    }

    /**
     * Check GetMap request functionality in the provided CRS.
     *
     * <p>Attempt is made to request the entire image.
     */
    private void checkGetMap(WebMapServer wms, Layer layer, CoordinateReferenceSystem crs)
            throws Exception {

        layer.clearCache();
        CRSEnvelope latLon = layer.getLatLonBoundingBox();
        GeneralEnvelope envelope = wms.getEnvelope(layer, crs);
        assertFalse(envelope.isEmpty() || envelope.isNull() || envelope.isInfinite());
        assertNotNull("Envelope " + CRS.toSRS(crs), envelope);

        GetMapRequest getMap = wms.createGetMapRequest();
        OperationType operationType = wms.getCapabilities().getRequest().getGetMap();

        getMap.addLayer(layer);
        String version = wms.getCapabilities().getVersion();

        getMap.setBBox(envelope);

        Properties properties = getMap.getProperties();
        String srs = null;
        if (properties.containsKey("SRS")) {
            srs = properties.getProperty("SRS");
        } else if (properties.containsKey("CRS")) {
            srs = properties.getProperty("CRS");
        }
        assertNotNull("setBBox supplied SRS information", srs);
        String expectedSRS = CRS.toSRS(envelope.getCoordinateReferenceSystem());
        assertEquals("srs matches CRS.toSRS", expectedSRS, srs);

        assertTrue("cite authority:" + srs, srs.contains("CRS") || srs.contains("EPSG"));

        // getMap.setSRS( srs );

        String format = format(operationType, "jpeg");
        getMap.setFormat(format);
        getMap.setDimensions(500, 500);

        URL url = getMap.getFinalURL();
        GetMapResponse response = wms.issueRequest(getMap);
        assertEquals("image/jpeg", response.getContentType());

        InputStream stream = response.getInputStream();
        BufferedImage image = ImageIO.read(stream);
        assertNotNull("jpeg", image);
        assertEquals(500, image.getWidth());
        assertEquals(500, image.getHeight());

        int rgb = image.getRGB(70, 420);
        Color sample = new Color(rgb);
        boolean forceXY = Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        String context = "srs=" + srs + " forceXY=" + forceXY + " Version=" + version;
        if (Color.WHITE.equals(sample)) {
            // System.out.println("FAIL: " + context + ": GetMap BBOX=" + envelope);
            // System.out.println("--> " + url);
            fail(context + ": GetMap BBOX=" + envelope);
        } else {
            // System.out.println("PASS: "+ context+": GetMap BBOX=" + bbox);
        }
    }

    /**
     * Check GetMap request functionality in the provided CRS.
     *
     * <p>Attempt is made to request the entire image.
     */
    private void checkGetFeatureInfo(WebMapServer wms, Layer layer, CoordinateReferenceSystem crs)
            throws Exception {

        layer.clearCache();
        CRSEnvelope latLon = layer.getLatLonBoundingBox();
        GeneralEnvelope envelope = wms.getEnvelope(layer, crs);
        assertFalse(envelope.isEmpty() || envelope.isNull() || envelope.isInfinite());
        assertNotNull("Envelope " + CRS.toSRS(crs), envelope);

        GetMapRequest getMap = wms.createGetMapRequest();
        OperationType operationType = wms.getCapabilities().getRequest().getGetMap();

        getMap.addLayer(layer);
        String version = wms.getCapabilities().getVersion();
        String srs = CRS.toSRS(envelope.getCoordinateReferenceSystem());
        getMap.setBBox(envelope);
        String format = format(operationType, "jpeg");
        getMap.setFormat(format);
        getMap.setDimensions(500, 500);
        URL url = getMap.getFinalURL();

        GetFeatureInfoRequest getFeatureInfo = wms.createGetFeatureInfoRequest(getMap);
        getFeatureInfo.setInfoFormat("text/html");
        getFeatureInfo.setQueryLayers(Collections.singleton(layer));
        getFeatureInfo.setQueryPoint(75, 100);
        URL url2 = getFeatureInfo.getFinalURL();

        GetFeatureInfoResponse response = wms.issueRequest(getFeatureInfo);
        assertEquals("text/html", response.getContentType());
        InputStream stream = response.getInputStream();
        StringBuilderWriter writer = new StringBuilderWriter();
        IOUtils.copy(stream, writer, "UTF-8");

        String info = writer.toString();
        assertTrue("response available", !info.isEmpty());
        assertTrue("html", info.contains("<html") || info.contains("<HTML"));
        boolean forceXY = Boolean.getBoolean(GeoTools.FORCE_LONGITUDE_FIRST_AXIS_ORDER);
        String context = "srs=" + srs + " forceXY=" + forceXY + " Version=" + version;
        if (!info.contains("tasmania_water_bodies.3")) {
            // System.out.println("FAIL: " + context + ": GetFeatureInfo BBOX=" + envelope);
            // System.out.println("GETMAP         --> " + url);
            // System.out.println("GETFEATUREINFO --> " + url2);
            fail(context + ": GetFeatureInfo BBOX=" + envelope);
        }
    }

    public void testGetStyle() throws Exception {

        String baseUrl = LocalGeoServerOnlineTest.LOCAL_GEOSERVER;
        String layers = LocalGeoServerOnlineTest.LOCAL_LAYERS;

        URL url = new URL(baseUrl);

        GetStylesResponse wmsResponse = null;
        GetStylesRequest wmsRequest = null;
        StyleFactory styleFactory = new StyleFactoryImpl();

        WebMapServer server =
                new WebMapServer(url) {
                    // GetStyle is only implemented in WMS 1.1.1
                    protected void setupSpecifications() {
                        specs = new Specification[1];
                        specs[0] = new WMS1_1_1();
                    }
                };

        wmsRequest = server.createGetStylesRequest();
        wmsRequest.setLayers(layers);
        // Test URL
        String queryParamters = wmsRequest.getFinalURL().getQuery();
        Map parameters = new HashMap();
        String[] rawParameters = queryParamters.split("&");
        for (String param : rawParameters) {
            String[] keyValue = param.split("=");
            parameters.put(keyValue[0], keyValue[1]);
        }

        assertTrue(parameters.size() >= 4);
        assertEquals("WMS", parameters.get("SERVICE"));
        assertEquals("GetStyles", parameters.get("REQUEST"));
        assertEquals("1.1.1", parameters.get("VERSION"));
        assertEquals(layers, parameters.get("LAYERS"));

        wmsResponse = server.issueRequest(wmsRequest);

        // Set encoding of response from HTTP content-type header
        ContentType contentType = new ContentType(wmsResponse.getContentType());
        InputStreamReader stream;
        if (contentType.getParameter("charset") != null)
            stream =
                    new InputStreamReader(
                            wmsResponse.getInputStream(), contentType.getParameter("charset"));
        else stream = new InputStreamReader(wmsResponse.getInputStream());

        Style[] styles = (new SLDParser(styleFactory, stream)).readXML();

        assert styles.length > 0;

        SLDTransformer styleTransform = new SLDTransformer();
        StyledLayerDescriptorBuilder SLDBuilder = new StyledLayerDescriptorBuilder();

        NamedLayerBuilder namedLayerBuilder = SLDBuilder.namedLayer();
        namedLayerBuilder.name(layers);
        StyleBuilder styleBuilder = namedLayerBuilder.style();

        for (int i = 0; i < styles.length; i++) {
            styleBuilder.reset(styles[i]);
            styles[i] = styleBuilder.build();
        }

        NamedLayer namedLayer = namedLayerBuilder.build();

        for (Style style : styles) namedLayer.addStyle(style);

        StyledLayerDescriptor sld = (new StyledLayerDescriptorBuilder()).build();
        sld.addStyledLayer(namedLayer);
        String xml = styleTransform.transform(sld);
        assert xml.length() > 300;
    }
}
