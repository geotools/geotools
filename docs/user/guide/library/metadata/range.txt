Range
-----

GeoTools inlcudes a **Range** class to represent a numeric, or value, range. This is handy when expressing scales a dataset is useful for.

.. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
   :language: java
   :start-after: // exampleRange start
   :end-before: // exampleRange end

This actually works with any Comparable such as Strings or enumerations.

.. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
   :language: java
   :start-after: // exampleRangeComparable start
   :end-before: // exampleRangeComparable end

We have a special **NumericRange** that uses the fact that numbers can work together to allow comparisons between NumericRange<Double> and NumericRange<Integer>.

.. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
   :language: java
   :start-after: // exampleNumberRange start
   :end-before: // exampleNumberRange end

