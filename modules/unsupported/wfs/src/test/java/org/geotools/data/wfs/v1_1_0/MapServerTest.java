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
package org.geotools.data.wfs.v1_1_0;

import static org.geotools.data.wfs.v1_1_0.DataTestSupport.MAPSRV_GOVUNITCE;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureIterator;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.MockHttpClient;
import org.geotools.data.wfs.MockHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.test.TestData;
import org.junit.Test;
import org.opengis.feature.simple.SimpleFeature;

/**
 * @author "Mauro Bartolomeoli - mauro.bartolomeoli@geo-solutions.it"
 *
 */
public class MapServerTest {

    private WFSDataStore getWFSDataStore(HTTPClient httpClient) throws IOException {
        URL capabilitiesUrl = new URL("http://127.0.0.1:8888/mapserver?service=WFS&version=1.1.0&REQUEST=GetCapabilities");        
        
        HTTPResponse httpResponse = httpClient.get(capabilitiesUrl);
        InputStream inputStream = httpResponse.getResponseStream();
        
        byte[] wfsCapabilitiesRawData = IOUtils.toByteArray(inputStream);
        InputStream capsIn = new ByteArrayInputStream(wfsCapabilitiesRawData);
        WFS_1_1_0_DataStore wfs = new WFS_1_1_0_DataStore(new WFS_1_1_0_Protocol(capsIn, httpClient, null, new MapServerStrategy()) {
            
            @Override
            public URL getDescribeFeatureTypeURLGet(String typeName) {
                return TestData.getResource(this, "MapServer/DescribeFeatureType_GovernmentalUnitCE.xsd");
            }            
        });
        wfs.setPreferPostOverGet(true);
        return wfs;
    }

    @Test
    public void testGetFeatures() throws Exception {
        
        WFSDataStore wfs = getWFSDataStore(new MockHttpClient() {
            
            
            @Override
            public HTTPResponse get(URL url) throws IOException {
                if (url.getQuery().contains("REQUEST=GetCapabilities")) {
                    return new MockHttpResponse(TestData.getResource(this, "MapServer/GetCapabilities_1_1_0.xml"), "text/xml");
                } else {
                    return new MockHttpResponse(TestData.getResource(this, "MapServer/GetFeature_GovernmentalUnitCE.xml"), "text/xml");                    
                }
                
            }

            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                return new MockHttpResponse(TestData.getResource(this, "MapServer/GetFeature_GovernmentalUnitCE.xml"), "text/xml");
            }            
        });
        ((WFS_1_1_0_DataStore)wfs).setMappedURIs(new MapServerStrategy().getNamespaceURIMappings());
        
        SimpleFeatureSource source = wfs.getFeatureSource(MAPSRV_GOVUNITCE.FEATURETYPENAME);
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
