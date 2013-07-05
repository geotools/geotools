/*
 *    ImageI/O-Ext - OpenSource Java Image translation Library
 *    http://www.geo-solutions.it/
 *    http://java.net/projects/imageio-ext/
 *    (C) 2007 - 2009, GeoSolutions
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
package org.geotools.imageio;

import it.geosolutions.imageio.stream.AccessibleStream;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.util.logging.Logging;

/**
 * Service provider interface for GeoSpatial aware {@link ImageReader} classes
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public abstract class GeoSpatialImageReaderSpi extends ImageReaderSpi {
    
    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(GeoSpatialImageReaderSpi.class);

    // //
    // StreamMetadataFormatNames and StreamMetadataFormatClassNames
    // //
    public static final boolean supportsStandardStreamMetadataFormat = false;

    public static final String nativeStreamMetadataFormatName = null;

    public static final String nativeStreamMetadataFormatClassName = null;

    // //
    // ImageMetadataFormatNames and ImageMetadataFormatClassNames
    // //
    public static final boolean supportsStandardImageMetadataFormat = false;

    public static final String nativeImageMetadataFormatName = null;

    public static final String nativeImageMetadataFormatClassName = null;

    public static final String VENDOR_NAME = "GeoTools";

    public static final Class< ? >[] STANDARD_INPUT_TYPES = new Class[]{AccessibleStream.class, ImageInputStream.class,
    File.class, URL.class, URI.class};   

    protected GeoSpatialImageReaderSpi(String version, String[] names,
            String[] suffixes, String[] MIMETypes, String readerClassName,
            String[] writerSpiNames,
            String nativeStreamMetadataFormatName, String nativeStreamMetadataFormatClassName,
            String[] extraStreamMetadataFormatNames, String[] extraStreamMetadataFormatClassNames,
            String[] extraImageMetadataFormatNames,
            String[] extraImageMetadataFormatClassNames) {
        super(VENDOR_NAME, version, names, suffixes, MIMETypes, readerClassName, STANDARD_INPUT_TYPES, writerSpiNames,
                supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat,
                GeoSpatialImageReaderSpi.nativeImageMetadataFormatName, GeoSpatialImageReaderSpi.nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);

        LOGGER.fine("NetCDFImageReaderSpi Constructor");        
    }
}
