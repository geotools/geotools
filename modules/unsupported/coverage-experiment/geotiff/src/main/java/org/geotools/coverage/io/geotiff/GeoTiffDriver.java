/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2005-2008, Open Source Geospatial Foundation (OSGeo)
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
/*
 * NOTICE OF RELEASE TO THE PUBLIC DOMAIN
 *
 * This work was created by employees of the USDA Forest Service's
 * Fire Science Lab for internal use.  It is therefore ineligible for
 * copyright under title 17, section 105 of the United States Code.  You
 * may treat it as you would treat any public domain work: it may be used,
 * changed, copied, or redistributed, with or without permission of the
 * authors, for free or for compensation.  You may not claim exclusive
 * ownership of this code because it is already owned by everyone.  Use this
 * software entirely at your own risk.  No warranty of any kind is given. 
 *
 * A copy of 17-USC-105 should have accompanied this distribution in the file
 * 17USC105.html.  If not, you may access the law via the US Government's
 * public websites:
 *   - http://www.copyright.gov/title17/92chap1.html#105
 *   - http://www.gpoaccess.gov/uscode/  (enter "17USC105" in the search box.)
 */
package org.geotools.coverage.io.geotiff;



import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.imageio.spi.ImageReaderSpi;
import javax.imageio.spi.ImageWriterSpi;
import javax.imageio.stream.ImageInputStream;

import org.geotools.coverage.io.CoverageAccess;
import org.geotools.coverage.io.driver.DefaultFileDriver;
import org.geotools.coverage.io.driver.FileDriver;
import org.geotools.data.Parameter;
import org.geotools.factory.Hints;
import org.opengis.util.ProgressListener;

/**
 * A driver for the GeoTIFF format.
 * 
 * @author Simone Giannecchini, GeoSolutions
 * @author Jody Garnett
 *
 * @source $URL: http://svn.osgeo.org/geotools/branches/2.7.x/build/maven/javadoc/../../../modules/unsupported/coverage-experiment/geotiff/src/main/java/org/geotools/coverage/io/geotiff/GeoTiffDriver.java $
 */
public class GeoTiffDriver extends DefaultFileDriver implements FileDriver {

	/** Logger. */
	private final static Logger LOGGER = org.geotools.util.logging.Logging.getLogger(GeoTiffDriver.class);

	/** {@link Set} of supported extensions for tiff world files. */
	final static Set<String> TIFF_WORLD_FILE_EXT;
	final static boolean JAIAvailable;
	final static boolean TiffAvailable;
	static ImageReaderSpi readerSpi;
	static ImageWriterSpi writerSpi;
	static {
		// check if we have JAI and or ImageIO

		// if these classes are here, then the runtine environment has
		// access to JAI and the JAI ImageI/O toolbox.
		boolean available = true;
		try {
			Class.forName("javax.media.jai.JAI");
		} catch (Throwable e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			available = false;
		}
		JAIAvailable = available;

		available = true;
		try {
			Class<?> clazz = Class
					.forName("com.sun.media.imageioimpl.plugins.tiff.TIFFImageReaderSpi");
			readerSpi = (ImageReaderSpi) clazz.newInstance();
			Class<?> clazz1 = Class
					.forName("com.sun.media.imageioimpl.plugins.tiff.TIFFImageWriterSpi");
			writerSpi = (ImageWriterSpi) clazz1.newInstance();

		} catch (Throwable e) {
			if (LOGGER.isLoggable(Level.FINE))
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			readerSpi = null;
			writerSpi = null;
			available = false;
		}
		TiffAvailable = available;

		final HashSet<String> tempSet = new HashSet<String>(2);
		tempSet.add(".tfw");
		tempSet.add(".tiffw");
		tempSet.add(".wld");
		TIFF_WORLD_FILE_EXT = Collections.unmodifiableSet(tempSet);
	}
	
	public GeoTiffDriver() {
		this(null);
	}

