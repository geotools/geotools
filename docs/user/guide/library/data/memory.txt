MemoryDataStore
---------------

We do have one MemoryDataStore suitable for storing temporary information in memory prior to saving it out to disk.
Please be advised that it is set up to accurately mirror information being located on disk and is not performant in
any way. That said it works; and is pretty easy to stuff your data into.

This implementation is actually offered by the **gt-main** module, it is being documented here for consistency.

References:

* `MemoryDataStore <http://docs.geotools.org/latest/javadocs/org/geotools/data/shapefile/ShapefileDataStoreFactory.html>`_ (javadocs)
* :doc:`gt-main DataUtilities<../main/data>`

Create
^^^^^^

MemoryDataStore is not fast - it is for testing. Why is it not fast do you ask? Because we use it to be strict and model what working with an external service is like (as such it is going to copy your data again and again and again).

Unlike most DataStores we will be creating this one by hand, rather than using a factory.::
  
  MemoryDataStore memory = new MemoryDataStore();
  
  // you are on the honour system to only add features of the same type
  memory.addFeature( feature );
  ...
  

* Q: Why is this so slow?
  
  It is slow for two reasons:
  
  * It is not indexed, every access involves looking at every feature and applying your filter to it
  * It duplicates each feature (so you don't accidentally modify something outside of a transaction)
  * It probably duplicates each feature in order to apply the feature for extra slowness.
  
* Q: Given me something faster
  
  The :doc:`gt-main DataUtilities<../main/data>` offers several high performance alternatives to
  MemoryDataStore.

Examples
^^^^^^^^

* Using a Memory DataStore to alter content.
  
  Thanks to Mau Macros for the following example:
   
   .. literalinclude:: /../src/main/java/org/geotools/data/DataExamples.java
      :language: java
      :start-after: // alter start
      :end-before: // alter end

