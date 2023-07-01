OpenGIS FAQ
-----------

Q: What is "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^

The `GeoAPI Implementation Specification <https://www.ogc.org/standard/geoapi/>`__ was a Java standard (Interfaces and Classes) provided by the Open Geospatial Consortium for interoperability between Java projects and libraries.

This was similar to the SFSQL specification implemented by PostGIS, SQLServer and others for database interoperability when working with GIS information.

Q: Does GeoTools implement "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

No we do not.

GeoTools worked on a pre-release of these interfaces in the hopes of collaborating with other projects. In GeoTools 2.7 these interfaces were `folded back <https://osgeo-org.atlassian.net/browse/GEOT-3364>`__ into the GeoTools OpenGIS module due to lack of collaboration opportunities and funding.

From the GeoTools 30.0 release these classes and there should be no references to the ``org.opengis`` packages in the GeoTools code base. You should also not need to reference them from your code. 

.. ToDo Add link to script to clean up customer code here


