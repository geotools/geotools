SQL Server Plugin
-----------------

Supports direct access to an SQL Server database.

References:

* http://www.microsoft.com/sqlserver/

**Maven**::

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-sqlserver</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "sqlserver"
"host"         Machine name or IP address to connect to
"port"         Port number to connect to
"schema"       The database schema to access
"database"     The databse to connect to
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

* JDBC Driver
  
  GeoTools is unable to ship the SQL Server JDBC driver with the standard
  distribution. It must be downloaded from Mircrosoft separately.
  
  * `SQL Server 2000 Driver for JDBC Service Pack 2 <http://www.microsoft.com/downloads/en/details.aspx?familyid=9F1874B6-F8E1-4BD6-947C-0FC5BF05BF71&displaylang=en>`_
  * `Microsoft SQL Server JDBC Driver 3.0 <http://www.microsoft.com/downloads/en/details.aspx?FamilyID=a737000d-68d0-4531-b65d-da0f2a735707>`_

