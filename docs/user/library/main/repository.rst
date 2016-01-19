Repository
----------

Applications based on Geotools must keep track of the geographic data accessed by the application. It is important not to create and throw away DataStores at runtime; as they often contain JDBC connections or open file handles.

If there is only one or are only a few sources of data, the application can easily keep track directly the data being handled. However, geographic information systems often handle large volumes of data and it can become complex to keep track of all the different sources of data. GeoTools has developed several approaches to track multiple sources of data but not all of these have matured to be fully functional.

The critical issue is that the GeoTools DataStore class is design:

* to be thread safe (so you can access data from multiple threads)
* not be duplicated (so a single DataStore talks to a single server or file)

If you have two instances of PostGISDataStore both talking to the same database there is a
chance for them to trip over each other - and BAD THINGS TO HAPPEN. A more extreme example
would be losing the contents of your Shapefile as to ShapefileDataStore attempt to update
it at the same time.

In the simplest approach, applications based on GeoTools keep track explicitly of the different DataStores which they are using and make sure they only connect to each source of data with one and only one DataStore class. They may choose to protect themselves from BAD THINGS by using the 'Singleton' design pattern.

An alternative approach for applications using more than one or a few DataStores, is to create a map structure with 'singleton' code to both ensure this singleton access to the data and easily grab the right DataStore when it is needed. The Map relates some identifier for the data store (the ID) with the DataStore class accessing that resource. URLs are often used for the ID.

This second approach is so common that GeoTools developed the **Repository** interface along with
two implementations to handle this situation.

A more sophisticated approach "Catalog" approached is used by GeoServer and uDig in order to track
large numbers of data sources (and lazily create DataStores as needed).

Singleton
^^^^^^^^^

A simple application may track its use of DataStores on its own. Ideally, it will create a
singleton structure around each DataStore to ensure that it only accesses the DataStore once.::
  
  class MyApp {
     public static DataStore store;
     public synchronized static DataStore getMyData(){
       if( store == null ){
            try {
                store = connect();
            }
            catch( IOException eek ){
                System.err.println("Could not connect to data store - exiting");
                eek.printStackTrace();
                System.exit(1); // die die die
            }
       }
       return store;
     }
     ....
  }

Map
^^^

When you start working with lots of data you end up storing the list of data sources in a Map to which you add the singleton protection code.::
  
  class MyApp {
     public static Map<String,DataStore> data;
     public synchronized static DataStore getData(String id) throws IOException {
       DataStore store = data.get( id );
       if( store == null ){
            store = connect( id );
            data.put( id, store );            
       }
       return store;
     }
     ....
  }

Usually you make up the ID based on:

* the URL of your data (for files and webservices)
* the jdbc url if you are using a database

Repository
^^^^^^^^^^

The use of a Map like that describe above happens so often that we have a special GeoTools interface and two implementations which you can use - so GeoTools code can look up and make use of your applications data.::
  
  interface Repository {
    Map getDataStores();
    SortedMap getFeatureSources();
    Set getPrefixes();
    boolean lockExists(String lockId)
    boolean lockRefresh(String lockId, Transaction)
    boolean lockRelease(String lockId, Transaction)
    source source(String dataStoreId, String typeName ) 
  }

In addition if you are working inside GeoServer or uDig they offer up access to their internal Catalog using this interface (so you can find DataStores as needed).

To look up individual FeatureSources we use a "typeRef" - basically "dataStoreId:typeName".

This interface is used by the gt-validation module in order to look up information for integrity tests.
Often tests make use of several dataStores (in order to make sure roads are on land for example).

There are two implementations:

* DefaultRepository - quick implementation - works fine.
* FeatureSourceRepository - quick implementation used to organise FeatureSources

DefaultRepository Example::
  
  class MyApp {
     public static DefaultRepository data;
     ....
     public void static main(String args[]){
        for( String file: args){
             File file = new File( args );
             if( file.exists ){
                  data.load( file );
             }
        }
        ...
     }
  }

A couple additional features are available to assist in managing information.

* Registery.load( file )
  
  Load up a DataStores from individual property files. The property files should have the
  information needed to connect to your DataStore implementation.
  
  file1.properties::
    
    url=file:./myshape.shp
  
  file2.properties::
    
    url=http://localhost/geoserver/wfs?SERVICE=WFS+REQUEST=GETCAPABILITIES+VERSION=1.0


Catalog
^^^^^^^

Two major applications (GeoServer and uDig) both use the idea of a Catalog to store "database"
of all the data they are working with (both DataStores and GridCoverages) and then "connect"
to the data only when needed.

You may have thousands of entries in your catalog (all the GIS data on your computer?) and only
be using 10 of them for your current map. This is the "lazy access" for which catalog was
created.

The other Catalog offers is the ability to manage WebMapServer, DataStores and Rasters in a similar
manner (rather than just DataStores).

Both GeoServer and and uDig offer some form of Catalog API. Here is an example of using the uDig catalog::
  
  Catalog catalog = new DefaultCatalog();
  ServiceFinder finder = new DefaultServiceFactory( catalog );
  
  WFSService service = finder.aquire( uri ); // uri of your GetCapabilities document
  
  IServiceInfo info = service.getInfo( new NullProgressListener() );
  
  String name = info.getName();
  String title = info.getTitle().toString();
  
  DataStore dataStore = service.resolve( DataStore.class, new NullProgressListener() );

This interface is set up for real world applications, progress listeners are used to report on progress
to a user interface while still giving the end user the ability to cancel what may be a long running operation.

References:

* http://geoserver.org/display/GEOS/Configuration+Proposal
* http://help.eclipse.org/helios/topic/org.eclipse.platform.doc.isv/resInt_filesystem.htm
* http://udig.github.io/docs/dev/catalog.html
