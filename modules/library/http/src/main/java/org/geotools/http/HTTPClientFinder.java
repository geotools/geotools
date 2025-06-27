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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import org.geotools.util.factory.FactoryCreator;
import org.geotools.util.factory.FactoryFinder;
import org.geotools.util.factory.FactoryRegistry;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;

/**
 * Factory finder for getting instances of HTTPClientFactory. All implementations should be registered as a file with
 * name org.geotools.http.HTTPClientFactory in META_INF/services of their respective jar.
 *
 * <p>To pick a particular implementation the hint HTTP_CLIENT_FACTORY could be set to the full class name, either in
 * the function call, or by Java property.
 *
 * @author Roar Brænden
 */
public class HTTPClientFinder extends FactoryFinder {

    /** The service registry for this manager. Will be initialized only when first needed. */
    private static volatile FactoryRegistry registry;

    private HTTPClientFinder() {
        // singleton
    }

    private static FactoryRegistry getServiceRegistry() {
        assert Thread.holdsLock(HTTPClientFinder.class);
        if (registry == null) {
            registry = new FactoryCreator(Arrays.asList(new Class<?>[] {HTTPClientFactory.class}));
        }
        return registry;
    }

    /** Get default HTTP client. Can be adjusted by setting property "org.geotools.http.client". */
    public static HTTPClient createClient() {
        final Hints hints = GeoTools.getDefaultHints();
        return lookupClient(hints, new LinkedList<>());
    }

    /** Get HTTP client with the given behaviors. */
    @SafeVarargs
    public static HTTPClient createClient(Class<? extends HTTPBehavior>... behaviors) {
        final Hints hints = GeoTools.getDefaultHints();
        final List<Class<? extends HTTPBehavior>> list = new ArrayList<>();
        for (Class<? extends HTTPBehavior> behavior : behaviors) {
            list.add(behavior);
        }
        return lookupClient(hints, list);
    }

    /**
     * Get a special HTTP client by specifying hint HTTP_CLIENT_FACTORY or HTTP_CLIENT
     *
     * <p>Merges with system defaults
     *
     * @param hints
     * @return
     */
    public static HTTPClient createClient(Hints hints) {
        final Hints merged = mergeSystemHints(hints);
        return lookupClient(merged, new LinkedList<>());
    }

    private static synchronized HTTPClient lookupClient(
            Hints hints, final List<Class<? extends HTTPBehavior>> behaviors) {
        if (hints == null) {
            throw new IllegalArgumentException("hints can't be null.");
        }
        if (behaviors == null) {
            throw new IllegalArgumentException("behaviors can't be null.");
        }

        if (System.getProperty("http.proxyHost") != null) {
            behaviors.add(HTTPProxy.class);
        }

        return getServiceRegistry()
                .getFactories(HTTPClientFactory.class, null, null)
                .filter(fact -> matchHttpFactoryHints(fact, hints))
                .filter(fact -> matchHttpClientHintsBehaviors(fact, hints, behaviors))
                .filter(fact -> matchDefault(fact, hints, behaviors))
                .findFirst()
                .orElseThrow(() -> new HTTPFactoryException("Couldn't create HTTP client.", hints, behaviors))
                .createClient(hints, behaviors);
    }

    /** Makes sure a call for getServiceRegistry will do a clean scan */
    public static synchronized void reset() {
        final FactoryRegistry copy = registry;
        registry = null;
        if (copy != null) {
            copy.deregisterAll();
        }
    }

    /** If HTTP_CLIENT_FACTORY is set we must match */
    private static boolean matchHttpFactoryHints(HTTPClientFactory fact, Hints hints) {
        if (!hints.containsKey(Hints.HTTP_CLIENT_FACTORY)) {
            return true;
        }
        Object val = hints.get(Hints.HTTP_CLIENT_FACTORY);
        return val instanceof String
                ? fact.getClass().getName().equalsIgnoreCase((String) val)
                : fact.getClass() == (Class<?>) val;
    }

    /** Check if any of then factory's clients can process the hints and behaviors */
    private static boolean matchHttpClientHintsBehaviors(
            HTTPClientFactory fact, Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {
        return fact.canProcess(hints, behaviors);
    }

    /** if no hint is specified, and no behaviors, we check if fact is the default */
    private static boolean matchDefault(
            HTTPClientFactory fact, Hints hints, List<Class<? extends HTTPBehavior>> behaviors) {
        if (hints.containsKey(Hints.HTTP_CLIENT)
                || hints.containsKey(Hints.HTTP_CLIENT_FACTORY)
                || !behaviors.isEmpty()) {
            return true;
        }
        return fact.getClass() == DefaultHTTPClientFactory.class;
    }
}
