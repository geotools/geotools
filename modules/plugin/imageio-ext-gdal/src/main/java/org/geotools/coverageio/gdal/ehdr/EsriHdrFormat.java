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
package org.geotools.coverageio.gdal.ehdr;

import it.geosolutions.imageio.plugins.ehdr.EsriHdrImageReaderSpi;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.api.coverage.grid.Format;
import org.geotools.api.data.DataSourceException;
import org.geotools.api.geometry.MismatchedDimensionException;
import org.geotools.coverageio.gdal.BaseGDALGridFormat;
import org.geotools.util.factory.Hints;

/**
 * An implementation of {@link Format} for the Esri Hdr format.
 *
 * @author Alexander Petkov, Fire Sciences Laboratory
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class EsriHdrFormat extends BaseGDALGridFormat implements Format {
    /** Logger. */
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(EsriHdrFormat.class);

    /** Creates an instance and sets the metadata. */
    public EsriHdrFormat() {
        super(new EsriHdrImageReaderSpi());

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("Creating a new EsriHdrFormat.");
        }

        setInfo();
    }

    private static InfoWrapper INFO = new InfoWrapper("EHdr Coverage Format", "EHdr");

    /** Sets the metadata information. */
    @Override
    protected void setInfo() {
        setInfo(INFO);
    }

    /** @see org.geotools.data.coverage.grid.AbstractGridFormat#getReader(Object, Hints) */
    @Override
    public EsriHdrReader getReader(Object source, Hints hints) {
        try {
            return new EsriHdrReader(source, hints);
        } catch (MismatchedDimensionException | DataSourceException e) {
            final RuntimeException re = new RuntimeException();
            re.initCause(e);
            throw re;
        }
    }
}
