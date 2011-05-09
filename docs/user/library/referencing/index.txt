Referencing
===========

Used to interpret referencing information allowing the determination of a location given a set of
coordinates. Includes implementation of CoordinateReferenceSystem (CRS), conversion and
transformation services.

.. sidebar:: Details
   
   .. toctree::
      :maxdepth: 1
      
      faq
      internal

.. toctree::
   :maxdepth: 1
   
   crs
   axis
   order
   compare
   epsg
   parameter
   transform
   calculator

Referencing plugins (choose only one epsg jar):

.. toctree::
   :maxdepth: 1
   
   hsql
   access
   postgresql
   wkt
   3d
   
Unsupported plugins:
   
.. toctree::
   :maxdepth: 1
   
   h2
   oracle

The gt-referencing module is our first example that makes use of the plugin system provided
by :doc:`gt-metadata <../metadata/index>`. It does require a little bit of care when configuring
the module with appropriate epsg authority.

.. image:: /images/gt-referencing.png

The gt-referencing module is responsible for:

* Implementation of the referencing interfaces from :doc:`gt-opengis <../opengis/index>` such
  as CoordinateReferenceSystem and MathTransform
* Providing a definition for common spatial reference system codes using plugins available
  on the CLASSPATH

References:

* http://www.epsg-registry.org/

This module is basically care and feeding for the CoordinateReferenceSystem class - and enough
math to make it useful. Before you get too worried it is easy to use (There is a class called
CRS that has helper methods for reprojection and stuff).