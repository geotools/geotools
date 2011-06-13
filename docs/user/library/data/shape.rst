Shapefile Plugin
----------------

Allows the GeoTools library to work with ESRI shapefiles.

References:

* `ShapefileDataStoreFactory <http://docs.geotools.org/latest/javadocs/org/geotools/data/shapefile/ShapefileDataStoreFactory.html>`_ (javadocs)
* http://en.wikipedia.org/wiki/Shapefile
* http://en.wikipedia.org/wiki/DBase

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
| "charset"               | Optional: Chartset used to decode strings in the   |
|                         | DBF file                                           |
+-------------------------+----------------------------------------------------+
| "timezone               | Optional: Timezone used to parse dates in the      |
|                         | DBF file                                           |
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

Working with an Existing Shapefile::
  
  File file = new File("example.shp");
  Map map = new HashMap();
  map.put( "url", file.toURL() );
  DataStore dataStore = DataStoreFinder.getDataStore( Map map );
  String typeName = dataStore.getTypeNames()[0];
  
  FeatureSource source = dataStore( typeName );
  
  Filter filter = CQL.toFilter("BBOX(THE_GEOM, 10,20,30,40)");
  FeatureCollection collection = source.getFeatures( filter );
  FeatureIterator iterator = collection.iterator();
  try {
      while( iterator.hasNext() ){
           Feature feature = (Feature) iterator.next();
           ...
      }
  }
  finally {
     collection.close( iterator );
  }

Creating
''''''''

Here is a quick example::
  
  FileDataStoreFactorySpi factory = new IndexedShapefileDataStoreFactory();
  
  File file = new File("my.shp");
  Map map = Collections.singletonMap( "url", file.toURL() );
  
  DataStore myData = factory.createNewDataStore( map );
  FeatureType featureType = DataUtilities.createType( "my", "geom:Point,name:String,age:Integer,description:String" );
  myData.createSchema( featureType );

The featureType created above was just done quickly, in your application you may wish to use a DefaultFeatureTypeBuilder.

Supports:

* attribute names must be 15 characters or you will get a warning:
* a single geometry column (stored in the SHP file)
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

* only work with MultiLineStirngs, MultiPolygon or MultiPoint. GIS data often travels
  in herds - so being restricted to the plural form is not a great limitation.
* only work with fixed length strings (you will find the FeatureType
  has a restriction to help you check this, and warnings will be produced if
  your content ends up trimmed).
* Only supports a single GeometryAttribute
* Shapefile does not support plain Geometry (ie mixed LineString, Point and Polygon all in the same file).

Force Projection
''''''''''''''''

If you run the above code, and then load the result in a GIS application like ArcView it will complain that the projection is unknown.

You can "force" the projection using the following code::
  
  CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
  shape.forceCoordianteReferneceSystem( crs );

This is only a problem if you did not specify the CoordinateReferenceSystem as part of your FeatureType's GeometryAttribute, or if a prj file has not been provided.

Character Sets
''''''''''''''

If you are working with Acerbic, Chinese or Korean character sets you will need to make use of the "charset" connection parameter when setting up your shapefile. The codes used here are the same as documented/defined for the Java Charset class. Indeed you can provide a Chartset or if you provide a String it will be converted to a Charset.

Thanks to the University of Soul for providing and testing this functionality.

Timezone
''''''''

The store will build dates using the default timezone. If you need to work against metereological data the timezone has normally to be forced to "UTC" instead.


Reading PRJ
^^^^^^^^^^^

You can use the CRS utility class to read the PRJ file if required. The contents of the file are in "well known text"::
  
  CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

Reading DBF
^^^^^^^^^^^

A shapefile is actually comprised of a core "shp" file and a number of "sidecar" files. One of the sidecar files is a "dbf" file used to record attributes. This is the original DBF file format provided by one of the original grandfather databases "DBase".

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
