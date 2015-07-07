Oracle (Optional)
-----------------

In order to use the Oracle module you need the proprietary JDBC driver from Oracle.

This page provides instructions for the plugins/jdbc/jdbc-oracle plugin:

* The old unsupported Oracle datastore can be built with similar instructions. You will  have to use -Doracle.jdbc in place of -Doracle when creating the Eclipse projects
* As of Oracle 10.2 oracle has decided to "seal" its jar files (a security option that can be turned on in the manifest file). This option limits access to package-protected members to classes defined in the package and in the same JAR file. This means that the only way to create a connection is via a DataSource (and we are not there yet since we still use JDBC Drivers).
   
  Please download a driver from the 10.1.x series in order to avoid the above problem.

Download the Driver and place into the Repository
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Unlike most external libraries used in GeoTools, we cannot redistribute this jar. However, you can obtain them from the Oracle website, free of charge, after registering:

1. Download the Oracle JDBC Driver: http://otn.oracle.com/software/tech/java/sqlj_jdbc/content.html
2. Once you download the jar, you should place them in the Oracle Maven repository directory::
      
      mvn install:install-file -Dfile=ojdbc7.jar -DgroupId=com.oracle -DartifactId=ojdbc7 -Dversion=12.1.0.2 -Dpackaging=jar -DgeneratePom=true

Switching between Oracle Profiles with oracle.jdbc property
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The oracle modules are all set up to work with a pretend "mock" jdbc driver in order to compile. To actually use the real thing you are going to need to set the oracle.jdbc property.

You will need to do this each time on the command line (as of Maven 2.0.4 there is no way to set a property in your setting.xml file that can be used to configure other profiles):

Here is an example that builds eclipse .classpath and .project files using the real driver::
   
   mvn eclipse:eclipse -Doracle

If the property is not set the “dummy” jdbc driver will be used.
