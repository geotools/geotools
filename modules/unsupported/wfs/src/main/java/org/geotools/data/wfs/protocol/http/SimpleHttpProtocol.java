/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.data.wfs.protocol.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.zip.GZIPInputStream;

import org.apache.commons.codec.binary.Base64;

/**
 * An {@link HTTPProtocol} implementation that relies on plain {@link HttpURLConnection}
 * 
 * @author Gabriel Roldan (OpenGeo)
 * @since 2.6.x
 * @version $Id$
 *
 * @source $URL$
 */
@SuppressWarnings("nls")
public class SimpleHttpProtocol extends AbstractHttpProtocol {

    private static class SimpleHttpResponse implements HTTPResponse {

        private HttpURLConnection conn;

        private InputStream inputStream;

        public SimpleHttpResponse(HttpURLConnection conn) {
            this.conn = conn;
        }

        public String getContentType() {
            return conn.getContentType();
        }

        public String getResponseCharset() {
            // String contentType = getContentType();
            return null;
        }

        public String getResponseHeader(String headerName) {
            String headerField = conn.getHeaderField(headerName);
            return headerField;
        }

        public InputStream getResponseStream() throws IOException {
            if (this.inputStream == null) {
                InputStream responseStream = conn.getInputStream();
                responseStream = new BufferedInputStream(responseStream);
                final String contentEncoding = conn.getContentEncoding();
                if (contentEncoding != null && contentEncoding.toLowerCase().indexOf("gzip") != -1) {
                    responseStream = new GZIPInputStream(responseStream);
                }
                this.inputStream = responseStream;
            }
            return this.inputStream;
        }

        public String getTargetUrl() {
            return conn.getURL().toExternalForm();
        }

    }

    public HTTPResponse issueGet(URL baseUrl, Map<String, String> kvp) throws IOException {
        URL targetUrl = createUrl(baseUrl, kvp);
        HttpURLConnection conn = openConnection(targetUrl, HttpMethod.GET);
        HTTPResponse response = new SimpleHttpResponse(conn);
        return response;
    }

    public HTTPResponse issuePost(URL targetUrl, POSTCallBack callback) throws IOException {
        HttpURLConnection conn = openConnection(targetUrl, HttpMethod.POST);

        long contentLength = callback.getContentLength();
        conn.setRequestProperty("Content-Length", String.valueOf(contentLength));

        String bodyContentType = callback.getContentType();
        conn.setRequestProperty("Content-Type", bodyContentType);

        OutputStream bodyOut = conn.getOutputStream();
        callback.writeBody(bodyOut);

        HTTPResponse response = new SimpleHttpResponse(conn);
        return response;
    }

    private HttpURLConnection openConnection(URL targetUrl, HttpMethod method) throws IOException {
        HttpURLConnection conn;
        try {
            conn = (HttpURLConnection) targetUrl.openConnection();
        } catch (ClassCastException wrongUrl) {
            throw new IOException("HTTP connection required for " + targetUrl);
        }
        if (0 < getTimeoutMillis()) {
            conn.setConnectTimeout(getTimeoutMillis());
            conn.setReadTimeout(getTimeoutMillis());
        }
        // remember to set the Accept-Encoding header before setting the authentication credentials
        // or an IllegalStateException is thrown
        if (isTryGzip()) {
            conn.setRequestProperty("Accept-Encoding", "gzip");
        }
        if (method == HttpMethod.POST) {
            conn.setRequestMethod("POST");
            // conn.setRequestProperty("Content-type", "text/xml, application/xml");
            conn.setDoOutput(true);
        } else {
            conn.setRequestMethod("GET");
        }
        conn.setDoInput(true);

        if (authUsername != null && authPassword != null) {
            String userPassword = authUsername + ":" + authPassword;
            byte[] encodedUserPassword = userPassword.getBytes();

            String base64UserAndPasswd = new String(Base64.encodeBase64(encodedUserPassword));
            
            conn.setRequestProperty("Authorization", "Basic " + base64UserAndPasswd);
        }

        return conn;
    }
}
