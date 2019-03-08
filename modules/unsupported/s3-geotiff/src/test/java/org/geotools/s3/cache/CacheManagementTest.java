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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.InputStream;
import org.geotools.s3.S3Connector;
import org.junit.After;
import org.junit.Test;

public class CacheManagementTest {

    public @After void after() {
        System.clearProperty(CacheConfig.S3_CACHING_EH_CACHE_CONFIG);
    }

    @Test
    public void testLoadExternalFile() throws Exception {
        File cacheConfigFile = new File(getClass().getResource("s3-geotiff-ehcache.xml").toURI());
        assertTrue(cacheConfigFile.exists());
        String configurationPath = cacheConfigFile.getAbsolutePath();
        System.setProperty(CacheConfig.S3_CACHING_EH_CACHE_CONFIG, configurationPath);

        CacheConfig cacheConfig = CacheManagement.DEFAULT.getCacheConfig();
        assertEquals(configurationPath, cacheConfig.getConfigurationPath());

        byte[] content = new byte[128];
        InputStream objectContent = new ByteArrayInputStream(content);
        S3Object value = new S3Object();
        value.setObjectContent(objectContent);

        AmazonS3 s3Client = mock(AmazonS3.class);
        when(s3Client.getObject(any(GetObjectRequest.class))).thenReturn(value);
        S3Connector connector = mock(S3Connector.class);
        when(connector.getS3Client()).thenReturn(s3Client);

        int block = 1;
        int blockSize = 512;
        CacheEntryKey key = new CacheEntryKey("bucket", "key", block, blockSize);
        byte[] chunk = CacheManagement.DEFAULT.getChunk(key, connector);
        assertEquals(content.length, chunk.length);
    }
}
