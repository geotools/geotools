Upgrade
=======

With a library as old as GeoTools you will occasionally run into a project from ten years ago that
needs to be upgraded. This page collects the upgrade notes for each release change; highlighting any
fundamental changes with code examples showing how to upgrade your code.

But first to upgrade - change your dependency to |release| (or an appropriate stable version)::
    
    <properties>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
        <geotools.version>|release|</geotools.version>
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

GeoTools 21.x
-------------

GeoTools 21 is the first is compatible with Java 8 and Java 11.

Restructured Library
^^^^^^^^^^^^^^^^^^^^

The library has been restructured with automatic module names for Java 11 use.

The following table shows how maven dependencies have changed, and the resulting automatic module name for Java 11 use.

.. list-table:: Restructure Library
   :widths: 30, 30, 40
   :header-rows: 1
   
   * - Dependency
     - Upgrade
     - Automatic Module Name
   * - gt-opengis
     - gt-opengis
     - org.geotools.opengis
   * - gt-metadata
     - gt-metadata
     - org.geotools.metadata
   * - gt-api
     - (removed)
     - 
   * - gt-referencing 
     - gt-referencing 
     - org.geotools.referencing
   * - gt-main
     - gt-main
     - org.geotools.main
   * - gt-xml
     - gt-xml
     - org.geotools.xml
   * - gt-xml
     - gt-xml
     - org.geotools.xml
   * - gt-main
     - gt-main
     - org.geotools.data
   * - gt-jdbc
     - gt-jdbc
     - org.geotools.jdbc

Repackage Library
^^^^^^^^^^^^^^^^^

Previously GeoTools reused packages across modules by design, this approach is no longer supported by JDK resulting in the following classes changing package.

.. list-table:: Restructure Library
   :widths: 30, 70
   :header-rows: 3
   
   * - Module
     - Package
   * - Upgrade
     - Package
   * - 
     - Classes Affected
   * - **gt-api**
     - **org.geotools.decorate**
   * - gt-metadata
     - org.geotools.util.decorate
   * - 
     - Abstract Store, Wrapper
   * - **gt-api**
     - **org.geotools.data**
   * - gt-main
     - org.geotools.data
   * - **gt-api**
     - **org.geotools.data.simple**
   * - gt-main
     - org.geotools.data.simple
   * - **gt-api**
     - **org.geotools.decorate**
   * - gt-main
     - org.geotools.util.decorate
   * - 
     - AbstractDecorator, Wrapper
   * - **gt-api**
     - **org.geotools.factory**
   * - gt-main
     - org.geotools.factory
   * - **gt-api**
     - **org.geotools.feature**
   * - gt-main
     - org.geotools.feature
   * - **gt-api**
     - **org.geotools.filter**
   * - gt-main
     - org.geotools.filter
   * - **gt-api**
     - **org.geotools.filter.expression**
   * - gt-main
     - org.geotools.filter.expression

GeoTools 20.x
-------------

GeoTools 20 requires Java 8.

Upgrade to JTS-1.16
^^^^^^^^^^^^^^^^^^^

The transitive dependency will correctly bring in the required jars::

     <dependency>
        <groupId>org.locationtech.jts</groupId>
        <artifactId>jts-core</artifactId>
        <version>${jts.version}</version>
     </dependency>

**Package change to org.locationtech.jts**

This release changes the package names from ``com.vividsolutions.jts`` to ``org.locationtech.jts``. To update your own code follow the `JTS Upgrade Guide <https://github.com/locationtech/jts/blob/master/MIGRATION.md>`__ instructions.

Using the command line to update your own pom.xml files::

   git grep -l com.vividsolutions | grep pom.xml | xargs sed -i "s/com.vividsolutions/org.locationtech.jts/g"
   
And codebase::

   git grep -l com.vividsolutions | xargs sed -i "s/com.vividsolutions/org.locationtech/"

**Use of copy rather than clone**

If you are in the habit of using **clone** to duplicate JTS objects (such as Geometry and Coordinate) you will find the **clone** method has been deprecated, and a **copy** method introduced to explicitly perform a deep copy::
    
    Geometry duplicate = geom.copy();

Migrate to JSR-363 Units
^^^^^^^^^^^^^^^^^^^^^^^^

This releases upgrades from the unofficial JSR-275 units library to the official JSR-363 units API.

Maven transitive dependency will correctly bring in the required jars::
   
    <dependency>
       <groupId>systems.uom</groupId>
       <artifactId>systems-common-java8</artifactId>
       <version>0.7.2</version>
    </dependency>

Package names have changed, resulting in some common search and replaces when upgrading:
  
* Search ``javax.measure.unit.Unit`` replace ``javax.measure.Unit``
* Search ``ConversionException`` replace  ``IncommensurableException``
  
  This is a checked exception, in areas of the GeoTools library where this was found we now return an IllegalArgument exception.
  
* Search ``converter == UnitConverter.IDENTITY`` replace ``converter.isIdentity()``
* Search ``javax.measure.unit.NonSI`` replace ``import si.uom.NonSI``
* Search ``javax.measure.unit.SI`` replace ``import si.uom.SI``
* Search ``SI.METER`` replace ``SI.METRE``
* Search ``javax.measure.converter.UnitConverter`` replace ``javax.measure.UnitConverter``
* Search ``javax.measure.unit.UnitFormat`` replace ``import javax.measure.format.UnitFormat``
* Search ``Unit.ONE`` replace ``AbstractUnit.ONE``
* Search ``Dimensionless.UNIT`` replace ``AbstractUnit.ONE``
* Search ``Unit.valueOf(unitString)`` replace ``Units.parseUnit(unitString)``
  
**Getting Unit instances**

If you know the unit to use at compile time, use one of the Unit instances defined as static variables in ``org.geotools.measure.Units``, ``si.uom.SI``, ``si.uom.NonSI`` or ``systems.uom.common.USCustomary``.

If you need to define new Units at runtime, it is important to immediately try to convert the new unit to one of the existing instances using Units.autocorrect method. Autocorrect applies some tolerance to locate an equivalent Unit. Skipping autocorrect will produce unexpected results and errors due to small differences in units definition.

.. code-block:: java

   // the result should be NonSI.DEGREE_ANGLE:
   Unit<?> deg = Units.autoCorrect(SI.RADIAN.multiply(0.0174532925199433));
   Unit<?> halfMetre = SI.METRE.divide(2);

.. code-block:: java

   // the result should be SI.METRE
   Unit<?> unit = Units.autocorrect(halfMetre.multiply(4).divide(2));
   
.. code-block:: java
   
   public <T extends Quantity<T>> Unit<T> deriveUnit(Unit<T>  baseUnit, double factor) {
      return Units.autocorrect(baseUnit.multiply(factor);)
   }

**Use a specific Quantity whenever possible**

This allows for type-safety checks at compile time:

.. code-block:: java

   Unit<Length> halfMetre = SI.METRE.divide(2);
   Unit<Length> stupidUnit = Units.autocorrect(halfMetre.multiply(4).divide(2));
     
**Formatting units**

Use ``org.geotools.measure.Units.toName(unit)`` to get the unit name (or unit label if name is not defined).

.. code-block:: java

   Unit<?> unit = ...
   System.out.println(Units.toName(unit)):

