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
package org.geotools.coverageio.gdal.jp2ecw;

import it.geosolutions.imageio.plugins.jp2ecw.JP2GDALEcwImageReaderSpi;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;

/**
 * Implementation of the {@link Format} service provider interface for JP2K
 * files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL$
 */
public final class JP2ECWFormatFactory extends BaseGridFormatFactorySPI implements GridFormatFactorySpi {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio.gdal.jp2ecw");

    /**
     * Tells me if the coverage plugin to access JP2K via ECW driver is
     * available or not.
     * 
     * @return {@code true} if the plugin is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class
                    .forName("it.geosolutions.imageio.plugins.jp2ecw.JP2GDALEcwImageReaderSpi");
            available = new JP2GDALEcwImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("JP2ECWFormatFactory is availaible.");
                } else {
                    LOGGER.fine("JP2ECWFormatFactory is not availaible.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("JP2ECWFormatFactory is not availaible.");
            }

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link JP2ECWFormat}.
     * 
     * @return A {@link JP2ECWFormat}.;
     */
    public JP2ECWFormat createFormat() {
        return new JP2ECWFormat();
    }
}
