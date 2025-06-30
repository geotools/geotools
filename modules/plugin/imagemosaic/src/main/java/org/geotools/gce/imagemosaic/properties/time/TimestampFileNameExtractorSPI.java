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
package org.geotools.gce.imagemosaic.properties.time;

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
import org.geotools.gce.imagemosaic.properties.PropertiesCollector;
import org.geotools.gce.imagemosaic.properties.PropertiesCollectorSPI;
import org.geotools.util.URLs;

public class TimestampFileNameExtractorSPI implements PropertiesCollectorSPI {

    public static final String REGEX = "regex";

    public static final String FORMAT = "format";

    public static final String FULL_PATH = "fullPath";

    public static final String USE_HIGH_TIME = "useHighTime";

    public static final String REGEX_PREFIX = REGEX + "=";

    public static final String FORMAT_PREFIX = FORMAT + "=";

    public static final String FULL_PATH_PREFIX = FULL_PATH + "=";

    public static final String USE_HIGH_TIME_PREFIX = USE_HIGH_TIME + "=";

    @Override
    public String getName() {
        return "TimestampFileNameExtractorSPI";
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
        String format = null;
        boolean useHighTime = false;
        boolean fullPath = false;
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

                int minIndex = value.length();

                // parameters can be in any order
                // look for the first parameter
                int[] indexesOf = new int[3];
                indexesOf[0] = value.indexOf("," + FORMAT_PREFIX);
                indexesOf[1] = value.indexOf("," + FULL_PATH_PREFIX);
                indexesOf[2] = value.indexOf("," + USE_HIGH_TIME_PREFIX);
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
        if (source != null) {
            properties = CoverageUtilities.loadPropertiesFromURL(source);
        }
        if (properties != null) {
            regex = properties.getProperty(REGEX);
            format = properties.getProperty(FORMAT);
            String fullPathParam = properties.getProperty(FULL_PATH);
            if (fullPathParam != null && fullPathParam.trim().length() > 0) {
                fullPath = Boolean.valueOf(fullPathParam);
            }
            String useHighTimeParam = properties.getProperty(USE_HIGH_TIME);
            if (useHighTimeParam != null && useHighTimeParam.trim().length() > 0) {
                useHighTime = Boolean.valueOf(useHighTimeParam);
            }
        }

        if (format != null) {
            format = format.trim();
        }

        if (regex != null) {
            regex = regex.trim();
            return new TimestampFileNameExtractor(this, propertyNames, regex, format, fullPath, useHighTime);
        }

        return null;
    }
}
