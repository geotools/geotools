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
import java.io.IOException;
import java.io.PrintStream;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

import javax.imageio.ImageIO;

import junit.framework.TestCase;

import org.geotools.data.ows.CRSEnvelope;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.WMSCapabilities;
import org.geotools.data.wms.WebMapServer;
import org.geotools.data.wms.request.GetMapRequest;
import org.geotools.data.wms.response.GetMapResponse;
import org.geotools.ows.ServiceException;
import org.xml.sax.SAXException;

public class ServersTest extends TestCase {
    public void testServers() throws Exception{

    	List servers = new ArrayList();
/*
    	File serverFile = TestData.file(this, "servers.txt");
    	BufferedReader br = new BufferedReader(new FileReader(serverFile));
    	String line = null;
    	while ((line = br.readLine()) != null) {
    		try {
    			servers.add(new URL(line));
    		} catch (MalformedURLException e) {

    		}
    	}
    	*/
        //servers.add(new URL("http://office.refractions.net/~chodgson/googlemaps/googlewms.php?request=getcapabilities"));

//    	URL[] servers = new URL[50];
//		servers[0] = new URL("http://wms.jpl.nasa.gov/wms.cgi?VERSION=1.1.1&SERVICE=WMS&REQUEST=GetCapabilities");
//    	servers[1] = new URL("http://demo.cubewerx.com/demo/cubeserv/cubeserv.cgi?CONFIG=main&SERVICE=WMS&VERSION=1.1.1&REQUEST=GetCapabilities");
//    	servers[2] = new URL("http://www2.dmsolutions.ca/cgi-bin/mswms_gmap?VERSION=1.1.0&REQUEST=GetCapabilities");
//  This returning Gzip content
//    	servers[3] = new URL("http://wms.cits.rncan.gc.ca/cgi-bin/cubeserv.cgi?VERSION=1.1.0&REQUEST=GetCapabilities");
//    	servers[4] = new URL("http://terraservice.net/ogccapabilities.ashx?version=1.1.1&request=GetCapabilties");
//    	servers[5] = new URL("http://www2.demis.nl/mapserver/Request.asp?VERSION=1.3.0&SERVICE=WMS&REQUEST=GetCapabilities");
//THIS ONE OFFLINE
//        servers[6] = new URL("http://datamil.udel.edu/servlet/com.esri.wms.Esrimap?servicename=DE_census2k_sf1&VERSION=1.0.0&request=capabilities");
//    	servers[7] = new URL("http://www.lifemapper.org/Services/WMS/?Service=WMS&VERSION=1.1.1&request=getcapabilities");
//    	this server returns OGC for 1.3.0
//    	servers[8] = new URL("http://globe.digitalearth.gov/viz-bin/wmt.cgi?VERSION=1.1.0&Request=GetCapabilities");
//    	servers[9] = new URL("http://www.geographynetwork.ca/wmsconnector/com.esri.wsit.WMSServlet/Geobase_NRN_NewfoundlandAndLabrador_I_Detail?request=GetCapabilities");
//    	servers[10] = new URL("http://gisdata.usgs.net/servlet/com.esri.wms.Esrimap?REQUEST=GetCapabilities&VERSION=1.3.0&SERVICE=WMS");
//    	servers[11] = new URL("http://www.refractions.net:8080/geoserver/wms/?SERVICE=WMS&REQUEST=GetCapabilities&VERSION=1.3.0"); //$NON-NLS-1$
//1.0.0 freezes.
//    	servers[12] = new URL("http://mapserv2.esrin.esa.it/cubestor/cubeserv/cubeserv.cgi?VERSION=1.1.1&REQUEST=GetCapabilities&SERVICE=WMS");
//		servers[13] = new URL("http://mesonet.agron.iastate.edu/wms/comprad.php?request=getcapabilities");
//		servers[14] = new URL("http://redspider.us/ionicweb/wfs/METRODC?request=getcapabilities&service=WMS&version=1.1.1");
//		servers[15] = new URL("http://webservices.ionicsoft.com/ionicweb/wfs/BOSTON_ORA?service=WMS&request=GetCapabilities&vesrion=1.1.1");
//		servers[16] = new URL("http://oceanesip.jpl.nasa.gov/de.xml");
//		servers[17] = new URL("http://libcwms.gov.bc.ca/wmsconnector/com.esri.wsit.WMSServlet/ogc_layer_service?REQUEST=capabilities");
//		servers[18] = new URL("http://www.geographynetwork.com/servlet/com.esri.wms.Esrimap?ServiceName=GFW_Forest&VERSION=1.0.0&request=capabilities");
//		servers[19] = new URL("http://atlas.gc.ca/cgi-bin/atlaswms_en?VERSION=1.1.0&request=GetCapabilities");
//OFFLINE		servers[20] = new URL("http://office.refractions.net:4001/cgi-bin/mapserv?map=/opt/dra2/orthophotos/tiles.map&request=getcapabilities");
//		servers[21] = new URL("http://mesonet.agron.iastate.edu/wms/comprad.php?request=getcapabilities");
//    	servers[22] = new URL("http://wms.larioja.org/request.asp?request=getcapabilities");
//    	servers[23] = new URL("http://atlas.walis.wa.gov.au/servlet/com.esri.wms.Esrimap?VERSION=1.1.0&Request=getcapabilities");
//    	servers[24] = new URL("http://iceds.ge.ucl.ac.uk/cgi-bin/wms?map=wms.map&SERVICE=WMS&REQUEST=GetCapabilities");
//    	servers[25] = new URL("http://test.landmap.ac.uk/ecwp/ecw_wms.dll?request=GetCapabilities&service=wms");
//    	servers[26] = new URL("http://emandev.cciw.ca/cgi-bin/mapserver/mapserv.exe?REQUEST=GetCapabilities&MAP=C:/Inetpub/wwwroot/emanco/cgi-bin/mapserver/naturewatch.map&VERSION=1.1.1&SERVICE=WMS");

        int total = 0;
        int passedCount = 0;

    	for (int i = 0; i < servers.size(); i++) {
    		URL server = (URL) servers.get(i);
            total++;
            Random random = new Random();
            String dir = "tests";
            String filename = URLEncoder.encode(server.getHost()+random.nextInt(10000),"UTF-8");
//            File file = new File("C:\\"+dir+"\\"+filename+".txt");
//            file.createNewFile();
//            PrintStream out = new PrintStream(new FileOutputStream(file));
            PrintStream out = System.out;
            boolean passed = serverTest(out, server);
            out.flush();
            out.close();

            if (passed) {
                //System.out.println(server.toExternalForm() + " passed.");
                passedCount++;
//                file.delete();
            } else {
            	//System.out.println(server.toExternalForm() + " failed.");
            }
//    		    WebMapServer wms = new WebMapServer(servers[i]);
//    			assertNotNull("Missing Capabilities",wms.getCapabilities());
//    		    WMSCapabilities capabilities = wms.getCapabilities();
//    		    assertNotNull(capabilities.getRequest());
		}

//        System.out.println("Total tested: "+total);
//        System.out.println("Total passed: "+passedCount);
    }