Use ``org.geotools.measure.Units.getDefaultFormat().format()`` to get the unit label (ignoring the name).

.. code-block:: java

   // prints "Litre"
   System.out.println(Units.toName(SI.LITRE))
   // prints "l"
   System.out.println(Units.getDefaultFormat().format(SI.LITRE))

.. code-block:: java

   // Most units don't define a name, so it does not make a difference
   // prints "m"
   System.out.println(Units.toName(SI.METRE))
   // prints "m"
   System.out.println(Units.getDefaultFormat().format(SI.METRE))
  
**Converting units**

If the unit Quantity type is known, use the type-safe getConverterTo() method:

.. code-block:: java

   Unit<Angle> unit = ...
   UnitConverter converter = unit.getConverterTo(SI.RADIAN);
   double convertedQuantity = converter.convert(3.1415);

If the Quantity type is undefined, use the convenience method ``org.geotools.measure.Units.getConverterToAny()``. Note that this method throws an IllegalArgumentException if units can't be converted:

.. code-block:: java

   Unit<?> unit = ...
   UnitConverter converter = Units.getConverterToAny(unit, SI.RADIAN);
   double convertedQuantity = converter.convert(3.1415);

**Using units**

If previously you made use of the Units in your code, to help with unit
conversion or simply to keep the units straight. You might have code like:

.. code-block:: java 

  Measure<Double, Length> dist = Measure.valueOf(distance, SI.METER);
  System.out.println(dist.doubleValue(SI.KILOMETER) + " Km");
  System.out.println(dist.doubleValue(NonSI.MILE) + " miles");

You will find it no longer compiles. It should be converted to use the ``Quantity`` classes.

.. code-block:: java

    import javax.measure.Quantity;
    import javax.measure.quantity.Length;
    import si.uom.SI;
    import systems.uom.common.USCustomary;

    import tec.uom.se.quantity.Quantities;
    import tec.uom.se.unit.MetricPrefix;

    Quantity<Length> dist = Quantities.getQuantity(distance, SI.METRE);
    System.out.println(dist.to(MetricPrefix.KILO(SI.METRE)).getValue() + " Km");
    System.out.println(dist.to(USCustomary.MILE) + " miles");

GeoTools 19.x
-------------

GeoTools is built and tested with Java 8 at this time, to use this library in a Java 9 or Java 10 environment additional JVM runtime arguments are required::

    --add-modules=java.xml.bind --add-modules=java.activation -XX:+IgnoreUnrecognizedVMOptions

These settings turn on several JRE modules that have been disabled by default in Java 9 onward.

GeoTools 15.x
-------------

GeoTools 15.x requires Java 8::

    <build>
        <plugins>
            <plugin>
                <inherited>true</inherited>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-compiler-plugin</artifactId>
                <configuration>
                    <source>1.8</source>
                    <target>1.8</target>
                </configuration>
            </plugin>
        </plugins>
    </build>

GeoTools 14.x
-------------
From 14.x version, the `JAI-EXT Project <https://github.com/geosolutions-it/jai-ext>`_ has been integrated in GeoTools. This project provides a high scalable Java API for image processing with support for NoData and ROI. 
This integration provides also the removal of the following classes, since they are now inside JAI-EXT:

* **ColorIndexer** from *gt-coverage* module;
* **GTCrop** from *gt-coverage* module;
* **GenericPiecewise** from *gt-render* module;
* **RasterClassifier** from *gt-render* module;
* **ArtifactsFilter** from *gt-imagemosaic* module.

Users may now decide to choose between JAI and JAI-EXT operations by simply using the **JAIExt** class containing utility methods for handling JAI/JAI-EXT registration.

A more detailed tutorial on how to use JAI-EXT may be found at the following :ref:`JAI-EXT Tutorial Page<jaiext>`.

TextSymbolizer provides direct access to the device independent Font list, removing deprecated array access methods. This change restores SLD 1.0 multi-lingual behaviour allowing several face/size combinations to be used during labeling.

BEFORE::

  textSymbolizer.addFont(font);
  Font[] array = textSymbolizer.getFonts();
  for(int i=0; i<array.length; i++){
      Font f = textSymbolizer.getFonts()[i];
      ...
   }
  
AFTER::
 
  textSymbolizer.fonts().add(font);
  for(Font f : textSymbolizer.fonts()){
     ...
  }

Transaction is now Closable for use with try-with-resource syntax::

   try (Transaction t = new DefaultTransaction()){
        store.setTransaction( t );
        store.addFeatures( newFeatures );
        t.commit();
   }

ShapefileDataStore representing shapefiles without any data, now return empty bounds on getBounds() instead of the bounds inside the shapefile header (mostly [0:0,0:0]). So bounds.isEmpty() and bounds.isNull() will return true for empty shapefiles.

GeoTools 13.0
-------------
As of GeoTools 13.0, the CoverageViewType classes have been removed. The AbstractDataStore class is also now deprecated. Extensive work has been done to bring in ContentDataStore as its replacement.

There is a `ContentDataStore Tutorial <http://docs.geotools.org/latest/userguide/tutorial/datastore/index.html>`_ to help with migration from AbstractDataStore.

Many readers and iterators are now Closable for use with try-with-resource syntax::

   try( SimpleFeatureIterator features = source.getFeatures( filter ) ){
       while( features.hasNext() ){
          SimpleFeature feature = features.next();
          ...
       }
   }

GeoTools 12.0
-------------
GeoTools now requires `Java 7 <http://docs.geotools.org/latest/userguide/build/install/jdk.html>`_ and this is the first release tested with OpenJDK! Please ensure you are using JDK 1.7 or newer for GeoTools 12. Both Oracle Java 7 and OpenJDK 7 are supported, tested, release targets.

Filter interfaces have been simplified. The GeoTools interfaces have been deprecated since GeoTools 2.3, and finally they have been removed. All filter interfaces now use the GeoAPI Filter.

GeoTools 11.0
-------------
Only new features were added in GeoTools 11.0.

GeoTools 10.0
-------------

.. sidebar:: Wiki

   * `GeoTools 10.0 <https://github.com/geotools/geotools/wiki/10.x>`_

   For background details on any API changes review the change proposals above.

GeoTools 10 add significant improvements in the coverage reading API.
For those migrating the first visible benefit is that referring to a generic grid coverage reader does not require anymore to use AbstractGridCoverage2DReader (an abstract class) but to the new GridCoverage2DReader interface. The old usage is still supported though, as most readers are still extending the same base class, but the usage of the interface allows for reader wrappers.

BEFORE::

  AbstractGridCoverage2DReader reader = format.getReader(source);
  
AFTER::
 
  GridCoverage2DReader reader = format.getReader(source);

GeoTools 9.0
------------

.. sidebar:: Wiki

   * `GeoTools 9.0 <https://github.com/geotools/geotools/wiki/9.x>`_

   For background details on any API changes review the change proposals above.

GeoTools 9 has resolved a long standing conflict between FeatureCollection acting as a "result" set capable of
streaming large datasets vs. acting as a familiar Java Collection. The Java 5 "for each" syntax prevents
the safe use of Iterator (as we cannot ensure it will be closed). As a result FeatureCollection no longer
can extend java Collection and is acting as a pure "result set" with streaming access provided by FeatureIterator.

