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

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.sf.ehcache.CacheException;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Status;
import net.sf.ehcache.constructs.blocking.CacheEntryFactory;
import net.sf.ehcache.loader.CacheLoader;
import org.geotools.s3.S3Connector;

/** Factory to create S3 chunks for caching */
public class S3ChunkEntryFactory implements CacheEntryFactory, CacheLoader {

    private static final Logger LOGGER = Logger.getLogger("S3");

    private int cacheBlockSize;

    public S3ChunkEntryFactory(CacheConfig config) {
        this.cacheBlockSize = config.getChunkSizeBytes();
    }

    @Override
    public Object createEntry(Object key) throws Exception {
        return createEntry(key, ((CacheEntryKey) key).getConnector());
    }

    private Object createEntry(Object key, S3Connector connector) throws IOException {
        byte[] val;
        CacheEntryKey entryKey = (CacheEntryKey) key;
        int nBytes;
        byte[] buffer = new byte[cacheBlockSize];
        try (S3Object object =
                this.initStream(
                        (long) entryKey.getBlock() * (long) this.cacheBlockSize,
                        entryKey.getBucket(),
                        entryKey.getKey(),
                        entryKey.getBlockSize(),
                        connector.getS3Client())) {
            if (object == null) {
                throw new RuntimeException(
                        "Unable to instantiate S3 stream. See logs for details.");
            }
            int readLength = this.cacheBlockSize;

            try (InputStream stream = object.getObjectContent()) {
                try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
                    while (readLength > 0) {
                        nBytes = stream.read(buffer, 0, readLength);

                        if (nBytes > 0) {
                            out.write(buffer, 0, nBytes);
                            readLength -= nBytes;
                        } else {
                            break;
                        }
                    }
                    val = out.toByteArray();
                }
            }
        }

        return val;
    }

    private S3Object initStream(
            long offset, String bucket, String key, int blockSize, AmazonS3 s3Client) {
        try {
            S3Object object =
                    s3Client.getObject(
                            (new GetObjectRequest(bucket, key))
                                    .withRange(offset, offset + blockSize));

            return object;
        } catch (Exception e) {
            LOGGER.warning(e.getMessage());
        }
        return null;
    }

    @Override
    public Object load(Object key) throws CacheException {
        throw new UnsupportedOperationException("Can't load chunk without loader argument.");
    }

    @Override
    public Map loadAll(Collection keys) {
        throw new UnsupportedOperationException("Can't load chunk without loader argument.");
    }

    @Override
    public Object load(Object key, Object argument) {
        S3Connector connector = (S3Connector) argument;
        try {
            return this.createEntry(key, connector);
        } catch (IOException e) {
            LOGGER.log(Level.FINE, "Exception creating entry for key: " + key, e);
            throw new RuntimeException("Exception creating entry for key: " + key);
        }
    }

    @Override
    public Map loadAll(Collection keys, Object argument) {
        throw new UnsupportedOperationException("Can't load chunk without loader argument.");
    }

    @Override
    public String getName() {
        return "S3ChunkEntryFactory";
    }

    @Override
    public CacheLoader clone(Ehcache cache) throws CloneNotSupportedException {
        throw new UnsupportedOperationException("Can't load chunk without loader argument.");
    }

    @Override
    public void init() {}

    @Override
    public void dispose() throws CacheException {}

    @Override
    public Status getStatus() {
        return null;
    }
}
