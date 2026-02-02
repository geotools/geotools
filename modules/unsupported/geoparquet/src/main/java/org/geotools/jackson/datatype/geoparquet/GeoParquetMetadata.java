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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.io.IOException;
import java.util.Map;
import java.util.Optional;
import org.locationtech.jts.geom.Envelope;
import tools.jackson.databind.ObjectMapper;

/**
 * Base class for GeoParquet metadata, representing the common structure of the "geo" metadata field in a GeoParquet
 * file. This includes the version, primary geometry column, and column metadata. Subclasses define specific version
 * constraints (e.g., 1.1.0, 1.2.0-dev).
 *
 * <p>The GeoParquet metadata is stored as a key-value metadata entry in the Parquet file, with the key "geo" and the
 * value being a JSON string. This class and its subclasses provide the object mapping for that JSON data structure,
 * following the GeoParquet specification.
 *
 * <p>Key components of the metadata include:
 *
 * <ul>
 *   <li>Specification version (e.g., "1.1.0")
 *   <li>Primary geometry column name
 *   <li>Geometry encoding details
 *   <li>Geometry types supported in each geometry column
 *   <li>CRS information
 *   <li>Bounding box data
 *   <li>Optional covering information for spatial indexing
 * </ul>
 *
 * <p>This implementation handles deserialization of the metadata and provides access to its various components,
 * enabling optimized operations such as bounds calculation and schema detection.
 *
 * @see <a href="https://github.com/opengeospatial/geoparquet/blob/main/format-specs/metadata.md">GeoParquet Metadata
 *     Specification</a>
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "version",
        defaultImpl = GeoParquetMetadataV1_1_0.class // Default to 1.1.0 if version is missing
        )
@JsonSubTypes({
    @JsonSubTypes.Type(value = GeoParquetMetadataV1_1_0.class, name = "1.1.0"),
    @JsonSubTypes.Type(value = GeoParquetMetadataV1_2_0Dev.class, name = "1.2.0-dev")
})
public abstract class GeoParquetMetadata {

    private static final ObjectMapper objectMapper = GeoParquetModule.createObjectMapper();

    @JsonProperty(value = "version", required = true)
    protected String version;

    @JsonProperty(value = "primary_column", required = true)
    protected String primaryColumn;

    @JsonProperty(value = "columns", required = true)
    protected Map<String, Geometry> columns;

    /**
     * Reads GeoParquet metadata from a JSON string.
     *
     * @param geo the JSON string containing the metadata
     * @return the parsed GeoParquetMetadata object
     * @throws IOException if the JSON cannot be parsed
     */
    public static GeoParquetMetadata readValue(String geo) throws IOException {
        return objectMapper.readValue(geo, GeoParquetMetadata.class);
    }

    /**
     * Gets the bounds of the primary geometry column.
     *
     * @return The envelope representing the bounds of the primary geometry column, or an empty envelope if bounds are
     *     not available
     */
    public Envelope bounds() {
        // shouldn't be null by spec but who knows...
        return Optional.ofNullable(columns.get(primaryColumn))
                .map(Geometry::bounds)
                .orElseGet(Envelope::new);
    }

    /**
     * Gets the GeoParquet specification version.
     *
     * @return the version string
     */
    public String getVersion() {
        return version;
    }

    /**
     * Sets the GeoParquet specification version.
     *
     * @param version the version string
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Gets the name of the primary geometry column.
     *
     * @return the primary column name
     */
    public String getPrimaryColumn() {
        return primaryColumn;
    }

    /**
     * Sets the name of the primary geometry column (must be non-empty).
     *
     * @param primaryColumn the primary column name
     */
    public void setPrimaryColumn(String primaryColumn) {
        this.primaryColumn = primaryColumn;
    }

    /**
     * Gets the geometry metadata for a specific column.
     *
     * @param column the name of the column
     * @return an Optional containing the column's geometry metadata, or empty if the column doesn't exist
     */
    public Optional<Geometry> getColumn(String column) {
        return Optional.ofNullable(columns).map(cols -> cols.get(column));
    }

    /**
     * Gets the column metadata, mapping column names to their geometry details.
     *
     * @return a map of column names to geometry metadata
     */
    public Map<String, Geometry> getColumns() {
        return columns;
    }

    /**
     * Sets the column metadata (must contain at least one entry).
     *
     * @param columns a map of column names to geometry metadata
     */
    public void setColumns(Map<String, Geometry> columns) {
        this.columns = columns;
    }
}
