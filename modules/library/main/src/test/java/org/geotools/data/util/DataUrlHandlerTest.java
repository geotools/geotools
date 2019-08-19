/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import junit.framework.TestCase;

/** Test the handling/decoding of data uris. */
public class DataUrlHandlerTest extends TestCase {

    public void testDataUriDecoding() throws Exception {
        URL url = new URL(null, "data:,YQo=", new DataUrlHandler());
        InputStreamReader in = new InputStreamReader(url.openStream());
        BufferedReader reader = new BufferedReader(in);
        String data = reader.readLine();
        assertEquals("a", data);
    }
}
