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
package org.geotools.gce.imagemosaic.properties.time;

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
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;

/**
 * 
 *
 * @source $URL$
 */
public class TimestampFileNameExtractorSPI implements PropertiesCollectorSPI {
    
    public final static String REGEX = "regex";
    public final static String FORMAT = "format";
    public final static String REGEX_PREFIX = REGEX + "=";
    public final static String FORMAT_PREFIX = FORMAT + "=";

    public String getName() {
        return "TimestampFileNameExtractorSPI";
    }

    public boolean isAvailable() {
        return true;
    }

    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }
	
    
    public PropertiesCollector create(final Object o, final List<String> propertyNames) {
        URL source = null;
        String regex = null;
        String format = null;
        if (o instanceof URL) {
            source = (URL) o;
        } else if (o instanceof File) {
            source = DataUtilities.fileToURL((File) o);
        } else if (o instanceof String) {
            try {
                source = new URL((String) o);
            } catch (MalformedURLException e) {

                String value = (String) o;
                
                // look for the regex
                if(value.startsWith(REGEX_PREFIX)) {
                    String tmp = value.substring(REGEX_PREFIX.length());
                    if(tmp.contains("," + FORMAT_PREFIX)) {
                        int idx = tmp.indexOf("," + FORMAT_PREFIX);
                        regex = tmp.substring(0, idx);
                        value = tmp.substring(idx + 1);
                    } else {
                        regex = tmp;
                    }
                }
                
                // look for the format
                if (value.startsWith(FORMAT_PREFIX)) {
                    format = value.substring(FORMAT_PREFIX.length());
                } 
            }
        } else {
            return null;
        }

        // it is a url
        if (source != null) {
            final Properties properties = Utils.loadPropertiesFromURL(source);
            regex = properties.getProperty(REGEX);
            format = properties.getProperty(FORMAT);
        }
        
        if(regex != null) {
            regex = regex.trim();
        }
        if(format != null) {
            format = format.trim();
        }
        
        if (regex != null) {
            return new TimestampFileNameExtractor(this, propertyNames, regex, format);
        }

        return null;

    }
    

}
