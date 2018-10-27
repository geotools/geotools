:Author: Torben Barsballe
:Thanks: geotools-user list
:Version: |release|
:License: Create Commons with attribution

**********************
 Java 11 Quickstart 
**********************

.. sectionauthor:: Torben Barsballe <tbarsballe@boundlessgeo.com>

Welcome 
=======

GeoTools 21.0 and newer can be used in a Java 11 modular application. This requires a few additions on top of the regular quickstart.

This quickstart assumes you have already run through one of the regular quickstarts:

 * Eclipse
 * NetBeans
 * IntelliJ
 * Maven

Java Install
============

Instead of Java 8, we will want to install Java 11.

#. Download the latest Java 11 JDK:

   * Oracle JDK: http://www.oracle.com/technetwork/java/javase/downloads/
   * OpenJDK: http://openjdk.java.net/
   
#. At the time of writing the latest Java 11 release was:
   
   * jdk-8u66-windows-i586.exe
   
#. Click through the installer you will need to set an acceptance a license agreement and so forth.
   By default this will install to:
   
   :file:`C:\\Program Files (x86)\\Java\jdk1.8.0_66`
     
.. Note::

   In this tutorial we refer to file and directory paths as used by Windows. If you are fortunate
   enough to be using another operating system such as Linux or OSX all of the commands and source
   code below will work, just modify the paths to suit. 

Adding Module Info
------------------

<Link to Jigsaw ref>

Updating the POM
----------------



Running the application
-----------------------
        
#. If you need some shapefiles to work with you will find a selection of data at the
   http://www.naturalearthdata.com/ project which is supported by the North American Cartographic
   Information Society. Head to the link below and download some cultural vectors. You can use the 'Download all 50m cultural themes' at top.
   
   * `1:50m Cultural Vectors <http://www.naturalearthdata.com/downloads/50m-cultural-vectors/>`_
   
   Unzip the above data into a location you can find easily such as the desktop.

#. You can run the application using Maven on the command line::
   
     mvn exec:java -Dexec.mainClass=org.geotools.tutorial.quickstart.Quickstart
   
#. The application will connect to your shapefile, produce a map context, and display the shapefile.

   .. image:: images/QuickstartMap.png
      :scale: 60
   
#. A couple of things to note about the code example:
   
* The shapefile is not loaded into memory. Instead it is read from disk each and every time it is needed.
  This approach allows you to work with data sets larger than available memory.
      
* We are using a very basic display style here that just shows feature outlines. In the examples
  that follow we will see how to specify more sophisticated styles.


Things to Try
=============

* Try out the different sample data sets.

* You can zoom in, zoom out and show the full extent and use the info tool to examine individual
  countries in the sample countries.shp file.

* Download the largest shapefile you can find and see how quickly it can be rendered. You should
  find that the very first time it will take a while as a spatial index is generated. After that
  rendering will become much faster.
  
* Fast: We know that one of the ways people select a spatial library is based on speed. By design
  GeoTools does not load the above shapefile into memory (instead it streams it off of disk
  each time it is drawn using a spatial index to only bring the content required for display).
  
  If you would like to ask GeoTools to cache the shapefile in memory try the following code:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartCache.java
     :language: java
     :start-after: // docs start cache
     :end-before:  // docs end cache
  
  You will also need to add this import statement:

  .. code-block:: java

     import org.geotools.data.CachingFeatureSource;

  .. Hint::
     When working in a text editor instead of an IDE use the `GeoTools javadocs 
     <http://docs.geotools.org/latest/javadocs/>`_ to work out what import statements are required
     in your source. The javadocs also list the GeoTools module in which each class is found.

  .. Note::

     When building you may see a message that CachingFeatureSource is deprecated. It's ok to ignore
     it, it's just a warning. The class is still under test but usable.

..  The ability to figure out what classes to import is a key skill; we are
    starting off here with a simple example with a single import.
  
* Try and sort out what all the different "side car" files are - and what they are for. The sample
  data set includes "shp", "dbf" and "shx". How many other side car files are there?

.. This exercise asks users to locate the geotools user guide or wikipedia
  
* Advanced: The use of FileDataStoreFinder allows us to work easily with files. The other way to do
  things is with a map of connection parameters. This techniques gives us a little more control over
  how we work with a shapefile and also allows us to connect to databases and web feature servers.

.. literalinclude:: /../src/main/java/org/geotools/tutorial/quickstart/QuickstartNotes.java
   :language: java
   :start-after: // start datastore
   :end-before:  // end datastore

* So what jars did maven actually use for the Quickstart application? Try the following on the
  command line:
  
  mvn dependency:tree
  
  We will be making use of some of the project in greater depth in the remaining tutorials.
