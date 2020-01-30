CQL FAQ
-------

Q: What is CQL used for?
^^^^^^^^^^^^^^^^^^^^^^^^

The **CQL** utility class has static methods to parse an input String into either a **Filter**
or an **Expression**.

Here is the most common use for the CQL class - asking for Features:

.. literalinclude:: /../src/main/java/org/geotools/cql/CQLExamples.java
   :language: java
   :start-after: // cql comparison start
   :end-before: // cql comparison end
  

The CQL utility class produced is a Filter; you can create Filters by hand (using a ``FilterFactory``  but this is much easier.

Q: What version of CQL is implemented?
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The input string defining the query predicate that CQL accepts has to respond to the grammar
of the OGC Common Query Language, defined in the Catalog Service for Web, v2.0.1, from the OGC.

We've added a couple extensions and fixes to that grammar in order to fix a bug in the definition
of temporal expressions and to leverage its use in the GeoTools library.

Q: Can I just try CQL out?
^^^^^^^^^^^^^^^^^^^^^^^^^^^

If you're developing with GeoTools, you can easily try out the CQL parser by depending on the ``gt-cql`` 
jar and running the **CQL** class as a normal Java application.

It will present a prompt on the console (standard input) from where you can input CQL strings and
will get back the corresponding Filter in XML encoding.::
    
    Expression Tester ("quit" to finish)
    >attr > 10
    <?xml version="1.0" encoding="UTF-8"?>
    <ogc:PropertyIsGreaterThan xmlns="http://www.opengis.net/ogc"
                               xmlns:ogc="http://www.opengis.net/ogc"
                               xmlns:gml="http://www.opengis.net/gml">
      <ogc:PropertyName>attr</ogc:PropertyName>
      <ogc:Literal>10</ogc:Literal>
    </ogc:PropertyIsGreaterThan>
    
    >quit
    Bye!

Q: What does ECQL offer?
^^^^^^^^^^^^^^^^^^^^^^^^

The ECQL class offers an extension of the basic CQL language, it is backward compatible so
the following still works:

.. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
   :language: java
   :start-after: // comparisonCQLCompatibility start
   :end-before: // comparisonCQLCompatibility end

The ECQL has a syntax more flexible and nearest to the natural language. 
By example, ECQL allows to use an expression in the left hand of comparison 
as is showed in the following example

.. literalinclude:: /../src/main/java/org/geotools/cql/ECQLExamples.java
   :language: java
   :start-after: // ecql expressionLessThanOrEqualToProperty start
   :end-before: // ecql expressionLessThanOrEqualToProperty end
