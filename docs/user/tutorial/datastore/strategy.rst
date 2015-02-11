:Author: Travis Brundage
:Thanks: geotools-devel list
:Version: |release|
:License: Creative Commons with attribution

Strategy Patterns
-----------------

Since this tutorial has been written the result has been broken out into a distinct **gt-csv** module. This work has also been forked into service as part of the GeoServer importer module. In GeoServer's fork, many interesting updates were made to add new abilities. The implementation is strictly concerned with reading content (as part of an import process) and has added a nifty CSVStrategy (with implementations for CSVAttributesOnly, CSVLatLonStrategy, CSVSpecifiedLatLngStrategy and SpecifiedWKTStrategy). In this section we'll discuss the new implementation with strategy objects. If you want to follow along with the code, it is available as an unsupported plugin:

* :download:`CSVDataStore.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java>`
* :download:`CSVDataStoreFactory.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java>`
* :download:`CSVFeatureReader.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureReader.java>`
* :download:`CSVIterator.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java>`
* :download:`CSVFeatureSource.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java>`
* :download:`CSVFileState.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java>`
* :download:`CSVStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java>`
* :download:`CSVStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java>`
* :download:`CSVAttributesOnlyStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVAttributesOnlyStrategy.java>`
* :download:`CSVLatLonStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java>`
* :download:`CSVSpecifiedLatLngStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedLatLngStrategy.java>`
* :download:`CSVSpecifiedWKTStrategy.java </../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java>`

CSVDataStore
^^^^^^^^^^^^

