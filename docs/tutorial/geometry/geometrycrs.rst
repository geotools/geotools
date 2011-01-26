:Author: Jody Garnett
:Author: Micheal Bedward
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

.. _geometrycrs:

***********************
 Geometry CRS Tutorial
***********************

.. sectionauthor:: Jody Garnett <jody.garnett@gmail.org>

.. contents::

Welcome
=========

Welcome to Geospatial for Java. This workbook is aimed at Java developers who
are new to geospatial and would like to get started.

Please set up your
development environment prior to starting this tutorial. We will list the maven
dependencies required at the start of the workbook.

This work book covers the dirty raw underbelly of the GIS world; yes I am afraid
we are talking about *Math*. However please do not be afraid, all the math
amounts to is shapes drawn on the earth.

This workbook is constructed in a code first manner; allowing you to work
through the code example and read on if you have any questions. This workbook is
part of the FOSS4G 2009 conference proceedings.

Jody Garnett

   Jody Garnett is the lead architect for the uDig project; and on the steering
   committee for GeoTools; GeoServer and uDig. Taking the roll of geospatial
   consultant a bit too literally Jody has presented workshops and training
   courses in every continent (except Antarctica). Jody Garnett is an employee
   of LISAsoft.

Michael Bedward

   Michael Bedward is a researcher with the NSW Department of Environment and
   Climate Change and an active contributor to the GeoTools users' list. He has
   a particularly wide grasp of all the possible mistakes one can make using
   GeoTools.

CRS Lab Application
====================

This tutorial gives a visual demonstration of coordinate reference systems by
displaying a shapefile and showing how changing the map projection morphs
the geometry of the features.

1. Please ensure your pom.xml includes the following:

.. literalinclude:: artifacts/pom.xml
        :language: xml
        :start-after: </properties>
        :end-before: <repositories>
    
2. Create the **CRSLab.java** file and copy and paste the following code.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // docs start source
   :end-before: // docs end main

3. Notice that we are customizing the JMapFrame by adding two buttons to its
   toolbar: one to check that feature geometries are valid (e.g. polygon
   boundaries are closed) and one to export reprojected feature data.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
      :language: java
      :start-after: // docs start display
      :end-before: // docs end display

4. Here is how we have configured JMapFrame

   * We have enabled a status line; this contains a button allowing the map
     coordinate reference system to be chagned.
     
   * We have enabled the toolbar and added two actions to it (which we will
     be defining in the next section).
     
Validate Geometry
-------------------

Our toobar action is implemented as a nested class, with most of the work being
done by a helper method in the parent class.

1. Create the **ValidateGeometryAction** mentioned in the previous section
   as an inner class.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // docs start validate action
   :end-before: // docs end validate action

2. This method checks the geometry associated with each feature in our shapefile
   for common problems (such as polygons not having closed boundaries).

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // docs start validate
   :end-before: // docs end validate

Export Reprojected Shapefile
-----------------------------

.. sidebar:: FeatureIterator
   
   Please close feature iterator after use to prevent resource leaks.

The next action will form a little utility that can  read in a shapefile and
write out a shapefile in a different coordinate reference system.

One important thing to pick up from this lab is how easy it is to create a
MathTransform between two CoordinateReferenceSystems. You can use the
MathTransform to transform points one at a time; or use the JTS utility class
to create a copy of a Geometry with the points modified.

We use similar steps to export a shapefile as used by the csv 2 shp example.
In this case we are reading the contents from an existing shapefile using
a **FeatureIterator**; and writing out the contents one at a time
using a **FeatureWriter**. Please close these objects after use.

1. The action is a nested class that delegates to the exportToShapefile
   method in the parent class.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
    :language: java
    :start-after: // docs start export action
    :end-before: // docs end export action

2. Exporting reprojected data to a shapefile

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // docs start export
   :end-before: // set up the math transform used to process the data
      
