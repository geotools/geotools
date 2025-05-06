# PROJJSON Test Resources

This directory contains test files for the PROJJSON Jackson binding implementation.

## Schema Version

All JSON files in this directory adhere to the PROJJSON schema version 0.7, as defined by:
https://proj.org/schemas/v0.7/projjson.schema.json

Note: These files were originally based on older schema versions (v0.1, v0.4) and have been updated to conform to v0.7.

## File Descriptions

- `geographic_crs.json`: Example of a Geographic CRS (NAD83)
- `projected_crs.json`: Example of a Projected CRS (WGS 84 / UTM zone 31N)
- `compound_crs.json`: Example of a Compound CRS (WGS 84 + EGM2008 height)
- `bound_crs.json`: Example of a Bound CRS (ETRS89 bound to WGS 84)
- `transformation.json`: Example of a Coordinate Transformation (NAD27 to NAD83)

## Key Changes from older PROJJSON versions to v0.7

1. In coordinate systems:
   - `subtype` property was renamed to `type`
   - `axis` property was renamed to `axes`

2. Bounding box format:
   - Changed from an object with named properties (`south_latitude`, `west_longitude`, etc.) 
   - To an array in the format: `[west, south, east, north]`

3. Other minor schema improvements for better consistency

## Sources

Examples extracted from:

* https://proj.org/en/stable/specifications/projjson.html#geographiccrs
* https://proj.org/en/stable/specifications/projjson.html#projectedcrs
* https://proj.org/en/stable/specifications/projjson.html#compoundcrs
* https://proj.org/en/stable/specifications/projjson.html#boundcrs
* https://proj.org/en/stable/specifications/projjson.html#transformation
