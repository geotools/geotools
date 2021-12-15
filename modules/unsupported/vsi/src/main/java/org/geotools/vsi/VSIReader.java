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
import java.io.IOException;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a VSI data source and create a {@link GridCoverage2D} from the data.
 *
 * @author Matthew Northcott, Catalyst IT Ltd.
 */
public final class VSIReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private static final String WORLD_FILE_EXT = "";

    /**
     * Creates a new instance of a {@link GridCoverageReader}.
     *
     * @param input Source object for which we want to build an {@link VSIReader}.
     * @throws IOException
     */
    public VSIReader(Object input) throws IOException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link GridCoverageReader}.
     *
     * @param input Source object for which we want to build an {@link VSIReader}.
     * @param hints Hints to be used by this reader throughout its life.
     * @throws IOException
     */
    public VSIReader(Object input, Hints hints) throws IOException {
        super(
                VSIDataset.fromObject(input).getFile(),
                hints,
                WORLD_FILE_EXT,
                new VRTImageReaderSpi());
    }

    /**
     * Return the corresponding format object for this resource
     *
     * @return an instance of the VSIFormat object that corresponds to this resource
     */
    @Override
    public Format getFormat() {
        return new VSIFormat();
    }
}
