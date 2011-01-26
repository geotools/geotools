.. _crslab:

CRS Lab
=======

This tutorial gives a visual demonstration of coordinate reference systems by displaying
a shapefile and showing how changing the map projection morphs the shape of the features.

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
      <dependency>
          <groupId>org.geotools</groupId>
          <artifactId>gt-swing</artifactId>
          <version>${geotools.version}</version>
      </dependency>
  </dependencies>

Example
-------

The example code is available
 * Directly from svn: CRSLab.java_
 * Included in the demo directory when you download the GeoTools source code

.. _CRSLab.java: http://svn.osgeo.org/geotools/trunk/demo/example/src/main/java/org/geotools/demo/CRSLab.java 
 
Main Application
----------------
1. Please create the file **CRSLab.java**
2. Copy and paste in the following code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end main

Customizing JMapFrame
---------------------

Displaying the shapefile
~~~~~~~~~~~~~~~~~~~~~~~~

This method opens and connects to a shapefile and uses a **JMapFrame** to display it. It should look familiar to you from 
the :ref:`quickstart` example. Notice that we are customizing the JMapFrame by adding two buttons to its toolbar: one to
check that feature geometries are valid (e.g. polygon boundaries are closed) and one to export reprojected feature data.

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start display
      :end-before: // docs end display

Validate geometry button action
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

This action is implemented as a nested class. Most of the work is done by a helper method in the parent class (see 
`Validating feature geometry`_ below). 

Note the use of the SwingWorker utility class to run the validation in a background thread. This allows large shapefiles to 
be checked without locking up the GUI.

  .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start validate action
      :end-before: // docs end validate action

..

      .. admonition:: The SwingWorker class

         The SwingWorker class is part of Java 6. GeoTools also includes it in the **gt-swing** module for use in 
         Java 5 applications.

Export button action
~~~~~~~~~~~~~~~~~~~~

This is another nested class that simply delegates to the exportToShapefile method in the parent class.

  .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start export action
      :end-before: // docs end export action

Validating feature geometry
---------------------------

This method checks the geometry associated with each feature in our shapefile for common problems (such as polygons
not having closed boundaries).


   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start validate
      :end-before: // docs end validate


Exporting reprojected data to a shapefile
-----------------------------------------

  .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start export
      :end-before: // docs end export

Running the application
-----------------------

Swapping between map projections
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When you start the application you will be prompted for a shapefile to display. In the screenshots below we are 
using the *bc_border* map which can be downloaded as part of the `uDig sample data`__.

.. _udigdata: http://udig.refractions.net/docs/data-v1_2.zip

__ udigdata_

.. image:: CRSLab_start.png

Click the CRS button at the right-hand end of the status bar and choose *Set the CRS...* from the pop-up menu to
display this chooser dialog:

.. image:: CRSLab_chooser.png

GeoTools includes a very extensive database of map projections drawn from the EPSG reference (see `Some useful links`_ below).
For our example shapefile, an appropriate alternative map projection is *BC Albers* (tip: you can find this quickly in the
chooser list by typing 3005).

When you click OK the map is displayed in the new projection:

.. image:: CRSLab_reprojected.png

Note that when you move the mouse over the map the coordinates are now displayed in metres (the unit of measurement that
applies to the *BC Albers* projection) rather than degrees.

To return to the original projection, open the CRS chooser again and type **4326** for the default geographic projection.
Notice that the map coordinates are now expressed in degrees once again.

Exporting the reprojected data
~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

When you change the map projection for the display the shapefile remains unchanged. With the *bc_border* shapefile, the
feature data are still in degrees but when we select the *BC Albers* projection the features are reprojected on the fly 
by GeoTools. To reproject the underlying data we need to export a new shapefile as follows:

 * Set the display of reprojected data (e.g. 3005 BC Albers for the *bc_border* shapefile).
 * Click the *Validate geometry* button to check feature geometries are ok.
 * If there are no geometry problems, click the *Export* button and enter a name and path for the new shapefile.

An alternative export to shapefile method
-----------------------------------------

This version of the export method shows how to use a **Query** object to retrieve reprojected features and write them to
a new shapefile instead of transforming the features 'by hand' as we did above.

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/CRSLab.java
      :language: java
      :start-after: // docs start export2
      :end-before: // docs end export2

Some useful links
-----------------

`EPSG registry <http://www.epsg-registry.org/>`_ 
  This is *the* place to go to look up map projections. You can search by geographic area, name and type (and, of course, by EPSG code !).

`Online coordinate conversion tool <http://gist.fsv.cvut.cz:8080/webref/>`_
  Produced by Jan Jezek and powered by GeoTools.

`Wikibook: Coordinate Reference Systems and Positioning <http://en.wikibooks.org/wiki/Coordinate_Reference_Systems_and_Positioning>`_
  A summary page with some useful definition and links to more detailed information

