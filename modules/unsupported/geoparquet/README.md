# GeoParquet DataStore

The GeoParquet DataStore provides read and query access to GeoParquet format data files, both local and remote, within the GeoTools framework.

[GeoParquet](https://github.com/opengeospatial/geoparquet) is an open format for geospatial data that builds on the [Apache Parquet](https://parquet.apache.org/) columnar storage format. GeoParquet brings the performance benefits of columnar storage to geospatial data, making it particularly well-suited for analytics and large datasets.

## Features

- **Local File Access**: Read GeoParquet files from your local filesystem
- **Remote File Access**: Access GeoParquet files via HTTP/HTTPS URLs and S3 storage
- **Directory Support**: Point to a directory and automatically work with all GeoParquet files within it
- **Hive Partitioning Support**: Automatically detects and handles Hive-partitioned datasets (key=value directory structure)
- **Partition Depth Control**: Configure how many levels of partitioning to use with the `max_hive_depth` parameter
- **Spatial Queries**: Full support for spatial filters (intersects, contains, within, etc.)
- **Attribute Filters**: Filter data based on attribute values
- **Geometry Simplification**: Supports on-the-fly geometry simplification for rendering optimization
- **GeoParquet Metadata**: Parses and utilizes the GeoParquet 'geo' metadata for schema information and optimizations
- **Projection Support**: Handles coordinate reference systems properly
- **Memory Efficient**: Leverages Parquet's efficient columnar storage format
- **Query Pushdown**: Pushes filters down to optimize data retrieval
- **Bounds Optimization**: Uses GeoParquet metadata or specialized bbox columns for fast bounds calculation


## How It Works

Under the hood, this DataStore uses [DuckDB](https://duckdb.org/) and its Spatial and Parquet extensions to provide high-performance access to GeoParquet files. DuckDB is an embedded analytical database that excels at reading and processing Parquet files. The implementation:

1. Creates SQL views over GeoParquet files (local or remote)
2. Detects and manages Hive-partitioned datasets (directory structures with key=value patterns)
3. Handles GeoParquet metadata parsing and schema detection
4. Translates GeoTools filters to optimized SQL queries
5. Converts spatial operations to DuckDB spatial functions
6. Manages extension loading (spatial, parquet, httpfs)

This implementation detail is abstracted away from the user, providing a clean GeoTools DataStore interface.

## Usage

### Basic Example

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "file:/path/to/data.parquet");

DataStore store = DataStoreFinder.getDataStore(params);
String[] typeNames = store.getTypeNames();
SimpleFeatureSource source = store.getFeatureSource(typeNames[0]);

// Read all features
SimpleFeatureCollection features = source.getFeatures();

// Or apply a filter
FilterFactory ff = CommonFactoryFinder.getFilterFactory();
Filter filter = ff.intersects(
    ff.property("geometry"),
    ff.literal(geometryFactory.toGeometry(new Envelope(0, 10, 0, 10)))
);
SimpleFeatureCollection filtered = source.getFeatures(filter);
```

### Directory of GeoParquet Files

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "file:/path/to/directory/with/parquet/files");

DataStore store = DataStoreFinder.getDataStore(params);
// Each GeoParquet file will appear as a separate feature type
String[] typeNames = store.getTypeNames();
```

### Hive-Partitioned Dataset

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "s3://my-bucket/data/year=*/month=*/day=*");
// Optionally limit partition depth
params.put("max_hive_depth", 2); // Only use year and month, ignore day

DataStore store = DataStoreFinder.getDataStore(params);
// Each distinct partition becomes a separate feature type
// With max_hive_depth=2, you'll get partitions like "year=2023_month=01"
String[] typeNames = store.getTypeNames();
```

### Remote GeoParquet File (HTTP)

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "https://example.com/data.parquet");

DataStore store = DataStoreFinder.getDataStore(params);
```

### Remote GeoParquet File (S3)

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "s3://my-bucket/data.parquet?region=us-west-2&access_key=AKIAIOSFODNN7EXAMPLE&secret_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");

DataStore store = DataStoreFinder.getDataStore(params);
```

## Working with Overture Maps Data

[Overture Maps](https://overturemaps.org/) provides open geospatial datasets in Parquet format that work perfectly with this DataStore. Here's how to download and prepare Overture Maps data for use.

### Preparing Overture Maps Data using DuckDB

Before using this DataStore, you may want to download and prepare Overture Maps data. DuckDB makes this easy with its ability to query remote Parquet files directly.

1. **Install DuckDB CLI** from [duckdb.org](https://duckdb.org/docs/installation/)

2. **Create a GeoParquet data extract**: See the [extract_overturemaps_data.md](extract_overturemaps_data) document for instructions on how to prepare an extract.


### Using the Downloaded Overture Maps Data with GeoParquet DataStore

Now you can use the downloaded files with the GeoParquet DataStore:

```java
Map<String, Object> params = new HashMap<>();
params.put("dbtype", "geoparquet");
params.put("uri", "file:/path/to/overture_rosario"); // Directory containing the Parquet files

