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
package org.geotools.data.ws.protocol.http;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;

import org.geotools.util.logging.Logging;

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
public class SimpleHttpProtocol implements HTTPProtocol {

    private static final Logger LOGGER = Logging.getLogger("org.geotools.data.ws.protocol.http");

    private static class SimpleHttpResponse implements HTTPResponse {

        private HttpURLConnection conn;

        private InputStream inputStream;

        public SimpleHttpResponse(HttpURLConnection conn) {
            this.conn = conn;
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
    }
    
    private boolean tryGzip;

   protected int timeoutMillis = -1;

    public boolean isTryGzip() {
        return this.tryGzip;
    }

    public void setTryGzip(boolean tryGzip) {
        this.tryGzip = tryGzip;
    }


    public int getTimeoutMillis() {
        return this.timeoutMillis;
    }

    public void setTimeoutMillis(int milliseconds) {
        this.timeoutMillis = milliseconds;
    }

    public HTTPResponse issuePost(URL targetUrl, POSTCallBack callback) throws IOException {
        HttpURLConnection conn = openConnection(targetUrl); 

        long contentLength = callback.getContentLength();
        conn.setRequestProperty("Content-Length", String.valueOf(contentLength));

        String bodyContentType = callback.getContentType();
        conn.setRequestProperty("Content-Type", bodyContentType);
        
        conn.setRequestProperty("SOAPAction", targetUrl.toString());
            
        OutputStream bodyOut = conn.getOutputStream();
        callback.writeBody(bodyOut);
        
        if(LOGGER.isLoggable(Level.WARNING)) {
            LOGGER.log(Level.WARNING, "Request to webservice backend is:\n" + bodyOut);
        }
        
        HTTPResponse response = new SimpleHttpResponse(conn);
        return response;
    }

    private HttpURLConnection openConnection(URL targetUrl) throws IOException {
        HttpURLConnection conn = (HttpURLConnection) targetUrl.openConnection();
        if (0 < getTimeoutMillis()) {
            conn.setConnectTimeout(getTimeoutMillis());
            conn.setReadTimeout(getTimeoutMillis());
        }
        if (isTryGzip()) {
            conn.setRequestProperty("Accept-Encoding", "gzip");
        }

        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setDoInput(true);
        return conn;
    }
}
