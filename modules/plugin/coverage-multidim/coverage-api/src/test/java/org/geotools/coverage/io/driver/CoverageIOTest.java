/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.driver;

import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.driver.TestDriver.TestCoverageAccess;
import org.geotools.coverage.io.impl.CoverageIO;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.junit.Assert;
import org.junit.Test;

public class CoverageIOTest extends Assert {

    @Test
    public void testCoverageIO() throws IOException {

        // Test on Drivers lookup
        final Driver[] drivers = CoverageIO.getAvailableDriversArray();
        assertNotNull(drivers);
        assertTrue(drivers[0] instanceof TestDriver);
        final TestDriver testDriver = (TestDriver) drivers[0];

        final URL testURL = new URL(TestDriver.TEST_URL);

        final Driver driver = CoverageIO.findDriver(testURL);
        assertEquals(testDriver, driver);
        final Set<Driver> driversSet = CoverageIO.findDrivers(testURL);
        assertSame(testDriver, driversSet.iterator().next());

        // Connecting to the only coverageAccess supported by the TestDriver
        Map<String, Serializable> connectionParams = new HashMap<String, Serializable>();
        connectionParams.put(DefaultFileDriver.URL.key, testURL);
        boolean canConnect = CoverageIO.canConnect(connectionParams);
        CoverageAccess access = CoverageIO.connect(connectionParams);
        assertNotNull(access);
        assertTrue(access instanceof TestCoverageAccess);
        assertTrue(canConnect);

        // Trying to connect to coverageAccess not being supported by the testDriver
        connectionParams = new HashMap<String, Serializable>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL("file:///unsupportedCoverage"));
        canConnect = CoverageIO.canConnect(connectionParams);
        assertFalse(canConnect);
        access = CoverageIO.connect(connectionParams);
        assertNull(access);
    }
}
