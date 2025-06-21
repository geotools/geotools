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
import java.util.HashMap;
import java.util.StringTokenizer;
import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.response.WMSGetCapabilitiesResponse;
import org.junit.Assert;
import org.junit.Test;

public class GetCapabilitiesRequestTest {

    @Test
    public void testGetCapabilitiesRequest() throws Exception {
        URL testURL =
                new URL("http://office.refractions.net:4001/cgi-bin/mapserv?map=/opt/dra2/orthophotos/tiles.map&");
        AbstractGetCapabilitiesRequest request = new Request(testURL);
        URL finalURL = request.getFinalURL();

        int index = finalURL.toExternalForm().lastIndexOf("?");
        String urlWithoutQuery = finalURL.toExternalForm().substring(0, index);

        Assert.assertEquals(urlWithoutQuery, "http://office.refractions.net:4001/cgi-bin/mapserv");

        HashMap<String, String> map = new HashMap<>();
        map.put("VERSION", "1.1.1");
        map.put("MAP", "/opt/dra2/orthophotos/tiles.map");
        map.put("REQUEST", "GetCapabilities");
        map.put("SERVICE", "WMS");

        StringTokenizer tokenizer = new StringTokenizer(finalURL.getQuery(), "&");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] param = token.split("=");
            if (param[0].equalsIgnoreCase("map")) {
                Assert.assertEquals(map.get(param[0].toUpperCase()), param[1]);
            } else {
                Assert.assertEquals(map.get(param[0]), param[1]);
            }
        }
    }

    protected static class Request extends AbstractGetCapabilitiesRequest {
        /** @param serverURL */
        public Request(URL serverURL) {
            super(serverURL);

            // TODO Auto-generated constructor stub
        }

        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initVersion()
         */
        @Override
        protected void initVersion() {
            setProperty(processKey("VERSION"), "1.1.1");
        }

        @Override
        protected void initService() {
            setProperty(processKey("SERVICE"), "WMS");
        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            return new WMSGetCapabilitiesResponse(httpResponse, hints);
        }
    }
}
