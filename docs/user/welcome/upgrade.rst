Upgrade
=======

With a library as old as GeoTools you will occasionally run into a project from ten years ago that
needs to be upgraded. This page collects the upgrade notes for each release change; highlighting any
fundemental changes with code examples showing how to upgrade your code.

But first to upgrade - change your dependency to 8-SNAPSHOT (or an appropriate stable version)::

    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <geotools.version>8-SNAPSHOT</geotools.version>
    </properties>
    ....
    <dependencies>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-shapefile</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        <dependency>
            <groupId>org.geotools</groupId>
            <artifactId>gt-swing</artifactId>
            <version>${geotools.version}</version>
        </dependency>
        ....
    </dependencies>

Upgrade from GeoTools 2.7
-------------------------

.. sidebar:: Wiki
   
   * `GeoTools 8.0 <http://docs.codehaus.org/display/GEOTOOLS/8.x>`_
   
   You are encourged to review the change proposals for GeoTools 8.0 for background information
   on the following changes:

The changes moving from GeoTools 2.7 to GeoTools 8.0 have a great emphasis on usability and
documentation. Because of the focus on ease of use; many of the changes here are marked "Optional"
this indicates that your code will not break; but you have a chance to clean it up and make
your code more readable.

Style
^^^^^

Some of the **gt-opengis** style methods that have been deprecated for a while are now removed.

* Mark.getRotation() / Mark.setRotation( Expression )
* Mark.getSize() / Mark.setSize( Expression )

These are handled in a similar manner:

* BEFORE::

      for( GraphicalSymbol symbol : graphic.graphicalSymbols() ){
          if( symbol instanceof Mark ){
               Mark mark = (Mark) symbol;
               mark.setSize( ff.literal( 8 ) );
          }
      }

* AFTER::

      graphic.setSize( ff.literal( 8 ) );

Function
''''''''

We have extended **gt-opengis** Function to make the FunctionName description (especially
argument names) more available.

* To updateyour code::

    class SplitFunction implements Function {
        public static FunctionName NAME = new FunctionNameImpl( "split", "geometry", "line" );
        ...
        FunctionName getFunctionName(){
            return NAME;
        }
        ...
    }

If you are extending abstract function expression base class; it provides a default implementation
of getFunctionName() allowing your code to compile.

FunctionExpression
''''''''''''''''''

In a related matter **gt-main** no longer provides access to the deprecated FunctionExpression
interface (it has returned an empty set for several releases now):

* BEFORE::

        Set<String> proposals = new TreeSet<String>();
        Set<Function> oldFunctions = FunctionFinder. CommonFactoryFinder.getFunctionExpressions(null);
        for( Function function : oldFunctions ) {
            proposals.add(function.getName().toLowerCase());
        }

* AFTER::

        Set<String> proposals = new TreeSet<String>();
        
        FunctionFinder functionFinder = new FunctionFinder(null);
        for( FunctionName function : functionFinder.getAllFunctionDescriptions() ){
            proposals.add(function.getName().toLowerCase());
        }

Direct Position and Envelope
^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Deprecated methods in **gt-opengis** and **gt-referencing** have now been removed.

======================================= ================================ ===============================
Deprecated method in 2.7                Replacement in 8.0               Notes
======================================= ================================ ===============================
DirectPosition.getCoordinates()         DirectPosition.getCoordinate()   For consistency with ISO 19107
Envelope.getCenter()                    Envelope.getMedian()}}           For consistency with ISO 19107
Envelope.getLength()                    Envelope.getSpan()               For consistency with ISO 19107
Precision.getMaximumSignificantDigits() Precision.getScale()}}           Remove duplication
PointArray.length()                     List.size()                      PointArray instance can be used
PointArray.position()                   this                             PointArray instance can be used
Position.getPosition()                  Position.getDirectPosition()     For consistency with ISO 19107
Point.setPosition()                     Point.setDirectPosition()        For consistency with ISO 19107
======================================= ================================ ===============================

NumberRange
^^^^^^^^^^^

The **gt-metadata** NumberRange class is finally sheading some of its deprecated methods.

** BEFORE::
      
      NumberRange before = new NumberRange( 0.0, 5.0 );

** AFTER::
      
      NumberRange<Double> after1 = new NumberRange( Double.class, 0.0, 5.0 );
      NumberRange<Double> after2 = NumberRage.create( 0.0, 5.0 );

Upgrade from GeoTools 2.6
-------------------------

The changes from GeoTools 2.6 to GeoTools 2.7 focus on making your code more readible; you will
find a number of optional changes (such as using Query rather than DefaultQuery) which will
simply make your code easier to follow.


Query
^^^^^

The *gt-api** module has been updated to make **Query** a concrete class rather than an interface.

* BEFORE::
        
        Query query = new DefaultQuery( typeName, filter );

* AFTER::
        
        Query query = new Query( typeName, filter );

Tips:

* You can perform a search and replace to change DefaultQuery to Query on your code base
* If you have your own implementation of Query your code is now broken; after many years we have
  never seen an implementation of Query in the wild. You should be able to fix by extending rather
  then implementing Query.
* DefaultQuery still exists but all of the implementation code has now been "pulled up" into
  Query and DefaultQuery marked as deprecated.
* In a similar fashion *FeatureLock* can now be directly constructed rather than use a Factory.

SimpleFeatureCollection
^^^^^^^^^^^^^^^^^^^^^^^

We have vastly cut down the use of Java generics for causual users of the GeoTools library. The
primary example of this is the introduction of **SimpleFeatureCollection** (which saves you
typing in **FeatureCollection<SimpleFeatureType,SimpleFeature>** each time).

