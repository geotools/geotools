/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2014 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.io.grib;

import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import ucar.nc2.grib.GribIndexCache;
import ucar.nc2.util.DiskCache2;

/**
 * Helper class used for setting a GRIB cache if defined with the JAVA argument -DGRIB_CACHE_DIR
 *
 * @author Nicola Lagomarsini GeoSolutions S.A.S.
 */
public class GribUtilities {

    /** The LOGGER for this class. */
    private static final Logger LOGGER =
            Logger.getLogger("org.geotools.coverage.io.grib.GribUtilities");

    /** String associated to the grib cache directory property */
    public static final String GRIB_CACHE_DIR = "GRIB_CACHE_DIR";

    /** Static initialization of the GRIB cache directory if set as JAVA argument */
    static {
        final Object cacheDir = System.getProperty(GRIB_CACHE_DIR);
        if (cacheDir != null) {
            String dir = (String) cacheDir;
            final File file = new File(dir);
            if (isValid(file)) {
                DiskCache2 cache = new DiskCache2(dir, false, 0, 0);
                cache.setAlwaysUseCache(true);
                GribIndexCache.setDiskCache2(cache);
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Setting the GribIndexCache folder to: " + dir);
                }
            }
        }
    }

    /** Method for checking if the input file is an existing writable directory. */
    public static boolean isValid(File file) {
        String dir = file.getAbsolutePath();
        if (!file.exists()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "The specified path doesn't refer "
                                + "to an existing folder. Please check the path: "
                                + dir);
            }
            return false;
        } else if (!file.isDirectory()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "The specified path doesn't refer "
                                + "to a directory. Please check the path: "
                                + dir);
            }
            return false;
        } else if (!file.canWrite()) {
            if (LOGGER.isLoggable(Level.WARNING)) {
                LOGGER.warning(
                        "The specified path refers to "
                                + "a directory which can't be written. Please check the path and"
                                + " the permissions for: "
                                + dir);
            }
            return false;
        }
        return true;
    }
}
