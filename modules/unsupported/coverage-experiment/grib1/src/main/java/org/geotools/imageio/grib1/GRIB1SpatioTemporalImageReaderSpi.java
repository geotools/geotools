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
package org.geotools.imageio.grib1;

import it.geosolutions.imageio.plugins.grib1.GRIB1ImageReaderSpi;

import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageReader;

import org.geotools.imageio.SpatioTemporalImageReaderSpi;

/**
 * Implementation of {@link SpatioTemporalImageReaderSpi} to handle HDF-AVHRR
 * files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/grib1/src/main/java/org/geotools/imageio/grib1/GRIB1SpatioTemporalImageReaderSpi.java $
 */
public class GRIB1SpatioTemporalImageReaderSpi extends
        SpatioTemporalImageReaderSpi {

    static final String[] suffixes = { "grb", "grib1", "bin" };

    static final String[] formatNames = { "GRIB-1", "GRIB" };

    static final String[] mimeTypes = { "image/grib-1" };

    static final String version = "1.0";

    static final String readerCN = "org.geotools.coverage.io.grib1.GRIB1SpatioTemporalImageReader";

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

    public GRIB1SpatioTemporalImageReaderSpi() {
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
        spi = new GRIB1ImageReaderSpi();
    }

    private GRIB1ImageReaderSpi spi;

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        return spi.canDecodeInput(source);
    }

    @Override
    public ImageReader createReaderInstance(Object extension)
            throws IOException {
        return new GRIB1SpatioTemporalImageReader(spi);
    }

    @Override
    public String getDescription(Locale locale) {
        return spi.getDescription(locale);
    }

}
