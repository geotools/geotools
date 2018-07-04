:Author: Travis Brundage
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Strategy Patterns
-----------------

Since this tutorial has been written the result has been broken out into a distinct **gt-csv** module. The work has also been forked into service as part of the GeoServer importer module. Originally in GeoServer's fork, a nifty CSVStrategy was added (with implementations for CSVAttributesOnlyStrategy, CSVLatLonStrategy, CSVSpecifiedLatLngStrategy and CSVSpecifiedWKTStrategy). In this section we'll discuss the new implementation which is a cleaning up and improvement on the strategy pattern they implemented, with read and write functionality. If you want to follow along with the code, it is available as an unsupported plugin:

* :download:`CSVDataStore.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java>`
* :download:`CSVDataStoreFactory.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java>`
* :download:`CSVFeatureReader.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureReader.java>`
* :download:`CSVFeatureSource.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java>`
* :download:`CSVFeatureStore.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureStore.java>`
* :download:`CSVFeatureWriter.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java>`
* :download:`CSVIterator.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java>`
* :download:`CSVFileState.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java>`
* :download:`CSVStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java>`
* :download:`CSVAttributesOnlyStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVAttributesOnlyStrategy.java>`
* :download:`CSVLatLonStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java>`
* :download:`CSVSpecifiedWKTStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java>`

CSVDataStore
^^^^^^^^^^^^

