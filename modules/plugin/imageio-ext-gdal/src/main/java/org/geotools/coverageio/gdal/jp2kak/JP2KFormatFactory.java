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
package org.geotools.coverageio.gdal.jp2kak;

import it.geosolutions.imageio.plugins.jp2kakadu.JP2GDALKakaduImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.coverage.grid.Format;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;

/**
 * Implementation of the {@link Format} service provider interface for JP2K files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class JP2KFormatFactory extends BaseGridFormatFactorySPI implements GridFormatFactorySpi {
    /** Logger. */
    private static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(JP2KFormatFactory.class);

    /**
     * Tells me if the coverage plugin to access JP2K is available or not.
     *
     * @return <code>true</code> if the plugin is available, <code>false</code> otherwise.
     */
    @Override
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {

            Class.forName("it.geosolutions.imageio.plugins.jp2kakadu.JP2GDALKakaduImageReaderSpi");
            available = new JP2GDALKakaduImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("JP2KFormatFactory is available.");
                } else {
                    LOGGER.fine("JP2KFormatFactory is not available.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("JP2KFormatFactory is not available.");
            }

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link JP2KFormat}.
     *
     * @return A {@link JP2KFormat}.;
     */
    @Override
    public JP2KFormat createFormat() {
        return new JP2KFormat();
    }
}
