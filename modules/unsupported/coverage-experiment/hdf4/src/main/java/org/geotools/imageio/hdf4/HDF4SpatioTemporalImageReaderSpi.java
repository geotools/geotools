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
package org.geotools.imageio.hdf4;

import it.geosolutions.imageio.plugins.hdf4.HDF4ImageReaderSpi;
import it.geosolutions.imageio.plugins.hdf4.aps.HDF4APSImageReaderSpi;
import it.geosolutions.imageio.plugins.hdf4.terascan.HDF4TeraScanImageReaderSpi;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.imageio.ImageReader;

import org.geotools.imageio.SpatioTemporalImageReaderSpi;

/**
 * Implementation of {@link SpatioTemporalImageReaderSpi} to handle HDF4 files.
 * 
 * @author Daniele Romagnoli, GeoSolutions
 * @author Alessio Fabiani, GeoSolutions
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/hdf4/src/main/java/org/geotools/imageio/hdf4/HDF4SpatioTemporalImageReaderSpi.java $
 */
public class HDF4SpatioTemporalImageReaderSpi extends SpatioTemporalImageReaderSpi {

    enum HDF4_TYPE {APS, TeraScan, UNDEFINED};
	
    static final String[] suffixes = { "hdf", "hdf4"};

    static final String[] formatNames = { "hdf4", "HDF" };

    static final String[] mimeTypes = { "image/hdf4" };

    static final String version = "1.0";

    static final String readerCN = "org.geotools.coverage.io.hdf4.HDF4SpatioTemporalImageReader";

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
    
    public HDF4SpatioTemporalImageReaderSpi() {
    	this(null);
    }
    
    public HDF4SpatioTemporalImageReaderSpi(Object input) {
        super(
                vendorName,
                version,
                formatNames,
                suffixes,
                mimeTypes,
                readerCN, // readerClassName
                GEO_STANDARD_INPUT_TYPES,
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
        spi = lookupForSPI(input); 
        
    }

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

	private HDF4ImageReaderSpi spi;

    @Override
    public boolean canDecodeInput(Object source) throws IOException {
    	spi = lookupForSPI(source);
    	if (spi != null)
    		return true;
    	return false;
    }

    @Override
    public ImageReader createReaderInstance(Object extension) throws IOException {
        return new HDF4SpatioTemporalImageReader(spi);
    }

    @Override
    public String getDescription(Locale locale) {
        return spi.getDescription(locale);
    }

}
