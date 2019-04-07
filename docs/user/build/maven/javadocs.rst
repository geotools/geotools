Generating Javadocs
-------------------

Javadoc can be generated using the standard ``mvn javadoc:javadoc`` command.

* It can be generated for an individual module::
     
     cd modules/main/cql
     mvn javadoc:javadoc

* Aggregated javadocs for the entire GeoTools project can be generated using::
     
     cd modules
     mvn javadoc:aggregate

Dependencies in aggregated javadoc
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

As of October 2006, aggregated javadoc using ``maven-javadoc-plugin`` version
2.0 fails to resolve all external dependencies like JTS. It may be related to
MJAVADOC-66, but the later said that the bug is already fixed.

Waiting for the next maven-javadoc-plugin release in the hope that it will be fixed there.


Modifying the javadoc configuration
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The **trunk/pom.xml** file contains the javadoc:javadoc goal configuration. This configuration includes hyperlink to external libraries like JTS, list of package to exclude, etc.

Excluded packages are:

  * ``com.\*`` 
  * ``org.geotools.maven.\*`` 
  * ``org.geotools.referencing.util.\*`` 
  * ``org.geotools.maven.\*`` * ``org.geotools.referencing.util.\*``

When Maven fails to generate the javadoc
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Building Javadoc produces many "warnings" and "errors". Only errors cause the build to stop.
GeoTools javadocs produce a lot of warnings making it difficult to spot the error that caused javadoc to fail.

1. Thankfully the following Unix command is very helpful::
      
      mvn javadoc:javadoc | grep "error"
   
2. The above gives a much smaller output, typically only about 5 lines. So we can spot immediately
   the cause of javadoc failure, for example something like::
      
      modules/library/referencing/src/main/java/org/geotools/referencing/factory/epsg/package.html:
      error - Close body tag missing from HTML file
   
3. In the above example  adding the missing </BODY> tag as suggested by the error message would the
   javadoc generation.
