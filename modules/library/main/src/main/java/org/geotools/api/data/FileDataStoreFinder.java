/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2008, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.api.data;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.geotools.factory.CommonFactoryFinder;

/**
 * Most of this code was copied from DataStoreFinder. See the Documentation there for details.
 *
 * <p>This searches for DataStores which support a singular file parsed in a particular file format.
 *
 * @author dzwiers
 * @see DataStoreFinder
 */
public class FileDataStoreFinder {
    /** The logger for the filter module. */
    protected static final Logger LOGGER = org.geotools.util.logging.Logging.getLogger(FileDataStoreFinder.class);

    private FileDataStoreFinder() {}

    /**
     * Checks each available datasource implementation in turn and returns the first one which claims to support the
     * given file..
     *
     * @param file the file
     * @return The first datasource which claims to process the required resource, returns null if none can be found.
     * @throws IOException If a suitable loader can be found, but it can not be attached to the specified resource
     *     without errors.
     */
    public static FileDataStore getDataStore(File file) throws IOException {
        return getDataStore(file.toURI().toURL());
    }

    /**
     * Checks each available datasource implementation in turn and returns the first one which claims to support the
     * resource identified by the params object.
     *
     * @param url URL for the input resource
     * @return The first datasource which claims to process the required resource, returns null if none can be found.
     * @throws IOException If a suitable loader can be found, but it can not be attached to the specified resource
     *     without errors.
     */
    public static FileDataStore getDataStore(URL url) throws IOException {
        Iterator<FileDataStoreFactorySpi> ps = getAvailableDataStores();

        while (ps.hasNext()) {
            FileDataStoreFactorySpi fac = ps.next();
            if (!fac.isAvailable()) {
                continue;
            }
            try {
                if (fac.canProcess(url)) {
                    return fac.createDataStore(url);
                }
            } catch (Throwable t) {
                /* The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not aquire " + fac.getDescription() + ":" + t, t);

                // Protect against DataStores that don't carefully
                // code canProcess
                continue;
            }
        }

        return null;
    }

    /**
     * Used to look up a FileDataStoreFactorySpi by extension.
     *
     * @param extension Extension such as "shp"
     * @return FileDataStoreFactorySpi
     */
    public static FileDataStoreFactorySpi getDataStoreFactory(String extension) {
        String extension2 = null;
        if (!extension.startsWith(".")) {
            extension2 = "." + extension;
        }
        Iterator<FileDataStoreFactorySpi> ps = getAvailableDataStores();
        while (ps.hasNext()) {
            FileDataStoreFactorySpi fac = ps.next();
            if (!fac.isAvailable()) {
                continue;
            }
            try {
                for (String ext : fac.getFileExtensions()) {
                    if (extension.equalsIgnoreCase(ext)) {
                        return fac;
                    }
                    if (extension2 != null && extension2.equalsIgnoreCase(ext)) {
                        return fac;
                    }
                }
            } catch (Throwable t) {
                /* The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not aquire " + fac.getDescription() + ":" + t, t);

                // Protect against DataStores that don't carefully
                // code canProcess
                continue;
            }
        }

        return null;
    }

    /**
     * Returns an iterator of FileDataStoreFactorySpi to allow for the easy creation of a FileDataStore
     *
     * @see FileDataStoreFactorySpi
     * @see FileDataStore
     */
    public static Iterator<FileDataStoreFactorySpi> getAvailableDataStores() {
        return CommonFactoryFinder.getFileDataStoreFactories(null).stream()
                .filter(dsf -> dsf.isAvailable())
                .iterator();
    }

    /**
     * Go through each file DataStore and check what file extentions are supported.
     *
     * @return Set of supported file extensions
     */
    public static Set<String> getAvailableFileExtentions() {
        Set<String> extentions = new HashSet<>();

        Iterator<FileDataStoreFactorySpi> ps = getAvailableDataStores();
        while (ps.hasNext()) {
            FileDataStoreFactorySpi fac = ps.next();
            try {
                for (String fileExtention : fac.getFileExtensions()) {
                    extentions.add(fileExtention);
                }
            } catch (Throwable t) {
                /* The logger for the filter module. */
                LOGGER.log(Level.WARNING, "Could not aquire " + fac.getDescription() + ":" + t, t);

                // Protect against DataStores that don't carefully
                // code canProcess
                continue;
            }
        }
        return extentions;
    }
}
