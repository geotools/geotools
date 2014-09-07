:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Optimisation of CSVDataStore
----------------------------

In this part we will explore several optimisation techniques using our PropertyDataStore.

Low-Level Optimisation
^^^^^^^^^^^^^^^^^^^^^^

AbstractDataStore provides a lot of functionality based on the five methods we implemented in the
Tutorials. By examining its implementation we have an opportunity to discuss several issues with
DataStore development. Please keep these issues in mind when applying your own DataStore
optimisations.

In general the "Gang of Four" decorator pattern is used to layer functionality around the
raw **FeatureReader** and **FeatureWriters** you provided. This is very similar to the design
of the **java-io** library (where a BufferedInputStream can be wrapped around a raw
FileInputStream).

From AbstractDataStore getFeatureReader( featureType, filter, transaction ):

.. note::
   
   Historically Filter.ALL and Filter.NONE were used as placeholder,
   as crazy as it sounds, Filter.ALL filters out ALL (accepts none)
   Filter.NONE filters out NONE (accepts ALL)/
   
   These two have been renamed in GeoTools 2.3 for the following:
   
   * Filter.ALL has been replaced with Filter.EXCLUDE
   * Filter.NONE has been replaced with Filter.INCLUDE

Here is an example of how AbstractDataStore applies wrappers around your raw feature reader::

    public  FeatureReader<SimpleFeatureType, SimpleFeature> getFeatureReader(Query query,Transaction transaction) throws IOException {
        Filter filter = query.getFilter();
        String typeName = query.getTypeName();
        String propertyNames[] = query.getPropertyNames();
        
        ....
        
        if (filter == Filter.EXCLUDES) {
            return new EmptyFeatureReader(featureType);
        }
        String typeName = featureType.getTypeName();
        FeatureReader reader = getFeatureReader(typeName);
        if (filter != Filter.INCLUDES) {
            reader = new FilteringFeatureReader(reader, filter);
        }
        if (transaction != Transaction.AUTO_COMMIT) {
            Map diff = state(transaction).diff(typeName);
            reader = new DiffFeatureReader(reader, diff);
        }
        if (!featureType.equals(reader.getFeatureType())) {
            reader = new ReTypeFeatureReader(reader, featureType);
        }
        return reader;
    }

Support classes used:

* EmptyFeatureReader represents an empty result (when using Filter.ALL)
* FilteringFeatureReader skips over filtered elements using hasNext()
* TransactionStateDiff records a difference Map for the Transaction
* DiffFeatureReader is used as a wrapper, allowing the Features to be checked for removal, or
  modification before being provided to the user. Any additions performed against the
  Transaction are also returned.
* ReTypeFeatureReader allows on the fly Schema change

From AbstractDataStore getFeatureWriter( typeName, filter, transaction)::
    
    public FeatureWriter getFeatureWriter(String typeName, Filter filter,
            Transaction transaction) throws IOException {
        if (filter == Filter.ALL) {
            FeatureType featureType = getSchema(typeName);
            return new EmptyFeatureWriter(featureType);
        }
        FeatureWriter writer;
        
        if (transaction == Transaction.AUTO_COMMIT) {
            writer = getFeatureWriter(typeName);
        } else {
            writer = state(transaction).writer(typeName);
        }
        if (lockingManager != null) {
            writer = lockingManager.checkedWriter(writer, transaction);
        }
        if (filter != Filter.NONE) {
            writer = new FilteringFeatureWriter(writer, filter);
        }
        return writer;
    }

Support classes used:

* EmptyFeatureWriter represents an empty result
* TransactionStateDiff records a difference map for the Transaction, and provides a FeatureWrapper
  around a FeatureReader where modifications are stored in the difference Map
* FeatureLocking support is provided InProcessLockingManager in the form of a wrapper that will
  prevent modification taking place with out correct authorization
* FilteringFeatureWriter is used to skip over any Features not meeting the constraints

Every helper class we discussed above can be replaced if your external data source supports the
functionality.

External Transaction Support
''''''''''''''''''''''''''''

All JDBC DataStores support the concept of Transactions natively. JDBDataStore supplies an
example of using Transaction.State to store JDBC connection rather than the Difference map
used above.::
    
    public class JDBCTransactionState implements State {
        private Connection connection;
        public JDBCTransactionState( Connection c) throws IOException{
            connection = c;
        }
        public Connection getConnection(){
            return connection;
        }
        public void commit() throws IOException {
            connection.commit();
        }
        public void rollback() throws IOException {
            connection.rollback();            
        }
    }

For the purpose of PropertyDataStore we could create a Transaction.State class that records a
temporary File name used for a difference file. By externalising differences to a file rather
than Memory we will be able to handle larger data sets; and recover changes in the event of
an application crash.

Another realistic example is making use of Java Enterprise Edition session information allow
"per user" edits.

External Locking Support
''''''''''''''''''''''''

Several DataStores have an environment that can support native locking. By replacing the use
of the InProcessLockingManager we can make use of native Strong Transaction Support.

Single Use Feature Writers
''''''''''''''''''''''''''

We have a total of three distinct uses for FeatureWriters:

* AbstractDataStore.getFeatureWriter( typeName, transaction )
  
  General purpose FeatureWriter
* AbstractDataStore.getFeatureWriter( typeName, filter, transaction )
  
  An optimised version that does not create new content can be created.
* AbstractDataStore.getFeatureWriterAppend( typeName, transaction)
  
  An optimised version that duplicates the origional file, and opens it in append mode can be
  created. We can also perform special tricks such as returning a Feature delegate to the user,
  which records when it has been modified.

High-Level Optimisation
^^^^^^^^^^^^^^^^^^^^^^^

DataStore, FeatureSource and FeatureStore provide a few methods specifically set up
for optimisation.

DataStore Optimisation
''''''''''''''''''''''

