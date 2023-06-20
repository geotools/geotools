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
package org.geotools.s3;

import com.amazonaws.services.s3.AmazonS3;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class S3ConnectorTest {
    @Test
    public void testDefaultRegionWithDefaultCredentialChain() {
        S3Connector s3Connector = new S3Connector("s3://bucket/prefix/raster.tif");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_Standard", s3Client.getRegion().name());
    }

    @Test
    public void testRegionOverrideWithDefaultCredentialChain() {
        S3Connector s3Connector = new S3Connector("s3://bucket/prefix/raster.tif?awsRegion=US_EAST_2");

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
        S3Connector s3Connector = new S3Connector("s3://bucket/prefix/raster.tif?useAnon=true&awsRegion=US_EAST_2");

        AmazonS3 s3Client = s3Connector.getS3Client();
        assertEquals("US_East_2", s3Client.getRegion().name());
    }
}