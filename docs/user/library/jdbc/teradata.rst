Teradata Plugin
-----------------

Supports direct access to a Teradata database.

**References**

* http://www.teradata.com/t/

**Maven**::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-teradata</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "teradata"
"host"         Machine name or IP address to connect to
"port"         Port number to connect to, default is 1025
"database"     The database to connect to, usually same as "user"
"user"         User name
"passwd"       Password
============== ============================================

Access
^^^^^^

Example use::
  
  java.util.Map params = new java.util.HashMap();
  params.put( "dbtype", "teradata");
  params.put( "host", "localhost");
  params.put( "port", 1025);
  params.put( "user", "geotools");
  params.put( "passwd", "geotools");
  
  DataStore dataStore=DataStoreFinder.getDataStore(params);

Setup
^^^^^

* JDBC Driver
  
  GeoTools is unable to ship the Teradata JDBC driver with the standard
  distribution. It must be downloaded from Teradata separately.
  
  * `Teradata JDBC Driver Download <http://downloads.teradata.com/download/connectivity/jdbc-driver>`_

