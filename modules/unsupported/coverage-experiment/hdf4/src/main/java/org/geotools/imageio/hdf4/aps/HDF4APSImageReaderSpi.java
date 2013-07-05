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
package org.geotools.imageio.hdf4.aps;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReader;

import org.geotools.imageio.hdf4.HDF4ImageReaderSpi;
import org.geotools.imageio.netcdf.NetCDFUtilities;

import ucar.nc2.dataset.NetcdfDataset;

/**
 * Service provider interface for the APS-HDF Image
 * 
 * @author Daniele Romagnoli
 */
public class HDF4APSImageReaderSpi extends HDF4ImageReaderSpi {

    private static final Logger LOGGER = Logger.getLogger(HDF4APSImageReaderSpi.class.toString());

    static final String readerCN = "it.geosolutions.imageio.plugins.hdf4.HDF4APSImageReader";

    public HDF4APSImageReaderSpi() {
        super(readerCN);

        if (LOGGER.isLoggable(Level.FINE))
            LOGGER.fine("HDF4TeraScanImageReaderSpi Constructor");
    }

    protected boolean isValidDataset(final NetcdfDataset dataset) {
    	
    	boolean found = false;
    	// APS
    	final String attrib = NetCDFUtilities.getGlobalAttributeAsString(dataset, HDF4APSProperties.STD_FA_CREATESOFTWARE); 
        if (attrib != null && attrib.length()>0 && attrib.startsWith("APS"))
        	found = true;
        return found;
	}

	public ImageReader createReaderInstance(Object input) throws IOException {
        return new HDF4APSImageReader(this);
    }
}
