/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2016, Open Source Geospatial Foundation (OSGeo)
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
import java.util.Properties;
import org.geotools.data.ows.Response;
import org.geotools.http.HTTPResponse;
import org.geotools.ows.ServiceException;
import org.geotools.ows.wms.request.AbstractGetMapRequest;
import org.geotools.ows.wms.request.GetMapRequest;
import org.junit.Assert;
import org.junit.Test;

public class AbstractGetMapRequestTest {

    @Test
    public void testGetFinalURL() throws Exception {
        URL badURL = new URL("http://test.com/map.php?LAYERS=Provincial Boundary");

        GetMapRequest request = new RequestTestHelp(badURL, null);

        request.addLayer("Provincial Boundary", "Two words");
        request.addLayer("Layer2", "");

        URL finalURL = request.getFinalURL();
        // System.out.println(finalURL);
        String processedURL = finalURL.toExternalForm();
        Assert.assertNotEquals(-1, processedURL.indexOf("LAYERS=Layer2,Provincial%20Boundary"));
        Assert.assertNotEquals(-1, processedURL.indexOf("STYLES=,Two%20words"));
        Assert.assertNotEquals(-1, processedURL.indexOf("SERVICE=WMS"));
    }

    private static class RequestTestHelp extends AbstractGetMapRequest {

        /** */
        public RequestTestHelp(URL onlineResource, Properties properties) {
            super(onlineResource, properties);
            // TODO Auto-generated constructor stub
        }

        /* (non-Javadoc)
         * @see AbstractGetMapRequest#initVersion()
         */
        @Override
        protected void initVersion() {
            // TODO Auto-generated method stub

        }

        @Override
        public Response createResponse(HTTPResponse httpResponse) throws ServiceException, IOException {
            // TODO Auto-generated method stub
            return null;
        }
    }
}
