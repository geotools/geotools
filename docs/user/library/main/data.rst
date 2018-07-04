DataUtilities
-------------

Working with GIS data can be difficult. Working with the number of classes in the GeoTools library can also be a bit intimidating. The **DataUtilities** a facade classes which can help simplify common data wrangling chores.

In almost all cases the methods for this class act as a front end for the classes provided by gt-main. This documentation makes note of the classes used internally so you can learn how the library is put together and can go look for more control if needed.

If you do have any commonly used data hacks in your code; please submit a patch request to the issue tracker and we can add more useful methods here.

References:

* `DataUtilities <http://docs.geotools.org/latest/javadocs/org/geotools/data/DataUtilities.html>`_ (javadocs)

File
^^^^

* checkFileReadable(File, Logger)
* checkDirectory(File)
* excludeFilters(FilenameFilter, FilenameFilter...)
* includeFilters(FilenameFilter, FilenameFilter...)
  
  Allows FilenameFilters to be combined.

FeatureType
^^^^^^^^^^^

Working with FeatureType can be a bit troubling, as much like Java String it is immutable (cannot be modified once created). 

FeatureType Creation
''''''''''''''''''''

DataUtilities provides the following methods to help you quickly create and modify FeatureType information.

* DataUtilities.createType( typeName, specification )
* DataUtilities.createType( namespace, typeName, specification)
  
  This is great for quickly whipping up a FeatureType when making test cases. For more
  control use SimpleFeatureTypeBuilder as described in gt-opengis.
  
  If you just want to quickly describe some information::
    
    SimpleFeatureType lineType = DataUtilities.createType("LINE", "centerline:LineString,name:\"\",id:0");
  
  I admit that looks a bit strange, you can also use a Java class names if it makes you happy::
    
    SimpleFeatureType schema = DataUtilities.createType("EDGE", "edge:Polygon,name:String,timestamp:java.util.Date");
  
  If you need to set the coordinate reference system as well::
    
    SimpleFeatureType lineType = DataUtilities.createType("LINE", "centerline:LineString:srid=32615,name:\"\",id:0");

  If you are into names spaces that can be handled as well::
  
    SimpleFeatureType lineType = DataUtilities.createType("http://somewhere.net/","LINE", "centerline:LineString,name:\"\",id:0");

  Now we don't want to see you writing code to build up your initial "definition" String, that means you are doing something
  general (and dynamic!) and   should go figure out SimpleFeatureTypeBuilder and SimpleFeatureBuilder.

* DataUtilities.spec(SimpleFeatureType)
  
  You can use this method to quickly get a text representation of a FeatureType::
    
    System.out.println("FeatureType: " + DataUtilities.spec(schema));

  The representation is the same one used by createType above.

FeatureType Modify
''''''''''''''''''

Because a FeatureType cannot be modified once created; all of the following methods
return a modified copy.

* DataUtilities.createSubType(SimpleFeatureType, String[], CoordinateReferenceSystem)
* DataUtilities.createSubType(SimpleFeatureType, String[], CoordinateReferenceSystem, String, URI)
* DataUtilities.createSubType(SimpleFeatureType, String[])
  
  Used to quickly produce a (slightly modified) copy of the provided FeatureType. Used to
  recast a FeatureType with a desired CoordinateReferenceSystem or limit a FeatureType
  to a specific list of attributes.
  
  There are actually a couple subType methods depending on how complicated you want to get.::
    
    FeatureType schema = DataUtilities.createType("EDGE", "edge:Polygon,name:String");
    CoordinateReferenceSystem crs = CRS.decode( "EPSG:4326" );
    
    schema = DataUtilities.createSubType( schema, null, crs );
  
  You can also get a bit more complicated and choose exactly which attributes you want.::
    
    FeatureType schema = DataUtilities.createType("EDGE", "edge:Polygon,name:String,timestamp:java.util.Date");
    
    schema = DataUtilities.subType( schema, new String[]{"edge","name"}, null );

FeatureType Summary
'''''''''''''''''''

FeatureType forms an interesting little data structure as shown in the gt-opengis diagrams.

The following methods traverse this data structure for you building up a summary to answer specific questions.

* DataUtilities.addMandatoryProperties( schema, propertyNames )
  
  Used to review a FeatureType and add the required properties to an existing list::
    
    List<PropertyName> requiredProperties = addMandatoryProperties( schema, null );

* compare(schema1, schema2)
  
  The retype methods allow us to start with an existing FeatureType and produced a
  simplified or modified copy.
  
  This method compares two feature types to sort out if one is a simplification
  of the other.
  
  ====== ==========================================================
  value  compare
  ====== ==========================================================
  +1     if schema1 is a sub type/reorder/renamespace of schema2
  0      if schema1 and schema2 are the same
  -1     if schema1 and schema2 are not related
  ====== ==========================================================

* isMatch(AttributeDescriptor, AttributeDescriptor)
  
  Used to check if values from the two attribute descriptors have a hope of matching.
  Both the name and the binding to a Java class are checked.

