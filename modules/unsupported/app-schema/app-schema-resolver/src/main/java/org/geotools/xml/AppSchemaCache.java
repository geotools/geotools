/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2004-2010, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Logger;

import org.geotools.data.DataUtilities;

/**
 * Cache containing application schemas. (Should also work for other file types.)
 * 
 * <p>
 * 
 * If configured to permit downloading, schemas not present in the cache are downloaded from the
 * network.
 * 
 * <p>
 * 
 * Only http/https URLs are supported.
 * 
 * <p>
 * 
 * Files are stored according to the Simple HTTP Resource Path (see
 * {@link AppSchemaResolver#getSimpleHttpResourcePath(URI))}.
 * 
 * @author Ben Caradoc-Davies, CSIRO Earth Science and Resource Engineering
 */
public class AppSchemaCache {

    private static final Logger LOGGER = org.geotools.util.logging.Logging
            .getLogger(AppSchemaCache.class.getPackage().getName());

    /**
     * The default block read size used when downloading a file.
     */
    private static final int DEFAULT_DOWNLOAD_BLOCK_SIZE = 4096;

    /**
     * Filenames used to recognise a GeoServer data directory.
     */
    private static final String[] GEOSERVER_DATA_DIRECTORY_FILENAMES = { "global.xml", "wcs.xml",
            "wfs.xml", "wms.xml" };

    /**
     * Subdirectories used to recognise a GeoServer data directory.
     */
    private static final String[] GEOSERVER_DATA_DIRECTORY_SUBDIRECTORIES = { "styles",
            "workspaces" };

    /**
     * Name of the subdirectory of the GeoServer data directory used for the cache.
     */
    private static final String GEOSERVER_CACHE_DIRECTORY_NAME = "app-schema-cache";

    /**
     * Is support for automatic detection of GeoServer data directories enabled? It is useful to
     * disable this in tests, to prevent downloading.
     */
    private static boolean geoserverSupportEnabled = true;

    /**
     * Root directory of the cache.
     */
    private final File directory;

    /**
     * True if resources not found in the cache are downloaded from the net.
     */
    private final boolean download;

    /**
     * A cache of application schemas (or other file types) rooted in the given directory, with
     * optional downloading.
     * 
     * @param directory
     *            the directory in which downloaded schemas are stored
     * @param download
     *            is downloading of schemas permitted. If false, only schemas already present in the
     *            cache will be resolved.
     */
    public AppSchemaCache(File directory, boolean download) {
        this.directory = directory;
        this.download = download;
    }

    /**
     * Return the root directory of the cache.
     */
    public File getDirectory() {
        return directory;
    }

    /**
     * Are schemas not already present in the cache downloaded from the network?
     */
    public boolean isDownloadAllowed() {
        return download;
    }

    /**
     * Recursively delete a directory or file.
     * 
     * @param file
     */
    static void delete(File file) {
        if (file.isDirectory()) {
            for (File f : file.listFiles()) {
                delete(f);
            }
        }
        file.delete();
    }