3. Set up a math transform used to process the data

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // set up the math transform used to process the data
   :end-before: // grab all features

4. Grab all features

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // grab all features
   :end-before: // And create a new Shapefile with a slight modified schema

5. To create a new shapefile we will need to produce a FeatureType that is
   similar to our original. The only difference will be the
   CoordinateReferenceSystem of the geometry descriptor.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // And create a new Shapefile with a slight modified schema
   :end-before: // carefully open an iterator and writer to process the results
        
6. We can now carefully open an iterator to go through the contents, and a
   writer to write out the new Shapefile.
        
.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // carefully open an iterator and writer to process the results
   :end-before: // docs end export

Running the Application
========================

To switch between map projections:

1. When you start the application you will be prompted for a shapefile to display.
   In the screenshots below we are using the *bc_border* map which can be
   downloaded as part of the uDig sample data file:
   `data-v1_2.zip <http://udig.refractions.net/docs/data-v1_2.zip>`

.. image:: images/CRSLab_start.png
   :width: 60%
      
2. GeoTools includes a very extensive database of map projections defined by
   EPSG reference numbers. For our example shapefile, an appropriate alternative
   map projection is *BC Albers*.
   
   You can find this quickly in the chooser list by typing 3005.

   When you click OK the map is displayed in the new projection:

.. image:: images/CRSLab_reprojected.png
      :width: 60%
      
3. Note that when you move the mouse over the map the coordinates are now
   displayed in metres (the unit of measurement that applies to the *BC Albers*
   projection) rather than degrees.

4. To return to the original projection, open the CRS chooser again and type
    **4326** for the default geographic projection.
    
    Notice that the map coordinates are now expressed in degrees once again.

Exporting the reprojected data:

1. When you change the map projection for the display the shapefile remains
   unchanged.
   
   With the *bc_border* shapefile, the feature data are still in degrees but
   when we select the *BC Albers* projection the features are reprojected on
   the fly.

2. Set the display of reprojected data (e.g. 3005 BC Albers for the *bc_border*
   shapefile).
   
3. Click the *Validate geometry* button to check feature geometries are ok.
4. If there are no geometry problems, click the *Export* button and enter a name
   and path for the new shapefile.

Things to Try
=============

Here are a couple things to try with the above application.

* Have a look at the coordinates displayed at the bottom of the screen in
  EPSG:4326 and in EPSG:3005. You should be able to see that one is measured in
  degrees and the other measured in meters.

* Make a button to print out the map coordinate reference system as human
  readable "Well Known Text". This is the same text format used by a
  shapefile's "prj" side car file.

* It is bad manners to keep the user waiting; the SwingWorker class is part of
  Java 6. GeoTools also includes SwingWorker the **gt-swing** module for use in
  Java 5 applications.
  
  Replace your ValidateGeometryAction with the following: 
  
.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // docs start validate action2
   :end-before: // docs end validate action2

* Visit the JTS web site and look up how to simplify geometry. Modify the example
  to simplify the geometry before writing it out -  there are several  techniques
  to try (the TopologyPreservingSimplifier and DouglasPeuckerSimplifier classes
  are recommended).

  This exercise is a common form of data preparation.

* One thing that can be dangerous about geometry, especially ones you make
  yourself, is that they can be invalid. 
  
  There are many tricks to fixing an invalid geometry. An easy place to start
  is to use geometry.buffer(0). Use this tip to build your own shapefile
  data cleaner cleaner.
  
* An alternate to doing all the geometry transformations by hand is to ask
  for the data in the projection required.

  This version of the export method shows how to use a **Query** object to
  retrieve reprojected features and write them to a new shapefile instead of
  transforming the features 'by hand' as we did above.

.. literalinclude:: ../../src/main/java/org/geotools/tutorial/crs/CRSLab.java
   :language: java
   :start-after: // We can now query to retrieve a FeatureCollection in the desired crs
   :end-before: // docs end export2
      