Feature
^^^^^^^

These methods help you work with the values stored in an individual feature. They can quickly produce a set of default values, or parse a set of provided strings into the
correct Java Objects as needed.

* template(SimpleFeatureType)
* template(SimpleFeatureType, String)
* template(SimpleFeatureType, Object[])
* template(SimpleFeatureType, String, Object[])
  
  These template methods create a new Feature using of sensible default values
  in the event the user supplied null for a mandatory value.

* defaultValues(SimpleFeatureType)
* defaultValues(SimpleFeatureType, Object[])
* defaultValue(AttributeDescriptor)
* defaultValue(Class)
  
  These methods provide sensible default values that are a good starting point for
  data entry (if you are giving your user a chance to enter new a new feature by hand).

* parse(SimpleFeatureType, String, String[])

  You can quickly parse out a new feature from input text::
    
    SimpleFeature feature = DataUtilities.parse( schema, fid, text );

* duplicate(Object)
  
  Performs a deep copy of the provided attribute value.
  It is aware of JTS Geometry, and GeoTools constructs such as SimpleFeature
  and will take appropriate measures.

* attributesEqual(Object, Object)
  
  You can safely compare if two attribute values are equal (without worrying about
  Geometry behaving funny)::
  
    DataUtilities.attributesEquals( feature1.getAttribute(1), feature2.getAttribute(1) );

* reType(SimpleFeatureType, SimpleFeature)
* reType(SimpleFeatureType, SimpleFeature, boolean)
  
  These methods allow you to modify a feature to match a new schema:

.. literalinclude:: /../src/main/java/org/geotools/main/MainExamples.java
   :language: java
   :start-after: // exampleRetype start
   :end-before: // exampleRetype end

FeatureCollection
^^^^^^^^^^^^^^^^^

Creating Features has gotten a lot easier with the advent of SimpleFeatureTypeBuilder.

You can still run into situations where adapting feature data is useful when calling methods. The DataUtilities class can help by providing wrappers taking your feature information from Arrays, Collections, FeatureReaders and other

In general we try and work with FeatureReader and FeatureIterator (as these support the
idea of "streaming" information larger than memory). You will find some areas of the code that want to load everything into memory (either as a Collection or Array) often for analysis.

**FeatureCollection**

FeatureCollection is used a lot in GeoTools code giving you a chance to use the following methods.

* DataUtilities.collection( FeatureCollection ) - copies into memory!
* DataUtilities.collection( FeatureReader ) - copies into memory!
* DataUtilities.collection( List<SimpleFeature> )
* DataUtilities.collection( SimpleFeature )
* DataUtilities.collection( SimpleFeature[] )
* DataUtilities.collection( SimpleFeatureIterator )

DataUtilities has helper methods to turn almost anything into a FeatureCollection, this
is really helpful when working with an API that expects a FeatureCollection.::
    
    Feature[] array;
    ...
    return DataUtilities.collection( array );

These methods are often used to add a single SimpleFeature to a FeatureStore::
    
    featureStore.addFeatures( DataUtilities.collection( newFeature ) );

Do be careful some of these implementations suck everything into memory! With GIS
data sizes this will eventually break your application.

* results(SimpleFeature[])
* results(SimpleFeatureCollection)
* results(FeatureCollection<T, F>)
  
These methods convert to a FeatureCollection; but with a twist. They will
produce an error (rather than an empty collection) if the input is null or empty.

In GeoTools 2.0 FeatureCollection was called **FeatureResults**, these methods are
left over from that time.
  
**FeatureCollections**

Internally these methods use the **FeatureCollections** class to create collection
to hold the content::
     
     SimpleFeatureCollection collection = FeatureCollections.newCollection();
     for (SimpleFeature feature : list) {
        collection.add(feature);
     }

FeatureSource
'''''''''''''

FeatureSource is a very capable class, in the worst case the wrappers provided here may need to load everything into memory to get the job done.

* source(SimpleFeature[])
  
  Will wrap a FeatureSource around the provided array allowing the features to
  be queried.

* source(FeatureCollection<SimpleFeatureType, SimpleFeature>)
  
  In a similar fashion a FeatureSource is wrapped around the provided collection.
  
  There are optimised implementations for:
  
  * ListFeatureCollection
  * SpatialIndexFeatureCollection
  * TreeSetFeatureCollection
  
  And as fall back of copying everything into a memory using a CollectionDataStore
  to hold the resulting feature source.

* createView(DataStore, Query)
  
  Creates a light weight "view" that focuses on combining the provided query with
  any requests made to the resulting featureSource.

* dataStore( source )
  
  Adapt source as a singleton dataStore

FeatureReader
'''''''''''''

FeatureReader is the best class we have to represent the nature of streaming large
quantities of information off disk and through your program. 

The following methods allow you to simulate a FeatureReader using information you
happen to have in memory.

* DataUtilities.reader(Collection<SimpleFeature>)
* DataUtilities.reader(FeatureCollection)
* DataUtilities.reader(SimpleFeature[])

