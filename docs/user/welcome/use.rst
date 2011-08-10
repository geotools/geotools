How to Use GeoTools
===================

GeoTools provides an excellent series of tutorials to help you get started:

* :doc:`Quickstart </tutorial/quickstart/index>`

Adding GeoTools to your Application
-----------------------------------

Maven (recommended):
   The quickstart is writen with the maven build system in mind. The maven build
   system is very good at sorting out dependencies between lots of little jars - and
   is the recommended way to start using GeoTools.
   
   Both Eclipse and NetBeans offer maven integration, for details please review
   the Eclipse Quickstart and NetBeans quickstart.
   
   Using maven in concert with your IDE, and looking over the pictures on this page is
   recommended.

Download:
   Traditionally users just dump all the jars into their IDE and have a go, please be
   advised that some of the jars will be in conflict.
   
   1. Dump everything from a binary distribution of GeoTools into your IDE
   2. Remove all the jars that say epsg in them - except for the gt2-epsg jar.
   3. Ensure your JRE has JAI and ImageIO if you are doing raster work
   4. Ignore the rest of this page   
   
   For detailed step-by-step instructions review the Eclipse quickstart and
   Netbeans quickstart. Instructions for downloading and selecting jars
   are provided at the end of the document as an alternative.


Module Matrix
-------------

The GeoTools library is live and online! So you can check up on modules, plugins and
extensions you are about to use:

* http://docs.codehaus.org/display/GEOTOOLS/Module+Matrix

This page includes a description of how good each module is (more stars is better). If there
are any serious problems (ie a red star) you may want to click on the module name to find out
more information.

The Module Matrix also lists unsupported modules allowing you to check on current status.

Public API
----------

As an open source library you are free to call any of the GeoTools classes needed for your
application to be delivered on time.

However, GeoTools offers a clean approach which should cause the least amount of disruption
to your code during library upgrades as GeoTools matures.

GeoTools cleanly separates out several groups of application programming interfaces (API)
from the internal implementation of the library.

If you write your code against these interfaces they offer a stable target as the library
upgrades. In the event these interfaces are changed (sometimes standards change on us) the
interfaces will be deprecated for a single release cycle allowing you a chance to upgrade.

Interfaces for Concepts and Ideas
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Formally these interfaces come from three locations:

* gt-opengis - interfaces backed by ISO and OGC standards (as explained in the Use of Standards page).
* jts topology suite - geometry library implementing the Simple Features for SQL (SFSQL) OGC standard.
* gt-api - interfaces provided by GeoTools.

We also have one stalled work in progress:

* gt-opengis has a set of ISO19107 geometry interfaces (anyone interested in curves and 3D?)

These interfaces represent spatial concepts and data structures in your application and are suitable
for use in method signatures.

Classes for Implementation
^^^^^^^^^^^^^^^^^^^^^^^^^^

While interfaces are used to represent the data structures employed by GeoTools, we also provide
public classes to get the job done.

Public classes are provided for the purpose of:

* Utility classes to make things easier to work with. Examples are the CQL, DataUtilities and JTS
  classes. Each of these provide public methods to help you make the most of the services provided
  by GeoTools.
* Helping glue the library together at runtime - an example is the FactoryFinders which allow you
  to look up available implementations on the CLASSPATH.
* GeoTools "Extensions" provide additional services on top of the library and require additional
  public classes to make this happen. An example is the ColorBrewer class provied by gt-brewer.

You can make use of public classes in these modules directly, in all cases they are utilities to
get the job done. These classes are suitable for use in your import section. There is no need to
use these classes in your method signatures as they do not represent data structures.

Factories for Creation
^^^^^^^^^^^^^^^^^^^^^^

Interfaces only define what a data structure should look like, and do not provide a way to create
an object. In Java the work around is to provide a "factory" that provides "create" methods which
you can use instead of **new**.

GeoTools provides Factory classes allowing you to create the various objects used by the library,
such as Features, Styles, Filters, CoordinateReferencingSystems, and DataStores.

GeoTools provides a FactoryFinder system to locate the available factory implementations on the
CLASSPATH. By using a FactoryFinder your code can be built to function using only interfaces.

For more information review the page How to Create Something which outlines how to locate an
appropriate implementation at runtime.

