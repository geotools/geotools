/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.grib1;

import java.util.Set;

import junit.framework.TestCase;

import org.geotools.coverage.io.driver.CoverageIO;
import org.geotools.coverage.io.driver.Driver;
import org.junit.Ignore;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Class for testing availability of GRIB1 Driver
 * 
 * @author Simone Giannecchini
 * @author Daniele Romagnoli
 */
public class ServiceTest extends TestCase {

    public ServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(ServiceTest.class);
    }

    @org.junit.Test
    @Ignore
    public void testIsAvailable() throws NoSuchAuthorityCodeException,
            FactoryException {
//        CoverageIO.scanForPlugins();
//        
//        Set<Driver> drivers = CoverageIO.getAvailableDrivers();
//        Driver driverFound = null;
//        for (Driver driver: drivers)
//            if (driver instanceof GRIB1Driver) {
//                driverFound = driver;
//                break;
//        }
//        assertTrue("GRIB1Driver not registered", driverFound!=null);
//        assertTrue("GRIB1Driver not available", driverFound.isAvailable());
    }
}
