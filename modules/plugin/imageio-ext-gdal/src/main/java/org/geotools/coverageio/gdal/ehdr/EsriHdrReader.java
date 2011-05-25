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
package org.geotools.coverageio.gdal.ehdr;

import it.geosolutions.imageio.plugins.ehdr.EsriHdrImageReaderSpi;

import java.util.logging.Logger;

import org.geotools.coverage.grid.GridCoverage2D;
import org.geotools.coverageio.gdal.BaseGDALGridCoverage2DReader;
import org.geotools.data.DataSourceException;
import org.geotools.factory.Hints;
import org.opengis.coverage.grid.Format;
import org.opengis.coverage.grid.GridCoverageReader;

/**
 * This class can read a EHdr data source and create a {@link GridCoverage2D}
 * from the data.
 * 
 * @author Alexander Petkov, Fire Sciences Laboratory
 * @author Daniele Romagnoli, GeoSolutions.
 * @author Simone Giannecchini (simboss), GeoSolutions
 * @since 2.5.x
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/plugin/imageio-ext-gdal/src/main/java/org/geotools/coverageio/gdal/ehdr/EsriHdrReader.java $
 */
public final class EsriHdrReader extends BaseGDALGridCoverage2DReader implements
        GridCoverageReader {
    /** Logger. */
    @SuppressWarnings("unused")
    private final static Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger("org.geotools.coverageio.gdal.ehdr");

    private final static String worldFileExt = ".wld";

    /**
     * Creates a new instance of a {@link EsriHdrReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build an
     *                {@link EsriHdrReader}.
     * @throws DataSourceException
     */
    public EsriHdrReader(Object input) throws DataSourceException {
        this(input, null);
    }

    /**
     * Creates a new instance of a {@link EsriHdrReader}. I assume nothing about
     * file extension.
     * 
     * @param input
     *                Source object for which we want to build an
     *                {@link EsriHdrReader}.
     * @param hints
     *                Hints to be used by this reader throughout his life.
     * @throws DataSourceException
     */
    public EsriHdrReader(Object input, Hints hints) throws DataSourceException {
        super(input, hints, worldFileExt, new EsriHdrImageReaderSpi());
    }

    /**
     * @see org.opengis.coverage.grid.GridCoverageReader#getFormat()
     */
    public Format getFormat() {
        return new EsriHdrFormat();
    }
}
