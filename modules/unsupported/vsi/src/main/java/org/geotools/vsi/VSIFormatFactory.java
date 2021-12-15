/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2021, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.vsi;

import it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi;
import java.util.logging.Logger;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;
import org.geotools.coverageio.BaseGridFormatFactorySPI;

public final class VSIFormatFactory extends BaseGridFormatFactorySPI
        implements GridFormatFactorySpi {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(VSIFormatFactory.class);

    /**
     * Return true if all necessary dependencies are met in order to use this resource type
     *
     * @return true if all necessary dependencies are met in order to use this resource type
     */
    @Override
    public boolean isAvailable() {
        boolean available = true;

        try {
            Class.forName("it.geosolutions.imageio.plugins.vrt.VRTImageReaderSpi");
            available = new VRTImageReaderSpi().isAvailable();
        } catch (ClassNotFoundException cnf) {
            available = false;
        }

        LOGGER.fine("VSIFormatFactory is " + (available ? "available." : "not available."));

        return available;
    }

    /**
     * Create and return the corrsponding format object of this class
     *
     * @return VSIFormat object for this data resource type
     */
    @Override
    public VSIFormat createFormat() {
        return new VSIFormat();
    }
}
