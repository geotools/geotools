:Author: Jody Garnett
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Making CSVDataStore Writable
----------------------------

In this part we will complete the CSVDataStore. At the end of this section we
will have a full functional CSVDataStore supporting both read and write operations.

The DataStore API has three methods that are involved in making content writable.

* DataStore.getFeatureWriter( typeName ) - an iterator that allows writing
* DataStore.createSchema( featureType )
* DataStore.getFeatureSource( typeName ) - returns a FeatureStore for read-write content

If a tree falls in the forest does anyone hear? When supporting modification chances are some for of event notification is going to be required.  Change notification for users is made available through several FeatureSource methods:

* FeatureSource.addFeatureListener( featureListener )
* FeatureSource.removeFeatureListener( featureListener )

CSVDataStoreFactory
^^^^^^^^^^^^^^^^^^^

Now that we are going to be writing files we can fill in the createNewDataStore method.

1. Open up CSVDataStoreFactory and fill in the method **createNewDataStore( Map params )** which we skipped over earlier.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // createNewDataStore start
      :end-before: // createNewDataStore end
      :prepend:       private static final Logger LOGGER = Logging.getLogger("org.geotools.data.csv");

2. The above code snippet introduces a GeoTools Logger we can use for warnings.
   
   Because GeoTools is a well mannered library it can be configured to use different logging engines. This allows it to integrate smoothly with larger projects.

CSVDataStore
^^^^^^^^^^^^^^^^^

1. Introduce the createSchema( featureType ) method used to set up a new file.
   
   This method provides the ability to create a new FeatureType. For our DataStore we
   will use this to create new properties files.
   
   To implement this method we will once again make use of the DataUtilities class.
   
   Add createSchema( featureType ):
   
   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: // createSchema start
      :end-before: // createSchema end

2. And revise our implementation of **createFeatureSource( ContentEntry )**.
   
   While we will still return a **FeatureSource**, we have the option of returning a the subclass **FeatureStore* for read-write files. The **FeatureStore** interface provides additional methods allowing the modification of content.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: // createFeatureSource start
      :end-before: // createFeatureSource end


CSVFeatureStore
^^^^^^^^^^^^^^^

#. Create **CSVFeatureStore**:

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureStore.java
      :language: java


CSVFeaureWriter
^^^^^^^^^^^^^^^

This class uses an interesting trick to simulate updating a file in place, while still supporting streaming operation. To avoid duplicating all the work we put into **CSVFeatureReader** a delegate is used to read the contents, while the output is sent to a temporary file. When streaming is closed the temporary file is moved into the correct location to effect the change.

FeatureWriter is less intuitive than FeatureReader in that it does not follow the example of Iterator as closely.

FeatureWriter defines the following methods:

* FeatureWriter.getFeatureType
* FeatureWriter.hasNext
* FeatureWriter.next
* FeatureWriter.write
* FeatureWriter.remove
* FeatureWriter.close

Our implementation of a FeatureWriter needs to do two things: support the FeatureWriter interface
and inform the DataStore of modifications.

1. Create the file CSVFeatureWriter.java:

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java

   Our constructor creates a CSVAttributeReader to access the existing contents of
   the DataStore. We made use of CSVAttributeReader to implement
   CSVFeatureReader in Section 1.

  We also create a CSVAttributeWriter operating against a temporary file. When the
  FeatureWriter is closed we will delete the original file and replace it with our new file.

2. Add FeatureWriter.getFeatureType() implementation:

3. Add hasNext() implementation:
    
   Our FeatureWriter makes use of two Features:
   
   * original: the feature provided by CSVAttributeReader
   * live: a duplicate of original provided to the user for modification
   
   When the FeatureWriter is used to write or remove information, the contents of both live
   and feature are set to null. If this has not been done already we will write out the
   current feature.

4. Add the helper function writeImplementation( Feature ):

5. Add next() implementation:

   The next method is used for two purposes:
   
   * To access Features for modification or removal (when working through existing content)
   * To create new Features (when working past the end of the file)
   
   To access existing Features, the AttributeReader is advanced, the current attribute and feature ID assembled into a Feature. This Feature is then duplicated and returned to the user. We will later compare the original to the user's copy to check if any modifications have been made.

6. Add write() implementation:

   In the write method we will need to check to see whether the user has changed anything. If so,
   we will need to remember to issue event notification after writing out their changes.

7. Add remove() implementation:

  To implement remove, we will skip over the origional feature (and just won't write it out).
  Most of the method is devoted to gathering up the information needed to issue
  a feature removed event.

8. Add close() Implementation:

   To implement close() we must remember to write out any remaining features in the DataStore
   before closing our new file. To implement this we have performed a small optimization: we
   echo the line acquired by the CSVFeatureReader.
   
   The last thing our FeatureWriter must do is replace the existing File with our new one.