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
package org.geotools.gce.imagemosaic.properties.numeric;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Properties;

import org.geotools.data.DataUtilities;
import org.geotools.gce.imagemosaic.Utils;
import org.geotools.gce.imagemosaic.properties.DefaultPropertiesCollectorSPI;
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
/**
 * SPI for the extraction of elevation information  from {@link File} names.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
abstract class NumericFileNameExtractorSPI extends
		DefaultPropertiesCollectorSPI implements PropertiesCollectorSPI {
    

        public NumericFileNameExtractorSPI(final String name) {
            super(name);
    
        }
	public PropertiesCollector create(
			final Object o,
			final List<String> propertyNames) {
		URL source=null;
		if (o instanceof URL){
		    source = (URL)o;
		} else if(o instanceof File) {
			source=DataUtilities.fileToURL((File) o);
		}
		else
			if(o instanceof String)
				try {
					source=new URL((String) o);
				} catch (MalformedURLException e) {
					return null;
				}
			else
				return null;
		// it is a url
		final Properties properties = Utils.loadPropertiesFromURL(source);
		if(properties.containsKey("regex"))
			return createInternal(this,propertyNames,properties.getProperty("regex"));
		
		return null;
		
	}
	
	abstract protected PropertiesCollector createInternal(NumericFileNameExtractorSPI numericFileNameExtractorSPI, List<String> propertyNames, String string);


}
