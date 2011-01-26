:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

.. _feature_tutorial:

********************
 Feature Tutorial
********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

.. contents::

Welcome
=======

.. sidebar:: FOSS4G

   Welcome to FOSS4G! Please grab a buddy to go through this workbook.

Welcome to Geospatial for Java -this workbook is aimed at Java developers who are new to geospatial
and would like to get started.

You should of completed either the GeoTools NetBeans Quickstart or the GeoTools Eclipse Quickstart
prior to running through this workbench. We need to be sure that you have an environment to work
in with GeoTools jars and all their dependencies. For those using maven we will start off each
section with the dependencies required.

This workbook features a new “code first” approach – we have made every effort to make these
examples both visual and code centered. We have included some background materials explaining
the concepts and ideas in case you are interested.

This workbook is part of the FOSS4G 2010 conference proceedings.

About Your Instructors

* Jody Garnett is the lead architect for the uDig project; and on the steering committee for
  GeoTools; GeoServer and uDig. Taking the role of geospatial consultant a bit too literally
  Jody has presented workshops and training courses in every continent (except Antarctica).
  Jody Garnett is an employee of LISAsoft. 

* Michael Bedward is a researcher with the NSW Department of Environment and Climate Change
  and an active contributor to the GeoTools users' list. He has a particularly wide grasp of
  all the possible mistakes one can make using GeoTools.

CSV2SHP
=======

We are trying a new track for introducing features this year; rather then reading through a
shapefile and ripping things apart in an artificial exercise, we are going to start by building
a shapefile from scratch so you get to see every last thing that goes into creating features.

The tutorial covers the following:

* Creating a FeatureType, FeatureCollection and Features
* Using a GeometryFactory to build Points
* Writing out a Shapefile
* Forcing a Projection

At the end of the tutorial you will be able to create your own custom shapefiles.   
   
Comma Separated Value
---------------------
To start with you will need a CSV file.


#. Create a text file location.csv and copy and paste the following locations into it:

   .. literalinclude:: artifacts/locations.csv

#. Or download :download:`locations.csv <artifacts/locations.csv>`.
#. Feel free to add other locations to the file such as your home town.

Dependencies
------------

Please ensure your pom.xml includes the following:

.. literalinclude:: artifacts/pom.xml
   :language: xml
   :start-after: </properties>
   :end-before: <repositories>

Note that the jars mentioned above will pull in a host of other dependencies
(such as the hsql database driver).

Main Application
----------------
1. Please create the class **Csv2Shape.java**
2. Copy and paste in the following code:

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
      :language: java
      :start-after: // start source
      :end-before: // docs break feature type

Now we look at the rest of the main method in sections...

Create a FeatureType
--------------------

We create a FeatureType to describe the data the we are importing from the CSV file and writing to a shapefile.
Here we use the DataUtilities convenience class:

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
   :language: java
   :start-after: // docs break feature type
   :end-before: // docs break feature collection

Read into a FeatureCollection
-----------------------------
We can now read the CSV File into a FeatureCollection; please note the following:

 * Use of FeatureCollections.newCollection() to create a FeatureCollection
 * Use of GeometryFactory to create new Points
 * Creation of features (SimpleFeature objects) using SimpleFeatureBuilder

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
      :language: java
      :start-after: // docs break feature collection
      :end-before: // docs break new shapefile

Create a shapefile From a FeatureCollection
-------------------------------------------

Things to note as we create the shapefile:

 * Use of DataStoreFactory with a parameter indicating we want a spatial index
 * The createSchema( SimpleFeatureType ) method to set up the shapefile
 * Our SimpleFeatureType did not include map projection (coordinate reference system) information needed to make a .prj file, so we call forceSchemaCRS to do this

 .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
    :language: java
    :start-after: // docs break new shapefil
    :end-before: // docs break transaction

Write the feature data to the shapefile
---------------------------------------