AbstractDataStore leaves open a number of methods for high-level optmisations:

* PropertyDataStore.getCount( query )
* PropertyDatastore.getBounds( query )

These methods are designed to allow you to easily report the contents of information that
is often contained in your file header. Implementing them is optional, and each method
provides a way for you to indicate if the information is unavilable.

* PropertyDatastore.getFeatureSource( typeName );

By default the implementations returned are based on FeatureReader and FeatureWriter.
Override this method to return your own subclasses that are tuned for your data format.

FeatureStore Optimisation
'''''''''''''''''''''''''

DataStores operating against rich external data sources can often perform high level optimisations.
JDBCDataStores for instance can often construct SQL statements that completely fulfil a request
without making use of FeatureWriters at all.

When performing these queries please remember two things:

1. Check the lockingManager - If you are not providing your own native locking support, please
   check the user's authorisation against the the lockingManager
2. Event Notification - Remember to fire the appropriate notification events when contents change,
   Feature Caches will depend on this notification to accurately track the contents of your
   DataStore

Cacheing and FeatureListener
''''''''''''''''''''''''''''

A common optimisation is to trade memory use for speed by use of a cache. In this section we will
present a simple cache for getBounds() and getCount(Query.ALL).

The best place to locate your cache is in your DataStore implementation, you will need to keep
a separate cache for each Transaction by making use of Transaction.State. By implementing a cache
in the DataStore all operations can benefit.

Another popular technique is to locate the cache in an instance of FeatureSource. While the cache
will be duplicated when multiple FeatureStores are in use, it is convenient to locate the cache 
next to the high-level operations that can best benefit.

Finally FeatureResults represents a great opportunity to cache results, rather than reissue them
repeatedly.

FeatureListener (and associated FeatureEvents) provides notification of modification which can be
used to keep your cache implementation in sync with the DataStore.

PropertyDataStore
^^^^^^^^^^^^^^^^^

We can fill in the following methods for PropertyDataStore:

1. getCount( Query )
   
   We would like to improve this by recognizing the special case where the user has asked for
   the count of all of the features. In this case the number of Features is the same as
   the number of lines in the file (minus one for the header information and any comments).

   Things to look out for when reviewing the code:
   
   * File time stamp used to indicate when we cached the value, this is used to invalidate our
     cache if the file is changed at all.
   * A little bit of care is taken to avoid counting the header, any comment lines,
     and multiple lines
   
   We can offer a simple optimisation by counting the number of lines in our file,
   when the Query requests all features (using Filter.INLCUDE):

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // getCount start
      :end-before: // getCount end

2. getBounds( Query )
   
   Our file format does not offer an easy way to sort out the bounds (spatial file formats
   often include this information in the header). As such we won't be implementing getBounds()

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // getBounds start
      :end-before: // getBounds end

3. getFeatureSource( typeName )

   We will be returning the following classes (which we will create in the next section).

   * PropertyFeatureSource - if the file is read-only
   * PropertyFeatureStore - if the file is writable

   Here is what that looks like:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // getFeatureSource start
      :end-before: // getFeatureSource stop

   For a writable file we extend AbstractFeatureLocking which supports thread-safe Locking, and
   provides the correct hooks into the AbstractDataStore listenerManager.

PropertyFeatureSource
^^^^^^^^^^^^^^^^^^^^^

To implement a caching example we are going to produce our own implementation of FeatureSource:

1. Create the file PropertyFeatureSource:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureSource.java
      :language: java
      :start-after: */
      :end-before: // constructor end

2. We are extending AbstractFeatureSource here, as such we not need to implement
   FeatureSource.getCount( query ) as the default implementation will call up to
   PropertyDataStore.getCount( query ) implemented earlier.
   
3. We can however generate the bounds information and cache the result.
   
   Once again we are using a timestamp of the file to notice if the file is changed
   on disk.

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureSource.java
      :language: java
      :start-after: // getBounds start
      :end-before: // getBounds end

4. Earlier we modified PropertyDataStore to create an instance of this class if the file
   was read-only.
   
PropertyFeatureStore
^^^^^^^^^^^^^^^^^^^^

We are going to perform a similar set of optimisations to PropertyFeatureStore; with the added
wrinkle of listening to feature events so we can update our cached values in the event
modifications are made.

1. Create PropertyFeatureStore as follows:
   
   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureStore.java
      :language: java
      :start-after: */
      :end-before: // constructor end
   
   FeatureEvent provides a bounding box which we can use to selectively invalidate cacheBounds
   
2. Yes it is a little awkward not being able to smoothly extend PropertyFeatureSource (this
   is one of the fixes we have addressed for ContentDataStore covered in the next tutorial).

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureStore.java
      :language: java
      :start-after: // implementation start
      :end-before: // implementation end

3. This time we can implement getCount( query ) locally; being sure to check both
   the filter (includes all features) and the transaction (auto_commit):
   
   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureStore.java
      :language: java
      :start-after: // getCount start
      :end-before: // getCount end

4. In a similar fashion getBounds( query ) can be generated and cached:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureStore.java
      :language: java
      :start-after: // getBounds start
      :end-before: // getBounds end
      
5. We have already modified PropertyDataStore to return an instance of this class
   if the file was writable.