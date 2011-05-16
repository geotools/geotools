GeodeticCalculator
------------------

The GeodeticCalculator is used to perform calculations on the Geoid (ie on the surface of
the world).

.. image:: /images/geodetic_calculator.PNG

You can configure your GeodeticCalculator to work with a specific Ellipsoid (or commonly from 
an CoordinateReferenceSystem) and then use it to perform a number

References:

* http://udig.refractions.net/files/tutorials/ToolPluginTutorial.pdf

Distance
^^^^^^^^

* Distance between two points
   
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // distance start
     :end-before: // distance end
   
  Although the above shows quickly creating a DirectPosition from a JTS Coordiante; you
  can use the GeodedicCalculator with any two positions, internally it will transform
  the points as needed.

* You can also generate the angle between the two points

  Continuing on from the previous example:
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // angle start
     :end-before: // angle end
   
* Generate location away from a point
  
  Finally you can turn the tables and use the GeodeticCalculator to generate a point
  a set direction and distance away from your starting point.
  
  .. literalinclude:: /../src/main/java/org/geotools/referencing/ReferencingExamples.java
     :language: java
     :start-after: // movePoint start
     :end-before: // movePoint end

