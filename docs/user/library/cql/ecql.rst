ECQL
----

The ECQL language is intended as an extension of CQL, thus you can write all predicates supported by CQL and use the new expression possibilities defined in the new syntax rules.

References

* `ECQL Parser Design <http://old.geotools.org/ECQL-Parser-Design_110493908.html>`__ (design doc with BNF, note, in addition to WKT syntax for geometries 
  GeoTools now supports also Extended WKT, same as PostGIS, see example below)
* `GeoServer CQL Examples <http://docs.geoserver.org/latest/en/user/tutorials/cql/cql_tutorial.html>`_ (GeoServer)

.. note::

  Starting with GeoTools 19.0 Geometry objects carrying a CoordinateReferenceSystem among user data get encoded as EWKT.
  If you do not desire so, set the ``Hints.ENCODE_EWKT`` system hint to false (e..g, ``Hints.putSystemDefault(Hints.ENCODE_EWKT, false);``).

ECQL Utility Class
^^^^^^^^^^^^^^^^^^

The ECQL utility class is method compatible allowing you to use it as a drop-in replacement for CQL.

.. image:: /images/ecql.PNG

Running
'''''''

As you can see above the ECQL class can be run on the command line.

It allows you to try out the ECQL examples on this page; and produces the XML Filter representation of the result.::
    
    ECQL Filter Tester
    "Separate with \";\" or \"quit\" to finish)
    >attr > 10
    <?xml version="1.0" encoding="UTF-8"?>
    <ogc:PropertyIsGreaterThan xmlns="http://www.opengis.net/ogc" xmlns:ogc="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml">
    <ogc:PropertyName>attr</ogc:PropertyName>
    <ogc:Literal>10</ogc:Literal>
    </ogc:PropertyIsGreaterThan>
    
    >quit
    Bye!

Examples
''''''''

* Filter by Comparing Values
  
  The CQL language limited us to referencing a ``propertyName`` against
  a more general expression.

  ECQL allows you to use full expressions everywhere:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
    :language: java
    :start-after: // ecql expressionLessThanOrEqualToProperty start
    :end-before: // ecql expressionLessThanOrEqualToProperty end        

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
    :language: java
    :start-after: // comparisonUsingExpressions start
    :end-before: // comparisonUsingExpressions end        
        
  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
    :language: java
    :start-after: // ecql betweenPredicate start
    :end-before: // ecql betweenPredicate end        
        
  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
    :language: java
    :start-after: // betweenUsingExpression start
    :end-before: // betweenUsingExpression end        


* Filter by a List of Features' ID
  
  The Filter XML format allows the definition of an **Id** Filter
  capturing a set of FeatureIDs (often representing a selection).
  
  Using string as id::

        Filter filter = ECQL.toFilter("IN ('river.1', 'river.2')");
  
  Using integer as id::
  
        Filter filter = ECQL.toFilter("IN (300, 301)");
  
  We tried a couple of experiments, not all of them worked leaving
  us with the following deprecated syntax::
  
        Filter filter = ECQL.toFilter("ID IN ('river.1', 'river.2')");

* Filter based in a set of values

  The following filter selects the countries which have silver, oil or gold as principal mineral resource:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // ecql inPredicate start
     :end-before: // ecql inPredicate end

* Filter using a text pattern:

  Filter for a text pattern using **LIKE** keyword:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // ecql likePredicate start
     :end-before: // ecql likePredicate end

  Case insensitive example with **ILIKE** keyword:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // ecql ilikePredicate start
     :end-before: // ecql ilikePredicate end
     
  ECQL allows you to test any two expression, including literals:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // ecql likePredicateInString start
     :end-before: // ecql likePredicateInString end


* Filter by spatial relation:
  
  The ability to use a full expression also applies to spatial operations
  allowing us to process a geometry using a function as in the following
  example::
  
        Filter filter = ECQL.toFilter("DISJOINT(the_geom, POINT(1 2))");
        Filter filter = ECQL.toFilter("DISJOINT(buffer(the_geom, 10) , POINT(1 2))");
        Filter filter = ECQL.toFilter(
                "DWITHIN(buffer(the_geom,5), POINT(1 2), 10, kilometers)");

  The following example shows how to make a filter using the RELATE operation. In this case, the DE-9IM pattern corresponds to the **contains** spatial relation, It will be true if the first geometry contains the second.

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: ecql relatePattern start
     :end-before: ecql relatePattern end
     
  The following variant shows the same, but giving the geometry a coordinate reference system using the EWKT convention of 
  preceding it with "SRID=epsgCode;":

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: ecql referencedRelatePattern start
     :end-before: ecql referencedRelatePattern end



* Filter by temporal relation:

  The temporal predicates allow to establish the relation between two given instant of time, or between an instant and an interval of time. 
  In the next sample, the during predicate is used to filter the cities where an earthquake has occurred between the  specified dates:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // duringPredicateWithLefHandtAttribute start
     :end-before: // duringPredicateWithLefHandtAttribute end


  In ECQL you can write a datetime expression in the left hand of the temporal predicate:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // afterPredicateWithLefHandtExpression start
     :end-before: // afterPredicateWithLefHandtExpression end
  
  in the Before predicate: 

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // beforePredicateWithLefHandtExpression start
     :end-before: // beforePredicateWithLefHandtExpression end

  in the During predicate:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // duringPredicateWithLefHandtExpression start
     :end-before: // duringPredicateWithLefHandtExpression end

  The following example presents a time predicate that includes the UTC time zone (GMT +3) in date-time expression:

  .. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
     :language: java
     :start-after: // utcTimeZone start
     :end-before: // utcTimeZone end

* Filter Nulls::
  
        Filter filter = ECQL.toFilter(" Name IS NULL");
        Filter filter = ECQL.toFilter("centroid( the_geom ) IS NULL");

* Property Exist Predicate::
        
        Filter resultFilter = ECQL.toFilter("aProperty EXISTS");

* Expression
  
  Expressions support is unchanged::
        
        Expression expr = ECQL.toExpression("X + 1");

* Filter list
  
  Filter list is still supported using a ";" to separate entries::
        
        List<Filter> list = ECQL.toFilterList("X=1; Y<4");

* Filter with date literals::
    
         Filter filter = ECQL.toFilter("foo = 1981-06-20");
         Filter filter = ECQL.toFilter("foo <= 1981-06-20T12:30:01Z");
