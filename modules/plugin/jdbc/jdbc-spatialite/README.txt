SpatiaLite GeoTools DataStore README
====================================

SQLiteJDBC
----------

This module utilizes the SQLiteJDBC driver made available as part of the Xerial
project:

  http://www.xerial.org/trac/Xerial/wiki/SQLiteJDBC

However to support SpatiaLite a slightly modified version of the driver is used.
The modified sources are available here:

  https://bitbucket.org/jdeolive/sqlite-jdbc/

Native Libraries
----------------

The resulting sqlitejdbc driver contains its own embedded copy of both sqlite 
spatialite. The version of spatialite is compiled against the proj and geos 
libraries.

Current library versions are as follows:

 * SQLite 3.7.2
 * SpatiaLite 2.4.0
 * GEOS 3.3.3 (3.1.1 on Windows)
 * PROJ 4.8 (4.6.1 on Windows)
 
Testing
-------

To run the unit tests in this module you must first have the proj and geos libs
installed on the system. If you don't already have them compiled or have an 
incompatible version built you can download pre-compiled binaries as described here:


Then create a test fixture file at ``%HOME%/.geotools/spatialite.properties`` with the 
following contents::

  driver=org.sqlite.JDBC
  url=jdbc:sqlite:target/geotools
  user=geotools
  username=geotools
  passwd=geotools
  password=geotools
  
To run the tests on Linux and OSX set the environment variable pointing to the location
of the proj and geos libs. For example::

  LD_LIBRARY_PATH=/usr/local/lib mvn clean install
  
Assuming the libs are installed under ``/usr/local/lib``. On OSX the command is::

  DYLD_LIBRARY_PATH=/usr/local/lib mvn clean install
  
On Windows systems no variable is needed since the DLL's must be installed in a directory
on the default search patch such as ``C:\Windows\System32``.
