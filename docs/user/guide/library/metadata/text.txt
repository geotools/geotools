Text
----

The **Text** utility class helps your code access GeoTools text
facilities.:

  .. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
     :language: java
     :start-after: // exampleText start
     :end-before: // exampleText end

The idea of an InternationalString comes from the gt-opengis module.

The above class is backed by a couple of implementations provided by gt-metadata:

* ResourceInternationalString
  
  Used to set up an InternationalString based on a resource
  bundle.
  
  .. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
     :language: java
     :start-after: // exampleResourceInternationalString start
     :end-before: // exampleResourceInternationalString end
  
  As noted above Java ResourceBundle is responsible for loading
  the correct property file for the requested String.

* SimpleInternationalString
  
  This is used when you need to quickly get back to work. You can
  replace the default english text with a real translator when a
  volunteer shows interest.
  
  .. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
     :language: java
     :start-after: // exampleSimpleInternationalStirng start
     :end-before: // exampleSimpleInternationalStirng end

* GrowableInternaionalString
  
  The easiest one to use as a programmer; allows you to set
  up multiple translations in code.
  
  .. literalinclude:: /../src/main/java/org/geotools/metadata/MetadataExamples.java
     :language: java
     :start-after: // exampleGrowableInternationalString start
     :end-before: // exampleGrowableInternationalString end