Geometry
========

Geometry is literally the shape of GIS.

Usually there is one geometry for a feature; and the attributes provide further
description or measurement.  It is sometimes hard to think of the geometry as
being another attribute. It helps if you consider situations where there are
several representations of the same thing.

We can represent the city of Sydney:

* as a single location, ie. a point
* as the city limits (so you can tell when you are inside Sydney), ie.  a polygon

Point
-----
Here is an example of creating a point using the Well-Known-Text (WKT) format.

.. code-block:: java

   GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

   WKTReader reader = new WKTReader( geometryFactory );
   Point point = (Point) reader.read("POINT (1 1)");

You can also create a Point by hand using the GeometryFactory directly.

.. code-block:: java

   GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
   Coordinate coord = new Coordinate( 1, 1 );
   Point point = geometryFactory.createPoint( coord );

Line
----
Here is an example of a WKT LineString.

.. code-block:: java

	GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	
	WKTReader reader = new WKTReader( geometryFactory );
	LineString line = (LineString) reader.read("LINESTRING(0 2, 2 0, 8 6)");

A LineString is a sequence of segments in the same manner as a java String is a
sequence of characters.

Here is an example using the Geometry Factory.

.. code-block:: java

	GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );

	Coordinate[] coords  =
    	new Coordinate[] {new Coordinate(0, 2), new Coordinate(2, 0), new Coordinate(8, 6) };

	LineString line = geometryFactory.createLineString(coordinates);
	
	
Polygon
-------

A polygon is formed in WKT by constructing an outer ring, and then a series of holes.

.. code-block:: java

	GeometryFactory geometryFactory = JTSFactoryFinder.getGeometryFactory( null );
	WKTReader reader = new WKTReader( geometryFactory );
	Polygon polygon = (Polygon) reader.read("POLYGON((20 10, 30 0, 40 10, 30 20, 20 10))");

Why not use Java Shape
----------------------

Java Shape is actually very useful and covers ideas mentioned above – it is
however very focused on drawing. Geometry allows us to handle the “information”
part of Geographic  Information System – we can use it to create new geometry
and test the relationships between geometries.

.. code-block:: java

        // Create Geometry using other Geometry
        Geometry smoke = fire.buffer( 10 );        
        Geometry evacuate = cities.intersection( smoke );
        
        // test important relationships
        boolean onFire = me.intersects( fire );
        boolean thatIntoYou = me.disjoint( you );
        boolean run = you.isWithinDistance( fire, 2 );
        
        // relationships actually captured as a fancy
        // String called an intersection matrix
        //
        IntersectionMatrix matrix = he.relate( you );        
        thatIntoYou = matrix.isDisjoint();

        // it really is a fancy string; you can do
        // pattern matching to sort out what the geometries
        // being compared are up to
        boolean disjoint = matrix.matches("FF*FF****");

You are encouraged to read the javadocs for JTS which contains helpful definitions.

.. tip::
	The disjoint predicate has the following equivalent definitions:
	
	* The two geometries have no point in common
	* The DE-9IM Intersection Matrix for the two geometries is FF*FF****
	* !g.intersects(this) (disjoint is the inverse of intersects)

Coordinate Reference System
===========================

Earlier we talked about the JTS library which provides our data model for
Geometry. This is the real rocket science for map making – the idea of a shape
and enough math to do something fun with it. But there is one question we have
not answered yet – what does a geometry mean?

You may think I am joking but the question is serious. A Geometry is just a
bunch of math (a set of points in the mathematical sense). They have no meaning
on their own.

An easier example is the number 3. The number 3 has no meaning on
its own. It is only when you attach a “unit” that  the number 3 can represent a
concept. 3 metres. 3 feet. 3 score years.

In order to provide a Geometry with meaning we need to know what those
individual points mean. We need to know where they are located – and the data
structure that tells us this is called the Coordinate Reference System.

