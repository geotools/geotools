DataStore
---------

A **DataStore** is used to access and store geospatial data in a range of vector formats
including shapefiles, GML files, databases, Web Feature Servers, and other formats.

References:

* :doc:`gt-api <../api/datastore>`

Create
^^^^^^

It is not recommended to create a DataStore by hand; instead we make use of a FactoryFinder which will locate
the correct plugin supporting the requested format.

We have three factory finders to choose from:

* **DataAccessFinder** is used to acquire a DataAccess
  
  DataAccessFinder will find out both those DataStores found by
  DataStoreFinder and the ones that only implement DataAccess but not
  DataStore.

* **DataStoreFinder** is used to acquire a DataStore

* **FileDataStoreFinder** is limited to working with FileDataStoreFactorySpi where a clear file extension is available

Depending on if we are connecting to existing content, or asking for a new file to be created we will handle things a little
differently.

* Access
  
  We are just going to focus on the most common case right now - getting access to an existing Shapefile.
  Rest assured that what you learn here will work just fine when talking to a real database like PostGIS or a web service like WFS.
  
  To create our shapefile we are going to use the DataStoreFinder utility class. Here is what that looks like::
    
    File file = new File("example.shp");
    Map map = new HashMap();
    map.put( "url", file.toURL() );
    DataStore dataStore = DataStoreFinder.getDataStore(map );

* Create
  
  To create a new shapefile on disk we are going to have to go one level deeper
  and ask FileDataStoreFinder for a factory matching the "shp" extension.::
  
    FileDataStoreFactorySpi factory = FileDataStoreFinder.getDataStoreFactory("shp");
    
    File file = new File("my.shp");
    Map map = Collections.singletonMap( "url", file.toURL() );
    
    DataStore myData = factory.createNewDataStore( map );
    FeatureType featureType = DataUtilities.createType( "my", "geom:Point,name:String,age:Integer,description:String" );
    
    myData.createSchema( featureType );

* Factory
  
  We can repeat the example of creating a new shapefile, just using DataStoreFinder to list
  the available implementations and then see which one is willing to create shapefile. 
  
  This time you need to do the work by hand.::
    
    File file = new File("my.shp");
    Map map = new HashMap();
    map.put( "url", file.toURL());
    
    for( Iterator i=DataStoreFinder.getAvailableDataStores(); i.hasNext(); ){
        DataStoreFactorySpi factory = (DataStoreFactorySpi) i.next();
        
        try {
            if (factory.canProcess(params)) {
                return fac.createNewDataStore(params);
            }
        }
        catch( Throwable warning ){
            System.err( factory.getDisplayName() + " failed:"+warning );
        }
    }
  
  As you see, the logic merely returns a DataStore from the first factory
  that can create one for us.

These examples bring up a couple of questions:

* Q: Why use a DataStoreFinder
  
  We are using a FactoryFinder (rather than just saying new
  ShapefileDataStore ) so GeoTools can have a look at your specific
  fiel choose the right implementation for the job.
  
  Currently GeoTools has two implementations:
  
  * ShapefileDataStore
    
    Provided by **ShapefileDataStoreFactory** for direct access to a
    shapefile, suitable for shapefiles located on network shares and web
    services
  
  * IndexedShapefileDataStore
    
    Provided by **IndexedShapefileDataStoreFactory** to makes use of
    (or create) a spatial index for fast access

* Q: What do we put it the Map?
  
   That's a hard question which forces us to read the documentation:
   
   * :doc: `shape` (user guide)
   * `ShapefileDataStoreFactory <http://docs.geotools.org/stable/javadocs/org/geotools/data/shapefile/ShapefileDataStoreFactory.html>`_ (javadocs)
   * `IndexedShapefileDataStoreFactory <http://docs.geotools.org/stable/javadocs/org/geotools/data/shapefile/indexed/IndexedShapefileDataStoreFactory.html>`_ (javadocs)
   
   This information is also available at runtime via the
   **DataStoreFactorySpi,getParameterInfo()** method. You can use this
   information to create a user interface in a dynamic application.

