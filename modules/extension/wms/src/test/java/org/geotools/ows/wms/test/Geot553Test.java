/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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

import java.io.IOException;
import java.net.URL;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;
import org.geotools.http.MockHttpResponse;
import org.geotools.ows.wms.Layer;
import org.geotools.ows.wms.WebMapServer;
import org.geotools.referencing.CRS;
import org.geotools.test.TestData;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.geometry.Envelope;

public class Geot553Test {

    @Test
    public void testGeot553() throws Exception {
        // prepare the responses
        MockHttpClient client =
                new MockHttpClient() {

                    @Override
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
        Layer layer = wms.getCapabilities().getLayer().getChildren()[2];

        Envelope env = wms.getEnvelope(layer, CRS.decode("EPSG:3005"));

        Assert.assertNotNull(env);
    }
}
