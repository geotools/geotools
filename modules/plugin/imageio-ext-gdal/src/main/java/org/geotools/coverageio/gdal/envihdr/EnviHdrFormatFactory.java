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
package org.geotools.coverageio.gdal.envihdr;

import it.geosolutions.imageio.plugins.envihdr.ENVIHdrImageReaderSpi;

import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;

/**
 * @author Mathew Wyatt, CSIRO Australia
 * @author Daniele Romagnoli, GeoSolutions SAS
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imageio-ext-gdal/src/main/java/org/geotools/coverageio/gdal/envihdr/EnviHdrFormatFactory.java $
 */
public class EnviHdrFormatFactory extends BaseGridFormatFactorySPI implements GridFormatFactorySpi  {

    /** Logger. */
    private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(
            "org.geotools.coverageio.gdal.envihdr");

    public boolean isAvailable() {
        boolean available = true;

        // if these classes are here, then the runtime environment has
        // access to JAI and the JAI ImageI/O toolbox.
        try {
            Class.forName("it.geosolutions.imageio.plugins.envihdr.ENVIHdrImageReaderSpi");
            available = new ENVIHdrImageReaderSpi().isAvailable();

            if (LOGGER.isLoggable(Level.FINE)) {
                if (available)
                    LOGGER.fine("EnviHdrFormatFactory is availaible.");
                else
                    LOGGER.fine("EnviHdrFormatFactory is not availaible.");
            }
        } catch (ClassNotFoundException cnf) {
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("EnviHdrFormatFactory is not availaible.");

            available = false;
        }

        return available;
    }

    /**
     * Creating a {@link EnviHdrFormat}
     *
     * @return A {@link EnviHdrFormat}
     */
    public EnviHdrFormat createFormat() {
        return new EnviHdrFormat();
    }

}
