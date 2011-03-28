How to Create
=============

Code leveraging GeoTools usually works against the Java interfaces only but interfaces in Java don't provide any way to create actual objects. GeoTools therefore provides Factories which are concrete implementations through whose interface users can create actual GeoTools objects such as Features, Styles, Filters, DataStores, and MathTransforms.

This page explains how to use the FactoryFinder system to find the appropriate Factory implementations to instantiate particular objects. The next page will show alternative approaches to obtain and use a particular implementation of an appropriate DataStore interface; those examples show the utility of the FactoryFinder system.

Creating in GeoTools
--------------------

To create an implementation (and not get your hands dirty by depending on a specific class) Java developers are asked to use a Factory. Other languages like scala allow the definition of a constructors as part of the interface itself.

in GeoTools we use a "FactoryFinder" to look for a factory implementation on the classpath.

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

Finding a FactoryFinder
-----------------------

There is a loose naming convention where we try and have a clear progression from interface name, factory name to factory finder name.

However in practice we found it useful to gather many of the common
factories together into a common class for lookup.

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
