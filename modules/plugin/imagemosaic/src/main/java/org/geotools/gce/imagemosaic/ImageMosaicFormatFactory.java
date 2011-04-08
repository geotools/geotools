/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2006-2008, Open Source Geospatial Foundation (OSGeo)
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

import it.geosolutions.imageio.utilities.ImageIOUtilities;

import java.awt.RenderingHints;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.spi.ImageReaderSpi;

import org.geotools.coverage.grid.io.AbstractGridFormat;
import org.geotools.coverage.grid.io.GridFormatFactorySpi;

import com.sun.media.imageioimpl.common.PackageUtil;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderCodecLibSpi;
import com.sun.media.imageioimpl.plugins.jpeg2000.J2KImageReaderSpi;
import com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi;

/**
 * Implementation of the GridCoverageFormat service provider interface for
 * mosaic of georeferenced images.
 * 
 * @author Simone Giannecchini, GeoSolutions S.A.S.
 * @since 2.3
 */
public final class ImageMosaicFormatFactory implements GridFormatFactorySpi {
        
	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(ImageMosaicFormatFactory.class);
	
	private static final String KAKADU_SPI = "it.geosolutions.imageio.plugins.jp2k.JP2KKakaduImageReaderSpi";
	
	private static final String GDAL_SPI = "it.geosolutions.imageio.gdalframework.GDALImageReaderSpi";
	
	private static final String GDAL_JP2ECW_SPI = "it.geosolutions.imageio.plugins.jp2ecw.JP2GDALEcwImageReaderSpi";
	
	private static final String GDAL_JP2MrSID_SPI = "it.geosolutions.imageio.plugins.jp2mrsid.JP2GDALMrSidImageReaderSpi";
	
        private static final String GDAL_JP2KAKADU_SPI = "it.geosolutions.imageio.plugins.jp2kakadu.JP2GDALKakaduImageReaderSpi";

	static {
	    
		replaceTIFF();
		
		if(hasJP2Kakadu()){
			replaceJP2Kakadu();
		}
		
		else{
			if(hasJP2GDALECW()){
				replaceECW();
			}
			if(hasJP2GDALMRSID()){
				replaceMRSID();
			}
			if(hasJP2GDALKakadu()){
				replaceGDALKakadu();
			}
		}
		
	}
	/**
	 * Tells me if this plugin will work on not given the actual installation.
	 * 
	 * <p>
	 * Dependecies are mostly from JAI and ImageIO so if they are installed you
	 * should not have many problems.
	 * 
	 * @return False if something's missing, true otherwise.
	 */
	public boolean isAvailable() {
		boolean available = true;

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.
		try {
			Class.forName("javax.media.jai.JAI");
			Class.forName("com.sun.media.jai.operator.ImageReadDescriptor");
		} catch (ClassNotFoundException cnf) {
			available = false;
		}

		return available;
	}


