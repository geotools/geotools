/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2019, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.internal.v2_0;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;
import javax.xml.namespace.QName;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.SimpleHttpClient;
import org.geotools.data.wfs.WFSTestData;
import org.geotools.data.wfs.internal.WFSClient;
import org.geotools.data.wfs.internal.WFSConfig;
import org.geotools.data.wfs.internal.WFSRequest;
import org.geotools.ows.ServiceException;
import org.junit.Test;

public class Strategy2_0Test {

    private WFSClient newClient(String resource) throws IOException, ServiceException {
        URL capabilitiesURL = WFSTestData.url(resource);
        HTTPClient httpClient = new SimpleHttpClient();

        WFSClient client = new WFSClient(capabilitiesURL, httpClient, new WFSConfig());
        return client;
    }

    @Test
    public void testNamespaceParamKeyValue() throws ServiceException, IOException {
        WFSClient wfsClient = newClient("GeoServer_2.2.x/1.0.0/GetCapabilities.xml");
        WFSRequest wfsReq = wfsClient.createDescribeFeatureTypeRequest();
        wfsReq.setTypeName(new QName("http://www.openplans.org/topp", "states", "prefix"));
        String url = wfsReq.getStrategy().buildUrlGET(wfsReq).toString();
        assertTrue(
                URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
                        .contains("NAMESPACE=xmlns(prefix="));
        assertTrue(url.contains("TYPENAME"));
        WFSClient clientV2 = newClient("GeoServer_2.2.x/2.0.0/GetCapabilities.xml");
        WFSRequest wfsReqV2 = clientV2.createDescribeFeatureTypeRequest();
        wfsReqV2.setTypeName(new QName("http://www.openplans.org/topp", "states", "prefix"));
        String urlV2 = wfsReqV2.getStrategy().buildUrlGET(wfsReqV2).toString();
        assertTrue(
                URLDecoder.decode(urlV2, StandardCharsets.UTF_8.toString())
                        .contains("NAMESPACES=xmlns(prefix,"));
        assertTrue(urlV2.contains("TYPENAMES"));
    }
}
