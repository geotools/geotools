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

import java.io.Serializable;
import org.geotools.s3.S3Connector;

/** Simple key for cache entries */
public class CacheEntryKey implements Serializable {
    private String bucket;
    private String key;
    private int block;
    private int blockSize;

    private transient S3Connector connector;

    public CacheEntryKey(String bucket, String key, int block, int blockSize) {
        this.bucket = bucket;
        this.key = key;
        this.block = block;
        this.blockSize = blockSize;
    }

    public String getBucket() {
        return bucket;
    }

    public String getKey() {
        return key;
    }

    public int getBlock() {
        return block;
    }

    public int getBlockSize() {
        return blockSize;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CacheEntryKey that = (CacheEntryKey) o;

        if (block != that.block) return false;
        if (blockSize != that.blockSize) return false;
        if (!bucket.equals(that.bucket)) return false;
        return key.equals(that.key);
    }

    @Override
    public int hashCode() {
        int result = bucket.hashCode();
        result = 31 * result + key.hashCode();
        result = 31 * result + block;
        result = 31 * result + blockSize;
        return result;
    }

    public S3Connector getConnector() {
        return connector;
    }

    public void setConnector(S3Connector connector) {
        this.connector = connector;
    }
}
