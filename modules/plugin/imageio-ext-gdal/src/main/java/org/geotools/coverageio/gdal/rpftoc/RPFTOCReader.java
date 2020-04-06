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
package org.geotools.coverageio.gdal.rpftoc;

import it.geosolutions.imageio.plugins.rpftoc.RPFTOCImageReaderSpi;
import java.io.File;
import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.util.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a RPFTOC data source and create a {@link GridCoverage2D} from the data.
 *
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 */
public final class RPFTOCReader extends BaseGDALGridCoverage2DReader implements GridCoverageReader {
    private static final String worldFileExt = "";

    /**
     * Creates a new instance of a {@link RPFTOCReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link RPFTOCReader}.
     */
    public RPFTOCReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link RPFTOCReader}. I assume nothing about file extension.
     *
     * @param input Source object for which we want to build an {@link RPFTOCReader}.
     * @param hints Hints to be used by this reader throughout his life.
     */
    public RPFTOCReader(Object input, Hints hints) throws DataSourceException {
        super(input, hints, worldFileExt, new RPFTOCImageReaderSpi());
    }

    /** @see org.opengis.coverage.grid.GridCoverageReader#getFormat() */
    public Format getFormat() {
        return new RPFTOCFormat();
    }

    /**
     * Override coverage name because of the default structure of the folders. Very often
     * XXX/RPF/A.TOC The parent directory is a better alternative
     *
     * @return the coverage name
     */
    public String getCoverageName() {
        /*
         * Override coverage name because of the default structure of the folders. Very often XXX/RPF/A.TOC
         * The coverage name would be always A. The parent directory is a better alternative.
         */
        String retVal = "RPFTOC Coverage";
        if (!super.getCoverageName().equals("A")) {
            retVal = super.getCoverageName();
        } else {
            retVal = "RPFTOC Coverage";
            File oFile = super.getInputFile().getParentFile();
            while (oFile != null) {
                String sName = oFile.getName();
                if (!sName.toUpperCase().equals("RPF")) {
                    retVal = sName;
                    break;
                } else {
                    oFile = oFile.getParentFile();
                }
            }
        }
        return retVal;
    }
}
