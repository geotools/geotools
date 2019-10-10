===
JTS
===

The JTS Topology Suite is an external project that GeoTools uses to provide an implementation of
the Geometry data structure. The major benefit is the numerically stable geometry operations as a
result of years of dedicated effort.

.. image:: /images/gt-jts.png

GeoTools is all about implementing spatial solutions, and we do our very best to follow a don't
invent here policy (rather than get off topic). The excellent **JTS Topology Suite** project offers
an implementation of Geometry which we use throughout our library.

The GeoTools provides some help for working with JTS:

* :doc:`gt-main <../main/index>` offers helper classes (such as *JTS* and *Geometries*) and extends JTS with a *CurvedGeometryFactory* for working with curves along with helper classes to translate Geometry into a Java Shape for display

**References**

* https://projects.eclipse.org/projects/locationtech.jts
* https://locationtech.github.io/jts/
* `JTS Developer Guide.pdf <https://github.com/locationtech/jts/blob/master/doc/JTS%20Developer%20Guide.pdf>`__
* `JTS Technical Specs.pdf <https://github.com/locationtech/jts/blob/master/doc/JTS%20Technical%20Specs.pdf>`__

**Maven**::
   
      <dependency>
        <groupId>org.locationtech</groupId>
        <artifactId>jts</artifactId>
        <version>1.13</version>
      </dependency>

**Contents**

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
