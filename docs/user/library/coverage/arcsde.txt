ArcSDE Plugin
-------------

The gt-arcsde plugin provides both vector and raster support.

References:

* :doc:`gt-arcsde <../data/arcsde>` for configuration details

ArcSDE Raster
^^^^^^^^^^^^^

Since Geotools 2.3.x and 2.4.x.

ESRI's ArcSDE spatial database product allows raster data to be stored inside a host RDBMS. ArcSDE then provides a standard API to access the raster data. It's worth noting that the merits of storing large chunks of raster data inside an RDBMS are arguable. Perhaps the DBMS is better at handling Disk I/O, and perhaps there are enterprise or data-management advantages. However, many people actually store raster data inside ArcSDE, so there is a need to support it.

Connecting to ArcSDE Raster datasets
''''''''''''''''''''''''''''''''''''

When using ArcSDE Raster support, you can use it just like a standard GridCoverage2DReader. However,
when instantiating it, you must pass it a String or File containing the URL of the SDE instance
and raster layer you wish to read.

ArcSDE connection URLs take the following syntax::
  
  sde://<user>:<pass>@<host>[:<port>]/[dbname]#<user_qualified_raster_table_name>

Here's an example::
  
  sde://scott:tiger@localhost:5151/gis#SDE.MY_RASTER_TABLE

And a code example::
  
  import org.geotools.coverage.grid.io.GridFormatFinder;
  import org.opengis.coverage.grid.Format;
  ...
  
  String connUrl = "sde://scott:tiger@localhost:5151/gis#SDE.MY_RASTER_TABLE";
  Format gridFormat = GridFormatFinder.findFormat(connUrl);
  AbstractGridCoverage2DReader reader = format.getReader(connUrl);
  ...

Implementation Notes
^^^^^^^^^^^^^^^^^^^^

Raster Support Features
'''''''''''''''''''''''

* Connection pooling (based on GT ArcSDE connection pooling)
* Auto-discovery of available CRS information
* Pyramid support (RRD or "overviews"), if present in the underlying raster dataset
  
  * 3 or 4 Band unsigned-byte RGBX Rasters
  * Colormapped rasters
  * Non unsigned-byte Rasters

Supported Raster Types
''''''''''''''''''''''

Currently ArcSDE Raster support is limited to 3-band or 4-band, one-byte-per-band, non-colormapped rasters. Support for further ArcSDE raster types is in progress. To check out what's on the roadmap, look at the GeoTools ArcSDE Raster Format information page.

======== =======================================
Support  Raster Type
======== =======================================
yes      3 or 4 Band unsigned-byte RGBX Rasters
no       Colormapped rasters
no       Non unsigned-byte Rasters
======== =======================================

General configuration notes
'''''''''''''''''''''''''''

Also make sure that the port number specified is the same as in the services file (%(%(%systemRoot%\system32\drivers\etc\services in windows), it should contain a line like (if yours is different then change the port param of your datastore definition)::
  
  esri_sde
  5151/tcp

* Raster Support Development Status: BETA (as of 6/2/2007)
  
  Current ArcSDE raster support is available as a downloadable copy of a developers source tree.
  
  There is limited support for different kinds of rasters (see the Raster Support Features section
  below for specific details) and the code has only been used in one ArcSDE environment.
    
  The 'Raster Support Plan' section, below, outlines a plan to make the Geotools ArcSDE raster
  code both support more formats, as well as enter the mainline development tree/process.
  
  Raster Support Plan:
  
  * Step 1 - Code Review.
  * Step 2 - Commit to 2.3.x
  * Step 3 - Port to 2.4.x (trunk)
  * Step 4 - Support more raster formats (in progress 6/2/2007)

