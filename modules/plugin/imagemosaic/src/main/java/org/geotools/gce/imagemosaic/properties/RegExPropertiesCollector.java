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
package org.geotools.gce.imagemosaic.properties;

import java.io.File;
import java.net.URI;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.geotools.util.logging.Logging;

public abstract class RegExPropertiesCollector extends PropertiesCollector implements FullPathCollector {

    private static final Logger LOGGER = Logging.getLogger(RegExPropertiesCollector.class);

    protected boolean fullPath = false;

    protected Pattern pattern;

    @Override
    public boolean isFullPath() {
        return fullPath;
    }

    @Override
    public void setFullPath(boolean fullPath) {
        this.fullPath = fullPath;
    }

    public RegExPropertiesCollector(
            PropertiesCollectorSPI spi, List<String> propertyNames, String regex, boolean fullPath) {
        super(spi, propertyNames);
        this.fullPath = fullPath;
        pattern = Pattern.compile(regex);
    }

    @Override
    public RegExPropertiesCollector collect(File file) {
        super.collect(file);

        // get name of the file
        final String absolutePath = file.getAbsolutePath();
        final String name = fullPath ? absolutePath : FilenameUtils.getBaseName(absolutePath);

        // get matches
        addMatches(name);
        return this;
    }

    @Override
    public RegExPropertiesCollector collect(URL url) {
        super.collect(url);

        // get name of the url
        final String fullUrl = url.toString();
        return collectFromPath(fullUrl);
    }

    @Override
    public RegExPropertiesCollector collect(URI uri) {
        super.collect(uri);

        // get path of the uri
        final String path = uri.toString();
        return collectFromPath(path);
    }

    private RegExPropertiesCollector collectFromPath(String name) {
        if (!fullPath) {
            int index = name.lastIndexOf("/");
            name = name.substring(index + 1);
        }

        // get matches
        addMatches(name);
        return this;
    }

    private void addMatches(String name) {
        final Matcher matcher = pattern.matcher(name);

        boolean matched = false;
        while (matcher.find()) {
            matched = true;
            // Chaining group Strings together
            int count = matcher.groupCount();
            String match = "";
            if (count == 0) {
                match = matcher.group();
            }
            for (int i = 1; i <= count; i++) {
                match += matcher.group(i);
            }
            addMatch(match);
        }
        if (!matched) LOGGER.fine("No matches found for the pattern: " + pattern.pattern() + " in the string: " + name);
    }

    @Override
    public void setProperties(Map<String, Object> map) {

        // get all the matches and convert them
        List<String> matches = getMatches();

        // set the properties, only if we have matches!
        if (matches.isEmpty()) {
            throw new IllegalArgumentException("No matches found for: " + this);
        }
        int index = 0;
        for (String propertyName : getPropertyNames()) {
            map.put(propertyName, matches.get(index++));
            // do we have more values?
            if (index >= matches.size()) return;
        }
    }

    @Override
    public String toString() {
        return "RegExPropertiesCollector{" + "fullPath=" + fullPath + ", pattern=" + pattern + '}';
    }
}
