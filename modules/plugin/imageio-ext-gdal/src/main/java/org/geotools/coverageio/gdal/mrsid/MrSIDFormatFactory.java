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
package org.geotools.coverageio.gdal.mrsid;

import it.geosolutions.imageio.plugins.mrsid.MrSIDImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;

/**
 * Implementation of the {@link Format} service provider interface for MrSID files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class MrSIDFormatFactory extends BaseGridFormatFactorySPI
        implements GridFormatFactorySpi {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(MrSIDFormatFactory.class);

    /**
     * Tells me if the coverage plugin to access MrSID is available or not.
     *
     * @return {@code true} if the plugin is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {

            Class.forName("it.geosolutions.imageio.plugins.mrsid.MrSIDImageReaderSpi");
            available = new MrSIDImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("MrSIDFormatFactory is available.");
                } else {
                    LOGGER.fine("MrSIDFormatFactory is not available.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("MrSIDFormatFactory is not available.");
            }

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link MrSIDFormat}
     *
     * @return A {@link MrSIDFormat}
     */
    public MrSIDFormat createFormat() {
        return new MrSIDFormat();
    }
}
