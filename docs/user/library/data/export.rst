Exporting
---------

One common thing people want to do is grab data existing data and export to a shapefile or PostGIS.
A lot of desktop and server applications work very well with shapefiles).

There are a few tricks to keep in mind when taking information between different formats
and we cover a few examples on this page.

References:

* :doc: `/tutorial/geometry/geometrycrs` tutorial covers transforming a shapefile

Memory
^^^^^^

When working on your own application you will often store your data in a MemoryDataStore
it is under construction.

* The following example shows how to export out a MemoryDataStore as a single shapefile:
  
   .. literalinclude:: /../src/main/java/org/geotools/data/DataExamples.java
      :language: java
      :start-after: // exportToShapefile start
      :end-before: // exportToShapefile end
  
* We also have an alternative example (thanks gaby) using FeatureWriter.
  
  FeatureSource and FeatureCollection are high level API, to get down and dirty you
  can use the low-level FeatureReader / FeatureWriter API.

   .. literalinclude:: /../src/main/java/org/geotools/data/DataExamples.java
      :language: java
      :start-after: // exportToShapefile2start
      :end-before: // exportToShapefile2 end


A couple of hints:

* Shapefile does note support "Geometry" so if you are working with mixed content
  you will need to export out three shapefiles, one for point, line and polygon.

WFS
^^^

You can use the above technique to copy from a WFS to a Shapefile.

A couple of hints:

* Please be advised that WFS TypeNames are not always valid filenames; you should take a moment to change non alphanumeric characters to "_" before generating the filename.::
  
   .. literalinclude:: /../src/main/java/org/geotools/data/DataExamples.java
      :language: java
      :start-after: // fixWFSTypeName start
      :end-before: // fixWFSTypeName end

* The WFS-T protocol does not allow us to implement createSchema so creating a new featureType
  will need to be done according to the procedure for your WFS prior to calling addFeature
  
  As an example GeoServer supports the use of a REST API for this purpose.

PostGIS
^^^^^^^

To copy information into PostGIS:

* You can export from PostGIS as expected using the examples above
* PostGIS also supports createSchema allowing you to create a new table to hold the content
* You may wish to adjust the featureType information, especially the lengths of strings
  prior to calling createSchema
