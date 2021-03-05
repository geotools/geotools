/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ows.GetCapabilitiesRequest;
import org.geotools.data.ows.Specification;
import org.geotools.ows.wms.CRSEnvelope;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WMS1_0_0;
import org.geotools.ows.wms.WMSCapabilities;
import org.geotools.ows.wms.WMSSpecification;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.ows.wms.xml.WMSSchema;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.geotools.xml.DocumentFactory;
import org.geotools.xml.SchemaFactory;
import org.geotools.xml.handlers.DocumentHandler;
import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;
import org.xml.sax.SAXException;

public class WMS1_0_0_OnlineTest {
    protected static final Logger LOGGER = Logging.getLogger(WMS1_0_0_OnlineTest.class);

    protected URL server;
    protected WMSSpecification spec;

    public WMS1_0_0_OnlineTest() throws Exception {
        this.spec = new WMS1_0_0();
        this.server =
                new URL(
                        "http://www2.demis.nl/mapserver/Request.asp?wmtver=1.0.0&request=getcapabilities");
    }

    @Test
    public void testGetVersion() {
        Assert.assertEquals(spec.getVersion(), "1.0.0");
    }

    @Test
    public void testCreateGetCapabilitiesRequest() throws Exception {
        GetCapabilitiesRequest request = spec.createGetCapabilitiesRequest(server);

        Properties properties = new Properties();

        StringTokenizer tokenizer = new StringTokenizer(request.getFinalURL().getQuery(), "&");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] param = token.split("=");
            properties.setProperty(param[0].toUpperCase(), param[1]);
        }

        checkProperties(properties);
        WebMapServer wms = new WebMapServer(server);
        WMSCapabilities capabilities = wms.getCapabilities();

        Assert.assertNotNull(capabilities);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testCreateDescribeLayerRequest() throws Exception {
        spec.createDescribeLayerRequest(null);
        Assert.fail("Shouldn't be able to create DescribeLayer requests for version 1.0.0");
    }

    protected void checkProperties(Properties properties) {
        Assert.assertEquals(properties.getProperty("REQUEST"), "capabilities");
        Assert.assertEquals(properties.getProperty("WMTVER"), "1.0.0");
    }

    @Test
    public void testCreateParser() throws Exception {
        WMSCapabilities capabilities = createCapabilities("1.0.0Capabilities.xml");
        Assert.assertEquals(capabilities.getVersion(), "1.0.0");
        Assert.assertEquals(capabilities.getService().getName(), "GetMap");
        Assert.assertEquals(capabilities.getService().getTitle(), "World Map");

        for (int i = 0; i < capabilities.getService().getKeywordList().length; i++) {
            Assert.assertEquals(
                    capabilities.getService().getKeywordList()[i],
                    "OpenGIS WMS Web Map Server".split(" ")[i]);
        }

        Assert.assertEquals(
                capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));
        Assert.assertEquals(
                capabilities.getRequest().getGetCapabilities().getFormats().get(0),
                "application/vnd.ogc.wms_xml");
        Assert.assertEquals(
                capabilities.getRequest().getGetFeatureInfo().getGet(),
                new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
        Assert.assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 4);

        Assert.assertEquals(capabilities.getLayerList().size(), 21);

        Layer[] layers =
                capabilities.getLayerList().toArray(new Layer[capabilities.getLayerList().size()]);
        Assert.assertEquals(layers[0].getTitle(), "World Map");
        Assert.assertNull(layers[0].getParent());
        Assert.assertTrue(layers[0].getSrs().contains("EPSG:4326")); //  case should not matter
        Assert.assertTrue(layers[0].getSrs().contains("EPSG:4327"));
        Assert.assertEquals(layers[1].getTitle(), "Bathymetry");
        Assert.assertEquals(layers[1].getName(), "Bathymetry");
        Assert.assertEquals(layers[20].getTitle(), "Ocean features");
        Assert.assertEquals(layers[20].getName(), "Ocean features");
        Assert.assertEquals(layers[0].getBoundingBoxes().size(), 1);

        CRSEnvelope bbox = layers[1].getBoundingBoxes().get("EPSG:4326");
        Assert.assertNotNull(bbox);
    }

    @Test
    public void testCreateGetMapRequest() throws Exception {
        try {
            CustomWMS wms = new CustomWMS(server);
            GetMapRequest request = wms.createGetMapRequest();
            request.setFormat("image/jpeg");
            // System.out.println(request.getFinalURL().toExternalForm());

            Assert.assertTrue(request.getFinalURL().toExternalForm().indexOf("jpeg") >= 0);
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                LOGGER.warning("Unable to test - timed out: " + ce);
            } else {
                throw (ce);
            }
        }
    }

    @Test
    @Ignore
    public void testCreateGetFeatureInfoRequest() throws Exception {
        /* TODO FIX THIS
        try{
            URL featureURL = new URL("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
            WebMapServer wms = getCustomWMS(featureURL);
            WMSCapabilities caps = wms.getCapabilities();
            assertNotNull(caps);
            assertNotNull(caps.getRequest().getGetFeatureInfo());

            GetMapRequest getMapRequest = wms.createGetMapRequest();

            List layers = Arrays.asList(WMSUtils.getNamedLayers(caps));
            List simpleLayers = new ArrayList();
            Iterator iter = layers.iterator();
            while (iter.hasNext()) {
                Layer layer = (Layer) iter.next();
                SimpleLayer sLayer = new SimpleLayer(layer.getName(), "");
                simpleLayers.add(sLayer);
                List styles = layer.getStyles();
                if (styles.size() == 0) {
                    sLayer.setStyle("");
                    continue;
                }
                Random random = new Random();
                int randomInt = random.nextInt(styles.size());
                sLayer.setStyle((String) styles.get(randomInt));
            }
            getMapRequest.setLayers(simpleLayers);

            getMapRequest.setSRS("EPSG:4326");
            getMapRequest.setDimensions("400", "400");
            getMapRequest.setFormat("image/png");

            getMapRequest.setBBox("-114.01268,59.4596930,-113.26043,60.0835794");
            URL url2 = getMapRequest.getFinalURL();

            GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getMapRequest);
            request.setQueryLayers(WMSUtils.getQueryableLayers(caps));
            request.setQueryPoint(200, 200);
            request.setInfoFormat(caps.getRequest().getGetFeatureInfo().getFormatStrings()[0]);

            // System.out.println(request.getFinalURL());

            GetFeatureInfoResponse response = (GetFeatureInfoResponse) wms.issueRequest(request);
            // System.out.println(response.getContentType());
            assertTrue( response.getContentType().indexOf("text/plain") != -1 );
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
            String line;

            boolean textFound = false;
            while ((line = in.readLine()) != null) {
                // System.out.println(line);
                if (line.indexOf("Wood Buffalo National Park") != -1) {
                    textFound = true;
                }
            }
            assertTrue(textFound);
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                // System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }
        */
    }

    /** */
    protected WebMapServer getCustomWMS(URL featureURL)
            throws SAXException, URISyntaxException, IOException {
        return new CustomWMS(featureURL);
    }

    protected WMSCapabilities createCapabilities(String capFile) throws Exception {
        try {
            File getCaps = TestData.file(this, capFile);
            URL getCapsURL = getCaps.toURI().toURL();
            Map<String, Object> hints = new HashMap<>();
            hints.put(DocumentHandler.DEFAULT_NAMESPACE_HINT_KEY, WMSSchema.getInstance());
            Object object =
                    DocumentFactory.getInstance(getCapsURL.openStream(), hints, Level.WARNING);

            SchemaFactory.getInstance(WMSSchema.NAMESPACE);

            Assert.assertTrue("Capabilities failed to parse", object instanceof WMSCapabilities);

            WMSCapabilities capabilities = (WMSCapabilities) object;
            return capabilities;
        } catch (java.net.ConnectException ce) {
            if (ce.getMessage().indexOf("timed out") > 0) {
                // System.err.println("Unable to test - timed out: " + ce);
                return null;
            } else {
                throw (ce);
            }
        }
    }

    // forces use of 1.0.0 spec
    private class CustomWMS extends WebMapServer {

        /** */
        public CustomWMS(URL serverURL) throws SAXException, URISyntaxException, IOException {
            super(serverURL);
            // TODO Auto-generated constructor stub
        }

        @Override
        protected void setupSpecifications() {
            specs = new Specification[1];
            specs[0] = new WMS1_0_0();
        }
    }
}
