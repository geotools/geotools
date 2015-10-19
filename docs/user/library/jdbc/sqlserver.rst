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

* JDBC Driver
  Since GeoTools version 14 this module is compiled using the JTDS driver which is an improved open source driver
  It can be freely distributed and is built by default now. If you require the use of the Microsoft driver follow 
  the instructions below.
 
  GeoTools is unable to ship the SQL Server JDBC driver with the standard
  distribution. It must be downloaded from Microsoft separately.
  
  * `SQL Server 2000 Driver for JDBC Service Pack 2 <http://www.microsoft.com/downloads/en/details.aspx?familyid=9F1874B6-F8E1-4BD6-947C-0FC5BF05BF71&displaylang=en>`_
  * `Microsoft SQL Server JDBC Driver 3.0 <http://www.microsoft.com/downloads/en/details.aspx?FamilyID=a737000d-68d0-4531-b65d-da0f2a735707>`_

