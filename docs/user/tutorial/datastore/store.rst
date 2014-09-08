:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Making CSVDataStore Writable
----------------------------

In this part we will complete the PropertyDataStore started above. At the end of this section we
will have a full functional PropertyDataStore supporting both read and write operations.

The DataStore API has two methods that are involved in making content writable.

* DataStore.getFeatureWriter( typeName )
* DataStore.createSchema( featureType )

AbstractDataStore asks us to implement two things in our subclass:

* PropertyDataStore() constructor must call super( true ) to indicate we are
  working with writable content
* PropertyDataStore.getFeatureWriter( typeName )

FeatureWriter defines the following methods:

* FeatureWriter.getFeatureType
* FeatureWriter.hasNext
* FeatureWriter.next
* FeatureWriter.write
* FeatureWriter.remove
* FeatureWriter.close

Change notification for users is made available through several FeatureSource methods:

* FeatureSource.addFeatureListener( featureListener )
* FeatureSource.removeFeatureListener( featureListener )

To trigger the featureListeners we will make use of several helper methods in AbstractDataSource:

* AbstractDataStore.fireAdded( feature )
* AbstractDataStore.fireRemoved( feature )
* AbstractDataStore.fireChanged( before, after )

PropertyDataStoreFactory
^^^^^^^^^^^^^^^^^^^^^^^^

Now that we are going to be writing files we can fill in the createNewDataStore method.

1. Open up PropertyDataStoreFactory and replace createNewDataStore with the following:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStoreFactory.java
      :language: java
      :start-after: // createNewDataStore start
      :end-before: // createNewDataStore end
   
   No surprises here; the code creates a directory for PropertyDataStore to work in.
  
PropertyDataStore
^^^^^^^^^^^^^^^^^

1. To start with, we need to make a change to our PropertyDataStore constructor:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // constructor start
      :end-before: // constructor end

   This change will tell AbstractDataStore that our subclass is willing to modify Features.

2. Implement createSchema( featureType)
   
   This method provides the ability to create a new FeatureType. For our DataStore we
   will use this to create new properties files.
   
   To implement this method we will once again make use of the DataUtilities class.
   
   Add createSchema( featureType ):
   
   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // createSchema start
      :end-before: // createSchema end

3. Implement getFeatureWriter( typeName )
   
   FeatureWriter is the low-level API storing Feature content. This method is not part of the
   public DataStore API and is only used by AbstractDataStore.
   
   Add getFeatureWriter( typeName ):
    
   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyDataStore.java
      :language: java
      :start-after: // getFeatureWriter start
      :end-before: // getFeatureWriter end
  
  FeatureWriter is less intuitive than FeatureReader in that it does not follow the example of
  Iterator as closely.

PropertyFeatureWriter
^^^^^^^^^^^^^^^^^^^^^

Our implementation of a FeatureWriter needs to do two things: support the FeatureWriter interface
and inform the DataStore of modifications.

1. Create the file PropertyFeatureWriter.java:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: */
      :end-before: // constructor end

   Our constructor creates a PropertyAttributeReader to access the existing contents of
   the DataStore. We made use of PropertyAttributeReader to implement
   PropertyFeatureReader in Section 1.

  We also create a PropertyAttributeWriter operating against a temporary file. When the
  FeatureWriter is closed we will delete the original file and replace it with our new file.

2. Add FeatureWriter.getFeatureType() implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // getFeatureType start
      :end-before: // getFeatureType end

3. Add hasNext() implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // next start
      :end-before: // next end
    
   Our FeatureWriter makes use of two Features:
   
   * original: the feature provided by PropertyAttributeReader
   * live: a duplicate of original provided to the user for modification
   
   When the FeatureWriter is used to write or remove information, the contents of both live
   and feature are set to null. If this has not been done already we will write out the
   current feature.

4. Add the helper function writeImplementation( Feature ):

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // writeImplementation start
      :end-before: // writeImplementation end

5. Add next() implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // next start
      :end-before: // next end

   The next method is used for two purposes:
   
   * To access Features for modification or removal (when working through existing content)
   * To create new Features (when working past the end of the file)
   
   To access existing Features, the AttributeReader is advanced, the current attribute and feature ID assembled into a Feature. This Feature is then duplicated and returned to the user. We will later compare the original to the user's copy to check if any modifications have been made.

6. Add write() implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // write start
      :end-before: // write end

   In the write method we will need to check to see whether the user has changed anything. If so,
   we will need to remember to issue event notification after writing out their changes.

7. Add remove() implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // remove start
      :end-before: // remove end

  To implement remove, we will skip over the origional feature (and just won't write it out).
  Most of the method is devoted to gathering up the information needed to issue
  a feature removed event.

8. Add close() Implementation:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyFeatureWriter.java
      :language: java
      :start-after: // close start
      :end-before: // close end

   To implement close() we must remember to write out any remaining features in the DataStore
   before closing our new file. To implement this we have performed a small optimization: we
   echo the line acquired by the PropertyFeatureReader.
   
   The last thing our FeatureWriter must do is replace the existing File with our new one.

PropertyAttributeWriter
^^^^^^^^^^^^^^^^^^^^^^^

In the previous section we explored the capabilities of our PropertyWriter through actual use;
now we can go ahead and define the class.

1. Create PropertyAttributeWriter:
    
   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: */
      :end-before: // constructor end

   A BufferedWriter is created over the provided File, and the provided featureType is used to
   implement getAttribtueCount() and getAttributeType( index ).

2. Add hasNext() and next() implementations:

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: // next start
      :end-before: // next end

  Our FeatureWriter does not provide any content of its own. FeatureWriters that are backed by
  JDBC ResultSets or random access file may use hasNext() to indicate that they are streaming
  over existing result set.
  
  Our implementation of next() will start a newLine for the feature that is about to be written.

3. Add writeFeatureID():

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: // writeFeatureID start
      :end-before: // writeFeatureID end

  Our file format is capable of storing FeatureIDs. Many DataStores will need to derive or encode
  FeatureID information into their Attributes.

4. Add write( int index, Object value ):

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: // write start
      :end-before: // write end

  Our implementation needs to prepend an equals sign before the first Attribute, or a bar for any
  other attribute.
  
  We also make sure to encode any newlines in String content, Geometry as wkt, and use the Converters
  class to handle any other objects correctly.

5. Add close():

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: // close start
      :end-before: // close end

6. Finally, to implement our FeatureWriter.close() optimization, we need to implement echoLine():

   .. literalinclude:: /../../modules/plugin/property/src/main/java/org/geotools/data/property/PropertyAttributeWriter.java
      :language: java
      :start-after: // echoLine start
      :end-before: // echoLine end