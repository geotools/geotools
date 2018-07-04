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

import com.amazonaws.auth.AnonymousAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import java.util.logging.Logger;

/** Utility methods for S3 access */
public class S3Utils {

    private static final Logger LOGGER = Logger.getLogger(S3Utils.class.getName());

    private static AmazonS3 getS3Client() {
        Regions region;
        if (System.getProperty("AWS_REGION") != null) {
            try {
                region = Regions.valueOf(System.getProperty("AWS_REGION"));
            } catch (IllegalArgumentException e) {
                // probably not great to have a default, but we can't just blow up if this
                // property isn't set
                LOGGER.warning(
                        "AWS_REGION property is set, but not set correctly. "
                                + "Check that the AWS_REGION property matches the Regions enum");
                region = Regions.US_EAST_1;
            }
        } else {
            LOGGER.warning("No AWS_REGION property set, defaulting to US_EAST_1");
            region = Regions.US_EAST_1;
        }

        AmazonS3 s3;
        if (Boolean.getBoolean("S3_USE_ANON")) {
            s3 = new AmazonS3Client(new AnonymousAWSCredentials());
        } else {
            s3 = new AmazonS3Client();
        }

        s3.setRegion(Region.getRegion(region));

        return s3;
    }

    /**
     * @param s3Path the s3:// url style path
     * @return bucket and key parts of the given S3 path, IN THAT ORDER
     */
    public static String[] getS3PathParts(String s3Path) {
        String[] parts = s3Path.split("/");
        StringBuilder keyBuilder = new StringBuilder();

        String bucket = parts[2];
        for (int i = 3; i < parts.length; i++) {
            keyBuilder.append("/").append(parts[i]);
        }
        String key = keyBuilder.toString();
        key = key.startsWith("/") ? key.substring(1) : key;

        return new String[] {bucket, key};
    }
}