	private static boolean hasJP2Kakadu() {
		try{
			@SuppressWarnings("unused")
			Class<?> cl = Class.forName(KAKADU_SPI);
			Class<?> utilityClass = Class.forName("it.geosolutions.util.KakaduUtilities");
			final Method method = utilityClass.getDeclaredMethod("isKakaduAvailable", (Class[])null);
			if (method != null){
				Boolean isAvailable = (Boolean) method.invoke(null, null);
				return isAvailable.booleanValue();
			}
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} catch (SecurityException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} catch (NoSuchMethodException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} catch (IllegalArgumentException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} catch (IllegalAccessException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} catch (InvocationTargetException e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, "Unable to load kakadu JPEG2000 reader spi",e);
		} 
		return false;
	}

	private static void replaceJP2Kakadu() {
		try{
			Class.forName(KAKADU_SPI);

			// imageio kakJP2 reader
			final String imageioJ2KImageReaderCodecName=J2KImageReaderCodecLibSpi.class.getName();
			
			if(PackageUtil.isCodecLibAvailable()){
				boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, KAKADU_SPI, imageioJ2KImageReaderCodecName, "JPEG 2000");
	        	if(!succeeded)
	        		if (LOGGER.isLoggable(Level.WARNING))
	        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+KAKADU_SPI+":"+imageioJ2KImageReaderCodecName);	
			}
        	
			// imageio kakJP2 reader
			final String imageioJ2KImageReaderName=J2KImageReaderSpi.class.getName();
			
			final boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, KAKADU_SPI, imageioJ2KImageReaderName, "JPEG 2000");
        	if(!succeeded)
        		if (LOGGER.isLoggable(Level.WARNING))
        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+KAKADU_SPI+":"+imageioJ2KImageReaderName);	
	        
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, "Unable to load specific JPEG2000 reader spi",e);
		} 
	}

	private static void replaceTIFF() {
		try{
			//check if our tiff plugin is in the path
			final String customTiffName=it.geosolutions.imageioimpl.plugins.tiff.TIFFImageReaderSpi.class.getName();
			Class.forName(customTiffName);

			// imageio tiff reader
			final String imageioTiffName=TIFFImageReaderSpi.class.getName();
			
			final boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, customTiffName, imageioTiffName, "tiff");
        	if(!succeeded)
        		if (LOGGER.isLoggable(Level.WARNING))
        			LOGGER.warning("Unable to set ordering between tiff readers spi");	
	        
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING,"Unable to load specific TIFF reader spi",e);
		} 
		
	}

	private static boolean hasJP2GDALKakadu() {
	    try{
                @SuppressWarnings("unused")
                Class<?> cl = Class.forName(GDAL_JP2KAKADU_SPI);
                Class<?> cGdal = Class.forName(GDAL_SPI);
                Object jp2Kak = cl.newInstance(); 
                final Method method = cGdal.getDeclaredMethod("isAvailable", (Class[])null);
                if (method != null){
                        Boolean isAvailable = (Boolean) method.invoke(jp2Kak, null);
                        return isAvailable.booleanValue();
                    }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (SecurityException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (NoSuchMethodException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (IllegalArgumentException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (IllegalAccessException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (InvocationTargetException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            } catch (InstantiationException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 Kakadu Reader SPI", e);
            }
            return false;
        }

	private static void replaceGDALKakadu() {
		try{
			//check if our kakJP2 plugin is in the path

			Class.forName(GDAL_JP2KAKADU_SPI);

			// imageio kakJP2 reader
			final String imageioJ2KImageReaderCodecName=J2KImageReaderCodecLibSpi.class.getName();
			
			if(PackageUtil.isCodecLibAvailable()){
				boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2KAKADU_SPI, imageioJ2KImageReaderCodecName, "JPEG 2000");
	        	if(!succeeded)
	        		if (LOGGER.isLoggable(Level.WARNING))
	        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2KAKADU_SPI+":"+imageioJ2KImageReaderCodecName);	
			}
        	
			// imageio kakJP2 reader
			final String imageioJ2KImageReaderName=J2KImageReaderSpi.class.getName();
			
			
			final boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2KAKADU_SPI, imageioJ2KImageReaderName, "JPEG 2000");
        	if(!succeeded)
        		if (LOGGER.isLoggable(Level.WARNING))
        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2KAKADU_SPI+":"+imageioJ2KImageReaderName);	
	        
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, "Unable to load specific JPEG2000 reader spi",e);
		} 
		
	}

	private static void replaceMRSID() {
		try{
			//check if our mrsidJP2 plugin is in the path
			Class.forName(GDAL_JP2MrSID_SPI );

			// imageio tiff reader
			final String imageioJ2KImageReaderCodecName=J2KImageReaderCodecLibSpi.class.getName();
			
			if(PackageUtil.isCodecLibAvailable()){
				boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2MrSID_SPI , imageioJ2KImageReaderCodecName, "JPEG 2000");
	        	if(!succeeded)
	        		if (LOGGER.isLoggable(Level.WARNING))
	        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2MrSID_SPI +":"+imageioJ2KImageReaderCodecName);	
			}
        	
			// imageio mrsidJP2 reader
			final String imageioJ2KImageReaderName=J2KImageReaderSpi.class.getName();
			
			
			final boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2MrSID_SPI , imageioJ2KImageReaderName, "JPEG 2000");
        	if(!succeeded)
        		if (LOGGER.isLoggable(Level.WARNING))
        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2MrSID_SPI +":"+imageioJ2KImageReaderName);	
	        
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, "Unable to load specific JPEG2000 reader spi",e);
		} 
		
	}

	private static boolean hasJP2GDALMRSID() {
	    try{
                @SuppressWarnings("unused")
                Class<?> cl = Class.forName(GDAL_JP2MrSID_SPI);
                Class<?> cGdal = Class.forName(GDAL_SPI);
                Object jp2MrSid = cl.newInstance(); 
                final Method method = cGdal.getDeclaredMethod("isAvailable", (Class[])null);
                if (method != null){
                        Boolean isAvailable = (Boolean) method.invoke(jp2MrSid, null);
                        return isAvailable.booleanValue();
                    }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (SecurityException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (NoSuchMethodException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (IllegalArgumentException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (IllegalAccessException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (InvocationTargetException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            } catch (InstantiationException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 MrSID Reader SPI", e);
            }
            return false;
        }

	private static void replaceECW() {
		try{
			//check if our ecwJP2 plugin is in the path
			Class.forName(GDAL_JP2ECW_SPI);

			// imageio ecwJP2 reader
			final String imageioJ2KImageReaderCodecName=J2KImageReaderCodecLibSpi.class.getName();
			
			if(PackageUtil.isCodecLibAvailable()){
				boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2ECW_SPI, imageioJ2KImageReaderCodecName, "JPEG 2000");
	        	if(!succeeded)
	        		if (LOGGER.isLoggable(Level.WARNING))
	        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2ECW_SPI+":"+imageioJ2KImageReaderCodecName);	
			}
        	
			// imageio ecwJP2 reader
			final String imageioJ2KImageReaderName=J2KImageReaderSpi.class.getName();
			
			final boolean succeeded=ImageIOUtilities.replaceProvider(ImageReaderSpi.class, GDAL_JP2ECW_SPI, imageioJ2KImageReaderName, "JPEG 2000");
        	if(!succeeded)
        		if (LOGGER.isLoggable(Level.WARNING))
        			LOGGER.warning("Unable to set ordering between jp2 readers spi-"+GDAL_JP2ECW_SPI+":"+imageioJ2KImageReaderName);	
	        
		} catch (ClassNotFoundException e) {
			if (LOGGER.isLoggable(Level.WARNING))
				LOGGER.log(Level.WARNING, "Unable to load specific JPEG2000 reader spi",e);
		} 
		
	}

	private static boolean hasJP2GDALECW() {
	    try{
                @SuppressWarnings("unused")
                Class<?> cl = Class.forName(GDAL_JP2ECW_SPI);
                Class<?> cGdal = Class.forName(GDAL_SPI);
                Object jp2ecwSPI = cl.newInstance(); 
                final Method method = cGdal.getDeclaredMethod("isAvailable", (Class[])null);
                if (method != null){
                        Boolean isAvailable = (Boolean) method.invoke(jp2ecwSPI, null);
                        return isAvailable.booleanValue();
                    }
            } catch (ClassNotFoundException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (SecurityException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (NoSuchMethodException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (IllegalArgumentException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (IllegalAccessException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (InvocationTargetException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            } catch (InstantiationException e) {
                if (LOGGER.isLoggable(Level.FINE))
                    LOGGER.log(Level.FINE, "Unable to load GDAL JP2 ECW Reader SPI", e);
            }
            return false;
    	}

	/**
	 * @see GridFormatFactorySpi#createFormat().
	 */
	public AbstractGridFormat createFormat() {
		return new ImageMosaicFormat();
	}
	
	/**
	 * Returns the implementation hints. The default implementation returns an
	 * empty map.
	 * 
	 * @return An empty map.
	 */
	public Map<RenderingHints.Key, ?> getImplementationHints() {
		return Collections.emptyMap();
	}
}
