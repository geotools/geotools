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
package org.geotools.coverageio.gdal.jp2kak;

import it.geosolutions.imageio.plugins.jp2kakadu.JP2GDALKakaduImageReaderSpi;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;


/**
 * This class can read a JP2K data source and create a {@link GridCoverage2D}
 * from the data.
 *
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL$
 */
public final class JP2KReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private final static String worldFileExt = ".j2w";

    /**
     * Creates a new instance of a {@link JP2KReader}. I assume nothing about
     * file extension.
     *
     * @param input
     *            Source object for which we want to build an JP2KReader.
     * @throws DataSourceException
     */
    public JP2KReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link JP2KReader} basing the decision on
     * whether the file is compressed or not. I assume nothing about file
     * extension.
     *
     * @param input
     *            Source object for which we want to build an {@link JP2KReader}.
     * @param hints
     *            Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public JP2KReader(Object input, final Hints hints)
        throws DataSourceException {
        super(input, hints, worldFileExt, new JP2GDALKakaduImageReaderSpi());
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new JP2KFormat();
    }
}
