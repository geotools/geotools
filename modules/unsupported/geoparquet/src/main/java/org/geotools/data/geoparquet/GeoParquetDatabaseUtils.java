/*
 *    GeoTools - The Open Source Java GIS Toolkit
 *    http://geotools.org
 *
 *    (C) 2002-2025, Open Source Geospatial Foundation (OSGeo)
 *
 *    This library is free software; you can redistribute it and/or
 *    modify it under the terms of the GNU Lesser General Public
 *    License as published by the Free Software Foundation;
 *    version 2.1 of the License.
 *
 *    This library is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 *    Lesser General Public License for more details.
 */
package org.geotools.data.geoparquet;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Map;
import org.geotools.api.data.DataAccessFactory;
import org.geotools.api.data.DataAccessFactory.Param;

/**
 * Utility class for managing DuckDB database files for GeoParquet DataStore.
 *
 * <p>This class provides methods for creating and managing temporary in-memory database URLs by set of connection
 * parameters. This way, datastore instances with the same connection parameters will get the same JDBC url, and
 * different connection parameters get different JDBC urls.
 */
final class GeoParquetDatabaseUtils {

    /**
     * Gets the JDBC URL for a GeoParquet DataStore configuration.
     *
     * <p>This is a convenience method that combines getting the temporary database file and constructing the JDBC URL
     * in a single step.
     *
     * @param params The DataStore configuration parameters
     * @param parametersInfo The parameter definitions for the DataStore
     * @return A JDBC URL pointing to the database file for this configuration
     * @throws IOException If there's an error creating the temporary directory
     */
    public static String getJDBCUrl(Map<String, ?> params, DataAccessFactory.Param[] parametersInfo)
            throws IOException {
        String configString = buildConfigString(params, parametersInfo);
        String hash = computeConfigHash(configString);
        return "jdbc:duckdb::memory:" + hash;
    }

    /** Private constructor to prevent instantiation of utility class. */
    private GeoParquetDatabaseUtils() {
        // Utility class should not be instantiated
    }

    private static String buildConfigString(Map<String, ?> params, Param[] parametersInfo) throws IOException {
        // Create a configuration key that includes all relevant parameters
        StringBuilder configKey = new StringBuilder();

        // Sort parameters by key for deterministic ordering
        Arrays.sort(parametersInfo, (p1, p2) -> p1.key.compareTo(p2.key));

        // Add each parameter's value to the config key
        for (DataAccessFactory.Param param : parametersInfo) {
            String key = param.key;
            Object value = param.lookUp(params);

            // Add parameter to the key even if null (to distinguish between null and not
            // present)
            configKey.append(key).append('=');
            if (value != null) {
                configKey.append(value.toString());
            } else {
                configKey.append("null");
            }
            configKey.append(';');
        }

        return configKey.toString();
    }

    /**
     * Computes a stable hash for a configuration string.
     *
     * <p>This method uses SHA-256 for a strong, collision-resistant hash.
     *
     * @param configString The configuration string to hash
     * @return A string representation of the hash
     * @throws IOException If SHA-256 algorithm is not available
     */
    private static String computeConfigHash(String configString) throws IOException {
        MessageDigest md;
        try {
            md = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new IOException("Failed to create SHA-256 hash for " + configString);
        }
        byte[] digest = md.digest(configString.getBytes(StandardCharsets.UTF_8));
        return bytesToHex(digest);
    }

    /**
     * Converts a byte array to a hexadecimal string representation.
     *
     * <p>This method is used instead of relying on external libraries like javax.xml.bind.DatatypeConverter, which is
     * not available in all Java versions.
     *
     * @param bytes The byte array to convert
     * @return A lowercase hexadecimal string representation of the byte array
     */
    private static String bytesToHex(byte[] bytes) {
        StringBuilder hexString = new StringBuilder(2 * bytes.length);
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }
}
