OpenGIS FAQ
-----------

Q: What is "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^

The `GeoAPI Implementation Specification <https://www.ogc.org/standard/geoapi/>`__ was a Java standard (Interfaces and Classes) provided by the Open Geospatial Consortium for interoperability between Java projects and libraries.

This was similar to the SFSQL specification implemented by PostGIS, SQLServer and others for database interoperability when working with GIS information.

Q: Does GeoTools implement "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

No we do not.

GeoTools worked on a pre-release of these interfaces in the hopes of collaborating with other projects. In GeoTools 2.7 these interfaces were `folded back <https://osgeo-org.atlassian.net/browse/GEOT-3364>`__ into the GeoTools ``gt-opengis`` module due to lack of collaboration opportunities and funding.

In GeoTools 30.0 we responded to a request from the Open GIS Consortium to drop or use of the ``org.opengis`` package used by the pre-release of GeoAPI. We have refactored all such use to ``org.geotools.api`` package. 

There is now no remaining references to ``org.opengis`` packages in the GeoTools code base.

Please see :ref:`update instructions <update30>` for instructions on how to udpate, including a script to refactor your code base.
