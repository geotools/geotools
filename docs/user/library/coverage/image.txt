Image Plugin
------------

The image module in the plugin group provides access to the 'world plus image' file formats.

This is the quickest way to get a GIF or JEPG onto a map display - simply add a small text file along side the image defining the image's extend and use this plugin.

Related:

* http://www.kralidis.ca/gis/worldfile.htm
* http://geos.gsi.gov.il/vladi/FEFLOW/help/general/file_format.html#tfw_file

The WorldImageReader allows access to image data through the GridFormatFinder. This supports raster images with an associated world file.::
  
  File file = new File("test.jpg");
  
  AbstractGridFormat format = GridFormatFinder.findFormat( file );
  AbstractGridCoverage2DReader reader = format.getReader( file );

World files have the same name as the image (different file extension) and contain just enough information to convert pixel coordinates to real world coordinates. They do not store any coordinate reference system information for the coordinates.

The gt-image plugin depends on Java to read the image file; so depending on how you have JAI / Image IO configured the following should be supported:

============= ======================= =====================
Image Format  Image Format Extension  World File Extension
============= ======================= =====================
JPEG          file.jpg or file.jpeg   file.jgw or file.wld
TIFF          file.tif or file.tiff   file.tfw or file.wld
PNG           file.png                file.pgw or file.wld
GIF           file.gif                file.gfw or file.wld
============= ======================= =====================

**World File**

A world file is a small text file that says where the corners of the image is. The extension of a world file is the first and last letters of the image extension with "w" appended at the end. The coordinates are in the units of the projections, so -180,180 -90,90 for lat/long.

If your image is not in lat/long you will have to include a .prj file, which is a text file with the WKT definition of the coordinate system that should be used.

Example contents of a world file are as follows:

+------+----------------+---------------------------------------------------------------+
|Line  | Example        | Definition                                                    |
+======+================+===============================================================+
|1     |0.0359281435    | Horizontal scale for a pixel                                  |
+------+----------------+---------------------------------------------------------------+
|2     |0.0000000000    | Rotation for row, usually zero                                |
+------+----------------+---------------------------------------------------------------+
|3     |0.0000000000    | Rotation for column, usually zero                             |
+------+----------------+---------------------------------------------------------------+
|4     |-0.0359281437   | Vertical scale for pixel, usually negative as rasters count   |
|      |                | down from the upper left corner                               |
+------+----------------+---------------------------------------------------------------+
|5     |-179.9820349282 | Translation, usually "easting" location of the upper left     |
|      |                | pixel (ie column 0)                                           |
+------+----------------+---------------------------------------------------------------+
|6     |89.9820359281   | Translation, usually "northing" location of the upper left    |
|      |                | pixel (ie row 0)                                              |
+------+----------------+---------------------------------------------------------------+

The Translation information can be thought of as the location of the upper left pixel, this
location is provided in the units of the projections being used:

* For lat/long this location should be in the range -180,180 -90,90
* If your image is not in lat/long you will have to include a .prj file, which is a text file with the
  WKT definition of the coordinate system that should be used.

**Projection File**

A projection file (prj) is WKT representation of the projection the image is in:

**image.prj**::
  
  GEOGCS["GCS_WGS_1984", DATUM["WGS_1984", SPHEROID["WGS_1984",6378137,298.257223563]],
  PRIMEM["Greenwich",0], UNIT["Degree",0.017453292519943295]]

JPEG Example
^^^^^^^^^^^^

If your image is world.jpg then you need a world.jgw file and optionally a world.prj file.

An example world.jgw::
  
  0.0359281435
  0.0000000000
  0.0000000000
  -0.0359281437
  -179.9820349282
  89.9820359281

An example world.prj is::
  
  GEOGCS["GCS_WGS_1984", DATUM["WGS_1984", SPHEROID["WGS_1984",6378137,298.257223563]],
  PRIMEM["Greenwich",0], UNIT["Degree",0.017453292519943295]]
 
