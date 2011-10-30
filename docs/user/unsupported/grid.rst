Vector grids
============

The GeoTools vector grid classes make it easy to create vector grids (also known as lattices)
consisting of either polygon or line elements, each of which is represented as a SimpleFeature.
Simple grids can be generated easily using either the Grids or Lines utility classes, while lower
level classes are availble for when more control over grid layout and attributes is required.

Note: Grids are currently constructed in memory with the whole grid being built at one time. 

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-grid</artifactId>
      <version>${geotools.version}</version>
    </dependency>


Polygon grids
-------------

The Grids utility class provides methods to create grids of rectangular or hexagonal elements.

Creating square grids
^^^^^^^^^^^^^^^^^^^^^

The easiest way to create a basic grid is with the static methods in the **Grids** utility class.
This example creates a lat-lon grid with squares 10 degrees wide to display over a map of Australia:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleSquareGrid start
   :end-before: // exampleSquareGrid end

Here is the resulting grid:

.. image:: /images/grid_square.png

Grids for display in different map projections
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The grid created in the previous section consists of SimpleFeatures, each of which has a minimal
polygon, ie. one represented by four corner vertices. This is fine if you only need to display it in
the one map projection. But say we need to display the above map in EPSG:4462 (Lambert's Conformal
Conic for Australia). The image below shows a square grid created in WGS84 (lat-lon) and then
transformed into Lambert's Conformal Conic. Because we only have vertices for the grid cell corners
the cell edges appear straight rather than curved. We've used 20 degree wide grid squares in this
example to make this effect obvious: 

.. image:: /images/grid_transformed.png

We can achieve a much better result by creating a grid where the polygons are *densified* by
inserting additional vertices along each side, so that when they are reprojected their edges
approximate curves much better:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleDensifiedSquareGrid start
   :end-before: // exampleDensifiedSquareGrid end

.. image:: /images/grid_transformed_densified.png

Creating hexagonal grids
^^^^^^^^^^^^^^^^^^^^^^^^

The Grids class also has methods to create hexagonal grids. These have the property that all six
neighbours of a grid element lie at an equal distance, in contrast to rectangular grids where the
diagonal neighbours are more distant than the orthogonal neighbours. This makes hexagonal grids
useful for analysing contagious spatial processes such as disease spread, wildfire, urban
development and animal movement.

Creating a basic hexagonal grid is simple:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleHexagonalGrid start
   :end-before: // exampleHexagonalGrid end

Which gives this result:

.. image:: /images/grid_hex.png

As with square grids, there is also a version of the createHexagonalGrid method that takes an
additional double argument for vertex spacing.

Working with a user-defined feature type
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

So far, none of the examples have required specifying a feature type for the vector grid. Instead a
default feature type was created for us with two attributes:

* 'element' (the Polygon instance)
* 'id' (a sequential integer ID value.

However, you can also provide your own feature type to associate other attributes with the grid
elements. To do this you override the **setAttributes** method of the **GridFeatureBuilder** class.
The following example creates a feature type with a 'color' attribute. The color value is then set
according to the position of each hexagonal element in the grid:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleCustomFeatureType start
   :end-before: // exampleCustomFeatureType end

Here is the result:

.. image:: /images/grid_hex_color.png

Selective creation of grid elements
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The **GridFeatureBuilder** class also offers a mechanism to choose which grid elements are created. In
the following example, a hexagonal grid is created with the constraint that only grid elements with
their center lying within the outline of Australia are included.

First, we sub-class **GridFeatureBuilder** and provide an implementation of its **getCreateFeature**
method that tests if a grid element lies within the polygon for Australia:
    
.. literalinclude:: /../src/main/java/org/geotools/grid/IntersectionBuilder.java
   :language: java
   :start-after: // IntersectionBuilder start
   :end-before: // IntersectionBuilder end

Next, we use our custom feature builder to create the grid:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleIntersection start
   :end-before: // exampleIntersection end

And here is the result:

.. image:: /images/grid_hex_shape.png

Finer control over grid element shape
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

All of the examples above use the Grids utility class. For finer control over the shape of grid
elements you can go down to the next level and use the Oblongs and Hexagons classes.

Oblongs
'''''''

This class is responsible for creating rectangular grid elements and grids (its name was chosen to
avoid confusion with java.awt.Rectangle). You can use this class directly when you want to create a
grid with rectangular, rather than square, elements as in this example:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleOblong start
   :end-before: // exampleOblong end


Hexagons
''''''''

This class is responsible for creating hexagonal grid elements and grids.

Use this class directly if you want to specify the orientation of the hexagons. Two orientations
are possible, "angled" and "flat":

.. image:: /images/grid_angled_flat.png

When you construct a hexagonal grid via the Grids class orientation defaults to "flat".
Here is how to create a grid of "angled" hexagons:

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleHexagonOrientation start
   :end-before: // exampleHexagonOrientation end

Here is what that looks like:

.. image:: /images/grid_hex_angled.png


Line grids
----------

For displaying a map grid over features you don't really need polygons. Lines will suffice and also
have the advantage that they can be more flexibly styled, e.g. different line thickness, color and
labelling for major vs minor grid lines.

The **Lines** utility class provides method to create *ortho-lines*, ie. lines parallel to the map
projection axes. In the example below, we create a grid of lines at two *levels*: major lines at
10 degree spacing and minor lines at 2 degree spacing. The levels are indicated by integer values
with larger values taking precedence over smaller (values are arbitrary, only their rank order
matters):

.. literalinclude:: /../src/main/java/org/geotools/grid/GridExamples.java
   :language: java
   :start-after: // exampleMajorMinorLines start
   :end-before: // exampleMajorMinorLines end


