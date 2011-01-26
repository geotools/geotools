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

import java.awt.image.BufferedImage;
import java.net.URL;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.imageio.ImageIO;

import org.geotools.data.ows.Layer;
import org.geotools.data.ows.StyleImpl;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WMSUtils;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.geometry.GeneralEnvelope;
import org.geotools.ows.ServiceException;
import org.geotools.referencing.CRS;
import org.opengis.referencing.crs.CoordinateReferenceSystem;


/**
 * @author Richard Gould
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 * @source $URL$
 */
public class WebMapServerOnlineTest extends ServerTestCase {
    URL serverURL;
    URL brokenURL;
    private URL featureURL;

    /*
     * @see TestCase#setUp()
     */
    protected void setUp() throws Exception {
        super.setUp();
        // serverURL = new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&SERVICE=WMS&?VERSION=1.3.0&REQUEST=GetCapabilities");
        serverURL = new URL("http://terraservice.net/ogccapabilities.ashx?version=1.1.1&request=GetCapabilties");
        // serverURL = new URL("http://www.mapserv.nl/wms?mosm&request=GetCapabilities&service=WMS&VERSION=1.1.1");        
        featureURL = new URL(
                "http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
        
        brokenURL = new URL("http://afjklda.com");
    }

    /*
     * Class under test for void WebMapServer(URL)
     */
    public void testWebMapServerURL() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        assertNotNull(wms.getCapabilities());
    }

    public void testGetCapabilities() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        assertNotNull(wms.getCapabilities());
    }

    public void testIssueGetMapRequest() throws Exception {
        WebMapServer wms = new WebMapServer(serverURL);

        WMSCapabilities capabilities = wms.getCapabilities();

        GetMapRequest request = wms.createGetMapRequest();

        //request.setVersion("1.1.1");

        Layer[] layers = WMSUtils.getNamedLayers(capabilities);
        Iterator iter = Arrays.asList(layers).iterator();
        int count = -1;
        while (iter.hasNext()) {

            Layer layer = (Layer) iter.next();
            count++;
            if (count >= 5) { 
                break;
            }
                        
            List styles = layer.getStyles();

            if (styles.size() == 0) {
                request.addLayer(layer);
                continue;
            }

            Random random = new Random();
            int randomInt = random.nextInt(styles.size());
            
            request.addLayer(layer, (StyleImpl) styles.get(randomInt));
        }

        Set srss = WMSUtils.getSRSs(capabilities);
        request.setSRS((String) srss.iterator().next());
        request.setDimensions("400", "400");

        String format = "image/gif";
        List formats = wms.getCapabilities().getRequest().getGetMap().getFormats();
        if (!formats.contains("image/gif")) {
            format = (String) formats.get(0);
        } 
        request.setFormat(format);

        request.setBBox("366800,2170400,816000,2460400");

        //System.out.println(request.getFinalURL());
        GetMapResponse response = (GetMapResponse) wms.issueRequest(request);

        assertEquals(response.getContentType(), format);
        //System.out.println("Content Type: " + response.getContentType());

        BufferedImage image = ImageIO.read(response.getInputStream());
        assertEquals(image.getHeight(), 400);
    }

    public void testIssueGetFeatureInfoRequest() throws Exception {
/* TODO fix this

//        http://dev1.dmsolutions.ca/cgi-bin/mswms_gmap?LAYERS=DEMO&FORMAT=image/png&TRANSPARENT=TRUE&HEIGHT=213&REQUEST=GetMap&BBOX=-172.367,35.667300000000004,-11.562400000000014,83.8293&WIDTH=710&STYLES=&SRS=EPSG:4326&VERSION=1.1.1
        
        WebMapServer wms = new WebMapServer(featureURL);
        WMSCapabilities capabilities = wms.getCapabilities();
        
        assertNotNull(capabilities);
        
        GetMapRequest getMapRequest = wms.createGetMapRequest();

        List layers = Arrays.asList(WMSUtils.getNamedLayers(capabilities));
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
        request.setQueryLayers(WMSUtils.getQueryableLayers(capabilities));
        request.setQueryPoint(200, 200);
        request.setInfoFormat("text/html");
        
        System.out.println(request.getFinalURL());

        GetFeatureInfoResponse response = (GetFeatureInfoResponse) wms.issueRequest(request);
        System.out.println(response.getContentType());
        assertTrue( response.getContentType().indexOf("text/html") != -1 );
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getInputStream()));
        String line;

        boolean textFound = false;
        while ((line = in.readLine()) != null) {
            System.out.println(line);
            if (line.indexOf("Wood Buffalo National Park") != -1) {
                textFound = true;
            }
        }
        assertTrue(textFound);
*/
    }
        
    public void testGetEnvelope() throws Exception {
        WebMapServer wms = new WebMapServer(featureURL);
        
        WMSCapabilities caps = wms.getCapabilities();
        
        Layer layer = (Layer) caps.getLayerList().get(1);
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        
        GeneralEnvelope envelope = wms.getEnvelope(layer, crs);
        
//        minx="-172.367" miny="35.6673" maxx="-11.5624" maxy="83.8293" />
        assertEquals(envelope.getMinimum(0), -172.367, 0.0);
        assertEquals(envelope.getMinimum(1), 35.6673, 0.0);
        assertEquals(envelope.getMaximum(0), -11.5624, 0.0);
        assertEquals(envelope.getMaximum(1), 83.8293, 0.0);

        crs = CRS.decode("EPSG:42304");
        envelope = wms.getEnvelope(layer, crs);
        
//        minx="-2.2e+06" miny="-712631" maxx="3.0728e+06" maxy="3.84e+06" />
        assertEquals(envelope.getMinimum(0), -2.2e+06, 0.0);
        assertEquals(envelope.getMinimum(1), -712631, 0.0);
        assertEquals(envelope.getMaximum(0), 3.0728e+06, 0.0);
        assertEquals(envelope.getMaximum(1), 3.84e+06, 0.0);
        
        layer = (Layer) caps.getLayerList().get(2);
        crs = CRS.decode("EPSG:4326");
        
        envelope = wms.getEnvelope(layer, crs);
        
//        minx="-178.838" miny="31.8844" maxx="179.94" maxy="89.8254" />
        assertEquals(envelope.getMinimum(0), -178.838, 0.0);
        assertEquals(envelope.getMinimum(1), 31.8844, 0.0);
        assertEquals(envelope.getMaximum(0), 179.94, 0.0);
        assertEquals(envelope.getMaximum(1), 89.8254, 0.0);
    }
    
    public void testServiceExceptions  () throws Exception { 
    	WebMapServer wms = new WebMapServer(featureURL);
    	GetMapRequest request = wms.createGetMapRequest();
    	request.addLayer("NoLayer", "NoStyle");
    	try {
    		//System.out.println(request.getFinalURL());
    		GetMapResponse response = wms.issueRequest(request);
    		assertTrue(false);
    	} catch (ServiceException e) {
    		//e.printStackTrace();
    	}    	
    }
}
