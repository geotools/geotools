/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2020, Open Source Geospatial Foundation (OSGeo)
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

import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.data.ows.HTTPClient;
import org.geotools.data.ows.LoggingHTTPClient;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/** @author Roar Brænden */
public abstract class AbstractHTTPClientFactory implements HTTPClientFactory {

    private Logger LOGGER = Logging.getLogger(AbstractHTTPClientFactory.class);

    private Class<HTTPClient> systemClient = null;

    private boolean logging = false;
    private String charset;

    @SuppressWarnings("unchecked")
    public AbstractHTTPClientFactory() {
        final Hints systemHints = GeoTools.getDefaultHints();
        if (systemHints.containsKey(Hints.HTTP_CLIENT)) {
            try {
                systemClient = (Class<HTTPClient>) systemHints.get(Hints.HTTP_CLIENT);
            } catch (ClassCastException ex) {
                LOGGER.log(Level.SEVERE, "Tried to cast Hint HTTP_CLIENT to HTTPClient", ex);
            }
        }

        applyLoggingHint(systemHints);
    }

    private void applyLoggingHint(Hints hints) {
        if (hints.containsKey(Hints.HTTP_LOGGING)) {
            final String hint = (String) hints.get(Hints.HTTP_LOGGING);
            final Boolean logging = Boolean.parseBoolean(hint);
            if (!logging && !"false".equalsIgnoreCase(hint)) {
                this.logging = true;
                this.charset = hint;
            } else {
                this.logging = logging;
            }
        }
    }

    @Override
    public void logging(boolean logging) {
        this.logging = logging;
    }

    @Override
    public void logging(String charset) {
        this.logging = true;
        this.charset = charset;
    }

    @Override
    public HTTPClient getClient() {
        return getClient(
                (systemClient != null ? new Hints(Hints.HTTP_CLIENT, systemClient) : new Hints()));
    }

    @Override
    public HTTPClient getClient(Hints hints) {
        applyLoggingHint(hints);
        HTTPClient client = createClient(hints);
        if (logging) {
            client =
                    (charset == null
                            ? new LoggingHTTPClient(client)
                            : new LoggingHTTPClient(client, charset));
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
