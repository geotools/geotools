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

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.locationtech.jts.geom.Envelope;

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

    private static final ObjectMapper objectMapper = new ObjectMapper();

    @JsonProperty(value = "version", required = true)
    protected String version;

    @JsonProperty(value = "primary_column", required = true)
    protected String primaryColumn;

    @JsonProperty(value = "columns", required = true)
    protected Map<String, Geometry> columns;

    public static GeoParquetMetadata readValue(String geo) throws IOException {
        return objectMapper.readValue(geo, GeoParquetMetadata.class);
    }

    Envelope bounds() {
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

    /**
     * Represents the metadata for a geometry column in a GeoParquet file, including required encoding and geometry
     * types, and optional attributes like bounding box, CRS, and edge type.
     */
    public static class Geometry {
        @JsonProperty(value = "encoding", required = true)
        protected String encoding;

        @JsonProperty(value = "geometry_types", required = true)
        protected List<String> geometryTypes;

        @JsonProperty("crs")
        protected Object crs;

        @JsonProperty("edges")
        protected String edges;

        @JsonProperty("orientation")
        protected String orientation;

        @JsonProperty("bbox")
        protected List<Double> bbox;

        @JsonProperty("epoch")
        protected Double epoch;

        @JsonProperty("covering")
        protected Covering covering;

        public Envelope bounds() {
            List<Double> bb = getBbox();
            if (bb == null || bb.size() < 4) {
                return new Envelope();
            }
            double minx = bb.get(0);
            double miny = bb.get(1);
            double maxx = bb.get(bb.size() == 6 ? 4 : 2);
            double maxy = bb.get(bb.size() == 6 ? 5 : 3);
            return new Envelope(minx, maxx, miny, maxy);
        }

        public String getEncoding() {
            return encoding;
        }

        public void setEncoding(String encoding) {
            this.encoding = encoding;
        }

        public List<String> getGeometryTypes() {
            return geometryTypes;
        }

        public void setGeometryTypes(List<String> geometryTypes) {
            this.geometryTypes = geometryTypes;
        }

        public Object getCrs() {
            return crs;
        }

        public void setCrs(Object crs) {
            this.crs = crs;
        }

        public String getEdges() {
            return edges;
        }

        public void setEdges(String edges) {
            this.edges = edges;
        }

        public String getOrientation() {
            return orientation;
        }

        public void setOrientation(String orientation) {
            this.orientation = orientation;
        }

        public List<Double> getBbox() {
            return bbox;
        }

        public void setBbox(List<Double> bbox) {
            this.bbox = bbox;
        }

        public Double getEpoch() {
            return epoch;
        }

        public void setEpoch(Double epoch) {
            this.epoch = epoch;
        }

        public Covering getCovering() {
            return covering;
        }

        public void setCovering(Covering covering) {
            this.covering = covering;
        }
    }

    /** Represents the covering metadata for a geometry column. */
    public static class Covering {
        @JsonProperty(value = "bbox", required = true)
        protected BboxCovering bbox;

        public BboxCovering getBbox() {
            return bbox;
        }

        public void setBbox(BboxCovering bbox) {
            this.bbox = bbox;
        }
    }

    /** Represents the bounding box covering structure, mapping min/max coordinates to column names. */
    public static class BboxCovering {
        @JsonProperty(value = "xmin", required = true)
        protected List<String> xmin;

        @JsonProperty(value = "xmax", required = true)
        protected List<String> xmax;

        @JsonProperty(value = "ymin", required = true)
        protected List<String> ymin;

        @JsonProperty(value = "ymax", required = true)
        protected List<String> ymax;

        public List<String> getXmin() {
            return xmin;
        }

        public void setXmin(List<String> xmin) {
            this.xmin = xmin;
        }

        public List<String> getXmax() {
            return xmax;
        }

        public void setXmax(List<String> xmax) {
            this.xmax = xmax;
        }

        public List<String> getYmin() {
            return ymin;
        }

        public void setYmin(List<String> ymin) {
            this.ymin = ymin;
        }

        public List<String> getYmax() {
            return ymax;
        }

        public void setYmax(List<String> ymax) {
            this.ymax = ymax;
        }
    }
}
