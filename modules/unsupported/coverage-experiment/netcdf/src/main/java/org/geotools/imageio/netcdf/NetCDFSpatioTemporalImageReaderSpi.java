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
package org.geotools.imageio.netcdf;

import it.geosolutions.imageio.plugins.netcdf.NetCDFImageReaderSpi;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageReader;

import org.geotools.imageio.SpatioTemporalImageReaderSpi;

/**
 * Implementation of {@link SpatioTemporalImageReaderSpi} to handle NetCDF
 * files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Simone Giannecchini, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/netcdf/src/main/java/org/geotools/imageio/netcdf/NetCDFSpatioTemporalImageReaderSpi.java $
 */
public class NetCDFSpatioTemporalImageReaderSpi extends
        SpatioTemporalImageReaderSpi {

    static final String[] suffixes = { "netcdf", "nc" };

    static final String[] formatNames = { "NetCDF", "NCDF" };

    static final String[] mimeTypes = { "image/netcdf" };

    static final String version = "1.0";

    static final String readerCN = "org.geotools.imageio.netcdf.NetCDFSpatioTemporalImageReader";

    static final String vendorName = "GeoSolutions";

    // writerSpiNames
    static final String[] wSN = { null };

    // StreamMetadataFormatNames and StreamMetadataFormatClassNames
    static final boolean supportsStandardStreamMetadataFormat = false;

    static final String nativeStreamMetadataFormatName = null;

    static final String nativeStreamMetadataFormatClassName = null;

    static final String[] extraStreamMetadataFormatNames = { null };

    static final String[] extraStreamMetadataFormatClassNames = { null };

    // ImageMetadataFormatNames and ImageMetadataFormatClassNames
    static final boolean supportsStandardImageMetadataFormat = false;

    static final String nativeImageMetadataFormatName = null;

    static final String nativeImageMetadataFormatClassName = null;

    static final String[] extraImageMetadataFormatNames = { null };

    static final String[] extraImageMetadataFormatClassNames = { null };

    public NetCDFSpatioTemporalImageReaderSpi() {
        super(
                vendorName,
                version,
                formatNames,
                suffixes,
                mimeTypes,
                readerCN, // readerClassName
                GEO_STANDARD_INPUT_TYPES,
                wSN, // writer Spi Names
                supportsStandardStreamMetadataFormat,
                nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName,
                extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames,
                supportsStandardImageMetadataFormat,
                nativeImageMetadataFormatName,
                nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames,
                extraImageMetadataFormatClassNames);
        spi = new NetCDFImageReaderSpi();
    }

    private NetCDFImageReaderSpi spi;

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        return spi.canDecodeInput(source);
    }

    @Override
    public ImageReader createReaderInstance(Object extension)
            throws IOException {
        return new NetCDFSpatioTemporalImageReader(spi);
    }

    @Override
    public String getDescription(Locale locale) {
        return spi.getDescription(locale);
    }

}
