/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2013 - 2016, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.coverage.grid.io.footprint;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.io.FilenameUtils;
import org.geotools.util.logging.Logging;
import org.locationtech.jts.geom.Geometry;
import org.opengis.feature.simple.SimpleFeature;

/**
 * A footprint provider looking for sidecar files (SHP, WKB, WKT, ...). By default, footprints are
 * searched as files living beside the data file. In case a "FOOTPRINTS_DATA_DIR" property is
 * specified, footprints are searched into an external directory too in case they aren't found on
 * the main folder.
 *
 * <p>This can be useful for cases where the data file lives into a read only folder.
 *
 * <p>Suppose data is in /path/to/mydata/tile.tif In the need of supporting footprints into a
 * different location, users should replicate that path within a common folder and define that
 * common folder through the "FOOTPRINTS_DATA_DIR" system property.
 *
 * <p>As an instance, users may put a tile.wkb into /footprints/path/to/mydata/tile.wkb having
 * specified -DFOOTPRINTS_DATA_DIR=/footprints at startup.
 *
 * @author Andrea Aime - GeoSolutions
 * @author Daniele Romagnoli - GeoSolutions
 * @see MultiLevelROIProviderFactory#FOOTPRINTS_DATA_DIR_KEY
 */
public class SidecarFootprintProvider implements FootprintGeometryProvider {

    static final Logger LOGGER = Logging.getLogger(SidecarFootprintProvider.class);

    static final Set<FootprintLoader> LOADERS = new HashSet<FootprintLoader>();

    private static final String FOOTPRINT_LOCATION_ATTRIBUTE = "location";

    /** String associated to the footprints data directory property */
    public static final String FOOTPRINTS_DATA_DIR_KEY = "FOOTPRINTS_DATA_DIR";

    /** The footprints data directory (when specified) */
    private static final String FOOTPRINTS_DATA_DIR;

