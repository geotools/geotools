# GeoParquet Test Resources

This directory contains test resources for the GeoParquet Jackson binding implementation.

## Files

- `example_metadata_1.1.json`: Example GeoParquet metadata, adhering to version 1.1.0 of the GeoParquet specification. This file is based on the official example from the GeoParquet specification repository.
- `example_metadata_1.2.0-dev.json`: Example GeoParquet metadata for the development version 1.2.0-dev, showcasing advanced features like 3D geometries, multiple geometry columns, and additional metadata fields.

## GeoParquet Metadata Structure

The example metadata follows the structure defined in the [GeoParquet Metadata Specification](https://github.com/opengeospatial/geoparquet/blob/main/format-specs/metadata.md). 

Key components include:
- `version`: The GeoParquet specification version (e.g., "1.1.0", "1.2.0-dev")
- `primary_column`: The name of the primary geometry column
- `columns`: Metadata for each geometry column, including:
  - `encoding`: The encoding format for the geometry data (e.g., "WKB", "point")
  - `geometry_types`: List of geometry types present in the column
  - `crs`: Coordinate reference system information in PROJJSON format (can be null)
  - `bbox`: Bounding box coordinates (2D or 3D)
  - `edges`: Edge type (e.g., "planar", "spherical")
  - `orientation`: Polygon winding order (typically "counterclockwise")
  - `epoch`: Optional temporal reference
  - `covering`: Optional spatial indexing information

## PROJJSON Schema Version

The CRS information in the example metadata uses PROJJSON schema version 0.7. Note that this is an updated version from the original example, which used version 0.6. The key differences include:

1. Bounding box format:
   - Changed from an object with named properties (`south_latitude`, `west_longitude`, etc.)
   - To an array in the format `[west, south, east, north]`

2. Coordinate system properties:
   - Changed `subtype` property to `type`
   - Changed `axis` property to `axes`

For more information on PROJJSON, see the [PROJ documentation](https://proj.org/en/latest/specifications/projjson.html).

## File Format

In a real GeoParquet file, the metadata is stored as a key-value entry in the Parquet file metadata, with the key "geo" and the value being the JSON metadata. The example files reflect this structure by wrapping the metadata in a "geo" object.

## Version Differences

The 1.2.0-dev example showcases several advanced features compared to 1.1.0:

1. **3D Geometry Support**:
   - 3D bounding box with Z min/max values (`[minX, minY, minZ, maxX, maxY, maxZ]`)
   - Z dimension in covering specification (zmin, zmax)
   - 3D coordinate system with height axis
   - Z-aware geometry types (e.g., "Polygon Z", "Point Z")

2. **Multiple Geometry Columns**:
   - Primary "geometry" column with full metadata
   - Secondary "points" column with simpler configuration and null CRS

3. **Enhanced Metadata**:
   - Explicit "counterclockwise" orientation setting
   - Spherical edges representation
   - Temporal dimension with epoch value
   - Support for GeometryCollection type

These examples help with testing GeoParquet implementations to ensure they can handle both current and future versions of the specification.