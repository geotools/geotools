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
package org.geotools.imageio.unidata;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;
import it.geosolutions.imageio.stream.input.URIImageInputStream;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.geotools.imageio.GeoSpatialImageReaderSpi;
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
            try {
                file = NetcdfDataset.open(input.getPath());
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

}
