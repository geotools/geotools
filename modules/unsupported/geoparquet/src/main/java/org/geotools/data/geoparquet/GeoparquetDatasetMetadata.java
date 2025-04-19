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

import static java.util.Objects.requireNonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;
import org.geotools.api.referencing.FactoryException;
import org.geotools.api.referencing.crs.CoordinateReferenceSystem;
import org.geotools.geometry.jts.ReferencedEnvelope;
import org.geotools.jackson.datatype.geoparquet.GeoParquetMetadata;
import org.geotools.jackson.datatype.geoparquet.GeoParquetModule;
import org.geotools.jackson.datatype.geoparquet.Geometry;
import org.geotools.jackson.datatype.projjson.ProjJSONHelper;
import org.geotools.referencing.CRS;
import org.locationtech.jts.geom.Envelope;

/**
 * Represents metadata for an entire GeoParquet dataset, which may contain multiple files.
 *
 * <p>This class collects and aggregates metadata from all files in a dataset, providing unified access to bounds, CRS
 * information, and other dataset-wide properties. When dealing with a directory of GeoParquet files, this class
 * provides a consolidated view of the metadata across all files.
 *
 * <p>Key functionality includes:
 *
 * <ul>
 *   <li>Computing unified bounds across all files in the dataset
 *   <li>Providing access to CRS information
 *   <li>Supporting directory-based datasets with multiple GeoParquet files
 * </ul>
 */
public class GeoparquetDatasetMetadata {

    /** Shared ObjectMapper instance for CRS parsing */
    private static final ObjectMapper MAPPER = GeoParquetModule.createObjectMapper();

    /** Map of file names to their parsed GeoParquet metadata */
    private Map<String, GeoParquetMetadata> md;

    /**
     * Creates a new dataset metadata instance from a map of individual file metadata.
     *
     * @param md Map of file names to their GeoParquet metadata
     */
    public GeoparquetDatasetMetadata(Map<String, GeoParquetMetadata> md) {
        this.md = Map.copyOf(requireNonNull(md));
    }

    public boolean isEmpty() {
        return md.isEmpty();
    }

    public Optional<String> getPrimaryColumnName() {
        return sample().map(GeoParquetMetadata::getPrimaryColumn);
    }

    public Optional<Geometry> getPrimaryColumn() {
        return getPrimaryColumnName().flatMap(this::getColumn);
    }

    public Optional<Geometry> getColumn(String column) {
        return sample().flatMap(md -> md.getColumn(column));
    }

    /**
     * Gets the CRS for the primary geometry column in the dataset.
     *
     * <p>This method extracts the CRS information from the GeoParquet metadata's 'geo' field for the primary geometry
     * column. The CRS is represented in PROJJSON format (v0.7 schema) with proper handling of CRS identifiers with
     * authority and code properties.
     *
     * <p>The implementation converts the PROJJSON representation to a GeoTools CoordinateReferenceSystem object for use
     * with GeoTools spatial operations.
     *
     * @return The CRS from the primary geometry column, or EPSG:4326 if not available
     */
    public CoordinateReferenceSystem getCrs() {
        return getPrimaryColumn()
                .map(Geometry::getCrs)
                .map(this::parseCrsObject)
                .orElseGet(this::defaultCrs);
    }

    /**
     * Gets the CRS for a specific geometry column in the dataset.
     *
     * <p>This method extracts the CRS information from the GeoParquet metadata's 'geo' field for the specified geometry
     * column. The CRS is represented in PROJJSON format (v0.7 schema) with proper handling of CRS identifiers with
     * authority and code properties.
     *
     * <p>The implementation converts the PROJJSON representation to a GeoTools CoordinateReferenceSystem object for use
     * with GeoTools spatial operations.
     *
     * <p>This allows for supporting multiple geometry columns with different CRS in the same dataset.
     *
     * @param columnName Name of the column to get CRS for
     * @return The CRS for the specified column, or EPSG:4326 if not available
     */
    public CoordinateReferenceSystem getCrs(String columnName) {
        return getColumn(columnName)
                .map(Geometry::getCrs)
                .map(this::parseCrsObject)
                .orElseGet(this::defaultCrs);
    }

    /**
     * Converts a PROJJSON model CRS to a GeoTools CRS.
     *
     * <p>This method handles the conversion between the strongly-typed PROJJSON model CRS (following v0.7 schema) from
     * the GeoParquet 'geo' metadata and GeoTools CoordinateReferenceSystem objects. This includes proper handling of
     * the CRS identifier with authority and code properties.
     *
     * <p>The conversion process:
     *
     * <ol>
     *   <li>Serializes the PROJJSON object model to a JSON string
     *   <li>Uses ProjJSONHelper to parse the JSON and create a GeoTools CRS
     * </ol>
     *
     * @param crs The PROJJSON model CRS object
     * @return The parsed GeoTools CoordinateReferenceSystem or null if conversion fails
     */
    private CoordinateReferenceSystem parseCrsObject(
            org.geotools.jackson.datatype.projjson.model.CoordinateReferenceSystem crs) {
        if (crs == null) {
            return null;
        }

        try {
            // Convert to JSON string
            String projjson = MAPPER.writeValueAsString(crs);
            // Parse with the helper that converts to GeoTools CRS
            return ProjJSONHelper.parseCRS(projjson);
        } catch (Exception e) {
            return null;
        }
    }

    private Optional<GeoParquetMetadata> sample() {
        return md.values().stream().findFirst();
    }

    private CoordinateReferenceSystem defaultCrs() {
        try {
            return CRS.decode("EPSG:4326", true);
        } catch (FactoryException e) {
            throw new IllegalStateException(e);
        }
    }

    public ReferencedEnvelope getBounds() {
        Stream<Envelope> allFilesBounds = md.values().stream().sequential().map(GeoParquetMetadata::bounds);
        Envelope fullBounds = allFilesBounds.reduce(new Envelope(), (b1, b2) -> {
            b1.expandToInclude(b2);
            return b1;
        });
        CoordinateReferenceSystem crs = getCrs();
        return new ReferencedEnvelope(fullBounds, crs);
    }
}
