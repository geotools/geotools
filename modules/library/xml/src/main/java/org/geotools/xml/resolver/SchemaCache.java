/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2010-2012, Open Source Geospatial Foundation (OSGeo)
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

package org.geotools.xml.resolver;

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
import org.geotools.util.URLs;

/**
 * Cache containing XML schemas. (Should also work for other file types.)
 *
 * <p>If configured to permit downloading, schemas not present in the cache are downloaded from the
 * network.
 *
 * <p>Only http/https URLs are supported.
 *
 * <p>Files are stored according to the Simple HTTP Resource Path (see {@link
 * SchemaResolver#getSimpleHttpResourcePath(URI))}.
 *
 * @author Ben Caradoc-Davies (CSIRO Earth Science and Resource Engineering)
 */
public class SchemaCache {

    private static final Logger LOGGER =
            org.geotools.util.logging.Logging.getLogger(SchemaCache.class);

    /** The default block read size used when downloading a file. */
    private static final int DEFAULT_DOWNLOAD_BLOCK_SIZE = 4096;

    /**
     * This is the default value of the keep query flag used when building an automatically
     * configured SchemaCache.
     */
    private static final boolean DEFAULT_KEEP_QUERY = true;

    /**
     * Subdirectories used to recognise a GeoServer data directory if automatic configuration is
     * enabled.
     */
    private static final String[] GEOSERVER_DATA_DIRECTORY_SUBDIRECTORIES = {
        "styles", "workspaces"
    };

    /**
     * Name of the subdirectory of a GeoServer data directory (or other directory) used for the
     * cache if automatic configuration is enabled.
     */
    private static final String CACHE_DIRECTORY_NAME = "app-schema-cache";

    /**
     * Key that should be used to setup a system property that sets the location that should be used
     * for schema cache location.
     */
    public static final String PROVIDED_CACHE_LOCATION_KEY = "schema.cache.dir";

    /**
     * Is support for automatic detection of GeoServer data directories or existing cache
     * directories enabled? It is useful to disable this in tests, to prevent downloading.
     */
    private static boolean automaticConfigurationEnabled = true;

    /** Root directory of the cache. */
    private final File directory;

    /** True if resources not found in the cache are downloaded from the net. */
    private final boolean download;

    /** True if query string components should be part of the discriminator for */
    private final boolean keepQuery;

    /** Default download timeout. Change it with -Dschema.cache.download.timeout=<milliseconds> */
    private static int downloadTimeout = 60000;

    static {
        if (System.getProperty("schema.cache.download.timeout") != null) {
            try {
                downloadTimeout =
                        Integer.parseInt(System.getProperty("schema.cache.download.timeout"));
            } catch (NumberFormatException e) {
                LOGGER.warning(
                        "schema.cache.download.timeout has a wrong format: should be a number");
            }
        }
    }

    /**
     * A cache of XML schemas (or other file types) rooted in the given directory, with optional
     * downloading.
     *
     * @param directory the directory in which downloaded schemas are stored
     * @param download is downloading of schemas permitted. If false, only schemas already present
     *     in the cache will be resolved.
     */
    public SchemaCache(File directory, boolean download) {
        this(directory, download, false);
    }

    /**
     * A cache of XML schemas (or other file types) rooted in the given directory, with optional
     * downloading.
     *
     * @param directory the directory in which downloaded schemas are stored
     * @param download is downloading of schemas permitted. If false, only schemas already present
     *     in the cache will be resolved.
     * @param keepQuery indicates whether or not the query components should be included in the
     *     path. If this is set to true then the query portion is converted to an MD5 message digest
     *     and that string is used to identify the file in the cache.
     */
    public SchemaCache(File directory, boolean download, boolean keepQuery) {
        this.directory = directory;
        this.download = download;
        this.keepQuery = keepQuery;
    }

    /** Return the root directory of the cache. */
    public File getDirectory() {
        return directory;
    }

    /**
     * Return the temp directory for not cached downloads (those occurring during another download,
     * to avoid conflicts among threads).
     */
    public File getTempDirectory() {
        try {
            File tempFolder = File.createTempFile("schema", "cache");
            tempFolder.delete();
            tempFolder.mkdirs();
            return tempFolder;
        } catch (IOException e) {
            LOGGER.severe("Can't create temporary folder");
            throw new RuntimeException(e);
        }
    }

    /** Are schemas not already present in the cache downloaded from the network? */
    public boolean isDownloadAllowed() {
        return download;
    }

