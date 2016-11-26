/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverageio.gdal.vrt;

import it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Implementation of the {@link Format} service provider interface for VRT files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.10.x
 *
 *
 * @source $URL$
 */
public final class VRTFormatFactory extends BaseGridFormatFactorySPI
        implements GridFormatFactorySpi {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio.gdal.vrt");

    @Override
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi");
            available = new VRTImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) {
                    LOGGER.fine("VRTFormatFactory is availaible.");
                } else {
                    LOGGER.fine("VRTFormatFactory is not availaible.");
                }
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("VRTFormatFactory is not availaible.");
            }

            available = false;
        }

        return available;
    }

    @Override
    public VRTFormat createFormat() {
        return new VRTFormat();
    }
}
