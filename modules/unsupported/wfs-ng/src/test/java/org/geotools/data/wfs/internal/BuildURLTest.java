/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.data.wfs.internal;

import static org.junit.Assert.*;

import java.util.HashMap;
import java.util.Map;
import org.junit.Test;

public class BuildURLTest {

    @Test
    public void testSimpleKvp() {

        String url = URIs.buildURL("http://www.google.com", "/q", buildMap("hello", "world"));

        assertEquals("http://www.google.com/q?hello=world", url);
    }

    @Test
    public void testKvpNoPath() {

        String url = URIs.buildURL("http://www.google.com/q", null, buildMap("hello", "world"));

        assertEquals("http://www.google.com/q?hello=world", url);
    }

    public static Map<String, String> buildMap(String... args) {
        Map<String, String> ret = new HashMap<String, String>();

        assertEquals("Builder must be given an even number of parameters", 0, args.length % 2);
        for (int i = 0; i < args.length; i += 2) {
            ret.put(args[i], args[i + 1]);
        }

        return ret;
    }
}
