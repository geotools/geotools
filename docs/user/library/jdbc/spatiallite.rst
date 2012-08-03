SpatiaLite Plugin
-----------------

Supports direct access to a SQLite/SpatiaLite database.

SQLite is a popular embedded relational database. SpatiaLite is the spatial extension to SQLite.

.. note::

   This plugin uses internal versions of both SQLite (3.7.2) and SpatialLite (2.4.0).
   Therefore the plugin is only capable of accessing databases that are compatible with these 
   versions.


References:

  * http://www.sqlite.org/
  * http://www.gaia-gis.it/spatialite/

**Maven**

::

   <dependency>
      <groupId>org.geotools.jdbc</groupId>
      <artifactId>gt-jdbc-spatiallite</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Note that the groupId is **org.geotools.jdbc** for this and other JDBC plugin modules.

Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

============== ============================================
Parameter      Description
============== ============================================
"dbtype"       Must be the string "spatiallite"
"database"     The databse to connect to
"user"         User name (optional)
============== ============================================

Access
^^^^^^

Example use::
  
  Map params = new HashMap();
  params.put("dbtype", "spatialite");
  params.put("database", "geotools.db");
  
  DataStore datastore = DataStoreFinder.getDataStore(params);

Setup
^^^^^

The SpatiaLite datastore ships with its own build of the SQLite and SpatiaLite 
libraries. The SpatiaLite component has been compiled with GEOS and PROJ support
so those libraries are required for the plugin to function. Setting up the plugin
requires:

#. Install the required native libraries for your platform
#. Ensure Java is configured to locate the native libraries

Prebuilt libraries are available for all supported platforms but you may choose to 
compile your own. For more information see:
  
* http://trac.osgeo.org/proj/
* http://trac.osgeo.org/geos/
  
Supported Platforms
~~~~~~~~~~~~~~~~~~~

The following platforms are currently supported.

Native Libraries
~~~~~~~~~~~~~~~~

Prebuilt libraries of PROJ and GEOS are available for a number of platforms. 

Windows 32-bit
==============

Prebuilt 32-bit Windows DLL's for PROJ and GEOS are available from:

  http://gridlock.opengeo.org/geotools/spatialite-libs/spatialite-libs-win-x86.zip
  
These libraries along with the original header files are also available from:

  http://www.gaia-gis.it/spatialite-2.3.1/binaries.html
  
These DLL files should be installed in a directory that is on the default search
path for DLL's such as ``C:\WINDOWS\System32``.

Windows 64-bit
==============

At this time Windows 64-bit libraries are not available but the 32-bit libraries should work on 64-bit systems.

Linux 32-bit
============

Prebuilt 32-bit Linux shared libraries for PROJ and GEOS are available from:

  http://gridlock.opengeo.org/geotools/spatialite-libs/spatialite-libs-linux-x86.zip
  
Linux 64-bit
============

Prebuilt 64-bit Linux shared libraries for PROJ and GEOS are available from:

  http://gridlock.opengeo.org/geotools/spatialite-libs/spatialite-libs-linux-x86_64.zip

Mac
===

Prebuilt 32/64-bit shared libraries for PROJ and GEOS for **Intel based Macs only** are available from:
 
  http://gridlock.opengeo.org/geotools/spatialite-libs/spatialite-libs-osx-intel.zip


Java Environment
~~~~~~~~~~~~~~~~

For the SpatiaLite plugin to function Java must be configured to load the required PROJ/GEOS libraries.

Windows
=======

The easiest way to allow Java to load the required DLL's is to put them in the ``C:\WINDOWS\System32`` directory.

Linux
=====

The libraries may be installed anywhere on the system, a common place to put them is under ``/usr/local``.
Java is configured by setting the ``-Djava.library.path`` system property::

  java -Djava.library.path=/usr/local/lib
  
It also maybe required to set the ``LD_LIBRARY_PATH`` environment variable as well::

  export LD_LIBRARY_PATH=$LD_LIBRARY_PATH:/usr/local/lib
  java -Djava.library.path=/usr/local/lib

Mac
===

Same as for Linux expect that ``LD_LIBRARY_PATH`` is replaced with ``DYLD_LIBRARY_PATH``.


  