The FeatureReader interface works in a similar manner to Iterator<Feature> with
the benefit of IOExceptions in case you are streaming from disk.

DataUtilities sill lets you adapt your own collection to this format.::
    
    FeatureCollection collection;
    
    return DataUtilities.reader( collection );

Summary
'''''''

The following methods provide a summary of feature information; often gathering up
boilerplate code that you would otherwise need to cut and paste into your application.

* DataUtilities.list(FeatureCollection)
  
  Loads the FeatureCollection into a normal java.util.List

* DataUtilities.fidSet(FeatureCollection<?, ?>)
  
  Goes through the FeatureCollection and produced the set of FeatureIds as a simple
  Set<String>.
  
  These identifiers can be used to retrieve features individually.
  
  An example use is displaying features in a table; by getting a set of identifiers
  you can uniquely identify each row, and then only query for the contents of the
  features displayed on screen as needed. 

* DataUtilities.bounds(FeatureCollection<? extends FeatureType, ? extends Feature>)
  
  There are methods to quickly get the bounds from a FeatureSource or FeatureCollection,
  but these methods are implementation dependent often making use of header information
  or summarising the spatial index in order to get you an answer quickly.
  
  Use this method to go through each feature one by one and compute a bounds.

Cast
''''

In GeoTools 2.7 we introduced the idea of a SimpleFeatureCollection (which is a short hand for FeatureCollection<SimpleFeatureType,SimpleFeature>).

While this really helped with learning the library (and the amount of typing required to use it)
we needed to introduce the following methods to help people safely "cast" to a SimpleFeatureCollection when they had a FeatureCollection.

* DataUtilities.simple(FeatureCollection<SimpleFeatureType, SimpleFeature>)
* DataUtilities.simple(FeatureSource)
* DataUtilities.simple(FeatureStore)
* DataUtilities.simple(FeatureType)
* DataUtilities.simple(FeatureLocking)

While the above methods do perform an instance of check; they are also willing to apply a wrapper to get the job done if needed.

In practice these methods are quick and easy to use:

.. literalinclude:: /../src/main/java/org/geotools/main/MainExamples.java
   :language: java
   :start-after: // exampleDataUtilities start
   :end-before: // exampleDataUtilities end

Query
^^^^^

A common task with gt-main is preparing a Query against a FeatureSource. DataUtilities has a number of methods to help.

* DataUtilities.mixQueries(Query, Query, String)
  
  Safely combines two queries in a sensible manner. The provided string is used for the name of the new query.

* DataUtilities.simplifyFilter(Query)

  Simplifies the filter contained in a query, eliminating non-functional clauses.

* DataUtilities.resolvePropertyNames(Query, SimpleFeatureType)
* DataUtilities.resolvePropertyNames(Filter, SimpleFeatureType)

  These two methods rewrite full property names to simple attribute names. 
  For example, property names such as "gml:name" are rewritten as simply "name".

* DataUtilities.addMandatoryProperties(SimpleFeatureType, List<PropertyName>)

SortBy
''''''

* DataUtilities.sortComparator(SortBy)
  
  Creates a Comparator that can be used to sort features as indicated by the Query
  SortBy provided.

Filter and Expression
'''''''''''''''''''''

Part of the fun of preparing a Query is ensuring you ask for the correct values to
perform the task you have in mind.

We have a number of methods to list required attributes for a Filter or Expression:

* DataUtilities.atttributeNames( Filter )
* DataUtilities.atttributeNames( Filter, FeatureType )
* DataUtilities.attributenNames( Expression )
* DataUtilities.attributenNames( Expression, FeatureType )
  
  The optional FeatureType is used as a reference point and can resolve any ambiguities
  between the simple xpath expressions, and the names used in the FeatureType.
  
  Here is an example of using this information to request a FeatureCollection that
  has the required attributes to evaluate the provided expression for every feature.::
    
    String attributes[] = DataUtilities.attributeNames( expression );
    Query query = new Query( typeName, Filter.ALL, attributes );
    
    SimpleFeatureCollection results = featureSource.features( query );
  
* DataUtilities.propertyNames( Filter )
* DataUtilities.propertyNames( Filter, FeatureType )
* DataUtilities.propertyNames( Expression )
* DataUtilities.propertyNames( Expression, FeatureType )
  
  A similar batch of methods using FilterAttributeExtractor to retrieve a
  Set<PropertyName>.
  
  Using a PropertyName is slightly more useful when considering complex XPath
  expressions that use namespaces.

**FilterAttributeExtractor**

Internally the above methods use FilterAttributeExtractor. You can use this class for
greater control.::
        
        FilterAttributeExtractor extract = new FilterAttributeExtractor(null);
        
        Set<String> names = new HashSet<String>();
        // used to collect names from expression1, expression2, and filter
        
        expression1.accept(extract, names);
        expression2.accept(extract, names);
        filter.accept(extract, names);
        
        String array[] = extract.getAttributeNames();
        Set<String> attributes = extract.getAttributeNameSet();
        Set<PropertyName> properties = extract.getPropertyNameSet();
