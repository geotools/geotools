package org.geotools.data.wfs.impl;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;

import org.geotools.data.wfs.internal.URIs;
import org.junit.Test;

public class BuildURLTest {

	@Test
	public void testSimpleKvp() {

		String url =  URIs.buildURL("http://www.google.com", "/q", buildMap("hello", "world"));
		
		assertEquals("http://www.google.com/q?hello=world", url);
	}


	@Test
	public void testKvpNoPath() {

		String url =  URIs.buildURL("http://www.google.com/q", null, buildMap("hello", "world"));
		
		assertEquals("http://www.google.com/q?hello=world", url);
	}
	
	public static Map<String, String> buildMap(String ... args) {
		Map<String, String> ret = new HashMap<String, String>();
		
		assertEquals("Builder must be given an even number of parameters", 0, args.length % 2);
		for (int i = 0; i < args.length; i += 2) {
			ret.put(args[i], args[i+1]);
		}
		
		return ret;
	}
	
}
