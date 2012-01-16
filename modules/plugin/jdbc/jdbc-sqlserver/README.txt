GeoTools SQL Server DataStore README
====================================

JDBC Driver
-----------

To use the sql server datastore the jdbc 3.0 driver must be downloaded from 
Microsoft:

  http://www.microsoft.com/download/en/details.aspx?displaylang=en&id=19847

The current tested driver version is 3.0 for SQL Server and SQL Azure. According to Microsoft this
driver provides access to SQL Azure, SQL Server 2008 R2, SQL Server 2008, SQL Server 2005 and 
SQL Server 2000 from any Java application, application server, or Java-enabled applet.

Make sure to unpack the sqljdbc.jar file, as that's the one providing support for the Java
version in this series (Java 1.5)

Maven
-----

If developing with the datastore and the rest of GeoTools: 

  1. install the JDBC driver into the location maven repository:

     mvn install:install-file  -Dfile=<path to sqljdbc.jar>
         -DartifactId=sqljdbc \ 
         -DgroupId=com.microsoft \ 
         -Dversion=3.0 \
         -Dpackaging=jar \ 
         -DgeneratePom=true

  2. Build the jdbc-sqlserver module using the *sqlServerDriver* profile:

     mvn clean install -P sqlServerDriver
  
