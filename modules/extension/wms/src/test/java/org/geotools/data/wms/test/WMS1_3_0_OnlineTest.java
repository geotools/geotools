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
package org.geotools.data.wms.test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Properties;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.Specification;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMS1_3_0;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetFeatureInfoRequest;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetFeatureInfoResponse;
import org.xml.sax.SAXException;

/**
 * @author rgould
 *
 * @source $URL$
 */
public class WMS1_3_0_OnlineTest extends WMS1_1_1_OnlineTest{
    
    private URL server2;
    public WMS1_3_0_OnlineTest() throws Exception {
        this.spec = new WMS1_3_0();
        this.server = new URL("http://www2.demis.nl/mapserver/Request.asp?Service=WMS&Version=1.3.0&Request=GetCapabilities");
        
        //TODO this server has changed - need to update the three commented out tests below - preferably, find a new server
        this.server2 = new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&REQUEST=GetCapabilities&VERSION=1.3.0");
    }
    
    public void testGetVersion() {
        assertEquals(spec.getVersion(), "1.3.0");
    }
    
    protected void checkProperties(Properties properties) {
        assertEquals(properties.get("VERSION"), "1.3.0");
        assertEquals(properties.get("REQUEST"), "GetCapabilities");
        assertEquals(properties.get("SERVICE"), "WMS");
    }
    
