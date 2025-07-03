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
package org.geotools.jackson.datatype.geoparquet;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * Represents the geospatial metadata for a GeoParquet file conforming to version 1.1.0 of the GeoParquet specification.
 * This class extends the common metadata structure and fixes the version to "1.1.0" as required by the schema. The
 * example below includes most optional fields to illustrate the full range of metadata supported.
 *
 * <pre>
 * <code>
 * {
 *   "version": "1.1.0",
 *   "primary_column": "geometry",
 *   "columns": {
 *     "geometry": {
 *       "encoding": "WKB",
 *       "geometry_types": ["Polygon", "MultiPolygon", "Point Z"],
 *       "crs": {"$schema": "https://proj.org/schemas/v0.7/projjson.schema.json", "type": "GeographicCRS", "name": "WGS 84"},
 *       "edges": "spherical",
 *       "orientation": "counterclockwise",
 *       "bbox": [5.8539652, 47.2737101, 15.0171958, 55.05256],
 *       "epoch": 2020.5,
 *       "covering": {
 *         "bbox": {
 *           "xmin": ["lon", "xmin"],
 *           "xmax": ["lon", "xmax"],
 *           "ymin": ["lat", "ymin"],
 *           "ymax": ["lat", "ymax"]
 *         }
 *       }
 *     },
 *     "points": {
 *       "encoding": "point",
 *       "geometry_types": ["Point"]
 *     }
 *   }
 * }
 * </code>
 * </pre>
 */
public class GeoParquetMetadataV1_1_0 extends GeoParquetMetadata {

    /** Constructs a new instance with the version fixed to "1.1.0". */
    public GeoParquetMetadataV1_1_0() {
        this.version = "1.1.0";
    }

    /**
     * Gets the GeoParquet specification version (always "1.1.0").
     *
     * @return the version string, fixed at "1.1.0"
     */
    @Override
    @JsonProperty(value = "version", required = true)
    public String getVersion() {
        return version;
    }

    /**
     * Sets the version, though it should only be "1.1.0" per the schema.
     *
     * @param version the version string
     */
    @Override
    public void setVersion(String version) {
        if (!"1.1.0".equals(version)) {
            throw new IllegalArgumentException("Version must be '1.1.0' for this class");
        }
        this.version = version;
    }
}
