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
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.geotools.util.Converters;
import org.junit.Assert;
import org.junit.Test;

/** Test the handling/decoding of data uris. */
public class DataUrlHandlerTest {

    @Test
    public void testDataUriDecoding() throws Exception {
        URL url = new URL(null, "data:,YQo=", new DataUrlHandler());
        checkDataUrlContent(url);
    }

    private void checkDataUrlContent(URL url) throws IOException {
        try (InputStreamReader in = new InputStreamReader(url.openStream(), StandardCharsets.UTF_8);
                BufferedReader reader = new BufferedReader(in)) {
            String data = reader.readLine();
            Assert.assertEquals("a", data);
        }
    }

    @Test
    public void testDataUrlConverter() throws Exception {
        String datasUrl = "data:,YQo=";
        URL url = Converters.convert(datasUrl, URL.class);
        Assert.assertNotNull(url);
        checkDataUrlContent(url);
    }

    @Test
    public void testDataUrlConverterFail() throws Exception {
        String wrongUrl = "wkt://Polygon (1 1, 1 0, 0 3)";
        URL url = Converters.convert(wrongUrl, URL.class);
        Assert.assertNull(url);
    }
}
