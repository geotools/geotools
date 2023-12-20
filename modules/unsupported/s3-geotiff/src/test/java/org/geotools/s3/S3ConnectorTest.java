/*
 * GeoTools - The Open Source Java GIS Toolkit
 * http://geotools.org
 *
 * (C) 2023, Open Source Geospatial Foundation (OSGeo)
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
package org.geotools.s3;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Before;
import org.junit.Test;

public class S3ConnectorTest {

    @Before
    public void before() {
        System.setProperty(
                S3Connector.S3_GEOTIFF_CONFIG_PATH, "./src/test/resources/s3.properties");
    }

    @Test
    public void testDefaultRegionWithDefaultCredentialChain() {
        S3Connector s3Connector = new S3Connector("s3://bucket/prefix/raster.tif");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_Standard", s3Client.getRegion().name());
    }

    @Test
    public void testRegionOverrideWithDefaultCredentialChain() {
        S3Connector s3Connector =
                new S3Connector("s3://bucket/prefix/raster.tif?awsRegion=US_EAST_2");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_East_2", s3Client.getRegion().name());
    }

    @Test
    public void testDefaultRegionWithAnonymousCredentialChain() {
        S3Connector s3Connector = new S3Connector("s3://bucket/prefix/raster.tif?useAnon=true");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_Standard", s3Client.getRegion().name());
    }

    @Test
    public void testRegionOverrideWithAnonymousCredentialChain() {
        S3Connector s3Connector =
                new S3Connector("s3://bucket/prefix/raster.tif?useAnon=true&awsRegion=US_EAST_2");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_East_2", s3Client.getRegion().name());
    }

    @Test
    public void testDefaultRegionWithCustomEndpointChain() {
        S3Connector s3Connector = new S3Connector("other://bucket/prefix/raster.tif");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_Standard", s3Client.getRegion().name());
        assertEquals(
                "http://your-other-s3-server/bucket/prefix",
                s3Client.getUrl("bucket", "prefix").toString());
    }

    @Test
    public void testRegionOverrideWithCustomEndpointChain() {
        S3Connector s3Connector =
                new S3Connector("other://bucket/prefix/raster.tif?awsRegion=US_EAST_2");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_East_2", s3Client.getRegion().name());
        assertEquals(
                "http://your-other-s3-server/bucket/prefix",
                s3Client.getUrl("bucket", "prefix").toString());
    }

    @Test
    public void throwsWithInvalidCustomEndpoint() {
        S3Connector s3Connector = new S3Connector("invalid/url/raster.tif");

        assertThrows(
                "Following this style: s3Alias://bucket/filename",
                IllegalArgumentException.class,
                s3Connector::getS3Client);
    }
}
