Use of Standards
================

GeoSpatial anything gets complicated quickly - one thing that almost helps is that GeoTools is based on "standards".


Relationship with OGC
^^^^^^^^^^^^^^^^^^^^^

Where possible we define our library in accordance with standards published by the Open Geospatial Consortium (OGC).

We do this so you can download lots of documentation and you will find the names line up between the GeoTools code base and the class diagrams in the standards). This is an example of the don't invent here policy - the documentation in this case is not invented here.

Relationship with ISO
^^^^^^^^^^^^^^^^^^^^^

Recently the OGC started collaborating with ISO TC211 Standards (which you have to pay in order to read). This is EVIL at least as far as open source is concerned - one nice person (Bryce!) has made up "primers" on the ISO specs.

* ISO19109+Primer.pdf
* ISO19123+Primer.pdf

These primers are not kind to the the faint of heart, but they are kind to the faint of budget.

.. _standards:

Standards Covered
^^^^^^^^^^^^^^^^^

GeoTools covers:

* GML 2
* GML 3
* SLD 1.0
* Filter 1.0
* Filter 1.1
* Common Query Language (from CSW 2.0 specification)
* WKT encoding of CRS
* WKT encoding of geometries
* WKB encoding of geometries
* WFS-T 1.0 client supporting reading and writing
* WFS 1.1 client supporting reading
* WMS client (handles WMS 1.0 through 1.3 although 1.3 is not well tested)
* ISO 19103, Geographic information - Conceptual schema language.
* ISO 19107, Feature Geometry (Topic 1).
* ISO 19111, Spatial Referencing by Coordinates (Topic 2).
* ISO 19115, Metadata (Topic 11).
* ISO 19123, Schema for coverage geometry and functions.
* ISO 19128, Layers and styles.
* OGC_01004, Grid Coverages implementation specification.
* OGC_01009, Coordinate Transformation Services implementation specification.
* OGC_02059, Filter encoding implementation specification.
* OGC_02070, Styled Layer Descriptor (SLD) implementation specification.
* OGC_03064, GO-1 Application Objects.
* OGC_04024, Web Map Service implementation specification.
* OGC_04094, Web Feature Service implementation specification.

The Java Topology Suite is an implementation of the SFSQL standard:

* OGC_99-049, Simple Features Implementation Specification for SQL

Of interest:

* ISO 19125, stripped down version of 19107 similar to what JTS offers

A word of caution - none of the above standards have an "OGC Compliance Suite" that can be passed in order to receive an official stamp. When implementing a service (such as WMS or WFS) you can ask to be tested and if successful receive a badge stating that you are standards compliant; all we can do is say we implement the standards.