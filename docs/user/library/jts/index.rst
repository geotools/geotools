===
JTS
===

The JTS Topology Suite is an external project that GeoTools uses to provide an implementation of the Geometry data structure. The major benefit is the numerically stable geometry operations as a result of years of dedicated effort.

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq

.. toctree::
   :maxdepth: 1

   geometry
   equals
   relate
   dim9
   operation
   filter
   simplify
   snap
   ring
   combine

GeoTools is all about implementing spatial solutions, and we do our very best to follow a don't invent here policy (rather than get off topic).
The excellent **JTS Topology Suite** project offers an implementation of Geometry which we use throughout our library.

.. image:: /images/gt-jts.png

The GeoTools provides some help for working with JTS:

* :doc:`gt-api <../api/index>` offers helper classes such as JTS and Geometries
* :doc:`gt-main <../main/index>` offers helper classes to translate Geometry into a Java Shape

References:

* http://sourceforge.net/projects/jts-topo-suite/
* http://tsusiatsoftware.net/jts/main.html
* http://www.vividsolutions.com/jts/bin/JTS%20Developer%20Guide.pdf
* http://www.vividsolutions.com/jts/bin/JTS%20Technical%20Specs.pdf
