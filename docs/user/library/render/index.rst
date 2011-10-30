Render
======

Supports the rendering of geospatial information using the Java2D api.

.. image:: /images/gt-render.png

This is most likely the reason you are interested in the GeoTools library - this module finally
lets you draw a map using all that data you set up.

**Reference**

* :doc:`/library/opengis/se`
* :doc:`/library/api/sld`
* http://www.opengeospatial.org/standards/sld
* http://www.opengeospatial.org/standards/se

**Tutorial**

* :doc:`style </tutorial/map/style>` (tutorial)

**Maven**::
   
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-referencing</artifactId>
      <version>${geotools.version}</version>
    </dependency>
    <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-epsg-hsql</artifactId>
      <version>${geotools.version}</version>
    </dependency>

**Contents**

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq

.. toctree::
   :maxdepth: 1
   
   gtrenderer
   map
   style
   icon

Graphic plugins:

.. toctree::
   :maxdepth: 1
   
   chart
   svg

Unsupported:

.. toctree::
   :maxdepth: 1
   
   wkt
   shapefile
