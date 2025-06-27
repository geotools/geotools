/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2016, Open Source Geospatial Foundation (OSGeo)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation;
 * version 2.1 of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 */
package org.geotools.gce.imagemosaic.namecollector;

import it.geosolutions.imageio.core.SourceSPIProvider;
import java.io.File;
import java.net.MalformedURLException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.io.FilenameUtils;
import org.geotools.coverage.grid.io.GridCoverage2DReader;
import org.geotools.gce.imagemosaic.Utils.SourceGetter;

/**
 * A {@link CoverageNameCollectorSPI} implementation setting up the coverage name based on a regex applied to the
 * fileName.
 */
public class FileNameRegexNameCollectorSPI implements CoverageNameCollectorSPI {

    public static String REGEX = "regex";

    @Override
    public CoverageNameCollector create(Object object, Map<String, String> map) {
        String regex = null;
        if (map != null && map.containsKey(REGEX) && (regex = map.get(REGEX)) != null) {
            return new FileNameRegexBasedCoverageNameCollector(regex);
        }
        throw new IllegalArgumentException(
                "FileNameRegexNameCollectorSPI should have " + "a defined REGEX property in the map");
    }

    /** A {@link CoverageNameCollector} implementation which setup the coverageName on top of a regex on the filename */
    static class FileNameRegexBasedCoverageNameCollector implements CoverageNameCollector {

        private Pattern pattern;

        public FileNameRegexBasedCoverageNameCollector(String regex) {
            this.pattern = Pattern.compile(regex);
        }

        @Override
        public String getName(GridCoverage2DReader reader, Map<String, String> map) {
            Object source = reader.getSource();
            SourceGetter sourceGetter = new SourceGetter(source);
            final File file = sourceGetter.getFile();
            String path = null;
            if (file == null) {
                if (source instanceof SourceSPIProvider) {
                    SourceSPIProvider provider = (SourceSPIProvider) source;
                    try {
                        path = provider.getSourceUrl().toString();
                    } catch (MalformedURLException e) {
                        throw new IllegalArgumentException(
                                "Unable to retrieve a valid source for the specified reader");
                    }
                    int indexOf = path.lastIndexOf("/");
                    path = path.substring(indexOf + 1);
                } else {
                    throw new IllegalArgumentException(
                            "Unable to retrieve a valid source file" + " for the specified reader");
                }
            } else {
                path = file.getAbsolutePath();
            }
            String baseName = FilenameUtils.getBaseName(path);
            final Matcher matcher = pattern.matcher(baseName);
            // Take the first match
            if (matcher.find()) {
                // Chaining group Strings together
                int count = matcher.groupCount();
                String match = "";
                for (int i = 1; i <= count; i++) {
                    match += matcher.group(i);
                }
                return match;
            }
            throw new IllegalArgumentException("Unable to retrieve the coverageName");
        }
    }
}