Catalog
'''''''

If you are working with GeoServer or uDig you have access to some great facilities for defining your DataStore before you create it. Think of it as a "just in time" or "lazy" DataStore.::

  Catalog catalog = new DefaultCatalog();
  ServiceFinder finder = new DefaultServiceFactory( catalog );
  
  File file = new File("example.shp");
  Service service = finder.aquire( file.toURI() );
  
  // Getting information about the Shapefile (BEFORE making the DataStore)
  IServiceInfo info = service.getInfo( new NullProgressListener() );
  String name = info.getName();
  String title = info.getTitle().toString();
  
  // Making the DataStore
  DataStore dataStore = service.resolve( DataStore.class, new NullProgressListener() );

The idea works similar to a "file handle", you can make a IService "handle" that represents your DataStore (and you can ask the handle several fun questions like "what is your name") before you actually create the beast.

This separation is really important in an application expecting to talk about thousands of sources of data at a time. Just because your application wants to know about a source of data does not always mean you need a DataStore yet.

A Catalog works with two important bits of information:

* URI - is the unique name of the data
* Map - is used to create a DataStore just in time using DataStoreFactoryFinder

The nice thing is that for many easy cases the catalog is smart enough to figure out the Map just from the URI.

Careful
^^^^^^^

Don't Duplicate
'''''''''''''''

DataStore's represent a live connection to your file or database:

* Don't create and throw away DataStores, or make Duplicates
* DataStores are BIG heavy-weight objects - many of them juggle database connection or load up spatial indexes on your behalf.
* Please keep your DataStore around for reuse
  
  * Manage them as a Singleton
  * Manage them in a Registry
  * Manage them in an application specific Catalog
  
  For more details please :doc:`../main/repository`

Direct Access
'''''''''''''
You can also dodge the FactoryFinder and make use of the following quick hacks.

This is not wise (as the implementation may change over time) but here is how it is done.

* Use new ShapefileDataStore::
    
    File file = new File("example.shp");
    URI namespace = new URI("refractions");
    boolean useMemoryMapped = true;
    DataStore shapefile = new ShapefileDataStore( example.toURL(), namespace, useMemoryMapped );
    
    String typeName = shapefile.getTypeName(); // should be "example"
    FeatureType schema = shapefile.getSchema( typeName ); // should be "refractions.example"
    
    FeatureSource contents = shapefile.getFeatureSource( typeName );
    int count = contents.getCount( Query.ALL );
    System.out.println( "Connected to "+file+ " with " + count );
  
  This hack may be fine for a quick code example, but in a real
  application can we ask you to use the DataStoreFactoryFinder. It will
  let the library sort out what implementation is appropriate.

* Use new IndexedShapefileDataStore::
    
    File file = new File("example.shp");
    URI namespace = new URI("refractions");
    boolean memoryMapped = true;
    boolean createIndex = true;
    byte treeType = IndexedShapefileDataStore.TREE_QIX;
    
    DataStore shapefile = new IndexedShapefileDataStore( example.toURL(), namespace, memoryMapped, createIndex, treeType );
    
    String typeName = shapefile.getTypeName(); // should be "example"
    ...
  
  This hack may be fine for a quick code example, but in a real
  application can we ask you to use the DataStoreFactoryFinder. It will
  let the library sort out what implementation is appropriate.

* Use IndexedShapefileDataStoreFactory::
    
    FileDataStoreFactorySpi factory = new IndexedShapefileDataStoreFactory();
    
    File file = new File("example.shp");
    Map map = Collections.singletonMap( "url", file.toURL() );
    
    DataStore dataStore = factory.createDataStore( map );
  
  This hack is a little bit harder to avoid - since you do want to use
  the factory directly in some cases (ie when creating a brand new file
  on disk). If possible ask the DataStoreFactoryFinder for all available
  factories (so you can make use of what is available at runtime).
