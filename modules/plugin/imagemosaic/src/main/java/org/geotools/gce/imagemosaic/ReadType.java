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
package org.geotools.gce.imagemosaic;

import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import javax.media.jai.RenderedOp;

import org.geotools.factory.Hints;
import org.geotools.resources.coverage.CoverageUtilities;
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;

import com.sun.media.jai.operator.ImageReadDescriptor;


/**
 * This enum can be used to distinguish between differet read methods, namely, JAI ImageRead based and Java2D direct read via ImageReader.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 * @author Stefan Alfons Krueger (alfonx), Wikisquare.de : Support for jar:file:foo.jar/bar.properties URLs
 *
 */
enum ReadType {


    DIRECT_READ{
    	
		@Override
		RenderedImage read(
				final ImageReadParam readParameters, 
				final int imageIndex,
				final URL granuleUrl, 
				final Rectangle rasterDimensions, 
				final ImageReader reader,
				final Hints hints,
				final boolean closeElements) {
	        //
    		// Using ImageReader to load the data directly
    		//
    		try{
                        //check source region
                        if(CoverageUtilities.checkEmptySourceRegion(readParameters, rasterDimensions)){
                            if (LOGGER.isLoggable(Level.WARNING))
                                LOGGER.warning("Provided an emptu source region to this read method ");                           
                            return null;                    
                        }
    
                        // checks on url
                        if(granuleUrl==null){
                            if (LOGGER.isLoggable(Level.WARNING))
                                LOGGER.warning("Provided a null URL to this read method ");                           
                            return null;                    
                        }
    	
    			if(reader==null){
    				if (LOGGER.isLoggable(Level.WARNING))
    					LOGGER.warning("Unable to get reader for URL " + granuleUrl);
    				return null;
    			}
    			
    			if (LOGGER.isLoggable(Level.FINER))
    			    LOGGER.log(Level.FINER, "reading file: " + granuleUrl);
    			
    			// read data
    			return reader.read(imageIndex,readParameters);
    		} catch (IOException e) {
    			if (LOGGER.isLoggable(Level.WARNING))
    				LOGGER.log(Level.WARNING,"Unable to compute source area for URL "
    						+ granuleUrl,e);	
    			return null;
    		} finally {
    			//close everything
    			try {
    				// reader
    				if(closeElements&&reader!=null)
    					reader.dispose();
    			} catch (Throwable t) {
    				// swallow the exception, we are just trying to close as much stuff as possible
    			} 
    			
						
    		}      
	}
    }, 
    
    
    JAI_IMAGEREAD{
		@Override
		RenderedImage read(
				final ImageReadParam readParameters, 
				final int imageIndex,
				final URL granuleUrl, 
				final Rectangle rasterDimensions, 
				final ImageReader reader,
				final Hints hints,
				final boolean closeElements) {


    		try{
				//check source regionepbjMosaic,
				if(CoverageUtilities.checkEmptySourceRegion(readParameters, rasterDimensions)){
                                    if (LOGGER.isLoggable(Level.WARNING))
                                        LOGGER.warning("Provided an emptu source region to this read method ");                           
                                    return null;                    
                                }

	                            // checks on url
	                        if(granuleUrl==null){
	                            if (LOGGER.isLoggable(Level.WARNING))
	                                LOGGER.warning("Provided a null URL to this read method ");                           
	                            return null;                    
	                        }
	                        
	                        if(reader==null){
	                                if (LOGGER.isLoggable(Level.WARNING))
	                                        LOGGER.warning("Unable to get reader for URL " + granuleUrl);
	                                return null;
	                        }
	                        
	                        // check input stream
	                        final ImageInputStream inStream=(ImageInputStream) reader.getInput();
				// read data    
				final RenderedOp raster = ImageReadDescriptor.create(
						inStream, 
						imageIndex,
						false,
						false, 
						false,
						null,
						null,
						readParameters,
						reader, 
						hints);
	
				if (raster != null)
					raster.getWidth();
				return raster;
    		}catch (Exception e) {
				if(LOGGER.isLoggable(Level.INFO))
					LOGGER.log(Level.INFO,e.getLocalizedMessage(),e);
				return null;
			}
		}
    }, 
    
    UNSPECIFIED{
		@Override
		RenderedImage read(
				final ImageReadParam readParameters, 
				final int imageIndex,
				final URL granuleUrl, 
				final Rectangle rasterDimensions, 
				final ImageReader reader,
				final Hints hints,
				final boolean closeElements) {
			throw new UnsupportedOperationException(Errors.format(ErrorKeys.UNSUPPORTED_OPERATION_$1,"read"));
		}
    };

    /** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging .getLogger(ReadType.class);
	
    /**
     * Default {@link ReadType}  enumeration.
     * 
     * <p>
     * We use the JAI ImageRead as the default type so that we can be sure that we can read very large mosaics with
     * deferred loading.
     * 
     * @return the default {@link ReadType}.
     */
    static ReadType getDefault() {
        return JAI_IMAGEREAD;
    }

        /**
         * Load the raster data from the underlying source with the specified read
         * type.
         * 
         * @param readParameters
         * @param imageIndex
         * @param rasterUrl
         * @param readDimension
         * @param hints {@link Hints} to control the read process
         *
         * @return a {@link RenderedImage} instance that matches the provided
         *         request parameters as close as possible.
         * 
         * @throws IOException
         *             in case something bad occurs during the decoding process.
         */
	abstract RenderedImage read(
			final ImageReadParam readParameters, 
			final int imageIndex,
			final URL granuleUrl, 
			final Rectangle rasterDimensions, 
			final ImageReader reader,
			final Hints hints,
			final boolean closeElements) ;
	
};
