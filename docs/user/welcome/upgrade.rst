Upgrade
=======

With a library as old as GeoTools you will occasionally run into a project from ten years ago that
needs to be upgraded. This page collects the upgrade notes for each release change; highlighting any
fundemental changes with code examples showing how to upgrade your code.

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

GeoTools 9.0
------------

.. sidebar:: Wiki
   
   * `GeoTools 9.0 <http://docs.codehaus.org/display/GEOTOOLS/9.x>`_
   
   For background details on any API changes review the change proposals above.

GeoTools 9 has resolved a long standing conflict between FeatureCollection acting as a "result" set capable of
streaming large datasets vs. acting as a familiar Java Collection. The Java 5 "for each" syntax prevents
the safe use of Iterator (as we cannot ensure it will be closed). As a result FeatureCollection no longer
can extend java Collection and is acting as a pure "result set" with streaming access provided by FeatureIterator.

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

	TreeSetFeatureCollection features = new TreeSetFeatureCollection();
	for( SimpleFeature feature : list ){
	   features.add( feature );
	}

ALTERNATE::

	SimpleFeatureCollection features = FeatureCollections.newCollection();
	if( features instanceof Collection ){
	    Collection<SimpleFeature> collection = (Collection) features;
	    collection.addAll( list );
	}
	else {
	    throw new IllegalStateException("FeatureCollections configured with immutbale implementation");
	}

        
FeatureCollection Iterator
^^^^^^^^^^^^^^^^^^^^^^^^^^

The deprecated FeatureCollection.iterator() method is no longer available, please use FeatureCollection.features()
as shown below.

BEFORE:
   
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


FeatureCollection close method
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

We have made FeatureCollection implement closable (for Java 7 try-with-resource compatibility). This
also provides an excellent replacement for FeatureCollection.close( Iterator ).

BEFORE::

        Iterator iterator = collection.iterator();
        try {
           ...
        } finally {
            if (collection instanceof SimpleFeatureCollection) {
                ((SimpleFeatureCollection) collection).close(iterator);
            }
        }

AFTER::

        Iterator iterator = collection.iterator();
        try {
           ...
        } finally {
			if (iterator instanceof Closeable) {
                try {
                    ((Closeable) iterator).close();
                } catch (IOException e) {
                    LOGGER.log(Level.FINE, e.getMessage(), e);
                }
            }
        }


GeoTools 8.0
------------

.. sidebar:: Wiki
   
   * `GeoTools 8.0 <http://docs.codehaus.org/display/GEOTOOLS/8.x>`_
   
   You are encourged to review the change proposals for GeoTools 8.0 for background information
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

GeoTools 2.7
------------

.. sidebar:: Wiki
   
   * `GeoTools 2.7.0 <http://docs.codehaus.org/display/GEOTOOLS/2.7.x>`_
   
   You are encourged to review the change proposals for GeoTools 2.7.0 for background information
   on the following changes.
   
The changes from GeoTools 2.6 to GeoTools 2.7 focus on making your code more readible; you will
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

GeoTools 2.6
------------

.. sidebar:: Wiki
   
   * `GeoTools 2.6.0 <http://docs.codehaus.org/display/GEOTOOLS/2.6.x>`_
   
   You are encourged to review the change proposals for GeoTools 2.6.0 for background information
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
2. Be EXTREMELY careful with the convetions for the inclusion/exclusion of the maximum coordinates.
3. GridRange2D IS a Ractangle and is mutable now!
   
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
  always overriden during a subsequent read.

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
   
   * `GeoTools 2.5.0 <http://docs.codehaus.org/display/GEOTOOLS/2.5.x>`_
   
   You are encourged to review the change proposals for GeoTools 2.5.0 for background information
   on the following changes.

The GeoTools 2.5.0 release is a major change to the GeoTools library due to the adoption of both
Java 5 and a new feature model.

FeatureCollction
^^^^^^^^^^^^^^^^

In transitioning your code to Java 5 please be careful not use use the *for each* loop construct.
We still need to call FeatureCollection.close( iterator).

Due to this restriction (of not using *for each* loop construct we have had to make FeatureCollection
no longer Colection.

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
        CoordianteReferenceSystem crs = CRS.decode("EPSG:4326");
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
   
   * `GeoTools 2.4.0 <http://docs.codehaus.org/display/GEOTOOLS/2.4.x>`_
   
   You are encourged to review the change proposals for GeoTools 2.4.0 for background information
   on the following changes.

The GeoTools 2.4.0 release is a major change to the GeoTools library due to the adoption of geoapi
Filter model. This new fileter model is immutable making it impossible to modify filters that
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

The use of FeatureReader has been revmoved from the FeatureStore API.

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
            assertTrue( Fitler.INCLUDE != filter );
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
collection) and we appologize for the inconvience.

Split Classification Expressions

The biggest user of the feature.getParent() mistake was the implementation of classificaiton
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

The GTRender interface was produced as a nuetral ground for client code; traditional users of
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
  
    org.geotools.resources.coverage.CoverageUtilities #wrapGc(GridCoverage gridCoverage)
    org.geotools.resources.coverage.CoverageUtilities #wrapGcReader(
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
