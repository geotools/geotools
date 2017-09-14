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
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.logging.Logger;

/**
 * Handles connecting to S3 and fetching parts
 */
public class S3Connector {
    private final static Logger LOGGER = Logger
            .getLogger(S3Utils.class.getName());

    private String regionString;
    private boolean useAnon = false;

    public S3Connector(String regionString, boolean useAnon) {
        this.regionString = regionString;
        this.useAnon = useAnon;
    }

    /**
     * Create an S3 connector from a URI-ish S3:// string. Notably, this constructor supports awsRegion and useAnon
     * as query parameters to control these settings.
     *
     * Also of note, this URL is largely ignored outside of the query parameters. Mainly this is used to control
     * authentication options
     *
     * @param input an s3:// style URL.
     */
    S3Connector(String input) {
        //Parse region and anon from URL
        try {
            URI s3Uri = new URI(input);
            List<NameValuePair> nameValuePairs = URLEncodedUtils.parse(s3Uri, "UTF-8");

            for (NameValuePair nvPair : nameValuePairs) {
                if ("awsRegion".equals(nvPair.getName())) {
                    this.regionString = nvPair.getValue();
                }

                if ("useAnon".equals(nvPair.getName())) {
                    this.useAnon = Boolean.parseBoolean(nvPair.getValue());
                }
            }

        } catch (URISyntaxException e) {
            LOGGER.finer("Error parsing S3 URL in order to parse query options");
        }
    }

    public AmazonS3 getS3Client() {
        Regions region;
        if (this.regionString != null) {
            try {
                region = Regions.valueOf(regionString);
            } catch (IllegalArgumentException e) {
                //probably not great to have a default, but we can't just blow up if this
                //property isn't set
                LOGGER.warning("AWS_REGION property is set, but not set correctly. "
                        + "Check that the AWS_REGION property matches the Regions enum");
                region = Regions.US_EAST_1;
            }
        } else {
            LOGGER.warning("No AWS_REGION property set, defaulting to US_EAST_1");
            region = Regions.US_EAST_1;
        }

        AmazonS3 s3;
        if (useAnon) {
            s3 = new AmazonS3Client(new AnonymousAWSCredentials());
        } else {
            s3 = new AmazonS3Client();
        }

        s3.setRegion(Region.getRegion(region));

        return s3;
    }

    /**
     *
     * @param s3Path the s3:// url style path
     * @return bucket and key parts of the given S3 path, IN THAT ORDER
     */
    public static String[] getS3PathParts(String s3Path) {
        String[] parts = s3Path.split("/");
        StringBuilder keyBuilder = new StringBuilder();

        String bucket = parts[2];
        for (int i=3; i < parts.length; i++ ) {
            keyBuilder.append("/").append(parts[i]);
        }
        String key = keyBuilder.toString();
        key = key.startsWith("/") ? key.substring(1) : key;

        return new String[] { bucket, key };
    }
}
