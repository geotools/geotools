/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v1_1;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;
import java.util.Collections;

import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.impl.TestHttpClient;
import org.geotools.data.wfs.impl.TestHttpResponse;
import org.geotools.data.wfs.impl.WFSContentDataStore;
import org.geotools.ows.ServiceException;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;

public class MapServerTest {

    private WFSContentDataStore getWFSDataStore(HTTPClient httpClient) throws IOException, ServiceException {
        URL capabilitiesUrl = new URL("http://127.0.0.1:8888/mapserver?service=WFS&version=1.1.0&REQUEST=GetCapabilities");        
        
        WFSContentDataStore wfs = new WFSContentDataStore(new WFSClient(capabilitiesUrl, httpClient, WFSConfig.fromParams(Collections.EMPTY_MAP)));
            
        return wfs;
    }
   
    @Test
    public void testGetFeatures() throws Exception {
        
        WFSContentDataStore wfs = getWFSDataStore(new TestHttpClient() {
            
            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("REQUEST=GetCapabilities")) {
                    return new TestHttpResponse(TestData.getResource(this, "MapServer/GetCapabilities_1_1_0.xml"), "text/xml");
                } else {
                    return new TestHttpResponse(TestData.getResource(this, "MapServer/GetFeature_GovernmentalUnitCE.xml"), "text/xml");                    
                }
                
            }

            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");     
                if (request.contains("<wfs:DescribeFeatureType"))
                {
                    return new TestHttpResponse(TestData.getResource(this, "MapServer/DescribeFeatureType_GovernmentalUnitCE.xsd"), "text/xml");
                } else {
                    return new TestHttpResponse(TestData.getResource(this, "MapServer/GetFeature_GovernmentalUnitCE.xml"), "text/xml");
                }
            }
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource("ms_GovernmentalUnitCE");
        SimpleFeatureCollection features = source.getFeatures();
        SimpleFeatureIterator reader = features.features();
        SimpleFeature sf = null;        
        try {
            if (reader.hasNext()) {
                sf = reader.next();
                assertNotNull(sf);
                assertTrue(sf.getAttribute("typeAbbreviation") instanceof String);
                assertTrue(sf.getAttribute("number") instanceof BigInteger);
                assertTrue(sf.getAttribute("doubleNumber") instanceof Double);
            }
            assertNotNull(sf);
        } finally {
            reader.close();
        }  
    } 
}