Separation of Concerns
''''''''''''''''''''''

While you could find and use each of the various Factory implementations directly this would
introduce a **dependency** between your code and that exact implementation. This idea of
depending on a specific implementation makes your code brittle with respect to change, and prevents
you from taking advantage of a better implementation when it is made available in the future.

Bad practice with direct dependency on ShapeFileDataStoreFactory::
   
   ShapefileDataStoreFactory factory = new ShapefileDataStoreFactory();
   ShapeFileDataStore = factory.createDataStore( file );

This code example would have been fine for GeoTools 2.1, however for GeoTools 2.2 an "indexed"
shapefile datastore was created with far better performance. The factory would be smart enough
to create an IndexedShapeFileDataStore if an index file was available.

Here is a replacement that allows GeoTools to return an indexed datastore if one is available::
   
    DataStore dataStore = DataStoreFinder.getDataStore( file );

The DataStoreFinder class looks up all the DataStoreFactory implementations available on the
CLASSPATH and sorts out which one can make a DataStore to access the provided file.

How to Create
-------------

Code leveraging GeoTools usually works against the Java interfaces only but interfaces in Java
don't provide any way to create actual objects. GeoTools therefore provides Factories which are
concrete implementations through whose interface users can create actual GeoTools objects such as
Features, Styles, Filters, DataStores, and MathTransforms.

This page explains how to use the FactoryFinder system to find the appropriate Factory
implementations to instantiate particular objects. The section page will show alternative approaches
to obtain and use a particular implementation of an appropriate DataStore interface; those examples
show the utility of the FactoryFinder system.

Creating in GeoTools
^^^^^^^^^^^^^^^^^^^^

To create an implementation (and not get your hands dirty by depending on a specific class) Java
developers are asked to use a Factory. Other languages like scala allow the definition of a
constructors as part of the interface itself.

In GeoTools we use a "FactoryFinder" to look for a factory implementation on the classpath.

Here is a quick example showing how to create and use a Filter::
  
  FilterFactory2 factory = CommonFactoryFinder.getFilterFactory2( null );
  Filter filter = factory.less( factory.property( "size" ), factory.literal( 2 ) );
  
  if( filter.evaulate( feature )){
     System.out.println( feature.getId() + " had a size larger than 2" );
  }

In this example we:

1. Found an object which implements the GeoAPI FilterFactory2 interface using a FactoryFinder.
   
   (CommonFactoryFinder gave us FilterFactoryImpl in this case)
2. Used the Factory to produce our Instance.
   
   (FilterFactoryImpl.less(..) method was used to create a PropertyIsLessThan Filter)
3. Used the instance to accomplish something.
  
   (we used the filter to check the size of a Feature )

FactoryFinder Reference
^^^^^^^^^^^^^^^^^^^^^^^

There is a loose naming convention where we try and have a clear progression from interface name,
factory name to factory finder name.

However in practice we found it useful to gather many of the common factories together into a
common class for lookup.

CommonFactoryFinder

* FilterFactory
* StyleFactory
* Function
* FeatureLockFactory
* FileDataStore - factory used to work with file datastores
* FeatureFactory - factory used to create features
* FeatureTypeFactory - factory used to create feature type description
* FeatureCollections - factory used to create feature collection

For access to feature (ie vector) information:

* DataAccessFinder - listing DataAccessFactory for working with feature data
* DataStoreFinder - lists DataStoreFactorySpi limited to simple features
* FileDataStoreFinder - Create of FileDataStoreFactorySpi instances limited to file formats

For access to coverage (ie raster) information:

* GridFormatFinder - access to GridFormatFactorySpi supporting raster formats
* CoverageFactoryFinder - access to GridCoverageFactory 

JTSFactoryFinder - used to create JTS GeometryFactory and PercisionModel

* GeometryFactory
* PrecisionModel

ReferencingFactoryFinder - used to list referencing factories

* DatumFactory
* CSFactory
* DatumAuthorityFactory
* CSAuthorityFactory
* CRSAuthorityFactory
* MathTransformFactory
* CoordinateOperationFactory
* CoordinateOperationAuthorityFactory

Where to get a Factory
----------------------

It really depends on your application, depending on your environment you may locate a factory by either:

* Using a GeoTools "FactoryFinder". Most factory finders are provided by the main module. They will hunt down an implementation on the CLASSPATH for you to use.
* Use of "Container" - you may find an implementation provided as part of your application container (especially for a Java EE application). You can take this approach in normal applications with a container implementation like Spring, or PicoContainer
* Use of "JNDI" - your application may also store an implementation in JNDI (this approach is often used to locate a DataSource in a JEE application)
* Direct use of a known factory. You can always create a new Factory yourself and make use of it to create interfaces.
* Direct use of an implementation. You may decide to duck the factory game completely and make use of a specific implementation using new.

These examples will usually use a factory finder of some sort. For the details please review the How to Find a Factory page.

FactoryFinder
^^^^^^^^^^^^^

While the use of Factories has become common place (especially in development environments like Spring). GeoTools has its own "FactoryFinder" classes, unique to project, which is how the library looks up what plugins are available for use.

These facilities are also available for use in your own application.

FactoryFinder uses the "built-in" Java plug-in system known as Factory Service Provide Interface. This technique allows a jar to indicate what services it makes available (in this case implementations of a factory interface). 

To make this easier to use we have a series of utility classes called "FactoryFinders". These classes work as a match maker - looking around at what is available on the CLASSPATH. They will perform the "search" and locate a the implementation you need.

Here is an example::
   
   FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory( null );

About FactorySPI
''''''''''''''''

