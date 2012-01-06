SpatiaLite Plugin
-----------------

Supports direct access to a SQLite/SpatiaLite database.

SQLite is a popular embedded relational database. SpatiaLite is the spatial extension to SQLite.

.. note::

   The plugin uses internal versions of SpatiaLite (2.3.1) and SQLite (3.7.2). 
   Therefore the plugin is only capable of accessing databases that are 
   compatible with these versions.

References:

  * http://www.sqlite.org/
  * http://www.gaia-gis.it/spatialite/

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-spatiallite</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

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
