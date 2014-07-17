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
package org.geotools.imageio.hdf4;

import it.geosolutions.imageio.stream.input.FileImageInputStreamExtImpl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;

import org.geotools.imageio.hdf4.aps.HDF4APSImageReaderSpi;
import org.geotools.imageio.hdf4.terascan.HDF4TeraScanImageReaderSpi;
import org.geotools.imageio.netcdf.NetCDFUtilities;
import org.geotools.imageio.unidata.UnidataImageReaderSpi;

import ucar.nc2.dataset.NetcdfDataset;
import ucar.nc2.dataset.NetcdfDataset.Enhance;
import ucar.nc2.iosp.hdf4.H4iosp;

/**
 * Service provider interface for the APS-HDF Image
 * 
 * @author Daniele Romagnoli
 */
public abstract class HDF4ImageReaderSpi extends UnidataImageReaderSpi {

	static{
		NetcdfDataset.setDefaultEnhanceMode(EnumSet.of(Enhance.CoordSystems));
	}
	
    public enum HDF4_TYPE {APS, TeraScan, UNDEFINED};

    private static final Logger LOGGER = Logger.getLogger(HDF4ImageReaderSpi.class.toString());

    static final String[] suffixes = { "hdf", "hdf4" };

    static final String[] formatNames = { "HDF4" };

    static final String[] MIMETypes = { "image/hdf4" };

    static final String version = "1.0";

    static final String readerCN = "it.geosolutions.imageio.plugins.hdf4.BaseHDF4ImageReader";

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

    static final List<HDF4ImageReaderSpi> SPIs = new ArrayList<HDF4ImageReaderSpi>(2);
    
    static{
        SPIs.add(new HDF4APSImageReaderSpi());
        SPIs.add(new HDF4TeraScanImageReaderSpi());
    }
    
    public HDF4ImageReaderSpi() {
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

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("HDF4TeraScanImageReaderSpi Constructor");
    }

    public HDF4ImageReaderSpi(final String readerCN) {
    	super(vendorName,
                version,
                formatNames,
                suffixes,
                MIMETypes,
                readerCN, 
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

    	spi = lookupForSPI(readerCN); 
	}
    
    private HDF4ImageReaderSpi spi;
    
    private HDF4ImageReaderSpi lookupForSPI(Object input) {
        for (HDF4ImageReaderSpi testingSpi: SPIs){
           try {
               if (testingSpi.canDecodeInput(input)){
                   return testingSpi;
               }
           } catch (IOException e) {
               return null;
           }
        }
        return null;
   }
    
	public boolean canDecodeInput(Object input) throws IOException {
        boolean found = false;
        
        if (input instanceof FileImageInputStreamExtImpl) {
            input = ((FileImageInputStreamExtImpl) input).getFile();
        }
        // check if this is an 
        if (input instanceof File) {
        	// open up as a netcdf dataset
            final NetcdfDataset dataset = NetCDFUtilities.getDataset(input);            
            if (dataset != null) {
            	
            	// first of all is it an HDF4??
            	// TODO change this when we will be allowed to use >= 4.0.46
            	if(!(dataset.getIosp() instanceof H4iosp))
            		return false;
            	
            	// now, check if we can read it
            	try{
	            	found = isValidDataset(dataset);
            	}
            	finally {
            		try{
            			dataset.close();
            		}
            		catch (Throwable e) {
						if(LOGGER.isLoggable(Level.FINE))
							LOGGER.log(Level.FINE,e.getLocalizedMessage(),e);
					}
            	}
        	}
        }
        return found;
    }

    protected boolean isValidDataset(final NetcdfDataset dataset) {
		return false;
	}

	public ImageReader createReaderInstance(Object input) throws IOException {
        return new HDF4ImageReaderProxy(spi);
    }

    public String getDescription( Locale locale ) {
        // return "HDF4 Image Reader, version " + version;
        return spi.getDescription(locale);
    }
}