The "FactorySPI" system is the out of the box plug in system that ships with Java. That is why we like it - we are sure you already are using the Java software after all. The SPI part is pronounced "spy" and stands for Service, Provider, Interface.

The FactorySPI system has a look on your CLASSPATH and locates implementations of a requested service scattered around all the jars you have. It does this by looking in the jar MANIFEST folder in a services directory.

Factory SPI is a runtime plugin system; so your application can "discover" and use new abilities that GeoTools provides over time. As our shapefile support gets better and better your application will notice and make use of the best implementation for the job.

If you are curious you can make use of the FactorySPI system yourself to locate anything we got going on in GeoTools::
   
   Hints hints = GeoTools.getDefaultHints();
   FactoryRegistry registry = new FactoryCreator(Arrays.asList(new Class[] {FilterFactory.class,}));
   Iterator i = registry.getServiceProviders( FilterFactory.class, null, hints );
   while( i.hasNext() ){
       FilterFactory factory = (FilterFactory) i.next();
   }

Notes:

* keep you FactoryRegistery around, hold it in a static field or global lookup service such as JNDI.
* The registry usually creates one instance (the first time you ask) and will return it to you again next time
* Specifically it will create you one instance per configuration (ie that Hints object), so if you ask again using the same hints you will get the same instance back

Think of FactoryRegistry keeping instances as singletons for you.  In the same manner as it is a Java best practice (when making a singleton) to "partition" by ThreadGroup (so different applets use different singletons). FactoryRegistry does not follow this practice - it uses Hints to "partition" - so two applets that are configured the same will end up using the same FilterFactory.

Application specific Alternatives
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^

Here are a couple of alternatives for stitching together your application.

Container
'''''''''

A container is a great way to take care of a lot of the boiler plate
code involved in working with factories. Much of this documentation
will use PicoContainer (just because it is small), while many real
world applications use the Spring container.

A container is basically a Map where you can look up instances.
In common use the instances are factories, and what makes a container
valuable is its ability automate the process of "wiring up" the
factories together.

Popular techniques:

* reflection - picocontainer looks the constructors using reflection to see if any of the required parameters are available
* configuration - Spring uses a big xml file marking how each factory is created

The other nice thing is the container can put off creating the
factories until you actually ask for them.::
  
  container.registerImplementationClass( PositionFactory.class, PositionFactoryImpl.class );
  container.registerImplementationClass( CoordinateFactory.class, CoordinateFactoryImpl.class );
  container.registerImplementationClass( PrimitiveFactory.class, PrimitiveFactoryImpl.class );
  container.registerImplementationClass( ComplexFactory.class, ComplexFactoryImpl.class );
  container.registerImplementationClass( AggregateFactory.class AggregateFactoryImpl.class );
  
  container.registerInstance( CoordianteReferenceSystem.class, CRS.decode("EPSG:4326") );
  
  WKTParser parser = (WKTParser) container.newInstance( WKTParser.class );

In the above example the WKTParser needs to be constructed with a PositionFactory, CoordinateFactory, PrimitiveFactory and ComplexFactory. Each one of these factories can only be constructed for a specific CoordinateReferenceSystem.

If we were not using a container to manage our factories it would of taken three times the number of lines of code just to set up a WKTParser.

JNDI
''''

