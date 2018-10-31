/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2018, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.srp;

import it.geosolutions.imageio.plugins.rpftoc.RPFTOCImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;

/**
 * Implementation of the {@link Format} service provider interface for SRP (ASRP/USPR) files.
 *
 * @author Andrea Aime, GeoSolutions
 */
public final class SRPFormatFactory extends BaseGridFormatFactorySPI
        implements GridFormatFactorySpi {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SRPFormatFactory.class);

    /**
     * Tells me if the coverage plugin to access Erdas imagine is available or not.
     *
     * @return <code>true</code> if the plugin is available, <code>false</code> otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("it.geosolutions.imageio.plugins.srp.SRPImageReaderSpi");
            available = new RPFTOCImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("SRPFormatFactory is available.");
                } else {
                    LOGGER.fine("SRPFormatFactory is not available.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("SRPFormatFactory is not available.");
            }

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link SRPFormat}
     *
     * @return A {@link SRPFormat}
     */
    public SRPFormat createFormat() {
        return new SRPFormat();
    }
}
