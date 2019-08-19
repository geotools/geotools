/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.netcdf;

import static org.junit.Assert.assertTrue;

import java.io.File;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.impl.CoverageIO;
import org.geotools.imageio.netcdf.NetCDFImageReader;
import org.geotools.test.TestData;
import org.geotools.util.logging.Logging;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Class for testing availability of NetCDF format factory
 *
 * @author Simone Giannecchini
 * @author Daniele Romagnoli
 */
public class ServiceTest {

    private static final Logger LOGGER = Logging.getLogger(ServiceTest.class);

    @Test
    public void isAvailable() throws NoSuchAuthorityCodeException, FactoryException {
        CoverageIO.scanForPlugins();
        Set<Driver> drivers = CoverageIO.getAvailableDrivers();
        Driver driverFound = null;
        for (Driver driver : drivers)
            if (driver instanceof NetCDFDriver) {
                driverFound = driver;
                break;
            }
        assertTrue("NetCDFDriver not registered", driverFound != null);
        assertTrue("NetCDFDriver not available", driverFound.isAvailable());
    }

    @Test
    public void isAvailableFromFile() throws Exception {
        final File file = TestData.file(this, "O3-NO2.nc");
        if (!file.exists()) {

            LOGGER.severe("Unable to locate test data O3-NO2.nc. Test aborted!");
            return;
        }
        Iterator<ImageReader> readers = ImageIO.getImageReaders(file);
        assertTrue("No valid readers found", readers.hasNext());

        boolean found = false;
        while (readers.hasNext()) {
            ImageReader reader = readers.next();
            if (reader instanceof NetCDFImageReader) {
                found = true;
            }
        }
        assertTrue("DummyUnidataReader not registered", found);
    }
}
