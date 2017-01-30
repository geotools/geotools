Shapefile Plugin
----------------

Allows the GeoTools library to work with ESRI shapefiles.

**References**

* `ShapefileDataStoreFactory <http://docs.geotools.org/latest/javadocs/org/geotools/data/shapefile/ShapefileDataStoreFactory.html>`_ (javadocs)
* http://en.wikipedia.org/wiki/Shapefile
* http://en.wikipedia.org/wiki/DBase

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-shapefile</artifactId>
      <version>${geotools.version}</version>
    </dependency>
    
Connection Parameters
^^^^^^^^^^^^^^^^^^^^^

The following connection parameters are available:

+-------------------------+----------------------------------------------------+
| Param                   | Description                                        |
+=========================+====================================================+
| "url"                   | A URL of the file ending in "shp"                  |
+-------------------------+----------------------------------------------------+
| "namespace"             | Optional: URI to use for the FeatureType           |
+-------------------------+----------------------------------------------------+
| "create spatial index"  | Optional: Use Boolean.TRUE to create an index      |
+-------------------------+----------------------------------------------------+
| "charset"               | Optional: Charset used to decode strings in the    |
|                         | DBF file                                           |
+-------------------------+----------------------------------------------------+
| "timezone"              | Optional: Timezone used to parse dates in the      |
|                         | DBF file                                           |
+-------------------------+----------------------------------------------------+
| "memory mapped buffer"  | Optional: memory map the files (not recommended    |
|                         | for large files under windows, defaults to false)  |
+------------------------------------------------------------------------------+
| "cache memory maps"     | Optional: when memory mapping, cache and reuse     |
|                         | memory maps (defaults to true)                     |
+------------------------------------------------------------------------------+
| "create spatial index"  | Optional: if false, won't try to create a spatial  |
|                         | index if missing (defaults to true)                |
+------------------------------------------------------------------------------+
| "enable spatial index"  | Optional: if false, the spatial index won't be used|
|                         | even if available (and won't be created if missing |
+-------------------------+----------------------------------------------------+



This information is also in the `javadocs <http://docs.geotools.org/latest/javadocs/org/geotools/data/shapefile/ShapefileDataStoreFactory.html>`_ .

Internally gt-shape provides a two implementations at this time; one for simple access and another that supports the use of an index. The factory will
be able to sort out which one is appropriate when using DataStoreFinder or FileDataStoreFinder.

Shapefile
^^^^^^^^^

A Shapefile is a common file format which contains numerous features of the same type. Each shapefile has a single feature type.

The classic three files:

* filename.shp: shapes
* filename.shx: shapes to attributes index
* filename.dbf: attributes

Basic metadata:
* filename.prj: projection

Open source extensions:

* filename.qix: quadtree spatial index
* filename.fix: feature id index
* filename.sld: style-layer-descriptor style xml object

ESRI extensions:

* filename.sbn: attribute index
* filename.sbx: spatial index
* filename.lyr: arcmap-only style object
* filename.avl: arcview style object
* filename.shp.xml: fgdc metadata

This style of file format (from the dawn of time) is referred to as "sidecar" files, at a minimum filename.shp and its sidecar file filename.dbf are needed. 

Access
''''''

Working with an Existing Shapefile:

.. literalinclude:: /../src/main/java/org/geotools/data/ShapefileExample.java
   :language: java
   :start-after: // start use
   :end-before: // end use


Creating
''''''''

Here is a quick example:

.. literalinclude:: /../src/main/java/org/geotools/data/ShapefileExample.java
   :language: java
   :start-after: // start create
   :end-before: // end create

The featureType created above was just done quickly, in your application you may wish to use a DefaultFeatureTypeBuilder.

Supports:

* attribute names must be 15 characters or you will get a warning:
* a single geometry column named "the_geom" (stored in the SHP file)
  * LineString, MultiLineString - Files occasionally contain invalid lines with one point
  * Polygon, MultiPolygon 
  * Point, MultiPoint*

* "simple" attributes (stored in the DBF file)
  
  * String  max length of 255
  * Integer
  * Double 
  * Boolean
  * Date - TimeStamp interpretation that is both date and time

Limitations:

* only work with MultiLineStrings, MultiPolygon or MultiPoint. GIS data often travels
  in herds - so being restricted to the plural form is not a great limitation.
* only work with fixed length strings (you will find the FeatureType
  has a restriction to help you check this, and warnings will be produced if
  your content ends up trimmed).
* Only supports a single GeometryAttribute
* Shapefile does not support plain Geometry (i.e. mixed LineString, Point and Polygon all in the same file).
* The shapefile maximum size is limited to 2GB (its sidecar DBF file often to 2GB, some system being able
  to read 4GB or more)

Dumping almost anything into a shapefile
''''''''''''''''''''''''''''''''''''''''

In case the feature collection to be turned into a shapefile is not fitting the shapefile format
limitations it's still possible to create shapefiles out of it, at ease, leaving all the
structural bridging work to the ``ShapefileDumper`` class.

In particular, given one or more feature collections, the dumper will:

* Reduce attribute names to the DBF accepted length, making sure there are not conflicts (counters being added at the end of the attribute name to handle this).
* Fan out multiple geometry type into parallel shapefiles, named after the original feature type, plus the geometry type as a suffix.
* Fan out multiple shapefiles in case the maximum size is reached.

Example usage:

.. literalinclude:: /../src/main/java/org/geotools/data/ShapefileExample.java
   :language: java
   :start-after: // start dumper
   :end-before: // end dumper

Force Projection
''''''''''''''''

If you run the above code, and then load the result in a GIS application like ArcView it will complain that the projection is unknown.

You can "force" the projection using the following code::
  
  CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
  shape.forceSchemaCRS( crs );

This is only a problem if you did not specify the CoordinateReferenceSystem as part of your FeatureType's GeometryAttribute, or if a prj file has not been provided.

Character Sets
''''''''''''''

If you are working with Arabic, Chinese or Korean character sets you will need to make use of the "charset" connection parameter when setting up your shapefile. The codes used here are the same as documented/defined for the Java Charset class. Indeed you can provide a Charset or if you provide a String it will be converted to a Charset.

Thanks to the University of Soul for providing and testing this functionality.

Timezone
''''''''

The store will build dates using the default timezone. If you need to work against meteorological data the timezone has normally to be forced to "UTC" instead.


Reading PRJ
^^^^^^^^^^^

You can use the CRS utility class to read the PRJ file if required. The contents of the file are in "well known text"::
  
  CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

Reading DBF
^^^^^^^^^^^

A shapefile is actually comprised of a core "shp" file and a number of "sidecar" files. One of the sidecar files is a "dbf" file used to record attributes. This is the original DBF file format provided by one of the original grandfather databases "DBase".

.. literalinclude:: /../src/main/java/org/geotools/data/ShapefileExample.java
   :language: java
   :start-after: // start read
   :end-before: // end read

The GeoTools library includes just enough DBF file format support to get out of bed in the morning; indeed you should considered these facilities an internal detail to our shapefile reading code.

Thanks to Larry Reeder form the user list for suppling the following code example::
  
  // Here's an example that should work (warning, I haven't
  // tried to compile this).  The example assumes the first field has a
  // character data type and the second has a numeric data type:
  
  FileInputStream fis = new FileInputStream( "yourfile.dbf" );
  DbaseFileReader dbfReader =  new DbaseFileReader(fis.getChannel(),
  false,  Charset.forName("ISO-8859-1"));
  
  while ( dbfReader.hasNext() ){
     final Object[] fields = dbfReader.readEntry();
     
     String field1 = (String) fields[0];
     Integer field2 = (Integer) fields[1];
     
     System.out.println("DBF field 1 value is: " + field1);
     System.out.println("DBF field 2 value is: " + field2);
  }
  
  dbfReader.close();
  fis.close();
