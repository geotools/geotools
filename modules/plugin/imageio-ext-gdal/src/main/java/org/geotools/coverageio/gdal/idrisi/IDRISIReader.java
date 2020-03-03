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
import java.util.logging.Logger;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a RST data source and create a {@link GridCoverage2D} from the data.
 *
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class IDRISIReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    /** Logger. */
    @SuppressWarnings("unused")
    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(IDRISIReader.class);

    /**
     * Creates a new instance of a {@link IDRISIReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link IDRISIReader}.
     */
    public IDRISIReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link IDRISIReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link IDRISIReader}.
     * @param hints Hints to be used by this reader throughout his life.
     */
    public IDRISIReader(Object input, Hints hints) throws DataSourceException {
        super(input, hints, DEFAULT_WORLDFILE_EXT, new IDRISIImageReaderSpi());
    }

    /** @see org.opengis.coverage.grid.GridCoverageReader#getFormat() */
    public Format getFormat() {
        return new IDRISIFormat();
    }
}
