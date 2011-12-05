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
package org.geotools.coverageio.gdal.idrisi;

import it.geosolutions.imageio.plugins.idrisi.IDRISIImageReaderSpi;

import java.awt.RenderingHints;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.opengis.coverage.grid.Format;


/**
 * Implementation of the {@link Format} service provider interface for IDRISI
 * files.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/trunk/modules/plugin/imageio-ext-gdal/src/main/java/org/geotools/coverageio/gdal/idrisi/IDRISIFormatFactory.java $
 */
public final class IDRISIFormatFactory implements GridFormatFactorySpi {
    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            IDRISIFormatFactory.class.toString());

    /**
     * Tells me if the coverage plugin to access EHdr is available or not.
     *
     * @return {@code true} if the plugin is available, {@code false} otherwise.
     */
    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("it.geosolutions.imageio.plugins.arcbinarygrid.ArcBinaryGridImageReaderSpi");
            available = new IDRISIImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available) 
                    LOGGER.fine("IDRISIFormatFactory is availaible.");
                else 
                    LOGGER.fine("IDRISIFormatFactory is not availaible.");
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE)) 
                LOGGER.fine("IDRISIFormatFactory is not availaible.");

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link IDRISIFormat}
     *
     * @return A {@link IDRISIFormat}
     */
    public IDRISIFormat createFormat() {
        return new IDRISIFormat();
    }

    /**
     * Returns the implementation hints. The default implementation returns en
     * empty map.
     */
    public Map<RenderingHints.Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
}
