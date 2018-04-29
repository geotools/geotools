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

import net.sf.ehcache.*;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.PersistenceConfiguration;
import net.sf.ehcache.constructs.blocking.SelfPopulatingCache;
import org.geotools.s3.S3Connector;

/** Very basic EhCache handling */
public enum CacheManagement {
    DEFAULT;

    public static final String DEFAULT_CACHE = "default_cache";
    private final CacheManager manager;
    private final CacheConfig config;

    CacheManagement() {
        CacheConfig config = CacheConfig.getDefaultConfig();

        this.manager = buildCache(config);
        this.config = config;
    }

    private static CacheManager buildCache(CacheConfig config) {
        CacheManager manager;
        if (config.getConfigurationPath() != null) {
            manager = CacheManager.newInstance(config.getConfigurationPath());
        } else {
            Configuration cacheConfig = new Configuration();
            cacheConfig.setMaxBytesLocalDisk((long) config.getDiskCacheSize());
            cacheConfig.setMaxBytesLocalHeap((long) config.getHeapSize());
            CacheConfiguration defaultCacheConfiguration =
                    new CacheConfiguration()
                            .persistence(
                                    new PersistenceConfiguration()
                                            .strategy(
                                                    PersistenceConfiguration.Strategy
                                                            .LOCALTEMPSWAP));
            cacheConfig.defaultCache(defaultCacheConfiguration);

            if (config.isUseDiskCache()) {
                DiskStoreConfiguration diskConfig = new DiskStoreConfiguration();
                diskConfig.setPath(config.getCacheDirectory().toAbsolutePath().toString());
                cacheConfig.diskStore(diskConfig);
            }

            manager = new CacheManager(cacheConfig);

            manager.addCache(DEFAULT_CACHE);
            Cache cache = manager.getCache(DEFAULT_CACHE);
            SelfPopulatingCache populatingCache =
                    new SelfPopulatingCache(cache, new S3ChunkEntryFactory(config));
            manager.replaceCacheWithDecoratedCache(cache, populatingCache);
        }

        return manager;
    }

    public byte[] getChunk(CacheEntryKey key, S3Connector connector) {
        key.setConnector(connector);
        return (byte[]) this.manager.getEhcache(DEFAULT_CACHE).get(key).getObjectValue();
    }

    public CacheConfig getCacheConfig() {
        return this.config;
    }
}
