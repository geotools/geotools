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
package org.geotools.imageio.unidata;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;
import it.geosolutions.imageio.stream.input.URIImageInputStream;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.stream.FileImageInputStream;

import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;


import org.geotools.imageio.GeoSpatialImageReaderSpi;
import org.geotools.imageio.unidata.utilities.UnidataUtilities;
import org.geotools.util.logging.Logging;

import ucar.nc2.NetcdfFile;
import ucar.nc2.dataset.NetcdfDataset;

/**
 * Service provider interface for the NetCDF Image
 *
 * @author Alessio Fabiani, GeoSolutions
 */
public abstract class UnidataImageReaderSpi extends GeoSpatialImageReaderSpi {

    /**
     * @param version
     * @param names
     * @param suffixes
     * @param MIMETypes
     * @param readerClassName
     * @param writerSpiNames
     * @param nativeStreamMetadataFormatName
     * @param nativeStreamMetadataFormatClassName
     * @param extraStreamMetadataFormatNames
     * @param extraStreamMetadataFormatClassNames
     * @param nativeImageMetadataFormatName
     * @param nativeImageMetadataFormatClassName
     * @param extraImageMetadataFormatNames
     * @param extraImageMetadataFormatClassNames
     */
    protected UnidataImageReaderSpi(String version, String[] names, String[] suffixes,
            String[] MIMETypes, String readerClassName, String[] writerSpiNames,
            String nativeStreamMetadataFormatName, String nativeStreamMetadataFormatClassName,
            String[] extraStreamMetadataFormatNames, String[] extraStreamMetadataFormatClassNames,
            String[] extraImageMetadataFormatNames, String[] extraImageMetadataFormatClassNames) {
        super(version, names, suffixes, MIMETypes, readerClassName, writerSpiNames,
                nativeStreamMetadataFormatName, nativeStreamMetadataFormatClassName,
                extraStreamMetadataFormatNames, extraStreamMetadataFormatClassNames,
                extraImageMetadataFormatNames, extraImageMetadataFormatClassNames);
    }

    /** Default Logger * */
    private static final Logger LOGGER = Logging.getLogger(UnidataImageReaderSpi.class);

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
                NetcdfDataset openDataset = NetcdfDataset.openDataset(uriInStream.getUri().toString());
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
                gribCheck &= UnidataUtilities.isGribAvailable();
                
                boolean isNetCDF = true;
                if (!cdfCheck && !hdf5Check && !gribCheck) {
                    if (!isNcML(input)) {
                        isNetCDF = false;
                    }
                }
                if (!isNetCDF) {
                    return false;
                }
                file = NetcdfDataset.openDataset(input.getPath());
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
