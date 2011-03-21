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
package org.geotools.gce.imagemosaic.catalog;

import java.io.File;
import java.io.IOException;
import java.io.Serializable;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.apache.commons.io.FilenameUtils;
import org.geotools.data.DataAccessFactory.Param;
import org.geotools.data.DataStoreFactorySpi;
import org.geotools.data.DataUtilities;
import org.geotools.data.shapefile.ShapefileDataStoreFactory;
import org.geotools.gce.imagemosaic.MosaicConfigurationBean;
import org.geotools.gce.imagemosaic.PathType;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.util.Converters;
import org.geotools.util.logging.Logging;

/**
 * Simple Factory class for creating {@link GranuleCatalog} elements for this mosaic.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public abstract class GranuleCatalogFactory {	
	
	private final static Logger LOGGER= Logging.getLogger("GranuleCatalogFactory");

	/**
	 * Default private constructor to enforce singleton
	 */
	private GranuleCatalogFactory() {
	}
	

	public static GranuleCatalog createGranuleCatalog(final  Map<String, Serializable> params, final boolean caching, final boolean create, final DataStoreFactorySpi spi){
		//TODO @todo this is a temporary hack before we have an even stupid SPI mechanism here
	    final GranuleCatalog catalogue= new GTDataStoreGranuleCatalog(params,create,spi);
	    if (caching) {
		    return new STRTreeGranuleCatalog(catalogue);
	    }
	    return  catalogue;
	}

	public static GranuleCatalog createGranuleCatalog(
			final URL sourceURL,
			final MosaicConfigurationBean configuration){
		final File sourceFile=DataUtilities.urlToFile(sourceURL);
		final String extension= FilenameUtils.getExtension(sourceFile.getAbsolutePath());
		if(extension.equalsIgnoreCase("shp"))
		{
			// shapefile, caching is always true by default
			final Map<String, Serializable> params = new HashMap<String, Serializable>();
			params.put(ShapefileDataStoreFactory.URLP.key, sourceURL);
			if (sourceURL.getProtocol().equalsIgnoreCase("file"))
				params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.TRUE);
			
			// add other standard params
			params.put("PathType",configuration.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE);
			params.put("LocationAttribute",configuration.getLocationAttribute());
			params.put("SuggestedSPI",configuration.getSuggestedSPI());
			params.put("Heterogeneous", configuration.isHeterogeneous());
			File parentDirectory=DataUtilities.urlToFile(sourceURL);
			if(parentDirectory.isFile())
				parentDirectory=parentDirectory.getParentFile();
			params.put("ParentLocation", DataUtilities.fileToURL(parentDirectory).toString());
			
			final DataStoreFactorySpi spi=configuration.isCaching() ? Utils.SHAPE_SPI : Utils.INDEXED_SHAPE_SPI;
			return configuration.isCaching()?new STRTreeGranuleCatalog(params,spi):new GTDataStoreGranuleCatalog(params,false,spi);
		}
		else
		{
			// read the properties file
			Properties properties = Utils.loadPropertiesFromURL(sourceURL);
			if (properties == null)
				return null;

			// SPI
			final String SPIClass = properties.getProperty("SPI");
			try {
				// create a datastore as instructed
				final DataStoreFactorySpi spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();

				// get the params
				final Map<String, Serializable> params = new HashMap<String, Serializable>();
				final Param[] paramsInfo = spi.getParametersInfo();
				for (Param p : paramsInfo) {
					// search for this param and set the value if found
					if (properties.containsKey(p.key))
						params.put(p.key, (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
					else if (p.required && p.sample == null)
						throw new IOException("Required parameter missing: "+ p.toString());
				}
				// add other standard params
				params.put("PathType",configuration.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE);
				params.put("LocationAttribute",configuration.getLocationAttribute());
				params.put("SuggestedSPI",configuration.getSuggestedSPI());
				params.put("Heterogeneous", configuration.isHeterogeneous());
				if(sourceURL!=null){
					File parentDirectory=DataUtilities.urlToFile(sourceURL);
					if(parentDirectory.isFile())
						parentDirectory=parentDirectory.getParentFile();
					params.put("ParentLocation", DataUtilities.fileToURL(parentDirectory).toString());
				}
				else
					params.put("ParentLocation", null);
				return configuration.isCaching()?new STRTreeGranuleCatalog(params,spi):new GTDataStoreGranuleCatalog(params,false,spi);
			} catch (ClassNotFoundException e) {
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING,e.getLocalizedMessage(),e);
				return null;
			} catch (InstantiationException e) {
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING,e.getLocalizedMessage(),e);
				return null;
			} catch (IllegalAccessException e) {
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING,e.getLocalizedMessage(),e);
				return null;
			} catch (IOException e) {
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING,e.getLocalizedMessage(),e);
				return null;
			}
		}
	}

}
