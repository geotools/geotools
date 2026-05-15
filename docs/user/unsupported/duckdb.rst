DuckDB DataStore
================

The DuckDB DataStore provides guarded access to DuckDB databases within the GeoTools framework.

`DuckDB <https://duckdb.org/>`_ is an embedded analytical database with strong support for vectorized query execution and spatial workloads. In GeoTools, the ``gt-duckdb`` module provides the shared DuckDB JDBC integration used directly by the DuckDB store and reused by the GeoParquet module.

**Maven**::

   <dependency>
     <groupId>org.geotools</groupId>
     <artifactId>gt-duckdb</artifactId>
     <version>${geotools.version}</version>
   </dependency>

Features
--------

- **DuckDB Database Access**: Connect to in-memory or file-backed DuckDB databases
- **Native Geometry Support**: Works with DuckDB native ``GEOMETRY`` columns
- **Spatial Queries**: Supports spatial filters through the GeoTools JDBC stack
- **Guarded Execution Model**: Public access is restricted to a GeoTools-managed execution policy
- **GeoTools JDBC Integration**: Supports namespace, fetch size, screenmap, and geometry simplification settings

Security Model
--------------

The public DuckDB store is exposed through a restricted GeoTools-side execution wrapper.

- Only the guarded GeoTools datastore API is intended for public use
- Arbitrary session scripts are not exposed
- Multi-statement SQL is rejected
- GeoTools JDBC virtual tables are disabled for the public store
- Initialization SQL is managed internally by GeoTools under the execution policy
- In ``read_only=true`` mode (default), public SQL is limited to read-only statements
- ``WITH`` common table expressions are rejected in public read-only mode
- In ``read_only=false`` mode, managed writes are enabled, but view creation and arbitrary session SQL remain blocked

This module is intended to reduce the exposed SQL surface while still allowing DuckDB-backed access through standard GeoTools APIs.

Usage
-----

In-Memory Database
^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "duckdb");
   params.put("memory", true);

   DataStore store = DataStoreFinder.getDataStore(params);

File-Backed Database
^^^^^^^^^^^^^^^^^^^^

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "duckdb");
   params.put("database", "/path/to/data.duckdb");

   DataStore store = DataStoreFinder.getDataStore(params);
   String[] typeNames = store.getTypeNames();
   SimpleFeatureSource source = store.getFeatureSource(typeNames[0]);

Query Example
^^^^^^^^^^^^^

.. code-block:: java

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

Parameters
----------

.. list-table::
   :header-rows: 1

   * - Parameter
     - Type
     - Required
     - Description
   * - **dbtype**
     - String
     - Yes
     - Must be ``"duckdb"``
   * - **memory**
     - Boolean
     - No
     - Use an in-memory DuckDB database (default: ``false``)
   * - **database**
     - String
     - No
     - Path to a DuckDB database file; required unless ``memory=true``
   * - **read_only**
     - Boolean
     - No
     - Guarded read-only mode toggle (default: ``true``). Set ``false`` to enable managed write operations.
   * - **namespace**
     - String
     - No
     - Namespace URI to use for features
   * - **fetch size**
     - Integer
     - No
     - JDBC fetch size used for result streaming
   * - **screenmap**
     - Boolean
     - No
     - Enable screenmap support for rendering optimization (default: ``true``)
   * - **simplification**
     - Boolean
     - No
     - Enable on-the-fly geometry simplification (default: ``true``)

Configuration Rules
-------------------

- ``memory`` and ``database`` are mutually exclusive
- ``database`` is required when ``memory`` is not ``true``

Write Mode
----------

To enable write operations for a specific store instance, set ``read_only`` to ``false``:

.. code-block:: java

   Map<String, Object> params = new HashMap<>();
   params.put("dbtype", "duckdb");
   params.put("database", "/path/to/data.duckdb");
   params.put("read_only", false);

   DataStore store = DataStoreFinder.getDataStore(params);

Write mode remains guarded by GeoTools policy controls. It is intended for GeoTools-managed datastore operations, not arbitrary SQL execution.

Relationship with GeoParquet
----------------------------

``gt-duckdb`` is the shared DuckDB foundation module. The GeoParquet datastore depends on it for DuckDB execution, connection management, and SQL dialect support, while keeping GeoParquet-specific metadata and dataset handling in ``gt-geoparquet``.

Notes
-----

- This is an unsupported GeoTools module
- The public store focuses on native DuckDB datasets with guarded access
- GeoParquet may rely on additional internal DuckDB capabilities through a trusted internal policy implemented in GeoTools