CSVDataStore now uses a CSVStrategy and CSVFileState. The CSVFileState holds information about the file we are reading from. CSVStrategy is an abstract class for the strategy objects CSVDataStore can use.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: import org.opengis.filter.Filter;
      :end-before: public Name getTypeName() {

Using the CSVFileState to do work for us, the **getTypeName()** method to create Name is much simpler.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: // docs start getTypeName
      :end-before: // docs end getTypeName
      
CSVDataStore now implements the FileDataStore interface to ensure a standard for operations which are performed by FileDataStores. As such, it must override its methods. Note the use of the CSVStrategy in order to determine the schema. Depending on the strategy defined, the schema for this store will be different. The implementation of **createFeatureSource()** checks to make sure the file is writable before allowing the writing of features. If it is, it actually uses a CSVFeatureStore instead of a CSVFeatureSource, which is a data structure that will allow being written to as well as read from.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: // docs start dataStoreOperations
      :end-before: // docs end dataStoreOperations

CSVDataStoreFactory
^^^^^^^^^^^^^^^^^^^

The new architecture with the added strategy objects expands the CSVDataStoreFactory's capabilities. It contains a few more :code:`Param` fields now. 
Much of the class's structure is improved to be more compartmentalized. The metadata is mostly the same with some data now being held in class fields rather than literals.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: import org.locationtech.jts.geom.GeometryFactory;
      :end-before: @Override


The method **isAvailable()** just attempts to read the class, and if it succeeds, returns true.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // docs start isAvailable
      :end-before: // docs end isAvailable

The **canProcess(Map<String, Serializable> params)** method was made more tolerant, now accepting URL and File params through the **fileFromParams(Map<String, Serializable> params)** method. It will try File first, then URL before giving up.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // docs start canProcess
      :end-before: // docs end canProcess

Finally, the different strategies are implemented in the **createDataStoreFromFile()** method. The method is overloaded to make some parameters optional, which the class will then fill in for us.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: // docs start createDataStoreFromFile
      :end-before: // docs end createDataStoreFromFile

CSVFeatureReader
^^^^^^^^^^^^^^^^

The CSVFeatureReader now delegates much of the functionality to a new class called CSVIterator as well as the CSVStrategy. The resulting code is very clean and short.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureReader.java
      :language: java
      :start-after: import org.opengis.feature.simple.SimpleFeatureType;

CSVFeatureSource
^^^^^^^^^^^^^^^^

CSVFeatureSource retains the same basic structure, but the code is assisted by the new classes. It now overloads the constructor:

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: import org.opengis.feature.simple.SimpleFeatureType;
      :end-before: public CSVDataStore getDataStore() {

The **getBoundsInternal(Query query)** method is now implemented by making use of the methods provided by ContentFeatureSource. A new ReferencedEnvelope is created to store the bounds for this feature source. It uses the feature type (**getSchema()**) to determine the CRS (**getCoordinateReferenceSystem()**) - this information is used to construct the bounds for the feature. The FeatureReader is now created by using the Query and CSVStrategy - the **getReader()** method calls **getReaderInternal(Query query)** which shows how it is created. Finally, using the reader, the features are cycled through and included in the bounds in order to calculate the bounds for this entire datastore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: // docs start getBoundsInternal
      :end-before: // docs end getBoundsInternal

The **getReaderInternal(Query query)** method now utilizes the strategy of the CSVDataStore rather than state to reflect the changes to the CSVFeatureReader design.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: // docs start getReaderInternal
      :end-before: // docs end getReaderInternal

The **getCountInternal(Query query)** method uses the same idea as **getBoundsInternal(Query query)** - it now utilizes the Query and CSVStrategy to obtain a FeatureReader, then simply counts them.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: // docs start getCountInternal
      :end-before: // docs end getCountInternal

The **buildFeatureType()** method is now very simple using **getSchema()** to grab the feature type of the datastore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: // docs start buildFeatureType
      :end-before: // docs end buildFeatureType

CSVFeatureStore
^^^^^^^^^^^^^^^

CSVFeatureStore essentially acts as a read/write version of CSVFeatureSource. Where CSVFeatureSource is only readable, CSVFeatureStore adds the ability to write through the use of a CSVFeatureWriter. The code is updated to use the strategy pattern which it must pass to the writer.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureStore.java
      :language: java
      :start-after: import org.opengis.feature.type.Name;

CSVFeatureWriter
^^^^^^^^^^^^^^^^

The CSVFeatureWriter handles the writing functionality for our CSVFeatureStore. With the new architecture, a new class called CSVIterator is used as our delegate (**private CSVIterator iterator;**) rather than the CSVFeatureReader.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java
      :start-after: import com.csvreader.CsvWriter;
      :end-before: public CSVFeatureWriter(CSVFileState csvFileState, CSVStrategy csvStrategy)

The feature type we grab for writing is dependent on our strategy; therefore, we must feed CSVFeatureWriter our CSVStrategy and grab the feature type from it. We'll aslo get our iterator, which reads the file, from our CSVStrategy. Finally, we'll set up a CsvWriter to write to a new file, temp, with the same headers from our current file.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java
      :start-after: // docs start CSVFeatureWriter
      :end-before: // docs end CSVFeatureWriter

The **hasNext()** method will first check if we're appending content, in which case we are done reading - there is nothing next. Otherwise, it passes off to the CSVIterator's implementation.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java
      :start-after: // featureType start
      :end-before: // hasNext end

The **next()** method will also check if we are appending. If we're not done reading, we grab the next from our iterator; otherwise, we are done so we want to append content. In this case, it will build the next feature we wish to append. **remove()** will just mark the current feature to be written as null, preventing it from being written.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java
      :start-after: // next start
      :end-before: // remove end

Finally, the **write()** method takes our current feature and uses the strategy to **encode** it. The encoding gives us back this feature as a CsvRecord, which our writer then writes out to the file. Finally, we take the temp file we've written to and copy its contents into the file our store holds in CSVFileState.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureWriter.java
      :language: java
      :start-after: // write start

CSVIterator
^^^^^^^^^^^

The CSVIterator is a helper class primarily for CSVFeatureReader. Much of the old code is now implemented here, and has the added benefit of allowing an iterator to be instantiated for use elsewhere, making the code more general than before. With the addition of the CSVFileState, the class now reads from it instead of the CSVDataStore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java
      :language: java
      :start-after: import com.csvreader.CsvReader;
      :end-before: private SimpleFeature buildFeature(String[] csvRecord) {

Because we're now using strategy objects to implement functionality, the **readFeature()** method no longer makes any assumptions about the nature of the data. It is delegated to the strategy to make such a decision. The resulting method is shorter, just passing what it reads off to builders to implement based on the strategy.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java
      :language: java
      :start-after: // docs start readFeature
      :end-before: // docs end readFeature

CSVFileState
^^^^^^^^^^^^

The CSVFileState is a new class to assist with File manipulation in our CSVDataStore. It will hold some information about our :file:`.csv` file and allow it to be opened for reading.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java
      :language: java
      :start-after: import com.csvreader.CsvReader;
      :end-before: public CsvReader openCSVReader() throws IOException {

The class opens the file for reading, ensures it is the correct CSV format, and gives back a CSVReader to read the file through a stream.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java
      :language: java
      :start-after: // docs start openCSVReader
      :end-before: // docs end openCSVReader

The **readCSVHeaders()** and **getCSVHeaders()** methods grab the headers from the file (thus, leaving just the data).

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java
      :language: java
      :start-after: // docs start getCSVHeaders
      :end-before: // docs end readCSVHeaders

CSVStrategy
^^^^^^^^^^^

CSVStrategy defines the API used internally by CSVDataStore when converting from CSV Records to Features (and vice versa).

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
      :language: java
      :start-after: // docs start CSVStrategy
      :end-before: // docs end CSVStrategy

The name "strategy" comes form the strategy pattern - where an object (the strategy) is injected into our CSVDataStore to configure it for use. CSVDataStore will call the strategy object as needed (rather than have a bunch of switch/case statements inside each method).

Subclasses of CSVStrategy will need to implement:

* **buildFeatureType()** - generate a FeatureType (from the CSV file headers - and possibly a scan off the data)
* **createSchema(SimpleFeatureType)** - create a new file using the provided feature type
* **decode(String, String[])** - decode a record from the csv file
* **encode(SimpleFeature)** - encode a feature as a record (to be written to the csv file)

This API is captured as an abstract class which can be subclassed for specific strategies. The strategy objects are used by the CSVDataStore to determine how certain methods will operate: by passing the strategy objects into the CSVDataStore, their implementation is used. Through this design, we can continue extending the abilities of the CSVDataStore in the future much more easily. 

The base class has some support methods available for use by all the strategy objects. The **createBuilder()** methods are helpers that set some of the common portions for the SimpleFeatureBuilder utility object, such as the type name, coordinate reference system, namespace URI, and then the column headers.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
      :language: java
      :start-after: // docs start CSVStrategy
      :end-before: // docs end CSVStrategy

The **findMostSpecificTypesFromData(CsvReader csvReader, String[] headers)** method attempts to find the type of the data being read. It attempts to read it as an Integer first, and if the format is incorrect, it tries a Double next, and if the format is still incorrect, it just defaults to a String type. It scans the entire file when doing so to ensure that later on the values do not change to a different type.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
      :language: java
      :start-after: // docs start createBuilder
      :end-before: // docs end findMostSpecificTypesFromData
      
CSVAttributesOnlyStrategy
^^^^^^^^^^^^^^^^^^^^^^^^^

The CSVAttributesOnlyStrategy is the simplest implementation. It directly reads the file and obtains the values as attributes for the feature. The feature type is built using helper methods from a support class which will be visited later. The headers from the :file:`.csv` file are read in as attributes for this feature. Each header is an attribute defined in that column, and each row provides the values for all the attributes of one feature. The csvRecord parameter contains one line of data read in from the file, and each String is mapped to its attribute. The SimpleFeatureBuilder utility class uses all the data to build this feature. 

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVAttributesOnlyStrategy.java
      :language: java
      :start-after: import com.csvreader.CsvWriter;

CSVLatLonStrategy
^^^^^^^^^^^^^^^^^

The CSVLatLonStrategy provides the additional component of supplanting Latitude and Longitude fields with a Point geometry. We search through the headers to see if there is a match for both Latitude and Longitude, and if so, we remove those attributes and replace it with the Point geometry. The user can specify the strings to use to search for the Lat and Lon columns (for example, "LAT" and "LON"). Otherwise, the class will attempt to parse for a valid lat/lon spelling. The user can also choose to name the geometry column, or else it will default to "location". Using this information, it builds the feature type.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :start-after: // docs start CSVLatLonStrategy
      :end-before: // docs end isLongitude

When encoding the feature, the geometry will grab the Y value first (latitude) and the X value second (longitude). This is in compliance with the standards by **WGS84**. Otherwise, it works the same as the attributes only strategy.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :start-after: // docs start encode
      :end-before: // docs end encode

When decoding a CsvRecord into a feature, we parse for the latField and lngField and store those values. At the end if we've successfully grabbed both a latitude and longitude, we create it as a Point in our feature.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :start-after: // docs start decode
      :end-before: // docs end decode

For our **createSchema()** method, we search for the geometry column that we should have created - specified with **WGS84** as the CRS - and if successful, we add our specified latField and lngField to the header. If unsuccessful, we throw an IOException. The rest of the columns just use the names they were given. If we find a GeometryDescriptor, we skip it because that was our Lat/Lon column. Everything else in this strategy is just stored as an Attribute. Finally, the header is written using the CsvWriter.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :start-after: // docs start createSchema
      :end-before: // docs end createSchema

CSVSpecifiedWKTStrategy
^^^^^^^^^^^^^^^^^^^^^^^

CSVSpecifiedWKTStrategy is the strategy used for a Well-Known-Text (WKT) format. A specified WKT must be passed to the strategy to be used to parse for the WKT.

Similar to the CSVLatLonStrategy, a specified WKT must be passed to the strategy to be used to parse for the WKT. If found, it attaches the Geometry class to the WKT in the header.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :start-after: import org.locationtech.jts.io.WKTWriter;
      :end-before: @Override

To build the feature type with this strategy, the only thing that needs to be changed is updating the specified WKT field. Instead of reading this data as an Integer, Double or String (as in the base CSVStrategy class's **createBuilder()** method), we want to use a Geometry class to store the information in the WKT Field's column. To do this, we create an AttributeBuilder, set our CRS to **WGS84** and the binding to :file:`Geometry.class`. We get an AttributeDescriptor from this builder, suppling it with the wktField specified as its name. Then we set the featureBuilder with this AttributeDescriptor, it overwrites it with the new information.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :start-after: // docs start buildFeatureType
      :end-before: // docs end buildFeatureType

For creating the schema, the only thing we search for is a GeometryDescriptor, which we will know is our wktField. Otherwise, we just use the names they were given.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :start-after: // docs start createSchema
      :end-before: // docs end createSchema

When encoding a feature, we simply parse for the wktField described by the strategy. If found, we use a WKTWriter to correctly write out the Geometry as a WKT field, which is then added to our CsvRecord. Otherwise, the value is passed to a utility method **convert()** which will write the value out as a String.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :start-after: // docs start encode
      :end-before: // docs end encode

When decoding a CsvRecord, we check if we are in the WKT column (current header value is the wktField specified) and if we have a GeometryDescriptor in our featureType. If both are true, we create a WKTReader to read the value as a Geometry type so that we can build our feature with this Geometry. If it fails for some reason, the exception is caught and the attribute is treated as null.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :start-after: // docs start decode
      :end-before: // docs end decode
