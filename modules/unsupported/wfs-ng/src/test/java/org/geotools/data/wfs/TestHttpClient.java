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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import org.apache.commons.io.IOUtils;
import org.geotools.http.HTTPResponse;
import org.geotools.http.MockHttpClient;

/** HTTPClient that returns a mockResponse for get and post. */
public class TestHttpClient extends MockHttpClient {

    public URL targetUrl;

    /**
     * Keeps the targetUrl.
     *
     * @return mockResponse The response set in the constructor
     */
    @Override
    public HTTPResponse get(final URL baseUrl) throws IOException {
        if (baseUrl.getProtocol().equals("file")) {
            return new TestHttpResponse(baseUrl, "text/xml");
        }
        this.targetUrl = baseUrl;
        return super.get(baseUrl);
    }

    /**
     * Keeps the incoming postContent in public variables.
     *
     * @return mockResponse The response set in the contructor
     */
    @Override
    public HTTPResponse post(final URL url, final InputStream postContent, final String postContentType)
            throws IOException {

        this.targetUrl = url;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        IOUtils.copy(postContent, out);
        String strippedPostContent = out.toString().replaceAll("handle=\"(.*?)\"", "");

        return super.post(
                url, new ByteArrayInputStream(strippedPostContent.getBytes(StandardCharsets.UTF_8)), postContentType);
    }

    @Override
    public void expectPost(URL url, String postContent, String postContentType, HTTPResponse response) {
        String strippedPostContent = postContent.toString().replaceAll("handle=\"(.*?)\"", "");
        super.expectPost(url, strippedPostContent, postContentType, response);
    }
}
