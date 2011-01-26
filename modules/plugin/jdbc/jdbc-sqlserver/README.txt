GeoTools SQL Server DataStore README
====================================

JDBC Driver
-----------

To use the sql server datastore the jdbc driver must be downloaded from 
Microsoft:

  http://msdn.microsoft.com/en-us/data/aa937724.aspx

The current tested driver version is 1.2 (SQL Server 2005). 

Maven
-----

If developing with the datastore and the rest of GeoTools: 

  1. install the JDBC driver into the location maven repository:

     mvn install:install-file  -Dfile=<path to sqljdbc.jar>
         -DartifactId=sqljdbc \ 
         -DgroupId=com.microsoft \ 
         -Dversion=1.2 \
         -Dpackaging=jar \ 
         -DgeneratePom=true

  2. Build the jdbc-sqlserver module using the *sqlServerDriver* profile:

     mvn clean install -P sqlServerDriver
  
