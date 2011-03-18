Utilities
---------

The Utilities class is interesting, in that it contains helper
methods used when implementing basic Java functionality such
as equals or hashcode.

* It is fairly annoying to compare if two objects are equal, when either of the objects could be null.
* It is also annoying to sort out how to compare or print an array

Here is what Utilities offers:

.. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
   :language: java
   :start-after: // exampleUtilities start
   :end-before: // exampleUtilities end

Several of the other methods are useful when you are implementing your own Object.

Here is an example:

.. literalinclude:: /../src/main/java/org/geotools/metadata/Example.java
   :language: java
