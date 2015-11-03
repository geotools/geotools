/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2015, Open Source Geospatial Foundation (OSGeo)
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
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;

import org.geotools.data.DataUtilities;
import org.geotools.imageio.netcdf.utilities.NetCDFUtilities;
import org.geotools.util.logging.Logging;

import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDataset.Enhance;

/**
 * Service provider interface for the NetCDF Image
 * 
 * @author Alessio Fabiani, GeoSolutions
 */
public class NetCDFImageReaderSpi extends ImageReaderSpi {

    public static final Class< ? >[] STANDARD_INPUT_TYPES = new Class[]{AccessibleStream.class, ImageInputStream.class,
        File.class, URL.class, URI.class};

    public static final String VENDOR_NAME = "GeoTools";

    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(NetCDFImageReaderSpi.class);

    static final String[] suffixes;

    static final String[] formatNames;

    static final String[] MIMETypes;

    static final String version = "1.0";

    static final String readerCN = "it.geosolutions.imageio.plugins.netcdf.NetCDFImageReader";

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

    private static final String FORCE_OPEN_CHECK = "NETCDF_FORCE_OPEN_CHECK";

    /** 
     * There are some grib files which are mal-formed. 
     * As an instance they contain some trailing bytes 
     * before the magic number.
     * 
     * We may want to try to check if the NetCDF readers 
     * may access them anyway. NOTE that this behaviour
     * will be applied to any input dataset.
     */
    private static final boolean forceOpenCheck;

    static {
        NetcdfDataset.setDefaultEnhanceMode(EnumSet.of(Enhance.CoordSystems));

        // If Grib Library is available, then the GRIB extension must be added to support.
        // If NC4 C Library is available, then the proper MIME Types must be added to support.
        List<String> suffixesList = new ArrayList<String>();
        Collections.addAll(suffixesList, "nc", "NC");

        List<String> formatNamesList = new ArrayList<String>();
        Collections.addAll(formatNamesList, "netcdf", "NetCDF", NetCDFUtilities.NETCDF_3);

        List<String> mimeTypesList = new ArrayList<String>();
        Collections.addAll(mimeTypesList, NetCDFUtilities.NETCDF3_MIMETYPE, "image/netcdf", "image/x-netcdf",
                "image/x-nc");

        if (NetCDFUtilities.isGribAvailable()) {
            Collections.addAll(suffixesList, "grib", "grb", "grb2");
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
        forceOpenCheck = Boolean.getBoolean(FORCE_OPEN_CHECK);
    }

    
    /** Default Constructor * */
    public NetCDFImageReaderSpi() {
        super(VENDOR_NAME, version, formatNames, suffixes, MIMETypes, readerCN, STANDARD_INPUT_TYPES, wSN,
                supportsStandardStreamMetadataFormat, nativeStreamMetadataFormatName,
                nativeStreamMetadataFormatClassName, extraStreamMetadataFormatNames,
                extraStreamMetadataFormatClassNames, supportsStandardImageMetadataFormat,
                nativeImageMetadataFormatName, nativeImageMetadataFormatClassName,
                extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
        
        LOGGER.fine("NetCDFImageReaderSpi Constructor");
    }

    /**
     * @see javax.imageio.spi.ImageReaderSpi#createReaderInstance(java.lang.Object)
     */
    @Override
    public ImageReader createReaderInstance(Object extension)
            throws IOException {
        return new NetCDFImageReader(this);
    }

    /**
     * @see javax.imageio.spi.IIOServiceProvider#getDescription(java.util.Locale)
     */
    @Override
    public String getDescription(Locale locale) {
        return new StringBuffer("NetCDF-CF Image Reader, version ").append(
                version).toString();
    }

    @Override
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
        if (source instanceof URIImageInputStream) {
            URIImageInputStream uriInStream = (URIImageInputStream) source;
            try {
                // TODO perhaps it would be better to not make an online check. Might be slowing down.
                NetcdfDataset openDataset = NetcdfDataset.acquireDataset(uriInStream.getUri().toString(), null);
                openDataset.close();
                return true;
            } catch (IOException e) {
                return false;
            }
        }
        if (input != null) {
            NetcdfFile file = null;
            FileImageInputStream fis = null;
            try {

                // Checking Magic Number
                fis = new FileImageInputStream(input);
                byte[] b = new byte[4];
                fis.mark();
                fis.readFully(b);
                fis.reset();
                boolean cdfCheck = (b[0] == (byte)0x43 && b[1] == (byte)0x44 && b[2] == (byte)0x46);
                boolean hdf5Check = (b[0] == (byte)0x89 && b[1] == (byte)0x48 && b[2] == (byte)0x44);
                boolean gribCheck = (b[0] == (byte)0x47 && b[1] == (byte)0x52 && b[2] == (byte)0x49 && b[3] == (byte)0x42);

                // Check if the GRIB library is available
                gribCheck &= NetCDFUtilities.isGribAvailable();
                
                boolean isNetCDF = true;
                if (!cdfCheck && !hdf5Check && !gribCheck) {
                    if (!isNcML(input)) {
                        isNetCDF = false;
                    }
                }
                if (!isNetCDF && !forceOpenCheck) {
                    return false;
                }
                file = NetcdfDataset.acquireDataset(DataUtilities.fileToURL(input).toString(), null);
                if (file != null) {
                    if (LOGGER.isLoggable(Level.FINE))
                        LOGGER.fine("File successfully opened");
                    canDecode = true;
                }
            } catch (IOException ioe) {
                canDecode = false;
            } finally {
                if (fis != null) {
                    try {
                        fis.close();
                    } catch (Throwable t) {

                    }
                }

                if (file != null)
                    file.close();
            }

        }
        return canDecode;
    }

    private boolean isNcML(File input) throws IOException {
        final StreamSource streamSource = new StreamSource(input);
        XMLStreamReader reader = null;
        try {
            reader = XMLInputFactory.newInstance().createXMLStreamReader(streamSource);
            reader.nextTag();
            if ("netcdf".equals(reader.getName().getLocalPart())) {
                return true;
            }
        } catch (XMLStreamException e) {

        } catch (FactoryConfigurationError e) {

        } finally {
            if (reader != null) {
                if (streamSource.getInputStream() != null) {
                    streamSource.getInputStream().close();
                }
                try {
                    reader.close();
                } catch (XMLStreamException e) {
                }
            }

        }
        return false;
    }

}
