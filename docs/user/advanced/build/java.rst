Java
=====

GeoTools is written in the Java Programming Language. The library is targeted for Java 6.

Java Runtime Environment:

* Java 6 - Geotools 8.0.x and above
* Java 5 - GeoTools 2.5.x and above
* Java 1.4 - GeoTools versions 2.4.x and below
* GeoTools is known to work with JRE provided by IBM, Apple and OpenJDK.

Java Extension:

* Java Advanced Imaging is used to process rasters. If you have installed the native support into your JRE you can take advantage of hardware acceleration.
* Java Image IO - used to support additional raster formats
* ImageIO-Ext - used to support additional geospatial raster formats

Current Language Policy
-----------------------

Our policy is waiting for the majority of our users before migrating to a new version of the Java language. In general we are held up by the slow migration of Java Enterprise Edition environments such as websphere.

IDE Settings
^^^^^^^^^^^^

When developing GeoTools please change your compile options to:

* Produce 6.0 compliant code 
