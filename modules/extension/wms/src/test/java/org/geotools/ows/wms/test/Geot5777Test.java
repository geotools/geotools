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
package org.geotools.ows.wms.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.geotools.data.ows.HTTPResponse;
import org.geotools.ows.MockHttpClient;
import org.geotools.ows.MockHttpResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.ows.wms.request.GetMapRequest;
import org.geotools.test.TestData;
import org.junit.Before;
import org.junit.Test;

/** @author ian */
public class Geot5777Test {

    /** @throws java.lang.Exception */
    @Before
    public void setUp() throws Exception {}

    @Test
    public void test() throws ServiceException, MalformedURLException, IOException {
        // prepare the responses
        MockHttpClient client =
                new MockHttpClient() {

                    public HTTPResponse get(URL url) throws IOException {
                        if (url.getQuery().contains("GetCapabilities")) {
                            URL caps = TestData.getResource(this, "geot553capabilities.xml");
                            return new MockHttpResponse(caps, "text/xml");
                        } else {
                            throw new IllegalArgumentException(
                                    "Don't know how to handle a get request over "
                                            + url.toExternalForm());
                        }
                    }
                };

        WebMapServer wms = new WebMapServer(new URL("http://test.org"), client);
        GetMapRequest request = wms.createGetMapRequest();
        request.setFormat("image/png");
        request.setDimensions("600", "600");
        request.setSRS("EPSG:4326");
        request.setBBox("-180, -85.0511287798, 180, 85.011287798");
        assertFalse(request.getFinalURL().toString().contains(" "));
    }
}
