GeoTools SQL Server DataStore README
====================================

JDBC Driver
-----------

The current tested driver version is 7.2.1 for SQL Server and SQL Azure. According to Microsoft this
driver provides access to SQL Azure, SQL Server 2017 from any Java application, application server, 
or Java-enabled applet.

This plugin will automatically download the MS SQL Server JDBC driver from the central maven repository.

Maven
-----

If developing with the datastore and the rest of GeoTools: 

  1. Build the jdbc-sqlserver module using the *sqlServerDriver* profile:

     mvn clean install -P sqlServerDriver
  