    public void testCreateParser() throws Exception {
        try{
            WMSCapabilities capabilities = createCapabilities("1.3.0Capabilities.xml");
            
            assertNotNull(capabilities);
            
            assertEquals(capabilities.getVersion(), "1.3.0");
            assertEquals(capabilities.getService().getName(), "WMS");
            assertEquals(capabilities.getService().getTitle(), "World Map");
            assertEquals(capabilities.getService().get_abstract(), "None");
            assertEquals(capabilities.getService().getOnlineResource(), new URL("http://www2.demis.nl"));
            
            assertEquals(capabilities.getService().getLayerLimit(), 40);
            assertEquals(capabilities.getService().getMaxWidth(), 2000);
            assertEquals(capabilities.getService().getMaxHeight(), 2000);
            
            assertEquals(capabilities.getRequest().getGetCapabilities().getFormats().get(0), "text/xml");
            assertEquals(capabilities.getRequest().getGetCapabilities().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
            assertEquals(capabilities.getRequest().getGetCapabilities().getPost(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
            
            assertEquals(capabilities.getRequest().getGetMap().getFormats().size(), 5);
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(0), "image/gif");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(1), "image/png");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(2), "image/jpeg");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(3), "image/bmp");
            assertEquals(capabilities.getRequest().getGetMap().getFormats().get(4), "image/swf");
            assertEquals(capabilities.getRequest().getGetMap().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
            
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().size(), 4);
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(0), "text/xml");
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(1), "text/plain");
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(2), "text/html");
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getFormats().get(3), "text/swf");
            assertEquals(capabilities.getRequest().getGetFeatureInfo().getGet(), new URL("http://www2.demis.nl/wms/wms.asp?wms=WorldMap&"));
            
            Layer topLayer = (Layer) capabilities.getLayerList().get(0);
            assertNotNull(topLayer);
            assertNull(topLayer.getParent());
            assertFalse(topLayer.isQueryable());
            assertEquals(topLayer.getTitle(), "World Map");
            assertEquals(topLayer.getSrs().size(), 1);
            assertTrue(topLayer.getSrs().contains("CRS:84"));
            
            CRSEnvelope llbbox = topLayer.getLatLonBoundingBox();
            assertNotNull(llbbox);
            assertEquals(llbbox.getMinX(), -180, 0.0);
            assertEquals(llbbox.getMaxX(), 180, 0.0);
            assertEquals(llbbox.getMinY(), -90, 0.0);
            assertEquals(llbbox.getMaxY(), 90, 0.0);
            
            assertEquals(topLayer.getBoundingBoxes().size(), 1);
            
            CRSEnvelope bbox = (CRSEnvelope) topLayer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -184, 0.0);
            assertEquals(bbox.getMaxX(), 180, 0.0);
            assertEquals(bbox.getMinY(), -90.0000000017335, 0.0);
            assertEquals(bbox.getMaxY(), 90, 0.0);
            
            Layer layer = (Layer) capabilities.getLayerList().get(1);
            assertEquals(layer.getParent(), topLayer);
            assertTrue(layer.isQueryable());
            assertEquals(layer.getName(), "Bathymetry");
            assertEquals(layer.getTitle(), "Bathymetry");
            
            // Added test to verify inheritance, should be same as previous llbbox
            llbbox = topLayer.getLatLonBoundingBox();
            assertNotNull(llbbox);
            assertEquals(llbbox.getMinX(), -180, 0.0);
            assertEquals(llbbox.getMaxX(), 180, 0.0);
            assertEquals(llbbox.getMinY(), -90, 0.0);
            assertEquals(llbbox.getMaxY(), 90, 0.0);
            
            bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -180, 0.0);
            assertEquals(bbox.getMaxX(), 180, 0.0);
            assertEquals(bbox.getMinY(), -90, 0.0);
            assertEquals(bbox.getMaxY(), 90, 0.0);
            
            assertEquals(capabilities.getLayerList().size(), 21);
            
            layer = (Layer) capabilities.getLayerList().get(20);
            assertEquals(layer.getParent(), topLayer);
            assertTrue(layer.isQueryable());
            assertEquals(layer.getName(), "Ocean features");
            assertEquals(layer.getTitle(), "Ocean features");
            
            // Added test to verify inheritance, should be same as previous llbbox
            llbbox = topLayer.getLatLonBoundingBox();
            assertNotNull(llbbox);
            assertEquals(llbbox.getMinX(), -180, 0.0);
            assertEquals(llbbox.getMaxX(), 180, 0.0);
            assertEquals(llbbox.getMinY(), -90, 0.0);
            assertEquals(llbbox.getMaxY(), 90, 0.0);
            
            bbox = (CRSEnvelope) layer.getBoundingBoxes().get("CRS:84");
            assertNotNull(bbox);
            assertEquals(bbox.getEPSGCode(), "CRS:84");
            assertEquals(bbox.getMinX(), -180, 0.0);
            assertEquals(bbox.getMaxX(), 179.999420166016, 0.0);
            assertEquals(bbox.getMinY(), -62.9231796264648, 0.0);
            assertEquals(bbox.getMaxY(), 68.6906585693359, 0.0);
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }
    }
    
    
    
    public void testCreateGetMapRequest() throws Exception {
        WebMapServer wms = new WebMapServer(server2);
        GetMapRequest request = wms.createGetMapRequest();
        request.setFormat("image/jpeg");
        System.out.println(request.getFinalURL().toExternalForm());
        
        assertTrue(request.getFinalURL().toExternalForm().indexOf("image%2Fjpeg") >= 0);
    }
    
    public void testCreateGetFeatureInfoRequest() throws Exception {
        try{
            URL featureURL = new URL("http://demo.cubewerx.com/cipi12/cubeserv/cubeserv.cgi?service=wms&request=getcapabilities");
            WebMapServer wms = getCustomWMS(featureURL);
            WMSCapabilities caps = wms.getCapabilities();
            assertNotNull(caps);
            assertNotNull(caps.getRequest().getGetFeatureInfo());
            
            GetMapRequest getMapRequest = wms.createGetMapRequest();
            
            getMapRequest.setProperty(GetMapRequest.LAYERS, "ETOPO2:Foundation");
//        List simpleLayers = getMapRequest.getAvailableLayers();
//        Iterator iter = simpleLayers.iterator();
//        while (iter.hasNext()) {
//                SimpleLayer simpleLayer = (SimpleLayer) iter.next();
//                Object[] styles = simpleLayer.getValidStyles().toArray();
//                if (styles.length == 0) {
//                        simpleLayer.setStyle("");
//                        continue;
//                }
//                Random random = new Random();
//                int randomInt = random.nextInt(styles.length);
//                simpleLayer.setStyle((String) styles[randomInt]);
//        }
//        getMapRequest.setLayers(simpleLayers);
            
            getMapRequest.setSRS("EPSG:4326");
            getMapRequest.setDimensions("400", "400");
            getMapRequest.setFormat("image/png");
//        http://demo.cubewerx.com/cipi12/cubeserv/cubeserv.cgi?INFO_FORMAT=text/html&LAYERS=ETOPO2:Foundation&FORMAT=image/png&HEIGHT=400&J=200&REQUEST=GetFeatureInfo&I=200&BBOX=-34.12087,15.503481,1.8462441,35.6043956&WIDTH=400&STYLES=&SRS=EPSG:4326&QUERY_LAYERS=ETOPO2:Foundation&VERSION=1.3.0
            getMapRequest.setBBox("-34.12087,15.503481,1.8462441,35.6043956");
            URL url2 = getMapRequest.getFinalURL();
            
            GetFeatureInfoRequest request = wms.createGetFeatureInfoRequest(getMapRequest);
//        request.setQueryLayers(request.getQueryableLayers());
            request.setProperty(GetFeatureInfoRequest.QUERY_LAYERS, "ETOPO2:Foundation");
            request.setQueryPoint(200, 200);
            request.setInfoFormat("text/html");
            
            //System.out.println(request.getFinalURL());
            
//     TODO   Currently this server rtreturns code 400 !?
            GetFeatureInfoResponse response = (GetFeatureInfoResponse) wms.issueRequest(request);
            //System.out.println(response.getContentType());
            assertTrue( response.getContentType().indexOf("text/html") != -1 );
            BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
            String line;
            
            boolean textFound = false;
            while ((line = in.readLine()) != null) {
                //System.out.println(line);
                if (line.indexOf("CubeSERV Feature Query") != -1) {
                    textFound = true;
                    break;
                }
            }
            assertTrue(textFound);
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }
        
    }
    
    public void testCreateDescribeLayerRequest() throws Exception {
        /*try{
            
            WebMapServer wms = new CustomWMS(server2);
            
            DescribeLayerRequest request = wms.createDescribeLayerRequest();
            assertNotNull(request);
//        http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&REQUEST=DescribeLayer&SERVICE=wms&VERSION=1.3.0&LAYERS=BARRIERL_1M:Foundation,POLBNDP_1M:Foundation,DQLINE_UTIL_1M:Foundation
            request.setLayers("BARRIERL_1M:Foundation,POLBNDP_1M:Foundation,DQLINE_UTIL_1M:Foundation");
            System.out.println(request.getFinalURL());
            DescribeLayerResponse response = (DescribeLayerResponse) wms.issueRequest(request);
            assertNotNull(response);
            
            LayerDescription[] layerDescs = response.getLayerDescs();
            assertEquals(layerDescs.length, 3);
            
            assertEquals(layerDescs[0].getName(), "BARRIERL_1M:Foundation");
            assertEquals(layerDescs[1].getName(), "POLBNDP_1M:Foundation");
            assertEquals(layerDescs[2].getName(), "DQLINE_UTIL_1M:Foundation");
            
            assertEquals(layerDescs[0].getWfs(), new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&SERVICE=WFS&DATASTORE=Foundation&"));
            assertEquals(layerDescs[1].getWfs(), new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&SERVICE=WFS&DATASTORE=Foundation&"));
            assertEquals(layerDescs[2].getWfs(), new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&SERVICE=WFS&DATASTORE=Foundation&"));
            
            assertEquals(layerDescs[0].getQueries().length, 1);
            assertEquals(layerDescs[1].getQueries().length, 1);
            assertEquals(layerDescs[2].getQueries().length, 1);
            
            assertEquals(layerDescs[0].getQueries()[0], "BARRIERL_1M");
            assertEquals(layerDescs[1].getQueries()[0], "POLBNDP_1M");
            assertEquals(layerDescs[2].getQueries()[0], "DQLINE_UTIL_1M");
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }*/
    }
    
    public void testCreateGetLegendGraphicRequest() throws Exception {
        /*try{
            WebMapServer wms = new CustomWMS(server2);
            GetLegendGraphicRequest request = wms.createGetLegendGraphicRequest();
            
            assertNotNull(request);
            
            Layer[] layers = WMSUtils.getNamedLayers(wms.getCapabilities());
            SimpleLayer layer = null;
            for (int i = 0; i < layers.length; i++) {
                if (layers[i].getName().equals("BARRIERL_1M:Foundation")) {
                    layer = new SimpleLayer(layers[i].getName(), "");
                    break;
                }
            }
            
            assertNotNull(layer);
            
            layer.setStyle("");
            request.setLayer(layer);
            
            request.setFormat("image/gif");
            
            request.setWidth("50");
            request.setHeight("50");
            
            System.out.println(request.getFinalURL());
            
            GetLegendGraphicResponse response = (GetLegendGraphicResponse) wms.issueRequest(request);
            assertNotNull(response);
            
            assertEquals(response.getContentType(), "image/gif");
            
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertEquals(image.getHeight(), 50);
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }*/
    }
    
    public void testParamEncoding() throws Exception {
        //this request does not work because it is encoded properly
        //Let's make sure that this doesn't happen again.
//		http://demo.cubewerx.com/cipi12/cubeserv/cubeserv.cgi?LAYERS=BARRIERL_1M%3AFoundation%2CBNDTXT_1M%3AFoundation&
//		FORMAT=image%2Fpng&TRANSPARENT=TRUE&HEIGHT=296&REQUEST=GetMap&
//		BBOX=9.543194770812995%2C2.9407237508305797%2C119.99700164794902%2C59.50530305123241&
//		WIDTH=577&STYLES=%2C&SRS=EPSG%3A4269&VERSION=1.1.1
       /* try{
            WebMapServer wms = new CustomWMS(server2);
            GetMapRequest request = wms.createGetMapRequest();
            
            List layers = new ArrayList();
            layers.add(new SimpleLayer("BARRIERL_1M:Foundation", ""));
            layers.add(new SimpleLayer("BNDTXT_1M:Foundation", ""));
            request.setLayers(layers);
            request.setSRS("EPSG:4269");
            request.setDimensions("566", "296");
            request.setTransparent(true);
            request.setFormat("image/png");
            request.setBBox("9.543194770812995,2.9407237508305797,119.99700164794902,59.50530305123241");
            
            GetMapResponse response = wms.issueRequest(request);
            
            BufferedImage image = ImageIO.read(response.getInputStream());
            assertEquals(image.getHeight(), 296);
        } catch(java.net.ConnectException ce){
            if(ce.getMessage().indexOf("timed out")>0){
                System.err.println("Unable to test - timed out: "+ce);
            } else{
                throw(ce);
            }
        }*/
    }
    
    
    protected WebMapServer getCustomWMS( URL featureURL ) throws SAXException, URISyntaxException, IOException {
        return new CustomWMS(featureURL);
    }
    //forces use of 1.3.0 spec
    private class CustomWMS extends WebMapServer {
        
        /**
         * @param serverURL
         * @param wait
         * @throws SAXException
         * @throws URISyntaxException
         * @throws IOException
         */
        public CustomWMS( URL serverURL) throws SAXException, URISyntaxException, IOException {
            super(serverURL);
            // TODO Auto-generated constructor stub
        }
        
        protected void setupSpecifications() {
            specs = new Specification[1];
            specs[0] = new WMS1_3_0();
        }
    }
}
