Oracle Plugin
-------------

Supports direct access to an Oracle database.

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

Example use::
  
  params.put(PostgisDataStoreFactory.LOOSEBBOX, true );

Setup
^^^^^

* JDBC Driver
  
  GeoTools is unable to ship the Oracle JDBC driver with the standard
  distribution. It must be downloaded from oracle separately.

* JDBC vs JDBC-NG
  
  The oracle support has been rewritten for GeoTools 2.6. This rewrite is
  called "jdbc-ng" and is easier to maintain. By in large the same
  connection parameters can be used.
