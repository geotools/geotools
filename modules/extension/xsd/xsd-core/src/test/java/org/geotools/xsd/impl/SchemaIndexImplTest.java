/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2026, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.xsd.impl;

import static java.util.Collections.emptyList;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.xsd.XSDSchema;
import org.eclipse.xsd.util.XSDSchemaLocator;
import org.geotools.util.NullEntityResolver;
import org.geotools.xsd.Schemas;
import org.junit.Test;

public class SchemaIndexImplTest {

    private static final String SHARED_NAMESPACE = "http://geotools.org/test/shared";

    private static final long DURATION_MS = 2000;

    private static final int INDEXING_THREADS = 3;

    /**
     * A shared schema (like the GML one) is indexed by every Encoder and Parser, while parsing a schema that imports it
     * modifies it. The adapters list of the shared schema must survive both happening at the same time.
     */
    @Test
    public void testIndexSharedSchemaWhileParsingSchemaImportingIt() throws Exception {
        XSDSchema shared = parse(getClass().getResource("shared.xsd").toString(), emptyList());
        // shared schemas always carry adapters, so each modification sends notifications
        Adapter permanent = new AdapterImpl();
        shared.eAdapters().add(permanent);

        List<XSDSchemaLocator> locators = List.of((schema, namespace, rawLocation, resolvedLocation) ->
                SHARED_NAMESPACE.equals(namespace) ? shared : null);
        String importer = getClass().getResource("importer.xsd").toString();

        AtomicBoolean stop = new AtomicBoolean();
        long deadline = System.currentTimeMillis() + DURATION_MS;
        List<Callable<Void>> tasks = new ArrayList<>();
        for (int i = 0; i < INDEXING_THREADS; i++) {
            tasks.add(untilDeadline(deadline, stop, () -> new SchemaIndexImpl(new XSDSchema[] {shared}).destroy()));
        }
        tasks.add(untilDeadline(deadline, stop, () -> Schemas.dispose(parse(importer, locators))));

        @SuppressWarnings("PMD.CloseResource") // shut down in the finally block, Java 17 has no close()
        ExecutorService executor = Executors.newFixedThreadPool(tasks.size());
        try {
            List<Future<Void>> futures = new ArrayList<>();
            for (Callable<Void> task : tasks) {
                futures.add(executor.submit(task));
            }
            for (Future<Void> future : futures) {
                future.get(DURATION_MS * 4, TimeUnit.MILLISECONDS);
            }
        } finally {
            executor.shutdownNow();
        }

        // the adapters list is still consistent and holds only the adapter added outside the indexes
        Adapter probe = new AdapterImpl();
        shared.eAdapters().add(probe);
        shared.eAdapters().remove(probe);
        assertEquals(List.of(permanent), new ArrayList<>(shared.eAdapters()));
        shared.eAdapters().remove(permanent);
    }

    private static XSDSchema parse(String location, List<XSDSchemaLocator> locators) throws IOException {
        return Schemas.parse(location, locators, emptyList(), emptyList(), NullEntityResolver.INSTANCE);
    }

    private static Callable<Void> untilDeadline(long deadline, AtomicBoolean stop, Action action) {
        return () -> {
            try {
                while (!stop.get() && System.currentTimeMillis() < deadline) {
                    action.run();
                }
            } catch (Exception | Error e) {
                stop.set(true);
                throw e;
            }
            return null;
        };
    }

    @FunctionalInterface
    private interface Action {
        void run() throws Exception;
    }
}