DataStore store = DataStoreFinder.getDataStore(params);

// List available layers
String[] typeNames = store.getTypeNames();

// Access buildings layer
SimpleFeatureSource buildings = store.getFeatureSource("buildings_rosario");

// Create a filter for a specific area in central Rosario
FilterFactory ff = CommonFactoryFinder.getFilterFactory();
Geometry centralRosario = WKT.read("POLYGON((-60.65 -32.93, -60.61 -32.93, -60.61 -32.95, -60.65 -32.95, -60.65 -32.93))");
Filter filter = ff.intersects(ff.property("geometry"), ff.literal(centralRosario));

// Query buildings in central Rosario
SimpleFeatureCollection centralBuildings = buildings.getFeatures(filter);
```

## Parameters

| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| **dbtype** | String | Yes | Must be "geoparquet" |
| **uri** | String | Yes | URI to GeoParquet file or directory (supports file://, https://, s3://) |
| **max_hive_depth** | Integer | No | Maximum depth of Hive partition hierarchy to use (null = all levels, 0 = none, 1+ = specific level) |
| **simplify** | Boolean | No | Enable geometry simplification for rendering optimization (default: `true`) |
| **namespace** | String | No | Namespace URI to use for features |

For S3 URIs, you can include authentication parameters:
```
s3://bucket/path/to/file.parquet?region=us-west-2&access_key=ACCESS_KEY&secret_key=SECRET_KEY&endpoint=ENDPOINT
```

## GeoParquet Metadata Support

The datastore supports GeoParquet metadata versions:
- 1.1.0 (standard)
- 1.2.0-dev (development version)

The implementation parses the `geo` metadata field from Parquet files to obtain:
- Primary geometry column name
- Geometry encoding details
- Geometry types
- CRS information
- Bounding box information
- Additional metadata fields

## Optimized Operations

- **Bounds calculation**: Uses GeoParquet metadata or specialized bbox columns when available, falling back to ST_Extent_Agg for general cases
- **Geometry simplification**: Supports `ST_SimplifyPreserveTopology` for rendering optimization (`ST_Simplify` results in too many empty geometries
    with the tolerance given by the GeoTools renderer as a Hint).
- **Query pushdown**: Converts GeoTools filters to optimized SQL queries that filter at the data source
- **View-based access**: Creates SQL views over Parquet files for efficient querying

## Implementation Notes

- The first time a DataStore is created, required DuckDB extensions (spatial, parquet, httpfs) will be installed
- For performance reasons, it's recommended to use a persistent DuckDB database when working with very large files repeatedly
- For remote files, the httpfs extension handles HTTP(S) and S3 communication
- Each GeoParquet file in a directory appears as a separate feature type in the DataStore
- On-the-fly geometry simplification uses ST_SimplifyPreserveTopology for best results with rendering

## Implemented Features

- Hive-partitioned dataset support for directories with key=value patterns
- Configurable partition depth with max_hive_depth parameter
- Support for Overture Maps data format and organization
- Bounds optimization using GeoParquet metadata or bbox columns
- Automatic feature type discovery from partitioned datasets
- Remote access to HTTP/S3 GeoParquet files
- Filter pushdown for optimized spatial queries

## Planned Improvements

- Identify actual **geometry type**. Currently all geometry columns are reported as type `Geometry`. Consider GeoParquet files can have multiple
  geometry types. If the `geo` metadata is available, they're reported (e.g. [Polygon, MultiPolygon, ...]). Otherwise it's hard to know.
- Support multiple CRS's, read the kvp 'geo' metadata
- Support **Struct** data types. Since they're "value objects" and not relationships, maybe just create a JSON attribute type and parse them as JSON,
  maintaining the store as "simple features". No need to build "complex features".
- Allow to configure [S3 credentials](https://duckdb.org/docs/stable/guides/network_cloud_storage/s3_import.html#credentials-and-configuration)
- Install [aws extension](https://duckdb.org/docs/stable/extensions/aws.html) to load credentials automatically
- Enable [Azure](https://duckdb.org/docs/stable/extensions/azure) as a data storage option
- Apply the `bbox` column optimization to spatial predicates. The `BBOX` filter takes advantage of geoparquet's `bbox` column to perform filter push down. Other spatial filters should be AND'ed with a BBOX filter to leverage this optimization too.

## Limitations

- Currently read-only (no writing capabilities)
- Tables don't show up in the DataStore until they've been accessed at least once
- Working with extremely large remote files may involve some latency on initial access
- This module is unsupported and still under development

## Requirements

- Java 11 or higher
- GeoTools 33 or later
- Internet connection (for extension installation if needed)

## Dependencies

- GeoTools Core and JDBC modules
- DuckDB JDBC driver (version specified in the root `pom.xml` for the `org.duckdb:duckdb_jdbc` dependency)
- Jackson Databind (for parsing GeoParquet metadata)