    /** Static initialization, FOOTPRINTS_DATA_DIR if set as JAVA argument */
    static {
        final Object prefixDir = System.getProperty(FOOTPRINTS_DATA_DIR_KEY);
        String footprintsDir = null;
        if (prefixDir != null) {
            String dir = (String) prefixDir;
            final File file = new File(dir);
            if (!file.exists()) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "The specified path doesn't refer "
                                    + "to an existing folder. Please check the path: "
                                    + dir);
                }
            } else if (!file.isDirectory()) {
                if (LOGGER.isLoggable(Level.WARNING)) {
                    LOGGER.warning(
                            "The specified path doesn't refer "
                                    + "to a directory. Please check the path: "
                                    + dir);
                }
            } else {
                if (LOGGER.isLoggable(Level.INFO)) {
                    LOGGER.info("Setting the Footprints data dir to: " + dir);
                }
                footprintsDir = dir;
            }
        }
        FOOTPRINTS_DATA_DIR = footprintsDir;
        Set<FootprintLoaderSpi> SPI = FootprintLoaderFinder.getAvailableLoaders();
        Iterator<FootprintLoaderSpi> iterator = SPI.iterator();
        while (iterator.hasNext()) {
            // Registering all the available loaders
            LOADERS.add(iterator.next().createLoader());
        }
    }

    /** A File reference. It can be both a folder, as well as the data file */
    private File reference;

    private volatile FootprintLoader lastLoader;

    public SidecarFootprintProvider(File reference) {
        this.reference = reference;
    }

    @Override
    public Geometry getFootprint(SimpleFeature feature) throws IOException {
        String path = getPath(feature);

        if (path != null) {
            return getFootprint(path);
        }
        return null;
    }

    private String getPath(SimpleFeature feature) throws IOException {
        String path = null;
        if (feature == null) {
            // The reference represents the data file itself
            path = reference.getAbsolutePath();
        } else {
            Object value = feature.getAttribute(FOOTPRINT_LOCATION_ATTRIBUTE);
            if (value instanceof String && !((String) value).matches("^(?i)https?://.*$")) {
                String strValue = (String) value;
                path = getFullPath(strValue);
            } else {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.fine(
                            "Could not use the location attribute value to search for "
                                    + "a sidecar file, the value was: "
                                    + value);
                }
            }
        }
        return path;
    }

    /** Return the footprint (if any) for a file referred by its path */
    public Geometry getFootprint(String path) throws IOException {
        String noExtension = getNoExtensionPath(path);

        // Try to reuse over and over the last loader, to avoid checking the others
        // The last loader is kept in a volatile variable and all the work is done via
        // a local copy of it to avoid concurrency issues
        FootprintLoader loader = lastLoader;
        Geometry result = null;
        try {
            // Check from cached loader
            if (loader != null) {
                result = loader.loadFootprint(noExtension);
            }
            // Check beside the original data
            if (result == null) {
                result = checkForFootprint(noExtension);
            }
            // Footprint still not found. Fallback by looking in alternative directory if defined
            if (result == null) {
                // Try looking at the alternative location if defined
                if ((noExtension = getAlternativePath(path, true)) != null) {
                    result = checkForFootprint(noExtension);
                }
            }

        } catch (Exception e) {
            throw new IOException("Failed to load the footprint for granule " + path, e);
        }
        return result;
    }

    private static String getAlternativePath(String path, boolean removeExtension) {
        return FOOTPRINTS_DATA_DIR != null
                ? getAlternativeFile(path, removeExtension).getAbsolutePath()
                : null;
    }

    public static File getAlternativeFile(File file) {
        return FOOTPRINTS_DATA_DIR != null
                ? getAlternativeFile(file.getAbsolutePath(), false)
                : null;
    }

    private static File getAlternativeFile(String path, boolean removeExtension) {
        String basePath = FilenameUtils.getPathNoEndSeparator(path);
        String name =
                removeExtension ? FilenameUtils.getBaseName(path) : FilenameUtils.getName(path);
        String alternativePath = basePath + File.separatorChar + name;
        return new File(FOOTPRINTS_DATA_DIR, alternativePath);
    }

    private Geometry checkForFootprint(String noExtension) {
        Geometry result = null;
        for (FootprintLoader test : LOADERS) {
            try {
                result = test.loadFootprint(noExtension);
                if (result != null) {
                    lastLoader = test;
                    break;
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            test.getClass().getName() + " threw exception loading footprint",
                            e);
                }
            }
        }
        return result;
    }

    private FootprintLoader getLoader(String noExtension) {
        for (FootprintLoader test : LOADERS) {
            try {
                Geometry result = test.loadFootprint(noExtension);
                if (result != null) {
                    return test;
                }
            } catch (Exception e) {
                if (LOGGER.isLoggable(Level.FINE)) {
                    LOGGER.log(
                            Level.FINE,
                            test.getClass().getName() + " threw exception loading footprint",
                            e);
                }
            }
        }
        return null;
    }

    private String getNoExtensionPath(String path) {
        int idx = path.lastIndexOf(".");
        return idx > 0 ? path.substring(0, idx) : path;
    }

    private String getFullPath(String strValue) throws IOException {
        File file = new File(strValue);
        if (!file.isAbsolute()) {
            file = new File(reference, strValue);
        }
        return file.getCanonicalPath();
    }

    @Override
    public void dispose() {
        // nothing to do, in this providers we don't keep files open
    }

    public static String getFootprintsDataDir() {
        return FOOTPRINTS_DATA_DIR;
    }

    @Override
    public List<File> getSidecars(SimpleFeature feature) throws IOException {
        String path = getPath(feature);
        if (path != null) {
            return getSidecars(path);
        }
        return Collections.emptyList();
    }

    public List<File> getSidecars(String path) throws IOException {
        String noExtensionPath = getNoExtensionPath(path);
        FootprintLoader loader = getLoader(noExtensionPath);
        if (loader != null) {
            return loader.getFootprintFiles(noExtensionPath);
        }
        return Collections.emptyList();
    }
}
