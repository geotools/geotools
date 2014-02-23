package org.geotools.data.wfs.v1_1_0;

import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.data.simple.SimpleFeatureCollection;
import org.geotools.data.simple.SimpleFeatureSource;
import org.geotools.data.wfs.MockHttpClient;
import org.geotools.data.wfs.MockHttpResponse;
import org.geotools.data.wfs.WFSDataStore;
import org.geotools.test.TestData;
import org.junit.Test;




public class ArcGISFeaturesParserTest {
	private static final String layerName = new String("Projected_StPaul");
	private String typeName = "Test_SaintPaul_TestStPaul:Projected_StPaul";
	
	private WFSDataStore getWFSDataStore(HTTPClient httpClient) throws IOException {
        URL capabilitiesUrl = new URL("http://127.0.0.1:8888/cgi-bin/tinyows?service=WFS&version=1.1.0&REQUEST=GetCapabilities");        
        
        HTTPResponse httpResponse = httpClient.get(capabilitiesUrl);
        InputStream inputStream = httpResponse.getResponseStream();
        
        byte[] wfsCapabilitiesRawData = IOUtils.toByteArray(inputStream);
        InputStream capsIn = new ByteArrayInputStream(wfsCapabilitiesRawData);
        WFS_1_1_0_DataStore wfs = new WFS_1_1_0_DataStore(new WFS_1_1_0_Protocol(capsIn, httpClient, null, new ArcGISServerStrategy()) {
            @Override
            public URL getDescribeFeatureTypeURLGet(String typeName) {
                return TestData.getResource(this, "arcgisserver/DescribeFeatureType.xml");
            }            
        });
        wfs.setPreferPostOverGet(true);
        return wfs;
    }
    
    public SimpleFeatureCollection GetFeatures() throws Exception {
        WFSDataStore wfs = getWFSDataStore(new TinyArcGISServerMockHttpClient() {
            @Override
            public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
                String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
                if (!postContentType.isEmpty()) 
                {
                	return new MockHttpResponse(TestData.getResource(this, "arcgisserver/GetFeaturesResult.xml"), "text/xml");
                }
                else 
                	return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
            }            
        });
        
        SimpleFeatureSource source = wfs.getFeatureSource(typeName);
        SimpleFeatureCollection featureCollection = source.getFeatures();
        
        return featureCollection;
        
    }
	  
	
	@Test
	public void testParseArcGISServerWFSFeatures() throws Exception{
		
		
		//find the typeName we're looking for
		SimpleFeatureCollection featureCollection = GetFeatures();
		
		Boolean isNotEmpty = !featureCollection.isEmpty();
		assertTrue(isNotEmpty);
	}
	
	class TinyArcGISServerMockHttpClient extends MockHttpClient {
        @Override
        public HTTPResponse get(URL url) throws IOException {
            if (url.getQuery().contains("REQUEST=GetCapabilities")) {
                return new MockHttpResponse(TestData.getResource(this, "arcgisserver/GetCapabilitiesResult.xml"), "text/xml");
            } else {
                return super.get(url);
            }
        }
        
        @Override
        public HTTPResponse post(URL url, InputStream postContent, String postContentType) throws IOException {
            String request = new String(IOUtils.toByteArray(postContent), "UTF-8");
            if (!postContentType.isEmpty()) 
            {
            	return new MockHttpResponse(TestData.getResource(this, "arcgisserver/GetFeaturesResult.xml"), "text/xml");
            }
            else 
            {
                return super.post(url, new ByteArrayInputStream(request.getBytes("UTF-8")), postContentType);
            }
        }                
    }
	
}
