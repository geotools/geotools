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
package org.geotools.gce.imagemosaic.properties;

import java.awt.RenderingHints.Key;
import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.geotools.data.DataUtilities;
import org.geotools.gce.imagemosaic.Utils;

/**
 * 
 *
 * @source $URL$
 */
public abstract class DefaultPropertiesCollectorSPI implements PropertiesCollectorSPI {

	private final String name;
	
	public String getName() {
		return name;
	}

	public DefaultPropertiesCollectorSPI(String name) {
		this.name = name;
	}

	public boolean isAvailable() {
		return true;
	}

	public Map<Key, ?> getImplementationHints() {
		return Collections.emptyMap();
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
                if(properties.containsKey("regex")){
                    final String property = properties.getProperty("regex");
                    if(property!=null){
                        return createInternal(this,propertyNames,property.trim());
                    }
                }
                
                return null;
                
        }

        abstract protected PropertiesCollector createInternal(PropertiesCollectorSPI fileNameExtractorSPI, List<String> propertyNames, String string);	

}
