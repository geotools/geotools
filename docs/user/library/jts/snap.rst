Snap a Point to a Line
----------------------

This example illustrates a common spatial operation: moving or *snapping* a point to lie on a nearby
line. For instance, you might use this approach to align point locations from a mobile device with a
map of roads. The JTS library is used for this sort of task day in and day out.

What you will learn:

* Use of a spatial index to cache features in memory and search efficiently.

* Going beyond the familiar JTS Geometry class methods and making direct use of other classes.

Related material:

* http://2007.foss4g.org/presentations/view.php?abstract_id=115

To begin, we prompt the user for a shapefile containing line features and connect to it:

.. literalinclude:: /../src/main/java/org/geotools/jts/SnapToLine.java
   :language: java
   :start-after: package org.geotools
   :end-before: // load shapefile end

You might be used to working with shapefiles as a streaming data source, ie.  reading features from
disk as required. Here we optimize things by extracting the line geometries from the features and
caching them in a JTS SpatialIndex object. This gives us speed in two ways: we have the lines in
memory and can search for them efficiently by location:

.. literalinclude:: /../src/main/java/org/geotools/jts/SnapToLine.java
   :language: java
   :start-after: // load shapefile end
   :end-before: // cache features end

Notice that we wrapped each feature's line geometry in a JTS **LocationIndexedLine** object which
we will use to find the point on a line closest to a reference point. We could have loaded the lines
directly into the spatial index, but this way we will avoid wrapping each line every time it is
tested against a point.

Now let's make some pretend point data:

.. literalinclude:: /../src/main/java/org/geotools/jts/SnapToLine.java
   :language: java
   :start-after: // cache features end 
   :end-before: // generate points end

At last we are ready to snap points. We create a search envelope of fixed size around each point and
use this to query the lines in the spatial index. 

In case your shapefile is large, we'll set a time limit on how long snapping continues:

.. literalinclude:: /../src/main/java/org/geotools/jts/SnapToLine.java
   :language: java
   :start-after: // generate points end

You can experiment with this code:

* try some actual roads - using real data makes a difference
* try using a QuadTree - it is often much slower (but you can add and remove things from a QuadTree at runtime)
* are you getting whacky results? Check if your geometry.isValid() prior to using it
* try simplifying the lines prior to creating the LocationIndexedLine - it should be much faster
* If you are unsure how to do activities listed above consult the "Secrets of JTS" link provided at the top of the page.
