EPSG HSQL Plugin
----------------

Provides an EPSG Coordinate Reference System authority based on the Java database HSQL.

The plugin includes it's own copy of the EPSG database that has been converted from the original Microsoft Access.

To use this plugin include it on your CLASSPATH.

Database Creation
^^^^^^^^^^^^^^^^^

The Plugin will unpack the database somewhere on your machine, usually where temporary files go to live.

On my Windows XP machine it is located here:

* C:\Documents and Settings\Jody\Local Settings\Temp\Geotools\Databases\HSQL

The following contents are usually in this folder::
  
  EPSG.data - this is the EPSG database around 8 megs
  EPSG.properties
  EPSG-script

Basically it works out of the box unless you are in a signed environment such as an applet.

Resetting the Database
^^^^^^^^^^^^^^^^^^^^^^

One debugging technique is to remove this folder; GeoTools will recreate it the next time it is needed. You can use this when upgrading versions of GeoTools to be assured of the latest EPSG database.