Here we use a Transaction to safely add the FeatureCollection in one go:

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
      :language: java
      :start-after: // docs break transaction
      :end-before: // end main

This completes the main method.

Prompt for the output shapefile
-------------------------------

This method prompts the user for an appropriate shapefile to write out to. The original csv file is used to determine a good default
shapefile name.

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
      :language: java
      :start-after: // start get shapefile
      :end-before: // end get shapefile


Running the Application
-----------------------

.. sidebar:: Show Your Shapefile

  You might like to see if you can view the new shapefile using the Quickstart.java application from
  the previous tutorial.

When you run this application it will prompt you for:

* The location of a CSV file to read; and then
* The a shapefile to create

Things to Try
===============

Another way to build a SimpleFeatureType
----------------------------------------

Although the DataUtilities class used above provided a quick and easy way to build a
SimpleFeatureType, for most applications you will want to take advantage of the more
flexible **SimpleFeatureTypeBuilder**. 

Here is how to use SimpleFeatureTypeBuilder to accomplish the same result:

   .. literalinclude:: ../../src/main/java/org/geotools/tutorial/feature/Csv2Shape.java
      :language: java
      :start-after: // start createFeatureType
      :end-before: // end createFeatureType

Note the use of an upper-case constant to hold the SimpleFeatureType. Because the SimpleFeatureType
class is immutable, tracking them as final variables can help you to remember that they cannot be
modified once created.

With this method our SimpleFeatureType contains a CoordinateReferenceSystem so there's no need to
call forceSchemaCRS to generate the ".prj" file. Also, we are now limiting the *Name* field to 15
characters.

Other things to try
-------------------

* Modify the code to read the feature attribute names from the data file header rather than
  hard-coding them in the application::
  
     LAT, LON, CITY, NUMBER
  
  You should be able to use the SimpleFeatureTypeBuilder
  
  .. instructor note: this is the real key to the exercise and why simple feature type builder
     exists ... allowing you to generate your own shapefile dynamically
  
* Use the Geometry “buffer” method to create circles based on the population size of the each city.

    .. code-block:: java

       Polygon polygon = location.buffer( 0.1 );

   .. instructor note: the code above is a trick; to see if they can correctly change their
      feature type to be a polgon
         
* It is easy to write a quick CSVReader as we have done here; but harder to write a good one that
  can handle quotation marks correctly. JavaCSV is an open source library to read CSV files with
  a variety of options. 

* To quickly find dependencies you can use the website http://mvnrepository.com/.
  
  Sites like this will directly provide you a maven dependency that you can cut and paste into
  your pom.xml.
  
  .. code-block:: xml
  
      <dependency>
        <groupId>net.sourceforge.javacsv</groupId>
        <artifactId>javacsv</artifactId>
        <version>2.0</version>
      </dependency>
   
  For a working example of how to use this library visit the http://www.csvreader.com/ website.
  
* Use the same techniques to create shapefiles from data in other structured file formats such as
  geojson
  
* The earth has just passed through a meteor storm – generate 100 circles of different sizes across
  the globe. Was your town hit?

  Generating a shapefile from a model or analysis is a common use.

  .. instructor note: either they display two shapefiles and look to see if their town was hit or
     they can do a contains test for their town against each circle
     
* Read up about the other Geometry classes supported by shapefiles: MultiLineString for linear
  features and MultiPolygon for area features and modify this example to work with these.

Feature
========

.. sidebar:: Conceptual Feature

   You can also draw ideas like urban growth or predicted rain fall.
   
A feature is quite simply something that can be drawn on a map. The strict definition is that a
feature is something in the real world – a feature of the landscape - Mt Everest, the Eiffel
Tower, or even something that moves around like your great aunt Alice.

Explaining the concept to Java developers is easy - a feature is an Object.

Like a java object features can contain some information about the real world thing that they
represent. This information is organized into attributes just as in Java information is slotted
into fields.

.. sidebar:: Key
   
   The "feature types" for a map are listed in the map key.
   
   .. image:: images/key.png
       :scale: 40
   
