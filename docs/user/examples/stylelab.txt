.. _stylelab:

Style Lab
===========

Style is all about looking good. In this lab we are going to learn the basics of how to make good looking maps.

Dependencies
------------
 
Please ensure your pom.xml includes the following::

    <properties>
        <geotools.version>2.6.4</geotools.version>
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
 * Directly from svn: StyleLab.java_
 * Included in the demo directory when you download the GeoTools source code

.. _StyleLab.java: http://svn.osgeo.org/geotools/tags/2.6.4/demo/example/src/main/java/org/geotools/demo/StyleLab.java 

Main Application
----------------
1. Please create the file **StyleLab.java**
2. Copy and paste in the following code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleLab.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end main

Displaying a shapefile
----------------------

If you have worked through the previous labs, most of this method will look familiar to you:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleLab.java
      :language: java
      :start-after: // docs start display
      :end-before: // docs end display

The main thing to note is that we are calling a **createStyle** method to set a Style for the map layer.
Let's look at this method next.

Creating a style
----------------

This method first looks to see if there is an SLD document (Styled Layer Descriptor) associated with the shapefile.
If it finds one it processes that file to create the style. Otherwise, it displays a **JSimpleStyleDialog** to
prompt the user for style choices:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleLab.java
      :language: java
      :start-after: // docs start create style
      :end-before: // docs end create style

The following two methods do the work of figuring out the SLD file name, based on the shapefile name, and processing
the SLD document if one is found:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleLab.java
      :language: java
      :start-after: // docs start sld
      :end-before: // docs end sld

Creating styles programmatically
--------------------------------

The methods that we've looked at so far are all we really need in this simple application. But now let's look at how to create a style programmatically.
This illustrates some of what is happening behind the scenes in the previous code. It also introduces you to **StyleFactory** and **FilterFactory** 
which provide a huge amount of flexibility in the styles that you can create.

In the code below, the first method works out what type of geometry we have in our shapefile: points, lines or polygons. It then calls a geometry-specific
method to create a Style object.

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleLab.java
      :language: java
      :start-after: // docs start alternative
      :end-before: // docs end alternative

Things to note:

* Each of the geometry specific methods is creating a type of **Symbolizer**: the class that controls how features are rendered
* Each method wraps the symbolizer in a **Rule**, then a **FeatureTypeStyle**, and finally a **Style**
* In real life, it is common to have more than one Rule in a FeatureTypeStyle. For example, we might create one rule to draw features when the 
  map is zoomed out, and another for when we are displaying fine details.



