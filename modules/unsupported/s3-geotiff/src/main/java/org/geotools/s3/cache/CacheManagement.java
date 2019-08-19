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

import com.google.common.annotations.VisibleForTesting;
import java.io.File;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.*;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.ConfigurationFactory;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import org.geotools.s3.S3Connector;
import org.geotools.util.logging.Logging;

/** Very basic EhCache handling */
public enum CacheManagement {
    DEFAULT;

    public static final String DEFAULT_CACHE = "default_cache";
    private CacheManager manager;
    private CacheConfig config;

    CacheManagement() {
        init(false);
    }

    final @VisibleForTesting void init(final boolean removeCacheIfExists) {
        this.config = CacheConfig.getDefaultConfig();
        this.manager = buildCache(config, removeCacheIfExists);
    }

    private static CacheManager buildCache(CacheConfig config, boolean removeCacheIfExists) {
        Configuration cacheConfig = null;
        if (config.getConfigurationPath() != null) {
            cacheConfig = loadConfiguration(config.getConfigurationPath());
        }
        if (cacheConfig == null) {
            cacheConfig = new Configuration();
            cacheConfig.setMaxBytesLocalDisk((long) config.getDiskCacheSize());
            cacheConfig.setMaxBytesLocalHeap((long) config.getHeapSize());

            if (config.isUseDiskCache()) {
                DiskStoreConfiguration diskConfig = new DiskStoreConfiguration();
                diskConfig.setPath(config.getCacheDirectory().toAbsolutePath().toString());
                cacheConfig.diskStore(diskConfig);
            }
        }
        if (cacheConfig.getDefaultCacheConfiguration() == null) {
            CacheConfiguration defaultCacheConfiguration =
                    new CacheConfiguration()
                            .persistence(
                                    new PersistenceConfiguration()
                                            .strategy(
                                                    PersistenceConfiguration.Strategy
                                                            .LOCALTEMPSWAP))
                            .timeToIdleSeconds(config.getTimeToIdle())
                            .timeToLiveSeconds(config.getTimeToLive());

            defaultCacheConfiguration.setMaxBytesLocalDisk((long) config.getDiskCacheSize());
            defaultCacheConfiguration.setMaxBytesLocalHeap((long) config.getHeapSize());

            cacheConfig.defaultCache(defaultCacheConfiguration);
        }

        // Use CacheManager.create() instead of new CacheManager(config) to avoid
        // "Another unnamed cache manager already exists..." exception
        CacheManager manager = CacheManager.create(cacheConfig);
        if (removeCacheIfExists && manager.cacheExists(DEFAULT_CACHE)) {
            manager.removeCache(DEFAULT_CACHE);
            logger().info("Re-creating cache " + DEFAULT_CACHE);
        }
        if (!manager.cacheExists(DEFAULT_CACHE)) {
            manager.addCache(DEFAULT_CACHE);
        }
        Cache cache = manager.getCache(DEFAULT_CACHE);
        SelfPopulatingCache populatingCache =
                new SelfPopulatingCache(cache, new S3ChunkEntryFactory(config));
        manager.replaceCacheWithDecoratedCache(cache, populatingCache);

        return manager;
    }

    /**
     * @return {@code null} if the cache can't be loaded from the given file, so the default config
     *     can be used; otherwise the CacheManagement class won't be loaded at all
     */
    private static Configuration loadConfiguration(String configFile) {
        try {
            logger().info("Loading s3-geotiff cache configuration from " + configFile);
            return ConfigurationFactory.parseConfiguration(new File(configFile));
        } catch (Exception e) {
            logger().log(
                            Level.WARNING,
                            String.format(
                                    "Unable to configure S3 GeoTiff cache from %s, using default config",
                                    configFile),
                            e);
        }
        return null;
    }

    /** Get the logger from this method because when needed the class hasn't been loaded yet */
    private static Logger logger() {
        return Logging.getLogger("org.geotools.s3.cache.CacheManagement");
    }

    public byte[] getChunk(CacheEntryKey key, S3Connector connector) {
        key.setConnector(connector);
        return (byte[]) this.manager.getEhcache(DEFAULT_CACHE).get(key).getObjectValue();
    }

    public CacheConfig getCacheConfig() {
        return this.config;
    }
}