* BEFORE::
    
    FeatureSource<SimpleFeatureType,SimpleFeature> source =
            (FeatureSource<SimpleFeatureType,SimpleFeature>) dataStore.getFeatureSource( typeName );
    Query query = new DefaultQuery( typeName, filter );
    FeatureCollection<SimpleFeatureType,SimpleFeature> featureCollection = source.getFeatures( query );

* AFTER::
    
    SimpleFeatureSource source = dataStore.getFeatureSource( typeName );
    Query query = new Query( typeName, filter );
    SimpleFeatureCollection featureCollection = source.getFeatures( query );

Tips:

* You can do a search and replace on this one; but you need to be very careful with any
  implementations you have that accept a FeatureCollection<SimpleFeatureType,SimpleFeature>
  as a method parameter!
  
* Be careful if you have your own FeatureStore implementation; a search and replace will change
  several of your methods so they no longer "override" the default implementation provided by
  AbstractFeatureStore.::
  
       @Override // this would fail; you do use Override right?
       public Set addFeatures( SimpleFeatureCollection features ){
          ... your implementation goes here ...
  
  To fix this code you will need to "undo" your search and replace for this method parameter::

       @Override
       public Set addFeatures( FeatureCollection<SimpleFeatureType,SimpleFeature> features ){
          ... your implementation goes here ...
  
  Note: If you use the @Override annotation in your code you will get a proper error; since your
  new method would no longer override anything.

SimpleFeatureSource
^^^^^^^^^^^^^^^^^^^

The **gt-api** module now defines **SimpleFeatuyreSource** (to save you a bit of typing). In addition
the **DataStore** interface now returns a **SimpleFeatureSource**; so if you want you optionally
can update your code for readability.

* BEFORE::

    FeatureSource<SimpleFeatureType,SimpleFeature> source =
           (FeatureSource<SimpleFeatureType,SimpleFeature>) dataStore.getFeatureSource( typeName );

* AFTER:
    
    SimpleFeatureSource source =  dataStore.getFeatureSource( typeName );

Tips:
* you can do this with a search and replace
* Be a bit careful when you have one of your own methods that is expecting a FeatureSource

SimpleFeatureStore
^^^^^^^^^^^^^^^^^^
In a similar fashion returns a SimpleFeatureCollection; it also has a couple of its own tricks:

* BEFORE::
  
    FeatureSource<SimpleFeatureType,SimpleFeature> source =
        (FeatureSource<SimpleFeatureType,SimpleFeature>) dataStore.getFeatureSource( typeName );
    if( source instanceof FeatureStore){
       // read write access
       FeatureStore<SimpleFeatureType,SimpleFeature> store =
            (FeatureStore<SimpleFeatureType,SimpleFeature>) source;
       store.addFeatures( newFeatures );
       ...

* AFTER::
  
    SimpleFeatureSource source =  dataStore.getFeatureSource( typeName );
    if( source instanceof SimpleFeatureStore){
       // read write access
       SimpleFeatureStore store = (SimpleFeatureStore) source;
       store.addFeatures( newFeatures );
       ...

SimpleFeatureLocking
^^^^^^^^^^^^^^^^^^^^

You can also explicitly use SimpleFeatureLocking if you want read/write/lock access to simple
feature content. Much like **Query** it has been made a concrete class.

FeatureStore modifyFeatures by Name
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The **FeatureStore** method modifyFeatures now allows you to modify features by name.

* BEFORE::
    
    FeatureSource<SimpleFeatureType,SimpleFeature> source =
        (FeatureSource<SimpleFeatureType,SimpleFeature>) dataStore.getFeatureSource( typeName );
    if( source instanceof FeatureStore){
       // read write access
       FeatureStore<SimpleFeatureType,SimpleFeature> store =
            (FeatureStore<SimpleFeatureType,SimpleFeature>) source;
       
       SimpleFeatureType schema = store.getSchema();
       AttributeDescriptor attribute = schema.getDescriptor( attributeName );
       store.modifyFeatures( attribute, attributeValue, filter );

* AFTER::
    
    SimpleFeatureSource source =  dataStore.getFeatureSource( typeName );
    if( source instanceof SimpleFeatureStore){
       // read write access
       SimpleFeatureStore store = (SimpleFeatureStore) source;
       store.modifyFeatures( attributeName, attributeValue, filter );
       ...

Tips:

* Generic FeatureSource allows modifyFeatures( Name, Value, filter )

CoverageProcessor
^^^^^^^^^^^^^^^^^

The DefaultProcessor and AbstractProcessor classes have been merged into a single class called
**CoverageProcessor**.

* BEFORE::
    
    final DefaultProcessor processor= new DefaultProcessor(hints)

* AFTER::
    
    final CoverageProcessor processor= new CoverageProcessor(hints)
  
  Or better::
  
      final CoverageProcessor processor= CoverageProcessor.getInstace(hints);

Tips:

* Try to always use the static getDefaultInstance method in order to leverage on SoftReference caching

GeneralEnvelope
^^^^^^^^^^^^^^^

We have been removing old deprecated code from the **GeneralEnvelope** class.

=============================== ===============================================
Old Method                      New Method
=============================== ===============================================
double getCenter(dimension)     DirectPosition getMedian()
double getCenter()              double getMedian(dimension)
double getLength(dimension)     double getSpan(dimension)
getLength(dimension, unit)      double getSpan(dimension, unit)
=============================== ===============================================
