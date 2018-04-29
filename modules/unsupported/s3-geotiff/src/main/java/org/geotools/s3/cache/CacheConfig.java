/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2017, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.s3.cache;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.logging.Level;
import java.util.logging.Logger;

/** Basic configuration properties for the S3 caching */
public class CacheConfig {

    private static final Logger LOGGER = Logger.getLogger("S3");

    // chunk size
    public static final String S3_CHUNK_SIZE_BYTES = "s3.caching.chunkSizeBytes";

    // whether disk caching should be disabled
    public static final String S3_CACHING_DISABLE_DISK = "s3.caching.disableDisk";

    // whether off heap should be used. currently not supported
    public static final String S3_CACHING_USE_OFF_HEAP = "s3.caching.useOffHeap";

    // the "chunk" size used to cache images
    public static final String S3_CACHING_CHUNK_SIZE_BYTES = "s3.caching.chunkSizeBytes";

    // the disk cache size.
    public static final String S3_CACHING_DISK_CACHE_SIZE = "s3.caching.diskCacheSize";

    // path for the disk cache
    public static final String S3_CACHING_DISK_PATH = "s3.caching.diskPath";

    // alternatively an EhCache 2.x XML config can be used to override all cache config
    public static final String S3_CACHING_EH_CACHE_CONFIG = "s3.caching.ehCacheConfig";

    public static final int MEBIBYTE_IN_BYTES = 1048576;

    // in heap cache size in bytes
    public static final String S3_CACHING_HEAP_SIZE = "s3.caching.heapSize";

    private boolean useDiskCache = true;
    private boolean useOffHeapCache = false;
    private int chunkSizeBytes = 5 * MEBIBYTE_IN_BYTES;
    private int diskCacheSize = 500 * MEBIBYTE_IN_BYTES;
    private int heapSize = 50 * MEBIBYTE_IN_BYTES;
    private Path cacheDirectory;
    private String configurationPath;

    public static CacheConfig getDefaultConfig() {
        CacheConfig config = new CacheConfig();

        if (System.getProperty(S3_CHUNK_SIZE_BYTES) != null) {
            config.setChunkSizeBytes(Integer.getInteger(S3_CHUNK_SIZE_BYTES));
        }

        if (Boolean.getBoolean(S3_CACHING_DISABLE_DISK)) {
            config.setUseDiskCache(false);
        }

        if (Boolean.getBoolean(S3_CACHING_USE_OFF_HEAP)) {
            config.setUseOffHeapCache(true);
        }

        if (System.getProperty(S3_CACHING_CHUNK_SIZE_BYTES) != null) {
            try {
                Integer chunkSize =
                        Integer.parseInt(System.getProperty(S3_CACHING_CHUNK_SIZE_BYTES));
                config.setChunkSizeBytes(chunkSize);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.FINER, "Can't parse chunk size", e);
            }
        }

        if (System.getProperty(S3_CACHING_HEAP_SIZE) != null) {
            try {
                Integer heapSize = Integer.parseInt(System.getProperty(S3_CACHING_HEAP_SIZE));
                config.setHeapSize(heapSize);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.FINER, "Can't parse heap", e);
            }
        }

        if (System.getProperty(S3_CACHING_DISK_CACHE_SIZE) != null) {
            try {
                Integer diskCacheSize =
                        Integer.parseInt(System.getProperty(S3_CACHING_DISK_CACHE_SIZE));
                config.setDiskCacheSize(diskCacheSize);
            } catch (NumberFormatException e) {
                LOGGER.log(Level.FINER, "Can't parse disk cache size", e);
            }
        }

        if (System.getProperty(S3_CACHING_DISK_PATH) != null) {
            try {
                String diskPath = System.getProperty(S3_CACHING_DISK_PATH);
                Path cachePath = Paths.get(diskPath);
                config.setCacheDirectory(cachePath);
            } catch (InvalidPathException e) {
                LOGGER.log(Level.FINER, "Can't parse disk cache path", e);
            }
        } else {
            if (config.isUseDiskCache()) {
                try {
                    config.setCacheDirectory(Files.createTempDirectory("s3Cachine"));
                } catch (IOException e) {
                    throw new RuntimeException(
                            "CAN'T CREATE TEMP CACHING DIRECTORY AND NO DIRECTORY SPECIFIED", e);
                }
            }
        }

        if (System.getProperty(S3_CACHING_EH_CACHE_CONFIG) != null) {
            String ehCachePath = System.getProperty(S3_CACHING_EH_CACHE_CONFIG);
            config.setConfigurationPath(ehCachePath);
        }

        return config;
    }

    public boolean isUseDiskCache() {
        return useDiskCache;
    }

    public void setUseDiskCache(boolean useDiskCache) {
        this.useDiskCache = useDiskCache;
    }

    public boolean isUseOffHeapCache() {
        return useOffHeapCache;
    }

    public void setUseOffHeapCache(boolean useOffHeapCache) {
        this.useOffHeapCache = useOffHeapCache;
    }

    public int getChunkSizeBytes() {
        return chunkSizeBytes;
    }

    public void setChunkSizeBytes(int chunkSizeBytes) {
        this.chunkSizeBytes = chunkSizeBytes;
    }

    public int getDiskCacheSize() {
        return diskCacheSize;
    }

    public void setDiskCacheSize(int diskCacheSize) {
        this.diskCacheSize = diskCacheSize;
    }

    public Path getCacheDirectory() {
        return cacheDirectory;
    }

    public void setCacheDirectory(Path cacheDirectory) {
        this.cacheDirectory = cacheDirectory;
    }

    public String getConfigurationPath() {
        return configurationPath;
    }

    public void setConfigurationPath(String configurationPath) {
        this.configurationPath = configurationPath;
    }

    public int getHeapSize() {
        return heapSize;
    }

    public void setHeapSize(int heapSize) {
        this.heapSize = heapSize;
    }
}
