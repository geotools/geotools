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

import java.util.List;
import java.util.logging.Logger;
import org.geotools.util.factory.Hints;
import org.geotools.util.logging.Logging;

/**
 * Base HTTPClientFactory adding wrapper client's to the desired HTTP Client like for instance
 * LoggingHTTPClient.
 *
 * @author Roar Brænden
 */
public abstract class AbstractHTTPClientFactory implements HTTPClientFactory {

    private final Logger LOGGER = Logging.getLogger(AbstractHTTPClientFactory.class);

    public AbstractHTTPClientFactory() {}

    @Override
    public boolean willCreate(Hints hints) {
        if (!hints.containsKey(Hints.HTTP_CLIENT)) {
            return true;
        }

        Object val = hints.get(Hints.HTTP_CLIENT);
        if (val instanceof String) {
            final String clsName = (String) val;
            return clientClasses().stream().anyMatch((cls) -> cls.getName().equals(clsName));
        } else {
            final Class<?> cls = (Class<?>) val;
            return clientClasses().stream().anyMatch((cls2) -> cls2 == cls);
        }
    }

    protected abstract List<Class<?>> clientClasses();

    @Override
    public abstract HTTPClient createClient();

    @Override
    public HTTPClient createClient(Hints hints) {
        return applyLogging(createClient(), hints);
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
            LOGGER.fine("Applying logging to HTTP Client.");
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
}
