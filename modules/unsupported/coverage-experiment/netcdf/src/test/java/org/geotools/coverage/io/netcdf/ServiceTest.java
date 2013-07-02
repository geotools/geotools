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
package org.geotools.coverage.io.netcdf;

import java.util.Set;

import junit.framework.Assert;

import org.geotools.coverage.io.Driver;
import org.geotools.coverage.io.impl.CoverageIO;
import org.junit.Test;
import org.opengis.referencing.FactoryException;
import org.opengis.referencing.NoSuchAuthorityCodeException;

/**
 * Class for testing availability of NetCDF format factory
 * 
 * @author Simone Giannecchini
 * @author Daniele Romagnoli
 *
 * @source $URL$
 */
public class ServiceTest extends Assert {

	@Test
    public void isAvailable() throws NoSuchAuthorityCodeException,
            FactoryException {
        CoverageIO.scanForPlugins();
        Set<Driver> drivers = CoverageIO.getAvailableDrivers();
        Driver driverFound = null;
        for (Driver driver: drivers)
            if (driver instanceof NetCDFDriver) {
                driverFound = driver;
                break;
        }
        assertTrue("NetCDFDriver not registered", driverFound!=null);
        assertTrue("NetCDFDriver not available", driverFound.isAvailable());
    }
}