    /** Recursively delete a directory or file. */
    static void delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null) {
                for (File f : files) {
                    delete(f);
                }
            }
        }
        file.delete();
    }

    /** Store the bytes in the given file, creating any necessary intervening directories. */
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
     * @param location and absolute http/https URL.
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
     * @param location and absolute http/https URL.
     * @return the bytes contained by the resource, or null if it could not be downloaded
     */
    static byte[] download(URI location) {
        return download(location, DEFAULT_DOWNLOAD_BLOCK_SIZE);
    }

    /**
     * Retrieve the contents of a remote URL.
     *
     * @param location and absolute http/https URL.
     * @param blockSize download block size
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
            connection.setConnectTimeout(downloadTimeout);
            connection.setReadTimeout(downloadTimeout);
            connection.setUseCaches(false);
            connection.connect();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                LOGGER.warning(
                        String.format(
                                "Unexpected response \"%d %s\" while downloading %s",
                                connection.getResponseCode(),
                                connection.getResponseMessage(),
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
            throw new RuntimeException("Error downloading location: " + location.toString(), e);
        }
    }

    /**
     * Return the local file URL of a schema, downloading it if not found in the cache.
     *
     * @param location the absolute http/https URL of the schema
     * @return the canonical local file URL of the schema, or null if not found
     */
    public String resolveLocation(String location) {
        String path = SchemaResolver.getSimpleHttpResourcePath(location, this.keepQuery);
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

        synchronized (SchemaCache.class) {
            if (file.exists()) {
                return URLs.fileToUrl(file).toExternalForm();
            }
        }

        if (isDownloadAllowed()) {
            byte[] bytes = download(location);
            if (bytes == null) {
                return null;
            }
            synchronized (SchemaCache.class) {
                if (!file.exists()) {
                    store(file, bytes);
                    LOGGER.info("Cached XML schema: " + location);
                }
                return URLs.fileToUrl(file).toExternalForm();
            }
        }
        return null;
    }

    /**
     * If automatic configuration is enabled, recursively search parent directories of file url for
     * a GeoServer data directory or directory containing an existing cache. If found, use it to
     * create a cache in the "app-schema-cache" subdirectory with downloading enabled.
     *
     * @param url a URL for a file in a GeoServer data directory.
     * @return a cache in the "app-schema-cache" subdirectory or null if not found or automatic
     *     configuration disabled.
     */
    public static SchemaCache buildAutomaticallyConfiguredUsingFileUrl(URL url) {
        if (!automaticConfigurationEnabled) {
            return null;
        }
        // let's check if a specific cache location was provided
        SchemaCache provided = getProvidedSchemaCache();
        if (provided != null) {
            // a specific schema cache location was provided, let's use it
            return provided;
        }
        File file = URLs.urlToFile(url);
        while (true) {
            if (file == null) {
                LOGGER.warning(
                        "Automatic app-schema-cache directory build failed, "
                                + "Geoserver root folder or app-schema-cache folder not found");
                return null;
            }
            if (isSuitableDirectoryToContainCache(file)) {
                return new SchemaCache(
                        new File(file, CACHE_DIRECTORY_NAME), true, DEFAULT_KEEP_QUERY);
            }
            file = file.getParentFile();
        }
    }

    /**
     * Helper that builds a schema cache if a system property "schema.cache.dir" was set.
     *
     * @return a schema cache, or NULL if no schema cache location was provided
     */
    private static SchemaCache getProvidedSchemaCache() {
        String directory = System.getProperty(PROVIDED_CACHE_LOCATION_KEY);
        if (directory == null) {
            return null;
        }
        // let's try to use the provided cache location
        File cacheDirectory = new File(directory);
        if (cacheDirectory.exists() && cacheDirectory.isFile()) {
            // there is nothing we can do, let's abort the instantiation
            throw new RuntimeException(
                    String.format(
                            "Provided schema cache directory '%s' already exists but it's a file.",
                            cacheDirectory.getAbsolutePath()));
        }
        if (!cacheDirectory.exists()) {
            // create the schema cache directory
            cacheDirectory.mkdir();
        }
        // looks like we are fine
        LOGGER.fine(
                String.format(
                        "Using provided schema cache directory '%s'.",
                        cacheDirectory.getAbsolutePath()));
        return new SchemaCache(cacheDirectory, true, DEFAULT_KEEP_QUERY);
    }

    /**
     * Turn off support for automatic configuration of a cache in GeoServer data directory or
     * detection of an existing cache. Intended for testing. Automatic configuration is enabled by
     * default.
     */
    public static void disableAutomaticConfiguration() {
        automaticConfigurationEnabled = false;
    }

    /**
     * The opposite of {@link #disableAutomaticConfiguration()}. Automatic configuration is enabled
     * by default.
     */
    public static void enableAutomaticConfiguration() {
        automaticConfigurationEnabled = true;
    }

    /**
     * Is automatic configuration enabled? Automatic configuration is enabled by default.
     *
     * @see #disableAutomaticConfiguration()
     */
    public static boolean isAutomaticConfigurationEnabled() {
        return automaticConfigurationEnabled;
    }

    /**
     * Guess whether a file is a GeoServer data directory or contains an existing app-schema-cache
     * subdirectory.
     *
     * @param directory the candidate file
     * @return true if it has the files and subdirectories expected of a GeoServer data directory,
     *     or contains an existing app-schema-cache subdirectory
     */
    static boolean isSuitableDirectoryToContainCache(File directory) {
        if (directory.isDirectory() == false) {
            return false;
        }
        if ((new File(directory, CACHE_DIRECTORY_NAME)).isDirectory()) {
            return true;
        }
        for (String subdirectory : GEOSERVER_DATA_DIRECTORY_SUBDIRECTORIES) {
            File dir = new File(directory, subdirectory);
            if (!dir.isDirectory()) {
                return false;
            }
        }
        // so there is a workspaces directory, lets check default.xml file
        File workspacesDir = new File(directory, "workspaces");
        File defaultXmlFile = new File(workspacesDir, "default.xml");
        if (!defaultXmlFile.exists()) return false;
        return true;
    }
}