ReferencedEnvelope and CRS
^^^^^^^^^^^^^^^^^^^^^^^^^^

ReferencedEnvelope has in the past only supported 2D extents, we have introduced the subclass ReferencedEnvelope3D
to support CoordinateReferenceSystems with three dimensions.

There is now a new factory method to safely construct the appropriate implementation for a provided CoordinateReferenceSystem
as shown below.

BEFORE::

  ReferencedEnvelope bbox = new ReferencedEnvelope( crs );
  ReferencedEnvelope copy = new ReferencedEnvelope( bbox );
  
AFTER::
  
  ReferencedEnvelope bbox = ReferencedEnvelope.create( crs );
  ReferencedEnvelope copy = ReferencedEnvelope.create( bbox );

This represents an *incompatible API change*, existing code using "new ReferencedEnvelope" may now throw
a RuntimeException when supplied with an incompatible CoordinateReferenceSystem.

FeatureCollection Add
^^^^^^^^^^^^^^^^^^^^^

With the FeatureCollection.add method being removed, you will need to use an explicit instance that supports
adding content.

BEFORE::

    SimpleFeatureCollection features = FeatureCollections.newCollection();

    for( SimpleFeature feature : list ){
       features.add( feature );
    }

AFTER::

    DefaultFeatureCollection features = new DefaultFeatureCollection();
    for( SimpleFeature feature : list ){
       features.add( feature );
    }

ALTERNATE (will throw exception if FeatureCollection does not implement java.util.Collection)::

    Collection<SimpleFeature> collection = DataUtilities.collectionCast( featureCollection );
    collection.addAll( list );

ALTERNATE DETAIL::

    SimpleFeatureCollection features = FeatureCollections.newCollection();
    if( features instanceof Collection ){
        Collection<SimpleFeature> collection = (Collection) features;
        collection.addAll( list );
    }
    else {
        throw new IllegalStateException("FeatureCollections configured with immutbale implementation");
    }
    
SPECIFIC::

    ListFeatureCollection features = new ListFeatureCollection( schema, list );

FeatureCollection Iterator
^^^^^^^^^^^^^^^^^^^^^^^^^^

The deprecated FeatureCollection.iterator() method is no longer available, please use FeatureCollection.features()
as shown below.

BEFORE::

  Iterator i=featureCollection.iterator();
  try {
      while( i.hasNext(); ){
         SimpleFeature feature = i.next();
         ...
      }
  }
  finally {
      featureCollection.close( i );
  }


AFTER::

    FeatureIterator i=featureCollection.features();
    try {
         while( i.hasNext(); ){
             SimpleFeature feature = i.next();
             ...
         }
    }
    finally {
         i.close();
    }

JAVA7::

    try ( FeatureIterator i=featureCollection.features()){
        while( i.hasNext() ){
             SimpleFeature feature = i.next();
             ...
        }
    }

How to Close an Iterator
^^^^^^^^^^^^^^^^^^^^^^^^

We have made FeatureIterator implement Closable (for Java 7 try-with-resource compatibility). This
also provides an excellent replacement for FeatureCollection.close( Iterator ).

If you are using any wrapping Iterators that still require the ability to close()
please consider the following approach.

BEFORE::

    Iterator iterator = collection.iterator();
    try {
       ...
    } finally {
        if (collection instanceof SimpleFeatureCollection) {
            ((SimpleFeatureCollection) collection).close(iterator);
        }
    }

QUICK::

    Iterator iterator = collection.iterator();
    try {
       ...
    } finally {
        DataUtilities.close( iterator );
    }

DETAIL::

    Iterator iterator = collection.iterator();
    try {
       ...
    } finally {
        if (iterator instanceof Closeable) {
            try {
               ((Closeable)iterator).close();
            }
            catch( IOException e){
                Logger log = Logger.getLogger( collection.getClass().getPackage().toString() );
                log.log(Level.FINE, e.getMessage(), e );
            }
        }
    }

JAVA7 using try-with-resource syntax for Iterator that implements Closeable::

    try ( Iterator i=collection.features()){
        while( i.hasNext() ){
             Object object = i.next();
             ...
        }
    }
    

GeoTools 8.0
------------

.. sidebar:: Wiki

   * `GeoTools 8.0 <https://github.com/geotools/geotools/wiki/8.x>`_

   You are encouraged to review the change proposals for GeoTools 8.0 for background information
   on the following changes.

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

Filter
^^^^^^

The filter system was upgrade to match Filter 2.0 resulting in a few additions. This mostly
effects people writing their own functions (as now we need to know about parameter types).

