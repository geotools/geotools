DB2 Spatial Data Source Implementation for Geotools

**  Build Issues  **

The DB2 spatial data plug-in requires JDBC jars that are non-distributable by
the Geotools team.  These jars are the db2jcc.jar and db2jcc_license.jar.

They can be installed manually into the Maven repository with commands
like those in the file installjar.db2 in this directory.
This needs to be modified to specify the correct location of the
source jar files.

**  Running Online Tests **

Setup the geotools database using the scripts in the
\db2\test\setup directory using a DB2 command window and
the following commands:
db2 -tvf setupdb.db2
db2 -tvf import-roads.db2
db2 -tvf FIDTestSetup.db2
db2 -tvf import-places.db2

Modify \db2\test\resources\setup\localtype4.properties to use
the appropriate connection information for your geotools database
and copy this to c:\Document and Settings\userid\.geotools\db2.

Run  Maven with:
mvn -P online test

**  Further Help **
If you are still having problems getting the DB2 Data Source
to work, ask a question via geotools-devel@lists.sourceforge.net
