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

    public static final String REGEX_PREFIX = REGEX + "=";

    public static final String FORMAT_PREFIX = FORMAT + "=";

    public static final String FULL_PATH_PREFIX = FULL_PATH + "=";

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
        boolean fullPath = false;
        if (o instanceof URL) {
            source = (URL) o;
        } else if (o instanceof File) {
            source = URLs.fileToUrl((File) o);
        } else if (o instanceof String) {
            try {
                source = new URL((String) o);
            } catch (MalformedURLException e) {

                String value = (String) o;

                // look for the regex
                int idx = 0;
                if (value.startsWith(REGEX_PREFIX)) {
                    String tmp = value.substring(REGEX_PREFIX.length());
                    if (tmp.contains("," + FORMAT_PREFIX)) {
                        idx = tmp.indexOf("," + FORMAT_PREFIX);
                        regex = tmp.substring(0, idx);
                        value = tmp.substring(idx + 1);
                    } else if (tmp.contains("," + FULL_PATH_PREFIX)) {
                        idx = tmp.indexOf("," + FULL_PATH_PREFIX);
                        regex = tmp.substring(0, idx);
                        value = tmp.substring(idx + 1);
                    } else {
                        regex = tmp;
                    }
                }

                // look for the format
                if (value.startsWith(FORMAT_PREFIX)) {
                    if (value.contains("," + FULL_PATH_PREFIX)) {
                        idx = value.indexOf("," + FULL_PATH_PREFIX);
                        format = value.substring(0, idx);
                        value = value.substring(idx + 1);
                    } else {
                        format = value;
                    }
                    format = format.substring(FORMAT_PREFIX.length());
                }

                // look for the full path param
                if (value.startsWith(FULL_PATH_PREFIX)) {
                    fullPath = Boolean.valueOf(value.substring(FULL_PATH_PREFIX.length()));
                }
            }
        } else {
            return null;
        }

        // it is a url
        if (source != null) {
            final Properties properties = CoverageUtilities.loadPropertiesFromURL(source);
            regex = properties.getProperty(REGEX);
            format = properties.getProperty(FORMAT);
            String fullPathParam = properties.getProperty(FULL_PATH);
            if (fullPathParam != null && fullPathParam.trim().length() > 0) {
                fullPath = Boolean.valueOf(fullPathParam);
            }
        }

        if (regex != null) {
            regex = regex.trim();
        }
        if (format != null) {
            format = format.trim();
        }

        if (regex != null) {
            return new TimestampFileNameExtractor(this, propertyNames, regex, format, fullPath);
        }
        return null;
    }
}
