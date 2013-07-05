/*
 *    JImageIO-extension - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    (C) 2007, GeoSolutions
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    either version 3 of the License, or (at your option) any later version.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.imageio.grib1;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

import java.io.File;
import java.io.IOException;
import java.util.Locale;
import java.util.logging.Level;

import javax.imageio.ImageReader;

import org.geotools.imageio.unidata.UnidataImageReaderSpi;

import ucar.nc2.NetcdfFile;

/**
 * Service provider interface for the GRIB1 Image
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
public class GRIB1ImageReaderSpi extends UnidataImageReaderSpi {
    static final String[] suffixes = { "grb", "grib1", "bin" };

    static final String[] formatNames = { "GRIB-1", "GRIB" };

    static final String[] MIMETypes = { "image/grib-1", "image/grib", "image/grb"  };

    static final String version = "1.0";

    static final String readerCN = "it.geosolutions.imageio.plugins.grib1.GRIB1ImageReader";

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

    public GRIB1ImageReaderSpi() {
        super(
                vendorName,
                version,
                formatNames,
                suffixes,
                MIMETypes,
                readerCN, // readerClassName
                STANDARD_INPUT_TYPES,
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

        if (LOGGER.isLoggable(Level.FINE)) {
            LOGGER.fine("GRIB1ImageReaderSpi Constructor");
        }
    }

    public boolean canDecodeInput(Object source) throws IOException {
        boolean canDecode = false;
        File input = null;
        if (source instanceof FileImageInputStreamExtImpl) {
            input = ((FileImageInputStreamExtImpl) source).getFile();
            if (LOGGER.isLoggable(Level.FINE))
                LOGGER.fine("Found a valid FileImageInputStream");
        }

        if (source instanceof File) {
            input = (File) source;
        }
        if (input != null) {
            NetcdfFile file = null;
            try {
                file = NetcdfFile.open(input.getPath());
                if (file != null) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("File successfully opened");
                    canDecode = true;
                }
            } catch (IOException ioe) {
                canDecode = false;
            } finally {
                if (file != null)
                    file.close();
            }

        }
        return canDecode;
    }

    public ImageReader createReaderInstance(Object input) throws IOException {
        return new GRIB1ImageReader(this);
    }

    public String getDescription(Locale locale) {
        return "GRIB1 Image Reader, version " + version;
    }

}
