/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2024, Open Source Geospatial Foundation (OSGeo)
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

import org.junit.Assert;
import org.junit.BeforeClass;

public class NetCDFBaseTest extends Assert {

    @BeforeClass
    public static void init() {
        System.setProperty("user.timezone", "GMT");
        System.setProperty("netcdf.coordinates.enablePlugins", "true");
        System.setProperty("org.geotools.coverage.io.netcdf.cachefile", "true");
        System.setProperty("org.geotools.coverage.io.netcdf.memorymap", "true");
        // We are hard limiting the mapped byte buffer to validate that it's
        // capable of reading files bigger than the max size of the memory
        // mapped byte buffer
        System.setProperty("org.geotools.coverage.io.netcdf.memorymaplimit", "65536");
    }
}