    /**
     * Store the bytes in the given file, creating any necessary intervening directories.
     * 
     * @param file
     * @param bytes
     */
    static void store(File file, byte[] bytes) {
        OutputStream output = null;
        try {
            if (file.getParentFile() != null && !file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            output = new BufferedOutputStream(new FileOutputStream(file));
            output.write(bytes);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            if (output != null) {
                try {
                    output.close();
                } catch (Exception e) {
                    // we tried
                }
            }
        }
    }

    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location
     *            and absolute http/https URL.
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    static byte[] download(String location) {
        URI locationUri;
        try {
            locationUri = new URI(location);
        } catch (URISyntaxException e) {
            return null;
        }
        return download(locationUri);
    }

    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location
     *            and absolute http/https URL.
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    static byte[] download(URI location) {
        return download(location, DEFAULT_DOWNLOAD_BLOCK_SIZE);
    }

    /**
     * Retrieve the contents of a remote URL.
     * 
     * @param location
     *            and absolute http/https URL.
     * @param blockSize
     *            download block size
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    static byte[] download(URI location, int blockSize) {
        try {
            URL url = location.toURL();
            String protocol = url.getProtocol();
            if (protocol == null || !(protocol.equals("http") || protocol.equals("https"))) {
                LOGGER.warning("Unexpected download URL protocol: " + protocol);
                return null;
            }
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setUseCaches(false);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.warning(String.format("Unexpected response \"%d %s\" while downloading %s",
                        connection.getResponseCode(), connection.getResponseMessage(),
                        location.toString()));
                return null;
            }
            // read all the blocks into a list
            InputStream input = null;
            List<byte[]> blocks = new LinkedList<byte[]>();
            try {
                input = connection.getInputStream();
                while (true) {
                    byte[] block = new byte[blockSize];
                    int count = input.read(block);
                    if (count == -1) {
                        // end-of-file
                        break;
                    } else if (count == blockSize) {
                        // full block
                        blocks.add(block);
                    } else {
                        // short block
                        byte[] shortBlock = new byte[count];
                        System.arraycopy(block, 0, shortBlock, 0, count);
                        blocks.add(shortBlock);
                    }
                }
            } finally {
                if (input != null) {
                    try {
                        input.close();
                    } catch (Exception e) {
                        // we tried
                    }
                }
            }
            // concatenate all the blocks
            int totalCount = 0;
            for (byte[] b : blocks) {
                totalCount += b.length;
            }
            byte[] bytes = new byte[totalCount];
            int position = 0;
            for (byte[] b : blocks) {
                System.arraycopy(b, 0, bytes, position, b.length);
                position += b.length;
            }
            return bytes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * Return the local file URL of a schema, downloading it if not found in the cache.
     * 
     * @param location
     *            the absolute http/https URL of the schema
     * @return the canonical local file URL of the schema, or null if not found
     */
    public String resolveLocation(String location) {
        String path = AppSchemaResolver.getSimpleHttpResourcePath(location);
        if (path == null) {
            return null;
        }
        String relativePath = path.substring(1);
        File file;
        try {
            file = new File(getDirectory(), relativePath).getCanonicalFile();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (file.exists()) {
            return DataUtilities.fileToURL(file).toExternalForm();
        } else if (isDownloadAllowed()) {
            byte[] bytes = download(location);
            if (bytes == null) {
                return null;
            }
            store(file, bytes);
            LOGGER.info("Cached application schema: " + location);
            return DataUtilities.fileToURL(file).toExternalForm();
        } else {
            return null;
        }
    }

    /**
     * Search parents of url for a GeoServer data directory. If found, use it to create a cache in
     * the "app-schema-cache" subdirectory, with downloading enabled.
     * 
     * @param url
     *            a URL for a file in a GeoServer data directory.
     * @return a cache in the "app-schema-cache" subdirectory
     */
    public static AppSchemaCache buildFromGeoserverUrl(URL url) {
        if (!geoserverSupportEnabled) {
            return null;
        }
        File file = DataUtilities.urlToFile(url);
        while (true) {
            if (file == null) {
                return null;
            }
            if (isGeoserverDataDirectory(file)) {
                return new AppSchemaCache(new File(file, GEOSERVER_CACHE_DIRECTORY_NAME), true);
            }
            file = file.getParentFile();
        }
    }

    /**
     * Turn off support for automatic construction of a cache in GeoServer data directory. Intended
     * for testing.
     */
    public static void disableGeoserverSupport() {
        geoserverSupportEnabled = false;
    }

    /**
     * The opposite of {@link #disableGeoserverSupport()}
     */
    public static void enableGeoserverSupport() {
        geoserverSupportEnabled = true;
    }

    /**
     * @see #disableGeoserverSupport()
     */
    public static boolean isGeoserverSupportEnabled() {
        return geoserverSupportEnabled;
    }

    /**
     * Guess whether a file is a GeoServer data directory.
     * 
     * @param dataDirectory
     *            the candidate file
     * @return true if it has the files and subdirectories expected of a GeoServer data directory
     */
    static boolean isGeoserverDataDirectory(File dataDirectory) {
        if (dataDirectory.isDirectory() == false) {
            return false;
        }
        for (String filename : GEOSERVER_DATA_DIRECTORY_FILENAMES) {
            File file = new File(dataDirectory, filename);
            if (!file.isFile()) {
                return false;
            }
        }
        for (String subdirectory : GEOSERVER_DATA_DIRECTORY_SUBDIRECTORIES) {
            File dir = new File(dataDirectory, subdirectory);
            if (!dir.isDirectory()) {
                return false;
            }
        }
        return true;
    }

}
