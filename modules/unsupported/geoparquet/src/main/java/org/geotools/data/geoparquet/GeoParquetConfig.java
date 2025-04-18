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

import static org.geotools.data.geoparquet.GeoParquetDataStoreFactory.MAX_HIVE_DEPTH;
import static org.geotools.data.geoparquet.GeoParquetDataStoreFactory.URI_PARAM;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Map;
import java.util.Objects;

/**
 * Configuration class for GeoParquet DataStore.
 *
 * <p>This class encapsulates the configuration parameters required to initialize and connect to a GeoParquet data
 * source. It handles validation and parsing of configuration values from the parameter map provided when creating a
 * DataStore instance.
 *
 * <p>Primary configuration parameters include:
 *
 * <ul>
 *   <li>targetUri - The URI pointing to the GeoParquet file(s)
 *   <li>maxHiveDepth - Controls how many levels of Hive partitioning to consider
 * </ul>
 *
 * <p>The class provides methods to create configuration objects from parameter maps and access the configured values in
 * a type-safe manner.
 */
class GeoParquetConfig {

    private URI targetUri;
    private Integer maxHiveDepth;

    /**
     * Constructs a new GeoParquetConfig.
     *
     * @param uri The target URI pointing to the GeoParquet file(s)
     * @param maxHiveDepth The maximum depth of Hive partitioning to consider (null for unlimited)
     */
    public GeoParquetConfig(URI uri, Integer maxHiveDepth) {
        this.targetUri = uri;
        this.maxHiveDepth = maxHiveDepth;
    }

    /**
     * Creates a GeoParquetConfig from a parameter map.
     *
     * <p>This factory method extracts and validates configuration parameters from the provided map, typically provided
     * by the DataStoreFactory when creating a new DataStore instance.
     *
     * @param params Map of parameter name/value pairs
     * @return A validated GeoParquetConfig instance
     * @throws IOException If the parameters are invalid or incomplete
     */
    public static GeoParquetConfig valueOf(Map<String, ?> params) throws IOException {
        java.net.URI uri = lookupURI(params);
        Integer maxHiveDepth = (Integer) MAX_HIVE_DEPTH.lookUp(params);
        if (maxHiveDepth != null && maxHiveDepth < 0) {
            throw new IOException(MAX_HIVE_DEPTH.key + " is negative: " + maxHiveDepth);
        }
        return new GeoParquetConfig(uri, maxHiveDepth);
    }

    /**
     * Extracts and validates the URI parameter from the parameter map.
     *
     * @param params Map of parameter name/value pairs
     * @return The validated URI
     * @throws IOException If the URI parameter is missing or invalid
     */
    private static java.net.URI lookupURI(Map<String, ?> params) throws IOException {
        String lookUp = (String) Objects.requireNonNull(URI_PARAM.lookUp(params));
        try {
            return new URI(lookUp);
        } catch (URISyntaxException e) {
            throw new IOException(e);
        }
    }

    /**
     * Gets the target URI for the GeoParquet data source.
     *
     * @return The target URI
     */
    public URI getTargetUri() {
        return targetUri;
    }

    /**
     * Gets the maximum Hive partition depth to consider.
     *
     * <p>This controls how many levels of key=value directories are used for partitioning:
     *
     * <ul>
     *   <li>null: Use all partition levels found
     *   <li>0: No partitioning, treat all files as a single dataset
     *   <li>1+: Use this many levels of partitioning
     * </ul>
     *
     * @return The maximum Hive partition depth, or null for unlimited
     */
    public Integer getMaxHiveDepth() {
        return maxHiveDepth;
    }
}
