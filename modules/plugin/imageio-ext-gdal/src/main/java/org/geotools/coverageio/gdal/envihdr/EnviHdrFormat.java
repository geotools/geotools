/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.MismatchedDimensionException;

/**
 * @author Mathew Wyatt, CSIRO Australia
 * @author Daniele Romagnoli, GeoSolutions SAS
 */
public final class EnviHdrFormat extends BaseGDALGridFormat implements Format {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(EnviHdrFormat.class);

    /**
     * Constructor for the {@code BaseGDALGridFormat}. It is invoked by the underlying
     * implementations.
     *
     * @param spi the format specific {@code ImageReaderSpi} instance
     */
    public EnviHdrFormat() {
        super(new ENVIHdrImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new EnviHdrFormat.");
        }

        setInfo();
    }

    private static InfoWrapper INFO = new InfoWrapper("ENVIHdr Coverage Format", "ENVIHdr");

    /** Sets the metadata information. */
    protected void setInfo() {
        setInfo(INFO);
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    public EnviHdrReader getReader(Object source, Hints hints) {
        try {
            return new EnviHdrReader(source, hints);
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
