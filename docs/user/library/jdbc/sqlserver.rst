SQL Server Plugin
-----------------

Supports direct access to an SQL Server database.

References:

* http://www.microsoft.com/sqlserver/
* http://jtds.sourceforge.net/

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-sqlserver</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "sqlserver" if using the Microsoft Driver, otherwise use "jtds-sqlserver"
"host"         Machine name or IP address to connect to
"port"         Port number to connect to
"instance"     Instance of Database to use (instead of port when multiple DB services are running)
"schema"       The database schema to access
"database"     The database to connect to
"user"         User name
"passwd"       Password
============== ============================================

Access
^^^^^^

Example use::
  
  java.util.Map params = new java.util.HashMap();
  params.put( "dbtype", "sqlserver");
  params.put( "host", "localhost");
  params.put( "port", 4866);
  params.put( "user", "geotools");
  params.put( "passwd", "geotools");
  
  DataStore dataStore=DataStoreFinder.getDataStore(params);

Setup
^^^^^

  Since GeoTools version 14 this module is compiled using the JTDS driver. The driver is still
  available, but in the meantime Microsoft released an open source version, while JTDS went unsupported.
  We recomment using the Microsoft JDBC driver, while the JTDS based data store factory is left
  for backwards compatibility. In either case, you don't have to do anything, both drivers
  are automatically downloaded by Maven (you might want to exclude one to reduce size).
