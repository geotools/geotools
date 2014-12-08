package org.geotools.data.wfs.protocol.http;

import static org.junit.Assert.*;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

public class HttpUtilTest {

	@Test
	public void testCreateUri() throws MalformedURLException {
		Map<String, String> queryStringKvp = new HashMap<String, String>();
		queryStringKvp.put("service", "WFS");
		queryStringKvp.put("version", "1.1.0");
		queryStringKvp.put("request", "GetFeature");
		queryStringKvp.put("typeName", "topp:states");
		
		URL url1 = new URL("http://localhost:8080/geoserver/topp/ows");
		assertEquals(
				"http://localhost:8080/geoserver/topp/ows?request=GetFeature&service=WFS&typeName=topp%3Astates&version=1.1.0",
				HttpUtil.createUri(url1, queryStringKvp));
		
		// when the request url is read from the capabilities, there might be a "&amp;"
		// at the end of the url
		URL url2 = new URL("http://localhost:8080/map/mapserv?map=/opt/data/carto/world.www.map&amp;");
		assertEquals(
				"http://localhost:8080/map/mapserv?map=%2Fopt%2Fdata%2Fcarto%2Fworld.www.map&request=GetFeature&service=WFS&typeName=topp%3Astates&version=1.1.0",
				HttpUtil.createUri(url2, queryStringKvp));
		
		URL url3 = new URL("http://localhost:8080/map/mapserv?map=/opt/data/carto/world.www.map&test=1");
		assertEquals(
				"http://localhost:8080/map/mapserv?map=%2Fopt%2Fdata%2Fcarto%2Fworld.www.map&request=GetFeature&service=WFS&test=1&typeName=topp%3Astates&version=1.1.0",
				HttpUtil.createUri(url3, queryStringKvp));
	}

}
