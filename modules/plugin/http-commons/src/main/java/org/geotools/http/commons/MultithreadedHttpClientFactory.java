/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.http.commons;

import java.util.Collections;
import java.util.List;
import org.geotools.http.AbstractHTTPClientFactory;
import org.geotools.http.HTTPBehavior;
import org.geotools.http.HTTPClient;
import org.geotools.http.HTTPConnectionPooling;
import org.geotools.http.LoggingHTTPClient;

/**
 * Factory for MultithreadedHttpClient
 *
 * <p>To use client set Hints.HTTP_CLIENT_FACTORY=MultithreadedHttpClientFactory.class, or
 * Hints.HTTP_CLIENT=MultithreadedHttpClient.class
 *
 * @author Roar Brænden
 */
public class MultithreadedHttpClientFactory extends AbstractHTTPClientFactory {

    @Override
    public List<Class<?>> clientClasses() {
        return Collections.singletonList(MultithreadedHttpClient.class);
    }

    @Override
    public final HTTPClient createClient(List<Class<? extends HTTPBehavior>> behaviors) {
        return new MultithreadedHttpClient();
    }

    @Override
    protected HTTPClient createLogging(HTTPClient client) {
        return new LoggingConnectionPoolingHTTPClient(client);
    }

    static class LoggingConnectionPoolingHTTPClient extends LoggingHTTPClient implements HTTPConnectionPooling {

        public LoggingConnectionPoolingHTTPClient(HTTPClient delegate) {
            super(delegate);
        }

        public LoggingConnectionPoolingHTTPClient(HTTPClient delegate, String charset) {
            super(delegate, charset);
        }

        @Override
        public int getMaxConnections() {
            return ((HTTPConnectionPooling) delegate).getMaxConnections();
        }

        @Override
        public void setMaxConnections(int maxConnections) {
            ((HTTPConnectionPooling) delegate).setMaxConnections(maxConnections);
        }

        @Override
        public void close() {}
    }
}
