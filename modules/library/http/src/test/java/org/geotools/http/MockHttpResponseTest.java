/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2022, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import static org.junit.Assert.assertArrayEquals;

import java.io.IOException;
import java.io.InputStream;
import org.junit.Test;

public class MockHttpResponseTest {

    private static final byte[] THE_RESPONSE = {1, 2, 3};

    @Test
    public void testReplay() throws IOException {
        byte[] payload = new byte[THE_RESPONSE.length];
        HTTPResponse response = new MockHttpResponse(THE_RESPONSE, "application/octet-stream");
        readAndCompare(payload, response);
        readAndCompare(payload, response);
    }

    private void readAndCompare(byte[] payload, HTTPResponse response) throws IOException {
        try (InputStream is = response.getResponseStream()) {
            is.read(payload);
            assertArrayEquals(THE_RESPONSE, payload);
        }
    }
}
