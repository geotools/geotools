Module Directory Structure
==========================

Geotools 2.4 and above complies to the Maven 2 standard layout with an extension regarding module hierarchy.

This section provides a summary where module is the module name and category is one of library, plugin, extension, demo or unsupported.

Module Structure
^^^^^^^^^^^^^^^^^

Module is strucutured 
* modules/category/module/pom.xml
  
  Provides metadata about the module for maven

* modules/category/module/src/site
  
  Files for Maven to automatically generate the site

* modules/category/module/src/site/site.xml
  
  Layout file for the site, required to add a menu entry for the review information

* modules/category/module/src/site/apt/review.apt
  
  Almost Plain Text file for copyright and licensing info

* modules/category/module/src/main/java
  
  Module Java source files

* modules/category/module/src/main/resources
  
  Resources to be included in the JAR file

* modules/category/module/src/test/java
 
  JUnit tests source files

* modules/category/module/src/test/resources
  
  Resources required for tests only

* modules/category/module/target
  
  Automatically created for Maven output (details below)

Module Targets:

* modules/category/module/target/classes
  
  Generated class files, results of last 'maven compile'

* modules/category/module/target/site
  
  Files for the automatic website, from 'maven site:site'

* modules/category/module/target/site/apidocs
  
  Generated javadocs, from last 'maven javadoc:javadoc'

* modules/category/module/target/surefire-reports
  
  Logs from junit, from last 'maven test'

* modules/category/module/target/test-classes
  
  Generated junit classfiles, from last 'maven test'

* modules/category/module/target/module-2.4-SNAPSHOT.jar
  
  Generated jar file, from last 'maven install'

Module Categories
^^^^^^^^^^^^^^^^^^

* modules/library/ - this is the core library
  
  * opengis - interfaces for standard GIS concepts
  * api - stable interfaces
  * referencing - default implementations of opengis interfaces
  * coverage - enables raster support
  * main	 default implementations  
  * jdbc - enables database support
  * render - implementation of SLD based rendering system
  * cql - implementation of plain text filter
  * Xml - support for xml 

* modules/plugin/ - modules that dynamically integrate to the GeoTools library at runtime
  
  * jdbc-postgis - plugin allowing jdbc to work with postgis database
  * epsg-hsql - plugin allowing referencing to work with epsg codes
  * Shapefile - plugin providing shapefile support

* modules/extension/ - extensions and extras built using the library
  
  * brewer - generate styles from for a feature collection

* modules/unsupported/ - modules that are not ready yet, or are orphaned and no longer have a contact person
  
  * oracle - legacy oracle datastore
  * process - framework for spatial processes
  * wps - client for web processing service

* demo/ - small working examples, usually part of a tutorial
