Using FeatureSource
-------------------

In this part we examine the abilities of the CSVDataStore.

DataStore
^^^^^^^^^

Now that we have implemented a simple DataStore we can explore some of the capabilities made available to us.

CSVDataStore API for data access:

* DataStore.getTypeNames()
* DataStore.getSchema( typeName )
* DataStore.getFeatureReader( featureType, filter, transaction )
* DataStore.getFeatureSource( typeName )

If you would like to follow along with these examples you can download 
:download:`CSVTest.java </../src/main/java/org/geotools/tutorial/csv/CSVTest.java>`.

* DataStore.getTypeNames()
  
  The method getTypeNames provides a list of the available types.
  
  getTypeNames() example:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example1 start
     :end-before: // example1 end

  Produces the following output (given a directory with example.properties):

  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example1 start
     :end-before: example1 end

* DataStore.getSchema( typeName )
  
  The method getSchema( typeName ) provides access to a FeatureType referenced by a type name.

  getSchema( typeName ) example:

  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example2 start
     :end-before: // example2 end

  Produces the following output:

  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example2 start
     :end-before: example2 end

* DataStore.getFeatureReader( query, transaction )
  
  The method getFeatureReader( query, transaction ) allows access to the contents
  of our DataStore.
  
  The method signature may be more complicated than expected, we certainly did not talk
  about Query or Transactions when we implemented our CSVDataStore. This is something
  that AbstractDataStore is handling for you and will be discussed later in the section
  on optimisation.

  * Query.getTypeName()
  
    Determines which FeatureType is being requested. In addition, Query supports the
    customization attributes, namespace, and typeName requested from the DataStore.
    While you may use DataStore.getSchema( typeName ) to retrieve the types as specified by
    the DataStore, you may also create your own FeatureType to limit the attributes returned
    or cast the result into a specific namespace.
  
  * Query.getFilter()
    
    Used to define constraints on which features should be fetched. The constraints
    can be on spatial and non-spatial attributes of the features.

  * Query.getPropertiesNames()
  
    Allows you to limit the number of properties of the returned Features to only those
    you are interested in.

  * Query.getMaxFeatures()
    
    Defines an upper limit for the number of features returned.
  
  * Query.getHandle()
    
    User-supplied name used to describe a query in user's terms in any generated error messages.
  
  * Query.getCoordinateSystem()
    
    Used to force the use of a user-supplied CoordinateSystem (rather than the one supplied
    by the DataStore). This capability will allow client code to use our DataStore with a
    CoordinateSystem of their choice. The coordinates returned by the DataStore will not be
    modified in any way.
  
  * Query.getCoordinateSystemReproject()
    
    Used to reproject the Geometries provided by a DataStore from their original value (or
    the one provided by Query.getCoordinateSystem) into a different coordinate system.
    The coordinate returned by the DataStore will be processed , either natively by
    Advanced DataStores, or using GeoTools reprojection routines.

  .. I suspect the following is no longer relevant (based on above)
  .. .. note::
     
  ..    Since this tutorial was written Query has expanding its capabilities
     (and the capabilities of your DataStore) to include support for reprojection.
     
     It also offers an "open ended" pathway for expansion using "query hints".
     
  * Transaction
    
    Allows access the contents of a DataStore during modification.

  With all of that in mind we can now proceed to our
  DataStore.getFeatureReader( featureType, filter, transaction ) example:
    
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example3 start
     :end-before: // example3 end

  Produces the following output:
  
  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example3 start
     :end-before: example3 end
	
  Example with a quick "selection" Filter:
    
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example4 start
     :end-before: // example4 end

  Produces the following output:
  
  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example4 start
     :end-before: example4 end

* DataStore.getFeatureSource( typeName )
  
  This method is the gateway to the higher level interface as provided by an instance of FeatureSource,
  FeatureStore or FeatureLocking. The returned instance represents the contents of a single
  named FeatureType provided by the DataStore. The type of the returned instance indicates
  the capabilities available.
  
  This far in our tutorial CSVDataStore will only support an instance of FeatureSource.

  Example getFeatureSource:
    
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example5 start
     :end-before: // example5 end
  
  Producing the following output:

  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example5 start
     :end-before: example5 end


FeatureSource
^^^^^^^^^^^^^

FeatureSource provides the ability to query a DataStore and represents the contents of a single
FeatureType. In our example, the PropertiesDataStore represents a directory full of properties
files. FeatureSource will represent a single one of those files.

FeatureSource defines:

* FeatureSource.getFeatures( query ) - request features specified by query
* FeatureSource.getFeatures( filter ) - request features based on constraints
* FeatureSource.getFeatures() - request all features
* FeatureSource.getSchema() - acquire FeatureType
* FeatureSource.getBounds() - return the bounding box of all features
* FeatureSource.getBounds( query ) - request bounding box of specified features
* FeatureSource.getCount( query ) - request number of features specified by query

FeatureSource also defines an event notification system and provides access to the DataStore
which created it. You may have more than one FeatureSource operating against a file at any time.

FeatureCollection
^^^^^^^^^^^^^^^^^

.. sidebar:: FeatureResults
   
   FeatureResults is the original name of FeatureCollection.
   Some of these methods have been replaced such as the use of
   DataUtilities.collection( featureCollection ) to load
   the contents into memory.
   
   It is interesting to note the design goal of capturing a
   prepared statement (rather than loading the features into memory).
   
   The class was renamed FeatureCollection to help those migrating
   from GeoTools 1.0.
   
While the FeatureSource API does allow you to represent a named FeatureType, it still does not
allow direct access to a FeatureReader. The getFeatures methods actually return an instance of
FeatureCollection.

FeatureCollection defines:

* FeatureCollection.getSchmea()
* FeatureCollection.features() - access to a FeatureIterator
* FeatureCollection.accepts( visitor, progress )
* FeatureCollection.getBounds() - bounding box of features
* FeatureCollection.getCount() - number of features
* DataUtilities.collection( featureCollection ) - used to load features into memory

FeatureCollection is the closest thing we have to a prepared request. Many DataStores are able to
provide optimised implementations that handles the above methods natively.

* FeatureCollection Example:
  
  .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVTest.java
     :language: java
     :start-after: // example6 start
     :end-before: // example6 end
  
  With the following output:

  .. literalinclude:: artifacts/read-output.txt
     :language: text
     :start-after: example6 start
     :end-before: example6 end

.. note::
   
   Warning: When calling ``FeatureSource.count(Query.ALL)`` be aware a DataStore implementation may return ``-1`` indicating that the value is too expensive for the DataStore to calculate. 
   
   You can think of this as:
   
   * FeatureSource is a way to perform a quick check for a precanned answer for count and bounds.
     The Shapefile format will keep this information in the header at the top of the
     file. In a similar fashion a Database may be able to quickly check an index for this information.
   * FeatureCollection checks the contents, and possibly checks each item, for an answer to
     size and bounds.
     
   This is a terrible API tradeoff to have to make, resulting from implementations taking ten minutes to performing a "full table scan".
     
Care should be taken when using the collection() method to capture the contents of a DataStore in
memory. GIS applications often produce large volumes of information and can place a strain
on memory use.
