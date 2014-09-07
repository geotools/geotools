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

