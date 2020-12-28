/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2020, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.geotools.data.ows.AbstractHttpClient;
import org.geotools.data.ows.HTTPResponse;

public class TestHttpClient extends AbstractHttpClient {

    private HTTPResponse mockResponse;

    public URL targetUrl;

    public String postCallbackContentType;

    public long postCallbackContentLength = -1;

    public ByteArrayOutputStream postCallbackEncodedRequestBody;

    public TestHttpClient(HTTPResponse mockResponse) {
        this.mockResponse = mockResponse;
    }

    @Override
    public HTTPResponse get(final URL baseUrl) throws IOException {
        if (baseUrl.getProtocol().equals("file")) {
            return new TestHttpResponse(baseUrl, "text/xml");
        }
        this.targetUrl = baseUrl;
        return mockResponse;
    }

    @Override
    public HTTPResponse post(
            final URL url, final InputStream postContent, final String postContentType)
            throws IOException {
        this.targetUrl = url;
        this.postCallbackContentType = postContentType;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(postContent, out);
        this.postCallbackEncodedRequestBody = out;
        this.postCallbackContentLength = out.size();
        return mockResponse;
    }
}
