/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2016, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.stream.AccessibleStream;
import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;
import it.geosolutions.imageio.stream.input.URIImageInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.ImageInputStream;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities.FileFormat;
import org.geotools.util.logging.Logging;

/**
 * Service provider interface for the NetCDF Image
 *
 * @author Alessio Fabiani, GeoSolutions
 */
public class NetCDFImageReaderSpi extends ImageReaderSpi {

    public static final Class<?>[] STANDARD_INPUT_TYPES =
            new Class[] {
                AccessibleStream.class, ImageInputStream.class, File.class, URL.class, URI.class
            };

    public static final String VENDOR_NAME = "GeoTools";

    /**
     * Number of bytes at the start of a file to search for a GRIB signature. Some GRIB files have
     * WMO headers prepended by a telecommunications gateway. NetCDF-Java Grib{1,2}RecordScanner
     * look for the header in this many bytes.
     */
    private static final int GRIB_SEARCH_BYTES = 16000;

    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(NetCDFImageReaderSpi.class);

    static final String[] suffixes;

    static final String[] formatNames;

    static final String[] MIMETypes;

    static final String version = "1.0";

    static final String readerCN = "it.geosolutions.imageio.plugins.netcdf.NetCDFImageReader";

    // writerSpiNames
    static final String[] wSN = {null};

    // StreamMetadataFormatNames and StreamMetadataFormatClassNames
    static final boolean supportsStandardStreamMetadataFormat = false;

    static final String nativeStreamMetadataFormatName = null;

    static final String nativeStreamMetadataFormatClassName = null;

    static final String[] extraStreamMetadataFormatNames = {null};

    static final String[] extraStreamMetadataFormatClassNames = {null};

    // ImageMetadataFormatNames and ImageMetadataFormatClassNames
    static final boolean supportsStandardImageMetadataFormat = false;

    static final String nativeImageMetadataFormatName = null;

    static final String nativeImageMetadataFormatClassName = null;

    static final String[] extraImageMetadataFormatNames = {null};

    static final String[] extraImageMetadataFormatClassNames = {null};

    static {
        // If Grib Library is available, then the GRIB extension must be added to support.
        // If NC4 C Library is available, then the proper MIME Types must be added to support.
        List<String> suffixesList = new ArrayList<String>();
        Collections.addAll(suffixesList, "nc", "NC");
        Collections.addAll(suffixesList, "ncml", "NCML");

        List<String> formatNamesList = new ArrayList<String>();
        Collections.addAll(formatNamesList, "netcdf", "NetCDF", NetCDFUtilities.NETCDF_3);

        List<String> mimeTypesList = new ArrayList<String>();
        Collections.addAll(
                mimeTypesList,
                NetCDFUtilities.NETCDF3_MIMETYPE,
                "image/netcdf",
                "image/x-netcdf",
                "image/x-nc");

        if (NetCDFUtilities.isGribAvailable()) {
            Collections.addAll(suffixesList, "grib", "grib2", "grb", "grb2");
            Collections.addAll(formatNamesList, "grib", "grib2", "GRIB", "GRIB2");
            Collections.addAll(mimeTypesList, "application/octet-stream");
        }
        if (NetCDFUtilities.isNC4CAvailable()) {
            Collections.addAll(formatNamesList, NetCDFUtilities.NETCDF_4C, "NetCDF-4");
            Collections.addAll(mimeTypesList, NetCDFUtilities.NETCDF4_MIMETYPE);
        }

        suffixes = suffixesList.toArray(new String[suffixesList.size()]);
        formatNames = formatNamesList.toArray(new String[formatNamesList.size()]);
        MIMETypes = mimeTypesList.toArray(new String[mimeTypesList.size()]);
    }

    /** Default Constructor * */
    public NetCDFImageReaderSpi() {
        super(
                VENDOR_NAME,
                version,
                formatNames,
                suffixes,
                MIMETypes,
                readerCN,
                STANDARD_INPUT_TYPES,
                wSN,
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

        LOGGER.fine("NetCDFImageReaderSpi Constructor");
    }

    /** @see javax.imageio.spi.ImageReaderSpi#createReaderInstance(java.lang.Object) */
    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new NetCDFImageReader(this);
    }

    /** @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale) */
    @Override
    public String getDescription(Locale locale) {
        return new StringBuffer("NetCDF-CF Image Reader, version ").append(version).toString();
    }

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
        URI input = null;
        if (source instanceof URI) {
            input = (URI) source;
        } else if (source instanceof File) {
            input = ((File) source).toURI();
        } else if (source instanceof FileImageInputStreamExtImpl) {
            input = ((FileImageInputStreamExtImpl) source).getFile().toURI();
            if (LOGGER.isLoggable(Level.FINE)) {
                LOGGER.fine("Found a valid FileImageInputStream");
            }
        } else if (source instanceof URIImageInputStream) {
            input = ((URIImageInputStream) source).getUri();
        }

        if (input != null) {
            return NetCDFUtilities.getFormat(input) != FileFormat.NONE;
        } else {
            return false;
        }
    }
}
