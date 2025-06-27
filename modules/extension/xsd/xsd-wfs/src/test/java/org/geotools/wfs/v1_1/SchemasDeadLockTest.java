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
package org.geotools.wfs.v1_1;

import static org.junit.Assert.assertTrue;

import java.util.concurrent.Callable;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;
import org.eclipse.xsd.XSDSchema;
import org.geotools.filter.v1_1.OGC;
import org.geotools.gml3.GML;
import org.geotools.xsd.XSD;
import org.geotools.xsd.ows.OWS;
import org.junit.Test;

/**
 * Tests showing deadlock in loading XSD schemas. Done here because it needs a number of inter-dependent XSD objects in
 * order to manifest. This 4 are enough to make it happen most of the time on a Ryzen 1700x (8 phsycal cores, the loads
 * actually run in parallel)
 */
public class SchemasDeadLockTest {
    protected static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(SchemasDeadLockTest.class);

    @Test
    public void testParse() throws Exception {
        ExecutorService executorService = Executors.newFixedThreadPool(4);

        // latch used to make the 4 threads all actually parse in parallel
        CountDownLatch threadCreationLatch = new CountDownLatch(1);

        // 4 inter-dependent schemas (WFS -> OGC -> GML and WFS -> OWS)
        executorService.submit(new XSDSchemaFetcher(threadCreationLatch, GML.getInstance()));
        executorService.submit(new XSDSchemaFetcher(threadCreationLatch, OGC.getInstance()));
        executorService.submit(new XSDSchemaFetcher(threadCreationLatch, WFS.getInstance()));
        executorService.submit(new XSDSchemaFetcher(threadCreationLatch, OWS.getInstance()));

        // releasing the schema building for good
        threadCreationLatch.countDown();
        executorService.shutdown();
        assertTrue(
                "Waited too long for termination, deadlock likely",
                executorService.awaitTermination(20, TimeUnit.SECONDS));
    }

    public static class XSDSchemaFetcher implements Callable<XSDSchema> {

        private XSD xsd;
        private CountDownLatch threadCreationLatch;

        XSDSchemaFetcher(CountDownLatch threadCreationLatch, XSD xsd) {
            this.threadCreationLatch = threadCreationLatch;
            this.xsd = xsd;
        }

        @Override
        public XSDSchema call() throws Exception {
            threadCreationLatch.await();
            return xsd.getSchema();
        }
    }
}
