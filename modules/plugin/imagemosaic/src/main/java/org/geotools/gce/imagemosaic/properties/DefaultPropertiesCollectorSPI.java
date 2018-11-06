/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2007 - 2016, Open Source Geospatial Foundation (OSGeo)
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
import org.geotools.coverage.util.CoverageUtilities;
import org.geotools.util.URLs;

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

    public static final String REGEX_PREFIX = "regex=";

    public PropertiesCollector create(final Object o, final List<String> propertyNames) {
        URL source = null;
        String property = null;
        if (o instanceof URL) {
            source = (URL) o;
        } else if (o instanceof File) {
            source = URLs.fileToUrl((File) o);
        } else if (o instanceof String) {
            try {
                source = new URL((String) o);
            } catch (MalformedURLException e) {

                String value = (String) o;
                if (value.startsWith(REGEX_PREFIX)) {
                    property = value.substring(REGEX_PREFIX.length());
                } else {
                    return null;
                }
            }
        } else {
            return null;
        }

        // it is a url
        if (source != null) {
            final Properties properties = CoverageUtilities.loadPropertiesFromURL(source);
            if (properties.containsKey("regex")) {
                property = properties.getProperty("regex");
            }
        }
        if (property != null) {
            return createInternal(this, propertyNames, property.trim());
        }

        return null;
    }

    protected abstract PropertiesCollector createInternal(
            PropertiesCollectorSPI fileNameExtractorSPI, List<String> propertyNames, String string);
}