The Coordinate Reference System defines a couple of
concepts for us:

* It defines the axis used – along with the units of measure.

  So you can have lat measured in degrees , and lon measured in degrees from the
  equator.

  Or you can have x measured in metres, and y measured in metres which is very
  handy for calculating distances or areas.

* It defines the shape of the world. No really it does – not all coordinate
  reference systems imagine the same shape of the world. The CRS used by Google
  pretends the world is a perfect sphere, while the CRS used by “EPSG:4326” has a
  different shape in mind – so if you mix them up your data will be drawn in the
  wrong place.

As a programmer I view the coordinate reference system idea as a neat hack.
Everyone is really talking about points in 3D space here – and rather than
having to record x,y,z all the time we are cheating and only recording two
points (lat,lon) and using a model of the shape of the earth in order to
calculate z.

EPSG Codes
----------
The first group that cared about this stuff enough to write it down
in database form was the European Petroleum Standards Group (EPSG). The database
is distributed in Microsoft Access and is ported into all kinds of other forms
including the gt-hsql jar included with GeoTools.

EPSG:4326
   EPSG Projection 4326 - WGS 84

   .. image:: images/epsg4326.png
      :scale: 30
   
   This is the big one: nformation measured by lat/lon using decimal degrees.
        
   ``CRS.decode(“EPSG:4326”);``

   `DefaultGeographicCRS.WGS84;``

EPSG: 3785
    Popular Visualisation CRS / Mercator 

    .. image:: images/epsg3785.png
       :scale: 30
        
    The official code for the “Google map” projection used by a lot of web mapping
    applications.  It is nice to pretend the world is a sphere (it makes your math
    very fast). However it looks really odd if you draw a square in Oslo.
        
    ``CRS.decode(“EPSG:3785”);``
       
EPSG:3005
    NAD83 / BC Albers

    .. image:: images/epsg3005.png
       :scale: 30
        
    Example of an “equal area” projection for the west coast of Canada. The axes
    are measured in metres which is handy for calculating distance or area.
        
    ``CRS.decode(“EPSG:3005”);``

Note that both EPSG:4326 and EPSG:3785 are using lat/lon – but arrive at a very
different shape for their map.

Axis Order
----------

.. sidebar:: Why

   When navigating by stars you can figure out latitude by the angle to the
   north star – but you need to guess for longitude based on how many days you
   have been traveling. Hence y/x order.

This is also where I need to make a public apology. As computer scientists we
occasionally get fed up when we work in a domain where “they are doing it
wrong”. Map making is an example of this. When we arrived on the scene maps were
always recording position in latitude, followed by longitude; that is, with the
north-south axis first and the east-west access second. When you draw that on
the screen quickly it looks like the world is sideways as the coordinates are
in”y/x” to my way of thinking and you need to swap them before drawing.

.. image:: images/axisOrder.png
   :scale: 40

We are so used to working in x/y order that we would end up assuming it was
supposed to be this way – and have been fighting with map makers ever since.

So if you see some data in “EPSG:4326” you have no idea if it is in x/y order or
in y/x order.

We have finally sorted out an alternative; rather then EPSG:4326 we are supposed
to use “urn:ogc:def:crs:EPSG:6.6:4326“. If you ever see that you can be sure
that a) someone really knows what they are doing and b) the data is recorded in
exactly the order defined by the EPSG database.

For more Information
---------------------

`EPSG registry <http://www.epsg-registry.org/>`_ 
  This is *the* place to go to look up map projections. You can search by
  geographic area, name and type and epsg code.

`Online coordinate conversion tool <http://gist.fsv.cvut.cz:8080/webref/>`_
  Produced by Jan Jezek and powered by GeoTools.

`Wikibook: Coordinate Reference Systems and Positioning <http://en.wikibooks.org/wiki/Coordinate_Reference_Systems_and_Positioning>`_
  A summary page with some useful definition and links to more detailed information

  
