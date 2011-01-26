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

import java.util.logging.Logger;

import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * @author Mathew Wyatt, CSIRO Australia
 * @author Daniele Romagnoli, GeoSolutions.
 */
public final class EnviHdrReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private final static String worldFileExt = ".wld";
     /** Logger. */
    @SuppressWarnings("unused")
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio.gdal.envihdr");

    /**
     * Creates a new instance of a {@link EnviHdrReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build an
     *                {@link EnviHdrReader}.
     * @throws DataSourceException
     */
    public EnviHdrReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link EnviHdrReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build an
     *                {@link EnviHdrReader}.
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public EnviHdrReader(Object input, final Hints hints) throws DataSourceException {
        super(input, hints, worldFileExt, new ENVIHdrImageReaderSpi());
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new EnviHdrFormat();
    }
}