CSVDataStore now uses a CSVStrategy and CSVFileState. The CSVFileState holds information about the file we are reading from. CSVStrategy is a generic interface for the strategy objects CSVDataStore can use.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :start-after: import org.opengis.filter.Filter;
      :end-before: public Name getTypeName() {

Using the CSVFileState to do work for us, the **createTypeNames()** method is much simpler.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :lines: 40-47

CSVDataStore now implements the FileDataStore interface to ensure a standard for operations which are performed by FileDataStores. As such, it must override its methods. Note the use of the CSVStrategy in order to determine the schema. Depending on the strategy defined, the schema for this store will be different. For this implementation, the CSVDataStore is read only, so the write methods throw a new UnsupportedOperationException.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStore.java
      :language: java
      :lines: 55-90

CSVDataStoreFactory
^^^^^^^^^^^^^^^^^^^

The new architecture with the added strategy objects expands the CSVDataStoreFactory's capabilities. It comprises of more Param fields now.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :start-after: import org.geotools.util.KVP;
      :end-before: @Override

Much of the class's structure is improved to be more compartmentalized. The metadata is mostly the same with some data now being held in class fields rather than literals.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :lines: 55-68

The method **isAvailable()** just attempts to read the class, and if it succeeds, returns true.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :lines: 99-107

The **canProcess(Map<String, Serializable> params)** method was made more tolerant, now accepting URL and File params through the **fileFromParams(Map<String, Serializable> params)** method. It will try File first, then URL before giving up.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :lines: 70-97

Finally, the different strategies are implemented in the **createDataStoreFromFile()** method. The method is overloaded to make some parameters optional, which the class will then fill in for us.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVDataStoreFactory.java
      :language: java
      :lines: 114-182

CSVFeatureReader
^^^^^^^^^^^^^^^^

The CSVFeatureReader now delegates much of the functionality to a new class called CSVIterator as well as the CSVStrategy. The resulting code is very clean and short.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureReader.java
      :language: java
      :start-after: import org.opengis.feature.simple.SimpleFeatureType;

CSVIterator
^^^^^^^^^^^

The CSVIterator is a helper class primarily for CSVFeatureReader. Much of the old code is now implemented here, and has the added benefit of allowing an iterator to be instantiated for use elsewhere, making the code more general than before. With the addition of the CSVFileState, the class now reads from it instead of the CSVDataStore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java
      :language: java
      :start-after: import com.csvreader.CsvReader;
      :end-before: private SimpleFeature buildFeature(String[] csvRecord) {

Because we're now using strategy objects to implement functionality, the readFeature() method no longer makes any assumptions about the nature of the data. It is delegated to the strategy to make such a decision. The resulting method is shorter, just passing what it reads off to builders to implement based on the strategy.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVIterator.java
      :language: java
      :lines: 54-60

CSVFeatureSource
^^^^^^^^^^^^^^^^

CSVFeatureSource retains the same basic structure, but the code is assisted by the new classes. It now overloads the constructor:

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :start-after: import org.opengis.feature.simple.SimpleFeatureType;
      :end-before: public CSVDataStore getDataStore() {

The **getBoundsInternal(Query query)** method is now implemented by making use of the methods provdied by ContentFeatureSource. A new ReferencedEnvelope is created to store the bounds for this feature source. It uses the feature type (**getSchema()**) to determine the CRS (**getCoordinateReferenceSystem()**) - this information is used to construct the bounds for the feature. The FeatureReader is now created by using the Query and CSVStrategy - the **getReader()** method calls **getReaderInternal(Query query)** which shows how it is created. Finally, using the reader, the features are cycled through and included in the bounds in order to calculate the bounds for this entire datastore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :lines: 41-54

The **getReaderInternal(Query query)** method now utilizes the strategy of the CSVDataStore rather than state to reflect the changes to the CSVFeatureReader design.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :lines: 69-73

The **getCountInternal(Query query)** method uses the same idea as **getBoundsInternal(Query query)** - it now utilizes the Query and CSVStrategy to obtain a FeatureReader, then simply counts them.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :lines: 56-67

The **buildFeatureType()** method is now very simple using **getSchema()** to grab the feature type of the datastore.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFeatureSource.java
      :language: java
      :lines: 75-77

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
      :lines: 94-106

The **readCSVHeaders()** and **getCSVHeaders()** methods grab the headers from the file (thus, leaving just the data).

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/CSVFileState.java
      :language: java
      :lines: 108-131

CSVStrategy
^^^^^^^^^^^

CSVStrategy define the API used internally by CSVDataStore when converting from CSV Records to Features (and vice versa).

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
      :language: java
      :start-after: // class definition start
      :end-before:  // class definition end

The name "strategy" comes form the strategy pattern - where an object (the strategy) is injected into our CSVDataStore to configure it for use. CSVDataStore will call the strategy object as needed (rather than have a bunch of switch/case statements inside each method).

Subclasses of CSVStrategy will need to implement:

* **buildFeatureType()** - generate a FeatureType (from the CSV file headers - and possibly a scan off the data)
* **createSchema(SimpleFeatureType)** - create a new file using the provided feature type
* **decode(String, String[])** - decode a record from the csv file
* **encode(SimpleFeature)** - encode a feature as a record (to be written to the csv file)


This API is captured as an abstract class which can be subclassed for specific strategies. The strategy objects are used by the CSVDataStore to determine how certain methods will operate: by passing the strategy objects into the CSVDataStore, their implementation is used. Through this design, we can continue extending the abilities of the CSVDataStore in the future much more easily. 

As an example the buildFeatureType method is used to parse the feature type once:

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
      :language: java
      :start-after: // featureType start
      :end-before: // featureType mend

The base class has some support methods available for use by all the strategy objects. The **createBuilder()** methods are helpers that set some of the common portions for the SimpleFeatureBuilder utility object, such as the type name, coordinate reference system, namespace URI, and then the column headers.

.. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
   :language: java
   :start-after: // builder start
   :end-before: // builder end

The **findMostSpecificTypesFromData(CsvReader csvReader, String[] headers)** method attempts to find the type of the data being read. It attempts to read it as an Integer first, and if the format is incorrect, it tries a Double next, and if the format is still incorrect, it just defaults to a String type.

.. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVStrategy.java
   :language: java
   :start-after: // type start
   :end-before: // type end
      
CSVAttributesOnlyStrategy
^^^^^^^^^^^^^^^^^^^^^^^^^

The CSVAttributesOnlyStrategy is the simplest implementation. It directly reads the file and obtains the values as attributes for the feature. The feature type is built using helper methods from a support class which will be visited later. The headers from the :file:`.csv` file are read in as attributes for this feature. Each header is an attribute defined in that column, and each row provides the values for all the attributes of one feature. The csvRecord parameter contains one line of data read in from the file, and each String is mapped to its attribute. The SimpleFeatureBuilder utility class uses all the data to build this feature. 

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVAttributesOnlyStrategy.java
      :language: java
      :start-after: import org.opengis.feature.simple.SimpleFeatureType;

CSVLatLonStrategy
^^^^^^^^^^^^^^^^^

The CSVLatLonStrategy provides the additional component of supplanting Latitude and Longitude fields with a Point geometry. We search through the headers to see if there is a match for both Latitude and Longitude, and if so, we remove those attributes and replace it with the Point geometry.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :start-after: import com.vividsolutions.jts.geom.Point;
      :end-before: @Override

When creating the feature, we check to see if we're in the Latitude column and take it as our y value, and check to see if we're in the Longitude column and take it as our x value. If we found Lat/Lon columns, we convert the x and y into a Point geometry and pass it to our feature builder. The atrribute name is predefined to be "locations" for the Geometry Column.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVLatLonStrategy.java
      :language: java
      :lines: 74-112

CSVSpecifiedLatLngStrategy
^^^^^^^^^^^^^^^^^^^^^^^^^^

The CSVSpecifiedLatLngStrategy behaves similarly to the CSVLatLonStrategy class with just a few minor differences. First, the Latitude and Longitude columns are specified (with a specific string, such as "lat" and "lon"). Further, the points constructed can have a different specified attribute name. However, the default is still "locations" for the Geometry Column if nothing is specified.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedLatLngStrategy.java
      :language: java
      :start-after: import com.vividsolutions.jts.geom.Point;


CSVSpecifiedWKTStrategy
^^^^^^^^^^^^^^^^^^^^^^^

CSVSpecifiedWKTStrategy is the strategy used for a Well-Known-Text (WKT) format. Similar to the CSVSpecifiedLatLngStrategy, a specified WKT must be passed to the strategy to be used to parse for the WKT. If found, it attaches the Geometry class to the WKT in the header.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :lines: 22-41

In creating a feature, the line is parsed for the WKT field specified before, and if found, attempts to read a Geometry object from the file. If for some reason the field is unparseable, it will just be set to a null value. The builder utility will then build this feature after the line has been fully read.

   .. literalinclude:: /../../modules/unsupported/csv/src/main/java/org/geotools/data/csv/parse/CSVSpecifiedWKTStrategy.java
      :language: java
      :lines: 43-71

