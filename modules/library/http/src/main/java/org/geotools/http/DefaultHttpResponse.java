/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URLConnection;
import java.util.zip.GZIPInputStream;

/**
 * HTTPResponse that uses URLConnection as its basis.
 *
 * @author Roar Brænden
 */
public class DefaultHttpResponse implements HTTPResponse {

    private URLConnection connection;

    private InputStream responseStream;

    @SuppressWarnings("PMD.CloseResource") // responseStream closed by dispose()
    public DefaultHttpResponse(final URLConnection connection) throws IOException {
        this.connection = connection;
        InputStream inputStream = null;
        try {
            inputStream = connection.getInputStream();
            final String contentEncoding = connection.getContentEncoding();

            if (contentEncoding != null && connection.getContentEncoding().indexOf("gzip") != -1) {
                inputStream = new GZIPInputStream(inputStream);
            }
        } catch (Exception e) {
            if (inputStream != null) {
                inputStream.close();
            }
            throw e;
        }
        responseStream = inputStream;
    }

    @Override
    public void dispose() {
        if (responseStream != null) {
            try {
                responseStream.close();
            } catch (IOException e) {
                // ignore
            }
            responseStream = null;
        }
        if (connection != null) {
            if (connection instanceof HttpURLConnection lConnection) {
                lConnection.disconnect();
            }
            connection = null;
        }
    }

    @Override
    public String getContentType() {
        return connection.getContentType();
    }

    @Override
    public String getResponseHeader(String headerName) {
        return connection.getHeaderField(headerName);
    }

    @Override
    public InputStream getResponseStream() throws IOException {
        return responseStream;
    }

    @Override
    public String getResponseCharset() {
        String contentType = getContentType();
        if (null == contentType) {
            return null;
        }
        String[] split = contentType.split(";");

        for (int i = 1; i < split.length; i++) {
            String[] mimeParam = split[i].split("=");
            if (mimeParam.length == 2 && "charset".equalsIgnoreCase(mimeParam[0])) {
                String charset = mimeParam[1];
                return charset.trim();
            }
        }
        return null;
    }
}
