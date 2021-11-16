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
package org.geotools.gce.geotiff;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import javax.measure.Unit;
import org.geotools.referencing.CRS;
import org.geotools.referencing.factory.AllAuthoritiesFactory;
import org.geotools.util.factory.Hints;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.crs.CRSAuthorityFactory;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class GeoTiffCRSFactoryDeadlockTest {

    @Test
    public void testDeadlockOnCRSFactories() throws ExecutionException, InterruptedException {
        boolean potentialDeadlock = false;
        final long timeout = 10;
        final int size = 30;

        final boolean[] exceptionOccurred = {false};
        ExecutorService executorService = Executors.newFixedThreadPool(8);

        List<Future> futures = new ArrayList<>(size);

        for (int i = 0; i < size; i += 3) {
            futures.add(getUnit(executorService, exceptionOccurred));
            futures.add(getCRS(executorService, exceptionOccurred));
            futures.add(getAuthorityFactory(executorService));
        }

        try {
            for (Future future : futures) {
                future.get(timeout, TimeUnit.SECONDS);
            }
            executorService.shutdown();
            executorService.awaitTermination(timeout, TimeUnit.SECONDS);
        } catch (InterruptedException | TimeoutException e) {
            // Timeout occurred while waiting for execution termination.
            potentialDeadlock = true;
        }
        assertFalse("FactoryException occurred", exceptionOccurred[0]);
        assertFalse("The execution didn't finished yet: potential Deadlock", potentialDeadlock);
    }

    Future getAuthorityFactory(ExecutorService executorService) {
        return executorService.submit(
                () -> {
                    CRSAuthorityFactory factory = CRS.getAuthorityFactory(true);
                    assertNotNull(factory);
                });
    }

    Future getUnit(ExecutorService executorService, final boolean[] exceptionOccurred) {
        return executorService.submit(
                () -> {
                    Hints hints = new Hints();
                    hints.add(new Hints(Hints.FORCE_LONGITUDE_FIRST_AXIS_ORDER, true));
                    AllAuthoritiesFactory factory = new AllAuthoritiesFactory(hints);
                    Unit<?> unit = null;
                    try {
                        unit = factory.createUnit("EPSG:9102");
                    } catch (FactoryException e) {
                        exceptionOccurred[0] = true;
                    }
                    assertNotNull(unit);
                });
    }

    Future getCRS(ExecutorService executorService, final boolean[] exceptionOccurred) {
        return executorService.submit(
                () -> {
                    CoordinateReferenceSystem crs = null;
                    try {
                        crs = CRS.decode("EPSG:4326", true);
                    } catch (FactoryException e) {
                        exceptionOccurred[0] = true;
                    }
                    assertNotNull(crs);
                });
    }
}
