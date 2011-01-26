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

import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.image.RenderedImage;
import java.io.IOException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.spi.ImageReaderSpi;
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
    	
    			if(reader==null)
    			{
    				if (LOGGER.isLoggable(Level.WARNING))
    					LOGGER.warning("Unable to get reader for URL " + granuleUrl);
    				return null;
    			}
    			
    			
    			//check source regione
    			if(CoverageUtilities.checkEmptySourceRegion(readParameters, rasterDimensions))
    				return null;
    			
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
		
    	RenderedImage read(
    			final ImageReadParam readP,
    			final int imageIndex, 
    			final URL rasterURL,
    			final Rectangle readDimension,
    			final ImageReaderSpi spi,
    			final Hints hints
    			)throws IOException{
    		//
    		// Using ImageReader to load the data directly
    		//
    		ImageInputStream inStream=null;
    		ImageReader reader=null;
    		try{
    			inStream = Utils.getInputStream(rasterURL);
    			if(inStream==null)
    				return null;
    	
    			reader=spi.createReaderInstance();
    			if(reader==null)
    			{
    				if (LOGGER.isLoggable(Level.WARNING))
    					LOGGER.warning("Unable to get reader for URL " + rasterURL);
    				return null;
    			}
    			
    			inStream.reset();
    			reader.setInput(inStream);
    			
    			//check source regione
    			if(CoverageUtilities.checkEmptySourceRegion(readP, readDimension))
    				return null;
    			
    			if (LOGGER.isLoggable(Level.FINER))
    			    LOGGER.log(Level.FINER, "reading file: " + rasterURL);
    			
    			// read data
    			return reader.read(imageIndex,readP);
    		} catch (IOException e) {
    			if (LOGGER.isLoggable(Level.WARNING))
    				LOGGER.log(Level.WARNING,"Unable to compute source area for URL "
    						+ rasterURL,e);	
    			return null;
    		} finally {
    			//close everything
    			try {
    				// reader
    				reader.dispose();
    			} catch (Throwable t) {
    				// swallow the exception, we are just trying to close as much stuff as possible
    			} 
    			
    			try {
    				// instream
    				inStream.close();
    			} catch (Throwable t) {
    				// swallow the exception, we are just trying to close as much stuff as possible
    			} 							
    		}        		
    	}
    }, 
    
    
    JAI_IMAGEREAD{
    	@Override
    	RenderedImage read(
    			final ImageReadParam readP,
    			final int imageIndex, 
    			final URL rasterUrl,
    			final Rectangle readDimension,
    			final ImageReaderSpi spi,
    			final Hints hints
    			) throws IOException{
    			
			//check source regionepbjMosaic,
			if(CoverageUtilities.checkEmptySourceRegion(readP, readDimension))
				return null;
			

    		
			// read data    	
//			final ParameterBlock pbjImageRead = new ParameterBlock();
//			pbjImageRead.add(Utils.getInputStream(rasterUrl));
//			pbjImageRead.add(imageIndex);
//			pbjImageRead.add(false);
//			pbjImageRead.add(false);
//			pbjImageRead.add(false);
//			pbjImageRead.add(null);
//			pbjImageRead.add(null);
//			pbjImageRead.add(readP);
//			pbjImageRead.add(spi.createReaderInstance());
			final RenderedOp raster;
//			if(tileDimension != null){
//			if (hints != null){    
				//build a proper layout
//				final ImageLayout layout = new ImageLayout();
//				layout.setTileWidth(tileDimension.width).setTileHeight(tileDimension.height);
//				raster = JAI.create("ImageRead", pbjImageRead,new RenderingHints(JAI.KEY_IMAGE_LAYOUT,layout));
//			    raster = JAI.create("ImageRead", pbjImageRead, hints);
			    raster = ImageReadDescriptor.create(
			    		Utils.getInputStream(rasterUrl), 
			    		imageIndex, 
			    		false, 
			    		false, 
			    		false,
			    		null, 
			    		null, 
			    		readP, 
			    		spi.createReaderInstance(),
			    		hints);
//			}
//			else
//				raster = JAI.create("ImageRead", pbjImageRead);
			//force rendering (a-la JAI)
			if (raster != null)
				raster.getWidth();
			return raster;
    	}

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
				if(CoverageUtilities.checkEmptySourceRegion(readParameters, rasterDimensions))
					return null;
	    			
				// read data    
				final RenderedOp raster = ImageReadDescriptor.create(
						Utils.getInputStream(granuleUrl), 
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
    		}catch (IOException e) {
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
    	@Override
		RenderedImage read(
    			final ImageReadParam readP,
    			final int imageIndex, 
    			final URL rasterUrl,
    			final Rectangle readDimension,
    			final ImageReaderSpi spi,
    			final Hints hints
    			)throws IOException{
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
	 * @param tileDimension
	 *            a {@link Dimension} object that can be used to suggest specific
	 *            tile dimension for the raster to load. It can be <code>null</code>.
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
			final URL rasterUrl,
			final Rectangle readDimension, 
			final ImageReaderSpi spi,
			final Hints hints
			) throws IOException;

	abstract RenderedImage read(
			final ImageReadParam readParameters, 
			final int imageIndex,
			final URL granuleUrl, 
			final Rectangle rasterDimensions, 
			final ImageReader reader,
			final Hints hints,
			final boolean closeElements) ;
	
};
