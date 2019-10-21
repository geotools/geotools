/*
 * GeoTools - The Open Source Java GIS Toolkit http://geotools.org
 *
 * (C) 2019, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or modify it under the terms of the GNU Lesser General
 * Public License as published by the Free Software Foundation; version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the implied
 * warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Lesser General Public License for more
 * details.
 */

package org.geotools.data.wfs.internal;

import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.WFSConfig.PreferredHttpMethod;
import org.geotools.ows.ServiceException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

/** @author Matthias Schulze - Landesamt für Digitalisierung, Breitband und Vermessung */
public class WFSRequestTest {

    @Before
    public void setUp() throws Exception {
        Loggers.RESPONSES.setLevel(Level.FINEST);
    }

    /** Test method for {@link org.geotools.data.wfs.internal.WFSRequest#getFinalURL()}. */
    @Test
    public void testGetFinalURLPost() throws IOException, ServiceException {
        WFSConfig config = new WFSConfig();
        config.preferredMethod = PreferredHttpMethod.HTTP_POST;
        WFSClient client = newClient("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", config);
        // WFSClient client = newClient("XtraServer/GetCapabilities.xml", config);
        WFSRequest request = client.createDescribeFeatureTypeRequest();
        URL url = request.getFinalURL();
        String query = url.getQuery();
        if (query != null && query.contains("=")) {
            Assert.fail("POST-Request should not contain Query String in URL");
        }
    }

    /** Test method for {@link org.geotools.data.wfs.internal.WFSRequest#getFinalURL()}. */
    @Test
    @Ignore
    public void testGetFinalURLGet() throws IOException, ServiceException {
        WFSConfig config = new WFSConfig();
        config.preferredMethod = PreferredHttpMethod.HTTP_GET;
        WFSClient client = newClient("GeoServer_2.2.x/1.0.0/GetCapabilities.xml", config);
        WFSRequest request = client.createDescribeFeatureTypeRequest();
        URL url = request.getFinalURL();
        String query = url.getQuery();
        if (query == null || !query.contains("=")) {
            Assert.fail("GET-Request should contain Query String in URL");
        }
    }

    private WFSClient newClient(String resource, WFSConfig config)
            throws IOException, ServiceException {
        URL capabilitiesURL = WFSTestData.url(resource);
        HTTPClient httpClient = new SimpleHttpClient();

        WFSClient client = new WFSClient(capabilitiesURL, httpClient, config);
        return client;
    }
}
