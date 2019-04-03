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
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.CoverageAccess.AccessType;
import org.geotools.coverage.io.CoverageSource;
import org.geotools.coverage.io.CoverageSource.SpatialDomain;
import org.geotools.coverage.io.Driver.DriverCapabilities;
import org.geotools.coverage.io.RasterLayoutTest;
import org.geotools.coverage.io.TestCoverageSourceDescriptor;
import org.geotools.coverage.io.impl.DefaultFileDriver;
import org.geotools.referencing.CRS;
import org.geotools.util.SimpleInternationalString;
import org.junit.Assert;
import org.junit.Test;
import org.opengis.feature.type.Name;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;
import org.opengis.referencing.crs.CoordinateReferenceSystem;

public class DriverTest extends Assert {

    private static final TestDriver driver = new TestDriver();

    private static CoordinateReferenceSystem WGS84;

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(TestDriver.class);

    static {
        try {
            WGS84 = CRS.decode("EPSG:4326", true);
        } catch (NoSuchAuthorityCodeException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        } catch (FactoryException e) {
            LOGGER.log(Level.FINER, e.getMessage(), e);
        }
    }

    @Test
    public void testDriver() throws IOException {

        SimpleInternationalString driverName =
                new SimpleInternationalString(TestDriver.TEST_DRIVER);

        // Testing main driver capabilities. That's a Dummy Driver, it can only connect
        Map<String, Serializable> connectionParams = new HashMap<String, Serializable>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TestDriver.TEST_URL));

        assertEquals(TestDriver.TEST_DRIVER, driver.getName());
        assertEquals(driverName, driver.getTitle());
        assertEquals(driverName, driver.getDescription());
        assertTrue(driver.canAccess(DriverCapabilities.CONNECT, connectionParams));
        assertFalse(driver.canAccess(DriverCapabilities.CREATE, connectionParams));
        assertFalse(driver.canAccess(DriverCapabilities.DELETE, connectionParams));
    }

    @Test
    public void testCoverageAccess() throws IOException {
        Map<String, Serializable> connectionParams = new HashMap<String, Serializable>();
        connectionParams.put(DefaultFileDriver.URL.key, new URL(TestDriver.TEST_URL));

        CoverageAccess access =
                driver.access(DriverCapabilities.CONNECT, connectionParams, null, null);
        assertFalse(access.isCreateSupported());
        assertFalse(access.isDeleteSupported());
        assertSame(driver, access.getDriver());

        // Checking proper coverage name
        final List<Name> names = access.getNames(null);
        final Name coverageName = names.get(0);
        assertEquals(1, names.size());
        assertEquals(TestCoverageSourceDescriptor.TEST_NAME, coverageName);

        final CoverageSource source =
                access.access(
                        TestCoverageSourceDescriptor.TEST_NAME,
                        null,
                        AccessType.READ_ONLY,
                        null,
                        null);
        CoordinateReferenceSystem crs = source.getCoordinateReferenceSystem();
        assertEquals(TestCoverageSourceDescriptor.TEST_NAME, source.getName(null));

        // Test dimensions and descriptors
        assertTrue(source.getAdditionalDomains().isEmpty());
        assertTrue(source.getDimensionDescriptors().isEmpty());
        assertNull(source.getVerticalDomain());
        assertNotNull(source.getTemporalDomain());

        SpatialDomain spatialDomain = source.getSpatialDomain();
        assertNotNull(spatialDomain);
        assertTrue(CRS.equalsIgnoreMetadata(spatialDomain.getCoordinateReferenceSystem2D(), WGS84));
        assertEquals(
                RasterLayoutTest.testRasterLayout,
                spatialDomain.getRasterElements(false, null).iterator().next());

        assertNotNull(crs);
        assertEquals(WGS84, crs);
    }
}
