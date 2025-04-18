GeoParquet DataStore
====================

The GeoParquet DataStore provides read and query access to GeoParquet format data files, both local and remote, within the GeoTools framework.

`GeoParquet <https://github.com/opengeospatial/geoparquet>`_ is an open format for geospatial data that builds on the `Apache Parquet <https://parquet.apache.org/>`_ columnar storage format. GeoParquet brings the performance benefits of columnar storage to geospatial data, making it particularly well-suited for analytics and large datasets.

**Maven**::
   
   <dependency>
     <groupId>org.geotools</groupId>
     <artifactId>gt-geoparquet</artifactId>
     <version>${geotools.version}</version>
   </dependency>

Features
--------

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

How It Works
------------

Under the hood, this DataStore uses `DuckDB <https://duckdb.org/>`_ and its Spatial and Parquet extensions to provide high-performance access to GeoParquet files. DuckDB is an embedded analytical database that excels at reading and processing Parquet files. The implementation:

1. Creates SQL views over GeoParquet files (local or remote)
2. Detects and manages Hive-partitioned datasets (directory structures with key=value patterns)
3. Handles GeoParquet metadata parsing and schema detection
4. Translates GeoTools filters to optimized SQL queries
5. Converts spatial operations to DuckDB spatial functions
6. Manages extension loading (spatial, parquet, httpfs)

This implementation detail is abstracted away from the user, providing a clean GeoTools DataStore interface.

Usage
-----

Basic Example
^^^^^^^^^^^^^

.. code-block:: java

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

Directory of GeoParquet Files
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "geoparquet");
   params.put("uri", "file:/path/to/directory/with/parquet/files");
   
   DataStore store = DataStoreFinder.getDataStore(params);
   // Each GeoParquet file will appear as a separate feature type
   String[] typeNames = store.getTypeNames();

Hive-Partitioned Dataset
^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "geoparquet");
   params.put("uri", "s3://my-bucket/data/year=*/month=*/day=*");
   // Optionally limit partition depth
   params.put("max_hive_depth", 2); // Only use year and month, ignore day
   
   DataStore store = DataStoreFinder.getDataStore(params);
   // Each distinct partition becomes a separate feature type
   String[] typeNames = store.getTypeNames();

Remote GeoParquet File (HTTP)
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "geoparquet");
   params.put("uri", "https://example.com/data.parquet");
   
   DataStore store = DataStoreFinder.getDataStore(params);

Remote GeoParquet File (S3)
^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "geoparquet");
   params.put("uri", "s3://my-bucket/data.parquet?region=us-west-2&access_key=AKIAIOSFODNN7EXAMPLE&secret_key=wJalrXUtnFEMI/K7MDENG/bPxRfiCYEXAMPLEKEY");
   
   DataStore store = DataStoreFinder.getDataStore(params);

Parameters
----------

===================== ======== ========= ===========================================================
Parameter             Type     Required  Description
===================== ======== ========= ===========================================================
**dbtype**            String   Yes       Must be "geoparquet"
**uri**               String   Yes       URI to GeoParquet file or directory (supports file://, https://, s3://)
**max_hive_depth**    Integer  No        Maximum depth of Hive partition hierarchy to use (null = all levels, 0 = none, 1+ = specific level)
**simplify**          Boolean  No        Enable geometry simplification for rendering optimization (default: ``true``)
**namespace**         String   No        Namespace URI to use for features
===================== ======== ========= ===========================================================

For S3 URIs, you can include authentication parameters::

   s3://bucket/path/to/file.parquet?region=us-west-2&access_key=ACCESS_KEY&secret_key=SECRET_KEY&endpoint=ENDPOINT

GeoParquet Metadata Support
---------------------------

The datastore supports GeoParquet metadata versions:

- 1.1.0 (standard)
- 1.2.0-dev (development version)

The implementation parses the ``geo`` metadata field from Parquet files to obtain:

- Primary geometry column name
- Geometry encoding details
- Geometry types
- CRS information
- Bounding box information
- Additional metadata fields

Hive Partitioning Support
-------------------------

Hive partitioning is a data organization strategy where files are arranged in directory hierarchies with path components in a key=value format. The GeoParquet DataStore now fully supports automatic detection and handling of these partitioned datasets.

What is Hive Partitioning?
^^^^^^^^^^^^^^^^^^^^^^^^^^

Hive partitioning organizes data files into a directory structure where each directory level represents a partition key and value. For example:

.. code-block::

   /data/year=2023/month=01/day=01/file.parquet
   /data/year=2023/month=01/day=02/file.parquet
   /data/year=2023/month=02/day=01/file.parquet

This organization provides several benefits:

- Partition pruning (only reading relevant files)
- Logical organization of data
- Self-descriptive directory structure
- Support for time series and other hierarchical data

How GeoParquet DataStore Handles Hive Partitions
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The DataStore automatically:

1. Detects key=value patterns in directories
2. Creates separate feature types for each unique partition path
3. Names feature types based on the partition values
4. Optimizes queries to only access relevant partitions
5. Allows controlling partition depth via the ``max_hive_depth`` parameter

Example with Time Series Data
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "geoparquet");
   params.put("uri", "s3://my-bucket/data/year=*/month=*/day=*");
   
   DataStore store = DataStoreFinder.getDataStore(params);
   
   // Get all feature types (one per partition)
   String[] typeNames = store.getTypeNames();
   
   // Get data for a specific day
   SimpleFeatureSource source = store.getFeatureSource("year_2023_month_02_day_01");

Controlling Partition Depth
^^^^^^^^^^^^^^^^^^^^^^^^^^^

You can control how many levels of partition hierarchy are used with the ``max_hive_depth`` parameter:

.. code-block:: java

   // Only use year and month, ignore day partitioning
   params.put("max_hive_depth", 2);
   
   // This will produce feature types like "year_2023_month_01"
   DataStore store = DataStoreFinder.getDataStore(params);

Working with Overture Maps Data
-------------------------------

`Overture Maps <https://overturemaps.org/>`_ provides open geospatial datasets in Parquet format that are organized using Hive partitioning. The files follow a structure like:

.. code-block::

   /release/2025-02-19.0/theme=buildings/type=building/
   /release/2025-02-19.0/theme=transportation/type=segment/

This works perfectly with the GeoParquet DataStore's Hive partitioning support, creating separate feature types for each theme and type combination. The GeoParquet module includes functionality to help prepare and utilize these datasets.

For details on downloading and preparing Overture Maps data, refer to the ``extract_overturemaps_data.md`` documentation included with the module.

Current Limitations
-------------------

- Currently read-only (no writing capabilities)
- Tables don't show up in the DataStore until they've been accessed at least once
- Working with extremely large remote files may involve some latency on initial access
- This module is unsupported and still under development

Requirements
------------

- Java 11 or higher
- GeoTools 33 or later
- Internet connection (for extension installation if needed)
