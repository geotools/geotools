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

import static org.junit.Assert.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Map;

import junit.framework.TestCase;

import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.ows.Layer;
import org.geotools.data.ows.MockHttpClient;
import org.geotools.data.ows.MockHttpResponse;
import org.geotools.data.wms.WebMapServer;
import org.geotools.geometry.GeneralDirectPosition;
import org.geotools.map.WMSCoverageReaderTest;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.opengis.geometry.DirectPosition;
import org.opengis.geometry.Envelope;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.MathTransform;


/**
 *
 *
 * @source $URL$
 */
public class Geot553Test extends TestCase {
    
    public void testGeot553 () throws Exception {
    	//-247941.17083210908,5334613.737657672,-194536.86526633866,5359024.191696413
    	double minx = -247941.17083210908;
    	double miny = 5334613.737657672;
    	double maxx = -194536.86526633866;
    	double maxy = 5359024.191696413;
    	
    	CoordinateReferenceSystem epsg26591 = CRS.decode("EPSG:26591");
    	CoordinateReferenceSystem epsg4326 = CRS.decode("EPSG:4326");
    	
        // prepare the responses
        MockHttpClient client = new MockHttpClient() {

            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("GetCapabilities")) {
                    URL caps = TestData.getResource(this, "geot553capabilities.xml");
                    return new MockHttpResponse(caps, "text/xml");
                } else {
                    throw new IllegalArgumentException(
                            "Don't know how to handle a get request over " + url.toExternalForm());
                }
            }

        };
        
        
        WebMapServer wms = new WebMapServer(new URL("http://test.org"), client);
        Layer layer = wms.getCapabilities().getLayer().getChildren()[2];
        
        Envelope env = wms.getEnvelope(layer, CRS.decode("EPSG:3005"));
        
        assertNotNull(env);
    }
}
