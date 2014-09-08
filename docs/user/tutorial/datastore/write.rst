:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Using CSVDataStore to Write Files
---------------------------------

In this part we will explore the full capabilities of our completed PropertyDataStore.

Now that we have completed our PropertyDataStore implementation, we can explore the remaining
capabilities of the DataStore API.

PropertyDataStore API for data modification:

* PropertyDataStore.createSchema( featureType )
* PropertyDataStore.getFeatureWriter( typeName, filter, Transaction )
* PropertyDataStore.getFeatureWriter( typeName, Transaction )
* PropertyDataStore.getFeatureWriterAppend( typeName, Transaction )
* PropertyDataStore.getFeatureSource( typeName )

FeatureSource
^^^^^^^^^^^^^

The DataStore.getFeatureSource( typeName ) method is the gateway to our high level api, as
provided by an instance of FeatureSource, FeatureStore or FeatureLocking.

Now that we have implemented writing operations, the result of this method supports:

* FeatureSource: the query operations outlined in DataStore Tutorial 2: Use
* FeatureStore: modification and transaction support
* FeatureLocking: Interaction with a Feature-based Locking

FeatureStore
''''''''''''

FeatureStore provides Transaction support and modification operations. FeatureStore is an
extension of FeatureSource. You may check the result of getFeatureSource( typeName ) with the instanceof operator.

Example of FeatureStore use:

.. literalinclude:: /../../modules/plugin/property/src/test/java/org/geotools/data/property/PropertyExamples.java
   :language: java
   :start-after: // featureStoreExample start
   :end-before: // featureStoreExample end

FeatureStore defines:
    
* FeatureStore.addFeatures( featureReader)
* FeatureStore.removeFeatures( filter )
* FeatureStore.modifyFeatures( type, value, filter )
* FeatureStore.modifyFeatures( types, values, filter )
* FeatureStore.setFeatures( featureReader )
* FeatureStore.setTransaction( transaction )

Once again, many DataStores are able to provide optimised implementations of these operations.

Transaction Example:

.. literalinclude:: /../../modules/plugin/property/src/test/java/org/geotools/data/property/PropertyExamples.java
   :language: java
   :start-after: // transactionExample start
   :end-before: // transactionExample end

This produces the following output:

  .. literalinclude:: artifacts/write-output.txt
     :language: text
     :start-after: transactionExample start
     :end-before: transactionExample end

.. note::
   
   Please review the above code example carefully as it is the best explanation
   of transaction independence you will find.
   
   Specifically:
   
   * "auto-commit" represents the current contents of the file on disk
   * Notice how the transactions only reflect the changes the user made relative to
     the current file contents.
     
     This is shown after t1 commit, where transaction t2 is seeing 4 features (ie the
     current file contents plus the one feature that has been added on t2).
   * This really shows that FeatureSource and FeatureStore are "views" into your data.
     
     * If you configure two FeatureStores with the same transaction they will act the same.
     * Transaction is important and represents what you are working on
       FeatureStore is not as important and is just used to make working with your data
       easier (or more efficient) than direct use of FeatureWriter.
  
FeatureLocking
''''''''''''''

FeatureLocking is the last extension to our high-level API. It provides support for preventing
modifications to features for the duration of a Transaction, or a period of time.

FeatureLocking defines:

* FeatureLocking.setFeatureLock( featureLock )
* FeatureLocking.lockFeatures( query ) - lock features specified by query
* FeatureLocking.lockFeatures( filter ) - lock according to constraints
* FeatureLocking.lockFeatures() - lock all
* FeatureLocking.unLockFeatures( query )
* FeatureLocking.unLockFeatures( filter )
* FeatureLocking.unLockFeatures()
* FeatureLocking.releaseLock( string )
* FeatureLocking.refreshLock( string )

The concept of a FeatureLock matches the description provided in the OGC Web Feature Server
Specification. Locked Features can only be used via Transactions that have been provided with
the correct authorization.

FeatureWriter
^^^^^^^^^^^^^

We have a number of FeatureWriters available for different uses; these implementations
are used by the default implementation of AbstractFeatureStore and AbstractFeatureLocking.

These classes serve as a good example of how to use FeatureWriter.

FeatureWriter Filter
''''''''''''''''''''

The DataStore.getFeatureWriter( typeName, filter, transaction ) method 
creates a FeatureWriter used to modify features indicated by a constraint.

Example - removing all features:

.. literalinclude:: /../../modules/plugin/property/src/test/java/org/geotools/data/property/PropertyExamples.java
   :language: java
   :start-after: // removeAllExample start
   :end-before: // removeAllExample end

This FeatureWriter does not allow the addition of new content. It can be used for modification and removal only.

DataStores can often provide an optimized implementation.

FeatureWriter
'''''''''''''

The DataStore.getFeatureWriter( typeName, transaction )  method creates a general purpose
FeatureWriter. New content may be added after iterating through the
provided content.

Example - completely replace all features:

.. literalinclude:: /../../modules/plugin/property/src/test/java/org/geotools/data/property/PropertyExamples.java
   :language: java
   :start-after: // replaceAll start
   :end-before: // replaceAll end

FeatureWriter Append
''''''''''''''''''''

The DataStore.getFeatureWriterAppend( typeName, transaction ) method creates a FeatureWriter
for adding content.

Example - making a copy:

.. literalinclude:: /../../modules/plugin/property/src/test/java/org/geotools/data/property/PropertyExamples.java
   :language: java
   :start-after: // copyContent start
   :end-before: // copyContent end

DataStores can often provide an optimised implementation of this method.
