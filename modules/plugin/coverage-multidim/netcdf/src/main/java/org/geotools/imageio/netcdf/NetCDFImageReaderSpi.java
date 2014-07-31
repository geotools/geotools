/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2014, Open Source Geospatial Foundation (OSGeo)
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
import java.util.EnumSet;
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

    static {
         NetcdfDataset.setDefaultEnhanceMode(EnumSet.of(Enhance.CoordSystems));
    }

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
 

    static{
        // If Grib Library is available, then the GRIB extension must be added to support
        if(NetCDFUtilities.isGribAvailable()){
            suffixes  = new String[]{ "nc", "NC", "grib", "grb", "grb2" };
            formatNames = new String[]{ "netcdf", "NetCDF", "grib", "grib2", "GRIB", "GRIB2" };
            MIMETypes = new String[]{ "application/x-netcdf", "image/netcdf", "image/x-netcdf", "image/x-nc" , "application/octet-stream" };
        }else{
            suffixes  = new String[]{ "nc", "NC" };
            formatNames = new String[]{ "netcdf", "NetCDF" };
            MIMETypes = new String[]{ "application/x-netcdf", "image/netcdf", "image/x-netcdf", "image/x-nc" };
        }
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
                if (!isNetCDF) {
                    return false;
                }
                file = NetcdfDataset.acquireDataset(input.getPath(), null);
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
