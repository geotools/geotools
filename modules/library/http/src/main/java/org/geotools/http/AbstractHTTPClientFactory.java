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
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;
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

    /**
     * Returns true if a given factory have a HTTP_CLIENT given by the hint, or meets the behaviors.
     */
    @Override
    public final boolean canProcess(Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {

        Object val = hints.get(Hints.HTTP_CLIENT);
        return clientClasses().stream()
                .filter(cls -> matchClientHint(cls, val))
                .anyMatch(
                        cls ->
                                behaviors.stream()
                                        .allMatch(behavior -> behavior.isAssignableFrom(cls)));
    }

    private static boolean matchClientHint(Class<?> cls, Object val) {
        if (val == null) {
            return true;
        }

        if (val instanceof String) {
            final String clsName = (String) val;
            return cls.getName().equals(clsName);
        } else {
            final Class<?> cls2 = (Class<?>) val;
            return cls2 == cls;
        }
    }

    /**
     * Return the HTTPClient classes that this factory will create.
     *
     * @return
     */
    protected abstract List<Class<?>> clientClasses();

    /**
     * Create instance of HTTPClient. Behaviors should be used if factory creates different
     * client's. Otherwise it's excessive to use.
     */
    @Override
    public abstract HTTPClient createClient(List<Class<? extends HTTPBehavior>> behaviors);

    @Override
    public final HTTPClient createClient(
            Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {
        HTTPClient client = createClient(behaviors);
        Set<Class<? extends HTTPBehavior>> missingBehaviors =
                behaviors.stream()
                        .filter(behavior -> !behavior.isInstance(client))
                        .collect(Collectors.toSet());
        if (!missingBehaviors.isEmpty()) {
            throw new RuntimeException(
                    String.format(
                            "HTTP client %s doesn't support behaviors: %s",
                            client.getClass().getName(),
                            missingBehaviors.stream()
                                    .map(behavior -> behavior.getSimpleName())
                                    .collect(Collectors.joining(", "))));
        }
        if (hints.containsKey(Hints.HTTP_LOGGING)) {
            return applyLogging(client, hints);
        } else {
            return client;
        }
    }

    /**
     * Wraps client with a LoggingHTTPClient if the HTTP_LOGGING hint is set.
     *
     * @param client
     * @param hints
     * @return
     */
    protected HTTPClient applyLogging(HTTPClient client, Hints hints) {
        LOGGER.fine("Applying logging to HTTP Client.");
        final String hint = (String) hints.get(Hints.HTTP_LOGGING);
        final Boolean logging = Boolean.parseBoolean(hint);
        if (logging) {
            return createLogging(client);
        } else if ("false".equalsIgnoreCase(hint)) {
            return client;
        } else {
            throw new RuntimeException(String.format("HTTP_LOGGING value %s is unknown.", hint));
        }
    }

    protected HTTPClient createLogging(HTTPClient client) {
        return new LoggingHTTPClient(client);
    }
}