	/**
	 * Creates a new instance of GeoTiffAccessFactory.
	 */
	public GeoTiffDriver(Hints hints) {
		super("GeoTIFF",
				"Tagged Image File Format with Geographic information",
				"Tagged Image File Format with Geographic information", hints, Arrays.asList("tiff", "tif"));
	}

	/**
	 * Informs the caller whether the libraries required by the GeoTiff reader
	 * are installed or not.
	 * 
	 * @return availability of the GeoTiff format.
	 */
	public boolean isAvailable() {
		return TiffAvailable;
	}
	
	@Override
	protected boolean canConnect(URL url,Map<String, Serializable> params) {
		if (url == null) {
			return false;
		}
		ImageInputStream inputStream = null;
		Object source = null;
		try {
			// /////////////////////////////////////////////////////////////
			//
			// URL management
			// In case the URL points to a file we need to get to the file
			// directly and avoid caching. In case it points to http or ftp
			// or it is an open stream we have very small to do and we need
			// to enable caching.
			//
			// /////////////////////////////////////////////////////////////
			if (url.getProtocol().equalsIgnoreCase("file")) {
				File file = urlToFile(url);
				if( file.exists() && file.canRead() && file.isFile()){
					// setting source
					source = file;
				}
				else {
					return false;
				}
			}
			else if (url.getProtocol().equalsIgnoreCase("http")
						|| url.getProtocol().equalsIgnoreCase("ftp")) {
				source = url.openStream();
			} else {
				return false;
			}

			// get a stream
			inputStream = (ImageInputStream) ((source instanceof ImageInputStream) ? source
					: ImageIO.createImageInputStream(source));
			if (inputStream == null) {
				if (LOGGER.isLoggable(Level.FINE))
					LOGGER.fine("Unable to get an ImageInputStream");
				return false;
			}
			// get a reader and check if it is a geotiff
			inputStream.mark();

			// tiff
			if (!readerSpi.canDecodeInput(inputStream)){
				return false;
			}
			return true;
		} catch (Throwable e) {
			if (LOGGER.isLoggable(Level.FINE)){
				LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
			}
			return false;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (Exception e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
				}
			}
			if (source != null && (source instanceof InputStream)) {
				try {
					((InputStream) source).close();
				} catch (Exception e) {
					if (LOGGER.isLoggable(Level.FINE))
						LOGGER.log(Level.FINE, e.getLocalizedMessage(), e);
				}
			}
		}
    }
    
    
	@Override
	protected CoverageAccess connect(URL source, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException {
    	final GeoTiffAccess retValue=  new GeoTiffAccess(this, source, params, hints, listener, false);
    	return retValue;
    }


	@Override
    protected CoverageAccess create(URL source, Map<String, Serializable> params,
            Hints hints, ProgressListener listener) throws IOException {
        return  new GeoTiffAccess(this, source, params, hints, listener, true);
    }
	

	/**
	 * GeoTiffDriver supports the creation of new files.
	 */
	@Override
	protected boolean canCreate(URL url,Map<String, Serializable> params) {
		File file = toFile( url );
		if( file == null ){
			return false; // not a file
		}
		// check if we are trying to create a new geotiff
		if (file.exists()) {
			return false;
		} else {
			// if it does not exist let's see if we could ever create it
			// the best way to check I came up with has been
			final File parent = file.getParentFile();
			return parent != null && parent.isDirectory() && parent.canWrite();
		}
	}
	
	/**
	 * Subclass can override to describe the parameters required
	 * to create a new Covearge.
	 * 
	 * @return
	 */
	protected Map<String, Parameter<?>> defineCreateParameterInfo(){
		HashMap<String, Parameter<?>> info = new HashMap<String, Parameter<?>>();
		info.put(URL.key, URL);
		return info;
	}

	public EnumSet<DriverOperation> getDriverCapabilities() {
		return EnumSet.of(DriverOperation.CONNECT, DriverOperation.CREATE);
	}
	    
}
