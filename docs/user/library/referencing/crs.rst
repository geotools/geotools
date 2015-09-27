CRS
---

We have provided the **CRS** helper class as your first point of call when working with the referencing module. This class allows you to quickly accomplish the most common referencing tasks.

References:

* http://docs.geotools.org/stable/javadocs/org/geotools/referencing/CRS.html

*Before you Start*

The most conservative way to deal with the definition of a **CoordinateReferenceSystem** is not
to. Instead make use of an authority that provides complete definitions defined by a simple code.

To hook this up make sure you have one of the **gt-epsg** plugins on your CLASSPATH. The
**gt-epsg-hsql** plugin is recommended.

Defining a CoordinateReferenceSystem
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

This is easily the most common use for the CRS class::
  
  import org.geotools.referencing.CRS;
  
  CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");

The example above assumes you have **gt-epsg-hsql** jar on your CLASSPATH.

Depending on the jars you have on your CLASSPATH a range of authorities are available to define a
coordinate reference system for you.

Using the "OGC URN" syntax::
  
  CoordinateReferenceSystem sourceCRS = CRS.decode("urn:ogc:def:ellipsoid:EPSG:6.0:7001");

Using the WMS AUTO2 syntax (which requires you pass in your current "position"::
  
  CoordinateReferenceSystem sourceCRS = CRS.decode("AUTO2:42001,"+lat+","+lon);

Well Known Text
'''''''''''''''

**CoordinateReferenceSystem** can also be defined by a text format ((called "Well Known Text" or WKT). This is a standard provided by the OGC and shows up in inside a shapefile "prj" file, or in a databases such as PostGIS and Oracle.

To parse WKT please use the CRS.parseWKT( txt ) method::
  
  String wkt = "GEOGCS[" + "\"WGS 84\"," + "  DATUM[" + "    \"WGS_1984\","
          + "    SPHEROID[\"WGS 84\",6378137,298.257223563,AUTHORITY[\"EPSG\",\"7030\"]],"
          + "    TOWGS84[0,0,0,0,0,0,0]," + "    AUTHORITY[\"EPSG\",\"6326\"]],"
          + "  PRIMEM[\"Greenwich\",0,AUTHORITY[\"EPSG\",\"8901\"]],"
          + "  UNIT[\"DMSH\",0.0174532925199433,AUTHORITY[\"EPSG\",\"9108\"]],"
          + "  AXIS[\"Lat\",NORTH]," + "  AXIS[\"Long\",EAST],"
          + "  AUTHORITY[\"EPSG\",\"4326\"]]";

  CoordinateReferenceSystem crs = CRS.parseWKT(wkt);

The different organisations each have slightly different ideas on how some of the names work in
the WKT standard. GeoTools does a good job of listing common aliases in order to read WKT
generated from a range of tools.

To generate WKT:

* To generate WKT against the standard:
    
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // toWKT start
     :end-before: // toWKT end
  
  produces the following::
    
    PROJCS["WGS 84 / UTM zone 35S", 
      GEOGCS["WGS 84", 
        DATUM["World Geodetic System 1984", 
          SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]], 
          AUTHORITY["EPSG","6326"]], 
        PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]], 
        UNIT["degree", 0.017453292519943295], 
        AXIS["Geodetic latitude", NORTH], 
        AXIS["Geodetic longitude", EAST], 
        AUTHORITY["EPSG","4326"]], 
      PROJECTION["Transverse Mercator", AUTHORITY["EPSG","9807"]], 
      PARAMETER["central_meridian", 27.0], 
      PARAMETER["latitude_of_origin", 0.0], 
      PARAMETER["scale_factor", 0.9996], 
      PARAMETER["false_easting", 500000.0], 
      PARAMETER["false_northing", 10000000.0], 
      UNIT["m", 1.0], 
      AXIS["Easting", EAST], 
      AXIS["Northing", NORTH], 
      AUTHORITY["EPSG","32735"]]

* To generate WKT that matches a bit more the ESRI use:
  
  As mentioned above different vendors don't exactly follow the standard. For the WKT above
  ESRI ArcMap produces the following::
     
     PROJCS["WGS_1984_UTM_Zone_35S",GEOGCS["GCS_WGS_1984",DATUM["D_WGS_1984",SPHEROID["WGS_1984",6378137,298.257223563]],
     PRIMEM["Greenwich",0],UNIT["Degree",0.017453292519943295]],PROJECTION["Transverse_Mercator"],PARAMETER["Central_Meridian",27],
     PARAMETER["Latitude_Of_Origin",0],PARAMETER["Scale_Factor",0.9996],PARAMETER["False_Easting",500000],PARAMETER["False_Northing",10000000],
     UNIT["Meter",1]
  
  Notable differences are:
  
  * the way the datum, unit of measure, projection and projection parameters are spelled out
  * it's all in one line.
  
  We can produce something similar using:
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // toWKTFormat start
     :end-before: // toWKTFormat end
  
  You can change the indent to 0 in order to get everything on a single line.
  
  Produces the following::
  
    PROJCS["WGS 84 / UTM zone 35S", 
      GEOGCS["WGS 84", 
        DATUM["D_WGS_1984", 
          SPHEROID["D_WGS_1984", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]], 
          AUTHORITY["EPSG","6326"]], 
        PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]], 
        UNIT["degree", 0.017453292519943295], 
        AXIS["Geodetic longitude", EAST], 
        AXIS["Geodetic latitude", NORTH], 
        AUTHORITY["EPSG","4326"]], 
      PROJECTION["Transverse Mercator", AUTHORITY["EPSG","9807"]], 
      PARAMETER["Longitude_Of_Origin", 27.0], 
      PARAMETER["Latitude_Of_Center", 0.0], 
      PARAMETER["scale_factor", 0.9996], 
      PARAMETER["false_easting", 500000.0], 
      PARAMETER["false_northing", 10000000.0], 
      UNIT["m", 1.0], 
      AXIS["Easting", EAST], 
      AXIS["Northing", NORTH], 
      AUTHORITY["EPSG","32735"]]
  
  While not 1-1 equal, the datum, projection, projection parameters and units are spelled
  out the same.

.. note:: Formatable
  
  The code above casted the CRS into a Formattable object, that gives the developer more
  control on how things are converted in WKT, and then asked to generate the WKT using the
  ESRI aliases and 2 indentation when generating the output.
  
  The default is CITATIONS.ESPG, indent 2 instead.

Matching a CoordinateReferenceSystem
''''''''''''''''''''''''''''''''''''

You can actually search based on any metadata, not just name, the way you do it is you construct
an example of what you are looking for - and than ask for the best match.

This functionality is especially useful when you have produced a CoordinateReferenceSystem by
parsing WKT and you would like to find the "official" code for it.::
  
  String wkt =
      "GEOGCS[\"ED50\",\n" +
      "  DATUM[\"European Datum 1950\",\n" +
      "  SPHEROID[\"International 1924\", 6378388.0, 297.0]],\n" +
      "PRIMEM[\"Greenwich\", 0.0],\n" +
      "UNIT[\"degree\", 0.017453292519943295]]";
  CoordinateReferenceSystem example = CRS.parseWKT(wkt);
  
  String code = CRS.lookupIdentifier( example, true ); // should be "EPSG:4230"
  CoordinateReferenceSystem crs = CRS.decode( code );

In the above example the projected is named "ED50", which is not the official name.

Finding a Math Transform
^^^^^^^^^^^^^^^^^^^^^^^^

Here is a quick sample use of the CRS class::
  
  import org.geotools.referencing.CRS;
  
  CoordinateReferenceSystem sourceCRS = CRS.decode("EPSG:4326");
  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
  
  MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, true);

When using a CoordinateReferenceSystem that has been parsed from WKT you will
often need to "relax" the accuracy by setting the **lenient** parameter to true when searching with findMathTransform.

The official CoordinateReferenceSystem definitions provided by the EPSG database have extra metadata (describing how to do Datum shifts for example), beyond what can be provided using WKT.::
  
  import org.geotools.referencing.CRS;
  
  String wkt = "PROJCS[\"NAD83 / BC Albers\","+
    "GEOGCS[\"NAD83\", "+
    "  DATUM[\"North_American_Datum_1983\", "+
    "    SPHEROID[\"GRS 1980\", 6378137.0, 298.257222101, AUTHORITY[\"EPSG\",\"7019\"]], "+
    "    TOWGS84[0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0], "+
    "    AUTHORITY[\"EPSG\",\"6269\"]], "+
    "  PRIMEM[\"Greenwich\", 0.0, AUTHORITY[\"EPSG\",\"8901\"]], "+
    "  UNIT[\"degree\", 0.017453292519943295], "+
    "  AXIS[\"Lon\", EAST], "+
    "  AXIS[\"Lat\", NORTH], "+
    "  AUTHORITY[\"EPSG\",\"4269\"]], "+
    "PROJECTION[\"Albers_Conic_Equal_Area\"], "+
    "PARAMETER[\"central_meridian\", -126.0], "+
    "PARAMETER[\"latitude_of_origin\", 45.0], "+
    "PARAMETER[\"standard_parallel_1\", 50.0], "+
    "PARAMETER[\"false_easting\", 1000000.0], "+
    "PARAMETER[\"false_northing\", 0.0], "+
    "PARAMETER[\"standard_parallel_2\", 58.5], "+
    "UNIT[\"m\", 1.0], "+
    "AXIS[\"x\", EAST], "+
    "AXIS[\"y\", NORTH], "+
    "AUTHORITY[\"EPSG\","3005"]]";
  CoordinateReferenceSystem example = CRS.parseWKT(wkt);
  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:4326");
  
  MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);

Transforming a Geometry
^^^^^^^^^^^^^^^^^^^^^^^

A **MathTransform**, as generated above, can be used by bashing away at the interface and feeding
it **DirectPosition** objects one at a time.

Or you could break out the JTS utility class where this work has been done for you::
  
  import org.geotools.geometry.jts.JTS;
  import org.geotools.referencing.CRS;
  
  MathTransform transform = CRS.findMathTransform(sourceCRS, targetCRS, false);
  Geometry targetGeometry = JTS.transform( sourceGeometry, transform);

Transforming an ISO Geometry is more straight forward::
  
  CoordinateReferenceSystem targetCRS = CRS.decode("EPSG:23032");
  Geometry target = geometry.transform( targetCRS );

Axis Order
^^^^^^^^^^

One thing that often comes up is the question of axis order.

The EPSG database often defines axis in an order that is inconvenient for display; we have a
method to quickly check what is going on.::
  
  if( CRS.getAxisOrder( coordianteReferenceSystem ) == CRS.AxisOrder.LAT_LON){
     // lat lon 
  }

Not all CoordinateReferenceSystems match a well defined axis order::
  
  CoordinateReferenceSystem crs = CRS.getHorizontalCRS(DefaultEngineeringCRS.GENERIC_2D));
  if( CRS.getAxisOrder(crs) == AxisOrder.INAPPLICABLE){
   // someone just made this up
  }

CoordinateReferenceSystem
^^^^^^^^^^^^^^^^^^^^^^^^^

The central user facing class for gt-referencing is **CoordinateReferenceSystem**.

Constants
'''''''''

Some CoordinateReferenceSystem instances are used so often it is worth making static final constants
for them. GeoTools has done so in order to cover the most common cases encountered when programming.

Static final constant CoordinateReferenceSystem in GeoTools:

A coordinate reference system using the WGS84 datum as an approximation of the shape of the earth:

* DefaultGeographicCRS.WGS84 - this is the most commonly used default
* DefaultGeographicCRS.WGS84_3D

A 3D coordinate reference system with the origin at the approximate centre of mass of the earth:

* DefaultGeocentricCRS.CARTESIAN
* DefaultGeocentricCRS.SPHERICAL

A contextually local coordinate reference system (for construction projects or moving objects):

* DefaultEngineeringCRS.CARTESIAN_2D (see the next section for a discussion of this value)
* DefaultEngineeringCRS.CARTESIAN_3D
* DefaultEngineeringCRS.GENERIC_2D
* DefaultEngineeringCRS.GENERIC_3D

A 1D coordinate reference system used for recording heights or depth relative to the ellipsoidal datum:

* DefaultVirticalCRS.ELLIPSOIDAL_HEIGHT

.. note::
   
   For those into the details; these static final constant CoordinateReferenceSystem cite "GeoTools"
   as the authority responsible for the definition. This is in marked contrast with the
   **CoordinateReferenceSystem** instances produced by an AuthorityFactory (those instances will
   credit a specific organisation like "EPSG").

Examples:

* Here is an example of accessing several of the predefined constants:
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // premadeObjects start
     :end-before: // premadeObjects end

* You can use the following math transform to convert from the common "long/lat" representation
  to three dimensions::
  
     MathTransform convert = CRS.findMathTransform( DefaultGeographicCRS.WGS84, DefaultGeocentricCRS.CARTESIAN);

GENERIC_2D
''''''''''

One constant deserves special mention as it is used as a "wild card" placeholder for when you
are unsure of your data. The concept of a "Generic 2D" CoordinateReferenceSystem is formally
intended for working with things like CAD drawings where the results are measured in meters.

When considered in the context of GIS we treat it as a "wildcard" allowing you to get a visual
of some sort.

Formally this is expressed by the `DefaultEngineeringCRS.GENERIC_2D javadocs <http://docs.geotools.org/latest/javadocs/org/geotools/referencing/crs/DefaultEngineeringCRS.html#GENERIC_2D>`_ as:
    A two-dimensional wildcard coordinate system with x, y axis in metres. At the difference of
    CARTESIAN_2D, this coordinate system is treated specially by the default coordinate operation
    factory with loose transformation rules: if no transformation path were found (for example
    through a derived CRS), then the transformation from this CRS to any CRS with a compatible
    number of dimensions is assumed to be the identity transform. This CRS is useful as a
    kind of wildcard when no CRS were explicitly specified.

The concept is available two ways:
  
* DefaultEngineeringCRS.GENERIC_2D
    
  This option lacks an EPSG identifier hindering interoperability with external systems.

* Using the code "EPSG:404000" (a custom code defined by GeoTools)::

    CoordinateReferenceSystem generic = CRS.decode("EPSG:404000");
  
  This value the same as DefaultEngineeringCRS.GENERIC_2D (with a epsg identifier and description).
  Since only this descriptive information is different *equals ignores metadata* will return true.
  
  The same value is also provided as a static constant::
     
     CartesianAuthoryFactory.GENERIC_2D
  
  This is the preferred way to represent an unknown CoordinateReferenceSystem in GeoTools.

Google Maps
'''''''''''

Google maps uses a bit of a shortcut, they make the assumption of a perfect sphere in order to be
just that much faster (after all they want a pretty picture nothing more).

GeoTools contains an implementation of Google Mercator (it was originally done as an experiment
in GeoServer).

Since this code has been donated you integrate your information with projection.

References:

* http://www.iter.dk/post/2008/05/SphericalWeb-Mercator-EPSG-code-3785.aspx
* http://johndeck.blogspot.com/2005/09/overlaying-mercator-projected-wms.html
* http://trac.openlayers.org/wiki/SphericalMercator

Using an EPSG code to look up the CoordinateReferenceSystem::
  
     CoordinateReferenceSystem sphericalMercator = CRS.decode("EPSG:3857");

If you are using an older copy of the EPSG database, the above code may not be supported yet.

Before this code was official there were a couple earlier attempts::
  
  // Google == 9009l3 in leet! (as defined by GeoServer)
  CoordinateReferenceSystem sphericalMercator = CRS.decode("EPSG:900913");
  // Deprecated EPSG code (they messed up something and issued EPSG:3857 as a replacement)
  CoordinateReferenceSystem sphericalMercator = CRS.decode("EPSG:3785");

Other than that you will need to define the projection yourself using WKT; or add it
into your EPSG database.::
  
  ﻿PROJCS["Google Mercator",
    GEOGCS["WGS 84",
      DATUM["World Geodetic System 1984",
        SPHEROID["WGS 84", 6378137.0, 298.257223563, AUTHORITY["EPSG","7030"]],
        AUTHORITY["EPSG","6326"]],
      PRIMEM["Greenwich", 0.0, AUTHORITY["EPSG","8901"]],
      UNIT["degree", 0.017453292519943295],
      AXIS["Geodetic latitude", NORTH],
      AXIS["Geodetic longitude", EAST],
      AUTHORITY["EPSG","4326"]],
    PROJECTION["Mercator_1SP"],
    PARAMETER["semi_minor", 6378137.0],
    PARAMETER["latitude_of_origin", 0.0],
    PARAMETER["central_meridian", 0.0],
    PARAMETER["scale_factor", 1.0],
    PARAMETER["false_easting", 0.0],
    PARAMETER["false_northing", 0.0],
    UNIT["m", 1.0],
    AXIS["Easting", EAST],
    AXIS["Northing", NORTH],
    AUTHORITY["EPSG","900913"]]

