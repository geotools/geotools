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
package org.geotools.gce.imagemosaic.properties.string;

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
import org.geotools.resources.i18n.ErrorKeys;
import org.geotools.resources.i18n.Errors;
/**
 * {@link PropertiesCollectorSPI} for a {@link PropertiesCollector} that is able to collect properties from a file name.
 * 
 * @author Simone Giannecchini, GeoSolutions SAS
 *
 */
public class StringFileNameExtractorSPI extends
		DefaultPropertiesCollectorSPI implements PropertiesCollectorSPI {

	public StringFileNameExtractorSPI() {
		super("StringFileNameExtractorSPI");
	}

	public PropertiesCollector create(
			final Object o,
			final List<String> propertyNames) {
		URL source = null;
		if (o instanceof URL){
		    source = (URL) o;
		} else if (o instanceof File) {
			source = DataUtilities.fileToURL((File) o);
		} else if (o instanceof String) {
				try {
					source = new URL((String) o);
				} catch (MalformedURLException e) {
					return null;
				}
		} else {
			return null;
		}
		// it is a url
		final Properties properties = Utils.loadPropertiesFromURL(source);
		if(properties == null){
		    throw new IllegalArgumentException(Errors.format(ErrorKeys.ILLEGAL_ARGUMENT_$2,"Unable to load source for properties collector",source));
		}
		if(properties.containsKey("regex"))
			return new StringFileNameExtractor(this,propertyNames,properties.getProperty("regex"));
		
		return null;
		
	}

}