If you are writing a Java EE Application there is a big global map in the sky called "InitialContext". Literally this is a map you can do look up by name and find Java instances in. It is so global in fact that the instances will be shared between applications.

This idea of a global cross application map is great for configuration and common services. If you are working with a Java EE application you will often find such things as:

* a CRSAuthorityFactory registered for any code wanting to use the referencing module
* a database listed under the Name "jdbc/EPSG" used to hold the EPSG tables
* a GeometryFactory, or FeatureTypeFactory and so on ...

Here is the GeoTools code that looks up a DataSource for an EPSG authority::
  
  Context context = JNDI.getInitialContext(null);
  DataSource source = (DataSource) context.lookup("jdbc/EPSG");

The JNDI interfaces are shipped with Java; and two implementations are provided (one to talk to LDAP directories such as organisations deploy for email address information, and another for configuration information stored on the file system with your JRE).

The difference between JNDI and a Container:

* JNDI is not a container - it is an interface that ships with Java that
  lets you ask things of a "directory service".
  
  A Java EE Application Server runs programs in a "container" and part
  of the "container configuration" is making sure that JNDI is set up
  and pointing to the Services (ie global variables) that the
  Application Server makes available to all applications.
  
  This same directory service can be used by you to share global
  variables between applications. Some things like the CRSAuthority
  can be treated as a "utility" and it makes sense to only have one
  of them for use from several applications at once.

Because making use of an application container is a good idea, and too hard to set up. There are a lot of alternative "light weight" containers available. Examples include pico container, JBoss container, Spring container and many many more. These containers focus on the storing of global variables (and making a lot of the difficult configuration automatic - like what factory needs to be created first).

Direct use of Factory
^^^^^^^^^^^^^^^^^^^^^

Sometimes you just need to go ahead and code it like you mean it. The GeoTools plugin system does have its place and purpose; but if you know exactly what you are doing; or want to test an exact situation you can dodge the plugin system and do the work by hand.

You can just use a specific factory that is known to you::
  
  DataStoreFactorySpi factory = new IndexedShapefileDataStoreFactory();
  
  File file = new File("example.shp");
  Map map = Collections.singletonMap( "url", file.toURL() );

  DataStore dataStore = factory.createDataStore( map );

You are depending on a specific class here (so it is not a real plug-in based solution in which GeoTools can find you the best implementation for the job). There is a good chance however that the factory will set you up with a pretty good implementation.

* Factory classes are Public in Name Only
  
  Factory classes are only public because we have to (so the factory
  finders can call them) - some programming environments such as OSGi
  will take special care to prevent you making direct use of these
  classes.
  
  If you are working on the uDig project you may find that class loader
  settings have prevented you from directly referring to one of these
  factory classes.

You can provide a "hint" asking the Factory Finder to retrieve you a specific instance::
  
  Hints hints = new Hints( Hints.FILTER_FACTORY, "org.geotools.filter.StrictFactory" );
  FilterFactory filterFactory = CommonFactoryFinder.getFilterFactory( hints );

You can skip the whole Factory madness and just do normal Java coding::
  
  File file = new File("example.shp");
  URI namespace = new URI("refractions");
  boolean useMemoryMapped = true;
  ShapefileDataStore shapefile = new ShapefileDataStore( example.toURL(), namespace, useMemoryMapped );

You are depending on a exact class here, violating the plug-in system and so on. Chances are that GeoTools should not let you do this (by making the constructor package visible and forcing you to use the associated DataStoreFactory instead).

This option is fine for quick hacks, you may find that the ShapefileDataStore has additional methods (to handle such things as forcing the "prj" file to be rewritten.::
  
  shapefile.forceSchemaCRS( CRS.decode( "EPSG:4326" ) );
