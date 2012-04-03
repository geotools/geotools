Oracle Plugin
-------------

Supports direct access to an Oracle database.

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-jdbc</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== =============================
Parameter      Description
============== =============================
"dbtype"       Must be the string "oracle"
"host"         Machine name or IP address to connect to
"port"         Port number to connect to, default 1521
"schema"       The database schema to access
"database"     The databse to connect to
"user"         User name
"passwd"       Password
============== =============================

Creation
^^^^^^^^

Connect using DataStore finder::

  java.util.Map params = new java.util.HashMap();
  params.put( "dbtype", "oracle");
  params.put( "host", "localhost");
  params.put( "port", 1521);
  params.put( "schema", "public");
  params.put( "database", "database");
  params.put( "user", "geotools");
  params.put( "passwd", "geotools");
  
  DataStore dataStore=DataStoreFinder.getDataStore(params);

Advanced
^^^^^^^^

+---------------------+------------------------------------------------+
| Parameter           | Description                                    |
+=====================+================================================+
| "loose bbox"        | Flag controlling loose bbox comparisons,       |
|                     | default is true                                |
+---------------------+------------------------------------------------+
| "Estimated extends" | Use the spatial index information to quickly   |
|                     | get an estimate of the data bounds             |
+---------------------+------------------------------------------------+
| "Geometry metadata  | An alternative table where geometry            |
| table"              | metadata information can be looked up          |
+---------------------+------------------------------------------------+


Example use::
  
  params.put(PostgisDataStoreFactory.LOOSEBBOX, true );
  
  
Geometry metadata table
^^^^^^^^^^^^^^^^^^^^^^^

The Oracle data store will, by default, look into the ``MDSYS.USER_SDO*`` and ``MDSYS.ALL_SDO*`` views
to determine the geometry type and native SRID of each geometry column.
Those views are automatically populated with information about the geometry columns stored in tables that the current
user owns (for the ``MDSYS.USER_SDO*`` views) or can otherwise access (for the ``MDSYS.ALL_SDO*`` views).

There are a few hiccups in this process:

  * if the connection pool user cannot access the tables (because impersonation is being used) 
    the MDSYS views will be empty, making it impossible to determine either the geometry type and the native SRID
  * the geometry type can be specified only while building the spatial indexes, as a index constraint, however 
    such information is often not included when creating the indexes
  * the views are populated dynamically based on the current user, if the database has thousands of tables and users
    the views can become very slow
    
Starting with GeoTools 2.7.5 the database administrator can address the above issues by manually creating a geometry metadata table
describing each geometry column, and then indicate its presence among the Oracle data store connection parameter named *Geometry metadata table*
(either as a simple table name, or a schema qualified one).
The table has the following structure (the table name is free, just indicate the one chosen in the data store connection parameter)::

	CREATE TABLE GEOMETRY_COLUMNS(
	   F_TABLE_SCHEMA VARCHAR(30) NOT NULL, 
	   F_TABLE_NAME VARCHAR(30) NOT NULL, 
	   F_GEOMETRY_COLUMN VARCHAR(30) NOT NULL, 
	   COORD_DIMENSION INTEGER, 
	   SRID INTEGER NOT NULL, 
	   TYPE VARCHAR(30) NOT NULL,
	   UNIQUE(F_TABLE_SCHEMA, F_TABLE_NAME, F_GEOMETRY_COLUMN),
	   CHECK(TYPE IN ('POINT','LINE', 'POLYGON', 'COLLECTION', 'MULTIPOINT', 'MULTILINE', 'MULTIPOLYGON', 'GEOMETRY') ));
	   
When the table is present the store wil first search it for information about each geometry column
to be classified, and fall back on the MDSYS views only if such table does not contain any information.

Setup
^^^^^

* JDBC Driver
  
  GeoTools is unable to ship the Oracle JDBC driver with the standard
  distribution. It must be downloaded from oracle separately.

* JDBC vs JDBC-NG
  
  The oracle support has been rewritten for GeoTools 2.6. This rewrite is
  called "jdbc-ng" and is easier to maintain. By in large the same
  connection parameters can be used.
