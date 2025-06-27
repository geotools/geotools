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
import java.io.IOException;
import java.io.StringReader;
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

    public static final String REGEX = "regex";

    public static final String FULL_PATH = "fullPath";

    public static final String REGEX_PREFIX = REGEX + "=";

    public static final String FULL_PATH_PREFIX = FULL_PATH + "=";

    @Override
    public String getName() {
        return name;
    }

    public DefaultPropertiesCollectorSPI(String name) {
        this.name = name;
    }

    @Override
    public boolean isAvailable() {
        return true;
    }

    @Override
    public Map<Key, ?> getImplementationHints() {
        return Collections.emptyMap();
    }

    @Override
    public PropertiesCollector create(final Object o, final List<String> propertyNames) {
        URL source = null;
        String regex = null;
        Properties properties = null;
        if (o instanceof URL) {
            source = (URL) o;
        } else if (o instanceof File) {
            source = URLs.fileToUrl((File) o);
        } else if (o instanceof String) {
            try {
                source = new URL((String) o);
            } catch (MalformedURLException e) {

                String value = (String) o;

                // parameters can be in any order
                // look for the first parameter
                int[] indexesOf = new int[3];
                indexesOf[1] = value.indexOf("," + FULL_PATH_PREFIX);
                int minIndex = value.length();
                for (int indexOf : indexesOf) {
                    minIndex = indexOf > 0 && indexOf < minIndex ? indexOf : minIndex;
                }

                if (value.startsWith(REGEX_PREFIX)) {
                    String prop = value;
                    regex = value.substring(REGEX_PREFIX.length(), minIndex);
                    // Do we have more parameters?
                    if (minIndex != value.length()) {
                        value = value.substring(minIndex + 1);
                        prop = value.replaceAll(",", "\n");

                        // Setup properties from String for future extraction
                        properties = new Properties();
                        try {
                            properties.load(new StringReader(prop));
                            properties.setProperty(REGEX, regex);
                        } catch (IOException e1) {
                            throw new IllegalArgumentException("Unable to parse the specified regex: " + value, e1);
                        }
                    }
                }
            }
        } else {
            return null;
        }

        // it is a url
        boolean fullPath = false;
        if (source != null) {
            properties = CoverageUtilities.loadPropertiesFromURL(source);
            if (properties.containsKey(REGEX)) {
                regex = properties.getProperty(REGEX);
                fullPath = Boolean.valueOf(properties.getProperty(FULL_PATH));
            }
        } else if (properties != null) { // it was inline
            regex = properties.getProperty(REGEX);
            String fullPathParam = properties.getProperty(FULL_PATH);
            if (fullPathParam != null && fullPathParam.trim().length() > 0) {
                fullPath = Boolean.valueOf(fullPathParam);
            }
        }
        if (regex != null) {
            PropertiesCollector pc = createInternal(this, propertyNames, regex.trim());
            if (pc == null) return null;
            if (fullPath) {
                if (pc instanceof FullPathCollector) {
                    ((FullPathCollector) pc).setFullPath(true);
                } else {
                    throw new IllegalArgumentException(
                            "This collector does not support the full path option: " + pc.getSpi());
                }
            }

            return pc;
        }

        return null;
    }

    protected abstract PropertiesCollector createInternal(
            PropertiesCollectorSPI fileNameExtractorSPI, List<String> propertyNames, String string);
}
