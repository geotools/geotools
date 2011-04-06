Spatial Lite Plugin
-------------------

Supports direct access to a SQLite/SpatiaLite database.

SQLite is a popular embedded relational database. SpatiaLite is the spatial extension to SQLite.

References:

  * http://www.sqlite.org/
  * http://www.gaia-gis.it/spatialite/

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "spatiallite"
"database"     The databse to connect to
"user"         User name (optional)
============== ============================================

Access
^^^^^^

Example use::
  
  Map params = new HashMap();
  params.put("dbtype", "spatialite");
  params.put("database", "geotools");
  
  DataStore datastore = DataStoreFinder.getDataStore(params);

Setup
^^^^^

* Native Libraries
  
  The SpatiaLite data store requires the native libraries for SQLite
  and SpatiaLite to be installed on the system. Precompiled libraries for
  SQLite and SpatiaLite are available.
  
  * http://www.sqlite.org/download.html
  * http://www.gaia-gis.it/spatialite/binaries.html