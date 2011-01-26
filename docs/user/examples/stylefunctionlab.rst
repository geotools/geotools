.. _stylefunctionlab:

Style Function Lab
==================

In :ref:`stylelab` we showed how to create simple rendering styles for features. In this lab we are going to 
look at a slightly more complex example: displaying a shapfile with a style that uses a colour lookup table indexed 
by the values of a chosen feature attribute.

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

You can set the *geotools.version* property somewhere near the top of your pom.xml (see the :ref:`quickstart` if you're not sure how to do this).

Example
-------

The example code is available
 * Directly from svn: StyleFunctionLab.java_
 * Included in the demo directory when you download the GeoTools source code

.. _StyleFunctionLab.java: http://svn.osgeo.org/geotools/trunk/demo/example/src/main/java/org/geotools/demo/StyleFunctionLab.java 

Main application and display method
-----------------------------------

Please create the file **StyleFunctionLab.java** then copy and paste in the following code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleFunctionLab.java
      :language: java
      :start-after: // docs start source
      :end-before: // docs end display

If you worked through the :ref:`stylelab` most of this code will be *deja vu*. The only change is that we're now prompting the user to 
select a feature attribute and passing the name of this to a new **createStyle** method. Let's look at that method now.

Creating a dynamic Style
------------------------

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleFunctionLab.java
      :language: java
      :start-after: // docs start create style
      :end-before: // docs end create style

In :ref:`stylelab` we created Fill and Stroke objects with *literal* (ie. constant) expressions like this:

.. sourcecode:: java

        // create a partially opaque outline stroke
        Stroke stroke = styleFactory.createStroke(
                filterFactory.literal(Color.BLUE),
                filterFactory.literal(1),
                filterFactory.literal(0.5));

        // create a partial opaque fill
        Fill fill = styleFactory.createFill(
                filterFactory.literal(Color.CYAN),
                filterFactory.literal(0.5));
    
But in the method above, we've replaced the constant colour expressions with a reference to the filter function **ColorLookupFunction** to make
feature colours dynamic, ie. each feature will be coloured based on its value for the selected attribute.

The custom filter function
--------------------------

GeoTools provides a huge selection of filter functions that can be used not only to control all aspects of display but also to query feature
data in complex ways. This topic will be explored further in some of the other Labs.

Meanwhile, we're going to look at how to create a custom function that GeoTools will call to provide colours as it draws each feature in the
shapefile. Here is the function code:

   .. literalinclude:: ../../../demo/example/src/main/java/org/geotools/demo/StyleFunctionLab.java
      :language: java
      :start-after: // docs start function
      :end-before: // docs end source

*To be continued...*

