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
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a VRT data source and create a {@link GridCoverage2D} from the data.
 *
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @author Ugo Moschini, GeoSolutions
 * @since 2.10.x
 */
public final class VRTReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private static final String worldFileExt = "";

    /**
     * Creates a new instance of a {@link VRTReader}.
     *
     * @param input Source object for which we want to build an {@link VRTReader}.
     */
    public VRTReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link VRTReader}.
     *
     * @param input Source object for which we want to build an {@link VRTReader}.
     * @param hints Hints to be used by this reader throughout its life.
     */
    public VRTReader(Object input, Hints hints) throws DataSourceException {
        super(input, hints, worldFileExt, new VRTImageReaderSpi());
    }

    @Override
    public Format getFormat() {
        return new VRTFormat();
    }
}
