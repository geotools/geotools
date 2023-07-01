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
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;

/**
 * An implementation of {@link Format} for the VRT format
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.10.x
 */
public final class VRTFormat extends BaseGDALGridFormat implements Format {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(VRTFormat.class);

    /** Creates an instance and sets the metadata. */
    public VRTFormat() {
        super(new VRTImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new VRTFormat.");
        }

        setInfo();
    }

    private static InfoWrapper INFO = new InfoWrapper("Virtual (VRT) Format", "VRT");

    @Override
    protected void setInfo() {
        setInfo(INFO);
    }

    @Override
    public VRTReader getReader(Object source, Hints hints) {
        try {
            return new VRTReader(source, hints);
        } catch (MismatchedDimensionException | DataSourceException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
}
