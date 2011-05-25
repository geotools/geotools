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
package org.geotools.data.wms.test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.StringTokenizer;

import org.geotools.data.ows.AbstractGetCapabilitiesRequest;
import org.geotools.data.ows.Response;
import org.geotools.data.wms.response.WMSGetCapabilitiesResponse;
import org.geotools.ows.ServiceException;


/**
 *
 *
 * @source $URL$
 */
public class GetCapabilitiesRequestTest extends ServerTestCase {
    public void testGetCapabilitiesRequest() throws Exception {
        URL testURL = new URL(
                "http://office.refractions.net:4001/cgi-bin/mapserv?map=/opt/dra2/orthophotos/tiles.map&");
        AbstractGetCapabilitiesRequest request = new Request(testURL);
        URL finalURL = request.getFinalURL();

        int index = finalURL.toExternalForm().lastIndexOf("?");
        String urlWithoutQuery = null;
        urlWithoutQuery = finalURL.toExternalForm().substring(0, index);

        assertEquals(urlWithoutQuery,
            "http://office.refractions.net:4001/cgi-bin/mapserv");

        HashMap map = new HashMap();
        map.put("VERSION", "1.1.1");
        map.put("MAP", "/opt/dra2/orthophotos/tiles.map");
        map.put("REQUEST", "GetCapabilities");
        map.put("SERVICE", "WMS");

        StringTokenizer tokenizer = new StringTokenizer(finalURL.getQuery(), "&");

        while (tokenizer.hasMoreTokens()) {
            String token = tokenizer.nextToken();
            String[] param = token.split("=");

            assertEquals((String) map.get(param[0]), param[1]);
        }
    }

    protected class Request extends AbstractGetCapabilitiesRequest {
        /**
         * DOCUMENT ME!
         *
         * @param serverURL
         */
        public Request(URL serverURL) {
            super(serverURL);

            // TODO Auto-generated constructor stub
        }

        /* (non-Javadoc)
         * @see org.geotools.data.wms.request.AbstractGetCapabilitiesRequest#initVersion()
         */
        protected void initVersion() {
            setProperty("VERSION", "1.1.1");
        }

		protected void initService() {
			setProperty("SERVICE", "WMS");
		}

		public Response createResponse(String contentType, InputStream inputStream) throws ServiceException, IOException {
			return new WMSGetCapabilitiesResponse(contentType, inputStream);
		}
    }
}