FeatureId
''''''''''

* BEFORE::

    FilterFactory2 ff = CommonFactoryFinder.getFilterFactory2(null);
    Filter filter;

    Set<FeatureId> selected = new HashSet<FeatureId>();
    selected.add(ff.featureId("CITY.98734597823459687235"));
    selected.add(ff.featureId("CITY.98734592345235823474"));

    filter = ff.id(selected);

* AFTER

  .. literalinclude:: /../src/main/java/org/geotools/opengis/FilterExamples.java
     :language: java
     :start-after: // id start
     :end-before: // id end

Function
''''''''

We have extended **gt-opengis** Function to make the FunctionName description (especially
argument names) more available.

* To update your code::

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

The **gt-metadata** NumberRange class is finally shedding some of its deprecated methods.

** BEFORE::

      NumberRange before = new NumberRange( 0.0, 5.0 );

** AFTER::

      NumberRange<Double> after1 = new NumberRange( Double.class, 0.0, 5.0 );
      NumberRange<Double> after2 = NumberRage.create( 0.0, 5.0 );

GeoTools 2.7
------------

.. sidebar:: Wiki

   * `GeoTools 2.7.0 <https://github.com/geotools/geotools/wiki/2.7.x>`_

   You are encouraged to review the change proposals for GeoTools 2.7.0 for background information
   on the following changes.

The changes from GeoTools 2.6 to GeoTools 2.7 focus on making your code more readable; you will
find a number of optional changes (such as using Query rather than DefaultQuery) which will
simplify make your code easier to follow.


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

We have vastly cut down the use of Java generics for casual users of the GeoTools library. The
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

GeoTools 2.6
------------

.. sidebar:: Wiki

   * `GeoTools 2.6.0 <https://github.com/geotools/geotools/wiki/2.6.x>`_

   You are encouraged to review the change proposals for GeoTools 2.6.0 for background information
   on the following changes.

The GeoTools 2.6.0 release is incremental in nature with the main change being the introduction
of the "JDBC-NG" datastores the idea of Query capabilities (so you can check what hints are
supported).

GridRange Removed
^^^^^^^^^^^^^^^^^

GridRange implementations have been removed as the result of a change we are inheriting from GeoAPI
where a swtich from GridRange to GridEnvelope has been made. GridRange comes from
Grid Coverages Implementation specification 1.0 (which is basically dead) while
GridEnvelope comes from ISO 19123 which looks like the replacement.

There is a big difference between interfaces though:

* **GridRange** treats its own maximum grid coordinates as EXCLUSIVES (like Java2D classes
  Rectangle2D, RenderedImage and Raster do); while
* **GridEnvelope** uses a different convention where maximum grid coordinates are INCLUSIVES.

This is shown in the code example below with the maxx variable.

As far as switching over to the new classes, the equivalence are as follows:

1. Replace **GridRange2D** with **GridEnvelope2D**

   Notice that now GridEnvelope2D is a Java2D rectangle and that it is also mutable!
2. Replace **GeneralGridRange** with **GeneralGridEnvelope**

There are a few more caveats, which we are showing here below.

BEFORE:

1. Use getSpan where getLength was used
2. Be EXTREMELY careful with the conventions for the inclusion/exclusion of the maximum coordinates.
3. GridRange2D IS a Rectangle and is mutable now!

   BEFORE::

        import org.geotools.coverage.grid.GeneralGridRange;
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        final GeneralGridRange originalGridRange = new GeneralGridRange(actualDim);
        final int w = originalGridRange.getLength(0);
        final int maxx = originalGridRange.getUpper(0);

        ...
        import org.geotools.coverage.grid.GridRange2D;
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        final GridRange2D originalGridRange2D = new GridRange2D(actualDim);
        final int w = originalGridRange2D.getLength(0);
        final int maxx = originalGridRange2D.getUpper(0);
        final Rectangle rect = (Rectangle)originalGridRange2D.clone();
    {code}

   AFTER::

        import org.geotools.coverage.grid.GeneralGridEnvelope;
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        final GeneralGridEnvelope originalGridRange=new GeneralGridEnvelope (actualDim,2);
        final int w = originalGridRange.getSpan(0);
        final int maxx = originalGridRange.getHigh(0)+1;

        import org.geotools.coverage.grid.GridEnvelope2D;
        final Rectangle actualDim = new Rectangle(0, 0, hrWidth, hrHeight);
        final GridEnvelope2D originalGridRange2D = new GridEnvelope2D(actualDim);
        final int w = originalGridRange2D.getSpan(0);
        final int maxx = originalGridRange2D.getHigh(0)+1;
        final Rectangle rect = (Rectangle)originalGridRange2D.clone();

OverviewPolicy Enum replace Hint use
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The hints to control overviews were deprecated and have now been removed.

The current deprecated values have been remove from the Hints class inside the Metadata module:

* VALUE_OVERVIEW_POLICY_QUALITY
* IGNORE_COVERAGE_OVERVIEW
* VALUE_OVERVIEW_POLICY_IGNORE
* VALUE_OVERVIEW_POLICY_NEAREST
* VALUE_OVERVIEW_POLICY_SPEED

You should use the enum that comes with the OverviewPolicy enum. Here below you will find a few examples:

* BEFORE::

        Hints hints = new Hints();
        hints.put(Hints.OVERVIEW_POLICY, Hints.VALUE_OVERVIEW_POLICY_SPEED);
        WorldImageReader wiReader = new WorldImageReader(file, hints);

* AFTER::

        Hints hints = new Hints();
        hints.put(Hints.OVERVIEW_POLICY, OverviewPolicy.SPEED);
        WorldImageReader wiReader = new WorldImageReader(file, hints);

Hints:

* Please, notice that the OverviewPolicy enum provide a method to get the default policy for
  overviews. The method is getDefaultPolicy().

CoverageUtilities and FeatureUtilities
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Deprecated methods have been remove from coverage utilities classes&nbsp;

We have removed deprecated methods from classes:

* CoverageUtilities.java
* FeatureUtilities.java

Existing code should change as follows:

* BEFORE::

    final FeatureCollection<SimpleFeatureType, SimpleFeature> fc=FeatureUtilities.wrapGridCoverageReader(reader)

* AFTER::

    final GeneralParameterValue[] params=...

    final FeatureCollection<SimpleFeatureType, SimpleFeature> fc=FeatureUtilities.wrapGridCoverageReader(reader,params)

Hints:

* This change allows us to store basic parameters to control how we will perform subsequent
  reads from this reader. The&nbsp; AbstractGridFormat READ_GRIDGEOMETRY2D parameter will be
  always overridden during a subsequent read.

Coverage Processing Classes
^^^^^^^^^^^^^^^^^^^^^^^^^^^

Deprecated methods have been remove from coverage processing classes:

* filteredSubsample(GridCoverage, int, int, float[], Interpolation, BorderExtender) has been removed

Here is what that looks like in code:

* BEFORE::

    public GridCoverage filteredSubsample(final GridCoverage   source,
                                          final int            scaleX,
                                          final int            scaleY,
                                          final float\[\]      qsFilter,
                                          final Interpolation  interpolation,
                                          final BorderExtender be) throws CoverageProcessingException {
         return filteredSubsample(source, scaleX, scaleY, qsFilter, interpolation);
    }

* AFTER::

    public GridCoverage filteredSubsample(final GridCoverage source,
                                          final int scaleX, final int scaleY,
                                          final float\[\] qsFilter,
                                          final Interpolation interpolation){
           // recolor(GridCoverage, Map\[\]) has been removed
           ...
    }

* BEFORE::

        recolor(final GridCoverage source, final Map[] colorMaps)

* AFTER::

        recolor(final GridCoverage source, final ColorMap[] colorMaps);
        // scale(GridCoverage, double, double, double, double, Interpolation, BorderExtender) has been removed

* BEFORE::

        scale(GridCoverage, double, double, double, double, Interpolation, BorderExtender)

* AFTER::

        scale(GridCoverage,double,double,double,double,Interpolation)
        // scale(GridCoverage, double, double, double, double, Interpolation, BorderExtender) has been removedBEFORE:

* BEFORE::

        scale(GridCoverage, double, double, double, double, Interpolation, BorderExtender)

* AFTER::

        scale(GridCoverage,double,double,double,double,Interpolation)

DefaultParameterDescriptor and Parameter
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Removed deprecated constructors from DefaultParameterDescriptor and Parameter classes.

* BEFORE::

    DefaultParameterDescriptor(Map<String,?>,defaultValue,minimum, maximum, unit, required)
    DefaultParameterDescriptor(Map<String,?>, defaultValue, minimum, maximum, required)
    DefaultParameterDescriptor(name, defaultValue, minimum, maximum)
    DefaultParameterDescriptor(name, defaultValue, minimum, maximum, unit)
    DefaultParameterDescriptor(name, remarks, defaultValue, required)
    DefaultParameterDescriptor(name, defaultValue)
    DefaultParameterDescriptor( name, valueClass, defaultValue)
    Parameter(name, value)
    Parameter(name, value, unit)
    Parameter(name, value)

* AFTER::

    DefaultParameterDescriptor.create(...)
    Parameter.create(...)

GeoTools 2.5
------------

.. sidebar:: Wiki

   * `GeoTools 2.5.0 <https://github.com/geotools/geotools/wiki/2.5.x>`_

   You are encouraged to review the change proposals for GeoTools 2.5.0 for background information
   on the following changes.

The GeoTools 2.5.0 release is a major change to the GeoTools library due to the adoption of both
Java 5 and a new feature model.

FeatureCollction
^^^^^^^^^^^^^^^^

In transitioning your code to Java 5 please be careful not use use the *for each* loop construct.
We still need to call FeatureCollection.close( iterator).

Due to this restriction (of not using *for each* loop construct we have had to make FeatureCollection
no longer Collection.

* Example (GeoTools 2.5 code)::

    FeatureCollection<SimpleFeatureType,SimpleFeature> featureCollection = feaureSource.getFeatures();
    Iterator<SimpleFeature> iterator = featureCollection.iterator();
    try {
        while( iterator.hasNext() ){
           SimpleFeature feature = iterator.next();
           ...
        }
    }
    finally {
       featureCollection.close( iterator );
    }

* Example (GeoTools 2.7 code)

  We have removed the need for the use of generics to minimize typing::

    SimpleFeatureCollection featureCollection = feaureSource.getFeatures();
    SimpleFeatureIterator iterator = featureCollection.features();
    try {
        while( iterator.hasNext() ){
           SimpleFeature feature = iterator.next();
           ...
        }
    }
    finally {
       iterator.close();
    }

JTSFactory
^^^^^^^^^^

We are cutting down on "anonymous" FactoryFinder use; creating JTSFactory to allow the
entire GeoTools library to share a JTS GeometryFactory.

* BEFORE (GeoTools 2.4 code)::

     GeometryFactory factory = new FactoryFinder().getGeometryFactory( null );

* AFTER (GeoTools 2.5 code)::

    GeometryFactory factory = JTSFactoryFinder.getGeometryFactory( null );

ProgressListener
^^^^^^^^^^^^^^^^

Transition to gt-opengis ProgressListener.

* Before (GeoTools 2.2 Code)::

    progress.setDescription( message );

* After (GeoTools 2.4 Code)::

    progress.setTask( new SimpleInternationalString( message ) );

To upgrade:

1. Search: import org.geotools.util.ProgressListener

   Replace: import org.opengis.util.ProgressListener

2. Update::

     setTask( new SimpleInternationalString( message ) ); // was setDescription( message );

SimpleFeature
^^^^^^^^^^^^^

We have (finally) made the move to an improved feature model. Please take the opportunity
to change your existing code to use *org.opengis.feature.simple.SimpleFeature*. The existing
GeoTools Feature interface is still in use; but it has been updated in
place to extend SimpleFeature.

* Before (GeoTools 2.4 Code)::

        import org.geotools.feature.FeatureType;
        ...
        CoordinateReferenceSystem crs = CRS.decode("EPSG:4326");
        final AttributeType GEOM =
            AttributeTypeFactory.newAttributeType("Location",Point.class,true, null,null,crs );
        final AttributeType NAME =
            AttributeTypeFactory.newAttributeType("Name",String.class, true );

        final FeatureType FLAG =
            FeatureTypeFactory.newFeatureType(new AttributeType[] { GEOM, NAME },"Flag");

        Feature flag1 = FLAG.create( "flag.1", new Object[]{ point, "Here" } );

        AttributeType attributes[] = FLAG.getAttributeTypes();
        AttributeType location = FLAG.getAttribute("Location");
        String label = location.getName();
        Class binding = location.getType();
        Geometry geom = flag1.getDefaultGeometry();

* After (GeoTools 2.5 Code)::

        import org.opengis.feature.simple.SimpleFeatureType;
        ...
        SimpleFeatureTypeBuilder builder = new SimpleFeatureTypeBuilder();
        builder.setName( "Flag" );
        builder.setNamespaceURI( "http://localhost/" );
        builder.setCRS( "EPSG:4326" );
        builder.add( "Location", Point.class );
        builder.add( "Name", String.class );

        SimpleFeatureType FLAG = builder.buildFeatureType();

        SimpleFeature flag1 = SimpleFeatureBuilder.build( FLAG, new Object[]{ point, "Here"}, "flag.1" );

        List<AttributeDescriptor> attributes = FLAG.getAttributes();
        AttributeDescriptor location = FLAG.getAttribute("Location");
        String label = location.getLocationName();
        Class binding = location.getType().getBinding();
        Geometry geom = (Geometry) flag1.getDefaultGeometry();

Here are some steps to start you off updating your code:

1. Search Replace

   * Search: **Feature** replace with **SimpleFeature**
   * Search: **FeatureType** replace with **SimpleFeatureType**

2. Fix the imports

   * Control-Shift-O in Eclipse IDE
   * Add casts as required for getDefaultGeometry()

3. FeatureType.create has been replaced with SimpleFeatureBuilder

   There is a static method to make the transition easier::

      SimpleFeatureFeatureBuilder.build( schema, attributes, fid );

4. For more code examples please see:

   * :doc:`/library/main/feature`

AttributeDescriptor and AttributeType
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

The concept of an AttributeType has been split into two now (allowing you to reuse common types).

* BEFORE (GeoTools 2.4 Code)::

    import org.geotools.feature.AttributeType;
    ...
    GeometryAttributeType att =
              (GeometryAttributeType) AttributeTypeBuilder.newAttributeDescriptor(geomTypeName,
                                                                                  targetGeomType,
                                                                                  isNillable,
                                                                                  Integer.MAX_VALUE,
                                                                                  Collections.EMPTY_LIST,
                                                                                  crs );

* AFTER (GeoTools 2.5 Code)::

    import org.geotools.feature.AttributeTypeBuilder;
    import org.opengis.feature.type.AttributeDescriptor
    ...
    AttributeTypeBuilder build = new AttributeTypeBuilder();
    build.setName( geomTypeName );
    build.setBinding( targetGeomType );
    build.setNillable(true);
    build.setCRS(crs);
    GeometryType type = build.buildGeometryType();
    GeometryDescriptor attribute = build.buildDescriptor( geomTypeName, type );

Name
^^^^

In order to better support app-schema work we can no longer assume names are a simple String. The
**Name** class has been introduced to make this easier and is availble
throughout the library: example FeatureSource.getName().

* BEFORE  (GeoTools 2.4 Code)::

    DataStore ds = ...
    String []typeNames = ds.getTypeNames();
    SimpleFeatureType type = ds.getSchema(typeNames[0]);
    assert type.getTypeName() == typeNames[0];
    FeatureSource source = ds.getFeatureSource(type.getTypeName());

* AFTER  (GeoTools 2.5 Code)::

    import org.opengis.feature.type.Name;
    ...

    DataStore ds = ...
    List<Name> featureNames = ds.getNames();
    SimpleFeatureType type = ds.getSchema(featureNames.get(0));
    // type.getName() may or may not be equal to featureNames.get(0), assume not. If they're its just an implementation detail.
    FeatureSource source = ds.getFeatureSource(featureNames.get(0));

DataStore
^^^^^^^^^

Transition to use of Java 5 Generics with DataStore API.

.. tip

   We have removed the need to use Generics in GeoTools 2.7 allowing the use of
   SimpleFeatureSource, SimpleFeatureCollection, SimpleFeatureStore etc.

* BEFORE  (GeoTools 2.4 Code)::

    DataStore ds = ...
    FeatureSource source = ds.getSource(typeName);
    FeatureStore store = (FeatureStore)source;
    FeatureLocking locking = (FeatureLocking)source;

    FeatureCollection collection = source.getFeatures();
    FeatureIterator features = collection.features();
    while(features.hasNext){
      SimpleFeature feature = features.next();
    }

    Transaction transaction = Transaction.AUTO_COMMIT;
    FeatureReader reader = ds.getFeatureReader(new DefaultQuery(typeName), transaction);
    FeatureWriter writer = ds.getFeatureWriter(typeName, transaction);

* AFTER  (GeoTools 2.5 Code)::

    DataStore ds = ...
    FeatureSource<SimpleFeatureType,SimpleFeature> source = ds.getSource(typeName);
    FeatureStore<SimpleFeatureType,SimpleFeature> store = (FeatureStore<SimpleFeatureType,SimpleFeature>)source;
    FeatureLocking<SimpleFeatureType,SimpleFeature> locking = (FeatureLocking<SimpleFeatureType,SimpleFeature>)source;

    FeatureCollection<SimpleFeatureType,SimpleFeature> collection = source.getFeatures();
    FeatureIterator<SimpleFeatureType,SimpleFeature> features = collection.features();
    while(features.hasNext){
       SimpleFeature feature = features.next();
    }
    Transaction transaction = Transaction.AUTO_COMMIT;
    FeatureReader<SimpleFeatureType,SimpleFeature> reader = ds.getFeatureReader(new DefaultQuery(typeName), transaction);
    FeatureWriter<SimpleFeatureType,SimpleFeature> writer = ds.getFeatureWriter(typeName, transaction);

* AFTER (GeoTools 2.7 Code)::

    DataStore ds = ...
    SimpleFeatureSource<SimpleFeatureType,SimpleFeature> source = ds.getSource(typeName);
    SimpleFeatureStore store = (SimpleFeatureStore) source;
    SimpleFeatureLocking locking = (SimpleFeatureLocking) source;

    SimpleFeatureCollection collection = source.getFeatures();
    SimpleFeatureIterator features = collection.features();
    while(features.hasNext){
       SimpleFeature feature = features.next();
    }
    Transaction transaction = Transaction.AUTO_COMMIT;
    FeatureReader<SimpleFeatureType,SimpleFeature> reader = ds.getFeatureReader(new DefaultQuery(typeName), transaction);
    FeatureWriter<SimpleFeatureType,SimpleFeature> writer = ds.getFeatureWriter(typeName, transaction);

DataAccess and DataStore
^^^^^^^^^^^^^^^^^^^^^^^^

* The DataAcess super class has been introduced, leaving DataStore to *only* work with SimpleFeature
  capable implementations.::

    import org.opengis.feature.type.Name;
    ...

    java.util.Map paramsMap = ...
    DataStore ds = DataStoreFinder.getDataStore(paramsMap);
    Name featureName = new org.geotools.feature.Name(namespace, localName);
    FeatureSource<SimpleFeatureType, SimpleFeature> source = ds.getSource(featureName);
    FeatureStore<SimpleFeatureType, SimpleFeature> store = (FeatureStore)source;
    FeatureLocking<SimpleFeatureType, SimpleFeature> locking = (FeatureLocking)source;

    FeatureCollection<SimpleFeatureType, SimpleFeature> collection = source.getFeatures();
    FeatureIterator<SimpleFeature> features = collection.features();
    while(features.hasNext){
     SimpleFeature feature = features.next();
    }

    Transaction transaction = Transaction.AUTO_COMMIT;
    FeatureReader<SimpleFeatureType, SimpleFeature> reader = ds.getFeatureReader(new DefaultQuery(typeName), transaction);
    FeatureWriter<SimpleFeatureType, SimpleFeature> writer = ds.getFeatureWriter(typeName, transaction);

* DataAccess: works both with SimpleFeature and normal Feature capable implementations::

    import org.opengis.feature.FeatureType;
    import org.opengis.feature.Feature;
    import org.opengis.feature.type.Name;
    ...

    java.util.Map paramsMap = ...
    DataAccess<FeatureType, Feature> ds = DataAccessFinder.getDataAccess(paramsMap);
    Name featureName = new org.geotools.feature.Name(namespace, localName);
    FeatureSource<FeatureType, Feature> source = ds.getSource(featureName);
    FeatureStore<FeatureType, Feature> store = (FeatureStore)source;
    FeatureLocking<FeatureType, Feature> locking = (FeatureLocking)source;

    FeatureCollection<FeatureType, Feature> collection = source.getFeatures();
    FeatureIterator<Feature> features = collection.features();
    while(features.hasNext){
     Feature feature = features.next();
    }
    //No DataAccess.getFeatureReader/Writer

GeoTools 2.4
------------

.. sidebar:: Wiki

   * `GeoTools 2.4.0 <https://github.com/geotools/geotools/wiki/2.4.x>`_

   You are encouraged to review the change proposals for GeoTools 2.4.0 for background information
   on the following changes.

The GeoTools 2.4.0 release is a major change to the GeoTools library due to the adoption of geoapi
Filter model. This new filter model is immutable making it impossible to modify filters that
have already been constructed; in trade it is threadsafe.

The following is needed when upgrading to 2.4.

ReferencingFactoryFinder
^^^^^^^^^^^^^^^^^^^^^^^^

Rename FactoryFinder to ReferencingFactoryFinder

* BEFORE (GeoTools 2.2 Code)::

    CRSFactory factory = FactoryFinder.getCSFactory( null );

* AFTER (GeoTools 2.4 Code)::

    CRSFactory factory = ReferencingFactoryFinder.getCSFactory( null );

FeatureStore addFeatures
^^^^^^^^^^^^^^^^^^^^^^^^

The use of FeatureReader has been removed from the FeatureStore API.

* Before (GeoTools 2.2 Code)::

    featureStore.addFeatures( DataUtilities.reader( collection )); // add FeatureCollection
    featureStore.addFeatures( DataUtilities.reader(array)); // add Feature[]
    featureStore.addFeatures( DataUtilities.reader(feature )); // add Feature
    featureStore.addFeatures( reader );

* After (GeoTools 2.4 Code)::

    featureStore.addFeatures( collection ); // add FeatureCollection
    featureStore.addFeatures( DataUtilities.collection( array ) ); // add Feature[]
    featureStore.addFeatures( DataUtilities.collection( feature )); // add Feature
    featureStore.addFeatures( DataUtilities.collection( reader )); // add FeatureReader

Note:

* DataUtilities.collection( reader ) will currently load the contents into memory, if you have
  any volunteer time a "lazy" implementation would be helpful.

FeatureSource getSupportedHints
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We added a getSupportedHints() method that can be used to check which Query hints are supported
by a certain FeatureSource. If your FeatureSource does not intend to leverage query hints, just
return an empty set.

* After (GeoTools 2.4 Code)::

    /**
     * By default, no Hints are supported
     */
    public Set getSupportedHints() {
        return Collections.EMPTY_SET;
    }

Query getHints
^^^^^^^^^^^^^^

We have added the method Query.getHints() allow users to pass in hints to control the query
process.

If you have a Query implementation other than DefaultQuery you'll need to add the getHints() method.
The default implementation, if you don't plan to leverage hints, can just return an
empty Hints object.

* After (GeoTools 2.4 Code)::

    /**
     * Returns an empty Hints set
     */
    public Hints getHints() {
        return new Hints(Collections.EMPTY_MAP);
    }

Filter
^^^^^^

We have completed the transition to GeoAPI Filter.

* Before (GeoTools 2.2 Code)::

    package org.geotools.filter;

    import junit.framework.TestCase;

    import org.geotools.filter.LogicFilter;
    import org.geotools.filter.FilterFactory;
    import org.geotools.filter.Filter;

    public class FilterFactoryBeforeTest extends TestCase {

        public void testBefore() throws Exception {
            FilterFactory ff = FilterFactoryFinder.createFilterFactory();

            CompareFilter filter = ff.createCompareFilter(Filter.COMPARE_GREATER_THAN);
            filter.addLeftValue( ff.createLiteralExpression(2));
            filter.addRightValue( ff.createLiteralExpression(1));

            assertTrue( filter.contrains( null ) );
            assertTrue( filter.getFilterType() == FilterType.COMPARE_GREATER_THAN );
            assertTrue( Filter.NONE != filter );
        }
    }

* AFTER (Quick GeoTools 2.3 Code)::

    public void testQuick() throws Exception {
        FilterFactory ff = FilterFactoryFinder.createFilterFactory();

        CompareFilter filter = ff.createCompareFilter(FilterType.COMPARE_GREATER_THAN);
        filter.addLeftValue( ff.createLiteralExpression(2));
        filter.addRightValue( ff.createLiteralExpression(1));

        assertTrue( filter.evaluate( null ) );
        assertTrue( Filters.getFilterType( filter ) == FilterType.COMPARE_GREATER_THAN);
        assertTrue( Filter.INCLUDE != filter );
    }

Here are the steps to follow to update your own code:

1. Substitute.

   =================================== =================================================
   Search                              Replace
   =================================== =================================================
   import org.geotools.filter.Filter;  import org.opengis.filter.Filter;
   import org.geotools.filter.SortBy;  import org.opengis.filter.sort.SortBy;
   Filter.NONE                         Filter.INCLUDE
   Filter.ALL                          Filter.EXCLUDE
   AbstractFilter.COMPARE              FilterType.COMPARE
   Filter.COMPARE                      FilterType.COMPARE
   Filter.GEOMETRY                     FilterType.GEOMETRY
   Filter.LOGIC                        FilterType.LOGIC
   =================================== =================================================

2. FilterType is no longer supported directly.

   BEFORE:

      int type = filter.getFilterType();

   AFTER:

      int type = Filters.getFilterType( filter );

3. You can no longer chain filters together.

   BEFORE::

     filter = filter.and( other )

   AFTER::

     filter = filterFactory.and( filter, other );

4. We have provided an adaptor for your old filter visitors.

   BEFORE::

     filter.accept( visitor )

   AFTER::

     Filters.accept( filter, visitor );

3. Update your code to use the new factory methods.

   BEFORE::

     filter = filterFactory.createCompareFilter(FilterType.COMPARE_EQUALS)
     filter.setLeftGeoemtry( expr1 );
     filter.setRightGeometry( expr3 );

   AFTER::

     filter = FilterFactory.equals(expr1,expr);

4. Literals cannot be modified once created.

   BEFORE::

     Literal literal = filterFactory.createLiteral();
     literal.setLiteral( obj );

   AFTER::

     Filter filter = filterFactory.literal( obj );

5. Property name support.

   BEFORE::

     filter = = filterFac.createAttributeExpression(schema, "name");

   AFTER::

     Filter filter = filterFactory.property(name);

h4. After (GeoTools 2.4 Code)::

        public void testAfter() throws Exception {
            FilterFactory ff = CommonFactoryFinder.getFilterFactory(null);

            Expression left = ff.literal(2);
            Expression right = ff.literal(2);
            PropertyIsGreaterThan filter = ff.greater( left, right );

            assertTrue( filter.evaluate( null ) );
            assertTrue( Filter.INCLUDE != filter );
        }

1. Substitute


   =============================================== ===================================================
   Search                                          Replace
   =============================================== ===================================================
   import org.geotools.filter.FilterFactory;       import org.opengis.filter.FilterFactory;
   FilterFactoryFinder.createFilterFactory()       CommonFactoryFinder.getFilterFactory(null);
   import org.geotools.filter.FilterFactoryFinder; import org.geotools.factory.CommonFactoryFinder
   import org.geotools.filter.CompareFilter;       import org.geoapi.spatial.BinaryComparisonOperator
   CompareFilter                                   BinaryComparisonOperator
   =============================================== ===================================================

2. Update code to use evaulate.

   BEFORE::

      if( filter.contains( feature ){

   AFTER::

      if( filter.evaluate( feature ){

3. Update code to use instanceof checks.

   BEFORE::

       if( filter.getFilterType() == FilterType.GEOMETRY_CONTAIN ) {

   AFTER::

       if( filter instanceof Contains ){


Note regarding different Geometries

* Geotools was formally limited to only JTS Geometry
* GeoTools filter nows can take either JTS Geometry or ISO Geometry

* If you need to convert from one to the other::

     JTSUtils.jtsToGo1(p, CRS.decode("EPSG:4326"));

Feature.getParent removed
^^^^^^^^^^^^^^^^^^^^^^^^^

The feature.getParent() method have been deprecated as a mistake and has now been removed.

* BEFORE (GeoTools 2.0 Code)::

    public void example( FeatureSource source ){
        FeatureCollection features = source.getFeatures();
        Iterator i = features.iterator();
        try {
            while( i.hasNext() ){
                  Feature feature = (Feature) i.next();
                  System.out.println( precentBoxed( feature ));
            }
        }
        finally {
            features.close( i );
        }
    }
    private double precentBoxed( Feature feature ){
         Envelope context = feature.getParent().getBounds();
         Envelope bbox = feature.getBounds();
         double boxedContext = context.width * context.height;
         double boxed = bbox.width * bbox.height;
         return (boxed / boxedContext) * 100.0
    }

* AFTER (GeoTools 2.2 Code)::

    public void example( FeatureSource source ){
        FeatureCollection features = source.getFeatures();
        Iterator i = features.iterator();
        try {
            while( i.hasNext() ){
                  Feature feature = (Feature) i.next();
                  System.out.println( precentBoxed( feature, features ));
            }
        }
        finally {
            features.close( i );
        }
    }
    private double precentBoxed( Feature feature, FeatureCollection parent ){
         Envelope context = parent.getBounds();
         Envelope bbox = feature.getBounds();
         double boxedContext = context.width * context.height;
         double boxed = bbox.width * bbox.height;
         return (boxed / boxedContext) * 100.0
    }

Notes:

* you will have to make API changes to pass the intended parent collection in

This is a mistake with the previous feature model (for a feature can exist in more then one
collection) and we apologise for the inconvenience.

Split Classification Expressions

The biggest user of the feature.getParent() mistake was the implementation of classification
functions. You will now need to split up these expressions into two parts.

* BEFORE (GeoTools 2.3):

  1. equal_interval( SPEED, 12 )
  2. uses getParent() internally to produce classification on feature collection;
  3. then checks which category each feature falls into

  Notes:

  * please note the above code depends on getParent(), so it is not safe even for GeoTools 2.3 (as some features have a null parent).

* AFTER (GeoTools 2.4):

  Apply the aggregation function to the feature collection:

  1. equalInterval( SPEED, 12 )
  2. produce classification on provided feature collection
  3. Construct a slot expression using the resulting literal::

        classify( SPEED, {0} )

  4. uses literal classification from step one

GTRenderer
^^^^^^^^^^

The GTRender interface was produced as a neutral ground for client code; traditional users of
LiteRenderer and LiteRenderer2 are asked to move to the implementation of GTRenderer called
StreamingRenderer.

* BEFORE (GeoTools 2.1):

  How to paint to an *outputArea* Rectangle::

    LiteRenderer2 draw = new LiteRenderer2(map);

    Envelope dataArea = map.getLayerBounds();
    AffineTransform transform = renderer.worldToScreenTransform(dataArea, outputArea);

    draw.paint(g2d, outputArea, transform);

* QUICK (GeoTools 2.2)

  How to paint to an *outputArea* Rectangle::

    StreamingRenderer draw = new StreamingRenderer();
    draw.setContext(map);

    draw.paint(g2d, outputArea, map.getLayerBounds() );

* BEST PRACTICE (GeoTools 2.2)::

    GTRenderer draw = new StreamingRenderer();
    draw.setContext(map);

    draw.paint(g2d, outputArea, map.getLayerBounds() );

  By letting your code depend only on the GTRenderer interface you can experiment with
  alternative implementations to find the best fit.

JTS
^^^

Swap moved to JTS utility class.

* BEFORE (GeoTools 2.1)::

    import org.geotools.geometry.JTS;
    import org.geotools.geometry.JTS.ReferencedEnvelope

* AFTER (GeoTools 2.2)::

    import org.geotools.geometry.jts.JTS;
    import org.geotools.geometry.jts.ReferencedEnvelope

JTS to Shape converters
^^^^^^^^^^^^^^^^^^^^^^^

Swap to moved Renderer JTS-to-Shape converters.

* BEFORE (GeoTools 2.3)::

    import org.geotools.renderer.lite.LiteShape;
    import org.geotools.renderer.lite.LiteShape2;
    import org.geotools.renderer.lite.PackedLineIterator;
    import org.geotools.renderer.lite.PointIterator;
    import org.geotools.renderer.lite.PolygonIterator;
    import org.geotools.renderer.lite.LineIterator;
    import org.geotools.renderer.lite.LineIterator2;
    import org.geotools.renderer.lite.Decimator;
    import org.geotools.renderer.lite.AbstractLiteIterator;
    import org.geotools.renderer.lite.TransformedShape;
    import org.geotools.renderer.lite.LiteCoordinateSequence;
    import org.geotools.renderer.lite.LiteCoordinateSequenceFactory;
    import org.geotools.renderer.lite.LiteCoordinateSequence;

* AFTER (GeoTools 2.4)::

    import org.geotools.geometry.jts.LiteShape;
    import org.geotools.geometry.jts.LiteShape2;
    import org.geotools.geometry.jts.PackedLineIterator;
    import org.geotools.geometry.jts.PointIterator;
    import org.geotools.geometry.jts.PolygonIterator;
    import org.geotools.geometry.jts.LineIterator;
    import org.geotools.geometry.jts.LineIterator2;
    import org.geotools.geometry.jts.Decimator;
    import org.geotools.geometry.jts.AbstractLiteIterator;
    import org.geotools.geometry.jts.TransformedShape;
    import org.geotools.geometry.jts.LiteCoordinateSequence;
    import org.geotools.geometry.jts.LiteCoordinateSequenceFactory;
    import org.geotools.geometry.jts.LiteCoordinateSequence;

Coverage utility classes
^^^^^^^^^^^^^^^^^^^^^^^^

Swap to moved Coverage utility classes.

* BEFORE (GeoTools 2.3)::

    import org.geotools.data.coverage.grid.*
    import org.geotools.image.imageio.*

  Wrapping a GridCoverage into a feature in 2.3::

    org.geotools.data.DataUtilities#wrapGc(GridCoverage gridCoverage)
    org.geotools.data.DataUtilities#wrapGcReader(
                AbstractGridCoverage2DReader gridCoverageReader,
                GeneralParameterValue[] params)

  GridCoverageExchange Utility classes in 2.3::

    org.geotools.data.coverage.grid.file.*
    org.geotools.data.coverage.grid.stream .*

  org.geotools.coverage.io classes in 2.3::

    org.geotools.coverage.io.AbstractGridCoverageReader.java,
    org.geotools.coverage.io.AmbiguousMetadataException.java,
    org.geotools.coverage.io.ExoreferencedGridCoverageReader.java,
    org.geotools.coverage.io.MetadataBuilder.java,
    org.geotools.coverage.io.MetadataException.java,
    org.geotools.coverage.io.MissingMetadataException.java

* AFTER (GeoTools 2.4)::

    import org.geotools.coverage.grid.io.*
    import  org.geotools.coverage.grid.io.imageio.*

  Wrapping a GridCoverage into a feature in 2.4::

    org.geotools.referencing.util.coverage.CoverageUtilities #wrapGc(GridCoverage gridCoverage)
    org.geotools.referencing.util.coverage.CoverageUtilities #wrapGcReader(
                AbstractGridCoverage2DReader gridCoverageReader,
                GeneralParameterValue[] params)

  GridCoverageExchange Utility classes in 2.4.

  The classes have been dismissed since apparently nobody was using. If needed
  we can reintroduce them as deprecated.

  org.geotools.coverage.io classes in 2.4.

  These classes have been moved to spike/exoreferenced waiting for Martin to review and merge into
  org.geotools.coverage.grid.io package

spatialschema
^^^^^^^^^^^^^

Renamed spatialschema to geometry.

* Do you know what **spatialschema** was? We did not find it clear either.

  Renamed to **geometry**?

* BEFORE::

    import org.opengis.spatialschema.geometry;
    import org.opengis.spatialschema.geometry.aggregate;
    import org.opengis.spatialschema.geometry.complex;
    import org.opengis.spatialschema.geometry.geometry;
    import org.opengis.spatialschema.geometry.primitive;

* AFTER::

    import org.opengis.geometry;
    import org.opengis.geometry.aggregate;
    import org.opengis.geometry.complex;
    import org.opengis.geometry.coordinate;
    import org.opengis.geometry.primitive;

Repackage ArcSDE
^^^^^^^^^^^^^^^^

Repackage arcsde datastore.

* BEFORE::

    import org.geotools.data.arcsde.ArcSDEDataStoreFactory;

* AFTER::

    import org.geotools.arcsde.ArcSDEDataStoreFactory;

WorldImage
^^^^^^^^^^

Sets of WorldImage extensions. Changed from a single String to a Set<String> .. because
one wld is not enough?

* BEFORE::

    private File toWorldFile(String fileRoot, String fileExt){
        File worldFile = new File( fileRoot + ".wld" );
        if( worldFile.exists() ){
            return worldFile;
        }
        String ext = WorldImageFormat.getWorldExtension( fileExt );
        File otherWorldFile = new File( fileRoot + ext );
        if( otherWorldFile.exists() ){
            return otherWorldFile;
        }
        return null;
    }

* AFTER::

     private File toWorldFile(String fileRoot, String fileExt){
        Set<String> other = WorldImageFormat.getWorldExtension( fileExt );
        File worldFile = new File( fileRoot + ".wld" );
        if( worldFile.exists() ){
            return worldFile;
        }
        for( String ext : other ){
            File otherWorldFile = new File( fileRoot + ext );
            if( otherWorldFile.exists() ){
                return otherWorldFile;
            }
        }
        return null;
    }
