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
package org.geotools.coverage.io.hdf4;

import java.util.Set;

import junit.framework.TestCase;

import org.geotools.coverage.io.driver.CoverageIO;
import org.geotools.coverage.io.driver.Driver;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Class for testing availability of NetCDF format factory
 * 
 * @author Simone Giannecchini
 * @author Daniele Romagnoli
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/hdf4/src/test/java/org/geotools/coverage/io/hdf4/ServiceTest.java $
 */
public class ServiceTest extends TestCase {

    public ServiceTest(java.lang.String testName) {
        super(testName);
    }

    public static void main(java.lang.String[] args) {
        junit.textui.TestRunner.run(ServiceTest.class);
    }

    public void testIsAvailable() throws NoSuchAuthorityCodeException,
            FactoryException {
        CoverageIO.scanForPlugins();
        
        Set<Driver> drivers = CoverageIO.getAvailableDrivers();
        Driver driverFound = null;
        for (Driver driver: drivers)
            if (driver instanceof HDF4Driver) {
                driverFound = driver;
                break;
        }
        assertTrue("HDF4Driver not registered", driverFound!=null);
        assertTrue("HDF4Driver not available", driverFound.isAvailable());
    }
}
