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

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the geospatial metadata for a GeoParquet file conforming to version 1.2.0-dev of the GeoParquet
 * specification. This class extends the common metadata structure and fixes the version to "1.2.0-dev" as required by
 * the schema. The example below includes most optional fields to illustrate the full range of metadata supported.
 *
 * <pre>
 * <code>
 * {
 *   "version": "1.2.0-dev",
 *   "primary_column": "geometry",
 *   "columns": {
 *     "geometry": {
 *       "encoding": "WKB",
 *       "geometry_types": ["Polygon", "MultiPolygon", "GeometryCollection"],
 *       "crs": null,
 *       "edges": "planar",
 *       "orientation": "counterclockwise",
 *       "bbox": [5.8539652, 47.2737101, 0.0, 15.0171958, 55.05256, 100.0],
 *       "epoch": 2021.0,
 *       "covering": {
 *         "bbox": {
 *           "xmin": ["x_coord", "xmin"],
 *           "xmax": ["x_coord", "xmax"],
 *           "ymin": ["y_coord", "ymin"],
 *           "ymax": ["y_coord", "ymax"]
 *         }
 *       }
 *     },
 *     "lines": {
 *       "encoding": "linestring",
 *       "geometry_types": ["LineString"]
 *     }
 *   }
 * }
 * </code>
 * </pre>
 */
public class GeoParquetMetadataV1_2_0Dev extends GeoParquetMetadata {

    /** Constructs a new instance with the version fixed to "1.2.0-dev". */
    public GeoParquetMetadataV1_2_0Dev() {
        this.version = "1.2.0-dev";
    }

    /**
     * Gets the GeoParquet specification version (always "1.2.0-dev").
     *
     * @return the version string, fixed at "1.2.0-dev"
     */
    @Override
    @JsonProperty(value = "version", required = true)
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version, though it should only be "1.2.0-dev" per the schema.
     *
     * @param version the version string
     */
    @Override
    public void setVersion(String version) {
        if (!"1.2.0-dev".equals(version)) {
            throw new IllegalArgumentException("Version must be '1.2.0-dev' for this class");
        }
        this.version = version;
    }
}
