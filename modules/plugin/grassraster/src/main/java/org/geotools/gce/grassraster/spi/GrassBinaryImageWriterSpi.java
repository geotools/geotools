/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2011, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.gce.grassraster.spi;

import java.io.File;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageTypeSpecifier;
import javax.imageio.ImageWriter;
import javax.imageio.spi.ImageWriterSpi;

import org.geotools.gce.grassraster.GrassBinaryImageReader;
import org.geotools.gce.grassraster.GrassBinaryImageWriter;

/**
 * The Service Provider Interface for GRASS binary rasters.
 * 
 * @author Andrea Antonello (www.hydrologis.com)
 * @since 3.0
 * @see GrassBinaryImageWriter
 * @see GrassBinaryImageReader
 * @see GrassBinaryImageWriterSpi
 */
@SuppressWarnings("nls")
public class GrassBinaryImageWriterSpi extends ImageWriterSpi {
    private static final String[] suffixes = {""};
    private static final String[] formatNames = {"grass", "GRASS", "grassbin",
            "GRASS binary raster"};
    private static final String[] MIMETypes = {"image/grass"};
    private static final String version = "1.0";
    private static final String vendorName = "www.hydrologis.com";

    /**
     * the class name of the image writer.
     */
    private static final String writerCN = "eu.hydrologis.jgrass.grassbinary.imageio.io.GrassBinaryImageWriter";

    /**
     * the outputTypes handled by the {@link GrassBinaryImageWriter}.
     */
    private static final Class< ? >[] outputTypes = new Class[]{File.class};

    /**
     * the readerSpiName
     */
    private static final String[] rSN = {"eu.hydrologis.jgrass.grassbinary.imageio.io.GrassBinaryImageReaderSpi"};

    /**
     * the flag for stream metadata support.
     */
    private static final boolean supportsStandardStreamMetadataFormat = false;

    private static final String nativeStreamMetadataFormatName = null;
    private static final String nativeStreamMetadataFormatClassName = null;
    private static final String[] extraStreamMetadataFormatNames = null;
    private static final String[] extraStreamMetadataFormatClassNames = null;

    /**
     * the flag for image metadata support.
     */
    private static final boolean supportsStandardImageMetadataFormat = false;

    private static final String nativeImageMetadataFormatName = "eu.hydrologis.jgrass.grassbinary.imageio.metadata.GrassBinaryImageMetadata_1.0";
    private static final String nativeImageMetadataFormatClassName = "eu.hydrologis.jgrass.grassbinary.imageio.metadata.GrassBinaryImageMetadataFormat";
    private static final String[] extraImageMetadataFormatNames = {null};
    private static final String[] extraImageMetadataFormatClassNames = {null};

    /**
     * default constructor for the service provider interface.
     */
    public GrassBinaryImageWriterSpi() {
        super(vendorName, version, formatNames, suffixes, MIMETypes, writerCN, outputTypes, rSN,
                supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat,
                nativeImageMetadataFormatName, nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);

    }

    public boolean canEncodeImage( ImageTypeSpecifier its ) {
        // TODO what has to be done here?
        return true;
    }

    public ImageWriter createWriterInstance( Object extension ) throws IOException {
        return new GrassBinaryImageWriter(this, null);
    }

    public String getDescription( Locale locale ) {
        return "GRASS binary raster image writer service provider interface, version " + version;
    }
}
