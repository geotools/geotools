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
 *
 */
package org.geotools.http;

import java.util.Arrays;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.Hints;

/**
 * Factory finder for getting instances of HTTPClientFactory. All implementations should be
 * registered as a file with name org.geotools.http.HTTPClientFactory in META_INF/services of their
 * respective jar.
 *
 * <p>To pick a particular implementation the hint HTTP_CLIENT_FACTORY could be set to the full
 * class name, either in the function call, or by Java property.
 *
 * @author Roar Brænden
 */
public class HTTPFactoryFinder extends FactoryFinder {

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    private HTTPFactoryFinder() {
        // singleton
    }

    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(HTTPFactoryFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {HTTPClientFactory.class}));
        }
        return registry;
    }

    /**
     * Get HTTP client with system defaults
     *
     * @return
     */
    public static synchronized HTTPClient getClient() {
        return getClient(null);
    }

    /**
     * Get a special HTTP client by specifying hint HTTP_CLIENT_FACTORY or HTTP_CLIENT
     *
     * <p>Merges with system defaults
     *
     * @param hints
     * @return
     */
    public static synchronized HTTPClient getClient(Hints hints) {
        final Hints merged = mergeSystemHints(hints);
        return getServiceRegistry()
                .getFactories(HTTPClientFactory.class, null, null)
                .filter((fact) -> matchHttpFactoryHints(merged, fact))
                .filter((fact) -> matchHttpClientHints(merged, fact.getClientClass()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("No HTTPClientFactory matched the hints."))
                .createClient(merged);
    }

    /** Makes sure a call for getServiceRegistry will do a clean scan */
    public static synchronized void reset() {
        final FactoryRegistry copy = registry;
        registry = null;
        if (copy != null) {
            copy.deregisterAll();
        }
    }

    private static boolean matchHttpFactoryHints(Hints hints, HTTPClientFactory fact) {
        if (!hints.containsKey(Hints.HTTP_CLIENT_FACTORY)) {
            return true;
        }
        Object val = hints.get(Hints.HTTP_CLIENT_FACTORY);
        return (val instanceof String
                ? fact.getClass().getName().equalsIgnoreCase((String) val)
                : fact.getClass() == (Class<?>) val);
    }

    private static boolean matchHttpClientHints(
            Hints hints, Class<? extends HTTPClient> clientCls) {
        if (!hints.containsKey(Hints.HTTP_CLIENT)) {
            return true;
        }
        Object val = hints.get(Hints.HTTP_CLIENT);
        return (val instanceof String
                ? clientCls.getName().equalsIgnoreCase((String) val)
                : val == clientCls);
    }
}
