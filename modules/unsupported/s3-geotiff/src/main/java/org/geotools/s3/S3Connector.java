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
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.S3ClientOptions;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.http.NameValuePair;
import org.apache.http.client.utils.URLEncodedUtils;

/** Handles connecting to S3 and fetching parts */
public class S3Connector {
    private static final Logger LOGGER = Logger.getLogger(S3Utils.class.getName());

    // The location of the propertie file
    public static final String S3_GEOTIFF_CONFIG_PATH = "s3.properties.location";

    private Properties prop;

    private String url;

    private String regionString;

    private boolean useAnon = false;

    public S3Connector(String regionString, boolean useAnon) {
        this.regionString = regionString;
        this.useAnon = useAnon;
    }

    /**
     * Create an S3 connector from a URI-ish S3:// string. Notably, this constructor supports
     * awsRegion and useAnon as query parameters to control these settings.
     *
     * <p>Also of note, this URL is largely ignored outside of the query parameters. Mainly this is
     * used to control authentication options
     *
     * @param input an s3:// style URL.
     */
    S3Connector(String input) {
        this.url = input;
        // Parse region and anon from URL
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
        // custom endpoint
        if (url != null && !url.startsWith("s3://")) {
            if (!url.contains("://")) {
                throw new IllegalArgumentException(
                        "Following this style: s3Alias://bucket/filename");
            }
            String s3Alias = url.split("://")[0];
            String pahtToFile = url.split("://")[1];

            Properties prop = readProperties(s3Alias);

            s3 =
                    new AmazonS3Client(
                            new BasicAWSCredentials(
                                    prop.getProperty(s3Alias + ".s3.user"),
                                    prop.getProperty(s3Alias + ".s3.password")));

            final S3ClientOptions clientOptions =
                    S3ClientOptions.builder().setPathStyleAccess(true).build();
            s3.setS3ClientOptions(clientOptions);
            String endpoint = prop.getProperty(s3Alias + ".s3.endpoint");
            if (!endpoint.endsWith("/")) {
                endpoint = endpoint + "/";
            }
            s3.setEndpoint(endpoint);

            // aws cli client
        } else if (useAnon) {
            s3 = new AmazonS3Client(new AnonymousAWSCredentials());
            s3.setRegion(Region.getRegion(region));
        } else {
            s3 = new AmazonS3Client();
            s3.setRegion(Region.getRegion(region));
        }

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

    private Properties readProperties(String s3Alias) {
        try {
            if (prop == null) {
                String property = System.getProperty(S3_GEOTIFF_CONFIG_PATH);
                if (property != null) {
                    prop = new Properties();
                    InputStream resourceAsStream = new FileInputStream(property);
                    prop.load(resourceAsStream);
                } else {
                    throw new IOException(
                            "Properties are missing! "
                                    + "The system property 's3.properties.location' should be set "
                                    + "and contain the path to the s3.properties file.");
                }
            }
            // check if the properties are not null.
            if (prop.getProperty(s3Alias + ".s3.user") == null) {
                throw new IllegalArgumentException(
                        "s3.properties file does not contains value for:" + s3Alias + ".s3.user");
            }
            if (prop.getProperty(s3Alias + ".s3.password") == null) {
                throw new IllegalArgumentException(
                        "s3.properties file does not contains value for:"
                                + s3Alias
                                + ".s3.password");
            }
            if (prop.getProperty(s3Alias + ".s3.endpoint") == null) {
                throw new IllegalArgumentException(
                        "s3.properties file does not contains value for:"
                                + s3Alias
                                + ".s3.endpoint");
            }
        } catch (IOException ex) {
            LOGGER.severe(ex.getMessage());
            throw new IllegalArgumentException("The properties could not be found.", ex);
        }
        return prop;
    }
}
