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
import static org.locationtech.jts.geom.Geometry.TYPENAME_GEOMETRYCOLLECTION;
import static org.locationtech.jts.geom.Geometry.TYPENAME_LINEARRING;
import static org.locationtech.jts.geom.Geometry.TYPENAME_LINESTRING;
import static org.locationtech.jts.geom.Geometry.TYPENAME_MULTILINESTRING;
import static org.locationtech.jts.geom.Geometry.TYPENAME_MULTIPOINT;
import static org.locationtech.jts.geom.Geometry.TYPENAME_MULTIPOLYGON;
import static org.locationtech.jts.geom.Geometry.TYPENAME_POINT;
import static org.locationtech.jts.geom.Geometry.TYPENAME_POLYGON;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
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
import org.locationtech.jts.geom.GeometryCollection;
import org.locationtech.jts.geom.LineString;
import org.locationtech.jts.geom.LinearRing;
import org.locationtech.jts.geom.MultiLineString;
import org.locationtech.jts.geom.MultiPoint;
import org.locationtech.jts.geom.MultiPolygon;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.Polygon;
import tools.jackson.databind.ObjectMapper;

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

    private static final Map<String, Class<? extends org.locationtech.jts.geom.Geometry>> GEOMNAME_TO_TYPE = Map.of(
            TYPENAME_POINT,
            Point.class,
            TYPENAME_MULTIPOINT,
            MultiPoint.class,
            TYPENAME_LINESTRING,
            LineString.class,
            TYPENAME_MULTILINESTRING,
            MultiLineString.class,
            TYPENAME_POLYGON,
            Polygon.class,
            TYPENAME_MULTIPOLYGON,
            MultiPolygon.class,
            TYPENAME_LINEARRING,
            LinearRing.class,
            TYPENAME_GEOMETRYCOLLECTION,
            GeometryCollection.class,
            "Geometry",
            org.locationtech.jts.geom.Geometry.class);

    /** Map of file names to their parsed GeoParquet metadata */
    private Map<String, GeoParquetMetadata> md;

    /** Map of geometry column names to JTS geometry types */
    private Map<String, Set<Class<? extends org.locationtech.jts.geom.Geometry>>> geometryTypes =
            new ConcurrentHashMap<>();

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
     * Determines the most specific geometry type for a geometry column.
     *
     * <p>This method analyzes the geometry types present in the dataset for the specified column and returns the most
     * specific common supertype. The process follows these steps:
     *
     * <ol>
     *   <li>If no types are available, return the generic Geometry class
     *   <li>If only one type is present, return that type
     *   <li>If the generic Geometry class is present, return that
     *   <li>If multiple base types are present (point, line, polygon), return GeometryCollection or Geometry
     *   <li>For each specialized type (point, line, polygon), return the most specific common type
     * </ol>
     *
     * <p>For example, if both Point and MultiPoint are present, the method will return the Puntal interface or
     * MultiPoint class. If only LineString is present, it will return LineString.
     *
     * @param column The geometry column name to get the narrowed type for
     * @return The most specific common geometry type for the column
     */
    public Class<? extends org.locationtech.jts.geom.Geometry> getNarrowedGeometryType(String column) {
        Set<Class<? extends org.locationtech.jts.geom.Geometry>> types = getGeometryTypes(column);

        // If empty, return the most generic type
        if (types.isEmpty()) {
            return org.locationtech.jts.geom.Geometry.class;
        }

        // If there's only one type, return it
        if (types.size() == 1) {
            return types.iterator().next();
        }

        // If we have the generic Geometry class, return that
        if (types.contains(org.locationtech.jts.geom.Geometry.class)) {
            return org.locationtech.jts.geom.Geometry.class;
        }

        boolean multi = false;
        boolean point = false;
        boolean line = false;
        boolean poly = false;

        for (Class<? extends org.locationtech.jts.geom.Geometry> c : types) {
            if (GeometryCollection.class.isAssignableFrom(c)) {
                multi = true;
            }
            if (org.locationtech.jts.geom.Puntal.class.isAssignableFrom(c)) {
                point = true;
            } else if (org.locationtech.jts.geom.Lineal.class.isAssignableFrom(c)) {
                line = true;
            } else if (org.locationtech.jts.geom.Polygonal.class.isAssignableFrom(c)) {
                poly = true;
            }
        }

        // If multiple base geometry types are present, return GeometryCollection or Geometry
        int baseTypeCount = (point ? 1 : 0) + (line ? 1 : 0) + (poly ? 1 : 0);
        if (baseTypeCount > 1) {
            return multi && types.contains(GeometryCollection.class)
                    ? GeometryCollection.class
                    : org.locationtech.jts.geom.Geometry.class;
        }

        // Now check for specific geometry types
        if (point) {
            return multi ? MultiPoint.class : Point.class;
        } else if (line) {
            return multi ? MultiLineString.class : LineString.class;
        } else if (poly) {
            return multi ? MultiPolygon.class : Polygon.class;
        } else if (multi) { // Fallback to GeometryCollection or Geometry
            return GeometryCollection.class;
        }
        // Default fallback
        return org.locationtech.jts.geom.Geometry.class;
    }

    /**
     * Gets the set of all geometry types present in a column across the dataset.
     *
     * <p>This method aggregates the geometry types from all files in the dataset for the specified column. If no column
     * name is provided, it uses the primary geometry column from the GeoParquet metadata.
     *
     * <p>The results are cached for performance to avoid repeated computation.
     *
     * @param column The name of the geometry column to get types for, or null to use the primary column
     * @return A set of geometry classes representing all types present in the column
     */
    public Set<Class<? extends org.locationtech.jts.geom.Geometry>> getGeometryTypes(String column) {
        if (column == null) {
            String defaultColumn = getPrimaryColumnName().orElse(null);
            if (defaultColumn == null) {
                return Set.of();
            }
            return getGeometryTypes(defaultColumn);
        }

        return geometryTypes.computeIfAbsent(column, c -> computeGeometryTypes(c));
    }

    /**
     * Computes the set of geometry types for a column from the GeoParquet metadata.
     *
     * <p>This method examines all files in the dataset, extracts the geometry types declared in the GeoParquet metadata
     * for the specified column, and converts them to JTS geometry classes.
     *
     * @param column The geometry column name to compute types for
     * @return A set of geometry classes representing all types present in the column
     */
    private Set<Class<? extends org.locationtech.jts.geom.Geometry>> computeGeometryTypes(String column) {
        Set<String> types = md.values().stream()
                .map(md -> md.getColumn(column))
                .filter(Optional::isPresent)
                .map(Optional::orElseThrow)
                .map(Geometry::getGeometryTypes)
                .flatMap(List::stream)
                .collect(Collectors.toSet());
        return types.stream().map(this::geomType).collect(Collectors.toSet());
    }

    /**
     * Converts a geometry type name from GeoParquet metadata to a JTS geometry class.
     *
     * <p>This method maps geometry type names from the GeoParquet metadata to the corresponding JTS geometry classes
     * using the GEOMNAME_TO_TYPE mapping.
     *
     * @param geometryType The name of the geometry type from GeoParquet metadata
     * @return The corresponding JTS geometry class
     */
    private Class<? extends org.locationtech.jts.geom.Geometry> geomType(String geometryType) {
        if (geometryType == null) return org.locationtech.jts.geom.Geometry.class;
        return GEOMNAME_TO_TYPE.get(geometryType);
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
