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
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.MismatchedDimensionException;

/**
 * An implementation of {@link Format} for the JP2K format based on the ECW driver.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
// See https://github.com/geosolutions-it/imageio-ext/issues/148
public final class JP2ECWFormat extends BaseGDALGridFormat implements Format {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(JP2ECWFormat.class);

    /** Creates an instance and sets the metadata. */
    public JP2ECWFormat() {
        super(new JP2GDALEcwImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new JP2KFormat.");
        }

        setInfo();
    }

    private static InfoWrapper INFO = new InfoWrapper("JP2K (ECW) Coverage Format", "JP2ECW");

    /** Sets the metadata information. */
    protected void setInfo() {
        setInfo(INFO);
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    public JP2ECWReader getReader(Object source, Hints hints) {
        try {
            return new JP2ECWReader(source, hints);
        } catch (MismatchedDimensionException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        } catch (DataSourceException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
}
