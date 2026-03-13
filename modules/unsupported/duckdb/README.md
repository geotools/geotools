# DuckDB DataStore

The DuckDB DataStore provides read-focused access to DuckDB databases within the GeoTools framework.

[DuckDB](https://duckdb.org/) is an embedded analytical database with strong support for vectorized query execution and spatial workloads. In GeoTools, the `gt-duckdb` module provides the shared DuckDB JDBC integration used directly by the DuckDB store and reused by the GeoParquet module.

## Features

- **DuckDB Database Access**: Connect to in-memory or file-backed DuckDB databases
- **Native Geometry Support**: Works with DuckDB native `GEOMETRY` columns
- **Spatial Queries**: Supports spatial filters through the GeoTools JDBC stack
- **Read-Focused Execution Model**: Public access is restricted to a guarded execution path
- **GeoTools JDBC Integration**: Supports namespace, fetch size, screenmap, and geometry simplification settings

## Security Model

The public DuckDB store is exposed through a restricted GeoTools-side execution wrapper.

- Only the guarded GeoTools datastore API is intended for public use
- Arbitrary session scripts are not exposed
- Multi-statement SQL is rejected
- GeoTools JDBC virtual tables are disabled for the public store
- Initialization SQL is managed internally by GeoTools under the execution policy

This module is intended to reduce the exposed SQL surface while still allowing DuckDB-backed read access through standard GeoTools APIs.

## Usage

### In-Memory Database

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "duckdb");
params.put("memory", true);

DataStore store = DataStoreFinder.getDataStore(params);
```

### File-Backed Database

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "duckdb");
params.put("database", "/path/to/data.duckdb");

DataStore store = DataStoreFinder.getDataStore(params);
String[] typeNames = store.getTypeNames();
SimpleFeatureSource source = store.getFeatureSource(typeNames[0]);
```

### Query Example

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "duckdb");
params.put("database", "/path/to/data.duckdb");
params.put("simplification", true);
params.put("screenmap", true);

DataStore store = DataStoreFinder.getDataStore(params);
SimpleFeatureSource source = store.getFeatureSource("roads");

FilterFactory ff = CommonFactoryFinder.getFilterFactory();
Filter filter = ff.intersects(
    ff.property("geom"),
    ff.literal(geometryFactory.toGeometry(new Envelope(0, 10, 0, 10)))
);

SimpleFeatureCollection features = source.getFeatures(filter);
```

## Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| **dbtype** | String | Yes | Must be `"duckdb"` |
| **memory** | Boolean | No | Use an in-memory DuckDB database (default: `false`) |
| **database** | String | No | Path to a DuckDB database file; required unless `memory=true` |
| **namespace** | String | No | Namespace URI to use for features |
| **fetch size** | Integer | No | JDBC fetch size used for result streaming |
| **screenmap** | Boolean | No | Enable screenmap support for rendering optimization (default: `true`) |
| **simplification** | Boolean | No | Enable on-the-fly geometry simplification (default: `true`) |

## Relationship with GeoParquet

`gt-duckdb` is the shared DuckDB foundation module. The GeoParquet datastore depends on it for DuckDB execution, connection management, and SQL dialect support, while keeping GeoParquet-specific metadata and dataset handling in `gt-geoparquet`.

## Notes

- This is an unsupported GeoTools module
- The public store focuses on native DuckDB datasets and guarded read access
- GeoParquet may rely on additional internal DuckDB capabilities through a trusted internal policy implemented in GeoTools