Occasionally you have two features that have a lot in common. You may have the LAX airport in Los
Angeles and the SYD airport in Sydney. Because these two features have a couple of things in common
it is nice to group them together  - in Java we would create a Class called Airport. On a map we
will create a Feature Type called Airport.

 .. image:: images/airport.png
 
Although it is not a capability supported by Java, early programming languages made use of a
prototype system (rather than a class system) that supported lots of “one off” Objects. You will
find this situation is fairly common when making maps – since how many Eiffel towers are there?
You will also occasionally find the same real world thing represented a couple of different ways
(the Eiffel tower can be a landmark or a tower depending on context).

Here is a handy cheat sheet:

  ====================== =============================
   Java                   GeoSpatial
  ====================== =============================
   Object                 Feature
   Class                  FeatureType
   Field                  Attribute
   Method                 Operation
  ====================== =============================

The Feature model is actually a little bit more crazy then us Java programmers are used to since
it considers both attribute and operation to be “properties” of a Feature. Perhaps when Java gets
closures we may be able to catch up.

The really interesting thing for me is that map makers were sorting out all this stuff back in the
1400s and got every bit as geeky as programmers do now. So although we would love to teach them
about object oriented programing they already have a rich set of ideas to describe the world. On
the bright side, map makers are starting to use UML diagrams.

FeatureClass
------------

In GeoTools we have an interface for Feature, FeatureType and Attribute provided by the GeoAPI
project. In general GeoAPI provides a very strict interface and GeoTools will provide a class.

 .. image:: images/feature.png
 
It is very common for a Feature to have only simple Attributes (String, Integer, Date and so on)
rather then references to other Features, or data structures such as List<Date>.  Features that
meet this requirement are so common we have broken out a sub-class to represent them called
SimpleFeature. 

At the Java level the Feature API provided by GeoTools is similar to how java.util.Map is used – it
is simply a data structure used to hold information. You can look up attribute values by key; and
the list of keys is provided by the FeatureType.

Geometry
--------

The other difference between an Object and a Feature is that a Feature has some form of location
information (if not we would not be able to draw it on a map). The location information is going
to be captured by a “Geometry” (or shape) that is stored in an attribute.

 .. image:: images/geometry.png
 
We make use of the JTS Topology Suite (JTS) to represent Geometry. The JTS library provides an
excellent implementation of Geometry – and gets geeky points for having a recursive acronym ! JTS
is an amazing library and does all the hard graph theory to let you work with geometry in a
productive fashion.

Here is an example of creating a Point using the Well-Known-Text (WKT) format.

.. code-block:: java

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
    
    WKTReader reader = new WKTReader( geometryFactory );
    Point point = (Point) reader.read("POINT (1 1)");
    
You can also create a Point by hand using the GeometryFactory directly.

.. code-block:: java

    GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
    
    Coordinate coord = new Coordinate( 1, 1 );
    Point point = geometryFactory.createPoint( coord );


DataStore
=========

We ran into DataStore already in our Quickstart. The DataStore api is used to represent a File,
Database or Service that has spatial data in it. The API has a couple of moving parts as shown
below.

 .. image:: images/datastore.png
 
The FeatureSource is used to read features, the subclass FeatureStore is used for read/write access.

The way to tell if a File can be written to in GeoTools is to use an instanceof check.

.. code-block:: java

    String typeNames = dataStore.getTypeNames()[0];
    SimpleFeatureSource source = store.getfeatureSource( typeName );
    if( source instanceof SimpleFeatureStore){
       SimpleFeatureStore store = (SimpleFeatureStore) source; // write access!   
       store.addFeatures( featureCollection );
       store.removeFeatures( filter ); // filter is like SQL WHERE
       store.modifyFeature( attribute, value, filter );
    }

We decided to handle write access as a sub-class (rather then an isWritable method) in order to keep
methods out of the way unless they could be used.

