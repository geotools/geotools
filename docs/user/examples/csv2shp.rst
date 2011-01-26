.. _csv2shp:

CSV 2 SHP Lab
=============

The tutorial covers the following:

 * Creating a FeatureType, FeatureCollection and Features
 * Using a GeometryFactory to build Points
 * Writing out a Shapefile
 * Forcing a Projection

At the end of the tutorial you will be able to create your own custom shapefiles from a wide variety of data in text files !

Comma Seperated Value
---------------------
To start with you will need a CSV file.  Create a text file *location.csv* and copy and paste the following locations into it::

  "Longitude", "Latitude", "Name"
  -33.84,  151.26, Sydney
  0, 52, London
  -123.31,  48.4,  Victoria BC
  
Feel free to add other locations to the file.

Dependencies
------------

Please ensure your pom.xml includes the following::

  <properties>
      <geotools.version>2.7-SNAPSHOT</geotools.version>
  </properties>

  <dependencies>
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-shapefile</artifactId>
      <version>${geotools.version}</version>
    </dependency>
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-epsg-hsql</artifactId>
      <version>${geotools.version}</version>
    </dependency>
  </dependencies>

Note that the jars mentioned above will pull in a host of other dependencies (such as the hsql database driver).

Example
-------

The example code is available
 * Directly from svn: Csv2Shape.java_.
 * Included in the demo directory when you download the GeoTools source code

.. _Csv2Shape.java:  http://svn.osgeo.org/geotools/trunk/demo/example/src/main/java/org/geotools/demo/Csv2Shape.java

Main Application
----------------
1. Please create the file **Csv2Shape.java**
2. Copy and paste in the following code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // start source
      :end-before: // docs break feature type

Now we look at the rest of the main method in sections...

Create a FeatureType
--------------------

We create a FeatureType to describe the data the we are importing from the CSV file and writing to a shapefile.
Here we use the DataUtilities convenience class:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // docs break feature type
      :end-before: // docs break feature collection

Read into a FeatureCollection
-----------------------------
We can now read the CSV File into a FeatureCollection; please note the following:

 * Use of FeatureCollections.newCollection() to create a FeatureCollection
 * Use of GeometryFactory to create new Points
 * Creation of features (SimpleFeature objects) using SimpleFeatureBuilder

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // docs break feature collection
      :end-before: // docs break new shapefile

Create a shapefile From a FeatureCollection
-------------------------------------------

Things to note as we create the shapefile:

 * Use of DataStoreFactory with a parameter indicating we want a spatial index
 * The createSchema( SimpleFeatureType ) method to set up the shapefile
 * Our SimpleFeatureType did not include map projection (coordinate reference system) information needed to make a .prj file, so we call forceSchemaCRS to do this

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // docs break new shapefile
      :end-before: // docs break transaction

Write the feature data to the shapefile
---------------------------------------

Here we use a Transaction to safely add the FeatureCollection in one go:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // docs break transaction
      :end-before: // end main

This completes the main method.

Prompt for the output shapefile
-------------------------------

This method prompts the user for an appropriate shapefile to write out to. The original csv file is used to determine a good default
shapefile name.

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // start get shapefile
      :end-before: // end get shapefile


Running the Application
-----------------------

When you run this application it will prompt you for:

 * the location of a CSV file to read; and then
 * a shapefile to create

You might like to see if you can view the new shapefile using the :ref:`quickstart` application !

Another way to build a SimpleFeatureType
----------------------------------------

Although the DataUtilities class used above provided a quick and easy way to build a SimpleFeatureType, for most applications you will want to take advantage of the more flexible **SimpleFeatureTypeBuilder**. 

Here is how to use SimpleFeatureTypeBuilder to accomplish the same result:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/Csv2Shape.java
      :language: java
      :start-after: // start createFeatureType
      :end-before: // end createFeatureType

Note the use of an upper-case constant to hold the SimpleFeatureType. Because the SimpleFeatureType class is immutable, tracking them as 
final variables can help you to remember what they are. 

With this method our SimpleFeatureType contains a CoordinateReferenceSystem so there's no needl to call forceSchemaCRS to generate the ".prj" file. Also, we are now limiting the *Name* field to 15 characters.

Other things to try
-------------------

* Modify the code to read the feature attribute names from the data file header rather than hard-coding them in the application.
* Use the same techniques to create shapefiles from data in other structured file formats.
* Read up about the other Geometry classes supported by shapefiles: MultiLineString for linear features and MultiPolygon for areal features and modify this example to work with these.