    public boolean serverTest(PrintStream out, URL url) {
        WebMapServer wms = null;

        boolean passed = true;
        out.println("Beginning tests for server:");
        out.println(url);
        out.print("Parsing Capabilities...");
        try {
            wms = new WebMapServer(url);
            out.println("passed.");
        } catch (ServiceException e) {
            out.println("failed.");
            passed = false;
            while (e != null) {
                if (e.getLocator() != null && e.getLocator().length() != 0) {
                    out.println("ServiceException at "+e.getLocator()+": "+e.getMessage()+"("+e.getCode()+")");
                }
                out.println("ServiceException: "+e.getMessage()+"("+e.getCode()+")");
                e = e.getNext();
            }
            return passed;
        } catch (IOException e) {
            out.println("failed.");
            passed = false;
            out.println("IOException: "+e.getMessage());
            e.printStackTrace(out);
            return passed;
        } catch (SAXException e) {
            out.println("failed.");
            passed = false;
            out.println("SAXException: "+e.getMessage());
            e.printStackTrace(out);
            return passed;
        }

        WMSCapabilities caps = wms.getCapabilities();
        assertNotNull(caps);

        out.println("Validating layer LatLonBoundingBoxes...");
        Iterator iter = caps.getLayerList().iterator();
        while (iter.hasNext()) {
            Layer layer = (Layer) iter.next();
            if (layer.getLatLonBoundingBox() == null) {
                if (layer.getName() != null) {
                    out.println("WARNING: Layer '"+layer.getName()+"' contains no LatLonBoundingBox.");
                    passed = false;
                }
            }
        }

        Layer layer = null;
        out.print("Looking for a named layer...");
        iter = caps.getLayerList().iterator();
        while (iter.hasNext()) {
            Layer tempLayer = (Layer) iter.next();
            if (tempLayer.getName() != null) {
                layer = tempLayer;
                out.println("found one. Using layer '"+layer.getName()+"'");
                break;
            }
        }
        String format = null;
        if (layer == null) {
            out.println("server contains no named layers. Cannot perform GetMap requests on it");
            passed = false;
        } else {
            out.print("Checking for GetMap operation...");
            if (caps.getRequest().getGetMap() == null) {
                out.println("NOT FOUND. Will attempt a request using 'image/gif' anyway.");
                passed = false;
                format = "image/gif";
            } else {
                out.println("found.");
                out.print("Searching for a suitable format...");
                List formats = caps.getRequest().getGetMap().getFormats();
                if (formats.contains("image/png")) {
                    format = "image/png";
                    out.println("using 'image/png'.");
                }

                if (format == null && formats.contains("image/gif")) {
                    format = "image/gif";
                    out.println("using 'image/gif'.");
                }

                if (format == null && formats.contains("image/jpeg")) {
                    format = "image/jpeg";
                    out.println("using 'image/jpeg'.");
                }

                if (format == null) {
                    format = (String) formats.get(0);
                    out.println("server does not support GIF, PNG or JPEG. Using '"+format+"'");
                    passed = false;
                }
            }
        }


        out.print("Performing GetMap operation...");
        GetMapRequest request = wms.createGetMapRequest();
        
        request.addLayer(layer);
        CRSEnvelope bbox = layer.getLatLonBoundingBox();
        request.setBBox(bbox);
        request.setFormat(format);
        request.setSRS("EPSG:4326");
        request.setDimensions("100","100");

        try {
            GetMapResponse response = wms.issueRequest(request);
            out.println("received a response.");

            out.print("Checking returned format...");
            if (response.getContentType().indexOf(format) == -1) {
                out.println("server returned bad format. Expected "+format+", got "+response.getContentType()+".");
                passed = false;
            } else {
                out.println("passed.");
            }

            out.print("Checking dimensions...");
            BufferedImage image = ImageIO.read(response.getInputStream());
            if (image == null) {
            	out.println("returned a bad image. ContentType is "+response.getContentType());
            	passed = false;
            } else if (image.getWidth() != 100 || image.getHeight() != 100) {
                out.println("server returned bad dimensions. Expect 100, 100. Returned "+image.getWidth()+","+image.getHeight());
                passed = false;
            } else {
                out.println("passed.");
            }
        } catch (ServiceException e) {
            out.println("failed.");
            passed = false;
            while (e != null) {
                if (e.getLocator() != null && e.getLocator().length() != 0) {
                    out.println("ServiceException at "+e.getLocator()+": "+e.getMessage()+"("+e.getCode()+")");
                }
                out.println("ServiceException: "+e.getMessage()+"("+e.getCode()+")");
                e = e.getNext();
            }
        } catch (IOException e) {
            out.println("failed.");
            passed = false;
            out.println("IOException: "+e.getMessage());
            e.printStackTrace(out);
        } catch (SAXException e) {
            out.println("failed.");
            passed = false;
            out.println("SAXException: "+e.getMessage());
            e.printStackTrace(out);
        } finally {
            out.println(request.getFinalURL());
        }

//        if (caps.getRequest().getGetFeatureInfo() != null) {
//            out.println("");
//            out.println("Server supports GetFeatureInfo requests. Beginning tests.");
//
//            Layer qLayer = null;
//            out.print("Locating a queryable layer...");
//            Set qLayers = WMSUtils.getQueryableLayers(caps);
//            if (qLayers != null && qLayers.size() != 0) {
//                qLayer = (Layer) qLayers.iterator().next();
//                out.println("found layer '"+qLayer.getName()+"'.");
//            } else {
//                out.println("NOT FOUND");
//                passed = false;
//            }
//
//            if (qLayer != null) {
//                GetFeatureInfoRequest gfiRequest = wms.createGetFeatureInfoRequest(request);
//                gfiRequest.addQueryLayer(qLayer);
//                gfiRequest.setFeatureCount(5);
//                gfiRequest.setQueryPoint(50,50);
//
//                String gfiFormat = caps.getRequest().getGetFeatureInfo().getFormatStrings()[0];
//                out.println("Using "+gfiFormat+" as format during GetFeatureInfo request.");
//
//                gfiRequest.setInfoFormat(gfiFormat);
//
//                try {
//                    out.print("Performing GetFeatureInfo request...");
//                    GetFeatureInfoResponse response = wms.issueRequest(gfiRequest);
//                    out.println("response received.");
//
//                    out.print("Checking returned format...");
//                    if (response.getContentType().indexOf(gfiFormat) == -1) {
//                        out.println("server returned bad format. Expected "+gfiFormat+", got "+response.getContentType()+".");
//                        passed = false;
//                    } else {
//                        out.println("passed.");
//                    }
//                } catch (ServiceException e) {
//                    out.println("failed.");
//                    passed = false;
//                    while (e != null) {
//                        if (e.getLocator() != null && e.getLocator().length() != 0) {
//                            out.println("ServiceException at "+e.getLocator()+": "+e.getMessage()+"("+e.getCode()+")");
//                        }
//                        out.println("ServiceException: "+e.getMessage()+"("+e.getCode()+")");
//                        e = e.getNext();
//                    }
//                } catch (IOException e) {
//                    out.println("failed.");
//                    passed = false;
//                    out.println("IOException: "+e.getMessage());
//                    e.printStackTrace(out);
//                } catch (SAXException e) {
//                    out.println("failed.");
//                    passed = false;
//                    out.println("SAXException: "+e.getMessage());
//                    e.printStackTrace(out);
//                } finally {
//                    out.println(gfiRequest.getFinalURL());
//                }
//            }
//        }
        return passed;
    }
}
