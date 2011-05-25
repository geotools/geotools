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
package org.geotools.coverageio.gdal.erdasimg;

import it.geosolutions.imageio.plugins.erdasimg.ErdasImgImageReaderSpi;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;
import org.opengis.coverage.grid.Format;


/**
 * Implementation of the {@link Format} service provider interface for ERDAS
 * Imagine files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL$
 */
public final class ErdasImgFormatFactory extends BaseGridFormatFactorySPI implements GridFormatFactorySpi {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.coverageio.gdal.erdasimg");

    /**
     * Tells me if the coverage plugin to access Erdas imagine is available or not.
     *
     * @return {@code true} if the plugin is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("it.geosolutions.imageio.plugins.erdasimg.ErdasImgImageReaderSpi");
            available = new ErdasImgImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) 
                    LOGGER.fine("ErdasImgFormatFactory is availaible.");
                else
                    LOGGER.fine("ErdasImgFormatFactory is not availaible.");
               
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.fine("ErdasImgFormatFactory is not availaible.");
            
            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link ErdasImgFormat}
     *
     * @return A {@link ErdasImgFormat}
     */
    public ErdasImgFormat createFormat() {
        return new ErdasImgFormat();
    }
}
