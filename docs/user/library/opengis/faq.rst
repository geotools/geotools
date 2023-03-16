OpenGIS FAQ
-----------

Q: What is "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^

The `GeoAPI Implementation Specification <https://www.ogc.org/standard/geoapi/>`__ is a Java standard (Interfaces and Classes) provided by the Open Geospatial Consortium for interoperability between Java projects and libraries.

This is similar to the SFSQL specification implemented by PostGIS, SQLServer and others for database interoperability when working with GIS information.

Q: Does GeoTools implement "GeoAPI"?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

No we do not.

GeoTools worked on a pre-release of these interfaces in the hopes of collaborating with other projects. In GeoTools 2.7 these interfaces were `folded back <https://osgeo-org.atlassian.net/browse/GEOT-3364>`__ into the GeoTools OpenGIS module due to lack of collaboration opportunities and funding.

Q: Why am I seeing Java exceptions related to the org.opengis namespace?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

A package conflict occurs when both GeoTools and GeoAPI make use of the ``org.opengis`` namespace in the same classloader.

You may have both GeoAPI and GeoTools in the same application, and the classloader is finding the GeoAPI implementation first. Check your classpath, including any shaded jars that might incorporate GeoAPI classes.

At this time the best approach is to use separate class loaders due to package name conflict.
