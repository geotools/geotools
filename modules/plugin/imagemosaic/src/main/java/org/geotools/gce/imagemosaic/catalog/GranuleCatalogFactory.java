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
import java.util.TimeZone;
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
 * Simple Factory class for creating {@link GranuleCatalog} instance to handle the catalog of granules for this mosaic.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 * @source $URL$
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
		
		// STANDARD PARAMS
		final Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put(Utils.Prop.PATH_TYPE,configuration.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE);
		params.put(Utils.Prop.LOCATION_ATTRIBUTE,configuration.getLocationAttribute());
		params.put(Utils.Prop.SUGGESTED_SPI,configuration.getSuggestedSPI());
		params.put(Utils.Prop.HETEROGENEOUS, configuration.isHeterogeneous());
		if(sourceURL!=null){
			File parentDirectory=DataUtilities.urlToFile(sourceURL);
			if(parentDirectory.isFile())
				parentDirectory=parentDirectory.getParentFile();
			params.put(Utils.Prop.PARENT_LOCATION, DataUtilities.fileToURL(parentDirectory).toString());
		}
		else
			params.put(Utils.Prop.PARENT_LOCATION, null);
		// add typename
		String typeName=configuration.getTypeName();
		if(typeName!=null){
			params.put(Utils.Prop.TYPENAME, configuration.getTypeName());
		}		
		// SPI
		DataStoreFactorySpi spi=null;
		
		// Now format specific code
		if(extension.equalsIgnoreCase("shp"))
		{
			//
			// SHAPEFILE
			//
			params.put(ShapefileDataStoreFactory.URLP.key, sourceURL);
			params.put(ShapefileDataStoreFactory.CREATE_SPATIAL_INDEX.key,Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.MEMORY_MAPPED.key, Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.CACHE_MEMORY_MAPS.key, Boolean.TRUE);
			params.put(ShapefileDataStoreFactory.DBFTIMEZONE.key, TimeZone.getTimeZone("UTC"));
			spi=Utils.SHAPE_SPI;			
		}
		else
		{
			// read the properties file
			Properties properties = Utils.loadPropertiesFromURL(sourceURL);
			if (properties == null)
				return null;

			// SPI for datastore
			final String SPIClass = properties.getProperty("SPI");
			try {
				// create a datastore as instructed
				spi = (DataStoreFactorySpi) Class.forName(SPIClass).newInstance();

				// get the params
				final Param[] paramsInfo = spi.getParametersInfo();
				for (Param p : paramsInfo) {
					// search for this param and set the value if found
					if (properties.containsKey(p.key))
						params.put(p.key, (Serializable) Converters.convert(properties.getProperty(p.key), p.type));
					else if (p.required && p.sample == null)
						throw new IOException("Required parameter missing: "+ p.toString());
				}

			} catch (Exception e) {
				if(LOGGER.isLoggable(Level.WARNING))
					LOGGER.log(Level.WARNING,e.getLocalizedMessage(),e);
				return null;
			}
		}		
		// istantiate
		return configuration.isCaching()?new STRTreeGranuleCatalog(params,spi):new GTDataStoreGranuleCatalog(params,false,spi);
	}

}
