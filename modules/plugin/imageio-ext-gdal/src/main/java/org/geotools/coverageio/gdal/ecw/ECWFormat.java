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
package org.geotools.coverageio.gdal.ecw;

import it.geosolutions.imageio.plugins.ecw.ECWImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.geometry.MismatchedDimensionException;

/**
 * An implementation of {@link Format} for the ECW format.
 *
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class ECWFormat extends BaseGDALGridFormat implements Format {

    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(ECWFormat.class);

    /** Creates an instance and sets the metadata. */
    public ECWFormat() {
        super(new ECWImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new ECWFormat.");
        }

        setInfo();
    }

    private static InfoWrapper INFO = new InfoWrapper("ECW Coverage Format", "ECW");

    /** Sets the metadata information. */
    protected void setInfo() {
        setInfo(INFO);
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    public ECWReader getReader(Object source, Hints hints) {
        try {
            return new ECWReader(source, hints);
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
