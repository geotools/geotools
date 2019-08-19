/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.referencing.factory.epsg.hsql;

import java.awt.geom.Rectangle2D;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.geotools.geometry.Envelope2D;
import org.geotools.referencing.CRS;
import org.geotools.referencing.ReferencingFactoryFinder;
import org.geotools.util.factory.GeoTools;
import org.geotools.util.factory.Hints;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;
import org.opengis.referencing.operation.CoordinateOperation;
import org.opengis.referencing.operation.CoordinateOperationFactory;
import org.opengis.referencing.operation.OperationNotFoundException;
import org.opengis.referencing.operation.TransformException;

/**
 * Unit test for <a href="https://jira.codehaus.org/browse/GEOT-4780">GEOT-4780</>.<br>
 * Detailled description :<br>
 * - One thread continuously retrieves the {@link CoordinateOperationFactory} through the {@link
 * ReferencingFactoryFinder}.<br>
 * - Another thread performs {@link #NUM_ITERATIONS} reprojections using CRS for which the HSQL
 * database indicates a NTv2Transform.<br>
 * When the second thread ends, the first one is stopped.<br>
 * Using multiple iterations, we quite always produce a deadlock.<br>
 * Note that I could not reproduce the problem with {@link #LENIENT} set to <code>true</code> here,
 * but I could in another application.<br>
 * <br>
 *
 * @author Stephane Wasserhardt
 */
public class ThreadedTransformTest {

    private static final int NUM_ITERATIONS = 1000;

    private static final Boolean LENIENT = false;

    private static final Hints HINTS = GeoTools.getDefaultHints();

    static {
        HINTS.put(Hints.LENIENT_DATUM_SHIFT, LENIENT);
    }

    private final ExecutorService EXECUTOR = Executors.newCachedThreadPool();

    private CoordinateReferenceSystem nad83;

    private CoordinateReferenceSystem wgs84;

    private Envelope2D envelope;

    /**
     * Instantiates the test data.
     *
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        nad83 = CRS.decode("EPSG:4269");
        wgs84 = CRS.decode("EPSG:4326");
        envelope = new Envelope2D(wgs84, new Rectangle2D.Double(-77.145996, 39.04541, 0.1, 0.1));
    }

    @After
    public void tearDown() throws Exception {
        EXECUTOR.shutdown();
    }

    protected void retrieve() {
        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
            ReferencingFactoryFinder.getCoordinateOperationFactory(HINTS);
        }
    }

    protected void transform()
            throws URISyntaxException, OperationNotFoundException, FactoryException,
                    TransformException {
        for (int iter = 0; iter < NUM_ITERATIONS; iter++) {
            CoordinateOperationFactory coordinateOperationFactory;
            coordinateOperationFactory =
                    ReferencingFactoryFinder.getCoordinateOperationFactory(HINTS);
            // The problem also appears using :
            // coordinateOperationFactory = CRS
            // .getCoordinateOperationFactory(LENIENT);

            final CoordinateOperation operation =
                    coordinateOperationFactory.createOperation(wgs84, nad83);

            CRS.transform(operation, envelope);
        }
    }

    /**
     * Main test method. Waits 30 seconds to perform envelope reprojections with multiple threads
     * while one threads retrieves the coordinate operation factory. If this fails, there's most
     * probably a deadlock that prevents the operations from finishing (use debugger to see threads
     * & owned/waiting locks).<br>
     * This may not fail all the time, but using multiple iterations should maximize chances of
     * producing the bug. Although this may vary from a computer to another...
     *
     * @throws Exception If an exception occurs while performing {@link #retrieve} or {@link
     *     #transform}.
     */
    @Test(timeout = 30000L)
    public void testMultithreadDeadlock() throws Exception {
        List<Future<Void>> futures = new ArrayList<>();
        // raise some hell with 32 total threads
        for (int i = 0; i < 16; i++) {
            Future<Void> f =
                    EXECUTOR.submit(
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    retrieve();
                                    return null;
                                }
                            });
            futures.add(f);
            f =
                    EXECUTOR.submit(
                            new Callable<Void>() {
                                @Override
                                public Void call() throws Exception {
                                    transform();
                                    return null;
                                }
                            });
            futures.add(f);
        }

        // wait for all
        for (Future<Void> f : futures) {
            f.get();
        }
    }
}
