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
  
  The SpatiaLite datastore ships with its own build of the SQLite and SpatiaLite 
  libraries. The SpatiaLite component has been compiled with GEOS and PROJ support
  so those libraries need to be installed on the system for the datastore to 
  function. Binaries for a variety of platforms are available at https://www.gaia-gis.it/fossil/libspatialite/index.

  See also:
  
     * http://trac.osgeo.org/proj/
     * http://trac.osgeo.org/geos/

* Java Environment

  In order to load the native libraries at runtime Java must be told where the libraries live
  with a system property named "java.library.path". This is specified during java startup::
  
     java -Djava.library.path=/usr/local/lib ...
     
  Depending on how the O/S is configured the additional ``LD_LIBRARY_PATH`` (unix/linux) and 
  ``DYLD_LIBRARY_PATH`` (osx) environment variables may need to be set.

  
