Use
===

GeoTools provides an excellent series of tutorials to help you get started:

* `Quickstart </tutorial/quickstart>`

Adding GeoTools to your Application
-----------------------------------

Maven (recommended):
   The quickstart is writen with the maven build system in mind. The maven build
   system is very good at sorting out dependencies between lots of little jars - and
   is the recommended way to start using GeoTools.
   
   Both Eclipse and NetBeans offer maven integration, for details please review
   the Eclipse Quickstart and NetBeans quickstart.
   
   Using maven in concert with your IDE, and looking over the pictures on this page is
   recommended.

Download:
   Traditionally users just dump all the jars into their IDE and have a go, please be
   advised that some of the jars will be in conflict.
   
   1. Dump everything from a binary distribution of GeoTools into your IDE
   2. Remove all the jars that say epsg in them - except for the gt2-epsg jar.
   3. Ensure your JRE has JAI and ImageIO if you are doing raster work
   4. Ignore the rest of this page   
   
   For detailed step-by-step instructions review the Eclipse quickstart and
   Netbeans quickstart. Instructions for downloading and selecting jars
   are provided at the end of the document as an alternative.


Module Matrix
-------------

The GeoTools library is live and online! So you can check up on modules, plugins and
extensions you are about to use:

* http://docs.codehaus.org/display/GEOTOOLS/Module+Matrix

This page includes a description of how good each module is (more stars is better). If there
are any serious problems (ie a red star) you may want to click on the module name to find out
more information.

The Module Matrix also lists unsupported modules allowing you to check on current status.
