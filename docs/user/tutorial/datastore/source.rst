:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Creating CSVDataStore
---------------------

Now with the setup out of the way we can get to work.

CVSDataStore
^^^^^^^^^^^^

The first step is to create a basic DataStore that only supports feature extraction. We will read
data from a csv file into the GeoTools feature model.

.. figure:: images/CSVDataStore.png

   CSVDataStore

To implement a DataStore we will subclass ContentDataStore. This is a helpful base class for
making new kinds of content available to GeoTools. The GeoTools library works with an interaction
model very similar to a database - with transactions and locks. ContentDataStore is going to handle
all of this for us - as long as we can teach it how to access our content.

ContentDataStore requires us to implement the following two methods:

* createTypeNames()
* createFeatureSource(ContentEntry entry)

The class *ContentEntry* is a bit of a scratch pad used to keep track of things for each type.

Our initial implementation will result in a read-only datastore for accessing CSV content:

#. To begin create the file CSVDataStore extending ContentDataStore

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStore.java
      :language: java
      :start-after: // header start
      :end-before: // header end

#. Add the reader

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStore.java
      :language: java
      :start-after: // reader start
      :end-before: // reader end

#. We are going to be working with a single CSV file

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStore.java
      :language: java
      :start-after: // constructor start
      :end-before: // constructor end


#. Listing TypeNames

   A DataStore may provide access to several different data products. The method **createTypeNames** provides a list of the information being published. This is called once; the result cached; and then the same list is returned by ContentDataStore.getTypeNames() each time.

   This answer is cached as a DataStore may have to do some heavy lifting to obtain this answer. WFS clients need to connect to a web service, download and parse a large XML document to obtain this list. JDBCDataStore needs to review the metadata published by the Database and determine which tables have spatial content.

   By caching this answer we prevent developers having to this work many times.

   After all that lead-in you will be disappointed to note that our list will be a single value - the name of the csv file.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStore.java
      :language: java
      :start-after: // createTypeNames start
      :end-before: // createTypeNames end

#. Next we have the **createFeatureSource** method.

   This is used to create a **FeatureSource** which is used by client code to access content. There is no cache for **FeatureSource** instances as they are managed directly by client code. Don't worry that is not as terrible as it sounds, we do track all the information and resources made available to a **FeatureSource** in a **ContentEntry** data structure. You can think of the **FeatureSource** instances sent out into the wild as light weight wrappers around **ContentEntry**.

   It is worth talking a little bit about **ContentEntry** which is passed into this method as a parameter. **ContentEntry** is used as a scratchpad holding all recorded information about the content we are working with.

   .. figure:: images/ContentEntry.png

      ContentEntry

   **ContentEntry** also contains a back pointer to the **ContentDataStore** in case your implementation of **FeatureSource** needs to phone home.

#. Implement createFeatureSource. Technically the **ContentEntry** is provided as "parameter object" holding the type name requested by the user, and any other context known to the DataStore.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStore.java
      :language: java
      :start-after: // createFeatureSource start
      :end-before: // createFeatureSource end

CSVFeatureSource
^^^^^^^^^^^^^^^^

*Intro and walk through of CSVFeatureSource and ContentState*

This will be long as it involves most of the work.



.. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVFeatureSource.java
   :language: java


CSVFeatureReader
^^^^^^^^^^^^^^^^

Go over actually parsing content, using **ContentState** to store any state.


.. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVFeatureReader.java
   :language: java

CSVDataStoreFactory
^^^^^^^^^^^^^^^^^^^

Now that we have implemented accessing and reading content what could possibly be left?

This is GeoTools so we need to wire in our new creation to the Factory Service Provider (SPI) plug-in system so that application can smoothly integrate our new creation.


To make your DataStore truly independent and plugable, you must create a class implementing the
**DataStoreFactorySpi** interface.

This allows the Service Provider Interface mechanism to dynamically plug in your new DataStore with
no implementation knowledge. Code that uses the DataStoreFinder can just add the new DataStore to
the classpath and it will work!

The DataStoreFactorySpi provides information on the Parameters required for construction.
DataStoreFactoryFinder provides the ability to create DataStores representing existing
information and the ability to create new physical storage.

1. PropertyDataStoreFactory

   * The "no argument" consturctor is required as it will be used by the
     Factory Service Provider (SPI) plug-in system.
   * getImplementationHints() is used to report on any "Hints" used for configuration
     by our factory. As an example our Factory could allow people to specify a specific
     FeatureFactory to use when creating a feature for each line.

   Create PropertyDataStoreFactory as follows:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :end-before: // definition end

2. We have a couple of methods to describe the DataStore.

   This *isAvaialble* method is interesting in that it can become a performance bottleneck if not implemented efficiently. DataStoreFactorySPI factories *all* called when a user attempts to connect, only the factories marked as *available* are shortlisted for further interaction.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // metadata start
      :end-before: // metadata end

3. The user is expected to supply a Map of connection parameters when creating a datastore.

   The allowable connection parameters are described using *Param[]*. Each *Param* describes a *key* used to store the value in the map, and the expected java type for the value. Additional fields indicate if the value is required and if a default value is available.

   This array of parameters form an API contract used to drive the creation of user interfaces.

   The API contract is open ended (we cannot hope to guess all the options needed in the future). The helper class **KVP** provides an easy to use implementation of **Map<String,Object>**. The keys used here are formally defined as static constants - complete with javadoc describing their use. If several authors agree on a new hint it will be added to these static constants.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // getParametersInfo start
      :end-before: // getParametersInfo end

4. Next we have some code to check if a set of provided connection parameters can actually be used.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // canProcess start
      :end-before: // canProcess end

   .. note::

      The directoryLookup has gotten considerably more complicated since this tutorial
      was first written. One of the benifits of CSVDataStore being used
      in real world situtations.

5. Armed with a map of connection parameters we can now create a Datastore for an **existing** csv file.

   Here is the code that finally calls our CSVDataStore constructor:

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // createDataStore start
      :end-before: // createDataStore end

6. How about creating a DataStore for a **new** csv file?

   Since initially our DataStore is read-only we will just throw an UnsupportedOperationException at this time.

   .. literalinclude:: /../src/main/java/org/geotools/tutorial/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // createNewDataStore start
      :end-before: // createNewDataStore end

6. The Factory Service Provider (SPI) system operates by looking at the META-INF/services
   folder and checking for implemetnations of DataStoreFactorySpi

   To "register" our CSVDataStoreFactory please create the following in `src/main/resources/`:

   *  META-INF/services/org.geotools.data.DataStoreFactorySpi

   This file requires the filename of the factory that implements the DataStoreSpi interface.

   Fill in the following content for your **org.geotools.data.DataStoreFactorySpi** file::

       org.geotools.tutorial.csv.CSVDataStoreFactory

That is it, in the next section we will try out your new DataStore.
