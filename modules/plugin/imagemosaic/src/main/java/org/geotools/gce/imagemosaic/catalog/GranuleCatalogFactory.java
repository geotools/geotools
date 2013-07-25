/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007-2013, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.factory.Hints;
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
	

	public static GranuleCatalog createGranuleCatalog(
	        final  Map<String, Serializable> params, 
	        final boolean caching, 
	        final boolean create, 
	        final DataStoreFactorySpi spi,
	        final Hints hints){
	    if (caching) {
		    return new STRTreeGranuleCatalog(params,spi,hints);
	    } else {
	        return new CachingDataStoreGranuleCatalog(new GTDataStoreGranuleCatalog(params,create,spi,hints));
	    }
	}

	public static GranuleCatalog createGranuleCatalog(
			final URL sourceURL,
			final CatalogConfigurationBean catalogConfigurationBean,
			final Map<String, Serializable> overrideParams,
	                final Hints hints){
		final File sourceFile=DataUtilities.urlToFile(sourceURL);
		final String extension= FilenameUtils.getExtension(sourceFile.getAbsolutePath());	
		
		// STANDARD PARAMS
		final Map<String, Serializable> params = new HashMap<String, Serializable>();
		params.put(Utils.Prop.PATH_TYPE, catalogConfigurationBean.isAbsolutePath()?PathType.ABSOLUTE:PathType.RELATIVE);
		params.put(Utils.Prop.LOCATION_ATTRIBUTE, catalogConfigurationBean.getLocationAttribute());
		params.put(Utils.Prop.SUGGESTED_SPI, catalogConfigurationBean.getSuggestedSPI());
		params.put(Utils.Prop.HETEROGENEOUS, catalogConfigurationBean.isHeterogeneous());
		if(sourceURL!=null){
			File parentDirectory=DataUtilities.urlToFile(sourceURL);
			if(parentDirectory.isFile())
				parentDirectory=parentDirectory.getParentFile();
			params.put(Utils.Prop.PARENT_LOCATION, DataUtilities.fileToURL(parentDirectory).toString());
		}
		else
			params.put(Utils.Prop.PARENT_LOCATION, null);
		// add typename
		String typeName = catalogConfigurationBean.getTypeName();
		if(typeName!=null){
			params.put(Utils.Prop.TYPENAME, catalogConfigurationBean.getTypeName());
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
		// Instantiate
		if (overrideParams != null && !overrideParams.isEmpty()) {
		    params.putAll(overrideParams);
		}
		final GranuleCatalog catalog = catalogConfigurationBean.isCaching()?
		        new STRTreeGranuleCatalog(params,spi,hints):
		            new CachingDataStoreGranuleCatalog(new GTDataStoreGranuleCatalog(params,false,spi,hints));

		return catalog;
	}

}
