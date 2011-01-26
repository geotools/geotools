Oracle Spatial Data Source Implementation for Geotools

**  Build Issues  **

The Oracle spatial data source requires jars that are non-distributable by 
the GeoTools team.  These jars are the sodapi.jar and jdbc driver (ojdbc14.jar).

We do provide a dummy jar that contains stub classes to allow the Oracle
data source to build, however the real jars will be required to run anything
that uses the data source.  

Note: If you get an error message to the effect of "Invalid logical handle"
make sure you are using the JDBC driver from your Oracle installation.

To place the real jars into your maven repository:

C:> cd C:\oracle\product\10.2.0\jdbc\lib
C:\oracle\product\10.2.0\jdbc\lib>mvn install:install-file -Dfile=ojdbc14.jar -DgroupId=com.oracle -DartifactId=ojdbc14 -Dversion=10.2.0 -Dpackaging=jar

You can make maven use the real jars over the stub jar by using -Doracle=true on
the command line:

mvn clean install -Doracle=true
mvn eclipse:eclipse -Doracle=true

If you use -Doracle=false (or don't say anything) a dummy jar will be used.

Firstly, you must setup an Oracle database with spatial support and load
the tables and data found in tests/unit/testData/testData.sql. Then edit
the test.properties file to point to your database.  Then you need to
edit the project.xml file.  Find the section that has the comment saying
"This section excludes the tests" and comment it out, then uncomment the section
that says "Uncomment to run tests".  Then you can type maven test in the
oraclespatial directory.


**  Further Help **
If you are still having problems getting the Oracle Data Source
to work, contact Sean Geoghegan at geotools-devel@lists.sourceforge.net