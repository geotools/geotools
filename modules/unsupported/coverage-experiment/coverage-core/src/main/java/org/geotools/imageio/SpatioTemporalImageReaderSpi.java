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
package org.geotools.imageio;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

/**
 * Basic Service provider interface to instantiate
 * {@link SpatioTemporalImageReader}
 * 
 * @author Daniele Romagnoli, GeoSolutions
 */
public abstract class SpatioTemporalImageReaderSpi extends ImageReaderSpi {

    protected static final Class<?>[] GEO_STANDARD_INPUT_TYPES = new Class[] {
            ImageInputStream.class, File.class, URL.class, URI.class };

    protected static final Logger LOGGER = Logger
            .getLogger("org.geotools.imageio");

    protected static final String vendorName = "GeoSolutions";

    public SpatioTemporalImageReaderSpi(String vendorName, String version,
            String[] names, String[] suffixes, String[] MIMETypes,
            String readerClassName, Class<?>[] inputTypes,
            String[] writerSpiNames,
            boolean supportsStandardStreamMetadataFormat,
            String nativeStreamMetadataFormatName,
            String nativeStreamMetadataFormatClassName,
            String[] extraStreamMetadataFormatNames,
            String[] extraStreamMetadataFormatClassNames,
            boolean supportsStandardImageMetadataFormat,
            String nativeImageMetadataFormatName,
            String nativeImageMetadataFormatClassName,
            String[] extraImageMetadataFormatNames,
            String[] extraImageMetadataFormatClassNames) {
        super(vendorName, version, names, suffixes, MIMETypes, readerClassName,
                inputTypes, writerSpiNames,
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
    }
}
