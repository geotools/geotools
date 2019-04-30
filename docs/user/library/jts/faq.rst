JTS FAQ
-------

Q: What is the relationship between JTS and GeoTools?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We make use of the Java Topology Suite to represent "Simple" Geometry, and the OpenGIS interfaces to represent everything else. We have been forced to define an API module of our own in a few cases, like data access, where something "standard" is not available.

You will be using JTS a lot as it literally is the "shape" of GeoTools. It captures shapes using constructs like ``Coordinate``  ``Point``  ``Polygon`` and ``LineString`` 

* Do keep in mind that JTS is pure topology and the Geometry objects are pure shapes with no meaning. For the meaning, placing that shape on the earth, you will need to consult a ``CoordinateReferenceSystem`` .
* JTS topological operations work in a two dimensional Cartesian plane. With this in mind three dimensional shapes can be represented, but not calculated with.
* JTS focuses on *linear* topology, you will need to represent curves as a ``LineString``  with many little segments.

With those notes it may sound like JTS is limited; it is instead focused
on the task at hand - Geographic Information Systems. While 3D and
curves may be common in CAD systems we will need a lot of funding and
raw science to make it work for GIS. Some of that work is happening in
GeoTools with ISO Geometry.

Q: What Geometry Specifications?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

JTS is an implementation of the OGC Simple Features for SQL Specification (i.e. SFSQL). It covers 2D constructs like Point, Line and Polygon. JTS is willing to carry a 3rd point around but does not use it for calculations - making it a 2.5D solution for Cartesian space.

======================== ============ ===============================
Specification            Supports     Implementation
======================== ============ ===============================
Simple Features for SQL  2.5D, linear Java Topology Suite
ISO Geometry             3D, curves   OpenGIS ISO Geometry interfaces
======================== ============ ===============================

GeoTools makes a point of working with both implementations, but frankly the ISO Geometry implementations are not ready yet.
