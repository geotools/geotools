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
package org.geotools.http;

import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.LoggingHTTPClient;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/** @author Roar Brænden */
public abstract class AbstractHTTPClientFactory implements HTTPClientFactory {

    public AbstractHTTPClientFactory() {}

    @Override
    public HTTPClient getClient() {
        final Hints hints = GeoTools.getDefaultHints();
        return applyLogging(createClient(hints), hints);
    }

    @Override
    public HTTPClient getClient(Hints hints) {
        hints = GeoTools.addDefaultHints(hints);
        return applyLogging(createClient(hints), hints);
    }

    /**
     * Wraps client with a LoggingHTTPClient if the HTTP_LOGGING hint is set. Override if using a
     * different LoggingHTTPClient.
     *
     * @param client
     * @param hints
     * @return
     */
    protected HTTPClient applyLogging(HTTPClient client, Hints hints) {
        if (hints.containsKey(Hints.HTTP_LOGGING)) {
            final String hint = (String) hints.get(Hints.HTTP_LOGGING);
            final Boolean logging = Boolean.parseBoolean(hint);
            if (logging) {
                return new LoggingHTTPClient(client);
            } else if (!"false".equalsIgnoreCase(hint)) {
                return new LoggingHTTPClient(client, hint);
            }
        }
        return client;
    }

    /**
     * Create the main client.
     *
     * @param hints
     * @return
     */
    protected abstract HTTPClient createClient(Hints hints);
}
