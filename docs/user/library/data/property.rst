Property Plugin
---------------

DataStore supporting the use of simple java property files for storing spatial data. This was originally based on a tutorial in the use of AbstractDataStore, but has now been migrated to use ContentDataStore. The old implementation has been moved to the unsupported module.

**Reference**

* :doc:`/tutorial/datastore/index`

**Maven**::
   
   <dependency>
      <groupId>org.geotools</groupId>
      <artifactId>gt-property</artifactId>
      <version>${geotools.version}</version>
    </dependency>

Example
^^^^^^^

The property datastore works with a custom format based on Java property files::
  
  _=id:Integer,geom:Geometry,name:String
  rd1=1|wkt|road one
  rd2=2|wkt|road two
  
These examples use the file :download:`example.properties </tutorial/artifacts/example.properties>`.
  
.. literalinclude:: /tutorial/artifacts/example.properties

Here is an example to parse the above file:

.. literalinclude:: /../src/main/java/org/geotools/property/PropertyExamples.java
   :language: java
   :start-after: // example3 start
   :end-before: // example3 end
